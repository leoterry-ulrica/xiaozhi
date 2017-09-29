package com.dist.bdf.facade.service.biz.priv.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dist.bdf.facade.service.GroupService;
import com.dist.bdf.facade.service.biz.domain.system.DcmPrivTemplateDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmTaskMaterialDmn;
import com.dist.bdf.facade.service.biz.priv.DocPrivService;
import com.dist.bdf.facade.service.uic.domain.DcmUserdomainroleDmn;
import com.dist.bdf.common.constants.ResourceConstants;
import com.dist.bdf.common.constants.RoleConstants;
import com.dist.bdf.manager.ecm.security.PrivilegeFactory;
import com.dist.bdf.model.entity.system.DcmGroup;
import com.dist.bdf.model.entity.system.DcmPrivtemplate;
import com.dist.bdf.model.entity.system.DcmRole;
import com.dist.bdf.model.entity.system.DcmUser;
import com.dist.bdf.model.entity.system.DcmUserdomainrole;
import com.filenet.api.constants.AccessRight;

@Component("ProjectDocPrivService")
public class ProjectDocPrivServiceImpl extends AbstractDocPrivServiceImpl implements DocPrivService {

	protected final static Logger LOG = LoggerFactory.getLogger(ProjectDocPrivServiceImpl.class);
	
	@Autowired
	private DcmPrivTemplateDmn privTemplateDmn;
	@Autowired
	private DcmUserdomainroleDmn udrDmn;
	@Autowired
	private DcmTaskMaterialDmn taskMaterialDmn;
	/*@Autowired
	private DcmShareDmn shareDmn;*/
	@Autowired
	private GroupService groupService;
	
	@Deprecated
	@Override
	public Long getMasks(DcmUser user, String versionSeriesId, String domainCode) {
		
        Long masks = PrivilegeFactory.Priv_None.getMask();
        LOG.debug(">>>检查是否被共享......");
		masks |= super.getSharePrivMasks(user, versionSeriesId);
		
		// 文件的资源类型属于项目，权限分类：
		// 1）用户属于项目成员，根据用户在项目中具体的角色获取；
		List<DcmRole> roles = this.udrDmn.getRolesOfUser(user.getId(), domainCode);
		if(roles != null && !roles.isEmpty()){
			
			LOG.info(">>>用户属于项目成员，根据用户在项目中具体的角色获取。");	
			masks |= this.privTemplateDmn.getPrivilegeMasks(ResourceConstants.ResourceType.RES_PCK_PROJECT, 1L, roles);
			
			return masks;
			
		}else {
		
			// 2）资料被事项共享出去给项目外的用户，属于事项共享
			// 与一般共享一样，不再处理
			/*DcmShare share = (DcmShare) this.shareDmn.getUniqueShareInfoByResIdAndTarget(versionSeriesId, user.getLoginName());
			if (share != null) {
				logger.info("资源[{}] 已共享给当前用户[{}]，拥有共享的权限。", versionSeriesId, user.getUserName());	
				masks = PrivilegeFactory.getSharePrivMasks();
			} */
			
			// 3）目前用户不属于项目组成员，但是所领导或者院领导，文档被事项引用
			LOG.info(">>>处理事项中文档的权限......");
			if(this.taskMaterialDmn.isExist(versionSeriesId)){
				
				LOG.info(">>>文件 [{}] 已被事项所引用......", versionSeriesId);
				DcmGroup projectGroup = this.groupService.getGroupByGuid(domainCode);
				// 判断当前用户在项目对应的牵头部门的角色：所领导或者院领导
				List<DcmUserdomainrole> orgRoles = this.udrDmn.getByUserIdDomainCodeRoleCode(user.getId(), projectGroup.getOrgCode(), new String[] {RoleConstants.RoleCode.R_Department_Leader, RoleConstants.RoleCode.R_Institute_Leader});
				if(orgRoles.size() >0 ){
					
					masks |= AccessRight.VIEW_CONTENT_AS_INT | PrivilegeFactory.Priv_Download.getMask();
					
				}		
			}	
		}
		LOG.info(">>>权限mask值："+masks);
		return masks;
	}

	@Deprecated
	@Override
	public Long getMasks(DcmUser user, String versionSeriesId, String domainCode, String parentId) {

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

		// 文件的资源类型属于项目，权限分类：
		// 1）用户属于项目成员，根据用户在项目中具体的角色获取；
		List<DcmRole> roles = this.udrDmn.getRolesOfUser(user.getId(), domainCode);
		if (roles != null && !roles.isEmpty()) {

			LOG.info(">>>用户属于项目成员，根据用户在项目中具体的角色获取。");
			masks = this.privTemplateDmn.getPrivilegeMasks(ResourceConstants.ResourceType.RES_PCK_PROJECT, 1L, roles);

			return masks;

		} else {

			// 2）资料被事项共享出去给项目外的用户，属于事项共享
			// 与一般共享一样，不再处理
			/*
			 * DcmShare share = (DcmShare)
			 * this.shareDmn.getUniqueShareInfoByResIdAndTarget(versionSeriesId,
			 * user.getLoginName()); if (share != null) { logger.info(
			 * "资源[{}] 已共享给当前用户[{}]，拥有共享的权限。", versionSeriesId,
			 * user.getUserName()); masks =
			 * PrivilegeFactory.getSharePrivMasks(); }
			 */

			// 3）目前用户不属于项目组成员，但是所领导或者院领导，文档被事项引用
			// PS：aws版本并沒有事项处理模块，其它版本依然保留
			LOG.info(">>>处理事项中文档的权限......");
			if (this.taskMaterialDmn.isExist(versionSeriesId)) {

				LOG.info(">>>文件 [{}] 已被事项所引用......", versionSeriesId);
				DcmGroup projectGroup = this.groupService.getGroupByGuid(domainCode);
				// 判断当前用户在项目对应的牵头部门的角色：所领导或者院领导
				List<DcmUserdomainrole> orgRoles = this.udrDmn.getByUserIdDomainCodeRoleCode(user.getId(),
						projectGroup.getOrgCode(), new String[] { RoleConstants.RoleCode.R_Department_Leader,
								RoleConstants.RoleCode.R_Institute_Leader });
				if (orgRoles.size() > 0) {

					masks |= AccessRight.VIEW_CONTENT_AS_INT | PrivilegeFactory.Priv_Download.getMask();
				}
			}
		}

		return masks;
	}

	@Override
	public Long getMasks(Long channelType, DcmUser user, String versionSeriesId, String domainCode, String parentId) {
		
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

		// 文件的资源类型属于项目，权限分类：
		// 1）用户属于项目成员，根据用户在项目中具体的角色获取；
		List<DcmRole> roles = this.udrDmn.getRolesOfUser(user.getId(), domainCode);
		if (roles != null && !roles.isEmpty()) {

			LOG.info(">>>用户属于项目成员，根据用户在项目中具体的角色获取。");
			masks = this.privTemplateDmn.getPrivilegeMasks(ResourceConstants.ResourceType.RES_PCK_PROJECT, 1L, roles);

			return masks;

		} else {

			// 2）资料被事项共享出去给项目外的用户，属于事项共享
			// 与一般共享一样，不再处理
			/*
			 * DcmShare share = (DcmShare)
			 * this.shareDmn.getUniqueShareInfoByResIdAndTarget(versionSeriesId,
			 * user.getLoginName()); if (share != null) { logger.info(
			 * "资源[{}] 已共享给当前用户[{}]，拥有共享的权限。", versionSeriesId,
			 * user.getUserName()); masks =
			 * PrivilegeFactory.getSharePrivMasks(); }
			 */

			// 3）目前用户不属于项目组成员，但是所领导或者院领导，文档被事项引用
			// PS：aws版本并沒有事项处理模块，其它版本依然保留
			LOG.info(">>>处理事项中文档的权限......");
			if (this.taskMaterialDmn.isExist(versionSeriesId)) {

				LOG.info(">>>文件 [{}] 已被事项所引用......", versionSeriesId);
				DcmGroup projectGroup = this.groupService.getGroupByGuid(domainCode);
				// 判断当前用户在项目对应的牵头部门的角色：所领导或者院领导
				List<DcmUserdomainrole> orgRoles = this.udrDmn.getByUserIdDomainCodeRoleCode(user.getId(),
						projectGroup.getOrgCode(), new String[] { RoleConstants.RoleCode.R_Department_Leader,
								RoleConstants.RoleCode.R_Institute_Leader });
				if (orgRoles.size() > 0) {

					masks |= AccessRight.VIEW_CONTENT_AS_INT | PrivilegeFactory.Priv_Download.getMask();
				}
			}
		}
		return masks;
	}
	@Override
	public Long getMasksByUserCode(Long channelType, DcmUser user, String versionSeriesId, String domainCode, String parentId) {
		
		Long masks = PrivilegeFactory.Priv_None.getMask();
		LOG.debug(">>>检查是否被共享......");

		if (1 == channelType) {

			LOG.info(">>>检查父文件夹 [{}]是否已被共享", parentId);
			masks |= super.getSharePrivMasks(user, parentId);
		} else {
			masks |= super.getSharePrivMasks(user, versionSeriesId);
		}

		// 文件的资源类型属于项目，权限分类：
		// 1）用户属于项目成员，根据用户在项目中具体的角色获取；
		List<DcmRole> roles = this.udrDmn.getRolesOfUser(user.getUserCode(), domainCode);
		if (roles != null && !roles.isEmpty()) {

			LOG.info(">>>用户属于项目成员，根据用户在项目中具体的角色获取。");
			masks = this.privTemplateDmn.getPrivilegeMasks(ResourceConstants.ResourceType.RES_PCK_PROJECT, 1L, roles);

			return masks;

		} else {

			// 2）资料被事项共享出去给项目外的用户，属于事项共享
			// 与一般共享一样，不再处理
			/*
			 * DcmShare share = (DcmShare)
			 * this.shareDmn.getUniqueShareInfoByResIdAndTarget(versionSeriesId,
			 * user.getLoginName()); if (share != null) { logger.info(
			 * "资源[{}] 已共享给当前用户[{}]，拥有共享的权限。", versionSeriesId,
			 * user.getUserName()); masks =
			 * PrivilegeFactory.getSharePrivMasks(); }
			 */

			// 3）目前用户不属于项目组成员，但是所领导或者院领导，文档被事项引用
			// PS：aws版本并沒有事项处理模块，其它版本依然保留
			LOG.info(">>>处理事项中文档的权限......");
			if (this.taskMaterialDmn.isExist(versionSeriesId)) {

				LOG.info(">>>文件 [{}] 已被事项所引用......", versionSeriesId);
				DcmGroup projectGroup = this.groupService.getGroupByGuid(domainCode);
				// 判断当前用户在项目对应的牵头部门的角色：所领导或者院领导
				List<DcmUserdomainrole> orgRoles = this.udrDmn.getByUserIdDomainCodeRoleCode(user.getId(),
						projectGroup.getOrgCode(), new String[] { RoleConstants.RoleCode.R_Department_Leader,
								RoleConstants.RoleCode.R_Institute_Leader });
				if (orgRoles.size() > 0) {
					masks |= AccessRight.VIEW_CONTENT_AS_INT | PrivilegeFactory.Priv_Download.getMask();
				}
			}
		}
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
		// 文件的资源类型属于项目，权限分类：
		// 1）用户属于项目成员，根据用户在项目中具体的角色获取；
		List<DcmPrivtemplate> privTemps = null;
		List<DcmUserdomainrole> udrs = this.udrDmn.findByProperties(new String[] { "userCode", "domainCode" },
				new Object[] { user.getUserCode(), domainCode });
		if (udrs != null && !udrs.isEmpty()) {

			LOG.info(">>>用户属于项目成员，根据用户在项目中具体的角色获取。");
			for (DcmUserdomainrole udr : udrs) {
				privTemps = this.privTemplateDmn.getPrivileges(realm, ResourceConstants.ResourceType.RES_PCK_PROJECT,
						1L, udr.getRoleCode());
				if (privTemps != null && !privTemps.isEmpty()) {
					for (DcmPrivtemplate pt : privTemps) {
						privCodes.add(pt.getPrivCode());
					}
				}
			}
		}
		return privCodes;
	}
}
