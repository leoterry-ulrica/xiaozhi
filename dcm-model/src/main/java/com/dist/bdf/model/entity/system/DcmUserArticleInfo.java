package com.dist.bdf.model.entity.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dist.bdf.base.entity.BaseEntity;

@Entity
@Table(name = "DCM_USER_ARTICLEINFO")
public class DcmUserArticleInfo extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private Date publishTime;
	private String summary;
	private Long userId;
	private String journal;
	private String author;
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "USERID")
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "PUBLISHTIME")
	public Date getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}
	@Column(name = "SUMMARY")
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getJournal() {
		return journal;
	}
	public void setJournal(String journal) {
		this.journal = journal;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DcmUserArticleInfo [name=" + name + ", publishTime=" + publishTime + ", summary=" + summary
				+ ", userId=" + userId + ", journal=" + journal + ", author=" + author + "]";
	}

}
