package com.dist.bdf.model.dto.system.user;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 用户基类dto
 * @author weifj
 * @version 1.0，2016/05/06，创建简单用户DTO
 */
public class BaseUserDTO implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Long id;
	/**
	 * 登录名
	 */
	@NotEmpty(message = "loginName can not be empty")
	protected String loginName;
    /**
     * 用户名，一般为中文名
     */
	@NotEmpty(message = "userName can not be empty")
	protected String userName;
	/**
	 *  用户在ldap中的dn
	 */
	protected String dn;
	/**
	 * 用户编码
	 */
	protected String userCode;
 
	public BaseUserDTO(){
		
	}
	public BaseUserDTO(Long id, String userName, String loginName){
		this.id = id;
		this.userName = userName;
		this.loginName = loginName;
	}
	public BaseUserDTO(Long id, String userName, String loginName, String dn){
		this.id = id;
		this.userName = userName;
		this.loginName = loginName;
		this.dn = dn;
	}
	public BaseUserDTO(Long id, String userCode, String userName, String loginName, String dn){
		this.id = id;
		this.userCode = userCode;
		this.userName = userName;
		this.loginName = loginName;
		this.dn = dn;
	}
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
	public String getDn() {
		return dn;
	}
	public void setDn(String dn) {
		this.dn = dn;
	}
	
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

}
