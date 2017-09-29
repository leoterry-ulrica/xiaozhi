package com.dist.bdf.model.dto.log;

import com.dist.bdf.base.utils.DateUtil;
import com.dist.bdf.model.entity.log.ExceptionLog;

/**
 * 
 * 类描述： 用于记录 异常日志的DTO对象
 * 
 * @创建人：沈宇汀
 * @创建时间：2015-1-4 下午2:25:23
 */
public class ExceptionLogDTO {
	/**
	 * 异常发生时间
	 */
	private String dateHappen;

	/**
	 * 异常名称
	 */
	private String exceptionName;

	/**
	 * 异常内容
	 */
	private String exceptionContent;

	public String getDateHappen() {
		return dateHappen;
	}

	public void setDateHappen(String dateHappen) {
		this.dateHappen = dateHappen;
	}

	public String getExceptionName() {
		return exceptionName;
	}

	public void setExceptionName(String exceptionName) {
		this.exceptionName = exceptionName;
	}

	public String getExceptionContent() {
		return exceptionContent;
	}

	public void setExceptionContent(String exceptionContent) {
		this.exceptionContent = exceptionContent;
	}

	/**
	 * 将异常日志的实体对象，转换为dto对象
	 * 
	 * @param log
	 */
	public static ExceptionLogDTO entity2vo(ExceptionLog log) {

		ExceptionLogDTO dto = new ExceptionLogDTO();
		dto.setDateHappen(DateUtil.toDateTimeStr(log.getDateHappen()));
		dto.setExceptionName(log.getExceptionName());
		dto.setExceptionContent(log.getExceptionContent());

		return dto;
	}
}
