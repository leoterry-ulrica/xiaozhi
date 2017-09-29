package com.dist.bdf.model.dto.system.user;

import java.io.Serializable;

public class UserOrgRolesAddDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long orgId;
	private Long[] roleIds;
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Long[] getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(Long[] roleIds) {
		this.roleIds = roleIds;
	}
}
