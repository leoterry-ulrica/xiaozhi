package com.dist.bdf.consumer.sga;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dist.bdf.base.constants.SessionContants;
import com.dist.bdf.base.controller.BaseController;
import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.result.Result;
import com.dist.bdf.base.utils.URLEncode;
import com.dist.bdf.common.conf.ecm.ECMConf;

import io.github.xdiamond.client.XDiamondConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.dist.bdf.facade.service.MaterialService;
import com.dist.bdf.facade.service.sga.SgaUserService;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
import com.dist.bdf.model.dto.sga.ConfirmlinkDTO;
import com.dist.bdf.model.dto.sga.SignupDTO;
import com.dist.bdf.model.dto.sga.UserBasicInfoRequestDTO;
import com.dist.bdf.model.dto.sga.UserEmailValidDTO;
import com.dist.bdf.model.dto.sga.UserLoginByEmailDTO;
import com.dist.bdf.model.dto.sga.UserRegisterSimpleDTO;
import com.dist.bdf.model.dto.sga.UserResponseDTO;
import com.dist.bdf.model.dto.wechat.UserInfo;
import com.dist.bdf.model.dto.wechat.WxappEncryptedUserInfo;
import com.dist.bdf.model.entity.sga.SgaUserAttachment;

@Api(tags = { "API-公共：用户信息模块" }, description = "SgaUserController")
@RestController
@RequestMapping(value = "/rest/sysservice/sga")
//@CrossOrigin(origins = "*")
public class SgaUserController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(SgaUserController.class);
	
	@Autowired
	private SgaUserService sgaUserService;
	@Autowired
	private XDiamondConfig xdiamondConf;
	@Autowired
	private MaterialService materialService;
	@Autowired
	private ECMConf ecmConf;
	
	@ApiOperation(value = "项目报名，包括自主报名和邀请报名。", notes = "registerEmail")
	@RequestMapping(value = "/user/register.send", method = { RequestMethod.POST })
	public Result registerEmail(
			@Valid
			@ApiParam(value = "电子邮箱") 
			@RequestBody SignupDTO dto, BindingResult result) {
		
		if(result.hasErrors())
			return super.errorResult("参数验证失败，详情："+result.toString());
		
		String serviceURI = super.getBaseURL()+"/rest/sysservice/sga/user/confirmlink";
		return super.successResult(this.sgaUserService.sendMailConfirmation(serviceURI, dto));
	}
	
    /*public void clickConfirmationLink(
			@ApiParam(value = "用户序列id") @RequestParam String uid,
			@ApiParam(value = "队列id") @RequestParam String queue,
			@ApiParam(value = "重定向地址") @RequestParam String redirect,
			@ApiParam(value = "注册类型") @RequestParam String registertypea,
			@ApiParam(value = "项目id") @RequestParam String projectid) throws Exception {*/
	@ApiOperation(value = "用户点击确认链接", notes = "clickConfirmationLink")
	@RequestMapping(value = "/user/confirmlink", method = { RequestMethod.GET })
	public void clickConfirmationLink(
			@Valid
			@ApiParam(value = "用户序列id") 
			@RequestParam ConfirmlinkDTO linkdto, BindingResult result) throws Exception {

		if(result.hasErrors())
			throw new BusinessException("参数验证失败，详情："+result.toString());
		
		UserEmailValidDTO dto = this.sgaUserService.clickConfirmationLink(linkdto);
		if (null == dto) {
			logger.error("链接无效，跳转到错误页面。");
			super.response.sendRedirect("/index.jsp");
			return;
		}

		super.response.sendRedirect(URLEncode.encodeURI(this.xdiamondConf.getProperty(linkdto.getRedirect())));
	}
	
	@ApiOperation(value = "用户注册", notes = "userRegister")
	@RequestMapping(value = "/user/register", method = { RequestMethod.POST })
	public Result userRegister(@RequestBody UserRegisterSimpleDTO dto) {
		
		return super.successResult(this.sgaUserService.registerFromValidLink(dto));
	}
	@ApiOperation(value = "loginByEmail", notes = "用户使用邮箱登录")
	@RequestMapping(value = "/user/login/email", method = { RequestMethod.POST })
	public Result loginByEmail(
			@Valid
		    @ApiParam( value = "email和加密后密码模型")
			@RequestBody UserLoginByEmailDTO dto, BindingResult result) {
		
		if(result.hasErrors())
			return super.errorResult("参数验证失败，详情："+result.toString());
		
		return super.successResult(this.sgaUserService.loginByEmail(dto.getEmail(), dto.getEncryptPwd()));
	}
	
	/*@ApiOperation(value = "loginByWechat", notes = "用户使用微信登录，不做密码有效性验证。")
	@RequestMapping(value = "/user/login/wechat", method = { RequestMethod.POST })
	public Result loginByWechat(
			@Valid
		    @ApiParam( value = "微信传过来的用户信息模型")
			@RequestBody UserInfo dto, BindingResult result) {
		
		if(result.hasErrors())
			return super.failResult("参数验证失败，详情："+result.toString());
		
		Object user = this.sgaUserService.loginByWechatNoPwd(dto);
		if(null == user) throw new BusinessException("微信登录失败。");
		
		return super.successResult(user);
	}*/
	
	@ApiOperation(value = "用户使用微信登录，不做密码有效性验证。", notes = "loginByWechat")
	@RequestMapping(value = "/user/login/wechat", method = { RequestMethod.GET })
	public Result loginByWechat(
		    @ApiParam( value = "微信传过来的唯一编码。"
		    		+ "code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。")
			@RequestParam(required = true) String code, 
			@ApiParam(value = "状态")
		    @RequestParam(required = false) String state) {

		UserResponseDTO user = this.sgaUserService.loginByWechatNoPwd(code, state);
		if(user != null && super.session != null) {
			// 存储用户编码
			 super.session.setAttribute(SessionContants.USER_KEY_2C, user.getUserCode());
		} else {
			if(super.session != null) {
				super.session.removeAttribute(SessionContants.USER_KEY_2C);
			}
			throw new BusinessException("微信登录失败。");
		}

		return super.successResult(user);
	}
	
	@ApiOperation(value = "更新用户的基本信息", notes = "updateUserBasicInfo")
	@RequestMapping(value = "/user/info/", method = { RequestMethod.PUT })
	public Result updateUserBasicInfo(
			@Valid
		    @ApiParam( value = "用户基本信息")
		    @RequestBody UserBasicInfoRequestDTO info, BindingResult result) {

		if(result.hasErrors()) {
			return super.errorResult("参数验证失败，详情："+result.toString());
		}
		UserResponseDTO user  = this.sgaUserService.updateUserBasicInfo(info);
		if(super.session != null && user != null) {
			super.session.setAttribute(SessionContants.USER_KEY_2C, user.getSysCode());
		} else {
			return super.failResult("更新失败");
		}
		return super.successResult(user);
	}

	@ApiOperation(value = "公众版检验用户的登录状态。", notes = "checkLoginStatus")
	@RequestMapping(value = "/v1/users/login/status", method = { RequestMethod.GET })
	public Result checkLoginStatus() {

		if(null == super.session) {
			return super.failResult("没有获取到session", "没有获取到session");
		}
		// session获取用户信息
		Object userCodeInSession = super.session.getAttribute(SessionContants.USER_KEY_2C);
		
		if(userCodeInSession != null) {
			// TODO 暂时通过session获取用户编码，重新查询数据库或者缓存，获取最新用户信息，因为目前前端修改用户信息后，重现创建了一个新session，原因未明。
			// 目前使用用户编码微作session key
			UserResponseDTO userDTO = this.sgaUserService.getUserByCode(userCodeInSession.toString());
			userDTO.setIsNew(false);
			// super.session.setAttribute(SessionContants.USER_KEY_2C, userSession);
			return super.successResult(userDTO, "用户已登录");
		} else {
			return super.failResult("session没有获取到用户登录状态信息", "用户未登录");
		}
	}
	
	@ApiOperation(value = "检测用户的微信信息。", notes = "checkUserInfoOfWechat")
	@RequestMapping(value = "/user/wechat/info/", method = { RequestMethod.POST})
	public Result checkUserInfoOfWechat(@RequestBody UserInfo info){
		
		return super.successResult(this.sgaUserService.checkUserInfoOfWechat(info));
	}
	
	@ApiOperation(value = "检测并获取微信小程序用户信息，返回系统中用户模型数据。", notes = "checkUserInfoOfWechatapp")
    @RequestMapping(value = "/user/wechatapp/info", method = RequestMethod.POST)
    public Result checkUserInfoOfWechatapp(
    		@ApiParam(value = "包含三个属性，encryptedData：加密数据，iv： 加密算法的初始向量，code：用户允许登录后，回调内容会带上 code（有效期五分钟），开发者需要将 code 发送到开发者服务器后台，使用code 换取 session_key api，将 code 换成 openid 和 session_key")
    		@RequestBody WxappEncryptedUserInfo info) {

		Result result = null;
        //登录凭证不能为空
        if (!StringUtils.hasLength(info.getCode())) {
        	result = super.errorResult("code 不能为空");
            return result;
        }

        return super.successResult(this.sgaUserService.checkUserInfoOfWechatapp(info));
    }
	@ApiOperation(value = "获取用户简历附件信息。", notes = "getUserResumes")
	@RequestMapping(value = "/user/resume/{userCode}", method = { RequestMethod.GET})
	public Result getUserResumes(@PathVariable String userCode){
		
		List<SgaUserAttachment> attachments = this.sgaUserService.getUserResumes(userCode);
		if(null == attachments || attachments.isEmpty()) {
			return super.successResult("没有找到用户相关简历附件信息");
		}
		List<DocumentDTO> docs = new ArrayList<DocumentDTO>(attachments.size());
		for(SgaUserAttachment attachment : attachments) {
			DocumentDTO tempDoc  = this.materialService.getDocInfoByVIdOs(this.ecmConf.getPublicObjectStore(""), attachment.getAttachVId(), userCode);
			if(tempDoc != null) {
				docs.add(tempDoc);
			}
		}
		return super.successResult(docs);
	}
	//  test for spring session begin
	/* @RequestMapping(value = "/login/{username}", method = {RequestMethod.GET})
	 public String login(@PathVariable String username){

		 HttpSession session =  request.getSession();
		 session.setAttribute(SessionContants.USER_KEY, username);

	        return String.format("session id : %s，登录成功", session.getId());
	    }

	    @RequestMapping(value = "index", method = {RequestMethod.GET})
	    public String index(){

	    	 HttpSession session =  request.getSession();
	    	 
	        return  String.format("当前客户端session id：%s，用户：%s", session.getId(), session.getAttribute(SessionContants.USER_KEY).toString());
	    }*/
	//  test for spring session end
		@ApiOperation(value = "获取所有注册的用户信息。", notes = "listUsers")
		@RequestMapping(value = "/users", method = { RequestMethod.GET})
		public Result listUsers(){
			return super.successResult(this.sgaUserService.listAllRegisterUsers());
		}
		@ApiOperation(value = "公众版账号登出。", notes = "logout")
		@RequestMapping(value = "/users/logout", method = { RequestMethod.GET})
		public Result logout(){

			logger.info(">>>用户注销");
			if(super.session != null) {
				super.session.removeAttribute(SessionContants.USER_KEY_2C);
			}
			return super.successResult("用户成功登出");
		}
		
		@ApiOperation(value = "根据用户编码获取用户信息", notes = "getUserInfoByUserCode")
		@RequestMapping(value = "/users/{userCode}", method = { RequestMethod.GET})
		public Result getUserInfoByUserCode(@PathVariable String userCode){

			return super.successResult(this.sgaUserService.getUserByCode(userCode));
		}
		@ApiOperation(value = "根据用户编码获取用户简历信息", notes = "getUserResumeInfo")
		@RequestMapping(value = "/users/resume/{userCode}", method = { RequestMethod.GET})
		public Result getUserResumeInfo(@PathVariable String userCode) {
			return super.successResult(this.sgaUserService.getUserResumeInfo(userCode));
		}
}
