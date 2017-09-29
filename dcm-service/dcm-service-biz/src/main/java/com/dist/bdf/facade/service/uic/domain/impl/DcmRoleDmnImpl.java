
package com.dist.bdf.facade.service.uic.domain.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.facade.service.uic.dao.DcmRoleDAO;
import com.dist.bdf.facade.service.uic.domain.DcmRoleDmn;
import com.dist.bdf.common.constants.RoleConstants;
import com.dist.bdf.model.entity.system.DcmRole;

/**
 * @author weifj
 * @version 1.0，2016/02/24，weifj，创建角色领域
 */
@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class DcmRoleDmnImpl extends GenericDmnImpl<DcmRole, Long> implements DcmRoleDmn {

	@Resource
	private DcmRoleDAO roleDao;
	//private GenericDAOImpl<DcmRole, Long> roleDao;
	
	@Override
	public GenericDAO<DcmRole, Long> getDao() {
		
		return roleDao;
	}

	@Override
	public List<DcmRole> getAllRole() {
		return super.find();
	}

	@Override
	public List<DcmRole> getRolesOfProject(){
		
		return super.findByProperty("roleType", RoleConstants.RoleType.T_Project, "sortId", true);
		//return super.find("from "+DcmRole.class.getSimpleName()+" where roleCode like 'R_Project_%'", new Object[]{});
	}
	@Override
	public DcmRole getRoleById(Long id) {
		
		return super.findById(id);
	}

	@Override
	public DcmRole getRoleByCode(String code) {
		
		return this.getDao().findUniqueByProperty("roleCode", code);
	}

	@Override
	public DcmRole getRoleByName(String name) {

		return this.getDao().findUniqueByProperty("roleName", name);
	}

	@Override
	public DcmRole addRole(String code, String name) {
		
		DcmRole role = new DcmRole();
		role.setRoleCode(code);
		role.setRoleName(name);
		
		role = super.add(role);

		return role;
	}

	@Override
	public DcmRole updateRoleById(Long id, String code, String name) {
		
		DcmRole role = new DcmRole();
		role.setId(id);
		role.setRoleCode(code);
		role.setRoleName(name);

		return this.getDao().update(role);
	}

	@Override
	public void deleteRoleById(Long id) {
		
		super.removeById(id);
	}

	@Override
	public void deleteRoleByCode(String code) {
		
		super.removeByProperty("rolecode", code);
	}

	@Override
	public void deleteRoleByName(String name) {
		
		super.removeByProperty("rolename", name);
	}

}
