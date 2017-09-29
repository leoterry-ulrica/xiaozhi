package com.dist.bdf.model.dto.sga;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目
 * @author weifj
 *
 */
public class CooProjectResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String sysCode;
	private String tag;
	private int status;
	private String caseCode;
	private Date createTime;
	private int registerCount;
	private int joinInCount;
	// private Long cid;
	private String poster;
	private String posterName;
	private String direction;
	private String description;
	private String region;
	private CompanySimpleInfoResponseDTO company;
	/**
	 * 人在项目中的状态
	 */
	private int userStatus;
	/**
	 * 用户报名时间
	 */
	private Date userRegTime;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getRegisterCount() {
		return registerCount;
	}
	public void setRegisterCount(int registerCount) {
		this.registerCount = registerCount;
	}
	public int getJoinInCount() {
		return joinInCount;
	}
	public void setJoinInCount(int joinInCount) {
		this.joinInCount = joinInCount;
	}
	/*public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}*/
	public String getCaseCode() {
		return caseCode;
	}
	public void setCaseCode(String caseCode) {
		this.caseCode = caseCode;
	}
	public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
	public String getPoster() {
		return poster;
	}
	public void setPoster(String poster) {
		this.poster = poster;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 用户在项目中的状态
	 * @return
	 */
	public int getUserStatus() {
		return userStatus;
	}
	
	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}
	/**
	 * 用户报名时间
	 */
	public Date getUserRegTime() {
		return userRegTime;
	}
	public void setUserRegTime(Date userRegTime) {
		this.userRegTime = userRegTime;
	}
	public CompanySimpleInfoResponseDTO getCompany() {
		return company;
	}
	public void setCompany(CompanySimpleInfoResponseDTO company) {
		this.company = company;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	@Override
	public String toString() {
		return "CooProjectResponseDTO [id=" + id + ", name=" + name + ", sysCode=" + sysCode + ", tag=" + tag
				+ ", status=" + status + ", caseCode=" + caseCode + ", createTime=" + createTime + ", registerCount="
				+ registerCount + ", joinInCount=" + joinInCount + ", poster=" + poster + ", direction=" + direction
				+ ", description=" + description + ", region=" + region + ", company=" + company + ", userStatus="
				+ userStatus + ", userRegTime=" + userRegTime + "]";
	}
	public String getPosterName() {
		return posterName;
	}
	public void setPosterName(String posterName) {
		this.posterName = posterName;
	}
	
}
