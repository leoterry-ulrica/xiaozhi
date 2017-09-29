package com.dist.bdf.model.dto.system.page;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.dist.bdf.base.dto.KeyValueDTO;

public class PageProjectResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<KeyValueDTO> projects = new LinkedList<KeyValueDTO>();
	
	public void add(Object key, Object value) {
		KeyValueDTO kv = new KeyValueDTO(key, value);
		projects.add(kv);
	}

	public List<KeyValueDTO> getProjects() {
		return projects;
	}

	public void setProjects(List<KeyValueDTO> projects) {
		this.projects = projects;
	}
}
