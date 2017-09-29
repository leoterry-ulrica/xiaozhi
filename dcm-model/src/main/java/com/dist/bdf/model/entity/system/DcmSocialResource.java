package com.dist.bdf.model.entity.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

/**
 * 资源社交化数据
 */
@Entity
@Table(name = "DCM_SOCIALRESOURCE")
public class DcmSocialResource extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields
	private String resId;
	private String parentResId;
	private String resTypeCode;
	private Long isFavorite;
	private Date timeLike;
	private Date timeFavorite;
	private Date timeTag;
	private Long isLike;
	/**
	 * 默认值是585d68
	 */
	private String tag = "585d68";
	private String creator;

	@Column(name="RESID", length=38)
	public String getResId() {
		return resId;
	}
	public void setResId(String resId) {
		this.resId = resId;
	}
	@Column(name="RESTYPECODE",length=50)
	public String getResTypeCode() {
		return resTypeCode;
	}
	public void setResTypeCode(String resTypeCode) {
		this.resTypeCode = resTypeCode;
	}
	@Column(name="ISFAVORITE")
	public Long getIsFavorite() {
		return isFavorite;
	}
	public void setIsFavorite(Long isFavorite) {
		this.isFavorite = isFavorite;
	}
	@Column(name="ISLIKE")
	public Long getIsLike() {
		return isLike;
	}
	public void setIsLike(Long isLike) {
		this.isLike = isLike;
	}
	@Column(name="TAG",length=10)
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	@Column(name="CREATOR",length=20)
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	@Column(name="PARENTRESID",length=38)
	public String getParentResId() {
		return parentResId;
	}

	public void setParentResId(String parentResId) {
		this.parentResId = parentResId;
	}
	/**
	 * 点赞时间
	 * @return
	 */
	@Column(name="TIMELIKE")
	public Date getTimeLike() {
		return timeLike;
	}
	public void setTimeLike(Date timeLike) {
		this.timeLike = timeLike;
	}
	/**
	 * 收藏时间
	 * @return
	 */
	@Column(name="TIMEFAVORITE")
	public Date getTimeFavorite() {
		return timeFavorite;
	}
	public void setTimeFavorite(Date timeFavorite) {
		this.timeFavorite = timeFavorite;
	}
	/**
	 * 标签时间
	 * @return
	 */
	@Column(name="TIMETAG")
	public Date getTimeTag() {
		return timeTag;
	}
	public void setTimeTag(Date timeTag) {
		this.timeTag = timeTag;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DcmSocialResource [resId=" + resId + ", parentResId=" + parentResId + ", resTypeCode=" + resTypeCode
				+ ", isFavorite=" + isFavorite + ", timeLike=" + timeLike + ", timeFavorite=" + timeFavorite
				+ ", timeTag=" + timeTag + ", isLike=" + isLike + ", tag=" + tag + ", creator=" + creator + "]";
	}

}