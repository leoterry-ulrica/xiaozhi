package com.dist.bdf.model.dto.dcm;

import com.dist.bdf.base.dto.BaseDTO;

/**
 * 团队空间详情信息的dto
 * @author weifj
 * @version 1.0，2016/03/29，weifj，创建
 *
 */
public class TeamSpaceDetailDTO extends BaseDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String classDescription;
	private String teamspaceName;
	private String teamspaceState;
	private String creator;
	private String dateCreated;
	private String dateLastModified;
	private String description;
	private String folderName;
	private String lastModifier;
	private String name; 
	private String owner;
	private String pathName;
	
	public String getClassDescription() {
		return classDescription;
	}
	public void setClassDescription(String classDescription) {
		this.classDescription = classDescription;
	}
	public String getTeamspaceName() {
		return teamspaceName;
	}
	public void setTeamspaceName(String teamspaceName) {
		this.teamspaceName = teamspaceName;
	}
	public String getTeamspaceState() {
		return teamspaceState;
	}
	public void setTeamspaceState(String teamspaceState) {
		this.teamspaceState = teamspaceState;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getDateLastModified() {
		return dateLastModified;
	}
	public void setDateLastModified(String dateLastModified) {
		this.dateLastModified = dateLastModified;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFolderName() {
		return folderName;
	}
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getLastModifier() {
		return lastModifier;
	}
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getPathName() {
		return pathName;
	}
	public void setPathName(String pathName) {
		this.pathName = pathName;
	}
}
