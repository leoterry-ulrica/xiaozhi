
package com.dist.bdf.model.dto.system.page;

/**
 * 分页参数定义
 * @author weifj
 * 2015-06-12
 */
public class ParaJsonPage<T> extends ParaJsonPageBase {

	private T queryinfo;

	/**
	 * @return the queryinfo
	 */
	public T getQueryinfo() {
		return queryinfo;
	}

	/**
	 * @param queryinfo the queryinfo to set
	 */
	public void setQueryinfo(T queryinfo) {
		this.queryinfo = queryinfo;
	}

	
}
