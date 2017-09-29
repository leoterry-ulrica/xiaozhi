
package com.dist.bdf.facade.service.biz.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.security.DesEncryptDap;
import com.dist.bdf.model.dto.sga.UserResponseDTO;
import com.dist.bdf.model.dto.system.UserDomainRoleDTO;
import com.dist.bdf.model.dto.system.UserResPrivRequestDTO;
import com.dist.bdf.model.dto.system.priv.PrivTemplateDTO;
import com.dist.bdf.model.entity.system.DcmGroup;
import com.dist.bdf.model.entity.system.DcmOrganization;
import com.dist.bdf.model.entity.system.DcmPrivilege;
import com.dist.bdf.model.entity.system.DcmPrivtemplate;
import com.dist.bdf.model.entity.system.DcmRole;
import com.dist.bdf.model.entity.system.DcmUser;
import com.dist.bdf.model.entity.system.DcmUserdomainrole;
import com.dist.bdf.facade.service.GroupService;
import com.dist.bdf.facade.service.LdapService;
import com.dist.bdf.facade.service.PrivilegeService;
import com.dist.bdf.facade.service.UserOrgService;
import com.dist.bdf.facade.service.biz.domain.dcm.CEAccessDmn;
import com.dist.bdf.facade.service.biz.domain.dcm.CEDocAccessDmn;
import com.dist.bdf.facade.service.biz.domain.dcm.CEFolderAccessDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmPrivTemplateDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmPrivilegeDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmTaskMaterialDmn;
import com.dist.bdf.facade.service.biz.factory.DocPrivFactory;
import com.dist.bdf.facade.service.biz.priv.DocPrivService;
import com.dist.bdf.facade.service.sga.SgaUserService;
import com.dist.bdf.facade.service.uic.domain.DcmRoleDmn;
import com.dist.bdf.facade.service.uic.domain.DcmUserdomainroleDmn;
import com.dist.bdf.common.conf.ecm.ECMConf;
import com.dist.bdf.common.conf.ecm.ExtPropertyMaterialConf;
import com.dist.bdf.common.constants.DomainType;
import com.dist.bdf.common.constants.ResourceConstants;
import com.dist.bdf.common.constants.RoleConstants;
import com.dist.bdf.manager.ecm.security.ConnectionService;
import com.dist.bdf.manager.ecm.security.PrivilegeFactory;
import com.dist.bdf.manager.ecm.utils.DocumentUtil;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.constants.AccessRight;
import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.ClassNames;
import com.filenet.api.constants.PermissionSource;
import com.filenet.api.core.Document;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.security.AccessPermission;
import com.ibm.ecm.util.p8.P8Connection;

/**
 * @author weifj
 * @version 1.0，2016/03/01，weifj，创建服务实现类
 * * 继承权限与自身获得的权限不能合并记录，否则，当继承权限被取消时，无法知道自身到底获得了什么权限。
 * CE相关定义：
 *   Access Type:访问类型
 *     DENY - 禁止，此方式优先级更高，即使再次使用ALLOW方式进行了授权也会被屏蔽
 *     ALLOW - 允许
 *   Inheritable Depth：继承深度，本应用中只使用0（不继承）和-1（所有子级均继承）
 *     0 - This object only (no inheritance).（仅此对象）
 *     1 - This object and immediate children only.（此对象和直接子代） Other positive values greater than 1 indicate the allowed depth of inheritance (for example, 2 means this object, its immediate children and grandchildren only can inherit the permission).
 *    -1 - This object and all children (infinite levels deep).（此对象和所有子代）
 *    -2 - All children (infinite levels deep) but not the object itself.（所有子代，但不是此对象）
 *    -3 - Immediate children only but not this object. （直接子代，但不是此对象）
 *    Other negative values less than -3 indicate the allowed depth of inheritance (for example, -4 means only immediate children and grandchildren of the object inherit the permission, but not the object itself).
 */
@Service("PrivService")
@Transactional(propagation = Propagation.REQUIRED)
public class PrivilegeServiceImpl implements PrivilegeService {

	protected final Logger logger = (Logger) LoggerFactory.getLogger(getClass());
	//本应用所支持的CE权限
	/*public static final int SUPPORTED_CE_ACCESS_RIGHT = AccessRight.VIEW_CONTENT_AS_INT | AccessRight.READ_AS_INT | AccessRight.DELETE_AS_INT
				| AccessRight.WRITE_AS_INT;*/
	//本应用授权时默认的CE权限
	public static final int DEFAULT_CE_ACCESS_RIGHT = 0;

	@Autowired
	private DcmUserdomainroleDmn udrDmn;
	@Autowired
	private DcmRoleDmn roleDmn;
	@Autowired
	private DcmPrivTemplateDmn privTemplateDmn;
	@Autowired
	private DcmPrivilegeDmn privDmn;
	@Autowired
	private LdapService ldapService;
	//@Autowired，不使用这个标签，此实例在业务过程中产生
	private CEAccessDmn derivedCEAccessDmn;
	@Autowired
	private CEFolderAccessDmn ceFolderAccessDmn;
	@Autowired
	private CEDocAccessDmn ceDocAccessDmn;
	@Autowired
	private ConnectionService connService;
	@Autowired
	private UserOrgService userOrgService;
	@Autowired
	private DcmTaskMaterialDmn taskMaterialDmn;
	@Autowired
	private GroupService groupService;
	@Autowired
	private DocPrivFactory docPrivFactory;
	@Autowired
	private ECMConf ecmConf;
	@Autowired
	private ExtPropertyMaterialConf extPropMaterialConf;
	@Autowired
	private DocumentUtil documentUtil;
	@Autowired
	private SgaUserService sgaUserService;
	@Autowired
	private Mapper dozerMapper;

	private CEAccessDmn getCEAccessDmnInstance(String ceResType) {

		if (ClassNames.DOCUMENT.equalsIgnoreCase(ceResType)) {

			return this.ceDocAccessDmn;

		} else if (ClassNames.FOLDER.equalsIgnoreCase(ceResType)) {

			return this.ceFolderAccessDmn;
		}else{
			// 缺省值为文档类
			return this.ceDocAccessDmn;
		}

	}

	@Override
	public Map<Long, Long> addUserToProjectGroup(UserDomainRoleDTO udrDto) {
		udrDto.setDomainType(DomainType.PROJECT);// 设置空间域类型编码
		return this.addUserToDomain(udrDto);
	}
	
	@Override
	public Object addUserToProjectGroupEx(UserDomainRoleDTO udrDto) {
		udrDto.setDomainType(DomainType.PROJECT);// 设置空间域类型编码
		return this.addUserToDomainEx(udrDto);
	}

	@Override
	public void removeUserFromProjectGroup(UserDomainRoleDTO udrDto) {
		this.removeUserFromDomain(udrDto);
	}
	
	@Override
	public void removeUserFromProjectGroupEx(UserDomainRoleDTO udrDto) {
		
		Map<String, Integer> users = udrDto.getUsers();

		for (Entry<String, Integer> entry : users.entrySet()) {
			this.udrDmn.removeUserFromDomain(entry.getKey(), udrDto.getDomainCode());
		}
	}

	/**
	 * 添加用户到空间域
	 * @param udrDto
	 */
	private Map<Long, Long> addUserToDomain(UserDomainRoleDTO udrDto) {

		Map<Long, Long> userJoininTimeMap = new HashMap<Long, Long>();
		
		Long[] userIds = udrDto.getUserIds();
		for (int i = 0; i < userIds.length; i++) {

			DcmUserdomainrole udr = this.udrDmn.addUserToDomain(userIds[i], udrDto.getDomainType(), udrDto.getDomainCode(),
					udrDto.getRoleCode());
			userJoininTimeMap.put(udr.getUserId(), udr.getCreateTime().getTime());
		}

		return userJoininTimeMap;
	}
	
	private Map<String, Long> addUserToDomainEx(UserDomainRoleDTO udrDto) {

		// 人员加入的时间结果
		Map<String, Long> usercodeJoininTimeMap = new HashMap<String, Long>();
		DcmUserdomainrole udr = null;
		Map<String, Integer> userMap = udrDto.getUsers();
		for(Entry<String, Integer> keyvalue : userMap.entrySet()) { 
			udr = this.udrDmn.getByDomainCodeUserCodeRoleCode(udrDto.getDomainCode(), keyvalue.getKey(), udrDto.getRoleCode());
			if(udr != null) {
				continue;
			}
			udr = new DcmUserdomainrole();
			udr.setCreateTime(new Date());
			udr.setUserType(keyvalue.getValue());
			udr.setUserCode(keyvalue.getKey());
			udr.setDomainCode(udrDto.getDomainCode());
			udr.setDomainType(udrDto.getDomainType());
			udr.setRoleCode(udrDto.getRoleCode());
			udr.setLastTime(new Date());
			if(0 == keyvalue.getValue()) {
				// 内部用户
				DcmUser innerUser = this.userOrgService.getUserEntityByCode(keyvalue.getKey());
				udr.setUserId(innerUser.getId());
			}
			 this.udrDmn.add(udr);
			 usercodeJoininTimeMap.put(udr.getUserCode(), udr.getCreateTime().getTime());
		}
		return usercodeJoininTimeMap;
	}

	/**
	 * 从空间域删除人
	 * @param udrDto
	 */
	private void removeUserFromDomain(UserDomainRoleDTO udrDto) {

		Long[] userIds = udrDto.getUserIds();

		for (Long userId : userIds) {
			this.udrDmn.removeUserFromDomain(userId, udrDto.getDomainCode());
		}
	}

	//@Override
/*	public Long getMasksOfRoleRes(Long roleId, String resType, String resId) {

		Long masks = 0L;

		DcmRole role = roleDmn.loadById(roleId);
		// 首先获取CE的基础权限（文件/文件夹权限）
		masks = getPermissionMasksByRoleRes(role, resType, resId);
		// 然后获取扩展权限
		List<DcmPrivilege> privs = privTemplateDmn.getPrivilegeList(resType, ResourceStatus.Active, role.getRoleCode());
		// 合并权限
		if (privs.size() > 0) {
			for (DcmPrivilege priv : privs) {
				masks |= priv.getPrivValue();
			}
		}

		return masks;
	}*/
	
	public Long getMasksOfRoleRes(DcmRole role, DcmUser user, String resType, String resId) {

		this.connService.initialize();
		Long masks = 0L;

		// 首先获取CE的基础权限（文件/文件夹权限）
		masks = getPermissionMasksByUserRes(this.connService.getDefaultOS(), user, ResourceConstants.ResourceType.RES_FILE, resId);
		// 然后获取扩展权限
		List<DcmPrivilege> privs = privTemplateDmn.getPrivilegeList(resType, ResourceConstants.ResourceStatus.ACTIVE, role.getRoleCode());
		// 合并权限
		if (privs.size() > 0) {
			for (DcmPrivilege priv : privs) {
				masks |= priv.getPrivValue();
			}
		}

		return masks;
	}


	
	@Override
	public Long getMasksOfUserRes(DcmUser user, String domainCode, String resType, String resId) {
		
		return this.getDocMasksOfUserRes(user.getId(), domainCode, resType, resId);
	/*	Long masks = 0L;
		List<DcmUserdomainrole> udrs = userDomainRoleDmn.getByUserIdDomainCode(user.getId(), domainCode);
		for (DcmUserdomainrole udr : udrs) {
			DcmRole role = roleDmn.getRoleByCode(udr.getRoleCode());
			masks |= this.getMasksOfRoleRes(role, user,resType, resId);
		}

		return masks;*/
		
	}
	@Deprecated
	@Override
	public Long getDocMasksOfUserRes(String userId, String resType, String resId) {
		
		return null;
	}

	/**
	 * 根据CE的资源类型和资源id获取操作权限
	 * @param ceResType，资源类型，来源于ClassNames，目前支持两种：文件夹或者文件
	 * @param resId，资源id
	 * @return 返回操作权限列表
	 */
	/*@Deprecated
	private AccessPermissionList getPermsByRes(String ceResType, String resId) {
		
		AccessPermissionList apl = null;
		if(ceResType.endsWith(ClassNames.FOLDER)){
			// 文件夹
			Folder folder = (Folder) this.folderAccessDmn.loadById(resId);
			apl = folder.get_Permissions();
			
		}else if(ceResType.endsWith(ClassNames.DOCUMENT)){
			// 文档类
			Document doc = (Document) this.docAccessDmn.loadById(resId);
	
			apl = doc.get_Permissions();
		}
	
		return apl;
	}*/

	@Override
	public Long getDirectPermissionMasksByRoleRes(Long roleId, String resType, String resId) {

		this.connService.initialize();
		Long masksAllow = PrivilegeFactory.Priv_None.getMask();
		Long masksDeny = PrivilegeFactory.Priv_None.getMask();
		AccessPermissionList apl = this.getCEAccessDmnInstance(resType).getPermissions(this.connService.getDefaultOS(), resId);//getPermsByRes(ceResType, resId);

		DcmRole role = this.roleDmn.loadById(roleId);
		String roleDN = this.ldapService.getDistinguishedName(role.getRoleCode());
		AccessPermission ap = null;
		//对所有许可（Permission）进行循环已确定role是否已经获得了授权
		for (Object object : apl) {
			ap = (AccessPermission) object;
			String granteeName = ap.get_GranteeName();
			//如果需要返回实际权限，则需要合并各种PermissionSource
			if (granteeName.equalsIgnoreCase(roleDN)
					&& ap.get_PermissionSource().getValue() == PermissionSource.SOURCE_DIRECT_AS_INT) {

				if (ap.get_AccessType() == AccessType.ALLOW) {
					// 允许类型
					masksAllow |= ap.get_AccessMask().longValue();

				} else {
					// 拒绝类型
					masksDeny |= ap.get_AccessMask().longValue();

				}
			}
		}
		this.connService.release();
		// 拒绝类型优先
		return masksAllow & ~masksDeny;

	}

	public Long getPermissionMasksByRoleRes(ObjectStore os, Long roleId, String resType, String resId) {

		Long masks = PrivilegeFactory.Priv_None.getMask();

		// 获取ldap的dn
		DcmRole role = this.roleDmn.loadById(roleId);
		String roleDN = this.ldapService.getDistinguishedName(role.getRoleCode());

		this.derivedCEAccessDmn = this.getCEAccessDmnInstance(resType);
		if (null == this.derivedCEAccessDmn)
			return masks;

		masks = this.derivedCEAccessDmn.getValidAccessMasks(os, roleDN, resId);

		return masks;
	}

	public Long getPermissionMasksByRoleRes(ObjectStore os, DcmRole role, String resType, String resId) {

		Long masks = PrivilegeFactory.Priv_None.getMask();

		// 获取ldap的dn
		String roleDN = this.ldapService.getDistinguishedName(role.getRoleCode());

		this.derivedCEAccessDmn = this.getCEAccessDmnInstance(resType);
		if (null == this.derivedCEAccessDmn)
			return masks;

		masks = this.derivedCEAccessDmn.getValidAccessMasks(os, roleDN, resId);

		return masks;
	}

	/**
	 * 
	 * @param os
	 * @param user
	 * @param resType 默认为文档类型
	 * @param resId
	 * @return
	 */
	private Long getPermissionMasksByUserRes(ObjectStore os, DcmUser user, String resType, String resId) {
		
		Long masks = PrivilegeFactory.Priv_None.getMask();

		List<String> grentees = new ArrayList<String>();
		// 获取用户在ldap的dn
		String userDn = this.ldapService.getDistinguishedName(user.getLoginName());
		logger.info(">>>获取用户dn：[{}]", userDn);
		grentees.add(userDn);
		// 判断所在组
		List<DcmOrganization> orgs = this.userOrgService.getOrgsByUserId(user.getId());
		logger.info(">>>准备获取用户所属机构：");
		if(orgs != null && orgs.size()>0){
			for(DcmOrganization org : orgs){
				String orgDn = this.ldapService.getDistinguishedName(org.getOrgName());
				logger.info(">>>机构名称：[{}]，dn：[{}]", org.getAlias(), orgDn);
				grentees.add(orgDn);
			}
		}

		this.derivedCEAccessDmn = this.getCEAccessDmnInstance(resType);
		if (null == this.derivedCEAccessDmn)
			return masks;

		masks = this.derivedCEAccessDmn.getValidAccessMasks(os, grentees, resId);

		return masks;
		
	}


	/**
	 * 此处获取的是直接权限，不包含继承权限
	 * @param roleId
	 * @param apl
	 * @return AccessPermission 如果不存在则返回null
	 */
	/*	private AccessPermission getDirectPermOfRoleFromPerms(Long roleId, AccessPermissionList apl) {
			DcmRole role = this.roleDmn.loadById(roleId);
			String roleDN = this.ldapService.getDistinguishedNameOfOrg(role.getRolecode());
			AccessPermission ap = null;
			//对所有许可（Permission）进行循环已确定role是否已经获得了授权
			for (Object object : apl) {
				ap = (AccessPermission) object;
				String granteeName = ap.get_GranteeName();
				//如果需要返回实际权限，则需要合并各种PermissionSource
				if (granteeName.equalsIgnoreCase(roleDN) && ap.get_AccessType() == AccessType.ALLOW
						&& ap.get_PermissionSource().getValue() == PermissionSource.SOURCE_DIRECT_AS_INT) {
					break;
				}
			}
			return ap;
		}*/

	//@SuppressWarnings({ "unchecked" })
	//当action=Action.NONE或者Action.INHERITABLE时，表示取消授权；
	//TODO 查看和验证权限时，需要合并自身的授权和从所有parent继承的授权；
	//@Override
	//@Deprecated
	/*public void authorize(Long roleId, int resType, String resId, Long masks) {
		logger.info(
				">>>>start:String authorize(Long roleId=[{}], Integer resType=[{}], String resId=[{}], Long masks=[{}])",
				roleId, resType, resId, masks);
		DcmRole role = this.roleDmn.loadById(roleId);
		//如果action排除是否继承位后=0，则表示取消授权
		boolean deauthorize = (masks & ~Action.INHERITABLE) == Action.NONE;
		//子级是否能够继承，如果未设置可继承，则为不继承，否则为所有子文件夹均继承；其他深度情况选择暂不处理
		boolean inheritable = (masks & Action.INHERITABLE) == Action.INHERITABLE;
		//如果action包含Print或者Download，则必须保证具有Read		//TODO 前端必须对此进行控制
	//		if ((masks & Action.PRINT) == Action.PRINT && (masks & Action.PRINT) == Action.DOWNLOAD)
	//			masks |= AccessRight.READ_AS_INT;
	//		Realm realm=Factory.Realm.fetchCurrent(connection, null);
	//		realm.findGroups();
		//先处理CE权限（action中低32位为CE权限），并去掉不支持的权限，合并默认的权限
	//		int ceMasks = (masks.intValue() & SUPPORTED_CE_ACCESS_RIGHT) | DEFAULT_CE_ACCESS_RIGHT;
		int ceMasks = masks.intValue() | DEFAULT_CE_ACCESS_RIGHT;
		String roleDN = this.ldapService.getDistinguishedNameOfOrg(role.getRolecode());
		Folder folder = (Folder) this.folderAccessDmn.loadById(resId);
		if (folder.equals(folderAccessDmn.getRootFolder())) {
			throw new BusinessException("授权失败：禁止对根文件夹进行授权");
		}
	
		AccessPermissionList apl = folder.get_Permissions();
		AccessPermission ap = getDirectPermOfRoleFromPerms(roleId, apl);
		if (deauthorize) {
			//如果找到了role的直接授权，则从列表中删除掉，否则不需要处理
			if (ap != null) {
				apl.remove(ap);
				//为folder设置权限
				folder.set_Permissions(apl);
				folder.save(RefreshMode.NO_REFRESH);
			} else {
				logger.info("role=[{}]对文件夹[{}]没有获得授权，无法取消", role.getRolename(), resId);
			}
		} else {
			//如果没有找到role的直接授权，则将新增的许可添加到许可列表中，否则就是对已有的许可进行修改
			if (ap == null) {
				ap = Factory.AccessPermission.createInstance();
				apl.add(ap);
				ap.set_GranteeName(roleDN);
			}
			//设置许可类型为允许
			ap.set_AccessType(AccessType.ALLOW);
			//设置继承深度
			if (inheritable)
				//-1:所有子级均继承
				ap.set_InheritableDepth(InheritableDepth.Object_And_AllChildren);
			else
				//0:不继承
				ap.set_InheritableDepth(InheritableDepth.Object_Only);
			
			//设置访问权限，注意role原有的授权将会被覆盖
			ap.set_AccessMask(ceMasks);
			//为folder设置权限
			folder.set_Permissions(apl);
			folder.save(RefreshMode.NO_REFRESH);
		}
		//再处理扩展权限
		//由于可能存在在其他工具和系统中修改permission的情况，因此本应用只处理扩展部分
		Long extendMasks = masks & Action.EXTENDS;
		if (deauthorize) {
			deauthorizeSelf(roleId, resType, resId, extendMasks);
		} else {
			if (extendMasks != Action.NONE) {
				authorizeSelf(roleId, resType, resId, extendMasks);
				//如果设置了继承，则为所有级别的Children授权。注意不能为Parent授权，因为对子文件夹具有权限并不意味着对父文件夹具有权限
				//如果同时为所有级别的Children进行授权，那么需要访问CE获得所有Children，写性能较低,但是不处理的话，在验证权限时如果找不到对应的授权，就必须递归查询Parent的授权，读性能较低
				//TODO 
			if (inheritable)
				authorizeChildren(roleId, resType, resId, extendMasks);
			}
		}
		logger.info("<<<<end.String authorize() return");
	}*/

	public void authorizeChildren(Long roleId, int resType, String resId, Long masks) {
		/*	List<String> children = this.resSearchable.findAllChildrenIds(resId);
			for (String child : children) {
				authorizeSelf(roleId, resType, child, masks);
			}*/
	}

	public void deauthorizeSelf(Long roleId, int resType, String resId, Long masks) {

		/*RoleAuth ra = this.roleAuthDmn.findDirectByRoleResId(roleId, resType, resId);
		if (ra != null)
			this.roleAuthDmn.removeById(ra.getId());*/

	}

	public void authorizeSelf(Long roleId, int resType, String resId, Long masks) {
		/*	RoleAuth ra = this.roleAuthDmn.findDirectByRoleResId(roleId, resType, resId);
			if (ra == null) {
				ra = new RoleAuth();
				ra.setRoleId(roleId);
				ra.setResType(resType);
				ra.setResId(resId);
				ra.setActions(masks);
				ra = this.roleAuthDmn.add(ra);
			} else {
				ra.setActions(masks);
				this.roleAuthDmn.modify(ra);
			}*/
	}
	/*@Override
	public void deauthorize(Long roleId, int resType, String resId, Long action) {
		// TODO Auto-generated method stub
		authorize(roleId, resType, resId, Action.NONE);
	}*/

	/*@Override
	public List<ActionVo> findActionsOfUserResMark(Long userId, int resType, String resId) {
		// TODO Auto-generated method stub
		return null;
	}
	*/
	/*	@Override
		public List<ActionVo> findActionsOfUserResId(Long userId, int resType, String resMark) {
	
			return null;
		}*/

	/*@Override
	public void setResSearcher(ResourceSearchable resSearchable) {
	
		this.resSearchable = resSearchable;
	}
	*/
	@Override
	public void authorize(int inheritableDepth, Long roleId, String resType, String resId, Long masks) {
		this.connService.initialize();
		// 获取角色实体
		DcmRole role = this.roleDmn.loadById(roleId);
		// 获取角色在ldap中的dn
		String roleDN = this.ldapService.getDistinguishedName(role.getRoleCode());
		// 根据资源类型，获取操作领域
		this.derivedCEAccessDmn = this.getCEAccessDmnInstance(resType);
		// 获取属于ce的权限mask值
		Long ceMasks = this.privDmn.getBasicPrivs(masks);
		// 获取属于系统扩展权限mask值
		Long extendedMasks = this.privDmn.getExtendedPrivs(masks);
		// 获取系统扩展权限的编码组
		@SuppressWarnings("unused")
		List<String> extendedPrivCodes = this.privDmn.getExtendedPrivCodes(extendedMasks);
		// 先处理ce权限
		this.derivedCEAccessDmn.authorize(this.connService.getDefaultOS(), inheritableDepth, roleDN, resId, ceMasks);
		// 
        this.connService.release();
	}

	@Override
	public void authorizeFromTemplate(int inheritableDepth, String sysRoleCode, String ldapGroupName, String resType,
			long resStatus, String resId) {

		this.connService.initialize();
		// 获取角色在ldap中的dn
		String ldapGroupDN = this.ldapService.getDistinguishedName(ldapGroupName);
		// 从权限模板中获取权限值
		Long allMasks = this.privTemplateDmn.getPrivilegeMasks(resType, resStatus, sysRoleCode);
		// 获取属于ce的权限mask值
		Long ceMasks = this.privDmn.getBasicPrivs(allMasks);
		System.out.println("==>ce的权限值：" + ceMasks);
		// 获取属于系统扩展权限mask值
		Long extendedMasks = this.privDmn.getExtendedPrivs(allMasks);
		System.out.println("==>系统扩展的权限值：" + extendedMasks);
		// 根据资源类型，获取操作领域
		this.derivedCEAccessDmn = this.getCEAccessDmnInstance(resType);
		// 设置ce权限
		this.derivedCEAccessDmn.authorize(this.connService.getDefaultOS(), inheritableDepth, ldapGroupDN, resId, ceMasks);
		System.out.println("==>完成ce授权。");
		this.connService.release();
	}

	/**
	 * 资源类型为个人类型的权限mask值
	 * @param user
	 * @param resId
	 * @param domainCode
	 * @return
	 */
	/*private Long getDocMasksInPerson(DcmUser user, String resId, String domainCode) {
		
		// 文件的资源类型属于个人，权限分类：个人资料和被共享
		if(user.getLoginName().equals(domainCode)) {

			logger.info("资源[{}] 属于个人[{}]资料，拥有所有权。", resId, user.getUserName());

			return PrivilegeFactory.All_PRIVS;
		} else {

			// 判断是否直接共享给当前用户
			DcmShare share = (DcmShare) this.shareDmn.getUniqueShareInfoByResIdAndTarget(resId, user.getLoginName());
			if (share != null) {
				logger.info("资源[{}] 已共享给当前用户[{}]，拥有共享的权限。", resId, user.getUserName());	
				return PrivilegeFactory.getSharePrivMasks();

			} 
		}
		logger.info("当前用户[{}] 对资源 [{}]没有权限。", user.getUserName(), resId);	
		return PrivilegeFactory.Priv_None;
		
	}*/
	/**
	 * 属于项目资料
	 * @param user
	 * @param resId
	 * @param domainCode
	 * @return
	 */
	/*private Long getDocMasksInProject(DcmUser user, String resId, String domainCode) {
		
		Long masks = PrivilegeFactory.Priv_None;
		
		// 文件的资源类型属于项目，权限分类：
		// 1）用户属于项目成员，根据用户在项目中具体的角色获取；
		List<DcmRole> roles = this.udrDmn.getRolesOfUser(user.getId(), domainCode);
		if(roles != null && !roles.isEmpty()){
			
			logger.info("用户属于项目成员，根据用户在项目中具体的角色获取。");	
			masks = this.privTemplateDmn.getPrivilegeMasks(ResourceConstants.ResourceType.Res_Pck_Project, 1L, roles);
			
			return masks;
			
		}else {
		
			// 2）资料被事项共享出去给项目外的用户，属于事项共享
			DcmShare share = (DcmShare) this.shareDmn.getUniqueShareInfoByResIdAndTarget(resId, user.getLoginName());
			if (share != null) {
				logger.info("资源[{}] 已共享给当前用户[{}]，拥有共享的权限。", resId, user.getUserName());	
				masks = PrivilegeFactory.getSharePrivMasks();
			} 
			
			// 3）目前用户不属于项目组成员，但是所领导或者院领导，文档被事项引用
			logger.info("处理事项中文档的权限......");
			if(this.taskMaterialDmn.isExist(resId)){
				
				logger.info("文件 [{}] 已被事项所引用......", resId);
				DcmGroup projectGroup = this.groupService.getGroupByCode(domainCode);
				// 判断当前用户在项目对应的牵头部门的角色：所领导或者院领导
				List<DcmUserdomainrole> orgRoles = this.udrDmn.getByUserIdDomainCodeRoleCode(user.getId(), projectGroup.getOrgCode(), new String[] {RoleConstants.RoleCode.R_Department_Leader, RoleConstants.RoleCode.R_Institute_Leader});
				if(orgRoles.size() >0 ){
					
					masks |= AccessRight.VIEW_CONTENT_AS_INT | PrivilegeFactory.Priv_Download;
					
				}		
			}	
		}
		
		return masks;
	}*/
	/**
	 * 属于所级资料
	 * @param user
	 * @param resId
	 * @param domainCode
	 * @return
	 */
	/*private Long getDocMasksInDepartment(DcmUser user, String resId, String domainCode) {
		
		Long masks = PrivilegeFactory.Priv_None;
		// 权限分类：
		// 1）根据当前用户在所中的具体角色的权限
		List<DcmRole> roles = this.udrDmn.getRolesOfUser(user.getId(), domainCode);
		if(roles != null && !roles.isEmpty()){
			logger.info("当前用户在所 [{}] 中的角色 [{}]......", domainCode, roles);
			masks = this.privTemplateDmn.getPrivilegeMasks(ResourceConstants.ResourceType.Res_Pck_Department, 1L, roles);
			
		}else{
			
			// 2）当前用户不属于所，但属于所在的院，根据用户在院中角色对所资料的权限
			DcmOrganization institute = this.userOrgService.getInstituteByUserLoginName(user.getLoginName());
			List<DcmRole> rolesOfInstitute = this.udrDmn.getRolesOfUser(user.getId(), institute.getOrgCode());
			logger.info("当前用户在院 [{}] 中的角色 [{}]......", institute.getOrgCode(), rolesOfInstitute);
			masks = this.privTemplateDmn.getPrivilegeMasks(ResourceConstants.ResourceType.Res_Pck_Department, 1L, rolesOfInstitute);
		}
		
		return masks;
	
	}*/
	/**
	 * 属于院级资料
	 * @param user
	 * @param resId
	 * @param domainCode
	 * @return
	 */
	/*private Long getDocMasksInInstitute(DcmUser user, String resId, String domainCode) {
		
		Long masks = PrivilegeFactory.Priv_None;
		// 权限分类：
		// 1）根据当前用户在院中的具体角色的权限
		List<DcmRole> roles = this.udrDmn.getRolesOfUser(user.getId(), domainCode);
		if(roles != null && !roles.isEmpty()){
			logger.info("当前用户在院 [{}] 中的角色 [{}]......", domainCode, roles);
			masks = this.privTemplateDmn.getPrivilegeMasks(ResourceConstants.ResourceType.Res_Pck_Institute, 1L, roles);
			
		}else{
			
			// 2）当前用户不属于院，但属于院下的某个所，判断用户所在的院是否是当前的院，如果是，则此时用户的角色是院员
			DcmOrganization institute = this.userOrgService.getInstituteByUserLoginName(user.getLoginName());
			if(institute.getOrgCode().equals(domainCode)){

				logger.info("当前用户不属于当前院 [{}]，但属于院下的某个所，默认角色是院员 [{}] ......", institute.getOrgCode(), RoleConstants.RoleCode.R_Institute_Member);
				masks = this.privTemplateDmn.getPrivilegeMasks(ResourceConstants.ResourceType.Res_Pck_Institute, 1L, RoleConstants.RoleCode.R_Institute_Member);
			}else{
				logger.info("当前用户 [{}] 不属于当前院 [{}] ......", user.getUserName(), domainCode);
			}
			
		}
		
		return masks;
	}*/
	
	@Override
	public Long getDocMasksOfUserRes(long userId, String domainCode, String resType, String resId) {

		Long masks = PrivilegeFactory.Priv_None.getMask();
		DcmUser user = this.userOrgService.getUserByIdInCache(userId);//.userDmn.loadById(userId);
		List<DcmUserdomainrole> udrs = this.udrDmn.getByUserIdDomainCode(userId, domainCode);
		if(udrs != null && !udrs.isEmpty()){
			
			boolean isDepartmentManager = false;
			for (DcmUserdomainrole udr : udrs) {
				
				DcmRole role = roleDmn.getRoleByCode(udr.getRoleCode());
				masks |= this.getMasksOfRoleRes(role,user, resType, resId);
				if(role.getRoleCode().equals(RoleConstants.RoleCode.R_Department_Manager)){
					logger.info("当前用户 [{}] 是所管......", user.getUserName());
					isDepartmentManager = true;
				}
			}
			if(!isDepartmentManager && ResourceConstants.ResourceType.RES_PCK_DEPARTMENT.equals(resType)) {
				logger.info("当前用户 [{}] 并非所管，而且资源类型为所级资料 [{}]......", user.getUserName(), resType);
				// 对于所级资料，所员只能浏览，下载，不能删除，不能添加版本
				masks = masks & ~AccessRight.DELETE_AS_INT & ~AccessRight.MAJOR_VERSION_AS_INT & ~AccessRight.MINOR_VERSION_AS_INT;
			}
		}else{
			// 说明当前用户跟当前空间域没有直接关联
			// 1）这个资料有可能是院级资料，但这个人只属于所
			if(ResourceConstants.ResourceType.RES_PCK_INSTITUTE.equals(resType)) {
				// 院级资料
				DcmOrganization institute = this.userOrgService.getInstituteByUserLoginName(user.getLoginName());
				if(institute != null && institute.getOrgCode().equals(domainCode)){
					// 说明属于当前院，而且角色默认为院员，应该获取到院员的权限
					DcmRole role = roleDmn.getRoleByCode(RoleConstants.RoleCode.R_Institute_Member);
					masks |= this.getMasksOfRoleRes(role,user, resType, resId);
					// 所用户对院级资料只能浏览，下载，不能删除，不能添加版本
					masks = masks & ~AccessRight.DELETE_AS_INT & ~AccessRight.MAJOR_VERSION_AS_INT & ~AccessRight.MINOR_VERSION_AS_INT;
				}
			}else if(ResourceConstants.ResourceType.RES_PCK_PROJECT.equals(resType)){
				logger.info("处理事项中文档的权限......");
				if(this.taskMaterialDmn.isExist(resId)){
					logger.info("文件 [{}] 已被事项所引用......", resId);
					DcmGroup projectGroup = this.groupService.getGroupByCode(domainCode);
					// 判断当前用户在项目对应的牵头部门的角色：所领导或者院领导
					List<DcmUserdomainrole> orgRoles = this.udrDmn.getByUserIdDomainCodeRoleCode(user.getId(), projectGroup.getOrgCode(), new String[] {RoleConstants.RoleCode.R_Department_Leader, RoleConstants.RoleCode.R_Institute_Leader});
					if(orgRoles.size() >0 ){
						
						masks |= AccessRight.VIEW_CONTENT_AS_INT | PrivilegeFactory.Priv_Download.getMask();
						
					}
				}
			}	
		}

		return masks;
	}
	

	@Override
	public List<String> getDocPrivCodes(String realm, String userId, String pwd, String versionSeriesId) {
		
		return PrivilegeFactory.getCodes(this.getDocPrivMasks(realm, userId, pwd, versionSeriesId));
	}
	@Override
	public List getDocPrivCodes(String realm, String userId, String versionSeriesId) {
	
		return PrivilegeFactory.getCodes(this.getDocPrivMasksByVID(realm, userId, versionSeriesId));
	}
	@Override
	public List<String> getDocPrivCodesSecurity(String realm, String userId, String pwd, String versionSeriesId) {
		
		return PrivilegeFactory.getCodes(this.getDocPrivMasks(realm, userId, DesEncryptDap.getInstance().decrypt(pwd), versionSeriesId));
	}
	
	@Override
	public Long getDocPrivMasks(String realm, String userId, String pwd, String versionSeriesId) {
		
		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(realm), userId, pwd);
        Document doc = this.documentUtil.loadByVersionSeriesId(p8conn.getObjectStore(), versionSeriesId);//loadByVersionSeriesId(this.connService.getDefaultOS(), vid);
		
		String domainCode = doc.getProperties().isPropertyPresent(this.extPropMaterialConf.getSpatialDomain())
				? doc.getProperties().getStringValue(this.extPropMaterialConf.getSpatialDomain()) : "";
		
		String resourceType = doc.getProperties().isPropertyPresent(this.extPropMaterialConf.getResourceType())
				? doc.getProperties().getStringValue(this.extPropMaterialConf.getResourceType()) : "";
				
	    logger.info("文档空间域 [{}]，资源类型 [{}] ", domainCode, resourceType);	
	    
	    Assert.hasLength(domainCode);
	    Assert.hasLength(resourceType);
	    
	    DcmUser user = this.userOrgService.getUserEntityByDN(userId);

		DocPrivService docPriv = docPrivFactory.getPrivService(resourceType);

		return docPriv.getMasks(user, versionSeriesId, domainCode);
		
	    //return this.getDocPrivMasks(userId, versionSeriesId, resourceType, domainCode);
	    
	}

	@Deprecated
	@Override
	public Long getDocPrivMasksByVID(String realm, String userId, String versionSeriesId) {

		try {
			P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
			Document doc = this.documentUtil.loadByVersionSeriesId(p8conn.getObjectStore(), versionSeriesId);// loadByVersionSeriesId(this.connService.getDefaultOS(),
																													// vid);

			String domainCode = doc.getProperties().isPropertyPresent(this.extPropMaterialConf.getSpatialDomain())
					? doc.getProperties().getStringValue(this.extPropMaterialConf.getSpatialDomain()) : "";

			String resourceType = doc.getProperties().isPropertyPresent(this.extPropMaterialConf.getResourceType()) 
					? doc.getProperties().getStringValue(this.extPropMaterialConf.getResourceType()) : "";

			logger.info("文档空间域 [{}]，资源类型 [{}] ", domainCode, resourceType);

			Assert.hasLength(domainCode);
			Assert.hasLength(resourceType);

			DcmUser user = this.userOrgService.getUserEntityByDN(userId);

			DocPrivService docPriv = docPrivFactory.getPrivService(resourceType);

			return docPriv.getMasks(user, versionSeriesId, domainCode);
		} catch (Exception ex) {
			ex.printStackTrace();
			return PrivilegeFactory.Priv_None.getMask();
		}

	}
    @Deprecated
	@Override
	public Long getDocPrivMasksByGUID(String realm, String userId, String id) {

		try {
			P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
			Document doc = this.documentUtil.loadById(p8conn.getObjectStore(), id);// loadByVersionSeriesId(this.connService.getDefaultOS(),
																							// vid);

			String domainCode = this.documentUtil.getDocResDomainCode(doc);// doc.getProperties().isPropertyPresent(this.extPropMaterialConf.getSpatialDomain())//
																				
			// ?
			// doc.getProperties().getStringValue(this.extPropMaterialConf.getSpatialDomain())
			// : "";

			String resourceType = this.documentUtil.getDocResTypeCode(doc);// doc.getProperties().isPropertyPresent(this.extPropMaterialConf.getResourceType())
																				// //
																				
			// ?
			// doc.getProperties().getStringValue(this.extPropMaterialConf.getResourceType())
			// : "";

			logger.info("文档空间域 [{}]，资源类型 [{}] ", domainCode, resourceType);

			Assert.hasLength(domainCode);
			Assert.hasLength(resourceType);

			DcmUser user = this.userOrgService.getUserEntityByDN(userId);

			DocPrivService docPriv = docPrivFactory.getPrivService(resourceType);

			return docPriv.getMasks(user, id, domainCode);
		} catch (Exception ex) {
			ex.printStackTrace();
			return PrivilegeFactory.Priv_None.getMask();
		}

	}
	
	@Override
	public Object getRolePrivsCodes(String roleCode, String resourceTypeCode) {
		
		return this.privTemplateDmn.getPrivilegeList(resourceTypeCode, 1L, roleCode);
	}
	@Override
	public Object getDocPrivCodes(UserResPrivRequestDTO dto) {
		
		Long masks = PrivilegeFactory.Priv_None.getMask();
		try {

			P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(dto.getRealm()));
			Document doc = this.documentUtil.loadByVersionSeriesId(p8conn.getObjectStore(), dto.getResId());// loadByVersionSeriesId(this.connService.getDefaultOS(),
																													// vid);

			String domainCode = doc.getProperties().isPropertyPresent(this.extPropMaterialConf.getSpatialDomain())
					? doc.getProperties().getStringValue(this.extPropMaterialConf.getSpatialDomain()) : "";

			String resourceType = doc.getProperties().isPropertyPresent(this.extPropMaterialConf.getResourceType())
					? doc.getProperties().getStringValue(this.extPropMaterialConf.getResourceType()) : "";

			logger.info("文档空间域 [{}]，资源类型 [{}] ", domainCode, resourceType);

			Assert.hasLength(domainCode);
			Assert.hasLength(resourceType);

			DcmUser user = this.userOrgService.getUserEntityByDN(dto.getUser());

			DocPrivService docPriv = docPrivFactory.getPrivService(resourceType);

			masks = docPriv.getMasks(user, dto.getResId(), domainCode, dto.getParentId());
		} catch (Exception ex) {
			ex.printStackTrace();
			masks = PrivilegeFactory.Priv_None.getMask();
		}
		
		return PrivilegeFactory.getCodes(masks);
		//return PrivilegeFactory.getCodes(this.getDocPrivMasksByVID(dto.getRealm(), dto.getUser(), dto.getResId(), dto.getParentId()));
	}
	
	@Override
	public Object getDocPrivCodesEx(UserResPrivRequestDTO dto) {
		
		Long masks = PrivilegeFactory.Priv_None.getMask();
		
		try {

			P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(dto.getRealm()));
			Document doc = this.documentUtil.loadByVersionSeriesId(p8conn.getObjectStore(), dto.getResId());// loadByVersionSeriesId(this.connService.getDefaultOS(),
																													// vid);
			String domainCode = doc.getProperties().isPropertyPresent(this.extPropMaterialConf.getSpatialDomain())
					? doc.getProperties().getStringValue(this.extPropMaterialConf.getSpatialDomain()) : "";

			String resourceType = doc.getProperties().isPropertyPresent(this.extPropMaterialConf.getResourceType())
					? doc.getProperties().getStringValue(this.extPropMaterialConf.getResourceType()) : "";

			logger.info(">>>文档空间域 [{}]，资源类型 [{}] ", domainCode, resourceType);

			Assert.hasLength(domainCode);
			Assert.hasLength(resourceType);

			DcmUser user = this.userOrgService.getUserEntityByDN(dto.getUser());

			DocPrivService docPriv = docPrivFactory.getPrivService(resourceType);

			masks = docPriv.getMasks(dto.getFrom(), user, dto.getResId(), domainCode, dto.getParentId());
			
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			// ex.printStackTrace();
			masks = PrivilegeFactory.Priv_None.getMask();
		}
		
		return PrivilegeFactory.getCodes(masks);
		//return PrivilegeFactory.getCodes(this.getDocPrivMasksByVID(dto.getRealm(), dto.getUser(), dto.getResId(), dto.getParentId()));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Set<String> getDocPrivCodesV1(UserResPrivRequestDTO dto) {

		try {

			P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(dto.getRealm()));
			Document doc = this.documentUtil.loadByVersionSeriesId(p8conn.getObjectStore(), dto.getResId());// loadByVersionSeriesId(this.connService.getDefaultOS(),
																													// vid);
			String domainCode = doc.getProperties().isPropertyPresent(this.extPropMaterialConf.getSpatialDomain())
					? doc.getProperties().getStringValue(this.extPropMaterialConf.getSpatialDomain()) : "";

			String resourceType = doc.getProperties().isPropertyPresent(this.extPropMaterialConf.getResourceType())
					? doc.getProperties().getStringValue(this.extPropMaterialConf.getResourceType()) : "";

			logger.info(">>>文档空间域 [{}]，资源类型 [{}] ", domainCode, resourceType);

			Assert.hasLength(domainCode);
			Assert.hasLength(resourceType);

			DcmUser user = null;
			if(0 == dto.getUserType()) {
				// 内部用户
				user =  this.userOrgService.getUserEntityByCode(dto.getUser());
			} else {
				// 外部用户
				UserResponseDTO sgaUser = (UserResponseDTO) this.sgaUserService.getUserByCode(dto.getUser());
				if(sgaUser != null) {
					user = new DcmUser();
					user.setUserCode(sgaUser.getUserCode());
					user.setLoginName(sgaUser.getLoginName());
					user.setUserName(sgaUser.getUserName());
				}
			}
			DocPrivService docPriv = docPrivFactory.getPrivService(resourceType);
			return (Set<String>) docPriv.getCodes(dto.getRealm(), dto.getFrom(), user, dto.getResId(), domainCode, dto.getParentId());
			
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return null;
	}

	@Deprecated
	@Override
	public Long getDocPrivMasksByVID(String realm, String userId, String versionSeriesId, String parentId) {

		try {

			P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
			Document doc = this.documentUtil.loadByVersionSeriesId(p8conn.getObjectStore(), versionSeriesId);// loadByVersionSeriesId(this.connService.getDefaultOS(),
																													// vid);

			String domainCode = doc.getProperties().isPropertyPresent(this.extPropMaterialConf.getSpatialDomain())
					? doc.getProperties().getStringValue(this.extPropMaterialConf.getSpatialDomain()) : "";

			String resourceType = doc.getProperties().isPropertyPresent(this.extPropMaterialConf.getResourceType())
					? doc.getProperties().getStringValue(this.extPropMaterialConf.getResourceType()) : "";

			logger.info("文档空间域 [{}]，资源类型 [{}] ", domainCode, resourceType);

			Assert.hasLength(domainCode);
			Assert.hasLength(resourceType);

			DcmUser user = this.userOrgService.getUserEntityByDN(userId);

			DocPrivService docPriv = docPrivFactory.getPrivService(resourceType);

			return docPriv.getMasks(user, versionSeriesId, domainCode, parentId);
		} catch (Exception ex) {
			ex.printStackTrace();
			return PrivilegeFactory.Priv_None.getMask();
		}

	}
	
	@Override
	public void modifyUserRole(Long userSeqId, String domainCode, String roleCode) {
			
		DcmUserdomainrole udr = this.udrDmn.findUniqueByProperties(new String[]{"userId","domainCode"}, new Object[]{userSeqId, domainCode});
		if(null == udr || (StringUtils.hasLength(udr.getRoleCode()) && udr.getRoleCode().equalsIgnoreCase(roleCode)))
			return;
		
		udr.setRoleCode(roleCode);
		udr.setLastTime(new Date());
		
		this.udrDmn.modify(udr);
	}
	
	@Override
	public void modifyUserRoleOfDomain(String userCode, String domainCode, String roleCode) {
		
		DcmUserdomainrole udr = this.udrDmn.findUniqueByProperties(new String[]{"userCode","domainCode"}, new Object[]{ userCode, domainCode});
		if(null == udr || (StringUtils.hasLength(udr.getRoleCode()) && udr.getRoleCode().equalsIgnoreCase(roleCode)))
			return;
		
		udr.setRoleCode(roleCode);
		udr.setLastTime(new Date());
		udr.setCreateTime(new Date());
		
		this.udrDmn.modify(udr);
	}
	
	@Override
	public void modifyUserIstop(Long userSeqId, String domainCode, int isTop) {
		
		DcmUserdomainrole udr = this.udrDmn.findUniqueByProperties(new String[]{"userId","domainCode"}, new Object[]{userSeqId, domainCode});
		if(null == udr || (udr.getIsTop() != null && udr.getIsTop().equals(isTop)))
			return;
		
		udr.setIsTop(isTop);
		this.udrDmn.modify(udr);
	}
	@Override
	public Object getPrivCodesOfBrainMap(String caseId, String userId) throws BusinessException {
		
		DcmUser user = this.userOrgService.getUserEntityByCode(userId);
		if(null == user) {
			throw new BusinessException("用户不存在，id："+userId);
		}
		
		List<DcmUserdomainrole> udrs = this.udrDmn.getByUserIdDomainCode(user.getId(), caseId);
		if(null == udrs || udrs.isEmpty()){
			return null;
		}
		List<String> privCodes = new ArrayList<String>();
		List<String> codes = null;
		for(DcmUserdomainrole udr : udrs){
			codes = this.privTemplateDmn.getPrivilegeCodes(ResourceConstants.ResourceType.RES_BRAINMAP, ResourceConstants.ResourceStatus.ACTIVE, udr.getRoleCode());
		    if(null == codes || codes.isEmpty()){
		    	continue;
		    }
		    privCodes.addAll(codes);
		}
		return privCodes;
	}
	
	@Override
	public Object getPrivCodesOfRestypeDomain(String resTypeCode, String[] domainTypeCodes, String userId) throws BusinessException {
		
		// DcmUser user = this.userOrgService.getUserEntityByCode(userId);
	/*	if(null == user) {
			throw new BusinessException("用户id[{0}]不存在", userId);
		}*/
		Map<String, Object[]> propertiesValuesMap = new HashMap<String, Object[]>();
		propertiesValuesMap.put("domainType", domainTypeCodes);
		propertiesValuesMap.put("userCode", new Object[]{userId});
		
		List<DcmUserdomainrole> udrs = this.udrDmn.findByProperties(propertiesValuesMap);
		if(null == udrs || udrs.isEmpty()){
			return null;
		}
		Set<String> privCodes = new HashSet<String>();
		List<String> temp = null;
		for(DcmUserdomainrole udr : udrs) {
			temp = this.privTemplateDmn.getPrivilegeCodes(resTypeCode, 1, udr.getRoleCode());
			if(null == temp) {
				continue;
			}
			privCodes.addAll(temp);
		}
		return privCodes;
	}
	@Override
	public List<PrivTemplateDTO>  getPrivCodesOfRestypeDomain(String realm, String[] resTypeCodes, String[] domainTypeCodes, String userId) throws BusinessException {
		
		Map<String, Object[]> propertiesValuesMap = new HashMap<String, Object[]>();
		propertiesValuesMap.put("domainType", domainTypeCodes);
		propertiesValuesMap.put("userCode", new Object[]{userId});
		
		List<DcmUserdomainrole> udrs = this.udrDmn.findByProperties(propertiesValuesMap);
		if(null == udrs || udrs.isEmpty()){
			return null;
		}
		List<PrivTemplateDTO> privTemps = new ArrayList<PrivTemplateDTO>();

		List<DcmPrivtemplate> temp = null;
		for(DcmUserdomainrole udr : udrs) {
			temp = this.privTemplateDmn.getPrivilegeCodes(realm, resTypeCodes, 1, udr.getRoleCode());
			if(null == temp) {
				continue;
			}
			for(DcmPrivtemplate tempDTO : temp) {
				privTemps.add(this.dozerMapper.map(tempDTO, PrivTemplateDTO.class));
			}
		}
		return privTemps;
	}
	@Override
	public List<PrivTemplateDTO> getPrivCodesOfRestypeDomain(String realm, String[] resTypeCodes, String[] domainTypeCodes, String userId, String domainCode) {
		Map<String, Object[]> propertiesValuesMap = new HashMap<String, Object[]>();
		propertiesValuesMap.put("domainType", domainTypeCodes);
		propertiesValuesMap.put("userCode", new Object[]{userId});
		propertiesValuesMap.put("domainCode", new String[]{domainCode});
		
		List<DcmUserdomainrole> udrs = this.udrDmn.findByProperties(propertiesValuesMap);
		if(null == udrs || udrs.isEmpty()){
			return null;
		}
		List<PrivTemplateDTO> privTemps = new ArrayList<PrivTemplateDTO>();

		List<DcmPrivtemplate> temp = null;
		for(DcmUserdomainrole udr : udrs) {
			temp = this.privTemplateDmn.getPrivilegeCodes(realm, resTypeCodes, 1, udr.getRoleCode());
			if(null == temp) {
				continue;
			}
			for(DcmPrivtemplate tempDTO : temp) {
				privTemps.add(this.dozerMapper.map(tempDTO, PrivTemplateDTO.class));
			}
		}
		return privTemps;	
	}
}
