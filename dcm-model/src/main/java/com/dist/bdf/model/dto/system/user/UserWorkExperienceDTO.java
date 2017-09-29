package com.dist.bdf.model.dto.system.user;

import java.io.Serializable;
import java.util.List;

import com.dist.bdf.model.entity.system.DcmAttachment;
import com.dist.bdf.model.entity.system.DcmUserWorkExperience;

public class UserWorkExperienceDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DcmUserWorkExperience info;
	/**
	 * 项目经历
	 */
	/*private List<UserPrjExperienceDTO> projects;*/
	private List<DcmAttachment> attachments;

	public List<DcmAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<DcmAttachment> attachments) {
		this.attachments = attachments;
	}
	
/*	public List<UserPrjExperienceDTO> getProjects() {
		return projects;
	}

	public void setProjects(List<UserPrjExperienceDTO> projects) {
		this.projects = projects;
	}*/

	public DcmUserWorkExperience getInfo() {
		return info;
	}

	public void setInfo(DcmUserWorkExperience info) {
		this.info = info;
	}
}
