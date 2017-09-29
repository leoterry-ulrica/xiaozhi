package com.dist.bdf.facade.service.security;

/**
 * 安全参数服务
 * @author weifj
 *
 */
public interface SecurityParaService {
	
	/**
	 * 获取公钥的模和指数（经过16进制编码）
	 * @return
	 */
	Object getDefaultPublicModulusAndExponentHex();

	/**
	 * 获取公钥的模（经过16进制编码）
	 * @return
	 */
	String getDefaultPublicModulusHex();
	/**
	 * 获取公钥的指数（经过16进制编码）
	 * @return
	 */
	String getDefaultPublicExponentHex();
}
