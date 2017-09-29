package com.dist.bdf.model.entity.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

@Entity
@Table(name = "DCM_USER_EDUCATION")
public class DcmUserEducation extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String academy;
	private String college;
	private String major;
	private String degree;
	private Date timeStart;
	private Date timeEnd;
	private Long userId;
	
	
	@Column(name = "USERID")
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	@Column(name = "ACADEMY")
	public String getAcademy() {
		return academy;
	}
	public void setAcademy(String academy) {
		this.academy = academy;
	}
	@Column(name = "COLLEGE")
	public String getCollege() {
		return college;
	}
	public void setCollege(String college) {
		this.college = college;
	}
	@Column(name = "MAJOR")
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	@Column(name = "DEGREE")
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	@Column(name = "TIMESTART")
	public Date getTimeStart() {
		return timeStart;
	}
	public void setTimeStart(Date timeStart) {
		this.timeStart = timeStart;
	}
	@Column(name = "TIMEEND")
	public Date getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(Date timeEnd) {
		this.timeEnd = timeEnd;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DcmUserEducation [academy=" + academy + ", college=" + college + ", major=" + major + ", degree="
				+ degree + ", timeStart=" + timeStart + ", timeEnd=" + timeEnd + ", userId=" + userId + "]";
	}

}
