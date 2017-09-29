package com.dist.bdf.consumer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dist.bdf.base.controller.BaseController;
import com.dist.bdf.base.result.Result;
import com.dist.bdf.base.utils.JSONUtil;
import com.dist.bdf.base.utils.StringUtil;
import com.dist.bdf.model.dto.dcm.TeamSpaceDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.dist.bdf.facade.service.EcmMgmtService;

@Api(tags={"API-ecm服务模块"}, description = "DcmController")
@RestController
@RequestMapping(value = "/rest/sysservice")
//@CrossOrigin(origins = "*")
public class DcmController extends BaseController {

	@Autowired
	private EcmMgmtService ecmMgmtService;
	
	// teamspace模块begin

	@ApiOperation(value = "创建团队空间", response = Result.class, notes="createTeamspace")
	@RequestMapping(value = "/createTeamspace", method = RequestMethod.POST)
	public Result createTeamspace(
			@ApiParam(value = "用户名")
			@RequestParam String userName, 
			@ApiParam(value = "密码")
			@RequestParam String userPwd) throws Exception {
		
		/*String userName = super.request.getParameter("userName");
		String userPwd = super.request.getParameter("userPwd");*/
		String teamspaceJson = super.request.getParameter("teamspace");
		if (StringUtil.isNullOrEmpty(userName) || StringUtil.isNullOrEmpty(userPwd)) {
			return super.failResult("用户名或者密码不能为空。");
		}
		TeamSpaceDTO template = (TeamSpaceDTO) JSONUtil.toObject(teamspaceJson, TeamSpaceDTO.class);
		String guid = ecmMgmtService.createTeamspace(userName, userPwd, template);
		System.out.println("teamspace创建成功，id：" + guid);
		return super.successResult(guid);
	}

	@ApiOperation(value = "获取团队空间所有模板", response = Result.class, notes="getAllTeamspaceTemplate")
	@RequestMapping(value = "/getAllTeamspaceTemplate", method = RequestMethod.GET)
	public Result getAllTeamspaceTemplate() {
		
		List<TeamSpaceDTO> list = this.ecmMgmtService.getAllTeamspaceTemplate();
		return super.successResult(list);

	}
	// teamspace模块end

}
