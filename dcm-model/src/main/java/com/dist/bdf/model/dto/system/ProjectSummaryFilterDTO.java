package com.dist.bdf.model.dto.system;

import java.io.Serializable;
/**
 * 项目汇总过滤信息
 * @author weifj
 *
 */
public class ProjectSummaryFilterDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 域
	 */
	private String realm;
	/**
	 * 用户编码
	 */
	private String userCode;
	/**
	 * 每页大小
	 */
	private int pageSize;
	/**
	 * 页码
	 */
	private int pageNo;
	
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
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

	/**
	 * 获取 域
	 * @return the realm
	 */
	public String getRealm() {
		return realm;
	}
	/**
	 * 设置 域
	 * @param realm the realm to set
	 */
	public void setRealm(String realm) {
		this.realm = realm;
	}
}
