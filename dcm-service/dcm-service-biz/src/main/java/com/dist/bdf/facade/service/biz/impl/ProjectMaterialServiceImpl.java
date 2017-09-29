package com.dist.bdf.facade.service.biz.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.facade.service.ProjectMaterialService;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.common.conf.ecm.ECMConf;
import com.dist.bdf.manager.ecm.security.ConnectionService;
import com.dist.bdf.manager.ecm.utils.FolderUtil;
import com.dist.bdf.model.dto.dcm.PageParaDTO;
import com.filenet.api.core.Folder;
import com.ibm.ecm.util.p8.P8Connection;

/**
 * 项目材料服务，不再引入case概念
 * @author weifj
 *
 */
@Service("ProjectMaterialService")
@Transactional(propagation = Propagation.REQUIRED)
public class ProjectMaterialServiceImpl extends CommonMaterialServiceImpl implements ProjectMaterialService {

	@Autowired
	private FolderUtil folderUtil;
	@Autowired
	private ECMConf ecmConf;
	@Autowired
	private ConnectionService connectionService;
	
	/**
	 * 获取项目根目录文件夹
	 * @param realm
	 * @param projectId
	 * @return
	 */
	private Folder getProjectRootFolder(String realm, String projectId) {
		
		String folderPath = (ecmConf.getProjectDirRoot().endsWith("/"))
				? (ecmConf.getProjectDirRoot() + projectId)
				: (ecmConf.getProjectDirRoot() + "/" + projectId);
				
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		return this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), folderPath);
	}
	@Override
	public Pagination getProjectFoldersPage(int pageSize, int pageNo, String realm, String projectId) {
		
		Folder projectFolder = this.getProjectRootFolder(realm, projectId);
		
		PageParaDTO page = new PageParaDTO();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setRealm(realm);
		page.setFolderId(projectFolder.get_Id().toString());
		
		return super.getSubFoldersPage(page);
	}
	@Override
	public Pagination getProjectDocsPage(int pageSize, int pageNo, String realm, String projectId) {
		
        Folder projectFolder = this.getProjectRootFolder(realm, projectId);
		
		PageParaDTO page = new PageParaDTO();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setRealm(realm);
		page.setFolderId(projectFolder.get_Id().toString());
		
		return super.getSubDocsPage(page);
	}
	@Override
	public Pagination getProjectSubFoldersPage(int pageSize, int pageNo, String realm, String parentFolderId) {
		
		PageParaDTO page = new PageParaDTO();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setRealm(realm);
		page.setFolderId(parentFolderId);
		
		return super.getSubFoldersPage(page);
	}
	@Override
	public Pagination getProjectSubDocsPage(int pageSize, int pageNo, String realm, String parentFolderId) {
		PageParaDTO page = new PageParaDTO();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setRealm(realm);
		page.setFolderId(parentFolderId);
		
		return super.getSubDocsPage(page);
	}
}
