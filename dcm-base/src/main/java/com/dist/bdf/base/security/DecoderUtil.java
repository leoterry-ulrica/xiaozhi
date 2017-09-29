package com.dist.bdf.base.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * 字符串解码
 * DAP4的加密解密策略
 * @author penggch
 * 
 */
public class DecoderUtil {
	/**
	 * url解码
	 * 
	 * @param s
	 *            待解码的字符串
	 * @return 解码后的字符串
	 * @throws UnsupportedEncodingException
	 */
	public static String urlDecode(String s)
			throws UnsupportedEncodingException {
		return urlDecode(s, SecurityDefine.Charset);
	}

	/**
	 * url解码
	 * 
	 * @param s
	 *            待解码的字符串
	 * @param charsetName
	 *            用于解码的字符集
	 * @return 解码后的字符串
	 * @throws UnsupportedEncodingException
	 */
	public static String urlDecode(String s, String charsetName)
			throws UnsupportedEncodingException {
		if (s == null || s.isEmpty())
			return "";
		return URLDecoder.decode(s, charsetName);
	}

	/**
	 * base64解码
	 * 
	 * @param s
	 *            待解码的字符串
	 * @return 解码后的字符串
	 * @throws IOException
	 */
	public static String base64Decode(String s) throws IOException {
		return base64Decode(s, SecurityDefine.Charset);
	}

	/**
	 * base64解码
	 * 
	 * @param s
	 *            待解码的字符串
	 * @param charsetName
	 *            用于解码的字符集
	 * @return 解码后的字符串
	 * @throws IOException
	 */
	public static String base64Decode(String s, String charsetName)
			throws IOException {
		if (s == null || s.isEmpty())
			return "";
		Base64 decoder = new Base64();
		byte[] bytes = decoder.decode(s.getBytes());
		return new String(bytes, charsetName);
	}

	public static String aes256(String content) {
		if(content==null) return null;
		byte[] bytes=null;
		try {
			bytes=Parse.hex2byte(content);
		}
		catch (Exception e)
		{
			Base64 decoder = new Base64();
			bytes = decoder.decode(content.getBytes());
		}
		byte[] decryptData = aes256(SecurityDefine.AESKey.getBytes(),
				bytes);
		return new String(decryptData);
	}
	
	public static byte[] aes256(byte[] contentArray) {
		SecretKeySpec secretKeySpec = new SecretKeySpec(SecurityDefine.AESKey.getBytes(), "AES");
		try {
			Cipher cipher = Cipher.getInstance(secretKeySpec.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
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
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			byte[] result = cipher.doFinal(contentArray);
			return result;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
}
