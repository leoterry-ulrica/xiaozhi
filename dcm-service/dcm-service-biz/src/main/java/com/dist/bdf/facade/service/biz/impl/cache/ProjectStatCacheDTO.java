package com.dist.bdf.facade.service.biz.impl.cache;

import java.io.Serializable;
import java.util.List;

import com.dist.bdf.model.dto.system.ProjectStatDTO;

public class ProjectStatCacheDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<ProjectStatDTO> data;
	private Long startTime;
	
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public List<ProjectStatDTO> getData() {
		return data;
	}
	public void setData(List<ProjectStatDTO> data) {
		this.data = data;
	}
	
}
