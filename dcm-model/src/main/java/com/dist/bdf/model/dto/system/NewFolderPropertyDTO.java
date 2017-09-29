package com.dist.bdf.model.dto.system;

import java.io.Serializable;
/**
 * 新文件夹属性DTO
 * @author Administrator
 *
 */
public class NewFolderPropertyDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String realm;
	private String user;
	private String pwd;
	private int type;
	private String parentFolderId;
	private String folderName;
	
	public NewFolderPropertyDTO(){
		
	}
	public NewFolderPropertyDTO(String user,
	 String pwd,
	 int type,
	 String parentFolderId,
	 String folderName){
		
		this.user = user;
		this.pwd = pwd;
		this.type = type;
		this.parentFolderId = parentFolderId;
		this.folderName = folderName;
	}
	
	public NewFolderPropertyDTO(String realm, String user, String pwd, int type, String parentFolderId,
			String folderName) {

		this.realm = realm;
		this.user = user;
		this.pwd = pwd;
		this.type = type;
		this.parentFolderId = parentFolderId;
		this.folderName = folderName;
	}

	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getParentFolderId() {
		return parentFolderId;
	}
	public void setParentFolderId(String parentFolderId) {
		this.parentFolderId = parentFolderId;
	}
	public String getFolderName() {
		return folderName;
	}
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
}
