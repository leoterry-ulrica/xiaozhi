package com.dist.bdf.model.dto.sga;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;
/**
 * 
 * 使用微信登录的模型
 * @author weifj
 *
 */
public class UserLoginByWechatDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String city;
	private String country;
	/**
	 * 性别：1：男；0：女
	 */
	private int gender;
	private String language;
	private String nickName;
	private String province;
	private String avatarUrl;
	@NotEmpty( message = "UserLoginByWechatDTO property [unionid] can not be empty")
	private String unionid;
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	@Override
	public String toString() {
		return "UserLoginByWechatDTO [city=" + city + ", country=" + country + ", gender=" + gender + ", language="
				+ language + ", nickName=" + nickName + ", province=" + province + ", avatarUrl=" + avatarUrl
				+ ", unionid=" + unionid + "]";
	}
}
