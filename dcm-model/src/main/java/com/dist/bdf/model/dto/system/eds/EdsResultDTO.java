package com.dist.bdf.model.dto.system.eds;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 行政区域数据模型
 * @author weifj
 * @version 1.0，2016/04/11，weifj，创建行政区划
 *
 */
public class EdsResultDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String externalDataIdentifier = "1,0";
	private List<Property> properties = new LinkedList<Property>();
	
	public String getExternalDataIdentifier() {
		return externalDataIdentifier;
	}
	public void setExternalDataIdentifier(String externalDataIdentifier) {
		this.externalDataIdentifier = externalDataIdentifier;
	}
	public List<Property> getProperties() {
		return properties;
	}
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	public void addProperty(Property property) {
		
		this.properties.add(property);
	}
}
