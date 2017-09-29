package com.dist.bdf.model.dto.wechat;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;
/**
 * 
 * 微信用户信息模型

 openid	用户的唯一标识
 nickname	用户昵称
 sex	用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
 province	用户个人资料填写的省份
 city	普通用户个人资料填写的城市
 country	国家，如中国为CN
 headimgurl	用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
 privilege	用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
 unionid	只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。详见：获取用户个人信息（UnionID机制）

（PS：把错误返回的情况也合并到此模型）
 * @author weifj
 *
 */
public class UserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 城市
	 */
	private String city;
	/**
	 * 国家
	 */
	private String country;
	/**
	 * 性别：1：男；0：女
	 */
	@Deprecated
	private int gender;
	/**
	 * 性别：1时是男性，值为2时是女性，值为0时是未知
	 */
	private int sex;
	/**
	 * 语言
	 */
	private String language;
	/**
	 * 昵称
	 */
	private String nickName;
	/**
	 * 省
	 */
	private String province;
	/**
	 * 头像
	 */
	private String headimgurl;
	/**
	 * 唯一id
	 * 如果开发者拥有多个移动应用、网站应用、和公众帐号（包括小程序），
	 * 可通过unionid来区分用户的唯一性，因为只要是同一个微信开放平台帐号下的移动应用、网站应用和公众帐号（包括小程序），用户的unionid是唯一的。
	 * 换句话说，同一用户，对同一个微信开放平台下的不同应用，unionId是相同的。
	 */
	@NotEmpty( message = "UserLoginByWechatDTO property [unionid] can not be empty")
	private String unionid;
	/**
	 * 用户在当前小程序的唯一标识 
	 */
	private String openId;
	/**
	 * 错误编码
	 */
	private int errcode;
	/**
	 * 错误信息
	 */
	private String errmsg;
	
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
	@Deprecated
	public int getGender() {
		return gender;
	}
	@Deprecated
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
	
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	public int getErrcode() {
		return errcode;
	}
	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
}
