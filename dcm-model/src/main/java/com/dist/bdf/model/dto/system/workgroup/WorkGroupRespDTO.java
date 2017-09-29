package com.dist.bdf.model.dto.system.workgroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 工作组信息反馈模型
 * @author weifj
 *
 */
public class WorkGroupRespDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private List<WorkGroupOrgDTO> orgs = new ArrayList<WorkGroupOrgDTO>();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<WorkGroupOrgDTO> getOrgs() {
		return orgs;
	}
	public void setOrgs(List<WorkGroupOrgDTO> orgs) {
		this.orgs = orgs;
	}
	public WorkGroupOrgDTO addOrg(String orgGuid, String orgName) {
		WorkGroupOrgDTO dto = new WorkGroupOrgDTO();
		dto.setGuid(orgGuid);
		dto.setName(orgName);
		this.orgs.add(dto);
		return dto;
	}
	@Override
	public String toString() {
		return "WorkGroupRepDTO [id=" + id + ", name=" + name + ", orgs=" + orgs + "]";
	}
}
