package com.dist.bdf.model.entity.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

@Entity
@Table(name = "DCM_USER_WORKEXPERIENCE")
public class DcmUserWorkExperience extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String unit;
	private String department;
	private String position;
	private String detail;
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
	@Column(name = "UNIT")
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	@Column(name = "DEPARTMENT")
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	@Column(name = "POSITION")
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	@Column(name = "DETAIL")
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DcmUserWorkExperience [unit=" + unit + ", department=" + department + ", position=" + position
				+ ", detail=" + detail + ", timeStart=" + timeStart + ", timeEnd=" + timeEnd + ", userId=" + userId
				+ "]";
	}

}
