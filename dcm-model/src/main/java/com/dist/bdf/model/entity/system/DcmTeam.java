package com.dist.bdf.model.entity.system;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

@Entity
@Table(name = "DCM_TEAM")
public class DcmTeam extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private Long projectId;
	private Date createTime;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DcmTeam [name=" + name + ", projectId=" + projectId + ", createTime=" + createTime + "]";
	}
}
