package com.dist.bdf.manager.wechat;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.security.DesEncryptRSA;
import com.dist.bdf.base.utils.HttpRequestHelper;
import com.dist.bdf.model.dto.wechat.AccessToken;
import com.dist.bdf.model.dto.wechat.ErrorMsg;
import com.dist.bdf.model.dto.wechat.TemplateMsgData;
import com.dist.bdf.model.dto.wechat.UserInfo;
import com.dist.bdf.model.dto.wechat.WechatTemplate;

/**
 * 微信相关管理类
 * @author weifj
 *
 */
public class WechatManager {

	private static Logger LOG = LoggerFactory.getLogger(WechatManager.class);

	/**
	 * 通过微信提供的code换取网页授权access_token
	 * 
	 * @param accessTokenURI
	 * @param appId
	 * @param appSecret
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public static AccessToken getAccessTokenByAuthorizationCode(String accessTokenURI, String appId, String appSecret, String code)
			throws BusinessException {

		LOG.info(">>>获取到的微信token uri：" + accessTokenURI);
		LOG.info(">>>获取到的微信appId：" + appId);
		LOG.info(">>>获取到的微信appSecret：" + appSecret);

		String formatTokenURI = String.format(accessTokenURI, appId, appSecret, code);

		String result = HttpRequestHelper.sendGet(formatTokenURI);
		if (!StringUtils.hasLength(result))
			throw new BusinessException("通过code换取网页授权access_token失败。");

		AccessToken token = JSONObject.parseObject(result, AccessToken.class);
		if (40029 == token.getErrcode())
			throw new BusinessException("通过code换取网页授权access_token失败，errmsg：" + token.getErrmsg());

		return token;
	}
	/**
	 * 通过客户端验证方式获取token
	 * @param accessTokenURI
	 * @param appId
	 * @param appSecret
	 * @return
	 * @throws BusinessException
	 */
	public static AccessToken getAccessTokenByClientCredential(String accessTokenURI, String appId, String appSecret)
			throws BusinessException {

		LOG.info(">>>获取到的微信token uri：" + accessTokenURI);
		LOG.info(">>>获取到的微信appId：" + appId);
		LOG.info(">>>获取到的微信appSecret：" + appSecret);

		String formatTokenURI = String.format(accessTokenURI, appId, appSecret);

		String result = HttpRequestHelper.sendGet(formatTokenURI);
		if (!StringUtils.hasLength(result))
			throw new BusinessException("通过客户端验证换取网页授权access_token失败。");

		AccessToken token = JSONObject.parseObject(result, AccessToken.class);
		if (40013 == token.getErrcode())
			throw new BusinessException("通过code换取网页授权access_token失败，errmsg：" + token.getErrmsg());

		return token;
	}
	/**
	 * 通过微信API拉取用户信息(需scope为 snsapi_userinfo)
	 * @param userInfoURI
	 * @param accessToken
	 * @param openId
	 * @return
	 * @throws BusinessException
	 */
    public static UserInfo getWxUserInfo(String userInfoURI, String accessToken, String openId) throws BusinessException{
		
		LOG.info(">>>获取到的用户信息 uri：" + userInfoURI);
		
		String formatUserInfoURI = String.format(userInfoURI, accessToken, openId);

		String result = HttpRequestHelper.sendGet(formatUserInfoURI);
		if(!StringUtils.hasLength(result))
			throw new BusinessException("拉取用户信息失败。");
		
		UserInfo userInfo = JSONObject.parseObject(result, UserInfo.class);
		if(40003 == userInfo.getErrcode())
			throw new BusinessException("拉取用户信息失败，errmsg："+userInfo.getErrmsg());
		
		return userInfo;
	}
    /**
     * 发送消息模板
     * @param sendTempMsgURI
     * @param accessToken
     * @param wechatTemplate
     * @throws BusinessException
     */
    public static void sendTemplateMessage(String sendTempMsgURI, String accessToken, WechatTemplate wechatTemplate) throws BusinessException {      

        String requestUrl = String.format(sendTempMsgURI, accessToken);
       String resultJSON = HttpRequestHelper.sendPost(requestUrl, JSONObject.toJSONString(wechatTemplate));
        LOG.info(">>>响应结果：{}", resultJSON);    
        ErrorMsg errormsg = JSONObject.parseObject(resultJSON, ErrorMsg.class);
        
        if (0 == errormsg.getErrcode()) {  
        	LOG.info(">>>模板消息发送成功");  
        } else {  
        	String exception = String.format("模板消息发送失败，错误码：%s，错误信息：%s ", errormsg.getErrcode(), errormsg.getErrmsg());
            LOG.info(">>>{} ", exception);  
            throw new BusinessException(exception);
        }  
    }  
    
	public static void main(String[] args) {

		// 测试微信小程序发送模板消息
		AccessToken accessToken = WechatManager.getAccessTokenByClientCredential(
				"https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
				DesEncryptRSA.getInstance().decrypt(
						"80b7d258dda62a001ea9d6c3f52c9c107afa57de15f984ca62ae5c2d186a2341475da5ec3380206738e787718f66ec0cbcb9566f15d3ac4ecc58b306dd707fe609071de4ebf91484d5a774320a3d42f6f022f8988ab0c9de99446c611f13558ca1194b9ff875d228ba98bdb60bbb495f60c06cd179949d3ec2bd32b5c115995a"),
				DesEncryptRSA.getInstance().decrypt(
						"86f97a957adfffadd38eeaab7b4176fce6a0aeec38cea0a76c9e22eae789bf9ec9247c47f9e7f8c7298d570f3e6c0f0f8245f94c05098cef59705925d42740e09560396bce7c0366d0e18d4782be4bb1742e782f3cad99f98b78f80c956de654b251ef770bcd45620ad12f5c8f6c00ca93540e7f90d8df08d2417db9d59f91e7"));

		String sendTempMsgURI = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=%s";
		WechatTemplate templateData = new WechatTemplate();
		templateData.setTouser("olRIJ0Xlz9oRhthM7vLesuv4e48g");
		templateData.setTemplate_id("7FH2Wn5g40GaPRIrSQQnB6cslH6CHWGQO7Y3OXrUSrg");
		templateData.setForm_id("8acfd66d680b6a5c065efb0f9f567cee");
		templateData.setPage("/pages/enterprise_detail/enterprise_detail?realm=dist");
		Map<String, TemplateMsgData> m = new HashMap<String, TemplateMsgData>();

		TemplateMsgData people = new TemplateMsgData();
		people.setColor("#000000");
		people.setValue("杨龙");
		m.put("keyword1", people);
		TemplateMsgData address = new TemplateMsgData();
		address.setColor("#000000");
		address.setValue("张衡路1000弄");
		m.put("keyword2", address);
		TemplateMsgData meetingTime = new TemplateMsgData();
		meetingTime.setColor("#000000");
		meetingTime.setValue("2017年4月26日");
		m.put("keyword3", meetingTime);
		TemplateMsgData registerTime = new TemplateMsgData();
		registerTime.setColor("#000000");
		registerTime.setValue("2017年05月02日");
		m.put("keyword4", registerTime);
		TemplateMsgData registerResult = new TemplateMsgData();
		registerResult.setColor("#000000");
		registerResult.setValue("报名成功");
		m.put("keyword5", registerResult);
		TemplateMsgData meetingName = new TemplateMsgData();
		meetingName.setColor("#000000");
		meetingName.setValue("规划十一届规划论坛");
		m.put("keyword6", meetingName);
		TemplateMsgData remark = new TemplateMsgData();
		remark.setColor("#000000");
		remark.setValue("希望准时参加，谢谢！");
		m.put("keyword7", remark);
		// 装载数据源
		templateData.setData(m);

		WechatManager.sendTemplateMessage(sendTempMsgURI, accessToken.getAccess_token(), templateData);
	}
}
