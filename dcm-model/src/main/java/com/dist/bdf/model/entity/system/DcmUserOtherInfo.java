package com.dist.bdf.model.entity.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

@Entity
@Table(name = "DCM_USER_OTHERINFO")
public class DcmUserOtherInfo extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String attachmentName;
	private String attachmentDesc;
	private Long userId;
	
	
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
	@Column(name = "USERID")
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DcmUserOtherInfo [attachmentName=" + attachmentName + ", attachmentDesc=" + attachmentDesc + ", userId="
				+ userId + "]";
	}

}
