package com.dist.bdf.model.entity.sga;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SGA_COM_DETAIL")
public class SgaComDetail extends SgaBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long cid;
	private String description;
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SgaComDetail [cid=" + cid + ", description=" + description + "]";
	}
}
