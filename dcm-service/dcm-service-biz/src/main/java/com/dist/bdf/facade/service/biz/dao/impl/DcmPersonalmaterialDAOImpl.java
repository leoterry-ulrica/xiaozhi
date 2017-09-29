package com.dist.bdf.facade.service.biz.dao.impl;

import org.springframework.stereotype.Repository;

import com.dist.bdf.base.dao.hibernate.GenericDAOImpl;
import com.dist.bdf.facade.service.biz.dao.DcmPersonalmaterialDAO;
import com.dist.bdf.model.entity.system.DcmPersonalmaterial;

@Repository
public class DcmPersonalmaterialDAOImpl extends GenericDAOImpl<DcmPersonalmaterial, Long> implements DcmPersonalmaterialDAO {

}
