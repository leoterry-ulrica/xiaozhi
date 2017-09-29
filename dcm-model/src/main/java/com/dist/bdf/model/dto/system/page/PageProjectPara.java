package com.dist.bdf.model.dto.system.page;

import java.io.Serializable;

/**
 * 项目分页信息
 * @author weifj
 * @version 1.0，2016/04/14，weifj，创建
 */
public class PageProjectPara implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer pageNo;
	private Integer pageSize;
	private String loginName;
	private String keyword;
	/**
	 * 域，如thupdi
	 */
	private String realm;
	/**
	 * 用户编码
	 */
	private String userCode;
	
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	/**
	 * 页码索引
	 * @return
	 */
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	/**
	 * 页码大小
	 * @return
	 */
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * 用户登录名，为了过滤不属于它的项目
	 * @return
	 */
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
}
