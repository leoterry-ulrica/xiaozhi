package com.dist.bdf.model.dto.system.user;

import java.io.Serializable;
import java.util.List;

import com.dist.bdf.model.entity.system.DcmAttachment;
import com.dist.bdf.model.entity.system.DcmUserEducation;

public class UserEducationDTO implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DcmUserEducation info;
	private List<DcmAttachment> attachments;

	public List<DcmAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<DcmAttachment> attachments) {
		this.attachments = attachments;
	}

	public DcmUserEducation getInfo() {
		return info;
	}

	public void setInfo(DcmUserEducation info) {
		this.info = info;
	}
}
