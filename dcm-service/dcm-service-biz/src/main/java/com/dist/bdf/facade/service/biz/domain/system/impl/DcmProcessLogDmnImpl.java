package com.dist.bdf.facade.service.biz.domain.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.facade.service.biz.dao.DcmProcessLogDAO;
import com.dist.bdf.facade.service.biz.domain.system.DcmProcessLogDmn;
import com.dist.bdf.model.entity.system.DcmProcessLog;

@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class DcmProcessLogDmnImpl  extends GenericDmnImpl<DcmProcessLog, Long> implements DcmProcessLogDmn {

    @Autowired
    private DcmProcessLogDAO dao;
    //private GenericDAOImpl<DcmProcessLog, Long> dao;
    
	@Override
	public GenericDAO<DcmProcessLog, Long> getDao() {

		return dao;
	}

	
	
}
