package com.dist.bdf.model.entity.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

/**
 * 资源分享表
 */
@Entity
@Table(name = "DCM_SHARE")
public class DcmShare extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields
	private String resId;
	private String resTypeCode;
	private String sourceDomainType;
	private String sourceDomainCode;
	private String targetDomainType;
	private String targetDomainCode;
	private String privCodes;
	private Date shareDateTime;
	private Date expiryDateTime;
	private String sharer;
	private Long status;
	private Long shareType = 0L;
	private Long isFolder = 0L;
	private String realm;

	// Constructors
	/** default constructor */
	public DcmShare() {
	}

	@Column(name = "RESID", length = 38)
	public String getResId() {
		return resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}
	
	/**
	 * 有效期
	 * @return
	 */
	@Column(name = "EXPIRYDATETIME")
	public Date getExpiryDateTime() {
		return expiryDateTime;
	}

	public void setExpiryDateTime(Date expiryDateTime) {
		this.expiryDateTime = expiryDateTime;
	}

	/**
	 * 分享时间
	 * @return
	 */
	@Column(name="SHAREDATETIME")
	public Date getShareDateTime() {
		return shareDateTime;
	}

	public void setShareDateTime(Date shareDateTime) {
		this.shareDateTime = shareDateTime;
	}
	@Column(name = "SOURCEDOMAINCODE", length = 50)
	public String getSourceDomainCode() {
		return sourceDomainCode;
	}

	public void setSourceDomainCode(String sourceDomainCode) {
		this.sourceDomainCode = sourceDomainCode;
	}
	@Column(name = "TARGETDOMAINCODE", length = 50)
	public String getTargetDomainCode() {
		return targetDomainCode;
	}

	public void setTargetDomainCode(String targetDomainCode) {
		this.targetDomainCode = targetDomainCode;
	}

	@Column(name = "RESTYPECODE", length = 50)
	public String getResTypeCode() {
		return resTypeCode;
	}
	public void setResTypeCode(String resTypeCode) {
		this.resTypeCode = resTypeCode;
	}

	public void setPrivCodes(String privCodes) {
		this.privCodes = privCodes;
	}
	/**
	 * 权限编码集合，多个code之间使用逗号分隔
	 * @return
	 */
	@Column(name = "PRIVCODES", length = 500)
	public String getPrivCodes() {
		return privCodes;
	}

	/**
	 * 分享来源的空间域类型
	 * @return
	 */
	@Column(name="SOURCEDOMAINTYPE", length=50)
	public String getSourceDomainType() {
		return sourceDomainType;
	}

	public void setSourceDomainType(String sourceDomainType) {
		this.sourceDomainType = sourceDomainType;
	}

	/**
	 * 分享目标空间域类型
	 * @return
	 */
	@Column(name="TARGETDOMAINTYPE", length=50)
	public String getTargetDomainType() {
		return targetDomainType;
	}

	public void setTargetDomainType(String targetDomainType) {
		this.targetDomainType = targetDomainType;
	}

	/**
	 * 分享者
	 * @return
	 */
	@Column(name="SHARER", length=20)
	public String getSharer() {
		return sharer;
	}

	public void setSharer(String sharer) {
		this.sharer = sharer;
	}
	/**
	 * 状态，是否有效
	 * 1：有效；0：无效
	 * @return
	 */
	@Column(name="STATUS", length=5)
	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	@Column(name="SHARETYPE")
	public Long getShareType() {
		return shareType;
	}

	public void setShareType(Long shareType) {
		this.shareType = shareType;
	}

	@Column(name="ISFOLDER")
	public Long getIsFolder() {
		return isFolder;
	}

	public void setIsFolder(Long isFolder) {
		this.isFolder = isFolder;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	@Override
	public String toString() {
		return "DcmShare [resId=" + resId + ", resTypeCode=" + resTypeCode + ", sourceDomainType=" + sourceDomainType
				+ ", sourceDomainCode=" + sourceDomainCode + ", targetDomainType=" + targetDomainType
				+ ", targetDomainCode=" + targetDomainCode + ", privCodes=" + privCodes + ", shareDateTime="
				+ shareDateTime + ", expiryDateTime=" + expiryDateTime + ", sharer=" + sharer + ", status=" + status
				+ ", shareType=" + shareType + ", isFolder=" + isFolder + ", realm=" + realm + "]";
	}

}