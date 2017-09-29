
package com.dist.bdf.base.security;

/**
 * @author weifj
 * @version 1.0，216/03/23，weifj，创建加密解密适配器
 */
public interface DesEncryptAdapter {


	/**
	 * 加密
	 * @param text
	 * @return
	 */
	String encrypt(String text);
	/**
	 * 解密
	 * @param text
	 * @return
	 */
	String decrypt(String text);

}
