package com.dist.bdf.model.dto.system.user;

import java.io.Serializable;

/**
 * 
 * 用户修改密码DTO
 * @author weifj
 *
 */
public class UserChangePwdDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 老密码
	 */
	private String oldPwd;
	/**
	 * 新密码
	 */
	private String newPwd;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOldPwd() {
		return oldPwd;
	}
	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}
	public String getNewPwd() {
		return newPwd;
	}
	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
}
