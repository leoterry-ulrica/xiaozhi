package com.dist.bdf.model.dto.dcm;

import com.dist.bdf.base.dto.BaseDTO;

public class FileContentDTO extends BaseDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String docName;
	private int size;
	//private InputStream contentStream;
	private byte[] contentStream;
	private Long lsize;
	
	/**
	 * 文件大小
	 * @return
	 */
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	/**
	 * 文件流
	 * @return
	 */
	public byte[] getContentStream() {
		return contentStream;
	}
	public void setContentStream(byte[] contentStream) {
		this.contentStream = contentStream;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	/**
	 * long类型大小
	 * @return
	 */
	public Long getLsize() {
		return lsize;
	}
	/**
	 * long类型大小
	 * @return
	 */
	public void setLsize(Long lsize) {
		this.lsize = lsize;
	}
}
