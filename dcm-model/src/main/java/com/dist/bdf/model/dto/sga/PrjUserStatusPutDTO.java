package com.dist.bdf.model.dto.sga;

import java.io.Serializable;

/**
 * 设置用户在合作项目中的状态
 * @author weifj
 *
 */
public class PrjUserStatusPutDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String caseId;
	private Long userId;
	/**
	 * 用户在项目中的状态。-2：未报名；-1：拒绝；0：待审核；1：参与；2：待定
	 */
	private Integer status;
	
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "PrjUserStatusPutDTO [caseId=" + caseId + ", userId=" + userId + ", status=" + status + "]";
	}
}
