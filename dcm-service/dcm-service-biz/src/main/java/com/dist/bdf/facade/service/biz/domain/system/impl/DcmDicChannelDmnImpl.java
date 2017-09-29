package com.dist.bdf.facade.service.biz.domain.system.impl;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.dao.Sort;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.facade.service.biz.dao.DcmDicChannelDAO;
import com.dist.bdf.facade.service.biz.domain.system.DcmDicChannelDmn;
import com.dist.bdf.model.entity.system.DcmDicChannel;


@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class DcmDicChannelDmnImpl  extends GenericDmnImpl<DcmDicChannel, Long> implements DcmDicChannelDmn {

	@Autowired
	private DcmDicChannelDAO dao;

	@Override
	public GenericDAO<DcmDicChannel, Long> getDao() {
		
		return dao;
	}
	
	@Override
	public List<DcmDicChannel> find() {
	
		Sort isBuildInSort = new Sort("isBuildIn", Sort.DESC);
		Sort createTimeSort = new Sort("createTime", Sort.ASC);
		List<Sort> sorts = new ArrayList<Sort>();
		sorts.add(isBuildInSort);
		sorts.add(createTimeSort);
		
		return this.getDao().find(sorts);
	}

	
}
