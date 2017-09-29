package com.dist.bdf.model.entity.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

/**
 * 用户-空间域-角色
 */
@Entity
@Table(name = "DCM_USERDOMAINROLE")
public class DcmUserdomainrole extends BaseEntity {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long userId;
	private String domainCode;
	private String domainType;
	private String roleCode;
	private Date createTime;
	private Integer isTop = 0;
	private Date lastTime;
	private String userCode;
	private Integer userType;

	// Constructors

	@Column(name = "USERID", length = 36)
	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "DOMAINCODE", length = 50)
	public String getDomainCode() {
		return this.domainCode;
	}

	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}

	@Column(name = "DOMAINTYPE", length = 50)
	public String getDomainType() {
		return this.domainType;
	}

	public void setDomainType(String domainType) {
		this.domainType = domainType;
	}

	@Column(name = "ROLECODE", length = 50)
	public String getRoleCode() {
		return this.roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getIsTop() {
		return isTop;
	}

	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DcmUserdomainrole [userId=" + userId + ", domainCode=" + domainCode + ", domainType=" + domainType
				+ ", roleCode=" + roleCode + ", createTime=" + createTime + ", isTop=" + isTop + ", lastTime="
				+ lastTime + ", userCode=" + userCode + ", userType=" + userType + "]";
	}

}