package com.dist.bdf.facade.service.biz.domain.dcm;

import com.filenet.api.core.ObjectStore;

/**
 * 
 * @author weifj
 * @version 1.0，2016/04/20，weifj，创建文档和文件夹领域
 *
 */
public interface DocAndFolderDmn {

	/**
	 * 根据文档id获取文档实体
	 * @param docId
	 * @return
	 */
	//Document loadDocById(ObjectStore os, String docId);
	/**
	 * 根据版本系列id查询文档
	 * @param docVid
	 * @return
	 */
	/*@Deprecated
	Document loadDocByVersionSeriesId(String docVid);*/
	/**
	 * 根据版本系列id查询文档
	 * @param os 存储库
	 * @param docVid
	 * @return
	 */
	//Document loadDocByVersionSeriesId(ObjectStore os, String docVid);
	
	//List<Document> loadDocByVersionSeriesId(ObjectStore os ,String[] docVid);
	
	/**
	 * 根据文件夹id获取文件夹实体
	 * @param folderId
	 * @return
	 */
	//Object loadFolderById(ObjectStore os, String folderId);
	/**
	 * 全文检索文档类
	 * @param keyword 关键字
	 * @return
	 */
	//Object fullTextSearchOfDoc(int pageNo, int pageSize,String keyword);
	//Object fullTextSearchOfDoc(ObjectStore os, int pageNo, int pageSize,String keyword);
	//Object fullTextSearchOfDocRank(int pageNo, int pageSize,String keyword);
	//Object fullTextSearchOfDocRank(ObjectStore os, int pageNo, int pageSize,String keyword);
	Object fullTextSearchOfDocRank(String targetObjectStore, int pageNo, int pageSize,String keyword);
	/**
	 * 精确查询
	 * @param property 属性
	 * @param value 属性关键字
	 * @return
	 */
     Object preciseQueryOfDoc(ObjectStore os, int pageNo, int pageSize,String property, String value);
     /**
      * 创建文件夹
      * @param parentFolderId 文件夹id
      * @param newFolderName 新文件夹名称
      * @return
      */
     //Object createFolder(String userId, String password, String parentFolderId, String newFolderName);
     /**
      * 
      * @param userId
      * @param password
      * @param os
      * @param parentFolderId
      * @param newFolderName
      * @return
      */
     //Object createFolder(ObjectStore os, String parentFolderId, String newFolderName);
     /**
      * 创建case子文件夹
      * @param os
      * @param parentFolderId
      * @param newFolderName
      * @return
      */
     //Object createCaseSubFolder(ObjectStore os, String parentFolderId, String newFolderName);
     /**
      * 
      * @param os
      * @param parentFolder
      * @param newFolderName
      * @return
      */
     //Object createFolder(ObjectStore os, Folder parentFolder, String newFolderName);
     /**
      * 根据资源id获取空间域值
      * @param resId
      * @return
      */
     //String getDocResDomainCode(ObjectStore os, String resId);
     /**
      * 获取文档资源空间域编码
      * @param doc
      * @return
      */
     //String getDocResDomainCode(Document doc);
     /**
      * 获取资源类型编码
      * @param resId
      * @return
      */
     //String getDocResTypeCode(ObjectStore os, String resId);
     /**
      * 获取资源类型编码
      * @param doc
      * @return
      */
     //String getDocResTypeCode(Document doc);
     /**
      * 获取版本系列id
      * @param doc
      * @return
      */
     //String getDocVersionSeriesId(Document doc);
}
