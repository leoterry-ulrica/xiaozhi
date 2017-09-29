package com.dist.bdf.model.dto.sga;

import java.io.Serializable;
import java.util.Date;

import com.dist.bdf.base.office.excel.Excel;

public class UserResponseDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	@Excel(name = "登录名")
	private String loginName;
	@Excel(name = "用户名")
	private String userName;
	private String sysCode;
	@Excel(name = "性别")
	private String sex;
	@Excel(name = "职位")
	private String position;
	@Excel(name = "联系电话")
	private String telephone;
	@Excel(name = "邮箱")
	private String email;
	@Excel(name = "单位")
	private String unit;
	private String wechat;
	private String avatar;
	@Excel(name = "报名状态")
	private Integer status;
	@Excel(name = "报名时间")
	private Date createTime;
	@Excel(name = "地址")
	private String address;
	private String openId;
	/**
	 * 是否新创建
	 */
	private boolean isNew;

	public boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
	}
	public String getUserCode() {
		return this.getSysCode();
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSysCode() {
		return sysCode;
	}

	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
}
