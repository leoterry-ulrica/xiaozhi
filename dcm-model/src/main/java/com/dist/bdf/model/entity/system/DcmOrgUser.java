package com.dist.bdf.model.entity.system;

import java.lang.Long;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

/**
 * 机构与用户关系表
 */
@Entity
@Table(name = "DCM_ORG_USER")
public class DcmOrgUser  extends BaseEntity {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long userId;
	private Long orgId;

	// Constructors

	// Property accessors

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "USERID", precision = 22, scale = 0)
	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "ORGID", precision = 22, scale = 0)
	public Long getOrgId() {
		return this.orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DcmOrgUser [userId=" + userId + ", orgId=" + orgId + "]";
	}

}