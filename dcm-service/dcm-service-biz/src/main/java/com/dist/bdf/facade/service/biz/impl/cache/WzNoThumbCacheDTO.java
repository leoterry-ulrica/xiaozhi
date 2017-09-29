package com.dist.bdf.facade.service.biz.impl.cache;

import java.io.Serializable;

import com.dist.bdf.base.page.Pagination;

public class WzNoThumbCacheDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long startTime;
	private Pagination page;
	
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Pagination getPage() {
		return page;
	}
	public void setPage(Pagination page) {
		this.page = page;
	}

}
