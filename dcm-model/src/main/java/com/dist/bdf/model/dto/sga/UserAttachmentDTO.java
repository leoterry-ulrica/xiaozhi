package com.dist.bdf.model.dto.sga;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.dist.bdf.model.dto.dcm.PropertiesExDTO;

/**
 * 用户附件
 * @author weifj
 *
 */
public class UserAttachmentDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotNull(message = "UserAttachmentDTO property userCode cannot be empty")
	private String userCode;
	private String attachVId;
	private String attachName;
	private Date createTime;
	private String mimeType;
	private Integer attachType;
	private String attachId;
	/**
	 * 如果为空，则使用默认公共存储库
	 */
	private String realm;
	/**
	 * 扩展属性
	 */
	private PropertiesExDTO propertiesEx = new PropertiesExDTO();
	/**
	 * 反射机制需要用到无参构造函数
	 */
	public UserAttachmentDTO() {
		
	}
	public UserAttachmentDTO(String userCode, String attachVId, String attachName, Long createTime, String mimeType, Integer attachType) {
		this.userCode = userCode;
		this.attachVId = attachVId;
		this.attachName = attachName;
		this.createTime = new Date(createTime);
		this.mimeType = mimeType;
		this.attachType = attachType;
	}
	public UserAttachmentDTO(String userCode, String attachVId, String attachId, String attachName, Long createTime, String mimeType, Integer attachType) {
		this.userCode = userCode;
		this.attachVId = attachVId;
		this.attachId = attachId;
		this.attachName = attachName;
		this.createTime = new Date(createTime);
		this.mimeType = mimeType;
		this.attachType = attachType;
	}
	
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
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
	
	@Override
	public String toString() {
		return "UserAttachmentDTO [userCode=" + userCode + ", attachVId=" + attachVId + ", attachName=" + attachName
				+ ", createTime=" + createTime + ", mimeType=" + mimeType + ", attachType=" + attachType + "]";
	}
	public String getAttachVId() {
		return attachVId;
	}
	public void setAttachVId(String attachVId) {
		this.attachVId = attachVId;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
	public PropertiesExDTO getPropertiesEx() {
		return propertiesEx;
	}
	public void setPropertiesEx(PropertiesExDTO propertiesEx) {
		this.propertiesEx = propertiesEx;
	}
	public String getAttachId() {
		return attachId;
	}
	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}
	
}
