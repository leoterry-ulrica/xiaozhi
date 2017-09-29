package com.dist.bdf.facade.service.sga.domain.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.facade.service.sga.dao.SgaFileRecordDAO;
import com.dist.bdf.facade.service.sga.domain.SgaFileRecordDmn;
import com.dist.bdf.model.entity.sga.SgaFileRecord;

@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class SgaFileRecordDmnImpl extends GenericDmnImpl<SgaFileRecord, Long> implements SgaFileRecordDmn {

	@Autowired
	private SgaFileRecordDAO dao;

	@Override
	public GenericDAO<SgaFileRecord, Long> getDao() {
		
		return dao;
	}
	
}
