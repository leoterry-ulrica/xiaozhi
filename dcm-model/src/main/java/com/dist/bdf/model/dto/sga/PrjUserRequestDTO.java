package com.dist.bdf.model.dto.sga;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.dist.bdf.model.entity.sga.SgaPrjUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * 
 * 报名项目的请求参数
 * @author weifj
 *
 */
public class PrjUserRequestDTO extends SgaPrjUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotEmpty(message = "PrjUserRequestDTO property [caseId] can not be empty")
	private String caseId;
	/**
	 * 企业序列id
	 */
	@NotNull(message = "PrjUserRequestDTO property [companyId] can not null")
	private Long companyId;
	
	@JsonIgnore
	public Long getId() {
		return null;
	}
	
	@JsonIgnore
	public Long getPid() {
		return null;
	}

	@JsonIgnore
	public Date getCreateTime() {
		return null;
	}
	@JsonIgnore
	public Integer getStatus() {
		return -1;
	}
	@JsonIgnore
	public String getDescription() {
		return "";
	}
	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	@Override
	public String toString() {
		return "PrjUserRequestDTO [caseId=" + caseId + ", companyId=" + companyId + "]";
	}
}
