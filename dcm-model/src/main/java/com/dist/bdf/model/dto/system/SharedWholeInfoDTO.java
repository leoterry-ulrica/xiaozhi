package com.dist.bdf.model.dto.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.dist.bdf.model.dto.dcm.DocumentDTO;
import com.dist.bdf.model.dto.system.user.UserSimpleDTO;

/**
 * 共享的完整信息，用于不通过icn的入口获取共享信息
 * @author weifj
 * @version 1.0，2016/05/04，weifj，创建
 */
public class SharedWholeInfoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	//private Long status;
	/**
	 * 共享者
	 */
	private UserSimpleDTO sharer;
	private int s_type;
	// private String s_dc;
	private int t_type;
	//private List<String> t_dcs = new ArrayList<String>();
	private Long isFolder;
	/**
	 * 共享资源
	 */
	private DocumentDTO shareRes;
	/**
	 * 目标空间域实体
	 */
	private Object targetDomainEntity;
	/**
	 * 源空间域实体
	 */
	private Object sourceDomainEntity;
	
	/**
	 * 是否有权下载
	 */
	private boolean havePriv;
	/**
	 * 是否被收藏
	 */
	private boolean isFavorite;
	/**
	 * 
	 */
	private long sharedTime;

	//private String shareTimeStr;
	/**
	 * 有效期
	 */
	//private String expiryTimeStr;
	
	/**
	 * 共享资源id
	 * @return
	 */
	/*public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
*/

	/*public Long getStatus() {
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
	}*/
	public int getS_type() {
		return s_type;
	}
	public void setS_type(int s_type) {
		this.s_type = s_type;
	}
/*	public String getS_dc() {
		return s_dc;
	}
	public void setS_dc(String s_dc) {
		this.s_dc = s_dc;
	}*/
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
/*	public List<String> getT_dcs() {
		return t_dcs;
	}
	public void setT_dcs(List<String> t_dcs) {
		this.t_dcs = t_dcs;
	}*/
	public Long getIsFolder() {
		return isFolder;
	}
	public void setIsFolder(Long isFolder) {
		this.isFolder = isFolder;
	}
	/*public DocumentDTO getDoc() {
		return doc;
	}
	public void setDoc(DocumentDTO doc) {
		this.doc = doc;
	}
	public List<Object> getT_entities() {
		return t_entities;
	}
	public void setT_entities(List<Object> t_entities) {
		this.t_entities = t_entities;
	}
	public Object getS_entity() {
		return s_entity;
	}
	public void setS_entity(Object s_entity) {
		this.s_entity = s_entity;
	}*/
	/*public List<Object> getTargetDomainEntities() {
		return targetDomainEntities;
	}
	public void setTargetDomainEntities(List<Object> targetDomainEntities) {
		this.targetDomainEntities = targetDomainEntities;
	}*/
	public Object getSourceDomainEntity() {
		return sourceDomainEntity;
	}
	public void setSourceDomainEntity(Object sourceDomainEntity) {
		this.sourceDomainEntity = sourceDomainEntity;
	}
	public UserSimpleDTO getSharer() {
		return sharer;
	}
	public void setSharer(UserSimpleDTO sharer) {
		this.sharer = sharer;
	}
	public DocumentDTO getShareRes() {
		return shareRes;
	}
	public void setShareRes(DocumentDTO shareRes) {
		this.shareRes = shareRes;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean getHavePriv() {
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
	public Object getTargetDomainEntity() {
		return targetDomainEntity;
	}
	public void setTargetDomainEntity(Object targetDomainEntity) {
		this.targetDomainEntity = targetDomainEntity;
	}
}
