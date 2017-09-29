package com.dist.bdf.facade.service.biz.domain.system.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.utils.StringUtil;
import com.dist.bdf.facade.service.biz.dao.DcmSocialResourceDAO;
import com.dist.bdf.facade.service.biz.domain.system.DcmSocialResourceDmn;
import com.dist.bdf.common.constants.ResourceConstants;
import com.dist.bdf.model.dto.system.SocialResourceDTO;
import com.dist.bdf.model.entity.system.DcmSocialResource;


/**
 * 资源社交化领域
 * @author weifj
 * @version 1.0，2016/03/30，weifj，创建
 */

@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class DcmSocialResourceDmnImpl extends GenericDmnImpl<DcmSocialResource, Long>implements DcmSocialResourceDmn {

	@Autowired
	private DcmSocialResourceDAO socialResDao;
	//private GenericDAOImpl<DcmSocialResource, Long> socialResDao;
	
	@Override
	public GenericDAO<DcmSocialResource, Long> getDao() {
		
		return socialResDao;
	}
	@Override
	public List<DcmSocialResource> getByResId(String resId) {
		
		return this.getDao().findByProperty("resId", resId);
	}
	@Override
	public DcmSocialResource getByResIdAndCreator(String resId, String creator){
		
		return this.getDao().findUniqueByProperties(new String[]{"resId","creator"}, new Object[]{resId, creator});
	}
	@Override
	public List<DcmSocialResource> getByResIdAndCreator(String[] resIds, String creator) {
		
		Map<String, Object[]>propertiesValuesMap = new HashMap<String, Object[]>();
		propertiesValuesMap.put("resId", resIds);
		propertiesValuesMap.put("creator", new Object[]{creator});
		
		return this.getDao().findByProperties(propertiesValuesMap);
	}
	@Override
	public List<DcmSocialResource> getWzByCaseIdentifierAndCreator(String caseIdentifier, String creator){
		
		return this.getDao().findByProperties(new String[]{"parentResId","resTypeCode", "creator"}, new Object[]{caseIdentifier,ResourceConstants.ResourceType.RES_WZ, creator});
	}
	@Override
	public List<DcmSocialResource> getWzByCaseIdAndCreator(String caseId, String creator) {
		
		return this.getDao().findByProperties(new String[]{"parentResId","resTypeCode", "creator"}, new Object[]{caseId,ResourceConstants.ResourceType.RES_WZ, creator});

	}
	@Override
	public
	List<DcmSocialResource> getWzByCaseId(String caseId) {
		
		return this.getDao().findByProperties(new String[]{"parentResId","resTypeCode"}, new Object[]{caseId,ResourceConstants.ResourceType.RES_WZ});

	}
	@Override
	public List<DcmSocialResource> getWzByCaseId(String caseId, String creator) {
		
		return this.getDao().findByProperties(new String[]{"parentResId","resTypeCode","creator"}, new Object[]{caseId,ResourceConstants.ResourceType.RES_WZ, creator});

	}
	@Override
	public Map<String, DcmSocialResource> getMapWzByCaseIdentifier(String caseIdentifier, String creator) {
		
		/*Query query = this.socialResDao.createQuery(queryString).createSQLQuery("select sum(id) SUMID from Tree t where pid in (select id from Tree)
				.addScalar("SUMID",Hibernate.INTEGER)*/
		return null;
	}
	@Override
	public void savaSocialData(SocialResourceDTO dto, String resTypeName) {
		
		//SocialWzParaDTO wzDTO = (SocialWzParaDTO) dto;
		if(StringUtil.isNullOrEmpty(dto.getGuid())){
			throw new BusinessException("资源id[guid]不能为空。");
		}
		
		DcmSocialResource sr = this.getDao().findUniqueByProperties(new String[]{"resId","creator"}, new Object[]{dto.getGuid(),dto.getCreator()});
		if(null == sr){
			sr = new DcmSocialResource();
			sr.setIsFavorite(dto.getIsFavorite()? 1L : 0L);
			sr.setIsLike(dto.getIsTop()? 1L : 0L);
			sr.setCreator(dto.getCreator());
			sr.setResId(dto.getGuid());
			sr.setResTypeCode(resTypeName);
			sr.setParentResId(dto.getParentResId());
			sr.setTag(dto.getTag());
			sr.setTimeLike(new Date());
			sr.setTimeFavorite(new Date());
			sr.setTimeTag(new Date());
		} else{
			sr.setCreator(dto.getCreator());
			sr.setResId(dto.getGuid());
			sr.setResTypeCode(resTypeName);
			sr.setParentResId(dto.getParentResId());

			long temp = dto.getIsFavorite()? 1L : 0L;
			Date currTime = new Date();
			if(sr.getIsFavorite() != null && !sr.getIsFavorite().equals(temp)){
				// 如果不相等，才说明要更新
				sr.setTimeFavorite(currTime);
				sr.setIsFavorite(temp);
			}
			temp = dto.getIsTop()? 1L : 0L;
			if(sr.getIsLike() != null && !sr.getIsLike().equals(temp)){
				sr.setTimeLike(currTime);
				sr.setIsLike(temp);	
			}
			if(null == sr.getTag()){
				sr.setTimeTag(currTime);
		    	sr.setTag(dto.getTag());
			}else if(!sr.getTag().equals(dto.getTag())){
		    	sr.setTimeTag(currTime);
		    	sr.setTag(dto.getTag());
		    }
		}
		this.getDao().saveOrUpdate(sr);
		// 方法欠妥，需要完善
		this.getDao().update(" UPDATE "+DcmSocialResource.class.getSimpleName()+" T SET T.tag = '"+dto.getTag()+"' WHERE resId = '"+dto.getGuid()+"'");
	}
	
	@Override
	public void savaSocialDataGZ(SocialResourceDTO dto, String resTypeName) {
		
		if(StringUtil.isNullOrEmpty(dto.getGuid())){
			throw new BusinessException("资源id[guid]不能为空。");
		}
		
		DcmSocialResource sr = this.getDao().findUniqueByProperties(new String[]{"resId","creator"}, new Object[]{dto.getGuid(),dto.getCreator()});
		if(null == sr){
			sr = new DcmSocialResource();
			sr.setIsFavorite(dto.getIsFavorite()? 1L : 0L);
			sr.setIsLike(dto.getIsTop()? 1L : 0L);
			sr.setCreator(dto.getCreator());
			sr.setResId(dto.getGuid());
			sr.setResTypeCode(resTypeName);
			sr.setParentResId(dto.getParentResId());
			sr.setTag(dto.getTag());
			sr.setTimeLike(new Date());
			sr.setTimeFavorite(new Date());
			sr.setTimeTag(new Date());
		} else{
			sr.setCreator(dto.getCreator());
			sr.setResId(dto.getGuid());
			sr.setResTypeCode(resTypeName);
			sr.setParentResId(dto.getParentResId());

			long temp = dto.getIsFavorite()? 1L : 0L;
			Date currTime = new Date();
			if(sr.getIsFavorite() != null && !sr.getIsFavorite().equals(temp)){
				// 如果不相等，才说明要更新
				sr.setTimeFavorite(currTime);
				sr.setIsFavorite(temp);
			}
			temp = dto.getIsTop()? 1L : 0L;
			if(sr.getIsLike() != null && !sr.getIsLike().equals(temp)){
				sr.setTimeLike(currTime);
				sr.setIsLike(temp);	
			}
			if(null == sr.getTag()){
				sr.setTimeTag(currTime);
		    	sr.setTag(dto.getTag());
			}else if(!sr.getTag().equals(dto.getTag())){
		    	sr.setTimeTag(currTime);
		    	sr.setTag(dto.getTag());
		    }
		}
		
		this.getDao().saveOrUpdate(sr);
	}
	
	@Override
	public void deleteByResId(String redId) {
		this.getDao().removeByProperty("resId", redId);
	}
	@Override
	public List<DcmSocialResource> getWzLikedByCaseIdentifierAndCreator(String caseIdentifier, String creator) {
		
		return this.getDao().findByProperties(new String[]{"parentResId","resTypeCode", "creator","isLike"}, new Object[]{caseIdentifier,ResourceConstants.ResourceType.RES_WZ, creator, 1L});
		
	}
	@Override
	public void deleteByParentResId(String parentResId) {
		
		this.getDao().removeByProperty("parentResId", parentResId);
	}
	
	@Override
	public boolean isFavorite(DcmSocialResource sr) {
		
		return  (null !=sr && 1L == sr.getIsFavorite());
	}
}
