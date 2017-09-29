package com.dist.bdf.model.dto.system.user;

import java.util.Date;

import com.dist.bdf.model.entity.system.DcmTeam;
import com.dist.bdf.model.entity.system.DcmUser;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserProjectResponseDTO extends DcmUser {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer isTop;
	private Integer isPartner;
	private DcmTeam team;
	
	@JsonIgnore
	public String getLoginName() {
		return "";
	}
	@JsonIgnore
	public String getSex() {
		return "";
	}
	@JsonIgnore
	public String getDn() {
		return "";
	}
	@JsonIgnore
	public String getRealm() {
		return "";
	}

	@JsonIgnore
	public String getPhone() {
		return "";
	}
	@JsonIgnore
	public String getUserPwd() {
		return "";
	}
	@JsonIgnore
	public String getEmail() {
		return "";
	}
	@JsonIgnore
	public Long getCurrentStatus() {
		return null;
	}

	@JsonIgnore
	public Date getDateLastActivity() {
		return null;
	}
	@JsonIgnore
	public Long getIsBuildin() {
		return null;
	}
	@JsonIgnore
	public String getMajor() {
		return "";
	}
	@JsonIgnore
	public String getQq() {
		return "";
	}
	@JsonIgnore
	public String getNativePlace() {
		return "";
	}
	@JsonIgnore
	public String getDepartment() {
		return "";
	}
	@JsonIgnore
	public String getPosition() {
		return "";
	}
	@JsonIgnore
	public Date getBirthday() {
		return null;
	}
	@JsonIgnore
	public String getSpeciality() {
		return "";
	}
	public Integer getIsTop() {
		return isTop;
	}
	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
	}
	@JsonIgnore
	public String getTelephone() {
		return "";
	}

	public Integer getIsPartner() {
		return isPartner;
	}
	public void setIsPartner(Integer isPartner) {
		this.isPartner = isPartner;
	}
	public DcmTeam getTeam() {
		return team;
	}
	public void setTeam(DcmTeam team) {
		this.team = team;
	}
}
