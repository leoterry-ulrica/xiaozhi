package com.dist.bdf.facade.service.biz.domain.system.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.facade.service.biz.dao.DcmNaotuTeamDAO;
import com.dist.bdf.facade.service.biz.domain.system.DcmNaotuTeamDmn;
import com.dist.bdf.model.entity.system.DcmNaotuTeam;


@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class DcmNaotuTeamDmnImpl  extends GenericDmnImpl<DcmNaotuTeam, Long> implements DcmNaotuTeamDmn {

	@Autowired
	private DcmNaotuTeamDAO naotuTeamDao;

	@Override
	public GenericDAO<DcmNaotuTeam, Long> getDao() {
		
		return naotuTeamDao;
	}
}
