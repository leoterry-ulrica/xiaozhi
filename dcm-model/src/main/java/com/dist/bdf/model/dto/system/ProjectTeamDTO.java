package com.dist.bdf.model.dto.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.dist.bdf.model.dto.system.team.TeamUserDTO;

/**
 * 项目团队DTO
 * @author weifj
 *
 */
public class ProjectTeamDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String teamName;
	private List<TeamUserDTO> users = new ArrayList<TeamUserDTO>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public List<TeamUserDTO> getUsers() {
		return users;
	}
	public void setUsers(List<TeamUserDTO> users) {
		this.users = users;
	}

}
