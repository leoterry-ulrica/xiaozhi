package com.dist.bdf.model.entity.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

@Entity
@Table(name = "DCM_PROCESSLOG")
public class DcmProcessLog extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String taskId;
	/**
	 * 操作类型。1：新增；-1：删除：2：发送；3：修改
	 */
	private Long actionType;
	private String fromUser;
	private String toUser;
	private Date dateCreated;
	private String comments;
	private String ip;
	private String operation;
	
	@Column(name = "TASKID")
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	@Column(name = "ACTIONTYPE")
	public Long getActionType() {
		return actionType;
	}
	public void setActionType(Long actionType) {
		this.actionType = actionType;
	}
	@Column(name = "FROMUSER")
	public String getFromUser() {
		return fromUser;
	}
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}
	@Column(name = "TOUSER")
	public String getToUser() {
		return toUser;
	}
	public void setToUser(String toUser) {
		this.toUser = toUser;
	}
	@Column(name = "DATECREATED")
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	@Column(name = "COMMENTS")
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	@Column(name = "IP")
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@Column(name = "OPERATION")
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DcmProcessLog [taskId=" + taskId + ", actionType=" + actionType + ", fromUser=" + fromUser + ", toUser="
				+ toUser + ", dateCreated=" + dateCreated + ", comments=" + comments + ", ip=" + ip + ", operation="
				+ operation + "]";
	}
}
