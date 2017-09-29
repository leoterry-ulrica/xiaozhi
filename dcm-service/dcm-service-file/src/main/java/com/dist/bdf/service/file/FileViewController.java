package com.dist.bdf.service.file;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dist.bdf.base.controller.BaseController;
import com.dist.bdf.base.result.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags={"API-文件查看服务模块"}, description = "FileViewController")
@RestController
//@CrossOrigin(origins = "*")
@RequestMapping(value = "/rest/sysservice")
public class FileViewController extends BaseController {

	@Autowired
	private CommonController commonCtl;

	@ApiOperation(value = "查看office各类文件", notes = "viewOffice")
    @RequestMapping(value="viewOfficeOld/{extension}/{docId:\\{.+\\}}", method = RequestMethod.GET) // 对传入进来的{}进行正则表达式过滤
    public void viewOffice(@PathVariable String extension, @PathVariable String docId) throws IOException {
    	
    	Result viewer = this.commonCtl.getViewerUrl(extension);
    	String wopiClient = viewer.getData().toString();//OfficeOnlineViewer.getViewer(extension);//"http://192.168.2.232/wv/wordviewerframe.aspx?WOPISrc=";
    	
    	String baseURL = super.request.getScheme() +"://" + super.request.getServerName()
		 + ":" +super.request.getServerPort()
		 + super.request.getContextPath();
    	
        String officeURL = baseURL+"/wopi/files/"+docId.replace("{", "").replace("}", "")+"/";
        // 重定向
    	super.response.sendRedirect(wopiClient+officeURL);
    	
    }

	@ApiOperation(value = "查看office各类文件，扩展接口，支持是否全屏播放。", notes = "viewOfficeEx")
    @RequestMapping(value="viewOffice/{extension}/{docId:\\{.+\\}}/{fullPlay}", method = RequestMethod.GET) // 对传入进来的{}进行正则表达式过滤
    public void viewOfficeEx(
    		@ApiParam(value = "不带点号. 的扩展名")
    		@PathVariable String extension,
    		@ApiParam(value = "文档在CE中的唯一标识")
    		@PathVariable String docId, 
    		@ApiParam(value = "0：非全屏；1：全屏")
    		@PathVariable int fullPlay) throws IOException {
    	
    	Result viewer = this.commonCtl.getViewerUrl(extension);
    	String wopiClient = viewer.getData().toString();//OfficeOnlineViewer.getViewer(extension);//"http://192.168.2.232/wv/wordviewerframe.aspx?WOPISrc=";
    	StringBuilder buf = new StringBuilder();
    	buf.append(super.request.getScheme());
    	buf.append("://");
    	buf.append(super.request.getServerName());
    	buf.append(":");
    	buf.append(super.request.getServerPort());
    	buf.append(super.request.getContextPath());
    	buf.append("/wopi/files/");
    	buf.append(docId.replace("{", "").replace("}","")+".thupdi/"); // 传入域的值：thupdi
    
    	if(1 == fullPlay) {
    		buf.append("&PowerPointView=SlideShowView");
    		//buf.append(fullPlay);
    	}

        // 重定向

    	super.response.sendRedirect(wopiClient+buf.toString());
    	
    }
    
	@ApiOperation(value = "查看office各类文件，扩展接口，支持是否全屏播放。", notes = "viewOfficeEx")
    @RequestMapping(value="viewOffice/{realm}/{extension}/{docId:\\{.+\\}}/{fullPlay}", method = RequestMethod.GET) // 对传入进来的{}进行正则表达式过滤
    public void viewOfficeEx(
    		@ApiParam(value = "域")
    		@PathVariable String realm,
    		@ApiParam(value = "不带点号. 的扩展名")
    		@PathVariable String extension,
    		@ApiParam(value = "文档在CE中的唯一标识")
    		@PathVariable String docId, 
    		@ApiParam(value = "0：非全屏；1：全屏")
    		@PathVariable int fullPlay) throws IOException {
    	
    	Result viewer = this.commonCtl.getViewerUrl(extension);
    	String wopiClient = viewer.getData().toString();//OfficeOnlineViewer.getViewer(extension);//"http://192.168.2.232/wv/wordviewerframe.aspx?WOPISrc=";
    	StringBuilder buf = new StringBuilder();
    	buf.append(super.request.getScheme());
    	buf.append("://");
    	buf.append(super.request.getServerName());
    	buf.append(":");
    	buf.append(super.request.getServerPort());
    	buf.append(super.request.getContextPath());
    	buf.append("/wopi/files/");
    	buf.append(docId.replace("{", "").replace("}","")+"."+ realm +"/"); // 域
    	if(1 == fullPlay) {
    		buf.append("&PowerPointView=SlideShowView");
    		//buf.append(fullPlay);
    	}

        // 重定向
    	super.response.sendRedirect(wopiClient+buf.toString());
    }
	
}
