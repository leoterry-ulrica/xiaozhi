package com.dist.bdf.facade.service.biz.domain.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.facade.service.biz.dao.DcmTaskDAO;
import com.dist.bdf.facade.service.biz.domain.system.DcmTaskDmn;
import com.dist.bdf.model.entity.system.DcmTask;


@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class DcmTaskDmnImpl  extends GenericDmnImpl<DcmTask, Long> implements DcmTaskDmn {

	@Autowired
	private DcmTaskDAO taskDAO;
	//private GenericDAOImpl<DcmTask, Long> taskDAO;
	
	@Override
	public GenericDAO<DcmTask, Long> getDao() {
		
		return taskDAO;
	}

	@Override
	public void deleteTaskById(String taskId) {
		
		this.taskDAO.removeByProperty("taskId", taskId);
	}
	
	@Override
	public void deleteTaskByCaseIdentifier(String caseIdentifier) {
		this.taskDAO.removeByProperty("caseIdentifier", caseIdentifier);
	}

	@Override
	public void deleteTasksByParentId(String parentTaskId) {
		
		this.taskDAO.removeByProperty("parentTaskId", parentTaskId);
	}

	@Override
	public List<DcmTask> getSubTasks(String caseIdentifier) {
		
		return this.taskDAO.findByProperty("caseIdentifier", caseIdentifier, "dateCreated", true);
	}

	
}
