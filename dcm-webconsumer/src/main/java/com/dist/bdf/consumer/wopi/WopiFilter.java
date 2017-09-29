package com.dist.bdf.consumer.wopi;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;

import com.dist.bdf.model.dto.dcm.OfficeFileContentDTO;
import com.dist.bdf.facade.service.DOfficeWebService;

@WebFilter("/wopi/*")
public class WopiFilter implements Filter {

	 private ServletContext  servletContext;
	  
	private DOfficeWebService officeOnline;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		servletContext  =filterConfig.getServletContext();
		Object ob = servletContext .getAttribute(
			     WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		WebApplicationContext context = (WebApplicationContext) ob;
		officeOnline = (DOfficeWebService) context.getBean("DOfficeWebService");
		
	}
	private void getFile(OfficeFileContentDTO content, HttpServletResponse response) {
		
		OutputStream toClient = null;

		try {
			toClient = new BufferedOutputStream(response.getOutputStream());
			String contentType = "application/octet-stream";
			//InputStream fis = content.getContentStream();
			// 以流的形式下载文件
			System.out.println("====>文件流大小："+content.getSize());
			//byte[] buffer = new byte[content.getSize()];//不能使用fis.available()，这个方法获取的大小并不是源文件大小
			//fis.read(buffer);
			//fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition",
					"attachment;filename=" + content.getDocName());
			response.addHeader("Content-Length", "" + content.getSize());
		
			response.setContentType(contentType);
			toClient.write(content.getContentStream());//toClient.write(buffer);
			toClient.flush();
			toClient.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if(toClient != null){
				try {
					toClient.flush();
					toClient.close();
				} catch (IOException e) {
		
					e.printStackTrace();
				}
			}
		}
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String uri = httpRequest.getRequestURI(); 
		//String fullPlay = uri.substring(uri.lastIndexOf("&"));
		//uri = uri.substring(0,uri.lastIndexOf("&"));
		// 解决中文乱码问题
		String fileUri = URLDecoder.decode(uri.substring(uri.indexOf("/wopi/") + 1, uri.length()), "UTF-8"); // /wopi/files/6F532108-35A3-47E3-A8AF-36D56D88D00
		// id不能带有花括号{}
		String docId =  "";
		
		if (fileUri.endsWith("/contents")) { // GetFile ：返回文件流
			fileUri = fileUri.substring(0,fileUri.indexOf("/contents"));
			docId = "{"+fileUri.substring(fileUri.lastIndexOf("files")+6)+"}";
			System.out.println("====>预览文档id："+docId);
			OfficeFileContentDTO content = officeOnline.getFile(docId);
			getFile(content, httpResponse);

		} else { // CheckFileInfo ：返回json
			docId = "{"+fileUri.substring(fileUri.lastIndexOf("files")+6,fileUri.lastIndexOf("/"))+"}";
			System.out.println("====>checkFileInfo，文档id："+docId);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter out = null;
			try {
				out = response.getWriter();
				String json = officeOnline.checkFileInfo(docId);
				System.out.println(json);
				out.write(json);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					out.close();
				}
			}
		}
		

	}


	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
