
package com.dist.bdf.facade.service.biz.domain.dcm;

import java.util.List;

import com.dist.bdf.model.dto.dcm.CaseDTO;
import com.dist.bdf.model.dto.dcm.CaseTypeDTO;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
import com.filenet.api.core.ObjectStore;
import com.ibm.casemgmt.api.DeployedSolution;

/**
 * @author weifj
 * @version 1.0，2016/03/19，weifj，创建ecm的case领域层
 *
 */
public interface CaseMgmtDmn {

	//public void createMicroJob(String folderId, String microJobName, String content);
	/**
	 * 获取案例类型
	 * @return
	 */
	public List<CaseTypeDTO> getCaseTypes(DeployedSolution solution);
	/**
	 * 根据类型获取案例实例
	 * @param typePrefix 案例类型前缀
	 * @return
	 */
	public List<CaseDTO> getCasesByType(ObjectStore os, String typePrefix);
	/**
	 * 获取case下的所有微作
	 * @param caseIdentifier 案例标识
	 * @return
	 */
	public Object getWZOfCase(ObjectStore os, String caseIdentifier, int pageNo, int pageSize);
	/**
	 * 根据案例标识获取案例实体
	 * @param caseIdentifier
	 * @return
	 */
	public Object getCaseByIdentifier(ObjectStore os, String caseIdentifier);
	/**
	 * 根据案例id获取案例实体
	 * @param os
	 * @param caseId
	 * @return
	 */
	public Object getCaseById(ObjectStore os, String caseId);
	/**
	 * 获取案例包
	 * @param caseIdentifier 案例标识
	 * @return
	 */
	List<DocumentDTO> getCasePackages(ObjectStore os, String caseIdentifier);
	/**
	 * 根据微作id获取微作DTO
	 * @param os
	 * @param wzId
	 * @return
	 */
	Object getWzInfoDTO(ObjectStore os, String wzId);
	/**
	 * 根据微作id获取微作实体
	 * @param os
	 * @param wzId
	 * @return
	 */
	Object getWz(ObjectStore os, String wzId);
}
