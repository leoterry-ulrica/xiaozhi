package com.dist.bdf.model.entity.system;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;
/**
 * 工作组与机构关联
 * @author weifj
 *
 */
@Entity
@Table(name = "DCM_WG_ORG")
public class DcmWgOrg extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private Long wgId;
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
		return "DcmWgOrg [wgId=" + wgId + ", orgGuid=" + orgGuid + "]";
	}
}
