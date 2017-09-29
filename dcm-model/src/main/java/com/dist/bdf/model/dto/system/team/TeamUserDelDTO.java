package com.dist.bdf.model.dto.system.team;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
/**
 * 团队成员删除的模型
 * @author weifj
 *
 */
public class TeamUserDelDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotNull(message = "TeamUserDelDTO property [teamId] can not be null")
	private Long teamId;
	@NotEmpty(message = "TeamUserDelDTO property [userIds] can not be empty")
	private Long[] userIds;
	
	public Long getTeamId() {
		return teamId;
	}
	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}
	public Long[] getUserIds() {
		return userIds;
	}
	public void setUserIds(Long[] userIds) {
		this.userIds = userIds;
	}
	
}
