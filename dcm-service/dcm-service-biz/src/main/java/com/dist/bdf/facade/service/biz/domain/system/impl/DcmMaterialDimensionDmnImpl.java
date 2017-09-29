package com.dist.bdf.facade.service.biz.domain.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.facade.service.biz.dao.DcmMaterialDimensionDAO;
import com.dist.bdf.facade.service.biz.domain.system.DcmMaterialDimensionDmn;
import com.dist.bdf.model.entity.system.DcmMaterialDimension;

/**
 * 资源社交化领域
 * @author weifj
 * @version 1.0，2016/04/19，weifj，创建
 */

@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class DcmMaterialDimensionDmnImpl extends GenericDmnImpl<DcmMaterialDimension, Long>implements DcmMaterialDimensionDmn {

	@Autowired
	private DcmMaterialDimensionDAO mdDao;
	//private GenericDAOImpl<DcmMaterialDimension, Long> mdDao;
	
	@Override
	public GenericDAO<DcmMaterialDimension, Long> getDao() {
		
		return mdDao;
	}

	@Override
	public Object getSubLevelByParentId(Long id) {
		
		return this.getDao().findByProperty("parentId", id, "orderId", true);
	}

	@Override
	public Object getSubLevelByParentName(String name) {
	
		return this.getDao().findByProperty("name", name, "orderId", true);
	}


}
