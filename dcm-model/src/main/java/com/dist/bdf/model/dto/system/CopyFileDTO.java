package com.dist.bdf.model.dto.system;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
/**
 * 文件拷贝数据模型
 * @author weifj
 *
 */
public class CopyFileDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 资源来源的域
	 */
	@NotNull(message = "CopyFileDTO property fromRealm canno be empty")
	private String fromRealm;
	/**
	 * 资源需要拷贝到目标域，如果为空，则使用默认的公共库
	 */
	private String toRealm;
	/**
	 * 资源id
	 */
	@NotNull(message = "CopyFileDTO property resId canno be empty")
	private String resId;
	/**
	 * 是否文档
	 */
	private Boolean isDoc;
	/**
	 * 文种类型：material（资料）和news（新闻）
	 */
	@NotNull(message = "CopyFileDTO property fileType canno be empty")
	private String fileType;
	/**
	 * 操作者id
	 */
	private String userId;
	
	public String getFromRealm() {
		return fromRealm;
	}
	public void setFromRealm(String fromRealm) {
		this.fromRealm = fromRealm;
	}
	public String getToRealm() {
		return toRealm;
	}
	public void setToRealm(String toRealm) {
		this.toRealm = toRealm;
	}
	public String getResId() {
		return resId;
	}
	public void setResId(String resId) {
		this.resId = resId;
	}
	public Boolean getIsDoc() {
		return isDoc;
	}
	public void setIsDoc(Boolean isDoc) {
		this.isDoc = isDoc;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

}
