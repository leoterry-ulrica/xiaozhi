
package com.dist.bdf.model.dto.dcm;


import com.dist.bdf.base.dto.BaseDTO;

/**
 * @author weifj
 * @version 1.0，2016/03/21，weifj，创建案例类型
 */
public class CaseTypeDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 案例类型名称
	 */
	private String name;
	/**
	 * 案例类型显示名称
	 */
	private String displayName;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
