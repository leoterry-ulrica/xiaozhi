package com.dist.bdf.model.dto.wechat;

import java.io.Serializable;
import java.util.Map;

/**
 * 微信模板消息（直接跟微信请求数据格式保持一致，忽略了驼峰命名规范）
 * 
 * @author weifj
 *
 */
public class WechatTemplate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 目标用户OPENID
	 */
	private String touser;
	/**
	 * 模板id
	 */
	private String template_id;
	/**
	 * URL置空，则在发送后，点击模板消息会进入一个空白页面（ios），或无法点击（android）
	 */
	private String url;
    /**
     * 表单id（小程序提交表单的id）
     */
    private String form_id;
    /**
     * 
     */
    private String page;
    /**
     * 详细内容
     */
	private Map<String, TemplateMsgData> data;

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, TemplateMsgData> getData() {
		return data;
	}

	public void setData(Map<String, TemplateMsgData> data) {
		this.data = data;
	}
	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getForm_id() {
		return form_id;
	}

	public void setForm_id(String form_id) {
		this.form_id = form_id;
	}

	@Override
	public String toString() {
		return "WechatTemplate [touser=" + touser + ", template_id=" + template_id + ", url=" + url + ", form_id=" + form_id + ", data=" + data + "]";
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}
}
