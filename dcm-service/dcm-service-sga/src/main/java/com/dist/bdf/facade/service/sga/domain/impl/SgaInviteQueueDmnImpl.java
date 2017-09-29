package com.dist.bdf.facade.service.sga.domain.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.facade.service.sga.dao.SgaInviteQueueDAO;
import com.dist.bdf.facade.service.sga.domain.SgaInviteQueueDmn;
import com.dist.bdf.model.entity.sga.SgaInviteQueue;


@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class SgaInviteQueueDmnImpl extends GenericDmnImpl<SgaInviteQueue, Long> implements SgaInviteQueueDmn {

	@Autowired
	private SgaInviteQueueDAO dao;
	
	@Override
	public GenericDAO<SgaInviteQueue, Long> getDao() {
		
		return dao;
	}
	
}
