package com.dist.bdf.model.dto.system.user;

import java.io.Serializable;

public class UserDetailRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户基本信息
	 */
	private UserDTO base;
	/**
	 * 用户详细信息
	 */
	private UserDetailDTO detail;
	
	public UserDTO getBase() {
		return base;
	}
	public void setBase(UserDTO base) {
		this.base = base;
	}
	public UserDetailDTO getDetail() {
		return detail;
	}
	public void setDetail(UserDetailDTO detail) {
		this.detail = detail;
	}
}
