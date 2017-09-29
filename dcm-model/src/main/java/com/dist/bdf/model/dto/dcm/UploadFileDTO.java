package com.dist.bdf.model.dto.dcm;

import java.io.Serializable;

@Deprecated
public class UploadFileDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String simpleName;
	private String extensionWithPoint;
	private byte[] stream;
	
	/**
	 * 文件名称，不带扩展名
	 * @return
	 */
	public String getSimpleName() {
		return simpleName;
	}
	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}
	/**
	 * 扩展名，带有点号“.”
	 * @return
	 */
	public String getExtensionWithPoint() {
		return extensionWithPoint;
	}
	public void setExtensionWithPoint(String extensionWithPoint) {
		this.extensionWithPoint = extensionWithPoint;
	}
	/**
	 * 文件流
	 * @return
	 */
	public byte[] getStream() {
		return stream;
	}
	public void setStream(byte[] stream) {
		this.stream = stream;
	}
	
}
