package com.dist.bdf.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dist.bdf.base.controller.BaseController;
import com.dist.bdf.base.result.Result;
import com.dist.bdf.model.dto.system.page.PageSimple;
import com.dist.bdf.model.entity.cad.TemplateEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.dist.bdf.facade.service.cad.CadService;

@Api(tags={"API-cad服务模块"}, description = "CadController")
@RestController
@RequestMapping(value = "/rest/sysservice")
//@CrossOrigin(origins = "*")
public class CadController extends BaseController {

	private Logger logger  = LoggerFactory.getLogger(getClass());
	@Autowired
	private CadService cadService;
	
	@ApiOperation(value = "新增或更新模板元数据", response = Result.class, notes="saveOrUpdateTemplate")
	@RequestMapping(value = "/cad/templates/", method = RequestMethod.POST)
	public Result saveOrUpdateTemplate(
			@ApiParam(value = "模板元数据信息JSON")
			@RequestBody TemplateEntity templateInfo) {
		
		logger.info(">>>模板元数据 : {}", templateInfo);
		TemplateEntity entity = this.cadService.saveOrUpdateTemplateMetadata(templateInfo);
		return super.successResult(entity.getId());
	}
	
	@ApiOperation(value = "获取所有模板元数据", response = Result.class, notes="listTemplate")
	@RequestMapping(value = "/cad/templates/", method = RequestMethod.GET)
	public Result listTemplate() {
		
		String templateMetadataXML = this.cadService.listTemplateMetadataXML();
		logger.info(">>>模板元数据XML：[{}]", templateMetadataXML);
		return super.successResult(templateMetadataXML);
	}
	@Deprecated
	@ApiOperation(value = "删除模板数据（切换到/v1/cad/templates/）", response = Result.class, notes="listTemplate")
	@RequestMapping(value = "/cad/templates/{realm}/{templateId}/{docId}", method = RequestMethod.DELETE)
	public Result deleteTemplate(
			@ApiParam(value = "域")
			@PathVariable String realm,
			@ApiParam(value = "模板元数据id")
			@PathVariable String templateId,
			@ApiParam(value = "ce文档id")
			@PathVariable String docId) {
		
		logger.info(">>>删除模板数据，realm：{}, templateId : {}, docId : {}", realm, templateId, docId);
	
		return super.successResult(this.cadService.deleteTemplate(realm, templateId, docId), "删除成功");
	}
	@ApiOperation(value = "删除模板数据", response = Result.class, notes="listTemplate")
	@RequestMapping(value = "/v1/cad/templates/{realm}/{templateId}/{id}/{vid}", method = RequestMethod.DELETE)
	public Result deleteTemplate(
			@ApiParam(value = "域")
			@PathVariable String realm,
			@ApiParam(value = "模板元数据id")
			@PathVariable String templateId,
			@ApiParam(value = "ce文档id")
			@PathVariable("id") String docId,
			@ApiParam(value = "ce文档版本系列id")
			@PathVariable("vid") String docVId) {
		
		logger.info(">>>删除模板数据，realm：{}, templateId : {}, docId : {}, docVId : {}", realm, templateId, docId, docVId);
	
		return super.successResult(this.cadService.deleteTemplate(realm, templateId, docId, docVId), "删除成功");
	}
	
	@ApiOperation(value = "获取个人收藏的cad模板", notes = "getPersonalFavoriteOfCadTemplate")
	@RequestMapping(value = "/v1/personfavorite/cadtemp/{parajson}", method = RequestMethod.GET)
	public Result getPersonalFavoriteOfCadTemplate(@ApiParam(value = "用户信息，分页查询的设置JSON信息", required = true) @PathVariable String parajson)
			throws Exception {

		PageSimple page = JSONObject.parseObject(parajson, PageSimple.class);
		return super.successResult(this.cadService.getFavoriteOfCadTemplate(page));
	}
	
	@ApiOperation(value = "提供给报建通，获取个人收藏的cad模板", notes = "getPersonalFavoriteOfCadTemplate")
	@RequestMapping(value = "/v1/personfavorite/cadtemp/bjt/{userCode}", method = RequestMethod.GET)
	public Result getPersonalFavoriteOfCadTemplateForBJT(
			@ApiParam(value = "用户信息，分页查询的设置JSON信息", required = true) @PathVariable String userCode)
			throws Exception {

		return super.successResult(this.cadService.getFavoriteOfCadTemplateForBJT(userCode));
	}

	@Deprecated
	@ApiOperation(value = "获取cad所有模板文件信息", notes = "listTemplates")
	@RequestMapping(value = "/v1/cad/templates/{realm}/{pageNo}/{pageSize}", method = RequestMethod.GET)
	public Result listTemplates(@ApiParam(value = "域", required = true) @PathVariable String realm,
			@ApiParam(value = "页码", required = true) @PathVariable int pageNo,
			@ApiParam(value = "每页大小", required = true) @PathVariable int pageSize) {

		return super.successResult(this.cadService.listTemplates(realm, pageNo, pageSize));
	}
	@ApiOperation(value = "获取cad所有模板文件信息", notes = "listTemplates")
	@RequestMapping(value = "/v2/cad/templates/{realm}/{userCode}/{pageNo}/{pageSize}", method = RequestMethod.GET)
	public Result listTemplates(@ApiParam(value = "域", required = true) @PathVariable String realm,
			@ApiParam(value = "用户编码", required = true) @PathVariable String userCode,
			@ApiParam(value = "页码", required = true) @PathVariable int pageNo,
			@ApiParam(value = "每页大小", required = true) @PathVariable int pageSize) {

		return super.successResult(this.cadService.listTemplates(realm, userCode, pageNo, pageSize));
	}
}
