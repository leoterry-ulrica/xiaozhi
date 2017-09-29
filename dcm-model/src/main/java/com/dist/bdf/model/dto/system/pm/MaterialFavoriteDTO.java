package com.dist.bdf.model.dto.system.pm;

import java.io.Serializable;

/**
 * 
 * @author weifj
 * @version 1.0，2016/04/28，weifj，创建
 */
public class MaterialFavoriteDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	/**
	 * 用于客户端打开文档的时候，重定向到打开的文档路径
	 */
	private int type;
	/**
	 * 
	 */
	private boolean havePriv;
	/**
	 * 是否被共享过
	 */
	private boolean isShare;
	/**
	 * 收藏时间
	 */
	private long favoriteTime;
	
	/**
	 * 资源id
	 * @return
	 */
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 返回给客户端的资源类型
	 * @return
	 */
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * 是否有权限（只要能查看，就视为有权限）
	 * @return
	 */
	public boolean getHavePriv() {
		return havePriv;
	}
	public void setHavePriv(boolean havePriv) {
		this.havePriv = havePriv;
	}
	public boolean isShare() {
		return isShare;
	}
	public void setShare(boolean isShare) {
		this.isShare = isShare;
	}
	public long getFavoriteTime() {
		return favoriteTime;
	}
	public void setFavoriteTime(long favoriteTime) {
		this.favoriteTime = favoriteTime;
	}
}
