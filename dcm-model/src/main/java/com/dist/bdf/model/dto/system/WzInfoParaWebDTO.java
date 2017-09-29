package com.dist.bdf.model.dto.system;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 
 * @author weifj
 * @version 1.0，2016/12/20，weifj，作为web端新建微作传入的参数信息
 */
public class WzInfoParaWebDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotEmpty( message = "WzInfoParaWebDTO property [userId] can not be empty")
	private String userId;
	@NotEmpty( message = "WzInfoParaWebDTO property [realm] can not be empty")
	private String realm;
	@NotEmpty( message = "WzInfoParaWebDTO property [caseId] can not be empty")
	private String caseId;
	private String content;
	private String[] atPersons;
	private String[] files;
	private String channelCode;
	/**
	 * 微作类型
	 * 0：普通微作，1：调研微作，2：任务微作
	 */
	private Integer type;
	
	/**
	 * 微作内容
	 * @return
	 */
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * @的人
	 * @return
	 */
	public String[] getAtPersons() {
		return atPersons;
	}
	public void setAtPersons(String[] atPersons) {
		this.atPersons = atPersons;
	}
	
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 关联文件列表
	 */
	public String[] getFiles() {
		return files;
	}
	public void setFiles(String[] files) {
		this.files = files;
	}
	/**
	 * 频道编码
	 */
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}
