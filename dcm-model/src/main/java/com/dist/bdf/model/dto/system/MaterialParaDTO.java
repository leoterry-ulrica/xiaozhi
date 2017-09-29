package com.dist.bdf.model.dto.system;

import java.io.Serializable;

/**
 * 
 * @author weifj
 * @version 1.0，2016/05/17，weifj，创建
 */
public class MaterialParaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 域
	 */
	private String realm;
	private String user;
	private String[] ids;
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String[] getIds() {
		return ids;
	}
	public void setIds(String[] ids) {
		this.ids = ids;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}

	
}
