package com.dist.bdf.model.entity.sga;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SGA_COM_AUDIT")
public class SgaComAudit extends SgaBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long cid;
	private Integer projectCount;
	private Integer registerCount;
	private Integer joininCount;
	
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public Integer getProjectCount() {
		return projectCount;
	}
	public void setProjectCount(Integer projectCount) {
		this.projectCount = projectCount;
	}
	public Integer getRegisterCount() {
		return registerCount;
	}
	public void setRegisterCount(Integer registerCount) {
		this.registerCount = registerCount;
	}
	public Integer getJoininCount() {
		return joininCount;
	}
	public void setJoininCount(Integer joininCount) {
		this.joininCount = joininCount;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SgaComStatistics [cid=" + cid + ", projectCount=" + projectCount + ", registerCount=" + registerCount
				+ ", joininCount=" + joininCount + "]";
	}
}
