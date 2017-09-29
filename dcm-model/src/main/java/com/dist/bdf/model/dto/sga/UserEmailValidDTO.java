package com.dist.bdf.model.dto.sga;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户注册信息邮件确认模型
 * @author weifj
 *
 */
public class UserEmailValidDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sysCode;
	private String email;
	private String wechat;
	private Date createTime;
	private int registerType;
	/**
	 * 标记。0：新创建；1：正常接受邀请，未完成注册(只是点击验证链接)；2：正常接受邀请，并完成注册；3：未接受邀请，自动过期：4：接受邀请，但已过期；
	 */
	private int mark;
	
	public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getWechat() {
		return wechat;
	}
	public void setWechat(String wechat) {
		this.wechat = wechat;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getRegisterType() {
		return registerType;
	}
	public void setRegisterType(int registerType) {
		this.registerType = registerType;
	}
	public int getMark() {
		return mark;
	}
	public void setMark(int mark) {
		this.mark = mark;
	}
	@Override
	public String toString() {
		return "UserEmailValidDTO [sysCode=" + sysCode + ", email=" + email + ", wechat=" + wechat + ", createTime="
				+ createTime + ", registerType=" + registerType + ", mark=" + mark + "]";
	}

}
