package com.dist.bdf.model.dto.system.page;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class PageSimple implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotNull(message = "PageSimple property [realm] cannot be null")
	private String realm;
	private int pageNo;
	private int pageSize;
	@NotNull(message = "PageSimple property [user] cannot be null")
	private String user;
	private long scope;
	
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	/**
	 * 范围（例如搜索范围）
	 * 在获取我的共享中，0：过期共享；1：有效共享；2：包括过期和有效的
	 * @return
	 */
	public long getScope() {
		return scope;
	}
	public void setScope(long scope) {
		this.scope = scope;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
}
