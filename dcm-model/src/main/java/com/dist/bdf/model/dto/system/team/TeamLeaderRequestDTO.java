package com.dist.bdf.model.dto.system.team;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class TeamLeaderRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotNull(message = "TeamLeaderRequestDTO property [teamId] can not be null")
	private Long teamId;
	@NotNull(message = "TeamLeaderRequestDTO property [preLeader] can not be null")
	private Long preLeader;
	@NotNull(message = "TeamLeaderRequestDTO property [newLeader] can not be null")
	private Long newLeader;
	/**
	 * op：操作类型，0：取消；1：设置
	 */
	private Integer op;
	public Long getPreLeader() {
		return preLeader;
	}
	public void setPreLeader(Long preLeader) {
		this.preLeader = preLeader;
	}
	public Long getNewLeader() {
		return newLeader;
	}
	public void setNewLeader(Long newLeader) {
		this.newLeader = newLeader;
	}
	public Integer getOp() {
		return op;
	}
	public void setOp(Integer op) {
		this.op = op;
	}
	public Long getTeamId() {
		return teamId;
	}
	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}
}
