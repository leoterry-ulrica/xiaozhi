package com.dist.bdf.model.dto.system;

import java.io.Serializable;

public class TaskProcessLogDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String taskId;
	/**
	 * 1：新增；-1：删除：2：发送；3：修改
	 */
	private Long actionType;
	private String fromUser;
	private String[] toUser;
	private String comments;
	/**
	 * 操作内容。
	 * 
	 {
    "taskId":"00202020",
    "actionType":0,
    "fromUser":"weifj",
    "toUser":[
        "张晶"
    ],
    "operation":""files":[{"id":"1100","op":-1,"name":"文件名称1"},{"id":"1200","op":1,"name":"文件名称2"},{"id":"1300","op":2,"name":"文件名称3","vid":"版本系列id"},{"id":"1300","op":3,"name":"文件名称4","vid":"版本系列id"}],"properties"：[{"id":"属性标识符","alias":"属性名称","oldValue":"老值","newValue":"新值"}]",
    "comments":"备注信息"
   }
  // 说明：files里面的op表示操作类型，-1表示删除文件；1表示新增文件；2表示添加新版本；3表示删除文档版本；vid是在2和3的情况下才有，表示版本系列号
   *           properties：属性修改情况，oldValue表示老值，newValue表示新值
	 */
	private String operation;
	private String ip;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public Long getActionType() {
		return actionType;
	}
	public void setActionType(Long actionType) {
		this.actionType = actionType;
	}
	public String getFromUser() {
		return fromUser;
	}
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}
	public String[] getToUser() {
		return toUser;
	}
	public void setToUser(String[] toUser) {
		this.toUser = toUser;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	
}
