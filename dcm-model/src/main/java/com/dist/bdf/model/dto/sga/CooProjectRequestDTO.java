package com.dist.bdf.model.dto.sga;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 添加合作项目的请求参数
 * @author weifj
 *
 */
public class CooProjectRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	/**
	 * ecm中case的id标识
	 */
	@NotEmpty(message = "CooProjectAddDTO property [caseId] can not be empty")
	private String caseId;
	@NotEmpty(message = "CooProjectAddDTO property [realm] can not be empty")
	private String realm;
	/**
	 * 项目状态。0：关闭；1：招募中；2：合作中；3：合作结束
	 */
	private int status;
	private String tag;
	/**
	 * 海报信息
	 */
	private ImgInfo posterInfo;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 招募方向
	 */
	private String direction;
	/**
	 * 区域，省市县
	 */
	private String region;
	
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public ImgInfo getPosterInfo() {
		return posterInfo;
	}
	public void setPosterInfo(ImgInfo posterInfo) {
		this.posterInfo = posterInfo;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	@Override
	public String toString() {
		return "CooProjectRequestDTO [name=" + name + ", caseId=" + caseId + ", realm=" + realm + ", status=" + status
				+ ", tag=" + tag + ", posterInfo=" + posterInfo + ", description=" + description + ", direction="
				+ direction + ", region=" + region + "]";
	}

}
