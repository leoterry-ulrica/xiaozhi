package com.dist.bdf.model.entity.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

/**
 * 权限模板表
 * @version 1.0，2016/03/11，weifj，修改restypestatus的数据类型，String->Long
 */
@Entity
@Table(name = "DCM_PRIVTEMPLATE")
public class DcmPrivtemplate  extends BaseEntity {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String resTypeCode;
	private Long resTypeStatus;
	private String roleCode;
	private String privCode;
	private String realm;
	private Integer scope;

	// Constructors

	// Property accessors
	
	@Column(name = "RESTYPECODE", length = 50)
	public String getResTypeCode() {
		return this.resTypeCode;
	}

	public void setResTypeCode(String resTypeCode) {
		this.resTypeCode = resTypeCode;
	}

	@Column(name = "RESTYPESTATUS")
	public Long getResTypeStatus() {
		return this.resTypeStatus;
	}

	public void setResTypeStatus(Long resTypeStatus) {
		this.resTypeStatus = resTypeStatus;
	}

	@Column(name = "ROLECODE", length = 50)
	public String getRoleCode() {
		return this.roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	@Column(name = "PRIVCODE", length = 50)
	public String getPrivCode() {
		return this.privCode;
	}

	public void setPrivCode(String privCode) {
		this.privCode = privCode;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public Integer getScope() {
		return scope;
	}

	public void setScope(Integer scope) {
		this.scope = scope;
	}

	@Override
	public String toString() {
		return "DcmPrivtemplate [resTypeCode=" + resTypeCode + ", resTypeStatus=" + resTypeStatus + ", roleCode="
				+ roleCode + ", privCode=" + privCode + ", realm=" + realm + ", scope=" + scope + "]";
	}

}