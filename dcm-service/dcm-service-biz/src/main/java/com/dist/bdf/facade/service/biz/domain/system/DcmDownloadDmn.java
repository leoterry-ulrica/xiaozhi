
package com.dist.bdf.facade.service.biz.domain.system;

import com.dist.bdf.base.domain.GenericDmn;
import com.dist.bdf.model.entity.system.DcmDownload;

/**
 * @author weifj
 * @version 1.0，2016/01/27，创建组领域
 */
public interface DcmDownloadDmn extends GenericDmn<DcmDownload, Long> {
	
	/**
	 * 根据空间域删除下载记录
	 * @param domainCode
	 */
	void delDownloadLogByDomainCode(String domainCode);
	/**
	 * 根据资源id集合，下载者删除下载记录
	 * @param resIds
	 */
	void delDownloadLogByResIds(String downloader, String[] resIds);

}
