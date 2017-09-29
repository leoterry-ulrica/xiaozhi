package com.dist.bdf.model.dto.sga;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 项目信息更新请求
 * 目前提供合作名称、标签、海报、状态和描述更新
 * @author weifj
 *
 */
public class ProjectUpdateDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 案例id，作为查找标识
	 */
	@NotEmpty(message = "caseId can not be empty...")
	private String caseId;
	/**
	 * 项目名称
	 */
	@NotEmpty(message = "name can not be empty...")
	private String name;
	/**
	 * 项目标签
	 */
	private String tag;
	/**
	 * 	项目简介
	 * @return
	 */
	private String description;
	/**
	 * 项目状态。0：关闭；1：招募中；2：合作中；3：合作结束
	 */
	private int status;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "ProjectUpdateDTO [caseId=" + caseId + ", name=" + name + ", tag=" + tag + ", description=" + description
				+ ", status=" + status + "]";
	}
	
}
