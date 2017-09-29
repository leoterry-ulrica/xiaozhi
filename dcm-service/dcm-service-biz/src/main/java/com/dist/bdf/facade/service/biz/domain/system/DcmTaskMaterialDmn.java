package com.dist.bdf.facade.service.biz.domain.system;


import java.util.List;

import com.dist.bdf.base.domain.GenericDmn;
import com.dist.bdf.model.entity.system.DcmTaskMaterial;

public interface DcmTaskMaterialDmn  extends GenericDmn<DcmTaskMaterial, Long>{

	/**
	 * 添加任务和材料的关联
	 * @param taskId
	 * @param materialId
	 */
	void addTaskMaterial(String taskId, String materialId);
	/**
	 * 根据材料ID删除任务下的附件关联关系
	 * @param materialId
	 */
	void deleteTaskMaterialById(String materialId);
	/**
	 * 根据任务id删除任务下的材料关联关系
	 * @param taskId
	 */
	void deleteMaterialByTaskId(String taskId);
	/**
	 * 根据任务id获取材料列表
	 * @param taskId
	 * @return
	 */
	List<DcmTaskMaterial> getMaterialByTaskId(String taskId);
	/**
	 * 判断文件是否被事项引用
	 * @param resId
	 * @return
	 */
	boolean isExist(String resId);
	
}
