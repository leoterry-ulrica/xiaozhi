package com.dist.bdf.model.entity.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

/**
 * 权限表
 */
@Entity
@Table(name = "DCM_PRIVILEGE")
public class DcmPrivilege  extends BaseEntity {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String privCode;
	private String privName;
	private Long privValue;

	// Constructors

	// Property accessors

	/**
	 * @return the privcode
	 */
	@Column(name = "PRIVCODE", length = 50)
	public String getPrivCode() {
		return privCode;
	}

	/**
	 * @param privcode the privcode to set
	 */
	public void setPrivCode(String privCode) {
		this.privCode = privCode;
	}

	/**
	 * @return the privname
	 */
	@Column(name = "PRIVNAME", length = 50)
	public String getPrivName() {
		return privName;
	}

	/**
	 * @param privname the privname to set
	 */
	public void setPrivName(String privName) {
		this.privName = privName;
	}

	/**
	 * @return the privvalue
	 */
	@Column(name = "PRIVVALUE", length = 50)
	public Long getPrivValue() {
		return privValue;
	}

	/**
	 * @param privvalue the privvalue to set
	 */
	public void setPrivValue(Long privValue) {
		this.privValue = privValue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DcmPrivilege [privCode=" + privCode + ", privName=" + privName + ", privValue=" + privValue + "]";
	}

}