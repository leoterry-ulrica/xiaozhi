package com.dist.bdf.model.dto.dcm;

import java.io.Serializable;

/**
 * 
 * @author weifj
 * @version 1.0，2016/05/03，weifj，创建分页参数DTO
 */
public class PageParaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int pageSize;
	private int pageNo;
	private String keyword;
	private String searchType;
	private String realm;
	private String userId;
	private String password;
	/**
	 * 父文件夹id，用户分页检索子文件夹或者文档
	 */
	private String folderId;
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
	@Deprecated
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Deprecated
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFolderId() {
		return folderId;
	}
	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}
}
