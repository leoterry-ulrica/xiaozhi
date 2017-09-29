package com.dist.bdf.model.dto.wechat;

import java.io.Serializable;
/**
 * 
 * 微信获取token返回的模型
 * 
 {
   "access_token":"ACCESS_TOKEN",
   "expires_in":7200,
   "refresh_token":"REFRESH_TOKEN",
   "openid":"OPENID",
   "scope":"SCOPE"
 }
 access_token	网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
 expires_in	access_token接口调用凭证超时时间，单位（秒）
 refresh_token	用户刷新access_token
 openid	用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
 scope	用户授权的作用域，使用逗号（,）分隔

 （PS：把错误返回的情况也合并到此模型）
 * @author weifj
 *
 */
public class AccessToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String access_token;
	private int expires_in;
	private String refresh_token;
	private String openid;
	private String scope;
	
	private int errcode;
	private String errmsg;
	
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public int getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
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
}
