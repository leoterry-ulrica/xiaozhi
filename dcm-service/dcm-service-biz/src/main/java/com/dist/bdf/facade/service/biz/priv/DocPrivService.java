package com.dist.bdf.facade.service.biz.priv;

import com.dist.bdf.model.entity.system.DcmUser;

public interface DocPrivService {

	/**
	 * 获取文档的权限mask值
	 * @param user
	 * @param versionSeriesId 版本系列id
	 * @param domainCode
	 * @return
	 */
	@Deprecated
	 Long getMasks(DcmUser user, String versionSeriesId, String domainCode);
	 /**
	  * 
	  * @param user
	  * @param versionSeriesId
	  * @param domainCode
	  * @param parentId 父文件夹id
	  * @return
	  */
	@Deprecated
	 Long getMasks(DcmUser user, String versionSeriesId, String domainCode, String parentId);
	 /**
	  * 
	  * @param channelType
	  * @param user
	  * @param versionSeriesId
	  * @param domainCode
	  * @param parentId
	  * @return
	  */
	 Long getMasks(Long channelType, DcmUser user, String versionSeriesId, String domainCode, String parentId);
	 /**
	  * 
	  * @param channelType
	  * @param user
	  * @param versionSeriesId
	  * @param domainCode
	  * @param parentId
	  * @return
	  */
	 Long getMasksByUserCode(Long channelType, DcmUser user, String versionSeriesId, String domainCode, String parentId);
	 /**
	  * 获取权限编码集合
	  * @param realm 域
	  * @param channelType
	  * @param user
	  * @param versionSeriesId
	  * @param domainCode
	  * @param parentId
	  * @return
	  */
	 Object getCodes(String realm, Long channelType, DcmUser user, String versionSeriesId, String domainCode, String parentId);
	 
}
