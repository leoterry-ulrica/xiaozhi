package com.dist.bdf.consumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dist.bdf.base.constants.SessionContants;
import com.dist.bdf.base.controller.BaseController;
import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.base.result.Result;
import com.dist.bdf.base.utils.HttpRequestHelper;
import com.dist.bdf.base.utils.JSONUtil;
import com.dist.bdf.common.conf.imgserver.ImgServerConf;
import com.dist.bdf.model.dto.dcm.FileContentLocalDTO;
import com.dist.bdf.model.dto.ldap.LdapPerson;
import com.dist.bdf.model.dto.sga.ImgInfo;
import com.dist.bdf.model.dto.sga.UserResponseDTO;
import com.dist.bdf.model.dto.system.DepartmentDTO;
import com.dist.bdf.model.dto.system.InstituteDTO;
import com.dist.bdf.model.dto.system.Org2UsersDTO;
import com.dist.bdf.model.dto.system.OrgBasicInfoUpdateDTO;
import com.dist.bdf.model.dto.system.OrgDelRequestDTO;
import com.dist.bdf.model.dto.system.OrgPositionAddDTO;
import com.dist.bdf.model.dto.system.OrgTeamAddDTO;
import com.dist.bdf.model.dto.system.OrgTypeAddDTO;
import com.dist.bdf.model.dto.system.OrgUserDTO;
import com.dist.bdf.model.dto.system.page.ParaJsonPageUser;
import com.dist.bdf.model.dto.system.user.BaseUserDTO;
import com.dist.bdf.model.dto.system.user.UserAddRequestDTO;
import com.dist.bdf.model.dto.system.user.UserArticleInfoDTO;
import com.dist.bdf.model.dto.system.user.UserCertificateInfoDTO;
import com.dist.bdf.model.dto.system.user.UserChangePwdDTO;
import com.dist.bdf.model.dto.system.user.UserDTO;
import com.dist.bdf.model.dto.system.user.UserDelRequestDTO;
import com.dist.bdf.model.dto.system.user.UserEducationDTO;
import com.dist.bdf.model.dto.system.user.UserLanguageDTO;
import com.dist.bdf.model.dto.system.user.UserLoginDTO;
import com.dist.bdf.model.dto.system.user.UserOtherInfoDTO;
import com.dist.bdf.model.dto.system.user.UserPersonUpdateRequestDTO;
import com.dist.bdf.model.dto.system.user.UserPrjExperienceDTO;
import com.dist.bdf.model.dto.system.user.UserSimpleDTO;
import com.dist.bdf.model.dto.system.user.UserTitleInfoDTO;
import com.dist.bdf.model.dto.system.user.UserTrainingDTO;
import com.dist.bdf.model.dto.system.user.UserUpdateRequestDTO;
import com.dist.bdf.model.dto.system.user.UserWorkExperienceDTO;
import com.dist.bdf.model.entity.sga.SgaCompany;
import com.dist.bdf.model.entity.system.DcmOrganization;
import com.dist.bdf.model.entity.system.DcmUser;
import com.google.common.base.Objects;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.dist.bdf.facade.service.UserOrgService;
import com.dist.bdf.facade.service.sga.SgaCompanyService;
import com.dist.bdf.facade.service.sga.SgaUserService;

/**
 * 
 * @author weifj
 * @version 1.0，2016/02/18，weifj，创建控制器
 * @version 1.1，2016/03/15，weifj，修改控制类@RestController，需要springMVC4.0支持
 * @RequestMapping(value = "sysservice",produces =
 *                       "text/plain;charset=UTF-8")其中不能带上produces =
 *                       "text/plain;charset=UTF-8"，
 *                       否则使用@RestController的时候，调接口抛出异常： 1）页面上：The resource
 *                       identified by this request is only capable of
 *                       generating responses with characteristics not
 *                       acceptable according to the request "accept" headers；
 *                       2）tomcat控制台： Could not find acceptable representation
 */
@Api(tags={"API-用户和组织机构服务模块"}, description = "UserController")
@RestController
@RequestMapping(value = "/rest/sysservice/")
//@CrossOrigin(origins = "*")
@Scope("prototype")
public class UserController extends BaseController {

	@Autowired
	private UserOrgService userService;
	@Autowired
	private SgaUserService sgaUserService;
	@Autowired
	private SgaCompanyService sgaCompanyService;
	@Autowired
	private ImgServerConf imgSerConf;
/*	@Autowired
	private FileStorageDao fileStorageDao;
*/
	// <=====用户机构管理模块begin

	/*@Deprecated
	@ApiOperation(value = "user/add", notes = "添加用户")
	@RequestMapping(value = "user/add/{parajson:.+}", method = {RequestMethod.POST, RequestMethod.GET})
	public Result addUser(
			@ApiParam(value = "用户的JSON格式信息")
			@PathVariable String parajson) throws Exception {

		UserDTO userDto = (UserDTO) JSONUtil.toObject(parajson, UserDTO.class);
		DcmUser user = this.userService.addUser(userDto);

		return super.successResult(user);
	}
	*/
	@Deprecated
	@ApiOperation(value = "添加用户，需要指定用户所在的机构和角色。（设计需优化，user->users）", notes = "addUser")
	@RequestMapping(value = "/user", method = {RequestMethod.POST})
	public Result addUser(
			@ApiParam(value = "用户的JSON格式信息")
			@Valid
			@RequestBody UserAddRequestDTO userDto, BindingResult result) throws Exception {

		if(result.hasErrors())
			return super.failResult(result.toString());
		
		return super.successResult(this.userService.addUser(super.getContextPath(), super.getBaseURL(), userDto));
	}
	@ApiOperation(value = "添加用户，需要指定用户所在的机构和角色。", notes = "addUserEx")
	@RequestMapping(value = "/users", method = {RequestMethod.POST})
	public Result addUserEx(
			@ApiParam(value = "用户的JSON格式信息")
			@Valid
			@RequestBody UserAddRequestDTO userDto, BindingResult result) throws Exception {

		if(result.hasErrors())
			return super.failResult(result.toString());
		
		return super.successResult(this.userService.addUserEx(super.getContextPath(), super.getBaseURL(), userDto));
	}

	/**
	 * 获取用户列表
	 * 
	 * @param parajson
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "获取用户列表", response = Result.class, notes="listAllUsers")
	@RequestMapping(value = "listAllUsers", method = {RequestMethod.GET})
	public Result listAllUsers() throws Exception {

		//super.request.getScheme() + "://" + super.request.getServerName() + ":"+ super.request.getServerPort() + super.request.getContextPath();

		List<UserDTO> list = this.userService.listAllUsers(super.getContextPath(request), super.getBaseURL(request));

		return super.successResult(list);
	}

	@ApiOperation(value = "获取简单用户信息", notes = "listSimpleUsers")
	@RequestMapping(value = "listSimpleUsers", method = RequestMethod.GET)
	public Result listSimpleUsers(){

		String baseURL = super.request.getScheme() + "://" + super.request.getServerName() + ":"
				+ super.request.getServerPort() + super.request.getContextPath();

		List<UserSimpleDTO> list = this.userService
				.listSimpleUsers(super.request.getServletContext().getRealPath("/"), baseURL);

		return super.successResult(list);
	}

	@ApiOperation(value = "根据域，获取简单用户列表", response = Result.class, notes="listSimpleUsers")
	@RequestMapping(value = "/listSimpleUsers/{realm}", method = {RequestMethod.GET})
	public Result listSimpleUsers(@PathVariable String realm) {

		List<UserSimpleDTO> list = this.userService.listSimpleUsers(super.getContextPath(request), super.getBaseURL(request), realm);

		return super.successResult(list);
	}
	@Deprecated
	@ApiOperation(value = "根据域，获取简单用户列表，包括体系内和体系外的人员（效率慢，切换到/v1/users/all）", response = Result.class, notes="listSimpleInnerAndExternalUsersByRealm")
	@RequestMapping(value = "/users/all/{realm}", method = {RequestMethod.GET})
	public Result listSimpleInnerAndExternalUsersByRealm(@PathVariable String realm) {

		Date begin = new Date();
		
		List<DcmUser> users = this.userService.listAllUsers(realm);
		Date end = new Date();
		LOG.info(">>>缓存消耗：{} ms", end.getTime() - begin.getTime());

		String baseURL = super.getBaseURL();
		baseURL = baseURL.endsWith("/") ? baseURL : (baseURL + "/");
		String contextPath = super.getContextPath();

		List<UserSimpleDTO> newUsers = new ArrayList<UserSimpleDTO>();
		UserSimpleDTO newU = null;
		for (DcmUser u : users) {

			if (1 != u.getCurrentStatus())
				continue;

			newU = new UserSimpleDTO();
			newU.setId(u.getId());
			newU.setLoginName(u.getLoginName());
			newU.setUserName(u.getUserName());
			newU.setSex(u.getSex());
			newU.setDn(u.getDn());
			newU.setUserCode(u.getUserCode());
			Date tempBegin = new Date();
			newU.setAvatar(this.userService.getUserAvatarLink(this.imgSerConf.getServerURI(), contextPath, baseURL, u));
			LOG.info(">>>单个头像消耗：{} ms", new Date().getTime() - tempBegin.getTime());
			newUsers.add(newU);
		}
		end = new Date();
		LOG.info(">>>获取用户头像消耗：{} ms", end.getTime() - begin.getTime());
		try {
			// 公众版注册用户
			List<UserSimpleDTO> sgaUsers = this.sgaUserService.listValidUsersByRealmPublic(realm);
			if(sgaUsers != null && !sgaUsers.isEmpty()) {
				newUsers.addAll(sgaUsers);
			}
		} catch (Exception e) {
			LOG.error(">>>获取公众版用户信息失败，详情：{}", e.getMessage(), e);
		}

		return super.successResult(newUsers);
	}
	@ApiOperation(value = "根据域，获取简单用户列表，包括体系内和体系外的人员", response = Result.class, notes="listSimpleInnerAndExternalUsersByRealm")
	@RequestMapping(value = "/v1/users/all/{realm}", method = {RequestMethod.GET})
	public Result listSimpleInnerAndExternalUsersByRealmEx(@PathVariable String realm) {

		Date begin = new Date();
		
		List<UserSimpleDTO> newUsers = this.userService.listSimpleUsers(getContextPath(), getBaseURL(), realm);
		Date end = new Date();
		LOG.info(">>>缓存消耗：{} ms", end.getTime() - begin.getTime());

		try {
			// 公众版注册用户
			List<UserSimpleDTO> sgaUsers = this.sgaUserService.listValidUsersByRealmPublic(realm);
			if(sgaUsers != null && !sgaUsers.isEmpty()) {
				newUsers.addAll(sgaUsers);
			}
			LOG.info(">>>外部用户消耗：{} ms", new Date().getTime() - end.getTime());
		} catch (Exception e) {
			LOG.error(">>>获取公众版用户信息失败，详情：{}", e.getMessage(), e);
		}

		return super.successResult(newUsers);
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "根据用户编码，获取所在企业下的简单用户列表，包括体系内和体系外的人员", response = Result.class, notes="listSimpleInnerAndExternalUsersByUserCode")
	@RequestMapping(value = "/users/company/{userCode}", method = {RequestMethod.GET})
	public Result listSimpleInnerAndExternalUsersByUserCode(@PathVariable String userCode) {

		List<SgaCompany> companies = this.sgaCompanyService.getComInfoByUserCode(userCode);
		if(null == companies || companies.isEmpty()) {
			return super.failResult("没有找到用户相关企业信息", "无法获取到用户列表");
		}
		List<UserSimpleDTO> newUsers = new ArrayList<UserSimpleDTO>();
		for(SgaCompany company : companies) {
			Result result = this.listSimpleInnerAndExternalUsersByRealm(company.getRealm());
			if(Objects.equal(Result.SUCCESS, result.getStatus())) {
				newUsers.addAll((List<UserSimpleDTO> ) result.getData());
			} else {
				LOG.info(">>>获取域[{}]用户列表失败", company.getShortName());
			}
		}
	
		return super.successResult(newUsers);
	}
	
	@ApiOperation(value = "根据机构域和登录名获取个人的dn（ldap）", notes = "getUserDNByRealmLoginname")
	@RequestMapping(value = "user/dn/{realm}/{loginName}", method = RequestMethod.GET)
	public Result getUserDNByRealmLoginname(@ApiParam(value = "机构域，例如：数慧，dist") @PathVariable String realm,
			@ApiParam(value = "登录名") @PathVariable String loginName) {

		LOG.info(">>>realm : " + realm);
		LOG.info(">>>loginName : " + loginName);
		BaseUserDTO userDTO = this.userService.getUserDNByRealmLoginname(realm, loginName);
		// 记录用户登录状态信息，但信息不完整
		if (userDTO != null) {
			UserResponseDTO userSession = new UserResponseDTO();
			userSession.setUserName(userDTO.getUserName());
			userSession.setSysCode(userDTO.getUserCode());
			userSession.setLoginName(userDTO.getLoginName());
			userSession.setId(userDTO.getId());
			userSession.setStatus(1);
			if (super.session != null) {
				session.setAttribute(SessionContants.USER_KEY_2B, userSession.getUserCode());
			}
		} else {
			if (super.session != null) {
				session.removeAttribute(SessionContants.USER_KEY_2B);
			}
			throw new BusinessException("登录失败");
		}
		return super.successResult(userDTO);
	}
	@ApiOperation(value = "根据登录名获取个人详细信息", notes = "getUserByLoginName")
	@RequestMapping(value = "getUserByLoginName/{loginName}", method = RequestMethod.GET)
	public Result getUserByLoginName(
			@ApiParam(value = "登录名")
			@PathVariable String loginName) {
		
/*		String baseURL = super.request.getScheme() + "://" + super.request.getServerName() + ":"
				+ super.request.getServerPort() + super.request.getContextPath();*/
		UserDTO dto = this.userService.getUserByLoginNameEx(super.getContextPath("/"),
				super.getBaseURL(), loginName, HttpRequestHelper.getIpAddr(request));

		return super.successResult(dto);
	}
	
	@ApiOperation(value = "根据用户编码获取个人详细信息", notes = "getUserByCode")
	@RequestMapping(value = "/users/{code}", method = RequestMethod.GET)
	public Result getUserByCode (
			@ApiParam(value = "用户编码")
			@PathVariable String code) {
		
/*		String baseURL = super.request.getScheme() + "://" + super.request.getServerName() + ":"
				+ super.request.getServerPort() + super.request.getContextPath();*/
		UserDTO dto = this.userService.getUserByCode(super.getContextPath("/"),
				super.getBaseURL(), code, HttpRequestHelper.getIpAddr(request));

		return super.successResult(dto);
	}
	
	@Deprecated
	@ApiOperation(value = "根据登录名获取个人详细信息", notes = "getUserByLoginName")
	@RequestMapping(value = "user/info/{realm}/{loginName}", method = RequestMethod.GET)
	public Result getUserByLoginName(
			@ApiParam(value = "机构域，例如：数慧，dist")
			@PathVariable String realm,
			@ApiParam(value = "登录名")
			@PathVariable String loginName) {
		
/*		String baseURL = super.request.getScheme() + "://" + super.request.getServerName() + ":"
				+ super.request.getServerPort() + super.request.getContextPath();*/
		UserDTO dto = this.userService.getUserByLoginName(super.getContextPath("/"),
				super.getBaseURL(), realm, loginName);

		return super.successResult(dto);
	}
	@ApiOperation(value = "根据登录名获取个人基本信息，用户查看和编辑。", notes = "getUserBasicInfoByGuid")
	@RequestMapping(value = "user/basicinfo/{code}", method = RequestMethod.GET)
	public Result getUserBasicInfoByGuid(@PathVariable String code){
		
		return super.successResult(this.userService.getUserBasicInfoByCode(code));
	}
	
	@Deprecated
	@ApiOperation(value = "分页查询用户信息", notes = "pageUsers")
	@RequestMapping(value = "pageUsers/{parajson:.+}", method = RequestMethod.GET)
	public Result pageUsers(
			@ApiParam(value = "分页设置信息")
			@PathVariable String parajson) throws Exception {

		ParaJsonPageUser pageParams = JSONObject.parseObject(parajson, ParaJsonPageUser.class);
		Pagination page = this.userService.pageUsers(pageParams.getPagepara(), pageParams.getQueryinfo());
		return super.successResult(page);
		
	}

	@Deprecated
	@ApiOperation(value="根据用户id集合删除信息。（禁止使用）",notes="delUsers")
	@RequestMapping(value = "delUsers/{parajson:.+}", method = {RequestMethod.GET, RequestMethod.POST})
	public Result delUsers(@PathVariable String parajson) throws Exception {

		Object[] userIds = JSONUtil.toArray(parajson, Long.class);

		this.userService.delUsers(userIds);
		return super.successResult("成功删除用户");
	}
	
	@ApiOperation(value="根据机构域和用户id集合删除信息",notes="delUsersEx")
	@RequestMapping(value = "/user", method = {RequestMethod.DELETE})
	public Result delUsersEx(
			@ApiParam( value = "用户的定位信息")
			@RequestBody UserDelRequestDTO dto) throws Exception {

		return super.successResult(this.userService.delUsers(dto));
	}

	@ApiOperation(value="添加院机构",notes="addInstitute")
	@RequestMapping(value = "addInstitute/{parajson:.+}", method = RequestMethod.POST)
	public Result addInstitute(@PathVariable String parajson) throws Exception {

		InstituteDTO dto = JSONObject.parseObject(parajson, InstituteDTO.class);

		DcmOrganization org = this.userService.addOrg(dto);
		return super.successResult(org);
	}

	@ApiOperation(value="添加所机构",notes="addDepartment")
	@RequestMapping(value = "/depart", method = RequestMethod.POST)
	public Result addDepartment(
			@ApiParam( value = "机构信息。")
			@Valid
			@RequestBody DepartmentDTO dto, BindingResult result) throws Exception {

		//DepartmentDTO dto = (DepartmentDTO) JSONUtil.toObject(parajson, DepartmentDTO.class);

		if(result.hasErrors())
			return super.errorResult(result.toString());
		
		return super.successResult(this.userService.addOrg(dto));
	}
	@ApiOperation(value="修改机构基本信息",notes="updateOrgBasicInfo")
	@RequestMapping(value = "/v1/depart", method={RequestMethod.PUT})
	public Result updateOrgBasicInfo(@RequestBody OrgBasicInfoUpdateDTO dto) {
		return super.successResult(this.userService.updateOrgBasicInfo(dto));
	}
	@ApiOperation(value="删除域下的所机构",notes="deleteOrg")
	@RequestMapping(value = "/org", method = {RequestMethod.DELETE, RequestMethod.POST})
	public Result deleteOrg(
			@ApiParam( value = "机构信息。")
			@Valid
			@RequestBody OrgDelRequestDTO dto) throws Exception {

		this.userService.deleteOrg(dto);
		return super.successResult(true);
	}

	@Deprecated
	@ApiOperation(value="获取机构列表，包括院和所",notes="listOrgs")
	@RequestMapping(value = "listOrgs", method = RequestMethod.GET)
	public Result listOrgs() {

		@SuppressWarnings("unchecked")
		Collection<DcmOrganization> orgs = (Collection<DcmOrganization>) this.userService.listOrgsInCache();
		return super.successResult(orgs);
	}
	@ApiOperation(value="listOrgs/{realm}",notes="根据机构域，获取机构列表，包括院和所。")
	@RequestMapping(value = "listOrgs/{realm}", method = RequestMethod.GET)
	public Result listOrgsByRealm(
			@ApiParam(value = "机构域，如：数慧，dist")
			@PathVariable String realm) {
		
		return super.successResult(this.userService.getDepartmentsAndInstitute(realm));
	}

	@ApiOperation(value="获取院列表",notes="listInstitutes")
	@RequestMapping(value = "org/institutes.get", method = RequestMethod.GET)
	public Result listInstitutes() {

		List<DcmOrganization> orgs = this.userService.listInstitutes();
		return super.successResult(orgs);
	}
	
	@ApiOperation(value="获取院下的部门列表",notes="listDepartmentByInstituteId")
	@RequestMapping(value = "org/departments.get/{id}", method = RequestMethod.GET)
	public Result listDepartmentByInstituteId(
			@PathVariable
			@ApiParam(value = "院的id")
			Long id) {

		return super.successResult( this.userService.listDepartments(id));	
	}
	
	@ApiOperation(value="获取当前用户所在院下的部门列表",notes="listCurrentDepartmentsByLoginName")
	@RequestMapping(value = "org/currdepartments.get/{loginName}", method = RequestMethod.GET)
	public Result listCurrentDepartmentsByLoginName(
			@PathVariable
			@ApiParam(value = "当前用户的登录名")
			String loginName) {

		return super.successResult( this.userService.listCurrentDepartments(loginName));
		
	}
	
	@Deprecated
	@ApiOperation(value="添加一组人到指定机构",notes="addUsersToOrg")
	@RequestMapping(value = "addUsersToOrg/{parajson:.+}", method = RequestMethod.POST)
	public Result addUsersToOrg(@PathVariable String parajson) throws Exception {

		OrgUserDTO dto = JSONObject.parseObject(parajson, OrgUserDTO.class);

		this.userService.addUsersToOrg(dto);
		return super.successResult("成功添加");


	}

	@ApiOperation(value="获取机构下的直接子机构",notes="listDirectChildOrgs")
	@RequestMapping(value = "listDirectChildOrgs/{orgId}", method = RequestMethod.GET)
	public Result listDirectChildOrgs(@PathVariable String orgId) {

		List<DcmOrganization> orgs = this.userService.listDirectChildOrgs(Long.valueOf(orgId));
		return super.successResult(orgs);
		
	}

	@ApiOperation(value="从一个机构删除一组人",notes="delUsersFromOrg")
	@RequestMapping(value = "/depart/user/", method  =RequestMethod.DELETE)
	public Result delUsersFromOrg(
			@ApiParam(value = "机构json信息")
			@Valid
			@RequestBody OrgUserDTO dto, BindingResult result) throws Exception {

		//OrgUserDTO dto = (OrgUserDTO) JSONUtil.toObject(parajson, OrgUserDTO.class);

		if(result.hasErrors())
			return super.errorResult(result.toString());
		
		this.userService.delUsersFromOrg(dto);
		return super.successResult("成功删除");
		
	}

	@ApiOperation(value="获取机构下的直接人员",notes="listDirectUsersOfOrg")
	@RequestMapping(value = "listDirectUsersOfOrg/{orgId}", method = RequestMethod.GET)
	public Result listDirectUsersOfOrg(
			@ApiParam(value = "机构id")
			@PathVariable String orgId) {

		List<UserDTO> users = this.userService.listDirectUsersOfOrg(super.getContextPath("/"),super.getBaseURL(), Long.valueOf(orgId));
		return super.successResult(users);
	}

	@ApiOperation(value="获取机构下所有用户信息",notes="listUsersOfOrg")
	@RequestMapping(value = "listUsersOfOrg/{orgId}", method = RequestMethod.GET)
	public Result listUsersOfOrg(
			@ApiParam(value = "机构id")
			@PathVariable String orgId) {

		List<DcmUser> users = this.userService.listUsersOfOrg(Long.valueOf(orgId));
		return super.successResult(users);
		
	}

	@ApiOperation(value="修改用户的基本信息，应该是提供给超级管理员使用。",notes="modifyUser")
	@RequestMapping(value = "/user/basicInfo", method = {RequestMethod.PUT})
	public Result modifyUser(
			@ApiParam(value = "UserDTO对象，不提供密码修改")
			@Valid
			@RequestBody UserUpdateRequestDTO userDto, BindingResult result) throws Exception {

		if(result.hasErrors())
			return super.failResult("参数验证失败，详情："+result.toString());
		
		return super.successResult(this.userService.updateUserBasicInfo(userDto));
	}
	
	@ApiOperation(value="修改用户的基本信息，提供给超级管理员使用。",notes="modifyUserByAdmin")
	@RequestMapping(value = "/user/basicInfoAdmin", method = {RequestMethod.PUT})
	public Result modifyUserByAdmin(
			@ApiParam(value = "UserDTO对象，提供密码修改。")
			@Valid
			@RequestBody UserUpdateRequestDTO userDto, BindingResult result) throws Exception {

		if(result.hasErrors())
			return super.failResult("参数验证失败，详情："+result.toString());
		
		return super.successResult(this.userService.updateUserBasicInfoByAdmin(userDto));
	}
	@ApiOperation(value="修改用户的基本信息，提供给超级管理员使用。",notes="modifyUserByAdminEx")
	@RequestMapping(value = "/v1/user/basicInfoAdmin", method = {RequestMethod.PUT})
	public Result modifyUserByAdminEx(
			@ApiParam(value = "UserDTO对象，提供密码修改。")
			@Valid
			@RequestBody UserUpdateRequestDTO userDto, BindingResult result) throws Exception {

		if(result.hasErrors())
			return super.failResult("参数验证失败，详情："+result.toString());
		
		return super.successResult(this.userService.updateUserBasicInfoByAdminEx(userDto));
	}
	
	@ApiOperation(value="修改个人的基本信息",notes="modifyUser")
	@RequestMapping(value = "/user/perBasicInfo", method = {RequestMethod.PUT})
	public Result modifyPersonalInfo(
			@ApiParam(value = "UserDTO对象")
			@Valid
			@RequestBody UserPersonUpdateRequestDTO userDto, BindingResult result) throws Exception {

		if(result.hasErrors())
			return super.failResult("参数验证失败，详情："+result.toString());
		
		return super.successResult(this.userService.updatePersonalBasicInfo(userDto));
	}

	@ApiOperation(value="获取机构和用户树状信息",notes="getOrgUserTree")
	@RequestMapping(value = "getOrgUserTree", method = RequestMethod.GET)
	public Result getOrgUserTree() {

		List<Org2UsersDTO> list = this.userService.getOrgUserTree(true);
		return this.successResult(list);
		
	}
	
	@ApiOperation(value="根据域，这里是院的唯一标识名称，获取机构树状信息",notes="getOrgUserTree")
	@RequestMapping(value = "getOrgUserTree/{realm}", method = RequestMethod.GET)
	public Result getOrgUserTree(@PathVariable String realm) {

		List<Org2UsersDTO> list = this.userService.getOrgUserTree(true, realm);
		return this.successResult(list);
		
	}
	
	@ApiOperation(value="获取当前用户所在院的机构和用户树状信息",notes="getCurrOrgUserTree")
	@RequestMapping(value = "getCurrOrgUserTree/{loginName}", method = RequestMethod.GET)
	public Result getCurrOrgUserTree(
			@ApiParam(value = "登录名")
			@PathVariable String loginName) {

		List<Org2UsersDTO> list = this.userService.getCurrOrgUserTree(loginName, true);
		return this.successResult(list);
		
	}

	@Deprecated
	@ApiOperation(value="获取当前用户所在院的机构和用户树状信息。(已过时，切换到/orguserTree/userId)",notes="getCurrOrgUserTreeByUserId")
	@RequestMapping(value = "getCurrOrgUserTreeByUserId/{userId}", method = RequestMethod.GET)
	public Result getCurrOrgUserTreeByUserId(
			@ApiParam(value = "用户序列id")
			@PathVariable Long userId) {

		List<Org2UsersDTO> list = this.userService.getCurrOrgUserTree(userId, true);
		return this.successResult(list);
		
	}
	
	@ApiOperation(value="根据用户序列Id，获取当前用户所在院的机构和用户树状信息",notes="getCurrOrgUserTreeByUserIdEx")
	@RequestMapping(value = "/orguserTree/userId/{userId}", method = RequestMethod.GET)
	public Result getCurrOrgUserTreeByUserIdEx(
			@ApiParam(value = "用户序列id")
			@PathVariable String userId) {

		List<Org2UsersDTO> list = this.userService.getCurrOrgUserTree(Long.parseLong(userId), true);
		return this.successResult(list);
		
	}
	@Deprecated
	@ApiOperation(value="获取机构树状信息",notes="getOrgTree")
	@RequestMapping(value = "getOrgTree", method = RequestMethod.GET)
	public Result getOrgTree() {

		List<Org2UsersDTO> list = this.userService.getOrgUserTree(false);
		return this.successResult(list);
		
	}

	@Deprecated
	@ApiOperation(value="从TDS同步用户信息",notes="syncUserInfoFromLdap")
	@RequestMapping(value = "syncUserInfoFromLdap", method = RequestMethod.GET)
	public Result syncUserInfoFromLdap() {
		Long timeBegin = System.currentTimeMillis();

		this.userService.syncUserInfoFromLdap();

		Long timeEnd = System.currentTimeMillis();
		return this.successResult(String.format("同步完成，总用时：%s ms", (timeEnd - timeBegin)));
	}
	@Deprecated
	@ApiOperation(value="从TDS同步机构和用户信息",notes="syncFromLdap")
	@RequestMapping(value = "syncFromLdap", method = RequestMethod.GET)
	public Result syncFromLdap() {
		
		Long timeBegin = System.currentTimeMillis();

		this.userService.syncFromLdap();

		Long timeEnd = System.currentTimeMillis();
		return this.successResult(String.format("同步完成，总用时：%s ms", (timeEnd - timeBegin)));	
	}
	
	@ApiOperation(value="从TDS同步机构和用户信息",notes="syncFromLdapEx")
	@RequestMapping(value = "tds/sync", method = RequestMethod.GET)
	public Result syncFromLdapEx() {
		
		Long timeBegin = System.currentTimeMillis();

		this.userService.syncFromLdapForCloudEx();

		Long timeEnd = System.currentTimeMillis();
		return this.successResult(String.format("同步完成，总用时：%s ms", (timeEnd - timeBegin)));	
	}
	@ApiOperation(value="根据域，从TDS同步机构和用户信息",notes="syncFromLdapByRealm")
	@RequestMapping(value = "tds/sync/{realm}", method = RequestMethod.GET)
	public Result syncFromLdapByRealm(@PathVariable String realm) {
		
		Long timeBegin = System.currentTimeMillis();

		this.userService.syncFromLdapForCloudEx(realm);

		Long timeEnd = System.currentTimeMillis();
		return this.successResult(String.format("同步完成，总用时：%s ms", (timeEnd - timeBegin)));	
	}
	
	@ApiOperation(value="从系统同步机构和用户信息到TDS",notes="syncToLdapEx")
	@RequestMapping(value = "syncToLdap/{realm}", method = RequestMethod.POST)
	public Result syncToLdapEx(@PathVariable String realm) {
		
		Long timeBegin = System.currentTimeMillis();

		this.userService.syncToLdapEx(realm);

		Long timeEnd = System.currentTimeMillis();
		return this.successResult(String.format("同步完成，总用时：%s ms", (timeEnd - timeBegin)));	
	}
	
	/**
	 * 从ldap中同步机构信息
	 * 
	 * @return
	 */
	/*@ApiOperation(value="syncOrgInfoFromLdap",notes="从TDS同步机构信息。")
	@RequestMapping(value = "syncOrgInfoFromLdap", method = RequestMethod.GET)
	public Result syncOrgInfoFromLdap() {
		try {
			Long timeBegin = System.currentTimeMillis();

			this.userService.syncOrganizationInfoFromLdap();

			Long timeEnd = System.currentTimeMillis();
			return this.successResult(String.format("同步完成，总用时：%s ms", (timeEnd - timeBegin)));
		} catch (Exception ex) {
			return super.failResultException(ex);
		}
	}*/
	
	@Deprecated
	@ApiOperation(value="验证用户信息，并返回用户信息",notes="authenticateUser")
	@RequestMapping(value = "authenticateUser/{userId}/{password}", method=RequestMethod.GET)
	public Result authenticateUser(
			@ApiParam(value="用户登录名")
			@PathVariable String userId, 
			@ApiParam(value="登录密码")
			@PathVariable String password){
		
		UserDTO dto = this.userService.authenticateUser(super.getContextPath(request), super.getBaseURL(request), userId, password);
		
		return super.successResult(dto);
	}
	@Deprecated
	@ApiOperation(value="通过ldap，验证用户信息，并返回用户信息。（已过时，切换到接口user/login/）",notes="authenticateUserFromLDAP")
	@RequestMapping(value = "authenticateUser/ldap/{realm}/{userId}/{password:.*}", method=RequestMethod.GET)
	public Result authenticateUserFromLDAP(
			@ApiParam(value="ldap域，例如：数慧，dist")
			@PathVariable String realm, 
			@ApiParam(value="用户登录名")
			@PathVariable String userId, 
			@ApiParam(value="登录密码")
			@PathVariable String password){
		
		UserDTO dto = this.userService.authenticateUserFromLDAP(super.getContextPath(request), super.getBaseURL(request), 
				realm, userId, password, HttpRequestHelper.getIpAddr(request));
		
		return super.successResult(dto);
	}
	
	@ApiOperation(value="验证用户信息，并返回用户信息",notes="login")
	@RequestMapping(value = "user/login/", method=RequestMethod.POST)
	public Result login(
			@ApiParam(value = "包括三个属性：realm（域），userId（用户id），password（密码）")
			@RequestBody
			UserLoginDTO loginDTO){
		
		UserDTO dto = this.userService.authenticateUserFromLDAP(super.getContextPath(request), super.getBaseURL(request), 
				loginDTO.getRealm(), loginDTO.getUserId(), loginDTO.getPassword(), HttpRequestHelper.getIpAddr(request));
		
		return super.successResult(dto);
	}
	// =====>用户机构管理模块end

	// <=====TDS模块begin

	@Deprecated
	@ApiOperation(value="获取ldap所有用户",notes="getLdapPersons")
	@RequestMapping(value = "getLdapPersons", method = RequestMethod.GET)
	public Result getLdapPersons() {
		
		Collection<LdapPerson> entries = this.userService.getLdapPersons();
		return super.successResult(entries);
	}
	
	@ApiOperation(value="获取ldap所有用户",notes="getLdapPersonsEx")
	@RequestMapping(value = "/ldap/persons", method = RequestMethod.GET)
	public Result getLdapPersonsEx() {
		
		Collection<LdapPerson> entries = this.userService.getLdapPersons();
		return super.successResult(entries);
	}
	
	@ApiOperation(value="获取ldap所有组",notes="getLdapGroups")
	@RequestMapping(value = "getLdapGroups", method = RequestMethod.GET)
   public Result getLdapGroups() {
		
		return super.successResult(this.userService.getLdapGroups());
	}

	@ApiOperation(value="获取ldap中工作组信息",notes="getWorkgroupInfo")
	@RequestMapping(value = "getWorkgroupInfo", method = RequestMethod.GET)
	public Result getWorkgroupInfo(){
		
		return super.successResult(this.userService.getWorkgroupInfo());
	}
	@ApiOperation(value="根据域，工作组名称获取ldap中工作组信息",notes="getWorkgroupInfo")
	@RequestMapping(value = "workgroupInfo/{realm}/{wgName}", method = RequestMethod.GET)
	public Result getWorkgroupInfo(
			@ApiParam(value = "ldap域，这里是指ObjectClass=organization的类型") 
			@PathVariable String realm,
			@ApiParam(value = "工作组名称") 
			@PathVariable String wgName) {

		return super.successResult(this.userService.getWorkgroupInfo(realm, wgName));
	}
	// ======>TDS模块end
	
	
	@ApiOperation(value="获取用户的详细信息",notes="getUserDetailInfo")
	@RequestMapping(value = "user/detail.get/{id}", method = RequestMethod.GET)
	public Result getUserDetailInfo(
			@ApiParam(value="用户主键ID")
			@PathVariable 
			Long id){
		
		return super.successResult(this.userService.getUserDetailById(id));
	}
	
/*	@ApiOperation(value="user/detail.update",notes="更新用户的基本信息和详细信息。")
	@RequestMapping(value = "user/detail.update/{parajson}", method = {RequestMethod.PUT, RequestMethod.POST})
	public Result updateUserDetailInfo(
			@ApiParam(value="需要更新的用户信息json，包括基本信息和详细信息。")
			@PathVariable 
			String parajson) throws Exception{
		
		UserDetailRequestDTO requestDTO = (UserDetailRequestDTO) JSONUtil.toObject(parajson, UserDetailRequestDTO.class);
		return super.successResult(this.userService.updateUserDetail(requestDTO));
	}*/
	
	@ApiOperation(value="刷新用户和机构的缓存信息",notes="refreshUserOrgCacheInfos")
	@RequestMapping(value = "user/cache.refresh", method = {RequestMethod.PUT, RequestMethod.GET})
	public Result refreshUserOrgCacheInfos() {
		
		this.userService.refreshUserOrgCacheInfos();

		return super.successResult("刷新完成。");
		
	}
	
	@ApiOperation(value="保存用户的学术成果信息",notes="saveUserArticleInfo")
	@RequestMapping(value = "user/detail/articleInfo.save/{parajson}", method = {RequestMethod.POST, RequestMethod.GET})
	public Result saveUserArticleInfo(
			@ApiParam(value="需要更新的用户学术成果json，包括基本信息和附件信息。")
			@PathVariable 
			String parajson) throws Exception{
		
		UserArticleInfoDTO requestDTO = (UserArticleInfoDTO) JSONUtil.toObject(parajson, UserArticleInfoDTO.class);
		
		return super.successResult(this.userService.saveUserDetail("ar", requestDTO));
	}
	
	@ApiOperation(value="保存用户的执业资格信息",notes="saveUserCertificateInfo")
	@RequestMapping(value = "user/detail/cerInfo.save/{parajson}", method = {RequestMethod.POST, RequestMethod.GET})
	public Result saveUserCertificateInfo(
			@ApiParam(value="需要更新的用户执业资格json，包括基本信息和附件信息。")
			@PathVariable 
			String parajson) throws Exception{
		
		UserCertificateInfoDTO requestDTO = (UserCertificateInfoDTO) JSONUtil.toObject(parajson, UserCertificateInfoDTO.class);
		
		return super.successResult(this.userService.saveUserDetail("cer", requestDTO));
	}
	
	@ApiOperation(value="保存用户的教育信息",notes="saveUserEducation")
	@RequestMapping(value = "user/detail/education.save/{parajson}", method = {RequestMethod.POST, RequestMethod.GET})
	public Result saveUserEducation(
			@ApiParam(value="需要更新的用户教育json，包括基本信息和附件信息。")
			@PathVariable 
			String parajson) throws Exception{
		
		UserEducationDTO requestDTO = (UserEducationDTO) JSONUtil.toObject(parajson, UserEducationDTO.class);
		
		return super.successResult(this.userService.saveUserDetail("edu", requestDTO));
	}
	
	@ApiOperation(value="保存用户的工作经历信息",notes="saveUserWorkExperience")
	@RequestMapping(value = "user/detail/experience.work.save/{parajson}", method = {RequestMethod.POST, RequestMethod.GET})
	public Result saveUserWorkExperience(
			@ApiParam(value="添加用户工作经历json，包括基本信息和附件信息。")
			@PathVariable 
			String parajson) throws Exception{
		
		UserWorkExperienceDTO requestDTO = (UserWorkExperienceDTO) JSONUtil.toObject(parajson, UserWorkExperienceDTO.class);
		
		return super.successResult(this.userService.saveUserDetail("wexp", requestDTO));
	}
	
	/**
	 * 注意：需要配置produces显示设置返回类型json
	 * 否则：The resource identified by this request is only capable of generating responses with characteristics
	 * @param parajson
	 * @return
	 * @throws Exception
	 */
/*	@ApiOperation(value="user/detail/experience.work.save.post",notes="保存用户的工作经历信息。")
	@RequestMapping(value = "user/detail/experience.work.save.post", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
	public Result saveUserWorkExperience2(
			@RequestBody
			@ApiParam(value="添加用户工作经历json，包括基本信息和附件信息。")
			String parajson) throws Exception{
		
	
		return super.successResult(parajson);
	}
	*/
	
	@ApiOperation(value="保存用户的项目经历信息",notes="saveUserPrjExperience")
	@RequestMapping(value = "user/detail/experience.prj.save/{parajson}", method = {RequestMethod.POST, RequestMethod.GET})
	public Result saveUserPrjExperience(
			@ApiParam(value="添加用户项目经历json，包括基本信息和附件信息。")
			@PathVariable 
			String parajson) throws Exception{
		
		UserPrjExperienceDTO requestDTO = (UserPrjExperienceDTO) JSONUtil.toObject(parajson, UserPrjExperienceDTO.class);
		
		return super.successResult(this.userService.saveUserDetail("pexp", requestDTO));
	}
	
	
	
	@ApiOperation(value="保存用户的语言等级信息",notes="saveUserLanguage")
	@RequestMapping(value = "user/detail/language.save/{parajson}", method = {RequestMethod.POST, RequestMethod.GET})
	public Result saveUserLanguage(
			@ApiParam(value="需要更新的用户语言等级json，包括基本信息和附件信息。")
			@PathVariable 
			String parajson) throws Exception {
		
		UserLanguageDTO requestDTO = (UserLanguageDTO) JSONUtil.toObject(parajson, UserLanguageDTO.class);
		
		return super.successResult(this.userService.saveUserDetail("lang", requestDTO));
	}
	
	@ApiOperation(value="保存用户的职称信息",notes="saveUserTitleInfos")
	@RequestMapping(value = "user/detail/title.save/{parajson}", method = {RequestMethod.POST, RequestMethod.GET})
	public Result saveUserTitleInfos(
			@ApiParam(value="需要更新的用户职称信息json，包括基本信息和附件信息。")
			@PathVariable 
			String parajson) throws Exception{
		
		UserTitleInfoDTO requestDTO = (UserTitleInfoDTO) JSONUtil.toObject(parajson, UserTitleInfoDTO.class);
		
		return super.successResult(this.userService.saveUserDetail("ti", requestDTO));
	}
	
	@ApiOperation(value="保存用户的培训经历信息",notes="saveUserTraining")
	@RequestMapping(value = "user/detail/training.save/{parajson}", method = {RequestMethod.POST, RequestMethod.GET})
	public Result saveUserTraining(
			@ApiParam(value="需要更新的用户培训经历信息json，包括基本信息和附件信息。")
			@PathVariable 
			String parajson) throws Exception{
		
		UserTrainingDTO requestDTO = (UserTrainingDTO) JSONUtil.toObject(parajson, UserTrainingDTO.class);
		
		return super.successResult(this.userService.saveUserDetail("tr", requestDTO));
	}
	
	@ApiOperation(value="保存用户的其它附件信息",notes="saveUserOther")
	@RequestMapping(value = "user/detail/other.save/{parajson}", method = {RequestMethod.POST, RequestMethod.GET})
	public Result saveUserOther(
			@ApiParam(value="需要更新的用户其它附件信息json，包括基本信息和附件信息。")
			@PathVariable 
			String parajson) throws Exception{
		
		UserOtherInfoDTO requestDTO = (UserOtherInfoDTO) JSONUtil.toObject(parajson, UserOtherInfoDTO.class);
		
		return super.successResult(this.userService.saveUserDetail("o", requestDTO));
	}
	
	@ApiOperation(value="删除用户的学术成果信息",notes="deleteUserArticleInfo")
	@RequestMapping(value = "user/detail/articleInfo.del/{refId}", method = {RequestMethod.DELETE, RequestMethod.GET})
	public Result deleteUserArticleInfo(
			@ApiParam(value="关联id，这里指用户主键id。")
			@PathVariable 
			String refId) throws Exception{
		
		return super.successResult(this.userService.deleteUserDetail("ar", Long.valueOf(refId)));
	}
	
	@ApiOperation(value="删除用户的执业资格信息",notes="deleteUserCertificateInfo")
	@RequestMapping(value = "user/detail/cerInfo.del/{refId}", method = {RequestMethod.DELETE, RequestMethod.GET})
	public Result deleteUserCertificateInfo(
			@ApiParam(value="关联id，这里指用户主键id。")
			@PathVariable 
			String refId) throws Exception{
		
		return super.successResult(this.userService.deleteUserDetail("cer", Long.valueOf(refId)));

	}
	
	@ApiOperation(value="删除用户的教育信息",notes="deleteUserEducation")
	@RequestMapping(value = "user/detail/education.del/{refId}", method = {RequestMethod.DELETE, RequestMethod.GET})
	public Result deleteUserEducation(
			@ApiParam(value="关联id，这里指用户主键id。")
			@PathVariable 
			String refId) throws Exception{
		
		return super.successResult(this.userService.deleteUserDetail("edu", Long.valueOf(refId)));
	}
	
	@ApiOperation(value="删除用户的工作经历和项目经历信息",notes="deleteUserWExperience")
	@RequestMapping(value = "user/detail/wexperience.del/{refId}", method = {RequestMethod.DELETE, RequestMethod.GET})
	public Result deleteUserWExperience(
			@ApiParam(value="关联id，这里指用户主键id。")
			@PathVariable 
			String refId) throws Exception{
		
		return super.successResult(this.userService.deleteUserDetail("wexp", Long.valueOf(refId)));

	}
	
	@ApiOperation(value="删除用户的项目经历信息",notes="deleteUserPExperience")
	@RequestMapping(value = "user/detail/pexperience.del/{refId}", method = {RequestMethod.DELETE, RequestMethod.GET})
	public Result deleteUserPExperience(
			@ApiParam(value="关联id，这里指工作实体主键id。")
			@PathVariable 
			String refId) throws Exception{
		
		return super.successResult(this.userService.deleteUserDetail("pexp", Long.valueOf(refId)));

	}
	
	
	@ApiOperation(value="删除用户的语言等级信息",notes="deleteUserLanguage")
	@RequestMapping(value = "user/detail/language.del/{refId}", method = {RequestMethod.DELETE, RequestMethod.GET})
	public Result deleteUserLanguage(
			@ApiParam(value="关联id，这里指用户主键id。")
			@PathVariable 
			String refId) throws Exception{
		
		return super.successResult(this.userService.deleteUserDetail("lang", Long.valueOf(refId)));

	}
	
	@ApiOperation(value="删除用户的职称信息",notes="deleteUserTitleInfos")
	@RequestMapping(value = "user/detail/title.del/{refId}", method = {RequestMethod.DELETE, RequestMethod.GET})
	public Result deleteUserTitleInfos(
			@ApiParam(value="关联id，这里指用户主键id。")
			@PathVariable 
			String refId) throws Exception{
		
		return super.successResult(this.userService.deleteUserDetail("ti", Long.valueOf(refId)));

	}
	
	@ApiOperation(value="删除用户的培训经历信息",notes="deleteUserTraining")
	@RequestMapping(value = "user/detail/training.del/{refId}", method = {RequestMethod.DELETE, RequestMethod.GET})
	public Result deleteUserTraining(
			@ApiParam(value="关联id，这里指用户主键id。")
			@PathVariable 
			String refId) throws Exception{
		
		return super.successResult(this.userService.deleteUserDetail("tr", Long.valueOf(refId)));

	}
	
	@ApiOperation(value="删除用户的其它附件信息",notes="deleteUserOther")
	@RequestMapping(value = "user/detail/other.del/{refId}", method = {RequestMethod.DELETE, RequestMethod.GET})
	public Result deleteUserOther(
			@ApiParam(value="关联id，这里指用户主键id。")
			@PathVariable 
			String refId) throws Exception{
		
		return super.successResult(this.userService.deleteUserDetail("o", Long.valueOf(refId)));

	}
	
	@ApiOperation(value="根据机构编码获取机构信息",notes="getOrgByCodeInCache")
	@RequestMapping(value = "org/info/code.get/{parajson}", method = {RequestMethod.GET})
	public Result getOrgByCodeInCache(@PathVariable String parajson) throws Exception {
		
		Object[] codes = (Object[]) JSONUtil.toArray(parajson, String.class);
		
		return super.successResult(this.userService.getOrgByCodeInCache(codes));
	}
	
	@ApiOperation(value="检测新增的用户登录名是否合法。true：合法；false：非法",notes="checkUserUniqueLoginName")
	@RequestMapping(value = "user/new.loginName.valid/{loginName}", method = {RequestMethod.GET})
	public Result checkUserUniqueLoginName(
			@PathVariable
			@ApiParam(value = "登录名")
			String loginName) {
		
		return super.successResult(this.userService.checkNewUserLoginNameValid(loginName));
	}
	
	@ApiOperation(value="通过ldap，检测新增的用户登录名是否合法。true：合法；false：非法",notes="checkUserUniqueLoginName")
	@RequestMapping(value = "user/new.loginName.valid/ldap/{orgRealm}/{loginName}", method = {RequestMethod.GET})
	public Result checkUserUniqueLoginName(
			@PathVariable
			@ApiParam(value = "机构域，例如：数慧，dist")
			String orgRealm,
			@PathVariable
			@ApiParam(value = "登录名")
			String loginName) {
		
		return super.successResult(this.userService.checkNewUserLoginNameValid(orgRealm, loginName));
	}

	/*@ApiOperation(value="cache/key.add",notes="测试分布式缓存，添加缓存key。")
	@RequestMapping(value = "cache/key.add/{key}/{value}", method = {RequestMethod.POST})
	public Result cache(@PathVariable String key, @PathVariable String value) {
		
		return super.successResult(this.userService.addCache(key, value));
		
	}
	*/
	/*@ApiOperation(value="cache/key.get",notes="测试分布式缓存，获取缓存key。")
	@RequestMapping(value = "cache/key.get/{key}", method = {RequestMethod.GET})
	public Result getCache(@PathVariable String key) {
		
		return super.successResult(this.userService.getCache(key));
		
	}*/
	
	/*@ApiOperation(value="cache/key.del",notes="测试分布式缓存，删除缓存key。")
	@RequestMapping(value = "cache/key.del/{key}", method = {RequestMethod.DELETE})
	public Result delCache(@PathVariable String key) {
		
		this.userService.deleteCache(key);
		return super.successResult("成功删除");
		
	}*/
	
/*	@ApiOperation(value="user/pwd.set",notes="changeUserPwd，修改用户密码。")
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "userId", value = "用户序列id", required = true, paramType = "query"),
		@ApiImplicitParam(name = "oldPwd", value = "原来的密码", required = true, paramType = "query"),
		@ApiImplicitParam(name = "newPwd", value = "新的密码", required = true, paramType = "query")
	})
	@RequestMapping(value = "user/pwd.set", method = {RequestMethod.PUT, RequestMethod.OPTIONS})
	public Result changeUserPwd(@RequestParam String userId, @RequestParam String oldPwd, @RequestParam String newPwd) {

		return super.successResult(this.userService.changeUserPwd(userId, oldPwd, newPwd));
	}*/
	
	@ApiOperation(value="修改用户密码",notes="changeUserPwd")
	@RequestMapping(value = "/user/pwd/", method = {RequestMethod.PUT})
	public Result changeUserPwd(
			@ApiParam(required = true, value = "UserChangePwdDTO，userId：用户序列id；oldPwd：原来的密码；newPwd：新的密码")
			@RequestBody UserChangePwdDTO dto) {

		
		return super.successResult(this.userService.changeUserPwd(dto.getUserId(), dto.getOldPwd(), dto.getNewPwd()));

	}
	
	@ApiOperation(value="登出系统",notes="logout")
	@RequestMapping(value = "user/logout/{userCode}", method=RequestMethod.GET)
	public Result logout(
			@ApiParam(value = "用户编码")
			@PathVariable String userCode) {
		
		return super.successResult(this.userService.logout(userCode, HttpRequestHelper.getIpAddr(request)));
	}
	
	@ApiOperation(value = "设置用户头像，并存放到mongodb去", notes = "uploadUserAvatarExNew")
	@RequestMapping(value = "/user/avatar/mongo", method = { RequestMethod.POST, RequestMethod.PUT })
	public Result uploadUserAvatarExNew(
			@ApiParam( value = "json数据，{\"id\":用户编码,\"type\":\"image/jpeg\",\"suffix\":\"jpg\",\"content\":\"base64值\"}")
			@RequestBody ImgInfo imgInfo) {
		
		try {
		
			LOG.info(">>>删除mongo file，code："+imgInfo.getId());
			this.userService.deleteAvatarInMongoByUserCode(imgInfo.getId());
			
			LOG.info(">>>userId:[{}], mime-type:[{}], suffix:[{}] ", imgInfo.getId(), imgInfo.getType(), imgInfo.getSuffix());
	    	String mogoFileId = this.userService.storeAvatarToMongo(imgInfo);//fileStorageDao.store(FileUtil.base64ToInputStream(imgInfo.getContent()) , newFileName, imgInfo.getType(), metaData);
	    	LOG.info(">>>mongo file id : "+ mogoFileId);

			String avatarURL = this.userService.updateUserAvatarEx(imgInfo.getId(), super.getBaseURL(), "rest/sysservice/user/avatar/fs/"+mogoFileId);
			System.out.println(">>>avatar url：" + avatarURL);
			return super.successResult(avatarURL);
			
		} catch (Exception ex) {
			LOG.error(">>>设置头像失败，详情：{}", ex.getMessage());
			return super.failResult("头像设置失败，详情："+ex.getMessage(), "头像设置失败");
		}
	}
	
	@ApiOperation(value = "浏览用户头像", response = Result.class, notes = "viewUserAvatar")
	@RequestMapping(value = "/user/avatar/fs/{id}", method = { RequestMethod.GET })
	public void viewUserAvatar(@PathVariable String id) {

		/*GridFSDBFile file = fileStorageDao.getById(id);
		if(null == file)
			throw new BusinessException("用户头像不存在");
		
		System.out.println("File Name:- " + file.getFilename());*/

	/*	try {
			InputStream is = file.getInputStream();
			super.response.setContentType(file.getContentType());
			super.response.setHeader("Cache-Control", "max-age=2592000"); // 一个月：30天
			super.response.setContentLength(((Long) file.getLength()).intValue()); // .setContentLengthLong(byId.getLength());
			super.response.getOutputStream().write(FileUtil.getBytes(is));
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}*/
		FileContentLocalDTO fileContent = this.userService.getAvatarFromMongo(id);
		super.response.setContentType(fileContent.getContentType());
		super.response.setHeader("Cache-Control", "max-age=2592000"); // 一个月：30天
		super.response.setContentLength(fileContent.getLsize().intValue());
		try {
			super.response.getOutputStream().write(fileContent.getContentStream());
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	@ApiOperation(value="添加机构下的职位",notes="addOrgPosition")
	@RequestMapping(value = "/org/positions/{orgId}/{name}", method=RequestMethod.POST)
	public Result addOrgPosition(
			@ApiParam(value = "机构序列id")
			@PathVariable Long orgId,
			@ApiParam(value = "职位名称")
			@PathVariable String name) {
		
		Object obj = this.userService.addOrgPosition(orgId, name);
		if(null == obj)
			return super.failResult("职位名称："+name+"已存在");
		
		return super.successResult(obj);
	}
	
	@ApiOperation(value="添加机构下的职位",notes="addOrgPositionEx")
	@RequestMapping(value = "/v1/org/positions", method=RequestMethod.POST)
	public Result addOrgPositionEx(
			@RequestBody OrgPositionAddDTO dto) {
		
		return super.successResult( this.userService.addOrgPosition(dto));
	}
	@ApiOperation(value="添加机构类型",notes="addOrgType")
	@RequestMapping(value = "/v1/org/types", method=RequestMethod.POST)
	public Result addOrgType(
			@RequestBody OrgTypeAddDTO dto) {
		
		return super.successResult( this.userService.addOrgType(dto));
	}
	@ApiOperation(value="获取机构类型",notes="getOrgTypes")
	@RequestMapping(value = "/v1/org/types/{orgCode}", method=RequestMethod.GET)
	public Result getOrgTypes(
			@ApiParam(value = "机构编码")
			@PathVariable String orgCode) {

		return super.successResult(this.userService.getOrgTypes(orgCode));
	}
	@ApiOperation(value="删除机构下的指定类型字典",notes="deleteOrgPosition")
	@RequestMapping(value = "/v1/org/types/{id}", method={RequestMethod.DELETE})
	public Result deleteOrgType(
			@ApiParam(value = "机构类型字典的序列id")
			@PathVariable Long id) {
		
		boolean issucceed = this.userService.deleteOrgType(id);
		Result result = issucceed? super.successResult("成功删除"):super.failResult("删除失败");
		
		return result;
	}
	@ApiOperation(value="获取机构下的职位",notes="getOrgPositions")
	@RequestMapping(value = "/org/positions/{orgId}", method=RequestMethod.GET)
	public Result getOrgPositions(
			@ApiParam(value = "机构序列id")
			@PathVariable Long orgId) {

		return super.successResult(this.userService.getOrgPositions(orgId));
	}
	
	@ApiOperation(value="获取机构下的职位",notes="getOrgPositions")
	@RequestMapping(value = "/v1/org/positions/{orgCode}", method=RequestMethod.GET)
	public Result getOrgPositions(
			@ApiParam(value = "机构编码")
			@PathVariable String orgCode) {

		return super.successResult(this.userService.getOrgPositions(orgCode));
	}
	
	@ApiOperation(value="删除机构下的指定职位",notes="deleteOrgPosition")
	@RequestMapping(value = "/org/positions/{id}", method={RequestMethod.DELETE, RequestMethod.POST})
	public Result deleteOrgPosition(
			@ApiParam(value = "职位的序列id")
			@PathVariable Long id) {
		
		boolean issucceed = this.userService.deleteOrgPosition(id);
		Result result = issucceed? super.successResult("成功删除"):super.failResult("删除失败");
		
		return result;
	}
	@ApiOperation(value="删除机构下的职位",notes="deleteOrgPosition")
	@RequestMapping(value = "/v1/org/positions/{orgCode}", method={RequestMethod.DELETE})
	public Result deleteOrgPosition(
			@ApiParam(value = "机构编码")
			@PathVariable String orgCode) {
		
		boolean issucceed = this.userService.deleteOrgPosition(orgCode);
		Result result = issucceed? super.successResult("成功删除"):super.failResult("删除失败");
		
		return result;
	}
	@ApiOperation(value="添加机构下的团队",notes="addOrgTeam")
	@RequestMapping(value = "/org/teams/{orgId}/{name}", method=RequestMethod.POST)
	public Result addOrgTeam(
			@ApiParam(value = "机构序列id", required = true)
			@PathVariable Long orgId,
			@ApiParam(value = "团队名称", required = true)
			@PathVariable String name) {
		
		Object obj = this.userService.addOrgTeam(orgId, name);
		if(null == obj)
			return super.failResult("团队名称："+name+"已存在");
		
		return super.successResult(obj);
	}
	@ApiOperation(value="添加机构下的团队",notes="addOrgTeam")
	@RequestMapping(value = "/v1/org/teams", method=RequestMethod.POST)
	public Result addOrgTeam(
			@RequestBody OrgTeamAddDTO dto) {
		
		return super.successResult(this.userService.addOrgTeam(dto));
	}
	
	@ApiOperation(value="获取机构下的团队",notes="getOrgTeams")
	@RequestMapping(value = "/org/teams/{orgId}", method=RequestMethod.GET)
	public Result getOrgTeams(
			@ApiParam(value = "机构序列id")
			@PathVariable Long orgId) {

		return super.successResult(this.userService.getOrgTeams(orgId));
	}
	@ApiOperation(value="获取机构下的团队",notes="getOrgTeams")
	@RequestMapping(value = "/v1/org/teams/{orgCode}", method=RequestMethod.GET)
	public Result getOrgTeams(
			@ApiParam(value = "机构编码")
			@PathVariable String orgCode) {

		return super.successResult(this.userService.getOrgTeams(orgCode));
	}
	
	@ApiOperation(value="删除机构下的指定团队",notes="deleteOrgTeam")
	@RequestMapping(value = "/org/teams/{id}", method={RequestMethod.DELETE, RequestMethod.POST})
	public Result deleteOrgTeam(
			@ApiParam(value = "团队的序列id")
			@PathVariable Long id) {
		
		boolean issucceed = this.userService.deleteOrgTeam(id);
		Result result = issucceed? super.successResult("成功删除"):super.failResult("删除失败");
		
		return result;
	}

	@ApiOperation(value="重命名机构别名",notes="renameOrgName")
	@RequestMapping(value = "/org/newname/{orgCode}/{newName}", method={RequestMethod.PUT, RequestMethod.POST})
	public Result renameOrgName (
			@ApiParam(value = "机构code")
			@PathVariable String orgCode,
			@ApiParam(value = "新名称")
			@PathVariable String newName) {
		
		boolean issucceed = this.userService.renameOrgName(orgCode, newName);
		Result result = issucceed? super.successResult("成功重命名"):super.failResult("重命名失败");
		
		return result;
	}
	
	/*@Deprecated
	@ApiOperation(value="针对用户名，模糊检索用户信息。",notes="fuzzySearchUsers")
	@RequestMapping(value = "/users/{keyword}", method={RequestMethod.GET})
	public Result fuzzySearchUsers (
			@ApiParam(value = "关键字")
			@PathVariable String keyword) {
		
		return super.successResult(this.userService.fuzzySearchUsers(keyword));
	}*/
	
	@ApiOperation(value="针对用户名，分页模糊检索用户信息。",notes="fuzzySearchUsersPage")
	@RequestMapping(value = "/users/{pageNo}/{pageSize}/{keyword}", method={RequestMethod.GET})
	public Result fuzzySearchUsersPage (
			@ApiParam(value = "页码", allowableValues="range[1, infinity]", required=true)
			@PathVariable int pageNo,
			@ApiParam(value = "每页大小", allowableValues="range[1, infinity]", required=true)
			@PathVariable int pageSize,
			@ApiParam(value = "关键字", required=true)
			@PathVariable String keyword) {
		
		return super.successResult(this.userService.fuzzySearchUsersPage(pageNo, pageSize, keyword));
	}
	
	@ApiOperation(value="针对用户名，分页模糊检索用户信息。",notes="fuzzySearchUsersPage")
	@RequestMapping(value = "/users/{realm}/{pageNo}/{pageSize}/{keyword}", method={RequestMethod.GET})
	public Result fuzzySearchUsersPage (
			@ApiParam(value = "域", required=true)
			@PathVariable String realm,
			@ApiParam(value = "页码", allowableValues="range[1, infinity]", required=true)
			@PathVariable int pageNo,
			@ApiParam(value = "每页大小", allowableValues="range[1, infinity]", required=true)
			@PathVariable int pageSize,
			@ApiParam(value = "关键字", required=true)
			@PathVariable String keyword) {
		
		return super.successResult(this.userService.fuzzySearchUsersPage(realm, pageNo, pageSize, keyword));
	}

}
