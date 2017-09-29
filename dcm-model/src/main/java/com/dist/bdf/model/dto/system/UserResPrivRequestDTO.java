package com.dist.bdf.model.dto.system;

import java.io.Serializable;

/**
 * 用户获取资源权限编码模型
 * @author weifj
 *
 */
public class UserResPrivRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String realm;
	private String user;
	private String resId;
	/**
	 * 父文件夹id，如果文件夹被共享，那可以快速验证资源resId是否被共享
	 * 如果channelType = 1，则parentId就是共享文件夹的id
	 */
	private String parentId;
	/**
	 * 通道类型，0：默认通道；1：共享文件夹通道
	 */
	/*@Deprecated
	private Long channelType;*/
	/**
	 * 判断文件的入口
	 */
	private Long from;
	/**
	 * 用户类型
	 */
	private Integer userType = 0;
	/**
	 * 资源类型
	 */
	private String resType;
	
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getResId() {
		return resId;
	}
	public void setResId(String resId) {
		this.resId = resId;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
/*	public Long getChannelType() {
		return channelType;
	}
	public void setChannelType(Long channelType) {
		this.channelType = channelType;
	}*/
	public Long getFrom() {
		return from;
	}
	public void setFrom(Long from) {
		this.from = from;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	/**
	 * 获取 #{bare_field_comment} 
	 * @return the resType
	 */
	public String getResType() {
		return resType;
	}
	/**
	 * 设置 #{bare_field_comment}
	 * @param resType the resType to set
	 */
	public void setResType(String resType) {
		this.resType = resType;
	}

}
