package com.dist.bdf.model.dto.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.dist.bdf.model.dto.dcm.DocumentDTO;

/**
 * 共享的信息
 * @author weifj
 * @version 1.0，2016/05/04，weifj，创建
 */
public class ShareInfoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private Long status;
	private String user;
	private int s_type;
	private String s_dc;
	private int t_type;
	private List<String> t_dcs = new ArrayList<String>();
	
	private List<ShareObjectSimpleDTO> t_dos = new ArrayList<ShareObjectSimpleDTO>();
	
	private Long isFolder;

	//private String shareTimeStr;
	/**
	 * 有效期
	 */
	//private String expiryTimeStr;
	
	/**
	 * 共享资源id
	 * @return
	 */
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}


	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
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
	/*public String getT_dc() {
		return t_dc;
	}
	public void setT_dc(String t_dc) {
		this.t_dc = t_dc;
	}
	public String getShareTimeStr() {
		return shareTimeStr;
	}
	public void setShareTimeStr(String shareTimeStr) {
		this.shareTimeStr = shareTimeStr;
	}*/
/*	public String getExpiryTimeStr() {
		return expiryTimeStr;
	}
	public void setExpiryTimeStr(String expiryTimeStr) {
		this.expiryTimeStr = expiryTimeStr;
	}*/
	public List<String> getT_dcs() {
		return t_dcs;
	}
	public void setT_dcs(List<String> t_dcs) {
		this.t_dcs = t_dcs;
	}
	public Long getIsFolder() {
		return isFolder;
	}
	public void setIsFolder(Long isFolder) {
		this.isFolder = isFolder;
	}
	public List<ShareObjectSimpleDTO> getT_dos() {
		return t_dos;
	}
	public void setT_dos(List<ShareObjectSimpleDTO> t_dos) {
		this.t_dos = t_dos;
	}

}
