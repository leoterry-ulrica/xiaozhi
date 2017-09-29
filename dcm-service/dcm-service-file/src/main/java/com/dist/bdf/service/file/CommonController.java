package com.dist.bdf.service.file;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dist.bdf.base.controller.BaseController;
import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.job.DelTempFileTimerTask;
import com.dist.bdf.base.result.Result;
import com.dist.bdf.base.utils.FileUtil;
import com.dist.bdf.common.conf.common.GlobalConf;
import com.dist.bdf.common.conf.officeonline.OfficeonlineConf;
import com.dist.bdf.model.dto.dcm.FileContentLocalDTO;
import com.dist.bdf.service.file.impl.FileServiceImpl;

import io.github.xdiamond.client.XDiamondConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = {"通用服务模块API"}, description = "CommonController")
@RestController
//@CrossOrigin(origins = "*")
@RequestMapping(value="/rest/sysservice")
public class CommonController extends BaseController {

	@Autowired
	private XDiamondConfig xconf;
	@Autowired
	private OfficeonlineConf ooConf;
	@Autowired
	private GlobalConf globalConf;
	@Autowired
	private FileServiceImpl fileService;
	
	private DelTempFileTimerTask delTask = new DelTempFileTimerTask();
	
	/*@ApiOperation(value = "getPropertyByKey", notes = "获取系统属性值")
	@RequestMapping(value="/getPropertyByKey/{key:.+}", method = RequestMethod.GET)*/
	public Result getPropertyByKey(
			@ApiParam(value = "属性key")
			@PathVariable String key) {
		
		Properties props = xconf.getProperties();
		if(props.containsKey(key)){
			return super.successResult(props.get(key));
		}
		return super.failResult("不存在属性key：["+key+"]");
	}
	

	@ApiOperation(value = "根据文件后缀获取查看器地址", response = Result.class, notes = "getViewerUrl")
	@RequestMapping(value="/getViewerUrl/{extension:.+}", method = RequestMethod.GET)
	public Result getViewerUrl(
			@ApiParam(value = "文档扩展名，不带点号“.”")
			@PathVariable String extension) {
		
		String server = ooConf.getServer();
		if(!server.endsWith("/")) server += "/";
		
		String key = "oo.viewer."+extension;
		Result value = getPropertyByKey(key);
		
		if(value.getStatus().equalsIgnoreCase("success")){
			return  super.successResult(server + value.getData());
		}

		throw new BusinessException("没有找到扩展名 [{0}] 对应的查看器。", extension);
		
	}
	
	public void clearTempDir(){
		
		delTask.setDirectories(globalConf.getClearDirectories());
		delTask.execute();
	}
	@Deprecated
	@ApiOperation(value = "根据id获取文件流(已废弃，切换到/v1/docstream/)", notes = "getDocContentStream")
	@RequestMapping(value="getDocContentStream/{realm}/{docId:\\{.+\\}}", method = {RequestMethod.GET})
	public void getDocContentStream(
			@ApiParam(value = "域")
			@PathVariable String realm,
			@ApiParam(value = "ce中文档id")
			@PathVariable String docId){
		
		OutputStream toClient = null;
		try{
		//String contentType = "application/octet-stream";
		FileContentLocalDTO local = this.fileService.getDocContentLocal(realm, docId);
		byte[] source = FileUtil.getBytes(local.getContentInputStream());
		toClient = new BufferedOutputStream(response.getOutputStream());
		response.setContentType(local.getContentType());
		response.setContentLength(local.getSize());

		toClient.write(source);
		toClient.flush();
		
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			try{
				// 关闭流
			if(toClient != null) toClient.close();
			}catch(IOException io){
				io.printStackTrace();
			}
		}
	}
	
	@ApiOperation(value = "根据id获取文件流", notes = "getDocContentStreamEx")
	@RequestMapping(value="/v1/docstream/{realm}/{docId:\\{.+\\}}", method = {RequestMethod.GET})
	public void getDocContentStreamEx(
			@ApiParam(value = "域")
			@PathVariable String realm,
			@ApiParam(value = "ce中文档id")
			@PathVariable String docId){
		
		OutputStream toClient = null;
		try{
		//String contentType = "application/octet-stream";
		FileContentLocalDTO local = this.fileService.getDocContentLocal(realm, docId);
		byte[] source = FileUtil.getBytes(local.getContentInputStream());
		toClient = new BufferedOutputStream(response.getOutputStream());
		response.setContentType(local.getContentType());
		response.setContentLength(local.getSize());

		toClient.write(source);
		toClient.flush();
		
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			try{
				// 关闭流
			if(toClient != null) toClient.close();
			}catch(IOException io){
				io.printStackTrace();
			}
		}
	}
}
