package com.dist.bdf.model.dto.system.workgroup;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;
/**
 * 工作组添加模型
 * @author weifj
 *
 */
public class WorkGroupAddDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	@NotEmpty(message = "WorkGroupAddDTO property [projectGuid] cannot be empty")
	private String projectGuid;
	private String realm;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProjectGuid() {
		return projectGuid;
	}
	public void setProjectGuid(String projectGuid) {
		this.projectGuid = projectGuid;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
	@Override
	public String toString() {
		return "WorkGroupAddDTO [name=" + name + ", projectGuid=" + projectGuid + ", realm=" + realm + "]";
	}
}
