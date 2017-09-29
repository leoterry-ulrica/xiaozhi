package com.dist.bdf.model.dto.system;

import java.io.Serializable;

public class IndexInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int countOfWz;
	private int countOfLike;
	private int countOfComment;
	private int countOfDoc;
	
	public IndexInfo(int countOfWz, int countOfLike, int countOfComment, int countOfDoc){
		
		this.countOfWz = countOfWz;
		this.countOfLike = countOfLike;
		this.countOfComment = countOfComment;
		this.countOfDoc = countOfDoc;
		
	}
	public int getCountOfWz() {
		return countOfWz;
	}
	public void setCountOfWz(int countOfWz) {
		this.countOfWz = countOfWz;
	}
	public int getCountOfLike() {
		return countOfLike;
	}
	public void setCountOfLike(int countOfLike) {
		this.countOfLike = countOfLike;
	}
	public int getCountOfComment() {
		return countOfComment;
	}
	public void setCountOfComment(int countOfComment) {
		this.countOfComment = countOfComment;
	}
	public int getCountOfDoc() {
		return countOfDoc;
	}
	public void setCountOfDoc(int countOfDoc) {
		this.countOfDoc = countOfDoc;
	}
}
