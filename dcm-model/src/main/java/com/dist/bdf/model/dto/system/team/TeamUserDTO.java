package com.dist.bdf.model.dto.system.team;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 团队与人员的关联
 * @author weifj
 *
 */
public class TeamUserDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	/**
	 * 登录名
	 */
	@NotEmpty(message = "loginName can not be empty")
	private String loginName;
    /**
     * 用户名，一般为中文名
     */
	@NotEmpty(message = "userName can not be empty")
	private String userName;
	/**
	 * 用户编码
	 */
	private String userCode;
	/**
	 * 
	 */
	private Integer isLeader;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public Integer getIsLeader() {
		return isLeader;
	}
	public void setIsLeader(Integer isLeader) {
		this.isLeader = isLeader;
	}
	
}
