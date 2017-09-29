package com.dist.bdf.model.dto.system;

import java.io.Serializable;
import java.util.List;

public class RegionTreeDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private List<RegionTreeDTO> children;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<RegionTreeDTO> getChildren() {
		return children;
	}
	public void setChildren(List<RegionTreeDTO> children) {
		this.children = children;
	}
}
