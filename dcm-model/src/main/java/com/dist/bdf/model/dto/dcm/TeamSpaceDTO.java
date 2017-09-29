package com.dist.bdf.model.dto.dcm;

import com.dist.bdf.base.dto.BaseDTO;

/**
 * 存储团队空间或者模板简单信息
 * 
 * @author weifj
 * @version 1.0，2016/04/06，weifj，创建团队空间模板dto
 */
public class TeamSpaceDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String templateName;
	private String type;
	private String lastModified;
	private String lastModifiedUser;
	private TeamMemberDTO[] team;
	private String description;
	
	/**
	 * 实例名称
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	/**
	 * teamspace类型，template 或者 instance
	 */
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 最后修改日期
	 */
	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
	/**
	 * 最后修改者
	 */
	public String getLastModifiedUser() {
		return lastModifiedUser;
	}

	public void setLastModifiedUser(String lastModifiedUser) {
		this.lastModifiedUser = lastModifiedUser;
	}

	/**
	 * 团队空间成员
	 * @return
	 */
	public TeamMemberDTO[] getTeam() {
		return team;
	}

	public void setTeam(TeamMemberDTO[] team) {
		this.team = team;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 模板名称
	 * @return
	 */
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

}
