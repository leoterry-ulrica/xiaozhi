package com.dist.bdf.base.security;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dist.bdf.base.utils.StringUtil;

/**
 * 
 * base64加密解密
 * @author weifj
 *
 */
public class DesEncryptBase64 implements DesEncryptAdapter {
	
	private static Logger logger = LoggerFactory.getLogger(DesEncryptBase64.class);
	
	private DesEncryptBase64(){
		
	}
	
	 /**
     * 获取适配器实例
     * @return
     */
    public static DesEncryptAdapter getInstance(){
    	
    	return InnerDesEncryptAdapter.INSTANCE;
    }
    
    static class InnerDesEncryptAdapter {
    	
    	private final static DesEncryptAdapter INSTANCE = new DesEncryptBase64();
    }
    
	/**
	 * base64编码
	 * 
	 * @param s
	 *            待编码的字符串
	 * @param charsetName
	 *            编码使用的字符集
	 * @return 编码后的字符串
	 * @throws UnsupportedEncodingException
	 */
	private static String base64Encode(String s, String charsetName) throws UnsupportedEncodingException {
		
		if(StringUtil.isNullOrEmpty(s)) return "";
		
		return new String(new Base64().encode(s.getBytes(charsetName)));
	}
	
	private static String base64Decode(String s, String charsetName) throws UnsupportedEncodingException {
		
		if(StringUtil.isNullOrEmpty(s)) return "";
		
		return new String(new Base64().decode(s.getBytes(charsetName)));
	}
	
	@Override
	public String encrypt(String text) {

		try {
			return base64Encode(text, SecurityDefine.Charset);
		} catch (UnsupportedEncodingException e) {
		
			logger.error(e.getMessage());
		}
		return "";
	}

	@Override
	public String decrypt(String text) {
		
		try {
			return base64Decode(text, SecurityDefine.Charset);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}

		return "";
	}

	public static void main(String[] args) {
		
		DesEncryptBase64 base64 = (DesEncryptBase64) DesEncryptBase64.getInstance();
		System.out.println(base64.decrypt("cGFzczEyMw=="));
/*		String s = "123123";
		String en = base64.Encrypt(s);
		System.out.println("加密后："+en);
		
		String de = base64.Decrypt(en);
		System.out.println("解密后："+de);
		*/
		Date date = new Date();
		
		long timestamp = date.getTime();
		long expires = timestamp + 7*24*60*60*1000; // 有效期：7天
		String token = "3F7D1F21-B63F-39DF-6E03-6C6A21900581";
		String signature = "timestamp="+timestamp+"&expires="+expires+"&access_token="+token;
		System.out.println("加密后："+base64.encrypt(signature));
		System.out.println("解密后："+base64.decrypt(base64.encrypt(signature)));
		
	}
}
