package com.dist.bdf.facade.service;

import java.util.List;
import java.util.Map;

import com.dist.bdf.model.dto.system.DownloadParaDTO;
import com.dist.bdf.model.dto.system.SocialWzParaDTO;
import com.dist.bdf.model.dto.system.SocialWzSimpleResultDTO;
import com.dist.bdf.model.entity.system.DcmSocialResource;

/**
 * 社交化服务
 * @author weifj
 * @version 1.0，2016/03/30，weifj，创建服务接口
 *
 */
public interface SocialService {

	/**
	 * 添加或更新微作社交化数据
	 * @param dto
	 * @return
	 */
	public void saveSocialData_WZ(SocialWzParaDTO dto);
	/**
	 * 删除微作的社交数据
	 * @param resId
	 */
	public void deleteSocialData_WZ(String resId);
	
	/**
	 * 根据微作id获取社交化数据
	 * @param wzId
	 * @return
	 */
	public List<DcmSocialResource> getWz(String wzId);
	
	/**
	 * 根据微作id和用户，查询社交情况
	 * @param dto
	 * @return
	 */
	public SocialWzSimpleResultDTO getSimpleWz(String wzId, String creator);
	/**
	 * 获取微作状态
	 * @param wzId
	 * @param creator
	 * @return
	 */
	public SocialWzSimpleResultDTO getSimpleWzStatus(String wzId);
	/**
	 * 根据案例标识获取包含的微作社交化数据
	 * @param caseIdentifier
	 * @return
	 */
	public List<DcmSocialResource> getWzOfCase(String caseIdentifier, String creator);
	/**
	 * 根据案例id获取map的微作，以微作的id作为key，实体作为value
	 * @param caseIdentifier
	 * @return
	 */
	public Map<String, SocialWzSimpleResultDTO> getMapWzOfCase(SocialWzParaDTO dto);
	/**
	 * 根据case的id获取map的微作，以微作的id作为key，实体作为value
	 * @param dto
	 * @return
	 */
	public Map<String, SocialWzSimpleResultDTO> getMapWzOfCaseById(SocialWzParaDTO dto);
	
	/**
	 * 下载资源记录
	 * @param downloadDTO
	 * @return
	 */
	boolean downloadRes(DownloadParaDTO downloadDTO);
	public SocialWzSimpleResultDTO getSimpleWzGZ(String guid, String creator);
	public void saveSocialData_WZ_GZ(SocialWzParaDTO dto);
	public Map<String, SocialWzSimpleResultDTO> getMapWzOfCaseGZ(SocialWzParaDTO dto);

}
