package com.dist.bdf.base.aliyun;

import org.apache.commons.httpclient.Header;  
import org.apache.commons.httpclient.HttpClient;  
import org.apache.commons.httpclient.NameValuePair;  
import org.apache.commons.httpclient.methods.PostMethod;

public class SendMsg_webchinese {

	public static void main(String[] args) throws Exception {

		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://gbk.api.smschinese.cn/");
		post.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=gbk");// 在头文件中设置转码
		NameValuePair[] data = { new NameValuePair("Uid", "leoterry"), // 注册的用户名
				new NameValuePair("Key", "054f6b06729e45c9d6ac"), // 注册成功后,登录网站使用的密钥
				new NameValuePair("smsMob", "13482029423"), // 手机号码
				new NameValuePair("smsText", "你的验证码是：123456") };//设置短信内容		
		post.setRequestBody(data);

		client.executeMethod(post);
		Header[] headers = post.getResponseHeaders();
		int statusCode = post.getStatusCode();
		System.out.println("statusCode:" + statusCode);
		for (Header h : headers) {
			System.out.println(h.toString());
		}
		String result = new String(post.getResponseBodyAsString().getBytes(
				"gbk"));
		System.out.println(result);
		post.releaseConnection();
	}
}
