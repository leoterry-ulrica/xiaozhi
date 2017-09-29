package com.dist.bdf.model.entity.system;

import java.lang.Long;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

/**
 * 组织机构
 */
@Entity
@Table(name = "DCM_ORGANIZATION")
public class DcmOrganization  extends BaseEntity {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long parentId;
	private String domainType;
	private String orgName;
	private Long orderId;
	private String orgCode;
	private String alias;
	private String dn;
	/**
	 * 域
	 */
	private String realm;
	/**
	 * 机构类型，如：中方、外方
	 */
	private String orgType;

	// Constructors

	// Property accessors

	@Column(name = "alias", length = 50)
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Column(name = "PARENTID", length = 36)
	public Long getParentId() {
		return this.parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Column(name = "DOMAINTYPE", length = 50)
	public String getDomainType() {
		return this.domainType;
	}

	public void setDomainType(String domainType) {
		this.domainType = domainType;
	}

	@Column(name = "ORGNAME", length = 100)
	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	@Column(name = "ORDERID", precision = 22, scale = 0)
	public Long getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	@Column(name = "ORGCODE", length = 50)
	public String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	@Column(name = "DN")
	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
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

	@Override
	public String toString() {
		return "DcmOrganization [parentId=" + parentId + ", domainType=" + domainType + ", orgName=" + orgName
				+ ", orderId=" + orderId + ", orgCode=" + orgCode + ", alias=" + alias + ", dn=" + dn + ", realm="
				+ realm + ", orgType=" + orgType + "]";
	}
}