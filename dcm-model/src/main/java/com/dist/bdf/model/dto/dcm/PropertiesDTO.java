package com.dist.bdf.model.dto.dcm;

import java.io.Serializable;

public class PropertiesDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String realm;
	private String userId;
	private String password;
	private String parentFolderId;
	/**
	 * 是否根目录。0：非根目录；1：根目录
	 */
	private Integer isRoot;
	private PropertiesExDTO propertiesEx;

	/**
	 * 获取用户标识符
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置用户标识符，现在使用用户在ldap中的dn
	 * @return
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getParentFolderId() {
		return parentFolderId;
	}

	public void setParentFolderId(String parentFolderId) {
		this.parentFolderId = parentFolderId;
	}

	public PropertiesExDTO getPropertiesEx() {
		return propertiesEx;
	}

	public void setPropertiesEx(PropertiesExDTO propertiesEx) {
		this.propertiesEx = propertiesEx;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public Integer getIsRoot() {
		return isRoot;
	}

	public void setIsRoot(Integer isRoot) {
		this.isRoot = isRoot;
	}
}
