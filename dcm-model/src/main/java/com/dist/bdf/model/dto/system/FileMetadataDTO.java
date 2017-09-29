package com.dist.bdf.model.dto.system;

import java.io.Serializable;

public class FileMetadataDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 文件名
	 */
	private String name;
	/**
	 * 文件大小
	 */
	private float size;
	/**
	 * 扩展名
	 */
	private String extension;
	/**
	 * 作者
	 */
	private String owner;
	/**
	 * hash编码
	 */
	private String hasCode;
	/**
	 * 最后修改时间
	 */
	private long dateLastModified;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getSize() {
		return size;
	}
	public void setSize(float size) {
		this.size = size;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getHasCode() {
		return hasCode;
	}
	public void setHasCode(String hasCode) {
		this.hasCode = hasCode;
	}
	public long getDateLastModified() {
		return dateLastModified;
	}
	public void setDateLastModified(long dateLastModified) {
		this.dateLastModified = dateLastModified;
	}
	
}
