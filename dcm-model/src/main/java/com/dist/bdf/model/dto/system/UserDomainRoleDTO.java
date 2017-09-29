
package com.dist.bdf.model.dto.system;

import java.util.Map;

import com.dist.bdf.base.dto.BaseDTO;


/**
 * @author weifj
 * @version 1.0，2016/02/24，weifj，创建DTO
 *
 */
public class UserDomainRoleDTO extends BaseDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long userId;
	private Map<String, Integer> users;
	private String domainCode;
	private String domainType;
	private String roleCode;
	//private String[] roleCodes;
	@Deprecated
	private Long[] userIds;
	
	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * @return the domainCode
	 */
	public String getDomainCode() {
		return domainCode;
	}
	/**
	 * @param domainCode the domainCode to set
	 */
	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}
	/**
	 * @return the domainType
	 */
	public String getDomainType() {
		return domainType;
	}
	/**
	 * @param domainType the domainType to set
	 */
	public void setDomainType(String domainType) {
		this.domainType = domainType;
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
	/**
	 * @return the userIds
	 */
	public Long[] getUserIds() {
		return userIds;
	}
	/**
	 * @param userIds the userIds to set
	 */
	public void setUserIds(Long[] userIds) {
		this.userIds = userIds;
	}
	/**
	 * key：用户编码，userCode
	 * value：用户类型，0：内部；1：外部
	 * @return
	 */
	public Map<String, Integer> getUsers() {
		return users;
	}
	public void setUsers(Map<String, Integer> users) {
		this.users = users;
	}
}
