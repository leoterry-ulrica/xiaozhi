package com.dist.bdf.model.dto.system;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 机构删除请求参数
 * @author weifj
 *
 */
public class OrgDelRequestDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotEmpty( message = "realm can not be empty")
	private String realm;
	@NotEmpty( message = "orgIds can not be empty")
	private Long[] orgIds;
	
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
	public Long[] getOrgIds() {
		return orgIds;
	}
	public void setOrgIds(Long[] orgIds) {
		this.orgIds = orgIds;
	}

}
