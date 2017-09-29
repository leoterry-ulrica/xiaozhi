package com.dist.bdf.model.entity.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

@Entity
@Table(name = "DCM_USER_PRJEXPERIENCE")
public class DcmUserPrjExperience extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String projectName;
	private String projectCode;
	private String position;
	private Date timeStart;
	private Date timeEnd;
	private Long refWorkExperienceId;
	private Long userId;
	private String projectDesc;
	
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

	@Column(name = "POSITION")
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	@Column(name = "REFWORKEXPERIENCEID")
	public Long getRefWorkExperienceId() {
		return refWorkExperienceId;
	}
	public void setRefWorkExperienceId(Long refWorkExperienceId) {
		this.refWorkExperienceId = refWorkExperienceId;
	}
	@Column(name = "USERID")
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	@Column(name = "PROJECTDESC")
	public String getProjectDesc() {
		return projectDesc;
	}
	public void setProjectDesc(String projectDesc) {
		this.projectDesc = projectDesc;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DcmUserPrjExperience [projectName=" + projectName + ", projectCode=" + projectCode + ", position="
				+ position + ", timeStart=" + timeStart + ", timeEnd=" + timeEnd + ", refWorkExperienceId="
				+ refWorkExperienceId + ", userId=" + userId + ", projectDesc=" + projectDesc + "]";
	}

}
