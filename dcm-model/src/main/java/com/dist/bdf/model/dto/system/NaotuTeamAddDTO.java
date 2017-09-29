package com.dist.bdf.model.dto.system;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
/**
 * 新增脑图和团队的关联关系
 * @author weifj
 *
 */
public class NaotuTeamAddDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotEmpty(message = "NaotuTeamAddDTO property [caseGuid] cannot be empty")
	private String caseId;
	@NotNull(message = "NaotuTeamAddDTO property [minderId] cannot be empty")
	private Integer minderId;
	@NotEmpty(message = "NaotuTeamAddDTO property [nodeId] cannot be empty")
	private String nodeId;
	@NotEmpty(message = "NaotuTeamAddDTO property [teamId] cannot be empty")
	private String teamId;
	@NotEmpty(message = "NaotuTeamAddDTO property [realm] cannot be empty")
	private String realm;
	
	public Integer getMinderId() {
		return minderId;
	}
	public void setMinderId(Integer minderId) {
		this.minderId = minderId;
	}
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	@Override
	public String toString() {
		return "NaotuTeamAddDTO [caseId=" + caseId + ", minderId=" + minderId + ", nodeId=" + nodeId + ", teamId="
				+ teamId + ", realm=" + realm + "]";
	}
}
