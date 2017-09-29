
package com.dist.bdf.facade.service.biz.domain.dcm;

import com.filenet.api.core.ObjectStore;

/**
 * 
 * ce 文档操作领域层
 * @author weifj
 * @version 1.0，2016/02/17，weifj，创建ce文档操作领域
 * @version 1.1，2016/03/03，weifj，添加接口inheritDirectParentFolderSecurity
 */
public interface CEDocAccessDmn extends CEAccessDmn{

	/**
	 * 继承直接父文件夹
	 * @param directParentFolderId 直接父文件夹id
	 * @param targetDocId 目标文档id
	 */
	public void inheritDirectParentFolderSecurity(ObjectStore os,String directParentFolderId, String targetDocId);

}
