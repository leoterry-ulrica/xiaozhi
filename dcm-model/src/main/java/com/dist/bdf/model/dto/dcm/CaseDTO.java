
package com.dist.bdf.model.dto.dcm;

import java.util.Date;

import com.dist.bdf.base.dto.BaseDTO;

/**
 * @author weifj
 * @version 1.0，2016/03/21，weifj，创建案例dto
 *
 */
public class CaseDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String folderName;
	@Deprecated
	private long dateLastModified;
	private String caseIdentifier;
	private String lastModifier;
	private String creator;
	private String owner;
	private String caseState;
	private long dateCreated;
	private String name;
	private String projectName;
	private String projectCode;
	private String delegateUnit;
	/**
	 * 修改时间
	 */
	private long modifiedOn;
	/**
	 * 案例类型
	 */
	private String caseType;
	/**
	 * 类描述
	 */
	private String classDescription;
	/**
	 * 工程地址，XMXX_YBYX_MXZBGCDZ
	 */
	private String projectAddress;
	private String majorType;
	private String projectType;
	/**
	 * 项目区域（区域分类），对应ce属性【XMXX_XMQY_SHENG,XMXX_XMQY_SHI,XMXX_XMQY_XIAN】
	 */
	private String projectRegion;
	private String  managementLevel; 
	private String qualityGoal;
	private String safetyGoal;
	
	/**
	 * 所属机构（组织分类），对应ce属性【XMXX_XMQTBM】，牵头部门
	 */
	private String org;
	/**
	 * 主要配合部门，XZ_PHBM
	 */
	private String orgCoo;
	/**
	 * 其他配合部门，XZ_QTPHBM
	 */
	private String[] orgOtherCoo;
	/**
	 * 业务类型（规划类别，业务类型），对应ce属性【XMXX_XMGHLB】
	 */
	private String business;
	/**
	 * 项目负责人，XMXX_RY_XMFZR
	 */
	private String projectManager;
	/**
	 * 应标部门，XMXX_TB_YBBM
	 */
	private String departmentYB;
	/**
	 * 基本情况，XMXX_YBYX_XMJBQK
	 */
	private String basicInfo;
	/**
	 * 招标单位，XMXX_YBYX_ZBDW2
	 */
	private String unitZB;
	/**
	 * 招标机构，XMXX_YBYX_ZBJG
	 */
	private String orgZB;
	/**
	 * 项目规模，XZ_XMGM
	 */
	private String scale;
	/**
	 * 院外配合部门
	 */
	private String ywphbm;
	/**
	 * 项目状态
	 */
	private String projectStatus;
	/**
	 * 业务类别，XZ_XM_YWLB
	 */
	private String businessType;
	/**
	 * 项目助理，XZ_RY_XMZL
	 */
	private String projectAssistant;
	/**
	 * 省
	 */
	private String province;
	/**
	 * 市
	 */
	private String city;
	/**
	 * 区
	 */
	private String county;
	/**
	 * 中标金额
	 */
	private Double zbje;
	/**
	 * 中标结果
	 */
	private Boolean zbjg;
	/**
	 * 中标单位
	 */
	private String zbdw;
	/**
	 * 中标时间
	 */
	private Date zbsj;
	
	/**
	 * 文种类型，是通过当前文件所在目录的名称集合，
	 * 例如：文件在目录：/2基础资料/202保密资料，则文种类型的值：2基础资料,202保密资料
	 */
	
	/**
	 * @return the folderName
	 */
	public String getFolderName() {
		return folderName;
	}
	/**
	 * @param folderName the folderName to set
	 */
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	/**
	 * @return the dateLastModified
	 */
	public long getDateLastModified() {
		return dateLastModified;
	}
	/**
	 * @param dateLastModified the dateLastModified to set
	 */
	public void setDateLastModified(long dateLastModified) {
		this.dateLastModified = dateLastModified;
	}
	/**
	 * @return the caseIdentifier
	 */
	public String getCaseIdentifier() {
		return caseIdentifier;
	}
	/**
	 * @param caseIdentifier the caseIdentifier to set
	 */
	public void setCaseIdentifier(String caseIdentifier) {
		this.caseIdentifier = caseIdentifier;
	}
	/**
	 * @return the lastModifier
	 */
	public String getLastModifier() {
		return lastModifier;
	}
	/**
	 * @param lastModifier the lastModifier to set
	 */
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}
	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	/**
	 * @return the caseState
	 */
	public String getCaseState() {
		return caseState;
	}
	/**
	 * @param caseState the caseState to set
	 */
	public void setCaseState(String caseState) {
		this.caseState = caseState;
	}
	/**
	 * 毫秒
	 * @return the dateCreated
	 */
	public long getDateCreated() {
		return dateCreated;
	}
	/**
	 * @param dateCreated the dateCreated to set
	 */
	public void setDateCreated(long dateCreated) {
		this.dateCreated = dateCreated;
	}
	/**
	 * 案例名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 项目名称
	 * @return
	 */
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	/**
	 * 委托单位
	 * @return
	 */
	public String getDelegateUnit() {
		return delegateUnit;
	}
	public void setDelegateUnit(String delegateUnit) {
		this.delegateUnit = delegateUnit;
	}
	/**
	 * 工程地址
	 * @return
	 */
	public String getProjectAddress() {
		return projectAddress;
	}
	public void setProjectAddress(String projectAddress) {
		this.projectAddress = projectAddress;
	}
	/**
	 * 专业类型
	 * @return
	 */
	public String getMajorType() {
		return majorType;
	}
	public void setMajorType(String majorType) {
		this.majorType = majorType;
	}
	/**
	 * 项目类型
	 * @return
	 */
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	/**
	 * 项目区域
	 * @return
	 */
	public String getProjectRegion() {
		return projectRegion;
	}
	public void setProjectRegion(String projectRegion) {
		this.projectRegion = projectRegion;
	}
	/**
	 * 管理级别
	 * @return
	 */
	public String getManagementLevel() {
		return managementLevel;
	}
	public void setManagementLevel(String managementLevel) {
		this.managementLevel = managementLevel;
	}
	/**
	 * 质量目标
	 * @return
	 */
	public String getQualityGoal() {
		return qualityGoal;
	}
	@Deprecated
	public void setQualityGoal(String qualityGoal) {
		this.qualityGoal = qualityGoal;
	}
	/**
	 * 安全目标
	 * @return
	 */
	public String getSafetyGoal() {
		return safetyGoal;
	}
	@Deprecated
	public void setSafetyGoal(String safetyGoal) {
		this.safetyGoal = safetyGoal;
	}
	public String getOrg() {
		return org;
	}
	public void setOrg(String org) {
		this.org = org;
	}
	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
	}
	public String getProjectManager() {
		return projectManager;
	}
	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}
	public String getDepartmentYB() {
		return departmentYB;
	}
	@Deprecated
	public void setDepartmentYB(String departmentYB) {
		this.departmentYB = departmentYB;
	}
	public String getBasicInfo() {
		return basicInfo;
	}
	@Deprecated
	public void setBasicInfo(String basicInfo) {
		this.basicInfo = basicInfo;
	}
	public String getUnitZB() {
		return unitZB;
	}
	@Deprecated
	public void setUnitZB(String unitZB) {
		this.unitZB = unitZB;
	}
	public String getOrgZB() {
		return orgZB;
	}
	@Deprecated
	public void setOrgZB(String orgZB) {
		this.orgZB = orgZB;
	}
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public String getCaseType() {
		return caseType;
	}
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	public String getClassDescription() {
		return classDescription;
	}
	public void setClassDescription(String classDescription) {
		this.classDescription = classDescription;
	}
	public String getYwphbm() {
		return ywphbm;
	}
	public void setYwphbm(String ywphbm) {
		this.ywphbm = ywphbm;
	}

	public String getProjectStatus() {
		return projectStatus;
	}
	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}
	public long getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(long modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getProjectAssistant() {
		return projectAssistant;
	}
	public void setProjectAssistant(String projectAssistant) {
		this.projectAssistant = projectAssistant;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getOrgCoo() {
		return orgCoo;
	}
	public void setOrgCoo(String orgCoo) {
		this.orgCoo = orgCoo;
	}
	public String[] getOrgOtherCoo() {
		return orgOtherCoo;
	}
	public void setOrgOtherCoo(String[] orgOtherCoo) {
		this.orgOtherCoo = orgOtherCoo;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public Double getZbje() {
		return zbje;
	}
	public void setZbje(Double zbje) {
		this.zbje = zbje;
	}
	public Boolean getZbjg() {
		return zbjg;
	}
	public void setZbjg(Boolean zbjg) {
		this.zbjg = zbjg;
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
	/**
	 * 项目编号
	 * @return
	 */
	public String getProjectCode() {
		return projectCode;
	}
	/**
	 * 项目编号
	 * @param projectCode
	 */
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
}
