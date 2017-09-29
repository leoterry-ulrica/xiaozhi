package com.dist.bdf.model.dto.system;

import java.io.Serializable;

public class ShareObjectDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object entity;
	private String[] privCodes;
	
	public Object getEntity() {
		return entity;
	}
	public void setEntity(Object entity) {
		this.entity = entity;
	}
	public String[] getPrivCodes() {
		return privCodes;
	}
	public void setPrivCodes(String[] privCodes) {
		this.privCodes = privCodes;
	}
}
