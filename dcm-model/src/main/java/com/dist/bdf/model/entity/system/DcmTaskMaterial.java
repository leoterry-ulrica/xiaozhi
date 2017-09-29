package com.dist.bdf.model.entity.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

@Entity
@Table(name = "DCM_TASK_MATERIAL")
public class DcmTaskMaterial extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String taskId;
	private String materialId;
	private Date dateCreated;
	
	@Column(name = "TASKID")
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Column(name = "MATERIALID")
	public String getMaterialId() {
		return materialId;
	}
	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
	
	@Column(name = "DATECREATED")
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DcmTaskMaterial [taskId=" + taskId + ", materialId=" + materialId + ", dateCreated=" + dateCreated
				+ "]";
	}
	
}
