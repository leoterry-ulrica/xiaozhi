package com.dist.bdf.facade.service.uic.impl;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.annotation.PostConstruct;

import org.dozer.Mapper;
import org.junit.internal.runners.statements.RunAfters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.page.PageParams;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.base.security.DesEncryptAES;
import com.dist.bdf.base.security.DesEncryptBase64;
import com.dist.bdf.base.security.DesEncryptRSA;
import com.dist.bdf.base.utils.DistThreadManager;
import com.dist.bdf.base.utils.FileUtil;
import com.dist.bdf.base.utils.StringUtil;
import com.dist.bdf.model.dto.dcm.FileContentLocalDTO;
import com.dist.bdf.model.dto.ldap.LdapOrg;
import com.dist.bdf.model.dto.ldap.LdapPerson;
import com.dist.bdf.model.dto.ldap.LdapTree;
import com.dist.bdf.model.dto.ldap.LdapWorkgroup;
import com.dist.bdf.model.dto.sga.ImgInfo;
import com.dist.bdf.model.dto.system.DcmOrganizationDTO;
import com.dist.bdf.model.dto.system.DepartmentDTO;
import com.dist.bdf.model.dto.system.InstituteDTO;
import com.dist.bdf.model.dto.system.Org2UsersDTO;
import com.dist.bdf.model.dto.system.OrgBasicInfoUpdateDTO;
import com.dist.bdf.model.dto.system.OrgDTO;
import com.dist.bdf.model.dto.system.OrgDelRequestDTO;
import com.dist.bdf.model.dto.system.OrgPositionAddDTO;
import com.dist.bdf.model.dto.system.OrgTeamAddDTO;
import com.dist.bdf.model.dto.system.OrgTypeAddDTO;
import com.dist.bdf.model.dto.system.OrgUserDTO;
import com.dist.bdf.model.dto.system.WorkgroupDTO;
import com.dist.bdf.model.dto.system.user.BaseUserDTO;
import com.dist.bdf.model.dto.system.user.UserAddRequestDTO;
import com.dist.bdf.model.dto.system.user.UserArticleInfoDTO;
import com.dist.bdf.model.dto.system.user.UserBasicDTO;
import com.dist.bdf.model.dto.system.user.UserCertificateInfoDTO;
import com.dist.bdf.model.dto.system.user.UserDTO;
import com.dist.bdf.model.dto.system.user.UserDelRequestDTO;
import com.dist.bdf.model.dto.system.user.UserDetailDTO;
import com.dist.bdf.model.dto.system.user.UserDetailRequestDTO;
import com.dist.bdf.model.dto.system.user.UserEducationDTO;
import com.dist.bdf.model.dto.system.user.UserLanguageDTO;
import com.dist.bdf.model.dto.system.user.UserOtherInfoDTO;
import com.dist.bdf.model.dto.system.user.UserPersonUpdateRequestDTO;
import com.dist.bdf.model.dto.system.user.UserPrjExperienceDTO;
import com.dist.bdf.model.dto.system.user.UserSearchDTO;
import com.dist.bdf.model.dto.system.user.UserSimpleDTO;
import com.dist.bdf.model.dto.system.user.UserTitleInfoDTO;
import com.dist.bdf.model.dto.system.user.UserTrainingDTO;
import com.dist.bdf.model.dto.system.user.UserUpdateRequestDTO;
import com.dist.bdf.model.dto.system.user.UserWorkExperienceDTO;
import com.dist.bdf.model.entity.system.DcmDicOrgExt;
import com.dist.bdf.model.entity.system.DcmLog;
import com.dist.bdf.model.entity.system.DcmOrgUser;
import com.dist.bdf.model.entity.system.DcmOrganization;
import com.dist.bdf.model.entity.system.DcmRole;
import com.dist.bdf.model.entity.system.DcmUser;
import com.dist.bdf.model.entity.system.DcmUserArticleInfo;
import com.dist.bdf.model.entity.system.DcmUserCertificateInfo;
import com.dist.bdf.model.entity.system.DcmUserEducation;
import com.dist.bdf.model.entity.system.DcmUserLanguage;
import com.dist.bdf.model.entity.system.DcmUserOtherInfo;
import com.dist.bdf.model.entity.system.DcmUserPrjExperience;
import com.dist.bdf.model.entity.system.DcmUserTitleInfo;
import com.dist.bdf.model.entity.system.DcmUserTraining;
import com.dist.bdf.model.entity.system.DcmUserWorkExperience;
import com.dist.bdf.model.entity.system.DcmUserdomainrole;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.dist.bdf.facade.service.RoleService;
import com.dist.bdf.facade.service.UserOrgService;
import com.dist.bdf.facade.service.biz.domain.system.DcmShareDmn;
import com.dist.bdf.facade.service.uic.domain.DcmLogDmn;
import com.dist.bdf.facade.service.uic.domain.DcmOrganizationDmn;
import com.dist.bdf.facade.service.uic.domain.DcmUserDmn;
import com.dist.bdf.facade.service.uic.domain.DcmUserdomainroleDmn;
import com.dist.bdf.facade.service.uic.domain.LdapDmn;
import com.dist.bdf.facade.service.uic.factory.LogFactory;
import com.dist.bdf.facade.service.uic.factory.UserInfoFactory;
import com.dist.bdf.common.conf.common.GlobalConf;
import com.dist.bdf.common.conf.common.LdapConf;
import com.dist.bdf.common.conf.imgserver.ImgServerConf;
import com.dist.bdf.common.constants.CacheKey;
import com.dist.bdf.common.constants.DicOrgExtType;
import com.dist.bdf.common.constants.DomainType;
import com.dist.bdf.common.constants.RoleConstants;
import com.dist.bdf.manager.cache.CacheStrategy;
import com.dist.bdf.manager.mongo.MongoFileStorageDmn;

/**
 * @author weifj
 * @version 1.0，2016/03/01，weifj，创建服务实现类
 * @version 1.1，2016/03/08，weifj
 *    1. 修改方法：addOrg(OrgDto orgDto)，增加同步ldap组的功能；
 *    2. 修改方法：addUsersToOrg(OrgUserDto dto)，增加同步ldap组的功能；
 *    3. 添加方法：delUsersFromOrg具体实现
 *    4. 添加方法：listDirectUsersOfOrg具体实现
 *  @version 1.2，2016/03/09，weifj
 *     1. 添加方法：listUsersOfOrg的具体实现
 *  @version 1.3，2016/03/10，weifj
 *     1. 添加方法：modifyUser(UserDto userDto)，修改用户
 *  @version 1.4，2016/04/18，weifj，修改从LDAP中同步的bug：第一次同步把ldap中cn当作是别名赋给了系统用户别名
 */
@Service("UserOrgService")
@Transactional(propagation = Propagation.REQUIRED)
public class UserOrgServiceImpl implements UserOrgService {
	
	private static Logger logger = LoggerFactory.getLogger(UserOrgServiceImpl.class);
	
	@Autowired
	private LdapDmn ldapDmn;
	@Autowired
	private DcmUserDmn userDmn;
	@Autowired
	private DcmOrganizationDmn orgDmn;
	@Autowired
	private DcmUserdomainroleDmn udrDmn;
	/*@Autowired
	private SecurityUtil securityUtil;*/
	@Autowired
	private LdapConf ldapConf;
	@Autowired
	private UserInfoFactory userInfoFactory;
	/*@Autowired
	private DistributedCacheService disCacheService;*/
	@Autowired
	private RoleService roleService;
	@Autowired
	private GlobalConf globalConf;
	@Autowired
	private CacheStrategy redisCache;
	@Autowired
	private LogFactory logFactory;
	@Autowired
	private DcmLogDmn logDmn;
	@Autowired
	private ImgServerConf imgSerConf;
	@Autowired
	private MongoFileStorageDmn mongoFileStorageDao;
	@Autowired
	private Mapper dozerMapper;
	@Autowired
	private DcmShareDmn dcmShareDmn;
	/**
	 * 刷新缓存信息
	 */
	@PostConstruct
	public void refreshUserOrgCacheInfos() {
		
		if(!this.globalConf.openCache())
			return;
	
		logger.debug(">>>异步进行刷新缓存数据......");
		DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				redisCache.removePattern(CacheKey.PREFIX_ORG_PATTERN);
				redisCache.removePattern(CacheKey.PREFIX_USER_PATTERN);
				redisCache.removePattern(CacheKey.PREFIX_REALM_USER_PATTERN);
				
				refreshUserCacheInfos();
				refreshOrgCacheInfos();	
			}
		});
		/*DistThreadManager.MyCacheThreadPool.execute(() -> { // lambda写法
			
			
		});*/
	}
	
    private void cacheUser(DcmUser user) {
		
		Assert.notNull(user);
		
		// this.redisCache.set(CacheKey.PREFIX_USER + user.getId(), user);
		this.redisCache.set(CacheKey.PREFIX_USER + user.getUserCode(), user);
		this.redisCache.set(CacheKey.PREFIX_USER + user.getLoginName(), user);
		if(StringUtils.hasLength(user.getDn()))
			this.redisCache.set(CacheKey.PREFIX_USER + user.getDn(), user);
	}

    private void cacheOrg(DcmOrganizationDTO org) {
		
		// this.redisCache.set(CacheKey.PREFIX_ORG + org.getId(), org);
		this.redisCache.set(CacheKey.PREFIX_ORG + org.getOrgCode(), org);
	}

	/**
	 * 同步用户信息
	 */
    public synchronized void refreshUserCacheInfos() {
		
		Date start = new Date();
		logger.info(">>>正在初始化用户信息......");
		//this.cacheManager.clearUserCache();
		List<DcmUser> users = this.userDmn.list();
		
		// 效率反而更低
		// CacheUserIdMap = users.stream().collect(Collectors.toMap(DcmUser :: getId, p -> (p)));
		// CacheUserNameMap = users.stream().collect(Collectors.toMap(DcmUser :: getLoginName, p -> (p)));
		for(DcmUser u : users) {
			cacheUser(u);
		}
	
		Date end = new Date();
		logger.info(">>>完成初始化用户信息，共消耗：{} 毫秒", end.getTime()-start.getTime());
	}
    
    public synchronized void refreshOrgCacheInfos(){
		
		Date start = new Date();
		logger.info(">>>正在初始化机构信息......");
		//this.cacheManager.clearOrgCache();

		List<DcmOrganization> orgs = this.orgDmn.find();
		for(DcmOrganization org : orgs){
			
			DcmOrganizationDTO orgDTO = DcmOrganizationDTO.clone(org);
			orgDTO.setSuperParentCode(getInstituteCode(org));
			
			cacheOrg(orgDTO);
		}
		Date end = new Date();
		logger.info(">>>完成初始化机构信息，共花费：{} 毫秒", end.getTime()-start.getTime());
	}
    
    /**
	 * 获取院级机构编码
	 * @param org
	 * @return
	 */
	private String getInstituteCode(DcmOrganization org) {

		if(null == org){
			return "";
		}
		if(org.getDomainType().equals(DomainType.INSTITUTE)){
			logger.info(">>>当前机构是院，直接返回机构编码。");
			return org.getOrgCode();
		}
		if(null == org.getParentId() || org.getParentId().equals(-1L)){
			logger.info(">>>当前机构已经是顶级机构，不存在院级机构。");
			return "";
		}
		DcmOrganization findOrg = this.orgDmn.loadById(org.getParentId());
		
		return getInstituteCode(findOrg);
		
	}
	/**
	 * 获取院级机构对象
	 * @param org
	 * @return
	 */
	private DcmOrganization getInstitute(DcmOrganization org) {

		if(null == org){
			return null;
		}
		if(org.getDomainType().equals(DomainType.INSTITUTE)){
			logger.info(">>>当前机构是院，直接返回机构编码。");
			return org;
		}
		if(null == org.getParentId() || org.getParentId().equals(-1L)){
			logger.info(">>>当前机构已经是顶级机构，不存在院级机构。");
			return null;
		}
		DcmOrganization findOrg = this.orgDmn.loadById(org.getParentId());
		
		return getInstitute(findOrg);
		
	}

	/**
	 * //updateUserLastActiveDateThread(user);
		//user.setDomainType(DomainType.Person);
		//user.setMajor(userDto.getMajor());
		//user.setQq(userDto.getQq());
		//user.setBirthday(userDto.getBirthday());
		//user.setNativePlace(userDto.getNativePlace());
	    //user.setPosition(userDto.getPosition());
	    //user.setSpeciality(userDto.getSpeciality());
	 */
	
	//TODO 此時返回的数据对象不太合理，应该只返回用户的基本信息，有时间请更正
	@Override
	@Deprecated
	public synchronized DcmUser addUser(String contextPath, String baseURL, UserAddRequestDTO userDto) throws BusinessException {

		String realLoginName = "";
		if (userDto.getLoginName().indexOf("_") > 0) {
			realLoginName = userDto.getLoginName();
		} else {
			realLoginName = userDto.getRealm() + "_" + userDto.getLoginName();
		}
		
		boolean isExist = this.userDmn.existByProperties(new String[] { "loginName", "currentStatus" },
				new Object[] { realLoginName, 1L });
		if (isExist) {
			throw new BusinessException("已存在用户，登录名：[{0}]，不能重复添加。", realLoginName);
		}
		DcmUser user = new DcmUser();
		user.setLoginName(realLoginName);
		user.setUserName(userDto.getUserName());
		// user.setUserPwd(DesEncryptDap.getInstance().Encrypt(userDto.getUserPwd()));
		// 注意解密方法
		String deEncryptPwd = ((DesEncryptRSA)DesEncryptRSA.getInstance()).decryptReverse(userDto.getUserPwd());
		user.setUserPwd(DesEncryptAES.getInstance().encrypt(deEncryptPwd));// DesEncryptBase64.getInstance().encrypt(userDto.getUserPwd()));
		user.setCurrentStatus(1L);
		user.setSex(userDto.getSex());
		user.setTelephone(userDto.getTelephone());
		user.setDateCreated(new Date());
		user.setDateLastActivity(new Date());
		user.setPhone(userDto.getPhone());
		user.setPosition(userDto.getPosition());
		user.setTeamName(userDto.getTeamName());
		user.setSuperAdmin(0); // 默认为0
		DcmOrganization org = this.getOrgById(userDto.getOrgId());
		
		user.setDepartment(org.getAlias());
		user.setEmail(userDto.getEmail());
		user.setUserCode(UUID.randomUUID().toString().toUpperCase());
		// 根据性别获取默认头像
		user.setAvatar(this.userDmn.getUserAvatarLink("", "", user));
		user.setRealm(userDto.getRealm());
		user.setDn(String.format("cn=%s,o=%s,%s", user.getLoginName(), user.getRealm(), this.ldapConf.getBn()));

		// user.setPosition(null == role? "未知":role.getRoleName());
		// this.userDmn.modify(user);
		
		if(!addUserToLdap(org, user, deEncryptPwd))
			throw new BusinessException("添加用户到LDAP失败。");
		
		userDmn.addUser(user);
		DcmRole role = this.roleService.getRoleById(userDto.getRoleId());
		addUserToOrg(org, user, role);

		if(this.globalConf.openCache()){
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					logger.debug(">>>把用户添加到缓存中......");
					// cacheUser(user);
					getOrgUserTree(true, true);	
					refreshUserOrgCacheInfos();
				}
			});
		}
		// 新创建实例，为了跟user的状态保持区别，否则下面的操作（setAvatar）一样会影响到user
		DcmUser newUser = user.clone();
		// 设置头像
		newUser.setAvatar(this.userDmn.getUserAvatarLink(contextPath, baseURL, newUser));
		newUser.setUserPwd("");
		// this.userModelToUserDTO(contextPath, baseURL, u);
		return newUser;
	}

	@Override
	public Object addUserEx(String contextPath, String baseURL, UserAddRequestDTO userDto) {
		
		String realLoginName = "";
		if (userDto.getLoginName().indexOf("_") > 0) {
			realLoginName = userDto.getLoginName();
		} else {
			realLoginName = userDto.getRealm() + "_" + userDto.getLoginName();
		}
		
		boolean isExist = this.userDmn.existByProperties(new String[] { "loginName", "currentStatus" },
				new Object[] { realLoginName, 1L });
		if (isExist) {
			throw new BusinessException("已存在用户，登录名：[{0}]，不能重复添加。", realLoginName);
		}
		DcmUser user = new DcmUser();
		user.setLoginName(realLoginName);
		user.setUserName(userDto.getUserName());
		// user.setUserPwd(DesEncryptDap.getInstance().Encrypt(userDto.getUserPwd()));
		// 注意解密方法
		String deEncryptPwd = ((DesEncryptRSA)DesEncryptRSA.getInstance()).decryptReverse(userDto.getUserPwd());
		user.setUserPwd(DesEncryptAES.getInstance().encrypt(deEncryptPwd));// DesEncryptBase64.getInstance().encrypt(userDto.getUserPwd()));
		user.setCurrentStatus(1L);
		user.setSex(userDto.getSex());
		user.setTelephone(userDto.getTelephone());
		user.setDateCreated(new Date());
		user.setDateLastActivity(new Date());
		user.setPhone(userDto.getPhone());
		user.setPosition(userDto.getPosition());
		user.setTeamName(userDto.getTeamName());
		user.setSuperAdmin(0); // 默认为0
		DcmOrganization orgDepartment = this.getOrgById(userDto.getDepartment().getOrgId());
		
		user.setDepartment(orgDepartment.getAlias());
		user.setEmail(userDto.getEmail());
		user.setUserCode(UUID.randomUUID().toString().toUpperCase());
		// 根据性别获取默认头像
		user.setAvatar(this.userDmn.getUserAvatarLink(this.imgSerConf.getServerURI(), "", "", user));
		user.setRealm(userDto.getRealm());
		user.setDn(String.format("cn=%s,o=%s,%s", user.getLoginName(), user.getRealm(), this.ldapConf.getBn()));

		if(!addUserToLdap(orgDepartment, user, deEncryptPwd))
			throw new BusinessException("添加用户到LDAP失败。");
		
		userDmn.addUser(user);
		
		DcmOrganization orgInstitute = this.getOrgById(userDto.getInstitute().getOrgId());
		// 院--角色
		addUserToOrg(orgInstitute, user, userDto.getInstitute().getRoleIds());
		// 所-角色
		addUserToOrg(orgDepartment, user, userDto.getDepartment().getRoleIds());

		if(this.globalConf.openCache()){
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					logger.debug(">>>把用户添加到缓存中......");
					// cacheUser(user);
					getOrgUserTree(true, true);	
					refreshUserOrgCacheInfos();
				}
			});
		}
		// 新创建实例，为了跟user的状态保持区别，否则下面的操作（setAvatar）一样会影响到user
		DcmUser newUser = user.clone();
		// 设置头像
		newUser.setAvatar(this.userDmn.getUserAvatarLink(contextPath, baseURL, newUser));
		newUser.setUserPwd("");
		// this.userModelToUserDTO(contextPath, baseURL, u);
		return newUser;
	}
	/**
	 * 添加用户到ldap
	 * @param userDto
	 * @param user
	 */
	private boolean addUserToLdap(DcmOrganization org, DcmUser user, String userOriginPwd) {
		
		if (ldapConf.isLdapEnabledBoolean()) {
			
			// 创建ldap用户
			// 用户的dn格式：cn=thupdi_weifj,o=thupdi,DC=XIAOZHI
			// 院的dn格式：o=thupdi,DC=XIAOZHI，thupdi是院标识
			// 部门的dn格式：cn=developer,cn=thupdi,o=thupdi,DC=XIAOZHI，developer是部门标识
			// 检查院下是否有重复用户
			String orgDn = "o=" + user.getRealm() + "," + this.ldapConf.getBn(); // 院的dn
			if(this.ldapDmn.isExistInetOrgPerson(orgDn, user.getLoginName())) {
				logger.info(">>>ldap中已存在用户 [{}]", user.getLoginName());
				return false;
			
			}
			Map<String, String> properties = new HashMap<String, String>();
			properties.put("displayName", user.getUserName());
			properties.put("sex", user.getSex());
			
			if (this.ldapDmn.addInetOrgPerson(orgDn, user.getLoginName(), this.userDmn.getUserLastName(user.getUserName()),
					userOriginPwd, user.getUserCode(), properties)) {

				// 添加用户到ldap组，默认组已经建立好
				String groupDn = String.format("cn=%s,cn=%s,o=%s,%s",
						org.getOrgName(), user.getRealm(), user.getRealm(),
						this.ldapConf.getBn());
				return this.ldapDmn.addInetOrgPersonToGroup(groupDn, Arrays.asList(user.getDn()));
			}
		}
		
		return false;
	}

	
	/**
	 * 添加用户到机构，并赋予角色
	 * @param userDto
	 * @param user
	 */
	private void addUserToOrg(DcmOrganization org, DcmUser user, DcmRole role) {

		// 机构和用户关系
		if (!org.getDomainType().equalsIgnoreCase(DomainType.INSTITUTE) && !this.orgDmn.existOrg2User(org.getId(), user.getId())) {

			DcmOrgUser ou = new DcmOrgUser();
			ou.setUserId(user.getId());
			ou.setOrgId(org.getId());

			this.orgDmn.addOrg2User(ou);
		}
		// 机构、用户和角色关系
		
		if (role != null) {
			// 默认是内部用户
			this.udrDmn.addUserToDomain(user.getId(), user.getUserCode(), 0, org.getDomainType(), org.getOrgCode(), role.getRoleCode());
		}
	}
	/**
	 * 批量添加用户-机构-角色
	 * @param org
	 * @param user
	 * @param roleIds
	 */
	private void addUserToOrg(DcmOrganization org, DcmUser user, Long[] roleIds)  {
		Assert.notEmpty(roleIds);
		DcmRole role = null;
		for(Long roleId : roleIds){
			role = this.roleService.getRoleById(roleId);
			if(null == role){
				continue;
			}
			addUserToOrg(org, user, role);
		}
	}
	
	@Override
	public DcmUser updateUserBasicInfo(UserUpdateRequestDTO userDto) throws BusinessException {

		// 是否更改过部门
		boolean isOrgChanged = false;
		DcmOrganization preOrg = null;
		DcmOrganization newOrg = null;
		DcmRole preRole = null;
		DcmRole newRole = null;

		DcmUser user = this.getUserEntityById(userDto.getId());// this.userDmn.findById();
		// user.setLoginName(userDto.getLoginName()); // 登录名不允许修改
		user.setUserName(userDto.getUserName());
		// user.setUserPwd(userDto.getUserPwd()); // 密码暂时不允许修改
		// user.setCurrentStatus(1L); // 默认是在有效用户才能修改
		user.setSex(userDto.getSex());
		user.setTelephone(userDto.getTelephone());
		user.setPhone(userDto.getPhone());
		user.setMajor(userDto.getMajor());
		user.setQq(userDto.getQq());
		user.setBirthday(userDto.getBirthday());
		user.setTeamName(userDto.getTeamName());
		user.setSuperAdmin(0);
		updateUserLastActiveDateThread(user);
		// user.setDepartment(userDto.getDepartment());
		user.setEmail(userDto.getEmail());
		user.setNativePlace(userDto.getNativePlace());
		user.setSpeciality(userDto.getSpeciality());

		if (!userDto.getPreOrgId().equals(userDto.getNewOrgId())) {
			isOrgChanged = true;
		}
		// 修改部门
		preOrg = this.getOrgById(userDto.getPreOrgId());
		// Assert.notNull(preOrg);
		newOrg = this.getOrgById(userDto.getNewOrgId());
		Assert.notNull(newOrg);
		preRole = this.roleService.getRoleById(userDto.getPreRoleId());
		// Assert.notNull(preRole);
		newRole = this.roleService.getRoleById(userDto.getNewRoleId());
		Assert.notNull(newRole);
		user.setDepartment(newOrg.getAlias());

		this.orgDmn.updateOrgAndRoleUser(userDto.getId(), preOrg, newOrg, preRole, newRole);
		user.setPosition(newRole.getRoleName());

		if (ldapConf.isLdapEnabledBoolean() && !user.getUserName().equals(userDto.getUserName())) {
			this.ldapDmn.alterUserName(user.getDn(), userDto.getUserName());

			if (isOrgChanged) {
				this.ldapDmn.removeUsersFromLdapGroup(preOrg.getDn(), new String[] { user.getDn() });
				this.ldapDmn.addInetOrgPersonToGroup(newOrg.getDn(), new String[] { user.getDn() });
			}
		}

		user = this.userDmn.modify(user);
		/*
		 * if(CacheUserIdMap.containsKey(user.getId())){
		 * CacheUserIdMap.remove(user.getId()); }
		 * if(CacheUserNameMap.containsKey(user.getLoginName())){
		 * CacheUserNameMap.remove(user.getLoginName()); }
		 */
		if(this.globalConf.openCache()){
			cacheUser(user);
			this.redisCache.removePattern(CacheKey.PREFIX_REALM_USER_PATTERN);
		}
		
		// CacheUserIdMap.put(user.getId(), user);
		// CacheUserNameMap.put(user.getLoginName(), user);

		return user;
	}
	@Override
	public Object updateUserBasicInfoByAdmin(UserUpdateRequestDTO userDto) {
		
		// 是否更改过部门
		boolean isOrgChanged = false;
		boolean isNameChanged = false;
		
		DcmOrganization preOrg = null;
		DcmOrganization newOrg = null;
		DcmRole preRole = null;
		DcmRole newRole = null;
		String decryptPwd = "";

		DcmUser user = this.getUserEntityById(userDto.getId());
		
		if(!user.getUserName().equalsIgnoreCase(userDto.getUserName())){
			isNameChanged = true;
			user.setUserName(userDto.getUserName());
		}
		if(userDto.getPwdMode()){
			Assert.notNull(userDto.getPwd(), "pwd can not be null");
			Assert.hasLength(userDto.getPwd(), "pwd can not be empty");
			decryptPwd = ((DesEncryptRSA)DesEncryptRSA.getInstance()).decryptReverse(userDto.getPwd());
			user.setUserPwd(DesEncryptAES.getInstance().encrypt(decryptPwd)); 
		}
		
		user.setSex(userDto.getSex());
		user.setTelephone(userDto.getTelephone());
		user.setPhone(userDto.getPhone());
		user.setMajor(userDto.getMajor());
		user.setQq(userDto.getQq());
		user.setBirthday(userDto.getBirthday());
		user.setTeamName(userDto.getTeamName());
		//user.setSuperAdmin(0);
		user.setEmail(userDto.getEmail());
		user.setNativePlace(userDto.getNativePlace());
		user.setSpeciality(userDto.getSpeciality());
		user.setPosition(userDto.getPosition());

		if (!userDto.getPreOrgId().equals(userDto.getNewOrgId())) {
			isOrgChanged = true;
		}
		// 修改部门
		preOrg = this.getOrgById(userDto.getPreOrgId());
		newOrg = this.getOrgById(userDto.getNewOrgId());
		Assert.notNull(newOrg, "新机构实体不能为null");
		preRole = this.roleService.getRoleById(userDto.getPreRoleId());
		newRole = this.roleService.getRoleById(userDto.getNewRoleId());
		Assert.notNull(newRole, "新角色实体不能为null");
		user.setDepartment(newOrg.getAlias());

		this.orgDmn.updateOrgAndRoleUser(userDto.getId(), preOrg, newOrg, preRole, newRole);
		/*user.setPosition(newRole.getRoleName());*/

		if (ldapConf.isLdapEnabledBoolean()) {
			if(isNameChanged){
				this.ldapDmn.alterUserName(user.getDn(), userDto.getUserName());
			}
			if(userDto.getPwdMode()){
				this.ldapDmn.alterUserPassowrdByDN(user.getDn(), decryptPwd);
			}

			if (isOrgChanged) {
				this.ldapDmn.removeUsersFromLdapGroup(preOrg.getDn(), new String[] { user.getDn() });
				this.ldapDmn.addInetOrgPersonToGroup(newOrg.getDn(), new String[] { user.getDn() });
			}
		}

		user = this.userDmn.modify(user);
		//updateUserLastActiveDateThread(user);
		if(this.globalConf.openCache()){
			cacheUser(user);
			this.redisCache.removePattern(CacheKey.CACHEKEY_REALM_USER+"*");
		}
		
		return user;
	}
	@Override
	public Object updateUserBasicInfoByAdminEx(UserUpdateRequestDTO userDto) {
		
		// 是否更改过部门
		boolean isOrgChanged = false;
		boolean isNameChanged = false;
		
		DcmOrganization preOrg = null;
		DcmOrganization newOrg = null;
		// DcmRole preRole = null;
		// DcmRole newRole = null;
		String decryptPwd = "";

		DcmUser user = this.getUserEntityById(userDto.getId());
		
		if(!user.getUserName().equalsIgnoreCase(userDto.getUserName())) {
			isNameChanged = true;
			user.setUserName(userDto.getUserName());
		}
		if(userDto.getPwdMode()) {
			Assert.notNull(userDto.getPwd(), "pwd can not be null");
			Assert.hasLength(userDto.getPwd(), "pwd can not be empty");
			decryptPwd = ((DesEncryptRSA)DesEncryptRSA.getInstance()).decryptReverse(userDto.getPwd());
			user.setUserPwd(DesEncryptAES.getInstance().encrypt(decryptPwd)); 
		}
		
		user.setSex(userDto.getSex());
		user.setTelephone(userDto.getTelephone());
		user.setPhone(userDto.getPhone());
		user.setMajor(userDto.getMajor());
		user.setQq(userDto.getQq());
		user.setBirthday(userDto.getBirthday());
		user.setTeamName(userDto.getTeamName());
		//user.setSuperAdmin(0);
		user.setEmail(userDto.getEmail());
		user.setNativePlace(userDto.getNativePlace());
		user.setSpeciality(userDto.getSpeciality());
		user.setPosition(userDto.getPosition());

		if (null == userDto.getPreDepartment().getOrgId() || !userDto.getPreDepartment().getOrgId().equals(userDto.getNewDepartment().getOrgId())) { // .getNewOrgId()
			// 部门调整
			isOrgChanged = true;
		}
		// 修改部门
		preOrg = this.getOrgById(userDto.getPreDepartment().getOrgId());
		newOrg = this.getOrgById(userDto.getNewDepartment().getOrgId());
		Assert.notNull(newOrg, "新机构实体不能为null");
		// preRole = this.roleService.getRoleById(userDto.getPreRoleId());
		// newRole = this.roleService.getRoleById(userDto.getNewRoleId());
		// Assert.notNull(newRole, "新角色实体不能为null");
		user.setDepartment(newOrg.getAlias());
		// 默认更改内部用户
		this.orgDmn.updateOrgAndRoleUser(0, user, userDto.getInstitute(), userDto.getPreDepartment(), userDto.getNewDepartment(), preOrg, newOrg);
		/*user.setPosition(newRole.getRoleName());*/

		if (ldapConf.isLdapEnabledBoolean()) {
			if(isNameChanged){
				this.ldapDmn.alterUserNameByDN(user.getDn(), userDto.getUserName());
			}
			if(userDto.getPwdMode()){
				this.ldapDmn.alterUserPassowrdByDN(user.getDn(), decryptPwd);
			}

			if (isOrgChanged) {
				if(preOrg != null) {
					this.ldapDmn.removeUsersFromLdapGroup(preOrg.getDn(), new String[] { user.getDn() });
				}
				this.ldapDmn.addInetOrgPersonToGroup(newOrg.getDn(), new String[] { user.getDn() });
			}
		}

		user = this.userDmn.modify(user);
		//updateUserLastActiveDateThread(user);
		if(this.globalConf.openCache()){
			cacheUser(user);
			this.redisCache.removePattern(CacheKey.CACHEKEY_REALM_USER+"*");
		}
		
		return user;
	}

	@Override
	public DcmUser getUserEntityByLoginName(String loginName) {

		DcmUser user = this.globalConf.openCache() ? (DcmUser) this.redisCache.get(CacheKey.PREFIX_USER + loginName)
				: null;
		if (user != null) {
			logger.info(">>>getUserEntityByLoginName，缓存获取到用户 [{}]", loginName);
			return user;
		}

		user = this.userDmn.findByLoginNameSimply(loginName);
		if (user != null && this.globalConf.openCache()) {
			final DcmUser finalUser = user;
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					cacheUser(finalUser);
				}
			});
		}

		return user;
	}

	@Override
	public DcmUser getUserEntityByDN(String dn) {
		
		DcmUser user = this.globalConf.openCache()? (DcmUser)this.redisCache.get(CacheKey.PREFIX_USER + dn):null;
		if(user != null) {
			logger.info(">>>getUserEntityByDN，缓存获取到用户，dn : [{}]", dn);
			return user;
		}
		user = this.userDmn.findByDN(dn);
		if (user != null && this.globalConf.openCache()) {
			final DcmUser finalUser = user;
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					cacheUser(finalUser);
				}
			});
		}

		return user;
	}
	@Override
	public DcmUser getUserEntityById(Long id) {
		
		DcmUser user = this.globalConf.openCache()? (DcmUser)this.redisCache.get(CacheKey.PREFIX_USER + id):null;
		if(user != null){
			logger.info(">>>getUserEntityById，缓存获取到用户，id [{}]", id);
			return user;
		}
		user = this.userDmn.loadById(id);
		if (user != null && this.globalConf.openCache()) {
			final DcmUser finalUser = user;
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					cacheUser(finalUser);
				}
			});
		}
		return user;
	}
	@Override
	public DcmUser getUserEntityByCode(String code) {
		
		DcmUser user = this.globalConf.openCache()? (DcmUser)this.redisCache.get(CacheKey.PREFIX_USER + code):null;
		if(user != null){
			logger.info(">>>getUserEntityByCode，缓存获取到用户，code [{}]", code);
			return user;
		}
		user = this.userDmn.findByCode(code);
		if (user != null && this.globalConf.openCache()) {
			final DcmUser finalUser = user;
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					cacheUser(finalUser);
				}
			});
		}
		return user;

	}
	@Deprecated
	@Override
	public UserDTO getUserByLoginName(String contextPath, String baseURL, String loginName) {

		DcmUser u = this.getUserEntityByLoginName(loginName);
		
		//updateUserLastActiveDateThread(u, baseURL);
		
		return this.getUser(contextPath, baseURL, u);
		/*if (null == u) {
			throw new BusinessException("业务库，登录名为 [{0}] 的用户不存在", loginName);
		}
		baseURL = baseURL.endsWith("/") ? baseURL : (baseURL + "/");
		UserDTO newU = userModelToUserDTO(contextPath, baseURL, u);
		
		return newU;*/
	}
	
	@Override
	public UserDTO getUserByLoginNameEx(String contextPath, String baseURL, String loginName, String clientIP) {
		
		DcmUser u = null;
		
		if(loginName.toLowerCase().startsWith("cn")){
			// dn登录
			u = this.getUserEntityByDN(loginName);
		}else {
			u = this.getUserEntityByLoginName(loginName);
		}
		
		updateUserLastActiveDateThread(u, "login", clientIP);
		
		return this.getUser(contextPath, baseURL, u);
		
	}
	@Override
	public UserDTO getUserByCode(String contextPath, String baseURL, String userCode, String clientIP) {
		
		DcmUser u = this.getUserEntityByCode(userCode);
	
		updateUserLastActiveDateThread(u, "login", clientIP);
		
		return this.getUserEx(contextPath, baseURL, u);
		
	}
	@Override
	public UserDTO getUserByLoginName(String contextPath, String baseURL, String realm, String loginName) {
		
		LdapPerson per = this.ldapDmn.searchUniquePerson("o="+realm+","+this.ldapConf.getBn(), loginName);
		if(null == per) return null;
		
		DcmUser u = this.getUserEntityByDN(per.getDn());
		
		try {
			URI uri = new URI(baseURL);
			
			updateUserLastActiveDateThread(u, "login", uri.getHost());
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		return this.getUser(contextPath, baseURL, u);
	}

	private UserDTO getUser(String contextPath, String baseURL, DcmUser user) throws BusinessException{
		
        //DcmUser u = this.getUserEntityByDN(dn);
		
		if (null == user) {
			throw new BusinessException(">>>缓存和业务库不存在用户......");
		}
		baseURL = baseURL.endsWith("/") ? baseURL : (baseURL + "/");
		UserDTO newU = userModelToUserDTO(contextPath, baseURL, user);
		
		return newU;
	}
    private UserDTO getUserEx(String contextPath, String baseURL, DcmUser user) throws BusinessException{
		
		if (null == user) {
			throw new BusinessException(">>>缓存和业务库不存在用户......");
		}
		baseURL = baseURL.endsWith("/") ? baseURL : (baseURL + "/");
		UserDTO newU = userModelToUserDTOEx(contextPath, baseURL, user);
		
		return newU;
	}

	@Override
	public List<DcmUser> listAllUsers() {

		return userDmn.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DcmUser> listAllUsers(final String realm) {
		
		List<DcmUser> users = this.globalConf.openCache()? (List<DcmUser>) this.redisCache.getList(CacheKey.CACHEKEY_REALM_USER + realm):null;
		
		if(null == users || users.isEmpty()){
			logger.info(">>>没有获取到院[{}]下的用户缓存信息", realm);
			users = this.userDmn.findByRealm(realm);//.findByLoginnameStartMatch(realm);
			if(users != null && this.globalConf.openCache()){
				
				final List<DcmUser> finalUsers = users;
				DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
					
					@Override
					public void run() {
						redisCache.setList(CacheKey.CACHEKEY_REALM_USER + realm, finalUsers);
					}
				});
			}
			return users;
		}
		logger.info(">>>获取到院[{}]下的用户缓存信息", realm);
		return users;
	}

	@Override
	public List<UserDTO> listAllUsers(String contextPath, String baseURL) throws BusinessException {

		List<DcmUser> users = this.listAllUsers();
		baseURL = baseURL.endsWith("/") ? baseURL : (baseURL + "/");
		List<UserDTO> newUsers = new ArrayList<UserDTO>();
		for (DcmUser u : users) {
			UserDTO newU = userModelToUserDTO(contextPath, baseURL, u);
			newUsers.add(newU);
		}

		return newUsers;
	}
	
	private UserDTO userModelToUserDTO(String contextPath, String baseURL, DcmUser u) {
		
		UserDTO newU = new UserDTO();
		newU.setId(u.getId());
		newU.setLoginName(u.getLoginName());
		newU.setUserName(u.getUserName());
		newU.setSex(u.getSex());
		newU.setCurrentStatus(u.getCurrentStatus());
		newU.setTelephone(u.getTelephone());
		newU.setEmail(u.getEmail());
		newU.setDateCreated(u.getDateCreated());
		newU.setBirthday(u.getBirthday());
		newU.setSpeciality(u.getSpeciality());
		newU.setUserCode(u.getUserCode());
		newU.setQq(u.getQq());
		newU.setMajor(u.getMajor());
		newU.setPhone(u.getPhone());
		newU.setNativePlace(u.getNativePlace());
		
		newU.setDepartment(u.getDepartment());
		newU.setPosition(u.getPosition());
		newU.setRealm(u.getRealm());
		newU.setTeamName(u.getTeamName());
		newU.setSuperAdmin(u.getSuperAdmin());
		//newU.setUserPwd(u.getUserPwd());
		newU.setAvatar(this.userDmn.getUserAvatarLink(this.imgSerConf.getServerURI(), contextPath, baseURL, u));
		newU.setDn(u.getDn());
		logger.info(">>>获取用户 [{}] 所属机构，以及对应的角色。", newU.getLoginName());
		List<DcmOrganization> findOrgs = this.orgDmn.getOrgsByUserId(u.getId());
		if(findOrgs != null && !findOrgs.isEmpty()){
			newU.setDepartment(findOrgs.get(0).getAlias());
		}
		List<DcmRole> roles = null;
		if (findOrgs != null && !findOrgs.isEmpty()) {
			List<DcmOrganization> institutes = new ArrayList<DcmOrganization>();
			for(DcmOrganization org : findOrgs){
				if(DomainType.INSTITUTE.equalsIgnoreCase(org.getDomainType())){
					institutes.add(org);
				}
				roles = (List<DcmRole>) this.udrDmn.getRolesOfUser(newU.getUserCode(), org.getOrgCode()); //.getRolesOfUser(newU.getId(), org.getOrgCode());
				// 是否多余？
				/*if((null == roles || roles.isEmpty()) && DomainType.Department.equalsIgnoreCase(org.getDomainType())){
					// 说明当前用户在当前所下没有角色，添加默认的所员角色
					roles = new ArrayList<DcmRole>();
					DcmRole r = new DcmRole();
					r.setRoleCode(RoleConstants.RoleCode.R_Department_Member);
					findRole = this.roleService.getRoleByCode(RoleConstants.RoleCode.R_Department_Member);
					r.setRoleName(null == findRole? "所员" : findRole.getRoleName());
					roles.add(r);
				}*/
				newU.addOrgAndRoles(org, roles);
			}
			if(institutes.isEmpty()){
				// 说明当前用户只属于所，额外添加当前用户所属的院，并给予默认的院员角色
				DcmOrganization belongInstitute = this.getInstituteByOrgInCache(findOrgs.get(0));
				if(belongInstitute != null) {
					roles = new ArrayList<DcmRole>();
					DcmRole r = this.roleService.getRoleByCode(RoleConstants.RoleCode.R_Institute_Member);
					/*findRole = this.roleService.getRoleByCode(RoleConstants.RoleCode.R_Institute_Member);
					r.setRoleName(null == findRole? "院员" : findRole.getRoleName());*/
					if(r != null) {
						roles.add(r);
					    newU.addOrgAndRoles(belongInstitute, roles);
					}		
				}
			}
		}
		return newU;
	}
    private UserDTO userModelToUserDTOEx(String contextPath, String baseURL, DcmUser u) {
		
		UserDTO newU = new UserDTO();
		newU.setId(u.getId());
		newU.setLoginName(u.getLoginName());
		newU.setUserName(u.getUserName());
		newU.setSex(u.getSex());
		newU.setCurrentStatus(u.getCurrentStatus());
		newU.setTelephone(u.getTelephone());
		newU.setEmail(u.getEmail());
		newU.setDateCreated(u.getDateCreated());
		newU.setBirthday(u.getBirthday());
		newU.setSpeciality(u.getSpeciality());
		newU.setUserCode(u.getUserCode());
		newU.setQq(u.getQq());
		newU.setMajor(u.getMajor());
		newU.setPhone(u.getPhone());
		newU.setNativePlace(u.getNativePlace());
		
		newU.setDepartment(u.getDepartment());
		newU.setPosition(u.getPosition());
		newU.setRealm(u.getRealm());
		newU.setTeamName(u.getTeamName());
		newU.setSuperAdmin(u.getSuperAdmin());
		//newU.setUserPwd(u.getUserPwd());
		newU.setAvatar(this.userDmn.getUserAvatarLink(this.imgSerConf.getServerURI(), contextPath, baseURL, u));
		newU.setDn(u.getDn());
		logger.info(">>>获取用户 [{}] 所属机构，以及对应的角色。", newU.getLoginName());
		List<DcmUserdomainrole> udrs = this.udrDmn.getByUserCodeDomainType(newU.getUserCode(), new String[]{DomainType.DEPARTMENT, DomainType.INSTITUTE});
		if(null == udrs || 0 == udrs.size()) {
			return newU;
		}
		Map<String, DcmOrganization> orgMap = new HashMap<String, DcmOrganization>();
		long roleType = -1L;
		for(DcmUserdomainrole udr : udrs) {
			if(!orgMap.containsKey(udr.getDomainCode())) {
				orgMap.put(udr.getDomainCode(), this.orgDmn.getOrgByCode(udr.getDomainCode()));
			}
			switch (udr.getDomainType()) {
			case DomainType.INSTITUTE:
				roleType = RoleConstants.RoleType.T_Institute;
				break;
			case DomainType.DEPARTMENT:
				roleType = RoleConstants.RoleType.T_Department;
				break;
			default:
				break;
			}
			newU.addOrgAndRoles(orgMap.get(udr.getDomainCode()), this.roleService.getRoleByCode(roleType, udr.getRoleCode()));
		}
		return newU;
	}
	
	private DcmOrganization getInstituteByOrgInCache(DcmOrganization org) {
		
		DcmOrganizationDTO orgDTO = this.globalConf.openCache()? (DcmOrganizationDTO)this.redisCache.get(CacheKey.PREFIX_ORG + org.getOrgCode()):null;
		if(null == orgDTO){ 
		   return this.getInstitute(org);
		}
		
		DcmOrganizationDTO ins = (DcmOrganizationDTO)this.redisCache.get(CacheKey.PREFIX_ORG + orgDTO.getSuperParentCode());
		DcmOrganization belongInstitute = (null == ins)? this.getInstitute(orgDTO.getOrg()) : ins.getOrg();
		return belongInstitute;
	}

	@Override
	public DcmOrganization getInstituteByUserLoginName(String loginName) {
		
		DcmUser user = this.getUserEntityByLoginName(loginName);
		
		List<DcmOrganization> orgs = this.orgDmn.getOrgsByUserId(user.getId());
		if(orgs != null && !orgs.isEmpty()){
			return this.getInstituteByOrgInCache(orgs.get(0));
		}
		logger.warn("没有找到用户 [{}] 所属的部门......", user.getUserName());
		return null;
	}
	
	@Override
	public DcmOrganization getInstituteByUserSeqId(Long userSeqId) {
		
	    DcmUser user = this.getUserEntityById(userSeqId);
		
		List<DcmOrganization> orgs = this.orgDmn.getOrgsByUserId(user.getId());
		if(orgs != null && !orgs.isEmpty()){
			return this.getInstituteByOrgInCache(orgs.get(0));
		}
		logger.warn("没有找到用户 [{}] 所属的部门......", user.getUserName());
		return null;
	}

	@Override
	public List<UserSimpleDTO> listSimpleUsers(String contextPath, String baseURL) {
		
		List<DcmUser> users = this.listAllUsers();
		baseURL = baseURL.endsWith("/") ? baseURL : (baseURL + "/");
		List<UserSimpleDTO> newUsers = new ArrayList<UserSimpleDTO>();
		for (DcmUser u : users) {
			UserSimpleDTO newU = new UserSimpleDTO();
			newU.setId(u.getId());
			newU.setLoginName(u.getLoginName());
			newU.setUserName(u.getUserName());
			newU.setSex(u.getSex());
			newU.setDn(u.getDn());
			newU.setUserCode(u.getUserCode());
			newU.setAvatar(this.userDmn.getUserAvatarLink(this.imgSerConf.getServerURI(), contextPath, baseURL, u));

			newUsers.add(newU);
		}

		return newUsers;
		
	}

	@Override
	public List<UserSimpleDTO> listSimpleUsers(String contextPath, String baseURL, String realm) {
		
		List<DcmUser> users = this.listAllUsers(realm);
		baseURL = baseURL.endsWith("/") ? baseURL : (baseURL + "/");
		List<UserSimpleDTO> newUsers = new ArrayList<UserSimpleDTO>();
		String ceadmin = "ceadmin";
		boolean haveCeadmin = false;
		UserSimpleDTO newU = null;
		for (DcmUser u : users) {
			
			if(u.getCurrentStatus().equals(0L)) continue;
			
			if(u.getLoginName().equalsIgnoreCase(ceadmin)){
			   haveCeadmin = true;
			}
			
			newU = new UserSimpleDTO();
			newU.setId(u.getId());
			newU.setLoginName(u.getLoginName());
			newU.setUserName(u.getUserName());
			newU.setSex(u.getSex());
			newU.setDn(u.getDn());
			newU.setUserCode(u.getUserCode());
			newU.setAvatar(this.userDmn.getUserAvatarLink(this.imgSerConf.getServerURI(), contextPath, baseURL, u));

			newUsers.add(newU);
		}
		
		if(!haveCeadmin){
		    logger.info(">>>没有ceadmin，默认添加");
		    newU = new UserSimpleDTO();
			newU.setId(System.currentTimeMillis());
			newU.setLoginName(ceadmin);
			newU.setUserName(ceadmin);
			newU.setSex("男");
			newU.setUserCode(ceadmin);
			newUsers.add(newU);
		}
		return newUsers;
		
	}
	/*@Override
	public List<UserSimpleDTO> listSimpleInnerAndExternalUsersEx(String contextPath, String baseURL, String realm) {

		List<DcmUser> users = this.listAllUsers(realm);

		baseURL = baseURL.endsWith("/") ? baseURL : (baseURL + "/");

		List<UserSimpleDTO> newUsers = new ArrayList<UserSimpleDTO>();
		UserSimpleDTO newU = null;
		for (DcmUser u : users) {

			if (u.getCurrentStatus().equals(0L))
				continue;

			newU = new UserSimpleDTO();
			newU.setId(u.getId());
			newU.setLoginName(u.getLoginName());
			newU.setUserName(u.getUserName());
			newU.setSex(u.getSex());
			newU.setDn(u.getDn());
			newU.setUserCode(u.getUserCode());
			newU.setAvatar(this.userDmn.getUserAvatarLink(this.imgSerConf.getServerURI(), contextPath, baseURL, u));

			newUsers.add(newU);
		}
		try {
			// 公众版注册用户
			List<UserResponseDTO> sgaUsers = this.sgaUserService.listValidUsersByRealm(realm);
			if (sgaUsers != null && !sgaUsers.isEmpty()) {
				for (UserResponseDTO ur : sgaUsers) {
					newU = new UserSimpleDTO();
					newU.setId(ur.getId());
					newU.setLoginName(ur.getLoginName());
					newU.setUserName(ur.getUserName());
					newU.setSex("m".equalsIgnoreCase(ur.getSex()) ? "男" : "女");
					newU.setUserCode(ur.getSysCode());
					newU.setAvatar(ur.getAvatar());
					newUsers.add(newU);
				}
			}
		} catch (Exception e) {
			logger.error(">>>获取公众版用户信息失败，详情：{}", e.getMessage());
		}

		return newUsers;
	}*/

	@Deprecated
	@Override
	public Pagination pageUsers(PageParams pageParam, UserDTO user) {

		StringBuffer hql = new StringBuffer();
		hql.append("from " + DcmUser.class.getSimpleName() + "  where 1=1");
		String showName = user.getLoginName();
		if (!StringUtil.isNullOrEmpty(showName)) {
			hql.append(" and loginName like '%" + showName + "%'");
		}
		hql.append(" and isBuildin =0 and currentStatus=1 ");
		Pagination pagination = this.userDmn.query(pageParam.getPage(), pageParam.getPageSize(), hql.toString());

		return pagination;

	}

    private void remove(DcmUser user) {
		
		Assert.notNull(user);
		
		this.redisCache.remove(CacheKey.PREFIX_USER + user.getId());
		this.redisCache.remove(CacheKey.PREFIX_USER + user.getUserCode());
		this.redisCache.remove(CacheKey.PREFIX_USER + user.getLoginName());
		if(StringUtils.hasLength(user.getDn()))
			this.redisCache.remove(CacheKey.PREFIX_USER + user.getDn());
		
	}

	@Deprecated
	@Override
	public boolean delUsers(Object[] userIds) {

		for (Object id : userIds) {

			Assert.notNull(id);

			final DcmUser user = this.userDmn.loadById(Long.valueOf(id.toString()));
			//TODO 级联删除LDAP信息
			if (ldapConf.isLdapEnabledBoolean()) {
				this.ldapDmn.deleteByCName(user.getLoginName());
				/*this.ldapDmn.removeUsersFromLdapGroup(String.valueOf(LdapConfig.get("ldap.baseGroup")),
						new String[] { user.getLoginName() });*/
			}

			logger.debug(">>>删除用户本身信息......");
			this.userDmn.removeLogicByIds(Long.valueOf(id.toString()));
			
			if(this.globalConf.openCache()){
				DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
					
					@Override
					public void run() {
						logger.debug(">>>刷新缓存用户状态数据......");
						remove(user);
					}
				});	
			}
		}
		return true;
	}
	
	@Override
	public boolean delUsers(final UserDelRequestDTO dto) {

		for (Long id : dto.getUserIds()) {
		
			Assert.notNull(id);

			final DcmUser user = this.userDmn.loadById(id);
			//TODO 级联删除LDAP信息
			if (ldapConf.isLdapEnabledBoolean()) {
				this.ldapDmn.deleteInetOrgPerson("o="+dto.getRealm()+","+this.ldapConf.getBn(), user.getLoginName());
				/*this.ldapDmn.removeUsersFromLdapGroup(String.valueOf(LdapConfig.get("ldap.baseGroup")),
						new String[] { user.getLoginName() });*/
			}

			logger.debug(">>>删除用户本身信息......");
			this.userDmn.removeLogicByIds(id);
			logger.debug(">>>删除用户与任何机构关联的信息......");
			this.orgDmn.deleteUser(new Long[]{id});
			logger.debug(">>>删除用户与权限的信息......");
			this.udrDmn.removeByProperty("userId", id);
			logger.debug(">>>删除与用户相关的共享信息......");
			this.dcmShareDmn.delShareBySourceDomainCode(user.getUserCode());
			this.dcmShareDmn.delShareByTargetDomainCode(user.getUserCode());
			this.dcmShareDmn.delResourceBySharer(user.getUserCode());
			if(this.globalConf.openCache()){
				DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
					
					@Override
					public void run() {
						logger.debug(">>>刷新缓存用户状态数据......");
						remove(user);
					}
				});
			}		
		}
		if (ldapConf.isLdapEnabledBoolean()) {
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					logger.debug(">>>清理域[{}]的缓存数据......", dto.getRealm());
					redisCache.remove(CacheKey.CACHEKEY_REALM_USER+dto.getRealm());
				}
			});
		}
		return true;
	}

	@Override
	public DcmOrganization addOrg(OrgDTO orgDto) throws BusinessException {

		if (this.orgDmn.isExistOrgName(orgDto.getOrgName()))
			throw new BusinessException("已存在机构 [{}]，不能重复添加。", orgDto.getOrgName());

		final DcmOrganization org = new DcmOrganization();
		org.setParentId(null == orgDto.getParentId() ? -1l : orgDto.getParentId());
		org.setDomainType(orgDto.getDomaintype());
		org.setOrderId(orgDto.getOrderId());
		org.setOrgName(orgDto.getOrgName());
		org.setOrgCode(orgDto.getOrgCode());
		org.setOrgType(orgDto.getOrgType());
		org.setRealm(orgDto.getRealm());
		org.setAlias(StringUtil.isNullOrEmpty(orgDto.getAlias()) ? orgDto.getOrgName() : orgDto.getAlias());
		// 部门dn格式：cn=dgbgs,cn=gzpi,o=gzpi,dc=xiaozhi
		org.setDn(String.format("cn=%s,cn=%s,o=%s,%s", orgDto.getOrgName(), orgDto.getRealm(), orgDto.getRealm(),
				this.ldapConf.getBn()));

		if (ldapConf.isLdapEnabledBoolean()) {

			String groupRootDn = String.format("cn=%s,o=%s,%s", orgDto.getRealm(), orgDto.getRealm(),
					this.ldapConf.getBn());// , groupCnValue;

			if (this.ldapDmn.isExistGroupOfname(groupRootDn, orgDto.getOrgName()))
				throw new BusinessException(String.format("LDAP中机构 [%s] 已存在，不能重复添加。", orgDto.getOrgName()));

			Map<String, String> properties = new HashMap<String, String>();
			properties.put("displayName", org.getAlias());

			/*
			 * List<String> memberDNs = new ArrayList<String>();
			 * memberDNs.add("cn=gzpi_nizhh,o=gzpi,DC=DIST");
			 */
			this.ldapDmn.addGroup(groupRootDn, org.getOrgName(), org.getOrgCode(), orgDto.getGroupType(), properties,
					null);

			this.orgDmn.add(org);

			if (this.globalConf.openCache()) {
				// 同步到缓存去
				DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
					
					@Override
					public void run() {
						DcmOrganizationDTO orgDTO = DcmOrganizationDTO.clone(org);
						orgDTO.setSuperParentCode(getInstituteCode(org));
						cacheOrg(orgDTO);
					}
				});
			}
		}

		return org;
	}

	@Override
	@Deprecated
	public Object listOrgsInCache() {

		logger.info("从缓存中获取机构信息......");
		return null;//this.cacheManager.getCacheOrgCodeMap().values();
		
		//return this.orgDmn.find();
	}

	@Deprecated
	@Override
	public void addUsersToOrg(OrgUserDTO dto) {

		this.orgDmn.addUsers(Long.valueOf(dto.getOrgId()), dto.getUserIds());
		if (ldapConf.isLdapEnabledBoolean()) {
			List<DcmUser> users = this.userDmn.findByProperty("id", dto.getUserIds());
			if (null == users || 0 == users.size()) {
				return;
			}
			String[] userNames = new String[users.size()];
			for (int i = 0; i < users.size(); i++) {
				userNames[i] = users.get(i).getLoginName();
			}
			DcmOrganization org = this.getOrgById(dto.getOrgId());
			if (null == org)
				throw new BusinessException(String.format("没有找到机构id为【%s】的信息......", dto.getOrgId()));

			this.ldapDmn.addUsersToLdapGroup(org.getOrgName(), userNames);
		}
	}

	@Override
	public List<DcmOrganization> listDirectChildOrgs(Long orgId) {

		return this.orgDmn.listDirectChildOrgs(orgId);
	}

	@Override
	public void delUsersFromOrg(OrgUserDTO dto) throws BusinessException {

		this.orgDmn.deleteUser(dto.getOrgId(), dto.getUserIds());
		
		if (ldapConf.isLdapEnabledBoolean()) {
			
			List<DcmUser> users = this.userDmn.findByProperty("id", dto.getUserIds());
			if (null == users || 0 == users.size()) {
				return;
			}
			String[] userNames = new String[users.size()];
			for (int i = 0; i < users.size(); i++) {
				userNames[i] = users.get(i).getDn();
			}
			DcmOrganization org = this.getOrgById(dto.getOrgId());//.orgDmn.loadById(dto.getOrgId());
			if (null == org)
				throw new BusinessException(String.format("没有找到机构id为【%s】的信息......", dto.getOrgId()));

			this.ldapDmn.removeUsersFromLdapGroup(org.getDn(), userNames);
		}
	}

	@Override
	public List<UserDTO> listDirectUsersOfOrg(Long orgId) {

		List<DcmOrgUser> orgusers = this.orgDmn.getOrgUserRef(orgId);
		if(null == orgusers || orgusers.isEmpty()){
			return null;
		}
		
		 List<UserDTO> userDTOs = new ArrayList<UserDTO>();
		for(DcmOrgUser ou : orgusers){
			
			DcmUser user =  this.getUserEntityById(ou.getUserId());
			
			userDTOs.add(this.userModelToUserDTO("", "" , user));
		}
		return userDTOs;
		
		//return this.orgDmn.listDirectUsersOfOrg(orgId);
	}
	@Override
	public List<UserDTO> listDirectUsersOfOrg(String contextPath, String baseURL, Long orgId) {
		
		List<DcmOrgUser> orgusers = this.orgDmn.getOrgUserRef(orgId);
		if(null == orgusers || orgusers.isEmpty()){
			return null;
		}
		
		 List<UserDTO> userDTOs = new ArrayList<UserDTO>();
		for(DcmOrgUser ou : orgusers){
			
			DcmUser user =  this.getUserEntityById(ou.getUserId());
			if(null == user){
				logger.warn("找不到用户 [{}] 的信息，请检查数据库机构与用户关联表是否存在脏数据......", ou.getUserId());
				continue;
			}
			if(user.getCurrentStatus().equals(0L)){
				logger.warn("用户 [{}] 的状态已标识删除......", ou.getUserId());
				continue;
			}
			
			userDTOs.add(this.userModelToUserDTO(contextPath, baseURL, user));
		}
		return userDTOs;
	}

	@Override
	public List<DcmUser> listUsersOfOrg(Long orgId) {

		return this.orgDmn.listUsersOfOrg(orgId);
	}

	@Override
	public String uploadUserAvatar(String contextPath, String baseURL, String loginName, String avatarRelativePath)  throws BusinessException{

		DcmUser findUser = this.userDmn.findByLoginName(loginName);
		if (null == findUser) {
			throw new BusinessException(String.format("登录名为【%s】的用户不存在", loginName));
		}
		File preAvatarPath = new File(contextPath + "/" + findUser.getAvatar());
		if (preAvatarPath.exists()) {
			// 删除原来的头像
			boolean result = preAvatarPath.delete();
			if (!result) {
				System.gc();
				preAvatarPath.delete();
			}
		}
		findUser.setAvatar(avatarRelativePath);
		this.userDmn.modify(findUser);

		String avatarUrl = baseURL.endsWith("/") ? (baseURL + avatarRelativePath)
				: (baseURL + "/" + avatarRelativePath);

		return avatarUrl;
	}

	@Override
	public List<Org2UsersDTO> getOrgUserTree(boolean loadUser) {

		return getOrgUserTree(loadUser, true);
	}
	
	/**
	 * 
	 * @param loadUser
	 * @param refreshCache 是否刷新缓存
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Org2UsersDTO> getOrgUserTree(boolean loadUser, boolean refreshCache) {

		List<Org2UsersDTO> result = null;
		String cacheKey = "";
		if(loadUser){
			cacheKey = CacheKey.PREFIX_ORG_USER_TREE_LOADUSER;
			
		} else {
			cacheKey = CacheKey.PREFIX_ORG_USER_TREE_NOTLOADUSER;
			
		}
		if(refreshCache){
			result = this.orgDmn.getAllOrgAndUser(loadUser);
			if(result != null){
				
				logger.info(">>>add OrgUserTree to temp cache, loadUser [{}]", loadUser);
				this.redisCache.set(cacheKey, result);
			}
			return result;
			
		} else {
			result = (List<Org2UsersDTO>) this.redisCache.getList(cacheKey);
			if(null == result) {
				
				result = this.orgDmn.getAllOrgAndUser(loadUser);
				if(result != null){
					logger.info(">>>add OrgUserTree to temp cache, loadUser [{}]", loadUser);
					this.redisCache.setList(cacheKey, result);
				}

			} else {
				logger.info(">>>get OrgUserTree from temp cache...");
			}
			return result;
		}
		
	}

	@Override
	public List<Org2UsersDTO> getOrgUserTree(boolean loadUser, String instituteUniqueName) {
		
		return this.orgDmn.getAllOrgAndUser(loadUser, instituteUniqueName);
	}
	@Override
	public List<Org2UsersDTO> getCurrOrgUserTree(String loginName, boolean loadUser) {

		List<DcmOrganization> orgs = new ArrayList<DcmOrganization>();
		DcmOrganization institute = this.getInstituteByUserLoginName(loginName);
		orgs.add(institute);
		orgs.addAll(this.listDepartments(institute.getId()));

		return this.orgDmn.getAllOrgAndUser(orgs, loadUser);
	}
	@Override
	public List<Org2UsersDTO> getCurrOrgUserTree(Long userSeqId, boolean loadUser) {
		
		try{
		List<DcmOrganization> orgs = new ArrayList<DcmOrganization>();
		DcmOrganization institute = this.getInstituteByUserSeqId(userSeqId);
		orgs.add(institute);
		orgs.addAll(this.listDepartments(institute.getId()));

		List<Org2UsersDTO> result = this.orgDmn.getAllOrgAndUser(orgs, loadUser);
		return result;
		}catch(Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
		}
		return null;
	}
	@Override
	public boolean checkUserInfo(String loginName, String password) {
		
		if (ldapConf.isLdapEnabledBoolean()) {
			return this.ldapDmn.checkUser(loginName, password);
		}
		/*DcmUser findUser = this.userDmn.findUniqueByProperties(new String[] { "loginName", "userPwd" },
				new Object[] { loginName, DesEncryptDap.getInstance().Encrypt(password) });*/
		DcmUser findUser = this.userDmn.findUniqueByProperties(new String[] { "loginName", "userPwd" },
				new Object[] { loginName, password });

		return (null != findUser);
	}
	@Override
	public boolean checkNewUserLoginNameValid(String loginName) {
		
		DcmUser findUser = this.userDmn.findByLoginNameSimply(loginName);
		
		return (null == findUser);
		
	}
	@Override
	public boolean checkNewUserLoginNameValid(String rootOrgRealm, String loginName) {
		
		LdapPerson ldapPer = this.ldapDmn.searchUniquePerson("o="+rootOrgRealm+","+this.ldapConf.getBn(), loginName);
		
		return (null == ldapPer);
	}
	//<=====================LDAP-TDS Begin======================

	@Override
	public void batchAddUserToLdap(List<DcmUser> userList) {

		if (userList != null && userList.size() > 0) {
			for (DcmUser user : userList) {
				String name = user.getLoginName();
				String firstName = user.getUserName();
				String password = user.getUserPwd();
				try {
					ldapDmn.addUser(name, firstName, password);
				} catch (Exception e) {
					String msg = "添加用户" + name + "失败";
					logger.info(msg, e);
				}
			}
		}
	}

	@Override
	public Collection<LdapPerson> getLdapPersons() {

       return this.ldapDmn.searchLdapPersonsMap().values();
	}
	

	@SuppressWarnings("unchecked")
	@Override
	 public Collection<LdapTree> getLdapGroups() {
		
		return (Collection<LdapTree>) this.ldapDmn.searchCurrGroups();
	}
	@Deprecated
	@Override
	public void syncUserInfoFromLdap() {

		Collection<LdapPerson> persons = this.getLdapPersons();
		syncUserInfoFromLdapInner(persons);
	}
	/**
	 * 内部函数
	 * @param persons
	 * @return 返回dn和dcmuser模型的key-value字典
	 */
	private Map<String, DcmUser> syncUserInfoFromLdapInner(Collection<LdapPerson> persons) {

		Map<String, DcmUser> dn2userMap = new HashMap<String, DcmUser>();
		
		for (LdapPerson entry : persons) {
			DcmUser findUser = this.userDmn.findByLoginNameSimply(entry.getCname());

			if (findUser != null) {
				// 如果存在，则更新用户的密码
				//findUser.setUserPwd(DesEncryptDap.getInstance().Encrypt(entry.getUserPassword()));
				findUser.setUserPwd(entry.getUserPassword());
				findUser.setSex(entry.getSex());
				findUser.setUserName(entry.getAliasName());
				findUser.setDn(entry.getDn().toLowerCase());
				this.userDmn.modify(findUser);
				
			} else {
				// 如果不存在，则新增用户信息
				findUser = new DcmUser();
				findUser.setLoginName(entry.getCname());
				findUser.setUserName(entry.getAliasName());
				findUser.setSex(entry.getSex());
				findUser.setAvatar(this.userDmn.getUserAvatarLink("", "", findUser));
				
				//findUser.setUserPwd(DesEncryptDap.getInstance().Encrypt(entry.getUserPassword()));
				findUser.setUserPwd(entry.getUserPassword());
				findUser.setCurrentStatus(1L);
				findUser.setDateCreated(new Date());
				updateUserLastActiveDateThread(findUser);
				findUser.setDomainType(DomainType.PERSON);
				findUser.setDn(entry.getDn().toLowerCase());

				this.userDmn.addUser(findUser);
			}
			
			dn2userMap.put(entry.getDn(), findUser);
		}
		
		this.refreshUserCacheInfos();

		return dn2userMap;
	}
	
	@Deprecated
	private Map<String, DcmUser> syncUserInfoFromLdapByDNInner(Collection<LdapPerson> persons) {

		Map<String, DcmUser> dn2userMap = new HashMap<String, DcmUser>();
		
		for (LdapPerson entry : persons) {
			DcmUser findUser = this.userDmn.findByDN(entry.getDn());

			if (findUser != null) {
				// 如果存在，则更新用户的密码
				//findUser.setUserPwd(DesEncryptDap.getInstance().Encrypt(entry.getUserPassword()));
				findUser.setUserPwd(entry.getUserPassword());
				findUser.setSex(entry.getSex());
				findUser.setUserCode(entry.getUid());
				findUser.setUserName(entry.getAliasName());
				findUser.setDn(entry.getDn().toLowerCase());
				this.userDmn.modify(findUser);
				
			} else {
				// 如果不存在，则新增用户信息
				findUser = new DcmUser();
				findUser.setLoginName(entry.getCname());
				findUser.setUserName(entry.getAliasName());
				findUser.setSex(entry.getSex());
				findUser.setUserCode(entry.getUid());
				findUser.setAvatar(this.userDmn.getUserAvatarLink("", "", findUser));
				
				//findUser.setUserPwd(DesEncryptDap.getInstance().Encrypt(entry.getUserPassword()));
				findUser.setUserPwd(entry.getUserPassword());
				findUser.setCurrentStatus(1L);
				findUser.setDateCreated(new Date());
				updateUserLastActiveDateThread(findUser);
				findUser.setDomainType(DomainType.PERSON);
				findUser.setDn(entry.getDn().toLowerCase());

				this.userDmn.addUser(findUser);
			}
			
			dn2userMap.put(entry.getDn(), findUser);
		}
		
		this.refreshUserCacheInfos();

		return dn2userMap;
	}
	
	private Map<String, DcmUser> syncUserInfoFromLdapByCode(Collection<LdapPerson> persons) {

		Map<String, DcmUser> dn2userMap = new HashMap<String, DcmUser>();
		String loginName = "";
		for (LdapPerson entry : persons) {
			DcmUser findUser = this.userDmn.findByCode(entry.getUid());

			if (findUser != null) {
				// 如果存在，则更新用户的密码
				findUser.setUserPwd(DesEncryptAES.getInstance().encrypt(entry.getUserPassword()));
				findUser.setSex(entry.getSex());
				findUser.setUserCode(entry.getUid());
				findUser.setUserName(entry.getAliasName());
				findUser.setLoginName(entry.getCname());
				findUser.setDn(entry.getDn().toLowerCase());
				loginName = findUser.getLoginName();
				//findUser.setAvatar(this.userDmn.getUserAvatarLink("", "", findUser));
				String[] split = loginName.split("_");
				if(2 == split.length){
					findUser.setRealm(split[0]);
				}
		
				this.userDmn.modify(findUser);
				
			} else {
				// 如果不存在，则新增用户信息
				findUser = new DcmUser();
				findUser.setLoginName(entry.getCname());
				findUser.setUserName(entry.getAliasName());
				findUser.setSex(entry.getSex());
				findUser.setUserCode(entry.getUid());
				findUser.setAvatar(this.userDmn.getUserAvatarLink("", "", findUser));
				findUser.setSuperAdmin(0);
				//findUser.setUserPwd(entry.getUserPassword());
				findUser.setUserPwd(DesEncryptAES.getInstance().encrypt(entry.getUserPassword()));
				findUser.setCurrentStatus(1L);
				findUser.setDateCreated(new Date());
				updateUserLastActiveDateThread(findUser);
				findUser.setDomainType(DomainType.PERSON);
				findUser.setDn(entry.getDn().toLowerCase());

				loginName = findUser.getLoginName();
				String[] split = loginName.split("_");
				if(2 == split.length){
					findUser.setRealm(split[0]);
				}
				
				this.userDmn.addUser(findUser);
			}
			
			dn2userMap.put(entry.getDn(), findUser);
		}
		
		this.refreshUserCacheInfos();

		return dn2userMap;
	}

	@SuppressWarnings("unchecked")
	@Deprecated
	@Override
	public void syncOrganizationInfoFromLdap() {
		
		List<DcmOrganization> orgs = this.orgDmn.getAllSimpleOrg();
		List<DcmUser> users = this.userDmn.list();
		
		Map<String, DcmOrganization> preOrgs = new HashMap<String, DcmOrganization>();
		Map<String, DcmUser> usersMap = new HashMap<String, DcmUser>();
		//Map<Long, DcmOrganization> newOrgsKeyId = new HashMap<Long, DcmOrganization>();
		Map<String, DcmOrganization> newOrgsKeyName = new HashMap<String, DcmOrganization>();
		List<DcmOrganization> modifiedOrgs = new ArrayList<DcmOrganization>();
		List<DcmOrgUser> org2user = new ArrayList<DcmOrgUser>();
		
		for(DcmOrganization org : orgs){
			preOrgs.put(org.getOrgName(), org);
		}
		for(DcmUser u : users){
			usersMap.put(u.getLoginName(), u);
		}
		Collection<LdapTree> groups = (Collection<LdapTree>) this.ldapDmn.searchCurrGroups();
		long i = -1;
		for(LdapTree lt : groups) {
			i++;
			if(lt.getGroupType() != null && lt.getGroupType().equalsIgnoreCase("r")){
				// 忽略角色类型的组
				continue;
			}
			String name = lt.getCn().split("=")[1];
			boolean isExistOrgName = preOrgs.keySet().contains(name);
			if(isExistOrgName){
				
				DcmOrganization oldOrg = preOrgs.get(name);
				
				// 修改
				if(lt.getParentTree() != null){
					// 不为空，说明父节点肯定是存在的情况
					oldOrg.setParentId(newOrgsKeyName.get(lt.getParentTree().getCn().split("=")[1]).getId());	
				}
				newOrgsKeyName.put(name, oldOrg);
				modifiedOrgs.add(oldOrg);
			
				preOrgs.remove(name);
			}else{
				// 新增
				DcmOrganization newOrg = new DcmOrganization();
				OrgDTO orgDTO = null;
				if(null == lt.getGroupType()){
					//
					orgDTO = new OrgDTO() {
						
						@Override
						public String getDomaintype() {
							
							return "root";
						}				
						@Override
						public String getDomainname() {
					
							return "root";
						}
						@Override
						public String getGroupType() {
						
							return null;
						}
					};
				}
				else if(lt.getGroupType().equalsIgnoreCase("i")){
					orgDTO = new InstituteDTO();

				}else if(lt.getGroupType().equalsIgnoreCase("d")){
					orgDTO = new DepartmentDTO();
				}
				newOrg.setDomainType(orgDTO.getDomaintype());
				newOrg.setOrgCode(StringUtil.isNullOrEmpty(lt.getGroupCode())? orgDTO.getOrgCode() : lt.getGroupCode());
				newOrg.setOrgName(name);
				newOrg.setAlias(lt.getAliasName());
				newOrg.setOrderId(i);
		
				if(lt.getParentTree() != null){
					newOrg.setParentId(newOrgsKeyName.get(lt.getParentTree().getCn().split("=")[1]).getId());
				}else{
					newOrg.setParentId(-1L);
				}
				newOrg = this.orgDmn.add(newOrg);
				//newOrgsKeyId.put(newOrg.getId(), newOrg);
				newOrgsKeyName.put(name, newOrg);
			}
			String[] members = lt.getMembers();
			if (members != null && members.length > 0) {
				for (String mem : members) {
					String cnValue = mem.split(",")[0].split("=")[1];
					if (!usersMap.containsKey(cnValue)) {
						System.err.println(String.format("数据库缺少用户：【%s】，请先同步ldap用户信息。", cnValue));
						continue;
					}
					DcmOrgUser ou = new DcmOrgUser();
					ou.setOrgId(newOrgsKeyName.get(name).getId());
					ou.setUserId(usersMap.get(cnValue).getId());
					org2user.add(ou);
				}
			}
		}
		this.orgDmn.batchSaveOrUpdate(modifiedOrgs);
		this.orgDmn.remove(preOrgs.values());
		this.orgDmn.clearAllOrg2User();
		this.orgDmn.addOrg2User(org2user);
					
	}
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public void syncFromLdap() {

		// 先同步用户信息
		this.syncUserInfoFromLdap();
				
		Map<String, DcmOrganization> newOrgsKeyName = new HashMap<String, DcmOrganization>();
		//Map<String, DcmUser> usersMap = new HashMap<String, DcmUser>();
		List<DcmOrgUser> org2user = new ArrayList<DcmOrgUser>();
		
		// 获取同步后的用户信息
	/*	List<DcmUser> users = this.userDmn.list();
		for (DcmUser u : users) {
			usersMap.put(u.getLoginName(), u);
		}*/
		// 删除机构信息和机构用户关联信息
		this.orgDmn.removeAll();
		this.orgDmn.clearAllOrg2User();

		Collection<LdapTree> groups = (Collection<LdapTree>) this.ldapDmn.searchCurrGroups();
		long i = -1;
		for (LdapTree lt : groups) {
			i++;
			if (lt.getGroupType() != null && lt.getGroupType().equalsIgnoreCase("r")) {
				// 忽略角色类型的组
				continue;
			}
			String name = lt.getCn().split("=")[1];
			// 新增
			DcmOrganization newOrg = new DcmOrganization();
			OrgDTO orgDTO = null;
			if (null == lt.getGroupType()) {
				//
				orgDTO = new OrgDTO() {

					@Override
					public String getDomaintype() {

						return "root";
					}

					@Override
					public String getDomainname() {

						return "root";
					}

					@Override
					public String getGroupType() {
	
						return null;
					}
				};
			} else if (lt.getGroupType().equalsIgnoreCase("i")) {
				// 院
				orgDTO = new InstituteDTO();

			} else if (lt.getGroupType().equalsIgnoreCase("d")) {
				// 部门/所
				orgDTO = new DepartmentDTO();
			}
			newOrg.setDomainType(orgDTO.getDomaintype());
			newOrg.setOrgCode(StringUtil.isNullOrEmpty(lt.getGroupCode()) ? orgDTO.getOrgCode() : lt.getGroupCode());
			newOrg.setOrgName(name);
			newOrg.setAlias(lt.getAliasName());
			newOrg.setOrderId(i);

			if (lt.getParentTree() != null) {
				newOrg.setParentId(newOrgsKeyName.get(lt.getParentTree().getCn().split("=")[1]).getId());
			} else {
				newOrg.setParentId(-1L);
			}
			newOrg = this.orgDmn.add(newOrg);

			newOrgsKeyName.put(name, newOrg);
			// 机构成员
			//ldapGroup2User(newOrgsKeyName, org2user, this.cacheManager.getCacheUserNameMap() , lt, name);
		}

		this.orgDmn.addOrg2User(org2user);
		// 重新刷新内存
		this.refreshUserOrgCacheInfos();
	}
	
	@Deprecated
	@Override
	public  void syncFromLdapEx() {
		
		// -->流程
		// 1 单纯同步用户，这个可以复用以前的接口
		    // 1.1 获取用户字典
		Map<String, LdapPerson> personsMap = this.ldapDmn.searchLdapPersonsMap();
		Map<String, DcmUser> dn2userMap = this.syncUserInfoFromLdapByDNInner(personsMap.values());
		
		// 清理机构，机构和人员关联信息
		this.orgDmn.removeAll();
		this.orgDmn.clearAllOrg2User();

		// 2 检索院和所信息
		   // 2.1 获取院字典和所字典
		OrgDTO orgDTO = null;
		Long i = 0L;
		Map<String, LdapTree> institutesMap = this.ldapDmn.searchInstituteGroupsMap();
		Map<String, LdapTree> departmentMap = this.ldapDmn.searchDepartmentGroupsMap();
		Set<String> keyset = institutesMap.keySet();
		for(String key : keyset){

			LdapTree instituteLdap = institutesMap.get(key);
			//String name = instituteLdap.getCn().split("=")[1];
			// 院
			orgDTO = new InstituteDTO();
			DcmOrganization instituteEntity = new DcmOrganization();
			instituteEntity.setDomainType(orgDTO.getDomaintype());
			instituteEntity.setOrgCode(StringUtil.isNullOrEmpty(instituteLdap.getGroupCode()) ? orgDTO.getOrgCode() : instituteLdap.getGroupCode());
			instituteEntity.setOrgName(instituteLdap.getCn());
			instituteEntity.setAlias(instituteLdap.getAliasName());
			instituteEntity.setOrderId(i);
			instituteEntity.setParentId(-1L);

			i ++;
			instituteEntity = this.orgDmn.add(instituteEntity);
			// 院下的人员或者机构
			this.recursionLdapTree(instituteLdap, instituteEntity, dn2userMap, departmentMap);
		}
		
	}
	/**
	 * 递归检索ldap信息
	 * @param ldap
	 * @param orgEntity
	 * @param dn2userMap
	 * @param departmentMap
	 */
	private void recursionLdapTree(LdapTree ldap, DcmOrganization orgEntity, Map<String, DcmUser> dn2userMap, Map<String, LdapTree> departmentMap) {
		
		String[] members = ldap.getMembers();
		if(null == members || 0 == members.length) return;
		
		for(int j =0;j<members.length; j++){
			
			String keyLowerCase = members[j].toLowerCase();
			// 根据dn，判断是个人，还是部门
			if(dn2userMap.containsKey(keyLowerCase)){
				// 人，建立院和人的关联关系
				DcmOrgUser org2user = new DcmOrgUser();
				org2user.setUserId(dn2userMap.get(keyLowerCase).getId());
				org2user.setOrgId(orgEntity.getId());

				this.orgDmn.addOrg2User(org2user);
				
			}else if(departmentMap.containsKey(keyLowerCase)){
				
				LdapTree departmentLdap = departmentMap.get(keyLowerCase);
				// 部门，建立院与部门的层级关系
				DcmOrganization departmentEntity = new DcmOrganization();
				departmentEntity.setDomainType(DomainType.DEPARTMENT);
				departmentEntity.setOrgCode(departmentLdap.getGroupCode());
				departmentEntity.setOrgName(departmentLdap.getCn());
				departmentEntity.setAlias(departmentLdap.getAliasName());
				departmentEntity.setOrderId(Long.valueOf(j));
				departmentEntity.setParentId(orgEntity.getId());
				
				departmentEntity = this.orgDmn.add(departmentEntity);

				this.recursionLdapTree(departmentLdap, departmentEntity, dn2userMap, departmentMap);
			}
		}
	}
	
   private void recursionLdapTree(List<LdapOrg> departments, DcmOrganization parentOrg, Map<String, DcmUser> dn2userMap) {
		
	   if(null == departments || departments.isEmpty()) return ;
	   
	   OrgDTO orgDTO = new DepartmentDTO();
	   Long i = 0L;
	   for(LdapOrg dep : departments){
	
			DcmOrganization departmentEntity = new DcmOrganization();
			departmentEntity.setDomainType(orgDTO.getDomaintype());
			departmentEntity.setOrgCode(StringUtil.isNullOrEmpty(dep.getUid()) ? orgDTO.getOrgCode() : dep.getUid());
			departmentEntity.setOrgName(dep.getName());
			departmentEntity.setAlias(dep.getAlias());
			departmentEntity.setOrderId(i);
			departmentEntity.setParentId(parentOrg.getId());
			departmentEntity.setDn(dep.getDn().toLowerCase());
			
			departmentEntity = this.orgDmn.add(departmentEntity);
			i++;
			List<LdapOrg> subDepartments = this.ldapDmn.searchDepartments(dep.getDn());
			recursionLdapTree(subDepartments, departmentEntity, dn2userMap);
			
			List<LdapPerson> persons = this.ldapDmn.searchPersons(dep.getDn());
			if(persons != null && !persons.isEmpty()){
				for(LdapPerson per : persons){
					// 人，建立院和人的关联关系
					DcmOrgUser org2user = new DcmOrgUser();
					org2user.setUserId(dn2userMap.get(per.getDn()).getId());
					org2user.setOrgId(departmentEntity.getId());

					this.orgDmn.addOrg2User(org2user);
				}
			}
	   }
	
	}
   /**
    * 
    * @param departments
    * @param parentOrg
    * @param dn2userMap
    */
   private void recursionLdapGroupofnameTree(List<LdapOrg> departments, DcmOrganization parentOrg, Map<String, DcmUser> dn2userMap) {
		
	   if(null == departments || departments.isEmpty()) return ;
	   
	   OrgDTO orgDTO = new DepartmentDTO();
	   Long i = 0L;
	   for(LdapOrg dep : departments){
	
			DcmOrganization departmentEntity = new DcmOrganization();
			departmentEntity.setDomainType(orgDTO.getDomaintype());
			departmentEntity.setOrgCode(StringUtil.isNullOrEmpty(dep.getUid()) ? orgDTO.getOrgCode() : dep.getUid());
			departmentEntity.setOrgName(dep.getName());
			departmentEntity.setAlias(dep.getAlias());
			departmentEntity.setOrderId(i);
			departmentEntity.setParentId(parentOrg.getId());
			departmentEntity.setDn(dep.getDn().toLowerCase());
			departmentEntity.setRealm(parentOrg.getRealm());
			
			departmentEntity = this.orgDmn.add(departmentEntity);
			i++;
			List<LdapOrg> subDepartments = this.ldapDmn.searchDepartmentsDefinedByGroupofname(dep.getDn());
			recursionLdapTree(subDepartments, departmentEntity, dn2userMap);
			
			String[] members = dep.getMembers();
			if(members != null && members.length > 0 ){
				for(String mem : members){
					// 人，建立院和人的关联关系
					DcmOrgUser org2user = new DcmOrgUser();
					DcmUser cacheUser = dn2userMap.get(mem.toLowerCase());
					if(cacheUser != null){
						org2user.setUserId(cacheUser.getId());
						org2user.setOrgId(departmentEntity.getId());

						this.orgDmn.addOrg2User(org2user);
					}
					
				}
			}
			
	   }
	
	}

   @Deprecated
	@Override
	public void syncFromLdapForCloud() {
		
		Map<String, DcmUser> dn2userMap = this.syncUserInfoFromLdapByDNInner(this.ldapDmn.searchAllPersons());
		
		// 清理机构，机构和人员关联信息
		this.orgDmn.removeAll();
		this.orgDmn.clearAllOrg2User();
		
		OrgDTO orgDTO = null;
		Long i = 0L;
		List<LdapOrg> institutes = this.ldapDmn.searchInstitutes();
		for(LdapOrg institute : institutes) {
			
			// 遍历院
			orgDTO = new InstituteDTO();
			DcmOrganization instituteEntity = new DcmOrganization();
			instituteEntity.setDomainType(orgDTO.getDomaintype());
			instituteEntity.setOrgCode(StringUtil.isNullOrEmpty(institute.getUid()) ? orgDTO.getOrgCode() : institute.getUid());
			instituteEntity.setOrgName(institute.getName());
			instituteEntity.setAlias(institute.getAlias());
			instituteEntity.setOrderId(i);
			instituteEntity.setDn(institute.getDn().toLowerCase());
			instituteEntity.setParentId(-1L);

			i++;
			instituteEntity = this.orgDmn.add(instituteEntity);
			
			// 检索院下的所机构
			List<LdapOrg> departments = this.ldapDmn.searchDepartments(institute.getDn());
			recursionLdapTree(departments, instituteEntity, dn2userMap);
	
		}
		
	
		this.refreshUserOrgCacheInfos();
	}
	
	@Override
	public void syncFromLdapForCloudEx() {

		// 同步用户信息
		Map<String, DcmUser> dn2userMap = this.syncUserInfoFromLdapByCode(this.ldapDmn.searchAllPersons());

		// 清理机构，机构和人员关联信息
		this.orgDmn.removeAll();
		this.orgDmn.clearAllOrg2User();

		OrgDTO orgDTO = null;
		Long i = 0L;
		List<LdapOrg> institutes = this.ldapDmn.searchInstitutes();
		for (LdapOrg institute : institutes) {

			// 遍历院
			orgDTO = new InstituteDTO();
			DcmOrganization instituteEntity = new DcmOrganization();
			instituteEntity.setDomainType(orgDTO.getDomaintype());
			instituteEntity.setOrgCode(
					StringUtil.isNullOrEmpty(institute.getUid()) ? orgDTO.getOrgCode() : institute.getUid());
			instituteEntity.setOrgName(institute.getName());
			instituteEntity.setAlias(institute.getAlias());
			instituteEntity.setOrderId(i);
			instituteEntity.setDn(institute.getDn().toLowerCase());
			instituteEntity.setParentId(-1L);
			instituteEntity.setRealm(institute.getName());

			i++;
			instituteEntity = this.orgDmn.add(instituteEntity);

			// 检索院下的所机构
			String rootGroupDn = "cn=" + institute.getName()+ "," + institute.getDn();
			List<LdapOrg> departments = this.ldapDmn.searchDepartmentsDefinedByGroupofname(rootGroupDn);
			recursionLdapGroupofnameTree(departments, instituteEntity, dn2userMap);

		}

		this.refreshUserOrgCacheInfos();
	}

	@Override
	public void syncFromLdapForCloudEx(String realm) {
		// 同步用户信息
		String bn = String.format("o=%s,%s", realm, this.ldapConf.getBn());
		Map<String, DcmUser> dn2userMap = this.syncUserInfoFromLdapByCode(this.ldapDmn.searchAllPersons(bn));

		// 清理机构，机构和人员关联信息
		this.orgDmn.removeAll(realm);

		OrgDTO orgDTO = null;
		Long i = 0L;
		//TODO 应该精确查询院信息，o=realm,DC=XIAOZHI
		List<LdapOrg> institutes = this.ldapDmn.searchInstitutes(); 
		for (LdapOrg institute : institutes) {

			if (!institute.getName().equalsIgnoreCase(realm))
				continue;

			// 遍历院
			orgDTO = new InstituteDTO();
			DcmOrganization instituteEntity = new DcmOrganization();
			instituteEntity.setDomainType(orgDTO.getDomaintype());
			instituteEntity.setOrgCode(
					StringUtil.isNullOrEmpty(institute.getUid()) ? orgDTO.getOrgCode() : institute.getUid());
			instituteEntity.setOrgName(institute.getName());
			instituteEntity.setAlias(institute.getAlias());
			instituteEntity.setOrderId(i);
			instituteEntity.setDn(institute.getDn().toLowerCase());
			instituteEntity.setParentId(-1L);
			instituteEntity.setRealm(institute.getName());

			i++;
			instituteEntity = this.orgDmn.add(instituteEntity);

			// 检索院下的所机构
			String rootGroupDn = "cn=" + institute.getName() + "," + institute.getDn();
			List<LdapOrg> departments = this.ldapDmn.searchDepartmentsDefinedByGroupofname(rootGroupDn);
			recursionLdapGroupofnameTree(departments, instituteEntity, dn2userMap);

		}

		this.refreshUserOrgCacheInfos();
	}
	/**
	 * 机构组下的用户关联信息
	 * @param newOrgsKeyName
	 * @param org2user
	 * @param usersMap
	 * @param lt
	 * @param name
	 */
	@Deprecated
	private void ldapGroup2User(Map<String, DcmOrganization> newOrgsKeyName, List<DcmOrgUser> org2user,
			Map<String, DcmUser> usersMap, LdapTree lt, String name) {
		
		String[] members = lt.getMembers();
		if (members != null && members.length > 0) {
			for (String mem : members) {
				String cnValue = mem.split(",")[0].split("=")[1];
				if (!usersMap.containsKey(cnValue)) {
					System.err.println(String.format("数据库缺少用户：【%s】，请先同步ldap用户信息。", cnValue));
					continue;
				}
				DcmOrgUser ou = new DcmOrgUser();
				ou.setOrgId(newOrgsKeyName.get(name).getId());
				ou.setUserId(usersMap.get(cnValue).getId());
				org2user.add(ou);
			}
		}
	
	}
	@Deprecated
	@Override
	public void syncToLdap() {

	/*	if (ldapConf.isLdapEnabledBoolean()) {
		    // 同步用户信息
			syncUsersToLdap();
			// 同步机构信息
			syncOrgsToLdap();
		}
*/
	}
	
	@Override
	public void syncToLdapEx(String realm) {

		if (ldapConf.isLdapEnabledBoolean()) {

			logger.info(">>>首先同步缓存用户和机构信息......");
			this.refreshUserCacheInfos();
			this.refreshOrgCacheInfos();
			
			logger.info(">>>当前同步的院是 [{}]", realm);
			syncInstitutesToLdap(realm);
		}

	}
	
	@Deprecated
	private void syncUsersToLdap() {
		
		logger.info(">>>syncToLdap，开始同步用户信息......");
		List<DcmUser> users = null;
		try{
		  users = this.listAllUsers();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		if(null == users || users.isEmpty()) {
			logger.info(">>>获取不到用户信息，跳过同步。");
			return ;
		
		}
	
		Map<String, Map<String, LdapPerson>> instituteUserMap = new HashMap<String, Map<String, LdapPerson>>();
		String realm = "";
		String bn = "";
		for (DcmUser user : users) {
			
			if(StringUtil.isNullOrEmpty(user.getRealm())) continue;
			
			realm = user.getRealm();
			bn = "o="+realm+",dc=xiaozhi";
			if(!instituteUserMap.containsKey(realm)){
				instituteUserMap.put(realm, this.ldapDmn.searchAllPersonsCnMap(bn));
			}
			
		    logger.info(">>>检测ldap中是否已存在当前用户信息.....");
			if(instituteUserMap.get(realm).containsKey(user.getLoginName().toLowerCase())){
				logger.info(">>>用户已存在[{}], bn [{}]", user.getLoginName(), bn);
				continue;
			}
			
			logger.info(">>>添加LDAP人员信息 [{}]", user.getUserName());
			Map<String, String> properties = new HashMap<String, String>();
			properties.put("displayName", user.getUserName());
			properties.put("sex", user.getSex());
			properties.put("uid", user.getUserCode());
			
			//String pwd = DesEncryptDap.getInstance().Decrypt(user.getUserPwd()); // TODO 目前linux版本不支持dap加密解密
			this.ldapDmn.addInetOrgPerson(bn,user.getLoginName(), user.getUserName(), user.getUserPwd(),user.getUserCode(),
					properties);

		}
		logger.info(">>>syncToLdap，结束同步用户信息......");
	}

	/**
	 * 同步指定的院
	 * @param targetRealm
	 */
	private void syncInstitutesToLdap(String targetRealm) {
		
		logger.info(">>>syncOrgsToLdap，开始同步机构信息......");
		
		List<DcmOrganization> institutes = this.listInstitutes();
		logger.info(">>>获取到院机构信息 [{}]", institutes);
		
		Map<String, LdapOrg> ldapInstitutesMap = this.ldapDmn.searchOrganizationOneLevelOMap();
		logger.info(">>>获取到ldap院机构信息 [{}]", ldapInstitutesMap);
		
		Map<String, Map<String, LdapPerson>> instituteUserMap = new HashMap<String, Map<String, LdapPerson>>();
		String realm = "";
		String bn = "";
		logger.info(">>>处理院的信息......");
		for(DcmOrganization institute : institutes){
			
			if(!targetRealm.equalsIgnoreCase(institute.getOrgName())) continue;
			
			if(ldapInstitutesMap.containsKey(institute.getOrgName())) {
				
				logger.info(">>>ldap中已存在院 [{}]。", institute.getOrgName());
				
			}else {
				
				logger.info(">>>ldap中不存在院 [{}]......", institute.getOrgName());
				logger.info(">>>添加院信息 [{}:{}]", institute.getAlias(), institute.getOrgName());
				this.ldapDmn.addOrganization("dc=xiaozhi",institute.getOrgName(), institute.getAlias(),institute.getOrgCode(), null);		
			
			}
			
			logger.info(">>>处理院下的人员信息......");
			List<DcmUser> users = this.userDmn.findByProperties(new String[]{"currentStatus", "realm"}, new Object[]{1L, institute.getOrgName()});
			for (DcmUser user : users) {
				
				if(StringUtil.isNullOrEmpty(user.getRealm())) continue;
				
				realm = user.getRealm();
				bn = "o="+realm+",dc=xiaozhi";
				if(!instituteUserMap.containsKey(realm)){
					instituteUserMap.put(realm, this.ldapDmn.searchAllPersonsCnMap(bn));
				}
				
			    logger.info(">>>检测ldap中是否已存在当前用户信息 [{}].....", user.getLoginName());
				if(instituteUserMap.get(realm).containsKey(user.getLoginName().toLowerCase())){
					logger.info(">>>用户已存在[{}], bn [{}]", user.getLoginName(), bn);
					continue;
				}
				
				logger.info(">>>添加LDAP人员信息 [{}]......", user.getLoginName());
				Map<String, String> properties = new HashMap<String, String>();
				properties.put("displayName", user.getUserName());
				properties.put("sex", user.getSex());
			
				logger.info(">>>添加人员信息 [{}] 到 [{}]......", user.getLoginName(), bn);
				//String pwd = DesEncryptDap.getInstance().Decrypt(user.getUserPwd()); // TODO 目前linux版本不支持dap加密解密
				this.ldapDmn.addInetOrgPerson(bn,user.getLoginName(), user.getUserName(), DesEncryptBase64.getInstance().decrypt(user.getUserPwd()), user.getUserCode(),
						properties);
				
				LdapPerson newPer = new LdapPerson();
				newPer.setCname(user.getLoginName());
				newPer.setUid(user.getUserCode());
				instituteUserMap.get(realm).put(newPer.getCname(), newPer);

			}
			
			logger.info(">>>处理院下的部门信息......");
			List<DcmOrganization> departments = this.orgDmn.listDirectChildOrgs(institute.getId());
			syncDepartmentsToLdap(this.ldapDmn.searchAllPersonsCnMap(bn), "cn="+realm+","+ bn, departments);
				
		}
		instituteUserMap.clear();
		logger.info(">>>syncOrgsToLdap，结束同步机构信息......");
	}
	
	private void syncDepartmentsToLdap(Map<String, LdapPerson> institutePersons, String instituteParenDn,
			List<DcmOrganization> departments) {

		if (null == departments || 0 == departments.size())
			return;

		Map<String, LdapOrg> subGroupsCnMap = this.ldapDmn.searchDepartmentsDefinedByGroupofnameCnMap(instituteParenDn);

		for (DcmOrganization dep : departments) {

			if (subGroupsCnMap.containsKey(dep.getOrgName())) {
				logger.info(">>>已存在部门信息 [{}]", dep.getOrgName());
				continue;
			}

			// Map<String, LdapPerson> ldapUserMap =
			// this.ldapDmn.searchAllPersonsMap("o="+org.getOrgName()+",dc=xiaozhi");
			Map<String, String> properties = new HashMap<String, String>();
			properties.put("displayName", dep.getAlias());

			// 获取机构下的一级人员
			List<DcmUser> usersOfOrg = this.orgDmn.getUsers(dep.getId());
			List<String> memberDNs = new ArrayList<String>();
			if (usersOfOrg != null && !usersOfOrg.isEmpty()) {

				for (DcmUser u : usersOfOrg) {

					if (!institutePersons.containsKey(u.getLoginName().toLowerCase())) {
						logger.warn(">>>ldap中不存在用户 [{}]", u.getLoginName());
						continue;
					}

					memberDNs.add(institutePersons.get(u.getLoginName().toLowerCase()).getDn());

				}
			}

			logger.info(">>>添加部门信息 [{}:{}]", dep.getAlias(), dep.getOrgName());
			this.ldapDmn.addGroup(instituteParenDn, dep.getOrgName(), dep.getOrgCode(), "d", properties, memberDNs);

		}

	}
	//=====================LDAP-TDS End======================>
	
	@Override
	public UserDTO authenticateUser(String contextPath, String baseURL, String userId, String password) throws BusinessException {

		DcmUser findUser = this.getUserEntityByLoginName(userId);
		/*if(null == findUser || !findUser.getUserPwd().equals(DesEncryptDap.getInstance().Encrypt(password))) {
			throw new BusinessException("用户 [{0}] 验证失败。", userId);
		}
		*/
		if(null == findUser || !findUser.getUserPwd().equals(password)) {
			throw new BusinessException("用户 [{0}] 验证失败。", userId);
		}
		
		
		//com.filenet.api.security.User user = securityUtil.authenticateUser(userId, password);
		//if(user != null){
		//	return this.getUserByLoginName(contextPath, baseURL, userId);
		//}
		return userModelToUserDTO(contextPath, baseURL, findUser);
	}
	
	@Override
	public UserDTO authenticateUserFromLDAP(String contextPath, String baseURL, String orgRealm, String userId, String password) throws BusinessException {
		
		String baseDN = "";
		
		if(StringUtil.isNullOrEmpty(orgRealm)){
			baseDN = this.ldapConf.getBn();
		}else {
			baseDN = "o="+orgRealm+","+ this.ldapConf.getBn();
		}
		
		LdapPerson ldapPer = this.ldapDmn.searchUniquePerson(baseDN, orgRealm + "_" +userId); // cn命名规范：域_登录名
		if(null == ldapPer) return null;
		
		if(ldapPer.getUserPassword().equals(password)) {
			DcmUser u = this.getUserEntityByDN(ldapPer.getDn());
			
			try {
				URI uri = new URI(baseURL);
				updateUserLastActiveDateThread(u, "login", uri.getHost());
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			return this.getUser(contextPath, baseURL, u);
		}
		throw new BusinessException("用户 [{0}] 验证失败。", userId);

	}
	@Override
	public UserDTO authenticateUserFromLDAP(String contextPath, String baseURL, String orgRealm, String userId, String password, String clientIP)  throws BusinessException{
		
	String baseDN = "";
		
		if(StringUtil.isNullOrEmpty(orgRealm)){
			baseDN = this.ldapConf.getBn();
		}else {
			baseDN = "o="+orgRealm+","+ this.ldapConf.getBn();
		}
		
		LdapPerson ldapPer = this.ldapDmn.searchUniquePerson(baseDN, orgRealm + "_" +userId); // cn命名规范：域_登录名
		if(null == ldapPer) return null;
		
		if(ldapPer.getUserPassword().equals(password)) {
			DcmUser u = this.getUserEntityByDN(ldapPer.getDn());
			
			//updateUserLastActiveDateThread(u, "login", clientIP);
			
			return this.getUser(contextPath, baseURL, u);
		}
		throw new BusinessException("用户 [{0}] 验证失败。", userId);
	}
	   
	
	@SuppressWarnings("unchecked")
	@Override
	public Object getWorkgroupInfo() {
		
		Map<String, LdapTree> map = (Map<String, LdapTree>) this.ldapDmn.searchCurrGroupMap();
		List<LdapTree> tree = (List<LdapTree>) this.ldapDmn.filterWorkGroups(map.values());// this.ldapDmn.searchWorkGroups();
		 if(tree != null && tree.size()>0){
			 List<WorkgroupDTO> wgs = new ArrayList<WorkgroupDTO>();
			 for(LdapTree wg : tree){
				 WorkgroupDTO dto = new WorkgroupDTO();
				 dto.setCn(wg.getCn().indexOf("=")>-1? wg.getCn().split("=")[1] : wg.getCn());
				 dto.setAliasName(wg.getAliasName());
				 dto.setGroupCode(wg.getGroupCode());
				 String[] users = wg.getMembers();
				 addGroupUsers(map, wgs, dto, users);
			 }
			 return wgs;
		 }
		 return null;
	}
	
	@Override
	public Object getWorkgroupInfo(String orgRealm, String workgroupName) {

		Map<String, LdapOrg> ldapOrgMap = this.ldapDmn.searchOrganizationAndUnitMap(orgRealm);
		LdapWorkgroup ldapWorkgroup = this.ldapDmn.searchWorkGroup(orgRealm, workgroupName);
		if (null == ldapWorkgroup)
			return null;

		WorkgroupDTO dto = new WorkgroupDTO();
		dto.setCn(ldapWorkgroup.getName());
		dto.setAliasName(ldapWorkgroup.getAlias());
		dto.setGroupCode(ldapWorkgroup.getUid());

		String[] members = ldapWorkgroup.getMembers();
		if (null == members || 0 == members.length)
			return dto;

		for (String mem : members) {
			if (ldapOrgMap.containsKey(mem.toLowerCase())) {
				// 是个院或者所或者工作小组
				// 获取下面所有的人员
				List<LdapPerson> pers = this.ldapDmn.searchPersons(mem);
				if (null == pers || pers.isEmpty())
					continue;

				for (LdapPerson per : pers) {
					DcmUser cacheUser = this.getUserEntityByDN(per.getDn().toLowerCase());
					if (cacheUser != null) {
						dto.addUser(cacheUser.getId(), cacheUser.getUserName(), cacheUser.getLoginName(),
								cacheUser.getDn().toLowerCase());
					}
				}
			}
		}

		return dto;
	}
	private void addGroupUsers(Map<String, LdapTree> map, List<WorkgroupDTO> wgs, WorkgroupDTO dto, String[] users) {
		if( null ==users || 0 == users.length){
		    return;
		 }
		 for(String u : users){
			 
			 String cn = u.toString().split(",")[0].split("=")[1];
			 if(map.containsKey(("cn="+cn).toLowerCase())){
				 // 说明还是个组
				 String[]  subMembers =  map.get(("cn="+cn).toLowerCase()).getMembers();//this.ldapDmn.getWorkGroupMembers((SearchResultEntry) lt.getTag());
				 addGroupUsers(map, wgs, dto, subMembers);
			 }
			 LdapTree lt = this.ldapDmn.getUniqueWorkGroup(cn);
			 if(lt != null && (lt.getGroupType().equals("d") || lt.getGroupType().equals("i"))){
				
			 }else{
				 
				 DcmUser cacheUser = this.globalConf.openCache()? (DcmUser)this.redisCache.get(CacheKey.PREFIX_USER + cn):this.getUserEntityByLoginName(cn);
				 if(cacheUser != null){
					 dto.addUserByKeyCode(cacheUser.getId(), cacheUser.getUserName(), cacheUser.getLoginName(), cacheUser.getUserCode());
				 } 
			 }
			
		 }
		 if(!wgs.contains(dto)){
			 wgs.add(dto);
		 }
		
	}
	
	@Override
	public DcmUser getUserByLoginNameInCache(String loginName) {
		
		DcmUser user = (DcmUser) this.redisCache.get(CacheKey.PREFIX_USER + loginName);
		if(user != null){
			logger.info(">>>getUserByLoginNameInCache，缓存中找到用户：[{}]", loginName);
			return user;
		}
		logger.warn(">>>缓存中未找到用户：[{}]", loginName);
		return user;
	}
	@Override
	public DcmUser getUserByIdInCache(Long id) {
		
		DcmUser user = (DcmUser) this.redisCache.get(CacheKey.PREFIX_USER + id);
		if(user != null){
			logger.debug("getUserByIdInCache，缓存中找到用户：[{}]", id);
			return user;
		}
		logger.debug("getUserByIdInCache，缓存中未找到用户：[{}]", id);
		return user;
	}

	@Override
	public Object updateUserDetail(UserDetailRequestDTO detailInfoDTO) throws BusinessException {
		
		// this.updateUserBasicInfo(detailInfoDTO.getBase());
		
		return true;
	}
	@Override
	public DcmOrganizationDTO getOrgDTOByCodeInCache(String orgCode) {
		
		logger.info(">>>从缓存中获取机构实体 [{}]", orgCode);
		DcmOrganizationDTO dto = (DcmOrganizationDTO) this.redisCache.get(CacheKey.PREFIX_ORG + orgCode);
		if(dto != null){
			logger.debug("getOrgDTOByCodeInCache，缓存中找到机构：[{}]", orgCode);
			return dto;
		}
		logger.debug("getUserByIdInCache，缓存中未找到机构：[{}]", orgCode);
		return dto;
	}
	
	@Override
	public Map<Object, DcmOrganization> getOrgByCodeInCache(Object[] orgCodes) {
		
		logger.info(">>>从缓存中获取机构实体 [{}]",orgCodes);
	
		Map<Object, DcmOrganization> orgs = new HashMap<Object, DcmOrganization>();
		for(Object cd : orgCodes) {
			DcmOrganizationDTO dto = getOrgDTOByCodeInCache(cd.toString());
			if(null == dto){
				DcmOrganization temp = new DcmOrganization();
				orgs.put(cd, temp);
				continue;
			}
			orgs.put(cd, dto.getOrg());
		}
		
		return orgs;
	}
	
	@Override
	public DcmOrganization getOrgByCode(final String code) {
		
		DcmOrganizationDTO dto = this.globalConf.openCache()? (DcmOrganizationDTO)this.redisCache.get(CacheKey.PREFIX_ORG + code):null;
		if(dto != null){
			logger.info(">>>从缓存中获取机构实体 [{}]",dto.getOrgName());
			return dto.getOrg();
		}
		logger.info(">>>从缓存中未获取机构实体 [{}]",code);
		final DcmOrganization org = this.orgDmn.getOrgByCode(code);
		if(org != null && this.globalConf.openCache()){
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					redisCache.set(CacheKey.PREFIX_ORG + code, DcmOrganizationDTO.clone(org));
				}
			});
		}
		
		return org;
	}
	
	@Override
	public DcmOrganization getOrgById(Long orgId) {
		
		DcmOrganizationDTO dto = this.globalConf.openCache()? (DcmOrganizationDTO)this.redisCache.get(CacheKey.PREFIX_ORG + orgId):null;
		if(dto != null){
			logger.info(">>>从缓存中获取机构实体 [{}]",dto.getOrgName());
			return dto.getOrg();
		}
		final DcmOrganization org = this.orgDmn.loadById(orgId);
		if(org != null && this.globalConf.openCache()){
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					cacheOrg(DcmOrganizationDTO.clone(org));
				}
			});
		}
		
		return org;
	}
	@Override
	public List<DcmOrganization> getOrgsOfUser(String loginName) {
		
		return this.orgDmn.getOrgsByUserId(this.getUserEntityByLoginName(loginName).getId());
	}
	
	
	@Override
	public UserDetailDTO getUserDetailById(Long userId) {
		
		UserDetailDTO detail = new UserDetailDTO();
		
		detail.setUserId(userId);
		logger.info(">>>获取学术成果......");
		detail.setArticleInfos(getArticleInfos(userId));
		logger.info(">>>获取执业资格......");
		detail.setCertificateInfos(getCertificateInfos(userId));
		logger.info(">>>获取教育经历......");
		detail.setEducations(getEducations(userId));
		logger.info(">>>获取语言水平......");
		detail.setLanguages(getLanguages(userId));
		logger.info(">>>获取工作经历和项目经历......");
		detail.setExperiences(getExperiences(userId));
		logger.info(">>>获取职称信息......");
		detail.setTitleInfos(getTitleInfos(userId));
		logger.info(">>>获取培训经历......");
		detail.setTrainings(getTrainings(userId));
		logger.info(">>>获取其它附件信息......");
		detail.setOthers(getOtherAttachmentInfo(userId));
		
		return detail;
	}
	@Override
	public List<UserArticleInfoDTO> getArticleInfos(Long refUserId) {
		
		List<DcmUserArticleInfo> result = this.userDmn.getArticleInfos(refUserId);
		if(result != null && !result.isEmpty()){
			List<UserArticleInfoDTO> list = new ArrayList<UserArticleInfoDTO>(result.size());
			for(DcmUserArticleInfo entity : result){
				UserArticleInfoDTO dto = new UserArticleInfoDTO();
				dto.setInfo(entity);
				dto.setAttachments(this.userDmn.getAttachmentInfoByRefId(entity.getId()));
				list.add(dto);
			}
			return list;
		}
		return null;
	}
	@Override
	public List<UserCertificateInfoDTO> getCertificateInfos(Long refUserId) {
		
		List<DcmUserCertificateInfo> result = this.userDmn.getCertificateInfos(refUserId);
		if(result != null && !result.isEmpty()){
			List<UserCertificateInfoDTO> list = new ArrayList<UserCertificateInfoDTO>(result.size());
			for(DcmUserCertificateInfo entity : result){
				UserCertificateInfoDTO dto = new UserCertificateInfoDTO();
				dto.setInfo(entity);
				dto.setAttachments(this.userDmn.getAttachmentInfoByRefId(entity.getId()));
				list.add(dto);
			}
			return list;
		}
		return null;
	}
	@Override
	public List<UserEducationDTO> getEducations(Long refUserId) {
		
		List<DcmUserEducation> result = this.userDmn.getEducations(refUserId);
		if(result != null && !result.isEmpty()){
			List<UserEducationDTO> list = new ArrayList<UserEducationDTO>(result.size());
			for(DcmUserEducation entity : result){
				UserEducationDTO dto = new UserEducationDTO();
				dto.setInfo(entity);
				dto.setAttachments(this.userDmn.getAttachmentInfoByRefId(entity.getId()));
				list.add(dto);
			}
			return list;
		}
		return null;
	}
	@Override
	public List<UserWorkExperienceDTO> getExperiences(Long refUserId) {
		
		List<DcmUserWorkExperience> result = this.userDmn.getWorkExperiences(refUserId);
		if(result != null && !result.isEmpty()){
			List<UserWorkExperienceDTO> list = new ArrayList<UserWorkExperienceDTO>(result.size());
			for(DcmUserWorkExperience entity : result){
				UserWorkExperienceDTO dto = new UserWorkExperienceDTO();
				dto.setInfo(entity);
				dto.setAttachments(this.userDmn.getAttachmentInfoByRefId(entity.getId()));
				List<DcmUserPrjExperience> projExps = this.userDmn.getPrjExperiences(entity.getId());
				if(projExps != null && !projExps.isEmpty()){
					List<UserPrjExperienceDTO> projects = new ArrayList<UserPrjExperienceDTO>(projExps.size());
					for(DcmUserPrjExperience prjEntity : projExps){
						UserPrjExperienceDTO prjDTO = new UserPrjExperienceDTO();
						prjDTO.setInfo(prjEntity);
						prjDTO.setAttachments(this.userDmn.getAttachmentInfoByRefId(prjEntity.getId()));
						projects.add(prjDTO);
					}
					//dto.setProjects(projects);
				}
				list.add(dto);
			}
			return list;
		}
		return null;
	}
	@Override
	public List<UserLanguageDTO> getLanguages(Long refUserId) {
		
		List<DcmUserLanguage> result = this.userDmn.getLanguages(refUserId);
		if(result != null && !result.isEmpty()){
			List<UserLanguageDTO> list = new ArrayList<UserLanguageDTO>(result.size());
			for(DcmUserLanguage entity : result){
				UserLanguageDTO dto = new UserLanguageDTO();
				dto.setInfo(entity);
				dto.setAttachments(this.userDmn.getAttachmentInfoByRefId(entity.getId()));
				list.add(dto);
			}
			return list;
		}
		return null;
	}
	@Override
	public List<UserTitleInfoDTO> getTitleInfos(Long refUserId) {
		List<DcmUserTitleInfo> result = this.userDmn.getTitleInfos(refUserId);
		if(result != null && !result.isEmpty()){
			List<UserTitleInfoDTO> list = new ArrayList<UserTitleInfoDTO>(result.size());
			for(DcmUserTitleInfo entity : result){
				UserTitleInfoDTO dto = new UserTitleInfoDTO();
				dto.setInfo(entity);
				dto.setAttachments(this.userDmn.getAttachmentInfoByRefId(entity.getId()));
				list.add(dto);
			}
			return list;
		}
		return null;
	}
	@Override
	public List<UserTrainingDTO> getTrainings(Long refUserId) {
		List<DcmUserTraining> result = this.userDmn.getTrainings(refUserId);
		if(result != null && !result.isEmpty()){
			List<UserTrainingDTO> list = new ArrayList<UserTrainingDTO>(result.size());
			for(DcmUserTraining entity : result){
				UserTrainingDTO dto = new UserTrainingDTO();
				dto.setInfo(entity);
				dto.setAttachments(this.userDmn.getAttachmentInfoByRefId(entity.getId()));
				list.add(dto);
			}
			return list;
		}
		return null;
	}
	@Override
	public List<UserOtherInfoDTO> getOtherAttachmentInfo(Long refUserId) {
		
		List<DcmUserOtherInfo> result = this.userDmn.getOtherAttachmentInfo(refUserId);
		
		if(result != null && !result.isEmpty()){
			List<UserOtherInfoDTO> list = new ArrayList<UserOtherInfoDTO>(result.size());
			for(DcmUserOtherInfo entity : result){
				UserOtherInfoDTO dto = new UserOtherInfoDTO();
				dto.setInfo(entity);
				dto.setAttachments(this.userDmn.getAttachmentInfoByRefId(entity.getId()));
				list.add(dto);
			}
			return list;
		}
		return null;
	}
	
/*	protected Object saveUserDetail(UserDetailInfoDmn detailDmn, Object info){
		
		return detailDmn.doSave(info);
	}*/
	
/*	protected Object deleteUserDetail(UserDetailInfoDmn detailDmn, Long refId) {
		
		return detailDmn.delete(refId);
	}*/
	
	@Override
	public Object saveUserDetail(String flag, Object info) {
		
		return this.userInfoFactory.getDetailDmn(flag).doSave(info);
	}
	
	@Override
	public Object deleteUserDetail(String flag, Long refId) {
		
		return this.userInfoFactory.getDetailDmn(flag).delete(refId);
		
	}

	@Override
	public List<DcmOrganization> listInstitutes() {
		
		return this.orgDmn.getAllInstitute();
	}

	@Override
	public List<DcmOrganization> listDepartments(Long instituteId) {
		
		return this.orgDmn.listOrgsOfOrg(instituteId);
	}
	
	@Override
	public List<DcmOrganization> listCurrentDepartments(String loginName) {
		
		DcmOrganization institute = this.getInstituteByUserLoginName(loginName);
		
		return this.listDepartments(institute.getId());
	}
	@Override
	public List<DcmUser> getUsersByOrgCode(String orgCode) {
		
		return this.orgDmn.getUsersByOrgCode(orgCode);
	}
	
	@Override
	public List<DcmUser> getUsersByInstituteName(String instituteUniqueName) {
		
		DcmOrganization org = this.orgDmn.getInstituteByUniqueName(instituteUniqueName);
		if(null == org) return null;
		
		return this.orgDmn.listUsersOfOrg(org.getId());
	}

	@Override
	public List<DcmOrganization> getAllDepartment() {
	
		return this.orgDmn.getAllDepartment();
	}
	@Override
	public List<DcmOrganization> getDepartmentsOfInstitute(String instituteUniqueName) {
		
		return this.orgDmn.listSubDepartmentsByInstituteUniqueName(instituteUniqueName);
	}
	@Override
	public List<DcmOrganization> getDepartmentsAndInstitute(String instituteUniqueName) {
		
		DcmOrganization ins = this.orgDmn.getInstituteByUniqueName(instituteUniqueName);
		if(null == ins) return null;
		
		List<DcmOrganization> orgs = this.orgDmn.listSubDepartmentsByInstituteUniqueName(ins);
		orgs.add(ins);
		
		return orgs;
	}
	@Override
	public String getUserAvatarLink(String contextPath, String baseURL, DcmUser user) {
	
		return this.userDmn.getUserAvatarLink(this.imgSerConf.getServerURI(), contextPath, baseURL, user);
	}
	@Override
	public List<DcmOrganization> getOrgsByUserId(Long id) {
		
		return this.orgDmn.getOrgsByUserId(id);
	}

	@Override
	public String updateUserAvatar(final DcmUser user, String baseURL,  String avatarRelativePath) {
		
		user.setAvatar(avatarRelativePath);
		this.userDmn.modify(user);

		if(this.globalConf.openCache()){
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					// 刷新缓存
					cacheUser(user);
				}
			});
		}
		String avatarUrl = baseURL.endsWith("/") ? (baseURL + avatarRelativePath)
				: (baseURL + "/" + avatarRelativePath);

		return avatarUrl;
	}
	@Override
	public String updateUserAvatarEx(String userCode, String baseURL, String avatarRelativePath) {
		
		final DcmUser user = this.getUserEntityByCode(userCode);
		if (null == user) {
			throw new BusinessException(String.format("id为[%s]的用户不存在", userCode));
		}
		user.setAvatar(avatarRelativePath);
		this.userDmn.modify(user);
		
		if(this.globalConf.openCache()){
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					// 刷新缓存
					cacheUser(user);
					redisCache.removePattern(CacheKey.CACHEKEY_REALM_USER+"*");
				}
			});
		}
		return this.userDmn.getUserAvatarLink(this.imgSerConf.getServerURI(), "", baseURL, user);
		/*String avatarUrl = baseURL.endsWith("/") ? (baseURL + avatarRelativePath)
				: (baseURL + "/" + avatarRelativePath);

		return avatarUrl;*/
	}

	@Override
	public BaseUserDTO getUserDNByRealmLoginname(String realm, String loginName) throws BusinessException{
		
		DcmUser user = this.getUserEntityByLoginName(realm+"_"+loginName);
		//String orgDn = "o="+realm+","+this.ldapConf.getBn();
		//LdapPerson per = this.ldapDmn.searchUniquePerson(orgDn, realm+"_"+loginName); // 注意cn的命名规范：域_登录名，因为ecm对ldap的cn需要是唯一的
		
		//if(null == per) throw new BusinessException("指定的域 [{}] 和 登录名 [{}] 不存在。", orgDn, loginName); 
		if(null == user) throw new BusinessException("用戶 [{}] 不存在。", realm+"_"+loginName);
		
		//DcmUser user = this.getUserEntityByDN(per.getDn());
		BaseUserDTO baseUser = new BaseUserDTO();
		baseUser.setLoginName(user.getLoginName());
		baseUser.setUserName(user.getUserName());
		baseUser.setUserCode(user.getUserCode());
		baseUser.setDn(user.getDn());
		baseUser.setId(user.getId());
		
		// updateUserLastActiveDateThread(user);
		
		return baseUser;
	}

	/**
	 * 更新用户的活跃时间
	 * @param user
	 */
	private void updateUserLastActiveDateThread(DcmUser user) {
		
	/*	DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				
				logger.debug(">>>更新用戶 [{}]的最后活跃时间......", user.getLoginName());
				user.setDateLastActivity(new Date());
				UserOrgServiceImpl.this.userDmn.modify(user);
				
			}
		});*/
		
	}

	private void updateUserLastActiveDateThread(final DcmUser user, final String action, final String clientIP) {

		DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				DcmLog log = null;
				if (action.equalsIgnoreCase("login")) {
					logger.debug(">>>记录用户 [{}]登录日志......", user.getLoginName());
					log = UserOrgServiceImpl.this.logFactory.createLoginLog(user, clientIP,
							"guihuaxiaozhi." + user.getRealm(), String.format("用户登录 [%s]", user.getLoginName()));

				} else {
					logger.debug(">>>记录用户 [{}]登出日志......", user.getLoginName());
					log = UserOrgServiceImpl.this.logFactory.createLogoutLog(user, clientIP,
							"guihuaxiaozhi." + user.getRealm(), String.format("用户登出 [%s]", user.getLoginName()));
				}

				UserOrgServiceImpl.this.logDmn.add(log);
			}
		});
	}

	@Override
	public boolean changeUserPwd(String userId, String oldPwd, String newPwd) throws BusinessException {
		
		final DcmUser user = this.getUserEntityById(Long.parseLong(userId));
		/*if(!user.getUserPwd().equals(DesEncryptDap.getInstance().Encrypt(oldPwd))) {
			throw new BusinessException("原来的密码输入有误，验证不通过。");
		}*/
		if(!user.getUserPwd().equals(DesEncryptAES.getInstance().encrypt(oldPwd))) {
			throw new BusinessException("原来的密码输入有误，验证不通过。");
		}
		
		//user.setUserPwd(DesEncryptDap.getInstance().Encrypt(newPwd));
		user.setUserPwd(DesEncryptAES.getInstance().encrypt(newPwd));
		this.userDmn.modify(user);
		if(this.globalConf.openCache()){
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					cacheUser(user);
				}
			});
		}
		
		if(this.ldapConf.isLdapEnabledBoolean()) {
			
			this.ldapDmn.alterUserPassowrd(user.getLoginName(), newPwd);	
		}
	
		return true;
	}
	
	@Override
	public Boolean logout(String userCode, String clientIP) {
		
		DcmUser user = this.getUserEntityByCode(userCode);
		if(null == user) return false;
		
		updateUserLastActiveDateThread(user, "logout", clientIP);
		//DcmLog log = this.logFactory.createLogoutLog(user, clientIP, "guihuaxiaozhi", String.format("用户登出 [%s]", user.getLoginName()));
		
		return true;
	}
	
	public static void main(String[]args) throws URISyntaxException {
		
		URI uri = new URI("http://127.2.45.9");
		System.out.println( uri.getHost());
		System.out.println( uri.getPort());
	}

	@Override
	public void deleteOrg(OrgDelRequestDTO dto) {
	
		for(Long id : dto.getOrgIds()) {
			DcmOrganization org = this.getOrgById(id);
			if(null == org) continue;
			
			// 格式：cn=dist,cn=gzpi,o=gzpi,DC=DIST
			String distinguishName = "cn="+org.getOrgName()+",cn="+dto.getRealm()+",o="+dto.getRealm()+","+this.ldapConf.getBn();
			if(this.ldapDmn.delete(distinguishName)) {
				
				this.orgDmn.deleteOrg(org);
			}
		}
	}

	@Override
	public UserSimpleDTO getSimpleUserByCode(String code) {
		
	  DcmUser user = this.getUserEntityByCode(code);
	  if(null == user) return null;
	  
	  UserSimpleDTO dto = new UserSimpleDTO();
	  dto.setAvatar(user.getAvatar());
	  dto.setDn(user.getDn());
	  dto.setId(user.getId());
	  dto.setLoginName(user.getLoginName());
	  dto.setSex(user.getSex());
	  dto.setUserCode(user.getUserCode());
	  dto.setUserName(user.getUserName());
	  
	  return dto;
	}
	
	@Override
	public Object getUserBasicInfoByCode(String code) {
		
		 DcmUser user = this.getUserEntityByCode(code);
		  if(null == user) return null;
		  
		  UserBasicDTO dto = new UserBasicDTO();
		  dto.setDn(user.getDn());
		  dto.setId(user.getId());
		  dto.setLoginName(user.getLoginName());
		  dto.setSex(user.getSex());
		  dto.setUserCode(user.getUserCode());
		  dto.setUserName(user.getUserName());
		  dto.setPhone(user.getPhone());
		  dto.setTelephone(user.getTelephone());
		  dto.setEmail(user.getEmail());
		  dto.setQq(user.getQq());
		  
		  return dto;
	}

	@Override
	public Object addOrgPosition(Long orgId, String positionName) {
	
		return this.orgDmn.addDicInfo(orgId, DicOrgExtType.POSITION, positionName);
	}
	@Override
	public Object addOrgPosition(OrgPositionAddDTO dto) {
		return this.orgDmn.addDicInfo(dto.getOrgCode(), DicOrgExtType.POSITION, dto.getName());
	}
	@Override
	public Object addOrgTeam(OrgTeamAddDTO dto) {
		return this.orgDmn.addDicInfo(dto.getOrgCode(), DicOrgExtType.TEAM, dto.getName());
	}
	@Override
	public Object addOrgType(OrgTypeAddDTO dto) {
		return this.orgDmn.addDicInfo(dto.getOrgCode(), DicOrgExtType.ORG_TYPE, dto.getName());
	}
	@Override
	public Object addOrgTeam(Long orgId, String teamName) {
		
		return this.orgDmn.addDicInfo(orgId, DicOrgExtType.TEAM, teamName);
	}
	@Override
	public boolean deleteOrgPosition(Long id) {
		return this.orgDmn.deleteDic(id);
	}
	@Override
	public boolean deleteOrgType(Long id) {
		return this.orgDmn.deleteDic(id);
	}
	@Override
	public boolean deleteOrgPosition(String orgCode) {
		return this.orgDmn.deleteDic(DicOrgExtType.POSITION, orgCode);
	}
	@Override
	public boolean deleteOrgTeam(final Long id) {
		
		boolean finishDelete = this.orgDmn.deleteDic(id);
		if(finishDelete){
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					DcmDicOrgExt dicTeam = UserOrgServiceImpl.this.orgDmn.getOrgDicById(id);
					UserOrgServiceImpl.this.userDmn.setTeamNull(dicTeam.getName());
					UserOrgServiceImpl.this.redisCache.removePattern(CacheKey.PREFIX_USER_PATTERN);
					UserOrgServiceImpl.this.redisCache.removePattern(CacheKey.PREFIX_REALM_USER_PATTERN);
				}
			});
		}
		
		return finishDelete;
	}
	@Override
	public boolean updatePersonalBasicInfo(UserPersonUpdateRequestDTO userDto) throws BusinessException {
		
		DcmUser user = this.getUserEntityById(userDto.getId());
		if(null == user) 
			throw new BusinessException("用户id："+userDto.getId()+"不存在");
		
		user.setTelephone(userDto.getTelephone());
		user.setPhone(userDto.getPhone());
		user.setEmail(userDto.getEmail());
		user.setQq(userDto.getQq());
		
		this.userDmn.modify(user);
		
		this.cacheUser(user);
		return true;
	}
	@Override
	public List<DcmDicOrgExt> getOrgPositions(Long orgId) {
		
		return this.orgDmn.getDics(orgId, DicOrgExtType.POSITION);
	}
	@Override
	public List<DcmDicOrgExt> getOrgPositions(String orgCode) {
		return this.orgDmn.getDics(orgCode, DicOrgExtType.POSITION);
	}
	@Override
	public  List<DcmDicOrgExt> getOrgTeams(String orgCode) {
		return this.orgDmn.getDics(orgCode, DicOrgExtType.TEAM);
	}
	@Override
	public  List<DcmDicOrgExt> getOrgTypes(String orgCode) {
		return this.orgDmn.getDics(orgCode, DicOrgExtType.ORG_TYPE);
	}
	@Override
	public List<DcmDicOrgExt> getOrgTeams(Long orgId) {
		
		return this.orgDmn.getDics(orgId, DicOrgExtType.TEAM);
	}
	
	@Override
	public boolean renameOrgName(String orgCode, String newName) throws BusinessException {
		
		DcmOrganization org = this.orgDmn.getOrgByCode(orgCode);
		if(null == org) throw new BusinessException("机构code："+orgCode+"不存在");
		
		org.setAlias(newName);
		this.orgDmn.modify(org);
		if(this.ldapConf.isLdapEnabledBoolean()){
			// 修改ldap的组的名称
			this.ldapDmn.alterGroupDisplayNameByDN(org.getDn(), newName);
		}
		this.cacheOrg(DcmOrganizationDTO.clone(org));
		return true;
	}
	@Deprecated
	@Override
	public List<UserSearchDTO> fuzzySearchUsers(String keyword) {
		
		List<DcmUser> users = this.userDmn.findByNameMatch(keyword);
		if(null == users || users.isEmpty()) return null;
		
		List<UserSearchDTO> searchUsers = new ArrayList<>(users.size());
		for(DcmUser u : users){

			if(!StringUtils.hasLength(u.getRealm()) || 0L == u.getCurrentStatus()){
				continue;
			}
			UserSearchDTO userSearcheDto = new UserSearchDTO();
			try {
				dozerMapper.map(userSearcheDto, u);
				// BeanUtils.copyProperties(userSearcheDto, u);
			} catch (Exception e) {
				logger.error(">>>复制对象时错误，详情："+e.getMessage());
			} 
			searchUsers.add(userSearcheDto);
		}
		
		return searchUsers;
	}
	@Override
	public Object fuzzySearchUsersPage(int pageNo, int pageSize, String keyword) {
		
		Pagination page = this.userDmn.findByNameMatch(pageNo, pageSize, keyword);
		if(null == page || null == page.getData() || page.getData().isEmpty()) {
			return null;
		}
		
		List<UserSearchDTO> searchUsers = toUserSearchDTO(page);
		page.setData(searchUsers);
		return new Pagination(pageNo, pageSize, page.getTotalCount(), searchUsers);
		// return page;
	}
	@Override
	public Object fuzzySearchUsersPage(String realm, int pageNo, int pageSize, String keyword){
	   
	   Pagination page = this.userDmn.findByNameMatch(realm, pageNo, pageSize, keyword);
		if(null == page || null == page.getData() || page.getData().isEmpty()) {
			return null;
		}
		
		List<UserSearchDTO> searchUsers = toUserSearchDTO(page);
		page.setData(searchUsers);
		return page;
	}
	/**
	 * DcmUser转换成UserSearchDTO
	 * @param page
	 * @return
	 */
	private List<UserSearchDTO> toUserSearchDTO(Pagination page) {
		
		List<UserSearchDTO> searchUsers = new ArrayList<>(page.getData().size());
		List<DcmOrganization> orgs = null;
		for(Object u : page.getData()){
			DcmUser user = (DcmUser) u;
			/*if(!StringUtils.hasLength(user.getRealm()) || 0L == user.getCurrentStatus()){
				page.setTotalCount(page.getTotalCount()-1);
				continue;
			}*/
			UserSearchDTO userSearcheDto = new UserSearchDTO();
			try {
		        userSearcheDto.setId(user.getId());
		        userSearcheDto.setUserCode(user.getUserCode());
		        userSearcheDto.setLoginName(user.getLoginName());
		        userSearcheDto.setRealm(user.getRealm());
		        userSearcheDto.setUserName(user.getUserName());
		        userSearcheDto.setTeamName(user.getTeamName());
		        userSearcheDto.setPosition(user.getPosition());
		        userSearcheDto.setTelephone(user.getTelephone());
		        userSearcheDto.setPhone(user.getPhone());
		        userSearcheDto.setEmail(user.getEmail());
		        userSearcheDto.setQq(user.getQq());
		        
				orgs = this.orgDmn.getOrgsByUserId(user.getId());
				if(orgs != null && !orgs.isEmpty()){
					userSearcheDto.setDepartment(orgs.get(0).getAlias());
				}
			} catch (Exception e) {
				logger.error(">>>对象转换时错误，详情："+e.getMessage(), e);
			} 
			searchUsers.add(userSearcheDto);
		}
		return searchUsers;
	}
	@Override
	public void deleteAvatarInMongoByUserCode(String userId) {
		
		this.mongoFileStorageDao.deleteByCode(userId);
	}
	@Override
	public String storeAvatarToMongo(ImgInfo imgInfo) {
		
		String newFileName = System.currentTimeMillis() + (imgInfo.getSuffix().startsWith(".")? imgInfo.getSuffix() : "."+imgInfo.getSuffix());
		
		DBObject metaData = new BasicDBObject();
    	metaData.put("suffix", imgInfo.getSuffix());
    	metaData.put("code", imgInfo.getId());
    	metaData.put("type", "avatar");
    	
    	return mongoFileStorageDao.store(FileUtil.base64ToInputStream(imgInfo.getContent()) , newFileName, imgInfo.getType(), metaData);
	}
	@Override
	public FileContentLocalDTO getAvatarFromMongo(String objectId) throws BusinessException {
		
		GridFSDBFile file = this.mongoFileStorageDao.getById(objectId);
		if(null == file)
			throw new BusinessException("用户头像不存在");
		
		try {
			FileContentLocalDTO fileContent = new FileContentLocalDTO();
			fileContent.setLsize(file.getLength());
			fileContent.setContentType(file.getContentType());
			fileContent.setContentStream(FileUtil.getBytes(file.getInputStream()));
			return fileContent;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException("获取用户头像失败，详情："+e.getMessage());
		}
	}
	@Override
	public String getUserAvatarLink(String imgServerURI, String contextPath, String baseURL, DcmUser user) {
		return this.userDmn.getUserAvatarLink(imgServerURI, contextPath, baseURL, user);
	}
	@Override
	public DcmOrganization getInstituteByUniqueName(String name) {
		return this.orgDmn.getInstituteByUniqueName(name);
	}
	@Override
	public Boolean updateOrgBasicInfo(OrgBasicInfoUpdateDTO dto) {
		
		DcmOrganization org = this.orgDmn.getOrgByCode(dto.getOrgCode());
		org.setAlias(dto.getName());
		org.setOrgType(dto.getOrgType());
		return true;
	}
	@Override
	public List<DcmOrganization> getOrgsByRealm(String realm) {
		return this.orgDmn.findByProperty("realm", realm);
	}
	@Override
	public Pagination getOrgsByRealm(String realm, String orgType) {
		Map<String, Object[]> equalProperties = new HashMap<String, Object[]>();
		equalProperties.put("realm", new String[]{realm});
		Map<String, Object[]> likeProperties = new HashMap<String, Object[]>();
		likeProperties.put("orgType", new String[]{orgType});
		return this.orgDmn.findByProperties(1, 1, equalProperties, likeProperties, "orgName", true);
		// return this.orgDmn.findByProperties(new String[]{"realm", "orgType"}, new Object[]{realm, orgType});
	}
	@Override
	public Boolean existOrg2User(Long userId, String orgCode) {
		DcmOrganization org = this.orgDmn.getOrgByCode(orgCode);
		if(null == org) return false;
		
		return this.orgDmn.existOrg2User(org.getId(), userId);
	}
}