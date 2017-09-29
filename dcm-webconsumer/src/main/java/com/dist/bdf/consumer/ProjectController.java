package com.dist.bdf.consumer;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSONObject;
import com.dist.bdf.base.controller.BaseController;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.base.result.Result;
import com.dist.bdf.base.utils.DateUtil;
import com.dist.bdf.base.utils.DistThreadManager;
import com.dist.bdf.base.utils.FileUtil;
import com.dist.bdf.common.conf.ecm.ECMConf;
import com.dist.bdf.common.constants.GlobalSystemParameters;
import com.dist.bdf.common.constants.WZTypeConstants;
import com.dist.bdf.facade.service.EcmMgmtService;
import com.dist.bdf.facade.service.GroupService;
import com.dist.bdf.facade.service.PrivilegeService;
import com.dist.bdf.facade.service.ProjectService;
import com.dist.bdf.facade.service.SocialService;
import com.dist.bdf.facade.service.sga.SgaProjectService;
import com.dist.bdf.model.dto.dcm.CaseDTO;
import com.dist.bdf.model.dto.dcm.CaseTypeDTO;
import com.dist.bdf.model.dto.sga.ImgInfo;
import com.dist.bdf.model.dto.system.ChannelDTO;
import com.dist.bdf.model.dto.system.GroupDTO;
import com.dist.bdf.model.dto.system.GroupUserDTO;
import com.dist.bdf.model.dto.system.GroupUserExDTO;
import com.dist.bdf.model.dto.system.M2ultipartFileDTO;
import com.dist.bdf.model.dto.system.NaotuTeamAddDTO;
import com.dist.bdf.model.dto.system.ProjectGroupDTO;
import com.dist.bdf.model.dto.system.SocialWzParaDTO;
import com.dist.bdf.model.dto.system.SocialWzSimpleResultDTO;
import com.dist.bdf.model.dto.system.TaskAddDTO;
import com.dist.bdf.model.dto.system.UserDomainRoleDTO;
import com.dist.bdf.model.dto.system.WzInfoDTO;
import com.dist.bdf.model.dto.system.WzInfoPageDTO;
import com.dist.bdf.model.dto.system.WzInfoParaDTO;
import com.dist.bdf.model.dto.system.WzInfoParaWebDTO;
import com.dist.bdf.model.dto.system.WzInfoSendToUserDTO;
import com.dist.bdf.model.dto.system.page.PageProjectPara;
import com.dist.bdf.model.dto.system.team.TeamLeaderRequestDTO;
import com.dist.bdf.model.dto.system.team.TeamUserDelDTO;
import com.dist.bdf.model.dto.system.workgroup.WorkGroupAddDTO;
import com.dist.bdf.model.dto.system.workgroup.WorkGroupOrgAddDTO;
import com.dist.bdf.model.entity.system.DcmGroup;
import com.google.common.base.Objects;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = { "API-项目服务模块" }, description = "ProjectController")
@RestController
@RequestMapping(value = "/rest/sysservice")
//@CrossOrigin(origins = "*")
public class ProjectController extends BaseController {

	private static Map<String, List<M2ultipartFileDTO>> myMultipartFileMap = new ConcurrentHashMap<String, List<M2ultipartFileDTO>>();
	private static Logger LOG = LoggerFactory.getLogger(ProjectController.class);
	
	@Autowired
	private CommonController commonCtl;
	@Autowired
	private EcmMgmtService ecmMgmtService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private SocialService socialService;
	@Autowired
	private SgaProjectService sgaProjectService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private PrivilegeService privService;
	@Autowired
	private ECMConf ecmConf;
	
	@Deprecated
	@ApiOperation(value = "添加项目组。（已过时，切换到接口group/project）", notes = "addProjectGroup")
	@RequestMapping(value = "addProjectGroup/{parajson:.+}", method = { RequestMethod.POST, RequestMethod.GET })
	public Result addProjectGroup(
			@ApiParam(value = "json格式的项目信息。") 
			@PathVariable String parajson) throws Exception {

		ProjectGroupDTO dto = JSONObject.parseObject(parajson, ProjectGroupDTO.class);//(ProjectGroupDTO) JSONUtil.toObject(parajson, ProjectGroupDTO.class);
		
		DcmGroup group = this.groupService.addGroup(dto);
		return super.successResult(group);

	}
	
	@ApiOperation(value = "针对广规院项目，添加项目组", notes = "addProjectGroupGZ")
	@RequestMapping(value = "addProjectGroupGZ/{parajson:.+}", method = { RequestMethod.POST, RequestMethod.GET })
	public Result addProjectGroupGZ(@ApiParam(value = "json格式的项目信息。") @PathVariable String parajson) throws Exception {

		ProjectGroupDTO dto = JSONObject.parseObject(parajson, ProjectGroupDTO.class);// (ProjectGroupDTO) JSONUtil.toObject(parajson, ProjectGroupDTO.class);

		DcmGroup group = this.groupService.addGroupGZ(dto);
		return super.successResult(group);

	}

	@Deprecated
	@ApiOperation(value = "添加项目组", notes = "addProjectGroupEx")
	@RequestMapping(value = "/group/project", method = { RequestMethod.POST })
	public Result addProjectGroupEx(
			@ApiParam(value = "json格式的项目信息。") 
			@Valid
			@RequestBody ProjectGroupDTO dto, BindingResult result) throws Exception {

		//ProjectGroupDTO dto = (ProjectGroupDTO) JSONUtil.toObject(parajson, ProjectGroupDTO.class);

		if(result.hasErrors()) 
			return super.errorResult(result.toString());
		
		DcmGroup group = this.groupService.addGroupEx(dto);
		return super.successResult(group);
	}
	
	@Deprecated
	@ApiOperation(value = "分页获取跟个人相关项目列表", notes = "listMyProjectsByPage")
	@RequestMapping(value = "listMyProjectsByPage/{parajson}", method = { RequestMethod.GET })
	public Result listMyProjectsByPage(@ApiParam(value = "json格式，传入用户名称，分页大小、页码") @PathVariable String parajson)
			throws Exception {

		PageProjectPara para = JSONObject.parseObject(parajson, PageProjectPara.class);
		Object result = this.groupService.searchProjectsByPage(para, Boolean.TRUE);
		return super.successResult(result);
	}
	
	@ApiOperation(value = "分页获取跟个人相关的一般项目列表", notes = "listMyCommonProjectsByPage")
	@RequestMapping(value = "/projects/mycommon/{parajson}", method = { RequestMethod.GET })
	public Result listMyCommonProjectsByPage(
			@ApiParam(value = "json格式，userCode：用户id，realm：域，pageSize：分页大小，pageNo：页码，keyword：关键字(全部传入*)") 
			@PathVariable String parajson)
			throws Exception {

		PageProjectPara para = JSONObject.parseObject(parajson, PageProjectPara.class);
		 // "XZ_CASETYPE_JYXM","XZ_CASETYPE_HZXM"
		return super.successResult(this.groupService.searchProjectsByPage(para, this.ecmConf.getProjectTypeCommon().split(";"), Boolean.TRUE));
	}
	
	@ApiOperation(value = "分页获取跟个人不相关的一般项目列表", notes = "listNotMyCommonProjectsByPage")
	@RequestMapping(value = "/projects/otherscommon/{parajson}", method = { RequestMethod.GET })
	public Result listOthersCommonProjectsByPage(
			@ApiParam(value = "json格式，userCode：用户id，realm：域，pageSize：分页大小，pageNo：页码，keyword：关键字(全部传入*)") 
			@PathVariable String parajson)
			throws Exception {

		PageProjectPara para = JSONObject.parseObject(parajson, PageProjectPara.class);
		// "XZ_CASETYPE_JYXM","XZ_CASETYPE_HZXM"
		return super.successResult( this.groupService.searchOthersProjectsByPage(para, this.ecmConf.getProjectTypeCommon().split(";")));
	}
	
	@ApiOperation(value = "分页获取跟个人相关的团队项目列表", notes = "listMyTeamProjectsByPage")
	@RequestMapping(value = "/projects/myteam/{parajson}", method = { RequestMethod.GET })
	public Result listMyTeamProjectsByPage(
			@ApiParam(value = "json格式，userCode：用户id，realm：域，pageSize：分页大小，pageNo：页码，keyword：关键字(全部传入*)") 
			@PathVariable String parajson)
			throws Exception {

		PageProjectPara para = JSONObject.parseObject(parajson, PageProjectPara.class);
		Object result = this.groupService.searchProjectsByPage(para, this.ecmConf.getProjectTypeTeam().split(";"), Boolean.TRUE);// "XZ_CPTDGL"
		return super.successResult(result);
	}
	
	@ApiOperation(value = "分页获取跟个人相关项目列表", notes = "listMyProjectsByPageGZ")
	@RequestMapping(value = "listMyProjectsByPageGZ/{parajson}", method = { RequestMethod.GET })
	public Result listMyProjectsByPageGZ(@ApiParam(value = "json格式，传入用户名称，分页大小、页码") @PathVariable String parajson)
			throws Exception {

		PageProjectPara para = JSONObject.parseObject(parajson, PageProjectPara.class);
		Object result = this.groupService.searchProjectsByPageGZ(para, Boolean.TRUE);
		return super.successResult(result);
	}

	
	@ApiOperation(value = "获取所有的案例类型字典信息", response = Result.class, notes = "getCaseTypes")
	@RequestMapping(value = "/getCaseTypes", method = { RequestMethod.GET })
	public Result getCaseTypes() {
		try {

			List<CaseTypeDTO> list = this.projectService.getCaseTypes();
			return super.successResult(list);

		} catch (Exception ex) {
			return super.failResultException(ex);
		}
	}
	@ApiOperation(value = "获取项目组列表", notes = "listProjectGroups")
	@RequestMapping(value = "listProjectGroups", method = { RequestMethod.GET })
	public Result listProjectGroups() {

		List<DcmGroup> groups = this.groupService.listProjectGroups();
		return super.successResult(groups);
	}
	@Deprecated
	@ApiOperation(value = "获取项目组下的角色，包括每个角色下的人员信息。（已废弃）", notes = "listUsersOfGroupByCode")
	@RequestMapping(value = "listUsersOfGroupByCode/{groupCode}", method = { RequestMethod.GET })
	public Result listUsersOfGroupByCode(@ApiParam(value = "组编码") @PathVariable String groupCode) {

		String baseURL = super.getBaseURL();// super.request.getScheme() + "://" + super.request.getServerName() + ":"
				//+ super.request.getServerPort() + super.request.getContextPath();
		String avatarLocalpath = super.request.getServletContext().getRealPath("/avatar/");
		List<GroupUserDTO> list = this.groupService.listUsersOfGroupByCode(avatarLocalpath, baseURL, groupCode);
		return super.successResult(list);

	}
	@Deprecated
	@ApiOperation(value = "获取项目组下的角色，包括每个角色下的人员信息。（已废弃）", notes = "listUsersOfGroupByCodeEx")
	@RequestMapping(value = "/group/project/roleusers/{groupCode}", method = { RequestMethod.GET })
	public Result listUsersOfGroupByCodeEx(
			@ApiParam(value = "组编码") 
			@PathVariable String groupCode) {

		String baseURL = super.getBaseURL();// super.request.getScheme() + "://" + super.request.getServerName() + ":"
				//+ super.request.getServerPort() + super.request.getContextPath();
		String avatarLocalpath = super.request.getServletContext().getRealPath("/avatar/");
		List<GroupUserDTO> list = this.groupService.listUsersOfGroupByCode(avatarLocalpath, baseURL, groupCode);
		return super.successResult(list);
	}
	
	@ApiOperation(value = "扩展接口，获取项目组下的角色，包括每个角色下的人员信息。", notes = "listUsersOfGroupByCodeExNew")
	@RequestMapping(value = "/group/project/roleusers/ex/{groupCode}", method = { RequestMethod.GET })
	public Result listUsersOfGroupByCodeExNew(
			@ApiParam(value = "组编码") 
			@PathVariable String groupCode) {

		String baseURL = super.getBaseURL();// super.request.getScheme() + "://" + super.request.getServerName() + ":"
				//+ super.request.getServerPort() + super.request.getContextPath();
		String avatarLocalpath = super.request.getServletContext().getRealPath("/avatar/");
		List<GroupUserExDTO> list = this.groupService.listUsersOfGroupByCodeEx(avatarLocalpath, baseURL, groupCode);
		return super.successResult(list);
	}
	
	/*@ApiOperation(value = "扩展接口，获取项目组下的角色，包括每个角色下的人员信息。（济南）", notes = "listUsersOfProjectGroupByCodeByRealm")
	@RequestMapping(value = "/group/project/roleusers/v1/{groupCode}", method = { RequestMethod.GET })
	public Result listUsersOfProjectGroupByCodeByRealm(
			@ApiParam(value = "组编码") 
			@PathVariable String groupCode) {

		String baseURL = super.getBaseURL();// super.request.getScheme() + "://" + super.request.getServerName() + ":"
				//+ super.request.getServerPort() + super.request.getContextPath();
		String avatarLocalpath = super.request.getServletContext().getRealPath("/avatar/");
		List<GroupUserExDTO> list = this.groupService.listUsersOfProjectGroupByCodeRealm(avatarLocalpath, baseURL, groupCode);
		return super.successResult(list);
	}
	@ApiOperation(value = "扩展接口，获取团队下的角色，包括每个角色下的人员信息。（济南）", notes = "listUsersOfTeamGroupByCodeByRealm")
	@RequestMapping(value = "/group/team/roleusers/v1/{groupCode}", method = { RequestMethod.GET })
	public Result listUsersOfTeamGroupByCodeByRealm(
			@ApiParam(value = "组编码") 
			@PathVariable String groupCode) {

		String baseURL = super.getBaseURL();// super.request.getScheme() + "://" + super.request.getServerName() + ":"
				//+ super.request.getServerPort() + super.request.getContextPath();
		String avatarLocalpath = super.request.getServletContext().getRealPath("/avatar/");
		List<GroupUserExDTO> list = this.groupService.listUsersOfTeamGroupByCodeRealm(avatarLocalpath, baseURL, groupCode);
		return super.successResult(list);
	}*/
	@ApiOperation(value = "扩展接口，获取组（项目组和团队）下的角色，包括每个角色下的人员信息。（济南）", notes = "listUsersOfGroupByCodeByRealm")
	@RequestMapping(value = "/group/roleusers/v1/{groupCode}", method = { RequestMethod.GET })
	public Result listUsersOfGroupByCodeByRealm(
			@ApiParam(value = "组编码") 
			@PathVariable String groupCode) {

		String baseURL = super.getBaseURL();// super.request.getScheme() + "://" + super.request.getServerName() + ":"
				//+ super.request.getServerPort() + super.request.getContextPath();
		String avatarLocalpath = super.request.getServletContext().getRealPath("/avatar/");
		List<GroupUserExDTO> list = this.groupService.listUsersOfGroupByCodeRealm(avatarLocalpath, baseURL, groupCode);
		return super.successResult(list);
	}
	
	@ApiOperation(value = "获取项目组下的角色，包括每个角色下的人员信息", notes = "listUsersOfGroupByCodeExGZ")
	@RequestMapping(value = "/group/project/roleusers/gz/{groupCode}", method = { RequestMethod.GET })
	public Result listUsersOfGroupByCodeExGZ(
			@ApiParam(value = "组编码") 
			@PathVariable String groupCode) {

		String baseURL = super.getBaseURL();// super.request.getScheme() + "://" + super.request.getServerName() + ":"
				//+ super.request.getServerPort() + super.request.getContextPath();
		String avatarLocalpath = super.request.getServletContext().getRealPath("/avatar/");
		List<GroupUserDTO> list = this.groupService.listUsersOfGroupByCodeGZ(avatarLocalpath, baseURL, groupCode);
		return super.successResult(list);

	}

	@ApiOperation(value = "获取项目组下的角色", notes = "listUsersOfProjectGroupByCodes，包括每个角色下的人员信息，返回是个Map对象，key是项目id")
	@RequestMapping(value = "/project/users.get/{parajson}", method = { RequestMethod.GET })
	public Result listUsersOfProjectGroupByCodes(@ApiParam(value = "项目组编码集合") @PathVariable String parajson)
			throws Exception {

		List<String> codeObjs = JSONObject.parseArray(parajson, String.class);// (Object[]) JSONUtil.toArray(parajson, String.class);
		String[] codeStrArray = codeObjs.toArray(new String[codeObjs.size()]);
		String baseURL = super.request.getScheme() + "://" + super.request.getServerName() + ":"
				+ super.request.getServerPort() + super.request.getContextPath();
		String avatarLocalpath = super.request.getServletContext().getRealPath("/avatar/");

		return super.successResult(this.groupService.listUsersOfProjectGroupByCodeGZ(avatarLocalpath, baseURL, codeStrArray));
	}
	
	@ApiOperation(value = "更新项目组信息", notes = "updateProjectGroup")
	@RequestMapping(value = "updateProjectGroup/{parajson:..+}", method = { RequestMethod.PUT })
	public Result updateProjectGroup(@ApiParam(value = "json格式项目组信息") @PathVariable String parajson) throws Exception {

		ProjectGroupDTO dto = JSONObject.parseObject(parajson, ProjectGroupDTO.class);
		GroupDTO groupDto = this.groupService.updateProjectGroup(dto);
		return super.successResult(groupDto);

	}
	@Deprecated
	@ApiOperation(value = "添加用户到指定项目组，建议切换到/projectgroup/users", notes = "addUserToProjectGroup")
	@RequestMapping(value = "addUserToProjectGroup/{parajson:.+}", method = { RequestMethod.GET, RequestMethod.POST })
	public Result addUserToProjectGroup(@ApiParam(value = "用户和项目组json信息") @PathVariable String parajson) {
		
		UserDomainRoleDTO dto = JSONObject.parseObject(parajson, UserDomainRoleDTO.class);
		return super.successResult(this.privService.addUserToProjectGroup(dto));
	}
	
	@ApiOperation(value = "添加用户到指定项目组", notes = "addUserToProjectGroupEx")
	@RequestMapping(value = "/v1/projectgroup/users", method = { RequestMethod.POST })
	public Result addUserToProjectGroupEx(
			@ApiParam(value = "用户和项目组json信息") 
			@RequestBody UserDomainRoleDTO dto) {
		
		return super.successResult(this.privService.addUserToProjectGroupEx(dto));
	}
	@Deprecated
	@ApiOperation(value = "从项目组中把一组人删除(已过时)", notes = "removeUserFromProject")
	@RequestMapping(value = "removeUserFromProject/{parajson:.+}", method = { RequestMethod.DELETE, RequestMethod.GET })
	public Result removeUserFromProject(@PathVariable String parajson) {
		
		UserDomainRoleDTO dto = JSONObject.parseObject(parajson, UserDomainRoleDTO.class);// (UserDomainRoleDTO) JSONUtil.toObject(parajson, UserDomainRoleDTO.class);
		// 删除权限信息
		this.privService.removeUserFromProjectGroup(dto);
		// 删除团队关联信息
		this.projectService.removeUsersFromTeam(dto.getUserIds());
		return super.successResult("成功删除");
	}
	
	@ApiOperation(value = "从项目组中把一组人删除(不支持requestbody)", notes = "removeUserFromProject")
	@RequestMapping(value = "/v1/projectgroup/users/{parajson}", method = { RequestMethod.DELETE , RequestMethod.GET})
	public Result removeUserFromProjectEx(
			@ApiParam(value = "被删除人员的JSON信息，map对象：users，key为用户编码，value是标识内外部用户，0(内部)/1(外部)；空间域编码：domainCode")
			@PathVariable String parajson) {
		
		UserDomainRoleDTO dto = JSONObject.parseObject(parajson, UserDomainRoleDTO.class);// (UserDomainRoleDTO) JSONUtil.toObject(parajson, UserDomainRoleDTO.class);
		// 删除权限信息
		this.privService.removeUserFromProjectGroupEx(dto);
		// 删除团队关联信息
		this.projectService.removeUsersFromTeam(dto.getUsers());
		return super.successResult("成功删除");
	}
	@Deprecated
	@ApiOperation(value = "分页获取所有项目列表", notes = "searchProjectsByPage")
	@RequestMapping(value = "searchProjectsByPage/{parajson}", method = { RequestMethod.GET })
	public Result searchProjectsByPage(
			@ApiParam(value = "json格式，传入用户名称，分页大小、页码") 
			@PathVariable String parajson) {

		PageProjectPara para = JSONObject.parseObject(parajson, PageProjectPara.class);
		Object result = this.groupService.searchProjectsByPage(para, Boolean.FALSE);
		return super.successResult(result);

	}
	
	@ApiOperation(value = "分页获取一般项目列表", notes = "searchCommonProjectsByPage")
	@RequestMapping(value = "/projects/common/{parajson}", method = { RequestMethod.GET })
	public Result searchCommonProjectsByPage(
			@ApiParam(value = "json格式，userCode：用户id，realm：域，pageSize：分页大小，pageNo：页码，keyword：关键字(全部传入*)") 
			@PathVariable String parajson) {

		PageProjectPara para = JSONObject.parseObject(parajson, PageProjectPara.class);
		Object result = this.groupService.searchProjectsByPage(para, new String[]{"XZ_CASETYPE_JYXM","XZ_CASETYPE_HZXM"},  Boolean.FALSE);
		return super.successResult(result);
	}
	
	@ApiOperation(value = "分页获取团队项目列表", notes = "searchTeamProjectsByPage")
	@RequestMapping(value = "/projects/team/{parajson}", method = { RequestMethod.GET })
	public Result searchTeamProjectsByPage(
			@ApiParam(value = "json格式，传入用户名称，分页大小、页码") 
			@PathVariable String parajson) {

		PageProjectPara para = JSONObject.parseObject(parajson, PageProjectPara.class);
		Object result = this.groupService.searchProjectsByPage(para, new String[]{"XZ_CPTDGL"},  Boolean.FALSE);
		return super.successResult(result);
	}
	
	@ApiOperation(value = "分页获取所有项目列表", notes = "searchProjectsByPageGZ")
	@RequestMapping(value = "searchProjectsByPageGZ/{parajson}", method = { RequestMethod.GET })
	public Result searchProjectsByPageGZ(@ApiParam(value = "json格式，传入用户名称，分页大小、页码") @PathVariable String parajson)
			throws Exception {

		PageProjectPara para = JSONObject.parseObject(parajson, PageProjectPara.class);
		Object result = this.groupService.searchProjectsByPageGZ(para, Boolean.FALSE);
		return super.successResult(result);
	}


	/**
	 * 获取所有case类型
	 * 
	 * @param typePrefix
	 *            case类型前缀
	 * @return
	 */
	@ApiOperation(value = "根据案例类型前缀，获取所有的案例信息", response = Result.class, notes = "getCasesByType")
	@RequestMapping(value = "/getCasesByType/{typePrefix}", method = { RequestMethod.GET })
	public Result getCasesByType(@PathVariable String typePrefix) {
		try {

			List<CaseDTO> list = this.projectService.getCasesByType(typePrefix);
			return super.successResult(list);

		} catch (Exception ex) {
			return super.failResultException(ex);
		}
	}

	@Deprecated
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "根据案例标识和创建者获取所有微作的点赞、收藏和标记信息", notes = "getWzByCaseIdentifierAndCreator")
	@RequestMapping(value = "getWzByCaseIdentifierAndCreator/{parajson:.+}", method = RequestMethod.GET)
	public Result getWzByCaseIdentifierAndCreator(
			@ApiParam(value = "案例标识和创建者JSON信息", required = true) @PathVariable String parajson) throws Exception {

		SocialWzParaDTO dto = JSONObject.parseObject(parajson, SocialWzParaDTO.class);
		Map map = this.socialService.getMapWzOfCase(dto);
		return super.successResult(map);

	}

	@Deprecated
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "根据案例标识和创建者获取所有微作的点赞、收藏和标记信息", notes = "getWzByCaseIdentifierAndCreatorGZ")
	@RequestMapping(value = "getWzByCaseIdentifierAndCreatorGZ/{parajson:.+}", method = RequestMethod.GET)
	public Result getWzByCaseIdentifierAndCreatorGZ(
			@ApiParam(value = "案例标识和创建者JSON信息", required = true) @PathVariable String parajson) throws Exception {

		SocialWzParaDTO dto = JSONObject.parseObject(parajson, SocialWzParaDTO.class);
		Map map = this.socialService.getMapWzOfCaseGZ(dto);
		return super.successResult(map);

	}

	@Deprecated
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "根据案例的id获取所有微作的点赞、收藏和标记信息(可切换到新接口：/wz/social/{parajson:.+})", notes = "getWzSocialByCaseId")
	@RequestMapping(value = "getWzByCaseId/{parajson:.+}", method = RequestMethod.GET)
	public Result getWzSocialByCaseId(
			@ApiParam(value = "案例标识和创建者JSON信息", required = true) @PathVariable String parajson) throws Exception {

		SocialWzParaDTO dto = JSONObject.parseObject(parajson, SocialWzParaDTO.class);// JSONUtil.toObject(parajson,
																						// SocialWzParaDTO.class);
		Map map = this.socialService.getMapWzOfCase(dto);
		return super.successResult(map);

	}
	
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "根据案例的id获取所有微作的点赞、收藏、标记信息", notes = "getWzSocialByCaseIdEx")
	@RequestMapping(value = "/wz/social/{parajson:.+}", method = RequestMethod.GET)
	public Result getWzSocialByCaseIdEx(
			@ApiParam(value = "案例标识和创建者JSON信息，包括realm、creator(用户编码)和caseId", required = true) @PathVariable String parajson) throws Exception {

		SocialWzParaDTO dto = JSONObject.parseObject(parajson, SocialWzParaDTO.class);// JSONUtil.toObject(parajson,
																						// SocialWzParaDTO.class);
		Map map = this.socialService.getMapWzOfCase(dto);
		return super.successResult(map);

	}

	@Deprecated
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "根据案例的id和创建者获取所有微作的点赞、收藏和标记信息", notes = "getMyWzByCaseId")
	@RequestMapping(value = "getMyWzByCaseId/{parajson:.+}", method = RequestMethod.GET)
	public Result getMyWzByCaseId(@ApiParam(value = "案例标识和创建者JSON信息", required = true) @PathVariable String parajson)
			throws Exception {

		SocialWzParaDTO dto = JSONObject.parseObject(parajson, SocialWzParaDTO.class);// (SocialWzParaDTO)
																						// JSONUtil.toObject(parajson,
																						// SocialWzParaDTO.class);
		Map map = this.socialService.getMapWzOfCaseById(dto);
		return super.successResult(map);

	}

	@Deprecated
	@ApiOperation(value = "根据案例标识，分页查询case下的微作信息", response = Result.class, notes = "getWZOfCase")
	@RequestMapping(value = "/getWZOfCase/{caseIdentifier}/{pageNo}/{pageSize}", method = { RequestMethod.GET })
	public Result getWZOfCase(@ApiParam(value = "案例标识") @PathVariable String caseIdentifier,
			@ApiParam(value = "页码") @PathVariable String pageNo,
			@ApiParam(value = "每页大小") @PathVariable String pageSize) {
		try {
			Object result = this.projectService.getWZOfCase(caseIdentifier, Integer.valueOf(pageNo),
					Integer.valueOf(pageSize));
			return super.successResult(result);

		} catch (Exception ex) {
			return super.failResultException(ex);
		}
	}

	@ApiOperation(value = "根据案例标识，分页查询case下的微作信息(包括0-一般微作/1-调研微作/2-任务微作)", response = Result.class, notes = "getWZsOfCaseById")
	@RequestMapping(value = "/wz/list/{info}", method = { RequestMethod.GET })
	public Result getWZsOfCaseById(
			@ApiParam(value = "json参数，属性包括：realm、caseId、pageNo、pageSize") @PathVariable String info) {

		WzInfoPageDTO pageInfo = JSONObject.parseObject(info, WzInfoPageDTO.class);

		return super.successResult(this.projectService.getWZsOfCaseById(pageInfo));
	}
	
	@ApiOperation(value = "根据案例标识，分页查询case下的一般微作信息", response = Result.class, notes = "getCommonWZsOfCaseById")
	@RequestMapping(value = "/wz/commontype/{info}", method = { RequestMethod.GET })
	public Result getCommonWZsOfCaseById(
			@ApiParam(value = "json参数，属性包括：realm、caseId、pageNo、pageSize") @PathVariable String info) {

		WzInfoPageDTO pageInfo = JSONObject.parseObject(info, WzInfoPageDTO.class);

		return super.successResult(this.projectService.getWZsOfCaseById(WZTypeConstants.COMMON, pageInfo));
	}
	@ApiOperation(value = "根据案例标识，分页查询case下的一般微作信息，返回缩略图URL，替代原来的base64编码。", response = Result.class, notes = "getCommonWZsOfCaseByIdV1")
	@RequestMapping(value = "/v1/wz/commontype/{info}", method = { RequestMethod.GET })
	public Result getCommonWZsOfCaseByIdV1(
			@ApiParam(value = "json参数，属性包括：realm、caseId、pageNo、pageSize") @PathVariable String info) {

		WzInfoPageDTO pageInfo = JSONObject.parseObject(info, WzInfoPageDTO.class);
        // super.getBaseURL()+"/thumb", super.getContextPath("thumb")
		Pagination page = this.projectService.getWZsOfCaseByIdThumbByte(WZTypeConstants.COMMON, pageInfo);
		@SuppressWarnings("unchecked")
		List<WzInfoDTO> wzInfoDTOs = (List<WzInfoDTO>) page.getData();
		this.commonCtl.generateWzThumbnail(super.request, wzInfoDTOs);
		return super.successResult(page);
	}
	@Deprecated
	@ApiOperation(value = "根据案例标识，分页查询case下的一般微作信息，提供给移动专用，返回缩略图URL，替代原来的base64编码。", response = Result.class, notes = "getCommonWZsOfCaseByIdForMobile")
	@RequestMapping(value = "/v1/mobile/wz/commontype/{info}", method = { RequestMethod.GET })
	public Result getCommonWZsOfCaseByIdForMobile(
			@ApiParam(value = "json参数，属性包括：realm、caseId、pageNo、pageSize") @PathVariable String info) {

		WzInfoPageDTO pageInfo = JSONObject.parseObject(info, WzInfoPageDTO.class);

		return super.successResult(this.projectService.getWZsOfCaseByIdForMobile(WZTypeConstants.COMMON, pageInfo, super.getBaseURL()+"/thumb", super.getContextPath("thumb")));
	}
	
	@ApiOperation(value = "根据案例标识，分页查询case下的调研类型微作信息", response = Result.class, notes = "getResearchtypeWZsOfCaseById")
	@RequestMapping(value = "/wz/researchtype/{info}", method = { RequestMethod.GET })
	public Result getResearchWZsOfCaseById(
			@ApiParam(value = "json参数，属性包括：realm、caseId、pageNo、pageSize") @PathVariable String info) {

		WzInfoPageDTO pageInfo = JSONObject.parseObject(info, WzInfoPageDTO.class);

		return super.successResult(this.projectService.getWZsOfCaseById(WZTypeConstants.RESEARCH, pageInfo));
	}
	@ApiOperation(value = "根据案例标识，分页查询case下的调研类型微作信息", response = Result.class, notes = "getResearchtypeWZsOfCaseById")
	@RequestMapping(value = "/v1/wz/researchtype/{info}", method = { RequestMethod.GET })
	public Result getResearchWZsOfCaseByIdV1(
			@ApiParam(value = "json参数，属性包括：realm、caseId、pageNo、pageSize") @PathVariable String info) {

		WzInfoPageDTO pageInfo = JSONObject.parseObject(info, WzInfoPageDTO.class);
		Pagination page = this.projectService.getWZsOfCaseById(WZTypeConstants.RESEARCH, pageInfo);
		@SuppressWarnings("unchecked")
		List<WzInfoDTO> wzInfoDTOs = (List<WzInfoDTO>) page.getData();
		this.commonCtl.generateWzThumbnail(super.request, wzInfoDTOs);
		return super.successResult(page);
	}
	@ApiOperation(value = "根据案例标识，分页查询case下的调研类型微作信息，没有缩略图", response = Result.class, notes = "getResearchWZsOfCaseByIdNothumb")
	@RequestMapping(value = "/v1/wz/researchtype/nothumb/{info}", method = { RequestMethod.GET })
	public Result getResearchWZsOfCaseByIdNothumb(
			@ApiParam(value = "json参数，属性包括：realm、caseId、pageNo、pageSize") @PathVariable String info) {

		WzInfoPageDTO pageInfo = JSONObject.parseObject(info, WzInfoPageDTO.class);

		return super.successResult(this.projectService.getWZsOfCaseNoThumbById(WZTypeConstants.RESEARCH, pageInfo));
	}
	
	@ApiOperation(value = "根据案例标识，分页查询case下的任务类型微作信息", response = Result.class, notes = "getTaskWZsOfCaseById")
	@RequestMapping(value = "/wz/tasktype/{info}", method = { RequestMethod.GET })
	public Result getTaskWZsOfCaseById(
			@ApiParam(value = "json参数，属性包括：realm、caseId、pageNo、pageSize") @PathVariable String info) {

		WzInfoPageDTO pageInfo = JSONObject.parseObject(info, WzInfoPageDTO.class);
		return super.successResult(this.projectService.getWZsOfCaseById(WZTypeConstants.TASK, pageInfo));
	}
	@ApiOperation(value = "根据案例标识，分页查询case下的任务类型微作信息，返回缩略图URL，替代原来的base64编码。", response = Result.class, notes = "getTaskWZsOfCaseByIdV1")
	@RequestMapping(value = "/v1/wz/tasktype/{info}", method = { RequestMethod.GET })
	public Result getTaskWZsOfCaseByIdV1(
			@ApiParam(value = "json参数，属性包括：realm、caseId、pageNo、pageSize") @PathVariable String info) {

		WzInfoPageDTO pageInfo = JSONObject.parseObject(info, WzInfoPageDTO.class);
		Pagination page = this.projectService.getWZsOfCaseByIdThumbByte(WZTypeConstants.TASK, pageInfo);
		@SuppressWarnings("unchecked")
		List<WzInfoDTO> wzInfoDTOs = (List<WzInfoDTO>) page.getData();
		this.commonCtl.generateWzThumbnail(super.request, wzInfoDTOs);
		return super.successResult(page);
	}

	@ApiOperation(value = "根据案例类型，获取环节信息", response = Result.class, notes = "getTachesByCaseType")
	@RequestMapping(value = "/getTachesByCaseType/{name}", method = RequestMethod.GET)
	public Result getTachesByCaseType(
			@ApiParam(name = "name", value = "案例类型标识符，例如经营项目类型，传入：XZ_CASETYPE_JYXM") @PathVariable String name) {

		Object result = this.ecmMgmtService.getTachesByCaseType(name);
		return super.successResult(result);
	}

	@ApiOperation(value = "根据所有的案例类型和案例数据信息", response = Result.class, notes = "getAllCases")
	@RequestMapping(value = "/getAllCases", method = RequestMethod.GET)
	public Result getAllCases() {

		Object result = this.projectService.getAllCases();
		return super.successResult(result);
	}

	/*
	 * @ApiOperation(value="getProjectPcks", notes = "逐层获取项目包资料信息。")
	 * 
	 * @RequestMapping(value="getProjectPcks/{caseIdentifier}", method =
	 * RequestMethod.GET) public Result getProjectPcks(
	 * 
	 * @ApiParam(value="传入案例标识，如：XZ_CASETYPE_JYXM_000000100008")
	 * 
	 * @PathVariable String caseIdentifier){
	 * 
	 * return
	 * super.successResult(this.projectService.getPackages(caseIdentifier)); }
	 */
	@ApiOperation(value = "逐层获取项目子包资料信息", notes = "getProjectPcks")
	@RequestMapping(value = "/getProjectPcks/{realm}/{caseIdentifier}", method = RequestMethod.GET)
	public Result getProjectPcks(@ApiParam(value = "域") @PathVariable String realm,
			@ApiParam(value = "传入案例id，如：XZ_CASETYPE_JYXM_000000100008") @PathVariable String caseIdentifier) {

		return super.successResult(this.projectService.getPackages(realm, caseIdentifier));
	}

	// <=====微作模块begin

	@Deprecated
	@ApiOperation(value = "提供给移动端，创建微作服务", response = Result.class, notes = "createWzForMobile")
	@RequestMapping(value = "/createWzForMobile", method = RequestMethod.POST)
	public Result createWzForMobile(HttpServletRequest request, @RequestParam String properties) throws Exception {

		// String json = request.getParameter("properties");
		LOG.info(">>>createWzForMobile，传入微作元数据：[{}]", properties);

		WzInfoParaDTO info = JSONObject.parseObject(properties, WzInfoParaDTO.class);
		List<M2ultipartFileDTO> uploadFiles = this.commonCtl.getUploadFiles(request);
		Object result = this.projectService.createWZForMobile(info, uploadFiles);

		return super.successResult(result);
	}
	
	@ApiOperation(value = "提供给移动端，创建微作服务", response = Result.class, notes = "createWzForMobile")
	@RequestMapping(value = "/wz/mobile", method = RequestMethod.POST)
	public Result createWzForMobileEx(@RequestParam String properties) throws Exception {

		// String json = request.getParameter("properties");
		LOG.info(">>>createWzForMobile，传入微作元数据："+properties);

		WzInfoParaDTO info = JSONObject.parseObject(properties, WzInfoParaDTO.class);
		List<M2ultipartFileDTO> uploadFiles = this.commonCtl.getUploadFiles(request);
		Object result = this.projectService.createWZForMobile(info, uploadFiles);

		return super.successResult(result);
	}
	
	@ApiOperation(value = "提供给移动小程序，创建微作服务", response = Result.class, notes = "createWzForMobileApp")
	@RequestMapping(value = "/wz/mobileapp", method = RequestMethod.POST)
	public Result createWzForMobileApp(
			@ApiParam(value = "微作的属性信息", required = true)
			@RequestParam("properties") String properties) throws Exception {

		// String json = request.getParameter("properties");
		LOG.info(">>>createWzForMobileApp，传入微作元数据：[{}]", properties);

		final WzInfoParaDTO info = JSONObject.parseObject(properties, WzInfoParaDTO.class);
		if(!myMultipartFileMap.containsKey(info.getVersion())){
			myMultipartFileMap.put(info.getVersion(), new ArrayList<M2ultipartFileDTO>());
		}
		List<M2ultipartFileDTO> myFiles = this.commonCtl.getUploadFiles(super.request);
		if(myFiles != null && !myFiles.isEmpty()){
			LOG.info(">>>文件名称[{}]，大小[{}]", myFiles.get(0).getOriginalFilename(), myFiles.get(0).getSize());
			myMultipartFileMap.get(info.getVersion()).add(myFiles.get(0));
		}
		
		if(info.getUpload()){
			LOG.info(">>>小程序文件已上传完毕，准备创建微作实体。");
			
			final WzInfoDTO result = this.projectService.createWZForMobile(info, myMultipartFileMap.get(info.getVersion()));
			LOG.info(">>>删除key："+info.getVersion());
			myMultipartFileMap.remove(info.getVersion());
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {

				@Override
				public void run() {
					LOG.info(">>>记录合作项目的微作信息");
					sgaProjectService.recordWz(info.getCaseId(), result.getGuid(), info.getUserId(),
							DateUtil.strToDate(result.getDateCreated()));
				}
			});

			return super.successResult(result);
		}else{
			LOG.info(">>>小程序文件继续上传......");
			return super.successResult("文件上传成功");
		}
	}
	
	@ApiOperation(value = "提供给移动端，创建微作服务", response = Result.class, notes = "createWzForMobile")
	@RequestMapping(value = "/wz/mobileex", method = RequestMethod.POST)
	public Result createWzForMobileEx(
			@RequestParam("files") MultipartFile[] files,
			@RequestParam("properties") String properties) throws Exception {

		// String json = request.getParameter("properties");
		// logger.info(">>>createWzForMobile，传入微作元数据：[{}]", properties);

		WzInfoParaDTO info = JSONObject.parseObject(properties, WzInfoParaDTO.class);
		List<M2ultipartFileDTO> uploadFiles = this.commonCtl.getUploadFiles(request);
		Object result = this.projectService.createWZForMobile(info, uploadFiles);

		return super.successResult(result);
	}

	

	@ApiOperation(value = "提供给web端，创建普通微作", response = Result.class, notes = "createWZForWeb")
	@RequestMapping(value = "/wz/web", method = { RequestMethod.POST })
	public Result createWZForWeb(@ApiParam(value = "json参数") @Valid @RequestBody final WzInfoParaWebDTO dto,
			BindingResult result) throws Exception {

		if (result.hasErrors())
			return super.errorResult("参数验证失败，详情：" + result.toString());
		
		final WzInfoDTO wzinfodto = (WzInfoDTO) this.projectService.createWZForWeb(dto);
		DistThreadManager.MyCacheThreadPool.execute(new Runnable() {

			@Override
			public void run() {
				LOG.info(">>>记录合作项目的微作信息");
				sgaProjectService.recordWz(dto.getCaseId(), wzinfodto.getGuid(), dto.getUserId(),
						DateUtil.strToDate(wzinfodto.getDateCreated()));
			}
		});

		return super.successResult(wzinfodto);
	}

	/*
	 * @ApiOperation(value = "deleteProject", response = Result.class,
	 * notes="删除项目所有信息。")
	 * 
	 * @RequestMapping(value = "/deleteProject/{caseIdentifier}",
	 * method=RequestMethod.GET) public Result deleteProject(
	 * 
	 * @ApiParam(value = "案例标识，如：XZ_CASETYPE_JYXM_000000100008")
	 * 
	 * @PathVariable String caseIdentifier) {
	 * 
	 * return
	 * super.successResult(this.projectService.deleteProject(caseIdentifier)); }
	 */
	@ApiOperation(value = "删除项目所有信息", response = Result.class, notes = "deleteProject")
	@RequestMapping(value = "/deleteProject/{caseGuid}", method = RequestMethod.GET)
	public Result deleteProject(
			@ApiParam(value = "案例标识guid，如：{E0C80757-0000-CC1D-B9FA-910DDF44C154}") @PathVariable String caseGuid) {

		return super.successResult(this.projectService.deleteProjectByGuid(caseGuid));
	}

	@ApiOperation(value = "获取属于个人的项目列表，按照创建时间倒序", response = Result.class, notes = "getMyProjects")
	@RequestMapping(value = "getMyProjects/{realm}/{userId}/{pwd}", method = RequestMethod.GET)
	public Result getMyProjects(@ApiParam(value = "域") @PathVariable String realm,
			@ApiParam(value = "用户登录名") @PathVariable String userId, @ApiParam(value = "密码") @PathVariable String pwd) {

		Object result = this.projectService.getMyProjects(realm, userId, pwd);

		return super.successResult(result);
	}

	@ApiOperation(value = "获取属于个人的项目列表，按照创建时间倒序", response = Result.class, notes = "getMyProjects")
	@RequestMapping(value = "getMyProjects/{realm}/{userId}", method = RequestMethod.GET)
	public Result getMyProjects(@ApiParam(value = "域") @PathVariable String realm,
			@ApiParam(value = "用户dn") @PathVariable String userId) {

		Object result = this.projectService.getMyProjects(realm, userId);
		return super.successResult(result);
	}

	@ApiOperation(value = "获取属于个人的项目列表，按照创建时间倒序", response = Result.class, notes = "getMyProjectsGZ")
	@RequestMapping(value = "getMyProjectsGZ/{realm}/{userId}", method = RequestMethod.GET)
	public Result getMyProjectsGZ(@ApiParam(value = "域") @PathVariable String realm,
			@ApiParam(value = "用户dn") @PathVariable String userId) {

		Object result = this.projectService.getMyProjectsGZ(realm, userId);

		return super.successResult(result);
	}

	/*@Deprecated
	@ApiOperation(value = "更新微作评论数，请求参数使用post方式（已过时）", response = Result.class, notes = "updateWzCommentsCount")
	@RequestMapping(value = "updateWzCommentsCount", method = RequestMethod.POST)
	public Result updateWzCommentsCount() {

		String parajson = super.request.getParameter("info");
		JSONObject jsonObj = JSONObject.parseObject(parajson);
		String userId = jsonObj.getString("user");
		String password = jsonObj.getString("password");
		int op = jsonObj.getIntValue("op");
		String wzId = jsonObj.getString("wzId");

		int count = this.projectService.updateWzCommentsCount(userId, password, wzId, op);

		return super.successResult(count);

	}
	*/
	@ApiOperation(value = "更新微作评论数", response = Result.class, notes = "updateWzCommentsCountEx")
	@RequestMapping(value = "/wz/commentsCount/{realm}/{wzId}/{op}", method = {RequestMethod.PUT, RequestMethod.POST })
	public Result updateWzCommentsCountEx(
			@ApiParam(value = "域，desktop值")
			@PathVariable String realm,
			@ApiParam(value = "微作id")
			@PathVariable String wzId,
			@ApiParam(value = "操作类型，int类型，1：增加1；0：减少1")
			@PathVariable int op) {

		/*String parajson = super.request.getParameter("info");
		JSONObject jsonObj = JSONObject.parseObject(parajson);
		String userId = jsonObj.getString("user");
		String password = jsonObj.getString("password");*/
		//int op = jsonObj.getIntValue("op");
		//String wzId = jsonObj.getString("wzId");

		int count = this.projectService.updateWzCommentsCount(realm, wzId, op);

		return super.successResult(count);

	}

	@ApiOperation(value = "获取项目下的子任务", response = Result.class, notes = "getSubTasksByMasterNode")
	@RequestMapping(value = "case.subtask.get/{caseIdentifier}", method = RequestMethod.GET)
	public Result getSubTasksByMasterNode(@PathVariable @ApiParam(value = "案例标识，即项目编号") String caseIdentifier) {

		return super.successResult(this.projectService.getSubTasks(caseIdentifier));
	}

	@ApiOperation(value = "添加管理节点下的子任务", response = Result.class, notes = "addSubTask")
	@RequestMapping(value = "masternode.subtask.add/{caseIdentifier}/{masterNodeId}/{taskId}/{taskName}", method = RequestMethod.POST)
	public Result addSubTask(@PathVariable @ApiParam(value = "案例标识，即项目编号") String caseIdentifier,
			@PathVariable @ApiParam(value = "管理节点id") String masterNodeId,
			@ApiParam(value = "任务id") @PathVariable String taskId,
			@ApiParam(value = "任务名称") @PathVariable String taskName) {

		this.projectService.addTask(caseIdentifier, taskId, masterNodeId, taskName);

		return super.successResult("添加成功");
	}

	@ApiOperation(value = "删除管理节点下指定的子任务", response = Result.class, notes = "deleteSubTask")
	@RequestMapping(value = "masternode.subtask.delete/{taskId}", method = { RequestMethod.DELETE, RequestMethod.GET })
	public Result deleteSubTask(@ApiParam(value = "任务id") @PathVariable String taskId) {

		this.projectService.deleteTaskById(taskId);

		return super.successResult("删除成功");
	}

	@ApiOperation(value = "删除项目下的子任务", response = Result.class, notes = "deleteSubTaskByCaseIdentifier")
	@RequestMapping(value = "case.subtask.delete/{caseIdentifier}", method = { RequestMethod.DELETE,
			RequestMethod.GET })
	public Result deleteSubTaskByCaseIdentifier(@ApiParam(value = "案例标识，即项目编号") @PathVariable String caseIdentifier) {

		this.projectService.deleteTaskByCaseIdentifier(caseIdentifier);

		return super.successResult("删除成功");
	}

	@ApiOperation(value = "删除微作社交化数据", notes = "deleteWZSocialData")
	@RequestMapping(value = "deleteWZSocialData/{resId}", method = RequestMethod.GET)
	public Result deleteWZSocialData(@ApiParam(value = "资源id", required = true) @PathVariable String resId) {

		this.socialService.deleteSocialData_WZ(resId);
		this.sgaProjectService.deletePrjWzRecordByWzId(resId);

		return super.successResult("删除成功");
	}

	@ApiOperation(value = "根据微作id和用户，获取单个微作的社交信息", notes = "getSingleSocialWz")
	@RequestMapping(value = "getSingleSocialWz/{parajson:.+}", method = RequestMethod.GET)
	public Result getSingleSocialWz(@PathVariable String parajson) {
		try {
			SocialWzParaDTO dto = JSONObject.parseObject(parajson, SocialWzParaDTO.class);
			SocialWzSimpleResultDTO result = this.socialService.getSimpleWz(dto.getGuid(), dto.getCreator());
			return super.successResult(result);

		} catch (Exception ex) {
			return super.failResultException(ex);
		}
	}

	@ApiOperation(value = "根据微作id和用户，获取单个微作的社交信息", notes = "getSingleSocialWzGZ")
	@RequestMapping(value = "getSingleSocialWzGZ/{parajson:.+}", method = RequestMethod.GET)
	public Result getSingleSocialWzGZ(@PathVariable String parajson) {
		
		SocialWzParaDTO dto = JSONObject.parseObject(parajson, SocialWzParaDTO.class);// (SocialWzParaDTO) JSONUtil.toObject(parajson, SocialWzParaDTO.class);
		SocialWzSimpleResultDTO result = this.socialService.getSimpleWzGZ(dto.getGuid(), dto.getCreator());
		return super.successResult(result);
	}

	@ApiOperation(value = "根据微作id，获取单个微作的状态信息", notes = "getSingleSocialWzStatus")
	@RequestMapping(value = "getSingleSocialWzStatus/{parajson:.+}", method = RequestMethod.GET)
	public Result getSingleSocialWzStatus(@PathVariable String parajson) {
		
		SocialWzParaDTO dto =  JSONObject.parseObject(parajson, SocialWzParaDTO.class);
		SocialWzSimpleResultDTO result = this.socialService.getSimpleWzStatus(dto.getGuid());
		return super.successResult(result);
	}

	@Deprecated
	@ApiOperation(value = "保存微作社交化数据。（已过时）", notes = "addWzSocialData")
	@RequestMapping(value = "addWzSocialData/{parajson}", method = RequestMethod.GET)
	public Result addWzSocialData(@PathVariable String parajson) throws Exception {

		SocialWzParaDTO dto = JSONObject.parseObject(parajson, SocialWzParaDTO.class);
		this.socialService.saveSocialData_WZ(dto);
		return super.successResult("保存成功");
	}

	@ApiOperation(value = "保存微作社交化数据。（已过时）", notes = "addWzSocialDataGZ")
	@RequestMapping(value = "addWzSocialDataGZ/{parajson}", method = RequestMethod.GET)
	public Result addWzSocialDataGZ(@PathVariable String parajson) throws Exception {

		SocialWzParaDTO dto = JSONObject.parseObject(parajson, SocialWzParaDTO.class);
		this.socialService.saveSocialData_WZ_GZ(dto);
		return super.successResult("保存成功");
	}

	@ApiOperation(value = "保存微作社交化数据", notes = "addWzSocialData")
	@RequestMapping(value = "/addWzSocialData", method = {RequestMethod.POST})
	public Result addWzSocialData(@RequestBody SocialWzParaDTO dto) throws Exception {

		this.socialService.saveSocialData_WZ(dto);
		return super.successResult("保存成功");
	}
	
	@ApiOperation(value = "保存微作社交化数据，包括添加计数器，用于移动端和PC端", notes = "addWzSocialDataEx")
	@RequestMapping(value = "/wz/social", method = {RequestMethod.POST, RequestMethod.PUT})
	public Result addWzSocialDataEx(
			@ApiParam(value = "JSON信息，包括caseIdentifier(案例id)、creator(用户编码)、guid(微作id)、isFavorite(是否收藏true/false)、isTop(是否点赞true/false)、realm(域)、tag(标签)")
			@RequestBody final SocialWzParaDTO dto) throws Exception {

		this.socialService.saveSocialData_WZ(dto);
		DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				ProjectController.this.projectService.updateWzFavoriteCount(dto.getRealm(), dto.getGuid(), dto.getIsFavorite()? 1:0);
				ProjectController.this.projectService.updateWzUpvoteCount(dto.getRealm(), dto.getGuid(), dto.getIsTop()? 1:0);
			}
		});
		return super.successResult("保存成功");
	}
	
	@Deprecated
	@ApiOperation(value = "添加频道，默认为自定义类型", notes = "addChannel")
	@RequestMapping(value = "channel/{code}/{name}", method = { RequestMethod.POST })
	public Result addChannel(@ApiParam(value = "频道编码。如果传入-1，系统内部则会自定义编码。") @PathVariable String code,
			@ApiParam(value = "频道名称") @PathVariable String name) throws Exception {

		return super.successResult(this.projectService.addChannel(code.replace("-1", ""), name, 0L));

	}

	@ApiOperation(value = "添加频道，默认为自定义类型", notes = "addChannelPost")
	@RequestMapping(value = "channel", method = { RequestMethod.POST })
	public Result addChannelPost(
			@ApiParam(value = "ChannelDTO，频道编码。如果传入-1，系统内部则会自定义编码。频道名称，案例id") @RequestBody ChannelDTO dto)
			throws Exception {

		return super.successResult(this.projectService.addChannel(dto, 0L));

	}

	@ApiOperation(value = "添加内置频道", notes = "addChannelBuildIn")
	@RequestMapping(value = "channel/buildin", method = { RequestMethod.POST })
	public Result addChannelBuildIn(
			@ApiParam(value = "ChannelDTO，频道编码。如果传入-1，系统内部则会自定义编码。频道名称，案例id") @RequestBody ChannelDTO dto)
			throws Exception {

		return super.successResult(this.projectService.addChannel(dto, 1L));

	}

	@ApiOperation(value = "获取频道", notes = "listChannels")
	@RequestMapping(value = "channel/", method = { RequestMethod.GET })
	public Result listChannels() {

		return super.successResult(this.projectService.listChannels());

	}

	@ApiOperation(value = "根据案例，获取频道", notes = "listChannels")
	@RequestMapping(value = "channel/{caseId}", method = { RequestMethod.GET })
	public Result listChannels(@PathVariable String caseId) {

		return super.successResult(this.projectService.listChannels(caseId));

	}

	@ApiOperation(value = "删除频道", notes = "deleteChannel")
	@RequestMapping(value = "channel/{id}", method = { RequestMethod.DELETE})
	public Result deleteChannel(@PathVariable Long id) {

		return super.successResult(this.projectService.deleteChannel(id));

	}

	@ApiOperation(value = "更新频道", notes = "updateChannel")
	@RequestMapping(value = "channel/{id}/{newName}", method = { RequestMethod.PUT, RequestMethod.GET })
	public Result updateChannel(@ApiParam(value = "频道的序列id") @PathVariable Long id,
			@ApiParam(value = "新名字") @PathVariable String newName) {

		return super.successResult(this.projectService.updateChannel(id, newName));

	}

	@ApiOperation(value = "获取项目中的合作包", response = Result.class, notes = "getCooperationPackage")
	@RequestMapping(value = "/project/coopkg/{realm}/{caseId}", method = { RequestMethod.GET })
	public Result getCooperationPackage(@ApiParam(value = "域") @PathVariable String realm,
			@ApiParam(value = "案例id") @PathVariable String caseId) {

		return super.successResult(this.projectService.getCooperationPackage(realm, caseId));
	}

	@ApiOperation(value = "根据案例id获取案例的信息", response = Result.class, notes = "getCaseById")
	@RequestMapping(value = "/case/id/{realm}/{caseId}", method = { RequestMethod.GET })
	public Result getCaseById(@ApiParam(value = "域，如：thupdi") @PathVariable String realm,
			@ApiParam(value = "案例id，如：{406BD258-0000-C01C-8998-BD3EA61BE2B4}") @PathVariable String caseId) {

		return super.successResult(this.projectService.getCaseById(realm, caseId));
	}
	
	@ApiOperation(value = "添加项目团队", response = Result.class, notes = "addTeam")
	@RequestMapping(value = "/team/{caseId}/{name}", method = { RequestMethod.POST })
	public Result addTeam(
			@ApiParam(value = "案例id，如：{406BD258-0000-C01C-8998-BD3EA61BE2B4}") 
			@PathVariable String caseId,
			@ApiParam(value = "团队名称")
			@PathVariable
			String name) {

		return super.successResult(this.projectService.addTeam(caseId, name));
	}
	
	@ApiOperation(value = "删除项目团队", response = Result.class, notes = "addTeam")
	@RequestMapping(value = "/team/{teamId}", method = { RequestMethod.DELETE })
	public Result deleteTeamById(
			@ApiParam(value = "团队id") 
			@PathVariable Long teamId) {

		this.projectService.deleteTeam(teamId);
		return super.successResult("删除成功");
	}

	@ApiOperation(value = "修改团队名称", response = Result.class, notes = "modifyProjectTeamName")
	@RequestMapping(value = "/team/name/{teamId}/{newName}", method = { RequestMethod.PUT })
	public Result modifyProjectTeamName(
			@ApiParam(value = "团队id") 
			@PathVariable Long teamId,
			@ApiParam(value = "团队新名称")
			@PathVariable
			String newName) {

		return super.successResult(this.projectService.modifyTeamName(teamId, newName));
	}
	
	@ApiOperation(value = "添加团队成员（切换到/team/user/{caseId}/{teamId}/{userId}）", response = Result.class, notes = "addTeamUser")
	@RequestMapping(value = "/team/user/{teamId}/{userId}", method = { RequestMethod.POST })
	public Result addTeamUser(
			@PathVariable
			@ApiParam(value = "团队id")
			Long teamId, 
			@PathVariable
			@ApiParam(value = "用户序列id")
			Long userId){
		
		return super.successResult(this.projectService.addTeamUser(teamId, userId));
	}
	@ApiOperation(value = "添加团队成员", response = Result.class, notes = "addTeamUser")
	@RequestMapping(value = "/v1/team/user/{caseId}/{teamId}/{userId}", method = { RequestMethod.POST })
	public Result addTeamUser(
			@ApiParam(value = "案例id，如：{406BD258-0000-C01C-8998-BD3EA61BE2B4}") 
			@PathVariable String caseId,
			@PathVariable
			@ApiParam(value = "团队id")
			Long teamId, 
			@PathVariable
			@ApiParam(value = "用户序列id")
			Long userId) {
		
		return super.successResult(this.projectService.addTeamUser(caseId, teamId, userId));
	}
	
	@ApiOperation(value = "删除团队成员", response = Result.class, notes = "deleteTeamUser")
	@RequestMapping(value = "/team/user", method = { RequestMethod.DELETE })
	public Result deleteTeamUser(
			@Valid
			@RequestBody
			@ApiParam(value = "团队成员删除的传入参数模型")
			TeamUserDelDTO dto, BindingResult result){
		
		if(result.hasErrors())
			return super.errorResult("参数验证失败，详情："+result.toString());
		
		this.projectService.deleteTeamUsers(dto.getTeamId(), dto.getUserIds());
		return super.successResult("删除成功");
	}
	
	@ApiOperation(value = "获取项目的团队信息，包括人员关联", response = Result.class, notes = "getProjectTeam")
	@RequestMapping(value = "/teams/{caseId}", method = { RequestMethod.GET })
	public Result getProjectTeams(
			@PathVariable
			@ApiParam(value = "项目标识符，即ce中的case id")
			String caseId){
		
		return super.successResult(this.projectService.getTeams(caseId));
	}
	
	@ApiOperation(value = "设置团队队长", response = Result.class, notes = "setTeamLeader")
	@RequestMapping(value = "/team/leader", method = { RequestMethod.POST, RequestMethod.PUT})
	public Result setTeamLeader(@RequestBody TeamLeaderRequestDTO dto){
		
		return super.successResult(this.projectService.setTeamLeader(dto));
	}
	@Deprecated
	@ApiOperation(value = "添加任务(设计不合理，切换到/tasks)", response = Result.class, notes = "addTask")
	@RequestMapping(value = "/task", method = { RequestMethod.POST})
	public Result addTask(
			@Valid
			@ApiParam(value = "请求参数模型，时间以毫秒为单位")
			@RequestBody TaskAddDTO dto, BindingResult result){
		
		if(result.hasErrors())
			return super.errorResult("参数验证失败，详情："+result.toString());
		
		this.projectService.saveOrUpdateTask(dto);
		return super.successResult("添加完成");
	}
	
	@ApiOperation(value = "添加或者更新任务", response = Result.class, notes = "saveOrUpdateTask")
	@RequestMapping(value = "/tasks", method = { RequestMethod.POST, RequestMethod.PUT})
	public Result saveOrUpdateTask(
			@Valid
			@ApiParam(value = "请求参数模型，时间以毫秒为单位")
			@RequestBody TaskAddDTO dto, BindingResult result) {
		
		if(result.hasErrors())
			return super.errorResult("参数验证失败，详情："+result.toString());
		
		this.projectService.saveOrUpdateTask(dto);
		return super.successResult("添加完成");
	}
	@ApiOperation(value = "删除任务", response = Result.class, notes = "deleteTask")
	@RequestMapping(value = "/tasks/{taskId}", method = { RequestMethod.DELETE})
	public Result deleteTask(
			@ApiParam(value = "任务id")
			@PathVariable String taskId) {
		
		this.projectService.deleteTaskById(taskId);
		return super.successResult("添加完成");
	}
	
	@ApiOperation(value = "获取项目下的任务开始和结束时间", response = Result.class, notes = "getTasksByCaseId")
	@RequestMapping(value = "/v1/tasks/{caseId}", method = { RequestMethod.GET})
	public Result getTasksByCaseId(
			@ApiParam(value = "案例标识")
			@PathVariable String caseId){
		
		return super.successResult(this.projectService.getTasksByCaseId(caseId));
	}
	
	@ApiOperation(value = "根据任务id获取相关属性", response = Result.class, notes = "getTaskById")
	@RequestMapping(value = "/v1/task/{taskId}", method = { RequestMethod.GET})
	public Result getTaskById(
			@ApiParam(value = "任务id")
			@PathVariable String taskId){
		
		return super.successResult(this.projectService.getTaskById(taskId));
	}

	@ApiOperation(value = "根据微作id，获取微作对象", response = Result.class, notes = "getWZById")
	@RequestMapping(value = "/wz/{realm}/{id}", method = { RequestMethod.GET})
	public Result getWZById(
			@ApiParam(value = "域，desktop值")
			@PathVariable String realm, 
			@ApiParam(value = "微作id")
			@PathVariable String id){
		
		return super.successResult(this.projectService.getWZById(realm, id));
	}
	@ApiOperation(value = "根据微作id，获取微作关联的图片，针对移动调研的微作", response = Result.class, notes = "getImageUrlsOfWz")
	@RequestMapping(value = "/v1/wz/img.url/{realm}/{id}", method = { RequestMethod.GET})
	public Result getImageUrlsOfWz(
	@ApiParam(value = "域，desktop值")
	@PathVariable String realm, 
	@ApiParam(value = "微作id")
	@PathVariable String id
	) {
		List<String> fileVids = this.projectService.getRefFileIdsOfWz(realm, id);
		if(null == fileVids || fileVids.isEmpty()) {
			return super.failResult("没有获取到文件列表");
		}
		Map<String, byte[]> bytes = this.ecmMgmtService.getImgContentStreamByVID(realm, fileVids);
		String fileName = "";
		Map<String, String> imgs = new LinkedHashMap<String, String>();
		
		for(Entry<String, byte[]> entry : bytes.entrySet()) {
			fileName = entry.getKey() + ".jpg";
			String dir = super.getContextPath(GlobalSystemParameters.DIR_TEMP);

			if (new File(dir + File.separatorChar + fileName).exists()) {
				LOG.info(">>>找到缓存文件：[{}]", fileName);
				imgs.put(entry.getKey() ,super.getBaseURL() + GlobalSystemParameters.DIR_TEMP + File.separatorChar + fileName);
				continue;
			}
			boolean result = FileUtil.createFile(entry.getValue(), dir, fileName);
			if (result) {
				imgs.put(entry.getKey(), super.getBaseURL() + GlobalSystemParameters.DIR_TEMP + File.separatorChar + fileName);
			}
		}
		return super.successResult(imgs);
	}
	@ApiOperation(value = "根据微作id，获取微作对象，返回缩略图URL，替代base64编码", response = Result.class, notes = "getWZByIdV1")
	@RequestMapping(value = "/v1/wz/{realm}/{id}", method = { RequestMethod.GET})
	public Result getWZByIdV1(
			@ApiParam(value = "域，desktop值")
			@PathVariable String realm, 
			@ApiParam(value = "微作id")
			@PathVariable String id){
		
		WzInfoDTO wz = this.projectService.getWZByIdThumbnailByte(realm, id);
		if(wz != null) {
			List<WzInfoDTO> wzList = new ArrayList<WzInfoDTO>();
			wzList.add(wz);
			this.commonCtl.generateWzThumbnail(super.request, wzList);
		}
		return super.successResult(wz);
	}
	
	@ApiOperation(value = "根据微作id，获取微作对象", response = Result.class, notes = "getWZById")
	@RequestMapping(value = "/v1/mobile/wz/{realm}/{id}", method = { RequestMethod.GET})
	public Result getWZByIdForMobile(
			@ApiParam(value = "域，desktop值")
			@PathVariable String realm, 
			@ApiParam(value = "微作id")
			@PathVariable String id){
		
		return super.successResult(this.projectService.getWZByIdForMobile(realm, id, super.getBaseURL()+"/thumb", super.getContextPath("thumb")));
	}
	
	@ApiOperation(value = "通过邮箱发送微作给@的人", response = Result.class, notes = "sendWzToUserByEmail")
	@RequestMapping(value = "/wz/send/email", method = { RequestMethod.POST})
	public Result sendWzToUserByEmail(
			@ApiParam(value = "域，desktop值")
			@RequestBody WzInfoSendToUserDTO dto){

		return super.successResult(this.projectService.sendWzToUserByEmail(dto));
	} 
    @ApiOperation(value = "分页模糊检索项目名称", response = Result.class, notes = "fuzzySearchProjectName")
	@RequestMapping(value = "/v1/prjnames/fuzzy/{realm}/{pageNo}/{pageSize}/{keyword}", method = { RequestMethod.GET})
	public Result fuzzySearchProjectName(@PathVariable String realm, @PathVariable Integer pageNo, @PathVariable Integer pageSize, @PathVariable String keyword) {

		return super.successResult(this.projectService.fuzzySearchProjectName(realm, pageNo, pageSize, keyword));
	} 
	@ApiOperation(value = "删除微作实体。", response = Result.class, notes = "deleteWzById")
	@RequestMapping(value = "/v1/wz/{realm}/{resId}/{userId}", method = { RequestMethod.DELETE})
	public Result deleteWzById(@PathVariable String realm, @PathVariable String resId, @PathVariable String userId) {
		
		WzInfoDTO wz = this.projectService.getWZById(realm, resId);
		if(Objects.equal(wz.getPublisher(), userId)) {
			LOG.info(">>>删除微作id[{}] 实体信息", resId);
			this.projectService.deleteWzById(realm, resId);
			LOG.info(">>>删除微作id[{}] 社交化信息", resId);
			this.socialService.deleteSocialData_WZ(resId);
			LOG.info(">>>删除微作id[{}] 公共信息", resId);
			this.sgaProjectService.deletePrjWzRecordByWzId(resId);
			
			return super.successResult("删除完成");
		} else {
			LOG.warn(">>>当前用户[{}]对此微作没有权限", userId);
			return super.failResult(String.format("当前用户[%s]不是微作[%s]的发布者，对此微作没有权限", userId, resId), "删除失败");
		}
	}
	@ApiOperation(value = "同步任务发布时间", response = Result.class, notes = "syncTaskPublishTime")
	@RequestMapping(value = "/v1/tasks/publishtime/sync/{realm}", method = { RequestMethod.PUT})
	public Result syncTaskPublishTime(@PathVariable String realm) {

		this.projectService.syncTaskPublishTime(realm);
		return super.successResult("任务发布时间完成同步");
	} 
	@ApiOperation(value = "获取项目所有用户信息，包括院内和院外", response = Result.class, notes = "getProjectUsers")
	@RequestMapping(value = "/v1/project/users/{caseId}", method = { RequestMethod.GET})
	public Result getProjectUsers(@PathVariable String caseId) {

		return super.successResult(this.projectService.getProjectUsers(caseId, super.getContextPath(), super.getBaseURL()));
	} 
	@ApiOperation(value = "检测用户在项目的有效性", response = Result.class, notes = "checkUserInProject")
	@RequestMapping(value = "/v1/project/checkuser/{caseId}/{userCode}", method = { RequestMethod.GET})
	public Result checkUserInProject(@PathVariable String caseId, @PathVariable String userCode) {

		return super.successResult(this.projectService.checkUserInProject(caseId, userCode));
	} 
	@ApiOperation(value = "设置项目背景图，并存放到mongodb去", notes = "uploadProjectImg")
	@RequestMapping(value = "/v1/project/img/mongo", method = { RequestMethod.POST, RequestMethod.PUT })
	public Result uploadProjectImg(
			@ApiParam( value = "json数据，{\"id\":用户编码,\"type\":\"image/jpeg\",\"suffix\":\"jpg\",\"content\":\"base64值\"}")
			@RequestBody ImgInfo imgInfo) {
		
		try {
			LOG.info(">>>删除mongo file，code："+imgInfo.getId());
			this.projectService.deleteImgInMongoByProjectCode(imgInfo.getId().replace("{", "").replace("}", ""));
			
			LOG.info(">>>projectCode:[{}], mime-type:[{}], suffix:[{}] ", imgInfo.getId(), imgInfo.getType(), imgInfo.getSuffix());
	    	String mogoFileId = this.projectService.storeImgToMongo(imgInfo);//fileStorageDao.store(FileUtil.base64ToInputStream(imgInfo.getContent()) , newFileName, imgInfo.getType(), metaData);
	    	LOG.info(">>>mongo file id : "+ mogoFileId);

			String projectImgURL = this.projectService.updateProjectImgURL(imgInfo.getId(), "rest/sysservice/project/img/fs/"+mogoFileId);
			LOG.info(">>>project img url：" + projectImgURL);
			return super.successResult(projectImgURL);
			
		} catch (Exception ex) {
			LOG.error(">>>设置项目背景图失败，详情：{}", ex.getMessage());
			return super.failResult("设置项目背景图失败，详情："+ex.getMessage(), "项目背景图失败");
		}
	}
	@ApiOperation(value = "更新是否热门项目状态", notes = "updateHotProjectStatus")
	@RequestMapping(value = "/v1/hotproject/status/{realm}/{caseId}/{status}", method = { RequestMethod.POST, RequestMethod.PUT })
	public Result updateHotProjectStatus(
			@ApiParam(value = "域", required = true)
			@PathVariable String realm, 
			@ApiParam(value = "案例guid", required = true)
			@PathVariable String caseId, 
			@ApiParam(value = "状态。0：非热门；1：热门", required = true)
			@PathVariable int status) {
		
		return super.successResult(this.projectService.updateHotProjectStatus(realm, caseId, status));
	}
	@ApiOperation(value = "获取项目背景图url", notes = "getProjectImgURL")
	@RequestMapping(value = "/v1/project/imgurl/{caseId}", method = { RequestMethod.GET })
	public Result getProjectImgURL(
			@ApiParam(value = "案例guid", required = true)
			@PathVariable String caseId) {
		
		Object result = this.projectService.getProjectImgURL(caseId);
		if(null == result) {
			LOG.warn(">>>没有找到项目的背景图信息：" + caseId);
		}
		return super.successResult(result);
	}
	@ApiOperation(value = "添加工作组", notes = "addWorkGroup")
	@RequestMapping(value = "/v1/project/workgroups", method = { RequestMethod.POST })
	public Result addWorkGroup(
			@Valid
			@ApiParam(value = "工作组模型", required = true)
			@RequestBody WorkGroupAddDTO dto, BindingResult result) {
		if(result.hasErrors()) {
			return super.failResult("参数验证失败，详情：" + result.toString());
		}
		return super.successResult(this.projectService.addWorkGroup(dto));
	}
	@ApiOperation(value = "删除工作组", notes = "deleteWorkGroup")
	@RequestMapping(value = "/v1/project/workgroups/{workgroupId}", method = { RequestMethod.DELETE })
	public Result deleteWorkGroup(
			@ApiParam(value = "工作组id", required = true)
			@PathVariable Long workgroupId) {
		this.projectService.deleteWorkGroup(workgroupId);
		return super.successResult("成功删除工作组");
	}
	@ApiOperation(value = "添加机构到工作组", notes = "addOrgToWorkgroup")
	@RequestMapping(value = "/v1/project/workgroups/org", method = { RequestMethod.POST })
	public Result addOrgToWorkgroup(
			@Valid
			@ApiParam(value = "工作组和机构模型", required = true)
			@RequestBody WorkGroupOrgAddDTO dto, BindingResult result) {
		if(result.hasErrors()) {
			return super.failResult("参数验证失败，详情：" + result.toString());
		}
		return super.successResult(this.projectService.addOrgToWorkgroup(dto));
	}

	@ApiOperation(value = "从工作组删除机构关联", notes = "deleteOrgFromWorkgroup")
	@RequestMapping(value = "/v1/project/workgroups/org/{workgroupId}/{orgGuid}", method = { RequestMethod.DELETE })
	public Result deleteOrgFromWorkgroup(@PathVariable Long workgroupId, @PathVariable String orgGuid) {
		this.projectService.deleteOrgFromWorkgroup(workgroupId, orgGuid);
		return super.successResult("工作组中已删除指定机构："+orgGuid);
	}
	@ApiOperation(value = "根据项目唯一标识删除所有工作组", notes = "deleteWorkgroupsByProjectGuid")
	@RequestMapping(value = "/v1/project/workgroups/all/{projectGuid}", method = { RequestMethod.DELETE })
	public Result deleteWorkgroupsByProjectGuid(@PathVariable String projectGuid) {
		this.projectService.deleteWorkgroupsByProjectGuid(projectGuid);
		return super.successResult("已从项目删除相关工作组");
	}
	@ApiOperation(value = "根据项目唯一标识获取相关工作组信息", notes = "getWorkgroupsByProjectGuid")
	@RequestMapping(value = "/v1/project/workgroups/{projectGuid}", method = { RequestMethod.GET })
	public Result getWorkgroupsByProjectGuid(
			@ApiParam(value = "项目guid", required = true)
			@PathVariable String projectGuid) {
		return super.successResult(this.projectService.getWorkgroupsByProjectGuid(projectGuid));
	}
	@ApiOperation(value = "添加脑图和团队的关联关系", notes = "addNaotuTeamRef")
	@RequestMapping(value = "/v1/naotu/teams", method = { RequestMethod.POST })
	public Result addNaotuTeamRef(
			@Valid
			@RequestBody NaotuTeamAddDTO dto, BindingResult result) {
		
		if(result.hasErrors()) {
			return super.errorResult(result.toString());
		}
		return super.successResult(this.projectService.addNaotuTeamRef(dto));
	}
	@ApiOperation(value = "删除脑图和团队的关联关系", notes = "deleteNaotuTeamRef")
	@RequestMapping(value = "/v1/naotu/teams/{realm}/{caseId}/{minderId}/{nodeId}/{teamId}", method = { RequestMethod.DELETE })
	public Result deleteNaotuTeamRef(
			@ApiParam(value = "域", required = true)
			@PathVariable String realm,
			@ApiParam(value = "案例guid", required = true)
			@PathVariable String caseId,
			@ApiParam(value = "脑图id", required = true)
			@PathVariable int minderId,
			@ApiParam(value = "脑图节点id", required = true)
			@PathVariable String nodeId,
			@ApiParam(value = "团队id", required = true)
			@PathVariable String teamId
			) {
		
		return super.successResult(this.projectService.deleteNaotuTeamRef(realm, caseId, minderId, nodeId, teamId));
	}
	@ApiOperation(value = "删除脑图节点，级联删除和团队的关联关系", notes = "deleteNaotuTeamRef")
	@RequestMapping(value = "/v1/naotu/teams/{realm}/{caseId}/{minderId}/{nodeId}", method = { RequestMethod.DELETE })
	public Result deleteNaotuTeamRef(
			@ApiParam(value = "域", required = true)
			@PathVariable String realm,
			@ApiParam(value = "案例guid", required = true)
			@PathVariable String caseId,
			@ApiParam(value = "脑图id", required = true)
			@PathVariable int minderId,
			@ApiParam(value = "脑图节点id", required = true)
			@PathVariable String nodeId
			) {
		
		return super.successResult(this.projectService.deleteNaotuTeamRef(realm, caseId, minderId, nodeId));
	}
	@ApiOperation(value = "删除脑图，级联删除和团队的关联关系", notes = "deleteNaotuTeamRef")
	@RequestMapping(value = "/v1/naotu/teams/{realm}/{caseId}/{minderId}", method = { RequestMethod.DELETE })
	public Result deleteNaotuTeamRef(
			@ApiParam(value = "域", required = true)
			@PathVariable String realm,
			@ApiParam(value = "案例guid", required = true)
			@PathVariable String caseId,
			@ApiParam(value = "脑图id", required = true)
			@PathVariable int minderId
			) {
		
		return super.successResult(this.projectService.deleteNaotuTeamRef(realm, caseId, minderId));
	}
	@ApiOperation(value = "获取脑图和团队的关联关系", notes = "getNaotuRefTeams")
	@RequestMapping(value = "/v1/naotu/teams/{realm}/{caseId}/{minderId}", method = { RequestMethod.GET })
	public Result getNaotuRefTeams(
			@PathVariable String realm, @PathVariable String caseId, @PathVariable Integer minderId) {
		return super.successResult(this.projectService.getNaotuRefTeams(realm, caseId, minderId));
	}
	@ApiOperation(value = "获取团队组", notes = "addNaotuTeamRef")
	@RequestMapping(value = "/v1/teamgroups/{realm}/{pageNo}/{pageSize}", method = { RequestMethod.GET })
	public Result getTeamGroups(
			@ApiParam(value = "域", required = true)
			@PathVariable String realm, 
			@ApiParam(value = "页码，从1开始", required = true)
			@PathVariable int pageNo, 
			@ApiParam(value = "每页大小", required = true)
			@PathVariable int pageSize) {
		return super.successResult(this.projectService.getTeamGroups(realm, pageNo, pageSize));
	}
}
