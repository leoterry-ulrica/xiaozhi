package com.dist.bdf.common.conf.wechat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import io.github.xdiamond.client.XDiamondConfig;
import io.github.xdiamond.client.annotation.EnableConfigListener;
/**
 * 共享区配置
 * @author weifj
 *
 */
@Configuration
@EnableConfigListener
public class WechatConf {

	@Autowired
	private XDiamondConfig xconf;
	
	/**
	 * 获取重定向地址（放在此类欠佳，需要独立出去）
	 * @return
	 */
	public String getUserRegisterRedirectURI() {
		
		return this.xconf.getProperty("sga.userregister.redirect.uri");
	}
	/**
	 * 获取web端appId的加密值，加密算法：RSA
	 * @return
	 */
	public String getWxAppIdEncrypt(){
		
		return this.xconf.getProperty("wx.appId.encrypt");
	}
	
	/**
	 * 获取微信小程序appId的加密值，加密算法：RSA
	 * @return
	 */
	public String getWxAppAppIdEncrypt(){
		
		return this.xconf.getProperty("wx.app.appId.encrypt");
	}
	
	/**
	 * 获取web端appSecret的加密值，加密算法：RSA
	 * @return
	 */
	public String getWxAppSecretEncrypt(){
		
		return this.xconf.getProperty("wx.appSecret.encrypt");
	}
	
	/**
	 * 获取微信小程序appSecret的加密值，加密算法：RSA
	 * @return
	 */
	public String getWxAppAppSecretEncrypt(){
		
		return this.xconf.getProperty("wx.app.appSecret.encrypt");
	}
	/**
	 * 获取拉取token的uri地址
	 * @return
	 */
	public String getInvokeAccessTokenURI(){
		
		return this.xconf.getProperty("wx.access_token.uri");
	}
	/**
	 * 获取通过客户端验证获取token的uri地址
	 * @return
	 */
    public String getInvokeAccessTokenByClientCredentialURI(){
		
		return this.xconf.getProperty("wx.access_token.client_credential.uri");
	}
	
	/**
	 * 获取拉取用户信息的uri地址
	 * @return
	 */
	public String getInvokeUserInfoURI(){
		
		return this.xconf.getProperty("wx.snsapi_userinfo.uri");
	}
	/**
	 * 向微信服务器，使用登录凭证 code 获取 session_key 和 openid的地址。
	 * @return
	 */
	public String getInvokeJscode2sessionURI(){	
		
		return this.xconf.getProperty("wx.jscode2session.uri");
	}
	/**
	 * 获取微信小程序模板消息发送的地址
	 * @return
	 */
	public String getWxappSendTemplateMsgURI() {
		return this.xconf.getProperty("wx.app.template.message.uri");
	}
	/**
	 * 微信小程序模板消息报名人属性
	 * @return
	 */
	public String getWxappTemplatemsgPropertyApplicant() {
		return this.xconf.getProperty("wx.app.templatemsg.property.applicant");
	}
	/**
	 * 微信小程序模板消息会议地点属性
	 * @return
	 */
	public String getWxappTemplatemsgPropertyAddress() {
		return this.xconf.getProperty("wx.app.templatemsg.property.address");
	}
	/**
	 * 微信小程序模板消息会议地点属性的值
	 * @return
	 */
    public String getWxappTemplatemsgPropertyAddressValue() {
		
		return this.xconf.getProperty("wx.app.templatemsg.property.address.value");
	}
	/**
	 * 微信小程序模板消息会议时间属性
	 * @return
	 */
	public String getWxappTemplatemsgPropertyMeetingTime() {
		return this.xconf.getProperty("wx.app.templatemsg.property.meetingtime");
	}
	/**
	 * 微信小程序模板消息会议时间属性值
	 * @return
	 */
	public String getWxappTemplatemsgPropertyMeetingTimeValue() {
		return this.xconf.getProperty("wx.app.templatemsg.property.meetingtime.value");
	}
	/**
	 * 微信小程序模板消息报名时间属性
	 * @return
	 */
	public String getWxappTemplatemsgPropertyRegistertimeTime() {
		return this.xconf.getProperty("wx.app.templatemsg.property.registertime");
	}
	/**
	 * 微信小程序模板消息报名结果属性
	 * @return
	 */
	public String getWxappTemplatemsgPropertyResult() {
		return this.xconf.getProperty("wx.app.templatemsg.property.result");
	}
	/**
	 * 微信小程序模板消息报名结果属性值
	 * @return
	 */
	public String getWxappTemplatemsgPropertyResultValue() {
		return this.xconf.getProperty("wx.app.templatemsg.property.result.value");
	}
	/**
	 * 微信小程序模板消息活动名称属性
	 * @return
	 */
	public String getWxappTemplatemsgPropertyMeetingName() {
		return this.xconf.getProperty("wx.app.templatemsg.property.meetingname");
	}
	/**
	 * 微信小程序模板消息活动名称属性值
	 * @return
	 */
	public String getWxappTemplatemsgPropertyMeetingNameValue() {
		return this.xconf.getProperty("wx.app.templatemsg.property.meetingname.value");
	}
	/**
	 * 微信小程序模板消息备注属性
	 * @return
	 */
	public String getWxappTemplatemsgPropertyRemark() {
		return this.xconf.getProperty("wx.app.templatemsg.property.remark");
	}
	/**
	 * 微信小程序模板消息备注属性值
	 * @return
	 */
	public String getWxappTemplatemsgPropertyRemarkValue() {
		return this.xconf.getProperty("wx.app.templatemsg.property.remark.value");
	}
	/**
	 * 模板消息点击跳转页面
	 * @return
	 */
	public String getWxappTemplateMessagePage() {
		return this.xconf.getProperty("wx.app.template.message.page");
	}
	/**
	 * 微信小程序模板消息id
	 * @return
	 */
	public String getWxappTemplateMessageId() {
		return this.xconf.getProperty("wx.app.template.message.id");
	}
	
}
