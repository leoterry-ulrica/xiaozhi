package com.dist.bdf.base.aliyun;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dist.bdf.base.aliyun.constant.Constants;
import com.dist.bdf.base.aliyun.constant.HttpSchema;
import com.dist.bdf.base.aliyun.enums.Method;
/**
 * 
 * 发送信息
 * AppKey&AppSecret签名认证模式
 * @author weifj
 *
 */
public class SingleSendSms {

	private static String APP_KEY = "24632407"; //AppKey从控制台获取
	private static String APP_SECRET = "b46c71eb284407a1715f3563caf695f3"; //AppSecret从控制台获取
	private static String SIGN_NAME = "签名名称"; // 签名名称从控制台获取，必须是审核通过的
	private static String TEMPLATE_CODE = "模板CODE"; //模板CODE从控制台获取，必须是审核通过的
	private static String HOST = "sms.market.alicloudapi.com"; //API域名从控制台获取

	private final static String ERRORKEY = "errorMessage"; //返回错误的key

	/**
	 * 默认构造
	 */
	public SingleSendSms() {
		
	}
	/**
	 * 自定义构造
	 * @param appKey AppKey从阿里云管理端获取
	 * @param appSecret AppSecret从阿里云管理端获取
	 * @param signName 签名名称从阿里云管理端获取，必须是审核通过的
	 * @param templateCode 模板CODE从阿里云管理端获取，必须是审核通过的
	 */
    public SingleSendSms(String appKey, String appSecret, String signName, String templateCode) {
		SingleSendSms.APP_KEY = appKey;
		SingleSendSms.APP_SECRET = appSecret;
		SingleSendSms.SIGN_NAME = signName;
		SingleSendSms.TEMPLATE_CODE = templateCode;
	}
    /**
     * 
     * @param phoneNum 目标手机号，多个手机号可以逗号分隔;
     * @param params 短信模板中的变量，数字必须转换为字符串，如短信模板中变量为${no}",则参数params的值为{"no":"123456"}
     */
	public void sendMsg(String phoneNum, String params) {
		String path = "/singleSendSms";

		Request request = new Request(Method.GET, HttpSchema.HTTP + HOST, path, APP_KEY, APP_SECRET,
				Constants.DEFAULT_TIMEOUT);

		//请求的query
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("SignName", SIGN_NAME);
		querys.put("TemplateCode", TEMPLATE_CODE);
		querys.put("RecNum", phoneNum);
		querys.put("ParamString", params);
		request.setQuerys(querys);

		try {
			Map<String, String> bodymap = new HashMap<String, String>();
			Response response = Client.execute(request);
			//根据实际业务需要，调整对response的处理
			if (null == response) {
				System.out.println("no response");
			} else if (200 != response.getStatusCode()) {
				System.out.println("StatusCode:" + response.getStatusCode());
				System.out.println("ErrorMessage:" + response.getErrorMessage());
				System.out.println("RequestId:" + response.getRequestId());
			} else {
				bodymap = readResponseBodyContent(response.getBody());
				if (null != bodymap.get(ERRORKEY)) {
					//当传入的参数不合法时，返回有错误说明
					System.out.println(bodymap.get(ERRORKEY));
				} else {
					//成功返回map，对应的key分别为：message、success等
					System.out.println(JSON.toJSONString(bodymap));
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * 读取响应体内容
	 * @param body
	 * @return
	 */
	private Map<String, String> readResponseBodyContent(String body) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			JSONObject jsonObject = JSON.parseObject(body);
			if (null != jsonObject) {
				for (Entry<String, Object> entry : jsonObject.entrySet()) {
					map.put(entry.getKey(), entry.getValue().toString());
				}
			}
			if ("false".equals(map.get("success"))) {
				map.put(ERRORKEY, map.get("message"));
			}
		} catch (Exception e) {
			map.put(ERRORKEY, body);
		}
		return map;
	}

	public static void main(String agrs[]) {
		SingleSendSms app = new SingleSendSms();
		app.sendMsg("18600000000,13800000000", "{'name':'David'}");
	}
}
