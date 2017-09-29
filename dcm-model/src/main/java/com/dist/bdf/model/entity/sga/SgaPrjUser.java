package com.dist.bdf.model.entity.sga;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "SGA_PRJ_USER")
public class SgaPrjUser extends SgaBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long pid;
	@NotNull(message = "SgaPrjUser property [userId] can not be null")
	private Long userId;
	private Integer status;
	private String description;
	private Date createTime;
	private String direction;
	/**
	 * 关联微信小程序的表单id（临时之用）
	 */
	private String formId;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SgaPrjUser [pid=" + pid + ", userId=" + userId + ", status=" + status + ", description=" + description
				+ ", createTime=" + createTime + ", direction=" + direction + "]";
	}
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}

}
