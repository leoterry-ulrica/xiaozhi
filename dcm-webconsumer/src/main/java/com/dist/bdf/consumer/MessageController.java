package com.dist.bdf.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dist.bdf.base.controller.BaseController;
import com.dist.bdf.base.result.Result;
import com.dist.bdf.common.constants.UserStatus;
import com.dist.bdf.facade.service.ProjectService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags={"API-消息服务模块"}, description = "MessageController")
@RestController
@RequestMapping(value = "/rest/sysservice")
//@CrossOrigin(origins = "*")
public class MessageController extends BaseController {

	private static Logger LOG  = LoggerFactory.getLogger(MessageController.class);
	@Autowired
	private ProjectService projectService;

	@ApiOperation(value = "发送微信小程序模板消息", response = Result.class, notes="sendWechatappTemplateMsg")
	@RequestMapping(value = "/wechatapp/templatemsg/{projectCode}/{userCode}/{status}", method = RequestMethod.POST)
	public Result sendWechatappTemplateMsg (
			@ApiParam(value = "项目唯一编码", required = true)
			@PathVariable 
			String projectCode,
			@ApiParam(value = "用户唯一编码", required = true)
			@PathVariable 
			String userCode, 
			@ApiParam(value = "用户状态", required = true)
			@PathVariable 
			Integer status) {
		
		LOG.info(">>>项目编码：{}，用户编码：{}，状态：{}", projectCode, userCode, status);
		// 如果状态是2（待定，审核中）
		if(UserStatus.PROJECT_UNDETERMINED == status) {
			// 发送模板消息
			this.projectService.sendWechatTemplateMsg(projectCode, userCode);
			return this.successResult("发送成功");
		}
	
		return super.failResult("用户状态无效");
	}
}
