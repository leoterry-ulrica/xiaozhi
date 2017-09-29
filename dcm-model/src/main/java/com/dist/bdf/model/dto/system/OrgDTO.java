
package com.dist.bdf.model.dto.system;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.dist.bdf.base.dto.BaseDTO;
/**
 * 组织机构dto
 * @author weifj
 * @version 1.0，2016/1/20，创建机构dto
 */
public abstract class OrgDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 机构名称
	 */
	@NotEmpty( message = "orgName can not be empty")
	@NotNull( message = "orgName can not be Null")
	protected String orgName;
	/**
	 * 别名
	 */
	protected String alias;
	/**
	 * 父节点id
	 */
	protected Long parentId;
	/**
	 * 排序号
	 */
	protected Long orderId;
	/**
	 * 机构域
	 */
	@NotEmpty( message = "realm can not be empty")
	@NotNull( message = "realm can not be Null")
	protected String realm;
	
	protected String orgType;
	
	/**
	 * @return the orgname
	 */
	public String getOrgName() {
		return orgName;
	}
	/**
	 * @param orgname the orgname to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/**
	 * @return 机构代码
	 */
	public String getOrgCode() {
		return UUID.randomUUID().toString().toUpperCase();
	}
	/**
	 * 获取空间域类型
	 * @return
	 */
	public abstract String getDomaintype();
	/**
	 * 获取空间域名称
	 * @return
	 */
	public abstract String getDomainname();
	/**
	 * 获取组类型（主要用于存入ldap）
	 * @return
	 */
	public abstract String getGroupType();
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
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
}
