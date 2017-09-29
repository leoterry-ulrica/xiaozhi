package com.dist.bdf.facade.service.biz.priv.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dist.bdf.facade.service.UserOrgService;
import com.dist.bdf.facade.service.biz.domain.system.DcmPrivTemplateDmn;
import com.dist.bdf.facade.service.biz.priv.DocPrivService;
import com.dist.bdf.facade.service.uic.domain.DcmUserdomainroleDmn;
import com.dist.bdf.common.constants.ResourceConstants;
import com.dist.bdf.common.constants.RoleConstants;
import com.dist.bdf.common.constants.RoleConstants.RoleType;
import com.dist.bdf.manager.ecm.security.PrivilegeFactory;
import com.dist.bdf.model.entity.system.DcmOrganization;
import com.dist.bdf.model.entity.system.DcmPrivtemplate;
import com.dist.bdf.model.entity.system.DcmRole;
import com.dist.bdf.model.entity.system.DcmUser;
import com.dist.bdf.model.entity.system.DcmUserdomainrole;

@Component("DepartmentDocPrivService")
public class DepartmentDocPrivServiceImpl extends AbstractDocPrivServiceImpl implements DocPrivService {

	protected final static Logger LOG = LoggerFactory.getLogger(DepartmentDocPrivServiceImpl.class);
	
	@Autowired
	private DcmPrivTemplateDmn privTemplateDmn;
	@Autowired
	private DcmUserdomainroleDmn udrDmn;
	@Autowired
	private UserOrgService userOrgService;
	
	@Deprecated
	@Override
	public Long getMasks(DcmUser user, String versionSeriesId, String domainCode) {
		
		Long masks = PrivilegeFactory.Priv_None.getMask();
		LOG.debug(">>>检查是否被共享......");
		masks |= super.getSharePrivMasks(user, versionSeriesId);
		// 权限分类：
		// 1）根据当前用户在所中的具体角色的权限
		List<DcmRole> roles = this.udrDmn.getRolesOfUser(user.getId(), domainCode);
		if(roles != null && !roles.isEmpty()){
			LOG.info(">>>当前用户在所 [{}] 中的角色 [{}]......", domainCode, roles);
			masks = this.privTemplateDmn.getPrivilegeMasks(ResourceConstants.ResourceType.RES_PCK_DEPARTMENT, 1L, roles);
			
		}else{
			
			// 2）当前用户不属于所，但属于所在的院，根据用户在院中角色对所资料的权限
			DcmOrganization institute = this.userOrgService.getInstituteByUserSeqId(user.getId());
			List<DcmRole> rolesOfInstitute = this.udrDmn.getRolesOfUser(user.getId(), institute.getOrgCode());
			LOG.info(">>>当前用户在院 [{}] 中的角色 [{}]......", institute.getOrgCode(), rolesOfInstitute);
			if(rolesOfInstitute != null && !rolesOfInstitute.isEmpty()) {
				masks = this.privTemplateDmn.getPrivilegeMasks(ResourceConstants.ResourceType.RES_PCK_DEPARTMENT, 1L, rolesOfInstitute);
			}
			
		}
		
		return masks;
	}

	@Deprecated
	@Override
	public Long getMasks(DcmUser user, String versionSeriesId, String domainCode, String parentId) {
		
		Long masks = PrivilegeFactory.Priv_None.getMask();
		LOG.debug(">>>检查是否被共享......");
		
		if(!parentId.equalsIgnoreCase("-1")){
			masks |= super.getSharePrivMasks(user, parentId);
			if(masks.equals(PrivilegeFactory.Priv_None)){
				masks |= super.getSharePrivMasks(user, versionSeriesId);
			}else{
				LOG.info(">>>资源所在根文件夹已被共享......");
			}
		}else {
			masks |= super.getSharePrivMasks(user, versionSeriesId);
		}
		
		// 权限分类：
		// 1）根据当前用户在所中的具体角色的权限
		List<DcmRole> roles = this.udrDmn.getRolesOfUser(user.getId(), domainCode);
		if(roles != null && !roles.isEmpty()){
			LOG.info(">>>当前用户在所 [{}] 中的角色 [{}]......", domainCode, roles);
			masks = this.privTemplateDmn.getPrivilegeMasks(ResourceConstants.ResourceType.RES_PCK_DEPARTMENT, 1L, roles);
			
		}else{
			
			// 2）当前用户不属于所，但属于所在的院，根据用户在院中角色对所资料的权限
			DcmOrganization institute = this.userOrgService.getInstituteByUserSeqId(user.getId());
			List<DcmRole> rolesOfInstitute = this.udrDmn.getRolesOfUser(user.getId(), institute.getOrgCode());
			LOG.info(">>>当前用户在院 [{}] 中的角色 [{}]......", institute.getOrgCode(), rolesOfInstitute);
			if(rolesOfInstitute != null && !rolesOfInstitute.isEmpty()) {
				masks = this.privTemplateDmn.getPrivilegeMasks(ResourceConstants.ResourceType.RES_PCK_DEPARTMENT, 1L, rolesOfInstitute);
			}
			
		}
		
		return masks;
	}
	
	@Override
	public Long getMasks(Long channelType, DcmUser user, String versionSeriesId, String domainCode, String parentId) {
		
		Long masks = PrivilegeFactory.Priv_None.getMask();
		LOG.debug(">>>检查是否被共享......");
		
		if(1 == channelType){
			
			LOG.info(">>>检查父文件夹 [{}]是否已被共享", parentId);
			
			masks |= super.getSharePrivMasks(user, parentId);
			/*if(masks.equals(PrivilegeFactory.Priv_None)){
				masks |= super.getSharePrivMasks(user, versionSeriesId);
			}else{
				logger.info(">>>资源所在根文件夹已被共享......");
			}*/
		}else {
			masks |= super.getSharePrivMasks(user, versionSeriesId);
		}
		
		// 权限分类：
		// 1）根据当前用户在所中的具体角色的权限
		List<DcmRole> roles = this.udrDmn.getRolesOfUser(user.getId(), domainCode);
		if(roles != null && !roles.isEmpty()){
			LOG.info(">>>当前用户在所 [{}] 中的角色 [{}]......", domainCode, roles);
			masks |= this.privTemplateDmn.getPrivilegeMasks(ResourceConstants.ResourceType.RES_PCK_DEPARTMENT, 1L, roles);
			
		}else{
			
			// 2）当前用户不属于所，但属于所在的院，根据用户在院中角色对所资料的权限
			DcmOrganization institute = this.userOrgService.getInstituteByUserSeqId(user.getId());
			List<DcmRole> rolesOfInstitute = this.udrDmn.getRolesOfUser(user.getId(), institute.getOrgCode());
			LOG.info(">>>当前用户在院 [{}] 中的角色 [{}]......", institute.getOrgCode(), rolesOfInstitute);
			if(rolesOfInstitute != null && !rolesOfInstitute.isEmpty()) {
				masks |= this.privTemplateDmn.getPrivilegeMasks(ResourceConstants.ResourceType.RES_PCK_DEPARTMENT, 1L, rolesOfInstitute);
			}
		}
		LOG.info(">>>权限mask值："+masks);
		return masks;
	}

	@Override
	public Long getMasksByUserCode(Long channelType, DcmUser user, String versionSeriesId, String domainCode,
			String parentId) {
		Long masks = PrivilegeFactory.Priv_None.getMask();
		LOG.debug(">>>检查是否被共享......");
		
		if(1 == channelType){
			
			LOG.info(">>>检查父文件夹 [{}]是否已被共享", parentId);
			
			masks |= super.getSharePrivMasks(user, parentId);
			
		}else {
			masks |= super.getSharePrivMasks(user, versionSeriesId);
		}
		
		// 权限分类：
		// 1）根据当前用户在所中的具体角色的权限
		List<DcmRole> roles = this.udrDmn.getRolesOfUser(user.getUserCode(), domainCode);
		if(roles != null && !roles.isEmpty()){
			LOG.info(">>>当前用户在所 [{}] 中的角色 [{}]......", domainCode, roles);
			masks |= this.privTemplateDmn.getPrivilegeMasks(ResourceConstants.ResourceType.RES_PCK_DEPARTMENT, 1L, roles);
			
		}else{
			
			// 2）当前用户不属于所，但属于所在的院，根据用户在院中角色对所资料的权限
			DcmOrganization institute = this.userOrgService.getInstituteByUserSeqId(user.getId());
			List<DcmRole> rolesOfInstitute = this.udrDmn.getRolesOfUser(user.getId(), institute.getOrgCode());
			LOG.info(">>>当前用户在院 [{}] 中的角色 [{}]......", institute.getOrgCode(), rolesOfInstitute);
			if(rolesOfInstitute != null && !rolesOfInstitute.isEmpty()) {
				masks |= this.privTemplateDmn.getPrivilegeMasks(ResourceConstants.ResourceType.RES_PCK_DEPARTMENT, 1L, rolesOfInstitute);
			}
		}
		LOG.info(">>>权限mask值："+masks);
		return masks;
	}

	@Override
	public Object getCodes(String realm, Long channelType, DcmUser user, String versionSeriesId, String domainCode,
			String parentId) {
		Set<String> privCodes = new HashSet<String>();
		String[] sharePrivCodes = null;
		LOG.debug(">>>检查是否被共享......");
		if (1 == channelType) {
			LOG.info(">>>检查父文件夹 [{}]是否已被共享", parentId);
			sharePrivCodes = super.getSharePrivCodes(user, parentId);
		} else {
			sharePrivCodes = super.getSharePrivCodes(user, versionSeriesId);
		}
		if (sharePrivCodes != null) {
			privCodes.addAll(Arrays.asList(sharePrivCodes));
		}

		// 权限分类：
		// 1）根据当前用户在所中的具体角色的权限
		List<DcmPrivtemplate> privTemps = null;
		List<DcmUserdomainrole> udrs = this.udrDmn.findByProperties(new String[] { "userCode", "domainCode" },
				new Object[] { user.getUserCode(), domainCode });
		if (udrs != null && !udrs.isEmpty()) {
			LOG.info(">>>当前用户在所 [{}] 中已设置有响应角色......", domainCode);
			for (DcmUserdomainrole udr : udrs) {
				privTemps = this.privTemplateDmn.getPrivileges(realm, ResourceConstants.ResourceType.RES_PCK_DEPARTMENT,
						1L, udr.getRoleCode());
				if (privTemps != null && !privTemps.isEmpty()) {
					for (DcmPrivtemplate pt : privTemps) {
						privCodes.add(pt.getPrivCode());
					}
				}
			}
		} else {
			LOG.warn(">>>当前用户在所中未被设置角色，默认为一般成员");
			// 先判断当前人是否在当前所
			if(this.userOrgService.existOrg2User(user.getId(), domainCode)) {
				privTemps = this.privTemplateDmn.getPrivileges(realm, ResourceConstants.ResourceType.RES_PCK_DEPARTMENT,
						1L, RoleConstants.RoleCode.R_D_MEMBER);
				if (privTemps != null && !privTemps.isEmpty()) {
					for (DcmPrivtemplate pt : privTemps) {
						privCodes.add(pt.getPrivCode());
					}
				}
			}
			// 2）当前用户不属于所，但属于所在的院，根据用户在院中角色对所资料的权限
	/*		DcmOrganization institute = this.userOrgService.getInstituteByUniqueName(user.getRealm());
			if (institute.getOrgCode().equals(domainCode)) {
				logger.info(">>>当前用户不属于当前院 [{}]，但属于院下的某个所，默认角色是院员 [{}] ......", institute.getOrgCode(),
						RoleConstants.RoleCode.R_MEMBER);
				// 院员对所
				privTemps = this.privTemplateDmn.getPrivileges(realm, ResourceConstants.ResourceType.RES_PCK_DEPARTMENT,
						1L, RoleConstants.RoleCode.R_MEMBER);
				if (privTemps != null && !privTemps.isEmpty()) {
					for (DcmPrivtemplate pt : privTemps) {
						privCodes.add(pt.getPrivCode());
					}
				}
			} else {
				logger.info(">>>当前用户 [{}] 不属于当前院 [{}] ......", user.getUserName(), domainCode);
			}*/
		}
		
		return privCodes;
	}
}
