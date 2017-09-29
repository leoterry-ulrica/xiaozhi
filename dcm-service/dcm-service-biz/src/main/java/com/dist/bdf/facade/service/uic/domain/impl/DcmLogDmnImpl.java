package com.dist.bdf.facade.service.uic.domain.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.facade.service.uic.dao.DcmLogDAO;
import com.dist.bdf.facade.service.uic.domain.DcmLogDmn;
import com.dist.bdf.model.entity.system.DcmLog;


@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class DcmLogDmnImpl extends GenericDmnImpl<DcmLog, Long> implements DcmLogDmn {

	@Autowired
	private DcmLogDAO logDAO;
	//private GenericDAOImpl<DcmTask, Long> taskDAO;
	
	@Override
	public GenericDAO<DcmLog, Long> getDao() {
		
		return logDAO;
	}
	
}
