package com.dist.bdf.model.entity.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

/**
 * 角色表
 */
@Entity
@Table(name = "DCM_ROLE")
public class DcmRole extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields
	private String roleCode;
	private String roleName;
	private Long roleType;
	private String comments;
	private String realm;
	private Integer sortId;

	// Constructors

	@Column(name = "ROLECODE", length = 50)
	public String getRoleCode() {
		return this.roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	@Column(name = "ROLENAME", length = 50)
	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name = "ROLETYPE")
	public Long getRoleType() {
		return roleType;
	}

	public void setRoleType(Long roleType) {
		this.roleType = roleType;
	}

	@Column(name = "COMMENTS")
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}
	public Integer getSortId() {
		return sortId;
	}

	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}

	@Override
	public String toString() {
		return "DcmRole [roleCode=" + roleCode + ", roleName=" + roleName + ", roleType=" + roleType + ", comments="
				+ comments + ", realm=" + realm + ", sortId=" + sortId + "]";
	}

}