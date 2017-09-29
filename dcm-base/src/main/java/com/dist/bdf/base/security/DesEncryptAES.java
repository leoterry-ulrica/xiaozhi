package com.dist.bdf.base.security;

import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES only supports key sizes of 16, 24 or 32 bytes
 * @author weifj
 *
 */
public class DesEncryptAES implements DesEncryptAdapter {

	private static final String key = "DIST_XDATA_AES_0";

	private DesEncryptAES() {
	}

	/**
	   * 获取适配器实例
	   * @return
	   */
	public static DesEncryptAdapter getInstance() {

		return InnerDesEncryptAdapter.INSTANCE;
	}

	static class InnerDesEncryptAdapter {

		private final static DesEncryptAdapter INSTANCE = new DesEncryptAES();
	}

	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 * @param password
	 *            加密密码
	 * @return
	 */
	public byte[] encrypt(byte[] data, byte[] key) {

		if (key.length != 16) {
			throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
		}
		try {
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.ENCRYPT_MODE, seckey);// 初始化
			byte[] result = cipher.doFinal(data);
			return result; // 加密
		} catch (Exception e) {
			throw new RuntimeException("encrypt fail!", e);
		}
	}

	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @param password
	 *            解密密钥
	 * @return
	 */
	public byte[] decrypt(byte[] data, byte[] key) {

		if (key.length != 16) {
			throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
		}
		try {
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, seckey);// 初始化
			byte[] result = cipher.doFinal(data);
			return result; // 加密
		} catch (Exception e) {
			throw new RuntimeException("decrypt fail!", e);
		}
	}

	public String encryptToBase64(String data, String key) {
		try {
			byte[] valueByte = encrypt(data.getBytes(SecurityDefine.Charset), key.getBytes(SecurityDefine.Charset));
			return new String(new Base64().encode(valueByte));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("encrypt fail!", e);
		}

	}

	public String decryptFromBase64(String data, String key) {
		try {
			byte[] originalData = new Base64().decode(data.getBytes());
			byte[] valueByte = decrypt(originalData, key.getBytes(SecurityDefine.Charset));
			return new String(valueByte, SecurityDefine.Charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("decrypt fail!", e);
		}
	}

	public byte[] genarateRandomKey() {
		KeyGenerator keygen = null;
		try {
			keygen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(" genarateRandomKey fail!", e);
		}
		SecureRandom random = new SecureRandom();
		keygen.init(random);
		Key key = keygen.generateKey();
		return key.getEncoded();
	}

	public String genarateRandomKeyWithBase64() {
		return new String(new Base64().encode(genarateRandomKey()));
	}

	@Override
	public String encrypt(String text) {

		return this.encryptToBase64(text, key);
	}

	@Override
	public String decrypt(String text) {

		return this.decryptFromBase64(text, key);
	}
	/**
	 * 获取key
	 * @return
	 */
	public String getKey() {
		
		return key;
	}
	public static void main(String[] args) {

		String data = "pass123";
		System.out.println("data原文：" + data);
		String en = DesEncryptAES.getInstance().encrypt(data);
		System.out.println("aes加密：" + en);
		System.out.println("aes解密：" + DesEncryptAES.getInstance().decrypt(en));
	}
}
