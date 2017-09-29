package com.dist.bdf.facade.service.biz.domain.system;


import java.util.List;

import com.dist.bdf.base.domain.GenericDmn;

import com.dist.bdf.model.entity.system.DcmTask;

public interface DcmTaskDmn  extends GenericDmn<DcmTask, Long>{

	/**
	 * 添加子任务
	 * @param caseIdentifier 案例标识
	 * @param taskId 子任务id
	 * @param parentTaskId 父任务id，特指项目的管理节点
	 * @param taskName 任务名称
	 */
	//void addTask(String caseIdentifier, String taskId, String parentTaskId, String taskName);
	/**
	 * 根据任务id删除实体
	 * @param taskId
	 */
	void deleteTaskById(String taskId);
	/**
	 * 根据案例标识删除任务
	 * @param caseIdentifier
	 */
	void deleteTaskByCaseIdentifier(String caseIdentifier);
	/**
	 * 根据管理节点删除子任务
	 * @param parentTaskId
	 */
	void deleteTasksByParentId(String parentTaskId);
	/**
	 * 根据案例标识获取子任务，并根据时间排序
	 * @param caseIdentifier 案例标识
	 * @return
	 */
	List<DcmTask> getSubTasks(String caseIdentifier);
	
}
