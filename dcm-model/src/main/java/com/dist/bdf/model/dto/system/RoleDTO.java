
package com.dist.bdf.model.dto.system;

import com.dist.bdf.base.dto.BaseDTO;

/**
 * @author weifj
 * @version 1.0，2016/02/24，weifj，创建角色DTO
 */
public class RoleDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 角色编码
	 */
	private String roleCode;
	/**
	 * 角色名称
	 */
	private String roleName;
	/**
	 * @return the roleCode
	 */
	public String getRoleCode() {
		return roleCode;
	}
	/**
	 * @param roleCode the roleCode to set
	 */
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}
	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
