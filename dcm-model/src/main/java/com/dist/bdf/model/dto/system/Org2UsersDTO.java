
package com.dist.bdf.model.dto.system;

import java.util.List;

import com.dist.bdf.base.dto.BaseDTO;
import com.dist.bdf.model.entity.system.DcmUser;


/**
 * @author weifj
 * @version 1.0，2016/03/18，weifj，创建机构与用户关联关系的DTO
 */
public class Org2UsersDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long parentId;
	private String orgName;
	private String orgCode;
	private Long orderId;
	private String orgType;
	/**
	 * 机构别名
	 */
	private String alias;
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	private List<DcmUser> users;
	
	/**
	 * @return the parentId
	 */
	public Long getParentId() {
		return parentId;
	}
	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}
	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	/**
	 * @return the orgCode
	 */
	public String getOrgCode() {
		return orgCode;
	}
	/**
	 * @param orgCode the orgCode to set
	 */
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	/**
	 * @return the orderId
	 */
	public Long getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
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
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
}
