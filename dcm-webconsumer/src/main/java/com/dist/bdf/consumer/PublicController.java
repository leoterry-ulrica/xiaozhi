package com.dist.bdf.consumer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dist.bdf.base.controller.BaseController;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.base.result.Result;
import com.dist.bdf.facade.service.InformationService;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = { "API-公开资讯服务模块" }, description = "PublicController")
@RestController
@RequestMapping(value = "/rest/sysservice")
//@CrossOrigin(origins = "*")
public class PublicController extends BaseController {

	@Autowired
	private InformationService infoService;
	@Autowired
	private CommonController commonController;

	@Deprecated
	@ApiOperation(value = "根据关键字检索资讯，检索全部传入星号*（后续会废弃）", notes = "searchInformation")
	@RequestMapping(value = "/v1/pubinfos/{pageNo}/{pageSize}/{keyword}", method = { RequestMethod.GET})
	public Result searchInformation(
			@ApiParam(value = "页码", required = true)
			@PathVariable Integer pageNo, 
			@ApiParam(value = "每页大小", required = true)
			@PathVariable Integer pageSize, 
			@ApiParam(value = "关键字", required = true)
			@PathVariable String keyword) {

		// 星号*过滤
		keyword = keyword.replace("*", "");
		return super.successResult(this.infoService.searchInformations(pageNo, pageSize, keyword));
	}
	@ApiOperation(value = "根据关键字检索资讯，检索全部传入星号*，返回图片的URL，替代了原来的base64编码。", notes = "searchInformationV2")
	@RequestMapping(value = "/v2/pubinfos/{pageNo}/{pageSize}/{keyword}", method = { RequestMethod.GET})
	public Result searchInformationV2(
			@ApiParam(value = "页码", required = true)
			@PathVariable Integer pageNo, 
			@ApiParam(value = "每页大小", required = true)
			@PathVariable Integer pageSize, 
			@ApiParam(value = "关键字", required = true)
			@PathVariable String keyword) {

		// 星号*过滤
		keyword = keyword.replace("*", "");
		// 缩略图生成本地文件
		Pagination page = this.infoService.searchInformationsThumbnailByte(pageNo, pageSize, keyword);
		@SuppressWarnings("unchecked")
		List<DocumentDTO> data = (List<DocumentDTO>) page.getData();
		this.commonController.generateDocThumbnail(super.request, data);
		return super.successResult(page);
	}

	@Deprecated
	@ApiOperation(value = "根据关键字检索资料类型资讯，检索全部传入星号*（后续废弃）", notes = "searchInformationOfMaterial")
	@RequestMapping(value = "/v1/pubinfos/material/{pageNo}/{pageSize}/{keyword}", method = { RequestMethod.GET})
	public Result searchInformationOfMaterial(
			@ApiParam(value = "页码", required = true)
			@PathVariable Integer pageNo, 
			@ApiParam(value = "每页大小", required = true)
			@PathVariable Integer pageSize, 
			@ApiParam(value = "关键字", required = true)
			@PathVariable String keyword) {

		// 星号*过滤
		keyword = keyword.replace("*", "");
		return super.successResult(this.infoService.searchInformationOfMaterial(pageNo, pageSize, keyword));
	}
	
	@ApiOperation(value = "根据关键字检索资料类型资讯，检索全部传入星号*，返回图片的URL，替代了原来的base64编码。", notes = "searchInformationOfMaterialV2")
	@RequestMapping(value = "/v2/pubinfos/material/{pageNo}/{pageSize}/{keyword}", method = { RequestMethod.GET})
	public Result searchInformationOfMaterialV2(
			@ApiParam(value = "页码", required = true)
			@PathVariable Integer pageNo, 
			@ApiParam(value = "每页大小", required = true)
			@PathVariable Integer pageSize, 
			@ApiParam(value = "关键字", required = true)
			@PathVariable String keyword) {

		// 星号*过滤
		keyword = keyword.replace("*", "");
		return super.successResult(this.infoService.searchInformationOfMaterialThumbnailByte(pageNo, pageSize, keyword));
	}

	@Deprecated
	@ApiOperation(value = "根据关键字检索新闻类型资讯，检索全部传入星号*（后续废弃）", notes = "searchInformationOfNews")
	@RequestMapping(value = "/v1/pubinfos/news/{pageNo}/{pageSize}/{keyword}", method = { RequestMethod.GET})
	public Result searchInformationOfNews(
			@ApiParam(value = "页码", required = true)
	        @PathVariable Integer pageNo, 
	        @ApiParam(value = "每页大小", required = true)
			@PathVariable Integer pageSize, 
			@ApiParam(value = "关键字", required = true)
			@PathVariable String keyword) {

		// 星号*过滤
		keyword = keyword.replace("*", "");
		return super.successResult(this.infoService.searchInformationOfNews(pageNo, pageSize, keyword));
	}
	@ApiOperation(value = "根据关键字检索新闻类型资讯，检索全部传入星号*，返回图片的URL，替代了原来的base64编码。", notes = "searchInformationOfNewsV2")
	@RequestMapping(value = "/v2/pubinfos/news/{pageNo}/{pageSize}/{keyword}", method = { RequestMethod.GET})
	public Result searchInformationOfNewsV2(
			@ApiParam(value = "页码", required = true)
	        @PathVariable Integer pageNo, 
	        @ApiParam(value = "每页大小", required = true)
			@PathVariable Integer pageSize, 
			@ApiParam(value = "关键字", required = true)
			@PathVariable String keyword) {

		// 星号*过滤
		keyword = keyword.replace("*", "");
		return super.successResult(this.infoService.searchInformationOfNewsThumbnailByte(pageNo, pageSize, keyword));
	}
}
