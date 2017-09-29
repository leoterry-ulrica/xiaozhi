package com.dist.bdf.facade.service.biz.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.facade.service.InformationService;
import com.dist.bdf.common.conf.ecm.ECMConf;
import com.dist.bdf.common.conf.ecm.ExtPropertyMaterialConf;
import com.dist.bdf.common.constants.FileTypeConstants;
import com.dist.bdf.manager.ecm.define.DataType;
import com.dist.bdf.manager.ecm.define.SimplePropertyFilter;
import com.dist.bdf.manager.ecm.security.ConnectionService;
import com.dist.bdf.manager.ecm.utils.FolderUtil;
import com.dist.bdf.manager.ecm.utils.SearchEngine;
import com.filenet.api.core.Folder;
import com.ibm.ecm.util.p8.P8Connection;

/**
 * 资讯服务
 * @author weifj
 *
 */
@Service("InformationService")
public class InformationServiceImpl implements InformationService {

	@Autowired
	private ConnectionService connService;
	@Autowired
	private ECMConf ecmConf;
	@Autowired
	private ExtPropertyMaterialConf extPropMaterialConf;
	@Autowired
	private FolderUtil folderUtil;
	@Autowired
	private SearchEngine searchEngine;
	
	@Override
	public Pagination searchInformations(int pageNo, int pageSize, String keyword) {
		
		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getPublicObjectStore(""));
		Folder infoRootFolder = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), this.ecmConf.getInformationDirRoot());
		
		return this.searchEngine.findSubDocsPage(p8conn.getObjectStore(), infoRootFolder.get_Id().toString(), null, pageNo, pageSize, keyword, true, false);
	}
	@Override
	public Pagination searchInformationsThumbnailByte(int pageNo, int pageSize, String keyword) {
		
		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getPublicObjectStore(""));
		Folder infoRootFolder = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), this.ecmConf.getInformationDirRoot());
		
		return this.searchEngine.findSubDocsPage(p8conn.getObjectStore(), infoRootFolder.get_Id().toString(), null, pageNo, pageSize, keyword, true, true);
	}
	@Override
	public Pagination searchInformationOfMaterial(int pageNo, int pageSize, String keyword) {
		
		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getPublicObjectStore(""));
		Folder infoRootFolder = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), this.ecmConf.getInformationDirRoot());
		List<SimplePropertyFilter> filters = new ArrayList<SimplePropertyFilter>();
		filters.add(new SimplePropertyFilter(this.extPropMaterialConf.getFileType(), DataType.SingleOfString, new String[]{FileTypeConstants.FILE_TYPE_MATERIAL}));

		return this.searchEngine.findSubDocsPage(p8conn.getObjectStore(), infoRootFolder.get_Id().toString(), filters, pageNo, pageSize, keyword, true, false);
	}
	
	@Override
	public Pagination searchInformationOfMaterialThumbnailByte(int pageNo, int pageSize, String keyword) {
		
		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getPublicObjectStore(""));
		Folder infoRootFolder = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), this.ecmConf.getInformationDirRoot());
		List<SimplePropertyFilter> filters = new ArrayList<SimplePropertyFilter>();
		filters.add(new SimplePropertyFilter(this.extPropMaterialConf.getFileType(), DataType.SingleOfString, new String[]{FileTypeConstants.FILE_TYPE_MATERIAL}));

		return this.searchEngine.findSubDocsPage(p8conn.getObjectStore(), infoRootFolder.get_Id().toString(), filters, pageNo, pageSize, keyword, true, true);
	}

	@Override
	public Pagination searchInformationOfNews(int pageNo, int pageSize, String keyword) {
		
		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getPublicObjectStore(""));
		Folder infoRootFolder = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), this.ecmConf.getInformationDirRoot());
		List<SimplePropertyFilter> filters = new ArrayList<SimplePropertyFilter>();
		filters.add(new SimplePropertyFilter(this.extPropMaterialConf.getFileType(), DataType.SingleOfString, new String[]{FileTypeConstants.FILE_TYPE_NEWS}));

		return this.searchEngine.findSubDocsPage(p8conn.getObjectStore(), infoRootFolder.get_Id().toString(), filters, pageNo, pageSize, keyword, true, false);
	}
	@Override
	public Pagination searchInformationOfNewsThumbnailByte(int pageNo, int pageSize, String keyword) {
		
		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getPublicObjectStore(""));
		Folder infoRootFolder = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), this.ecmConf.getInformationDirRoot());
		List<SimplePropertyFilter> filters = new ArrayList<SimplePropertyFilter>();
		filters.add(new SimplePropertyFilter(this.extPropMaterialConf.getFileType(), DataType.SingleOfString, new String[]{FileTypeConstants.FILE_TYPE_NEWS}));

		return this.searchEngine.findSubDocsPage(p8conn.getObjectStore(), infoRootFolder.get_Id().toString(), filters, pageNo, pageSize, keyword, true, true);
	}
}
