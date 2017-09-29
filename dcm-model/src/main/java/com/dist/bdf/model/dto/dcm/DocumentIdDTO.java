package com.dist.bdf.model.dto.dcm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DocumentIdDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 版本系列id集合
	 */
	private List<String> vids = new ArrayList<String>();
	/**
	 * 标识集合
	 */
	private List<String> ids = new ArrayList<String>();
	
	public List<String> getVids() {
		return vids;
	}
	public void setVids(List<String> vids) {
		this.vids = vids;
	}
	public List<String> getIds() {
		return ids;
	}
	public void setIds(List<String> ids) {
		this.ids = ids;
	}
}
