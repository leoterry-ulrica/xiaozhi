package com.dist.bdf.model.dto.system.pm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dist.bdf.model.dto.system.ShareObjectSimpleDTO;

/**
 * 个人资料信息DTO
 * 
 * @author weifj
 * @version 1.0，2016/04/28，weifj，创建
 */
public class PersonalMaterialInfoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, InfoAttribute> folders = new HashMap<String, InfoAttribute>();
	private Map<String, InfoAttribute> files = new HashMap<String, InfoAttribute>();

	public void addFolder(String folderId, boolean isShare, boolean isFavorite) {

		InfoAttribute att = new InfoAttribute();
		att.setIsShare(isShare);
		att.setIsFavorite(isFavorite);

		this.folders.put(folderId, att);
	}

	public void addFolder(String folderId, boolean isShare, boolean isFavorite, List<String> shareTargetDomainCodes) {

		InfoAttribute att = new InfoAttribute();
		att.setIsShare(isShare);
		att.setIsFavorite(isFavorite);
		att.setT_dcs(shareTargetDomainCodes);

		this.folders.put(folderId, att);
	}
	
	public void addFolderEx(String folderId, boolean isShare, boolean isFavorite, List<ShareObjectSimpleDTO> shareTargetDomainCodes) {

		InfoAttribute att = new InfoAttribute();
		att.setIsShare(isShare);
		att.setIsFavorite(isFavorite);
		// att.setT_dcs(shareTargetDomainCodes);
		att.setT_dos(shareTargetDomainCodes);

		this.folders.put(folderId, att);
	}
	

	public void addFile(String fileId, boolean isShare, boolean isFavorite) {

		InfoAttribute att = new InfoAttribute();
		att.setIsShare(isShare);
		att.setIsFavorite(isFavorite);

		this.files.put(fileId, att);
	}

	/**
	 * 
	 * @param fileId
	 * @param isShare
	 * @param isFavorite
	 * @param shareTargetDomainCodes
	 *            共享的目标空间域编码集
	 */
	public void addFile(String fileId, boolean isShare, boolean isFavorite, List<String> shareTargetDomainCodes) {

		InfoAttribute att = new InfoAttribute();
		att.setIsShare(isShare);
		att.setIsFavorite(isFavorite);
		att.setT_dcs(shareTargetDomainCodes);

		this.files.put(fileId, att);
	}

	public void addFileEx(String fileId, boolean isShare, boolean isFavorite,
			List<ShareObjectSimpleDTO> shareTargetDomainPrivcodes) {

		InfoAttribute att = new InfoAttribute();
		att.setIsShare(isShare);
		att.setIsFavorite(isFavorite);
		att.setT_dos(shareTargetDomainPrivcodes);

		this.files.put(fileId, att);
	}

	public Map<String, InfoAttribute> getFolders() {
		return folders;
	}

	public void setFolders(Map<String, InfoAttribute> folders) {
		this.folders = folders;
	}

	public Map<String, InfoAttribute> getFiles() {
		return files;
	}

	public void setFiles(Map<String, InfoAttribute> files) {
		this.files = files;
	}

}

class InfoAttribute implements Serializable {

	private boolean isShare;
	private boolean isFavorite;
	private List<String> t_dcs;
	private List<ShareObjectSimpleDTO> t_dos = new ArrayList<ShareObjectSimpleDTO>();

	/**
	 * 是否被共享
	 * 
	 * @return
	 */
	public boolean getIsShare() {
		return isShare;
	}

	public void setIsShare(boolean isShare) {
		this.isShare = isShare;
	}

	/**
	 * 是否被收藏
	 * 
	 * @return
	 */
	public boolean getIsFavorite() {
		return isFavorite;
	}

	public void setIsFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	public List<String> getT_dcs() {
		return t_dcs;
	}

	public void setT_dcs(List<String> t_dcs) {
		this.t_dcs = t_dcs;
	}

	public List<ShareObjectSimpleDTO> getT_dos() {
		return t_dos;
	}

	public void setT_dos(List<ShareObjectSimpleDTO> t_dos) {
		this.t_dos = t_dos;
	}

}