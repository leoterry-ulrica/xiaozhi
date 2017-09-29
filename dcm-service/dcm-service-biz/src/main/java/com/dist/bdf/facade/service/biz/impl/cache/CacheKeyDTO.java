package com.dist.bdf.facade.service.biz.impl.cache;

import java.io.Serializable;

public class CacheKeyDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long startTime;
	private String dataKey;
	
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public String getDataKey() {
		return dataKey;
	}
	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}
}
