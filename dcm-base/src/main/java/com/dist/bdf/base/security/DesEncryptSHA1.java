package com.dist.bdf.base.security;

import java.security.MessageDigest;
import java.util.Date;

import com.dist.bdf.base.utils.StringUtil;

public class DesEncryptSHA1 implements DesEncryptAdapter {

	private MessageDigest digest;  
	 
	 /**
     * 获取适配器实例
     * @return
     */
    public static DesEncryptAdapter getInstance(){
    	
    	return InnerDesEncryptAdapter.INSTANCE;
    }
    
    static class InnerDesEncryptAdapter {
    	
    	private final static DesEncryptAdapter INSTANCE = new DesEncryptSHA1();
    }
    
    
	@Override
	public String encrypt(String text) {

		try {
			digest = MessageDigest.getInstance("SHA-1");
			String strDes = null;
			byte[] bt = text.getBytes();
			digest.update(bt);
			strDes = StringUtil.byte2hex(digest.digest());
			return strDes;
		} catch (Exception e) {
			throw new InternalError("init MessageDigest error:" + e.getMessage());
		}
	}

	@Override
	public String decrypt(String text) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) {
		
		DesEncryptSHA1 sha = (DesEncryptSHA1) DesEncryptSHA1.getInstance();
		
	    Date date = new Date();
		
		long timestamp = date.getTime();
		long expires = timestamp + 7*24*60*60*1000; // 有效期：7天
		String token = "3F7D1F21-B63F-39DF-6E03-6C6A21900581";
		String signature = "timestamp="+timestamp+"&expires="+expires+"&access_token="+token;
		System.out.println("加密后："+sha.encrypt(signature));
		System.out.println("解密后："+sha.decrypt(sha.encrypt(signature)));
	}
	

}
