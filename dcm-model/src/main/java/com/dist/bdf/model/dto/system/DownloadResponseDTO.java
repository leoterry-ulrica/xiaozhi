package com.dist.bdf.model.dto.system;

import java.io.Serializable;
/**
 * 下载参数DTO
 * @author weifj
 * @version 1.0，2016/05/03，weifj
 *
 */
public class DownloadResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private int type;
	private long downloadTime;
	private boolean havePriv;
	
	/**
	 * 被下载的资源id
	 * @return
	 */
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 客户端传过来的类型
	 * @return
	 */
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getDownloadTime() {
		return downloadTime;
	}
	public void setDownloadTime(long downloadTime) {
		this.downloadTime = downloadTime;
	}
	/**
	 * 是否有权限再次下载
	 * @return
	 */
	public boolean isHavePriv() {
		return havePriv;
	}
	public void setHavePriv(boolean havePriv) {
		this.havePriv = havePriv;
	}
}
