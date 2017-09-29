
package com.dist.bdf.base.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.dist.bdf.base.security.DesEncryptDap.InnerDesEncryptAdapter;

/**
 * MD5加密，不提供解密，不能实现序列化接口，否则破坏单例模式
 * @author weifj
 * @version 1.0,2016/03/23，weifj，创建md5加密解密接口
 * @version 1.1，2016/03/30，weifj，添加属性adapter的类型：volatile
 *
 */
public class DesEncryptMD5 implements DesEncryptAdapter {


	private DesEncryptMD5() {

	}

	 /**
	    * 获取适配器实例
	    * @return
	    */
	   public static DesEncryptAdapter getInstance(){
	   	
	   	return InnerDesEncryptAdapter.INSTANCE;
	   }
	   
	   static class InnerDesEncryptAdapter {
	   	
	   	private final static DesEncryptAdapter INSTANCE = new DesEncryptMD5();
	   }

	@Override
	public String encrypt(String text) {

		if (null == text) {
			return text;
		}
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(text.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			return buf.toString().toUpperCase();// 32位的加密

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
		return text;
		
	}

	@Override
	public String decrypt(String text) {

		return text;
	}
	
	public static void main(String[] args) {
		
		DesEncryptMD5 md5 = new DesEncryptMD5();
		
		String encrypt = md5.encrypt("pass123");
		System.out.println("加密后："+encrypt);
		
		
		String decrypt = md5.decrypt(encrypt);
		decrypt = md5.decrypt(decrypt);
		System.out.println("解密后："+decrypt);
		
	}

}
