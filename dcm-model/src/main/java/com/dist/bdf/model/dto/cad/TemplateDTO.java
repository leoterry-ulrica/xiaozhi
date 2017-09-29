package com.dist.bdf.model.dto.cad;

import com.dist.bdf.model.entity.cad.TemplateEntity;

public class TemplateDTO  extends TemplateEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 空间域
	 */
	private String domain;
	/**
	 * 是否可查询
	 */
	private boolean searchable;
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
	 * 发布者
	 */
	private String publisher;
	
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
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
}
