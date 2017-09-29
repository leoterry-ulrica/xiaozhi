package com.dist.bdf.model.entity.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

@Entity
@Table(name = "DCM_USER_LANGUAGE")
public class DcmUserLanguage extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type;
	private String levels;
	private Long userId;
	
	@Column(name = "USERID")
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	@Column(name = "TYPE")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Column(name = "LEVELS")
	public String getLevels() {
		return levels;
	}
	public void setLevels(String levels) {
		this.levels = levels;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DcmUserLanguage [type=" + type + ", levels=" + levels + ", userId=" + userId + "]";
	}

}
