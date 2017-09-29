package com.dist.bdf.model.entity.sga;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SGA_USER_ATTACHMENT")
public class SgaUserAttachment extends SgaBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long userId;
	private String attachVId;
	private String attachName;
	private Date createTime;
	private String mimeType;
	private Integer attachType;
	private String attachId;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getAttachName() {
		return attachName;
	}
	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public Integer getAttachType() {
		return attachType;
	}
	public void setAttachType(Integer attachType) {
		this.attachType = attachType;
	}
	public String getAttachVId() {
		return attachVId;
	}
	public void setAttachVId(String attachVId) {
		this.attachVId = attachVId;
	}
	public String getAttachId() {
		return attachId;
	}
	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}
	@Override
	public String toString() {
		return "SgaUserAttachment [userId=" + userId + ", attachVId=" + attachVId + ", attachName=" + attachName
				+ ", createTime=" + createTime + ", mimeType=" + mimeType + ", attachType=" + attachType + ", attachId="
				+ attachId + "]";
	}

	
}
