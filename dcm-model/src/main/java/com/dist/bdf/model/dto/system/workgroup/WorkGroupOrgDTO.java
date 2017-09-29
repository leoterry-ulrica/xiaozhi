package com.dist.bdf.model.dto.system.workgroup;

import java.io.Serializable;
/**
 * 工作组关联的机构模型
 * @author weifj
 *
 */
public class WorkGroupOrgDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String guid;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	@Override
	public String toString() {
		return "WorkGroupOrgDTO [name=" + name + ", guid=" + guid + "]";
	}
}
