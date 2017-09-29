package com.dist.bdf.model.entity.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

@Entity
@Table(name = "DCM_USER_TITLEINFO")
public class DcmUserTitleInfo extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private Date getTime;
	private Long userId;
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "USERID")
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	@Column(name = "GETTIME")
	public Date getGetTime() {
		return getTime;
	}
	public void setGetTime(Date getTime) {
		this.getTime = getTime;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DcmUserTitleInfo [name=" + name + ", getTime=" + getTime + ", userId=" + userId + "]";
	}

}
