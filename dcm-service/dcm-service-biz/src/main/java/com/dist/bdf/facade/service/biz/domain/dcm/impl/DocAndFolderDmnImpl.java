package com.dist.bdf.facade.service.biz.domain.dcm.impl;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.common.conf.ecm.ECMConf;
import com.dist.bdf.common.conf.ecm.ExtPropertyMaterialConf;
import com.dist.bdf.facade.service.GroupService;
import com.dist.bdf.facade.service.UserOrgService;
import com.dist.bdf.facade.service.biz.domain.dcm.DocAndFolderDmn;
import com.dist.bdf.facade.service.biz.task.ComputeDocSocialDataTask;
import com.dist.bdf.manager.ecm.security.ConnectionService;
import com.dist.bdf.manager.ecm.utils.DistSocialUtil;
import com.dist.bdf.manager.ecm.utils.DocumentUtil;
import com.dist.bdf.manager.ecm.utils.FolderUtil;
import com.dist.bdf.manager.ecm.utils.SearchEngine;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
import com.dist.bdf.model.dto.dcm.DocumentSortDTO;
import com.filenet.api.core.Document;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.ibm.ecm.util.p8.P8Connection;

/**
 * 
 * @author weifj
 * @version 1.0，2016/04/20，weifj，创建实现体
 */
@Service
public class DocAndFolderDmnImpl implements DocAndFolderDmn {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ECMConf ecmConf;
	@Autowired
	private ConnectionService connectionService;
   @Autowired
   private SearchEngine searchEngine;
  /* @Autowired
   private FolderUtil folderUtil;*/
   /*@Autowired
   private DocumentUtil docUtil;*/
   @Autowired
   private DistSocialUtil socialUtil;
  /* @Autowired
   private ExtPropertyMaterialConf extPropMaterialConf;*/
   @Autowired
   private GroupService groupService;
   @Autowired
   private UserOrgService userService;
  
  /* @Override
   public Document loadDocById(ObjectStore os, String docId) {
	   
	   Document findDoc = docUtil.loadById(os, docId);
	   //this.connectionService.release();
	   return findDoc;
   }*/
/*   @Deprecated
   @Override
   public Document loadDocByVersionSeriesId(String docVid) {
	   
	   this.connectionService.initialize();
	   return this.docUtil.loadByVersionSeriesId(this.connectionService.getDefaultOS(), docVid);
	   
   }*/
  /* @Override
   public Document loadDocByVersionSeriesId(ObjectStore os, String docVid) {
	   
	   return this.docUtil.loadByVersionSeriesId(os, docVid);
   }*/
  /* @Override
   public List<Document> loadDocByVersionSeriesId(ObjectStore os, String[] docVid) {
	   
	   List<Document> docs = this.searchEngine.searchAllWithProperty(os, this.ecmConf.getDefaultDocumentClass(), "VersionSeries ", docVid);
	   
	   return docs;
   }*/
  /* @Override
   public Folder loadFolderById(ObjectStore os, String folderId) {

	   Folder findFolder = this.folderUtil.loadById(os, folderId);
	   this.connectionService.release();
	   return findFolder;
   }*/
	/*@Override
	public Pagination fullTextSearchOfDoc(int pageNo, int pageSize, String keyword) {
	
		connectionService.initialize();
			Pagination docs = searchEngine.fullTextSearch(connectionService.getDefaultOS(), ecmConf.getDefaultDocumentClass(),false, null, null, null, null, keyword, pageNo, pageSize, true);
		
		connectionService.release();
		
		return (Pagination) this.fullTextSearchOfDoc(connectionService.getDefaultOS(), pageNo, pageSize, keyword);
	}*/
	/*@Override
	public Pagination fullTextSearchOfDoc(ObjectStore os, int pageNo, int pageSize,String keyword) {
		
		Pagination docs = searchEngine.fullTextSearch(os, ecmConf.getDefaultDocumentClass(),false, null, null, null, null, keyword, pageNo, pageSize, true);
		return docs;
	}*/
	/*@Override
	public Object fullTextSearchOfDocRank(int pageNo, int pageSize,String keyword) {
		
		connectionService.initialize();
		return this.searchEngine.fullTextSearchOfDocRank(this.connectionService.getDefaultOS(), pageNo, pageSize, keyword);
		
	}*/
	/*@SuppressWarnings("unchecked")
	@Override
	public Object fullTextSearchOfDocRank(ObjectStore os, int pageNo, int pageSize,String keyword) {
		// 第一层排序，根据关键字的匹配度
		Pagination pageData = this.searchEngine.fullTextSearchOfDocRank(os, pageNo, pageSize, keyword);
		// 第二层排序，名字匹配度优先，然后文档的下载数和收藏数之和进行排序，如果下载数和收藏数之和相等，则根据文档的创建时间
		List<DocumentDTO> docs = (List<DocumentDTO>) pageData.getData();
		
		if(null == docs || 0 == docs.size()) {
			logger.info(">>>没有检索到任何数据，关键词：[{}]", keyword);
			return null;
		
		}
		
		final ForkJoinPool pool = new ForkJoinPool(6);
		final ComputeDocSocialDataTask task = new ComputeDocSocialDataTask (os, this.socialUtil, docs);
		 
		List<DocumentSortDTO> result = pool.invoke(task);
		logger.info(">>>计算完成后结果集大小：[{}]", result.size());
		pageData.setData(result);
		logger.info(">>>根据设定的排序规则，对结果集进行重新排序...");
		Collections.sort(result);

		return pageData;
	}*/

	@Override
	public Object fullTextSearchOfDocRank(String targetObjectStore, int pageNo, int pageSize, String keyword) {
/*
		P8Connection p8conn = this.connectionService.getP8Connection(targetObjectStore);
		// 第一层排序，根据关键字的匹配度
		Pagination pageData = this.searchEngine.fullTextSearchOfDocRank(p8conn.getObjectStore(), pageNo, pageSize,
				keyword);
		// 第二层排序，名字匹配度优先，然后文档的下载数和收藏数之和进行排序，如果下载数和收藏数之和相等，则根据文档的创建时间
		@SuppressWarnings("unchecked")
		List<DocumentDTO> docs = (List<DocumentDTO>) pageData.getData();

		if (null == docs || 0 == docs.size()) {
			logger.info(">>>没有检索到任何数据，关键词：[{}]", keyword);
			return pageData;
		}

		final ForkJoinPool pool = new ForkJoinPool(6);
		final ComputeDocSocialDataTask task = new ComputeDocSocialDataTask(targetObjectStore, this.connectionService,
				this.socialUtil, this.userService, this.groupService, docs, keyword);

		List<DocumentSortDTO> result = pool.invoke(task);
		logger.info(">>>计算完成后结果集大小：[{}]", result.size());
		pageData.setData(result);
		logger.info(">>>根据设定的排序规则，对结果集进行重新排序...");
		Collections.sort(result);

		return pageData;*/
		return null;
	}

	@Override
	public Pagination preciseQueryOfDoc(ObjectStore os,int pageNo, int pageSize,String property, String value) {
		
		Pagination docs =  searchEngine.searchWithProperty(os, ecmConf.getDefaultDocumentClass(), property, value,pageNo,pageSize,true);

		return docs;
	}
	
	/*@Override
	public DocumentDTO createFolder(String userId, String password, String parentFolderId, String newFolderName) {
		
		P8Connection p8Conn = this.connectionService.getP8Connection(userId, password);
		Folder newFolder = folderUtil.add(p8Conn.getObjectStore(), parentFolderId, newFolderName);
		if(null == newFolder){
			throw new BusinessException("createFolder 创建文件夹失败。");
		}
		return this.folderUtil.folder2dto(newFolder);
	
	}*/
	/*@Deprecated
	@Override
	public DocumentDTO createFolder(ObjectStore os, String parentFolderId, String newFolderName) {
		
		Folder newFolder = folderUtil.add(os, parentFolderId, newFolderName);
		if(null == newFolder){
			throw new BusinessException("createFolder 创建文件夹失败。");
		}
		return this.folderUtil.folder2dto(newFolder);

	}*/
	/*@Deprecated
	@Override
	public  Object createCaseSubFolder(ObjectStore os, String parentFolderId, String newFolderName) {
		
		Folder newFolder = folderUtil.addCaseSubFolder(os, parentFolderId, newFolderName);
		if(null == newFolder){
			throw new BusinessException("createFolder 创建文件夹失败。");
		}
		return this.folderUtil.folder2dto(newFolder);
	}*/
	/*@Override
	public  DocumentDTO createFolder(ObjectStore os, Folder parentFolder, String newFolderName) {
		
		Folder newFolder = folderUtil.add(os, parentFolder, newFolderName);
		if(null == newFolder) {
			throw new BusinessException("createFolder 创建文件夹失败。");
		}
		return this.folderUtil.folder2dto(newFolder);
		
	}*/
	/*@Override
	public String getDocResDomainCode(ObjectStore os, String resId) {

		Document doc = this.loadDocById(os, resId);
		if(null == doc) return "";
		
		String domainCode = doc.getProperties().isPropertyPresent(this.extPropMaterialConf.getSpatialDomain())? doc.getProperties().getStringValue(this.extPropMaterialConf.getSpatialDomain()) : "";
		
		return domainCode;
	}*/
	/*@Override
	public  String getDocResDomainCode(Document doc) {
		String domainCode = doc.getProperties().isPropertyPresent(this.extPropMaterialConf.getSpatialDomain())? doc.getProperties().getStringValue(this.extPropMaterialConf.getSpatialDomain()) : "";
		return domainCode;
	}*/
	/*@Override
	public String getDocResTypeCode(ObjectStore os, String resId) {
		
		Document doc = this.loadDocById(os, resId);
		if(null == doc) return "";
		
		String resTypeCode = doc.getProperties().isPropertyPresent(this.extPropMaterialConf.getResourceType())? doc.getProperties().getStringValue(this.extPropMaterialConf.getResourceType()) : "";
		return resTypeCode;
		
	}*/
	/*@Override
	public String getDocResTypeCode(Document doc) {
		
		if(null == doc) return "";
		
		String resTypeCode = doc.getProperties().isPropertyPresent(this.extPropMaterialConf.getResourceType())? doc.getProperties().getStringValue(this.extPropMaterialConf.getResourceType()) : "";
		return resTypeCode;
	}*/
	
/*	@Override
	public String getDocVersionSeriesId(Document doc) {
		
		if(null == doc) return "";
		
		return doc.get_VersionSeries().get_Id().toString();//.getProperties().isPropertyPresent("VersionSeries")? doc.getProperties().getStringValue("VersionSeries") : "";
	}*/
	
		
}
