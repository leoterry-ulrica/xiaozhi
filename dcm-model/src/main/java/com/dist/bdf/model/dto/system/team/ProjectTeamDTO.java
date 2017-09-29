package com.dist.bdf.model.dto.system.team;

import java.io.Serializable;
import java.util.List;

/**
 * 项目与团队的关联（未在项目中使用）
 * @author weifj
 *
 */
public class ProjectTeamDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String caseId;
	/**
	 * 操作类型。0：删除；1：添加；2：更新
	 */
	private Integer op;
	private Long teamId;
	private String teamName;
	
	private List<TeamUserDTO> teamUsers;

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
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

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public List<TeamUserDTO> getTeamUsers() {
		return teamUsers;
	}

	public void setTeamUsers(List<TeamUserDTO> teamUsers) {
		this.teamUsers = teamUsers;
	}
}
