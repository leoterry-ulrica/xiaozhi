package com.dist.bdf.facade.service.biz.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.dist.bdf.base.utils.StringUtil;
import com.dist.bdf.model.dto.system.DownloadParaDTO;
import com.dist.bdf.model.dto.system.SocialResourceDTO;
import com.dist.bdf.model.dto.system.SocialWzParaDTO;
import com.dist.bdf.model.dto.system.SocialWzSimpleResultDTO;
import com.dist.bdf.model.entity.system.DcmDownload;
import com.dist.bdf.model.entity.system.DcmSocialResource;
import com.dist.bdf.facade.service.SocialService;
import com.dist.bdf.facade.service.biz.domain.system.DcmDownloadDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmSocialResourceDmn;
import com.dist.bdf.common.constants.ResourceConstants;

/**
 * 社交化服务实现
 * @author weifj
 * @version 1.0，,216/03/30，weifj，创建
 *
 */
@Service("SocialService")
@Transactional(propagation = Propagation.REQUIRED)
public class SocialServiceImpl implements SocialService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private DcmSocialResourceDmn socialResDmn;
	@Autowired
	private DcmDownloadDmn downloadDmn;
	
	@Override
	public void saveSocialData_WZ(SocialWzParaDTO dto) {
		
		// 微作的类型
		SocialResourceDTO srDTO = new SocialResourceDTO();
		srDTO.setCreator(dto.getCreator());
		srDTO.setGuid(dto.getGuid());
		srDTO.setIsFavorite(dto.getIsFavorite());
		srDTO.setIsTop(dto.getIsTop());
		srDTO.setParentResId(dto.getCaseIdentifier());
		srDTO.setTag(dto.getTag());
		this.socialResDmn.savaSocialData(srDTO, ResourceConstants.ResourceType.RES_WZ);
	}
	
	@Override
	public void saveSocialData_WZ_GZ(SocialWzParaDTO dto) {
		
		// 微作的类型
		SocialResourceDTO srDTO = new SocialResourceDTO();
		srDTO.setCreator(dto.getCreator());
		srDTO.setGuid(dto.getGuid());
		srDTO.setIsFavorite(dto.getIsFavorite());
		srDTO.setIsTop(dto.getIsTop());
		srDTO.setParentResId(dto.getCaseIdentifier());
		srDTO.setTag(dto.getTag());
		this.socialResDmn.savaSocialDataGZ(srDTO, ResourceConstants.ResourceType.RES_WZ);

	}

	@Override
	public void deleteSocialData_WZ(String resId) {
		this.socialResDmn.deleteByResId(resId);
	}
	@Override
	public List<DcmSocialResource> getWz(String caseId) {
		
		return this.socialResDmn.getByResId(caseId);
	}

	@Override
	public SocialWzSimpleResultDTO getSimpleWz(String wzId, String creator) {
		
		SocialWzSimpleResultDTO dto = new SocialWzSimpleResultDTO();
		// 跟用户无关，处理欠妥
		DcmSocialResource currentUser2sr = this.socialResDmn.findUniqueByProperties(new String[]{"resId","resTypeCode","creator"}, new Object[]{wzId, ResourceConstants.ResourceType.RES_WZ, creator});
		//this.socialResDmn.findUniqueByProperties(new String[]{"resId","resTypeCode","creator"}, new Object[]{wzId, ResourceConstants.ResourceType.Res_WZ,creator});
		DcmSocialResource common2sr = this.socialResDmn.findUniqueByProperties(new String[]{"resId","resTypeCode"}, new Object[]{wzId, ResourceConstants.ResourceType.RES_WZ});
		if(null == currentUser2sr) {
			dto.setIsFavorite(false);
			dto.setIsTop(false);
			dto.setTag(null == common2sr? "585d68" : common2sr.getTag());
			return dto;
		} 
		
		dto.setIsFavorite(0 == currentUser2sr.getIsFavorite()? false:true);
		dto.setIsTop(0 == currentUser2sr.getIsLike()? false:true);
		dto.setTag(currentUser2sr.getTag());
		
		return dto;
	}
	
	@Override
	public SocialWzSimpleResultDTO getSimpleWzGZ(String wzId, String creator) {
		
	SocialWzSimpleResultDTO dto = new SocialWzSimpleResultDTO();
		
		DcmSocialResource sr = this.socialResDmn.findUniqueByProperties(new String[]{"resId","resTypeCode","creator"}, new Object[]{wzId, ResourceConstants.ResourceType.RES_WZ,creator});
		if(null == sr) {
			dto.setIsFavorite(false);
			dto.setIsTop(false);
			dto.setTag("585d68");
			return dto;
		}
		
		dto.setIsFavorite(0 == sr.getIsFavorite()? false:true);
		dto.setIsTop(0 == sr.getIsLike()? false:true);
		dto.setTag(sr.getTag());
		
		return dto;
	}
	@Override
	public SocialWzSimpleResultDTO getSimpleWzStatus(String wzId) {
		
		SocialWzSimpleResultDTO dto = new SocialWzSimpleResultDTO();
		// 跟用户无关，处理欠妥
		DcmSocialResource sr = this.socialResDmn.findUniqueByProperties(new String[]{"resId","resTypeCode"}, new Object[]{wzId, ResourceConstants.ResourceType.RES_WZ});//this.socialResDmn.findUniqueByProperties(new String[]{"resId","resTypeCode","creator"}, new Object[]{wzId, ResourceConstants.ResourceType.Res_WZ,creator});
		if(null == sr) {

			dto.setIsFavorite(false);
			dto.setIsTop(false);
			dto.setTag("585d68");
			return dto;
			
		}
		
		dto.setIsFavorite(0 == sr.getIsFavorite()? false:true);
		dto.setIsTop(0 == sr.getIsLike()? false:true);
		dto.setTag(sr.getTag());
		
		return dto;
		
	}
	@Override
	public List<DcmSocialResource> getWzOfCase(String caseIdentifier, String creator){
		
		return this.socialResDmn.getWzByCaseIdentifierAndCreator(caseIdentifier, creator);
	}

	@Override
	public Map<String, SocialWzSimpleResultDTO> getMapWzOfCase(SocialWzParaDTO dto) {

		Map<String, SocialWzSimpleResultDTO> result = new HashMap<String, SocialWzSimpleResultDTO>();

		List<DcmSocialResource> list = this.socialResDmn.getWzByCaseId(dto.getCaseId());
		if (list.isEmpty())
			return result;

		for (DcmSocialResource sr : list) {
			// 避免key为null的情况
			if (StringUtils.isEmpty(sr.getResId()))
				continue;

			SocialWzSimpleResultDTO newDto = new SocialWzSimpleResultDTO();
			if (dto.getCreator().equalsIgnoreCase(sr.getCreator())) {
				// 最终以当前登录用户的状态为准
				newDto.setIsFavorite(sr.getIsFavorite().equals(1L) ? true : false);
				newDto.setIsTop(sr.getIsLike().equals(1L) ? true : false);
				newDto.setTag(StringUtils.isEmpty(sr.getTag()) ? "585D68" : sr.getTag());
				result.put(sr.getResId(), newDto);

			} else {
				if (!result.containsKey(sr.getResId())) {
					newDto.setIsFavorite(false);
					newDto.setIsTop(false);
					newDto.setTag(StringUtil.isNullOrEmpty(sr.getTag()) ? "585D68" : sr.getTag());
					result.put(sr.getResId(), newDto);
				}
			}
		}
		return result;
	}
	
	@Override
	public Map<String, SocialWzSimpleResultDTO> getMapWzOfCaseGZ(SocialWzParaDTO dto) {
		
		Map<String, SocialWzSimpleResultDTO> result = new HashMap<String, SocialWzSimpleResultDTO>();
		List<DcmSocialResource> list = this.socialResDmn.getWzByCaseIdentifierAndCreator(dto.getCaseIdentifier(), dto.getCreator());
		if(list.isEmpty()) return result;
		
		for(DcmSocialResource sr : list){
			// 避免key为null的情况
			if(StringUtil.isNullOrEmpty(sr.getResId())) continue;
			
			SocialWzSimpleResultDTO newDto = new SocialWzSimpleResultDTO();
			newDto.setIsFavorite(0 == sr.getIsFavorite()? false:true);
			newDto.setIsTop(0 == sr.getIsLike()? false:true);
			newDto.setTag(StringUtil.isNullOrEmpty(sr.getTag())? "585D68":sr.getTag());// 设置默认值
			result.put(sr.getResId(), newDto);
		}
		return result;
		
	}
	@Override
	public Map<String, SocialWzSimpleResultDTO> getMapWzOfCaseById(SocialWzParaDTO dto) {
		
		Map<String, SocialWzSimpleResultDTO> result = new HashMap<String, SocialWzSimpleResultDTO>();
		List<DcmSocialResource> list = this.socialResDmn.getWzByCaseIdAndCreator(dto.getCaseId(), dto.getCreator());
		if(list.isEmpty()) return result;
		
		for(DcmSocialResource sr : list){
			// 避免key为null的情况
			if(StringUtil.isNullOrEmpty(sr.getResId())) continue;
			
			SocialWzSimpleResultDTO newDto = new SocialWzSimpleResultDTO();
			newDto.setIsFavorite(0 == sr.getIsFavorite()? false:true);
			newDto.setIsTop(0 == sr.getIsLike()? false:true);
			newDto.setTag(StringUtil.isNullOrEmpty(sr.getTag())? "585D68":sr.getTag());// 设置默认值
			result.put(sr.getResId(), newDto);
		}
		return result;
		
	}
	
	@Override
	public boolean downloadRes(DownloadParaDTO downloadDTO) {

		logger.info(">>>downloadRes(DownloadParaDTO downloadDTO) , user=[{}], resId=[{}], type=[{}]", downloadDTO.getUser(),downloadDTO.getId(),downloadDTO.getType());
		
		DcmDownload d = new DcmDownload();
		d.setCreateTime(new Date());
		d.setDownloader(downloadDTO.getUser());
		d.setResId(downloadDTO.getId());
		d.setResTypeCode(ResourceConstants.ResourceType.getResTypeCode(downloadDTO.getType()));
		d.setDomainCode(downloadDTO.getDomainCode());
		
		this.downloadDmn.add(d);

		return true;
		
	}


}
