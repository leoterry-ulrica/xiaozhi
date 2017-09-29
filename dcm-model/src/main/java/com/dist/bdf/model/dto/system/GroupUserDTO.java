
package com.dist.bdf.model.dto.system;

import java.util.List;

import com.dist.bdf.base.dto.BaseDTO;
import com.dist.bdf.model.entity.system.DcmUser;


/**
 * @author weifj
 * @version 1.0，2016/02/24，weifj，创建组和用户关联dto
 *
 */
public class GroupUserDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户组
	 */
	private List<DcmUser> users;
	/**
	 * 角色名称
	 */
	private String roleName;
	/**
	 * 用户对用的角色代码
	 */
	private String roleCode;
	/**
	 * @return the users
	 */
	public List<DcmUser> getUsers() {
		return users;
	}
	/**
	 * @param users the users to set
	 */
	public void setUsers(List<DcmUser> users) {
		this.users = users;
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
	
	
}
