package com.dist.bdf.model.dto.system;

import java.io.Serializable;
/**
 * 机构基本信息更新
 * @author weifj
 *
 */
public class OrgBasicInfoUpdateDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String orgCode;
	private String name;
	private String orgType;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	@Override
	public String toString() {
		return "OrgBasicInfoUpdateDTO [orgCode=" + orgCode + ", name=" + name + ", orgType=" + orgType + "]";
	}
}
