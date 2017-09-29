package com.dist.bdf.model.dto.sga;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 
 * 企业图片信息
 * @author weifj
 *
 */
public class ImgInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotEmpty(message = "ImgInfo property id cannot be empty")
	private String id;
	/**
	 * mime-type
	 */
	private String type;
	/**
	 * 后缀
	 */
	@NotEmpty(message = "ImgInfo property suffix cannot be empty")
	private String suffix;
	/**
	 * base64编码值
	 */
	@NotEmpty(message = "ImgInfo property content cannot be empty")
	private String content;
	/**
	 * 名称
	 */
	private String name;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "ImgInfo [id=" + id + ", type=" + type + ", suffix=" + suffix + ", content=" + content + "]";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
