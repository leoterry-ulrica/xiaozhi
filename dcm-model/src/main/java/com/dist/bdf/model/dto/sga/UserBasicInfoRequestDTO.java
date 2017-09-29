package com.dist.bdf.model.dto.sga;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 * 用户基本信息
 * @author weifj
 *
 */
public class UserBasicInfoRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "UserBasicInfoRequestDTO property [id] can not be null")
	private Long id;
	private String loginName;
	private String userName;
	private String sex;
	private String position;
	private String telephone;
	private String email;
	private String unit;
	private String avatar;
	private String address;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	@Override
	public String toString() {
		return "UserBasicInfoRequestDTO [id=" + id + ", loginName=" + loginName + ", userName=" + userName + ", sex="
				+ sex + ", position=" + position + ", telephone=" + telephone + ", email=" + email + ", unit=" + unit
				+ ", avatar=" + avatar + "]";
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

}
