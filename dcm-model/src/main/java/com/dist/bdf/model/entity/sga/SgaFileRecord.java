package com.dist.bdf.model.entity.sga;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SGA_FILERECORD")
public class SgaFileRecord extends SgaBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String resId;
	private String creator;
	private Date createTime;
	private String resTypeCode;
	private String domainCode;
	private String agent;
	private String opType;
	
	public String getResId() {
		return resId;
	}
	public void setResId(String resId) {
		this.resId = resId;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getResTypeCode() {
		return resTypeCode;
	}
	public void setResTypeCode(String resTypeCode) {
		this.resTypeCode = resTypeCode;
	}
	public String getDomainCode() {
		return domainCode;
	}
	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public String getOpType() {
		return opType;
	}
	public void setOpType(String opType) {
		this.opType = opType;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SgaFileRecord [resId=" + resId + ", creator=" + creator + ", createTime=" + createTime
				+ ", resTypeCode=" + resTypeCode + ", domainCode=" + domainCode + ", agent=" + agent + ", opType="
				+ opType + "]";
	}
	
}
