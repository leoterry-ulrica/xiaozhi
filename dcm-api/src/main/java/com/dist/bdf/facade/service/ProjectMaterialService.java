package com.dist.bdf.facade.service;

import com.dist.bdf.base.page.Pagination;

/**
 * 项目资料服务，没有case概念
 * @author weifj
 *
 */
public interface ProjectMaterialService {

	/**
	 * 分页获取项目根目录下文件夹
	 * @param pageSize
	 * @param pageNo
	 * @param projectId
	 * @return
	 */
	Pagination getProjectFoldersPage(int pageSize, int pageNo, String realm, String projectId);
	/**
	 * 分页获取项目子文件夹
	 * @param pageSize
	 * @param pageNo
	 * @param realm
	 * @param parentFolderId 父文件夹id
	 * @return
	 */
	Pagination getProjectSubFoldersPage(int pageSize, int pageNo, String realm, String parentFolderId);
	/**
	 * 分页获取项目根目录下文档
	 * @param pageSize
	 * @param pageNo
	 * @param realm
	 * @param projectId 
	 * @return
	 */
	Pagination getProjectDocsPage(int pageSize, int pageNo, String realm, String projectId);
	/**
	 * 分页获取项目文件夹下的子文档
	 * @param pageSize
	 * @param pageNo
	 * @param realm
	 * @param parentFolderId 父文件夹id
	 * @return
	 */
	Pagination getProjectSubDocsPage(int pageSize, int pageNo, String realm, String parentFolderId);
}
