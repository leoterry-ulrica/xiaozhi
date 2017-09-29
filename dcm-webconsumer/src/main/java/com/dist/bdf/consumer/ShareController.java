package com.dist.bdf.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dist.bdf.base.controller.BaseController;
import com.dist.bdf.base.result.Result;
import com.dist.bdf.model.dto.system.ShareParaDTO;
import com.dist.bdf.model.dto.system.page.PageSimple;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.dist.bdf.facade.service.ShareService;

@Api(tags = { "API-共享服务模块" }, description = "ShareController")
@RestController
@RequestMapping(value = "/rest/sysservice")
//@CrossOrigin(origins = "*")
public class ShareController extends BaseController {

	@Autowired
	private ShareService shareService;

	@Deprecated
	@ApiOperation(value = "添加资料共享（已过时，切换到接口res/share）", notes = "addResShare")
	@RequestMapping(value = "addResShare/{parajson:.+}", method = { RequestMethod.GET, RequestMethod.POST })
	public Result addResShare(@ApiParam(value = "资源分享信息设置") @PathVariable String parajson) throws Exception {

		ShareParaDTO dto = JSONObject.parseObject(parajson, ShareParaDTO.class);

		this.shareService.addResourceShare(dto);
		return super.successResult("共享成功");
	}
	
	@ApiOperation(value = "添加资料共享", notes = "addResShareEx")
	@RequestMapping(value = "/res/share", method = { RequestMethod.POST })
	public Result addResShareEx(@ApiParam(value = "资源分享信息设置") @RequestBody ShareParaDTO dto) throws Exception {

		this.shareService.addResourceShare(dto);
		return super.successResult("共享成功");
	}

	@Deprecated
	@ApiOperation(value = "删除资源共享（已过时，切换到接口res/share/cancel）", notes = "delResShare")
	@RequestMapping(value = "delResShare/{parajson:.+}", method = { RequestMethod.GET, RequestMethod.POST })
	public Result delResShare(@PathVariable String parajson) {
		try {
			ShareParaDTO dto = JSONObject.parseObject(parajson, ShareParaDTO.class);

			this.shareService.delResourceShare(dto);
			return super.successResult("成功取消共享");

		} catch (Exception ex) {
			return super.failResultException(ex);
		}
	}
	@ApiOperation(value = "取消资源共享", notes = "delResShareEx")
	@RequestMapping(value = "res/share/cancel", method = { RequestMethod.DELETE })
	public Result delResShareEx(
			@ApiParam(value = "资源取消分享信息设置") 
			@RequestBody ShareParaDTO dto) {
		
		this.shareService.delResourceShare(dto);
		return super.successResult("成功取消共享");
		
	}
	
	@ApiOperation(value = "检测共享信息", notes = "checkShareInfo")
	@RequestMapping(value = "checkShareInfo", method = { RequestMethod.GET })
	public Result checkShareInfo() {
		
		return super.successResult(this.shareService.checkShareInfo());
	}

	@Deprecated
	@ApiOperation(value = "获取个人共享出去的资源，提供给web使用。", notes = "getPersonalShare，设计有问题，摒弃，切换到/share/person/web/。")
	@RequestMapping(value = "getPersonalShare/{parajson:.+}", method = { RequestMethod.GET, RequestMethod.POST })
	public Result getPersonalShare(@PathVariable String parajson) throws Exception {
		
		PageSimple dto = JSONObject.parseObject(parajson, PageSimple.class);
		Object result = this.shareService.getPersonalShare(dto);
		return super.successResult(result);
	}
	
	@ApiOperation(value = "获取个人共享出去的资源，提供给web使用", notes = "getPersonalShareWeb")
	@RequestMapping(value = "/share/person/web/{parajson:.+}", method = { RequestMethod.GET, RequestMethod.POST })
	public Result getPersonalShareWeb(@PathVariable String parajson) throws Exception {
		
		PageSimple dto = JSONObject.parseObject(parajson, PageSimple.class);
		Object result = this.shareService.getPersonalShare(dto);
		return super.successResult(result);
	}
	
	@ApiOperation(value = "获取个人共享出去的资源，提供给端使用", notes = "getPersonalShareEx")
	@RequestMapping(value = "/share/person/{parajson:.+}", method = { RequestMethod.GET})
	public Result getPersonalShareEx(@PathVariable String parajson) throws Exception {
		
		PageSimple dto = JSONObject.parseObject(parajson, PageSimple.class);
		Object result = this.shareService.getPersonalShareWholeInfo(dto);
		return super.successResult(result);
	}

	@ApiOperation(value = "获取别人共享给我的资源", notes = "getSharedInfoByOthers")
	@RequestMapping(value = "getSharedInfoByOthers/{parajson:.+}", method = { RequestMethod.GET, RequestMethod.POST })
	public Result getSharedInfoByOthers(@ApiParam(value = "json格式信息") @PathVariable String parajson) throws Exception {
		
		PageSimple dto =  JSONObject.parseObject(parajson, PageSimple.class);
		return super.successResult(this.shareService.getSharedInfoByOthers(dto));

	}
	
	@ApiOperation(value = "获取别人共享给我的资源，提供给端使用", notes = "getSharedInfoByOthersEx")
	@RequestMapping(value = "/shared/others/{parajson:.+}", method = { RequestMethod.GET})
	public Result getSharedInfoByOthersEx(@ApiParam(value = "json格式信息") @PathVariable String parajson) {
		
		PageSimple dto = JSONObject.parseObject(parajson, PageSimple.class);
		return super.successResult(this.shareService.getSharedWholeInfoByOthers(dto));

	}

}
