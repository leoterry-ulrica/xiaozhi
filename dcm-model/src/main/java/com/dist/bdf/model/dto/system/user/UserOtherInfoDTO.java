package com.dist.bdf.model.dto.system.user;

import java.io.Serializable;
import java.util.List;

import com.dist.bdf.model.entity.system.DcmAttachment;
import com.dist.bdf.model.entity.system.DcmUserOtherInfo;

public class UserOtherInfoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DcmUserOtherInfo info;
	private List<DcmAttachment> attachments;

	public List<DcmAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<DcmAttachment> attachments) {
		this.attachments = attachments;
	}

	public DcmUserOtherInfo getInfo() {
		return info;
	}

	public void setInfo(DcmUserOtherInfo info) {
		this.info = info;
	}

}
