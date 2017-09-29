package com.dist.bdf.model.dto.file;

import com.dist.bdf.base.utils.JSONUtil;
import com.dist.bdf.base.utils.Md5CaculateUtil;

/**
 * 文件信息
 * @author weifj
 * @version 1.0，2016/06/01，weifj，创建
 */
public class FileInfoRequestDTO {

	/**
	 * 文件名称
	 */
	private String baseFileName;
	/**
	 * 作者id
	 */
	private String ownerId;
	/**
	 * 文件大小，以bytes为单位
	 */
	private long size;
	/**
	 * 版本号，最后修改时间的值
	 */
	private long version;
	/**
	 * 文件类型
	 */
	private String mimeType;
	
	  public FileInfoRequestDTO() {
		  
	  }
    public FileInfoRequestDTO(String baseFileName, String ownerId, long size, long version, String mimeType) {
		
    	this.baseFileName = baseFileName;
    	this.ownerId = ownerId;
    	this.size = size;
    	this.version = version;
    	this.mimeType = mimeType;
    	
	}
	
	
	public String getBaseFileName() {
		return baseFileName;
	}
	public void setBaseFileName(String baseFileName) {
		this.baseFileName = baseFileName;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public long getVersion() {
		return version;
	}
	public void setVersion(long version) {
		this.version = version;
	}
	
	@Override
	public String toString() {
		
		return JSONUtil.toJSONString(this);
	}
	public static void main(String[]args){
		
		FileInfoRequestDTO info = new FileInfoRequestDTO("测试文档","weifj",23, 1229282, "txt");
		
		String md5 = Md5CaculateUtil.md5Hex(JSONUtil.toJSONString(info));
		System.out.println(md5);
	}


	public String getMimeType() {
		return mimeType;
	}


	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
}
