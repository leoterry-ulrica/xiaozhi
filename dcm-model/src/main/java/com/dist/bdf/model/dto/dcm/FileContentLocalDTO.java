package com.dist.bdf.model.dto.dcm;

import java.io.InputStream;
/**
 * 包括文件内容和属性的数据模型
 * @author weifj
 *
 */
public class FileContentLocalDTO extends FileContentDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InputStream contentInputStream;
	private String contentType;
	/**
	 * 存储文档相关属性
	 */
	private DocumentDTO docProperties;
	
	public FileContentLocalDTO() {
		
	}

/*	public FileContentLocalDTO(String name, String contentType, InputStream is) {

		super.setDocName(name);
		this.setContentType(contentType);
		this.setContentInputStream(is);
	}*/
	
	public InputStream getContentInputStream() {
		return contentInputStream;
	}

	public void setContentInputStream(InputStream contentInputStream) {
		this.contentInputStream = contentInputStream;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public DocumentDTO getDocProperties() {
		return docProperties;
	}

	public void setDocProperties(DocumentDTO docProperties) {
		this.docProperties = docProperties;
	}

}
