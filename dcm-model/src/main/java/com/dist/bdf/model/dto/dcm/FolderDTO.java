package com.dist.bdf.model.dto.dcm;


import java.io.Serializable;

public class FolderDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String guid;
	private String name;
	private String parentGuid;
	private String lastModifier;
	private long dateLastModified;
	private String owner;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentGuid() {
		return parentGuid;
	}
	public void setParentGuid(String parentGuid) {
		this.parentGuid = parentGuid;
	}
	public String getLastModifier() {
		return lastModifier;
	}
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	public long getDateLastModified() {
		return dateLastModified;
	}
	public void setDateLastModified(long dateLastModified) {
		this.dateLastModified = dateLastModified;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}

}
