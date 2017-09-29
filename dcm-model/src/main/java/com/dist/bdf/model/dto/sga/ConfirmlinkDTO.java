package com.dist.bdf.model.dto.sga;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 确认链接模型
 * @author weifj
 *
 */
public class ConfirmlinkDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotNull(message = "ConfirmlinkDTO property [uid] can not be null")
	private Long uid;
	private Long queueId;
	@NotEmpty(message = "ConfirmlinkDTO property [redirect] can not be empty")
	private String redirect;
	private int registerType;
	private String projectId;
	private Long comId;
	
	public ConfirmlinkDTO() {
		
	}
	public ConfirmlinkDTO(Long uid, Long queue, String redirect, int registerType, String projectId) {
		
		this.uid = uid;
		this.queueId = queue;
		this.redirect = redirect;
		this.registerType = registerType;
		this.projectId = projectId;
	}
	
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	public Long getQueueId() {
		return queueId;
	}
	public void setQueueId(Long queueId) {
		this.queueId = queueId;
	}
	public String getRedirect() {
		return redirect;
	}
	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}
	public int getRegisterType() {
		return registerType;
	}
	public void setRegisterType(int registerType) {
		this.registerType = registerType;
	}
	public Long getComId() {
		return comId;
	}
	public void setComId(Long comId) {
		this.comId = comId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	@Override
	public String toString() {
		return "ConfirmlinkDTO [uid=" + uid + ", queueId=" + queueId + ", redirect=" + redirect + ", registerType="
				+ registerType + ", projectId=" + projectId + ", comId=" + comId + "]";
	}
}
