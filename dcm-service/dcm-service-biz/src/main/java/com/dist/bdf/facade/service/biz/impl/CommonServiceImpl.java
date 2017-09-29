package com.dist.bdf.facade.service.biz.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dist.bdf.facade.service.CommonService;
import com.dist.bdf.facade.service.PrivilegeService;
import com.dist.bdf.facade.service.UserOrgService;
import com.dist.bdf.facade.service.biz.domain.dcm.CEDocAccessDmn;
import com.dist.bdf.facade.service.biz.domain.dcm.DocAndFolderDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmDicChannelDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmDownloadDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmPersonalMaterialDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmResourceTypeDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmShareDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmSocialResourceDmn;
import com.dist.bdf.facade.service.uic.domain.DcmUserdomainroleDmn;
import com.dist.bdf.manager.ecm.security.ConnectionService;
import com.dist.bdf.manager.ecm.utils.DistSocialUtil;
import com.dist.bdf.manager.ecm.utils.DocumentUtil;
import com.dist.bdf.manager.ecm.utils.FolderUtil;
import com.dist.bdf.manager.ecm.utils.SearchEngine;
import com.dist.bdf.model.entity.system.DcmPersonalmaterial;
import com.dist.bdf.model.entity.system.DcmResourceType;
import com.dist.bdf.model.entity.system.DcmShare;
import com.dist.bdf.model.entity.system.DcmSocialResource;
import com.filenet.api.core.ObjectStore;

@Service("CommonService")
public class CommonServiceImpl implements CommonService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected DcmPersonalMaterialDmn personalMaterialDmn;
	@Autowired
	protected CEDocAccessDmn docDmn;
	/*@Autowired
	protected DcmUserDmn userDmn;*/
	@Autowired
	protected PrivilegeService privService;
	@Autowired
	protected DcmShareDmn shareDmn;
	@Autowired
	protected DcmSocialResourceDmn socialResDmn;
	@Autowired
	protected ConnectionService connectionService;
	@Autowired
	protected SearchEngine searchEngine;
	@Autowired
	protected DocAndFolderDmn docAndFolderDmn;
	@Autowired
	protected DcmPersonalMaterialDmn pmDmn;
	@Autowired
	protected FolderUtil folderUtil;
	@Autowired
	protected DocumentUtil docUtil;
	@Autowired
	protected DcmDownloadDmn downloadDmn;
	@Autowired
	protected UserOrgService userOrgService;
	@Autowired
	protected DcmUserdomainroleDmn udrDmn;
	@Autowired
	protected DcmResourceTypeDmn resTypeDmn;
	@Autowired
	protected DistSocialUtil distSocialUtil;
	@Autowired
	protected DcmDicChannelDmn dcmDicChannelDmn;
	
	public void deleteSubMaterialRecursion(String parentId) {

		List<DcmPersonalmaterial> pms = this.personalMaterialDmn.findByProperty("parentResId", parentId);
		if (pms != null && pms.size() > 0) {
			for (DcmPersonalmaterial pm : pms) {
				deleteSubMaterialRecursion(pm.getResId());
			}
		}
		this.personalMaterialDmn.removeByProperty("resId", parentId);
	}
	
	public Object clearResource(String resId) {
	
		logger.info("正在清理个人资料......");
		deleteSubMaterialRecursion(resId);
		logger.info("正在清理社交化数据......");
		this.socialResDmn.removeByProperty("resId", resId);
		logger.info("正在清理共享信息......");
		this.shareDmn.delResourceShare(resId);
		logger.info("正在清理下载记录......");
		this.downloadDmn.removeByProperty("resId", resId);
		logger.info("清理完成。");
		return true;
	}
	/**
	 * 清理业务数据和ce统计数据
	 * @param os
	 * @param resId
	 * @return
	 */
	protected Object clearResource(ObjectStore os, String resId) {
		
		logger.info("正在清理个人资料......");
		deleteSubMaterialRecursion(resId);
		logger.info("正在清理社交化数据......");
		this.socialResDmn.removeByProperty("resId", resId);
		logger.info("正在清理共享信息......");
		this.shareDmn.delResourceShare(resId);
		logger.info("正在清理下载记录......");
		this.downloadDmn.removeByProperty("resId", resId);
		logger.info("正在清理统计数据记录......");
		this.distSocialUtil.deleteSocialDatum(os, resId);
		logger.info("清理完成。");
		return true;
	}
	
    protected Object clearResource(ObjectStore os, List<String> resIds) {

		for(String resId : resIds){
			
			clearResource(os, resId);
		}
		
		return true;
	}

	/*@Override
	public boolean isHaveDownloadPriv(String user, String resId) {

		boolean havePriv = Boolean.FALSE;

		try {
			DcmPersonalmaterial pm = this.personalMaterialDmn.findUniqueByProperties(new String[] { "resId", "creator" },
					new Object[] { resId, user });
			if (pm != null) {
				logger.info("属于个人资料，拥有所有权。");
				havePriv = Boolean.TRUE;
				return havePriv;
			}

			DcmShare share = (DcmShare) this.shareDmn.getUniqueShareInfoByResIdAndTarget(resId, user);
			if (share != null) {
				logger.info("资料被共享给当前用户，拥有下载权限。");
				havePriv = Boolean.TRUE;
				return havePriv;
			}
			String domainCode = this.docAndFolderDmn.getDocResDomainCode(resId);
			// 判断当前用户是否属于这个项目组，如果是，则都有下载权限
			DcmUser findUser = this.userOrgService.getUserByLoginNameInCache(user);
			List<DcmUserdomainrole> udrs = udrDmn.getByUserIdDomainCode(findUser.getId(), domainCode);
			if (udrs != null && udrs.size() > 0) {
				logger.info("当前用户属于此项目 [{}]，拥有下载权限。", domainCode);
				havePriv = Boolean.TRUE;
				return havePriv;
			}
			// 判断具体权限
			// 来源于其它空间域的文档
			if (!StringUtil.isNullOrEmpty(domainCode)) {
				if (findUser != null) {
					Long masks = privService.getMasksOfUserRes(findUser, domainCode, ResourceConstants.ResourceType.Res_File, resId);
					if (PrivilegeFactory.haveViewContent(masks) || PrivilegeFactory.haveDownload(masks)) {
						logger.info("判断资源的CE权限和系统扩展权限，拥有下载权限。", domainCode);
						havePriv = Boolean.TRUE;
						return havePriv;
					}
				}
			}
		} catch (Exception ex) {
			logger.error("可能在ce中没有找到相关资源，详情：[{}]", ex.getMessage());
			ex.printStackTrace();
		}

		return havePriv;
	}*/
	/*@Override
	public boolean isHaveDownloadPriv(String user, String resId, String domainCode) {
		
		boolean havePriv = Boolean.FALSE;

		DcmPersonalmaterial pm = this.personalMaterialDmn.findUniqueByProperties(new String[] { "resId", "creator" },
				new Object[] { resId, user });
		if (pm != null) {
			logger.info("属于个人资料，拥有所有权。");
			havePriv = Boolean.TRUE;
			return havePriv;
		}

		DcmShare share = (DcmShare) this.shareDmn.getUniqueShareInfoByResIdAndTarget(resId, user);
		if (share != null) {
			logger.info("资料被共享给当前用户，拥有下载权限。");
			havePriv = Boolean.TRUE;
			return havePriv;
		} 

		// 判断具体权限
		// 来源于其它空间域的文档
		if (!StringUtil.isNullOrEmpty(domainCode)) {
			DcmUser findUser = this.userDmn.findByLoginName(user);
			if (findUser != null) {
				Long masks = privService.getMasksOfUserRes(findUser, domainCode,
						ResourceConstants.ResourceType.Res_File, resId);
				if (PrivilegeFactory.haveViewContent(masks) || PrivilegeFactory.haveDownload(masks)) {
					logger.info("判断资源的CE权限和系统扩展权限，拥有下载权限。");
					havePriv = Boolean.TRUE;
					return havePriv;
				}
			}
		}
		return havePriv;
		
	}*/
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean isShare(String resId) {

		List<DcmShare> shares = (List<DcmShare>) this.shareDmn.getShareInfosByResId(resId);
		if (shares != null && shares.size() > 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isFavorite(String userId, String resId) {
		
		DcmSocialResource sr = this.socialResDmn.getByResIdAndCreator(resId, userId);

        return null == sr ? Boolean.FALSE : (0 == sr.getIsFavorite()) ? Boolean.FALSE : Boolean.TRUE;
	}
	
	@Override
	public List<DcmResourceType> getResourceTypes() {
		
		return this.resTypeDmn.find();
	}

	
	
}
