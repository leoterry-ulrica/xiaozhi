package com.dist.bdf.model.dto.sga;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户附件返回信息
 * @author weifj
 *
 */
public class UserAttachRepDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String attachVId;
	private String attachName;
	private Date createTime;
	private String mimeType;
	private Integer attachType;
	private String attachId;
	
	public String getAttachVId() {
		return attachVId;
	}
	public void setAttachVId(String attachVId) {
		this.attachVId = attachVId;
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
	public String getAttachId() {
		return attachId;
	}
	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}
	@Override
	public String toString() {
		return "UserAttachRepDTO [attachVId=" + attachVId + ", attachName=" + attachName + ", createTime=" + createTime
				+ ", mimeType=" + mimeType + ", attachType=" + attachType + ", attachId=" + attachId + "]";
	}
}
