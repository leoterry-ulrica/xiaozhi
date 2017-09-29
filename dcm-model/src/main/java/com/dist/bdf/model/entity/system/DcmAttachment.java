package com.dist.bdf.model.entity.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

@Entity
@Table(name = "DCM_ATTACHMENT")
public class DcmAttachment extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String attachmentId;
	private String attachmentName;
	private String attachmentDesc;
	private Long referenceId;
	
	
	@Column(name = "ATTACHMENTID")
	public String getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	@Column(name = "ATTACHMENTNAME")
	public String getAttachmentName() {
		return attachmentName;
	}
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	@Column(name = "ATTACHMENTDESC")
	public String getAttachmentDesc() {
		return attachmentDesc;
	}
	public void setAttachmentDesc(String attachmentDesc) {
		this.attachmentDesc = attachmentDesc;
	}
	@Column(name = "REFERENCEID")
	public Long getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DcmAttachment [attachmentId=" + attachmentId + ", attachmentName=" + attachmentName
				+ ", attachmentDesc=" + attachmentDesc + ", referenceId=" + referenceId + "]";
	}

}
