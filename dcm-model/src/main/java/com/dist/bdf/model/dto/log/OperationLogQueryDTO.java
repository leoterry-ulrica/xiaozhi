package com.dist.bdf.model.dto.log;

/**
 * 类描述： 用于保存查询统计数据的查询条件的javabean
 * 
 * @创建人：沈宇汀
 * @创建时间：2014-12-31 上午11:16:59
 */
public class OperationLogQueryDTO {

	/**
	 * 操作者真实姓名
	 */
	private String operatorName;

	/**
	 * 操作者账号
	 */
	private String operatorAccount;

	/**
	 * 操作日志的内容点
	 */
	private String point;

	/**
	 * 操作时间-开始
	 */
	private String dateBegin;
	/**
	 * 操作时间-结束
	 */
	private String dateEnd;

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

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}

	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

}
