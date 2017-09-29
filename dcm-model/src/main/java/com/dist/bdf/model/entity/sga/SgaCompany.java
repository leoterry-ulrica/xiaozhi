package com.dist.bdf.model.entity.sga;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SGA_COMPANY")
public class SgaCompany extends SgaBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String shortName;
	private String sysCode;
	private String address;
	private String personCount;
	private String logo;
	private Date createTime;
	private Integer status;
	private String img;
	private String realm;
	private String logoName;
	private String imgName;
	/**
	 * 资料来源数
	 */
	private String materialSourceCount;
	/**
	 * 资料来源部门
	 */
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

	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
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
		return "SgaCompany [name=" + name + ", shortName=" + shortName + ", sysCode=" + sysCode + ", address=" + address
				+ ", personCount=" + personCount + ", logo=" + logo + ", createTime=" + createTime + ", status="
				+ status + ", img=" + img + ", realm=" + realm + ", logoName=" + logoName + ", imgName=" + imgName
				+ ", materialSourceCount=" + materialSourceCount + ", materialSourceDepartment="
				+ materialSourceDepartment + "]";
	}
	
}
