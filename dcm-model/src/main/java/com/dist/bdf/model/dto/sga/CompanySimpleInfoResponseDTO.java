package com.dist.bdf.model.dto.sga;

import java.io.Serializable;

/**
 * 企业信息响应对象
 * @author weifj
 *
 */
public class CompanySimpleInfoResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String shortName;
	private String sysCode;
	private String realm;
	
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
	@Override
	public String toString() {
		return "CompanySimpleInfoResponseDTO [id=" + id + ", name=" + name + ", shortName=" + shortName + ", sysCode="
				+ sysCode + ", realm=" + realm + "]";
	}
	
}
