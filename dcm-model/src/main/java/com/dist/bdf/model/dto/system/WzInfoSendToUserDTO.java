package com.dist.bdf.model.dto.system;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 发送微作给用户的模型
 * @author weifj
 *
 */
public class WzInfoSendToUserDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 微作关联的人
	 */
	@NotEmpty(message = "WzInfoSendToUserDTO property [userIds] cannot be empty...")
	private String[] userIds;
	private String content;
	private String caseCode;
	private String caseName;
	/**
	 * 主题
	 */
	private String subject;
	
	public String[] getUserIds() {
		return userIds;
	}
	public void setUserIds(String[] userIds) {
		this.userIds = userIds;
	}
	/**
	 * 微作的内容
	 */
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 项目名称
	 * @return
	 */
	public String getCaseName() {
		return caseName;
	}
	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}
	/**
	 * 案例标识（不是guid）
	 */
	public String getCaseCode() {
		return caseCode;
	}
	public void setCaseCode(String caseCode) {
		this.caseCode = caseCode;
	}
	/**
	 * 获取 主题
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * 设置 主题
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
}
