package com.dist.bdf.consumer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dist.bdf.base.controller.BaseController;
import com.dist.bdf.base.result.Result;
import com.dist.bdf.model.dto.system.RoleDTO;
import com.dist.bdf.model.dto.system.UserResPrivRequestDTO;
import com.dist.bdf.model.dto.system.priv.PrivTemplateDTO;
import com.dist.bdf.model.entity.system.DcmGroup;
import com.dist.bdf.model.entity.system.DcmOrganization;
import com.dist.bdf.model.entity.system.DcmRole;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.dist.bdf.facade.service.GroupService;
import com.dist.bdf.facade.service.PrivilegeService;
import com.dist.bdf.facade.service.RoleService;
import com.dist.bdf.facade.service.UserOrgService;
import com.dist.bdf.common.constants.DomainType;
import com.dist.bdf.common.constants.ResourceConstants;
import com.dist.bdf.common.constants.RoleConstants;

@Api(tags = { "API-权限服务模块" }, description = "PrivilegeController")
@RestController
@RequestMapping(value = "/rest/sysservice")
//@CrossOrigin(origins = "*")
public class PrivilegeController extends BaseController {

	@Autowired
	private PrivilegeService privService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private UserOrgService userService;

	// <=====用户-空间域-角色模块begin

	/**
	 * 对于外部用户就不适合了
	 * @return
	 */
	/*@Deprecated
	@ApiOperation(value = "修改用户在某个空间域中的角色", notes = "modifyUserRole")
	@RequestMapping(value = "/role/user/{userSeqId}/{domainCode}/{roleCode}", method = {RequestMethod.PUT})
	public Result modifyUserRole(
			@PathVariable
			@ApiParam(value = "用户序列id")
			Long userSeqId, 
			@PathVariable
			@ApiParam(value = "空间域编码")
			String domainCode, 
			@PathVariable
			@ApiParam(value = "角色编码")
			String roleCode){
		
		this.privService.modifyUserRole(userSeqId, domainCode, roleCode);
		return super.successResult("修改成功");
	}*/
	
	@ApiOperation(value = "修改用户在某个空间域中的角色", notes = "modifyUserRoleOfDomain")
	@RequestMapping(value = "/role/user/{userCode}/{domainCode}/{roleCode}", method = {RequestMethod.PUT})
	public Result modifyUserRoleOfDomain(
			@PathVariable
			@ApiParam(value = "用户唯一编码")
			String userCode, 
			@PathVariable
			@ApiParam(value = "空间域编码")
			String domainCode, 
			@PathVariable
			@ApiParam(value = "角色编码")
			String roleCode){
		
		this.privService.modifyUserRoleOfDomain(userCode, domainCode, roleCode);
		return super.successResult("修改成功");
	}
	
	@ApiOperation(value = "修改用户在项目中是否大牛", notes = "modifyUserIstop")
	@RequestMapping(value = "/role/user/top/{userSeqId}/{domainCode}/{top}", method = {RequestMethod.PUT})
	public Result modifyUserIstop(
			@PathVariable
			@ApiParam(value = "用户序列id")
			Long userSeqId, 
			@PathVariable
			@ApiParam(value = "空间域编码")
			String domainCode, 
			@PathVariable
			@ApiParam(value = "是否大牛，0：否；1：是")
			int top){
		
		this.privService.modifyUserIstop(userSeqId, domainCode, top);
		return super.successResult("修改成功");
	}

	// ======>用户-空间域-角色模块end

	// <=====角色模块begin

	@ApiOperation(value = "获取角色列表", notes = "listAllRoles")
	@RequestMapping(value = "listAllRoles", method = { RequestMethod.GET })
	public Result listAllRoles() {
		try {

			List<DcmRole> roles = this.roleService.listAllRoles();
			return super.successResult(roles);

		} catch (Exception ex) {
			return super.failResultException(ex);
		}
	}

	@ApiOperation(value = "获取跟项目相关的角色", notes = "getRolesOfProject")
	@RequestMapping(value = "/role/prj", method = { RequestMethod.GET })
	public Result getRolesOfProject() {
		
		List<DcmRole> roles = this.roleService.getRolesOfProject();
		return super.successResult(roles);
	}
	@ApiOperation(value = "获取域下跟项目相关的角色", notes = "getRolesOfProjectByRealm")
	@RequestMapping(value = "/roles/v1/prj/{realm}", method = { RequestMethod.GET })
	public Result getRolesOfProjectByRealm(@PathVariable String realm) {

		return super.successResult(this.roleService.getRolesOfProject(realm));
	}
	@ApiOperation(value = "获取域下跟团队相关的角色", notes = "getRolesOfTeamByRealm")
	@RequestMapping(value = "/roles/v1/team/{realm}", method = { RequestMethod.GET })
	public Result getRolesOfTeamByRealm(@PathVariable String realm) {

		return super.successResult(this.roleService.getRolesOfTeam(realm));
	}

	@Deprecated
	@ApiOperation(value = "获取跟机构相关的角色", notes = "getRolesOfOrg")
	@RequestMapping(value = "/role/org", method = { RequestMethod.GET })
	public Result getRolesOfOrg() {
		
		List<DcmRole> roles = this.roleService.getRolesOfOrg();
		return super.successResult(roles);
	}
	
	@ApiOperation(value = "获取跟院类型相关的角色", notes = "getRolesOfInstitute")
	@RequestMapping(value = "/roles/institute", method = { RequestMethod.GET })
	public Result getRolesOfInstitute() {

		return super.successResult( this.roleService.getRolesOfOrg(RoleConstants.RoleType.T_Institute));
	}
	@ApiOperation(value = "获取域下跟院类型相关的角色", notes = "getRolesOfInstituteByRealm")
	@RequestMapping(value = "/roles/v1/institute/{realm}", method = { RequestMethod.GET })
	public Result getRolesOfInstituteByRealm(@PathVariable String realm) {

		return super.successResult( this.roleService.getRolesOfOrg(realm ,RoleConstants.RoleType.T_Institute));
	}
	@ApiOperation(value = "获取跟所类型相关的角色", notes = "getRolesOfDepartment")
	@RequestMapping(value = "/roles/department", method = { RequestMethod.GET })
	public Result getRolesOfDepartment() {

		return super.successResult( this.roleService.getRolesOfOrg(RoleConstants.RoleType.T_Department));
	}
	@ApiOperation(value = "获取域下跟所类型相关的角色", notes = "getRolesOfDepartmentByRealm")
	@RequestMapping(value = "/roles/v1/department/{realm}", method = { RequestMethod.GET })
	public Result getRolesOfDepartmentByRealm(@PathVariable String realm) {
		return super.successResult( this.roleService.getRolesOfOrg(realm, RoleConstants.RoleType.T_Department));
	}

	@ApiOperation(value = "添加角色", notes = "addRole")
	@RequestMapping(value = "addRole/{parajson:.+}", method = { RequestMethod.POST })
	public Result addRole(@ApiParam(value = "角色json格式") @PathVariable String parajson) {
		
		RoleDTO dto = JSONObject.parseObject(parajson, RoleDTO.class);
		DcmRole role = this.roleService.addRole(dto);
		return super.successResult(role);

	}
	
	@ApiOperation(value = "更新角色", notes = "modifyRole")
	@RequestMapping(value = "modifyRole/{parajson:.+}", method = { RequestMethod.PUT })
	public Result modifyRole(@PathVariable String parajson) {
		
		RoleDTO dto = JSONObject.parseObject(parajson, RoleDTO.class);
		DcmRole role = this.roleService.updateRole(dto);
		return super.successResult(role);
	}


	@ApiOperation(value = "根据角色id集合删除信息", notes = "delRoles")
	@RequestMapping(value = "delRoles/{parajson:.+}", method = { RequestMethod.DELETE })
	public Result delRoles(@PathVariable String parajson) {

		List<Long> roleIds = JSONObject.parseArray(parajson, Long.class);// JSONUtil.toArray(parajson, Long.class);

		this.roleService.delRoles(roleIds);
		return super.successResult("成功删除角色");

	}
	// ======>角色模块end

	/*
	 * @ApiOperation(value="getResMasks", notes = "获取用户对资源的权限mask值")
	 * 
	 * @RequestMapping(value = "getResMasks/{parajson}", method =
	 * RequestMethod.GET) public Result getResMasks(
	 * 
	 * @ApiParam(value = "json格式的用户、资源信息。")
	 * 
	 * @PathVariable String parajson) {
	 * 
	 * JSONObject jsonObj = JSONObject.fromObject(parajson);
	 * 
	 * Long masks =
	 * this.privService.getDocMasksOfUserRes(jsonObj.getString("user"),
	 * jsonObj.getString("resId"));
	 * 
	 * return super.successResult(masks);
	 * 
	 * }
	 */
	@Deprecated
	@ApiOperation(value = "获取用户对资源的权限编码（已过时，切换到接口/resPriv/codes）", notes = "getUserResPrivCodes")
	@RequestMapping(value = "getResPrivCodes/{parajson}", method = { RequestMethod.GET, RequestMethod.POST })
	public Result getUserResPrivCodes(
			@ApiParam(value = "json格式的用户、资源信息。key : user（用户dn）、resId、realm") @PathVariable String parajson) {

		JSONObject jsonObj = JSONObject.parseObject(parajson);

		return super.successResult(this.privService.getDocPrivCodes(jsonObj.getString("realm"),
				jsonObj.getString("user"), jsonObj.getString("resId")));
		// return
		// super.successResult(this.privService.getDocPrivCodesOfUserRes(jsonObj.getString("user"),
		// jsonObj.getString("resId")));
	}

	@Deprecated
	@ApiOperation(value = "获取用户对资源的权限编码，目前PC端在使用，后续要切换到/res/priv/codes", notes = "getUserResPrivCodesEx")
	@RequestMapping(value = "/resPriv/codes/{parajson}", method = { RequestMethod.GET })
	public Result getUserResPrivCodesEx(
			@ApiParam(value = "json格式的用户、资源信息。key : user（用户dn）、resId、realm、parentId") @PathVariable String parajson)
			throws Exception {

		UserResPrivRequestDTO dto = JSONObject.parseObject(parajson, UserResPrivRequestDTO.class);

		return super.successResult(this.privService.getDocPrivCodes(dto));
		// return
		// super.successResult(this.privService.getDocPrivCodesOfUserRes(jsonObj.getString("user"),
		// jsonObj.getString("resId")));
	}

	@Deprecated
	@ApiOperation(value = "获取用户对资源的权限编码（建议切换到/v1/res/priv/codes）", notes = "getUserResPrivCodes")
	@RequestMapping(value = "/res/priv/codes", method = { RequestMethod.POST })
	public Result getUserResPrivCodes(
			@ApiParam(value = "json格式的用户、资源信息。key : user（用户dn）、resId、realm、parentId、from（文件入口来源，0：默认入口；1：共享文件夹入口）") @RequestBody UserResPrivRequestDTO dto)
			throws Exception {

		// UserResPrivRequestDTO dto = (UserResPrivRequestDTO)
		// JSONUtil.toObject(parajson, UserResPrivRequestDTO.class);
		return super.successResult(this.privService.getDocPrivCodesEx(dto));
		// return
		// super.successResult(this.privService.getDocPrivCodesOfUserRes(jsonObj.getString("user"),
		// jsonObj.getString("resId")));
	}

	@ApiOperation(value = "获取用户对资源的权限编码，特指文档资源", notes = "getUserResPrivCodesV1")
	@RequestMapping(value = "/v1/res/priv/codes", method = { RequestMethod.POST })
	public Result getUserResPrivCodesV1(
			@ApiParam(value = "json格式的用户、资源信息。user（用户编码）、resId、realm、parentId、from（文件入口来源，0：默认入口；1：共享文件夹入口）") 
			@RequestBody UserResPrivRequestDTO dto)
			throws Exception {

		return super.successResult(this.privService.getDocPrivCodesV1(dto));
	}
	
	@Deprecated
	@ApiOperation(value = "以安全的方式，密码是密文，获取用户对资源的权限编码", notes = "getUserResPrivCodesSecurity")
	@RequestMapping(value = "resPrivCodes/security/{parajson}", method = RequestMethod.GET)
	public Result getUserResPrivCodesSecurity(
			@ApiParam(value = "json格式的用户、资源信息。key : user、resId、realm、password") @PathVariable String parajson) {

		JSONObject jsonObj = JSONObject.parseObject(parajson);

		return super.successResult(this.privService.getDocPrivCodesSecurity(jsonObj.getString("realm"),
				jsonObj.getString("user"), jsonObj.getString("password"), jsonObj.getString("resId")));
		// return
		// super.successResult(this.privService.getDocPrivCodesOfUserRes(jsonObj.getString("user"),
		// jsonObj.getString("resId")));
	}

	/*
	 * @ApiOperation(value="doc.privcodes.vid.get", notes =
	 * "通过版本系列id，获取用户对资源的权限编码")
	 * 
	 * @RequestMapping(value = "doc.privcodes.vid.get/{parajson}", method =
	 * RequestMethod.GET) public Result getResPrivCodesByVid(
	 * 
	 * @ApiParam(value = "json格式的用户、资源信息。")
	 * 
	 * @PathVariable String parajson) {
	 * 
	 * JSONObject jsonObj = JSONObject.fromObject(parajson);
	 * 
	 * return
	 * super.successResult(this.privService.getDocPrivCodes(jsonObj.getString(
	 * "user"), jsonObj.getString("resId"))); }
	 */

	@ApiOperation(value = "通过权限模板，获取角色对某类资源的权限", notes = "getRolePrivsCodes")
	@RequestMapping(value = "/role.priv.get/{roleCode}/{resTypeCode}", method = RequestMethod.GET)
	public Result getRolePrivsCodes(@ApiParam(value = "角色编码") @PathVariable String roleCode,
			@ApiParam(value = "资源类型编码") @PathVariable String resTypeCode) {

		return super.successResult(this.privService.getRolePrivsCodes(roleCode, resTypeCode));
	}
	@Deprecated
	@ApiOperation(value = "获取项目用户对脑图的权限编码(已过时，建议切换到/privcodes/brainmap)", notes = "getPrivCodesOfBrainMapOld")
	@RequestMapping(value = "/brainmap/privcodes/{caseId}/{userId}", method = RequestMethod.GET)
	public Result getPrivCodesOfBrainMapOld(
			@ApiParam(value = "案例id")
            @PathVariable String caseId,
			@ApiParam(value = "用户id，uuid") 
			@PathVariable String userId) {

		return super.successResult(this.privService.getPrivCodesOfBrainMap(caseId, userId));
	}
	
	@ApiOperation(value = "获取项目用户对脑图的权限编码", notes = "getPrivCodesOfBrainMap")
	@RequestMapping(value = "/privcodes/brainmap/{caseId}/{userId}", method = RequestMethod.GET)
	public Result getPrivCodesOfBrainMap(
			@ApiParam(value = "案例id")
            @PathVariable String caseId,
			@ApiParam(value = "用户id，uuid") 
			@PathVariable String userId) {

		return super.successResult(this.privService.getPrivCodesOfBrainMap(caseId, userId));
	}

	@ApiOperation(value = "获取用户对于资源[项目空间]的权限编码", notes = "getPrivCodesOfPrjSpace")
	@RequestMapping(value = "/privcodes/prjspace/{userCode}", method = RequestMethod.GET)
	public Result getPrivCodesOfPrjSpace(
			@ApiParam(value = "用户id，uuid") 
			@PathVariable String userCode) {

		return super.successResult(this.privService.getPrivCodesOfRestypeDomain(ResourceConstants.ResourceType.RES_SPACE_PROJECT, new String[]{DomainType.INSTITUTE, DomainType.DEPARTMENT},userCode));
	}
	
	@ApiOperation(value = "获取用户对于资源[所级资料]的权限编码", notes = "getPrivCodesOfDepartment")
	@RequestMapping(value = "/privcodes/dep/{userCode}", method = RequestMethod.GET)
	public Result getPrivCodesOfDepartment(
			@ApiParam(value = "用户id，uuid") 
			@PathVariable String userCode) {

		return super.successResult(this.privService.getPrivCodesOfRestypeDomain(ResourceConstants.ResourceType.RES_PCK_DEPARTMENT, new String[]{DomainType.DEPARTMENT},userCode));
	}
	
	@ApiOperation(value = "获取用户对于资源[院级资料]的权限编码", notes = "getPrivCodesOfInstitute")
	@RequestMapping(value = "/privcodes/ins/{userCode}", method = RequestMethod.GET)
	public Result getPrivCodesOfInstitute(
			@ApiParam(value = "用户id，uuid") 
			@PathVariable String userCode) {

		return super.successResult(this.privService.getPrivCodesOfRestypeDomain(ResourceConstants.ResourceType.RES_PCK_INSTITUTE, new String[]{DomainType.INSTITUTE},userCode));
	}

	@ApiOperation(value = "获取用户对于系统资源的权限编码", notes = "getPrivCodesOfSystem")
	@RequestMapping(value = "/privcodes/v1/system/{realm}/{userCode}", method = RequestMethod.GET)
	public Result getPrivCodesOfSystem(@ApiParam(value = "域", required = true) @PathVariable String realm,
			@ApiParam(value = "用户id，uuid", required = true) @PathVariable String userCode) {

		return super.successResult(this.privService.getPrivCodesOfRestypeDomain(realm,
				new String[] { ResourceConstants.ResourceType.RES_System }, new String[] { DomainType.SYSTEM },
				userCode));
	}
	@ApiOperation(value = "获取用户对于[院级资料]和[院级空间]的权限编码", notes = "getPrivCodesOfInstitute")
	@RequestMapping(value = "/privcodes/v1/ins/{realm}/{userCode}", method = RequestMethod.GET)
	public Result getPrivCodesOfInstitute(@ApiParam(value = "域", required = true) @PathVariable String realm,
			@ApiParam(value = "用户id，uuid", required = true) @PathVariable String userCode) {

		return super.successResult(this.privService.getPrivCodesOfRestypeDomain(realm,
				new String[] { ResourceConstants.ResourceType.RES_SPACE_INSTITUTE,
						ResourceConstants.ResourceType.RES_PCK_INSTITUTE },
				new String[] { DomainType.INSTITUTE }, userCode));
	}
	@ApiOperation(value = "获取用户对于[所级资料]和[所级空间]的权限编码", notes = "getPrivCodesOfDepartment")
	@RequestMapping(value = "/privcodes/v1/dep/{realm}/{userCode}", method = RequestMethod.GET)
	public Result getPrivCodesOfDepartment(@ApiParam(value = "域", required = true) @PathVariable String realm,
			@ApiParam(value = "用户id，uuid", required = true) @PathVariable String userCode) {

		return super.successResult(this.privService.getPrivCodesOfRestypeDomain(realm,
				new String[] { ResourceConstants.ResourceType.RES_SPACE_DEPARTMENT,
						ResourceConstants.ResourceType.RES_PCK_DEPARTMENT },
				new String[] { DomainType.DEPARTMENT }, userCode));
	}
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "获取用户对于[项目空间]、[项目包]和[脑图]的权限编码", notes = "getPrivCodesOfProject")
	@RequestMapping(value = "/privcodes/v1/project/{realm}/{userCode}/{projectNo}", method = RequestMethod.GET)
	public Result getPrivCodesOfProject(@ApiParam(value = "域", required = true) @PathVariable String realm,
			@ApiParam(value = "用户id，uuid", required = true) @PathVariable String userCode,
			@ApiParam(value = "项目编号", required = true) @PathVariable String projectNo) {

		List<PrivTemplateDTO> privCodes = new ArrayList<PrivTemplateDTO>();
		LOG.info(">>>获取项目的主责部门，人在部门的角色，对资源【所级空间】的权限");
		DcmGroup projectGroup = this.groupService.getGroupByGuid(projectNo);
		if(!StringUtils.isEmpty(projectGroup.getOrgCode())) {
			List<PrivTemplateDTO> depPrivs = (List<PrivTemplateDTO>) this.privService.getPrivCodesOfRestypeDomain(realm,
					new String[] { ResourceConstants.ResourceType.RES_SPACE_DEPARTMENT},
					new String[] { DomainType.DEPARTMENT }, userCode, projectGroup.getOrgCode());
			if(depPrivs != null && !depPrivs.isEmpty()) {
				privCodes.addAll(depPrivs);
			}
		}
		LOG.info(">>>获取主责部门所在的院，人在院的角色，对资源【院级空间】的权限");
		DcmOrganization insOrg = this.userService.getInstituteByUniqueName(projectGroup.getRealm());
		if(insOrg != null) {
			List<PrivTemplateDTO> insPrivs = (List<PrivTemplateDTO>) this.privService.getPrivCodesOfRestypeDomain(realm,
					new String[] { ResourceConstants.ResourceType.RES_SPACE_INSTITUTE},
					new String[] { DomainType.INSTITUTE }, userCode, insOrg.getOrgCode());
			if(insPrivs != null && !insPrivs.isEmpty()) {
				privCodes.addAll(insPrivs);
			}
		}
		
		LOG.info(">>>获取人在项目中的角色，对资源【项目空间】、【项目包】和【脑图】的权限");
		List<PrivTemplateDTO> projectPrivs = (List<PrivTemplateDTO>) this.privService.getPrivCodesOfRestypeDomain(realm,
				new String[] { ResourceConstants.ResourceType.RES_SPACE_PROJECT, ResourceConstants.ResourceType.RES_PCK_PROJECT,
						ResourceConstants.ResourceType.RES_BRAINMAP },
				new String[] { DomainType.PROJECT }, userCode, projectNo);
		if(projectPrivs != null && !projectPrivs.isEmpty()) {
			privCodes.addAll(projectPrivs);
		}
		return super.successResult(privCodes);
	}
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "获取用户对于[团队空间]、[团队包]和[脑图]的权限编码", notes = "getPrivCodesOfTeam")
	@RequestMapping(value = "/privcodes/v1/team/{realm}/{userCode}/{projectNo}", method = RequestMethod.GET)
	public Result getPrivCodesOfTeam(@ApiParam(value = "域", required = true) @PathVariable String realm,
			@ApiParam(value = "用户id，uuid", required = true) @PathVariable String userCode,
			@ApiParam(value = "项目编号", required = true) @PathVariable String projectNo) {

		List<PrivTemplateDTO> privCodes = new ArrayList<PrivTemplateDTO>();
		LOG.info(">>>获取团队的主责部门，人在部门的角色，对资源【所级空间】的权限");
		DcmGroup projectGroup = this.groupService.getGroupByGuid(projectNo);
		if(!StringUtils.isEmpty(projectGroup.getOrgCode())) {
			List<PrivTemplateDTO> depPrivs = (List<PrivTemplateDTO>) this.privService.getPrivCodesOfRestypeDomain(realm,
					new String[] { ResourceConstants.ResourceType.RES_SPACE_DEPARTMENT},
					new String[] { DomainType.DEPARTMENT }, userCode, projectGroup.getOrgCode());
			if(depPrivs != null && !depPrivs.isEmpty()) {
				privCodes.addAll(depPrivs);
			}
		}
		LOG.info(">>>获取主责部门所在的院，人在院的角色，对资源【院级空间】的权限");
		DcmOrganization insOrg = this.userService.getInstituteByUniqueName(projectGroup.getRealm());
		if(insOrg != null) {
			List<PrivTemplateDTO> insPrivs = (List<PrivTemplateDTO>) this.privService.getPrivCodesOfRestypeDomain(realm,
					new String[] { ResourceConstants.ResourceType.RES_SPACE_INSTITUTE},
					new String[] { DomainType.INSTITUTE }, userCode, insOrg.getOrgCode());
			if(insPrivs != null && !insPrivs.isEmpty()) {
				privCodes.addAll(insPrivs);
			}
		}
		
		LOG.info(">>>获取人在团队中的角色，对资源【团队空间】、【团队包】和【脑图】的权限");
		List<PrivTemplateDTO> projectPrivs = (List<PrivTemplateDTO>) this.privService.getPrivCodesOfRestypeDomain(realm,
				new String[] { ResourceConstants.ResourceType.RES_SPACE_PROJECT, ResourceConstants.ResourceType.RES_PCK_PROJECT,
						ResourceConstants.ResourceType.RES_BRAINMAP },
				new String[] { DomainType.PROJECT }, userCode, projectNo);
		if(projectPrivs != null && !projectPrivs.isEmpty()) {
			privCodes.addAll(projectPrivs);
		}
		return super.successResult(privCodes);
	}
}
