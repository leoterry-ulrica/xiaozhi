
package com.dist.bdf.facade.service.biz.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.model.dto.dcm.DocumentDTO;
import com.dist.bdf.model.dto.system.DomainOpDTO;
import com.dist.bdf.model.dto.system.ShareInfoDTO;
import com.dist.bdf.model.dto.system.ShareObjectDTO;
import com.dist.bdf.model.dto.system.ShareObjectSimpleDTO;
import com.dist.bdf.model.dto.system.ShareParaDTO;
import com.dist.bdf.model.dto.system.ShareWholeInfoDTO;
import com.dist.bdf.model.dto.system.SharedInfoDTO;
import com.dist.bdf.model.dto.system.SharedWholeInfoDTO;
import com.dist.bdf.model.dto.system.UserResPrivRequestDTO;
import com.dist.bdf.model.dto.system.page.PageSimple;
import com.dist.bdf.model.entity.system.DcmShare;
import com.ibm.ecm.util.p8.P8Connection;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.base.utils.DateUtil;
import com.dist.bdf.base.utils.StringUtil;
import com.dist.bdf.facade.service.ShareService;
import com.dist.bdf.facade.service.biz.domain.system.DcmShareDmn;
import com.dist.bdf.common.conf.ecm.ECMConf;
import com.dist.bdf.common.constants.DomainType;
import com.dist.bdf.common.constants.ResourceConstants;
import com.dist.bdf.manager.ecm.security.PrivilegeFactory;
import com.dist.bdf.manager.ecm.utils.CaseUtil;


/**
 * @author weifj
 *
 */
@Service("ShareService")
@Transactional(propagation=Propagation.REQUIRED)
public class ShareServiceImpl extends CommonServiceImpl implements ShareService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private DcmShareDmn shareDmn;
	@Autowired
	private ECMConf ecmConf;
	@Autowired
	private CaseUtil caseUtil;
	
	/*@Override
	 public DcmShare addResourceShare(ShareDTO shareDto) {
		
		DcmShare ds = new DcmShare();
		
		ds.setSourceDomainType(shareDto.getFromDomainType());
		ds.setSourceDomainCode(shareDto.getFromDomainCode());
		ds.setTargetDomainType(shareDto.getToDomainType());
		ds.setTargetDomainCode(shareDto.getToDomainCode());
		ds.setPrivCodes(StringUtils.join(shareDto.getPrivCodes(), ","));
		ds.setResId(shareDto.getResId());
		ds.setResTypeCode(shareDto.getResTypeCode());
		ds.setShareDateTime(new Date());
		ds.setExpiryDateTime(DateUtil.strToDate(shareDto.getExpiryDateTimeStr()));
		long masks = PrivilegeFactory.getSharePrivMasks();
		List<String> privCodes = PrivilegeFactory.getCodes(masks);
		ds.setPrivCodes(StringUtils.join(privCodes,","));

		return this.shareDmn.add(ds);
	}*/
	
	@Override
	 public void delResourceShare(ShareParaDTO shareDto) {

		this.shareDmn.delShareByResAndTargetDomainType(shareDto.getId(), DomainType.getDomainTypeCode(shareDto.getType()));
	}
	@Override
	public Boolean addResourceShare(ShareParaDTO shareDto) {
		
		//JSONObject jsonObj = JSONObject.fromObject(json);
		String resId = shareDto.getId();//jsonObj.getString("id");
		String sharer = shareDto.getUser();// jsonObj.getString("user");
		// int sourceResType = shareDto.getS_type();// jsonObj.getInt("s_type");
		// String sourceResTypeCode = ResourceConstants.ResourceType.getResTypeCode(sourceResType);
		// String sourceDomainType = DomainType.getDomainTypeCode(sourceResType);
		// String sourceDomainCode = shareDto.getS_dc();// jsonObj.getString("s_dc");
		
		int targetResType = shareDto.getT_type();// jsonObj.getInt("t_type");
		String targetDomainType = DomainType.getDomainTypeCode(targetResType);
		// 目标空间域数组
		DomainOpDTO[] targetDcArray = shareDto.getT_dcs();//jsonObj.getJSONArray("t_dcs");
	
		List<String> privCodes = PrivilegeFactory.getCodes(PrivilegeFactory.getSharePrivMasks());
		String privCodesStr = StringUtils.join(privCodes,",");
		int targetDcSize =  targetDcArray.length;
		if(targetDcArray != null && targetDcSize >0){
			for(int i=0; i< targetDcSize;i++){
				DomainOpDTO temp = targetDcArray[i];
				if(0 == temp.getOp()){
					// 删除共享
					this.shareDmn.delResourceShare(resId, DomainType.getDomainTypeCode(shareDto.getT_type()), temp.getDc());
				} else if(1 == temp.getOp()){
					// 添加共享
				
					DcmShare dcmshare = this.shareDmn.findUniqueByProperties(new String[]{"resId","sharer","targetDomainCode","status"}, new Object[]{resId,sharer,temp.getDc(), 1L});
					Date now = new Date();
					if(null == dcmshare) {
				    	
				    	dcmshare = new DcmShare();
				    	dcmshare.setResId(resId);
				    	dcmshare.setResTypeCode(ResourceConstants.ResourceType.getResTypeCode(shareDto.getS_type()));
				    	dcmshare.setSourceDomainType(DomainType.getDomainTypeCode(shareDto.getS_type()));
				    	dcmshare.setSourceDomainCode(shareDto.getS_dc());
				    	dcmshare.setTargetDomainType(targetDomainType);
				    	dcmshare.setTargetDomainCode(temp.getDc());
				    	dcmshare.setPrivCodes(StringUtil.isNullOrEmpty(temp.getPrivCode())? privCodesStr : temp.getPrivCode());
				    	dcmshare.setStatus(1L);
				    	dcmshare.setShareType(shareDto.getShareType());
				    	dcmshare.setIsFolder(shareDto.getIsFolder());
				    	dcmshare.setRealm(shareDto.getRealm());
				    	dcmshare.setShareDateTime(now);
				    	dcmshare.setSharer(sharer);
				    	// 使用默认有效期：2年
			    		dcmshare.setExpiryDateTime(DateUtil.addYear(now, 2));
				    		    	
				    	this.shareDmn.add(dcmshare);
				    } else {
				    	// 如果已存在，则验证是否已过期
				    	/*if(DateUtil.compare(dcmshare.getExpiryDateTime(), now) > 0){
				    		// 说明过期了
				    		dcmshare.setStatus(0L);
				    	}*/
				    }
				} else if(2 == temp.getOp()){
					// 更改
					DcmShare dcmshare = this.shareDmn.findUniqueByProperties(new String[]{"resId","sharer","targetDomainCode","status"}, new Object[]{resId,sharer,temp.getDc(), 1L});
					if(dcmshare != null){
						dcmshare.setPrivCodes(StringUtil.isNullOrEmpty(temp.getPrivCode())? privCodesStr : temp.getPrivCode());
						dcmshare.setRealm(shareDto.getRealm());
				    	this.shareDmn.modify(dcmshare);
					}
				}
				// 
			
			}
		}
		
		return true;
	}
	@Override
	public Object checkShareInfo() {
		
		List<DcmShare> data = this.shareDmn.find();
		Date now = new Date();
		if(data != null && data.size()>0){
			for(DcmShare s : data){
				if(DateUtil.compare(s.getExpiryDateTime(), now) > 0){
		    		// 说明过期了
		    		s.setStatus(0L);
		    		this.shareDmn.modify(s);
		    	}
			}
		}
		
		return this.shareDmn.find();
	}
	
	@Override
	public  Object getPersonalShare(PageSimple pageInfo) {
		
		Pagination page =  (Pagination) this.shareDmn.getSharedInfoBySharer(pageInfo.getUser(),pageInfo.getPageNo(),pageInfo.getPageSize(),pageInfo.getScope(), 0L);
		return convertToShareInfo(pageInfo, page);

	}
	
	@Override
	public Object getPersonalShareWholeInfo(PageSimple pageInfo) {
	
		Pagination page =  (Pagination) this.shareDmn.getSharedInfoBySharer(pageInfo.getUser(),pageInfo.getPageNo(),pageInfo.getPageSize(),pageInfo.getScope(), 0L);
		return convertToShareWholeInfo(pageInfo, page);

	}
	
	private Pagination convertToShareWholeInfo(PageSimple pageInfo, Pagination page) {
		
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(pageInfo.getRealm()));
		
		List<ShareWholeInfoDTO> data = new ArrayList<ShareWholeInfoDTO>();
		@SuppressWarnings("unchecked")
		List<DcmShare> shareInfo = (List<DcmShare>) page.getData();
		Map<String,ShareWholeInfoDTO> resAndType2ShareMap = new HashMap<String, ShareWholeInfoDTO>();
		DocumentDTO dcoDTO = null;
		if(shareInfo != null && shareInfo.size() >0){
			ShareWholeInfoDTO dto = null;
			for(DcmShare share : shareInfo){
				String resIdAndType = share.getResId()+"-"+share.getTargetDomainType();
				if(resAndType2ShareMap.containsKey(resIdAndType)){
					dto = resAndType2ShareMap.get(resIdAndType);
				}else{
					dto = new ShareWholeInfoDTO();
					dto.setId(share.getResId());
					// dto.setShareTimeStr(DateUtil.toDateTimeStr(share.getShareDateTime()));
					// dto.setExpiryTimeStr(DateUtil.toDateTimeStr(share.getExpiryDateTime()));
					dto.setS_type(ResourceConstants.ResourceType.getClientResType(share.getResTypeCode()));
					
					dto.setSourceDomainEntity(this.getEntityByType(share.getSourceDomainType(), share.getSourceDomainCode(), p8conn));
					
					//dto.setS_dc(share.getSourceDomainCode());
					dto.setT_type(DomainType.getClientResType(share.getTargetDomainType()));
					
					dto.setSharer(this.userOrgService.getSimpleUserByCode(pageInfo.getUser()));
					/*if(DateUtil.compare(new Date(), share.getExpiryDateTime())>0) {
						// 已过期
						dto.setStatus(0L);
					}else{
						dto.setStatus(share.getStatus());
					}	*/
					resAndType2ShareMap.put(resIdAndType, dto);
				}
				ShareObjectDTO sod = new ShareObjectDTO();
				sod.setEntity(this.getEntityByType(share.getTargetDomainType(), share.getTargetDomainCode(), p8conn));
				sod.setPrivCodes(StringUtil.isNullOrEmpty(share.getPrivCodes())? null : share.getPrivCodes().split(","));
				
				dto.getTargetDomainEntities().add(sod);
			
				dto.setIsFolder(share.getIsFolder());
				// 获取ce实体
				if(1 == share.getIsFolder()){
					// 文件夹
					dcoDTO = this.folderUtil.getFolderDTOById(p8conn.getObjectStore(), dto.getId());
				}else {
					dcoDTO = this.docUtil.loadDocDTOByVersionSeriesId(p8conn.getObjectStore(), dto.getId());
				}
				dto.setShareRes(dcoDTO);
			}
		}
		if(resAndType2ShareMap.size() >0 ){
			Set<String> keys = resAndType2ShareMap.keySet();
			for(String key : keys){
				data.add(resAndType2ShareMap.get(key));
			}
			
		}
		
		page.setData(data);
		return page;
	}
	/**
	 * 转换成共享信息对象
	 * @param pageInfo
	 * @param page
	 * @return
	 */
	private Pagination convertToShareInfo(PageSimple pageInfo, Pagination page) {
		
		List<ShareInfoDTO> data = new ArrayList<ShareInfoDTO>();
		@SuppressWarnings("unchecked")
		List<DcmShare> shareInfo = (List<DcmShare>) page.getData();
		Map<String,ShareInfoDTO> resAndType2ShareMap = new HashMap<String, ShareInfoDTO>();
		if(shareInfo != null && shareInfo.size() >0){
			ShareInfoDTO dto = null;
			for(DcmShare share : shareInfo){
				String resIdAndType = share.getResId()+"-"+share.getTargetDomainType();
				if(resAndType2ShareMap.containsKey(resIdAndType)){
					dto = resAndType2ShareMap.get(resIdAndType);
				}else{
					dto = new ShareInfoDTO();
					dto.setId(share.getResId());
					// dto.setShareTimeStr(DateUtil.toDateTimeStr(share.getShareDateTime()));
					// dto.setExpiryTimeStr(DateUtil.toDateTimeStr(share.getExpiryDateTime()));
					dto.setS_type(ResourceConstants.ResourceType.getClientResType(share.getResTypeCode()));
					dto.setS_dc(share.getSourceDomainCode());
					dto.setT_type(DomainType.getClientResType(share.getTargetDomainType()));
					
					dto.setUser(pageInfo.getUser());
					if(DateUtil.compare(new Date(), share.getExpiryDateTime())>0) {
						// 已过期
						dto.setStatus(0L);
					}else{
						dto.setStatus(share.getStatus());
					}	
					resAndType2ShareMap.put(resIdAndType, dto);
				}
				//dto.getT_dcs().add(share.getTargetDomainCode());
				dto.getT_dos().add(new ShareObjectSimpleDTO(share.getTargetDomainCode(), StringUtil.isNullOrEmpty(share.getPrivCodes())? null : share.getPrivCodes().split(",")));
				dto.setIsFolder(share.getIsFolder());
			}
		}
		if(resAndType2ShareMap.size() >0 ){
			Set<String> keys = resAndType2ShareMap.keySet();
			for(String key : keys){
				data.add(resAndType2ShareMap.get(key));
			}
			
		}
		
		page.setData(data);
		return page;
	}
	
	/**
	 * 转换成被共享信息对象
	 * @param pageInfo
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Pagination convertToSharedInfo(PageSimple pageInfo, Pagination page) {
		
		List<SharedInfoDTO> data = new ArrayList<SharedInfoDTO>();
		List<DcmShare> shareInfo = (List<DcmShare>) page.getData();
		if(shareInfo != null && shareInfo.size() >0){

			for(DcmShare share : shareInfo){

				SharedInfoDTO dto = new SharedInfoDTO();

				dto.setId(share.getResId());
				// dto.setShareTimeStr(DateUtil.toDateTimeStr(share.getShareDateTime()));
				// dto.setExpiryTimeStr(DateUtil.toDateTimeStr(share.getExpiryDateTime()));
				dto.setS_type(ResourceConstants.ResourceType.getClientResType(share.getResTypeCode()));
				dto.setS_dc(share.getSourceDomainCode());
				dto.setT_type(DomainType.getClientResType(share.getTargetDomainType()));
				dto.setT_dc(share.getTargetDomainCode());
				dto.setUser(share.getSharer()); // 注意是获取分享者
				dto.setIsFolder(share.getIsFolder());
				
				logger.info(">>>根据权限mask值判断是否有下载权限......");
			    if(0 == share.getIsFolder()){
					
					// 文档
					// Long privMasks = this.privService.getDocPrivMasksByVID(pageInfo.getRealm(), pageInfo.getUser(), share.getResId());
					// dto.setHavePriv(PrivilegeFactory.haveDownload(privMasks));
			    	UserResPrivRequestDTO requestDTO = new UserResPrivRequestDTO();
					requestDTO.setRealm(pageInfo.getRealm());
					requestDTO.setUser(pageInfo.getUser());
					requestDTO.setFrom(0L);
					requestDTO.setResId(share.getResId());
					List<String> privCodes = (List<String>) this.privService.getDocPrivCodesEx(requestDTO);
					dto.setPrivCodes(privCodes);
					
				 }else {
				
					 dto.setPrivCodes(StringUtil.isNullOrEmpty(share.getPrivCodes())? null : Arrays.asList(share.getPrivCodes().split(",")));
					// dto.setHavePriv(true);
				 }
				
				
                dto.setIsFavorite(super.isFavorite(pageInfo.getUser(), share.getResId()));
                dto.setSharedTime(share.getShareDateTime().getTime());
				if(DateUtil.compare(new Date(), share.getExpiryDateTime())>0) {
					// 已过期
					dto.setStatus(0L);
				}else{
					dto.setStatus(share.getStatus());
				}
				data.add(dto);
			}
		}
				
		page.setData(data);
		return page;
	}
	
	/**
	 * 获取共享对象实体
	 * @param domainType
	 * @param domainCode
	 * @param p8conn
	 * @return
	 */
	private Object getEntityByType(String domainType, String domainCode, P8Connection p8conn) {
		
		if(domainType.equalsIgnoreCase(DomainType.PERSON)) {
			// 共享对象：个人
			return this.userOrgService.getSimpleUserByCode(domainCode);
			
		}else if(domainType.equalsIgnoreCase(DomainType.INSTITUTE) ||
				domainType.equalsIgnoreCase(DomainType.DEPARTMENT)) {
			// 共享对象：院或所
			return this.userOrgService.getOrgByCode(domainCode);
			
		} else if(domainType.equalsIgnoreCase(DomainType.PROJECT)) {
			// 共享对象：项目
			return this.caseUtil.getCaseById(p8conn.getObjectStore(), domainCode);
		}
		return null;
	}

	private Pagination convertToSharedWholeInfo(PageSimple pageInfo, Pagination page) {

		P8Connection p8conn = this.connectionService
				.getP8Connection(this.ecmConf.getTargetObjectStore(pageInfo.getRealm()));

		List<SharedWholeInfoDTO> data = new ArrayList<SharedWholeInfoDTO>();
		DocumentDTO dcoDTO = null;
		@SuppressWarnings("unchecked")
		List<DcmShare> shareInfo = (List<DcmShare>) page.getData();
		if (shareInfo != null && shareInfo.size() > 0) {

			for (DcmShare share : shareInfo) {

				try {

					SharedWholeInfoDTO dto = new SharedWholeInfoDTO();

					dto.setId(share.getResId());
					// dto.setShareTimeStr(DateUtil.toDateTimeStr(share.getShareDateTime()));
					// dto.setExpiryTimeStr(DateUtil.toDateTimeStr(share.getExpiryDateTime()));
					dto.setS_type(ResourceConstants.ResourceType.getClientResType(share.getResTypeCode()));

					dto.setSourceDomainEntity(
							this.getEntityByType(share.getSourceDomainType(), share.getSourceDomainCode(), p8conn));

					dto.setT_type(DomainType.getClientResType(share.getTargetDomainType()));

					ShareObjectDTO sod = new ShareObjectDTO();
					sod.setEntity(
							this.getEntityByType(share.getTargetDomainType(), share.getTargetDomainCode(), p8conn));
					sod.setPrivCodes(
							StringUtil.isNullOrEmpty(share.getPrivCodes()) ? null : share.getPrivCodes().split(","));
					dto.setTargetDomainEntity(sod);

					dto.setSharer(this.userOrgService.getSimpleUserByCode(share.getSharer())); // 注意是获取分享者
					dto.setIsFolder(share.getIsFolder());

					logger.info(">>>根据权限mask值判断是否有下载权限......");
					if (0 == share.getIsFolder()) {
						// 文档
						/*Long privMasks = this.privService.getDocPrivMasksByVID(pageInfo.getRealm(), pageInfo.getUser(),
								share.getResId());*/
						dto.setHavePriv(false);// PrivilegeFactory.haveDownload(privMasks)

					} else {
						// 默认文件夹都有权限
						dto.setHavePriv(true);
					}

					dto.setIsFavorite(super.isFavorite(pageInfo.getUser(), share.getResId()));
					dto.setSharedTime(share.getShareDateTime().getTime());
					/*
					 * if(DateUtil.compare(new Date(),
					 * share.getExpiryDateTime())>0) { // 已过期 dto.setStatus(0L);
					 * }else{ dto.setStatus(share.getStatus()); }
					 */
					// 获取ce实体
					if (1 == share.getIsFolder()) {
						// 文件夹
						dcoDTO = this.folderUtil.getFolderDTOById(p8conn.getObjectStore(), dto.getId());
					} else {
						dcoDTO = this.docUtil.loadDocDTOByVersionSeriesId(p8conn.getObjectStore(), dto.getId());
					}
					dto.setShareRes(dcoDTO);
					data.add(dto);
				} catch (Exception e) {
					logger.error(">>>模型转换出错，但已忽略，详情：" + e.getMessage());
					continue;
				}
			}
		}

		page.setData(data);
		return page;
	}
	@Override
	public Object getSharedInfoByOthers(PageSimple pageInfo) {
		
		Pagination page =  (Pagination) this.shareDmn.getSharedInfoByOthers(pageInfo.getUser(),pageInfo.getPageNo(),pageInfo.getPageSize(), pageInfo.getScope());
		
		return convertToSharedInfo(pageInfo, page);

	}
	
	@Override
	public Object getSharedWholeInfoByOthers(PageSimple pageInfo) {
		
        Pagination page =  (Pagination) this.shareDmn.getSharedInfoByOthers(pageInfo.getUser(),pageInfo.getPageNo(),pageInfo.getPageSize(), pageInfo.getScope());
		
		return convertToSharedWholeInfo(pageInfo, page);
		
	}
}
