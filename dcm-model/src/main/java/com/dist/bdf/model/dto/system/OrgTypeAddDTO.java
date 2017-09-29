package com.dist.bdf.model.dto.system;

import java.io.Serializable;
/**
 * 机构类型添加
 * @author weifj
 *
 */
public class OrgTypeAddDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private String orgCode;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	@Override
	public String toString() {
		return "OrgTypeAddDTO [name=" + name + ", orgCode=" + orgCode + "]";
	}
}
