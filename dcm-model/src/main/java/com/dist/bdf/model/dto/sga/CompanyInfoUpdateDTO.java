package com.dist.bdf.model.dto.sga;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 
 * 项目信息添加的DTO
 */
public class CompanyInfoUpdateDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotNull(message = "id can not be null...")
	private Long id;
	@NotEmpty(message = "name can not be empty...")
	private String name;
	@NotEmpty(message = "shortName can not be empty...")
	private String shortName;
	private String address;
	private int personCount;
	private int status;
	
	/**
	 * logo
	 */
	private ImgInfo logoInfo;
	/**
	 * 背景图
	 */
	private ImgInfo imgInfo;
	// private String logo;

	private String description;
	
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
	public int getPersonCount() {
		return personCount;
	}
	public void setPersonCount(int personCount) {
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "CompanyInfoUpdateDTO [id=" + id + ", name=" + name + ", shortName=" + shortName + ", address=" + address
				+ ", personCount=" + personCount + ", status=" + status + ", logoInfo=" + logoInfo + ", imgInfo="
				+ imgInfo + ", description=" + description + "]";
	}
	
}
