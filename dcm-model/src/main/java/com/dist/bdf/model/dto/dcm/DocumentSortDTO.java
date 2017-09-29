package com.dist.bdf.model.dto.dcm;

import java.io.Serializable;

import com.dist.bdf.base.utils.StringUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * 第一层排序，根据关键字的匹配度
 * 第二层排序，名字匹配度优先，然后文档的下载数和收藏数之和进行排序，如果下载数和收藏数之和相等，则根据文档的最后修改时间
 * 
 * 实现接口Comparable，用于排序
 * @author weifj
 * @version 1.0 
 */
public class DocumentSortDTO implements Comparable<DocumentSortDTO>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DocumentDTO doc;
	private int downloadCount;
	private int favorites;
	private int upvoteCount;
	private String keyword;
	/**
	 * 来源，如果是项目资料，则是项目名称；如果是机构资料，则是机构名称；如果是个人，则是个人的名字
	 */
	private String source;
	
	public DocumentSortDTO() {
		
	}

	public DocumentSortDTO(DocumentDTO docDTO, String source, int downloadCount, int favorites, int upvoteCount, String keyword) {
		
		this.doc = docDTO;
		this.downloadCount = downloadCount;
		this.favorites = favorites;
		this.upvoteCount = upvoteCount;
		this.keyword = keyword;
		
	}
	@Override
	public int compareTo(DocumentSortDTO d) {
		
		int masterHit = StringUtil.hit(this.doc.getName(), this.keyword);
		int followerHit = StringUtil.hit(d.doc.getName(), this.keyword);
		
	    if(masterHit * followerHit > 0) {
	    	// && masterHit > followerHit)
	    	// 根据下载数和收藏数之和
	    	if(masterHit >= followerHit){
	    		return 1;
	    	}
		    return compareProperties(d);
	    }else {
	    	// masterHit * followerHit = 0的情况，说明至少有一个匹配度为0
	    	if(masterHit > followerHit){
	    		return 1;
	    	}
	    	return -1;
	    }
	}

	private int compareProperties(DocumentSortDTO d) {
		if(this.downloadCount + this.favorites > d.downloadCount + d.favorites) {
			return 1;
		}else if(this.downloadCount + this.favorites < d.downloadCount + d.favorites){
			return -1;
		}else{
			// 如果相等，则进一步使用创建时间进行排序
		    // 根据创建时间
		/*	if(this.doc.getDateCreated() > d.doc.getDateCreated()) {
				return 1;
			} else {
				return -1;
			}*/
		   return this.doc.getDateCreated().compareTo(d.doc.getDateCreated());
		}
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
	@JsonIgnore
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}

	public DocumentDTO getDoc() {
		return doc;
	}

	public void setDoc(DocumentDTO doc) {
		this.doc = doc;
	}

}
