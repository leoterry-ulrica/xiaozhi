package com.dist.bdf.model.dto.system.workgroup;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class WorkGroupOrgAddDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotNull(message =  "WorkGroupOrgAddDTO property [wgId] cannot be empty")
	private Long wgId;
	@NotEmpty(message = "WorkGroupOrgAddDTO property [orgGuid] cannot be empty")
	private String orgGuid;
	public Long getWgId() {
		return wgId;
	}
	public void setWgId(Long wgId) {
		this.wgId = wgId;
	}
	public String getOrgGuid() {
		return orgGuid;
	}
	public void setOrgGuid(String orgGuid) {
		this.orgGuid = orgGuid;
	}
	@Override
	public String toString() {
		return "WorkGroupOrgAddDTO [wgId=" + wgId + ", orgGuid=" + orgGuid + "]";
	}
}
