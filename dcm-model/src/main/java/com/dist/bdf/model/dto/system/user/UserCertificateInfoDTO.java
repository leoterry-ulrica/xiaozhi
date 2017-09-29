package com.dist.bdf.model.dto.system.user;

import java.io.Serializable;
import java.util.List;

import com.dist.bdf.model.entity.system.DcmAttachment;
import com.dist.bdf.model.entity.system.DcmUserCertificateInfo;

public class UserCertificateInfoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DcmUserCertificateInfo info;
	private List<DcmAttachment> attachments;

	public List<DcmAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<DcmAttachment> attachments) {
		this.attachments = attachments;
	}

	public DcmUserCertificateInfo getInfo() {
		return info;
	}

	public void setInfo(DcmUserCertificateInfo info) {
		this.info = info;
	}
	
}
