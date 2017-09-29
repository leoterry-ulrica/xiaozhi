package com.dist.bdf.model.dto.system;

import java.io.Serializable;
import java.util.List;

/**
 * 被共享的信息
 * @author weifj
 * @version 1.0，2016/05/04，weifj，创建
 */
public class SharedInfoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private Long status;
	private String user;
	private int s_type;
	private String s_dc;
	private int t_type;
	private String t_dc;
	private long sharedTime;
	private long isFolder;
	private List<String> privCodes;
	/**
	 * 是否有权下载
	 */
	private boolean havePriv;
	/**
	 * 是否被收藏
	 */
	private boolean isFavorite;

	//private String shareTimeStr;
	/**
	 * 有效期
	 */
	//private String expiryTimeStr;
	
	/**
	 * 共享资源id
	 * @return
	 */
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}


	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public int getS_type() {
		return s_type;
	}
	public void setS_type(int s_type) {
		this.s_type = s_type;
	}
	public String getS_dc() {
		return s_dc;
	}
	public void setS_dc(String s_dc) {
		this.s_dc = s_dc;
	}
	public int getT_type() {
		return t_type;
	}
	public void setT_type(int t_type) {
		this.t_type = t_type;
	}
	/*public String getT_dc() {
		return t_dc;
	}
	public void setT_dc(String t_dc) {
		this.t_dc = t_dc;
	}
	public String getShareTimeStr() {
		return shareTimeStr;
	}
	public void setShareTimeStr(String shareTimeStr) {
		this.shareTimeStr = shareTimeStr;
	}*/
/*	public String getExpiryTimeStr() {
		return expiryTimeStr;
	}
	public void setExpiryTimeStr(String expiryTimeStr) {
		this.expiryTimeStr = expiryTimeStr;
	}*/
	public String getT_dc() {
		return t_dc;
	}
	public void setT_dc(String t_dc) {
		this.t_dc = t_dc;
	}
	public boolean isHavePriv() {
		return havePriv;
	}
	public void setHavePriv(boolean havePriv) {
		this.havePriv = havePriv;
	}
	public boolean getIsFavorite() {
		return isFavorite;
	}
	public void setIsFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}
	public long getSharedTime() {
		return sharedTime;
	}
	public void setSharedTime(long sharedTime) {
		this.sharedTime = sharedTime;
	}
	public long getIsFolder() {
		return isFolder;
	}
	public void setIsFolder(long isFolder) {
		this.isFolder = isFolder;
	}
	public List<String> getPrivCodes() {
		return privCodes;
	}
	public void setPrivCodes(List<String> privCodes) {
		this.privCodes = privCodes;
	}

}
