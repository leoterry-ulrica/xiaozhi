package com.dist.bdf.base.utils;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

/**
 * 项目相关的工具类，提供获取项目的绝对路径等方法。
 * 
 * @author 何顺
 * @create 2014年12月11日
 */
public class WebUtil {
	/**
	 * 获取当前项目的路径
	 * 
	 * @return
	 */
	public static String getRootPath() {
		String classPath = WebUtil.class.getClassLoader().getResource("/").getPath();
		String rootPath = "";
		// windows下
		if ("\\".equals(File.separator)) {
			rootPath = classPath.substring(1, classPath.indexOf("/WEB-INF/classes"));
			rootPath = rootPath.replace("/", "\\");
		}
		// linux下
		if ("/".equals(File.separator)) {
			rootPath = classPath.substring(0, classPath.indexOf("/WEB-INF/classes"));
			rootPath = rootPath.replace("\\", "/");
		}
		return rootPath;
	}

	/**
	 * 获取该访问者的真实IP地址
	 * 
	 * @param request
	 *            请求
	 * @return IP地址字符串
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
