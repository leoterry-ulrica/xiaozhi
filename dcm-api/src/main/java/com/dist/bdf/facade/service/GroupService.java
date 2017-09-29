
package com.dist.bdf.facade.service;

import java.util.List;
import java.util.Map;

import com.dist.bdf.model.dto.system.GroupDTO;
import com.dist.bdf.model.dto.system.GroupUserDTO;
import com.dist.bdf.model.dto.system.GroupUserExDTO;
import com.dist.bdf.model.dto.system.ProjectUserResponseDTO;
import com.dist.bdf.model.dto.system.page.PageProjectPara;
import com.dist.bdf.model.entity.system.DcmGroup;
import com.dist.bdf.model.entity.system.DcmRole;
import com.dist.bdf.model.entity.system.DcmUser;
import com.dist.bdf.model.entity.system.DcmUserdomainrole;

/**
 * @author weifj
 * @version 1.0，2016/03/01，weifj，创建讨论组和项目组服务
 */
public interface GroupService {

	/**
	 * 添加组（项目组/讨论组）
	 * @param groupDto
	 * @return
	 */
	@Deprecated
	public DcmGroup addGroup(GroupDTO groupDto);
	
	public DcmGroup addGroupGZ(GroupDTO groupDto);
	/**
	 * 添加组
	 * @param dto
	 * @return
	 */
	DcmGroup addGroupEx(GroupDTO groupDto);
	/**
	 * 根据guid删除组
	 * @param guid
	 */
	void deleteGroupByGuid(String guid);
	void deleteGroupByCode(String caseIdentifier);
	/**
	 * 获取所有的项目组
	 * @return
	 */
	public List<DcmGroup> listProjectGroups();
	/**
	 * 分页查询项目信息，并根据用户进行过滤
	 * @param page
	 * @return
	 */
	@Deprecated
	public Object listProjectsByPage(PageProjectPara page, String flag);
	/**
	 *  分页查询项目信息，并根据用户进行过滤
	 * @param page
	 * @param isPersonal 是否只查询自己的项目，true：只查询自己；false：查询所有的
	 * @return
	 */
	public Object searchProjectsByPage(PageProjectPara page, boolean isPersonal);
	
	public Object searchProjectsByPageGZ(PageProjectPara page, boolean isPersonal);
	
	Map<String, List<String>> getProjectAndOrgPrivCodes(DcmUser user);
	
	/**
	 * 获取所有的讨论组
	 * @return
	 */
	public List<DcmGroup> listDiscussionGroups();
	/**
	 * 获取个人的讨论组
	 * @param loginName
	 * @return
	 */
	public Object listPersonalDiscussionGroups(String loginName);
	/**
	 * 获取组内部的人
	 * @param contextPath 上下文物理路径
	 * @param baseURL 基础URL
	 * @param groupCode
	 * @return
	 */
	public List<GroupUserDTO> listUsersOfGroupByCode(String contextPath, String baseURL, String groupCode);
	/**
	 * 获取项目组成员
	 * @param contextPath
	 * @param baseURL
	 * @param groupCode 项目组编号
	 * @return
	 */
	public Map<String, ProjectUserResponseDTO> listUsersOfProjectGroupByCode(String contextPath, String baseURL, String[] groupCodes);
	/**
	 * 更新项目组的信息，目前只支持更新组名称
	 * @param groupDto
	 * @return ProjectGroupDto
	 */
	public GroupDTO updateProjectGroup(GroupDTO groupDto);
	/**
	 * 更新讨论组信息，目前只支持更新组名称
	 * @param groupDto
	 * @return
	 */
	public GroupDTO updateDiscussionGroup(GroupDTO groupDto);
	/**
	 * 根据编码获取组
	 * @param code
	 * @return
	 */
	@Deprecated
	DcmGroup getGroupByCode(String code);
	/**
	 * 根据组的guid查询，优先从缓存中获取
	 * @param id
	 * @return
	 */
	DcmGroup getGroupByGuid(String id);
	/**
	 * 获取用户在项目中的角色
	 * @param projectGuid
	 * @param userSeqId
	 * @return
	 */
	List<DcmRole> getRolesInProject(String projectGuid, Long userSeqId);
	/**
	 * 提供广州
	 * @param avatarLocalpath
	 * @param baseURL
	 * @param groupCode
	 * @return
	 */
	public List<GroupUserDTO> listUsersOfGroupByCodeGZ(String avatarLocalpath, String baseURL, String groupCode);

	Map<String, ProjectUserResponseDTO> listUsersOfProjectGroupByCodeGZ(String contextPath, String baseURL,
			Object[] groupCodes);

	List<GroupUserExDTO> listUsersOfGroupByCodeEx(String contextPath, String baseURL, String groupCode);
	/**
	 * 分页检索一般项目
	 * @param page
	 * @param projectType 项目类型，即案例类型，如：XZ_CASETYPE_JYXM
	 * @param isPersonal
	 * @return
	 */
	Object searchProjectsByPage(PageProjectPara page, String[] projectType,  boolean isPersonal);
	/**
	 * 分页获取其他人的项目信息
	 * @param page
	 * @param projectType
	 * @return
	 */
	Object searchOthersProjectsByPage(PageProjectPara page, String[] projectType);
	/**
	 * 添加项目组（新服务）
	 * @param groupDto
	 * @return
	 */
	DcmGroup addProjectGroupNew(GroupDTO groupDto);
	/**
	 * 添加组
	 * @param groupDto
	 * @param leaderRoleCode 负责人角色编码
	 * @param assistantRoleCode 助理角色编码
	 * @return
	 */
	DcmGroup addGroup(GroupDTO groupDto, String leaderRoleCode, String assistantRoleCode);
	/**
	 * 添加团队
	 * @param groupDto
	 * @return
	 */
	DcmGroup addTeamGroup(GroupDTO groupDto);
	/**
	 * 根据域，编码，获取项目组成员和角色
	 * @param contextPath
	 * @param baseURL
	 * @param groupCode
	 * @return
	 */
	List<GroupUserExDTO> listUsersOfProjectGroupByCodeRealm(String contextPath, String baseURL, String groupCode);
	/**
	 * 根据域，编码，获取团队组成员和角色
	 * @param contextPath
	 * @param baseURL
	 * @param groupCode
	 * @return
	 */
	List<GroupUserExDTO> listUsersOfTeamGroupByCodeRealm(String contextPath, String baseURL, String groupCode);

	List<GroupUserExDTO> listUsersOfGroupByCodeRealm(String contextPath, String baseURL, String groupCode);
}
