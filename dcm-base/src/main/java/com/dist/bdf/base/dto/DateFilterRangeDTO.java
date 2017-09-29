package com.dist.bdf.base.dto;

import java.io.Serializable;
import java.util.Date;
/**
 * 时间过滤范围DTO
 * @author weifj
 *
 */
public class DateFilterRangeDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DateFilterRangeDTO(String propertyName, Date beginTime, Date endTime) {
		
		this.propertyName = propertyName;
		this.beginTime = beginTime;
		this.endTime = endTime;
	}
	/**
	 * 属性名称
	 */
	private String propertyName;
	/**
	 * 开始时间
	 */
	private Date beginTime;
	/**
	 * 结束时间
	 */
	private Date endTime;
	
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
