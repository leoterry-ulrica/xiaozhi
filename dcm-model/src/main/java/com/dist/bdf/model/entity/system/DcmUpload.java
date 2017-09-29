package com.dist.bdf.model.entity.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

/**
 * 下载记录模型
 */
@Entity
@Table(name = "DCM_UPLOAD")
public class DcmUpload extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields
	private String resId;
	private String uploader;
	private Date createTime;
	private String resTypeCode;
	private String domainCode;
	private String proxy;

	// Constructors
	
	@Column(name="DOMAINCODE")
	public String getDomainCode() {
		return domainCode;
	}

	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}

	@Column(name = "CREATETIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name="RESID")
	public String getResId() {
		return resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}

	@Column(name="RESTYPECODE")
	public String getResTypeCode() {
		return resTypeCode;
	}

	public void setResTypeCode(String resTypeCode) {
		this.resTypeCode = resTypeCode;
	}
	
	@Column(name="UPLOADER")
	public String getUploader() {
		return uploader;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	@Column(name="PROXY")
	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DcmUpload [resId=" + resId + ", uploader=" + uploader + ", createTime=" + createTime + ", resTypeCode="
				+ resTypeCode + ", domainCode=" + domainCode + ", proxy=" + proxy + "]";
	}

}