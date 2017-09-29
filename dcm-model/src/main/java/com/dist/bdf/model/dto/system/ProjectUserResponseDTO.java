package com.dist.bdf.model.dto.system;

import java.io.Serializable;
import java.util.List;

public class ProjectUserResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String projectName;
	/**
	 * 角色和用户关联DTO
	 * 此命名欠妥
	 */
	private List<GroupUserDTO> roles;
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public List<GroupUserDTO> getRoles() {
		return roles;
	}
	public void setRoles(List<GroupUserDTO> roles) {
		this.roles = roles;
	}
}
