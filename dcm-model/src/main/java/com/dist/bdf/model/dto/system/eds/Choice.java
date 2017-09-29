package com.dist.bdf.model.dto.system.eds;

import java.io.Serializable;

public class Choice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String displayName;
	private String value;
	
	public Choice(String displayName, String value) {
		this.displayName = displayName;
		this.value = value;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
