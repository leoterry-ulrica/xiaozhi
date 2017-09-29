package com.dist.bdf.base.utils.ftp;

import java.io.Serializable;
import java.util.List;

/**
 * ftp目录
 * @author weifj
 *
 */
public class FTPDir implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String label;
	private String type;
	private String fullPath;
	private String extension;
	private List<FTPDir> children;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFullPath() {
		return fullPath;
	}
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	public List<FTPDir> getChildren() {
		return children;
	}
	public void setChildren(List<FTPDir> children) {
		this.children = children;
	}
}
