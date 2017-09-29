package com.dist.bdf.facade.service.biz.domain.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.facade.service.biz.dao.DcmTaskMaterialDAO;
import com.dist.bdf.facade.service.biz.domain.system.DcmTaskMaterialDmn;
import com.dist.bdf.model.entity.system.DcmTaskMaterial;


@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class DcmTaskMaterialDmnImpl  extends GenericDmnImpl<DcmTaskMaterial, Long> implements DcmTaskMaterialDmn {

	@Autowired
	private DcmTaskMaterialDAO taskMaterialDAO;
	//private GenericDAOImpl<DcmTaskMaterial, Long> taskMaterialDAO;
	
	@Override
	public GenericDAO<DcmTaskMaterial, Long> getDao() {
		
		return taskMaterialDAO;
	}

	@Override
	public void addTaskMaterial(String taskId, String materialId) {
		
		DcmTaskMaterial tm = new DcmTaskMaterial();
		tm.setMaterialId(materialId);
		tm.setTaskId(taskId);
		
		this.taskMaterialDAO.save(tm);
		
	}

	@Override
	public void deleteTaskMaterialById(String materialId) {
		
		this.taskMaterialDAO.removeByProperty("materialId", materialId);
	}

	@Override
	public void deleteMaterialByTaskId(String taskId) {
		
		this.taskMaterialDAO.removeByProperty("taskId", taskId);
	}

	@Override
	public List<DcmTaskMaterial> getMaterialByTaskId(String taskId) {
		
		return this.taskMaterialDAO.findByProperty("taskId", taskId);
	}
	
	@Override
	public boolean isExist(String resId) {
		
		return (null != this.getDao().findUniqueByProperty("materialId", resId));
	}
}
