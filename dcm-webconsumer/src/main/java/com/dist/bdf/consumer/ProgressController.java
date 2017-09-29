package com.dist.bdf.consumer;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dist.bdf.base.constants.SessionContants;
import com.dist.bdf.base.controller.BaseController;
import com.dist.bdf.base.upload.ProgressEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 获取上传文件进度controller
 * @author weifj
 *
 */
@Api(tags={"API-获取上传文件进度"}, description = "ProgressController")
@RestController
@RequestMapping(value = "/rest/fileStatus")
//@CrossOrigin(origins = "*")
public class ProgressController extends BaseController{

	@ApiOperation(value = "获取文件上传的进度", notes = "initCreateInfo")
	@RequestMapping(value = "/upfile/progress", method = {RequestMethod.POST, RequestMethod.GET} )
	public String initCreateInfo(HttpServletRequest request) {
		ProgressEntity status = (ProgressEntity) request.getSession().getAttribute(SessionContants.UPLOAD_FILE);
		if(status==null){
			return "{}";
		}
		return status.toString();
	}
}