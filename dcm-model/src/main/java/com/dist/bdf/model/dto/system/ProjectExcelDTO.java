package com.dist.bdf.model.dto.system;

import java.io.Serializable;
import java.util.Date;
import com.dist.bdf.base.office.excel.Excel;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 项目信息导出excel信息
 * @author weifj
 *
 */
public class ProjectExcelDTO implements Serializable {

	/**
	 * 
	 */
	@Excel(skip = true)
	private static final long serialVersionUID = 1L;

	@Excel(name = "项目名称", width = 20)
	private String projectName;
	@Excel(name = "项目编号", width = 20)
	private String projectCode;
	@Excel(name = "省", width = 10)
	private String province;
	@Excel(name = "市", width = 10)
	private String city;
	@Excel(name = "县", width = 10)
	private String county;
	@Excel(name = "业务类型", width = 20)
	private String businessType;
	@Excel(name = "是否中标")
	private Boolean zbjg;
	@Excel(name = "中标金额")
	private Double zbje;
	@Excel(name = "中标单位")
	private String zbdw;
	@Excel(name = "中标时间")
	private Date zbsj;
	@Excel(name = "项目经理")
	private String[] projectManager;
	@Excel(name = "项目助理")
	private String[] projectAssistant;
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
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
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public Double getZbje() {
		return zbje;
	}
	public void setZbje(Double zbje) {
		this.zbje = zbje;
	}
	public String getZbdw() {
		return zbdw;
	}
	public void setZbdw(String zbdw) {
		this.zbdw = zbdw;
	}
	public Date getZbsj() {
		return zbsj;
	}
	public void setZbsj(Date zbsj) {
		this.zbsj = zbsj;
	}
	public Boolean getZbjg() {
		return zbjg;
	}
	public void setZbjg(Boolean zbjg) {
		this.zbjg = zbjg;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
}
