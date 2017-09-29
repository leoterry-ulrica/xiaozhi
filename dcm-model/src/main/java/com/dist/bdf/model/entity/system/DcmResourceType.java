package com.dist.bdf.model.entity.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

/**
 * 资源类型
 */
@Entity
@Table(name = "DCM_RESTYPE")
public class DcmResourceType  extends BaseEntity {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String resTypeCode;
	private String resTypeName;

	// Constructors

	@Column(name = "RESTYPECODE", length = 50)
	public String getRestypecode() {
		return this.resTypeCode;
	}

	public void setResTypeCode(String resTypeCode) {
		this.resTypeCode = resTypeCode;
	}

	@Column(name = "RESTYPENAME", length = 100)
	public String getResTypeName() {
		return this.resTypeName;
	}

	public void setResTypeName(String resTypeName) {
		this.resTypeName = resTypeName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DcmResourceType [resTypeCode=" + resTypeCode + ", resTypeName=" + resTypeName + "]";
	}

}