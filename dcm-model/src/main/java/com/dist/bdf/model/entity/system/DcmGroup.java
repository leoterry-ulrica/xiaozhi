package com.dist.bdf.model.entity.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

/**
 * 组，包括讨论组和项目组
 * 
 */
@Entity
@Table(name = "DCM_GROUP")
public class DcmGroup extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields
	private String domainType;
	private String groupName;
	private String groupCode;
	private String creator;
	private Date createTime;
	private Date modifiedTime;
	private String guid;
	private String orgCode;
	private String orgType;
	private String realm;
	private String img;

	// Constructors

	@Column(name="GUID",length = 38)
	public String getGuid() {
		return guid;
	}


	public void setGuid(String guid) {
		this.guid = guid;
	}


	/** default constructor */
	public DcmGroup() {
	}


	@Column(name = "DOMAINTYPE", length = 50)
	public String getDomainType() {
		return this.domainType;
	}

	public void setDomainType(String domainType) {
		this.domainType = domainType;
	}

	@Column(name = "GROUPNAME", length = 100)
	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Column(name = "GROUPCODE", length = 50)
	public String getGroupCode() {
		return this.groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}


	@Column(name = "CREATOR", length = 36)
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}


	@Column(name = "CREATETIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "MODIFIEDTIME")
	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	@Column(name = "ORGCODE")
	public String getOrgCode() {
		return orgCode;
	}


	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	@Column(name = "ORGTYPE")
	public String getOrgType() {
		return orgType;
	}


	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	@Column(name = "REALM")
	public String getRealm() {
		return realm;
	}


	public void setRealm(String realm) {
		this.realm = realm;
	}
	public String getImg() {
		return img;
	}


	public void setImg(String img) {
		this.img = img;
	}


	@Override
	public String toString() {
		return "DcmGroup [domainType=" + domainType + ", groupName=" + groupName + ", groupCode=" + groupCode
				+ ", creator=" + creator + ", createTime=" + createTime + ", modifiedTime=" + modifiedTime + ", guid="
				+ guid + ", orgCode=" + orgCode + ", orgType=" + orgType + ", realm=" + realm + ", img=" + img + "]";
	}

}