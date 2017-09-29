package com.dist.bdf.model.dto.system;

import java.io.Serializable;

/**
 * 资源社交化数据dto，为了简化返回结果属性，不继承基类BaseDTO
 */
public class SocialWzSimpleResultDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields
	private boolean isFavorite;
	private boolean isTop;
	private String tag;
	
	/**
	 * 对应实体模型的isLike，为了与前端模型保持一致，快速解析
	 * @return
	 */
	public boolean getIsTop() {
		return isTop;
	}

	public void setIsTop(boolean isTop) {
		this.isTop = isTop;
	}

	public void setIsFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}
	
	public boolean getIsFavorite() {
		return isFavorite;
	}

	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}

}