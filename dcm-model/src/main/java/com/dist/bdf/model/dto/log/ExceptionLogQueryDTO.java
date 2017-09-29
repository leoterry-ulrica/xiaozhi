package com.dist.bdf.model.dto.log;

/**
 * 
 * 类描述： 用于保存异常日志查询统计数据的查询条件的javabean
 * 
 * @创建人：沈宇汀
 * @创建时间：2015-1-4 下午2:13:47
 */
public class ExceptionLogQueryDTO {

	/**
	 * 操作日志的内容点
	 */
	private String exceptionName;

	/**
	 * 操作时间-开始
	 */
	private String dateBegin;
	/**
	 * 操作时间-结束
	 */
	private String dateEnd;

	public String getExceptionName() {
		return exceptionName;
	}

	public void setExceptionName(String exceptionName) {
		this.exceptionName = exceptionName;
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
