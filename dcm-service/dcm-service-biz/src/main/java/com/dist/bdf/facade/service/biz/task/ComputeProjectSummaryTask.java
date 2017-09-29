package com.dist.bdf.facade.service.biz.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dist.bdf.facade.service.uic.domain.DcmUserdomainroleDmn;
import com.dist.bdf.model.dto.system.ProjectSummaryDTO;
import com.dist.bdf.model.entity.system.DcmGroup;
import com.dist.bdf.model.entity.system.DcmUserdomainrole;
/**
 * 项目汇总
 * @author weifj
 *
 */
public class ComputeProjectSummaryTask extends RecursiveTask<List<ProjectSummaryDTO>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(ComputeProjectSummaryTask.class);
	private static final int SEQUENTIAL_THRESHOLD = 10;
	private List<DcmGroup> projects;
	private DcmUserdomainroleDmn userDomainRoleDmn;
	private int start;
	private int end;

	public ComputeProjectSummaryTask(List<DcmGroup> projects, int start, int end, DcmUserdomainroleDmn userDomainRoleDmn) {
		this.projects = projects;
		this.start = start;
		this.end = end;
		this.userDomainRoleDmn = userDomainRoleDmn;
	}
	@Override
	protected List<ProjectSummaryDTO> compute() {

		int length = end - start;
		if (length < SEQUENTIAL_THRESHOLD) {
			return computeDirectly();
		}
		List<ProjectSummaryDTO> projectSummaryDTOs = new ArrayList<ProjectSummaryDTO>();
		final int split = length / 2;
		final ComputeProjectSummaryTask left = new ComputeProjectSummaryTask(this.projects, this.start, this.start + split, this.userDomainRoleDmn);
		left.fork();
		final ComputeProjectSummaryTask right = new ComputeProjectSummaryTask(this.projects, this.start + split, this.end, this.userDomainRoleDmn);
		projectSummaryDTOs.addAll(right.compute()); // 右边任务继续拆分
		projectSummaryDTOs.addAll(left.join());
		return projectSummaryDTOs;
	}

	private List<ProjectSummaryDTO> computeDirectly() {

		List<ProjectSummaryDTO> subProjectSummary = new ArrayList<ProjectSummaryDTO>();
		if (null == this.projects || this.projects.isEmpty()) {
			return subProjectSummary;
		}
		Date dateStart = new Date();
		for (int i = start; i < end; i++) {
			ProjectSummaryDTO dto = new ProjectSummaryDTO();
			DcmGroup project = this.projects.get(i);
			dto.setCaseCode(project.getGroupCode());
			dto.setCaseId(project.getGuid());
			dto.setName(project.getGroupName());
			List<DcmUserdomainrole> udrs = this.userDomainRoleDmn.getByDomainCode(project.getGuid());
			dto.setMemberCount((null == udrs | udrs.isEmpty()) ? 0 : udrs.size());
			subProjectSummary.add(dto);
		}
		Date dateEnd = new Date();
		logger.info(">>>任务消耗时间：[{}] ms", (dateEnd.getTime() - dateStart.getTime()));
		return subProjectSummary;
	}

}
