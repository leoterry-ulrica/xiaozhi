package com.dist.bdf.facade.service;

import com.dist.bdf.base.page.Pagination;

/**
 * 咨询服务
 * @author weifj
 *
 */
public interface InformationService {

	/**
	 *  分页获取所有的资讯资料
	 * @param pageNo
	 * @param pageSize
	 * @param keyword 如果不做关键字过滤，则传入空""
	 * @return 分页实体数据为：List<DocumentDTO>
	 */
	Pagination searchInformations(int pageNo, int pageSize, String keyword);
	/**
	 * 分页获取资料类型的资讯
	 * @param pageNo
	 * @param pageSize
	 * @param keyword 如果不做关键字过滤，则传入空""
	 * @return
	 */
	Pagination searchInformationOfMaterial(int pageNo, int pageSize, String keyword);
	/**
	 * 分页获取新闻类型的资讯
	 * @param pageNo
	 * @param pageSize
	 * @param keyword 如果不做关键字过滤，则传入空""
	 * @return
	 */
	Pagination searchInformationOfNews(int pageNo, int pageSize, String keyword);
	/**
	 * 分页检索咨询，缩略图返回字节流
	 * @param pageNo
	 * @param pageSize
	 * @param keyword
	 * @return
	 */
	Pagination searchInformationsThumbnailByte(int pageNo, int pageSize, String keyword);
	/**
	 * 分页检索资料类型的咨询，缩略图返回字节流
	 * @param pageNo
	 * @param pageSize
	 * @param keyword
	 * @return
	 */
	Pagination searchInformationOfMaterialThumbnailByte(int pageNo, int pageSize, String keyword);
	/**
	 * 分页检索新闻类型的咨询，缩略图返回字节流
	 * @param pageNo
	 * @param pageSize
	 * @param keyword
	 * @return
	 */
	Pagination searchInformationOfNewsThumbnailByte(int pageNo, int pageSize, String keyword);
}
