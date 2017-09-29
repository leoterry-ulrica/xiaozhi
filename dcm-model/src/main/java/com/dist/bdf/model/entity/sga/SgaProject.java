package com.dist.bdf.model.entity.sga;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SGA_PROJECT")
public class SgaProject extends SgaBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String sysCode;
	private String tag;
	private Integer status;
	private String caseCode;
	private Date createTime;
	private Integer registerCount;
	private Integer joinInCount;
	private Long cid;
	private String poster;
	private String direction;
	private String posterName;
	/**
	 * 区域，省市县
	 */
	private String region;
	
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
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
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
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}

	public String getPosterName() {
		return posterName;
	}
	public void setPosterName(String posterName) {
		this.posterName = posterName;
	}
	@Override
	public String toString() {
		return "SgaProject [name=" + name + ", sysCode=" + sysCode + ", tag=" + tag + ", status=" + status
				+ ", caseCode=" + caseCode + ", createTime=" + createTime + ", registerCount=" + registerCount
				+ ", joinInCount=" + joinInCount + ", cid=" + cid + ", poster=" + poster + ", direction=" + direction
				+ ", posterName=" + posterName + ", region=" + region + "]";
	}
	
}
