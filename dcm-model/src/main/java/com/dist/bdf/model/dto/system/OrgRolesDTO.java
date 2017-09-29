package com.dist.bdf.model.dto.system;

import java.io.Serializable;
import java.util.List;

import com.dist.bdf.model.entity.system.DcmOrganization;
import com.dist.bdf.model.entity.system.DcmRole;
/**
 * 
 * @author weifj
 * @version 1.0，2016/05/12，weifj，创建机构和角色
 *
 */

public class OrgRolesDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DcmOrganization org;
    /**
     * 角色
     */
    private List<DcmRole> roles;
  
	public List<DcmRole> getRoles() {
		return roles;
	}
	public void setRoles(List<DcmRole> roles) {
		this.roles = roles;
	}
	public DcmOrganization getOrg() {
		return org;
	}
	public void setOrg(DcmOrganization org) {
		this.org = org;
	}
}
