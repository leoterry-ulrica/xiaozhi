package com.dist.bdf.model.dto.system.user;

import java.io.Serializable;
import java.util.Map;
/**
 * 用户-机构-角色的更新DTO
 * @author weifj
 *
 */
public class UserOrgRolesUpdateDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long orgId;
	private Map<Long, Integer> roleIdMode;
	// private Long[] roleIds;
	
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	/*public Long[] getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(Long[] roleIds) {
		this.roleIds = roleIds;
	}*/
	/**
	 * key：角色的id
	 * value：是否删除和更新，0：删除；1：添加
	 * @return
	 */
	public Map<Long, Integer> getRoleIdMode() {
		return roleIdMode;
	}
	public void setRoleIdMode(Map<Long, Integer> roleIdMode) {
		this.roleIdMode = roleIdMode;
	}
}
