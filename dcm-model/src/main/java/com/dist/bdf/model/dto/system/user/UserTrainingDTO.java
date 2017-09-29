package com.dist.bdf.model.dto.system.user;

import java.io.Serializable;
import java.util.List;

import com.dist.bdf.model.entity.system.DcmAttachment;
import com.dist.bdf.model.entity.system.DcmUserTraining;

public class UserTrainingDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DcmUserTraining info;
	private List<DcmAttachment> attachments;

	public List<DcmAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<DcmAttachment> attachments) {
		this.attachments = attachments;
	}

	public DcmUserTraining getInfo() {
		return info;
	}

	public void setInfo(DcmUserTraining info) {
		this.info = info;
	}
}
