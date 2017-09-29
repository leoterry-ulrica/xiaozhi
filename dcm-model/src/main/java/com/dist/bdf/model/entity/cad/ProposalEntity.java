package com.dist.bdf.model.entity.cad;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Field;

import com.dist.bdf.common.constants.MongoFieldConstants;

public class ProposalEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Field(MongoFieldConstants.NAME)
	private String name;
	@Field(MongoFieldConstants.DESCRIPTION)
	private String description;
	@Field(MongoFieldConstants.DATABASE_TYPE)
	private String dbType;
	@Field(MongoFieldConstants.SERVER)
	private String server;
	@Field(MongoFieldConstants.DATABASE_NAME)
	private String dbName;
	@Field(MongoFieldConstants.DATABASE_USER)
	private String dbUser;
	@Field(MongoFieldConstants.DATABASE_PASSWORD)
	private String dbPassword;
	@Field(MongoFieldConstants.LOGIN_USER)
	private String loginUser;
	@Field(MongoFieldConstants.LOGIN_PASSWORD)
	private String loginPassword;
	/**
	 * 获取 #{bare_field_comment} 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置 #{bare_field_comment}
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取 #{bare_field_comment} 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * 设置 #{bare_field_comment}
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 获取 #{bare_field_comment} 
	 * @return the dbType
	 */
	public String getDbType() {
		return dbType;
	}
	/**
	 * 设置 #{bare_field_comment}
	 * @param dbType the dbType to set
	 */
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	/**
	 * 获取 #{bare_field_comment} 
	 * @return the server
	 */
	public String getServer() {
		return server;
	}
	/**
	 * 设置 #{bare_field_comment}
	 * @param server the server to set
	 */
	public void setServer(String server) {
		this.server = server;
	}
	/**
	 * 获取 #{bare_field_comment} 
	 * @return the dbName
	 */
	public String getDbName() {
		return dbName;
	}
	/**
	 * 设置 #{bare_field_comment}
	 * @param dbName the dbName to set
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	/**
	 * 获取 #{bare_field_comment} 
	 * @return the dbUser
	 */
	public String getDbUser() {
		return dbUser;
	}
	/**
	 * 设置 #{bare_field_comment}
	 * @param dbUser the dbUser to set
	 */
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	/**
	 * 获取 #{bare_field_comment} 
	 * @return the dbPassword
	 */
	public String getDbPassword() {
		return dbPassword;
	}
	/**
	 * 设置 #{bare_field_comment}
	 * @param dbPassword the dbPassword to set
	 */
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	/**
	 * 获取 #{bare_field_comment} 
	 * @return the loginUser
	 */
	public String getLoginUser() {
		return loginUser;
	}
	/**
	 * 设置 #{bare_field_comment}
	 * @param loginUser the loginUser to set
	 */
	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}
	/**
	 * 获取 #{bare_field_comment} 
	 * @return the loginPassword
	 */
	public String getLoginPassword() {
		return loginPassword;
	}
	/**
	 * 设置 #{bare_field_comment}
	 * @param loginPassword the loginPassword to set
	 */
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProposalEntity [name=" + name + ", description=" + description + ", dbType=" + dbType + ", server="
				+ server + ", dbName=" + dbName + ", dbUser=" + dbUser + ", dbPassword=" + dbPassword + ", loginUser="
				+ loginUser + ", loginPassword=" + loginPassword + "]";
	}
	
}
