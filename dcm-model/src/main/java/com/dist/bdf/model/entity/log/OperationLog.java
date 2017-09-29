package com.dist.bdf.model.entity.log;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

/**
 * 类描述： 用于记录 操作日志的实体类。
 * 
 * @创建人：沈宇汀
 * @创建时间：2014-12-30 下午4:16:26
 */
@Entity
@Table(name = "T_OPERATION_LOG")
public class OperationLog extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1357675729871335894L;

	/**
	 * 操作者的真实姓名
	 */
	@Column(name = "OPERATOR_NAME")
	private String operatorName;
	/**
	 * 操作者的登录名/账户
	 */
	@Column(name = "OPERATOR_ACCOUNT")
	private String operatorAccount;
	/**
	 * 操作者IP
	 */
	@Column(name = "IP")
	private String ip;
	/**
	 * 操作时间
	 */
	@Column(name = "DATE_OPERATED")
	private Date dateOperated;
	/**
	 * 整个操作的执行时间,单位为ms
	 */
	@Column(name = "EXECUTE_DURATION")
	private Long executeDuration;
	/**
	 * 操作点，比如:菜单管理
	 */
	@Column(name = "POINT")
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

	public Date getDateOperated() {
		return dateOperated;
	}

	public void setDateOperated(Date dateOperated) {
		this.dateOperated = dateOperated;
	}

	public Long getExecuteDuration() {
		return executeDuration;
	}

	public void setExecuteDuration(Long executeDuration) {
		this.executeDuration = executeDuration;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

}
