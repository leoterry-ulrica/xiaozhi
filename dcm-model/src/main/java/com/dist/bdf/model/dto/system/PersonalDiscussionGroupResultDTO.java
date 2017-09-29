package com.dist.bdf.model.dto.system;

import java.io.Serializable;

/**
 * 个人讨论组结果集
 * @author weifj
 * @version 1.0，,2016/04/14，weifj，创建
 */
public class PersonalDiscussionGroupResultDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String groupName;
	private String groupCode;
	private String joinTime;
	private String roleCode;
	private String creator;
	private String createTime;
	
	/**
	 * 讨论组名称
	 * @return
	 */
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	/**
	 * 讨论组编码
	 * @return
	 */
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	/**
	 * 加入时间
	 * @return
	 */
	public String getJoinTime() {
		return joinTime;
	}
	public void setJoinTime(String joinTime) {
		this.joinTime = joinTime;
	}
	/**
	 * 角色编码
	 * @return
	 */
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	/**
	 * 创建者
	 * @return
	 */
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * 创建时间
	 * @return
	 */
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}
