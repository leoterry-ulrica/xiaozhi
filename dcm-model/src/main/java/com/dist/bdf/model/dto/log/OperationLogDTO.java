package com.dist.bdf.model.dto.log;

import com.dist.bdf.base.utils.DateUtil;

/**
 * 类描述： 用于记录 操作日志的DTO对象
 * 
 * @创建人：沈宇汀
 * @创建时间：2014-12-30 下午4:16:26
 */
public class OperationLogDTO {

	/**
	 * 操作者的真实姓名
	 */
	private String operatorName;
	/**
	 * 操作者的登录名/账户
	 */
	private String operatorAccount;
	/**
	 * 操作者IP
	 */
	private String ip;
	/**
	 * 操作时间
	 */
	private String dateOperated;
	/**
	 * 整个操作的执行时间,单位为ms
	 */
	private String executeDuration;
	/**
	 * 操作点，比如:菜单管理
	 */
	private String point;

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperatorAccount() {
		return operatorAccount;
	}

	public void setOperatorAccount(String operatorAccount) {
		this.operatorAccount = operatorAccount;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDateOperated() {
		return dateOperated;
	}

	public void setDateOperated(String dateOperated) {
		this.dateOperated = dateOperated;
	}

	public String getExecuteDuration() {
		return executeDuration;
	}

	public void setExecuteDuration(String executeDuration) {
		this.executeDuration = executeDuration;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

}
