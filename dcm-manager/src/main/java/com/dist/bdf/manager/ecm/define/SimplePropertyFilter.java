package com.dist.bdf.manager.ecm.define;

import java.io.Serializable;
/**
 * 属性过滤器
 * @author weifj
 *
 */
public class SimplePropertyFilter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 属性名称
	 */
	private String propertyName;
	/**
	 * 属性类型，包括ecm的数据类型（类：DataType），如ListOfString
	 */
	private String propertyType;
	/**
	 * 属性过滤的值
	 */
	private Object[] values;
	
	public SimplePropertyFilter(String propertyName, String propertyType, Object[] values) {
		this.propertyName = propertyName;
		this.propertyType = propertyType;
		this.values = values;
	}
	/**
	 * 获取属性名称
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}
	/**
	 * 设置属性名称
	 * @param propertyName the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	/**
	 * 获取属性类型，包括ecm的数据类型，如ListOfString
	 * @return the propertyType
	 */
	public String getPropertyType() {
		return propertyType;
	}
	/**
	 * 设置属性类型，包括ecm的数据类型，如ListOfString
	 * @param propertyType the propertyType to set
	 */
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
	/**
	 * 获取属性过滤的值
	 * @return the values
	 */
	public Object[] getValues() {
		return values;
	}
	/**
	 * 设置属性过滤的值
	 * @param values the values to set
	 */
	public void setValues(Object[] values) {
		this.values = values;
	}

}
