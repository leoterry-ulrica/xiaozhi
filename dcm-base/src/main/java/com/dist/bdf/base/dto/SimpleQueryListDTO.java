/**
 * 
 */
package com.dist.bdf.base.dto;

/**
 * 简单过滤列表Dto
 * 如果需要通过一个属性进行查询，并通过一个属性进行排序，返回列表，则使用此Dto
 * @author 李其云
 * @version 1.0 2015-8-26
 */
public class SimpleQueryListDTO extends SimpleListDTO {

	/**
	 * 过滤属性名称
	 */
	private String filterName;
	/**
	 * 过滤属性取值
	 */
	private String filterValue;
	/**
	 * @return the filterName
	 */
	public String getFilterName() {
		return filterName;
	}
	/**
	 * @param filterName the filterName to set
	 */
	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}
	/**
	 * @return the filterValue
	 */
	public String getFilterValue() {
		return filterValue;
	}
	/**
	 * @param filterValue the filterValue to set
	 */
	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}
}
