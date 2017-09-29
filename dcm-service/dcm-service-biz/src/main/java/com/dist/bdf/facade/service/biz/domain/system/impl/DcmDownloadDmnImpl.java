
package com.dist.bdf.facade.service.biz.domain.system.impl;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.facade.service.biz.dao.DcmDownloadDAO;
import com.dist.bdf.facade.service.biz.domain.system.DcmDownloadDmn;
import com.dist.bdf.model.entity.system.DcmDownload;

/**
 * @author weifj
 * @version 1.0，2016/02/23，weifj，创建组领域实现
 *
 */
@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class DcmDownloadDmnImpl extends GenericDmnImpl<DcmDownload,Long> implements DcmDownloadDmn {

	@Autowired
	private DcmDownloadDAO downloadDao;
	//private GenericDAOImpl<DcmDownload, Long> downloadDao;
	
	@Override
	public GenericDAO<DcmDownload, Long> getDao() {
		
		return downloadDao;
	}
	
	@Override
	public void delDownloadLogByDomainCode(String domainCode) {
		
		this.getDao().removeByProperty("domainCode", domainCode);
	}
	
	@Override
	public void delDownloadLogByResIds(String downloader, String[] resIds) {
		
		Map<String, Object[]> propertiesValues = new HashMap<String, Object[]>();
		propertiesValues.put("downloader", new String[]{downloader});
		propertiesValues.put("resId", resIds);
		
		this.getDao().removeByProperties(propertiesValues);
	}
}
