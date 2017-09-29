package com.dist.bdf.model.dto.sga;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 
 * 企业信息添加的DTO
 */
public class CompanyInfoAddDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotEmpty(message = "CompanyInfoAddDTO property [name] can not be empty...")
	private String name;
	private String shortName;
	private String address;
	private String personCount;
	@NotEmpty(message = "CompanyInfoAddDTO property [realm] can not be empty...")
	private String realm;
	private int status;
	
	/**
	 * logo
	 */
	private ImgInfo logoInfo;
	/**
	 * 背景图
	 */
	private ImgInfo imgInfo;
	//private String logo;

	private String description;
	
	private String materialSourceCount;
	private String materialSourceDepartment;
	
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	/*public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}*/
	public ImgInfo getLogoInfo() {
		return logoInfo;
	}
	public void setLogoInfo(ImgInfo logoInfo) {
		this.logoInfo = logoInfo;
	}
	public ImgInfo getImgInfo() {
		return imgInfo;
	}
	public void setImgInfo(ImgInfo imgInfo) {
		this.imgInfo = imgInfo;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
		return "CompanyInfoAddDTO [name=" + name + ", shortName=" + shortName + ", address=" + address
				+ ", personCount=" + personCount + ", realm=" + realm + ", status=" + status + ", logoInfo=" + logoInfo
				+ ", imgInfo=" + imgInfo + ", description=" + description + ", materialSourceCount="
				+ materialSourceCount + ", materialSourceDepartment=" + materialSourceDepartment + "]";
	}
	
	
	
}
