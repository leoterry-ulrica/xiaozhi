
package com.dist.bdf.facade.service.biz.domain.system.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.common.conf.ecm.ECMConf;
import com.dist.bdf.facade.service.biz.dao.DcmPersonalmaterialDAO;
import com.dist.bdf.facade.service.biz.domain.system.DcmPersonalMaterialDmn;
import com.dist.bdf.manager.ecm.utils.FolderUtil;
import com.dist.bdf.manager.ecm.utils.SearchEngine;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
import com.dist.bdf.model.entity.system.DcmPersonalmaterial;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;

/**
 * @author weifj
 * @version 1.0，2016/02/24，weifj，创建角色领域
 */
@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class DcmPersonalMaterialDmnImpl extends GenericDmnImpl<DcmPersonalmaterial, Long> implements DcmPersonalMaterialDmn {

	@Autowired
	private DcmPersonalmaterialDAO perMaterialDao;
	//private GenericDAOImpl<DcmPersonalmaterial, Long> perMaterialDao;
	@Autowired
	private ECMConf ecmConf;
	@Autowired
	private SearchEngine searchEngine;
	@Autowired
	private FolderUtil folderUtil;

	@Override
	public GenericDAO<DcmPersonalmaterial, Long> getDao() {
		
		return perMaterialDao;
	}

	@Override
	public DcmPersonalmaterial getByResId(String resId) {
		
		return this.getDao().findUniqueByProperty("resId", resId);
	}
	@Override
	public List<DocumentDTO> getPersonalPackages(ObjectStore os,String userCode) {
		
		//this.connectionService.initialize();
		
		List<DocumentDTO> data = this.searchEngine.findSubFoldersAndDocumentsByFolderPath(os, getPersonalPath(userCode));
		
		//this.connectionService.release();
		return data;
	}
	@Override
	public String getPersonalPath(String userCode){
		
		logger.info("断言用户编码不能为空。");
		Assert.notNull(userCode);
		
        String personalDirRoot =  ecmConf.getPersonalDirRoot();
		
		String personalPath = (personalDirRoot.endsWith("/"))? ecmConf.getPersonalDirRoot()+userCode :  ecmConf.getPersonalDirRoot()+"/"+userCode ;

		return personalPath;
	}
	
	@Override
	public Folder getPersonalFolder(ObjectStore os, String userCode) {
		
		Folder folder = this.folderUtil.loadAndCreateByPath(os, getPersonalPath(userCode));
		return folder;
		
	}
}
