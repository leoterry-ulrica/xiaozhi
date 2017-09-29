package com.dist.bdf.model.dto.sga;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 项目报名模型
 * @author weifj
 *
 */
public class SignupDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 邮箱
	 */
	@NotEmpty(message = "SignupDTO property [email] can not be empty")
	private String email;
	/**
	 * 项目guid
	 */
	@NotEmpty(message = "SignupDTO property [projectId] can not be empty")
	private String projectId;
	/**
	 * 注册类型。0：默认自主注册；1：项目报名；2：项目邀请；3：企业邀请
	 */
	private int registerType;
	/**
	 * 企业id
	 */
	private Long comId;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public Long getComId() {
		return comId;
	}
	public void setComId(Long comId) {
		this.comId = comId;
	}
	public int getRegisterType() {
		return registerType;
	}
	public void setRegisterType(int registerType) {
		this.registerType = registerType;
	}
	@Override
	public String toString() {
		return "SignupDTO [email=" + email + ", projectId=" + projectId + ", registerType=" + registerType + ", comId="
				+ comId + "]";
	}
}
