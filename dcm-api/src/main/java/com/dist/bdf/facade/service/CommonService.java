package com.dist.bdf.facade.service;

public interface CommonService {

	/**
	 * 递归删除个人资料
	 * @param parentId
	 */
	void deleteSubMaterialRecursion(String parentId);
	/**
	 * 清理文档资源
	 * @param resId
	 * @return
	 */
	Object clearResource(String resId);
	
	/**
	 * 判断是否有下载权限
	 * @param user
	 * @param resId
	 * @return
	 */
	//boolean isHaveDownloadPriv(String user, String resId);
	/**
	 * 判断是否有下载权限
	 * @param user
	 * @param resId
	 * @param domainCode
	 * @return
	 */
	//boolean isHaveDownloadPriv(String user, String resId, String domainCode);

	/**
	 * 判断是否存在共享
	 * @param resId
	 * @return
	 */
	boolean isShare(String resId);
	
	/**
	 * 判断是否被用户收藏
	 * @param userId
	 * @param resId
	 * @return
	 */
	boolean isFavorite(String userId, String resId);
	/**
	 * 获取所有资源类型
	 * @return
	 */
	Object getResourceTypes();


}
