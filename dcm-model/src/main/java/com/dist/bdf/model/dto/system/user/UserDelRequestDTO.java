package com.dist.bdf.model.dto.system.user;

import java.io.Serializable;

public class UserDelRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 需要被删除的用户序列id集合
	 */
	private Long[] userIds;
	/**
	 * 机构域
	 */
	private String realm;
	
	public Long[] getUserIds() {
		return userIds;
	}
	public void setUserIds(Long[] userIds) {
		this.userIds = userIds;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
}
