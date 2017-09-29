package com.dist.bdf.model.dto.dcm;

import java.io.Serializable;

public class OfficeFileInfoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String BaseFileName;
	private String OwnerId;
	private String Size;
	private String AllowExternalMarketplace;
	private String Version;
	
	public String getBaseFileName() {
		return BaseFileName;
	}
	public void setBaseFileName(String baseFileName) {
		BaseFileName = baseFileName;
	}
	public String getOwnerId() {
		return OwnerId;
	}
	public void setOwnerId(String ownerId) {
		OwnerId = ownerId;
	}
	public String getSize() {
		return Size;
	}
	public void setSize(String size) {
		Size = size;
	}
	public String getAllowExternalMarketplace() {
		return AllowExternalMarketplace;
	}
	public void setAllowExternalMarketplace(String allowExternalMarketplace) {
		AllowExternalMarketplace = allowExternalMarketplace;
	}
	public String getVersion() {
		return Version;
	}
	public void setVersion(String version) {
		Version = version;
	}
}
