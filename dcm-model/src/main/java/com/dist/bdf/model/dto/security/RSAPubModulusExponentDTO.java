package com.dist.bdf.model.dto.security;

import java.io.Serializable;
/**
 * RSA公钥的模和指数
 * @author weifj
 *
 */
public class RSAPubModulusExponentDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String modulus;
	private String exponent;
	
	public String getModulus() {
		return modulus;
	}
	public void setModulus(String modulus) {
		this.modulus = modulus;
	}
	public String getExponent() {
		return exponent;
	}
	public void setExponent(String exponent) {
		this.exponent = exponent;
	}
}
