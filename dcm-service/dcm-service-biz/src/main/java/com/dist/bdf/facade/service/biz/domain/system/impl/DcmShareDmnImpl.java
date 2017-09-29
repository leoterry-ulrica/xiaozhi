package com.dist.bdf.facade.service.biz.domain.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.base.utils.StringUtil;
import com.dist.bdf.facade.service.biz.dao.DcmShareDAO;
import com.dist.bdf.facade.service.biz.domain.system.DcmShareDmn;
import com.dist.bdf.model.dto.system.ShareObjectSimpleDTO;
import com.dist.bdf.model.entity.system.DcmShare;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

/**
 * 
 * @author weifj
 * @create 2015-06-06
 */
@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class DcmShareDmnImpl extends GenericDmnImpl<DcmShare, Long>implements DcmShareDmn {

	@Autowired
	private DcmShareDAO shareDao;
	//private GenericDAOImpl<DcmShare, Long> shareDao;

	@Override
	public GenericDAO<DcmShare, Long> getDao() {
		return shareDao ;
	}

	@Override
	public DcmShare addResourceShare(DcmShare share){
		
		return this.getDao().save(share);
	}
	
	@Override
	public void delResourceShare(String resId, String domainCode, List<String> privCodes){
		
		if(null==privCodes || 0 == privCodes.size()){
			return;
		}
		for(String privCode : privCodes){
			this.getDao().removeByProperties(new String[]{"resId","domainCode","privCode"}, new Object[]{resId,domainCode,privCode});	
		}
	}
	@Override
	public void delResourceShare(String resId, String domainCode) {
		
		DcmShare share = this.getDao().findUniqueByProperties(new String[]{"resId","targetDomainCode"}, new Object[]{resId, domainCode});
		if(share != null){
			share.setStatus(0L);
			this.getDao().update(share);
		}
	}
	@Override
	public void delResourceShare(String resId) {
		List<DcmShare> shares = this.getDao().findByProperty("resId", resId);
		if(shares != null && shares.size() >0){
			for(DcmShare s : shares){
				s.setStatus(0L);
				this.getDao().update(s);
			}
		}
	}
	@Override
	public void delShareByResAndTargetDomainType(String resId, String targetDomainTypeCode) {
		
		List<DcmShare> shares = this.getDao().findByProperties(new String[]{"resId","targetDomainType"}, new Object[]{resId, targetDomainTypeCode});
		if(shares != null && shares.size() >0){
			for(DcmShare s : shares){
				s.setStatus(0L);
				this.getDao().update(s);
			}
		
		}
	}
	@Override
	public void delResourceShare(String resId, String targetDomainTypeCode, String targetDomainCode) {
		
		List<DcmShare> shares = this.getDao().findByProperties(new String[]{"resId","targetDomainType","targetDomainCode"}, new Object[]{resId, targetDomainTypeCode,targetDomainCode});
		if(shares != null && shares.size() >0){
			for(DcmShare s : shares){
				s.setStatus(0L);
				this.getDao().update(s);
			}
		
		}
	}
	@Override
	public List<DcmShare> getShareInfosByResId(String resId) {
		
		return this.getDao().findByProperties(new String[]{"resId","status"}, new Object[]{resId, 1L}, "shareDateTime", false);
	}
	@Override
	public List<String> getTargetDomainCodes(String resId, String targetDomainType) {

		List<DcmShare> list = this.getDao().findByProperties(new String[]{"resId","status","targetDomainType"}, new Object[]{resId, 1L, targetDomainType}, "shareDateTime", false);
	   if(list != null && list.size()>0){
		   List<String> tdCodes = new ArrayList<String>();
		   for(DcmShare s : list){
			   if(tdCodes.contains(s.getTargetDomainCode())) continue;
			   
			   tdCodes.add(s.getTargetDomainCode());
		   }
		   return tdCodes;
	   }
     return null;
	}
	
	@Override
	public List<ShareObjectSimpleDTO> getTargetDomainPrivCodes(String resId, String targetDomainType) {

		List<DcmShare> list = this.getDao().findByProperties(new String[]{"resId","status","targetDomainType"}, new Object[]{resId, 1L, targetDomainType}, "shareDateTime", false);
	   if(list != null && list.size()>0){
		   List<ShareObjectSimpleDTO> tdCodes = new ArrayList<ShareObjectSimpleDTO>();
		   for(DcmShare s : list){
			   
			   ShareObjectSimpleDTO dto = new ShareObjectSimpleDTO();
			   dto.setDomainCode(s.getTargetDomainCode());
			   dto.setPrivCodes(StringUtil.isNullOrEmpty(s.getPrivCodes())? null : s.getPrivCodes().split(","));
			   
			   tdCodes.add(dto);
		   }
		   return tdCodes;
	   }
     return null;
	}
	
	
	
	@Override
	public Object getUniqueShareInfoByResIdAndTarget(String resId, String targetDomainCode) {
		
		return this.getDao().findUniqueByProperties(new String[]{"resId","targetDomainCode", "status"}, new Object[]{resId,targetDomainCode, 1L});
	}
	
	@Override
	public Object getUniqueShareInfoByResIdAndTarget(String resId, String[] targetDomainCodes) {
		
		Map<String, Object[]> propvalues = new HashMap<>();
		propvalues.put("resId", new Object[]{resId});
		propvalues.put("targetDomainCode", targetDomainCodes);
		propvalues.put("status", new Object[]{1L});
		
		List<DcmShare> shares = this.getDao().findByProperties(propvalues);
		if(shares != null && !shares.isEmpty()) return shares.get(0);
		
		return null;
	}
	@Override
    public Pagination getSharedInfoBySharer(String userName, int pageNo, int pageSize, long status) {
		if(status <2 ){
			Map<String,Object[]> propertiesValuesMap = new HashMap<String,Object[]>();
			propertiesValuesMap.put("status", new Object[]{status});
			propertiesValuesMap.put("sharer", new Object[]{userName});
			return this.getDao().findByPropertiesAndValues(pageNo, pageSize, propertiesValuesMap, "shareDateTime",false);	
		}
		return this.getDao().findByProperty(pageNo, pageSize, "sharer", userName,"shareDateTime",false);
	}
	@Override
	public Object getSharedInfoBySharer(String userName, int pageNo, int pageSize, long status, long shareType) {
		
		Map<String,Object[]> propertiesValuesMap = new HashMap<String,Object[]>();
		propertiesValuesMap.put("status", new Object[]{status});
		propertiesValuesMap.put("sharer", new Object[]{userName});
		propertiesValuesMap.put("shareType", new Object[]{shareType});
		return this.getDao().findByPropertiesAndValues(pageNo, pageSize, propertiesValuesMap, "shareDateTime",false);	
		
	}
	@Override
	public Object getSharedInfoByOthers(String userId, int pageNo, int pageSize, long status) {
		if(status <2 ){
			Map<String,Object[]> propertiesValuesMap = new HashMap<String,Object[]>();
			propertiesValuesMap.put("status", new Object[]{status});
			propertiesValuesMap.put("targetDomainCode", new Object[]{userId});
			propertiesValuesMap.put("shareType", new Object[]{0L});
			return this.getDao().findByPropertiesAndValues(pageNo, pageSize, propertiesValuesMap, "shareDateTime",false);	
		}
		return this.getDao().findByProperty(pageNo, pageSize, "targetDomainCode", userId,"shareDateTime",false);
	}

	@Override
	public void delShareBySourceDomainCode(String domainCode) {
		
		this.getDao().removeByProperty("sourceDomainCode", domainCode);
	}
	
	@Override
	public void delShareByTargetDomainCode(String domainCode) {
		
		this.getDao().removeByProperty("targetDomainCode", domainCode);
	}
	@Override
	public void delResourceBySharer(String userCode) {
		
		this.getDao().removeByProperty("sharer", userCode);
	}
}
