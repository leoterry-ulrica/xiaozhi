package com.dist.bdf.model.dto.system;


/**
 * 微作社交化数据dto
 */
public class SocialWzParaDTO extends SocialResourceDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields
	private String caseIdentifier;
	// case的id
	private String caseId;
	/**
	 * 域
	 */
	private String realm;
	/**
	 * 案例标识
	 * @return
	 */
	public String getCaseIdentifier() {
		return caseIdentifier;
	}
	public void setCaseIdentifier(String caseIdentifier) {
		this.caseIdentifier = caseIdentifier;
	}

	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	/**
	 * 获取 #{bare_field_comment} 
	 * @return the realm
	 */
	public String getRealm() {
		return realm;
	}
	/**
	 * 设置 #{bare_field_comment}
	 * @param realm the realm to set
	 */
	public void setRealm(String realm) {
		this.realm = realm;
	}

}