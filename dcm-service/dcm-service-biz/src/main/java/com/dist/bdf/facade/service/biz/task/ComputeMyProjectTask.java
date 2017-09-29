package com.dist.bdf.facade.service.biz.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;
import com.dist.bdf.common.conf.ecm.ECMConf;
import com.dist.bdf.common.conf.ecm.ExtPropertyCaseConf;
import com.dist.bdf.manager.ecm.define.DataType;
import com.dist.bdf.manager.ecm.define.SimplePropertyFilter;
import com.dist.bdf.manager.ecm.security.ConnectionService;
import com.dist.bdf.manager.ecm.utils.CaseUtil;
import com.dist.bdf.model.dto.dcm.CaseDTO;
import com.dist.bdf.model.entity.system.DcmGroup;
import com.ibm.ecm.util.p8.P8Connection;

/**
 * 计算属于当前用户管辖范围内的项目列表 规则： 1）企业决策管理者：企业内部所有项目 2）部门决策管理者：主责部门的项目
 * 
 * @author weifj
 *
 */
public class ComputeMyProjectTask extends RecursiveTask<Map<String, CaseDTO>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int SEQUENTIAL_THRESHOLD = 20;
	private List<DcmGroup> projects;
	private CaseUtil caseUtil;
	private ConnectionService connService;
	private ECMConf ecmConf;
	private ExtPropertyCaseConf extPropCaseConf;
	private final int start;
	private final int end;
	private String realm;
	private String[] businessTypes;

	/*public ComputeMyProjectTask(String realm, List<DcmGroup> projects, ConnectionService connService, CaseUtil caseUtil, ECMConf ecmConf,
			int start, int end) {

		this.realm = realm;
		this.projects = projects;
		this.connService = connService;
		this.caseUtil = caseUtil;
		this.ecmConf = ecmConf;
		this.start = start;
		this.end = end;
	}*/
	
	public ComputeMyProjectTask(String realm, List<DcmGroup> projects, ConnectionService connService, CaseUtil caseUtil,
			ECMConf ecmConf, ExtPropertyCaseConf extPropCaseConf, String[] businessTypes, 
			int start, int end) {

		this.realm = realm;
		this.projects = projects;
		this.connService = connService;
		this.caseUtil = caseUtil;
		this.ecmConf = ecmConf;
		this.businessTypes = businessTypes;
		this.extPropCaseConf = extPropCaseConf;
		this.start = start;
		this.end = end;
	}

	@Override
	protected Map<String, CaseDTO> compute() {
		final int length = end - start;
		if (length < SEQUENTIAL_THRESHOLD) {
			return computeDirectlyEx();
			//return computeDirectly();
		}
		Map<String, CaseDTO> allMap = new HashMap<String, CaseDTO>();
		final int split = length / 2;
		final ComputeMyProjectTask left = new ComputeMyProjectTask(this.realm, this.projects, this.connService,
				this.caseUtil, this.ecmConf, this.extPropCaseConf, this.businessTypes, this.start, this.start + split);
		left.fork();
		final ComputeMyProjectTask right = new ComputeMyProjectTask(this.realm, this.projects, this.connService,
				this.caseUtil, this.ecmConf, this.extPropCaseConf, this.businessTypes, this.start + split, this.end);
		allMap.putAll(right.compute()); // 右边任务继续拆分
		allMap.putAll(left.join());
		return allMap;
	}

	@Deprecated
	private Map<String, CaseDTO> computeDirectly() {

		Map<String, CaseDTO> cases = new HashMap<String, CaseDTO>();
		List<String> caseIds = new ArrayList<>();
		for (int i = start; i < end; i++) {
			DcmGroup g = this.projects.get(i);
			caseIds.add(g.getGuid());
		}
		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(this.realm));
		List<CaseDTO> tempCaseDTOs =  this.caseUtil.getCasesById(p8conn.getObjectStore(), caseIds);
		if(tempCaseDTOs != null && !tempCaseDTOs.isEmpty()) {
			for(CaseDTO caseDTO : tempCaseDTOs) {
				if(!cases.containsKey(caseDTO.getGuid())) {
					cases.put(caseDTO.getGuid(), caseDTO);
				}
			}
			// cases = tempCaseDTOs.stream().collect(DistCollectors.toMap(CaseDTO::getGuid, (p)->p));
		}
		return cases;
	}
	/**
	 * 真正的业务逻辑处理
	 * @return
	 */
	private Map<String, CaseDTO> computeDirectlyEx() {

		Map<String, CaseDTO> cases = new HashMap<String, CaseDTO>();
		List<String> caseIds = new ArrayList<>();
		for (int i = start; i < end; i++) {
			DcmGroup g = this.projects.get(i);
			caseIds.add(g.getGuid());
		}
		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(this.realm));
		List<SimplePropertyFilter> filters = new ArrayList<SimplePropertyFilter>();
		if(this.businessTypes != null && this.businessTypes.length>0) {
			filters.add(new SimplePropertyFilter(this.extPropCaseConf.getBusinessType(), DataType.ListOfString, this.businessTypes));
		}
		if(!caseIds.isEmpty()) {
			filters.add(new SimplePropertyFilter("Id", DataType.SingleOfString, caseIds.toArray(new String[caseIds.size()])));
		}
	/*	if(this.projectManagers != null && this.projectManagers.length > 0) {
			SimplePropertyFilter projectManagerFilter = new SimplePropertyFilter();
			projectManagerFilter.setPropertyName(this.extPropCaseConf.getProjectLeader());
			projectManagerFilter.setValues(this.projectManagers);
			projectManagerFilter.setPropertyType("String");
			filters.add(projectManagerFilter);
		}
		if(this.projectAssistants != null && this.projectAssistants.length > 0) {
			SimplePropertyFilter projectAssistantFilter = new SimplePropertyFilter();
			projectAssistantFilter.setPropertyName(this.extPropCaseConf.getProjectAssistant());
			projectAssistantFilter.setValues(this.projectAssistants);
			projectAssistantFilter.setPropertyType("String");
			filters.add(projectAssistantFilter);
		}
*/
		List<CaseDTO> tempCaseDTOs =  this.caseUtil.getCasesByFilter(p8conn.getObjectStore(), filters);
		if(tempCaseDTOs != null && !tempCaseDTOs.isEmpty()) {
			for(CaseDTO caseDTO : tempCaseDTOs) {
				if(!cases.containsKey(caseDTO.getGuid())) {
					cases.put(caseDTO.getGuid(), caseDTO);
				}
			}
			// cases = tempCaseDTOs.stream().collect(DistCollectors.toMap(CaseDTO::getGuid, (p)->p));
		}
		return cases;
	}
}
