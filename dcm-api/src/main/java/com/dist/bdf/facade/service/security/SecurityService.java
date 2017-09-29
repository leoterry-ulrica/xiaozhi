package com.dist.bdf.facade.service.security;

public interface SecurityService {

	/**
	 * RSA加密
	 * @param text
	 * @return
	 */
	String encryptRSA(String text);
	/**
	 * RSA解密
	 * @param text
	 * @return
	 */
	String decryptRSA(String text); 

}
