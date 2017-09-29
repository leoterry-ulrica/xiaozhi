package com.dist.bdf.consumer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dist.bdf.base.constants.MimeType;
import com.dist.bdf.base.controller.BaseController;
import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.job.DelTempFileTimerTask;
import com.dist.bdf.base.result.Result;
import com.dist.bdf.base.utils.Base64Utils;
import com.dist.bdf.base.utils.HttpRequestHelper;
import com.dist.bdf.base.utils.StringUtil;
import com.dist.bdf.base.utils.URLEncode;
import com.dist.bdf.common.conf.common.GlobalConf;
import com.dist.bdf.common.conf.officeonline.OfficeonlineConf;
import com.dist.bdf.facade.service.CommonService;
import com.dist.bdf.facade.service.EdsService;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
import com.dist.bdf.model.dto.system.M2ultipartFileDTO;
import com.dist.bdf.model.dto.system.ThumbnailDTO;
import com.dist.bdf.model.dto.system.WzInfoDTO;
import com.dist.bdf.model.dto.system.eds.EdsResultDTO;

import io.github.xdiamond.client.XDiamondConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

@Api(tags={"API-通用服务模块"}, description = "CommonController")
@RestController
@RequestMapping(value="/rest/sysservice")
//@CrossOrigin(origins = "*")
public class CommonController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private XDiamondConfig xconf;
	@Autowired
	private OfficeonlineConf ooConf;
	@Autowired
	private GlobalConf globalConf;
	@Autowired
	private CommonService commonService;
	@Autowired
	private EdsService edsService;
	
	private DelTempFileTimerTask delTask = new DelTempFileTimerTask();
	
	
	/**
	 * 接收上传的文件
	 * @param request
	 * @return
	 */
	public List<M2ultipartFileDTO> getUploadFiles(HttpServletRequest request) {
		
		List<M2ultipartFileDTO> uploadFiles = new ArrayList<M2ultipartFileDTO>();
		try {
			int count = 0;
			//创建一个通用的多部分解析器
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
					request.getSession().getServletContext());
			//判断 request 是否有文件上传,即多部分请求
			if (multipartResolver.isMultipart(request)) {
				//转换成多部分request  
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				//取得request中的所有文件名
				Iterator<String> iter = multiRequest.getFileNames();

				while (iter.hasNext()) {
					count++;
					//记录上传过程起始时的时间，用来计算上传时间
					//int pre = (int) System.currentTimeMillis();
					//取得上传文件
					MultipartFile mpfile = multiRequest.getFile(iter.next());
					if (mpfile != null) {
						uploadFiles.add(toMyMultipartFile(mpfile));
					}
				}
			}
			logger.info("接收到 [{}] 个文件", count);
		} catch (Exception ex) {
			logger.error(ex.getMessage(),ex);

		}
		return uploadFiles;
	}

	/**
	 * MultipartFile转换成自定义的M2ultipartFileDTO
	 * @param mpfile
	 * @param extension
	 * @return
	 * @throws IOException
	 * @throws MagicParseException
	 * @throws MagicMatchNotFoundException
	 * @throws MagicException
	 */
	protected M2ultipartFileDTO toMyMultipartFile(MultipartFile mpfile)
			throws IOException, MagicParseException, MagicMatchNotFoundException, MagicException {
		
		String originalFilename = mpfile.getOriginalFilename();
		String extension = originalFilename.substring( mpfile.getOriginalFilename().lastIndexOf("."));
		M2ultipartFileDTO m2file = new M2ultipartFileDTO();
		m2file.setOriginalFilename(originalFilename);
		m2file.setContentStream(mpfile.getBytes());
		String mimeType = MimeType.getMimeType(extension);
		if(StringUtil.isNullOrEmpty(mimeType)){
			
			logger.info(">>>开始字节流获取文件mimeType");
			MagicMatch match = Magic.getMagicMatch(mpfile.getBytes(), true);
			mimeType = (null == match)? mpfile.getContentType() : match.getMimeType();// file.getContentType()不准确
			logger.info(">>>结束获取到的文件mimeType："+ mimeType);
		}

		m2file.setContentType(mimeType);
		m2file.setSize(mpfile.getSize());
		return m2file;
	}
	/**
	 * 
	 * @param mpfile
	 * @param bytes 文件真实大小
	 * @return
	 * @throws IOException
	 * @throws MagicParseException
	 * @throws MagicMatchNotFoundException
	 * @throws MagicException
	 */
	protected M2ultipartFileDTO toMyMultipartFile(MultipartFile mpfile, byte[] bytes)
			throws IOException, MagicParseException, MagicMatchNotFoundException, MagicException {
		
		String originalFilename = mpfile.getOriginalFilename();
		String extension = originalFilename.substring( mpfile.getOriginalFilename().lastIndexOf("."));
		M2ultipartFileDTO m2file = new M2ultipartFileDTO();
		m2file.setOriginalFilename(originalFilename);
		m2file.setContentStream(bytes);
		String mimeType = MimeType.getMimeType(extension);
		if(StringUtil.isNullOrEmpty(mimeType)){
			
			logger.info(">>>开始字节流获取文件mimeType");
			MagicMatch match = Magic.getMagicMatch(mpfile.getBytes(), true);
			mimeType = (null == match)? mpfile.getContentType() : match.getMimeType();// file.getContentType()不准确
			logger.info(">>>结束获取到的文件mimeType："+ mimeType);
		}

		m2file.setContentType(mimeType);
		m2file.setSize(mpfile.getSize());
		return m2file;
	}
	@ApiOperation(value = "property/key", notes = "获取系统属性值")
	@RequestMapping(value="/property/key/{key:.+}", method = RequestMethod.GET)
	public Result getPropertyByKey(
			@ApiParam(value = "属性key")
			@PathVariable String key) {
		
		Properties props = xconf.getProperties();
		if(props.containsKey(key)){
			return super.successResult(props.get(key));
		}
		return super.failResult("不存在属性key：["+key+"]");
	}
	

	@ApiOperation(value = "根据文件后缀获取查看器地址", response = Result.class, notes = "getViewerUrl")
	@RequestMapping(value="/getViewerUrl/{extension:.+}", method = RequestMethod.GET)
	public Result getViewerUrl(
			@ApiParam(value = "文档扩展名，不带点号“.”")
			@PathVariable String extension) {
		
		String server = ooConf.getServer();
		if(!server.endsWith("/")) server += "/";
		
		String key = "oo.viewer."+extension;
		Result value = getPropertyByKey(key);
		
		if(value.getStatus().equalsIgnoreCase("success")){
			return  super.successResult(server + value.getData());
		}

		throw new BusinessException("没有找到扩展名 [{0}] 对应的查看器。", extension);
		
	}
	
	public void clearTempDir(){
		
		delTask.setDirectories(globalConf.getClearDirectories());
		delTask.execute();
	}
	
	@ApiOperation(value = "获取所有资源类型", response = Result.class, notes = "getResourceTypes")
	@RequestMapping(value="/res/type", method = RequestMethod.GET)
	public Result getResourceTypes() {
		
		return super.successResult(this.commonService.getResourceTypes());
		
	}
	
	// <=====eds模块begin

		@ApiOperation(value = "提供给经营性项目的外部数据", response = EdsResultDTO.class, notes="getEdsData_JYXM")
		@RequestMapping(value = "/eds/type/XZ_CASETYPE_JYXM", method = RequestMethod.POST) // 不能使用GET方法
		public EdsResultDTO getEdsData_JYXM(
				@ApiParam(value="icn插件服务调取eds传入的body内容") 
				@RequestBody String requestBodyContent) throws Exception {
			
			logger.info(">>>XZ_CASETYPE_JYXM，请求体内容：[{}]" , requestBodyContent);

			JSONObject obj = JSONObject.parseObject(requestBodyContent);
			// 截取属性数组
			JSONArray properties = obj.getJSONArray("properties");

			logger.info(">>>请求体的属性[properties]值信息：[{}]", properties.toString());
			String repositoryId = obj.getString("repositoryId");
			logger.info(">>>属性[repositoryId]值信息：[{}]", repositoryId);
			EdsResultDTO result = this.edsService.getEdsDataJYXM(repositoryId, properties.toString());
			//String encode = StringUtil.utf8Encode(JSONUtil.toJSONString(result));
			logger.info(">>>结果信息：[{}]" , JSONObject.toJSONString(result));
		
			return result;
		}
		@ApiOperation(value = "提供给团队项目的外部数据（济南版本在使用）", response = EdsResultDTO.class, notes="getEdsData_TDXM")
		@RequestMapping(value = "/eds/type/XZ_CASETYPE_TDXM", method = RequestMethod.POST) // 不能使用GET方法
		public EdsResultDTO getEdsData_TDXM(
				@ApiParam(value="icn插件服务调取eds传入的body内容") 
				@RequestBody String requestBodyContent) throws Exception {
			
			logger.info(">>>XZ_CASETYPE_TDXM，请求体内容：[{}]" , requestBodyContent);

			JSONObject obj = JSONObject.parseObject(requestBodyContent);
			// 截取属性数组
			JSONArray properties = obj.getJSONArray("properties");

			logger.info(">>>请求体的属性[properties]值信息：[{}]", properties.toString());
			String repositoryId = obj.getString("repositoryId");
			logger.info(">>>属性[repositoryId]值信息：[{}]", repositoryId);
			EdsResultDTO result = this.edsService.getEdsDataTDXM(repositoryId, properties.toString());
			//String encode = StringUtil.utf8Encode(JSONUtil.toJSONString(result));
			logger.info(">>>结果信息：[{}]" , JSONObject.toJSONString(result));
		
			return result;
		}
		@ApiOperation(value = "提供给团队管理性项目的外部数据", response = EdsResultDTO.class, notes="getEdsData_TDGL")
		@RequestMapping(value = "/eds/type/XZ_CPTDGL", method = RequestMethod.POST) // 不能使用GET方法
		public EdsResultDTO getEdsData_TDGL(
				@ApiParam(value="icn插件服务调取eds传入的body内容") 
				@RequestBody String requestBodyContent) throws Exception {
			
			logger.info(">>>请求体内容：[{}]" , requestBodyContent);

			JSONObject obj = JSONObject.parseObject(requestBodyContent);
			// 截取属性数组
			JSONArray properties = obj.getJSONArray("properties");

			logger.info(">>>请求体的属性[properties]值信息：[{}]", properties.toString());
			String repositoryId = obj.getString("repositoryId");
			logger.info(">>>属性[repositoryId]值信息：[{}]", repositoryId);
			EdsResultDTO result = this.edsService.getEdsDataTDGL(repositoryId, properties.toString());
			//String encode = StringUtil.utf8Encode(JSONUtil.toJSONString(result));
			logger.info(">>>结果信息：[{}]" , JSONObject.toJSONString(result));
		
			return result;
		}
		
		@ApiOperation(value = "广州，提供给经营性项目的外部数据", response = EdsResultDTO.class, notes="getEdsData_JYXM_GZPI")
		@RequestMapping(value = "/eds/type/GZPI_CASE_JYXM", method = RequestMethod.POST) // 不能使用GET方法
		public EdsResultDTO getEdsData_JYXM_GZPI(
				@ApiParam(value="icn插件服务调取eds传入的body内容") 
				@RequestBody String requestBodyContent) throws Exception {
			
			logger.info(">>>请求体内容：[{}]" , requestBodyContent);

			JSONObject obj = JSONObject.parseObject(requestBodyContent);
			// 截取属性数组
			JSONArray properties = obj.getJSONArray("properties");

			logger.info(">>>请求体的属性[properties]值信息：[{}]", properties.toString());
			String repositoryId = obj.getString("repositoryId");
			logger.info(">>>属性[repositoryId]值信息：[{}]", repositoryId);
			EdsResultDTO result = this.edsService.getEdsDataGZ(repositoryId, properties.toString());
			//String encode = StringUtil.utf8Encode(JSONUtil.toJSONString(result));
			logger.info(">>>结果信息：[{}]" , JSONObject.toJSONString(result));
			
			return result;
		}

		/**
		 * 提供给科研性项目的用户信息
		 * 
		 * @return
		 */
		// @RequestMapping(value="/eds/type/XZ_CASETYPE_KYXM")
		/*public EdsResultDTO getEdsData_KYXM(@RequestBody String requestBodyContentJson) {
			System.out.println("====>请求体内容：" + requestBodyContentJson);
			JSONObject obj = JSONObject.fromObject(requestBodyContentJson);
			// 截取属性数组
			JSONArray properties = obj.getJSONArray("properties");
			return this.edsService.getEdsData(properties.toString());
		}*/

		@ApiOperation(value = "提供给应标意向申请的外部数据", response = EdsResultDTO.class, notes="getEdsData_YXSQ")
		@RequestMapping(value = "/eds/type/XZ_CASETYPE_YXSQ", method = RequestMethod.POST) // 不能使用GET方法
		public EdsResultDTO getEdsData_YXSQ(@RequestBody String requestBodyContentJson) {
			System.out.println("====>请求体内容：" + requestBodyContentJson);
			JSONObject obj = JSONObject.parseObject(requestBodyContentJson);
			// 截取属性数组
			JSONArray properties = obj.getJSONArray("properties");
			return this.edsService.getEdsDataYXSQ(properties.toString());
		}
		
		@ApiOperation(value = "广州，提供给应标意向申请的外部数据", response = EdsResultDTO.class, notes="getEdsData_YXSQ_GZPI")
		@RequestMapping(value = "/eds/type/GZPI_CASE_YXSQ", method = RequestMethod.POST) // 不能使用GET方法
		public EdsResultDTO getEdsData_YXSQ_GZPI(@RequestBody String requestBodyContentJson) {
			System.out.println("====>请求体内容：" + requestBodyContentJson);
			JSONObject obj = JSONObject.parseObject(requestBodyContentJson);
			// 截取属性数组
			JSONArray properties = obj.getJSONArray("properties");
			return this.edsService.getEdsDataYXSQ(properties.toString());
		}

		@ApiOperation(value="刷新机构的缓存信息",notes="refreshRegionTreeCache")
		@RequestMapping(value = "region/cache.refresh", method = {RequestMethod.PUT, RequestMethod.GET})
		public Result refreshRegionTreeCache() {
			
			this.edsService.refreshRegionTreeCache();

			return super.successResult("刷新完成。");
		}
		
		@ApiOperation(value = "提供给合作项目的外部数据", response = EdsResultDTO.class, notes="getEdsData_HZXM")
		@RequestMapping(value = "/eds/type/XZ_CASETYPE_HZXM", method = RequestMethod.POST) // 不能使用GET方法
		public EdsResultDTO getEdsData_HZXM(
				@ApiParam(value="icn插件服务调取eds传入的body内容") 
				@RequestBody String requestBodyContent) throws Exception {
			
			logger.info(">>>请求体内容：[{}]" , requestBodyContent);

			JSONObject obj = JSONObject.parseObject(requestBodyContent);
			// 截取属性数组
			JSONArray properties = obj.getJSONArray("properties");

			logger.info(">>>请求体的属性[properties]值信息：[{}]", properties.toString());
			String repositoryId = obj.getString("repositoryId");
			logger.info(">>>属性[repositoryId]值信息：[{}]", repositoryId);
			EdsResultDTO result = this.edsService.getEdsDataHZXM(repositoryId, properties.toString());
			//String encode = StringUtil.utf8Encode(JSONUtil.toJSONString(result));
			logger.info(">>>结果信息：[{}]" , JSONObject.toJSONString(result));
		
			return result;
		}

		// =====>eds模块end
		@ApiOperation(value = "分词服务", notes="pullWord")
		@RequestMapping(value = "/pullword", method = RequestMethod.GET) 
		public Result pullWord(
				@ApiParam(value = "[a paragraph of chinese words] for example: source=清华大学是好学校", required = true)
				@RequestParam String source, 
				@Valid
				@NumberFormat(style = NumberFormat.Style.NUMBER)
				@ApiParam(value = "[threshold] for example: param1=0 to pull all word, param1=1 to pull word with probability with 100%", required = true)
				@RequestParam float threshold, 
				@Valid
				@NumberFormat(style = NumberFormat.Style.NUMBER)
				@ApiParam(value = "[debug] for example: param2=0 debug model is off, param2=1 debug mode in on(show all probabilities of each word)", required = true)
				@RequestParam int debug){
			
			return super.successResult(HttpRequestHelper.sendGet(URLEncode.encodeURI(String.format("http://api.pullword.com/get.php?source=%s&param1=%s&param2=%s", source, threshold, debug))));
		}
		/**
		 * 获取缩略图可访问URL
		 * @param request 
		 * @return
		 */
		public String getThumbURL(HttpServletRequest request) {
			return super.getBaseURL(request)+"/thumb";
		}
		/**
		 * 获取缩略图上下文路径
		 * @param request 
		 * @return
		 */
		public String getThumbContextPath(HttpServletRequest request) {
			return super.getContextPath(request, "thumb");
		}
		/**
		 * 生成微作缩略图
		 * @param wzInfoDTOs
		 */
		public void generateWzThumbnail(HttpServletRequest request, List<WzInfoDTO> wzInfoDTOs) {
			if(wzInfoDTOs !=null && !wzInfoDTOs.isEmpty()) {
				String contextPath = this.getThumbContextPath(request);
				String baseURL = this.getThumbURL(request);
				for(WzInfoDTO wz : wzInfoDTOs) {
					List<ThumbnailDTO> thumbnails = wz.getThumbnails();
					if(thumbnails != null && !thumbnails.isEmpty()) {
						for(ThumbnailDTO thumb : thumbnails) {
							String tempFile = contextPath + "/" +thumb.getId()+".png";
							try {
								if(!new File(tempFile).exists() && thumb.getImgByte() != null) {
									Base64Utils.byteArrayToFile(thumb.getImgByte(), tempFile);
								}
								thumb.setImg(baseURL +  "/" +thumb.getId()+".png");
							} catch (Exception e) {
								logger.error(e.getMessage(), e);
							}
						}
					}
				}
			}
		}
		/**
		 * 生成文档的缩略图
		 * @param data
		 */
		public void generateDocThumbnail(HttpServletRequest request, List<DocumentDTO> data) {
			if(data != null && !data.isEmpty()) {
				String contextPath = this.getThumbContextPath(request);
				String baseURL = this.getThumbURL(request);
				for(DocumentDTO doc : data) {
					ThumbnailDTO thumbnail = doc.getThumbnail();
					if(null == thumbnail) continue;
					
					String tempFile = contextPath + "/" +doc.getId()+".png";
					try {
						if(!new File(tempFile).exists() && thumbnail.getImgByte() != null) {
							Base64Utils.byteArrayToFile(thumbnail.getImgByte(), tempFile);
						}
						thumbnail.setImg(baseURL +  "/" +thumbnail.getId()+".png");
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		}
}
