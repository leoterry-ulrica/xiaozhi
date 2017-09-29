package com.dist.bdf.model.dto.system;

import java.io.Serializable;
/**
 * 项目与成员信息
 * @author weifj
 *
 */
public class ProjectUserDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户编码
	 */
	private String userCode;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 登录名
	 */
	private String loginName;
	/**
	 * 头像
	 */
	private String avatar;
	/**
	 * 单位
	 */
	private String unit;
	/**
	 * 职位
	 */
	private String position;
	/**
	 * 是否大牛
	 */
	private Integer isTop;
	/**
	 * 用户类型，0：内部；1：外部
	 */
	private Integer userType;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public Integer getIsTop() {
		return isTop;
	}
	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
	}

	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	@Override
	public String toString() {
		return "ProjectUserDTO [userCode=" + userCode + ", userName=" + userName + ", loginName=" + loginName
				+ ", avatar=" + avatar + ", unit=" + unit + ", position=" + position + ", isTop=" + isTop
				+ ", userType=" + userType + "]";
	} 
	
}
