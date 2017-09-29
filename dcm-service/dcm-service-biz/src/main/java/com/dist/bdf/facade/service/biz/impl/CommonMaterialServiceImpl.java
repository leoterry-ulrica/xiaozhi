package com.dist.bdf.facade.service.biz.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.security.auth.Subject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.base.utils.DistThreadManager;
import com.dist.bdf.base.utils.StringUtil;
import com.dist.bdf.facade.service.MaterialService;
import com.dist.bdf.facade.service.biz.domain.system.DcmTaskMaterialDmn;
import com.dist.bdf.facade.service.biz.task.ComputeResPrivTask;
import com.dist.bdf.facade.service.sga.SgaCompanyService;
import com.dist.bdf.common.conf.ecm.ECMConf;
import com.dist.bdf.common.conf.ecm.ExtPropertyConf;
import com.dist.bdf.common.conf.ecm.ExtPropertyMaterialConf;
import com.dist.bdf.common.constants.CacheKey;
import com.dist.bdf.common.constants.DomainType;
import com.dist.bdf.common.constants.ResourceConstants;
import com.dist.bdf.manager.cache.CacheStrategy;
import com.dist.bdf.manager.ecm.define.StorageAreaType;
import com.dist.bdf.manager.ecm.security.ConnectionService;
import com.dist.bdf.manager.ecm.utils.DistSocialUtil;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
import com.dist.bdf.model.dto.dcm.DocumentIdDTO;
import com.dist.bdf.model.dto.dcm.PageParaDTO;
import com.dist.bdf.model.dto.sga.CompanyInfoResponseDTO;
import com.dist.bdf.model.dto.system.MaterialDTO;
import com.dist.bdf.model.dto.system.MaterialMoveRequestDTO;
import com.dist.bdf.model.dto.system.MaterialMoveSourceDTO;
import com.dist.bdf.model.dto.system.MaterialParaDTO;
import com.dist.bdf.model.dto.system.MaterialSummaryDTO;
import com.dist.bdf.model.dto.system.NewFolderPropertyDTO;
import com.dist.bdf.model.dto.system.ShareObjectSimpleDTO;
import com.dist.bdf.model.dto.system.TaskMaterialDTO;
import com.dist.bdf.model.dto.system.pm.PersonalMaterialInfoDTO;
import com.dist.bdf.model.entity.system.DcmPersonalmaterial;
import com.dist.bdf.model.entity.system.DcmShare;
import com.dist.bdf.model.entity.system.DcmSocialResource;
import com.dist.bdf.model.entity.system.DcmTaskMaterial;
import com.dist.bdf.model.entity.system.DcmUser;
import com.filenet.api.admin.StorageArea;
import com.filenet.api.collection.StorageAreaSet;
import com.filenet.api.core.CmAbstractPersistable;
import com.filenet.api.core.Document;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;
import com.ibm.ecm.util.p8.P8Connection;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * 收藏和点赞：文档版本系列id
        下载：文档id
 * @author weifj
 *
 */
@Service("CommonMaterialService")
@Transactional(propagation = Propagation.REQUIRED)
public class CommonMaterialServiceImpl extends CommonServiceImpl implements MaterialService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private ReadWriteLock lock = new ReentrantReadWriteLock();// 实现于独立接口ReadWriteLock（并未继承于Lock）
	
	/*@Autowired
	private DocumentUtil documentUtil;*/
	@Autowired
	private DistSocialUtil distSocialUtil;
	@Autowired
	private DcmTaskMaterialDmn taskMaterialDmn;
	@Autowired
	@Qualifier("ExtPropertyConf")
	private ExtPropertyConf extPropConf;
	@Autowired
	private ECMConf ecmConf;
	@Autowired
	protected ConnectionService connectionService;
	@Autowired
	private SgaCompanyService sgaComService;
	@Autowired
	private CacheStrategy redisCache;
	@Autowired
	private ExtPropertyMaterialConf extPropertyMaterialConf;
	
	@Override
	public Object getMaterialInfo(MaterialParaDTO info) {
		
		/**
		 * 版本系列id集合
		 */
		String[]vIds = info.getIds();
		String userId = info.getUser();

		 List<DcmSocialResource> socialRes = this.socialResDmn.getByResIdAndCreator(vIds, userId);
		 // jdk1.8新特性，list以流的方式转换成map（效率慢）
		 // Map<String, DcmSocialResource> map = socialRes.stream().collect(Collectors.toMap(DcmSocialResource :: getResId, (p) -> p));
		 Map<String, DcmSocialResource> srMap = new HashMap<String, DcmSocialResource>(socialRes.size());
		 for(DcmSocialResource sr : socialRes){
			 
			 if(srMap.containsKey(sr.getResId())) continue;
			 
			 srMap.put(sr.getResId(), sr);
		 }

		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(info.getRealm()));
		 
		List<Document> docs = this.searchEngine.searchAllWithProperty(p8conn.getObjectStore(), this.ecmConf.getDefaultDocumentClass(), "VersionSeries ", vIds);//.loadDocByVersionSeriesId(p8conn.getObjectStore(), vIds);
		Map<String, Document> docVidMap = new HashMap<String, Document>(docs.size());
		for (Document doc : docs) {

			String vid = this.docUtil.getDocVersionSeriesId(doc);
			if (docVidMap.containsKey(vid))	continue;

			docVidMap.put(vid, doc);
		}

	/*	 final ComputeResPrivTask task = new ComputeResPrivTask( 
				 userId, 
				 ids, 
				 super.docAndFolderDmn,
				 super.socialResDmn ,
				 super.privService);*/
		 final ForkJoinPool pool = new ForkJoinPool(6);
		 final ComputeResPrivTask task = new ComputeResPrivTask (
				 userId, 
				 vIds,
				 srMap, 
				 docVidMap,
				 super.socialResDmn,
				 super.docUtil);
		 
		Map<String, MaterialDTO> result = pool.invoke(task);
		logger.info(">>>计算完成后结果集大小：[{}]", result.size());
		return result;

	}
	
	@Override
	public Object getMaterialInfo(String domainTypeCode,String json) {
		
		PersonalMaterialInfoDTO dto = new PersonalMaterialInfoDTO();

		/*Map<String, JSONObject> foldersMap = new HashMap<String, JSONObject>();
		Map<String, JSONObject> filesMap = new HashMap<String, JSONObject>();*/

		JSONObject jsonObj = JSONObject.fromObject(json);
		String parentFolderId = jsonObj.getString("folderID");
		//String parentFolderCreator = json.getString("creator");
		int clientResType = jsonObj.getInt("type");
		String resType = ResourceConstants.ResourceType.getResTypeCode(clientResType);
		String loginUser = jsonObj.getString("user");
		JSONArray folders = jsonObj.getJSONArray("folders");
		for (int i = 0; i < folders.size(); i++) {
			JSONObject subFolder = (JSONObject) folders.get(i);

			String resId = subFolder.getString("id");
			@SuppressWarnings("unchecked")
			List<DcmShare> shares = (List<DcmShare>) shareDmn.getShareInfosByResId(resId);
			List<String> shareTargetDomainCodes = this.shareDmn.getTargetDomainCodes(resId, DomainType.PERSON); 

			dto.addFolder(resId, (null == shares || 0 == shares.size()) ? Boolean.FALSE : Boolean.TRUE, Boolean.FALSE, shareTargetDomainCodes);
			//foldersMap.put(resId, attr);

			DcmPersonalmaterial pm = this.personalMaterialDmn.getByResId(resId);
			if (null == pm) {
				pm = new DcmPersonalmaterial();
				pm.setResId(resId);
				pm.setParentResId(parentFolderId);
				pm.setDateCreated(new Date());
				pm.setResTypeCode(resType);
				pm.setIsFolder(1);
				pm.setCreator(subFolder.getString("creator"));
				this.personalMaterialDmn.add(pm);
			}
		}

		// 文件
		JSONArray files = jsonObj.getJSONArray("files");
		for (int i = 0; i < files.size(); i++) {
			JSONObject subFile = (JSONObject) files.get(i);

			String resId = subFile.getString("id");
			//List<DcmShare> shares = (List<DcmShare>) shareDmn.getShareInfosByResId(resId);
			List<String> shareTargetDomainCodes = this.shareDmn.getTargetDomainCodes(resId, DomainType.PERSON); // 此处有问题，万一分享给项目组，并不是人。目前先只考虑目标是人？
			DcmSocialResource sr = this.socialResDmn.getByResIdAndCreator(resId, loginUser);
			dto.addFile(resId, (null == shareTargetDomainCodes || shareTargetDomainCodes.isEmpty()) ? Boolean.FALSE : Boolean.TRUE,
					this.socialResDmn.isFavorite(sr), shareTargetDomainCodes);

			DcmPersonalmaterial pm = this.personalMaterialDmn.getByResId(resId);
			if (null == pm) {
				pm = new DcmPersonalmaterial();
				pm.setResId(resId);
				pm.setParentResId(parentFolderId);
				pm.setDateCreated(new Date());
				pm.setResTypeCode(resType);
				pm.setIsFolder(0);
				pm.setCreator(subFile.getString("creator"));
				this.personalMaterialDmn.add(pm);
			}
		}

		return dto;
	}
	
	@Override
	public Object getMaterialInfoEx(String domainTypeCode,String json) {
		
		PersonalMaterialInfoDTO dto = new PersonalMaterialInfoDTO();

		/*Map<String, JSONObject> foldersMap = new HashMap<String, JSONObject>();
		Map<String, JSONObject> filesMap = new HashMap<String, JSONObject>();*/

		JSONObject jsonObj = JSONObject.fromObject(json);
		String parentFolderId = jsonObj.getString("folderID");
		//String parentFolderCreator = json.getString("creator");
		int clientResType = jsonObj.getInt("type");
		String resType = ResourceConstants.ResourceType.getResTypeCode(clientResType);
		String loginUser = jsonObj.getString("user");
		JSONArray folders = jsonObj.getJSONArray("folders");
		for (int i = 0; i < folders.size(); i++) {
			JSONObject subFolder = (JSONObject) folders.get(i);

			String resId = subFolder.getString("id");
			@SuppressWarnings("unchecked")
			List<DcmShare> shares = (List<DcmShare>) shareDmn.getShareInfosByResId(resId);
			// List<String> shareTargetDomainCodes = this.shareDmn.getTargetDomainCodes(resId, DomainType.Person); 
			// TODO 此处有问题，万一分享给项目组，并不是人。目前先只考虑目标是人？
			List<ShareObjectSimpleDTO> shareTargetDomainCodes = this.shareDmn.getTargetDomainPrivCodes(resId, DomainType.PERSON); 
			
			dto.addFolderEx(resId, (null == shares || 0 == shares.size()) ? Boolean.FALSE : Boolean.TRUE, Boolean.FALSE, shareTargetDomainCodes);
			//foldersMap.put(resId, attr);

			DcmPersonalmaterial pm = this.personalMaterialDmn.getByResId(resId);
			if (null == pm) {
				pm = new DcmPersonalmaterial();
				pm.setResId(resId);
				pm.setParentResId(parentFolderId);
				pm.setDateCreated(new Date());
				pm.setResTypeCode(resType);
				pm.setIsFolder(1);
				pm.setCreator(subFolder.getString("creator"));
				this.personalMaterialDmn.add(pm);
			}
		}

		// 文件
		JSONArray files = jsonObj.getJSONArray("files");
		for (int i = 0; i < files.size(); i++) {
			JSONObject subFile = (JSONObject) files.get(i);

			String resId = subFile.getString("id");
			//List<DcmShare> shares = (List<DcmShare>) shareDmn.getShareInfosByResId(resId);
			List<ShareObjectSimpleDTO> shareTargetDomainCodes = this.shareDmn.getTargetDomainPrivCodes(resId, DomainType.PERSON); // 此处有问题，万一分享给项目组，并不是人。目前先只考虑目标是人？
			DcmSocialResource sr = this.socialResDmn.getByResIdAndCreator(resId, loginUser);
			dto.addFileEx(resId, (null == shareTargetDomainCodes || shareTargetDomainCodes.isEmpty()) ? Boolean.FALSE : Boolean.TRUE,
					this.socialResDmn.isFavorite(sr), shareTargetDomainCodes);

			DcmPersonalmaterial pm = this.personalMaterialDmn.getByResId(resId);
			if (null == pm) {
				pm = new DcmPersonalmaterial();
				pm.setResId(resId);
				pm.setParentResId(parentFolderId);
				pm.setDateCreated(new Date());
				pm.setResTypeCode(resType);
				pm.setIsFolder(0);
				pm.setCreator(subFile.getString("creator"));
				this.personalMaterialDmn.add(pm);
			}
		}

		return dto;
	}

	/*@Override
	public Object uploadFiles(String userId, String password, String parentFolderId,  PropertiesExDTO propertiesEx, List<M2ultipartFileDTO> files) {

		String folderPath = "";
		if(StringUtil.isNullOrEmpty(parentFolderId) && propertiesEx.getResourceType().equals(ResourceConstants.ResourceType.Res_Pck_Person)){
			// 属于个人根目录
			folderPath = super.personalMaterialDmn.getPersonalPath(userId);
		}else{
			// 指定某个父文件夹
			folderPath =  ((Folder) this.docAndFolderDmn.loadFolderById(parentFolderId)).get_PathName();
		}

		try {
			
			P8Connection p8conn = this.connectionService.getP8Connection(userId, password);
			Folder folder = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), folderPath);
			
			List<DocumentDTO> docs = this.documentUtil.createDocumentsByFolder(p8conn.getObjectStore(), folder, propertiesEx, files);
		
			//this.connectionService.release();
			
			return docs;

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			throw new BusinessException("创建文档资料失败。详情：[{0}]", ex.getMessage());
		}
	}*/
	@Override
	public DocumentDTO createFolder(String userId, String password, String parentFolderId, String folderName)  throws BusinessException {

		Assert.notNull(parentFolderId);
		
		P8Connection p8Conn = this.connectionService.getP8Connection(userId, password);
		DocumentDTO dto = null;
		
		if(StringUtil.isNullOrEmpty(parentFolderId)){
			throw new BusinessException("父文件夹标识[parentFolderId]不能为null或者空字符。");
		}
		
		dto = this.folderUtil.folder2dto(folderUtil.add(p8Conn.getObjectStore(), parentFolderId, folderName));//(DocumentDTO) this.docAndFolderDmn.createFolder(p8Conn.getObjectStore(), parentFolderId, folderName);
		return dto;
		
	}
	
	@Override
	public Object createFolder(NewFolderPropertyDTO property) {
		
		P8Connection p8Conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(property.getRealm()), property.getUser(), property.getPwd());
		DocumentDTO dto = null;
		String domainTypeCode = DomainType.getDomainTypeCode(property.getType());
		
		if(StringUtil.isNullOrEmpty(property.getParentFolderId()) && domainTypeCode.equalsIgnoreCase(DomainType.PERSON)){
			// 个人文件夹根目录
			DcmUser user = this.userOrgService.getUserEntityByDN(property.getUser());
			String personalPath = pmDmn.getPersonalPath(user.getUserCode());
			Folder folder = this.folderUtil.loadAndCreateByPath(p8Conn.getObjectStore(), personalPath);
			
			dto = this.folderUtil.folder2dto(folderUtil.add(p8Conn.getObjectStore(), folder, property.getFolderName()));//(DocumentDTO) this.docAndFolderDmn.createFolder(p8Conn.getObjectStore(), folder, property.getFolderName());	
		} 
		else{
		
			if(domainTypeCode.equals(DomainType.PROJECT)){
				// 如果在项目下新建文件夹，则需要指定对应的文件夹类别
				Folder newFolder = folderUtil.addCaseSubFolder(p8Conn.getObjectStore(), property.getParentFolderId(), property.getFolderName());
			/*	if(null == newFolder){
					throw new BusinessException("createFolder 创建文件夹失败。");
				}*/
				dto = this.folderUtil.folder2dto(newFolder);
				
				//dto = (DocumentDTO) this.docAndFolderDmn.createCaseSubFolder(p8Conn.getObjectStore(), property.getParentFolderId(), property.getFolderName());
			}else{
				Folder newFolder = folderUtil.add(p8Conn.getObjectStore(), property.getParentFolderId(), property.getFolderName());
				/*if(null == newFolder){
					throw new BusinessException("createFolder 创建文件夹失败。");
				}*/
				dto = this.folderUtil.folder2dto(newFolder);
				//dto = (DocumentDTO) this.docAndFolderDmn.createFolder(p8Conn.getObjectStore(), property.getParentFolderId(), property.getFolderName());
			}
			
		}
	
		
		return dto;
	}
	@Deprecated
	@Override
	public Object getFoldersAndDocuments(String folderId) {
		
		this.connectionService.initialize();
		
		List<DocumentDTO> data = this.searchEngine.findSubFoldersAndDocumentsByFolderId(this.connectionService.getDefaultOS(), folderId);
		
		this.connectionService.release();
		
		return data;
		
	}
	@Override
	public Object getFoldersAndDocuments(String realm, String folderId) {
		
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		
	    List<DocumentDTO> data = this.searchEngine.findSubFoldersAndDocumentsByFolderId(p8conn.getObjectStore(), folderId);
		
		return data;
	}
	@Override
	public Object getFoldersAndDocuments(String realm, String userId, String pwd, String folderId) {
		
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm), userId, pwd);
		
		List<DocumentDTO> data = this.searchEngine.findSubFoldersAndDocumentsByFolderId(p8conn.getObjectStore(), folderId);
		
		return data;
	}
	@Deprecated
	@Override
	public Object deleteFolderById(String userId, String password, String folderId) {
		
		P8Connection p8Conn = this.connectionService.getP8Connection(userId, password);
		this.folderUtil.deleteById(p8Conn.getObjectStore(), folderId);
		
		return true;
		
	}
	@Override
	public Object deleteFolderById(String realm, String userId, String password, String folderId) {
		
		P8Connection p8Conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm), userId, password);
		this.folderUtil.deleteById(p8Conn.getObjectStore(), folderId);
		
		return true;
	}
	
	@Override
	public Object deleteFolderByIdAndReturnIds(String realm, String userId, String password, String folderId) {
		
		final P8Connection p8Conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm), userId, password);
		
		final DocumentIdDTO refIds = new DocumentIdDTO();
		this.folderUtil.deleteById(p8Conn.getObjectStore(), folderId, refIds);
		
		if(!refIds.getIds().isEmpty()){
			
			logger.info(">>>异步清理文档相关业务数据......");
			//this.clearResource(versionSeriesId);
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					
					UserContext uc = UserContext.get();
					Subject subject = p8Conn.getSubject();
					uc.pushSubject(subject);
					
					CommonMaterialServiceImpl.this.clearResource(p8Conn.getObjectStore(), refIds.getVids());
				}
			});
	       
/*			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
				
				@Override
				public void run() {

					CommonMaterialServiceImpl.this.clearResource(p8Conn.getObjectStore(), refIds.getIds());
				}
			});*/
          
		}
		
		return refIds;
		
	}
	
	
	@Deprecated
	@Override
	public Object updateFolder(String userId, String password, String folderId, String newFolderName) {
		
		P8Connection p8Conn = this.connectionService.getP8Connection(userId, password);
		this.folderUtil.modifyNameById(p8Conn.getObjectStore(), folderId, newFolderName);
		
		return true;
	}
	@Deprecated
	@Override
	public Object updateFolder(String realm, String userId, String password, String folderId, String newFolderName) {
		
		P8Connection p8Conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm), userId, password);
		this.folderUtil.modifyNameById(p8Conn.getObjectStore(), folderId, newFolderName);
		
		return true;
	}
	@Override
	public Object renameFolderName(String realm, String folderId, String newFolderName) {
		
		P8Connection p8Conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		this.folderUtil.modifyNameById(p8Conn.getObjectStore(), folderId, newFolderName);
		
		return true;
	}
	@Override
	public Boolean renameDocName(String realm, String docId, String newName) {
		
		P8Connection p8Conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		this.docUtil.modifyNameById(p8Conn.getObjectStore(), docId, newName);
		return true;
	}
	@Deprecated
	@Override
	public Object deleteDoc(String userId, String password, String docId) {
		
		logger.info(">>>userId : [{}], password : [{}]", userId, password);
		
		P8Connection p8Conn = this.connectionService.getP8Connection(userId, password);
		this.docUtil.deleteDocument(p8Conn.getObjectStore(), docId);
		
		logger.info(">>>清理文档 [{}] 相关业务数据......", docId);
		this.clearResource(docId);
		return true;
	}
	@Override
	public Object deleteDoc(final String realm, final String userId, final String password, final String docId) {
		
         logger.debug(">>>userId : [{}], password : [{}]", userId, password);
		
		P8Connection p8Conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm), userId, password);
		if(this.docUtil.deleteDocument(p8Conn.getObjectStore(), docId)){
		
			logger.info(">>>异步清理文档 [{}] 相关业务数据......", docId);
			//this.clearResource(docId);
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {	
				@Override
				public void run() {
					
					P8Connection p8ConnNew = CommonMaterialServiceImpl.this.connectionService.getP8Connection(CommonMaterialServiceImpl.this.ecmConf.getTargetObjectStore(realm), userId, password);

					CommonMaterialServiceImpl.this.clearResource(p8ConnNew.getObjectStore(), docId);
				}
			});	
		}
		return true;
	}
	@Deprecated
	protected int addCESocialSummaryData(String documentId, String propertyName, boolean isAdd) {

		this.connectionService.initialize();
		Map<String, Object> property = new HashMap<String, Object>();
		property.put(propertyName, 0);
	
		CmAbstractPersistable result = distSocialUtil.addSocialDatum(this.connectionService.getDefaultOS(), documentId, property, isAdd);
		if(null == result) return 0;
		
		return result.getProperties().getInteger32Value(propertyName);

	}
	@Deprecated
	protected int addCESocialSummaryDataDownload(String documentId, String propertyName, boolean isAdd) {

		this.connectionService.initialize();
		Map<String, Object> property = new HashMap<String, Object>();
		property.put(propertyName, 0);
		property.put(this.extPropConf.getObjectIdType(), 0); // "XZ_ObjectIdType"，标识类型，0：文档id，1：文档系列id
		
		CmAbstractPersistable result = distSocialUtil.addSocialDatum(this.connectionService.getDefaultOS(), documentId, property, isAdd);
		if(null == result) return 0;
		
		return result.getProperties().getInteger32Value(propertyName);

	}
	
	protected int addCESocialSummaryDataDownload(String realm, String documentId, String propertyName, boolean isAdd) {

		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		Map<String, Object> property = new HashMap<String, Object>();
		property.put(propertyName, 0);
		property.put(this.extPropConf.getObjectIdType(), 0); // "XZ_ObjectIdType"，标识类型，0：文档id，1：文档系列id
		
		CmAbstractPersistable result = distSocialUtil.addSocialDatum(p8conn.getObjectStore(), documentId, property, isAdd);
		if(null == result) return 0;
		
		return result.getProperties().getInteger32Value(propertyName);

	}
	/**
	 * 收藏统计数据
	 * @param documentId
	 * @param propertyName
	 * @param isAdd
	 * @return
	 */
	@Deprecated
	protected int addCESocialSummaryDataFavorite(String documentId, String propertyName, boolean isAdd) {

		this.connectionService.initialize();
		Map<String, Object> property = new HashMap<String, Object>();
		property.put(propertyName, 0);
		property.put(this.extPropConf.getObjectIdType(), 1); // "XZ_ObjectIdType"，标识类型，0：文档id，1：文档系列id
		
		CmAbstractPersistable result = distSocialUtil.addSocialDatum(this.connectionService.getDefaultOS(), documentId, property, isAdd);
		if(null == result) return 0;
		
		return result.getProperties().getInteger32Value(propertyName);

	}
	
	protected int addCESocialSummaryDataFavorite(String realm, String documentId, String propertyName, boolean isAdd) {

		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		Map<String, Object> property = new HashMap<String, Object>();
		property.put(propertyName, 0);
		property.put(this.extPropConf.getObjectIdType(), 1); // "XZ_ObjectIdType"，标识类型，0：文档id，1：文档系列id
		
		CmAbstractPersistable result = distSocialUtil.addSocialDatum(p8conn.getObjectStore(), documentId, property, isAdd);
		if(null == result) return 0;
		
		return result.getProperties().getInteger32Value(propertyName);

	}
	protected int addCESocialSummaryDataLike(String realm, String documentId, String propertyName, boolean isAdd) {

		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		Map<String, Object> property = new HashMap<String, Object>();
		property.put(propertyName, 0);
		property.put(this.extPropConf.getObjectIdType(), 0); // "XZ_ObjectIdType"，标识类型，0：文档id，1：文档系列id
		
		CmAbstractPersistable result = distSocialUtil.addSocialDatum(p8conn.getObjectStore(), documentId, property, isAdd);
		if(null == result) return 0;
		
		return result.getProperties().getInteger32Value(propertyName);

	}
	@Deprecated
	@Override
	public int addDownloadCountOfSummaryData(String documentId) {

		lock.writeLock().lock();
		
		try{
		return addCESocialSummaryDataDownload(documentId, "ClbDownloadCount", true); // 下载状态只有一个：下载
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
		return addCESocialSummaryDataDownload(realm, documentId, "ClbDownloadCount", true); // 下载状态只有一个：下载
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			lock.writeLock().unlock();
		}
		return 0;
	}
	
	@Deprecated
	@Override
	public int addFavoriteCountOfSummaryData(String documentId, boolean isFavorite) {
		
		lock.writeLock().lock();
		
		try{
		return addCESocialSummaryDataFavorite(documentId, this.extPropConf.getFavorites(), isFavorite);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			lock.writeLock().unlock();
		}
		return 0;
		
	}
	
	@Override
	public int addFavoriteCountOfSummaryData(String realm, String documentId, boolean isFavorite) {
		
		lock.writeLock().lock();
		
		try{
		return addCESocialSummaryDataFavorite(realm, documentId, this.extPropConf.getFavorites(), isFavorite);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			lock.writeLock().unlock();
		}
		return 0;
	}
	
	@Deprecated
	@Override
	public int addLikeCountOfSummaryData(String documentId, boolean isLike) {
		
		lock.writeLock().lock();
		
		try{
		return addCESocialSummaryData(documentId, this.extPropConf.getUpvoteCount(), isLike);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			lock.writeLock().unlock();
		}
		return 0;
	
	}
	
	@Override
	public int addLikeCountOfSummaryData(String realm, String documentId, boolean isLike) {
		
		lock.writeLock().lock();
		
		try{
		return addCESocialSummaryDataLike(realm, documentId, this.extPropConf.getUpvoteCount(), isLike);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			lock.writeLock().unlock();
		}
		return 0;
	
	}
	
	protected void addTaskMaterial(String taskId, String materialId) {
		
		DcmTaskMaterial tm = new DcmTaskMaterial();
		tm.setMaterialId(materialId);
		tm.setTaskId(taskId);
		tm.setDateCreated(new Date());
		
		this.taskMaterialDmn.add(tm);
		
	}
	
	@Override
	public void addTaskMaterial(TaskMaterialDTO dto) {
		
		String[] materialIds = dto.getMaterialIds();
		if(materialIds != null && materialIds.length>0) {
			for(String mid : materialIds){
				if(!StringUtil.isNullOrEmpty(mid)) {
					this.addTaskMaterial(dto.getTaskId(), mid);
				}
			}
		}
	}

	@Override
	public void deleteTaskMaterialById(String materialId) {
		
		this.taskMaterialDmn.deleteTaskMaterialById(materialId);
	}

	@Override
	public List<DcmTaskMaterial> getMaterialByTaskId(String taskId) {
		
		return this.taskMaterialDmn.getMaterialByTaskId(taskId);
	}

	@Override
	public Object deleteDocByVId(final String userId, final String password, final String versionSeriesId) {

		logger.info(">>>userId : [{}], password : [{}], vid : [{}]", userId, password, versionSeriesId);

		P8Connection p8Conn = this.connectionService.getP8Connection(userId, password);
		this.docUtil.deleteDocumentByVId(p8Conn.getObjectStore(), versionSeriesId);

		logger.info(">>>异步清理文档(vid) [{}] 相关业务数据......", versionSeriesId);
		//this.clearResource(versionSeriesId);
		
		DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
			
			@Override
			public void run() {

				P8Connection p8ConnNew = CommonMaterialServiceImpl.this.connectionService.getP8Connection(userId, password);
				CommonMaterialServiceImpl.this.clearResource(p8ConnNew.getObjectStore(), versionSeriesId);
			}
		});
       
		return true;
	}
	@Override
	public Object deleteDocByVId(final String realm, final String userId, final String password, final String versionSeriesId) {
		
		logger.debug(">>>userId : [{}], password : [{}], vid : [{}]", userId, password, versionSeriesId);

		P8Connection p8Conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm), userId, password);
		if(this.docUtil.deleteDocumentByVId(p8Conn.getObjectStore(), versionSeriesId)){
			
			logger.info(">>>异步清理文档(vid) [{}] 相关业务数据......", versionSeriesId);
			//this.clearResource(versionSeriesId);
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
	        	
				@Override
				public void run() {
					
					P8Connection p8ConnNew = CommonMaterialServiceImpl.this.connectionService.getP8Connection(CommonMaterialServiceImpl.this.ecmConf.getTargetObjectStore(realm), userId, password);
					CommonMaterialServiceImpl.this.clearResource(p8ConnNew.getObjectStore(), versionSeriesId);
				}
			});
		}
		return true;
	}
	
	@Override
	public Object doMove(MaterialMoveRequestDTO info) {
	
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(info.getRealm()));
		Folder targetFolder = this.folderUtil.loadById(p8conn.getObjectStore(), info.getTargetFolder());
		
		List<MaterialMoveSourceDTO> source = info.getSource();
		List<String> docIds = new ArrayList<String>();
		List<String> folderIds = new ArrayList<>();
		
		for(MaterialMoveSourceDTO s : source) {
			String id = s.getId();
			if(s.getType().equalsIgnoreCase("d")) {
				docIds.add(id);
			}else if(s.getType().equalsIgnoreCase("f")) {
				folderIds.add(id);
			}
		}
		
		this.docUtil.moveToFolder(p8conn.getObjectStore(), docIds, targetFolder);
		this.folderUtil.moveToFolder(p8conn.getObjectStore(), folderIds, targetFolder);
		
		return true;
	}
	@Override
	public Pagination getSubFoldersPage(PageParaDTO dto) {
		
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(dto.getRealm()));

		return this.searchEngine.findSubFoldersPage(p8conn.getObjectStore(), dto.getFolderId(), dto.getPageNo(), dto.getPageSize());

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Pagination getSubDocsPage(PageParaDTO dto) {
		
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(dto.getRealm()));

		//return this.folderUtil.findSubDocsPage(p8conn.getObjectStore(), dto.getFolderId(), dto.getPageNo());
		Pagination page = this.searchEngine.findSubDocsPage(p8conn.getObjectStore(), dto.getFolderId(), dto.getPageNo(), dto.getPageSize());
		if(page != null && page.getData() != null && !page.getData().isEmpty()) {
			List<DocumentDTO> docs = (List<DocumentDTO>) page.getData();
			DcmUser user = null;
			for(DocumentDTO doc : docs) {
				user = (DcmUser) this.redisCache.get(CacheKey.PREFIX_USER + doc.getCreator());
				if(null == user) continue;
				doc.setOwner(user.getUserName());
			}
		}
		return page;
	}
	
	@Override
	public Object getDocVersionsByVId(String realm, String versionSeriesId) {
		
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		
		return this.docUtil.loadDocDTOsByVersionSeriesId(p8conn.getObjectStore(), versionSeriesId);

	}
	@Override
	public DocumentDTO getDocInfoById(String realm, String docId, String userId) {
		
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		DocumentDTO docDTO = this.docUtil.loadDocDTOById(p8conn.getObjectStore(), docId);
		if(StringUtils.isEmpty(docDTO.getPublisher())){
			DcmUser findUser = this.userOrgService.getUserEntityByLoginName(docDTO.getCreator());
			if(findUser != null) {
				docDTO.setPublisher(findUser.getUserCode());
			}
		}
		
		DcmSocialResource sr = this.socialResDmn.getByResIdAndCreator(docDTO.getVersionSeriesId(), userId);
		docDTO.setIsFavorite(this.socialResDmn.isFavorite(sr));
		
		return docDTO;
	}
	@Override
	public DocumentDTO getDocInfoByVId(String realm, String vid, String userId) {
		
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		DocumentDTO docDTO = this.docUtil.loadDocDTOByVersionSeriesId(p8conn.getObjectStore(), vid);
		if(StringUtils.isEmpty(docDTO.getPublisher())){
			DcmUser findUser = this.userOrgService.getUserEntityByLoginName(docDTO.getCreator());
			if(findUser != null) {
				docDTO.setPublisher(findUser.getUserCode());
			}
		}
		
		DcmSocialResource sr = this.socialResDmn.getByResIdAndCreator(vid, userId);
		docDTO.setIsFavorite(this.socialResDmn.isFavorite(sr));
		
		return docDTO;
	}
	@Override
	public DocumentDTO getDocInfoByVIdOs(String objectStoreName, String vid, String userId) {
		
		P8Connection p8conn = this.connectionService.getP8Connection(objectStoreName);
		DocumentDTO docDTO = this.docUtil.loadDocDTOByVersionSeriesId(p8conn.getObjectStore(), vid, true, false);
		if(StringUtils.isEmpty(docDTO.getPublisher())){
			DcmUser findUser = this.userOrgService.getUserEntityByLoginName(docDTO.getCreator());
			if(findUser != null) {
				docDTO.setPublisher(findUser.getUserCode());
			}
		}
		
		DcmSocialResource sr = this.socialResDmn.getByResIdAndCreator(vid, userId);
		docDTO.setIsFavorite(this.socialResDmn.isFavorite(sr));
		
		return docDTO;
	}
	@Override
	public Object deleteDocAdmin(final String realm, final String docId) {
			
			P8Connection p8Conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
			if(this.docUtil.deleteDocument(p8Conn.getObjectStore(), docId)){
			
				logger.info(">>>异步清理文档 [{}] 相关业务数据......", docId);
				//this.clearResource(docId);
				DistThreadManager.MyCacheThreadPool.execute(new Runnable() {	
					@Override
					public void run() {
						
						P8Connection p8ConnNew = CommonMaterialServiceImpl.this.connectionService.getP8Connection(CommonMaterialServiceImpl.this.ecmConf.getTargetObjectStore(realm));

						CommonMaterialServiceImpl.this.clearResource(p8ConnNew.getObjectStore(), docId);
					}
				});	
			}
			return true;
	}
	
	@Override
	public Object deleteDocByVIdAdmin(final String realm, final String versionSeriesId) {
		
		P8Connection p8Conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		if(this.docUtil.deleteDocumentByVId(p8Conn.getObjectStore(), versionSeriesId)){
			
			logger.info(">>>异步清理文档(vid) [{}] 相关业务数据......", versionSeriesId);
			//this.clearResource(versionSeriesId);
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
	        	
				@Override
				public void run() {
					
					P8Connection p8ConnNew = CommonMaterialServiceImpl.this.connectionService.getP8Connection(CommonMaterialServiceImpl.this.ecmConf.getTargetObjectStore(realm));
					CommonMaterialServiceImpl.this.clearResource(p8ConnNew.getObjectStore(), versionSeriesId);
				}
			});
		}
		return true;
	}
	@Override
	public MaterialSummaryDTO getMaterialSummaryByRealm(String realm) {
		
		P8Connection p8Conn = this.connectionService.newP8Connection(this.ecmConf.getTargetObjectStore(realm));
	
		ObjectStore os = p8Conn.getObjectStore();
		StorageAreaSet storageAreaSet = os.get_StorageAreas();
        @SuppressWarnings("unchecked")
		Iterator<StorageArea> iterator = storageAreaSet.iterator();
        MaterialSummaryDTO dto = new MaterialSummaryDTO();
        // 保留两位精度
        DecimalFormat df =new DecimalFormat("#.00");
        while(iterator.hasNext()) {
        	
        	  StorageArea sa = iterator.next();
        	  if(!StorageAreaType.FileStorageArea.equalsIgnoreCase(sa.get_ClassDescription().get_SymbolicName())) 
        		  continue;
        	  
        	  dto.setContentElementCount(sa.get_ContentElementCount());
        	  dto.setContentElementKBytes(Double.valueOf(df.format(sa.get_ContentElementKBytes())));
        	  dto.setContentElementMBytes(Double.valueOf(df.format(sa.get_ContentElementKBytes()/1024)));
        	  dto.setContentElementGBytes(Double.valueOf(df.format(sa.get_ContentElementKBytes()/(1024*1024))));
        	  dto.setContentElementTBytes(Double.valueOf(df.format(sa.get_ContentElementKBytes()/(1024*1024*1024))));
        	  // dto.setDateLastModified(sa.get_DateLastModified());
        	  break;
        }
        Document newestDoc = this.searchEngine.getNewestDoc(p8Conn.getObjectStore());
        if(newestDoc != null) {
        	dto.setDateLastModified(newestDoc.get_DateLastModified());
        }
        CompanyInfoResponseDTO company = this.sgaComService.getComInfoByRealm(realm);
        if(company != null) {
        	dto.setMaterialSourceCount(company.getMaterialSourceCount());
        }
        return dto;
	}
	@Override
	public MaterialSummaryDTO getBasicMaterialStat(String realm, Date beginTime, Date endTime) {
		
		P8Connection p8Conn = this.connectionService.newP8Connection(this.ecmConf.getTargetObjectStore(realm));
		Map<String, Object[]> propertiesValues = new HashMap<String, Object[]>();
		propertiesValues.put(this.extPropertyMaterialConf.getResourceType(), new String[]{"Res_Pck_Department", "Res_Pck_Institute"});
		return this.searchEngine.getMaterialSummaryDTO(p8Conn.getObjectStore(), propertiesValues, beginTime, endTime);
	}
}

