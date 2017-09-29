/**
 * 
 */
package com.dist.bdf.base.dto;

/**
 * 简单列表Dto
 * 如果需要通过一个属性进行排序，返回列表，则使用此Dto
 * @author 李其云
 * @version 1.0 2015-8-26
 */
public class SimpleListDTO {

	/**
	 * 排序属性名
	 */
	private String sorterName;
	/**
	 * 是否升序排列
	 */
	private boolean isAsc;
	/**
	 * @return 排序属性名
	 */
	public String getSorterName() {
		return sorterName;
	}
	/**
	 * @param sorterName 排序属性名
	 */
	public void setSorterName(String sorterName) {
		this.sorterName = sorterName;
	}
	/**
	 * @return 升序返回true，降序返回false
	 */
	public boolean isAsc() {
		return isAsc;
	}
	/**
	 * @param isAsc 是否升序排列，升序为true，降序为false
	 */
	public void setAsc(boolean isAsc) {
		this.isAsc = isAsc;
	}

}
