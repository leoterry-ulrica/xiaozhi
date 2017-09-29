package com.dist.bdf.manager.ecm.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.utils.FileUtil;
import com.dist.bdf.base.utils.StringUtil;
// import com.dist.bdf.cache.DistCacheManager;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
import com.dist.bdf.model.dto.dcm.DocumentIdDTO;
import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.constants.ClassNames;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;

/**
 * ce文件夹工具类
 * @author weifj
 * @version 1.0，2016/05/13，weifj，创建ce文件夹工具类
 */
@Component
public class FolderUtil {

	private final Logger logger = (Logger) LoggerFactory.getLogger(getClass());
	
	/*@Autowired
	private DistCacheManager cacheManager;*/
	@Autowired
	private DistSocialUtil distSocialUtil;
	
	/**
	 * 拼接文件夹
	 * @param path
	 * @param folderName
	 * @return
	 */
	protected static String concatPath(String path, String folderName) {
		return path + (path.endsWith(FileUtil.S_SYMBL) ? folderName : FileUtil.S_SYMBL + folderName);
	}
	
	/**
	 * 新建默认文件夹实例
	 * @param os
	 * @return
	 */
	protected Folder createInstance(ObjectStore os) {
		
		return Factory.Folder.createInstance(os, ClassNames.FOLDER, null);
	}
	/**
	 * 根据文件夹类名称创建
	 * @param os
	 * @param folderClassSymbolName
	 * @return
	 */
   protected Folder createInstance(ObjectStore os, String folderClassSymbolName) {
		
		return Factory.Folder.createInstance(os, folderClassSymbolName, null);
	}
   /**
    * 创建案例文件夹
    * @param os
    * @return
    */
    public Folder createCaseFolderInstance(ObjectStore os) {
	   
	   return createInstance(os, "CmAcmCaseFolder");
   }
   /**
    * 创建案例子文件夹
    * @param os
    * @return
    */
   public Folder createCaseSubFolderInstance(ObjectStore os) {
	   
	   return createInstance(os, "CmAcmCaseSubfolder");
   }
	/**
	 * 根据路径和文件夹名称查找文件夹
	 * @param os
	 * @param pathAndFolderName
	 * @return
	 */
	public Folder fetchInstanceByPath(ObjectStore os, String pathAndFolderName) {

		return Factory.Folder.fetchInstance(os, pathAndFolderName, null);
	}
	/**
	 * 根据文件夹id检索文件夹
	 * @param os
	 * @param folderId
	 * @return
	 */
	public Folder fetchInstanceById(ObjectStore os, String folderId) {
	
		return Factory.Folder.fetchInstance(os, new Id(folderId), null);
	}
	/**
	 * 添加文件夹
	 * @param os
	 * @param pathAndFolderName 新文件夹路径和名称
	 * @return
	 */
	public Folder add(ObjectStore os, String pathAndFolderName) {

		String[] subFolders = pathAndFolderName.split(FileUtil.S_SYMBL);
		String currentPath = "";
		Folder currentFolder = null;
		Folder rootFolder = os.get_RootFolder();
		for (String subFolderName : subFolders) {
			if (StringUtils.isEmpty(subFolderName)) {
				continue;
			}
			currentPath += "/" + subFolderName;
			try {
				currentFolder = this.loadByPath(os, currentPath);
			} catch (Exception e) {
				logger.warn(e.getMessage());
				currentFolder = null;
			}
			if (null == currentFolder) {
				rootFolder = add(os, rootFolder, subFolderName);
			} else {
				rootFolder = currentFolder;
			}
		}
		return rootFolder;
		/*
		 * int nameStart = pathAndFolderName.lastIndexOf(FileUtil.S_SYMBL); if
		 * (nameStart <= 1) return add(os, os.get_RootFolder(),
		 * pathAndFolderName); else return addByPath(os,
		 * pathAndFolderName.substring(0, nameStart),
		 * pathAndFolderName.substring(nameStart + 1));
		 */
	}
	/**
	 * 在指定目录下创建文件夹
	 * @param os
	 * @param path
	 * @param folderName
	 * @return
	 */
	public Folder addByPath(ObjectStore os, String path, String folderName) {
		
		Folder parentFolder = loadByPath(os, path);
		
		return add(os, parentFolder, folderName);
	}
	/**
	 * 根据父文件夹id，创建子文件夹
	 * @param os
	 * @param parentFolderId
	 * @param folderName
	 * @return
	 */
	public Folder add(ObjectStore os, String parentFolderId, String folderName) {
		Folder parentFolder;
		if (StringUtil.isNullOrEmpty(parentFolderId))
			parentFolder = os.get_RootFolder();
		else
			parentFolder = loadById(os, parentFolderId);

		logger.debug(">>>parent folder:id=[{}],path=[{}] load ok", parentFolder.get_Id(), parentFolder.get_PathName());
		return add(os, parentFolder, folderName);
	}
	/**
	 * 创建case子文件夹
	 * @param os
	 * @param parentFolderId
	 * @param folderName
	 * @return
	 */
	public Folder addCaseSubFolder(ObjectStore os, String parentFolderId, String folderName) {
		Folder parentFolder;
		if (StringUtil.isNullOrEmpty(parentFolderId))
			parentFolder = os.get_RootFolder();
		else
			parentFolder = loadById(os, parentFolderId);

		logger.debug("parent folder:id=[{}],path=[{}] load ok", parentFolder.get_Id(), parentFolder.get_PathName());
		return this.addCaseSubFolder(os, parentFolder, folderName);
	}
	/**
	 * 
	 * @param os
	 * @param parentFolder
	 * @param folderName
	 * @return
	 */
	public Folder add(ObjectStore os ,Folder parentFolder, String folderName) {
		Folder folder;
		String fullName = concatPath(parentFolder.get_PathName(), folderName);
		logger.debug("parent folder:id=[{}],path=[{}]", parentFolder.get_Id(), parentFolder.get_PathName());
		folder = getByPath(os, fullName);
		if (folder != null)
			throw new BusinessException("创建文件夹[{0}]失败：已经存在同名的文件夹", folderName);
		folder = createInstance(os);
		folder.set_Parent(parentFolder);
		folder.set_FolderName(folderName);
		
		//注意：为了后续操作能够使用folder中变化的属性，必须使用REFRESH模式，如果使用NO_REFRESH模式，则会导致API_PROPERTY_NOT_IN_CACHE型的EngineRuntimeException
		folder.save(RefreshMode.REFRESH);
		logger.debug("new folder:id=[{}],path=[{}]", folder.get_Id(), folder.get_PathName());
		return folder;
	}
	/**
	 * 添加案例子文件夹
	 * @param os
	 * @param parentFolder
	 * @param folderName
	 * @return
	 */
	public Folder addCaseSubFolder(ObjectStore os ,Folder parentFolder, String folderName) {
		Folder folder;
		String fullName = concatPath(parentFolder.get_PathName(), folderName);
		logger.debug("parent folder:id=[{}],path=[{}]", parentFolder.get_Id(), parentFolder.get_PathName());
		folder = getByPath(os, fullName);
		if (folder != null)
			throw new BusinessException("创建case子文件夹[{0}]失败：已经存在同名的文件夹", folderName);
		folder = this.createCaseSubFolderInstance(os);//.createCaseFolderInstance(os);
		folder.set_Parent(parentFolder);
		folder.set_FolderName(folderName);
		
		//注意：为了后续操作能够使用folder中变化的属性，必须使用REFRESH模式，如果使用NO_REFRESH模式，则会导致API_PROPERTY_NOT_IN_CACHE型的EngineRuntimeException
		folder.save(RefreshMode.REFRESH);
		logger.debug("new folder:id=[{}],path=[{}]", folder.get_Id(), folder.get_PathName());
		return folder;
	}
	/**
	 * 根据id检索文件夹
	 * @param os
	 * @param resId
	 * @return
	 */
	public Folder loadById(ObjectStore os ,String resId) {
		try {
			return fetchInstanceById(os, resId);
		} catch (Exception ex) {
			logger.error(">>>检索文件夹失败，详情：[{}]",ex.getMessage());
			return null;
		}

	}
	/**
	 * 根据路径和文件夹名称检索文件夹
	 * @param os
	 * @param pathAndFolderName
	 * @return
	 */
	public Folder loadByPath(ObjectStore os ,String pathAndFolderName) {
		Folder folder = fetchInstanceByPath(os, pathAndFolderName);
		if (folder == null)
			throw new BusinessException("文件夹[{0}]不存在", pathAndFolderName);
		return folder;
	}
	/**
	 * 加载文件夹，如果没有，则创建
	 * @param os
	 * @param pathAndFolderName
	 * @return
	 */
	public Folder loadAndCreateByPath(ObjectStore os, String pathAndFolderName) {

		Folder folder = null;
		try{
		   folder = fetchInstanceByPath(os, pathAndFolderName);
		}catch(Exception ex){
			logger.error("没有找到文件夹 [{}]，准备创建......", pathAndFolderName);
		}
		if (null == folder) {
			try {
				folder = this.add(os, pathAndFolderName);
				return folder;
			} catch (Exception ex) {
				logger.error("创建文件夹失败，[{}]", pathAndFolderName);
				return folder;
			}
		}
		return folder;
	}
	/**
	 * 
	 * @param os
	 * @param path
	 * @param folderName
	 * @return
	 */
	public Folder loadByPath(ObjectStore os ,String path, String folderName) {
		return loadByPath(os, concatPath(path, folderName));
	}
	/**
	 * 根据父文件夹和子文件夹名称，获取子文件夹，如果没有，则返回null；否则直接返回子文件夹对象
	 * @param os
	 * @param parentFolder
	 * @param subFolderName
	 * @return
	 */
	public Folder loadByParentFolder(ObjectStore os ,Folder parentFolder, String subFolderName) {
		Folder folder = null;
		try {
			folder = loadByPath(os, concatPath(parentFolder.get_PathName(), subFolderName));
		} catch (Exception ex) {
			logger.warn("文件夹[{}]下没有存在子文件夹[{}]",parentFolder.get_PathName(), subFolderName);
		}
		return folder;
	}
	/**
	 * 如果没有，则创建（创建默认文件夹类），再加载
	 * @param os
	 * @param parentFolder
	 * @param subFolderName
	 * @return
	 */
	public Folder loadAndCreateByParentFolder(ObjectStore os ,Folder parentFolder, String subFolderName) {
		Folder folder = null;
		try {
			folder = loadByPath(os, concatPath(parentFolder.get_PathName(), subFolderName));
		} catch (Exception ex) {
			logger.warn("文件夹[{}]下没有存在子文件夹[{}]",parentFolder.get_PathName(), subFolderName);
		}
		if(null == folder){
			logger.info("在文件夹[{}]下创建子文件夹[{}]",parentFolder.get_PathName(), subFolderName);
			folder = createInstance(os);
			folder.set_Parent(parentFolder);
			folder.set_FolderName(subFolderName);	
			//注意：为了后续操作能够使用folder中变化的属性，必须使用REFRESH模式，如果使用NO_REFRESH模式，则会导致API_PROPERTY_NOT_IN_CACHE型的EngineRuntimeException
			folder.save(RefreshMode.REFRESH);
		}
		return folder;
	}
	/**
	 * 特指案例子文件夹，如果没有，则创建，再加载
	 * @param os
	 * @param parentFolder
	 * @param subFolderName
	 * @return
	 */
	public Folder loadAndCreateCaseSubFolderByParentFolder(ObjectStore os ,Folder parentFolder, String subFolderName) {
		Folder folder = null;
		try {
			folder = loadByPath(os, concatPath(parentFolder.get_PathName(), subFolderName));
		} catch (Exception ex) {
			logger.warn("文件夹[{}]下没有存在子文件夹[{}]",parentFolder.get_PathName(), subFolderName);
		}
		if(null == folder){
			logger.info("在文件夹[{}]下创建子文件夹[{}]",parentFolder.get_PathName(), subFolderName);
			folder = this.createCaseSubFolderInstance(os);
			folder.set_Parent(parentFolder);
			folder.set_FolderName(subFolderName);	
			//注意：为了后续操作能够使用folder中变化的属性，必须使用REFRESH模式，如果使用NO_REFRESH模式，则会导致API_PROPERTY_NOT_IN_CACHE型的EngineRuntimeException
			folder.save(RefreshMode.REFRESH);
		}
		return folder;
	}
	/**
	 * 
	 * @param os
	 * @param folderId
	 * @return
	 */
	public Folder getById(ObjectStore os ,String folderId) {
		Folder folder = null;
		try {
			folder = fetchInstanceById(os, folderId);
		} catch (EngineRuntimeException e) {
			logger.debug("ex:id=[{}],errorid=[{}],key=[{}]", e.getExceptionCode().getId(),
					e.getExceptionCode().getErrorId(), e.getExceptionCode().getKey());
			//errorId=FNRCE0051，key=E_OBJECT_NOT_FOUND，即找不到指定的对象
			if (!"FNRCE0051".equals(e.getExceptionCode().getErrorId()))
				throw e;
		}
		return folder;
	}
	/**
	 * 
	 * @param os
	 * @param folderId
	 * @return
	 */
	public DocumentDTO getFolderDTOById(ObjectStore os ,String folderId) {
		Folder folder = null;
		try {
			folder = fetchInstanceById(os, folderId);
		} catch (EngineRuntimeException e) {
			logger.debug("ex:id=[{}],errorid=[{}],key=[{}]", e.getExceptionCode().getId(),
					e.getExceptionCode().getErrorId(), e.getExceptionCode().getKey());
			//errorId=FNRCE0051，key=E_OBJECT_NOT_FOUND，即找不到指定的对象
			if (!"FNRCE0051".equals(e.getExceptionCode().getErrorId()))
				throw e;
		}
		if(folder != null){
			return this.folder2dto(folder);
		}
		return null;
	}
	/**
	 * 
	 * @param path
	 * @param folderName
	 * @return
	 */
	public Folder getByPath(ObjectStore os ,String path, String folderName) {
		return getByPath(os, concatPath(path, folderName));
	}
	/**
	 * 
	 * @param os
	 * @param pathAndFolderName
	 * @return
	 */
	public Folder getByPath(ObjectStore os ,String pathAndFolderName) {
		logger.debug("getByName(String pathAndFolderName=[{}])", pathAndFolderName);
		Folder folder = null;
		try {
			folder = fetchInstanceByPath(os, pathAndFolderName);
		} catch (EngineRuntimeException e) {
			logger.debug("ex:id=[{}],errorid=[{}],key=[{}]", e.getExceptionCode().getId(),
					e.getExceptionCode().getErrorId(), e.getExceptionCode().getKey());
			//errorId=FNRCE0051，key=E_OBJECT_NOT_FOUND，即对象找不到
			if (!"FNRCE0051".equals(e.getExceptionCode().getErrorId()))
				throw e;
		}
		return folder;
	}
	/**
	 * 
	 * @param folder
	 * @param newFolderName
	 * @return
	 */
	public Folder modifyName(Folder folder, String newFolderName) {
		folder.set_FolderName(newFolderName);
		folder.save(RefreshMode.REFRESH);
		return folder;
	}
	/**
	 * 
	 * @param os
	 * @param folderId
	 * @param newFolderName
	 * @return
	 */
	public Folder modifyNameById(ObjectStore os ,String folderId, String newFolderName) {
		return modifyName(loadById(os,folderId), newFolderName);
	}
	/**
	 * 
	 * @param os
	 * @param path
	 * @param folderName
	 * @param newFolderName
	 * @return
	 */
	public Folder modifyNameByPath(ObjectStore os ,String path, String folderName, String newFolderName) {
		return modifyName(loadByPath(os, path, folderName), newFolderName);
	}
	/**
	 * 
	 * @param os
	 * @param pathAndFolderName
	 * @param newFolderName
	 * @return
	 */
	public Folder modifyNameByPath(ObjectStore os ,String pathAndFolderName, String newFolderName) {
		return modifyName(loadByPath(os, pathAndFolderName), newFolderName);
	}
	/**
	 * 根据文件夹id删除文件夹，如果文件夹内包含子文件夹或者文件，则递归删除
	 * @param os
	 * @param folderId
	 */
	public void deleteById(ObjectStore os ,String folderId) {
		
		Folder folder = loadById(os, folderId);
		if(null == folder) return;
		
		deleteFolderRecursion(folder);

	}
	/**
	 * 递归删除文件夹和文件，并返回文件的系列id集合和id集合
	 * @param os
	 * @param folderId
	 * @param refIds
	 * @return DocumentIdDTO
	 */
    public DocumentIdDTO deleteById(ObjectStore os ,String folderId, DocumentIdDTO refIds) {
		
		Folder folder = loadById(os, folderId);
		if(null == folder) return refIds;
		
		deleteFolderRecursion(folder, refIds);

		return refIds;
	}

	/**
	 * 递归删除
	 * @param folder
	 */
	protected void deleteFolderRecursion(Folder folder){
		
		try {
			
            FolderSet childs = folder.get_SubFolders();
            DocumentSet docs = folder.get_ContainedDocuments();
        	String vid = "";
			String id = "";
            if(!docs.isEmpty()){
                Iterator docIterator = docs.iterator();
                Document doc = null;
                while (docIterator.hasNext()){
                	
                	doc = (Document)docIterator.next();
                	
                	vid = doc.get_VersionSeries().get_Id().toString();
					id = doc.get_Id().toString();
                    
                    doc.delete();
                    doc.save(RefreshMode.NO_REFRESH);
					try {
						logger.info(">>>根据文档系列id删除统计数据......");
						distSocialUtil.deleteSocialDatum(folder.getObjectStore(), vid);
						logger.info(">>>根据文档id删除统计数据......");
						distSocialUtil.deleteSocialDatum(folder.getObjectStore(), id);
					} catch (Exception ex) {
						// TODO
					}
			
                }
            }
            if(!childs.isEmpty()){
                Iterator iterator = childs.iterator();
                while (iterator.hasNext()){
                    this.deleteFolderRecursion((Folder)iterator.next());
                }
            }
            folder.delete();
            folder.save(RefreshMode.REFRESH);
        }catch (Exception e){
            e.printStackTrace();
        }
	}
	
	protected void deleteFolderRecursion(Folder folder, DocumentIdDTO refIds) {

		try {

			FolderSet childs = folder.get_SubFolders();
			DocumentSet docs = folder.get_ContainedDocuments();
			String vid = "";
			String id = "";
			if (!docs.isEmpty()) {
				Iterator docIterator = docs.iterator();
				Document doc = null;
				while (docIterator.hasNext()) {

					doc = (Document) docIterator.next();
					
					vid = doc.get_VersionSeries().get_Id().toString();
					id = doc.get_Id().toString();
					
					doc.delete();
					doc.save(RefreshMode.REFRESH);

					try {
						logger.info(">>>根据文档系列id删除统计数据......");
						this.distSocialUtil.deleteSocialDatum(folder.getObjectStore(), vid);
						logger.info(">>>根据文档id删除统计数据......");
						this.distSocialUtil.deleteSocialDatum(folder.getObjectStore(), id);
					} catch (Exception ex) {
						// TODO
					}
					refIds.getVids().add(vid);
					refIds.getIds().add(id);
				}
			}
			if (!childs.isEmpty()) {
				Iterator iterator = childs.iterator();
				while (iterator.hasNext()) {
					this.deleteFolderRecursion((Folder) iterator.next(), refIds);
				}
			}
			folder.delete();
			folder.save(RefreshMode.REFRESH);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据路径删除文件夹
	 * @param os
	 * @param path
	 * @param folderName
	 */
	public void deleteByPath(ObjectStore os ,String path, String folderName) {
		
		Folder folder =loadByPath(os, path, folderName);
		this.deleteFolderRecursion(folder);
	}
	/**
	 * 
	 * @param os
	 * @param pathAndFolderName
	 */
	public void deleteByPath(ObjectStore os ,String pathAndFolderName) {
		
		Folder folder = loadByPath(os, pathAndFolderName);
		this.deleteFolderRecursion(folder);
	}
	/**
	 * 根据当前文件夹id，检索父文件夹
	 * @param os
	 * @param folderId
	 * @return
	 */
	public Folder findParentById(ObjectStore os ,String folderId) {
		return loadById(os, folderId).get_Parent();
	}
	/**
	 * 根据当前文件夹路径，检索父文件夹
	 * @param os
	 * @param path
	 * @return
	 */
	public Folder findParentByPath(ObjectStore os ,String path) {
		return loadByPath(os, path).get_Parent();
	}
	/**
	 * 查找子文件夹
	 * @param folder
	 * @return
	 */
	public List<Folder> findSubFolders(Folder folder) {
		FolderSet set = folder.get_SubFolders();
		List<Folder> list = new ArrayList<Folder>();
		Iterator<?> iterator = set.iterator();
		while (iterator.hasNext()) {
			folder = (Folder) iterator.next();
			list.add(folder);
		}
		return list;
	}
	/**
	 * 获取指定页码的子文件夹
	 * @param folder
	 * @param pageNo
	 * @return
	 */
	/*public Pagination findSubFoldersPage(Folder folder, int pageNo) {
		
		FolderSet fs = folder.get_SubFolders();
		PageIterator pi = fs.pageIterator();
		int i = 0;
		boolean lastPage = false;
		while(pi.nextPage()){
			i++;
			if(i == pageNo){
				
				List<DocumentDTO> list = this.folder2dto(pi.getCurrentPage());
				if(pi.nextPage()){
					lastPage = false;
				}else{
					lastPage = true;
				}
				Pagination pg = new Pagination(i, list.size(), -1, lastPage, list);
				return pg;
			}
		}
		return null;
	}*/
	/**
	 * 分页获取文件夹下的文档
	 * @param folder
	 * @param pageNo
	 * @return
	 */
	/*public Pagination findSubDocsPage(Folder folder, int pageNo) {
		
		DocumentSet ds = folder.get_ContainedDocuments();
		PageIterator pi = ds.pageIterator();
		int i = 0;
		boolean lastPage = false;
		while(pi.nextPage()){
			i++;
			if(i == pageNo){
				
				List<DocumentDTO> list = this.docUtil.document2dto(pi.getCurrentPage());
				if(pi.nextPage()){
					lastPage = false;
				}else {
					lastPage = true;
				}
				Pagination pg = new Pagination(i, list.size(), -1, lastPage, list);
				return pg;
			}
		}
		return null;
	}*/
	
  /*  public Pagination findSubDocsPage(ObjectStore os, String folderId, int pageNo) {
		
    	Folder folder = this.loadById(os, folderId);
    	if(null == folder) return null;
    	
    	return this.findSubDocsPage(folder, pageNo);
	}*/
	/**
	 * 获取指定页码的子文件夹
	 * @param os
	 * @param folderId 文件夹id
	 * @param pageNo 页码
	 * @return
	 */
  /*  public Pagination findSubFoldersPage(ObjectStore os, String folderId, int pageNo) {
		
    	Folder folder = this.loadById(os, folderId);
    	if(null == folder) return null;
    	
    	return this.findSubFoldersPage(folder, pageNo);
		
	}
    */
    
	/**
	 * 根据当前文件夹id获取子文件夹
	 * @param os
	 * @param folderId
	 * @return
	 */
	public List<Folder> findSubFoldersById(ObjectStore os ,String folderId) {
		Folder folder = loadById(os, folderId);
		return findSubFolders(folder);
	}
	/**
	 * 根据当前文件夹路径获取子文件夹
	 * @param os
	 * @param path
	 * @return
	 */
	public List<Folder> findSubFoldersByPath(ObjectStore os ,String path) {
		Folder folder = loadByPath(os, path);
		return findSubFolders(folder);
	}
	
	/**
	 * 文件夹转DTO
	 * @param set
	 * @return
	 */
	public List<DocumentDTO> folder2dto(FolderSet set) {
		List<DocumentDTO> documentDTOs = new ArrayList<DocumentDTO>();

		Iterator folderIte = set.iterator();

		while (folderIte.hasNext()) {
			Folder f = (Folder) folderIte.next();
			DocumentDTO dd = folder2dto(f);
			if(null == dd) continue;
			
			documentDTOs.add(dd);
		}

		return documentDTOs;
	}
	/**
	 * 文件夹转DocumentDto
	 * @param f
	 * @return
	 */
	public DocumentDTO folder2dto(Folder f) {
		
		
		if ("收藏夹".equals(f.get_Name())) {
			return null;
		}
		DocumentDTO dto = new DocumentDTO();
		if(!f.get_SubFolders().isEmpty() || !f.get_ContainedDocuments().isEmpty()){
			dto.setIsEmpty(false);
		}else {
			dto.setIsEmpty(true);
		}
		dto.setIsFolder(true);
		dto.setId(f.get_Id().toString());
		dto.setName(f.get_FolderName());
		dto.setText(f.get_FolderName());
		dto.setPid(f.get_Parent() == null ? null : f.get_Parent().get_Id()
				.toString());
		dto.setModifiedBy(f.get_LastModifier());
		dto.setModifiedOn(f.get_DateLastModified().getTime());
	/*	if(this.cacheManager != null){
			if(StringUtil.isNullOrEmpty(f.get_Owner())){
				logger.info(">>>文件夹的owner 为空......");
				dto.setOwner(f.get_Owner());
			}else {
				DcmUser u = this.cacheManager.getUser(f.get_Owner().toLowerCase());
				if( u != null){
					dto.setOwner(u.getUserName());
					dto.setCreatorCode(u.getUserCode());
				} else {
					dto.setOwner(f.get_Owner());
					logger.info(">>>在缓存中没有获取到用户 [{}]的信息......", dto.getOwner());
				}	
			}
			
		} else {
			logger.info(">>>缓存管理器 [cacheManager] 为空......");
			dto.setOwner(f.get_Owner());
		}*/
		
		dto.setOwner(f.get_Owner());
		//dto.setFolder(true);
		dto.setState("closed");
		dto.setPath("invisible"); //f.get_PathName()
		
		return dto;
	}
	
	public List<DocumentDTO> folder2dto(Object[] folders) {
		
		List<DocumentDTO> documentDTOs = new ArrayList<DocumentDTO>();

		for(Object obj : folders){

			Folder f = (Folder) obj;
			DocumentDTO dto = this.folder2dto(f);
			if(null == dto) continue;
			
			documentDTOs.add(dto);
		}

		return documentDTOs;
	}
	/**
	 * 文件夹移动
	 * @param sourceFolder 源文件夹
	 * @param targetFolder 目标文件夹
	 * @return
	 */
	public boolean moveToFolder(Folder sourceFolder, Folder targetFolder) {
		
		sourceFolder.move(targetFolder);
		
		sourceFolder.save(RefreshMode.REFRESH);
		
		return true;
	}
	/**
	 * 文件夹移动
	 * @param os
	 * @param sourceFolderId
	 * @param targetFolderId
	 * @return
	 */
    public boolean moveToFolder(ObjectStore os, String sourceFolderId, String targetFolderId) {
		
		Folder sourceFolder = Factory.Folder.fetchInstance(os, new Id(sourceFolderId), null);
		Folder targetFolder = Factory.Folder.fetchInstance(os, new Id(targetFolderId), null);
		
		return this.moveToFolder(sourceFolder, targetFolder);
	}
    public boolean moveToFolder(ObjectStore os, String sourceFolderId, Folder targetFolder) {
		
		Folder sourceFolder = Factory.Folder.fetchInstance(os, new Id(sourceFolderId), null);
		
		return this.moveToFolder(sourceFolder, targetFolder);
	}
    /**
     * 
     * @param os
     * @param sourceFolderIds
     * @param targetFolderId
     */
	public void moveToFolder(ObjectStore os, String[] sourceFolderIds, String targetFolderId) {

		Folder targetFolder = Factory.Folder.fetchInstance(os, new Id(targetFolderId), null);
		
		for (String sourceFolderId : sourceFolderIds) {

			moveToFolder(os, sourceFolderId, targetFolder);
		}

	}
	
	public void moveToFolder(ObjectStore os, List<String> sourceFolderIds, Folder targetFolder) {


		for (String sourceFolderId : sourceFolderIds) {

			moveToFolder(os, sourceFolderId, targetFolder);
		}

	}
	/**
	 * 批量移动文件夹
	 * @param sourceFolders
	 * @param targetFolder
	 */
	public void moveToFolder(Folder[] sourceFolders, Folder targetFolder) {

		for (Folder sourceFolder : sourceFolders) {

			moveToFolder(sourceFolder, targetFolder);
		}

	}
	/**
	 * 根据文件夹id批量检索
	 * @param os
	 * @param ids
	 * @return
	 */
	public List<Folder> listFolderById(ObjectStore os, String[] ids) {
		List<Folder> folders = null;
		if (ids != null && ids.length > 0) {
			folders = new ArrayList<Folder>();
			String queryString = "Select Name,Permissions,DateCreated,Id FROM FOLDER where Id IN (";
			StringBuffer sb = new StringBuffer();
			for(String id : ids) {
				if (id != null) {
					sb.append("'" + id + "',");
				}
			}

			queryString += sb.substring(0, sb.toString().length() - 1) + ")";

			SearchScope ss = new SearchScope(os);
			IndependentObjectSet independObjectSet = ss.fetchObjects(new SearchSQL(queryString), 1000, null, false);
			Iterator it = independObjectSet.iterator();
			while (it.hasNext()) {
				IndependentObject independobj = (IndependentObject) it.next();
				folders.add((Folder) independobj);
			}
		}
		return folders;
	}
}
