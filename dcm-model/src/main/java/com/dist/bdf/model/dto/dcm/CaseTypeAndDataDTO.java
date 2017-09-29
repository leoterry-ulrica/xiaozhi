
package com.dist.bdf.model.dto.dcm;

import java.io.Serializable;
import java.util.List;

/**
 * @author weifj
 * @version 1.0，2016/05/11，weifj，把案例类型和案例数据进行合并
 */
public class CaseTypeAndDataDTO implements Serializable {

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
	 * 类型下面的案例数据
	 */
	private List<CaseDTO> cases;
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
	public List<CaseDTO> getCases() {
		return cases;
	}
	public void setCases(List<CaseDTO> cases) {
		this.cases = cases;
	}

}
