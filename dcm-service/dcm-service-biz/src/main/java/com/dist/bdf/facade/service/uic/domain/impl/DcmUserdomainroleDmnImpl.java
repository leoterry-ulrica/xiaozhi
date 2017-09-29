
package com.dist.bdf.facade.service.uic.domain.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.dao.hibernate.GenericDAOImpl;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.facade.service.uic.domain.DcmRoleDmn;
import com.dist.bdf.facade.service.uic.domain.DcmUserdomainroleDmn;
import com.dist.bdf.model.entity.system.DcmRole;
import com.dist.bdf.model.entity.system.DcmUser;
import com.dist.bdf.model.entity.system.DcmUserdomainrole;

/**
 * @author weifj
 * @version 1.0，2016/02/24，weifj，创建用户-域-角色领域
 */
@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class DcmUserdomainroleDmnImpl extends GenericDmnImpl<DcmUserdomainrole, Long> implements DcmUserdomainroleDmn {

	@Autowired
	//private DcmUserdomainroleDAO userDomainRoleDao;
	private GenericDAOImpl<DcmUserdomainrole, Long> userDomainRoleDao;
	@Autowired
	private DcmRoleDmn roleDmn;
	
	@Override
	public GenericDAO<DcmUserdomainrole, Long> getDao() {
		// TODO Auto-generated method stub
		return userDomainRoleDao;
	}

	@Override
	public List<DcmUserdomainrole> getByUserIdDomainCode(Long userId, String domainCode) {
		// TODO Auto-generated method stub
		List<DcmUserdomainrole> udrs = this.getDao().findByProperties(new String[]{"userId","domainCode"}, new Object[]{userId, domainCode});
		
		return udrs;
	}

	@Override
	public List<DcmUserdomainrole> getByUserIdDomainType(Long userId, String domainType, String roleCode) {
		
        List<DcmUserdomainrole> udrs = this.getDao().findByProperties(new String[]{"userId","domainType", "roleCode"}, new Object[]{userId, domainType, roleCode});
		
		return udrs;
	}
	@Override
	public List<DcmUserdomainrole> getByUserIdDomainType(Long userId, String[] domainTypes) {
		
		Map<String, Object[]> propertiesValuesMap = new HashMap<String, Object[]>();
		propertiesValuesMap.put("userId", new Long[]{userId});
		propertiesValuesMap.put("domainType", domainTypes);
		return this.getDao().findByProperties(propertiesValuesMap);
	}
	@Override
	public List<DcmUserdomainrole> getByUserCodeDomainType(String userCode, String[] domainTypes) {
		Map<String, Object[]> propertiesValuesMap = new HashMap<String, Object[]>();
		propertiesValuesMap.put("userCode", new String[]{userCode});
		propertiesValuesMap.put("domainType", domainTypes);
		return this.getDao().findByProperties(propertiesValuesMap);
	}
	@Override
	public List<DcmUserdomainrole> getByUserIdDomainCodeRoleCode(Long userId, String domainCode, String[] roleCode) {
		
		Map<String, Object[]> propertiesValuesMap = new HashMap<String, Object[]>();
		propertiesValuesMap.put("userId", new Object[]{userId});
		propertiesValuesMap.put("domainCode", new Object[]{domainCode});
		propertiesValuesMap.put("roleCode", roleCode);
		
		return this.getDao().findByProperties(propertiesValuesMap);
		
	}
	@Override
	public DcmUserdomainrole addUserToDomain(Long userId, String domainType, String domainCode, String roleCode) {
		
		DcmUserdomainrole findUdr = super.findUniqueByProperties(new String[]{"userId","domainCode","roleCode"}, new Object[]{userId,domainCode,roleCode});
		if(findUdr != null) {
			return findUdr;
		}
		
		DcmUserdomainrole udr = new DcmUserdomainrole();
		udr.setDomainCode(domainCode);
		udr.setDomainType(domainType);
		udr.setRoleCode(roleCode);
		udr.setUserId(userId);
		udr.setCreateTime(new Date());
	    return super.add(udr);
	}

	@Override
	public void addUserToDomain(Long userId, String userCode, int userType, String domainType, String domainCode, String roleCode) {
		
		DcmUserdomainrole findUdr = super.findUniqueByProperties(new String[]{"userId","domainCode","roleCode"}, new Object[]{userId,domainCode,roleCode});
		if(findUdr != null) {
			findUdr.setUserCode(userCode);
			findUdr.setUserType(userType);
			 super.modify(findUdr);
			 return ;
		}
		
		DcmUserdomainrole udr = new DcmUserdomainrole();
		udr.setDomainCode(domainCode);
		udr.setDomainType(domainType);
		udr.setRoleCode(roleCode);
		udr.setUserId(userId);
		udr.setCreateTime(new Date());
		udr.setUserCode(userCode);
		udr.setUserType(userType);
	    super.add(udr);
	    
	}
	@Override
	public void removeUserFromDomain(Long userId, String groupCode) {
		
		this.getDao().removeByProperties(new String[]{"userId","domainCode"}, new Object[]{userId, groupCode});
	
	}
	
	@Override
	public List<DcmUserdomainrole> getByDomainCode(String domainCode){
		
		return this.getDao().findByProperty("domainCode", domainCode);
	}
	@Override
	public List<DcmUserdomainrole> getByDomainCodeRoleCode(String domainCode, String roleCode){
		
		return this.getDao().findByProperties(new String[]{"domainCode","roleCode"}, new Object[]{domainCode, roleCode});
	}
	@Override
	public DcmUserdomainrole getByDomainCodeUserCodeRoleCode(String domainCode, String userCode, String roleCode) {
		return this.getDao().findUniqueByProperties(new String[]{"domainCode","userCode", "roleCode"}, new Object[]{domainCode, userCode, roleCode});
	}
	@Override
	public String getDomainTypeByDomainCode(String code) {
		
		DcmUserdomainrole udr = this.getDao().findUniqueByProperty("domainCode", code);
		if(null == udr){
			throw new BusinessException("没有找到空间域["+code+"] 对应的空间域类型");
			
		}
		return udr.getDomainType();
	}
	@Override
	public List<DcmRole> getRolesOfUser(Long userId, String orgCode) {

		List<DcmUserdomainrole> udrs = this.getDao().findByProperties(new String[]{"userId","domainCode"}, new Object[]{userId, orgCode});
		if(null == udrs || 0 == udrs.size()) return null;
		
		List<DcmRole> roles = new ArrayList<DcmRole>();
		for(DcmUserdomainrole udr : udrs){
			roles.add(this.roleDmn.getRoleByCode(udr.getRoleCode()));
		}
		return roles;
	}
	@Override
	public void deleteByDomainCode(String domainCode) {
		
		this.getDao().removeByProperty("domainCode", domainCode);
		
	}
	@Override
	public List<DcmUserdomainrole> getProjectGroupsByUserId(Long id) {
		
		return this.getDao().findByProperties(new String[]{"userId","domainType"}, new Object[]{id, "Domain_Project"}, "createTime", false);
	}
	@Override
	public List<DcmUserdomainrole> getByUserIdDomainType(Long userId, String domainType) {
		
		 List<DcmUserdomainrole> udrs = this.getDao().findByProperties(new String[]{"userId","domainType"}, new Object[]{userId, domainType});
			
		return udrs;
	}
	@Override
	public DcmUserdomainrole addUserToDomain(DcmUser user, String domainType, String domainCode, String roleCode) {
		
		DcmUserdomainrole findUdr = super.findUniqueByProperties(new String[]{"userCode","domainCode","roleCode"}, new Object[]{user.getUserCode(),domainCode,roleCode});
		if(findUdr != null) {
			findUdr.setUserCode(user.getUserCode());
			findUdr.setUserType(0);
			super.modify(findUdr);
			return findUdr;
		}
		
		DcmUserdomainrole udr = new DcmUserdomainrole();
		udr.setDomainCode(domainCode);
		udr.setDomainType(domainType);
		udr.setRoleCode(roleCode);
		udr.setUserId(user.getId());
		udr.setUserCode(user.getUserCode());
		udr.setUserType(0);
		udr.setCreateTime(new Date());
		udr.setLastTime(new Date());
	    super.add(udr);
	    
	    return udr;
	}
	@Override
	public void removeUserFromDomain(String userCode, String groupCode) {
		this.getDao().removeByProperties(new String[]{"userCode","domainCode"}, new Object[]{userCode, groupCode});
	}
	@Override
	public List<DcmUserdomainrole> getByUsercodeRolecode(String userCode, String roleCode) {
		
		 List<DcmUserdomainrole> udrs = this.getDao().findByProperties(new String[]{"userCode","roleCode"}, new Object[]{userCode, roleCode});
		return udrs;
	}
	@Override
	public List<DcmRole> getRolesOfUser(String userCode, String domainCode) {

		List<DcmUserdomainrole> udrs = this.getDao().findByProperties(new String[]{"userCode","domainCode"}, new Object[]{userCode, domainCode});
		if(null == udrs || 0 == udrs.size()) return null;
		
		List<DcmRole> roles = new ArrayList<DcmRole>();
		for(DcmUserdomainrole udr : udrs){
			roles.add(this.roleDmn.getRoleByCode(udr.getRoleCode()));
		}
		return roles;
	}
	@Override
	public List<DcmUserdomainrole> getByUserIdRolecode(Long userId, String roleCode) {
		
		 return this.getDao().findByProperties(new String[]{"userId","roleCode"}, new Object[]{userId, roleCode});

	}
	@Override
	public List<DcmUserdomainrole> getByDomainCodeUserCode(String caseId, String userCode) {
		
		return this.getDao().findByProperties(new String[]{"domainCode","userCode"}, new String[]{caseId, userCode});
	}
}
