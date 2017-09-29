package com.dist.bdf.model.dto.system;

import com.dist.bdf.base.dto.BaseDTO;
import com.dist.bdf.model.entity.system.DcmOrganization;

/**
 * 基本是实体模型DcmOrganization的扩展版superParentCode（特值所属院的编码）
 * @author weifj
 *
 */
public class DcmOrganizationDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getDn() {
		return dn;
	}
	public void setDn(String dn) {
		this.dn = dn;
	}
	private Long parentId;
	private String domainType;
	private String orgName;
	private Long orderId;
	private String orgCode;
	private String alias;
	private String dn;
	private String superParentCode;
	private DcmOrganization org;
	
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getDomainType() {
		return domainType;
	}
	public void setDomainType(String domainType) {
		this.domainType = domainType;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getSuperParentCode() {
		return superParentCode;
	}
	public void setSuperParentCode(String superParentCode) {
		this.superParentCode = superParentCode;
	}
	
	public static DcmOrganizationDTO clone(DcmOrganization org) {
		
		DcmOrganizationDTO dto = new DcmOrganizationDTO();
		dto.setAlias(org.getAlias());
		dto.setDomainType(org.getDomainType());
		dto.setId(org.getId());
		dto.setOrderId(org.getOrderId());
		dto.setOrgCode(org.getOrgCode());
		dto.setOrgName(org.getOrgName());
		dto.setParentId(org.getParentId());
		dto.setDn(org.getDn());
		dto.setOrg(org);

		return dto;
	}
	public DcmOrganization getOrg() {
/*		
		DcmOrganization org = new DcmOrganization();
		org.setAlias(org.getAlias());
		org.setDomainType(org.getDomainType());
		org.setId(org.getId());
		org.setOrderId(org.getOrderId());
		org.setOrgCode(org.getOrgCode());
		org.setOrgName(org.getOrgName());
		org.setParentId(org.getParentId());*/
		
		return org;
	}
	public void setOrg(DcmOrganization org) {
		this.org = org;
	}
}
