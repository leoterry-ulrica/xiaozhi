
package com.dist.bdf.model.dto.system;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author weifj
 * @version 1.0，2016/03/12，weifj
 *    1. 创建共享DTO
 *
 */
public class ShareParaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 资源id
	 */
	private String id;
	/**
	 * 用户编码，如：6A28E940-84A7-46EF-B019-2676A23D7DD2
	 */
	private String user;
	/**
	 * 分享的源类型，1（个人）、2（项目）、3（所）、4（院）
	 */
	private int s_type;
	/**
	 * 分享的资源的空间域编码
	 */
	private String s_dc;
	/**
	 * 分享的目标类型，1（个人）、2（项目）、3（所）、4（院）
	 */
	private int t_type;
	/**
	 * 分享的目标空间域和操作
	 */
	private DomainOpDTO[] t_dcs;
	private String t_dc;

	/**
	 * 是否文件夹。1：文件夹；0：文档
	 */
	private Long isFolder = 0L;
	
	/**
	 * 事项共享，一般共享：0；事项共享：1
	 * 默认为：0
	 */
	private Long shareType = 0L;
	/**
	 * 客户端传过来的资源类型
	 */
	private int type;
	/**
	 * 有效期
	 */
	private String expiryTimeStr;
	/*
	 * 有效期，时间戳
	 */
	private long expiryTime;
	/**
	 * 域
	 */
	private String realm;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getS_type() {
		return s_type;
	}

	public void setS_type(int s_type) {
		this.s_type = s_type;
	}

	public String getS_dc() {
		return s_dc;
	}

	public void setS_dc(String s_dc) {
		this.s_dc = s_dc;
	}

	public int getT_type() {
		return t_type;
	}

	public void setT_type(int t_type) {
		this.t_type = t_type;
	}

	public DomainOpDTO[] getT_dcs() {
		return t_dcs;
	}

	public void setT_dcs(DomainOpDTO[] t_dcs) {
		this.t_dcs = t_dcs;
	}

	public String getExpiryTimeStr() {
		return expiryTimeStr;
	}

	public void setExpiryTimeStr(String expiryTimeStr) {
		this.expiryTimeStr = expiryTimeStr;
	}

	public String getT_dc() {
		return t_dc;
	}

	public void setT_dc(String t_dc) {
		this.t_dc = t_dc;
	}

	public long getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(long expiryTime) {
		this.expiryTime = expiryTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Long getShareType() {
		return shareType;
	}

	public void setShareType(Long shareType) {
		this.shareType = shareType;
	}

	public Long getIsFolder() {
		return isFolder;
	}

	public void setIsFolder(Long isFolder) {
		this.isFolder = isFolder;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	@Override
	public String toString() {
		return "ShareParaDTO [id=" + id + ", user=" + user + ", s_type=" + s_type + ", s_dc=" + s_dc + ", t_type="
				+ t_type + ", t_dcs=" + Arrays.toString(t_dcs) + ", t_dc=" + t_dc + ", isFolder=" + isFolder
				+ ", shareType=" + shareType + ", type=" + type + ", expiryTimeStr=" + expiryTimeStr + ", expiryTime="
				+ expiryTime + ", realm=" + realm + "]";
	}

}
