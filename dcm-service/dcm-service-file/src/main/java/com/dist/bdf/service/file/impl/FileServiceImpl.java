package com.dist.bdf.service.file.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.utils.DistThreadManager;
import com.dist.bdf.base.utils.StringUtil;
import com.dist.bdf.facade.service.file.FileService;
import com.dist.bdf.facade.service.sga.SgaProjectService;
import com.dist.bdf.common.conf.ecm.ECMConf;
import com.dist.bdf.common.conf.ecm.ExtPropertyConf;
import com.dist.bdf.common.constants.ResourceConstants;
import com.dist.bdf.manager.ecm.security.ConnectionService;
import com.dist.bdf.manager.ecm.utils.DistSocialUtil;
import com.dist.bdf.manager.ecm.utils.DocumentUtil;
import com.dist.bdf.manager.ecm.utils.FolderUtil;
import com.dist.bdf.model.dto.cad.TemplateDTO;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
import com.dist.bdf.model.dto.dcm.FileContentLocalDTO;
import com.dist.bdf.model.dto.dcm.PropertiesDTO;
import com.dist.bdf.model.dto.dcm.PropertiesExDTO;
import com.dist.bdf.model.dto.sga.FileRecordDTO;
import com.dist.bdf.model.dto.sga.UserAttachmentDTO;
import com.dist.bdf.model.dto.system.CopyFileDTO;
import com.filenet.api.core.CmAbstractPersistable;
import com.filenet.api.core.Document;
import com.filenet.api.core.Folder;
import com.ibm.ecm.util.p8.P8Connection;

@Service("FileService")
public class FileServiceImpl implements FileService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ConnectionService connectionService;
	@Autowired
	private ECMConf ecmConf;
	@Autowired
	private FolderUtil folderUtil;
	@Autowired
	private DocumentUtil documentUtil;
	@Autowired
	private DistSocialUtil distSocialUtil;
	@Autowired
	@Qualifier("ExtPropertyConf")
	private ExtPropertyConf extPropConf;
	@Autowired
	private SgaProjectService sgaProjectService;
	 
	private ReadWriteLock lock = new ReentrantReadWriteLock();// 实现于独立接口ReadWriteLock（并未继承于Lock）
	  
	  /**
	     * 根据机构域获取对应的目标存储库
	     * @param realm
	     * @return
	     */
	 /* private String getTargetObjectStore(String realm) {
			
			if(StringUtil.isNullOrEmpty(realm)){
				return this.ecmConf.getEcmTargetObjectStoreName();
			}
			String targetObjectStore = xconf.getProperty("realm."+realm);
			if(StringUtil.isNullOrEmpty(targetObjectStore)){
				logger.info("xdiamond conf [{}] is null......, use default targetObjectStore name", realm);
				targetObjectStore = this.ecmConf.getEcmTargetObjectStoreName();
			}
			
			return targetObjectStore;
		}
		*/
		
	@Override
    public String getPersonalPath(String userId){
		
		logger.info("断言用户id不能为空。");
		Assert.notNull(userId);
		
        String personalDirRoot =  ecmConf.getPersonalDirRoot();
		
		String personalPath = (personalDirRoot.endsWith("/"))? ecmConf.getPersonalDirRoot()+userId :  ecmConf.getPersonalDirRoot()+"/"+userId ;

		return personalPath;
	}
    
   
    /**
     * 
     * @param docId
     * @return
     */
    @Override
    public FileContentLocalDTO getDocContentLocal(String docId) {

		this.connectionService.initialize();
		FileContentLocalDTO dto = this.documentUtil.loadLocalById(this.connectionService.getDefaultOS(), docId);

		return dto;

	}
    @Override
    public FileContentLocalDTO getDocContentLocal(String realm, String userId, String password, String docId) {

    	P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm),userId, password);
  		FileContentLocalDTO dto = this.documentUtil.loadLocalById(p8conn.getObjectStore(), docId);

  		return dto;
  	}

    @Override
	public Object uploadFiles(String userId, String password, String parentFolderId,  PropertiesExDTO propertiesEx, List<MultipartFile> files) {

		return this.uploadFiles(null, userId, password, parentFolderId, propertiesEx, files);
		/*String folderPath = "";
	
		try {
			if(StringUtil.isNullOrEmpty(parentFolderId) && propertiesEx.getResourceType().equals(ResourceConstants.ResourceType.Res_Pck_Person)){
				// 属于个人根目录
				folderPath = this.getPersonalPath(userId);
			}else{
				// 指定某个父文件夹
				folderPath =  ((Folder) this.loadFolderById(parentFolderId)).get_PathName();
			}
			
			P8Connection p8conn = this.connectionService.getP8Connection(userId, password);
			Folder folder = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), folderPath);
			
			List<DocumentDTO> docs = this.documentUtil.createDocument(p8conn.getObjectStore(), folder, propertiesEx, files);
			
			this.connectionService.release();
			
			return docs;

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			throw new BusinessException("创建文档资料失败。详情：[{0}]", ex.getMessage());
		}*/
	}
	@Override
	public Object uploadFiles(String realm, String userId, String password, String parentFolderId,  PropertiesExDTO propertiesEx, List<MultipartFile> files) throws BusinessException {

		String folderPath = "";
		Folder folder = null;
		try {
			P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm), userId, password);
			
			if(StringUtil.isNullOrEmpty(parentFolderId) && propertiesEx.getResourceType().equals(ResourceConstants.ResourceType.RES_PCK_PERSON)){
				// 属于个人根目录
				folderPath = this.getPersonalPath(userId);
				folder = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), folderPath);
			}else{
				// 指定某个父文件夹
				folder = this.folderUtil.loadById(p8conn.getObjectStore(), parentFolderId);
				//folderPath =  findFolder.get_PathName();
			}
			
			
			List<DocumentDTO> docs = this.documentUtil.createDocument(p8conn.getObjectStore(), folder, propertiesEx, files);
			
			//this.connectionService.release();
			
			return docs;

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			throw new BusinessException("创建文档资料失败。详情：[{0}]", ex.getMessage());
		}
	}
	/**
	 * 从外面上传项目文件
	 * @param realm
	 * @param projectId
	 * @param files
	 * @return
	 */
	@Override
	public Object uploadProjectFilesExternal(String realm, String projectId, List<MultipartFile> files) throws BusinessException{

		List<String> vids = new ArrayList<String>();
		
		Folder folder = null;
		try {
			P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
			
			// 获取项目根文件夹
			Folder projectFolder = this.folderUtil.getById(p8conn.getObjectStore(), projectId);
		
			// 指定某个子文件夹
			folder = this.folderUtil.loadAndCreateCaseSubFolderByParentFolder(p8conn.getObjectStore(), projectFolder, "合作项目");
			
			List<Document> docs = this.documentUtil.createDocuments(p8conn.getObjectStore(), folder, files);
			if(docs!= null && !vids.isEmpty()){
				for(Document d : docs) {
					vids.add(this.documentUtil.getDocVersionSeriesId(d));
					
				}
			}
			//this.connectionService.release();
			
			return true;

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			throw new BusinessException("创建文档资料失败。详情：[{0}]", ex.getMessage());
		}
	}
	@Override
	public Object uploadLocalFiles(String userId, String password, String parentFolderId,  PropertiesExDTO propertiesEx, List<FileContentLocalDTO> files) {

		return this.uploadLocalFiles(null, userId, password, parentFolderId, propertiesEx, files);
		/*String folderPath = "";
	
		try {

			if(StringUtil.isNullOrEmpty(parentFolderId) && propertiesEx.getResourceType().equals(ResourceConstants.ResourceType.Res_Pck_Person)){
				// 属于个人根目录
				folderPath = this.getPersonalPath(userId);
			}else{
				// 指定某个父文件夹
				folderPath =  ((Folder) this.loadFolderById(parentFolderId)).get_PathName();
			}
			
			P8Connection p8conn = this.connectionService.getP8Connection(userId, password);
			Folder folder = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), folderPath);
			
			List<DocumentDTO> docs = this.documentUtil.createDocuments(p8conn.getObjectStore(), folder, propertiesEx, files);
			
			return docs;

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			throw new BusinessException("创建文档资料失败。详情：[{0}]", ex.getMessage());
		} finally {
			this.connectionService.release();
		}*/
	}
	@Override
	public Object uploadLocalFiles(String realm, String userId, String password, String parentFolderId,  PropertiesExDTO propertiesEx, List<FileContentLocalDTO> files) throws BusinessException {

		String folderPath = "";
		Folder folder = null;
		try {

			P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm),userId, password);
			
			if(StringUtil.isNullOrEmpty(parentFolderId) && propertiesEx.getResourceType().equalsIgnoreCase(ResourceConstants.ResourceType.RES_PCK_PERSON)){
				// 属于个人根目录
				folderPath = this.getPersonalPath(propertiesEx.getDomain());
				folder = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), folderPath);
			} else {
				// 指定某个父文件夹
				folder = this.folderUtil.loadById(p8conn.getObjectStore(), parentFolderId);
			}
			
			List<DocumentDTO> docs = this.documentUtil.createDocuments(p8conn.getObjectStore(), folder, propertiesEx, files);
			
			return docs;

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			throw new BusinessException("创建文档资料失败。详情：[{0}]", ex.getMessage());
		} /*finally {
			this.connectionService.release();
		}*/
	}
	@Override
	public DocumentDTO uploadFiles(String userId, String password, String parentFolderId,  PropertiesExDTO propertiesEx, FileContentLocalDTO file) throws BusinessException {

		String folderPath = "";
		Folder folder = null;
		try {
			P8Connection p8conn = this.connectionService.getP8Connection(userId, password);
			
			if(StringUtil.isNullOrEmpty(parentFolderId) && propertiesEx.getResourceType().equals(ResourceConstants.ResourceType.RES_PCK_PERSON)){
				// 属于个人根目录
				folderPath = this.getPersonalPath(userId);
				folder = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), folderPath);
				
			}else{
				// 指定某个父文件夹
				folder = this.folderUtil.loadById(p8conn.getObjectStore(), parentFolderId);
				//folderPath =  folder.get_PathName();
			}
			
			DocumentDTO doc = this.documentUtil.createDocument(p8conn.getObjectStore(), folder, propertiesEx, file);
			
			//this.connectionService.release();
			
			return doc;

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			throw new BusinessException("创建文档资料失败。详情：[{0}]", ex.getMessage());
		}
	}
	/**
	 * 检测文件信息
	 * @param docId
	 * @return
	 */
	@Override
	public String checkFileInfo(String docId) {
		
		this.connectionService.initialize();
		
		Document doc = documentUtil.loadById(this.connectionService.getDefaultOS(), docId);
	
		String baseFileName = doc.get_Name();     //文件名
	    String ownerId =  doc.get_Owner();//"admin"; //文件所有者的唯一编号
	    long size = doc.get_ContentSize().longValue();  //文件大小，以bytes为单位
	    long version = doc.get_DateLastModified().getTime();  //文件版本号，文件最后被修改时间值

	    /*OfficeFileInfoDTO dto = new OfficeFileInfoDTO();
	    dto.setAllowExternalMarketplace("true");
	    dto.setBaseFileName(baseFileName);
	    dto.setOwnerId(ownerId);
	    dto.setSize(String.valueOf(size));
	    dto.setVersion(String.valueOf(version));*/
	    
	    JSONObject metadataJson = new JSONObject();
	    metadataJson.put("BaseFileName", baseFileName);
	    metadataJson.put("OwnerId", ownerId);
	    metadataJson.put("Size", size);
	    metadataJson.put("AllowExternalMarketplace", true);
	    metadataJson.put("Version", version);
	    
	    //return JSONUtil.toJSONString(dto);
	    this.connectionService.release();
	    
	    return metadataJson.toString();
	    /*return "{\"BaseFileName\":\"" + baseFileName + "\",\"OwnerId\":\"" + ownerId + "\",\"Size\":\"" + size
	                        + "\",\"AllowExternalMarketplace\":\"" + true + "\",\"Version\":\"" + version + "\"}";*/
	}
	
	/**
	 * 获取文件的基本信息
	 * @param realm 域，如：数慧，dist
	 * @param docId
	 * @return
	 */
	@Override
	public String checkFileInfo(String realm, String docId) {
		
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		
		Document doc = documentUtil.loadById(p8conn.getObjectStore(), docId);
	
		String baseFileName = doc.get_Name();     //文件名
	    String ownerId =  doc.get_Owner();//"admin"; //文件所有者的唯一编号
	    long size = doc.get_ContentSize().longValue();  //文件大小，以bytes为单位
	    long version = doc.get_DateLastModified().getTime();  //文件版本号，文件最后被修改时间值
	    
	    JSONObject metadataJson = new JSONObject();
	    metadataJson.put("BaseFileName", baseFileName);
	    metadataJson.put("OwnerId", ownerId);
	    metadataJson.put("Size", size);
	    metadataJson.put("AllowExternalMarketplace", true);
	    metadataJson.put("Version", version);

	    return metadataJson.toString();
	    
	}
	@Override
	public int addDownloadCountOfSummaryData(String documentId) {

		lock.writeLock().lock();
		
		try{
			this.connectionService.initialize();
			Map<String, Object> property = new HashMap<String, Object>();
			property.put( "ClbDownloadCount", 0);
			property.put(this.extPropConf.getObjectIdType(), 0); // "XZ_ObjectIdType"，标识类型，0：文档id，1：文档系列id
			CmAbstractPersistable result = distSocialUtil.addSocialDatum(this.connectionService.getDefaultOS(), documentId, property, true);
			if(null == result) return 0;
			
			return result.getProperties().getInteger32Value( "ClbDownloadCount");
			
		//return addCESocialSummaryData(documentId, "ClbDownloadCount", true); // 下载状态只有一个：下载
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			lock.writeLock().unlock();
		}
		return 0;
	}
	@Override
	public int addDownloadCountOfSummaryData(String realm, String documentId) {

		lock.writeLock().lock();
		
		try{
			P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		
			Map<String, Object> property = new HashMap<String, Object>();
			property.put( "ClbDownloadCount", 0);
			property.put(this.extPropConf.getObjectIdType(), 0); // "XZ_ObjectIdType"，标识类型，0：文档id，1：文档系列id
			CmAbstractPersistable result = distSocialUtil.addSocialDatum(p8conn.getObjectStore(), documentId, property, true);
			if(null == result) return 0;
			
			return result.getProperties().getInteger32Value( "ClbDownloadCount");
			
		//return addCESocialSummaryData(documentId, "ClbDownloadCount", true); // 下载状态只有一个：下载
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			lock.writeLock().unlock();
		}
		return 0;
	}
	
/*	private int addCESocialSummaryData(String documentId, String propertyName, boolean isAdd) {

		

	}*/
	@Override
	public FileContentLocalDTO getDocContentLocal(String realm, String docId) {
		
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
  		FileContentLocalDTO dto = this.documentUtil.loadLocalById(p8conn.getObjectStore(), docId);

  		return dto;
	}
	/**
	 * 建立新版本
	 * @param realm
	 * @param docId
	 * @param is
	 * @return
	 */
	@Override
	public DocumentDTO newVersion(String realm, String docId, InputStream is) {
	
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		
		return this.documentUtil.newVersion(p8conn.getObjectStore(), docId, is);
	}
	@Override
	public DocumentDTO newVersion(String realm, String docId, FileContentLocalDTO fileContentLocalDTO) {
		
	    P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		
		return this.documentUtil.newVersion(p8conn.getObjectStore(), docId, fileContentLocalDTO.getContentInputStream());
	}
	@Override
	public DocumentDTO newVersion(String realm, String docId, MultipartFile multipartFile) {
		
		   P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
			
			try {
				return this.documentUtil.newVersion(p8conn.getObjectStore(), docId, multipartFile.getInputStream());
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				return null;
			}
	}
	@Override
	public DocumentDTO newVersion(String realm, String docId, String publisher, MultipartFile multipartFile) {
		
		 P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
			
			try {
				Map<String, Object> propertiesValues = new HashMap<String, Object>();
				propertiesValues.put(this.extPropConf.getPublisher(), publisher);
				return this.documentUtil.newVersionEx(p8conn.getObjectStore(), docId, propertiesValues, multipartFile.getInputStream());
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				return null;
			}
			
	}
	@Override
	public Object uploadProjectFilesExternal(final PropertiesDTO properties, List<MultipartFile> uploadMFiles) throws BusinessException {

		try {
			final String creator = properties.getUserId();
			P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(properties.getRealm()));
			
			// 指定某个父文件夹
			Folder folder = this.folderUtil.loadById(p8conn.getObjectStore(), properties.getParentFolderId());
			// 指定发布者
			properties.getPropertiesEx().setPublisher(creator);
			List<DocumentDTO> docs = new ArrayList<DocumentDTO>();
			for(MultipartFile f : uploadMFiles){
				final DocumentDTO doc = this.documentUtil.createDocument(p8conn.getObjectStore(), folder, properties.getPropertiesEx(), f);
				docs.add(doc);
				
				logger.info(">>>添加文件到记录表");
				DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
					
					@Override
					public void run() {
						
						FileRecordDTO dto = new FileRecordDTO();
						dto.setCreator(creator);
						dto.setDomainCode(properties.getPropertiesEx().getDomain());
						dto.setResId(doc.getId());
						dto.setResTypeCode(properties.getPropertiesEx().getResourceType());
						sgaProjectService.recordUploadFile(dto);
					}
				});
			}
	
			return docs;

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			throw new BusinessException("创建文档资料失败。详情：[{0}]", ex.getMessage());
		} 
	}
	@Override
	public List<DocumentDTO> uploadProjectFilesForBPM(final PropertiesDTO properties, List<MultipartFile> uploadMFiles) throws BusinessException {

		try {
			final String creator = properties.getUserId();
			P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(properties.getRealm()));
			Folder folder = null;
			if(1 == properties.getIsRoot()) {
				// 指定项目父文件夹
				folder = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), this.ecmConf.getProjectDirRoot() + "/" + properties.getParentFolderId());
			} else {
				folder = this.folderUtil.loadById(p8conn.getObjectStore(), properties.getParentFolderId());
			}
		
			// 指定发布者
			properties.getPropertiesEx().setPublisher(creator);
			List<DocumentDTO> docs = new ArrayList<DocumentDTO>();
			for(MultipartFile f : uploadMFiles){
				final DocumentDTO doc = this.documentUtil.createDocument(p8conn.getObjectStore(), folder, properties.getPropertiesEx(), f);
				docs.add(doc);
			}
	
			return docs;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			throw new BusinessException("创建文档资料失败。详情：[{0}]", ex.getMessage());
		} 
	}
	@Override
	public List<DocumentDTO> uploadProjectLocalFilesForBPM(final PropertiesDTO properties, List<FileContentLocalDTO> uploadMFiles) throws BusinessException {

		try {
			final String creator = properties.getUserId();
			P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(properties.getRealm()));
			
			// 指定项目父文件夹
			Folder folder = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), this.ecmConf.getProjectDirRoot() + "/" + properties.getParentFolderId());
			// 指定发布者
			properties.getPropertiesEx().setPublisher(creator);
			List<DocumentDTO> docs = new ArrayList<DocumentDTO>();
			for(FileContentLocalDTO f : uploadMFiles){
				final DocumentDTO doc = this.documentUtil.createDocument(p8conn.getObjectStore(), folder, properties.getPropertiesEx(), f);
				docs.add(doc);
			}
	
			return docs;

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			throw new BusinessException("创建文档资料失败。详情：[{0}]", ex.getMessage());
		} 
	}
	@Override
	public DocumentDTO uploadCADTemplate(TemplateDTO templateDTO, List<MultipartFile> uploadMFiles) {
		
		if(null == uploadMFiles || uploadMFiles.isEmpty()) {
			return null;
		}
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(templateDTO.getRealm()));
		Folder templateFolder = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), this.ecmConf.getCADTemplateDir());
		logger.info(">>>删除原模板文件：" + (templateDTO.getName()+"."+templateDTO.getSuffix()));
		this.documentUtil.deleteDocument(p8conn.getObjectStore(), templateFolder, templateDTO.getName()+"."+templateDTO.getSuffix());
		PropertiesExDTO propertiesExt = new PropertiesExDTO();
		propertiesExt.setDomain(templateDTO.getDomain());
		propertiesExt.setSearchable(templateDTO.getSearchable());
		propertiesExt.setResourceType(templateDTO.getResourceType());
		propertiesExt.setPublisher(templateDTO.getPublisher());
		for(MultipartFile f : uploadMFiles){
			 try {
				 DocumentDTO doc = this.documentUtil.createDocument(p8conn.getObjectStore(), templateFolder, propertiesExt, f);
				if(doc != null){
					// 单个文件上传
					return doc;
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				return null;
			}
		}
		return null;
	}
	
	@Override
    public List<DocumentDTO> uploadPublicFiles(PropertiesDTO properties, List<MultipartFile> uploadMFiles) throws BusinessException {
		
		try {
			final String creator = properties.getUserId();

			P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getPublicObjectStore(properties.getRealm()));
			// 资讯文件夹
			Folder inforRootFolder = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), this.ecmConf.getInformationDirRoot());
			// 指定发布者
			properties.getPropertiesEx().setPublisher(creator);
			List<DocumentDTO> docs = new ArrayList<DocumentDTO>();
			for(MultipartFile f : uploadMFiles){
				DocumentDTO doc = this.documentUtil.createDocument(p8conn.getObjectStore(), inforRootFolder, properties.getPropertiesEx(), f);
				docs.add(doc);
			}
			return docs;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new BusinessException("创建文档资料失败。详情：[{0}]", ex.getMessage());
		} 
	}
	@Override
	public List<DocumentDTO> uploadPersonalResume(UserAttachmentDTO dto, List<MultipartFile> uploadMFiles) {
		
		dto.getPropertiesEx().setDomain(dto.getUserCode());
		dto.getPropertiesEx().setResourceType(ResourceConstants.ResourceType.RES_PCK_PERSON);
		dto.getPropertiesEx().setSearchable(false);
		// 指定发布者
		dto.getPropertiesEx().setPublisher(dto.getUserCode());
		try {

 			P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getPublicObjectStore(dto.getRealm()));
			Folder personalResumeRootFolder = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), this.ecmConf.getPersonalResumeDir()+"/" + dto.getUserCode());

			List<DocumentDTO> docs = new ArrayList<DocumentDTO>();
			for(MultipartFile f : uploadMFiles){
				DocumentDTO doc = this.documentUtil.createDocument(p8conn.getObjectStore(), personalResumeRootFolder, dto.getPropertiesEx(), f);
				docs.add(doc);
			}
			return docs;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new BusinessException("创建文档资料失败。详情：[{0}]", ex.getMessage());
		} 
	}
	@Override
	public DocumentDTO copyFile2PublicFromBusiness(CopyFileDTO dto) {
		
		try {
			// 检索到源文件
			P8Connection p8connSource = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(dto.getFromRealm()));
			FileContentLocalDTO file = this.documentUtil.loadLocalById(p8connSource.getObjectStore(), dto.getResId());
			// 连接公共库
			P8Connection p8connTarget = this.connectionService.getP8Connection(this.ecmConf.getPublicObjectStore(dto.getToRealm()));
			// 资讯文件夹
			Folder inforRootFolder = this.folderUtil.loadAndCreateByPath(p8connTarget.getObjectStore(), this.ecmConf.getInformationDirRoot());
			
			PropertiesExDTO properties = new PropertiesExDTO();
			properties.setFileType(dto.getFileType());
			properties.setDomain(file.getDocProperties().getDomain());
			properties.setResourceType(file.getDocProperties().getResourceType());
			properties.setPublisher(dto.getUserId());
			properties.setRegion(file.getDocProperties().getRegion());
			properties.setOrg(file.getDocProperties().getOrganization());
			properties.setSearchable(true);
			return this.documentUtil.createDocument(p8connTarget.getObjectStore(), inforRootFolder, properties, file);
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new BusinessException("从企业资料拷贝到公共库失败。详情：[{0}]", ex.getMessage());
		}
	}
}
