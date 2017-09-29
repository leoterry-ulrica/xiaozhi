package com.dist.bdf.model.entity.sga;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SGA_PRJ_DETAIL")
public class SgaPrjDetail extends SgaBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long pid;
	private String description;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SgaPrjDetail [pid=" + pid + ", description=" + description + "]";
	}
	
}
