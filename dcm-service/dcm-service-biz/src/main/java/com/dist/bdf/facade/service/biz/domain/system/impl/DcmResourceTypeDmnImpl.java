
package com.dist.bdf.facade.service.biz.domain.system.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.facade.service.biz.dao.DcmResourceTypeDAO;
import com.dist.bdf.facade.service.biz.domain.system.DcmResourceTypeDmn;
import com.dist.bdf.model.entity.system.DcmResourceType;


/**
 * @author weifj
 * @version 1.0，2016/02/23，weifj，创建组领域实现
 *
 */
@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class DcmResourceTypeDmnImpl extends GenericDmnImpl<DcmResourceType,Long> implements DcmResourceTypeDmn {

	@Autowired
	private DcmResourceTypeDAO dao;
	//private GenericDAOImpl<DcmResourceType, Long> dao;
	
	@Override
	public GenericDAO<DcmResourceType, Long> getDao() {

		return dao;
	}

	@Override
	public Object addType(DcmResourceType resType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object updateType(DcmResourceType resType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object deleteType(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object deleteTypeByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object deleteTypeByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getResTypeByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getResTypeByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}


}
