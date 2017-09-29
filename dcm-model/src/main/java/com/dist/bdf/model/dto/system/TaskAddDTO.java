package com.dist.bdf.model.dto.system;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 添加task的参数模型
 * @author weifj
 *
 */
public class TaskAddDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * case id
	 */
	@NotEmpty(message = "TaskAddDTO property [caseId] can not be empty")
	private String caseId;
	@NotEmpty(message = "TaskAddDTO property [taskId] can not be empty")
	private String taskId;
	/**
	 * 以毫秒为单位
	 */
	private Long beginTime;
	/**
	 * 以毫秒为单位
	 */
	private Long endTime;
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
	private String status;
	private String evaluation;
	private String realm;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public Long getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
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
	public String getHeadFeedback() {
		return headFeedback;
	}
	public void setHeadFeedback(String headFeedback) {
		this.headFeedback = headFeedback;
	}
	public String getCustomerFeedback() {
		return customerFeedback;
	}
	public void setCustomerFeedback(String customerFeedback) {
		this.customerFeedback = customerFeedback;
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
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
	
}
