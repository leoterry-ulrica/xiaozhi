package com.dist.bdf.model.dto.system;

import java.io.Serializable;
import java.util.List;

public class ProjectOrgMaskDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String orgName;
	private List<String> privCodes;
	
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public List<String> getPrivCodes() {
		return privCodes;
	}
	public void setPrivCodes(List<String> privCodes) {
		this.privCodes = privCodes;
	}

}
