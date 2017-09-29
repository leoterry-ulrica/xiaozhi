package com.dist.bdf.service.file.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.dist.bdf.base.constants.MimeType;
import com.dist.bdf.base.utils.StringUtil;
import com.dist.bdf.model.dto.dcm.FileContentLocalDTO;
import com.dist.bdf.model.dto.system.M2ultipartFileDTO;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

public final class FileOperateUtil {

	public static String FILEDIR = null;

	private static Logger logger = LoggerFactory.getLogger(FileOperateUtil.class);
	/**
	 * 上传
	 * 
	 * @param request
	 * @throws IOException
	 */
	public static void upload(HttpServletRequest request) throws IOException {
		MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = mRequest.getFileMap();
		File file = new File(FILEDIR);
		if (!file.exists()) {
			file.mkdir();
		}
		Iterator<Map.Entry<String, MultipartFile>> it = fileMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, MultipartFile> entry = it.next();
			MultipartFile mFile = entry.getValue();
			if (mFile.getSize() != 0 && !"".equals(mFile.getName())) {
				write(mFile.getInputStream(), new FileOutputStream(initFilePath(mFile.getOriginalFilename())));
			}
		}
	}

	private static String initFilePath(String name) {
		String dir = getFileDir(name) + "";
		File file = new File(FILEDIR + dir);
		if (!file.exists()) {
			file.mkdir();
		}
		Long num = new Date().getTime();
		Double d = Math.random() * num;
		return (file.getPath() + "/" + num + d.longValue() + "_" + name).replaceAll(" ", "-");
	}

	private static int getFileDir(String name) {
		return name.hashCode() & 0xf;
	}

	public static void download(String downloadfFileName, ServletOutputStream out) {

		try {
			FileInputStream in = new FileInputStream(new File(FILEDIR + "/" + downloadfFileName));
			write(in, out);
		} catch (FileNotFoundException e) {
			try {
				FileInputStream in = new FileInputStream(
						new File(FILEDIR + "/" + new String(downloadfFileName.getBytes("iso-8859-1"), "utf-8")));
				write(in, out);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 写入数据
	 * 
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static void write(InputStream in, OutputStream out) throws IOException {
		try {
			byte[] buffer = new byte[1024];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
			out.flush();
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
			}
			try {
				out.close();
			} catch (IOException ex) {
			}
		}
	}
	
	/**
	 * 接收上传的文件
	 * @param request
	 * @return
	 */
	public static List<M2ultipartFileDTO> getUploadFiles(HttpServletRequest request) {

		List<M2ultipartFileDTO> uploadFiles = new ArrayList<M2ultipartFileDTO>();
		try {
			int count = 0;
			//创建一个通用的多部分解析器
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
					request.getSession().getServletContext());
			//判断 request 是否有文件上传,即多部分请求
			if (multipartResolver.isMultipart(request)) {
				//转换成多部分request  
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				//取得request中的所有文件名
				Iterator<String> iter = multiRequest.getFileNames();

				while (iter.hasNext()) {
					count++;
					//记录上传过程起始时的时间，用来计算上传时间
					//int pre = (int) System.currentTimeMillis();
					//取得上传文件
					MultipartFile mpfile = multiRequest.getFile(iter.next());
					if (mpfile != null) {
						uploadFiles.add(toMyMultipartFile(mpfile));
					}
				}
			}
			logger.info("接收到 [{}] 个文件", count);
		} catch (Exception ex) {
			logger.error(ex.getMessage());

		}
		return uploadFiles;
	}
	
	/**
	 * 接收上传的文件
	 * @param request
	 * @return
	 */
	public static List<MultipartFile> getUploadMFiles(HttpServletRequest request) {

		List<MultipartFile> uploadFiles = new ArrayList<MultipartFile>();
		try {
			int count = 0;
			//创建一个通用的多部分解析器
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
					request.getSession().getServletContext());
			//判断 request 是否有文件上传,即多部分请求
			if (multipartResolver.isMultipart(request)) {
				//转换成多部分request  
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				//取得request中的所有文件名
				Iterator<String> iter = multiRequest.getFileNames();

				while (iter.hasNext()) {
					count++;
					//记录上传过程起始时的时间，用来计算上传时间
					//int pre = (int) System.currentTimeMillis();
					//取得上传文件
					MultipartFile mpfile = multiRequest.getFile(iter.next());
					if (mpfile != null) {
						uploadFiles.add(mpfile);
					}
				}
			}
			logger.info("接收到 [{}] 个文件", count);
		} catch (Exception ex) {
			logger.error(ex.getMessage());

		}
		return uploadFiles;
	}

	/**
	 * MultipartFile转换成自定义的M2ultipartFileDTO
	 * @param mpfile
	 * @param extension
	 * @return
	 * @throws IOException
	 * @throws MagicParseException
	 * @throws MagicMatchNotFoundException
	 * @throws MagicException
	 */
	public static M2ultipartFileDTO toMyMultipartFile(MultipartFile mpfile)
			throws IOException, MagicParseException, MagicMatchNotFoundException, MagicException {
		
		String originalFilename = mpfile.getOriginalFilename();
		String extension = "";
		if(originalFilename.contains(".")) {
			extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		}
		M2ultipartFileDTO m2file = new M2ultipartFileDTO();
		m2file.setOriginalFilename(originalFilename);
		m2file.setContentStream(mpfile.getBytes());
		String mimeType = MimeType.getMimeType(extension);
		if(StringUtil.isNullOrEmpty(mimeType)){
			
			logger.info(">>>开始字节流获取文件mimeType");
			MagicMatch match = Magic.getMagicMatch(mpfile.getBytes(), true);
			mimeType = (null == match)? mpfile.getContentType() : match.getMimeType();// file.getContentType()不准确
			logger.info(">>>结束获取到的文件mimeType："+ mimeType);
		}

		m2file.setContentType(mimeType);
		m2file.setSize(mpfile.getSize());
		return m2file;
	}
	
	public static FileContentLocalDTO toFileContentLocalDTO(MultipartFile mpfile, InputStream is)
	
			throws IOException, MagicParseException, MagicMatchNotFoundException, MagicException {
		
		String originalFilename = mpfile.getOriginalFilename();
		String extension = "";
		if(originalFilename.contains(".")) {
			extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		}
		FileContentLocalDTO m2file = new FileContentLocalDTO();
		m2file.setDocName(mpfile.getOriginalFilename());
		m2file.setContentInputStream(is);
		// String mimeType = MimeType.getMimeType(extension);
		/*if(StringUtil.isNullOrEmpty(mimeType)){
			logger.info(">>>使用默认的mimeType：application/octet-stream");
			mimeType = "application/octet-stream";*/
		/*	logger.info(">>>开始字节流获取文件mimeType");
			MagicMatch match = Magic.getMagicMatch(mpfile.getBytes(), false);
			mimeType = (null == match)? mpfile.getContentType() : match.getMimeType();// file.getContentType()不准确
			logger.info(">>>结束获取到的文件mimeType："+ mimeType);*/
		//}

		m2file.setContentType(MimeType.getMimeType(extension));
		return m2file;
	}
	/**
	 * 
	 * @param mpfile
	 * @param bytes 文件真实大小
	 * @return
	 * @throws IOException
	 * @throws MagicParseException
	 * @throws MagicMatchNotFoundException
	 * @throws MagicException
	 */
	public static M2ultipartFileDTO toMyMultipartFile(MultipartFile mpfile, byte[] bytes)
			throws IOException, MagicParseException, MagicMatchNotFoundException, MagicException {
		
		String originalFilename = mpfile.getOriginalFilename();
		String extension = originalFilename.substring( mpfile.getOriginalFilename().lastIndexOf("."));
		M2ultipartFileDTO m2file = new M2ultipartFileDTO();
		m2file.setOriginalFilename(originalFilename);
		m2file.setContentStream(bytes);
		String mimeType = MimeType.getMimeType(extension);
		if(StringUtil.isNullOrEmpty(mimeType)){
			
			logger.info("--->开始字节流获取文件mimeType");
			MagicMatch match = Magic.getMagicMatch(mpfile.getBytes(), true);
			mimeType = (null == match)? mpfile.getContentType() : match.getMimeType();// file.getContentType()不准确
			logger.info("--->结束获取到的文件mimeType："+ mimeType);
		}

		m2file.setContentType(mimeType);
		m2file.setSize(mpfile.getSize());
		return m2file;
	}
}
