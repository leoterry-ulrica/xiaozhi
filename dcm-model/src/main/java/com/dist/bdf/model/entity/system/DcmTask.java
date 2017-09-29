package com.dist.bdf.model.entity.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

@Entity
@Table(name = "DCM_TASK")
public class DcmTask extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String taskId;
	private String parentTaskId;
	private Date dateCreated;
	private String caseIdentifier;
	private String taskName;
	private Date beginTime;
	private Date endTime;
	private String target;
	private String focus;
	private String audience;
	private Integer hardwareReq; 
	private Integer solutionTemp;
	private Integer localSupport;
	private String headFeedback;
	private String customerFeedback;
	private String expectPeople;
	private String type;
	private Date responseTime;
	private String realm;
	
	/**
	 * 任务状态。585d68：待接受；0032FF：进行中；00C800：已完成；FF6432：停止
	 */
	private String status;
	/**
	 * 任务评价
	 */
	private String evaluation;
	
	@Column(name = "TASKID")
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	@Column(name = "PARENTTASKID")
	public String getParentTaskId() {
		return parentTaskId;
	}
	public void setParentTaskId(String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}
	
	@Column(name = "DATECREATED")
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	@Column(name = "CASEIDENTIFIER")
	public String getCaseIdentifier() {
		return caseIdentifier;
	}
	public void setCaseIdentifier(String caseIdentifier) {
		this.caseIdentifier = caseIdentifier;
	}
	
	@Column(name = "TASKNAME")
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getFocus() {
		return focus;
	}
	public void setFocus(String focus) {
		this.focus = focus;
	}
	public String getAudience() {
		return audience;
	}
	public void setAudience(String audience) {
		this.audience = audience;
	}
	public Integer getHardwareReq() {
		return hardwareReq;
	}
	public void setHardwareReq(Integer hardwareReq) {
		this.hardwareReq = hardwareReq;
	}
	public Integer getSolutionTemp() {
		return solutionTemp;
	}
	public void setSolutionTemp(Integer solutionTemp) {
		this.solutionTemp = solutionTemp;
	}
	public Integer getLocalSupport() {
		return localSupport;
	}
	public void setLocalSupport(Integer localSupport) {
		this.localSupport = localSupport;
	}
	public String getCustomerFeedback() {
		return customerFeedback;
	}
	public void setCustomerFeedback(String customerFeedback) {
		this.customerFeedback = customerFeedback;
	}
	public String getHeadFeedback() {
		return headFeedback;
	}
	public void setHeadFeedback(String headFeedback) {
		this.headFeedback = headFeedback;
	}
	public String getExpectPeople() {
		return expectPeople;
	}
	public void setExpectPeople(String expectPeople) {
		this.expectPeople = expectPeople;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}
	/**
	 * 获取 #{bare_field_comment} 
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * 设置 #{bare_field_comment}
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	public String getEvaluation() {
		return evaluation;
	}
	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}
	@Override
	public String toString() {
		return "DcmTask [taskId=" + taskId + ", parentTaskId=" + parentTaskId + ", dateCreated=" + dateCreated
				+ ", caseIdentifier=" + caseIdentifier + ", taskName=" + taskName + ", beginTime=" + beginTime
				+ ", endTime=" + endTime + ", target=" + target + ", focus=" + focus + ", audience=" + audience
				+ ", hardwareReq=" + hardwareReq + ", solutionTemp=" + solutionTemp + ", localSupport=" + localSupport
				+ ", headFeedback=" + headFeedback + ", customerFeedback=" + customerFeedback + ", expectPeople="
				+ expectPeople + ", type=" + type + ", responseTime=" + responseTime + ", status=" + status
				+ ", evaluation=" + evaluation + "]";
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
}
