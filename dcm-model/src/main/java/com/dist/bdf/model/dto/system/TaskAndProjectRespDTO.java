package com.dist.bdf.model.dto.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.dist.bdf.base.office.excel.Excel;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 任务和项目信息响应信息
 * @author weifj
 *
 */
public class TaskAndProjectRespDTO implements Serializable, Comparable<TaskAndProjectRespDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 序号，用于导出excel之用
	 */
	@Excel(name = "序号", skip = true)
	private Integer index;
	@Excel(skip = true)
	private String taskId;
	/**
	 * 省
	 */
	@Excel(name = "省", width = 10)
	private String province;
	/**
	 * 市
	 */
	@Excel(name = "市", width = 10)
	private String city;
	/**
	 * 区
	 */
	@Excel(name = "县", width = 10)
	private String county;
	/**
	 * 委托单位
	 */
	@Excel(name = "客户名称", width = 15)
	private String delegateUnit;
	/**
	 * 项目名称
	 */
	@Excel(name = "项目名称", width = 20)
	private String projectName;
	/**
	 * 业务类型
	 */
	@Excel(name = "业务类型", width = 20)
	private String businessType;
	/**
	 * 牵头部门
	 */
	@Excel(skip = true)
	private String orgMaster;
	/**
	 * 主要配合部门
	 */
	@Excel(skip = true)
	private String orgCoo;
	/**
	 * 其他配合部门
	 */
	@Excel(skip = true)
	private String orgOtherCoo;
	@Excel(name = "相关部门")
	private List<String> orgRef = new ArrayList<String>();
	/**
	 * 项目经理
	 */
	@Excel(name = "负责人")
	private String[] projectManager;
	/**
	 * 项目助理
	 */
	@Excel(name = "助理")
	private String[] projectAssistant;
	@Excel(name = "任务目标")
	private String target;
	@Excel(name = "客户关注点")
	private String focus;
	@Excel(name = "任务类型")
	private String type;
	@Excel(name = "受众")
	private String audience;
	@Excel(name = "具体说明")
	private String content;
	@Excel(name = "现场")
	private Integer localSupport;
	@Excel(name = "模板")
	private Integer solutionTemp;
	@Excel(name = "硬件")
	private Integer hardwareReq; 
	@Excel(name = "开始时间", dateFormat = "yyyy-MM-dd")
	private Date beginTime;
	@Excel(name = "结束时间", dateFormat = "yyyy-MM-dd")
	private Date endTime;
	@Excel(name = "发起时间")
	private Date createTime;
	@Excel(name = "预期人员")
	private String expectPeople;
	@Excel(name = "负责人反馈")
	private String headFeedback;
	@Excel(name = "客户反馈")
	private String customerFeedback;
	/**
	 * 响应时间
	 */
	@Excel(name = "响应时间")
	private Date responseTime;
	/**
	 * 任务状态：
	 * 待接受，"585d68"
	 * 进行中，"0032FF"
	 * 已完成，"00C800"
	 * 停止，"FF6432"
	 */
	@Excel(name = "任务状态")
	private String status;
	/**
	 * 任务@的人
	 */
	@Excel(name = "执行人")
	private Object[] associatePerson;
	/**
	 * 项目唯一id
	 */
	@Excel(skip = true)
	private String caseId;
	/**
	 * 任务发布者，CE中微作的创建者
	 */
	@Excel(name = "发布者")
	private String publisher;
	@Excel(name = "发布时间")
	private Date publishTime; 
	@Excel(name = "任务评价")
	private String evaluation;
	
	/************************************************************
	 *                                   项目信息
	 ***********************************************************/
	/**
	 * 项目区域
	 */
	@Excel(skip = true)
	private String region;

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
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String[] getProjectManager() {
		return projectManager;
	}
	public void setProjectManager(String[] projectManager) {
		this.projectManager = projectManager;
	}
	public String[] getProjectAssistant() {
		return projectAssistant;
	}
	public void setProjectAssistant(String[] projectAssistant) {
		this.projectAssistant = projectAssistant;
	}
	public Object[] getAssociatePerson() {
		return associatePerson;
	}
	public void setAssociatePerson(Object[] associatePerson) {
		this.associatePerson = associatePerson;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * 获取任务内容
	 * @return
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置任务内容
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getDelegateUnit() {
		return delegateUnit;
	}
	public void setDelegateUnit(String delegateUnit) {
		this.delegateUnit = delegateUnit;
	}
	@JsonIgnore
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	@JsonIgnore
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	@JsonIgnore
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public Date getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}
	public String getOrgMaster() {
		return orgMaster;
	}
	public void setOrgMaster(String orgMaster) {
		this.orgMaster = orgMaster;
		if(!StringUtils.isEmpty(orgMaster)) {
			this.orgRef.add(orgMaster);
		}
	}
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getOrgCoo() {
		return orgCoo;
	}
	public void setOrgCoo(String orgCoo) {
		this.orgCoo = orgCoo;
		if(!StringUtils.isEmpty(orgCoo)) {
			this.orgRef.add(orgCoo);
		}
		// this.orgRef =  new String[]{ this.orgMaster, this.orgCoo, this.orgOtherCoo }; 
	}
	public String getOrgOtherCoo() {
		return orgOtherCoo;
	}
	public void setOrgOtherCoo(String orgOtherCoo) {
		this.orgOtherCoo = orgOtherCoo;
		if(!StringUtils.isEmpty(orgOtherCoo)) {
			this.orgRef.add(orgOtherCoo);
		}
		// this.orgRef = new String[]{ this.orgMaster, this.orgCoo, this.orgOtherCoo }; 
	}
	@JsonIgnore
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	@JsonIgnore
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取 #{bare_field_comment} 
	 * @return the orgRef
	 */
	public List<String> getOrgRef() {
		return this.orgRef;
	}
	/**
	 * 设置 #{bare_field_comment}
	 * @param orgRef the orgRef to set
	 */
	public void setOrgRef(List<String> orgRef) {
		// this.orgRef =  new String[]{ this.orgMaster, this.orgCoo, this.orgOtherCoo }; 
		this.orgRef = orgRef;
	}
	@Override
	public int compareTo(TaskAndProjectRespDTO targetTask) {
		if(null == targetTask){
			return -1;
		}
	    return  this.getCreateTime().compareTo((targetTask).getCreateTime());
	}
	/**
	 * 获取 #{bare_field_comment} 
	 * @return the publisher
	 */
	public String getPublisher() {
		return publisher;
	}
	/**
	 * 设置 #{bare_field_comment}
	 * @param publisher the publisher to set
	 */
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	/**
	 * 获取 #{bare_field_comment} 
	 * @return the publishTime
	 */
	public Date getPublishTime() {
		return publishTime;
	}
	/**
	 * 设置 #{bare_field_comment}
	 * @param publishTime the publishTime to set
	 */
	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}
	public String getEvaluation() {
		return evaluation;
	}
	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}

}
