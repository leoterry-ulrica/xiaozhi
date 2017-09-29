package com.dist.bdf.service.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONObject;
import com.dist.bdf.base.controller.BaseController;
import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.result.Result;
import com.dist.bdf.base.utils.JSONUtil;
import com.dist.bdf.base.utils.Md5CaculateUtil;
import com.dist.bdf.base.utils.StringUtil;
import com.dist.bdf.facade.service.cad.CadService;
import com.dist.bdf.facade.service.file.FileService;
import com.dist.bdf.facade.service.sga.SgaUserService;
import com.dist.bdf.common.constants.AttachmentTypeConstants;
import com.dist.bdf.common.constants.GlobalSystemParameters;
import com.dist.bdf.common.constants.ResourceConstants;
import com.dist.bdf.common.constants.ResourceConstants.ResourceType;
import com.dist.bdf.model.dto.cad.TemplateDTO;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
import com.dist.bdf.model.dto.dcm.FileContentLocalDTO;
import com.dist.bdf.model.dto.dcm.PropertiesDTO;
import com.dist.bdf.model.dto.dcm.PropertiesExDTO;
import com.dist.bdf.model.dto.file.FileInfoRequestDTO;
import com.dist.bdf.model.dto.file.FileInfoResponseDTO;
import com.dist.bdf.model.dto.sga.UserAttachmentDTO;
import com.dist.bdf.model.dto.system.CopyFileDTO;
import com.dist.bdf.model.entity.cad.TemplateEntity;
import com.dist.bdf.service.file.utils.FileOperateUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags={"API-文件上传服务模块"}, description = "FileUploadController")
@RestController
@RequestMapping(value = "/rest/sysservice")
//@CrossOrigin(origins = "*")
@Scope("prototype") // 用于多线程下载文件所用，否则单例模式下，response对象被共享
public class FileUploadController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	@Autowired
	private FileService fileService;
	@Autowired
	private CadService cadService;
	@Autowired
	private SgaUserService sgaUserService;
	
	//private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

	/**
	 * 上传文件哈希表
	 */
	private static volatile Map<String, FileInfoRequestDTO> UploadFileMap = Collections.synchronizedMap(new ConcurrentHashMap<String, FileInfoRequestDTO>());
	
	 /* public SessionMgr() {
	    	//TODO session超时的时候关闭
	        scheduler.scheduleAtFixedRate(new Runnable() {
	            @Override
	            public void run() {
	            	synchronized(this)
	            	{
	            		long now = System.currentTimeMillis();
		                sessionMap.keySet().forEach(k -> {
		                    SessionData data = sessionMap.get(k);
							WebSocketSession session = data.getSession();
							long lastKickTime = now;
							if (session!=null && session.getAttributes().get(IMConstants.lastKickTime)!=null)
							{
								lastKickTime= (long)session.getAttributes().get(IMConstants.lastKickTime);
							}
							if (session==null)
							{
								cleanDest4Session(k);
								sessionMap.remove(k);
							}
							// now < lastKickTime || 
							if ((now-lastKickTime)>1000*60*timeoutMinutes)
							{
								String userName = (String) session.getAttributes().get(IMConstants.loginName);
								 System.out.println("超时关闭连接:  "+ userName);
							 	//cleanDest4Session(k);//这里不用调。close时候会间接调用disConnectSession
							    //sessionMap.remove(k);//这里没必要， close里会删除                        	 
								 kickLegalUser(session, IMConstants.errTimeout);
								 //session.close();
							}
		                });
	            	}
	            }
	        }, 10, 10, TimeUnit.SECONDS);
	    	}*/
	
	/*@Autowired
	private CommonController commonCtl;*/
	//@Autowired
    //private UserOrgService userService; 
   //@Autowired
   //private ProjectService projectService;
    
	 @ApiOperation(value = "上传个人文档资料，需要把个人登录名和文件同时post过来。(测试使用)", notes = "uploadFile", response = Result.class)
	 @RequestMapping(value="uploadFile")
		public Result uploadFile() throws IllegalStateException, IOException {
	    	try{
	       int count = 0;
			//创建一个通用的多部分解析器
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(super.request.getSession().getServletContext());
			//判断 request 是否有文件上传,即多部分请求
			if(multipartResolver.isMultipart(super.request)){
				//转换成多部分request  
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)super.request;
				//取得request中的所有文件名
				Iterator<String> iter = multiRequest.getFileNames();
				
				while(iter.hasNext()){
					count ++;
					//记录上传过程起始时的时间，用来计算上传时间
					int pre = (int) System.currentTimeMillis();
					//取得上传文件
					MultipartFile file = multiRequest.getFile(iter.next());
					if(file != null){
						//取得当前上传文件的文件名称
						String oriFileName = file.getOriginalFilename();
						
						//如果名称不为“”,说明该文件存在，否则说明该文件不存在
						if(oriFileName.trim() !="") {
							System.out.println("<upload=====原文件名称："+oriFileName);
							String extension = oriFileName.substring(oriFileName.lastIndexOf("."));
							//重命名上传后的文件名
							String newFileName = pre+extension;
							//定义上传路径
							String localpath = super.request.getServletContext().getRealPath("/test/");
							if(!new File(localpath).exists()){
								new File(localpath).mkdirs();
							}
							String path = localpath +"/"+ newFileName;
							File localFile = new File(path);
							file.transferTo(localFile);
							System.out.println("<upload=====成功上传文件到服务器："+localFile);
							// String avatarRelativePath = "avatar/"+localFile.getName();

							// return super.successResult(avatarRelativePath);
						}
					}
				}	
			}
			return super.successResult("完成上传，共上传："+count+" 个");
			
	    	}catch(Exception ex){
	    		ex.printStackTrace(); 	
	    	}
	    	System.out.println("<upload=====上传文件失败。");
			return super.failResult("上传文件失败");
		}
	 protected PropertiesDTO getPersonalFileProperties(JSONObject jsonProperties) { 
		 
		  logger.info(jsonProperties.toString());
		 PropertiesDTO dto = new PropertiesDTO();

		 
		 dto.setUserId(jsonProperties.getString("user"));// super.request.getParameter("userId");
		 dto.setPassword(jsonProperties.getString("pwd"));
		 dto.setParentFolderId(jsonProperties.getString("parentId"));
		 dto.setRealm(jsonProperties.getString("realm"));
		 PropertiesExDTO propEx = new PropertiesExDTO();
		 propEx.setFileType(jsonProperties.getString("fileType"));
		 propEx.setBusiness(jsonProperties.getString("business"));
		 propEx.setDomain(jsonProperties.getString("domain")); // 使用用户编码
		 propEx.setOrg(jsonProperties.getString("org"));
		 propEx.setRegion(jsonProperties.getString("region"));
		 propEx.setResourceType(ResourceConstants.ResourceType.RES_PCK_PERSON);
		 propEx.setSearchable(false);
		 propEx.setTags(jsonProperties.getString("tags"));
		 propEx.setDesc(jsonProperties.getString("desc"));
		 
		 dto.setPropertiesEx(propEx);
		 
		 return dto;
		 
	 }

	private Result uploadPersonalFile(List<FileContentLocalDTO> uploadFiles, JSONObject jsonProperties) {
		
			PropertiesDTO dto = getPersonalFileProperties(jsonProperties);
			if(StringUtil.isNullOrEmpty(dto.getUserId())) throw new BusinessException("请求参数没有获取到个人信息。");
			
			@SuppressWarnings("unchecked")
			List<DocumentDTO> docs = (List<DocumentDTO>) this.fileService.uploadLocalFiles(dto.getRealm(),
					dto.getUserId(), dto.getPassword(), dto.getParentFolderId(),
					dto.getPropertiesEx(), uploadFiles);

			return super.successResult(docs);
		}

		
	 @ApiOperation(value = "上传个人文档资料，需要把个人登录名和文件同时post过来。", notes = "uploadPersonalFile", response = Result.class)
	 @RequestMapping(value="uploadFile/person", method = RequestMethod.POST)
	 public Result uploadPersonalFile(@RequestParam String properties) throws Exception {
		 
		 //String properties = super.request.getParameter("properties");
		 JSONObject jsonProperties = JSONObject.parseObject(properties);
         PropertiesDTO dto = this.getPersonalFileProperties(jsonProperties);
		 List<MultipartFile> uploadFiles = FileOperateUtil.getUploadMFiles(super.request);
	
		 @SuppressWarnings("unchecked")
			List<DocumentDTO> docs = (List<DocumentDTO>) this.fileService.uploadFiles(dto.getRealm(),
					dto.getUserId(), dto.getPassword(), dto.getParentFolderId(),
					dto.getPropertiesEx(), uploadFiles);

		return super.successResult(docs);
		 
	 }
	 /**
	  * 统一处理断点续传服务
	  * @param resourceType
	  * @return
	  * @throws IllegalStateException
	  * @throws IOException
	  */
	private Result uploadFileBreakpoint(String properties, String resourceType)
			throws IllegalStateException, IOException {

		Result result = null;
		BufferedOutputStream bufferedOutputStream = null;
		File tempFile = null;
		// 是否上传完成
		boolean isFinished = false;
		// 先获取文件的唯一id
		// String properties = super.request.getParameter("properties");
		JSONObject jsonProperties = JSONObject.parseObject(properties);
		String fileId = jsonProperties.getString("fileId");

		logger.info("文件哈希id [{}] ......", fileId);

		// 文件的完整路径
		String tempDir = super.getContextPath(GlobalSystemParameters.UPLOAD_FILE_TEMP);
		String tempSavePath = tempDir + "/" + fileId;
		logger.info("文件临时路径：[{}]", tempSavePath);

		try {
			// 创建一个通用的多部分解析器
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
					super.request.getSession().getServletContext());
			// 判断 request 是否有文件上传,即多部分请求
			if (multipartResolver.isMultipart(super.request)) {
				// 转换成多部分request
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) super.request;
				// 取得request中的所有文件名
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {

					// 断点续传，正常来说这个文件是存在服务器的某个目录下
					tempFile = new File(tempSavePath);
					// 如果存在，则往文件里继续添加内容，取得上传文件
					MultipartFile multipartFile = multiRequest.getFile(iter.next());
					if (null == multipartFile)
						continue;

					// 取得当前上传文件的文件名称
					String oriFileName = multipartFile.getOriginalFilename();

					// 如果名称不为空，说明该文件存在，否则说明该文件不存在
					if (StringUtils.isEmpty(oriFileName.trim()))
						continue;

					// in = file.getInputStream();
					long needSkipBytes = 0;
					if (tempFile.exists()) {
						// 续传
						needSkipBytes = tempFile.length();
					} else {
						// 第一次传
						tempFile.createNewFile();
					}
					System.out.println("跳过的KB为：" + String.format("%.2f", needSkipBytes / (double) 1024));

					bufferedOutputStream = new BufferedOutputStream(
							new FileOutputStream(tempFile.getAbsolutePath(), true));
					bufferedOutputStream.write(multipartFile.getBytes());
					bufferedOutputStream.flush();

					FileInfoRequestDTO fileInfoCache = UploadFileMap.get(fileId);
					if (null == fileInfoCache)
						continue;

					logger.info("缓存原大小：" + fileInfoCache.getSize());
					logger.info("本地临时文件原大小：" + tempFile.length());
					logger.info("文件总大小（KB） ：" + String.format("%.2f", fileInfoCache.getSize() / (double) 1024));
					logger.info("目前块大小（KB） ：" + String.format("%.2f", multipartFile.getSize() / (double) 1024));
					logger.info("已接收大小（KB） ：" + String.format("%.2f", tempFile.length() / (double) 1024));

					if (fileInfoCache.getSize() <= tempFile.length()) {

						logger.info("文件已上传完成，准备处理业务逻辑......");
						isFinished = true;
						// 说明文件已上传完整到临时目录，进行业务的存储
						List<FileContentLocalDTO> uploadFiles = new ArrayList<FileContentLocalDTO>();
						uploadFiles.add(
								FileOperateUtil.toFileContentLocalDTO(multipartFile, new FileInputStream(tempFile)));

						switch (resourceType) {
						case ResourceConstants.ResourceType.RES_PCK_PERSON:

							result = this.uploadPersonalFile(uploadFiles, jsonProperties);
							break;
						case ResourceConstants.ResourceType.RES_PCK_DEPARTMENT:

							result = this.uploadDepartmentFile(uploadFiles, jsonProperties);
							break;
						case ResourceConstants.ResourceType.RES_PCK_INSTITUTE:

							result = this.uploadInstituteFile(uploadFiles, jsonProperties);
							break;
						case ResourceConstants.ResourceType.RES_PCK_PROJECT:

							result = this.uploadProjectFile(uploadFiles, jsonProperties);
							break;
						case ResourceConstants.ResourceType.RES_PCK_PROJECT_BPM:

							result = this.uploadProjectFileForBPM(uploadFiles, jsonProperties);
							break;
						default:
                            // 新版本
							if (!uploadFiles.isEmpty()) {

								String realm = jsonProperties.getString("realm");
								String docId = jsonProperties.getString("docId");
								String publisher =  jsonProperties.getString("publisher");
								logger.info(">>>newVersion, realm : [{}], docId : [{}], publisher : [{}]", realm, docId, publisher);
								result = super.successResult(this.fileService.newVersion(realm, docId, publisher, FileOperateUtil.getUploadMFiles(request).get(0)));

							}
							break;
						}	
						return result;
					}
				}
			}
			// return super.successResult("完成上传。");
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(">>>上传文件失败。");
			return super.failResult("上传文件失败");
		} finally {
			  releaseTempFileResource(bufferedOutputStream,tempFile, fileId, isFinished);
		}

		logger.info(">>>文件部分上传完成");
		return super.successResult("文件部分上传完成");
	}

	/**
	 * 释放资源
	 * @param bufferedOutputStream
	 * @param tempFile
	 * @param fileId
	 */
	private void releaseTempFileResource(BufferedOutputStream bufferedOutputStream, File tempFile, String fileId, boolean isFinished) {
		try {

			if (tempFile != null && isFinished) {
				if (bufferedOutputStream != null) {
					//bufferedOutputStream.flush();
					bufferedOutputStream.close();
				}
				
				logger.info("删除临时文件 [{}]", tempFile.getAbsolutePath());
				if (tempFile.delete()) {
					logger.info("成功删除");
					UploadFileMap.remove(fileId);
				} else {
					logger.info("删除失败");
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	 
     protected PropertiesDTO getProjectFileProperties(JSONObject jsonProperties) {
		 
		 PropertiesDTO dto = new PropertiesDTO();
		 logger.info(jsonProperties.toString());
		 
		 dto.setUserId(jsonProperties.getString("user")); 
		 dto.setPassword(jsonProperties.getString("pwd"));
		 dto.setParentFolderId(jsonProperties.getString("parentId"));
		 dto.setRealm(jsonProperties.getString("realm"));
		 PropertiesExDTO propEx = new PropertiesExDTO();
		 propEx.setFileType(jsonProperties.getString("fileType"));
		 propEx.setBusiness(jsonProperties.getString("business"));
		 propEx.setDomain(jsonProperties.getString("domain"));
		 dto.setIsRoot(jsonProperties.getInteger("isRoot"));
		 Assert.hasLength(propEx.getDomain());
		 
		 propEx.setAssociateProject(jsonProperties.getString("domain"));
		 propEx.setOrg(jsonProperties.getString("org"));
		 propEx.setRegion(jsonProperties.getString("region"));
		 propEx.setResourceType(ResourceConstants.ResourceType.RES_PCK_PROJECT);
		 propEx.setSearchable(true);
		 propEx.setTags(jsonProperties.getString("tags"));
		 propEx.setDesc(jsonProperties.getString("desc"));
		 dto.setPropertiesEx(propEx);
		 
		 return dto;
		 
	 }
     protected PropertiesDTO getProjectFilePropertiesForBPM(JSONObject jsonProperties) {
		 
		 PropertiesDTO dto = new PropertiesDTO();
		 logger.info(jsonProperties.toString());
		 
		 dto.setUserId(jsonProperties.getString("user")); 
		 dto.setParentFolderId(jsonProperties.getString("parentId"));
		 dto.setRealm(jsonProperties.getString("realm"));
		 PropertiesExDTO propEx = new PropertiesExDTO();
		 propEx.setFileType(jsonProperties.getString("fileType"));
		 propEx.setBusiness(jsonProperties.getString("business"));
		 propEx.setDomain(jsonProperties.getString("domain"));
		 
		 Assert.hasLength(propEx.getDomain());
		 
		 propEx.setAssociateProject(jsonProperties.getString("domain"));
		 propEx.setOrg(jsonProperties.getString("org"));
		 propEx.setRegion(jsonProperties.getString("region"));
		 propEx.setResourceType(ResourceConstants.ResourceType.RES_PCK_PROJECT);
		 propEx.setSearchable(true);
		 propEx.setTags(jsonProperties.getString("tags"));
		 propEx.setDesc(jsonProperties.getString("desc"));
		 dto.setPropertiesEx(propEx);
		 
		 return dto;
		 
	 }
 
	 @ApiOperation(value = "上传项目文档资料，需要把个人登录名和文件同时post过来。", notes = "uploadProjectFile", response = Result.class)
	 @RequestMapping(value="uploadFile/project", method = RequestMethod.POST)
	 public Result uploadProjectFile(@RequestParam String properties) throws Exception {
		 
		 //String properties = super.request.getParameter("properties");
		 JSONObject jsonProperties = JSONObject.parseObject(properties);

		 PropertiesDTO dto = this.getProjectFileProperties(jsonProperties);
		 List<MultipartFile> uploadFiles = FileOperateUtil.getUploadMFiles(super.request);
		 
		 @SuppressWarnings("unchecked")
			List<DocumentDTO> docs = (List<DocumentDTO>) this.fileService.uploadFiles(dto.getRealm(),
					dto.getUserId(), dto.getPassword(), dto.getParentFolderId(),
					dto.getPropertiesEx(), uploadFiles);

		return super.successResult(docs);
	 }
	 
	 private Result uploadProjectFile(List<FileContentLocalDTO> uploadFiles, JSONObject jsonProperties) {
			
			PropertiesDTO dto = this.getProjectFileProperties(jsonProperties);
			if(StringUtil.isNullOrEmpty(dto.getUserId())) throw new BusinessException("请求参数没有获取到个人信息。");
			
			@SuppressWarnings("unchecked")
			List<DocumentDTO> docs = (List<DocumentDTO>) this.fileService.uploadLocalFiles(dto.getRealm(),
					dto.getUserId(), dto.getPassword(), dto.getParentFolderId(),
					dto.getPropertiesEx(), uploadFiles);

			return super.successResult(docs);
	}
	 private Result uploadProjectFileForBPM(List<FileContentLocalDTO> uploadFiles, JSONObject jsonProperties) {
			
			PropertiesDTO dto = this.getProjectFilePropertiesForBPM(jsonProperties);
			if(StringUtil.isNullOrEmpty(dto.getUserId())) throw new BusinessException("请求参数没有获取到个人信息。");
			
			List<DocumentDTO> docs = (List<DocumentDTO>) this.fileService.uploadProjectLocalFilesForBPM(dto, uploadFiles);

			return super.successResult(docs);
		} 
	 
	 protected PropertiesDTO getDepartmentFileProperties(JSONObject jsonProperties) {
		 
		 PropertiesDTO dto = new PropertiesDTO();
		 logger.info(jsonProperties.toString());
		 
		 dto.setUserId(jsonProperties.getString("user"));
		 dto.setPassword(jsonProperties.getString("pwd"));
		 dto.setParentFolderId(jsonProperties.getString("parentId"));
		 dto.setRealm(jsonProperties.getString("realm"));
		 PropertiesExDTO propEx = new PropertiesExDTO();
		 propEx.setFileType(jsonProperties.getString("fileType"));
		 propEx.setBusiness(jsonProperties.getString("business"));
		 propEx.setDomain(jsonProperties.getString("domain"));
		 propEx.setOrg(jsonProperties.getString("org"));
		 propEx.setRegion(jsonProperties.getString("region"));
		 propEx.setResourceType(ResourceConstants.ResourceType.RES_PCK_DEPARTMENT);
		 propEx.setSearchable(true);
		 propEx.setTags(jsonProperties.getString("tags"));
		 propEx.setDesc(jsonProperties.getString("desc"));
		 dto.setPropertiesEx(propEx);
		 
		 return dto;
		 
	 }
	 
	private Result uploadDepartmentFile(List<FileContentLocalDTO> uploadFiles, JSONObject jsonProperties) {
			
			PropertiesDTO dto = this.getDepartmentFileProperties(jsonProperties);
			if(StringUtil.isNullOrEmpty(dto.getUserId())) throw new BusinessException("请求参数没有获取到个人信息。");
			
			@SuppressWarnings("unchecked")
			List<DocumentDTO> docs = (List<DocumentDTO>) this.fileService.uploadLocalFiles(dto.getRealm(),
					dto.getUserId(), dto.getPassword(), dto.getParentFolderId(),
					dto.getPropertiesEx(), uploadFiles);

			return super.successResult(docs);
	}

	 @ApiOperation(value = "上传所级文档资料，需要把个人登录名和文件同时post过来。", notes = "uploadDepartmentFile", response = Result.class)
	 @RequestMapping(value="uploadFile/department", method = RequestMethod.POST)
	 public Result uploadDepartmentFile() throws Exception {
		 
		 String properties = super.request.getParameter("properties");
		 JSONObject jsonProperties = JSONObject.parseObject(properties);
		 
		 PropertiesDTO dto = this.getDepartmentFileProperties(jsonProperties);
		 List<MultipartFile> uploadFiles = FileOperateUtil.getUploadMFiles(super.request);
		 
		 @SuppressWarnings("unchecked")
			List<DocumentDTO> docs = (List<DocumentDTO>) this.fileService.uploadFiles(
					dto.getUserId(), dto.getPassword(), dto.getParentFolderId(),
					dto.getPropertiesEx(), uploadFiles);

		return super.successResult(docs);

	 }
	 
     protected PropertiesDTO getInstituteFileProperties(JSONObject jsonProperties) {
		 
		 PropertiesDTO dto = new PropertiesDTO();
		 //logger.info(jsonProperties.toString());
		 
		 dto.setUserId(jsonProperties.getString("user"));
		 dto.setPassword(jsonProperties.getString("pwd"));
		 dto.setParentFolderId(jsonProperties.getString("parentId"));
		 dto.setRealm(jsonProperties.getString("realm"));
		 PropertiesExDTO propEx = new PropertiesExDTO();
		 propEx.setFileType(jsonProperties.getString("fileType"));
		 propEx.setBusiness(jsonProperties.getString("business"));
		 propEx.setDomain(jsonProperties.getString("domain"));
		 propEx.setOrg(jsonProperties.getString("org"));
		 propEx.setRegion(jsonProperties.getString("region"));
		 propEx.setResourceType(ResourceConstants.ResourceType.RES_PCK_INSTITUTE);
		 propEx.setSearchable(true);
		 propEx.setTags(jsonProperties.getString("tags"));
		 propEx.setDesc(jsonProperties.getString("desc"));
		 dto.setPropertiesEx(propEx);
		 
		 return dto;
	 }
     
	 @ApiOperation(value = "上传院级文档资料，需要把个人登录名和文件同时post过来。", notes = "uploadInstituteFile", response = Result.class)
	 @RequestMapping(value="uploadFile/institute", method = RequestMethod.POST)
	 public Result uploadInstituteFile() throws Exception {
		 
		 String properties = super.request.getParameter("properties");
		 JSONObject jsonProperties = JSONObject.parseObject(properties);
		 PropertiesDTO dto = this.getInstituteFileProperties(jsonProperties);
		 List<MultipartFile> uploadFiles = FileOperateUtil.getUploadMFiles(super.request);
		 
		 @SuppressWarnings("unchecked")
			List<DocumentDTO> docs = (List<DocumentDTO>) this.fileService.uploadFiles(dto.getRealm(),
					dto.getUserId(), dto.getPassword(), dto.getParentFolderId(),
					dto.getPropertiesEx(), uploadFiles);

		return super.successResult(docs);
		 
	 }
	 
	 private Result uploadInstituteFile(List<FileContentLocalDTO> uploadFiles, JSONObject jsonProperties) {
			
			PropertiesDTO dto = this.getInstituteFileProperties(jsonProperties);
			if(StringUtil.isNullOrEmpty(dto.getUserId())) throw new BusinessException("请求参数没有获取到个人信息。");
			
			@SuppressWarnings("unchecked")
			List<DocumentDTO> docs = (List<DocumentDTO>) this.fileService.uploadLocalFiles(dto.getRealm(),
					dto.getUserId(), dto.getPassword(), dto.getParentFolderId(),
					dto.getPropertiesEx(), uploadFiles);

			return super.successResult(docs);
	}
	 
	/**
	 * 最好能够把请求数据放在RequestBody中，以免特殊字符造成请求失败
	 * @param metadataJson
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "检测文件是否已上传过，或者上传了一部分，并返回文件md5值。", response = Result.class , notes = "checkFileMetadata")
	@RequestMapping(value = "/file/info.check/{metadataJson}", method = {RequestMethod.GET, RequestMethod.POST})
	public Result checkFileMetadata(@PathVariable String metadataJson) throws Exception {

		logger.info("文件元数据：[{}]", metadataJson);
		FileInfoRequestDTO info = (FileInfoRequestDTO) JSONUtil.toObject(metadataJson, FileInfoRequestDTO.class);
		String fileId = Md5CaculateUtil.md5Hex(info.toString());
		if(!UploadFileMap.containsKey(fileId)){
			UploadFileMap.put(fileId, info);
		}
		String tempDir = super.getContextPath(GlobalSystemParameters.UPLOAD_FILE_TEMP);
		String tempSavePath = tempDir + "/" + fileId;
		
		File file = new File(tempSavePath);
		if(file.exists()){
			logger.info("文件哈希id [{}]，元数据 [{}] 已存在服务器，大小 [{}]", fileId, info.toString(), file.length());
			if(file.length() > info.getSize()){
				logger.warn("服务端临时文件大小跟请求大小不匹配，删除临时文件.....");
				file.delete();
				
				return super.successResult(new FileInfoResponseDTO(fileId, 0L, Boolean.TRUE));
			}
			return super.successResult(new FileInfoResponseDTO(fileId, file.length(), Boolean.TRUE));
		}
		
		return super.successResult(new FileInfoResponseDTO(fileId, file.length(), Boolean.FALSE));
		
	}
	@ApiOperation(value = "个人文件断点续传。", response = Result.class, notes = "uploadPersonalBreakpoint")
	@RequestMapping(value = "uploadFile/person.bkp", method = { RequestMethod.POST })
	public Result uploadPersonalBreakpoint(@RequestParam String properties) throws IllegalStateException, IOException {

		return this.uploadFileBreakpoint(properties, ResourceConstants.ResourceType.RES_PCK_PERSON);
	}

	@ApiOperation(value = "所级文件断点续传。", response = Result.class, notes = "uploadDepartmentBreakpoint")
	@RequestMapping(value = "uploadFile/department.bkp", method = { RequestMethod.POST })
	public Result uploadDepartmentBreakpoint(@RequestParam String properties) throws IllegalStateException, IOException {

		return this.uploadFileBreakpoint(properties, ResourceConstants.ResourceType.RES_PCK_DEPARTMENT);
	}
	
	@ApiOperation(value = "院级文件断点续传。", response = Result.class, notes = "uploadInstituteBreakpoint")
	@RequestMapping(value = "uploadFile/institute.bkp", method = { RequestMethod.POST })
	public Result uploadInstituteBreakpoint(@RequestParam String properties) throws IllegalStateException, IOException {

		return this.uploadFileBreakpoint(properties, ResourceConstants.ResourceType.RES_PCK_INSTITUTE);
	}
	
	@ApiOperation(value = "项目文件断点续传。", response = Result.class, notes = "uploadProjectBreakpoint")
	@RequestMapping(value = "uploadFile/project.bkp", method = { RequestMethod.POST })
	public Result uploadProjectBreakpoint(@RequestParam String properties) throws IllegalStateException, IOException {

		return this.uploadFileBreakpoint(properties, ResourceConstants.ResourceType.RES_PCK_PROJECT);
	}
	
	@ApiOperation(value = "上传新版本，断点续传，提供给PC端使用。", response = Result.class, notes = "uploadDocVersionBreakpoint")
	@RequestMapping(value = "/doc/version/new/", method = { RequestMethod.POST })
	public Result uploadDocVersionBreakpoint(@RequestParam String properties) throws IllegalStateException, IOException {

		return this.uploadFileBreakpoint(properties, "");
	}
	@ApiOperation(value = "上传新版本，不支持断点续传，提供给WEB端使用。", response = Result.class, notes = "uploadDocVersion")
	@RequestMapping(value = "/v1/doc/version/new/", method = { RequestMethod.POST })
	public Result uploadDocVersion(@RequestParam String properties) throws IllegalStateException, IOException {
		
		JSONObject jsonProperties = JSONObject.parseObject(properties);
		String realm = jsonProperties.getString("realm");
		String docId = jsonProperties.getString("docId");
		logger.info(">>>newVersion, realm : [{}], docId : [{}]", realm, docId);
		return super.successResult(this.fileService.newVersion(realm, docId, FileOperateUtil.getUploadMFiles(request).get(0)));
	}
	@ApiOperation(value = "上传新版本，不支持断点续传，提供给WEB端使用，添加发布者记录。", response = Result.class, notes = "uploadDocVersionV2")
	@RequestMapping(value = "/v2/doc/version/new/", method = { RequestMethod.POST })
	public Result uploadDocVersionV2(@RequestParam String properties) throws IllegalStateException, IOException {
		
		JSONObject jsonProperties = JSONObject.parseObject(properties);
		String realm = jsonProperties.getString("realm");
		String docId = jsonProperties.getString("docId");
		String publisher =  jsonProperties.getString("publisher");
		logger.info(">>>newVersion, realm : [{}], docId : [{}], publisher : [{}]", realm, docId, publisher);
		return super.successResult(this.fileService.newVersion(realm, docId, publisher, FileOperateUtil.getUploadMFiles(request).get(0)));
	}
	/**
	 示例：
	 {
    "associateProject":"XZ_CASETYPE_HZXM_000000140001",
    "business":"",
    "desc":"",
    "domain":"XZ_CASETYPE_HZXM_000000140001",
    "fileType":"XZ_FILE_ZL",
    "org":"",
    "parentId":"{465A3C62-3922-4E28-90B4-79A398479D5C}",
    "realm":"thupdi",
    "region":"",
    "resourceType":"Res_Pck_Project",
    "searchable":true,
    "tags":"",
    "user":"A16540D4-6F70-48F5-8ADF-4C5FA91C1A13"
   }

	 * @param properties
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@ApiOperation(value = "通过外部上传项目文件。", response = Result.class, notes = "uploadProject")
	@RequestMapping(value = "/uploadFile/project/external", method = { RequestMethod.POST })
	public Result uploadProjectByExternal(
			@RequestParam String properties) throws IllegalStateException, IOException {

		PropertiesDTO dto = this.getProjectFileProperties(JSONObject.parseObject(properties));
		return super.successResult(this.fileService.uploadProjectFilesExternal(dto, FileOperateUtil.getUploadMFiles(request)));
	}
	
	@ApiOperation(value = "提供给BPM，上传项目文件。", response = Result.class, notes = "uploadProjectForBPM")
	@RequestMapping(value = "/uploadFile/project/bpm", method = { RequestMethod.POST })
	public Result uploadProjectForBPM(
			@RequestParam String properties) throws IllegalStateException, IOException {

		PropertiesDTO dto = this.getProjectFileProperties(JSONObject.parseObject(properties));
		return super.successResult(this.fileService.uploadProjectFilesForBPM(dto, FileOperateUtil.getUploadMFiles(request)));
	}
	@ApiOperation(value = "提供给BPM，断点上传项目文件。", response = Result.class, notes = "uploadProjectForBpmBreakpoint")
	@RequestMapping(value = "/uploadFile/project.bpm.bkp", method = { RequestMethod.POST })
	public Result uploadProjectForBpmBreakpoint(
			@RequestParam String properties) throws IllegalStateException, IOException {

		return this.uploadFileBreakpoint(properties, ResourceConstants.ResourceType.RES_PCK_PROJECT_BPM);
	}
	
	@ApiOperation(value = "上传CAD模板文件和元数据", response = Result.class, notes = "uploadCADTemplate")
	@RequestMapping(value = "/uploadFile/cadTemplate", method = { RequestMethod.POST })
	public Result uploadCADTemplate(
			@RequestParam String properties) {

		TemplateDTO templateDTO = JSONObject.parseObject(properties,TemplateDTO.class);
		// 属于院包
		templateDTO.setResourceType(ResourceType.RES_PCK_INSTITUTE);
		templateDTO.setSearchable(true);
		
		logger.info(">>>上传模板文件......");
		List<MultipartFile> uploadMFiles = FileOperateUtil.getUploadMFiles(request);
		DocumentDTO docDTO = null;
		if(uploadMFiles != null && !uploadMFiles.isEmpty()) {
			docDTO = this.fileService.uploadCADTemplate(templateDTO , uploadMFiles);
		}

		logger.info(">>>上传模板元数据：{}", properties);
		templateDTO.setDocId(null == docDTO? "" : docDTO.getId());
		templateDTO.setDocVId(null == docDTO? "" : docDTO.getVersionSeriesId());
		TemplateEntity templateEntity = this.cadService.saveOrUpdateTemplateMetadata(templateDTO);
		templateDTO.setId(templateEntity.getId());
		
		return super.successResult(templateEntity);
	}
	@ApiOperation(value = "上传资讯文件，存储到2C的资讯项目包。JSON格式信息，包括realm、userId，扩展属性的fileType，可能值：material(资料)和news(新闻)；domain；resourceType", response = Result.class, notes = "uploadPublicFile")
	@RequestMapping(value = "/uploadFile/public", method = { RequestMethod.POST })
	public Result uploadPublicFile(
			@ApiParam(example = "{\"realm\":\"dist\", \"userId\":\"用户编码\", \"propertiesEx\":{\"fileType\":\"material | news\", \"domain\":\"xxx\", \"resourceType\":\"Res_Pck_Institute\"}}")
			@RequestParam String properties) throws IllegalStateException, IOException {

		PropertiesDTO dto = JSONObject.parseObject(properties, PropertiesDTO.class);
		if(StringUtils.isEmpty(dto.getPropertiesEx().getResourceType())) {
			dto.getPropertiesEx().setResourceType(ResourceConstants.ResourceType.RES_PCK_INSTITUTE);
		}
		dto.getPropertiesEx().setSearchable(true);
		return super.successResult(this.fileService.uploadPublicFiles(dto, FileOperateUtil.getUploadMFiles(request)));
	}
	
	@ApiOperation(value = "从企业中拷贝文件到资讯存储库。", response = Result.class, notes = "copyFile2PublicFromBusiness")
	@RequestMapping(value = "/uploadFile/transfer2public", method = { RequestMethod.POST })
	public Result copyFile2PublicFromBusiness(
			@Valid
			@ApiParam(value = "需要拷贝的文件信息")
			@RequestBody CopyFileDTO dto, BindingResult result) throws IllegalStateException, IOException {
		
		if(result.hasErrors()) {
			return super.errorResult("参数验证失败，详情："+result.toString());
		}
		return super.successResult(this.fileService.copyFile2PublicFromBusiness(dto));
	}
	
	@ApiOperation(value = "上传个人简历。", response = Result.class, notes = "uploadPersonalResume")
	@RequestMapping(value = "/uploadFile/resume", method = { RequestMethod.POST })
	public Result uploadPersonalResume(
			@ApiParam(value = "属性中的key：realm如果是空，则引用默认公共库；其它key包括：userCode[用户编码]")
			@RequestParam String properties) throws IllegalStateException, IOException {

		UserAttachmentDTO dto = JSONObject.parseObject(properties, UserAttachmentDTO.class);
		List<DocumentDTO> docs = this.fileService.uploadPersonalResume(dto, FileOperateUtil.getUploadMFiles(request));
		if(null == docs || docs.isEmpty()) {
			return super.failResult("个人简历文件上传失败");
		}
		List<UserAttachmentDTO> attachments = new ArrayList<UserAttachmentDTO>();
		for(DocumentDTO doc : docs) {
			attachments.add(new UserAttachmentDTO(dto.getUserCode(), doc.getVersionSeriesId(), doc.getId(), doc.getName(), doc.getDateCreated(), doc.getMimeType(), AttachmentTypeConstants.ATTACHMENT_TYPE_RESUME));
		}
	
		return super.successResult(this.sgaUserService.saveOrUpdateUserAttachementMetadata(attachments));
	}
	
	
}
