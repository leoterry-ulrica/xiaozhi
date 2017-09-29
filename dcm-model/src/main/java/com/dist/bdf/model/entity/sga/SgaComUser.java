package com.dist.bdf.model.entity.sga;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SGA_COM_USER")
public class SgaComUser extends SgaBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long cid;
	private Long userId;
	private Integer status;
	
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SgaComUser [cid=" + cid + ", userId=" + userId + ", status=" + status + "]";
	}
	
	
}
