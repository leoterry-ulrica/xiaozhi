package com.dist.bdf.consumer.sga;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dist.bdf.base.controller.BaseController;
import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.office.excel.ExcelDataFormatter;
import com.dist.bdf.base.office.excel.ExcelUtils;
import com.dist.bdf.base.result.Result;
import com.dist.bdf.base.utils.Base64Utils;
import com.dist.bdf.base.utils.DistThreadManager;
import com.dist.bdf.facade.service.PrivilegeService;
import com.dist.bdf.facade.service.ProjectService;
import com.dist.bdf.facade.service.sga.SgaProjectService;
import com.dist.bdf.facade.service.sga.SgaUserService;
import com.dist.bdf.common.conf.imgserver.ImgServerConf;
import com.dist.bdf.common.constants.GlobalSystemParameters;
import com.dist.bdf.common.constants.RoleConstants;
import com.dist.bdf.common.constants.UserStatus;
import com.dist.bdf.model.dto.sga.CooProjectAddDTO;
import com.dist.bdf.model.dto.sga.CooProjectRequestDTO;
import com.dist.bdf.model.dto.sga.CooProjectResponseDTO;
import com.dist.bdf.model.dto.sga.ImgInfo;
import com.dist.bdf.model.dto.sga.PrjUserRequestDTO;
import com.dist.bdf.model.dto.sga.PrjUserStatusPutDTO;
import com.dist.bdf.model.dto.sga.UserResponseDTO;
import com.dist.bdf.model.dto.system.UserDomainRoleDTO;
import com.dist.bdf.model.entity.sga.SgaProject;
import com.dist.bdf.model.entity.sga.SgaUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = { "API-公共：项目信息模块" }, description = "SgaProjectController")
@RestController
@RequestMapping(value = "/rest/sysservice/sga")
//@CrossOrigin(origins = "*")
public class SgaProjectController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(SgaProjectController.class);
	
	@Autowired
	private SgaProjectService sgaProjectService;
	@Autowired
	private Mapper dozerMapper;
	@Autowired
	private ImgServerConf imgServerConf;
	@Autowired
	private SgaUserService sgaUserService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private PrivilegeService privService;
	
	/**
	 * 获取项目的海报URL
	 * @param shortName
	 * @param poster
	 * @return
	 */
	/*@Deprecated
	private String getProjectPosterURLInner(String shortName, String poster) {
		
		String url = super.getBaseURL();
		if (!StringUtils.hasLength(poster)) {
			url = super.getBaseURL() + GlobalSystemParameters.DEFAULT_POSTER_PATH + "/"
					+ "pp.jpg";//StringUtil.getPYIndexStr(shortName, false).substring(0, 1).toLowerCase() + ".jpg";
		} else if (new File(super.getContextPath() + poster).exists()) {
			url = super.getBaseURL() + poster;
		} else {
			if(poster.toLowerCase().startsWith("http")){
				return poster;
			}
			url = super.getBaseURL() + GlobalSystemParameters.DEFAULT_POSTER_PATH + "/"
					+ "pp.jpg";// StringUtil.getPYIndexStr(shortName, false).substring(0, 1).toLowerCase() + ".jpg";
		}
		return url;
	}*/
	/**
	 * 获取项目海报URL
	 * @param img
	 * @return
	 */
	private String getProjectPosterURLInner(String img) {

		String url = super.getBaseURL();
		if (StringUtils.isEmpty(img)) {
			url = super.getBaseURL() + GlobalSystemParameters.DEFAULT_POSTER_PATH + "/" + "pp.jpg";
		} else if (img.contains("/fs/")) {
			url = this.imgServerConf.getServerURI().endsWith("/") ? this.imgServerConf.getServerURI() + img
					: this.imgServerConf.getServerURI() + "/" + img;
		} else if (new File(super.getContextPath() + img).exists()) {
			url = super.getBaseURL() + img;
		} else {
			if (img.toLowerCase().startsWith("http")) {
				return img;
			}
			url = super.getBaseURL() + GlobalSystemParameters.DEFAULT_POSTER_PATH + "/" + "pp.jpg";
		}
		return url;
	}
	
    private Result setPosterInner(String id, String type, String suffix, String content) {
		
		try {
			//JSONObject jsonObj = JSONObject.parseObject(logoInfo);
			// String caseId = logoInfo.getId().toString();//jsonObj.getString("id");
			// String type = logoInfo.getType();//jsonObj.getString("type");
			// String suffix = logoInfo.getSuffix();//jsonObj.getString("suffix");

			logger.info(">>>case id:[{}], mime-type:[{}], suffix:[{}] ", id, type, suffix);

			String newFileName = System.currentTimeMillis() + (suffix.startsWith(".") ? suffix : "." + suffix);
			String localpath = super.getContextPath(GlobalSystemParameters.POSTER_PATH) + "/" + newFileName;

			Base64Utils.decodeToFile(localpath, content);//jsonObj.getString("content")

			if (!new File(localpath).exists())
				throw new BusinessException("上传文件失败");

			String logoRelativePath = GlobalSystemParameters.POSTER_PATH + "/" + newFileName;

			// 删除旧的头像
			CooProjectResponseDTO project = this.sgaProjectService.getProjectByCaseId(id);
			if (null == project) {
				throw new BusinessException(String.format("case id为[%s]的项目不存在", id));
			}

			if (!StringUtils.isEmpty(project.getPoster()) && -1 == project.getPoster().toLowerCase().indexOf("default")) {
				File preAvatarPath = new File(super.getContextPath() + "/" + project.getPoster());
				if (preAvatarPath.exists()) {
					// 删除原来的头像
					boolean result = preAvatarPath.delete();
					if (!result) {
						logger.info(">>>删除企业原logo文件失败：[{}]", preAvatarPath);
					}
				}
			}

			String logoURL = this.sgaProjectService.changePoster(id, logoRelativePath);
			logoURL = super.getBaseURL() + logoRelativePath;
			System.out.println(">>>logo url：" + logoURL);
			return super.successResult(logoURL);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		logger.info(">>>上传文件失败......");
		return super.failResult("上传文件失败");
	}
	@Deprecated
	@ApiOperation(value = "添加项目或者保存项目(已废弃，切换到/v1/projects/)", notes = "saveOrUpdateProject")
	@RequestMapping(value = "/project/save.update", method = { RequestMethod.POST, RequestMethod.PUT })
	public Result saveOrUpdateProject(
			@Valid
			@ApiParam(value = "项目添加的JSON信息")
			@RequestBody CooProjectRequestDTO dto, BindingResult result) throws Exception {
		
		if(result.hasErrors())
			return super.failResult(result.toString());
		
		// TODO 为何需要复制到一个有完全相同的属性的对象？
		CooProjectAddDTO addDTO = this.dozerMapper.map(dto, CooProjectAddDTO.class);
		// BeanUtils.copyProperties(addDTO, dto);
		/*addDTO.setName(dto.getName());
		addDTO.setCaseId(dto.getCaseId());
		addDTO.setDescription(dto.getDescription());
		addDTO.setStatus(dto.getStatus());
		addDTO.setTag(dto.getTag());
		addDTO.setRealm(dto.getRealm());*/
		SgaProject project = (SgaProject) this.sgaProjectService.saveOrUpdate(addDTO);
		Result resultObj = null;
		
		if(dto.getPosterInfo() != null && !StringUtils.isEmpty(dto.getPosterInfo().getContent())){
			JSONObject logoJson = JSONObject.parseObject(JSONObject.toJSONString(dto.getPosterInfo()));
			logoJson.put("id", project.getSysCode());
			resultObj = this.setPosterInner(project.getSysCode(), logoJson.getString("type"), logoJson.getString("suffix"), logoJson.getString("content") );
			if(resultObj.getStatus().equalsIgnoreCase("success"))
				project.setPoster(resultObj.getData().toString());
		}
		return super.successResult(project);
	}
	@ApiOperation(value = "添加项目或者保存项目", notes = "saveOrUpdateProjectV1")
	@RequestMapping(value = "/v1/projects", method = { RequestMethod.POST, RequestMethod.PUT })
	public Result saveOrUpdateProjectV1(
			@Valid
			@ApiParam(value = "项目添加的JSON信息")
			@RequestBody CooProjectRequestDTO dto, BindingResult result) throws Exception {
		
		if(result.hasErrors())
			return super.failResult("参数验证失败，详情："+result.toString());
		
		// CooProjectAddDTO并没有海报信息
		CooProjectAddDTO addDTO = this.dozerMapper.map(dto, CooProjectAddDTO.class);
		SgaProject project = (SgaProject) this.sgaProjectService.saveOrUpdate(addDTO);

		if (dto.getPosterInfo() != null && !StringUtils.isEmpty(dto.getPosterInfo().getContent())) {
			this.sgaProjectService.deletePosterByProjectCode(project.getSysCode());
			dto.getPosterInfo().setId(project.getSysCode());
			String relativePath = this.sgaProjectService.updatePoster(dto.getPosterInfo());
			project.setPoster(this.getProjectPosterURLInner(relativePath));
		}
		return super.successResult(project);
	}
	@Deprecated
	@ApiOperation(value = "设置项目海报(已废弃，切换到/v1/project/poster)", notes = "uploadProjectPoster")
	@RequestMapping(value = "/project/poster", method = { RequestMethod.POST, RequestMethod.PUT })
	public Result uploadProjectPoster(
			@ApiParam(value = "json数据，{\"id\":案例标识,\"type\":\"image/jpeg\",\"suffix\":\"jpg\",\"content\":\"base64值\"}", required = true)
			@RequestBody ImgInfo imgInfo ){
		return this.setPosterInner(imgInfo.getId(), imgInfo.getType(), imgInfo.getSuffix(), imgInfo.getContent());
	}
	
	@ApiOperation(value = "设置项目海报", notes = "uploadProjectPosterV1")
	@RequestMapping(value = "/v1/project/poster", method = { RequestMethod.POST, RequestMethod.PUT })
	public Result uploadProjectPosterV1(
			@Valid
			@ApiParam(value = "json数据，{\"id\":案例标识,\"type\":\"image/jpeg\",\"suffix\":\"jpg\",\"content\":\"base64值\"}", required = true)
			@RequestBody ImgInfo imgInfo, BindingResult result ){
		
		if(result.hasErrors()) {
			return super.failResult("参数验证失败，详情："+ result.toString());
		}
		this.sgaProjectService.deletePosterByProjectCode(imgInfo.getId());
		String relativePath = this.sgaProjectService.updatePoster(imgInfo);
		if(StringUtils.isEmpty(relativePath)) {
			return super.failResult("项目海报修改失败");
		}
		return super.successResult(this.imgServerConf.getServerURI() + relativePath);

	}
	
	@ApiOperation(value = "根据案例标识获取合作项目信息", notes = "getProjectByCaseId")
	@RequestMapping(value = "/project/caseid/{caseId}", method = { RequestMethod.GET })
	public Result getProjectByCaseId(
			@ApiParam( value = "案例标识，如：{78944816-817F-4C2C-BB77-2E5383AB9DE9}")
			@PathVariable String caseId) {
		
		CooProjectResponseDTO p = this.sgaProjectService.getProjectByCaseId(caseId);
		p.setPoster(this.getProjectPosterURLInner(p.getPoster()));
		
		return super.successResult(p);
	}
	
	@ApiOperation(value = "修改项目的状态", notes = "changeProjectStatus")
	@RequestMapping(value = "/project/status.chage/{caseId}/{status}", method = { RequestMethod.PUT })
	public Result changeProjectStatus(
			@ApiParam( value = "案例标识，如：{78944816-817F-4C2C-BB77-2E5383AB9DE9}")
			@PathVariable String caseId,
			@ApiParam( value = "项目状态。0：关闭；1：招募中；2：合作中；3：合作结束")
			@PathVariable int status) {

		return super.successResult(this.sgaProjectService.changeStatus(caseId, status));
	}
	@Deprecated
	@ApiOperation(value = "获取企业下的所有项目信息（已废弃，切换到/projects/{realm}/{userId}）", notes = "getProjectsByCompanyRealm")
	@RequestMapping(value = "/projects/com.realm/{realm}", method = { RequestMethod.GET })
	public Result getProjectsByCompanyRealm(
			@ApiParam( value = "企业域名称，如：thupdi")
			@PathVariable String realm) {

		List<CooProjectResponseDTO> datas = this.sgaProjectService.getProjectsByCompanyRealm(realm);
		if(datas != null && !datas.isEmpty()){
			for(CooProjectResponseDTO p : datas){
				p.setPoster(this.getProjectPosterURLInner(p.getPoster()));	
			}
		}
		return super.successResult(datas);
	}
	
	@ApiOperation(value = "获取企业下的所有项目信息", notes = "getProjectsByCompanyRealmEx")
	@RequestMapping(value = "/projects/{realm}/{userId}", method = { RequestMethod.GET })
	public Result getProjectsByCompanyRealmEx(@ApiParam(value = "企业域名称，如：thupdi") @PathVariable String realm,
			@ApiParam(value = "用户序列id。如果用户未登录，则传入-1。") @PathVariable Long userId) {

		if(-1 == userId)
			logger.warn(">>>当前用户未登录");
		
		List<CooProjectResponseDTO> datas = this.sgaProjectService.getProjectsByCompanyRealm(realm, userId);
		if (datas != null && !datas.isEmpty()) {
			for (CooProjectResponseDTO p : datas) {
				p.setPoster(this.getProjectPosterURLInner(p.getPoster()));
			}
		}
		return super.successResult(datas);
	}
	
	@ApiOperation(value = "获取用户相关的所有项目信息", notes = "getProjectsByUserId")
	@RequestMapping(value = "/projects/user/{userId}", method = { RequestMethod.GET })
	public Result getProjectsByUserId(
			@ApiParam( value = "用户序列id(欠妥，可能会造成被恶意获取，应使用guid)")
			@PathVariable Long userId) {

		List<CooProjectResponseDTO> datas = this.sgaProjectService.getProjectsByUserId(userId);
		if(datas != null && !datas.isEmpty()) {
			for(CooProjectResponseDTO p : datas) {
				p.setPoster(this.getProjectPosterURLInner(p.getPoster()));	
			}
		}
		return super.successResult(datas);
	}
	
	@ApiOperation(value = "报名项目", notes = "joinInProject")
	@RequestMapping(value = "/project/joinin", method = { RequestMethod.POST })
	public Result joinInProject(
			@Valid
			@RequestBody PrjUserRequestDTO dto, BindingResult result) {
		
		logger.info(">>>报名项目参数：{}", JSONObject.toJSONString(dto));
		if(result.hasErrors())
			return super.errorResult("参数验证失败，详情：" + result.toString());
		
		return super.successResult(this.sgaProjectService.joinInProject(dto));
	}
	
	@ApiOperation(value = "获取项目中注册的用户信息，这里指的是外部用户", notes = "listRegisterUsers")
	@RequestMapping(value = "/project/regUsers/{caseId}", method = { RequestMethod.GET })
	public Result listRegisterUsers(@PathVariable String caseId) {
		return super.successResult(this.sgaProjectService.listRegisterUsers(caseId));
	}
	@ApiOperation(value = "项目注册用户导出excel，返回下载URL。", response = Result.class, notes = "exportProjectRegUserToExcel")
	@RequestMapping(value = "/v1/project/regUsers/excel/{caseId}", method = { RequestMethod.POST})
	public Result exportProjectRegUserToExcel(@PathVariable String caseId) throws Exception {
		
		// 构造数据对象   
		List<UserResponseDTO> regUsers = this.sgaProjectService.listRegisterUsers(caseId);
		if(null == regUsers || regUsers.isEmpty()) {
			return super.failResult("无数据可导出");
		}
        ExcelDataFormatter edf = new ExcelDataFormatter();
        Map<String, String> statusFormatMap = new HashMap<String, String>();

        statusFormatMap.put("-1", "拒绝");
        statusFormatMap.put("0", "待审核");
        statusFormatMap.put("1", "参与");
        statusFormatMap.put("2", "报名成功");
        edf.set("status", statusFormatMap);
        
        Map<String, String> sexFormatMap = new HashMap<String, String>();
        sexFormatMap.put("f", "女");
        sexFormatMap.put("m", "男");
        edf.set("sex", sexFormatMap);
      
        String path = super.getContextPath(GlobalSystemParameters.DIR_FREEMARKER);
        File outFile = new File(path +"/"+ UUID.randomUUID().toString() + ".xlsx");
        logger.info(">>>写入本地文件");
        ExcelUtils.writeToFile(regUsers, edf, outFile.getAbsolutePath(), "用户注册信息", UserResponseDTO.class);
        if (outFile.exists()) {
        	logger.info(">>>生成的文件：[{}]", outFile.getAbsolutePath());
        	return super.successResult(super.getBaseURL() + GlobalSystemParameters.DIR_FREEMARKER + "/" +outFile.getName());
		}
        return super.failResult("excel生成失败");
	}
	@ApiOperation(value = "设置注册用户在项目中的状态", notes = "setUserStatusInProject")
	@RequestMapping(value = "/project/userstatus", method = { RequestMethod.PUT })
	public Result setUserStatusInProject(@RequestBody PrjUserStatusPutDTO dto){
		
		return super.successResult(this.sgaProjectService.setUserStatusInProject(dto));
	}
	
	@ApiOperation(value = "绿色通道，直接参与", notes = "joininProjectDirectoryAndMsg")
	@RequestMapping(value = "/v1/project/joiningreen", method = { RequestMethod.POST })
	public Result joininProjectDirectoryAndMsg(@Valid
			@RequestBody final PrjUserRequestDTO dto, BindingResult result) {
		
		if(result.hasErrors())
			return super.errorResult("参数验证失败，详情：" + result.toString());
		
		// 先参与
	    this.sgaProjectService.joinInProject(dto);
	    // 然后设置状态为1，表示参与状态
	    PrjUserStatusPutDTO prjUserStatus = new PrjUserStatusPutDTO();
	    prjUserStatus.setCaseId(dto.getCaseId());
	    prjUserStatus.setStatus(UserStatus.PROJECT_JOININ);
	    prjUserStatus.setUserId(dto.getUserId());
	    this.sgaProjectService.setUserStatusInProject(prjUserStatus);
	    // 加入项目组
	    final SgaUser user = sgaUserService.getUserEntityById(dto.getUserId());
	    UserDomainRoleDTO udrDto = new UserDomainRoleDTO();
	    Map<String, Integer> users = new HashMap<String, Integer>();
	    users.put(user.getSysCode(), 1);
	    udrDto.setUsers(users);
	    udrDto.setDomainCode(dto.getCaseId());
	    udrDto.setRoleCode(RoleConstants.RoleCode.R_Project_Partner);
	    this.privService.addUserToProjectGroupEx(udrDto);
	    // 发送微信模板信息
	    DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
			@Override
			public void run() {
			    
			    if(user != null) {
			    	projectService.sendWechatTemplateMsg(dto.getCaseId(), user.getSysCode());
			    }
			}
		});
	  
		return super.successResult("参与成功");
	}
}
