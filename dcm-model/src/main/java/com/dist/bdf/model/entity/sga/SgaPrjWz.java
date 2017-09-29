package com.dist.bdf.model.entity.sga;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SGA_PRJ_WZ")
public class SgaPrjWz extends SgaBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long pid;
	private String wzId;
	private String creator;
	private Date createTime;
	

	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getWzId() {
		return wzId;
	}
	public void setWzId(String wzId) {
		this.wzId = wzId;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SgaPrjWz [pid=" + pid + ", wzId=" + wzId + ", creator=" + creator + ", createTime=" + createTime + "]";
	}
	
	
}
