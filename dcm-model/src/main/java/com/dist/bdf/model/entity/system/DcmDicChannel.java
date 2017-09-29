package com.dist.bdf.model.entity.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

@Entity
@Table(name = "DCM_DIC_CHANNEL")
public class DcmDicChannel extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String code;
	private Long isBuildIn;
	private Date createTime;
	private String caseId;
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "ISBUILDIN")
	public Long getIsBuildIn() {
		return isBuildIn;
	}
	public void setIsBuildIn(Long isBuildIn) {
		this.isBuildIn = isBuildIn;
	}
	@Column(name = "CREATETIME")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name = "CODE")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Column(name = "CASEID")
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DcmDicChannel [name=" + name + ", code=" + code + ", isBuildIn=" + isBuildIn + ", createTime="
				+ createTime + ", caseId=" + caseId + "]";
	}
	
}
