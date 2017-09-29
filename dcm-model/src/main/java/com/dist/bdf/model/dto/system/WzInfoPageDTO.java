package com.dist.bdf.model.dto.system;

import java.io.Serializable;

/**
 * 
 * 分页请求微作的信息
 * @author weifj
 *
 */
public class WzInfoPageDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String realm;
	private String caseId;
	private int pageNo;
	private int pageSize;
	
	/**
	 * 域
	 * @return
	 */
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
	/**
	 * 案例id
	 * @return
	 */
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	/**
	 * 页码
	 * @return
	 */
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	/**
	 * 每页大小
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
