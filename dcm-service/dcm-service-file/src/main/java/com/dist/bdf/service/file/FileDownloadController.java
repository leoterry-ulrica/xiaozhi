package com.dist.bdf.service.file;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dist.bdf.base.controller.BaseController;

import com.dist.bdf.base.result.Result;
import com.dist.bdf.base.utils.DistThreadManager;
import com.dist.bdf.model.dto.dcm.FileContentLocalDTO;
import com.dist.bdf.service.file.impl.FileServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags={"API-文件服务模块"}, description = "FileDownloadController")
@RestController
//@CrossOrigin(origins = "*")
@RequestMapping(value = "/rest/sysservice")
@Scope("prototype") // 用于多线程下载文件所用，否则单例模式下，response对象被共享
public class FileDownloadController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(FileDownloadController.class);
	// private static volatile Map<String, FileContentLocalDTO> DownloadFileCacheMap = Collections.synchronizedMap(new ConcurrentHashMap<String, FileContentLocalDTO>());
	
   //@Autowired
   //private MaterialService materialService;
   @Autowired
	private FileServiceImpl fileService;
 		

	@ApiOperation(value = "断点下载文件，只要指定读取数据的起始位置。", notes = "downloadDocBreakpoint")
	@RequestMapping(value = "/file/download.bkp", method = {RequestMethod.POST})
	public void downloadDocBreakpoint (
			@ApiParam(value = "文档和用户信息")
			@RequestBody String parajson) {

		logger.debug("request : "+ parajson);
		
		logger.info(">>>下载请求session id："+super.request.getSession().getId());
		
		JSONObject json = JSONObject.parseObject(parajson);
		final String docId = json.getString("docId");
		final String realm = json.getString("realm");
		InputStream inputStream = null;
		OutputStream toClient = null;
		FileContentLocalDTO fileDTO = null;

		long pos = 0;
		long last = - 1;

		try {
			// 断点续传
			//super.response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
			String rangeString = super.request.getHeader("Range");
			logger.info(">>>请求数据范围Range："+ rangeString);
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
		/*	if(DownloadFileCacheMap.containsKey(docId)){
				fileDTO = DownloadFileCacheMap.get(docId);
			}else{
				fileDTO = this.fileService.getDocContentLocal(json.getString("realm"), json.getString("userId"), json.getString("password"), docId);
				DownloadFileCacheMap.put(docId, fileDTO);
			}
			*/
			fileDTO = this.fileService.getDocContentLocal(realm, docId);
				
			inputStream = fileDTO.getContentInputStream();
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
				byte[] bytes = new byte[1024];
				while(inputStream.read(bytes) != -1) {
					toClient.write(bytes);
				}
				toClient.flush();
			}catch(Exception ex){
				String simplename = ex.getClass().getSimpleName();
				if("ClientAbortException".equals(simplename)){
				     logger.warn(">>>用户取消下载......");
				     toClient.write(JSONObject.toJSONString(new Result("error", "用户取消下载......")).getBytes("utf-8"));
				}else{
					 ex.printStackTrace();
					 toClient.write(JSONObject.toJSONString(new Result("error", ex.getMessage())).getBytes("utf-8"));
				}
				return;
			}

			if(-1 == last){
				logger.info(">>>文件已下载完成......");
				logger.info(">>>修改文件的下载数......");
				DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
					
					@Override
					public void run() {
						FileDownloadController.this.fileService.addDownloadCountOfSummaryData(realm, docId);
					}
				});
			}
		} catch (Exception ex) {
			 // 忽略 ClientAbortException 之类的异常
		} finally {
			try {

				if (inputStream != null)
					inputStream.close();
				if(toClient != null){
					toClient.close();
				}
			} catch (IOException io) {
				io.printStackTrace();
			}
		}
	}

    /**
     * PS：注意不能使用swagger提供的api页面测试，否则下载的文件不正常；直接在浏览器中进行测试
     * @param realm
     * @param docId
     */
	@ApiOperation(value = "根据id获取文件流", notes = "downloadFile")
	@RequestMapping(value = "/file/download/{realm}/{docId:\\{.+\\}}", method = { RequestMethod.GET })
	public void downloadFile(
			@ApiParam(value = "域") 
			@PathVariable String realm,
			@ApiParam(value = "ce中文档id") 
			@PathVariable String docId) {

		OutputStream toClient = null;
		try {
			FileContentLocalDTO fileDTO = this.fileService.getDocContentLocal(realm, docId);
			response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileDTO.getDocName(), "utf-8") );
			toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType(fileDTO.getContentType());
			//response.setContentLength(fileDTO.getSize());
			
			InputStream is = fileDTO.getContentInputStream();
			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = is.read(buffer)) != -1) {
				toClient.write(buffer, 0, length);
			}
			toClient.flush();
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			 try {
				toClient.write(JSONObject.toJSONString(new Result("error", ex.getMessage(), "下载失败")).getBytes("utf-8"));
				toClient.flush();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		} finally {
			try {
				// 关闭流
				if (toClient != null)
					toClient.close();
			} catch (IOException io) {
				logger.error(io.getMessage(), io);
			}
		}
	}

}
