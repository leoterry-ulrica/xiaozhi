package com.dist.bdf.model.dto.system;

import java.io.Serializable;

public class TaskMaterialDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String taskId;
	private String[] materialIds;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String[] getMaterialIds() {
		return materialIds;
	}
	public void setMaterialIds(String[] materialIds) {
		this.materialIds = materialIds;
	}
}
