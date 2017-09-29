package com.dist.bdf.model.dto.system;

import java.io.Serializable;

public class ShareObjectSimpleDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String domainCode;
	private String[] privCodes;
	
	public ShareObjectSimpleDTO() {
		
	}
	
    public ShareObjectSimpleDTO(String domainCode, String[] privCodes) {
		
    	this.domainCode = domainCode;
    	this.privCodes = privCodes;
    	
	}
	public String[] getPrivCodes() {
		return privCodes;
	}
	public void setPrivCodes(String[] privCodes) {
		this.privCodes = privCodes;
	}
	public String getDomainCode() {
		return domainCode;
	}
	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}
}
