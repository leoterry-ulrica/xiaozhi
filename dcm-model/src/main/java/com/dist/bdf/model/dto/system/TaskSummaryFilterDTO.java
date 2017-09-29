package com.dist.bdf.model.dto.system;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import com.dist.bdf.base.office.excel.Excel;
/**
 * 任务汇总过滤信息
 * @author weifj
 *
 */
public class TaskSummaryFilterDTO implements Serializable {

	/**
	 * 
	 */
	@Excel(skip = true)
	private static final long serialVersionUID = 1L;
	/**
	 * 域
	 */
	@Excel(skip = true)
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
	
	/**
	 * 项目编号
	 */
	private String[] projectNo;
	/**
	 * 业务类别
	 */
	private String[] businessType;
	/**
	 * 项目负责人
	 */
	private String[] prjManager;
	/**
	 * 项目助理
	 */
	private String[] prjAssistant;
	/**
	 * 任务类型
	 */
	private String[] taskType;
	/**
	 * 是否本地支持，0/1
	 */
	private Integer[] localSupport;
	/**
	 * 开始时间
	 */
	private Date beginTime;
	/**
	 * 结束时间
	 */
	private Date endTime;
	/**
	 * 任务状态
	 */
	private String[] status;
	/**
	 * 关联的人
	 */
	private String[] associatePerson;
	/**
	 * 发布者，微作的创建者
	 */
	private String[] publisher;
	/**
	 * 发布开始时间
	 */
	private Date publishBeginTime;
	/**
	 * 发布结束时间
	 */
	private Date publishEndTime;
	
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
	public String[] getProjectNo() {
		return projectNo;
	}
	public void setProjectNo(String[] projectNo) {
		this.projectNo = projectNo;
	}
	public String[] getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String[] businessType) {
		this.businessType = businessType;
	}
	public String[] getPrjManager() {
		return prjManager;
	}
	public void setPrjManager(String[] prjManager) {
		this.prjManager = prjManager;
	}
	public String[] getPrjAssistant() {
		return prjAssistant;
	}
	public void setPrjAssistant(String[] prjAssistant) {
		this.prjAssistant = prjAssistant;
	}
	public String[] getTaskType() {
		return taskType;
	}
	public void setTaskType(String[] taskType) {
		this.taskType = taskType;
	}
	public Integer[] getLocalSupport() {
		return localSupport;
	}
	public void setLocalSupport(Integer[] localSupport) {
		this.localSupport = localSupport;
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
	public String[] getStatus() {
		return status;
	}
	public void setStatus(String[] status) {
		this.status = status;
	}
	public String[] getAssociatePerson() {
		return associatePerson;
	}
	public void setAssociatePerson(String[] associatePerson) {
		this.associatePerson = associatePerson;
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
	/**
	 * 获取 发布者 
	 * @return the publisher
	 */
	public String[] getPublisher() {
		return publisher;
	}
	/**
	 * 设置 发布者
	 * @param publisher the publisher to set
	 */
	public void setPublisher(String[] publisher) {
		this.publisher = publisher;
	}
	public Date getPublishBeginTime() {
		return publishBeginTime;
	}
	public void setPublishBeginTime(Date publishBeginTime) {
		this.publishBeginTime = publishBeginTime;
	}
	public Date getPublishEndTime() {
		return publishEndTime;
	}
	public void setPublishEndTime(Date publishEndTime) {
		this.publishEndTime = publishEndTime;
	}
	@Override
	public String toString() {
		return "TaskSummaryFilterDTO [realm=" + realm + ", userCode=" + userCode + ", pageSize=" + pageSize
				+ ", pageNo=" + pageNo + ", projectNo=" + Arrays.toString(projectNo) + ", businessType="
				+ Arrays.toString(businessType) + ", prjManager=" + Arrays.toString(prjManager) + ", prjAssistant="
				+ Arrays.toString(prjAssistant) + ", taskType=" + Arrays.toString(taskType) + ", localSupport="
				+ Arrays.toString(localSupport) + ", beginTime=" + beginTime + ", endTime=" + endTime + ", status="
				+ Arrays.toString(status) + ", associatePerson=" + Arrays.toString(associatePerson) + ", publisher="
				+ Arrays.toString(publisher) + ", publishBeginTime=" + publishBeginTime + ", publishEndTime="
				+ publishEndTime + "]";
	}

}
