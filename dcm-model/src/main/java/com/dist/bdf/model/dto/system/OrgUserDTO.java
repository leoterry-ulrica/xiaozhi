
package com.dist.bdf.model.dto.system;

import org.hibernate.validator.constraints.NotEmpty;

import com.dist.bdf.base.dto.BaseDTO;

/**
 * 机构和用户关联的DTO
 * @author weifj
 * @version 1.0，2016/01/29，weifj，创建机构和用户关联的DTO
 * @version 1.1,2016/02/26，weifj，修改属性类型
 *
 */
public class OrgUserDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户id数组
	 */
	@NotEmpty( message = "userIds can not be empty")
	private Long[] userIds;
	/**
	 * 机构id
	 */
	private Long orgId;
	/**
	 * 机构名称
	 *//*
	private String orgName;*/
	/**
	 * @return the userIdArray
	 */
	/*public Long[] getUserIdArray() {
		return userIdArray;
	}
	*//**
	 * @param userIdArray the userIdArray to set
	 *//*
	public void setUserIdArray(Long[] userIdArray) {
		this.userIdArray = userIdArray;
	}*/
	/**
	 * @return the orgId
	 */
	public Long getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return the orgName
	 */
/*	public String getOrgName() {
		return orgName;
	}
	*//**
	 * @param orgName the orgName to set
	 *//*
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}*/
	public Long[] getUserIds() {
		return userIds;
	}
	public void setUserIds(Long[] userIds) {
		this.userIds = userIds;
	}

}
