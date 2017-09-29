package com.dist.bdf.facade.service.biz.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.facade.service.AdminService;

@Service("AdminService")
@Transactional(propagation = Propagation.REQUIRED)
public class AdminServiceImpl implements AdminService {

	private static Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
	
	@Override
	public Object getGlobalStat(String realm, String admin, String pwd, String operate, Long timeStart, long timeEnd) {
		
		logger.info(">>>统计文档个数......");
		
		return null;
	}

}
