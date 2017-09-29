package com.dist.bdf.model.dto.file;

import com.dist.bdf.base.utils.JSONUtil;

public class FileInfoResponseDTO {

	/**
	 * 文件id
	 */
	private String fileId;
	/**
	 * 文件大小
	 */
	private long size;
	/**
	 * 是否已存在
	 */
	private boolean exist;
	
	public FileInfoResponseDTO(String fileId, long size, boolean exist) {
		
		this.fileId = fileId;
		this.size = size;
		this.exist = exist;
	}
	
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	
	@Override
	public String toString() {
		
		return JSONUtil.toJSONString(this);
	}

	public boolean isExist() {
		return exist;
	}

	public void setExist(boolean exist) {
		this.exist = exist;
	}
}
