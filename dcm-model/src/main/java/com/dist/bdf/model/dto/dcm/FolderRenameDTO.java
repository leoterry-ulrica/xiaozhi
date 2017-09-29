package com.dist.bdf.model.dto.dcm;

import java.io.Serializable;
/**
 * 文件夹重命名参数
 * @author weifj
 *
 */
public class FolderRenameDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String folderId;
	private String newName;
	private String realm;
	
	public String getFolderId() {
		return folderId;
	}
	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}
	public String getNewName() {
		return newName;
	}
	public void setNewName(String newName) {
		this.newName = newName;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
	@Override
	public String toString() {
		return "FolderRenameDTO [folderId=" + folderId + ", newName=" + newName + ", realm=" + realm + "]";
	}

}
