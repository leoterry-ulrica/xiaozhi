package com.dist.bdf.facade.service.biz.priv.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dist.bdf.common.constants.ResourceConstants;
import com.dist.bdf.common.constants.RoleConstants;
import com.dist.bdf.facade.service.biz.domain.system.DcmPrivTemplateDmn;
import com.dist.bdf.facade.service.biz.priv.DocPrivService;
import com.dist.bdf.manager.ecm.security.PrivilegeFactory;
import com.dist.bdf.model.entity.system.DcmPrivtemplate;
import com.dist.bdf.model.entity.system.DcmUser;

@Component("PersonDocPrivService")
public class PersonDocPrivServiceImpl extends AbstractDocPrivServiceImpl implements DocPrivService {

	protected final static Logger LOG = LoggerFactory.getLogger(PersonDocPrivServiceImpl.class);
	@Autowired
	private DcmPrivTemplateDmn privTemplateDmn;
/*	@Autowired
	private DcmShareDmn shareDmn;
	*/
	@Deprecated
	@Override
	public Long getMasks(DcmUser user, String versionSeriesId, String domainCode) {

		// 文件的资源类型属于个人，权限分类：个人资料和被共享
		if (user.getLoginName().equals(domainCode) || user.getUserCode().equals(domainCode)) {

			LOG.info(">>>资源[{}] 属于个人[{}]资料，拥有所有权。", versionSeriesId, user.getUserName());

			return PrivilegeFactory.All_PRIVS;
		} else {

			// 判断是否直接共享给当前用户
			/*
			 * DcmShare share = (DcmShare)
			 * this.shareDmn.getUniqueShareInfoByResIdAndTarget(versionSeriesId,
			 * user.getLoginName()); if (share != null) { logger.info(
			 * "资源[{}] 已共享给当前用户[{}]，拥有共享的权限。", versionSeriesId,
			 * user.getUserName()); return PrivilegeFactory.getSharePrivMasks();
			 * 
			 * }
			 */
			return super.getSharePrivMasks(user, versionSeriesId);
		}
		/*
		 * logger.info("当前用户[{}] 对资源 [{}]没有权限。", user.getUserName(),
		 * versionSeriesId); return PrivilegeFactory.Priv_None;
		 */
	}
	
	@Deprecated
	@Override
	public Long getMasks(DcmUser user, String versionSeriesId, String domainCode, String parentId) {

		// 文件的资源类型属于个人，权限分类：个人资料和被共享
		if (user.getUserCode().equals(domainCode)) {

			LOG.info(">>>资源[{}] 属于个人[{}]资料，拥有所有权。", versionSeriesId, user.getUserName());

			return PrivilegeFactory.All_PRIVS;
		} else {
			Long masks = PrivilegeFactory.Priv_None.getMask();
			LOG.debug(">>>检查是否被共享......");

			if (!parentId.equalsIgnoreCase("-1")) {
				masks |= super.getSharePrivMasks(user, parentId);
				if (masks.equals(PrivilegeFactory.Priv_None)) {
					masks |= super.getSharePrivMasks(user, versionSeriesId);
				} else {
					LOG.info(">>>资源所在根文件夹已被共享......");
				}
			} else {
				masks |= super.getSharePrivMasks(user, versionSeriesId);
			}
			return masks;
		}
	}

	@Override
	public Long getMasks(Long channelType, DcmUser user, String versionSeriesId, String domainCode, String parentId) {
		
		// 文件的资源类型属于个人，权限分类：个人资料和被共享
		if (user.getUserCode().equals(domainCode) || user.getLoginName().equals(domainCode)) {

			LOG.info(">>>资源[{}] 属于个人[{}]资料，拥有所有权。", versionSeriesId, user.getUserName());

			return PrivilegeFactory.All_PRIVS;
		} else {
			Long masks = PrivilegeFactory.Priv_None.getMask();
			LOG.debug(">>>检查是否被共享......");

			if (1 == channelType) {

				LOG.info(">>>检查父文件夹 [{}]是否已被共享", parentId);

				masks |= super.getSharePrivMasks(user, parentId);
				/*
				 * if(masks.equals(PrivilegeFactory.Priv_None)){ masks |=
				 * super.getSharePrivMasks(user, versionSeriesId); }else{
				 * logger.info(">>>资源所在根文件夹已被共享......"); }
				 */
			} else {
				masks |= super.getSharePrivMasks(user, versionSeriesId);
			}
			return masks;
		}
	}

	@Override
	public Long getMasksByUserCode(Long channelType, DcmUser user, String versionSeriesId, String domainCode,
			String parentId) {
		return this.getMasks(channelType, user, versionSeriesId, domainCode, parentId);
	}

	@Override
	public Set<String> getCodes(String realm, Long channelType, DcmUser user, String versionSeriesId, String domainCode,
			String parentId) {
		// 文件的资源类型属于个人，权限分类：个人资料和被共享
		List<DcmPrivtemplate> privTemps = null;
		Set<String> privCodes = new HashSet<String>();
		if (user.getUserCode().equals(domainCode) || user.getLoginName().equals(domainCode)) {

			LOG.info(">>>资源[{}] 属于个人[{}]资料，拥有所有权。", versionSeriesId, user.getUserName());
			privTemps = this.privTemplateDmn.getPrivileges(realm, ResourceConstants.ResourceType.RES_PCK_PERSON,
					1L, RoleConstants.RoleCode.R_OWNER);
			if (privTemps != null && !privTemps.isEmpty()) {
				for (DcmPrivtemplate pt : privTemps) {
					privCodes.add(pt.getPrivCode());
				}
			}
			return privCodes; // new HashSet<>(Arrays.asList(PrivilegeFactory.getOwnerPrivCodes()));
		} else {
			LOG.debug(">>>检查是否被共享......");
			String[] sharePrivCodes = null;
			if (1 == channelType) {
				LOG.info(">>>检查父文件夹 [{}]是否已被共享", parentId);
				sharePrivCodes = super.getSharePrivCodes(user, parentId);
			} else {
				sharePrivCodes = super.getSharePrivCodes(user, versionSeriesId);
			}
			if (sharePrivCodes != null) {
				return new HashSet<>(Arrays.asList(sharePrivCodes));
			}
		}
		return null;
	}

}
