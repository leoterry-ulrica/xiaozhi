package com.dist.bdf.model.dto.system;

import java.io.Serializable;
import java.util.List;

import com.dist.bdf.model.dto.dcm.DocumentDTO;

/**
 * 个人文件包
 * @author weifj
 *
 */
public class PersonalPcksResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String rootFolderId;
	private List<DocumentDTO> docs;
	
	public String getRootFolderId() {
		return rootFolderId;
	}
	public void setRootFolderId(String rootFolderId) {
		this.rootFolderId = rootFolderId;
	}
	public List<DocumentDTO> getDocs() {
		return docs;
	}
	public void setDocs(List<DocumentDTO> docs) {
		this.docs = docs;
	}
}
