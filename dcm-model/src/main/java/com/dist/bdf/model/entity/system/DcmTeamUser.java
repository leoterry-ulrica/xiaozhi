package com.dist.bdf.model.entity.system;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

@Entity
@Table(name = "DCM_TEAM_USER")
public class DcmTeamUser extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long teamId;
	private Long userId;
	private Date createTime;
	private Integer isLeader;
	private Long projectId;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getTeamId() {
		return teamId;
	}
	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getIsLeader() {
		return isLeader;
	}
	public void setIsLeader(Integer isLeader) {
		this.isLeader = isLeader;
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	@Override
	public String toString() {
		return "DcmTeamUser [teamId=" + teamId + ", userId=" + userId + ", createTime=" + createTime + ", isLeader="
				+ isLeader + ", projectId=" + projectId + "]";
	}
}
