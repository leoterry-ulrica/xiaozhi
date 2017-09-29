package com.dist.bdf.model.dto.dcm;

import java.io.Serializable;
import java.util.List;

import com.dist.bdf.model.dto.system.ThumbnailDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 文件夹的DTO
 * 
 * @author weifj
 * @version 1.0，2016/04/20，weifj，创建文档类
 * 
 */
public class DocumentDTO implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 文件夹ID
	 */
	private String id;

	/**
	 * 文件夹名称
	 */
	private String name;
	/**
	 * 別名
	 */
	private String text;
	/**
	 * 所有者
	 */
	private String owner;
	/**
	 * 修改人
	 */
	private String modifiedBy;

	/**
	 * 修改时间
	 */
	private long modifiedOn;

	/**
	 * parent folder id
	 */
	private String pid;
	/**
	 * 文件真实大小
	 */
	private Double size;

	/**
	 * 主版本号，如：2
	 */
	private Integer majorVersion;
	/**
	 * 次要版本号，如：0
	 */
	private Integer minorVersion;
	/**
	 * 是否文件夹
	 */
	private boolean isFolder;
	/**
	 * 文档标签
	 */
	private String docTags;

	/**
	 * 是否检出
	 */
	private boolean isCheckout;

	private String versionStatus;

	/**
	 * 状态
	 */
	private String state;

	private String path;
	/**
	 * mimetype
	 */
	private String mimeType;
	/**
	 * 文种
	 */
	private String fileType;
	/**
	 * 所属区域
	 */
	private String region;
	/**
	 * 所属机构
	 */
	private String organization;
	/**
	 * 业务类型
	 */
	private String business;
	/**
	 * 空间域
	 */
	private String domain;
	/**
	 * 资源类型
	 */
	private String resourceType;
	/**
	 * 简介
	 */
	private String describe;
	/**
	 * 创建时间
	 */
	private Long dateCreated;
	/**
	 * 是否收藏
	 */
	private boolean isFavorite;
	/**
	 * 创建者，登录名
	 */
	private String creator;
	/**
	 * 匹配度
	 */
	private Double rank;
	/**
	 * 发布者
	 */
	private String publisher;
	/**
	 * 子文件夹
	 */
	private List<DocumentDTO> children;
	/**
	 * 版本系列id
	 */
	private String versionSeriesId;
	/**
	 * 如果是文件夹，这个属性才生效
	 * 如果当前文件夹下有其它内容，则为false；否则为true
	 */
	private boolean isEmpty;
	/**
	 * 缩略图
	 */
	private ThumbnailDTO thumbnail;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(long modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Double getSize() {
		return size;
	}

	public void setSize(Double size) {
		this.size = size;
	}

	public Integer getMajorVersion() {
		return majorVersion;
	}

	public void setMajorVersion(Integer majorVersion) {
		this.majorVersion = majorVersion;
	}

	@JsonIgnore
	public String getVersionStatus() {
		return versionStatus;
	}

	public void setVersionStatus(String versionStatus) {
		this.versionStatus = versionStatus;
	}
	@JsonIgnore
	public boolean getIsCheckout() {
		return isCheckout;
	}

	public void setIsCheckout(boolean isCheckout) {
		this.isCheckout = isCheckout;
	}

	public Integer getMinorVersion() {
		return minorVersion;
	}

	public void setMinorVersion(Integer minorVersion) {
		this.minorVersion = minorVersion;
	}
	@JsonIgnore
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

/*	public List<DocumentDTO> getChildren() {
		return children;
	}

	public void setChildren(List<DocumentDTO> children) {
		this.children = children;
	}
*/
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * 文种
     * @return
     */
	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * 所属区域
	 * @return
	 */
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * 所属组织
	 * @return
	 */
	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	/**
	 * 业务类型
	 * @return
	 */
	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public boolean getIsFolder() {
		return isFolder;
	}

	public void setIsFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}

	public List<DocumentDTO> getChildren() {
		return children;
	}

	public void setChildren(List<DocumentDTO> children) {
		this.children = children;
	}

	public String getDocTags() {
		return docTags;
	}

	public void setDocTags(String docTags) {
		this.docTags = docTags;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getVersionSeriesId() {
		return versionSeriesId;
	}

	public void setVersionSeriesId(String versionSeriesId) {
		this.versionSeriesId = versionSeriesId;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public boolean getIsEmpty() {
		return isEmpty;
	}

	public void setIsEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}
	/**
	 * 创建时间
	 * @return
	 */
	public Long getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Long dateCreated) {
		this.dateCreated = dateCreated;
	}
	/**
	 * 是否被收藏
	 * @return
	 */
	public boolean getIsFavorite() {
		return isFavorite;
	}

	public void setIsFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	/**
	 * 获取 #{bare_field_comment} 
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * 设置 #{bare_field_comment}
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * 获取 #{bare_field_comment} 
	 * @return the rank
	 */
	public Double getRank() {
		return rank;
	}

	/**
	 * 设置 #{bare_field_comment}
	 * @param rank the rank to set
	 */
	public void setRank(Double rank) {
		this.rank = rank;
	}

	/**
	 * 获取 #{bare_field_comment} 
	 * @return the publisher
	 */
	public String getPublisher() {
		return publisher;
	}

	/**
	 * 设置 #{bare_field_comment}
	 * @param publisher the publisher to set
	 */
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public ThumbnailDTO getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(ThumbnailDTO thumbnail) {
		this.thumbnail = thumbnail;
	}

}
