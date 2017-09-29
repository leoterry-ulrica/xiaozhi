package com.dist.bdf.model.dto.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 新创建微作返回的数据模型
 * @author weifj
 * @version 1.0，2016/05/19，weifj，创建
 */
public class WzNewResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 微作id
	 */
	private String wzId;
	/**
	 * 文档id列表
	 */
	private List<String> docIds = new ArrayList<String>();
	
	public String getWzId() {
		return wzId;
	}
	public void setWzId(String wzId) {
		this.wzId = wzId;
	}
	public List<String> getDocIds() {
		return docIds;
	}
	public void setDocIds(List<String> docIds) {
		this.docIds = docIds;
	}
	
	public void addDocId(String id){
		this.docIds.add(id);
	}
}
