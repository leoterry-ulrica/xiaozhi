package com.dist.bdf.base.page;

/**
 * 封装分页的参数
 * 
 * @author shenyuting
 * 
 */
public class PageParams {
	// 页码
	private Integer page = 1;
	// 每页的数量
	private Integer pageSize = 10;
	// 排序的字段
	private String sort;
	// 正 倒序
	private boolean isAsc;

	public PageParams() {
		super();
	}

	/**
	 * 构造分页参数的方法，仅包含分页和当前页的数量
	 * 
	 * @param page
	 *            当前页码
	 * @param rows
	 *            当前页的数量
	 */
	public PageParams(Integer page, Integer pageSize) {
		super();
		this.page = page;
		this.pageSize = pageSize;
	}

	/**
	 * 构造分页参数的方法，仅包含排序的字段和顺序
	 * 
	 * @param sort
	 *            需要排序的字段名
	 * @param order
	 *            排序的方式：asc desc
	 */
	public PageParams(String sort, boolean isAsc) {
		super();
		this.sort = sort;
		this.isAsc = isAsc;
	}

	/**
	 * 构造分页参数的方法，支持分页并且排序
	 * 
	 * @param page
	 *            当前页码
	 * @param rows
	 *            当前页的数量
	 * @param sort
	 *            需要排序的字段名
	 * @param order
	 *            排序的方式：asc desc
	 */
	public PageParams(Integer page, Integer pagesize, String sort, boolean isAsc) {
		super();
		this.page = page;
		this.pageSize = pagesize;
		this.sort = sort;
		this.isAsc = isAsc;
	}

	/**
	 * 获取页码，如 第1 2 3 页
	 * 
	 * @return
	 */
	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	/**
	 * 获取当前行的数量，如一页有20条数据
	 * 
	 * @return
	 */

	/**
	 * 根据哪个字段进行排序，如id name
	 * 
	 * @return
	 */
	public String getSort() {
		return sort;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPagesize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public boolean getIsAsc() {
		return isAsc;
	}

	public void setIsAsc(boolean isAsc) {
		this.isAsc = isAsc;
	}

}
