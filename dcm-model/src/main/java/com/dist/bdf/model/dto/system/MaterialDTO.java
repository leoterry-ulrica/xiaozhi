package com.dist.bdf.model.dto.system;

import java.io.Serializable;

/**
 * 
 * @author weifj
 * @version 1.0，2016/05/17，weifj，创建
 */
public class MaterialDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用于客户端打开文档的时候，重定向到打开的文档路径
	 */
	private int type;
	/**
	 * 是否有下载权限
	 */
	private boolean havePriv;
	/**
	 * 是否已收藏
	 */
	private boolean isFavorite;
	
	public MaterialDTO( int type, boolean havePriv, boolean isFavorite) {
		this.type = type;
		this.havePriv = havePriv;
		this.isFavorite = isFavorite;
	}

	public boolean getIsFavorite() {
		return isFavorite;
	}
	public void setIsFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
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
}
