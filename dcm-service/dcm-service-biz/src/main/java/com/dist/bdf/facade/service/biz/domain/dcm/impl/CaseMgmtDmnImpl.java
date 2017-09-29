
package com.dist.bdf.facade.service.biz.domain.dcm.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.facade.service.biz.domain.dcm.CaseMgmtDmn;
import com.dist.bdf.manager.ecm.utils.CaseUtil;
import com.dist.bdf.model.dto.dcm.CaseDTO;
import com.dist.bdf.model.dto.dcm.CaseTypeDTO;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
import com.dist.bdf.model.dto.system.WzInfoDTO;
import com.filenet.api.core.Document;
import com.filenet.api.core.ObjectStore;
import com.ibm.casemgmt.api.DeployedSolution;

/**
 * @author weifj
 * @version 1.0，2016/03/21，weifj，创建案例管理领域
 */
@Service
public class CaseMgmtDmnImpl implements CaseMgmtDmn {

	private Logger logger = LoggerFactory.getLogger(CaseMgmtDmnImpl.class);

	@Autowired
	private CaseUtil caseUtil;
	
	
	@Override
	public List<CaseTypeDTO> getCaseTypes(DeployedSolution solution) {

		//connectionService.initialize();
		return caseUtil.getCaseTypes(solution);
	}

	@Override
	public List<CaseDTO> getCasesByType(ObjectStore os, String typePrefix) {

		return this.caseUtil.getCasesByType(os, typePrefix);
		
		
	}
	
	@Override
	public WzInfoDTO getWzInfoDTO(ObjectStore os, String wzId) {
		
		return this.caseUtil.getWzInfoDTO(os, wzId);
		
	}
	@Override
	public Document getWz(ObjectStore os, String wzId) {
		
		return this.caseUtil.getWz(os, wzId);
		
	}
	
	@Override
	public Pagination getWZOfCase(ObjectStore os, String caseIdentifier, int pageNo, int pageSize) {

		return this.caseUtil.getWZsOfCase(os, caseIdentifier, pageNo, pageSize);
	
	}
	@Override
	public CaseDTO getCaseByIdentifier(ObjectStore os, String caseIdentifier) {
		
		return this.caseUtil.getCaseByIdentifier(os, caseIdentifier);

	}
	@Override
	public Object getCaseById(ObjectStore os, String caseId) {
		
		return this.caseUtil.getCaseById(os, caseId);
	}
	@Override
	public List<DocumentDTO> getCasePackages(ObjectStore os, String caseIdentifier) {

		return this.caseUtil.getCasePackages(os, caseIdentifier);
		
	}

}
