package com.dist.bdf.model.entity.system;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

/**
 * 脑图与团队关联
 * 
 */
@Entity
@Table(name = "DCM_NAOTU_TEAM")
public class DcmNaotuTeam extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields
	private String caseId;
	private Integer minderId;
	private String nodeId;
	private String teamId;
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
		return "DcmNaotuTeam [caseId=" + caseId + ", minderId=" + minderId + ", nodeId=" + nodeId + ", teamId=" + teamId
				+ ", realm=" + realm + "]";
	}
	
}