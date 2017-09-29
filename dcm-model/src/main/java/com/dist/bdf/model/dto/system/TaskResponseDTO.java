package com.dist.bdf.model.dto.system;

import java.io.Serializable;
import java.util.Date;

public class TaskResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String taskId;
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
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
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
}
