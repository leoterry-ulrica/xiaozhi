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
@Table(name = "DCM_DOWNLOAD")
public class DcmDownload extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields
	private String resId;
	private String downloader;
	private Date createTime;
	private String resTypeCode;
	private String domainCode;

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

	@Column(name="DOWNLOADER")
	public String getDownloader() {
		return downloader;
	}

	public void setDownloader(String downloader) {
		this.downloader = downloader;
	}
	@Column(name="RESTYPECODE")
	public String getResTypeCode() {
		return resTypeCode;
	}

	public void setResTypeCode(String resTypeCode) {
		this.resTypeCode = resTypeCode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DcmDownload [resId=" + resId + ", downloader=" + downloader + ", createTime=" + createTime
				+ ", resTypeCode=" + resTypeCode + ", domainCode=" + domainCode + "]";
	}

}