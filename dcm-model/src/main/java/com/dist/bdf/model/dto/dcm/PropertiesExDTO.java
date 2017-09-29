package com.dist.bdf.model.dto.dcm;

import java.io.Serializable;

/**
 * 扩展属性
 * @author weifj
 * @version 1.0，2016/05/12，weifj，创建扩展属性DTO
 */
public class PropertiesExDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 文种
	 */
	private String fileType;
	/**
	 * 所属区域
	 */
	private String region;
	/**
	 * 所属机构
	 */
	private String org;
	/**
	 * 业务类型
	 */
	private String business;
	/**
	 * 空间域
	 */
	private String domain;
	/**
	 * 是否可查询
	 */
	private boolean searchable;
	/**
	 * 文档或文件夹标签
	 */
	private String tags;
	/**
	 * 资源类型：
	 * Res_Pck_Institute：院包
	 * Res_Pck_Department：	所包
	 * Res_Pck_Project	：项目包
	 * Res_Pck_Person	：个人包
	 * Res_Pck_Group：组包
	 */
	private String resourceType;
	/**
	 * 简介描述信息
	 */
	private String desc;
	/**
	 * 关联的项目
	 */
	private String associateProject;
	/**
	 * 发布者
	 */
	private String publisher;
	
	
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
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
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public boolean getSearchable() {
		return searchable;
	}
	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}
	
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getAssociateProject() {
		return associateProject;
	}
	public void setAssociateProject(String associateProject) {
		this.associateProject = associateProject;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	@Override
	public String toString() {
		return "PropertiesExDTO [fileType=" + fileType + ", region=" + region + ", org=" + org + ", business="
				+ business + ", domain=" + domain + ", searchable=" + searchable + ", tags=" + tags + ", resourceType="
				+ resourceType + ", desc=" + desc + ", associateProject=" + associateProject + ", publisher="
				+ publisher + "]";
	}
}
