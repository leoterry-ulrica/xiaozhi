package com.dist.bdf.model.dto.system;

import java.io.Serializable;
/**
 * 热门项目汇总模型
 * @author weifj
 *
 */
public class HotProjectSummaryDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * case id
	 */
	private String caseId;
	/**
	 * case名称
	 */
	private String name;
	/**
	 * 项目阶段
	 */
	private String stage;
	/**
	 * 参与人数
	 */
	private Integer memberCount;
	/**
	 * 任务个数
	 */
	private Integer taskCount;
	/**
	 * 提交成果个数
	 */
	private Integer docCount;
	/**
	 * 项目背景图url
	 */
	private String imgURL;
	
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	public Integer getMemberCount() {
		return memberCount;
	}
	public void setMemberCount(Integer memberCount) {
		this.memberCount = memberCount;
	}
	public Integer getTaskCount() {
		return taskCount;
	}
	public void setTaskCount(Integer taskCount) {
		this.taskCount = taskCount;
	}
	public Integer getDocCount() {
		return docCount;
	}
	public void setDocCount(Integer docCount) {
		this.docCount = docCount;
	}
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgURL() {
		return imgURL;
	}
	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}
	@Override
	public String toString() {
		return "HotProjectSummaryDTO [caseId=" + caseId + ", name=" + name + ", stage=" + stage + ", memberCount="
				+ memberCount + ", taskCount=" + taskCount + ", docCount=" + docCount + ", imgURL=" + imgURL + "]";
	}
}
