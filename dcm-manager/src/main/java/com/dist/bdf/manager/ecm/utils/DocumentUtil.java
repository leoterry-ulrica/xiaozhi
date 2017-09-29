package com.dist.bdf.manager.ecm.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import com.dist.bdf.base.utils.StringUtil;
import com.dist.bdf.common.conf.ecm.ECMConf;
import com.dist.bdf.common.conf.ecm.ExtPropertyConf;
import com.dist.bdf.common.conf.ecm.ExtPropertyMaterialConf;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
import com.dist.bdf.model.dto.dcm.FileContentLocalDTO;
import com.dist.bdf.model.dto.dcm.PropertiesExDTO;
import com.dist.bdf.model.dto.system.M2ultipartFileDTO;
import com.dist.bdf.model.dto.system.ThumbnailDTO;
import com.filenet.api.admin.StorageArea;
import com.filenet.api.collection.CmThumbnailSet;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.DocumentSet;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.ClassNames;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.constants.ReservationType;
import com.filenet.api.constants.VersionStatus;
import com.filenet.api.core.CmThumbnail;
import com.filenet.api.core.ContentElement;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.core.VersionSeries;
import com.filenet.api.core.Versionable;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.Properties;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.RepositoryRow;
import com.filenet.api.sweep.CmThumbnailRequest;
import com.filenet.api.util.Id;
import com.filenet.apiimpl.core.VersionSeriesImpl;
import com.filenet.apiimpl.query.RepositoryRowImpl;

/**
 * ce文档操作工具类
 * 
 * @author weifj
 *
 */

@Component
public class DocumentUtil {

	private static Logger logger = LoggerFactory.getLogger(DocumentUtil.class);

	@Autowired
	private ECMConf ecmConf;
	@Autowired
	private ExtPropertyMaterialConf extPropMaterialConf;
	@Autowired
	@Qualifier("ExtPropertyConf")
	private ExtPropertyConf extCommonPropertyConf;

	/*
	 * @Autowired private DistCacheManager cacheManager;
	 */
	/**
	 * 设置文档内容
	 * 
	 * @param doc
	 * @param file
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void settingFileContent(Document doc, MultipartFile file) throws IOException {
		ContentElementList contentList = Factory.ContentElement.createList();
		ContentTransfer content1 = Factory.ContentTransfer.createInstance();
		content1.set_RetrievalName(file.getOriginalFilename());
		content1.setCaptureSource(file.getInputStream());
		content1.set_ContentType(file.getContentType());
		contentList.add(content1);
		doc.set_ContentElements(contentList);
		doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
		doc.save(RefreshMode.REFRESH);
		System.out.println("setting content success!");

	}

	/**
	 * 设置文档内容
	 * 
	 * @param doc
	 * @param file
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void settingFileContent(Document doc, File file) throws IOException {
		ContentElementList contentList = Factory.ContentElement.createList();
		ContentTransfer content1 = Factory.ContentTransfer.createInstance();
		content1.set_RetrievalName(file.getName());
		content1.setCaptureSource(new FileInputStream(file.getAbsolutePath()));
		content1.set_ContentType("text/plain");
		contentList.add(content1);
		doc.set_ContentElements(contentList);
		doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
		doc.save(RefreshMode.REFRESH);
		System.out.println("setting content success!");

	}

	/**
	 * 设置文档内容，并设置mimetype
	 * 
	 * @param doc
	 * @param file
	 * @param contentType
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void settingFileContent(Document doc, File file, String contentType) throws IOException {
		ContentElementList contentList = Factory.ContentElement.createList();
		ContentTransfer content1 = Factory.ContentTransfer.createInstance();
		content1.set_RetrievalName(file.getName());
		content1.setCaptureSource(new FileInputStream(file.getAbsolutePath()));
		content1.set_ContentType(contentType);
		contentList.add(content1);
		doc.set_ContentElements(contentList);
		doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
		doc.save(RefreshMode.REFRESH);
		System.out.println("setting content success!");

	}

	@SuppressWarnings("unchecked")
	public Document createDocument(ObjectStore os, String title, Folder folder, MultipartFile file) throws IOException {

		Document doc = Factory.Document.createInstance(os, this.ecmConf.getDefaultDocumentClass());

		// Set document properties.
		doc.getProperties().putValue("DocumentTitle", title);
		doc.set_MimeType(file.getContentType());

		StorageArea sa = Factory.StorageArea.getInstance(os, new Id(ecmConf.getStorageareaId()));
		doc.set_StorageArea(sa);
		doc.save(RefreshMode.REFRESH);

		ContentElementList contentList = Factory.ContentElement.createList();
		ContentTransfer content1 = Factory.ContentTransfer.createInstance();
		content1.set_RetrievalName(file.getOriginalFilename());
		content1.setCaptureSource(file.getInputStream());
		content1.set_ContentType(file.getContentType());
		contentList.add(content1);
		doc.set_ContentElements(contentList);

		doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
		// Check in the document.
		doc.save(RefreshMode.REFRESH);

		// File the document.
		ReferentialContainmentRelationship rcr = folder.file(doc, AutoUniqueName.AUTO_UNIQUE, title,
				DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
		rcr.save(RefreshMode.REFRESH);
		return doc;
	}

	/**
	 * 
	 * @param os
	 * @param title
	 * @param folderId
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public Document createDocument(ObjectStore os, String title, String folderId, MultipartFile file)
			throws IOException {

		Folder folder = Factory.Folder.getInstance(os, ClassNames.FOLDER, new Id(folderId));

		return this.createDocument(os, title, folder, file);

	}

	public List<Document> createDocuments(ObjectStore os, String folderId, List<MultipartFile> files)
			throws IOException {

		if (null == files || 0 == files.size()) {
			return null;
		}
		Folder folder = Factory.Folder.getInstance(os, ClassNames.FOLDER, new Id(folderId));
		return this.createDocuments(os, folder, files);

	}

	/**
	 * 
	 * @param os
	 * @param folderPath
	 * @param files
	 * @return
	 * @throws IOException
	 */
	public List<Document> createDocumentByFolderPath(ObjectStore os, String folderPath, List<MultipartFile> files)
			throws IOException {

		if (null == files || 0 == files.size()) {
			return null;
		}
		Folder folder = Factory.Folder.fetchInstance(os, folderPath, null);
		return this.createDocuments(os, folder, files);

	}

	/**
	 * byte[]转inputStream
	 * 
	 * @param buf
	 * @return
	 */
	private InputStream byte2Input(byte[] buf) {
		return new ByteArrayInputStream(buf);
	}

	public List<DocumentDTO> createDocumentsByFolderPath(ObjectStore os, String folderPath, PropertiesExDTO properties,
			List<M2ultipartFileDTO> files) throws IOException {

		if (null == files || 0 == files.size()) {
			return null;
		}
		Folder folder = Factory.Folder.fetchInstance(os, folderPath, null);
		List<DocumentDTO> docs = new ArrayList<DocumentDTO>();
		for (M2ultipartFileDTO m2file : files) {

			docs.add(this.createDocument(os, folder, properties, m2file));
		}

		return docs;

	}

	public List<DocumentDTO> createDocumentsByFolder(ObjectStore os, Folder folder, PropertiesExDTO properties,
			List<M2ultipartFileDTO> files) throws IOException {

		if (null == files || 0 == files.size()) {
			return null;
		}

		return this.createDocumentsByFolderPath(os, folder.get_PathName(), properties, files);

	}

	/*
	 * public List<DocumentDTO> createDocumentByFolderEx(ObjectStore os, Folder
	 * folder, PropertiesExDTO properties, List<MultipartFile> files) throws
	 * IOException {
	 * 
	 * if (null == files || 0 == files.size()) { return null; }
	 * 
	 * return this.createDocument(os, folder, properties, files);
	 * 
	 * }
	 */
	@SuppressWarnings("unchecked")
	public List<Document> createDocuments(ObjectStore os, Folder folder, List<MultipartFile> files) throws IOException {

		if (null == files || 0 == files.size()) {
			return null;
		}
		List<Document> docs = new ArrayList<Document>();
		for (MultipartFile file : files) {
			Document doc = Factory.Document.createInstance(os, this.ecmConf.getDefaultDocumentClass());

			// Set document properties.
			doc.getProperties().putValue("DocumentTitle", file.getOriginalFilename());
			doc.set_MimeType(file.getContentType());

			StorageArea sa = Factory.StorageArea.getInstance(os, new Id(ecmConf.getStorageareaId()));
			doc.set_StorageArea(sa);
			doc.save(RefreshMode.REFRESH);

			ContentElementList contentList = Factory.ContentElement.createList();
			ContentTransfer content1 = Factory.ContentTransfer.createInstance();
			content1.set_RetrievalName(file.getOriginalFilename());
			content1.setCaptureSource(file.getInputStream());
			content1.set_ContentType(file.getContentType());
			contentList.add(content1);
			doc.set_ContentElements(contentList);
			doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
			// Check in the document.
			doc.save(RefreshMode.REFRESH);

			// 生成缩略图
			this.createThumbnailRequest(os, doc);

			// File the document.
			ReferentialContainmentRelationship rcr = folder.file(doc, AutoUniqueName.AUTO_UNIQUE,
					file.getOriginalFilename(), DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
			rcr.save(RefreshMode.REFRESH);
			docs.add(doc);
		}
		return docs;
	}

	@SuppressWarnings("unchecked")
	public DocumentDTO createDocument(ObjectStore os, Folder folder, PropertiesExDTO properties, M2ultipartFileDTO file)
			throws IOException {

		Document doc = Factory.Document.createInstance(os, this.ecmConf.getDefaultDocumentClass());

		// Set document properties.
		setDocProperties(doc, file.getOriginalFilename(), properties);

		doc.set_MimeType(file.getContentType());
		doc.set_SecurityFolder(folder);// 继承父文件夹

		StorageArea sa = Factory.StorageArea.getInstance(os, new Id(ecmConf.getStorageareaId()));
		doc.set_StorageArea(sa);
		doc.save(RefreshMode.REFRESH);

		ContentElementList contentList = Factory.ContentElement.createList();
		ContentTransfer content1 = Factory.ContentTransfer.createInstance();
		content1.setCaptureSource(this.byte2Input(file.getContentStream()));
		content1.set_RetrievalName(file.getOriginalFilename());
		content1.set_ContentType(file.getContentType());
		contentList.add(content1);
		doc.set_ContentElements(contentList);
		doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
		// Check in the document.
		doc.save(RefreshMode.REFRESH);

		// 生成缩略图
		this.createThumbnailRequest(os, doc);

		// File the document.
		ReferentialContainmentRelationship rcr = folder.file(doc, AutoUniqueName.AUTO_UNIQUE,
				file.getOriginalFilename(), DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
		rcr.save(RefreshMode.REFRESH);

		return this.document2dto(doc);
	}

	/**
	 * 设置文档属性
	 * 
	 * @param doc
	 * @param documentTitle
	 * @param properties
	 */
	private void setDocProperties(Document doc, String documentTitle, PropertiesExDTO properties) {

		Properties props = doc.getProperties();
		props.putValue("DocumentTitle", documentTitle);
		props.putObjectValue(this.extPropMaterialConf.getSpatialDomain(), properties.getDomain());
		props.putObjectValue(this.extPropMaterialConf.getSearchable(), properties.getSearchable());
		props.putObjectValue(this.extPropMaterialConf.getOrganization(), properties.getOrg());
		props.putObjectValue(this.extPropMaterialConf.getRegion(), properties.getRegion());
		props.putObjectValue(this.extPropMaterialConf.getBusiness(), properties.getBusiness());
		props.putObjectValue(this.extPropMaterialConf.getFileType(), properties.getFileType());
		props.putObjectValue(this.extPropMaterialConf.getTags(), properties.getTags());
		props.putObjectValue(this.extPropMaterialConf.getResourceType(), properties.getResourceType());
		props.putObjectValue(this.extPropMaterialConf.getDescribe(), properties.getDesc());
		props.putObjectValue(this.extCommonPropertyConf.getPublisher(), properties.getPublisher());
		
		if (!StringUtil.isNullOrEmpty(properties.getAssociateProject())) {
			doc.getProperties().putObjectValue(this.extPropMaterialConf.getAssociateProject(),
					properties.getAssociateProject());
		}

	}

	public List<DocumentDTO> createDocument(ObjectStore os, Folder folder, PropertiesExDTO properties,
			List<MultipartFile> files) throws IOException {

		if (null == files || 0 == files.size()) {
			return null;
		}
		List<DocumentDTO> docs = new ArrayList<DocumentDTO>();
		for (MultipartFile file : files) {
			docs.add(createDocument(os, folder, properties, file));
		}
		return docs;
	}

	public DocumentDTO createDocument(ObjectStore os, Folder folder, PropertiesExDTO properties,
			FileContentLocalDTO file) throws IOException {

		return this.createDocument(os, folder, properties, file.getContentType(), file.getDocName(),
				file.getContentInputStream());
	}

	public List<DocumentDTO> createDocuments(ObjectStore os, Folder folder, PropertiesExDTO properties,
			List<FileContentLocalDTO> files) throws IOException {

		List<DocumentDTO> list = new ArrayList<DocumentDTO>();
		for (FileContentLocalDTO f : files) {
			list.add(this.createDocument(os, folder, properties, f.getContentType(), f.getDocName(),
					f.getContentInputStream()));
		}
		return list;
	}

	public DocumentDTO createDocument(ObjectStore os, Folder folder, PropertiesExDTO properties, String contentType,
			String originalFilename, InputStream is) throws IOException {

		Document doc = Factory.Document.createInstance(os, this.ecmConf.getDefaultDocumentClass());

		// Set document properties.
		setDocProperties(doc, originalFilename, properties);
		/*
		 * doc.getProperties().putValue("DocumentTitle", originalFilename);
		 * doc.getProperties().putObjectValue("GZPI_SpatialDomain",
		 * properties.getDomain());
		 * doc.getProperties().putObjectValue("GZPI_Searchable",
		 * properties.getSearchable());
		 * doc.getProperties().putObjectValue("GZPI_Organization",
		 * properties.getOrg());
		 * doc.getProperties().putObjectValue("GZPI_Region",
		 * properties.getRegion());
		 * doc.getProperties().putObjectValue("GZPI_Business",
		 * properties.getBusiness());
		 * doc.getProperties().putObjectValue("GZPI_FileType",
		 * properties.getFileType());
		 * doc.getProperties().putObjectValue("GZPI_Tags",
		 * properties.getTags());
		 * doc.getProperties().putObjectValue("GZPI_ResourceType",
		 * properties.getResourceType());
		 * doc.getProperties().putObjectValue("GZPI_DESCRIBE",
		 * properties.getDesc());
		 */

		doc.set_MimeType(contentType);
		doc.set_SecurityFolder(folder);// 继承父文件夹

		StorageArea sa = Factory.StorageArea.getInstance(os, new Id(ecmConf.getStorageareaId()));
		doc.set_StorageArea(sa);
		doc.save(RefreshMode.REFRESH);

		ContentElementList contentList = Factory.ContentElement.createList();
		ContentTransfer content1 = Factory.ContentTransfer.createInstance();
		content1.setCaptureSource(is);
		content1.set_RetrievalName(originalFilename);
		content1.set_ContentType(contentType);
		contentList.add(content1);
		doc.set_ContentElements(contentList);
		doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
		// Check in the document.
		doc.save(RefreshMode.REFRESH);

		// 生成缩略图
		this.createThumbnailRequest(os, doc);

		// File the document.
		ReferentialContainmentRelationship rcr = folder.file(doc, AutoUniqueName.AUTO_UNIQUE, originalFilename,
				DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
		rcr.save(RefreshMode.REFRESH);
		return this.document2dto(doc);
	}

	public DocumentDTO createDocument(ObjectStore os, Folder folder, PropertiesExDTO properties, MultipartFile file)
			throws IOException {
		// Document doc = Factory.Document.createInstance(os,
		// this.ecmConf.getDefaultDocumentClass());

		return this.createDocument(os, folder, properties, file.getContentType(), file.getOriginalFilename(),
				file.getInputStream());
		// Set document properties.
		/*
		 * doc.getProperties().putValue("DocumentTitle",
		 * file.getOriginalFilename());
		 * //doc.getProperties().putObjectValue("GZPI_DocumentTitle",
		 * file.getOriginalFilename());
		 * doc.getProperties().putObjectValue("GZPI_SpatialDomain",
		 * properties.getDomain());
		 * doc.getProperties().putObjectValue("GZPI_Searchable",
		 * properties.getSearchable());
		 * doc.getProperties().putObjectValue("GZPI_Organization",
		 * properties.getOrg());
		 * doc.getProperties().putObjectValue("GZPI_Region",
		 * properties.getRegion());
		 * doc.getProperties().putObjectValue("GZPI_Business",
		 * properties.getBusiness());
		 * doc.getProperties().putObjectValue("GZPI_FileType",
		 * properties.getFileType());
		 * doc.getProperties().putObjectValue("GZPI_Tags",
		 * properties.getTags());
		 * doc.getProperties().putObjectValue("GZPI_ResourceType",
		 * properties.getResourceType());
		 * 
		 * doc.set_MimeType(file.getContentType());
		 * doc.set_SecurityFolder(folder);// 继承父文件夹
		 * 
		 * StorageArea sa = Factory.StorageArea.getInstance(os, new
		 * Id(ecmConf.getStorageareaId())); doc.set_StorageArea(sa);
		 * doc.save(RefreshMode.REFRESH);
		 * 
		 * ContentElementList contentList = Factory.ContentElement.createList();
		 * ContentTransfer content1 = Factory.ContentTransfer.createInstance();
		 * content1.setCaptureSource(file.getInputStream());
		 * content1.set_RetrievalName(file.getOriginalFilename());
		 * content1.set_ContentType(file.getContentType());
		 * contentList.add(content1); doc.set_ContentElements(contentList);
		 * doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY,
		 * CheckinType.MAJOR_VERSION); // Check in the document.
		 * doc.save(RefreshMode.REFRESH);
		 * 
		 * // 生成缩略图 this.createThumbnailRequest(os, doc);
		 * 
		 * // File the document. ReferentialContainmentRelationship rcr =
		 * folder.file(doc, AutoUniqueName.AUTO_UNIQUE,
		 * file.getOriginalFilename(),
		 * DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
		 * rcr.save(RefreshMode.REFRESH); return this.document2dto(doc);
		 */
	}

	public Document createDocument(ObjectStore os, String title, String folderId, MultipartFile file,
			String versionType) throws IOException {

		Document doc = Factory.Document.createInstance(os, this.ecmConf.getDefaultDocumentClass());

		// Set document properties.
		doc.getProperties().putValue("DocumentTitle", title);
		doc.set_MimeType(file.getContentType());

		StorageArea sa = Factory.StorageArea.getInstance(os, new Id(ecmConf.getStorageareaId()));
		doc.set_StorageArea(sa);
		doc.save(RefreshMode.NO_REFRESH);

		ContentElementList contentList = Factory.ContentElement.createList();
		ContentTransfer content1 = Factory.ContentTransfer.createInstance();
		content1.set_RetrievalName(file.getOriginalFilename());
		content1.setCaptureSource(file.getInputStream());
		content1.set_ContentType(file.getContentType());
		contentList.add(content1);
		doc.set_ContentElements(contentList);
		if ("minor".equalsIgnoreCase(versionType)) {
			doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MINOR_VERSION);
		} else {
			doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
		}
		// Check in the document.
		doc.save(RefreshMode.NO_REFRESH);

		// File the document.
		Folder folder = Factory.Folder.getInstance(os, ClassNames.FOLDER, new Id(folderId));
		ReferentialContainmentRelationship rcr = folder.file(doc, AutoUniqueName.AUTO_UNIQUE, title,
				DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
		rcr.save(RefreshMode.NO_REFRESH);
		return doc;
	}

	@SuppressWarnings("unchecked")
	public void createDocuments(ObjectStore os, String title, String folderId, List<FileItem> uploadedForlder,
			String versionType) throws IOException {
		Document doc = Factory.Document.createInstance(os, this.ecmConf.getDefaultDocumentClass());

		// Set document properties.
		doc.getProperties().putValue("DocumentTitle", title);
		doc.set_MimeType("text/plain");

		StorageArea sa = Factory.StorageArea.getInstance(os, new Id(ecmConf.getStorageareaId()));
		doc.set_StorageArea(sa);

		ContentElementList contentList = Factory.ContentElement.createList();
		for (FileItem fileItem : uploadedForlder) {
			ContentTransfer content = Factory.ContentTransfer.createInstance();
			content.setCaptureSource(fileItem.getInputStream());
			content.set_ContentType(fileItem.getContentType());
			content.set_RetrievalName(fileItem.getName());
			contentList.add(content);
		}

		doc.set_ContentElements(contentList);
		if ("minor".equalsIgnoreCase(versionType)) {
			doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MINOR_VERSION);
		} else {
			doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
		}
		// Check in the document.
		doc.save(RefreshMode.NO_REFRESH);

		// File the document.
		Folder folder = Factory.Folder.getInstance(os, ClassNames.FOLDER, new Id(folderId));
		ReferentialContainmentRelationship rcr = folder.file(doc, AutoUniqueName.AUTO_UNIQUE, title,
				DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
		rcr.save(RefreshMode.NO_REFRESH);

	}

	/**
	 * 创建空文档
	 * 
	 * @param os
	 * @param title
	 * @param folderId
	 * @return
	 */
	public Document createEmptyDocument(ObjectStore os, String title, String folderId) {

		Document doc = Factory.Document.createInstance(os, this.ecmConf.getDefaultDocumentClass());

		// Set document properties.
		doc.getProperties().putValue("DocumentTitle", title);
		doc.set_MimeType("text/plain");

		StorageArea sa = Factory.StorageArea.getInstance(os, new Id(ecmConf.getStorageareaId()));
		doc.set_StorageArea(sa);
		doc.save(RefreshMode.NO_REFRESH);

		// Check in the document.
		doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
		doc.save(RefreshMode.NO_REFRESH);
		// doc.checkout(com.filenet.api.constants.ReservationType.EXCLUSIVE,
		// null, doc.getClassName(), doc.getProperties());
		// doc.save(RefreshMode.REFRESH);
		// System.out.println(doc.get_Id().toString());

		// File the document.
		Folder folder = Factory.Folder.getInstance(os, ClassNames.FOLDER, new Id(folderId));
		ReferentialContainmentRelationship rcr = folder.file(doc, AutoUniqueName.AUTO_UNIQUE, title,
				DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
		rcr.save(RefreshMode.NO_REFRESH);
		System.out.println("create document  success!");
		return doc;
	}

	/**
	 * 根据文档id删除
	 * 
	 * @param os
	 * @param id
	 */
	public boolean deleteDocument(ObjectStore os, String id) {
		Document doc = Factory.Document.fetchInstance(os, new Id(id), null);
		doc.delete();
		doc.save(RefreshMode.REFRESH);
		//System.out.println("根据文档id delete success!");
		return true;
	}

	/**
	 * 根据文档系列id删除
	 * 
	 * @param os
	 * @param versionSeriesId
	 */
	public boolean deleteDocumentByVId(ObjectStore os, String versionSeriesId) {

		VersionSeries vs = Factory.VersionSeries.fetchInstance(os, new Id(versionSeriesId), null);
		vs.delete();
		vs.save(RefreshMode.NO_REFRESH);
		System.out.println("根据文档系列id delete success!");

		return true;
	}

	/**
	 * 获取文档的缩略图，并转base64编码
	 * 
	 * @param doc
	 * @param thumbnailByte 是否返回字节流
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ThumbnailDTO getThumbnail(Document doc, boolean thumbnailByte) {

		ThumbnailDTO dto = new ThumbnailDTO();
		dto.setName(doc.get_Name());
		CmThumbnailSet thumbnails = doc.get_CmThumbnails();
		if (thumbnails.isEmpty())
			return dto;
		Iterator<Document> iter = thumbnails.iterator();
		while (iter.hasNext()) {
			CmThumbnail tn = (CmThumbnail) iter.next();
			dto.setId(doc.get_Id().toString());
			dto.setVid(doc.get_VersionSeries().get_Id().toString());
			dto.setMimeType(tn.get_MimeType());
			if(thumbnailByte) {
				dto.setImgByte(tn.get_Image());
			} else {
				dto.setImg(Base64Utils.encodeToString(tn.get_Image()));
			}
			return dto;
		}
		return dto;
	}

	/**
	 * 生成缩略图
	 * 
	 * @param os
	 * @param docId
	 */
	public void createThumbnailRequest(ObjectStore os, String docId) {

		PropertyFilter pf = new PropertyFilter();
		pf.addIncludeProperty(new FilterElement(null, null, null, "ContentElements ElementSequenceNumber", null));
		Document doc = Factory.Document.fetchInstance(os, new Id(docId), pf);
		logger.info("createThumbnailRequest，文档名称：[{}]", doc.get_Name());

		// Get document's content elements.
		ContentElementList cel = doc.get_ContentElements();
		Iterator<Document> iter = cel.iterator();
		ContentElement ce;
		CmThumbnailRequest request;

		// Iterate content elements, and create a thumbnail request for each
		// content element.
		while (iter.hasNext()) {
			ce = (ContentElement) iter.next();
			request = Factory.CmThumbnailRequest.createInstance(os, ClassNames.CM_THUMBNAIL_REQUEST);
			request.set_InputDocument(doc);

			request.set_ElementSequenceNumber(ce.get_ElementSequenceNumber());
			request.save(RefreshMode.REFRESH);
		}
	}

	/**
	 * 生成缩略图
	 * 
	 * @param os
	 * @param doc
	 */
	public void createThumbnailRequest(ObjectStore os, Document doc) {

		logger.info("createThumbnailRequest，文档名称：[{}]", doc.get_Name());

		// Get document's content elements.
		ContentElementList cel = doc.get_ContentElements();
		Iterator<Document> iter = cel.iterator();
		ContentElement ce;
		CmThumbnailRequest request;

		// Iterate content elements, and create a thumbnail request for each
		// content element.
		while (iter.hasNext()) {
			ce = (ContentElement) iter.next();
			request = Factory.CmThumbnailRequest.createInstance(os, ClassNames.CM_THUMBNAIL_REQUEST);
			request.set_InputDocument(doc);

			request.set_ElementSequenceNumber(ce.get_ElementSequenceNumber());
			request.save(RefreshMode.REFRESH);
		}
	}

	/**
	 * 根据id检索文档
	 * 
	 * @param os
	 * @param id
	 * @return
	 */
	public Document loadById(ObjectStore os, String id) {

		return Factory.Document.fetchInstance(os, new Id(id), null);
	}

	/**
	 * 根据id获取文档的DTO模型
	 * 
	 * @param os
	 * @param id
	 * @return
	 */
	public DocumentDTO loadDocDTOById(ObjectStore os, String id) {

		return this.document2dto(Factory.Document.fetchInstance(os, new Id(id), null));
	}

	/**
	 * 获取文档dto，主要获取inputstream
	 * 
	 * @param os
	 * @param id
	 * @return
	 */
	public FileContentLocalDTO loadLocalById(ObjectStore os, String id) {

		Document doc = this.loadById(os, id);
		FileContentLocalDTO dto = new FileContentLocalDTO();
		dto.setSize(doc.get_ContentSize().intValue());
		dto.setLsize(doc.get_ContentSize().longValue());
		dto.setDocName(doc.get_Name());
		dto.setContentType(doc.get_MimeType());
		// ContentElementList contents = doc.get_ContentElements();

		InputStream is = doc.accessContentStream(0);
		dto.setContentInputStream(is);
		dto.setDocProperties(this.document2dto(doc));
		/*
		 * Iterator it = contents.iterator(); while (it.hasNext()) {
		 * ContentTransfer ct = (ContentTransfer) it.next(); is =
		 * ct.accessContentStream(); dto.setContentInputStream(is); break; }
		 */

		return dto;
	}
	/**
	 * 根据版本系列id，转换成本地文件内容
	 * @param os
	 * @param vid
	 * @return
	 */
	public FileContentLocalDTO loadLocalByVId(ObjectStore os, String vid) {

		Document doc = this.loadByVersionSeriesId(os, vid);
		FileContentLocalDTO dto = new FileContentLocalDTO();
		// 主体文档的id
		dto.setGuid(doc.get_Id().toString());
		dto.setSize(doc.get_ContentSize().intValue());
		dto.setLsize(doc.get_ContentSize().longValue());
		dto.setDocName(doc.get_Name());
		dto.setContentType(doc.get_MimeType());
		// ContentElementList contents = doc.get_ContentElements();

		InputStream is = doc.accessContentStream(0);
		dto.setContentInputStream(is);
		dto.setDocProperties(this.document2dto(doc));

		return dto;
	}

	/**
	 * 根据版本系列号获取最新文档
	 * 
	 * @param os
	 * @param vid
	 * @return
	 */
	public Document loadByVersionSeriesId(ObjectStore os, String vid) {

		return (Document) Factory.VersionSeries.fetchInstance(os, new Id(vid), null).get_CurrentVersion();
	}

	public DocumentDTO loadDocDTOByVersionSeriesId(ObjectStore os, String vid) {

		return this.document2dto(
				(Document) Factory.VersionSeries.fetchInstance(os, new Id(vid), null).get_CurrentVersion());
	}

	/**
	 * 根据版本系列id获取当前文档的所有文档
	 * 
	 * @param os
	 * @param vid
	 * @return
	 */
	public List<DocumentDTO> loadDocDTOsByVersionSeriesId(ObjectStore os, String vid) {

		List<DocumentDTO> docs = new ArrayList<DocumentDTO>();

		VersionSeries vs = Factory.VersionSeries.fetchInstance(os, new Id(vid), null);
		@SuppressWarnings("unchecked")
		Iterator<Document> iterator = vs.get_Versions().iterator();
		while (iterator.hasNext()) {
			docs.add(this.document2dto(iterator.next()));
		}
		return docs;
	}

	/**
	 * CE的文档类转换成系统模型
	 * 
	 * @param document
	 * @return
	 */
	public DocumentDTO document2dto(Document document) {

		if (null == document) {
			return null;
		}
		logger.debug(">>>" + document.get_Name() + "   " + document.get_MimeType());
		// System.out.println(document.get_Name()+" "+ document.get_MimeType());
		DocumentDTO dto = new DocumentDTO();
		dto.setIsFolder(false);
		dto.setId(document.get_Id().toString());
		dto.setName(document.getProperties().getStringValue("DocumentTitle"));
		dto.setText(document.getProperties().getStringValue("DocumentTitle"));
		dto.setModifiedBy(document.get_LastModifier());
		dto.setModifiedOn(document.get_DateLastModified().getTime());
		dto.setDateCreated(document.get_DateCreated().getTime());
		dto.setCreator(document.get_Creator());
		dto.setOwner(document.get_Owner());
		dto.setMajorVersion(document.get_MajorVersionNumber());
		dto.setMinorVersion(document.get_MinorVersionNumber());
		dto.setSize(document.get_ContentSize());
		dto.setIsCheckout(document.get_IsReserved());
		dto.setVersionStatus(document.get_VersionStatus().toString());

		document.get_Owner();
		// 项目扩展的文档类字段
		dto.setFileType(document.getProperties().isPropertyPresent(extPropMaterialConf.getFileType())
				? document.getProperties().getStringValue(extPropMaterialConf.getFileType()) : "");
		dto.setOrganization(document.getProperties().isPropertyPresent(extPropMaterialConf.getOrganization())
				? document.getProperties().getStringValue(extPropMaterialConf.getOrganization()) : "");
		dto.setRegion(document.getProperties().isPropertyPresent(extPropMaterialConf.getRegion())
				? document.getProperties().getStringValue(extPropMaterialConf.getRegion()) : "");
		dto.setBusiness(document.getProperties().isPropertyPresent(extPropMaterialConf.getBusiness())
				? document.getProperties().getStringValue(extPropMaterialConf.getBusiness()) : "");
		dto.setDocTags(document.getProperties().isPropertyPresent(extPropMaterialConf.getTags())
				? document.getProperties().getStringValue(extPropMaterialConf.getTags()) : "");
		dto.setResourceType(document.getProperties().isPropertyPresent(extPropMaterialConf.getResourceType())
				? document.getProperties().getStringValue(extPropMaterialConf.getResourceType()) : "");
		dto.setDescribe(document.getProperties().isPropertyPresent(extPropMaterialConf.getDescribe())
				? document.getProperties().getStringValue(extPropMaterialConf.getDescribe()) : "");
		dto.setDomain(document.getProperties().isPropertyPresent(extPropMaterialConf.getSpatialDomain())
				? document.getProperties().getStringValue(extPropMaterialConf.getSpatialDomain()) : "");
		dto.setPublisher(document.getProperties().isPropertyPresent(this.extCommonPropertyConf.getPublisher())? 
				document.getProperties().getStringValue(this.extCommonPropertyConf.getPublisher()) : "");
		
		// 获取documen的content的类型。
		ContentElementList contentElements = document.get_ContentElements();
		if (!contentElements.isEmpty()) {
			ContentElement element = (ContentElement) contentElements.get(contentElements.size() - 1);
			dto.setMimeType(element.get_ContentType());
		}
		// 获取版本系列号
		VersionSeries vs = (VersionSeries) document.getProperties().getObjectValue("VersionSeries");
		
		dto.setVersionSeriesId(null == vs ? "" : vs.get_Id().toString());
		return dto;
	}

	public DocumentDTO properties2dto(Properties properties) {

		DocumentDTO dto = new DocumentDTO();
		dto.setIsFolder(false);
		dto.setId(properties.getIdValue("Id").toString());
		dto.setName(properties.isPropertyPresent("DocumentTitle") ? properties.getStringValue("DocumentTitle") : "");
		dto.setModifiedBy(
				properties.isPropertyPresent("LastModifier") ? properties.getStringValue("LastModifier") : "");
		dto.setModifiedOn(properties.isPropertyPresent("DateLastModified")
				? properties.getDateTimeValue("DateLastModified").getTime() : null);
		dto.setOwner(properties.isPropertyPresent("Owner") ? properties.getStringValue("Owner") : "");
		dto.setMajorVersion(properties.isPropertyPresent("MajorVersionNumber")
				? properties.getInteger32Value("MajorVersionNumber") : null);
		dto.setMinorVersion(properties.isPropertyPresent("MinorVersionNumber")
				? properties.getInteger32Value("MinorVersionNumber") : null);
		dto.setSize(properties.isPropertyPresent("ContentSize") ? properties.getFloat64Value("ContentSize") : 0);
		dto.setDateCreated(properties.getDateTimeValue("DateCreated").getTime());
		dto.setRank(properties.isPropertyPresent("Rank") ? properties.getFloat64Value("Rank") : 0);

		// dto.setIsCheckout(document.get_IsReserved());
		// dto.setVersionStatus(document.get_VersionStatus().toString());

		// 项目扩展的文档类字段
		dto.setFileType(properties.isPropertyPresent(extPropMaterialConf.getFileType())
				? properties.getStringValue(extPropMaterialConf.getFileType()) : "");
		dto.setOrganization(properties.isPropertyPresent(extPropMaterialConf.getOrganization())
				? properties.getStringValue(extPropMaterialConf.getOrganization()) : "");
		dto.setRegion(properties.isPropertyPresent(extPropMaterialConf.getRegion())
				? properties.getStringValue(extPropMaterialConf.getRegion()) : "");
		dto.setBusiness(properties.isPropertyPresent(extPropMaterialConf.getBusiness())
				? properties.getStringValue(extPropMaterialConf.getBusiness()) : "");
		dto.setDocTags(properties.isPropertyPresent(extPropMaterialConf.getTags())
				? properties.getStringValue(extPropMaterialConf.getTags()) : "");
		dto.setResourceType(properties.isPropertyPresent(extPropMaterialConf.getResourceType())
				? properties.getStringValue(extPropMaterialConf.getResourceType()) : "");
		dto.setDescribe(properties.isPropertyPresent(extPropMaterialConf.getDescribe())
				? properties.getStringValue(extPropMaterialConf.getDescribe()) : "");
		dto.setDomain(properties.isPropertyPresent(extPropMaterialConf.getSpatialDomain())
				? properties.getStringValue(extPropMaterialConf.getSpatialDomain()) : "");

		// 获取documen的content的类型。
		dto.setMimeType(properties.isPropertyPresent("MimeType") ? properties.getStringValue("MimeType") : "");

		// 获取版本系列号
		dto.setVersionSeriesId(properties.isPropertyPresent("VersionSeries")
				? ((VersionSeriesImpl) properties.getEngineObjectValue("VersionSeries")).get_Id().toString() : "");

		return dto;
	}

	/**
	 * 转换成DTO
	 * 
	 * @param set
	 * @return
	 */
	public List<DocumentDTO> document2dto(DocumentSet set) {

		List<DocumentDTO> documentDTOs = new ArrayList<DocumentDTO>();

		Iterator<Document> documentIte = set.iterator();

		while (documentIte.hasNext()) {

			Document document = (Document) documentIte.next();

			documentDTOs.add(this.document2dto(document));

		}

		return documentDTOs;
	}

	/**
	 * 转换成DTO
	 * 
	 * @param documents
	 *            文档数组
	 * @return
	 */
	public List<DocumentDTO> document2dto(Object[] documents) {
		List<DocumentDTO> documentDTOs = new ArrayList<DocumentDTO>();

		for (Object obj : documents) {

			Document document = (Document) obj;
			documentDTOs.add(this.document2dto(document));
		}

		return documentDTOs;
	}
	/**
	 * 
	 * @param documents
	 * @param needThumbnails 是否需要缩略图
	 * @return
	 */
	public List<DocumentDTO> document2dto(Object[] documents, boolean needThumbnails, boolean thumbnailByte) {
		List<DocumentDTO> documentDTOs = new ArrayList<DocumentDTO>();

		DocumentDTO dto = null;
		for (Object obj : documents) {

			Document document = (Document) obj;
			dto = this.document2dto(document);
			documentDTOs.add(dto);
			if(needThumbnails) {
				dto.setThumbnail(this.getThumbnail(document, thumbnailByte));
			}
		}

		return documentDTOs;
	}

	public List<DocumentDTO> repositoryRow2dto(Object[] rows) {
		List<DocumentDTO> documentDTOs = new ArrayList<DocumentDTO>();

		for (Object obj : rows) {

			RepositoryRow row = (RepositoryRowImpl) obj;
			documentDTOs.add(this.repositoryRow2dto(row));
		}

		return documentDTOs;
	}

	public DocumentDTO repositoryRow2dto(RepositoryRow row) {

		return this.properties2dto(row.getProperties());
	}

	/**
	 * 文件移动到目标文件夹
	 * 
	 * @param docId
	 * @param folderId
	 * @return
	 */
	public boolean moveToFolder(Document doc, Folder folder) {

		ReferentialContainmentRelationship rcr = folder.file(doc, AutoUniqueName.AUTO_UNIQUE, doc.get_Name(),
				DefineSecurityParentage.DEFINE_SECURITY_PARENTAGE);

		Folder securityFolder = doc.get_SecurityFolder();
		ReferentialContainmentRelationship orginFolder = securityFolder.unfile(doc);
		orginFolder.save(RefreshMode.REFRESH);

		rcr.save(RefreshMode.REFRESH);

		return true;
	}

	/**
	 * 文件移动到目标文件夹
	 * 
	 * @param os
	 * @param docId
	 * @param folderId
	 * @return
	 */
	public boolean moveToFolder(ObjectStore os, String docId, String folderId) {

		Document doc = Factory.Document.fetchInstance(os, new Id(docId), null);
		Folder targetFolder = Factory.Folder.fetchInstance(os, new Id(folderId), null);

		return moveToFolder(doc, targetFolder);

	}

	/**
	 * 
	 * @param os
	 * @param docId
	 * @param folder
	 * @return
	 */
	public boolean moveToFolder(ObjectStore os, String docId, Folder folder) {

		Document doc = Factory.Document.fetchInstance(os, new Id(docId), null);

		return moveToFolder(doc, folder);

	}

	/**
	 * 
	 * @param os
	 * @param docIds
	 * @param folderId
	 */
	public void moveToFolder(ObjectStore os, String[] docIds, String folderId) {

		Folder targetFolder = Factory.Folder.fetchInstance(os, new Id(folderId), null);
		for (String docId : docIds) {
			this.moveToFolder(os, docId, targetFolder);
		}

	}

	public void moveToFolder(ObjectStore os, List<String> docIds, String folderId) {

		Folder targetFolder = Factory.Folder.fetchInstance(os, new Id(folderId), null);
		for (String docId : docIds) {
			this.moveToFolder(os, docId, targetFolder);
		}

	}

	public void moveToFolder(ObjectStore os, List<String> docIds, Folder targetFolder) {

		for (String docId : docIds) {
			this.moveToFolder(os, docId, targetFolder);
		}

	}

	/**
	 * 
	 * @param docs
	 * @param folder
	 */
	public void moveToFolder(Document[] docs, Folder folder) {

		for (Document doc : docs) {
			this.moveToFolder(doc, folder);
		}

	}

	/**
	 * 添加新版本
	 * 
	 * @param os
	 * @param docId
	 *            文档id
	 * @param is
	 *            新文件版本流
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DocumentDTO newVersion(ObjectStore os, String docId, InputStream is) {

		// Get document.
		Document doc = Factory.Document.fetchInstance(os, new Id(docId), null);
		String docName = doc.get_Name();
		// check out
		doc.checkout(com.filenet.api.constants.ReservationType.EXCLUSIVE, null, doc.getClassName(),
				doc.getProperties());
		//doc.save(RefreshMode.REFRESH);

		if (doc.get_IsCurrentVersion().booleanValue() == false) {
			doc = (Document) doc.get_CurrentVersion();
		}
		if (doc.get_IsReserved().booleanValue() == true
				&& (doc.get_VersionStatus().getValue() != VersionStatus.RESERVATION_AS_INT)) {
			doc = (Document) doc.get_Reservation();
			// 沿用前面文档的标题
			doc.getProperties().putValue("DocumentTitle", docName);
			ContentElementList contentList = Factory.ContentElement.createList();
			ContentTransfer content1 = Factory.ContentTransfer.createInstance();
			content1.setCaptureSource(is);
			content1.set_ContentType(doc.get_MimeType());
			contentList.add(content1);
			doc.set_ContentElements(contentList);
			// check in
			doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
			doc.save(RefreshMode.REFRESH);
			logger.info(">>>新版本上传成功");
		}
		return this.document2dto((Document) doc.get_CurrentVersion());
	}
	/**
	 * 新上传版本，并重置名称
	 * @param os
	 * @param docId
	 * @param is
	 * @param newName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DocumentDTO newVersion(ObjectStore os, String docId, InputStream is, String newName) {

		// Get document.
		Document doc = Factory.Document.fetchInstance(os, new Id(docId), null);
		// check out
		doc.checkout(com.filenet.api.constants.ReservationType.EXCLUSIVE, null, doc.getClassName(), doc.getProperties());
		//doc.save(RefreshMode.REFRESH);

		if (doc.get_IsCurrentVersion().booleanValue() == false) {
			doc = (Document) doc.get_CurrentVersion();
		}
		if (doc.get_IsReserved().booleanValue() == true
				&& (doc.get_VersionStatus().getValue() != VersionStatus.RESERVATION_AS_INT)) {
			doc = (Document) doc.get_Reservation();
			doc.getProperties().putValue("DocumentTitle", newName);
			ContentElementList contentList = Factory.ContentElement.createList();
			ContentTransfer content1 = Factory.ContentTransfer.createInstance();
			content1.setCaptureSource(is);
			content1.set_ContentType(doc.get_MimeType());
			contentList.add(content1);
			doc.set_ContentElements(contentList);
			// check in
			doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
			doc.save(RefreshMode.REFRESH);
			logger.info(">>>新版本上传成功");
			
			logger.info(">>>文档标题同步更新到其它版本");
			syncDocVersionTitle(os, newName, doc);
		}
		return this.document2dto((Document) doc.get_CurrentVersion());
	}

	private void syncDocVersionTitle(ObjectStore os, String newName, Document doc) {
		VersionSeries vs = Factory.VersionSeries.fetchInstance(os, new Id(doc.get_VersionSeries().get_Id().toString()), null);
		Iterator<Document> iterator = vs.get_Versions().iterator();
		Document subDoc = null;
		while (iterator.hasNext()) {
			subDoc = iterator.next();
			subDoc.getProperties().putObjectValue("DocumentTitle", newName);
			subDoc.save(RefreshMode.REFRESH);
		}
	}

	/**
	 * 获取文档资源的空间域编码
	 * 
	 * @param os
	 * @param resId
	 * @return
	 */
	public String getDocResDomainCode(ObjectStore os, String resId) {

		Document doc = this.loadById(os, resId);
		if (null == doc)
			return "";

		return this.getDocResDomainCode(doc);

	}

	/**
	 * 获取文档资源的空间域编码
	 * 
	 * @param doc
	 * @return
	 */
	public String getDocResDomainCode(Document doc) {
		String domainCode = doc.getProperties().isPropertyPresent(this.extPropMaterialConf.getSpatialDomain())
				? doc.getProperties().getStringValue(this.extPropMaterialConf.getSpatialDomain()) : "";
		return domainCode;
	}

	/**
	 * 获取文档资源的类型编码
	 * 
	 * @param os
	 * @param resId
	 * @return
	 */
	public String getDocResTypeCode(ObjectStore os, String resId) {

		Document doc = this.loadById(os, resId);
		if (null == doc)
			return "";

		return this.getDocResTypeCode(doc);
	}

	/**
	 * 获取文档的资源类型
	 * 
	 * @param doc
	 * @return
	 */
	public String getDocResTypeCode(Document doc) {

		if (null == doc)
			return "";

		String resTypeCode = doc.getProperties().isPropertyPresent(this.extPropMaterialConf.getResourceType())
				? doc.getProperties().getStringValue(this.extPropMaterialConf.getResourceType()) : "";
		return resTypeCode;
	}

	/**
	 * 获取文档资源的版本系列id
	 * 
	 * @param doc
	 * @return
	 */
	public String getDocVersionSeriesId(Document doc) {

		if (null == doc)
			return "";

		return doc.get_VersionSeries().get_Id().toString();// .getProperties().isPropertyPresent("VersionSeries")?
															// doc.getProperties().getStringValue("VersionSeries")
															// : "";
	}
	/**
	 * 删除指定文件夹下的一个文件
	 * @param os
	 * @param parentFolder 所在文件夹
	 * @param docName 文件名称
	 */
	public void deleteDocument(ObjectStore os, Folder parentFolder, String docName) {
		
		this.deleteDocumentByPath(os, parentFolder.get_PathName() + "/" + docName);
	}
	/**
	 * 根据路径删除文档
	 * @param os
	 * @param path
	 */
	public void deleteDocumentByPath(ObjectStore os, String path) {
		try{
		Document doc = Factory.Document.fetchInstance(os, path, null);
		if(null == doc) 
			return;
		
		doc.delete();
		doc.save(RefreshMode.REFRESH);
		}catch(Exception e){
			logger.warn(">>>删除文档出现警告，详情："+e.getMessage());
		}
	}
	/**
	 * 重命名文档名称，包括它的所有子版本的名称
	 * @param objectStore
	 * @param docId
	 * @param newName
	 */
	public void modifyNameById(ObjectStore objectStore, String docId, String newName) {
		Document doc = Factory.Document.fetchInstance(objectStore, new Id(docId), null);
		if(null == doc) {
			logger.warn(">>>无法找到文档，id:{}", docId);
			return;
		}
		doc.getProperties().putObjectValue("DocumentTitle", newName);
		doc.save(RefreshMode.REFRESH);
		VersionSeries vs = Factory.VersionSeries.fetchInstance(objectStore, new Id(doc.get_VersionSeries().get_Id().toString()), null);
		@SuppressWarnings("unchecked")
		Iterator<Document> iterator = vs.get_Versions().iterator();
		Document subDoc = null;
		while (iterator.hasNext()) {
			subDoc = iterator.next();
			subDoc.getProperties().putObjectValue("DocumentTitle", newName);
			subDoc.save(RefreshMode.REFRESH);
		}
	}
	/**
	 * 根据版本系列id获取文档信息
	 * @param objectStore 存储库
	 * @param vid 版本系列id
	 * @param needThumbnails 是否需要缩略图
	 * @param thumbnailByte 是否返回缩略图字节流
	 * @return
	 */
	public DocumentDTO loadDocDTOByVersionSeriesId(ObjectStore os, String vid, boolean needThumbnails, boolean thumbnailByte) {
	
		Document doc = (Document) Factory.VersionSeries.fetchInstance(os, new Id(vid), null).get_CurrentVersion();
		DocumentDTO dto = this.document2dto(doc);
		if(needThumbnails) {
			dto.setThumbnail(this.getThumbnail(doc, thumbnailByte));
		}
		return dto;
	}
	/**
	 * 创建新版本
	 * @param os
	 * @param docId
	 * @param propertiesValues 额外需要更新属性的值
	 * @param is
	 * @return
	 */
	public DocumentDTO newVersion(ObjectStore os, String docId, Map<String, Object> propertiesValues,
			InputStream is) {
		// Get document.
		Document doc = Factory.Document.fetchInstance(os, new Id(docId), null);
		String docName = doc.get_Name();
		// check out
		doc.checkout(com.filenet.api.constants.ReservationType.EXCLUSIVE, null, doc.getClassName(),
				doc.getProperties());
		//doc.save(RefreshMode.REFRESH);

		if (doc.get_IsCurrentVersion().booleanValue() == false) {
			doc = (Document) doc.get_CurrentVersion();
		}
		if (doc.get_IsReserved().booleanValue() == true
				&& (doc.get_VersionStatus().getValue() != VersionStatus.RESERVATION_AS_INT)) {
			doc = (Document) doc.get_Reservation();
			// 沿用前面文档的标题
			doc.getProperties().putValue("DocumentTitle", docName);
			// 更新属性
			if(propertiesValues != null && !propertiesValues.isEmpty()) {
				Iterator iter = propertiesValues.entrySet().iterator();
				while (iter.hasNext()) {
				   Entry<String, Object> entry = (Entry<String, Object>) iter.next();
				   doc.getProperties().putObjectValue(entry.getKey(), entry.getValue());
				}
			}
			ContentElementList contentList = Factory.ContentElement.createList();
			ContentTransfer content1 = Factory.ContentTransfer.createInstance();
			content1.setCaptureSource(is);
			content1.set_ContentType(doc.get_MimeType());
			contentList.add(content1);
			doc.set_ContentElements(contentList);
			// check in
			doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
			doc.save(RefreshMode.REFRESH);
			logger.info(">>>新版本上传成功");
		}
		return this.document2dto((Document) doc.get_CurrentVersion());
	}
	/**
	 * 创建新版本
	 * @param os
	 * @param docId
	 * @param propertiesValues 额外需要更新属性的值
	 * @param is
	 * @return
	 */
	public DocumentDTO newVersionEx(ObjectStore os, String docId, Map<String, Object> propertiesValues,
			InputStream is) {
		// Get document.
		Document doc = Factory.Document.fetchInstance(os, new Id(docId), null);
		String docName = doc.get_Name();
		doc = (Document) doc.get_CurrentVersion();
		if (!doc.get_IsReserved()) {
			doc.checkout(ReservationType.EXCLUSIVE, null, doc.getClassName(),doc.getProperties());
			doc.save(RefreshMode.REFRESH);
		}
		Document reservation = (Document) doc.get_Reservation();
		Properties props = reservation.getProperties();
		// Change property value.
		props.putValue("DocumentTitle", docName);
		// 更新属性
		if (propertiesValues != null && !propertiesValues.isEmpty()) {
			Iterator iter = propertiesValues.entrySet().iterator();
			while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				Entry<String, Object> entry = (Entry<String, Object>) iter.next();
				props.putObjectValue(entry.getKey(), entry.getValue());
			}
		}
		ContentElementList contentList = Factory.ContentElement.createList();
		ContentTransfer content1 = Factory.ContentTransfer.createInstance();
		content1.setCaptureSource(is);
		content1.set_ContentType(doc.get_MimeType());
		contentList.add(content1);
		reservation.set_ContentElements(contentList);
		reservation.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
		reservation.save(RefreshMode.REFRESH);
		Versionable version = doc.get_CurrentVersion();

		logger.info(">>>新版本上传成功");
		return this.document2dto((Document) version);
	}
}
