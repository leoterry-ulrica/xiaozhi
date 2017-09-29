package com.dist.bdf.model.entity.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;
/**
 * 
 * @author weifj
 *
 */
@Entity
@Table(name = "DCM_PERSONALMATERIAL")
public class DcmPersonalmaterial extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String resId;
	private String parentResId;
	private String resTypeCode;
	private Date dateCreated;
	private String creator;
	private Integer isFolder;
	
	@Column(name="RESID")
	public String getResId() {
		return resId;
	}
	public void setResId(String resId) {
		this.resId = resId;
	}
	@Column(name="PARENTRESID")
	public String getParentResId() {
		return parentResId;
	}
	public void setParentResId(String parentResId) {
		this.parentResId = parentResId;
	}
	@Column(name="RESTYPECODE")
	public String getResTypeCode() {
		return resTypeCode;
	}
	public void setResTypeCode(String resTypeCode) {
		this.resTypeCode = resTypeCode;
	}
	@Column(name="DATECREATED")
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	@Column(name="CREATOR")
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	@Column(name="ISFOLDER")
	public int getIsFolder() {
		return isFolder;
	}
	public void setIsFolder(int isFolder) {
		this.isFolder = isFolder;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DcmPersonalmaterial [resId=" + resId + ", parentResId=" + parentResId + ", resTypeCode=" + resTypeCode
				+ ", dateCreated=" + dateCreated + ", creator=" + creator + ", isFolder=" + isFolder + "]";
	}
}
