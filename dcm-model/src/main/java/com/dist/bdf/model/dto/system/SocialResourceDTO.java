package com.dist.bdf.model.dto.system;

import com.dist.bdf.base.dto.BaseDTO;

/**
 * 资源社交化数据dto
 */
public class SocialResourceDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields
	//private String resId;
	private String parentResId;
	protected boolean isFavorite;
	protected boolean isTop;
	protected String tag;
	protected String creator;

	/*public String getResId() {
		return resId;
	}
	public void setResId(String resId) {
		this.resId = resId;
	}*/

	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public boolean getIsFavorite() {
		return isFavorite;
	}
	public void setIsFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}
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

	public String getParentResId() {
		return parentResId;
	}

	public void setParentResId(String parentResId) {
		this.parentResId = parentResId;
	}
	
}