
package com.dist.bdf.facade.service.biz.domain.system;

import java.util.List;

import com.dist.bdf.base.domain.GenericDmn;
import com.dist.bdf.model.dto.system.ShareObjectSimpleDTO;
import com.dist.bdf.model.entity.system.DcmShare;

/**
 *  
 * @author weifj
 * @version 1.0，2016/03/12，创建
 * @version 1.1，2016/03/12，weifj
 *    1. 添加addResourceShare，资源共享的方法
 */
public interface DcmShareDmn extends GenericDmn<DcmShare, Long> {

	/**
	 * 添加资源共享
	 * @param share
	 * @return
	 */
	public DcmShare addResourceShare(DcmShare share);
	/**
	 * 删除资源共享的一组权限
	 * @param resId 资源id
	 * @param domainCode 空间域编码
	 * @param privCodes 一组权限编码
	 */
	void delResourceShare(String resId, String domainCode, List<String> privCodes);
	void delResourceShare(String resId, String domainCode);
	/**
	 * 删除关于此资源的所有共享
	 * @param resId
	 */
	void delResourceShare(String resId);
	/**
	 * 根据资源id，目标空间域类型编码，删除共享信息
	 * @param resId
	 * @param targetDomainTypeCode
	 */
	void delShareByResAndTargetDomainType(String resId, String targetDomainTypeCode);
	/**
	 * 根据源空间域删除共享信息
	 * @param domainCode
	 */
	void delShareBySourceDomainCode(String domainCode);
	/**
	 * 根据目标空间域删除共享信息
	 * @param domainCode
	 */
	void delShareByTargetDomainCode(String domainCode);
	/**
	 * 根据资源id、目标空间域类型和目标空间编码，删除共享信息
	 * @param resId
	 * @param targetDomainTypeCode
	 * @param targetDomainCode
	 */
	void delResourceShare(String resId, String targetDomainTypeCode, String targetDomainCode);
	/**
	 * 根据资源id获取所有有效的共享信息
	 * @param resId
	 * @return
	 */
	Object getShareInfosByResId(String resId);
	/**
	 * 指定目标空间域类型，获取资源被共享出去的目标空间域集合
	 * @param resId
	 * @param targetDomainType
	 * @return
	 */
	List<String> getTargetDomainCodes(String resId, String targetDomainType);
	/**
	 * 根据资源id和目标空间域，获取唯一的共享信息
	 * @param resId
	 * @param targetDomainCode
	 * @return
	 */
	Object getUniqueShareInfoByResIdAndTarget(String resId, String targetDomainCode);
	Object getUniqueShareInfoByResIdAndTarget(String resId, String[] targetDomainCodes);
	/**
	 * 根据共享者名称获取共享信息，并以共享时间倒序
	 * @param userName
	 * @param pageNo 
	 * @param pageSize
	 * @param status 状态：0，1和2，其中2表示包括0和1
	 * @return
	 */
	Object getSharedInfoBySharer(String userName, int pageNo, int pageSize, long status);
	/**
	 * 根据共享者名称、状态和共享类型获取共享信息，并以共享时间倒序
	 * @param userName
	 * @param pageNo
	 * @param pageSize
	 * @param status
	 * @param shareType 共享类型
	 * @return
	 */
	Object getSharedInfoBySharer(String userName, int pageNo, int pageSize, long status, long shareType);
	/**
	 * 获取其它人共享给我的信息，并以共享时间倒序
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @param status 状态：0，1和2，其中2表示包括0和1
	 * @return
	 */
	Object getSharedInfoByOthers(String userId, int pageNo, int pageSize, long status);
	/**
	 * 
	 * @param resId
	 * @param targetDomainType
	 * @return
	 */
	List<ShareObjectSimpleDTO> getTargetDomainPrivCodes(String resId, String targetDomainType);
	/**
	 * 删除共享者所有相关资源
	 * @param userCode
	 */
	void delResourceBySharer(String userCode);
}
