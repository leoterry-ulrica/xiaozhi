/**
 * 
 */
package com.dist.bdf.base.dto;

/**
 * 简单分页Dto
 * 如果需要通过一个属性进行排序，返回分页，则使用此Dto
 * @author 李其云
 * @version 1.0 2015-8-26
 */
public class SimplePageDTO extends SimpleListDTO {

    /**
     * 分页大小
     */
    protected int pageSize;
    /**
     * 当前分页号
     */
    protected int pageNo;
	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}
	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * @return the pageNo
	 */
	public int getPageNo() {
		return pageNo;
	}
	/**
	 * @param pageNo the pageNo to set
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
    
}
