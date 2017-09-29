package com.dist.bdf.base.security;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class EncoderUtil {
	
	/**
	 * url编码
	 * 
	 * @param s
	 *            待编码的字符串
	 * @return 编码后的字符串
	 * @throws UnsupportedEncodingException
	 */
	public static String urlEncode(String s) throws UnsupportedEncodingException {
		return urlEncode(s, SecurityDefine.Charset);
	}

	/**
	 * url编码
	 * 
	 * @param s
	 *            待编码的字符串
	 * @param charsetName
	 *            编码使用的字符集
	 * @return 编码后的字符串
	 * @throws UnsupportedEncodingException
	 */
	public static String urlEncode(String s, String charsetName) throws UnsupportedEncodingException {
		if (s == null || s.isEmpty())
			return "";
		return URLEncoder.encode(s, charsetName);
	}

	/**
	 * base64编码
	 * 
	 * @param s
	 *            待编码的字符串
	 * @return 编码后的字符串
	 * @throws UnsupportedEncodingException
	 */
	public static String base64Encode(String s) throws UnsupportedEncodingException {
		return base64Encode(s, SecurityDefine.Charset);
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
	public static String base64Encode(String s, String charsetName) throws UnsupportedEncodingException {
		if (s == null || s.isEmpty())
			return "";
		return new String(new Base64().encode(s.getBytes(charsetName)));
	}

	/**
	 * 将字符串编码成ldap可识别的字符串
	 * 
	 * @param s
	 *            待编码的字符串
	 * @return 编码后的字符串
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String ldapEncode(String s) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		return ldapEncode(s, SecurityDefine.Charset);
	}

	/**
	 * 将字符串编码成ldap可识别的字符串
	 * 
	 * @param s
	 *            待编码的字符串
	 * @param charsetName
	 *            编码使用的字符集
	 * @return 编码后的字符串
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String ldapEncode(String s, String charsetName)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		if (s == null || s.isEmpty())
			return "";
		MessageDigest md = MessageDigest.getInstance("SHA");
		byte[] digest = md.digest(s.getBytes(charsetName));

		Base64 encoder = new Base64();
		return "{SHA}" + new String(encoder.encode(digest));
	}

	public static String aes256(String content) {
		return aes256Base64(content);
	}

	public static String aes256hex(String content) {
		byte[] encryptData = aes256(SecurityDefine.AESKey.getBytes(), content.getBytes());
		return Parse.byte2hex(encryptData);
	}
	public static String aes256Base64(String content) {
		byte[] encryptData = aes256(SecurityDefine.AESKey.getBytes(), content.getBytes());
		Base64 encoder = new Base64();
		return new String(encoder.encode(encryptData));
	}
	public static byte[] aes256(byte[] contentArray) {
		SecretKeySpec secretKeySpec = new SecretKeySpec(SecurityDefine.AESKey.getBytes(), "AES");
		try {
			Cipher cipher = Cipher.getInstance(secretKeySpec.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			byte[] result = cipher.doFinal(contentArray);
			return result;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	public static byte[] aes256(byte[] keyArray, byte[] contentArray) {
		SecretKeySpec secretKeySpec = new SecretKeySpec(keyArray, "AES");
		try {
			Cipher cipher = Cipher.getInstance(secretKeySpec.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			byte[] result = cipher.doFinal(contentArray);
			return result;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	public static String md5(String content) {
		try {
			return Parse.byte2hex(md5(content.getBytes(SecurityDefine.Charset)));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	public static byte[] md5(byte[] contentArray) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return md.digest(contentArray);
		} catch (NoSuchAlgorithmException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	public static String sha(String content) {
		try {
			return Parse.byte2hex(sha(content.getBytes(SecurityDefine.Charset)));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	public static byte[] sha(byte[] contentArray) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			return md.digest(contentArray);
		} catch (NoSuchAlgorithmException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
}
