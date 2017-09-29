package com.dist.bdf.facade.service.biz.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.facade.service.biz.priv.DocPrivService;
import com.dist.bdf.common.constants.ResourceConstants;

@Component
public class DocPrivFactory {

	@Autowired
	@Qualifier(value = "PersonDocPrivService")
	private DocPrivService personDoc;
	
	@Autowired
	@Qualifier(value = "ProjectDocPrivService")
	private DocPrivService projectDoc;
	
	@Autowired
	@Qualifier(value = "DepartmentDocPrivService")
	private DocPrivService departmentDoc;
	
	@Autowired
	@Qualifier(value = "InstituteDocPrivService")
	private DocPrivService instituteDoc;
	
	public DocPrivService getPrivService(String resourceType) {
		
		DocPrivService instance = null;
		
		switch (resourceType) {
		case ResourceConstants.ResourceType.RES_PCK_PERSON:
			instance = this.personDoc;
			break;
		case ResourceConstants.ResourceType.RES_PCK_PROJECT:
			instance = this.projectDoc;
			break;
		case ResourceConstants.ResourceType.RES_PCK_DEPARTMENT:
			instance = this.departmentDoc;
			break;
		case ResourceConstants.ResourceType.RES_PCK_INSTITUTE:
			instance = this.instituteDoc;
			break;
		default:
			throw new BusinessException("没有找到资源类型 [{0}] 对应的权限服务对象。", resourceType);
		}
		
		return instance;
	}
}
