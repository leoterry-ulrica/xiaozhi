package com.dist.bdf.model.dto.system;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * 微作邮件发送反馈的模型
 * @author weifj
 *
 */
import java.util.List;
public class WzEmailResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<String> noEUsers = new ArrayList<String>();
	private List<String> illegalEUsers = new ArrayList<String>();
	private List<String> failEUsers = new ArrayList<String>();
	
	/**
	 * 不填写邮箱的用户
	 * @return
	 */
	public List<String> getNoEUsers() {
		return noEUsers;
	}
	public void setNoEUsers(List<String> noEUsers) {
		this.noEUsers = noEUsers;
	}
	/**
	 * 填写非法邮箱的用户
	 * @return
	 */
	public List<String> getIllegalEUsers() {
		return illegalEUsers;
	}
	public void setIllegalEUsers(List<String> illegalEUsers) {
		this.illegalEUsers = illegalEUsers;
	}
	/**
	 * 发送失败的用户
	 * @return
	 */
	public List<String> getFailEUsers() {
		return failEUsers;
	}
	public void setFailEUsers(List<String> failEUsers) {
		this.failEUsers = failEUsers;
	}
}
