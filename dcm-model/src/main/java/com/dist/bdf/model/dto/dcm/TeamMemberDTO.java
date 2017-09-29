package com.dist.bdf.model.dto.dcm;

import com.dist.bdf.base.dto.BaseDTO;

/**
 * 团队成员（包括用户和用户组）
 * @author Administrator
 *
 */
public class TeamMemberDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String type;
	private String[] roleNames;
	
	/**
	 * 成员名称（用户名称或者组名称）
	 * @return
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 类型，值：USER/GROUP
	 * @return
	 */
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 角色名称数组
	 * @return
	 */
	public String[] getRoleNames() {
		return roleNames;
	}
	public void setRoleNames(String[] roleNames) {
		this.roleNames = roleNames;
	}
}
