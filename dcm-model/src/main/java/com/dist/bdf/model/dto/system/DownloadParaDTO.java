package com.dist.bdf.model.dto.system;

import java.io.Serializable;
/**
 * 下载参数DTO
 * @author weifj
 * @version 1.0，2016/05/03，weifj
 *
 */
public class DownloadParaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String user;
	private int type;
	private String domainCode;
	private String[] resIds;
	private String realm; // 机构域
	
	public String getDomainCode() {
		return domainCode;
	}
	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}
	/**
	 * 被下载的资源id
	 * @return
	 */
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 下载者
	 * @return
	 */
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	/**
	 * 客户端传过来的类型
	 * @return
	 */
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String[] getResIds() {
		return resIds;
	}
	public void setResIds(String[] resIds) {
		this.resIds = resIds;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
}
