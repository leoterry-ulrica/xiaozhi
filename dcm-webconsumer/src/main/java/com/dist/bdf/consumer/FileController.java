package com.dist.bdf.consumer;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.dist.bdf.base.utils.Base64Utils;
import com.dist.bdf.base.utils.ImageUtils;
import com.dist.bdf.base.utils.JSONUtil;
import com.dist.bdf.base.utils.Md5CaculateUtil;
import com.dist.bdf.base.utils.StringUtil;
import com.dist.bdf.model.dto.dcm.PropertiesDTO;
import com.dist.bdf.model.dto.dcm.PropertiesExDTO;
import com.dist.bdf.model.dto.file.FileInfoRequestDTO;
import com.dist.bdf.model.dto.file.FileInfoResponseDTO;
import com.dist.bdf.model.entity.system.DcmUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
// import net.sf.json.JSONObject;
import com.dist.bdf.facade.service.UserOrgService;
import com.dist.bdf.common.constants.GlobalSystemParameters;
import com.dist.bdf.common.constants.ResourceConstants;

@Api(tags = { "API-文件服务模块" }, description = "FileController")
@RestController
@RequestMapping(value = "/rest/sysservice")
//@CrossOrigin(origins = "*")
public class FileController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(FileController.class);

	/**
	 * 上传文件哈希表
	 */
	private static volatile Map<String, FileInfoRequestDTO> UploadFileMap = Collections
			.synchronizedMap(new ConcurrentHashMap<String, FileInfoRequestDTO>());

	@Autowired
	private CommonController commonCtl;
	@Autowired
	private UserOrgService userService;

	// <=====上传用户头像begin
	@Deprecated
	@RequestMapping(value = "uploadAvatar", method = {RequestMethod.POST})
	public Result uploadAvatar() throws IllegalStateException, IOException {
		try {
			// 获取用户的唯一标识：登录名
			String loginName = super.request.getParameter("loginName");
			System.out.println("<upload=====传入的登录名：" + loginName);
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
					// 记录上传过程起始时的时间，用来计算上传时间
					int pre = (int) System.currentTimeMillis();
					// 取得上传文件
					MultipartFile file = multiRequest.getFile(iter.next());
					if (file != null) {
						// 取得当前上传文件的文件名称
						String oriFileName = file.getOriginalFilename();

						// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
						if (oriFileName.trim() != "") {
							System.out.println("<upload=====原文件名称：" + oriFileName);
							String extension = oriFileName.substring(oriFileName.lastIndexOf("."));
							// 重命名上传后的文件名
							String newFileName = pre + extension;
							// 定义上传路径
							String localpath = super.getContextPath(GlobalSystemParameters.UPLOAD_AVATAR);
						
							String path = localpath + "/" + newFileName;
							File localFile = new File(path);
							file.transferTo(localFile);
							System.out.println("<upload=====成功上传文件到服务器：" + localFile);
							String avatarRelativePath = GlobalSystemParameters.UPLOAD_AVATAR + "/" + localFile.getName();

							String baseURL = super.request.getScheme() + "://" + super.request.getServerName() + ":"
									+ super.request.getServerPort() + super.request.getContextPath();
							String avatarURL = this.userService.uploadUserAvatar(
									super.request.getServletContext().getRealPath("/"), baseURL, loginName,
									avatarRelativePath);
							System.out.println("<upload=====avatar url：" + avatarURL);
							return super.successResult(avatarURL);
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("<upload=====上传文件失败。");
		return super.failResult("上传文件失败");
	}

	/**
	 * 上传用户头像
	 * @param userId
	 * @param originX
	 * @param originY
	 * @param width
	 * @param height
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@Deprecated
	@ApiOperation(value = "/user/avatar.set", notes = "设置用户头像")
	@RequestMapping(value = "/user/avatar.set", method = { RequestMethod.POST, RequestMethod.PUT })
	public Result uploadUserAvatar(@RequestParam String userId, @RequestParam String originX,
			@RequestParam String originY, @RequestParam String width, @RequestParam String height) {
		try {
			// 获取用户的唯一标识：用户id
			// String userId = super.request.getParameter("userId");
			logger.info(">>>userId:[{}], originX:[{}], originY:[{}], width:[{}], height:[{}] ", userId, originX,
					originY, width, height);

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
					// 记录上传过程起始时的时间，用来计算上传时间
					int pre = (int) System.currentTimeMillis();
					// 取得上传文件
					MultipartFile file = multiRequest.getFile(iter.next());
					if (file != null) {
						// 取得当前上传文件的文件名称
						String oriFileName = file.getOriginalFilename();

						// 如果名称不为"",说明该文件存在，否则说明该文件不存在
						if (oriFileName.trim() != "") {

							System.out.println(">>>原文件名称：" + oriFileName);
							String extension = oriFileName.substring(oriFileName.lastIndexOf(".")).toLowerCase();
							// 重命名上传后的文件名
							String newFileName = pre + extension;
							String temp = super.getContextPath(GlobalSystemParameters.UPLOAD_FILE_TEMP);
						
							String path = temp + File.separatorChar + newFileName;
							File tempLocalFile = new File(path);
							file.transferTo(tempLocalFile);

							logger.info(">>>成功上传文件到服务器临时目录：" + tempLocalFile);
							// 定义正式路径
							String localpath = super.request.getServletContext()
									.getRealPath(GlobalSystemParameters.UPLOAD_AVATAR) + File.separatorChar + newFileName;
							logger.info(">>>准备裁剪图片到：" + localpath);

							ImageUtils.cutImage(tempLocalFile.getAbsolutePath(), localpath, Float.valueOf(originX).intValue(),
									Float.valueOf(originY).intValue(), Float.valueOf(width).intValue(), Float.valueOf(height).intValue());

							String avatarRelativePath = GlobalSystemParameters.UPLOAD_AVATAR + File.separatorChar
									+ tempLocalFile.getName();

							logger.info(">>>删除临时文件......");
							tempLocalFile.delete();
							// 删除旧的头像
							DcmUser findUser = this.userService.getUserByIdInCache(Long.parseLong(userId));
							if (null == findUser) {
								throw new BusinessException(String.format("id为[%s]的用户不存在", userId));
							}
							String contextPath = super.request.getServletContext().getRealPath(File.separatorChar + "");

							if (!StringUtil.isNullOrEmpty(findUser.getAvatar())
									&& -1 == findUser.getAvatar().toLowerCase().indexOf("default")) {
								File preAvatarPath = new File(contextPath + File.separatorChar + findUser.getAvatar());
								if (preAvatarPath.exists()) {
									// 删除原来的头像
									boolean result = preAvatarPath.delete();
									if (!result) {
										logger.info(">>>删除用户老头像文件失败：[{}]", preAvatarPath);
									}
								}
							}

							String baseURL = super.request.getScheme() + "://" + super.request.getServerName() + ":"
									+ super.request.getServerPort() + super.request.getContextPath();
							String avatarURL = this.userService.updateUserAvatar(findUser, baseURL, avatarRelativePath);
							System.out.println(">>>avatar url：" + avatarURL);
							return super.successResult(avatarURL);
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		logger.info(">>>上传文件失败......");
		return super.failResult("上传文件失败");
	}
	
	
	@ApiOperation(value = "设置用户头像", notes = "uploadUserAvatarEx")
	@RequestMapping(value = "/user/avatar", method = { RequestMethod.POST, RequestMethod.PUT })
	public Result uploadUserAvatarEx(
			@ApiParam( value = "json数据，{\"userId\":1267,\"type\":\"image/jpeg\",\"suffix\":\"jpg\",\"content\":\"base64值\"}")
			@RequestBody String imgInfo) {
		try {
			JSONObject jsonObj = JSONObject.parseObject(imgInfo);
			Long userId = jsonObj.getLong("userId");
			String type = jsonObj.getString("type");
			String suffix = jsonObj.getString("suffix");
			
			logger.info(">>>userId:[{}], mime-type:[{}], suffix:[{}] ", userId, type, suffix);

			String newFileName = System.currentTimeMillis() + (suffix.startsWith(".")? suffix : "."+suffix);
			String localpath =super.getContextPath(GlobalSystemParameters.UPLOAD_AVATAR) + "/" + newFileName;
			
			Base64Utils.decodeToFile(localpath, jsonObj.getString("content"));
			
			if(!new File(localpath).exists()) throw new BusinessException("上传文件失败");
			
			String avatarRelativePath = GlobalSystemParameters.UPLOAD_AVATAR + "/" + newFileName;
			
			DcmUser findUser = this.userService.getUserByIdInCache(userId);
			if (null == findUser) {
				throw new BusinessException(String.format("id为[%s]的用户不存在", userId));
			}

			if (!StringUtil.isNullOrEmpty(findUser.getAvatar())
					&& -1 == findUser.getAvatar().toLowerCase().indexOf("default")) {
				File preAvatarPath = new File(super.getContextPath() + "/" + findUser.getAvatar());
				if (preAvatarPath.exists()) {
					// 删除原来的头像
					boolean result = preAvatarPath.delete();
					if (!result) {
						logger.info(">>>删除用户老头像文件失败：[{}]", preAvatarPath);
					}
				}
			}

			String avatarURL = this.userService.updateUserAvatar(findUser, super.getBaseURL(), avatarRelativePath);
			System.out.println(">>>avatar url：" + avatarURL);
			return super.successResult(avatarURL);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		logger.info(">>>上传文件失败......");
		return super.failResult("上传文件失败");
	}
	
	@ApiOperation(value = "查看office各类文件", notes = "viewOffice")
	@RequestMapping(value = "viewOfficeOld/{extension}/{docId}", method = RequestMethod.GET) // 对传入进来的{}进行正则表达式过滤
	public void viewOffice(@PathVariable String extension, @PathVariable String docId) throws IOException {

		Result viewer = this.commonCtl.getViewerUrl(extension);
		String wopiClient = viewer.getData().toString();// OfficeOnlineViewer.getViewer(extension);//"http://192.168.2.232/wv/wordviewerframe.aspx?WOPISrc=";

		String baseURL = super.request.getScheme() + "://" + super.request.getServerName() + ":"
				+ super.request.getServerPort() + super.request.getContextPath();

		String officeURL = baseURL + "/wopi/files/" + docId.replace("{", "").replace("}", "") + "/";
		// 重定向
		super.response.sendRedirect(wopiClient + officeURL);

	}

	@ApiOperation(value = "查看office各类文件，扩展接口，支持是否全屏播放", notes = "viewOfficeEx")
	@RequestMapping(value = "viewOffice/{extension}/{docId}/{fullPlay}", method = RequestMethod.GET) // 对传入进来的{}进行正则表达式过滤
	public void viewOfficeEx(@ApiParam(value = "不带点号. 的扩展名") @PathVariable String extension,
			@ApiParam(value = "文档在CE中的唯一标识") @PathVariable String docId,
			@ApiParam(value = "0：非全屏；1：全屏") @PathVariable int fullPlay) throws IOException {

		Result viewer = this.commonCtl.getPropertyByKey("dist.viewer.office.url");
		System.out.println(viewer.getData());
		System.out.println(extension);
		System.out.println(docId);
		System.out.println(fullPlay);

		super.response.sendRedirect(viewer.getData() + "/" + extension + "/" + docId + "/" + fullPlay);

		// super.response.sendRedirect("http://192.168.2.208:8081/bdp-provider-file/rest/sysservice/viewOffice/"+extension+"/"+docId+"/"+fullPlay);
		/*
		 * Result viewer = this.commonCtl.getViewerUrl(extension); String
		 * wopiClient =
		 * viewer.getData().toString();//OfficeOnlineViewer.getViewer(extension)
		 * ;//"http://192.168.2.232/wv/wordviewerframe.aspx?WOPISrc=";
		 * StringBuilder buf = new StringBuilder();
		 * buf.append(super.request.getScheme()); buf.append("://");
		 * buf.append(super.request.getServerName()); buf.append(":");
		 * buf.append(super.request.getServerPort());
		 * buf.append(super.request.getContextPath());
		 * buf.append("/wopi/files/"); buf.append(docId.replace("{",
		 * "").replace("}", "")+"/"); if(1 == fullPlay) {
		 * buf.append("&PowerPointView=SlideShowView"); //buf.append(fullPlay);
		 * }
		 * 
		 * // 重定向 super.response.sendRedirect(wopiClient+buf.toString());
		 */

	}

	@ApiOperation(value = "查看office各类文件，扩展接口，支持是否全屏播放", notes = "viewOfficeEx")
	@RequestMapping(value = "viewOffice/{realm}/{extension}/{docId}/{fullPlay}", method = RequestMethod.GET) // 对传入进来的{}进行正则表达式过滤
	public void viewOfficeEx(@ApiParam(value = "域") @PathVariable String realm,
			@ApiParam(value = "不带点号. 的扩展名") @PathVariable String extension,
			@ApiParam(value = "文档在CE中的唯一标识") @PathVariable String docId,
			@ApiParam(value = "0：非全屏；1：全屏") @PathVariable int fullPlay) throws IOException {

		Result viewer = this.commonCtl.getPropertyByKey("dist.viewer.office.url");
		System.out.println(viewer.getData());
		System.out.println(extension);
		System.out.println(docId);
		System.out.println(fullPlay);

		super.response.sendRedirect(viewer.getData() + "/" + realm + "/" + extension + "/" + docId + "/" + fullPlay);

		// super.response.sendRedirect("http://192.168.2.208:8081/bdp-provider-file/rest/sysservice/viewOffice/"+extension+"/"+docId+"/"+fullPlay);
		/*
		 * Result viewer = this.commonCtl.getViewerUrl(extension); String
		 * wopiClient =
		 * viewer.getData().toString();//OfficeOnlineViewer.getViewer(extension)
		 * ;//"http://192.168.2.232/wv/wordviewerframe.aspx?WOPISrc=";
		 * StringBuilder buf = new StringBuilder();
		 * buf.append(super.request.getScheme()); buf.append("://");
		 * buf.append(super.request.getServerName()); buf.append(":");
		 * buf.append(super.request.getServerPort());
		 * buf.append(super.request.getContextPath());
		 * buf.append("/wopi/files/"); buf.append(docId.replace("{",
		 * "").replace("}", "")+"/"); if(1 == fullPlay) {
		 * buf.append("&PowerPointView=SlideShowView"); //buf.append(fullPlay);
		 * }
		 * 
		 * // 重定向 super.response.sendRedirect(wopiClient+buf.toString());
		 */

	}

	// 测试
	@RequestMapping(value = "uploadFile", method = {RequestMethod.POST})
	public Result uploadFile() throws IllegalStateException, IOException {
		try {
			int count = 0;
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
					count++;
					// 记录上传过程起始时的时间，用来计算上传时间
					int pre = (int) System.currentTimeMillis();
					// 取得上传文件
					MultipartFile file = multiRequest.getFile(iter.next());
					if (file != null) {
						// 取得当前上传文件的文件名称
						String oriFileName = file.getOriginalFilename();

						// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
						if (oriFileName.trim() != "") {
							System.out.println(">>>原文件名称：" + oriFileName);
							String extension = oriFileName.substring(oriFileName.lastIndexOf("."));
							// 重命名上传后的文件名
							String newFileName = pre + extension;
							// 定义上传路径
							String localpath = super.request.getServletContext().getRealPath("/test/");
							if (!new File(localpath).exists()) {
								new File(localpath).mkdirs();
							}
							String path = localpath + "/" + newFileName;
							File localFile = new File(path);
							file.transferTo(localFile);
							System.out.println(">>>成功上传文件到服务器：" + localFile);
		
						}
					}
				}
			}
			return super.successResult("完成上传，共上传：" + count + " 个");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println(">>>上传文件失败。");
		return super.failResult("上传文件失败");
	}

	protected PropertiesDTO getPersonalFileProperties(JSONObject jsonProperties) {

		logger.info(jsonProperties.toString());
		PropertiesDTO dto = new PropertiesDTO();

		dto.setUserId(jsonProperties.getString("user"));// super.request.getParameter("userId");
		dto.setPassword(jsonProperties.getString("pwd"));
		dto.setParentFolderId(jsonProperties.getString("parentId"));
		PropertiesExDTO propEx = new PropertiesExDTO();
		propEx.setFileType(jsonProperties.getString("fileType"));
		propEx.setBusiness(jsonProperties.getString("business"));
		propEx.setDomain(dto.getUserId());
		propEx.setOrg(jsonProperties.getString("org"));
		propEx.setRegion(jsonProperties.getString("region"));
		propEx.setResourceType(ResourceConstants.ResourceType.RES_PCK_PERSON);
		propEx.setSearchable(false);
		propEx.setTags(jsonProperties.getString("tags"));

		dto.setPropertiesEx(propEx);

		return dto;

	}

	protected PropertiesDTO getProjectFileProperties(JSONObject jsonProperties) {

		PropertiesDTO dto = new PropertiesDTO();
		logger.info(jsonProperties.toString());

		dto.setUserId(jsonProperties.getString("user"));
		dto.setPassword(jsonProperties.getString("pwd"));
		dto.setParentFolderId(jsonProperties.getString("parentId"));

		PropertiesExDTO propEx = new PropertiesExDTO();
		propEx.setFileType(jsonProperties.getString("fileType"));
		propEx.setBusiness(jsonProperties.getString("business"));
		propEx.setDomain(jsonProperties.getString("domain"));
		propEx.setOrg(jsonProperties.getString("org"));
		propEx.setRegion(jsonProperties.getString("region"));
		propEx.setResourceType(ResourceConstants.ResourceType.RES_PCK_PROJECT);
		propEx.setSearchable(true);
		propEx.setTags(jsonProperties.getString("tags"));
		dto.setPropertiesEx(propEx);

		return dto;

	}

	protected PropertiesDTO getDepartmentFileProperties(JSONObject jsonProperties) {

		PropertiesDTO dto = new PropertiesDTO();
		logger.info(jsonProperties.toString());

		dto.setUserId(jsonProperties.getString("user"));
		dto.setPassword(jsonProperties.getString("pwd"));
		dto.setParentFolderId(jsonProperties.getString("parentId"));

		PropertiesExDTO propEx = new PropertiesExDTO();
		propEx.setFileType(jsonProperties.getString("fileType"));
		propEx.setBusiness(jsonProperties.getString("business"));
		propEx.setDomain(jsonProperties.getString("domain"));
		propEx.setOrg(jsonProperties.getString("org"));
		propEx.setRegion(jsonProperties.getString("region"));
		propEx.setResourceType(ResourceConstants.ResourceType.RES_PCK_DEPARTMENT);
		propEx.setSearchable(true);
		propEx.setTags(jsonProperties.getString("tags"));
		dto.setPropertiesEx(propEx);

		return dto;
	}

	protected PropertiesDTO getInstituteFileProperties(JSONObject jsonProperties) {

		PropertiesDTO dto = new PropertiesDTO();
		logger.info(jsonProperties.toString());

		dto.setUserId(jsonProperties.getString("user"));
		dto.setPassword(jsonProperties.getString("pwd"));
		dto.setParentFolderId(jsonProperties.getString("parentId"));

		PropertiesExDTO propEx = new PropertiesExDTO();
		propEx.setFileType(jsonProperties.getString("fileType"));
		propEx.setBusiness(jsonProperties.getString("business"));
		propEx.setDomain(jsonProperties.getString("domain"));
		propEx.setOrg(jsonProperties.getString("org"));
		propEx.setRegion(jsonProperties.getString("region"));
		propEx.setResourceType(ResourceConstants.ResourceType.RES_PCK_INSTITUTE);
		propEx.setSearchable(true);
		propEx.setTags(jsonProperties.getString("tags"));
		dto.setPropertiesEx(propEx);

		return dto;

	}

	@Deprecated
	@ApiOperation(value = "检测文件是否已上传过，或者上传了一部分，并返回文件md5值", response = Result.class, notes = "checkFileMetadata")
	@RequestMapping(value = "file/info.check/{metadataJson}", method = { RequestMethod.GET })
	public Result checkFileMetadata(@PathVariable String metadataJson) throws Exception {

		FileInfoRequestDTO info = (FileInfoRequestDTO) JSONUtil.toObject(metadataJson, FileInfoRequestDTO.class);
		String fileId = Md5CaculateUtil.md5Hex(info.toString());
		if (!UploadFileMap.containsKey(fileId)) {
			UploadFileMap.put(fileId, info);
		}
		String tempDir = super.getContextPath(GlobalSystemParameters.UPLOAD_FILE_TEMP);
		String tempSavePath = tempDir + "/" + fileId;

		File file = new File(tempSavePath);
		if (file.exists()) {
			logger.info("文件哈希id [{}]，元数据 [{}] 已存在服务器，大小 [{}]", fileId, info.toString(), file.length());
			return super.successResult(new FileInfoResponseDTO(fileId, file.length(), Boolean.TRUE));
		}

		return super.successResult(new FileInfoResponseDTO(fileId, file.length(), Boolean.FALSE));

	}
	
	/*@ApiOperation(value = "mongodb测试", response = Result.class, notes = "testMongodb")
	@RequestMapping(value = "file/mongo", method = { RequestMethod.GET })
	public void testMongodb() throws Exception {

		 InputStream inputStream = null;
		// FileInputStream is; 
        try {
        	is = new FileInputStream(file);  
            int i = is.available(); // 得到文件大小  
            byte data[] = new byte[i];  
            is.read(data); // 读数据  
          File f = new File("h:\\123.jpg");
            inputStream = new FileInputStream(f);
            DBObject metaData = new BasicDBObject();
    		metaData.put("brand", "Audi");
    		metaData.put("model", "Audi A3");
    		metaData.put("Suffix", "."+FileUtil.getSuffix(f.getName()));
    		metaData.put("description",
    				"Audi german automobile manufacturer that designs, engineers, and distributes automobiles");
    		String id = fileStorageDao.store(inputStream , f.getName(), MimeType.getMimeType(FileUtil.getSuffix(f.getName())), metaData);
    		System.out.println("Find By Id----------------------");
    		
    		long start = new Date().getTime();
    		System.out.println("begin : " + start);
    		GridFSDBFile byId = fileStorageDao.getById(id);
    		System.out.println("File Name:- " + byId.getFilename());
    		
    		byId.writeTo("h:\\456.jpg");
    		
    		System.out.println("end, cost : "+(new Date().getTime() - start));
    			
    		inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
			if(inputStream != null)
				inputStream.close();
		}
		
	}
	*/
/*	
	@ApiOperation(value = "mongodb测试", response = Result.class, notes = "viewMongodbFile")
	@RequestMapping(value = "file/mongo/view/{id}", method = { RequestMethod.GET })
	public void viewMongodbFile(@PathVariable String id) {
		
		GridFSDBFile byId = fileStorageDao.getById(id);
		System.out.println("File Name:- " + byId.getFilename());

		try {
			InputStream is = byId.getInputStream();
			super.response.setContentType(byId.getContentType());
			super.response.setHeader("Cache-Control", "max-age=2592000"); // 一个月：30天
			super.response.setContentLength(((Long)byId.getLength()).intValue()); // .setContentLengthLong(byId.getLength());
			super.response.getOutputStream().write(FileUtil.getBytes(is));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}*/

}
