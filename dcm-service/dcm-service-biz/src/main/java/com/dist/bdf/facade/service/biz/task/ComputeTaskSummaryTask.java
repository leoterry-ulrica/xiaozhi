package com.dist.bdf.facade.service.biz.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dist.bdf.common.conf.ecm.ECMConf;
import com.dist.bdf.common.constants.DomainType;
import com.dist.bdf.common.constants.RoleConstants;
import com.dist.bdf.facade.service.uic.domain.DcmUserdomainroleDmn;
import com.dist.bdf.manager.ecm.security.ConnectionService;
import com.dist.bdf.manager.ecm.utils.CaseUtil;
import com.dist.bdf.model.dto.dcm.CaseDTO;
import com.dist.bdf.model.dto.system.TaskAndProjectRespDTO;
import com.dist.bdf.model.dto.system.WzInfoDTO;
import com.dist.bdf.model.entity.system.DcmTask;
import com.dist.bdf.model.entity.system.DcmUserdomainrole;
import com.google.common.base.Objects;
import com.ibm.ecm.util.p8.P8Connection;
/**
 * 任务汇总计算
 * @author weifj
 *
 */
public class ComputeTaskSummaryTask extends RecursiveTask<List<TaskAndProjectRespDTO>>{

	private static Logger logger = LoggerFactory.getLogger(ComputeTaskSummaryTask.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int SEQUENTIAL_THRESHOLD = 20;
	private List<DcmTask> tasks;
	private Map<String, DcmTask> taskMap;
	private String[] taskIdArray;
	private Map<String, CaseDTO> projectDTOMap;
	private ConnectionService connService;
	private ECMConf ecmConf;
	private int start;
	private int end;
	private String realm;
	private CaseUtil caseUtil;
	// private DcmSocialResourceDmn dcmSocialResDmn;
	private Map<String, String> orgCodeNameMap;
	private Map<String, String> wzTaskId2CreatorMap;
	private DcmUserdomainroleDmn udrDmn;
	/**
	 * case对应的项目负责人map
	 */
	private static Map<String, Set<String>> Case2prjManagerMap = new HashMap<String, Set<String>>();
	/**
	 * case对应的项目助理map
	 */
	private static Map<String, Set<String>> Case2prjAssistantMap = new HashMap<String, Set<String>>();
	/**
	 * 存储已经查询过的案例id集合
	 */
	private static List<String> RecordQueryCaseIds = new ArrayList<String>();
	/**
	 * 原子计数器
	 */
	private static AtomicInteger AtomicInteger = new AtomicInteger(0);  
	
	public ComputeTaskSummaryTask(String realm, ConnectionService connService, CaseUtil caseUtil, ECMConf ecmConf, 
			int start, int end, Map<String, CaseDTO> projectDTOMap, List<DcmTask> tasks, Map<String, String> orgCodeNameMap, Map<String, String> wzTaskId2CreatorMap,DcmUserdomainroleDmn udrDmn) {
		
		this.tasks = tasks;
		if(start == end -1){
			logger.info(">>>tasks只有一个元素");
			this.taskMap = new HashMap<String, DcmTask>();
			this.taskMap.put(this.tasks.get(start).getTaskId(), this.tasks.get(start));
		}else {
			logger.info(">>>tasks有超过一个的元素");
			// 注意获取子集合的开始索引和结束索引（fromIndex < 0 || toIndex > size || fromIndex > toIndex）
			List<DcmTask> subTasks = this.tasks.subList(start, end);
			this.taskMap = new HashMap<String, DcmTask>();
			for(DcmTask t : subTasks) {
				if(!this.taskMap.containsKey(t.getTaskId())) {
					this.taskMap.put(t.getTaskId(), t);
				}
			}
			// this.taskMap = .stream().collect(DistCollectors.toMap(DcmTask::getTaskId, (t)->t));
		}
		
		String[] tempArray = new String[this.taskMap.size()];
		this.taskIdArray = this.taskMap.keySet().toArray(tempArray);
		this.projectDTOMap = projectDTOMap;
		this.connService = connService;
		this.ecmConf = ecmConf;
		// this.dcmSocialResDmn = dcmSocialResDmn;
		this.start = start;
		this.end = end;
		this.realm = realm;
		this.caseUtil = caseUtil;
		this.orgCodeNameMap = orgCodeNameMap;
		this.wzTaskId2CreatorMap = wzTaskId2CreatorMap;
		this.udrDmn = udrDmn;
	}
	@Override
	protected List<TaskAndProjectRespDTO> compute() {
		
	    int length = end - start;
		if (length < SEQUENTIAL_THRESHOLD) {
			return computeDirectly();
		}
		List<TaskAndProjectRespDTO> taskPrjDTOs = new ArrayList<TaskAndProjectRespDTO>();
		final int split = length / 2;
		final ComputeTaskSummaryTask left = new ComputeTaskSummaryTask(this.realm, this.connService,
				this.caseUtil, this.ecmConf, this.start, this.start + split, this.projectDTOMap, this.tasks, this.orgCodeNameMap, this.wzTaskId2CreatorMap, this.udrDmn);
		left.fork();
		final ComputeTaskSummaryTask right = new ComputeTaskSummaryTask(this.realm, this.connService,
				this.caseUtil, this.ecmConf, this.start + split, this.end, this.projectDTOMap, this.tasks, this.orgCodeNameMap, this.wzTaskId2CreatorMap, this.udrDmn);
		taskPrjDTOs.addAll(right.compute()); // 右边任务继续拆分
		taskPrjDTOs.addAll(left.join());
		
		return taskPrjDTOs;
	}

	private List<TaskAndProjectRespDTO> computeDirectly() {

		List<TaskAndProjectRespDTO> taskPrjDTOs = new ArrayList<TaskAndProjectRespDTO>();
		if(null == this.taskIdArray || 0 == this.taskIdArray.length) {
			return taskPrjDTOs;
		}
		TaskAndProjectRespDTO tempTaskPrj = null;
		WzInfoDTO wzInfo = null;
		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(this.realm));
		List<WzInfoDTO> wzInfoDTOs = this.caseUtil.getWzInfosDTO(p8conn.getObjectStore(), this.taskIdArray, false);
		
		Map<String, WzInfoDTO> wzInfoMap = new HashMap<String, WzInfoDTO>();
		for(WzInfoDTO wz : wzInfoDTOs) {
			if(!wzInfoMap.containsKey(wz.getGuid())) {
				wzInfoMap.put(wz.getGuid(), wz);
			}
		}
		// wzInfoDTOs.stream().collect(DistCollectors.toMap(WzInfoDTO::getGuid, (w)->w));
		// List<DcmSocialResource> socials = this.dcmSocialResDmn.findByProperty("resId", this.taskIdArray);
		// Map<String, DcmSocialResource> wzSocailMap = new HashMap<String, DcmSocialResource>();
		/*if(socials != null && !socials.isEmpty()){
			wzSocailMap = socials.stream().collect(DistCollectors.toMap(DcmSocialResource::getResId, (s)->s));
		}*/
		// DcmSocialResource socialRes = null;
		for (int i = start; i < end; i++) {
			DcmTask subtask = this.tasks.get(i);

			tempTaskPrj = new TaskAndProjectRespDTO();
			wzInfo = wzInfoMap.get(subtask.getTaskId());
			if (wzInfo != null) {
				tempTaskPrj.setAssociatePerson(wzInfo.getAssociatePersons());
				Document contentHtml = Jsoup.parse(wzInfo.getContent());
				tempTaskPrj.setContent(null == contentHtml? "" : contentHtml.text());
			}
			tempTaskPrj.setIndex(AtomicInteger.incrementAndGet());
			tempTaskPrj.setAudience(subtask.getAudience());
			tempTaskPrj.setBeginTime(subtask.getBeginTime());
			tempTaskPrj.setBusinessType(this.projectDTOMap.get(subtask.getCaseIdentifier()).getBusinessType());
			tempTaskPrj.setCustomerFeedback(subtask.getCustomerFeedback());
			tempTaskPrj.setEndTime(subtask.getEndTime());
			tempTaskPrj.setExpectPeople(subtask.getExpectPeople());
			tempTaskPrj.setFocus(subtask.getFocus());
			tempTaskPrj.setHardwareReq(subtask.getHardwareReq());
			tempTaskPrj.setHeadFeedback(subtask.getHeadFeedback());
			tempTaskPrj.setLocalSupport(subtask.getLocalSupport());
			tempTaskPrj.setSolutionTemp(subtask.getSolutionTemp());
			tempTaskPrj.setTarget(subtask.getTarget());
			tempTaskPrj.setTaskId(subtask.getTaskId());
			tempTaskPrj.setType(subtask.getType());
			tempTaskPrj.setResponseTime(subtask.getResponseTime());
			tempTaskPrj.setCaseId(subtask.getCaseIdentifier());
			tempTaskPrj.setCreateTime(subtask.getDateCreated());
			tempTaskPrj.setEvaluation(subtask.getEvaluation());
			
			if(!RecordQueryCaseIds.contains(subtask.getCaseIdentifier()) && !Case2prjManagerMap.containsKey(subtask.getCaseIdentifier())) {
				Map<String, Object[]> properties = new HashMap<String, Object[]>();
				properties.put("domainType", new String[]{DomainType.PROJECT});
				properties.put("domainCode", new String[]{subtask.getCaseIdentifier()});
				List<DcmUserdomainrole> udrs = this.udrDmn.findByProperties(properties);
				if(udrs != null && !udrs.isEmpty()) {
					// 项目负责人
					Set<String> managers = new HashSet<String>();
					// 项目助理
					Set<String> assistants = new HashSet<String>();
					RecordQueryCaseIds.add(subtask.getCaseIdentifier());
					for(DcmUserdomainrole udr : udrs) {
						if(Objects.equal(RoleConstants.RoleCode.R_Project_Manager, udr.getRoleCode())) {
							managers.add(udr.getUserCode());
						}
						if(Objects.equal(RoleConstants.RoleCode.R_Project_Assistant, udr.getRoleCode())) {
							assistants.add(udr.getUserCode());
						}
					}
					Case2prjManagerMap.put(subtask.getCaseIdentifier(), managers);
					Case2prjAssistantMap.put(subtask.getCaseIdentifier(), assistants);
				} else {
					Case2prjManagerMap.put(subtask.getCaseIdentifier(), new HashSet<String>(0));
					Case2prjAssistantMap.put(subtask.getCaseIdentifier(), new HashSet<String>(0));
				}
			}
			Set<String> tempManagers = Case2prjManagerMap.get(subtask.getCaseIdentifier());
			Set<String> tempAssistants = Case2prjAssistantMap.get(subtask.getCaseIdentifier());
			tempTaskPrj.setProjectManager(tempManagers.toArray(new String[tempManagers.size()]));
			tempTaskPrj.setProjectAssistant(tempAssistants.toArray(new String[tempAssistants.size()]));
			tempTaskPrj.setProjectName(this.projectDTOMap.get(subtask.getCaseIdentifier()).getProjectName());
			tempTaskPrj.setDelegateUnit(this.projectDTOMap.get(subtask.getCaseIdentifier()).getDelegateUnit());
			tempTaskPrj.setRegion(this.projectDTOMap.get(subtask.getCaseIdentifier()).getProjectRegion());
			tempTaskPrj.setProvince(this.projectDTOMap.get(subtask.getCaseIdentifier()).getProvince());
			tempTaskPrj.setCity(this.projectDTOMap.get(subtask.getCaseIdentifier()).getCity());
			tempTaskPrj.setCounty(this.projectDTOMap.get(subtask.getCaseIdentifier()).getCounty());
			// 有顺序，主责部门+主要配合部门+其它配合部门
			tempTaskPrj.setOrgMaster(this.orgCodeNameMap.get(this.projectDTOMap.get(subtask.getCaseIdentifier()).getOrg()));
			tempTaskPrj.setOrgCoo(this.orgCodeNameMap.get(this.projectDTOMap.get(subtask.getCaseIdentifier()).getOrgCoo()));
			tempTaskPrj.setOrgOtherCoo(this.orgCodeNameMap.get(this.projectDTOMap.get(subtask.getCaseIdentifier()).getOrgOtherCoo()));
			// TODO 需要修改成真正的发布者
			tempTaskPrj.setPublisher(this.wzTaskId2CreatorMap.get(subtask.getTaskId()));
			tempTaskPrj.setStatus(subtask.getStatus());
			tempTaskPrj.setPublishTime(subtask.getDateCreated());
		/*	socialRes = wzSocailMap.get(subtask.getTaskId());
			if(socialRes != null) {
				tempTaskPrj.setStatus(socialRes.getTag());
			}else {
				// 默认：待接受
				tempTaskPrj.setStatus("585d68");
			}*/
			taskPrjDTOs.add(tempTaskPrj);
		}
		return taskPrjDTOs;
	}
	/**
	 * 清理所有静态数据
	 */
	public void clear() {
		
		Case2prjManagerMap.clear();
		Case2prjAssistantMap.clear();
		RecordQueryCaseIds.clear();
	}
}
