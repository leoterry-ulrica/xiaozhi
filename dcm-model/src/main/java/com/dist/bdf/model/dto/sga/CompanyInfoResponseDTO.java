package com.dist.bdf.model.dto.sga;

import java.io.Serializable;
import java.util.Date;

/**
 * 企业信息响应对象
 * @author weifj
 *
 */
public class CompanyInfoResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String shortName;
	private String sysCode;
	private String address;
	private String personCount;
	private String logo;
	private String logoName;
	private Date createTime;
	private String img;
	private String imgName;
	private String description;
	private String materialSourceCount;
	private String materialSourceDepartment;
	/**
	 * 项目的总个数
	 */
	private Integer projectCount;
	private Integer registerCount;
	private Integer joininCount;
	private String realm;
	private Integer status;
	/**
	 * 招募的项目个数
	 */
	private Integer projectRecruitedCount;
	
	/**
	 * 招募率
	 */
	private double rate = 0.0;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPersonCount() {
		return personCount;
	}
	public void setPersonCount(String personCount) {
		this.personCount = personCount;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getProjectCount() {
		return projectCount;
	}
	public void setProjectCount(Integer projectCount) {
		this.projectCount = projectCount;
	}
	public Integer getRegisterCount() {
		return registerCount;
	}
	public void setRegisterCount(Integer registerCount) {
		this.registerCount = registerCount;
	}
	public Integer getJoininCount() {
		return joininCount;
	}
	public void setJoininCount(Integer joininCount) {
		this.joininCount = joininCount;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getProjectRecruitedCount() {
		return projectRecruitedCount;
	}
	public void setProjectRecruitedCount(Integer projectRecruitedCount) {
		this.projectRecruitedCount = projectRecruitedCount;
	}
	public String getLogoName() {
		return logoName;
	}
	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}
	public String getImgName() {
		return imgName;
	}
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	public String getMaterialSourceCount() {
		return materialSourceCount;
	}
	public void setMaterialSourceCount(String materialSourceCount) {
		this.materialSourceCount = materialSourceCount;
	}
	public String getMaterialSourceDepartment() {
		return materialSourceDepartment;
	}
	public void setMaterialSourceDepartment(String materialSourceDepartment) {
		this.materialSourceDepartment = materialSourceDepartment;
	}
	@Override
	public String toString() {
		return "CompanyInfoResponseDTO [id=" + id + ", name=" + name + ", shortName=" + shortName + ", sysCode="
				+ sysCode + ", address=" + address + ", personCount=" + personCount + ", logo=" + logo + ", logoName="
				+ logoName + ", createTime=" + createTime + ", img=" + img + ", imgName=" + imgName + ", description="
				+ description + ", materialSourceCount=" + materialSourceCount + ", materialSourceDepartment="
				+ materialSourceDepartment + ", projectCount=" + projectCount + ", registerCount=" + registerCount
				+ ", joininCount=" + joininCount + ", realm=" + realm + ", status=" + status
				+ ", projectRecruitedCount=" + projectRecruitedCount + ", rate=" + rate + "]";
	}
	
}
