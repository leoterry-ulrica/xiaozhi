package com.dist.bdf.model.entity.system;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;
/**
 * 工作组
 * @author weifj
 *
 */
@Entity
@Table(name = "DCM_WORKGROUP")
public class DcmWorkGroup extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
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
		return "DcmWorkgroup [name=" + name + ", projectGuid=" + projectGuid + ", realm=" + realm + "]";
	}

}
