package com.dist.bdf.model.dto.sga;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 
 * 发送邀请的信息
 * @author weifj
 *
 */
public class InvitationInfoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 发送的邮箱地址
	 */
	@NotEmpty(message = "email can not be empty...")
	private String email;
	/**
	 * 系统编号，这里指项目guid号
	 */
	@NotEmpty(message = "sysCode can not be empty...")
	private String sysCode;
	/**
	 * 邮件正文
	 * 如果正文为空，则使用系统自定义内容。
	 */
	private String content;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "InvitationInfoDTO [email=" + email + ", sysCode=" + sysCode + ", content=" + content + "]";
	}

}
