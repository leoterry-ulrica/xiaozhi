package com.dist.bdf.consumer;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dist.bdf.base.controller.BaseController;
import com.dist.bdf.base.result.Result;
import com.dist.bdf.base.utils.JSONUtil;
import com.dist.bdf.model.dto.system.DiscussionGroupDTO;
import com.dist.bdf.model.dto.system.GroupDTO;
import com.dist.bdf.model.dto.system.PersonalDiscussionGroupResultDTO;
import com.dist.bdf.model.dto.system.ProjectGroupDTO;
import com.dist.bdf.model.dto.system.TeamGroupDTO;
import com.dist.bdf.model.entity.system.DcmGroup;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.dist.bdf.facade.service.GroupService;

@Api(tags = { "API-项目组和讨论组服务模块" }, description = "GroupController")
@RestController
@RequestMapping(value = "/rest/sysservice")
//@CrossOrigin(origins = "*")
public class GroupController extends BaseController {

	@Autowired
	private GroupService groupService;
	
	@Deprecated
	@ApiOperation(value = "添加讨论组。(已过时，切换到接口group/discuss)", notes = "addDiscussionGroup")
	@RequestMapping(value = "addDiscussionGroup/{parajson:.+}", method = { RequestMethod.POST, RequestMethod.GET })
	public Result addDiscussionGroup(@ApiParam(value = "json格式讨论组信息。") @PathVariable String parajson) throws Exception {

		DiscussionGroupDTO dto = (DiscussionGroupDTO) JSONUtil.toObject(parajson, DiscussionGroupDTO.class);

		DcmGroup group = this.groupService.addGroup(dto);
		return super.successResult(group);

	}
	
	@ApiOperation(value = "添加讨论组", notes = "addDiscussionGroupEx")
	@RequestMapping(value = "group/discuss", method = { RequestMethod.POST })
	public Result addDiscussionGroupEx(
			@ApiParam(value = "json格式讨论组信息。") 
			@Valid
			@RequestBody DiscussionGroupDTO dto, BindingResult result) throws Exception {

		//DiscussionGroupDTO dto = (DiscussionGroupDTO) JSONUtil.toObject(parajson, DiscussionGroupDTO.class);

		if(result.hasErrors()) 
			return super.errorResult(result.toString());
		
		DcmGroup group = this.groupService.addGroupEx(dto);
		return super.successResult(group);

	}

	/*
	 * @ApiOperation(value="listAllProjectsByPage", notes = "分页获取所有项目列表。")
	 * 
	 * @RequestMapping(value = "listAllProjectsByPage/{parajson}" , method =
	 * {RequestMethod.GET}) public Result listAllProjectsByPage(
	 * 
	 * @ApiParam(value = "json格式，传入用户名称，分页大小、页码")
	 * 
	 * @PathVariable String parajson) throws Exception {
	 * 
	 * PageProjectPara para = (PageProjectPara) JSONUtil.toObject(parajson,
	 * PageProjectPara.class); Object result =
	 * this.groupService.listProjectsByPage(para, Flag.ALL); return
	 * super.successResult(result);
	 * 
	 * }
	 */
	
	@ApiOperation(value = "获取所有讨论组列表", notes = "listDiscussionGroups")
	@RequestMapping(value = "listDiscussionGroups", method = { RequestMethod.GET })
	public Result listDiscussionGroups() {

		List<DcmGroup> groups = this.groupService.listDiscussionGroups();
		return super.successResult(groups);
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "获取个人讨论组", notes = "listPersonalDiscussionGroups")
	@RequestMapping(value = "listPersonalDiscussionGroups/{loginName}", method = { RequestMethod.GET })
	public Result listPersonalDiscussionGroups(@ApiParam(value = "登录名") @PathVariable String loginName) {

		List<PersonalDiscussionGroupResultDTO> list = (List<PersonalDiscussionGroupResultDTO>) this.groupService
				.listPersonalDiscussionGroups(loginName);
		return super.successResult(list);
	}

    

	@ApiOperation(value = "更新讨论组信息", notes = "updateDiscussionGroup")
	@RequestMapping(value = "updateDiscussionGroup/{parajson:..+}")
	public Result updateDiscussionGroup(@ApiParam(value = "json格式讨论组信息") @PathVariable String parajson)
			throws Exception {

		DiscussionGroupDTO dto = (DiscussionGroupDTO) JSONUtil.toObject(parajson, DiscussionGroupDTO.class);
		GroupDTO groupDto = this.groupService.updateDiscussionGroup(dto);
		return super.successResult(groupDto);
	}
	
	@ApiOperation(value = "获取用户在项目组中的角色", notes = "getRolesInProject")
	@RequestMapping(value = "/group/project/role/{projectGuid}/{userSeqId}", method = {RequestMethod.GET})
	public Result getRolesInProject(
			
			@ApiParam(value = "项目guid") 
			@PathVariable String projectGuid,
			@ApiParam(value = "用户序列id") 
			@PathVariable Long userSeqId
			) {

		return super.successResult(this.groupService.getRolesInProject(projectGuid, userSeqId));
	}
	@ApiOperation(value = "添加项目组，新服务接口（满足济南）", notes = "addProjectGroupNew")
	@RequestMapping(value = "/group/project/v1", method = { RequestMethod.POST })
	public Result addProjectGroupNew (
			@ApiParam(value = "json格式的项目信息。") 
			@Valid
			@RequestBody ProjectGroupDTO dto, BindingResult result) throws Exception {

		if(result.hasErrors()) 
			return super.errorResult(result.toString());
		
		DcmGroup group = this.groupService.addProjectGroupNew(dto);
		return super.successResult(group);
	}
	@ApiOperation(value = "添加团队，新服务接口（满足济南）", notes = "addTeam")
	@RequestMapping(value = "/group/team/v1", method = { RequestMethod.POST })
	public Result addTeam (
			@ApiParam(value = "json格式的团队信息。") 
			@Valid
			@RequestBody TeamGroupDTO dto, BindingResult result) throws Exception {

		if(result.hasErrors()) 
			return super.errorResult(result.toString());
		
		DcmGroup group = this.groupService.addTeamGroup(dto);
		return super.successResult(group);
	}
}
