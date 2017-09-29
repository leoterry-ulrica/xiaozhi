package com.dist.bdf.model.entity.system;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

@Entity
@Table(name = "DCM_DIC_ORGEXT")
public class DcmDicOrgExt extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private Long orgId;
	private Integer type;
	private String orgCode;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	@Override
	public String toString() {
		return "DcmDicOrgExt [name=" + name + ", orgId=" + orgId + ", type=" + type + ", orgCode=" + orgCode + "]";
	}
}
