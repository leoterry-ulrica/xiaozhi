package com.dist.bdf.model.dto.system;

import java.io.Serializable;
/**
 * 项目汇总
 * @author weifj
 *
 */
public class ProjectSummaryDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 项目guid
	 */
	private String caseId;
	/**
	 * 项目编码
	 */
	private String caseCode;
	/**
	 * 项目名称
	 */
	private String name;
	/**
	 * 项目组成员个数
	 */
	private Integer memberCount;
	
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getCaseCode() {
		return caseCode;
	}
	public void setCaseCode(String caseCode) {
		this.caseCode = caseCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getMemberCount() {
		return memberCount;
	}
	public void setMemberCount(Integer memberCount) {
		this.memberCount = memberCount;
	}
	@Override
	public String toString() {
		return "ProjectSummaryDTO [caseId=" + caseId + ", caseCode=" + caseCode + ", name=" + name + ", memberCount="
				+ memberCount + "]";
	}
}
