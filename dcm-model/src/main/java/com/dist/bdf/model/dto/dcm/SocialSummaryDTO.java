package com.dist.bdf.model.dto.dcm;

import java.io.Serializable;

public class SocialSummaryDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 文档id
	 */
	private String docId;
	/**
	 * 下载数
	 */
	private int downloadCount;
	/**
	 * 收藏数
	 */
	private int favorites;
	/**
	 * 点赞数
	 */
	private int upvoteCount;
	
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public int getDownloadCount() {
		return downloadCount;
	}
	public void setDownloadCount(int downloadCount) {
		this.downloadCount = downloadCount;
	}
	public int getFavorites() {
		return favorites;
	}
	public void setFavorites(int favorites) {
		this.favorites = favorites;
	}
	public int getUpvoteCount() {
		return upvoteCount;
	}
	public void setUpvoteCount(int upvoteCount) {
		this.upvoteCount = upvoteCount;
	}
}
