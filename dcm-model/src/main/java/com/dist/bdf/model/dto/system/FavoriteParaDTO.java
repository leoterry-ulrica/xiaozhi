package com.dist.bdf.model.dto.system;

import java.io.Serializable;

public class FavoriteParaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private boolean isFavorite;
	private String user;
	private int type;
	/**
	 * 扩展属性，资源类型
	 */
	private String resType;
	private String realm;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean getIsFavorite() {
		return isFavorite;
	}
	public void setIsFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
	public String getResType() {
		return resType;
	}
	public void setResType(String resType) {
		this.resType = resType;
	}
	
}
