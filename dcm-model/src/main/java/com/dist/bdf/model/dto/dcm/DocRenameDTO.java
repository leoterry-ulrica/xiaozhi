package com.dist.bdf.model.dto.dcm;

import java.io.Serializable;
/**
 * 文件重命名参数
 * @author weifj
 *
 */
public class DocRenameDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String docId;
	private String newName;
	private String realm;
	
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
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	@Override
	public String toString() {
		return "DocRenameDTO [docId=" + docId + ", newName=" + newName + ", realm=" + realm + "]";
	}

}
