package com.dist.bdf.facade.service.biz.dao.impl;

import org.springframework.stereotype.Repository;

import com.dist.bdf.base.dao.hibernate.GenericDAOImpl;
import com.dist.bdf.facade.service.biz.dao.DcmRegionDAO;
import com.dist.bdf.model.entity.system.DcmRegion;

@Repository
public class DcmRegionDAOImpl  extends GenericDAOImpl<DcmRegion, Long> implements DcmRegionDAO {

}
