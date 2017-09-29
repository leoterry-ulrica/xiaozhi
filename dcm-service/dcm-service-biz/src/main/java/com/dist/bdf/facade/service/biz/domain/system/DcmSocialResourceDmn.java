
package com.dist.bdf.facade.service.biz.domain.system;

import java.util.List;
import java.util.Map;

import com.dist.bdf.base.domain.GenericDmn;
import com.dist.bdf.model.dto.system.SocialResourceDTO;
import com.dist.bdf.model.entity.system.DcmSocialResource;

/**
 *  
 * @author weifj
 * @version 1.0，2016/03/30，创建
 */
public interface DcmSocialResourceDmn extends GenericDmn<DcmSocialResource, Long> {

	/**
	 * 根据资源id获取相关的所有人的点赞、收藏和标记数据
	 * @param resId
	 * @return
	 */
	public List<DcmSocialResource> getByResId(String resId);
	/**
	 * 根据资源id和创建者获取
	 * @param resId
	 * @param creator
	 * @return
	 */
	public DcmSocialResource getByResIdAndCreator(String resId, String creator);
	/**
	 * 
	 * @param resId
	 * @param creator
	 * @return
	 */
	public List<DcmSocialResource> getByResIdAndCreator(String[] resId, String creator);
	/**
	 * 根据案例标识和创建者获取微作社交化信息
	 * @param caseIdentifier
	 * @return
	 */
	public List<DcmSocialResource> getWzByCaseIdentifierAndCreator(String caseIdentifier, String creator);
	/**
	 * 根据案例id和创建者获取微作社交化信息
	 * @param caseIdentifier
	 * @param creator
	 * @return
	 */
	List<DcmSocialResource> getWzByCaseIdAndCreator(String caseIdentifier, String creator);
	/**
	 * 根据案例id获取微作
	 * @param caseId
	 * @return
	 */
	List<DcmSocialResource> getWzByCaseId(String caseId);
	/**
	 * 根据案例标识和创建者获取被赞微作
	 * @param caseIdentifier
	 * @param creator
	 * @return
	 */
	public List<DcmSocialResource> getWzLikedByCaseIdentifierAndCreator(String caseIdentifier, String creator);
	/**
	 * 获取微作的map数据，以资源id作为key，实体作为value
	 * @param caseIdentifier
	 * @return
	 */
	public Map<String, DcmSocialResource> getMapWzByCaseIdentifier(String caseIdentifier, String creator);
	/**
	 * 提供保存各种资源的社交化数据。
	 * 没有，则新增；否则更新
	 * @param dto
	 * @param resTypeName
	 */
	public void savaSocialData(SocialResourceDTO dto, String resTypeName);
	/**
	 * 根据资源id删除社交数据
	 * @param redId
	 */
	public void deleteByResId(String redId);
	/**
	 * 根据父类资源id，删除社交数据
	 * @param parentResId
	 */
	public void deleteByParentResId(String parentResId);
	/**
	 * 判断是否已被收藏
	 * @param sr
	 * @return
	 */
	boolean isFavorite(DcmSocialResource sr);
	/**
	 * 
	 * @param caseId
	 * @param creator
	 * @return
	 */
	public List<DcmSocialResource> getWzByCaseId(String caseId, String creator);
	public void savaSocialDataGZ(SocialResourceDTO srDTO, String resWz);
}
