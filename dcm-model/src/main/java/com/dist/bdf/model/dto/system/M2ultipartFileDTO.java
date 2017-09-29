package com.dist.bdf.model.dto.system;

import java.io.Serializable;

/**
 * 
 * @author weifj
 * @version 1.0，2016/05/12，weifj，创建我的文件类
 *    M2ultipartFile：MyMultipartFile
 *
 */
public class M2ultipartFileDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String originalFilename;
	private String contentType;
	private long size;
	private byte[] contentStream;
	
	public String getOriginalFilename() {
		return originalFilename;
	}
	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public byte[] getContentStream() {
		return contentStream;
	}
	public void setContentStream(byte[] contentStream) {
		this.contentStream = contentStream;
	}

}
