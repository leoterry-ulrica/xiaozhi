
package com.dist.bdf.facade.service.biz.domain.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.facade.service.biz.dao.DcmPrivilegeDAO;
import com.dist.bdf.facade.service.biz.domain.system.DcmPrivilegeDmn;
import com.dist.bdf.manager.ecm.security.PrivilegeFactory;
import com.dist.bdf.model.entity.system.DcmPrivilege;


/**
 * @author weifj
 * @version 1.0，2016/03/03，weifj，创建权限领域实现体
 *
 */
@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class DcmPrivilegeDmnImpl  extends GenericDmnImpl<DcmPrivilege, Long>  implements DcmPrivilegeDmn {

	@Autowired
	private DcmPrivilegeDAO privDao;
	//private GenericDAOImpl<DcmPrivilege, Long> privDao;
	
	@Override
	public GenericDAO<DcmPrivilege, Long> getDao() {
		
		return privDao;
	}

	@Override
	public List<DcmPrivilege> getAllPrivs() {
		
		return this.getDao().find();
	}

	@Override
	public DcmPrivilege getPrivByCode(String code) {

		return super.findUniqueByProperty("privCode", code);
	}

	@Override
	public DcmPrivilege getPrivByName(String name) {
		return super.findUniqueByProperty("privName", name);
	}

	@Override
	public DcmPrivilege getPrivByValue(String value) {
		return super.findUniqueByProperty("privValue", value);
	}

	@Override
	public Long getBasicPrivs(Long masks) {
		
		return PrivilegeFactory.BASICS & masks;
	}
	
	@Override
	public Long getExtendedPrivs(Long masks) { 
		
		return PrivilegeFactory.EXTENDS & masks;
	}
	
	@Override
	public List<String> getExtendedPrivCodes(Long validMasks) {
		
		Long extendedMasks = this.getExtendedPrivs(validMasks);
		
		return PrivilegeFactory.getCodes(extendedMasks);
	}

}
