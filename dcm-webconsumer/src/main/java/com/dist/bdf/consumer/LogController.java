package com.dist.bdf.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dist.bdf.base.controller.BaseController;
import com.dist.bdf.base.result.Result;
import com.dist.bdf.base.utils.JSONUtil;
import com.dist.bdf.facade.service.LogService;
import com.dist.bdf.model.dto.system.TaskProcessLogDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags={"API-日志服务模块"}, description = "LogController")
@RestController
@RequestMapping(value = "/rest/sysservice")
//@CrossOrigin(origins = "*")
public class LogController extends BaseController {

	@Autowired
	private LogService logService;
	
	/*@ApiOperation(value="log.task.add", notes = "添加事项日志。")
	@RequestMapping(value = "log.task.add/{parajson:.+}", method = {RequestMethod.POST})
	public Result addTaskProcessLog(
			@ApiParam(value = "json格式日志")
			@PathVariable
			String parajson
			) throws Exception {
		
	
		TaskProcessLogDTO dto = (TaskProcessLogDTO) JSONUtil.toObject(parajson, TaskProcessLogDTO.class);
		dto.setIp(super.getRequestIp());
			
		return super.successResult(this.logService.addTaskProcessLog(dto));
		
	}*/
	
	@ApiOperation(value="添加事项日志", notes = "addTaskProcessLog2")
	@RequestMapping(value = "log.task.add", method = {RequestMethod.POST})
	public Result addTaskProcessLog2(
			@ApiParam(value = "参数信息放在请求体中。")
			@RequestBody String requestBodyContent
			) throws Exception {
		
	
		TaskProcessLogDTO dto = (TaskProcessLogDTO) JSONUtil.toObject(requestBodyContent, TaskProcessLogDTO.class);
		dto.setIp(super.getRequestIp());
			
		return super.successResult(this.logService.addTaskProcessLog(dto));
		
	}
	
	
	
}
