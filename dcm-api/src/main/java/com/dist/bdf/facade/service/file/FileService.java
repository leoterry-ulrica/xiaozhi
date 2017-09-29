package com.dist.bdf.facade.service.file;

import java.io.InputStream;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.model.dto.cad.TemplateDTO;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
import com.dist.bdf.model.dto.dcm.FileContentLocalDTO;
import com.dist.bdf.model.dto.dcm.PropertiesDTO;
import com.dist.bdf.model.dto.dcm.PropertiesExDTO;
import com.dist.bdf.model.dto.sga.UserAttachmentDTO;
import com.dist.bdf.model.dto.system.CopyFileDTO;

public interface FileService  {
	
    String getPersonalPath(String userId);
    
    /**
     * 
     * @param docId
     * @return
     */
    FileContentLocalDTO getDocContentLocal(String docId);
    
    FileContentLocalDTO getDocContentLocal(String realm, String userId, String password, String docId);

	Object uploadFiles(String userId, String password, String parentFolderId,  PropertiesExDTO propertiesEx, List<MultipartFile> files);
	
	Object uploadFiles(String realm, String userId, String password, String parentFolderId,  PropertiesExDTO propertiesEx, List<MultipartFile> files) throws BusinessException;
	
	Object uploadLocalFiles(String userId, String password, String parentFolderId,  PropertiesExDTO propertiesEx, List<FileContentLocalDTO> files);
	Object uploadLocalFiles(String realm, String userId, String password, String parentFolderId,  PropertiesExDTO propertiesEx, List<FileContentLocalDTO> files) throws BusinessException;
	
	DocumentDTO uploadFiles(String userId, String password, String parentFolderId,  PropertiesExDTO propertiesEx, FileContentLocalDTO file) throws BusinessException;
	/**
	 * 检测文件信息
	 * @param docId
	 * @return
	 */
	String checkFileInfo(String docId);
	
	/**
	 * 获取文件的基本信息
	 * @param realm 域，如：数慧，dist
	 * @param docId
	 * @return
	 */
	String checkFileInfo(String realm, String docId);
	
	int addDownloadCountOfSummaryData(String documentId);
	
	int addDownloadCountOfSummaryData(String realm, String documentId);

	FileContentLocalDTO getDocContentLocal(String realm, String docId);
	/**
	 * 建立新版本
	 * @param realm
	 * @param docId
	 * @param is
	 * @return
	 */
	DocumentDTO newVersion(String realm, String docId, InputStream is);
	/**
	 * 从外面上传项目文件
	 * @param realm
	 * @param projectId
	 * @param files
	 * @return
	 */
	Object uploadProjectFilesExternal(String realm, String projectId, List<MultipartFile> files) throws BusinessException;
	/**
	 * 通过外部上传项目文件
	 * @param properties
	 * @param uploadMFiles
	 * @return
	 */
	Object uploadProjectFilesExternal(PropertiesDTO properties, List<MultipartFile> uploadMFiles);
	/**
	 * 上传cad模板文件，并返回文档id
	 * @param realm 域
	 * @param fileId 文件id
	 * @param uploadMFiles
	 * @return
	 */
	DocumentDTO uploadCADTemplate(TemplateDTO templateDTO, List<MultipartFile> uploadMFiles);
	/**
	 * 上传公共文件（资讯）
	 * @param properties
	 * @param uploadMFiles
	 * @return
	 * @throws BusinessException
	 */
	List<DocumentDTO> uploadPublicFiles(PropertiesDTO properties, List<MultipartFile> uploadMFiles) throws BusinessException;
	/**
	 * 新版本上传
	 * @param realm
	 * @param docId
	 * @param fileContentLocalDTO
	 * @return
	 */
	DocumentDTO newVersion(String realm, String docId, FileContentLocalDTO fileContentLocalDTO);
	/**
	 * 
	 * @param realm
	 * @param docId
	 * @param multipartFile
	 * @return
	 */
	DocumentDTO newVersion(String realm, String docId, MultipartFile multipartFile);
	/**
	 * 上传个人简历
	 * @param dto
	 * @param uploadMFiles
	 * @return
	 */
	List<DocumentDTO> uploadPersonalResume(UserAttachmentDTO dto, List<MultipartFile> uploadMFiles);
	/**
	 * 从企业中拷贝文件到公共存储库，目前只支持文档拷贝
	 * @param dto
	 * @return
	 */
	DocumentDTO copyFile2PublicFromBusiness(CopyFileDTO dto);
	/**
	 * 
	 * @param realm
	 * @param docId
	 * @param publisher
	 * @param multipartFile
	 * @return
	 */
	DocumentDTO newVersion(String realm, String docId, String publisher, MultipartFile multipartFile);
	/**
	 * 提供给BPM上传资料
	 * @param properties
	 * @param uploadMFiles
	 * @return
	 * @throws BusinessException
	 */
	List<DocumentDTO> uploadProjectFilesForBPM(PropertiesDTO properties, List<MultipartFile> uploadMFiles)
			throws BusinessException;

	List<DocumentDTO> uploadProjectLocalFilesForBPM(PropertiesDTO properties, List<FileContentLocalDTO> uploadMFiles)
			throws BusinessException;
}
