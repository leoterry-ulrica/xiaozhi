package com.dist.bdf.model.dto.system;

import java.io.Serializable;

public class DomainOpDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 空间域编码
	 */
	private String dc;
	/**
	 * 操作，1：添加；0：去除
	 */
	private int op;
	/**
	 * 权限编码
	 */
	private String privCode;
	
	public String getDc() {
		return dc;
	}
	public void setDc(String dc) {
		this.dc = dc;
	}
	public int getOp() {
		return op;
	}
	public void setOp(int op) {
		this.op = op;
	}
	public String getPrivCode() {
		return privCode;
	}
	public void setPrivCode(String privCode) {
		this.privCode = privCode;
	}
	
}
