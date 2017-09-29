
package com.dist.bdf.facade.service.biz.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.base.utils.DateUtil;
import com.dist.bdf.base.utils.DistThreadManager;
import com.dist.bdf.base.utils.StringUtil;
import com.dist.bdf.facade.service.*;
import com.dist.bdf.facade.service.biz.domain.system.DcmGroupDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmPrivTemplateDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmTeamDmn;
import com.dist.bdf.facade.service.sga.SgaUserService;
import com.dist.bdf.facade.service.uic.domain.DcmRoleDmn;
import com.dist.bdf.facade.service.uic.domain.DcmUserdomainroleDmn;
import com.dist.bdf.manager.cache.CacheStrategy;
import com.dist.bdf.common.conf.common.GlobalConf;
import com.dist.bdf.common.constants.CacheKey;
import com.dist.bdf.common.constants.DomainType;
import com.dist.bdf.common.constants.Flag;
import com.dist.bdf.common.constants.ResourceConstants;
import com.dist.bdf.common.constants.RoleConstants;
import com.dist.bdf.model.dto.sga.UserResponseDTO;
/*import com.dist.bdf.facade.service.cache.DistributedCacheService;*/
import com.dist.bdf.model.dto.system.DcmOrganizationDTO;
import com.dist.bdf.model.dto.system.DiscussionGroupDTO;
import com.dist.bdf.model.dto.system.GroupDTO;
import com.dist.bdf.model.dto.system.GroupUserDTO;
import com.dist.bdf.model.dto.system.GroupUserExDTO;
import com.dist.bdf.model.dto.system.PersonalDiscussionGroupResultDTO;
import com.dist.bdf.model.dto.system.ProjectGroupDTO;
import com.dist.bdf.model.dto.system.ProjectOrgMaskDTO;
import com.dist.bdf.model.dto.system.ProjectUserResponseDTO;
import com.dist.bdf.model.dto.system.ProjectUserResponseExDTO;
import com.dist.bdf.model.dto.system.TeamGroupDTO;
import com.dist.bdf.model.dto.system.page.PageProjectPara;
import com.dist.bdf.model.dto.system.page.PageProjectResult;
import com.dist.bdf.model.dto.system.user.UserProjectResponseDTO;
import com.dist.bdf.model.entity.system.DcmGroup;
import com.dist.bdf.model.entity.system.DcmRole;
import com.dist.bdf.model.entity.system.DcmUser;
import com.dist.bdf.model.entity.system.DcmUserdomainrole;

/**
 * @author weifj
 * @version 1.0，2016/03/01，weifj，创建服务实现类
 */
@Service(value="GroupService")
@Transactional(propagation = Propagation.REQUIRED)
public class GroupServiceImpl implements GroupService {

	private Logger logger  = LoggerFactory.getLogger(getClass());
	
/*	@Autowired
	private DcmUserDmn userDmn;*/
	@Autowired
	private DcmGroupDmn groupDmn;
	@Autowired
	private DcmUserdomainroleDmn udrDmn;
	@Autowired
	private DcmRoleDmn roleDmn;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserOrgService userOrgService;
	@Autowired
	private DcmPrivTemplateDmn privTemplateDmn;
/*	@Autowired
	private DistributedCacheService disCacheService;
	*/
	@Autowired
	private GlobalConf globalConf;
	@Autowired
	private CacheStrategy redisCache;
	@Autowired
	private SgaUserService sgaUserService;
	@Autowired
	private DcmTeamDmn teamDmn;
	
	@Override
	public DcmGroup addGroup(GroupDTO groupDto) {

		DcmGroup group = new DcmGroup();
		group.setDomainType(groupDto.getDomainType());
		group.setGroupCode(groupDto.getGroupCode());
		group.setGroupName(groupDto.getGroupName());
		group.setCreateTime(new Date());
		group.setCreator(groupDto.getCreator());
		group.setGuid(groupDto.getGuid());
		group.setOrgCode(groupDto.getOrgCode());
		group.setRealm(groupDto.getRealm());
		DcmOrganizationDTO orgDTO = userOrgService.getOrgDTOByCodeInCache(groupDto.getOrgCode());
		group.setOrgType(null == orgDTO ? "" : orgDTO.getDomainType());

		final DcmGroup addGroup = this.groupDmn.add(group);
		if (groupDto.getDomainType() == DomainType.PROJECT) {
			// 把初始化时的项目负责人添加到项目负责人的角色中
			String xmfzr = ((ProjectGroupDTO) groupDto).getXmfzr();
			logger.info(">>>项目负责人[{}]添加到项目中", xmfzr);
			DcmUser findUser = this.userOrgService.getUserEntityByCode(xmfzr);// .findByLoginNameSimply(xmfzr);
			if (findUser != null) {
				this.udrDmn.addUserToDomain(findUser, addGroup.getDomainType(), addGroup.getGuid(),
						RoleConstants.RoleCode.R_Project_Manager);
			}
			String xmzl = ((ProjectGroupDTO) groupDto).getXmzl();
			logger.info(">>>项目助理[{}]添加到项目中", xmzl);
			if (StringUtils.hasLength(xmzl)) {	
				DcmUser userXmzl = this.userOrgService.getUserEntityByCode(xmzl);
				if (userXmzl != null){
					this.udrDmn.addUserToDomain(userXmzl, addGroup.getDomainType(), addGroup.getGuid(),
							RoleConstants.RoleCode.R_Project_Assistant);
				}
			}
		}

		if (this.globalConf.openCache()) {
			logger.info(">>>async, project is added to cache, key:[{}]...", addGroup.getGuid());
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
				public void run() {
					GroupServiceImpl.this.redisCache.set(CacheKey.PREFIX_PROJECT + addGroup.getGuid(), addGroup);
				}
			});
		}

		return addGroup;

	}
	
	public DcmGroup addGroupGZ(GroupDTO groupDto) {

		DcmGroup group = new DcmGroup();
		group.setDomainType(groupDto.getDomainType());
		group.setGroupCode(groupDto.getGroupCode());
		group.setGroupName(groupDto.getGroupName());
		group.setCreateTime(new Date());
		group.setCreator(groupDto.getCreator());
		group.setGuid(groupDto.getGuid());
		group.setOrgCode(groupDto.getOrgCode());
		group.setRealm(groupDto.getRealm());
		DcmOrganizationDTO orgDTO = userOrgService.getOrgDTOByCodeInCache(groupDto.getOrgCode());
		group.setOrgType(null == orgDTO ? "" : orgDTO.getDomainType());

		final DcmGroup addGroup = this.groupDmn.add(group);
		if (DomainType.PROJECT == groupDto.getDomainType()) {

			String xmfzr = ((ProjectGroupDTO) groupDto).getXmfzr();
			if (StringUtils.hasLength(xmfzr)) {
				logger.info(">>>项目负责人[{}]添加到项目中", xmfzr);
				DcmUser userXmfzr = this.userOrgService.getUserEntityByCode(xmfzr);// .findByLoginNameSimply(xmfzr);
				if (userXmfzr != null)
					this.udrDmn.addUserToDomain(userXmfzr.getId(), addGroup.getDomainType(), addGroup.getGroupCode(),
							RoleConstants.RoleCode.R_Project_Manager);
			}

			String xmzl = ((ProjectGroupDTO) groupDto).getXmzl();
			if (StringUtils.hasLength(xmzl)) {
				logger.info(">>>项目助理[{}]添加到项目中", xmzl);
				DcmUser userXmzl = this.userOrgService.getUserEntityByCode(xmzl);
				if (userXmzl != null)
					this.udrDmn.addUserToDomain(userXmzl.getId(), addGroup.getDomainType(), addGroup.getGroupCode(),
							RoleConstants.RoleCode.R_Project_Assistant);
			}
		}

		if(this.globalConf.openCache()){
			logger.info(">>>async, project is added to cache, key:[{}]...", addGroup.getGuid());
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
				public void run() {
					cacheGroup(addGroup);
				}
			});
		}

		return addGroup;
	}
	
    private void cacheGroup(DcmGroup group) {
		
		this.redisCache.set(CacheKey.PREFIX_PROJECT + group.getId(), group);
		this.redisCache.set(CacheKey.PREFIX_PROJECT + group.getGuid(), group);
	}
	
	@Override
	public DcmGroup addGroupEx(GroupDTO groupDto) {

		DcmGroup group = new DcmGroup();
		group.setDomainType(groupDto.getDomainType());
		group.setGroupCode(groupDto.getGroupCode());
		group.setGroupName(groupDto.getGroupName());
		group.setCreateTime(new Date());
		group.setCreator(groupDto.getCreator());
		group.setGuid(groupDto.getGuid());
		group.setOrgCode(groupDto.getOrgCode());
		group.setRealm(groupDto.getRealm());

		DcmOrganizationDTO orgDTO = userOrgService.getOrgDTOByCodeInCache(groupDto.getOrgCode());
		group.setOrgType(null == orgDTO ? "" : orgDTO.getDomainType());

		final DcmGroup addGroup = this.groupDmn.add(group);
		if (DomainType.PROJECT == groupDto.getDomainType()) {
			// 把初始化时的项目负责人添加到项目负责人的角色中
			String xmfzr = ((ProjectGroupDTO) groupDto).getXmfzr();
			logger.info(">>>项目负责人[{}]添加到项目中", xmfzr);
			DcmUser findUser = this.userOrgService.getUserEntityByCode(xmfzr);// .findByLoginNameSimply(xmfzr);
			if (findUser != null){
				this.udrDmn.addUserToDomain(findUser, addGroup.getDomainType(), addGroup.getGuid(),
						RoleConstants.RoleCode.R_Project_Manager);
			}
			String xmzl = ((ProjectGroupDTO) groupDto).getXmzl();
			if (StringUtils.hasLength(xmzl)) {
				logger.info(">>>项目助理[{}]添加到项目中", xmzl);
				DcmUser userXmzl = this.userOrgService.getUserEntityByCode(xmzl);
				if (userXmzl != null)
					this.udrDmn.addUserToDomain(userXmzl, addGroup.getDomainType(), addGroup.getGuid(),
							RoleConstants.RoleCode.R_Project_Assistant);
			}
		}

		if (this.globalConf.openCache()) {
			logger.info(">>>async, project is added to cache, key:[{}]...", addGroup.getGuid());
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
				public void run() {

					cacheGroup(addGroup);
				}
			});
		}
		return addGroup;
	}
	@Override
	public DcmGroup addProjectGroupNew(GroupDTO groupDto) {

		return this.addGroup(groupDto, RoleConstants.RoleCode.R_P_LEADER, RoleConstants.RoleCode.R_P_ASSISTANT);
		/*DcmGroup group = new DcmGroup();
		group.setDomainType(groupDto.getDomainType());
		group.setGroupCode(groupDto.getGroupCode());
		group.setGroupName(groupDto.getGroupName());
		group.setCreateTime(new Date());
		group.setCreator(groupDto.getCreator());
		group.setGuid(groupDto.getGuid());
		group.setOrgCode(groupDto.getOrgCode());
		group.setRealm(groupDto.getRealm());

		DcmOrganizationDTO orgDTO = userOrgService.getOrgDTOByCodeInCache(groupDto.getOrgCode());
		group.setOrgType(null == orgDTO ? "" : orgDTO.getDomainType());

		final DcmGroup addGroup = this.groupDmn.add(group);
		if (DomainType.PROJECT == groupDto.getDomainType()) {
			// 把初始化时的项目负责人添加到项目负责人的角色中
			String xmfzr = ((ProjectGroupDTO) groupDto).getXmfzr();
			logger.info(">>>项目负责人[{}]添加到项目中", xmfzr);
			DcmUser findUser = this.userOrgService.getUserEntityByCode(xmfzr);// .findByLoginNameSimply(xmfzr);
			if (findUser != null){
				this.udrDmn.addUserToDomain(findUser, addGroup.getDomainType(), addGroup.getGuid(),
						RoleConstants.RoleCode.R_P_LEADER);
			}
			String xmzl = ((ProjectGroupDTO) groupDto).getXmzl();
			if (StringUtils.hasLength(xmzl)) {
				logger.info(">>>项目助理[{}]添加到项目中", xmzl);
				DcmUser userXmzl = this.userOrgService.getUserEntityByCode(xmzl);
				if (userXmzl != null)
					this.udrDmn.addUserToDomain(userXmzl, addGroup.getDomainType(), addGroup.getGuid(),
							RoleConstants.RoleCode.R_P_ASSISTANT);
			}
		}

		if (this.globalConf.openCache()) {
			logger.info(">>>async, project is added to cache, key:[{}]...", addGroup.getGuid());
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
				public void run() {

					cacheGroup(addGroup);
				}
			});
		}
		return addGroup;*/
	}
	@Override
	public DcmGroup addTeamGroup(GroupDTO groupDto) {

		return this.addGroup(groupDto, RoleConstants.RoleCode.R_T_LEADER, RoleConstants.RoleCode.R_T_ASSISTANT);
	}
	/**
	 * 获取负责人
	 * @param groupDto
	 * @return
	 */
	private String getLeader(GroupDTO groupDto) {
		if(groupDto instanceof ProjectGroupDTO) {
			return ((ProjectGroupDTO) groupDto).getXmfzr();
		}
		if(groupDto instanceof TeamGroupDTO) {
			return ((TeamGroupDTO) groupDto).getXmfzr();
		}
		return "";
	}
	/**
	 * 获取助理
	 * @param groupDto
	 * @return
	 */
	private String getAssistant(GroupDTO groupDto) {
		if(groupDto instanceof ProjectGroupDTO) {
			return ((ProjectGroupDTO) groupDto).getXmzl();
		}
		if(groupDto instanceof TeamGroupDTO) {
			return ((TeamGroupDTO) groupDto).getXmzl();
		}
		return "";
	}
	@Override
	public DcmGroup addGroup(GroupDTO groupDto, String leaderRoleCode, String assistantRoleCode) {

		DcmGroup group = new DcmGroup();
		group.setDomainType(groupDto.getDomainType());
		group.setGroupCode(groupDto.getGroupCode());
		group.setGroupName(groupDto.getGroupName());
		group.setCreateTime(new Date());
		group.setCreator(groupDto.getCreator());
		group.setGuid(groupDto.getGuid());
		group.setOrgCode(groupDto.getOrgCode());
		group.setRealm(groupDto.getRealm());

		DcmOrganizationDTO orgDTO = userOrgService.getOrgDTOByCodeInCache(groupDto.getOrgCode());
		group.setOrgType(null == orgDTO ? "" : orgDTO.getDomainType());

		final DcmGroup addGroup = this.groupDmn.add(group);
		if (DomainType.PROJECT == groupDto.getDomainType()) {
			// 把初始化时的项目负责人添加到项目负责人的角色中
			String xmfzr =this.getLeader(groupDto);
			logger.info(">>>项目负责人[{}]添加到项目中", xmfzr);
			DcmUser findUser = this.userOrgService.getUserEntityByCode(xmfzr);// .findByLoginNameSimply(xmfzr);
			if (findUser != null){
				this.udrDmn.addUserToDomain(findUser, addGroup.getDomainType(), addGroup.getGuid(),
						leaderRoleCode);
			}
			String xmzl = this.getAssistant(groupDto);
			if (StringUtils.hasLength(xmzl)) {
				logger.info(">>>项目助理[{}]添加到项目中", xmzl);
				DcmUser userXmzl = this.userOrgService.getUserEntityByCode(xmzl);
				if (userXmzl != null)
					this.udrDmn.addUserToDomain(userXmzl, addGroup.getDomainType(), addGroup.getGuid(),
							assistantRoleCode);
			}
		}

		if (this.globalConf.openCache()) {
			logger.info(">>>async, project is added to cache, key:[{}]...", addGroup.getGuid());
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
				public void run() {
					cacheGroup(addGroup);
				}
			});
		}
		return addGroup;
	}
	
    private void removeGroup(String id) {
		
		this.redisCache.remove(CacheKey.PREFIX_PROJECT + id);
		this.redisCache.remove(CacheKey.PREFIX_PROJECT + id);
	}
	
	@Override
	public void deleteGroupByGuid(final String guid) {

		this.groupDmn.deleteGroupByGuid(guid);

		if (this.globalConf.openCache()) {
			logger.info(">>>删除缓存组信息......");
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {

				@Override
				public void run() {
					removeGroup(guid);
				}
			});
		}
	}
	@Override
	public void deleteGroupByCode(String caseIdentifier) {
		
		this.groupDmn.deleteGroupByCode(caseIdentifier);
			
	}
	@Override
	public List<DcmGroup> listProjectGroups() {
        
		return this.groupDmn.listProjectGroups();
	}
	@Override
	public List<DcmGroup> listDiscussionGroups() {
		
		return this.groupDmn.listDiscussionGroups();
	}
	@Override
	public List<GroupUserDTO> listUsersOfGroupByCode(String contextPath, String baseURL, String groupCode) {
		
		return this.listUsersOfProjectGroupByCode(contextPath, baseURL, new String[]{groupCode}).get(groupCode).getRoles();
	}
	
	@Override
	public List<GroupUserExDTO> listUsersOfGroupByCodeEx(String contextPath, String baseURL, String groupCode) {
		
		return this.listUsersOfProjectGroupByCodeEx(contextPath, baseURL, new String[]{groupCode}).get(groupCode).getRoles();
	}
	@Override
	public List<GroupUserExDTO> listUsersOfGroupByCodeRealm(String contextPath, String baseURL, String groupCode) {
		
		DcmGroup group = this.groupDmn.getGroupByGuid(groupCode);
		List<DcmRole> roles = null;
		if(group.getGroupCode().startsWith("XZ_CPTDGL_")) {
			// 团队类型
			// 获取团队角色
			roles = this.roleService.getRolesOfTeam(group.getRealm());
		} else {
			// 项目类型
			// 获取项目角色
			roles = this.roleService.getRolesOfProject(group.getRealm());
		}
		ProjectUserResponseExDTO dto = this.listUsersOfProjectGroupByCodeRealm(contextPath, baseURL, group, roles);
		return null == dto? null : dto.getRoles();
	}
	@Override
	public List<GroupUserExDTO> listUsersOfProjectGroupByCodeRealm(String contextPath, String baseURL, String groupCode) {
		
		DcmGroup projectGroup = this.groupDmn.getGroupByGuid(groupCode);
		// 获取项目角色
		List<DcmRole> roles = this.roleService.getRolesOfProject(projectGroup.getRealm());
		ProjectUserResponseExDTO dto = this.listUsersOfProjectGroupByCodeRealm(contextPath, baseURL, projectGroup, roles);
		return null == dto? null : dto.getRoles();
	}
	@Override
	public List<GroupUserExDTO> listUsersOfTeamGroupByCodeRealm(String contextPath, String baseURL, String groupCode) {
		
		DcmGroup projectGroup = this.groupDmn.getGroupByGuid(groupCode);
		// 获取团队角色
		List<DcmRole> roles = this.roleService.getRolesOfTeam(projectGroup.getRealm());
		ProjectUserResponseExDTO dto = this.listUsersOfProjectGroupByCodeRealm(contextPath, baseURL, projectGroup, roles);
		return null == dto? null : dto.getRoles();
	}
	@Override
	public List<GroupUserDTO> listUsersOfGroupByCodeGZ(String contextPath, String baseURL, String groupCode) {
		
		return this.listUsersOfProjectGroupByCodeGZ(contextPath, baseURL, new String[]{groupCode}).get(groupCode).getRoles();
	}
	
	@Override
	public Map<String, ProjectUserResponseDTO> listUsersOfProjectGroupByCodeGZ(String contextPath, String baseURL, Object[] groupCodes) {
		
        baseURL = baseURL.endsWith("/")? baseURL : (baseURL+"/");
		
		Map<String, ProjectUserResponseDTO> groupUserMap = new HashMap<String, ProjectUserResponseDTO>();
		// 获取角色列表
		List<DcmRole> roles = this.roleDmn.getRolesOfProject();
		for(Object groupGuid : groupCodes){
			ProjectUserResponseDTO userResponse = new ProjectUserResponseDTO();
			DcmGroup findGroup = this.groupDmn.getGroupByCode(groupGuid.toString());
			userResponse.setProjectName(null == findGroup? "" : findGroup.getGroupName());
			
			List<GroupUserDTO> groupusers = new ArrayList<GroupUserDTO>();
			for(DcmRole r : roles) {
				GroupUserDTO newGUD = new GroupUserDTO();
				newGUD.setId(r.getId());
				newGUD.setRoleCode(r.getRoleCode());
				newGUD.setRoleName(r.getRoleName());
				List<DcmUserdomainrole> udrs = this.udrDmn.getByDomainCodeRoleCode(groupGuid.toString(), r.getRoleCode());
				List<DcmUser> users = new ArrayList<DcmUser>();
				for(DcmUserdomainrole udr : udrs){
					DcmUser findUser = this.userOrgService.getUserEntityById(udr.getUserId()).clone();//.loadById().clone();
					if(findUser != null) {
						findUser.setAvatar(this.userOrgService.getUserAvatarLink(contextPath, baseURL, findUser));

						users.add(findUser);
					
					}
				}
				newGUD.setUsers(users);
				
				groupusers.add(newGUD);
		
			}
			userResponse.setRoles(groupusers);
			groupUserMap.put(groupGuid.toString(), userResponse);
		}

		return groupUserMap;
		
	}
	
	@Override
	public Map<String, ProjectUserResponseDTO> listUsersOfProjectGroupByCode(String contextPath, String baseURL, String[] groupCodes) {
		
        baseURL = baseURL.endsWith("/")? baseURL : (baseURL+"/");
		
		Map<String, ProjectUserResponseDTO> groupUserMap = new HashMap<String, ProjectUserResponseDTO>();
		// 获取角色列表
		List<DcmRole> roles = this.roleDmn.getRolesOfProject();
		for(String groupGuid : groupCodes){
			ProjectUserResponseDTO userResponse = new ProjectUserResponseDTO();
			DcmGroup findGroup = this.groupDmn.getGroupByGuid(groupGuid);
			userResponse.setProjectName(null == findGroup? "" : findGroup.getGroupName());
			
			List<GroupUserDTO> groupusers = new ArrayList<GroupUserDTO>();
			for(DcmRole r : roles) {
				GroupUserDTO newGUD = new GroupUserDTO();
				newGUD.setId(r.getId());
				newGUD.setRoleCode(r.getRoleCode());
				newGUD.setRoleName(r.getRoleName());
				List<DcmUserdomainrole> udrs = this.udrDmn.getByDomainCodeRoleCode(groupGuid, r.getRoleCode());
				List<DcmUser> users = new ArrayList<DcmUser>();
				for(DcmUserdomainrole udr : udrs){

					UserProjectResponseDTO respDTO = new UserProjectResponseDTO();
					if(0 == udr.getUserType()){
						logger.info(">>>内部用户："+udr.getUserCode());
						DcmUser findUser = this.userOrgService.getUserEntityByCode(udr.getUserCode());
						if (findUser != null) {
							respDTO.setId(findUser.getId());
							respDTO.setUserName(findUser.getUserName());
							respDTO.setUserCode(findUser.getUserCode());
							respDTO.setDateCreated(udr.getLastTime());
							respDTO.setAvatar(this.userOrgService.getUserAvatarLink(contextPath, baseURL, findUser));
							respDTO.setIsTop(udr.getIsTop());
							respDTO.setIsPartner(0);
							users.add(respDTO);
						}
					}else {
						logger.info(">>>外部用户："+udr.getUserCode());
						UserResponseDTO userResp = sgaUserService.getUserByCode(udr.getUserCode());
						if(userResp != null){
							respDTO.setId(userResp.getId());
							respDTO.setUserName(userResp.getUserName());
							respDTO.setLoginName(userResp.getLoginName());
							respDTO.setDateCreated(udr.getLastTime());
							respDTO.setUserCode(userResp.getUserCode());
							respDTO.setIsTop(udr.getIsTop());
							respDTO.setIsPartner(1);
							respDTO.setAvatar(userResp.getAvatar()); // 微信注册的前提下，才直接获取微信头像
							users.add(respDTO);
						}
					}
					/*DcmUser findUser = this.userOrgService.getUserEntityById(udr.getUserId());//.clone();//.loadById().clone();
					if(findUser != null) {
						findUser.setUserPwd("");
						findUser.setAvatar(this.userOrgService.getUserAvatarLink(contextPath, baseURL, findUser));
						users.add(findUser);	
					}*/
				}
				newGUD.setUsers(users);		
				groupusers.add(newGUD);
		
			}
			userResponse.setRoles(groupusers);
			groupUserMap.put(groupGuid, userResponse);
		}

		return groupUserMap;
		
	}
	
	public Map<String, ProjectUserResponseExDTO> listUsersOfProjectGroupByCodeEx(String contextPath, String baseURL,
			String[] groupCodes) {

		baseURL = baseURL.endsWith("/") ? baseURL : (baseURL + "/");

		Map<String, ProjectUserResponseExDTO> groupUserMap = new HashMap<String, ProjectUserResponseExDTO>();
		// 获取角色列表
		List<DcmRole> roles = this.roleDmn.getRolesOfProject();
		for (String groupGuid : groupCodes) {
			ProjectUserResponseExDTO userResponse = new ProjectUserResponseExDTO();
			DcmGroup findGroup = this.groupDmn.getGroupByGuid(groupGuid);
			userResponse.setProjectName(null == findGroup ? "" : findGroup.getGroupName());

			List<GroupUserExDTO> groupusers = new ArrayList<GroupUserExDTO>();
			for (DcmRole r : roles) {
				GroupUserExDTO newGUD = new GroupUserExDTO();
				newGUD.setId(r.getId());
				newGUD.setRoleCode(r.getRoleCode());
				newGUD.setRoleName(r.getRoleName());
				List<DcmUserdomainrole> udrs = this.udrDmn.getByDomainCodeRoleCode(groupGuid, r.getRoleCode());
				List<UserProjectResponseDTO> users = new ArrayList<UserProjectResponseDTO>(udrs.size());
				for (DcmUserdomainrole udr : udrs) {
					UserProjectResponseDTO respDTO = new UserProjectResponseDTO();
					if(0 == udr.getUserType()){
						logger.info(">>>内部用户："+udr.getUserCode());
						DcmUser findUser = this.userOrgService.getUserEntityByCode(udr.getUserCode());
						if (findUser != null) {
							respDTO.setId(findUser.getId());
							respDTO.setUserName(findUser.getUserName());
							respDTO.setUserCode(findUser.getUserCode());
							respDTO.setDateCreated(udr.getLastTime());
							respDTO.setAvatar(this.userOrgService.getUserAvatarLink(contextPath, baseURL, findUser));
							respDTO.setIsTop(udr.getIsTop());
							respDTO.setIsPartner(0);
							users.add(respDTO);
						}
					}else {
						logger.info(">>>外部用户："+udr.getUserCode());
						UserResponseDTO userResp = sgaUserService.getUserByCode(udr.getUserCode());
						if(userResp != null){
							respDTO.setId(userResp.getId());
							respDTO.setUserName(userResp.getUserName());
							respDTO.setDateCreated(udr.getLastTime());
							respDTO.setUserCode(userResp.getUserCode());
							respDTO.setIsTop(udr.getIsTop());
							respDTO.setIsPartner(1);
							respDTO.setAvatar(userResp.getAvatar()); // 微信注册的前提下，才直接获取微信头像
							users.add(respDTO);
						}
					}
				}
				newGUD.setUsers(users);
				groupusers.add(newGUD);
			}
			userResponse.setRoles(groupusers);
			groupUserMap.put(groupGuid, userResponse);
		}
		return groupUserMap;
	}
	public ProjectUserResponseExDTO listUsersOfProjectGroupByCodeRealm(String contextPath, String baseURL,
			DcmGroup findGroup, List<DcmRole> roles) {

		baseURL = baseURL.endsWith("/") ? baseURL : (baseURL + "/");

		ProjectUserResponseExDTO userResponse = new ProjectUserResponseExDTO();
		userResponse.setProjectName(null == findGroup ? "" : findGroup.getGroupName());

		List<GroupUserExDTO> groupusers = new ArrayList<GroupUserExDTO>();
		for (DcmRole r : roles) {
			GroupUserExDTO newGUD = new GroupUserExDTO();
			newGUD.setId(r.getId());
			newGUD.setRoleCode(r.getRoleCode());
			newGUD.setRoleName(r.getRoleName());
			List<DcmUserdomainrole> udrs = this.udrDmn.getByDomainCodeRoleCode(findGroup.getGuid(), r.getRoleCode());
			List<UserProjectResponseDTO> users = new ArrayList<UserProjectResponseDTO>(udrs.size());
			for (DcmUserdomainrole udr : udrs) {
				UserProjectResponseDTO respDTO = new UserProjectResponseDTO();
				if(0 == udr.getUserType()){
					logger.info(">>>内部用户："+udr.getUserCode());
					DcmUser findUser = this.userOrgService.getUserEntityByCode(udr.getUserCode());
					if (findUser != null) {
						respDTO.setId(findUser.getId());
						respDTO.setUserName(findUser.getUserName());
						respDTO.setUserCode(findUser.getUserCode());
						respDTO.setDateCreated(udr.getLastTime());
						respDTO.setAvatar(this.userOrgService.getUserAvatarLink(contextPath, baseURL, findUser));
						respDTO.setIsTop(udr.getIsTop());
						respDTO.setIsPartner(0);
						respDTO.setTeam(this.teamDmn.getTeam(findGroup.getId(), findUser.getId()));
						users.add(respDTO);
					}
				}else {
					logger.info(">>>外部用户："+udr.getUserCode());
					UserResponseDTO userResp = sgaUserService.getUserByCode(udr.getUserCode());
					if(userResp != null){
						respDTO.setId(userResp.getId());
						respDTO.setUserName(userResp.getUserName());
						respDTO.setDateCreated(udr.getLastTime());
						respDTO.setUserCode(userResp.getUserCode());
						respDTO.setIsTop(udr.getIsTop());
						respDTO.setIsPartner(1);
						respDTO.setAvatar(userResp.getAvatar()); // 微信注册的前提下，才直接获取微信头像
						users.add(respDTO);
					}
				}
			}
			newGUD.setUsers(users);
			groupusers.add(newGUD);
		}
		userResponse.setRoles(groupusers);
		return userResponse;
	}
	@Override
	public ProjectGroupDTO updateProjectGroup(GroupDTO groupDto){
		
		DcmGroup g = this.groupDmn.updateGroup(groupDto.getGroupCode(), groupDto.getGroupName());
		
		ProjectGroupDTO dto = new ProjectGroupDTO();
		dto.setId(g.getId());
		dto.setCreateTime(DateUtil.toDateTimeStr(g.getCreateTime()));
		dto.setModifiedTime(DateUtil.toDateTimeStr(g.getModifiedTime()));
		dto.setCreator(g.getCreator());
		dto.setGroupCode(g.getGroupCode());
		dto.setGroupName(g.getGroupName());
		
		return dto;
	}
	@Override
	public DiscussionGroupDTO updateDiscussionGroup(GroupDTO groupDto){
		
        DcmGroup g = this.groupDmn.updateGroup(groupDto.getGroupCode(), groupDto.getGroupName());
		
		DiscussionGroupDTO dto = new DiscussionGroupDTO();
		dto.setId(g.getId());
		dto.setCreateTime(DateUtil.toDateTimeStr(g.getCreateTime()));
		dto.setModifiedTime(DateUtil.toDateTimeStr(g.getModifiedTime()));
		dto.setCreator(g.getCreator());
		dto.setGroupCode(g.getGroupCode());
		dto.setGroupName(g.getGroupName());
		
		return dto;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Object listProjectsByPage(PageProjectPara page, String flag) {
		
		DcmUser user = this.userOrgService.getUserEntityByLoginName(page.getLoginName());

		if(flag.equals(Flag.ALL)){
			return getAllProjectPriv(page, user);
		}else {
			return getPageProject(page, user);

		}
	
	}
	private Object getPageProject(PageProjectPara page, DcmUser user) {
		// 表示只是跟个人相关的项目
		StringBuilder buf = new StringBuilder();
		buf.append("SELECT DISTINCT GROUPCODE");
		buf.append("  FROM (SELECT ROWNUM RNO, GROUPCODE");
		buf.append("          FROM (SELECT DISTINCT G.GROUPCODE, G.CREATETIME");
		buf.append("                  FROM DCM_GROUP G");
		buf.append("                  INNER JOIN DCM_USERDOMAINROLE UDR ON G.GROUPCODE =");
		buf.append("                                                            UDR.DOMAINCODE");
		buf.append("                 WHERE G.DOMAINTYPE = '"+DomainType.PROJECT+"'");
		buf.append("                   AND UDR.DOMAINTYPE = '"+DomainType.PROJECT+"'");
		buf.append("                   AND UDR.USERID = "+user.getId());
		buf.append("                 ORDER BY G.CREATETIME DESC)");
		buf.append("         WHERE ROWNUM <= "+page.getPageNo()+" * "+page.getPageSize()+")");
		buf.append(" WHERE RNO > ("+page.getPageNo()+" - 1) * "+page.getPageSize());
		
		return this.groupDmn.queryBySql(buf.toString());
	}
	private Object getAllProjectPriv(PageProjectPara page, DcmUser user) {
		// 表示在所有项目列表基础上，进行分页获取	
		StringBuilder buf = new StringBuilder();
		buf.append("SELECT DISTINCT DOMAINCODE FROM DCM_USERDOMAINROLE WHERE USERID=");
		buf.append(user.getId());
		buf.append(" AND domainType='");
		buf.append(DomainType.PROJECT);
		buf.append("'");
		List<String> projectNumList = (List<String>)this.udrDmn.queryBySql(buf.toString());

		Pagination records = this.groupDmn.findByProperty(page.getPageNo(), page.getPageSize(), "domainType", DomainType.PROJECT, "createTime", false);
		
		//result.setHaveMore(records.getData().size()<=page.getPageSize());
		List<DcmGroup> groups = (List<DcmGroup>) records.getData();
		PageProjectResult result = new PageProjectResult();

		for(final DcmGroup g : groups) {
			
			result.add(g.getGroupCode(), (projectNumList.contains(g.getGroupCode())));
		}
		
		return result;
	}
	
	@Override
	public Object searchProjectsByPage(PageProjectPara page, boolean isPersonal) {

		PageProjectResult result = new PageProjectResult();

		try {
			/*
			 * if(StringUtil.isNullOrEmpty(page.getKeyword()) |
			 * StringUtil.isNullOrEmpty(page.getKeyword().trim())){ return null;
			 * }
			 */

			// loginName：dn
			DcmUser user = this.userOrgService.getUserEntityByDN(page.getLoginName());// .getUserEntityByCode(page.getUserCode());//.getUserByLoginNameInCache(page.getLoginName());//.userDmn.findByLoginName(page.getLoginName());

			// 表示在所有项目列表基础上，进行分页获取

			/*
			 * Map<String, List<String>> privCodesMap = new HashMap<String,
			 * List<String>>(); List<String> projectCodes = new
			 * ArrayList<String>();
			 */
			// 遍历当前人在项目和机构中的权限
			ProjectAndOrgPrivCodes pao = getProjectAndOrgPrivCodesObj(user);

			Pagination records = null;
			if (isPersonal) {

				if (0 == pao.ProjectCodes.size()) {
					return result;
				}
				// 个人项目暂不支持检索
				String[] projectCodeArray = new String[pao.ProjectCodes.size()];
				Map<String, Object[]> map = new HashMap<String, Object[]>();
				pao.ProjectCodes.toArray(projectCodeArray);
				logger.debug("projectCodeArray : [{}]", JSONObject.toJSONString(projectCodeArray));
				
				map.put("guid", projectCodeArray);

				records = this.groupDmn.findByProperty(page.getPageNo(), page.getPageSize(), map, "createTime", false);

			} else {
				Map<String, Object[]> equalProperties = new HashMap<String, Object[]>();
				equalProperties.put("domainType", new Object[]{DomainType.PROJECT});
				// 考虑多租户的情况
				equalProperties.put("realm", new String[]{user.getRealm()});

				Map<String, Object[]> likeProperties = new HashMap<String, Object[]>();
				if (!StringUtil.isNullOrEmpty(page.getKeyword()) && !page.getKeyword().equals("*")) {
					// 不是全部
					likeProperties.put("groupCode", new Object[]{page.getKeyword()});
					likeProperties.put("groupName", new Object[]{page.getKeyword()});
					likeProperties.put("creator", new Object[]{page.getKeyword()});
					likeProperties.put("guid", new Object[]{page.getKeyword()});
				}

				records = this.groupDmn.findByProperties(page.getPageNo(), page.getPageSize(), equalProperties,
						likeProperties, "createTime", false);
			}

			@SuppressWarnings("unchecked")
			List<DcmGroup> groups = (List<DcmGroup>) records.getData();
			logger.debug("获取到的项目组：[{}]", JSONObject.toJSONString(records.getData()));

			DcmOrganizationDTO orgDTO = null;
			List<String> privCodes = null;
			for (final DcmGroup g : groups) {

				ProjectOrgMaskDTO prjOrgDTO = new ProjectOrgMaskDTO();
				if (!StringUtils.isEmpty(g.getOrgCode())){
					orgDTO = this.userOrgService.getOrgDTOByCodeInCache(g.getOrgCode());
				}else{
					orgDTO = null;
				}
				
				prjOrgDTO.setOrgName(null == orgDTO ? "" : orgDTO.getAlias());

				privCodes = pao.PrivCodesMap.containsKey(g.getGuid()) ? pao.PrivCodesMap.get(g.getGuid())
						: new ArrayList<String>();
				logger.info(">>>当前人在项目组中权限 [{}]", privCodes);

				List<String> tempCodes = pao.PrivCodesMap.containsKey(g.getOrgCode())
						? pao.PrivCodesMap.get(g.getOrgCode()) : new ArrayList<String>();
				logger.info(">>>当前人在项目的牵头部门中的权限 [{}]", tempCodes);
				privCodes.addAll(tempCodes);
				prjOrgDTO.setPrivCodes(privCodes);

				result.add(g.getGuid(), prjOrgDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			// ex.printStackTrace();
		}
		return result;

	}
	
	@Override
	public Object searchProjectsByPage(PageProjectPara page, String[] projectType, boolean isPersonal) {

		PageProjectResult result = new PageProjectResult();

		Map<String, Object[]> equalProperties = new ConcurrentHashMap<String, Object[]>();
		Map<String, Object[]> likeProperties = new ConcurrentHashMap<String, Object[]>();
		try {
			// loginName：dn
			DcmUser user = this.userOrgService.getUserEntityByCode(page.getUserCode());

			// 表示在所有项目列表基础上，进行分页获取
			// 遍历当前人在项目和机构中的权限
			ProjectAndOrgPrivCodes prjAndOrgPrivCodes = this.getProjectAndOrgPrivCodesObj(user);
			// 项目分页数据
			Pagination prjPageData = null;
			if (isPersonal) {

				if (0 == prjAndOrgPrivCodes.ProjectCodes.size()) {
					return result;
				}
				// 个人项目暂不支持检索
				String[] projectCodeArray = new String[prjAndOrgPrivCodes.ProjectCodes.size()];

				prjAndOrgPrivCodes.ProjectCodes.toArray(projectCodeArray);
				logger.debug(">>>projectCodeArray : [{}]", JSONObject.toJSONString(projectCodeArray));
				equalProperties.put("guid", projectCodeArray);
				equalProperties.put("realm", new String[] { page.getRealm() });

				// 根据项目类型筛选
				likeProperties.put("groupCode", projectType);
				if (!StringUtil.isNullOrEmpty(page.getKeyword()) && !page.getKeyword().equals("*")) {
					// 不是全部
					likeProperties.put("groupCode", new Object[] { page.getKeyword() });
					likeProperties.put("groupName", new Object[] { page.getKeyword() });
					likeProperties.put("guid", new Object[] { page.getKeyword() });
				}
				 prjPageData = this.groupDmn.findByProperties(page.getPageNo(), page.getPageSize(), equalProperties, likeProperties,
						"createTime", false);

				// records = this.groupDmn.findByProperty(page.getPageNo(),
				// page.getPageSize(), map, "createTime", false);

			} else {

				equalProperties.put("domainType", new Object[] { DomainType.PROJECT });
				equalProperties.put("realm", new Object[] { page.getRealm() });

				if (!StringUtil.isNullOrEmpty(page.getKeyword()) && !page.getKeyword().equals("*")) {
					// 不是全部
					likeProperties.put("groupCode", new Object[] { page.getKeyword() });
					likeProperties.put("groupName", new Object[] { page.getKeyword() });
					likeProperties.put("guid", new Object[] { page.getKeyword() });
				}

				// 根据项目类型筛选，项目编码的前缀是项目类型
				likeProperties.put("groupCode", projectType);
				prjPageData = this.groupDmn.findByProperties(page.getPageNo(), page.getPageSize(), equalProperties,
						likeProperties, "createTime", false);
			}

			@SuppressWarnings("unchecked")
			List<DcmGroup> groups = (List<DcmGroup>) prjPageData.getData();
			logger.debug(">>>获取到的项目组：[{}]", JSONObject.toJSONString(prjPageData.getData()));

			DcmOrganizationDTO orgDTO = null;
			List<String> privCodes = null;
			for (final DcmGroup g : groups) {

				ProjectOrgMaskDTO prjOrgDTO = new ProjectOrgMaskDTO();
				if (!StringUtils.isEmpty(g.getOrgCode())) {
					orgDTO = this.userOrgService.getOrgDTOByCodeInCache(g.getOrgCode());
				} else {
					orgDTO = null;
				}

				prjOrgDTO.setOrgName(null == orgDTO ? "" : orgDTO.getAlias());

				privCodes = prjAndOrgPrivCodes.PrivCodesMap.containsKey(g.getGuid()) ? prjAndOrgPrivCodes.PrivCodesMap.get(g.getGuid())
						: new ArrayList<String>();
				logger.info(">>>当前人在项目组中权限 [{}]", privCodes);

				List<String> tempCodes = prjAndOrgPrivCodes.PrivCodesMap.containsKey(g.getOrgCode())
						? prjAndOrgPrivCodes.PrivCodesMap.get(g.getOrgCode()) : new ArrayList<String>();
				logger.info(">>>当前人在项目的牵头部门中的权限 [{}]", tempCodes);
				privCodes.addAll(tempCodes);
				prjOrgDTO.setPrivCodes(privCodes);

				result.add(g.getGuid(), prjOrgDTO);
			}

		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error(ex.getMessage(),ex);
		}
		return result;

	}
	
	@Override
	public Object searchOthersProjectsByPage(PageProjectPara page, String[] projectTypes) {
		
		PageProjectResult result = new PageProjectResult();

		/*Map<String, Object[]> equalProperties = new ConcurrentHashMap<String, Object[]>();
		Map<String, Object[]> likeProperties = new ConcurrentHashMap<String, Object[]>();
		Map<String, Object[]> notProperties = new ConcurrentHashMap<String, Object[]>();*/
		try {
			DcmUser user = this.userOrgService.getUserEntityByCode(page.getUserCode());

			// 表示在所有项目列表基础上，进行分页获取
			// 遍历当前人在项目和机构中的权限
			ProjectAndOrgPrivCodes prjAndOrgPrivCodes = this.getProjectAndOrgPrivCodesObj(user);

			Criteria criteria = this.groupDmn.createCriteria();
			
			criteria.add(Restrictions.eq("domainType",  DomainType.PROJECT));
			criteria.add(Restrictions.eq("realm",  page.getRealm()));

			if (!StringUtil.isNullOrEmpty(page.getKeyword()) && !page.getKeyword().equals("*")) {
				// 不是全部
				/*likeProperties.put("groupCode", new Object[] { page.getKeyword() });
				likeProperties.put("groupName", new Object[] { page.getKeyword() });*/
				/*likeProperties.put("creator", new Object[] { page.getKeyword() });
				likeProperties.put("guid", new Object[] { page.getKeyword() });*/
				Disjunction  disKeyword = Restrictions.disjunction();
				disKeyword.add(Restrictions.like("groupCode", page.getKeyword(), MatchMode.ANYWHERE));
				disKeyword.add(Restrictions.like("groupName", page.getKeyword(), MatchMode.ANYWHERE));
				disKeyword.add(Restrictions.like("guid", page.getKeyword(), MatchMode.ANYWHERE));
				criteria.add(disKeyword);
			}
			if(projectTypes != null && projectTypes.length>0){
				// 根据项目类型筛选，项目编码的前缀是项目类型
				Disjunction  disPrjType = Restrictions.disjunction();
				//likeProperties.put("groupCode", projectType);
				for(String subPrjType : projectTypes){
					disPrjType.add(Restrictions.like("groupCode", subPrjType, MatchMode.START));
				}
				criteria.add(disPrjType);
			}
		  
			// 非条件，排除当前用户的项目
			String[] projectCodeArray = new String[prjAndOrgPrivCodes.ProjectCodes.size()];
			prjAndOrgPrivCodes.ProjectCodes.toArray(projectCodeArray);
			if(projectCodeArray.length>0){
				criteria.add(Restrictions.not(Restrictions.in("guid", projectCodeArray)));
				//notProperties.put("guid", projectCodeArray);
			}
			criteria.addOrder(Order.desc("createTime"));
			// 项目分页数据
			Pagination prjPageData = this.groupDmn.findByCriteria(page.getPageNo(), page.getPageSize(), criteria);
			/*this.groupDmn.findByProperties(page.getPageNo(), page.getPageSize(), equalProperties,
					likeProperties, notProperties, "createTime", false);*/
			@SuppressWarnings("unchecked")
			List<DcmGroup> groups = (List<DcmGroup>) prjPageData.getData();
			//logger.debug(">>>获取到的项目组：[{}]", JSONObject.toJSONString(prjPageData.getData()));

			DcmOrganizationDTO orgDTO = null;
			List<String> privCodes = null;
			for (final DcmGroup g : groups) {

				ProjectOrgMaskDTO prjOrgDTO = new ProjectOrgMaskDTO();
				if (!StringUtils.isEmpty(g.getOrgCode())) {
					orgDTO = this.userOrgService.getOrgDTOByCodeInCache(g.getOrgCode());
				} else {
					orgDTO = null;
				}

				prjOrgDTO.setOrgName(null == orgDTO ? "" : orgDTO.getAlias());

				privCodes = prjAndOrgPrivCodes.PrivCodesMap.containsKey(g.getGuid()) ? prjAndOrgPrivCodes.PrivCodesMap.get(g.getGuid())
						: new ArrayList<String>();
				logger.info(">>>当前人在项目组中权限 [{}]", privCodes);

				List<String> tempCodes = prjAndOrgPrivCodes.PrivCodesMap.containsKey(g.getOrgCode())
						? prjAndOrgPrivCodes.PrivCodesMap.get(g.getOrgCode()) : new ArrayList<String>();
				logger.info(">>>当前人在项目的牵头部门中的权限 [{}]", tempCodes);
				privCodes.addAll(tempCodes);
				prjOrgDTO.setPrivCodes(privCodes);

				result.add(g.getGuid(), prjOrgDTO);
			}

		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error(ex.getMessage(), ex);
		}
		return result;
	}
	
	public Object searchProjectsByPageGZ(PageProjectPara page, boolean isPersonal) {

		PageProjectResult result = new PageProjectResult();

		try {
			// loginName：dn
			DcmUser user = this.userOrgService.getUserEntityByLoginName(page.getLoginName());// .getUserEntityByCode(page.getUserCode());//.getUserByLoginNameInCache(page.getLoginName());//.userDmn.findByLoginName(page.getLoginName());

			// 表示在所有项目列表基础上，进行分页获取

			/*
			 * Map<String, List<String>> privCodesMap = new HashMap<String,
			 * List<String>>(); List<String> projectCodes = new
			 * ArrayList<String>();
			 */
			// 遍历当前人在项目和机构中的权限
			ProjectAndOrgPrivCodes pao = getProjectAndOrgPrivCodesObj(user);

			Pagination records = null;
			if (isPersonal) {

				if (0 == pao.ProjectCodes.size()) {
					return result;
				}
				// 个人项目暂不支持检索
				String[] projectCodeArray = new String[pao.ProjectCodes.size()];
				Map<String, Object[]> map = new HashMap<String, Object[]>();
				pao.ProjectCodes.toArray(projectCodeArray);
				logger.debug("projectCodeArray : [{}]", JSONObject.toJSONString(projectCodeArray));
				map.put("groupCode", projectCodeArray);

				records = this.groupDmn.findByProperty(page.getPageNo(), page.getPageSize(), map, "createTime", false);

			} else {
				Map<String, Object[]> equalProperties = new HashMap<String, Object[]>();
				equalProperties.put("domainType", new Object[]{DomainType.PROJECT});

				Map<String, Object[]> likeProperties = new HashMap<String, Object[]>();
				if (!StringUtil.isNullOrEmpty(page.getKeyword()) && !page.getKeyword().equals("*")) {
					// 不是全部
					likeProperties.put("groupCode", new Object[]{page.getKeyword()});
					likeProperties.put("groupName", new Object[]{page.getKeyword()});
					likeProperties.put("creator", new Object[]{page.getKeyword()});
					likeProperties.put("guid", new Object[]{page.getKeyword()});
				}

				records = this.groupDmn.findByProperties(page.getPageNo(), page.getPageSize(), equalProperties,
						likeProperties, "createTime", false);
			}

			@SuppressWarnings("unchecked")
			List<DcmGroup> groups = (List<DcmGroup>) records.getData();
			logger.debug("获取到的项目组：[{}]", JSONObject.toJSONString(records.getData()));

			DcmOrganizationDTO orgDTO = null;
			List<String> privCodes = null;
			for (final DcmGroup g : groups) {

				ProjectOrgMaskDTO prjOrgDTO = new ProjectOrgMaskDTO();
				orgDTO = this.userOrgService.getOrgDTOByCodeInCache(g.getOrgCode());
				prjOrgDTO.setOrgName(null == orgDTO ? "" : orgDTO.getAlias());

				privCodes = pao.PrivCodesMap.containsKey(g.getGroupCode()) ? pao.PrivCodesMap.get(g.getGroupCode())
						: new ArrayList<String>();
				logger.info(">>>当前人在项目组中权限 [{}]", privCodes);

				List<String> tempCodes = pao.PrivCodesMap.containsKey(g.getOrgCode())
						? pao.PrivCodesMap.get(g.getOrgCode()) : new ArrayList<String>();
				logger.info(">>>当前人在项目的牵头部门中的权限 [{}]", tempCodes);
				privCodes.addAll(tempCodes);
				prjOrgDTO.setPrivCodes(privCodes);

				result.add(g.getGroupCode(), prjOrgDTO);
			}

			return result;
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return result;
	}
	/**
	 * 内部类
	 * @author weifj
	 *
	 */
	class ProjectAndOrgPrivCodes {
		
		protected DcmUser User;
		protected Map<String, List<String>> PrivCodesMap = new HashMap<String, List<String>>();
		protected List<String> ProjectCodes = new ArrayList<String>();
		
	}
	private ProjectAndOrgPrivCodes getProjectAndOrgPrivCodesObj(DcmUser user) {
		
		Map<String, Object[]> propertyValues = new HashMap<String, Object[]>();
		propertyValues.put("userCode", new Object[] { user.getUserCode() });
		propertyValues.put("domainType", new Object[] { DomainType.INSTITUTE, DomainType.DEPARTMENT,
				DomainType.DISCUSSION, DomainType.PROJECT });
		
		return getProjectAndOrgPrivCodesObj(propertyValues);
		
	}
	
	private ProjectAndOrgPrivCodes getProjectAndOrgPrivCodesObj(Map<String, Object[]> propertyValuesFilter) {
		
		ProjectAndOrgPrivCodes pao = new ProjectAndOrgPrivCodes();
		
		List<String> privCodes;
		List<DcmUserdomainrole> udrs = this.udrDmn.findByProperties(propertyValuesFilter);
		if (udrs != null && !udrs.isEmpty()) {

			for (DcmUserdomainrole udr : udrs) {

				if(udr.getDomainType().equals(DomainType.PROJECT)){
					pao.ProjectCodes.add(udr.getDomainCode());
				}
				privCodes = new ArrayList<String>();

				if (pao.PrivCodesMap.containsKey(udr.getDomainCode())) {
					privCodes = pao.PrivCodesMap.get(udr.getDomainCode());
				}
				logger.info(">>>空间域类型 [{}]，空间域编码 [{}]，角色编码 [{}]", udr.getDomainType(), udr.getDomainCode(),
						udr.getRoleCode());
				List<String> tempCodes = this.privTemplateDmn.getPrivilegeCodes(ResourceConstants.ResourceType.RES_SPACE_PROJECT, 1L,
						udr.getRoleCode());
				logger.info(">>>从权限模板中获取到的权限编码值 [{}]", tempCodes);
				privCodes.addAll(tempCodes);

				pao.PrivCodesMap.put(udr.getDomainCode(), privCodes);

			}

		}
		return pao;
	}
	
	
	
	@Override
	public Map<String, List<String>> getProjectAndOrgPrivCodes(DcmUser user) {
		
/*		Map<String, Object[]> propertyValues = new HashMap<String, Object[]>();
		propertyValues.put("userId", new Object[] { user.getId() });
		propertyValues.put("domainType", new Object[] { DomainType.Institute, DomainType.Department,
				DomainType.Discussion, DomainType.Project });*/
		
		return getProjectAndOrgPrivCodesObj(user).PrivCodesMap;
		
	}
	/**
	 * 获取用户在机构，并且指定项目中的权限map
	 * @param user
	 * @param caseIdentifier
	 * @return
	 */
   /* private Map<String, List<String>> getProjectAndOrgPrivCodes(DcmUser user, String caseIdentifier) {
		
    	Map<String, List<String>> privCodesMap = new HashMap<String, List<String>>();
    	
		Map<String, Object[]> propertyValues = new HashMap<String, Object[]>();
		// 机构
		propertyValues.put("userId", new Object[] { user.getId() });
		propertyValues.put("domainType", new Object[] { DomainType.INSTITUTE, DomainType.DEPARTMENT});
		privCodesMap.putAll(getProjectAndOrgPrivCodesObj(propertyValues).PrivCodesMap);
		
		propertyValues.clear();
		
		// 定位项目
		propertyValues.put("userId", new Object[] { user.getId() });
		propertyValues.put("domainCode", new Object[]{caseIdentifier});
		privCodesMap.putAll(getProjectAndOrgPrivCodesObj(propertyValues).PrivCodesMap);
		
		return privCodesMap;
	}*/
	
	@Override
	public List<PersonalDiscussionGroupResultDTO> listPersonalDiscussionGroups(String loginName) {
		
		DcmUser user = this.userOrgService.getUserEntityByLoginName(loginName);
		// 降序排序
		List<DcmUserdomainrole> list = this.udrDmn.findByProperties(new String[]{"userId","domainType"}, new Object[]{user.getId(),DomainType.DISCUSSION},"createTime",false);
	    List<PersonalDiscussionGroupResultDTO> results = new LinkedList<PersonalDiscussionGroupResultDTO>();
	    List<String> container = new LinkedList<String>();
	    for(DcmUserdomainrole udr : list){
	    	if(container.contains(udr.getDomainCode())){
	    		continue;
	    	}else{
	    		container.add(udr.getDomainCode());
	    	}
	    	PersonalDiscussionGroupResultDTO dto = new PersonalDiscussionGroupResultDTO();
	    	DcmGroup g = this.groupDmn.getGroupByCode(udr.getDomainCode());
	    	dto.setGroupName(g.getGroupName());
	    	dto.setGroupCode(udr.getDomainCode());
	    	dto.setJoinTime(DateUtil.toDateTimeStr(udr.getCreateTime()));
	    	dto.setRoleCode(udr.getRoleCode());
	    	dto.setCreator(g.getCreator());
	    	dto.setCreateTime(DateUtil.toDateTimeStr(g.getCreateTime()));
	    	results.add(dto);
	    }
	    return results;
	}
	
	@Deprecated
	@Override
	public DcmGroup getGroupByCode(String code) {
		
		return this.groupDmn.getGroupByCode(code);
	}
	
	@Override
	public DcmGroup getGroupByGuid(String id) {
		
		DcmGroup group = this.globalConf.openCache()? (DcmGroup) this.redisCache.get(CacheKey.PREFIX_PROJECT + id):null;
		if(group != null){
			logger.info(">>>缓存获取到项目信息，case id[{}]", id);
			return group; 
		}
		
		group = this.groupDmn.getGroupByGuid(id);
		if(group != null && this.globalConf.openCache()){
			cacheGroup(group);
		}

		return group;
	}
	
	@Override
	public List<DcmRole> getRolesInProject(String projectGuid, Long userSeqId) {
		
		List<DcmUserdomainrole> udrs = this.udrDmn.getByUserIdDomainCode(userSeqId, projectGuid);
		if(null == udrs || udrs.isEmpty()) return null;
		
		List<DcmRole> roles = new ArrayList<DcmRole>();
		for(DcmUserdomainrole udr : udrs) {
			roles.add(this.roleDmn.getRoleByCode(udr.getRoleCode()));
		}
		return roles;
	}

}
