package com.dist.bdf.model.dto.sga;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 用户使用邮箱登录的模型
 * @author weifj
 *
 */
public class UserLoginByEmailDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 电子邮件
	 */
	@NotEmpty( message= "UserLoginByEmailDTO property [email] can not be empty")
	private String email;
	/**
	 * 加密后的密码
	 */
	@NotEmpty( message= "UserLoginByEmailDTO property [encryptPwd] can not be empty")
	private String encryptPwd;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEncryptPwd() {
		return encryptPwd;
	}
	public void setEncryptPwd(String encryptPwd) {
		this.encryptPwd = encryptPwd;
	}
	@Override
	public String toString() {
		return "UserLoginByEmailDTO [email=" + email + ", encryptPwd=" + encryptPwd + "]";
	}
}
