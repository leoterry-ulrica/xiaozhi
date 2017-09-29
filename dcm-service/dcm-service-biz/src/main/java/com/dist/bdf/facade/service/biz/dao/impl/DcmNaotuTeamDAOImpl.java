package com.dist.bdf.facade.service.biz.dao.impl;

import org.springframework.stereotype.Repository;

import com.dist.bdf.base.dao.hibernate.GenericDAOImpl;
import com.dist.bdf.facade.service.biz.dao.DcmNaotuTeamDAO;
import com.dist.bdf.model.entity.system.DcmNaotuTeam;

@Repository
public class DcmNaotuTeamDAOImpl  extends GenericDAOImpl<DcmNaotuTeam, Long> implements DcmNaotuTeamDAO {

}
