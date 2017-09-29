package com.dist.bdf.consumer;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dist.bdf.base.controller.BaseController;

import com.dist.bdf.base.result.Result;

import com.dist.bdf.model.dto.dcm.FileContentDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.dist.bdf.facade.service.EcmMgmtService;
import com.dist.bdf.facade.service.MaterialService;

@Deprecated
@Api(tags={"API-文件下载服务模块"}, description = "FileDownloadController")
@RestController
@RequestMapping(value = "/rest/sysservice")
@Scope("prototype") // 用于多线程下载文件所用，否则单例模式下，response对象被共享
//@CrossOrigin(origins = "*")
public class FileDownloadController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(FileDownloadController.class);
	
	@Autowired
	private EcmMgmtService ecmMgmtService;
   @Autowired
   private MaterialService materialService;
	
	@ApiOperation(value = "断点下载文件，只要指定读取数据的起始位置", notes = "downloadDocBreakpoint")
	@RequestMapping(value = "file/download.bkp/{docId:\\{.+\\}}", method = {RequestMethod.GET})
	public void downloadDocBreakpoint (
			@ApiParam(value = "ce中文档id")
			@PathVariable final String docId) {

		System.out.println("下载请求session id："+super.request.getSession().getId());
		
		OutputStream toClient = null;
		ServletOutputStream servletOutputStream = null;
		FileContentDTO fileDTO = null;

		long pos = 0;
		long last = - 1;
		try {
			// 断点续传
			//super.response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
			String rangeString = super.request.getHeader("Range");
			logger.info("请求数据范围Range："+ rangeString);
			String numRang = request.getHeader("Range").replaceAll("bytes=", "");
			String[] range = numRang.split("-");
			if(2 != range.length) {
				//throw new BusinessException("请求文件的范围有误。Range："+rangeString);
				// 说明是最后一个数据块，只有起始位置
				pos = Long.parseLong(range[0].replaceAll("-", "").trim());
				
			} else {
				 pos = Long.parseLong(range[0].trim());
                 last = Long.parseLong(range[1].trim());
			}	

			fileDTO = this.ecmMgmtService.getDocContentBreakpoint(docId, pos);
			//inputStream = new ByteArrayInputStream(fileDTO.getContentStream());
			String contentType = "application/octet-stream";//"application/octet-stream;charset=utf-8";
			super.response.setCharacterEncoding("utf-8");
			super.response.setContentType(contentType);
			super.response.setContentLength(fileDTO.getSize());
			super.response.setHeader("Accept-Ranges", "bytes");
			super.response.setHeader("Content-Disposition", "attachment;filename=" + fileDTO.getDocName());
			if (-1 == last) {
				last = fileDTO.getLsize() - 1;
			}
			String contentRange = new StringBuffer("bytes ").append(pos).append("-").append(last).append("/").append(fileDTO.getLsize()).toString();
			super.response.setHeader("Content-Range", contentRange);
						
			try{
				toClient = new BufferedOutputStream(response.getOutputStream());
				toClient.write(fileDTO.getContentStream());
				toClient.flush();
			
			}catch(Exception ex){
				String simplename = ex.getClass().getSimpleName();
				if("ClientAbortException".equals(simplename)){
				     logger.warn("用户取消下载......");
				     toClient.write(new Result("error", "用户取消下载......").toJsonString().getBytes());
				}else{
					 ex.printStackTrace();
					 toClient.write(new Result("error", ex.getMessage()).toJsonString().getBytes());
				}
				return;
			}

			if(-1 == last){
				logger.info("文件已下载完成......");
				// 修改下载数
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						FileDownloadController.this.materialService.addDownloadCountOfSummaryData(docId);
					}
				}).start();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {

				if (servletOutputStream != null)
					servletOutputStream.close();
				if(toClient != null){
					toClient.close();
				}

			} catch (IOException io) {
				io.printStackTrace();
			}
		}
	}
	
	
}
