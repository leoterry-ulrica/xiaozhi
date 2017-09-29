package com.dist.bdf.base.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.HandlerMapping;

import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.result.Result;
import com.dist.bdf.base.utils.JSONUtil;

import net.sf.json.util.CycleDetectionStrategy;

/**
 * 基础Controller，提供一些通用的功能
 * @author 李其云 2015-9-22
 * @version 1.0，2016/03/15，weifj
 *    1. protected Result successResult(Object data)：返回成功对象
 *    2. protected Result failResult(Object data)：返回失败对象
 * 
 *  PS：
 *  @RequestMapping(value = "sysservice",produces = "text/plain;charset=UTF-8")其中不能带上produces = "text/plain;charset=UTF-8"，
 *     否则使用@RestController的时候，调接口抛出异常：
 *     1）页面上：The resource identified by this request is only capable of generating responses with characteristics not acceptable according to the request "accept" headers；
 *     2）tomcat控制台： Could not find acceptable representation
 * 
 */
public class BaseController {

	protected static Logger LOG = LoggerFactory.getLogger(BaseController.class);
	/**
	 * session会话
	 */
	protected HttpSession session;

	/**
	 * httpRequest
	 */

	protected HttpServletRequest request;
	/**
	 * response
	 */
	protected HttpServletResponse response;

	/**
	 * @param request
	 *            请求
	 */
	@ModelAttribute
	public void setRequest(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.session = request.getSession();
	}

	/**
	 * 获取发送请求的客户端ip
	 * @return 返回ip地址
	 */
	protected String getRequestIp() {

		return this.request.getRemoteAddr();
	}
	/** 
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址; 
     *  
     * @param request 
     * @return 
     * @throws IOException 
     */  
    public final static String getClientIpAddress(HttpServletRequest request) {  
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址  
  
        String ip = request.getHeader("X-Forwarded-For");  
        if (LOG.isInfoEnabled()) {  
        	LOG.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);  
        }  
  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("Proxy-Client-IP");  
                if (LOG.isInfoEnabled()) {  
                	LOG.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);  
                }  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("WL-Proxy-Client-IP");  
                if (LOG.isInfoEnabled()) {  
                	LOG.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);  
                }  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_CLIENT_IP");  
                if (LOG.isInfoEnabled()) {  
                	LOG.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);  
                }  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
                if (LOG.isInfoEnabled()) {  
                	LOG.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);  
                }  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getRemoteAddr();  
                if (LOG.isInfoEnabled()) {  
                	LOG.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);  
                }  
            }  
        } else if (ip.length() > 15) {  
            String[] ips = ip.split(",");  
            for (int index = 0; index < ips.length; index++) {  
                String strIp = (String) ips[index];  
                if (!("unknown".equalsIgnoreCase(strIp))) {  
                    ip = strIp;  
                    break;  
                }  
            }  
        }  
        return ip;  
    }  
	// 数据绑定
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		// CustomDateEditor 可以换成自己定义的编辑器。
		// 注册一个Date 类型的绑定器 。
		binder.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
	}

	/**
	 * 返回成功的json字符串。
	 *
	 * @author 何顺
	 * @return
	 */
	protected String success() {
		return new Result(Result.SUCCESS).toJsonString();
	}

	/**
	 * 返回成功的json字符串
	 *
	 * @author 何顺
	 * @param data
	 *            数据
	 * @return
	 */
	protected String success(Object data) {
		return new Result(Result.SUCCESS, data).toJsonString();
	}

	/**
	 * 返回成功的对象
	 * @param data
	 * @return
	 */
	protected Result successResult(Object data) {
		return new Result(Result.SUCCESS, data, "操作成功", HttpServletResponse.SC_OK);
	}

	/**
	 * 返回成功的json字符串
	 *
	 * @author weifj
	 * @param data
	 * @param cycleDetectionStrategy 转换成json的级别设置
	 *            数据
	 * @return
	 */
	protected String success(Object data, CycleDetectionStrategy cycleDetectionStrategy) {
		return new Result(Result.SUCCESS, data).toJsonString(cycleDetectionStrategy);
	}

	/**
	 * 返回失败时的json字符串
	 *
	 * @author 何顺
	 * @param data
	 *            数据
	 * @return
	 */
	protected String fail(Object data) {
		return new Result(Result.FAIL, data).toJsonString();
	}

	/**
	 * 
	 * @param ex
	 * @return
	 */
	protected String fail(Exception ex) {
		return new Result(Result.FAIL, ex.getMessage()).toJsonString();
	}

	/**
	 * 返回业务判断失败的结果
	 * @param data
	 * @return
	 */
	protected Result failResult(Object data) {
		return new Result(Result.FAIL, data, "操作失败");
	}
	
	protected Result failResult(Object data, String message) {
		return new Result(Result.FAIL, data, message);
	}

	/**
	 * 返回失败的结果
	 * @param data
	 * @return
	 */
	protected Result errorResult(Object data) {
		return new Result(Result.ERROR, data);
	}
	/**
	 * 处理业务异常结果
	 * @param ex
	 * @return
	 */
	protected Result failResultException(Exception ex) {
		ex.printStackTrace();
		return failResult(ex.getMessage());
	}
	

	/**
	 * 处理系统无法处理的异常结果
	 * @param ex
	 * @return
	 */
	protected Result errorResultException(Exception ex) {
		ex.printStackTrace();
		return errorResult(ex.getMessage());
	}

	/**
	 * 获取请求的父级URL
	 * @param request
	 * @return
	 */
	protected String getBaseURL(HttpServletRequest request) {
	
		if (null == request) {
			throw new BusinessException("请求的request为空");
		}
		// 忽略默认端口80和443
		String port = ((80 ==  request.getServerPort() || 443 ==  request.getServerPort())? "" : (":" + request.getServerPort()));
		// 在反向代理服务器（如nginx配置了X-Forwarded-Scheme或者X-Forwarded-Proto），为了获取https
		String scheme = request.getHeader("X-Forwarded-Scheme");
		if(StringUtils.isEmpty(scheme)) {
			scheme = request.getHeader("X-Forwarded-Proto");
			if(StringUtils.isEmpty(scheme)) {
				return request.getScheme() + "://" + request.getServerName() + port + request.getContextPath();
			}
		}
		return scheme + "://" + request.getServerName() + port + request.getContextPath();
	}
	/**
	 * 获取
	 * @return
	 */
	 protected String getBaseURL() {
		return this.getBaseURL(request);
	}
	 
	/**
	 * 获取上下文物理路径
	 * @param request
	 * @return
	 */
	protected String getContextPath(HttpServletRequest request){
		
		if (null == request) {
			throw new BusinessException("请求的request为空");
		}
		return request.getServletContext().getRealPath("/");
		
	}
	/**
	 * 获取上下文物理路径
	 * @return
	 */
    protected String getContextPath(){
		
		if (null == request) {
			throw new BusinessException("请求的request为空");
		}
		return request.getServletContext().getRealPath("/");
		
	}
	/**
	 * 获取上下文物理路径
	 * @param relativePath
	 * @return
	 */
	protected String getContextPath(String relativePath){
		
		if (null == request) {
			throw new BusinessException("请求的request为空");
		}
		String path = request.getServletContext().getRealPath(relativePath);
		File dir = new File(path);
		if(!dir.exists()){
			dir.mkdirs();
		}
		return dir.getAbsolutePath();
	}
   protected String getContextPath(HttpServletRequest request, String relativePath){
		
		if (null == request) {
			throw new BusinessException("请求的request为空");
		}
		String path = request.getServletContext().getRealPath(relativePath);
		File dir = new File(path);
		if(!dir.exists()){
			dir.mkdirs();
		}
		return dir.getAbsolutePath();
	}
	
	
	/**
	 * 返回验证是否成功的字符串
	 *
	 * @author 何顺
	 * @param existed
	 *            验证结果，true表示验证正确
	 * @return
	 */
	protected String validate(boolean existed) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("valid", existed);
		String jsonString = JSONUtil.toJSONString(result);
		return jsonString;
	}

	/**
	 * 把指定URL后的字符串全部截断当成参数
	 * 这么做是为了防止URL中包含中文或者特殊字符（/等）时，匹配不了的问题
	 * @param request
	 * @return
	 */
	protected String extractPathFromPattern(HttpServletRequest request) {

		String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);

	}

	/**
	 * 统一异常处理，实现内容可以个性化定制 
	 * @param response
	 * @param ex
	 * @return
	 * @throws Exception
	 */
	@ExceptionHandler({ RuntimeException.class, Exception.class })
	public Result exceptionHandler(HttpServletResponse response, Exception ex) {

		LOG.error("Result exceptionHandler, detail：" + ex.getMessage());
		ex.printStackTrace();
		String msg = ex.getMessage();
		if (ex instanceof BusinessException || msg.contains("BusinessException")) {

			return this.failResult(ex.getMessage());
		} else {
			if (msg.contains("E_OBJECT_NOT_FOUND")) {
				// msg.contains("类名：Document")
				return this.failResult("文档或请求项不存在。");
			}
			return this.errorResult(ex.getMessage());
		}
	}

	/**
	 * AJAX输出，返回null
	 * @param content
	 * @param type
	 * @param response
	 * @return
	 */
    public String ajax(String content, String type, HttpServletResponse response) {
        try {
            response.setContentType(type + ";charset=UTF-8");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.getWriter().write(content);
            response.getWriter().flush();
        } catch (IOException e) {
        	LOG.error("ajax", e);
        }
        return null;
    }
	
	/**
	 * AJAX输出HTML，返回null
	 * @param html
	 * @param response
	 * @return
	 */
    public String ajaxHtml(String html, HttpServletResponse response) {
        return ajax(html, "text/html", response);
    }
    
    /**
     * AJAX输出json，返回null
     * @param json
     * @param response
     * @return
     */
    public String ajaxJson(String json, HttpServletResponse response) {
        return ajax(json, "application/json", response);
    }
    /**
     * 获取成功消息
     * @param data
     * @param message
     * @return
     */
	public Result successResult(Object data, String message) {
		return new Result(Result.SUCCESS, data, message, HttpServletResponse.SC_OK);
	}
}
