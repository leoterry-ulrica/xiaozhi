package com.dist.bdf.model.entity.sga;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SGA_INVITEQUEUE")
public class SgaInviteQueue extends SgaBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sysCode;
	private Date createTime;
	private Date expires;
	private Integer mark;
	private Integer registerType;
	
	public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getMark() {
		return mark;
	}
	public void setMark(int mark) {
		this.mark = mark;
	}
	public Date getExpires() {
		return expires;
	}
	public void setExpires(Date expires) {
		this.expires = expires;
	}

	public int getRegisterType() {
		return registerType;
	}
	public void setRegisterType(int registerType) {
		this.registerType = registerType;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SgaInviteQueue [sysCode=" + sysCode + ", createTime=" + createTime + ", expires=" + expires + ", mark="
				+ mark + ", registerType=" + registerType + "]";
	}
	
}
