package com.dist.bdf.model.dto.system;

import java.io.Serializable;
/**
 * 项目组和团队的汇总信息
 * @author weifj
 *
 */
public class GroupSummaryDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 项目总个数
	 */
	private Integer projectCount;
	/**
	 * 团队总个数=国内团队个数 + 国外团队个数
	 */
	private Integer teamCount;
	/**
	 * 机构个数=国内机构个数 + 国外机构个数
	 */
	private Integer orgCount;
	/**
	 * 国内团队个数
	 */
	private Integer domesticOrgCount;
	/**
	 * 国外团队个数
	 */
	private Integer abroadOrgCount;
	/**
	 * 总参与人数
	 */
	private Integer memberCount;
	
	public Integer getProjectCount() {
		return projectCount;
	}
	public void setProjectCount(Integer projectCount) {
		this.projectCount = projectCount;
	}
	public Integer getTeamCount() {
		return teamCount;
	}
	public void setTeamCount(Integer teamCount) {
		this.teamCount = teamCount;
	}
	public Integer getMemberCount() {
		return memberCount;
	}
	public void setMemberCount(Integer memberCount) {
		this.memberCount = memberCount;
	}
	public Integer getOrgCount() {
		return orgCount;
	}
	public void setOrgCount(Integer orgCount) {
		this.orgCount = orgCount;
	}
	public Integer getDomesticOrgCount() {
		return domesticOrgCount;
	}
	public void setDomesticOrgCount(Integer domesticOrgCount) {
		this.domesticOrgCount = domesticOrgCount;
	}
	public Integer getAbroadOrgCount() {
		return abroadOrgCount;
	}
	public void setAbroadOrgCount(Integer abroadOrgCount) {
		this.abroadOrgCount = abroadOrgCount;
	}
	@Override
	public String toString() {
		return "GroupSummaryDTO [projectCount=" + projectCount + ", teamCount=" + teamCount + ", orgCount=" + orgCount
				+ ", domesticOrgCount=" + domesticOrgCount + ", abroadOrgCount=" + abroadOrgCount + ", memberCount="
				+ memberCount + "]";
	}
	
}
