
package com.dist.bdf.facade.service.uic.domain;

import java.util.Collection;
import java.util.List;

import com.dist.bdf.base.domain.GenericDmn;
import com.dist.bdf.model.dto.system.Org2UsersDTO;
import com.dist.bdf.model.dto.system.OrgDTO;
import com.dist.bdf.model.dto.system.user.UserOrgRolesUpdateDTO;
import com.dist.bdf.model.entity.system.DcmDicOrgExt;
import com.dist.bdf.model.entity.system.DcmOrgUser;
import com.dist.bdf.model.entity.system.DcmOrganization;
import com.dist.bdf.model.entity.system.DcmRole;
import com.dist.bdf.model.entity.system.DcmUser;

/**
 * 组织机构领域
 * @author weifj
 * @version 1.0，2016/01/15，weifj，创建
 * @version 1.1，2016/02/17，weifj，添加方法备注
 * @version 1.2，2016/03/08，weifj
 *    1. 修改方法参数：public void deleteUser(Long orgId, Object[] userIdArray)
 *    2. 添加方法：List<DcmUser> listDirectUsersOfOrg(Long orgId)，获取机构下直接人员
 * @version 1.3，2016/03/09，weifj
 *    1. 添加方法：List<DcmUser> listUsersOfOrg(Long orgId)，获取机构下所有人员信息
 *    2. 添加方法：List<DcmOrganization> listOrgsOfOrg(Long orgId)，获取机构下的所有子机构
 */
public interface DcmOrganizationDmn extends GenericDmn<DcmOrganization, Long> {

	/**
	 * 获取机构和用户关联信息
	 * @param orgId
	 * @return
	 */
	List<DcmOrgUser> getOrgUserRef(Long orgId);
	/**
	 * 获取简单的机构列表，过滤掉domainType=root情况
	 * @return
	 */
	List<DcmOrganization> getAllSimpleOrg();
	/**
	 * 添加机构
	 * @param parentId
	 * @param domainType
	 * @param orderId
	 * @param orgName
	 * @param orgCode
	 * @return
	 */
	//DcmOrganization addOrg(Long parentId, String domainType, Long orderId, String orgName, String orgCode);
	/**
	 * 编辑机构，只允许修改名称 
	 * @param orgDto
	 * @return 返回编辑后的机构实体
	 */
	Object editOrg(OrgDTO orgDto);
	/**
	 * 根据机构id删除，同时需要在DCM_UserDomainRole移除相应权限，以及删除机构和用户的关联信息
	 * @param id
	 * @return
	 */
	Object deleteOrg(DcmOrganization org);
	/**
	 * 根据机构编码删除，同时需要在DCM_UserDomainRole移除相应权限
	 * @param code
	 * @return
	 */
	Object deleteOrgByCode(String code);
	/**
	 * 根据名称删除机构，同时需要在DCM_UserDomainRole移除相应权限
	 * @param name
	 * @return
	 */
	void deleteOrgByName(String name);
	/**
	 * 检测是否存在相同名称的机构
	 * @param name
	 * @return
	 */
	boolean isExistOrgName(String name);	
	/**
	 * 检测是否存在相同编码的机构
	 * @param code
	 * @return
	 */
	boolean isExistOrgCode(String code);
	/**
	 * 添加一组用户到指定机构
	 * @param orgId
	 * @param userIdArray
	 * @return
	 */
	void addUsers(Long orgId, Object[] userIdArray);
	/**
	 * 从指定机构删除一组用户，同时需要在DCM_UserDomainRole移除相应权限
	 * @param orgId 机构id
	 * @param userIdArray 用户id数组
	 * @return
	 */
	void deleteUser(Long orgId, Object[] userIdArray);
	/**
	 * 删除机构关联的用户记录
	 * @param userIds
	 */
	void deleteUser(Long[] userIds);
	/**
	 * 获取指定机构下的一级用户列表
	 * @param orgId
	 * @return
	 */
	List<DcmUser> getUsers(Long orgId);
	/**
	 * 根据部门编码获取用户信息
	 * @param orgCode
	 * @return
	 */
	List<DcmUser> getUsersByOrgCode(String orgCode);
	/**
	 * 获取机构下的直接子机构
	 * @param orgId 机构id
	 * @return
	 */
	List<DcmOrganization> listDirectChildOrgs(Long orgId);
	/**
	 * 获取机构下的直接人员
	 * @param orgId
	 * @return
	 */
	List<DcmUser> listDirectUsersOfOrg(Long orgId);
	/**
	 * 获取机构下所有的用户
	 * @param orgId 机构id
	 * @return
	 */
	List<DcmUser> listUsersOfOrg(Long orgId);
	/**
	 * 获取机构下的所有子机构
	 * @param orgId
	 * @return
	 */
	List<DcmOrganization> listOrgsOfOrg(Long orgId);
	/**
	 * 通过院机构唯一名称获取所有部门信息
	 * @param orgUniqueName
	 * @return
	 */
	List<DcmOrganization> listSubDepartmentsByInstituteUniqueName(String name);
	/**
	 * 通过院机构获取所有部门信息
	 * @param institute
	 * @return
	 */
	List<DcmOrganization> listSubDepartmentsByInstituteUniqueName(DcmOrganization institute);
	/**
	 * 根据用户id，获取所属的机构信息
	 * 如果关联表不存在，则返回null；
	 * @param userId
	 * @return
	 */
	List<DcmOrganization> getOrgsByUserId(Long userId);
	/**
	 * 获取所有机构和用户信息
	 * @param loadUser 是否加载用户
	 * @return
	 */
	List<Org2UsersDTO> getAllOrgAndUser(boolean loadUser);
	/**
	 * 根据院的唯一标识获取机构用户信息
	 * @param loadUser
	 * @param instituteUniqueName
	 * @return
	 */
	List<Org2UsersDTO> getAllOrgAndUser(boolean loadUser, String instituteUniqueName);
	/**
	 * 获取指定机构和人员（如果指定加载人员）
	 * @param orgs
	 * @param loadUser
	 * @return
	 */
	List<Org2UsersDTO> getAllOrgAndUser(List<DcmOrganization> orgs ,boolean loadUser);
	/**
	 * 清除机构和用户的关联关系
	 */
	void clearAllOrg2User();
	/**
	 * 批量添加机构和用户的关联关系
	 * @param coll
	 */
	void addOrg2User(Collection<DcmOrgUser> coll);
	
	void addOrg2User(DcmOrgUser orguser);
	/**
	 * 获取所有部门信息
	 * @return
	 */
	List<DcmOrganization> getAllDepartment();
	/**
	 * 获取所有院
	 * @return
	 */
	List<DcmOrganization> getAllInstitute();
	/**
	 * 根据机构编码获取机构信息
	 * @param code
	 * @return
	 */
	DcmOrganization getOrgByCode(String code);
	/**
	 * 根据机构的唯一名称获取院机构信息
	 * @param name
	 * @return
	 */
	DcmOrganization getInstituteByUniqueName(String name);
	/**
	 * 是否存在机构和用户的关联关系
	 * @param id
	 * @param id2
	 * @return
	 */
	boolean existOrg2User(Long orgId, Long userId);
	/**
	 * 调整用户的部门，同时更新对应新部门的角色
	 * @param userId 用户id
	 * @param preOrgId 原来机构
	 * @param newOrgId 新机构
	 * @param preRoleId 原来角色
	 * @param newRoleId 新角色
	 */
	void updateOrgAndRoleUser(Long userId, DcmOrganization preOrg, DcmOrganization newOrg, DcmRole preRole, DcmRole newRole);
	/**
	 * 添加字典信息，包括机构下的岗位、团队
	 * @param orgId
	 * @param name
	 * @return
	 */
	DcmDicOrgExt addDicInfo(Long orgId, Integer type, String name);
	
	boolean deleteDic(Long id);
	/**
	 * 获取字典信息
	 * @param orgId
	 * @param position
	 */
	List<DcmDicOrgExt> getDics(Long orgId, Integer type);
	/**
	 * 获取机构字典信息
	 * @param id
	 * @return
	 */
	DcmDicOrgExt getOrgDicById(Long id);
	void updateOrgAndRoleUser(Long id, UserOrgRolesUpdateDTO institute, UserOrgRolesUpdateDTO preDepartment, UserOrgRolesUpdateDTO newDepartment, DcmOrganization preOrg, DcmOrganization newOrg);
	/**
	 * 
	 * @param userType 用户类型，0：内部；1：外部
	 * @param user
	 * @param institute
	 * @param preDepartment
	 * @param newDepartment
	 * @param preOrg
	 * @param newOrg
	 */
	void updateOrgAndRoleUser(Integer userType, DcmUser user, UserOrgRolesUpdateDTO institute, UserOrgRolesUpdateDTO preDepartment,
			UserOrgRolesUpdateDTO newDepartment, DcmOrganization preOrg, DcmOrganization newOrg);
	/**
	 * 
	 * @param orgCode
	 * @param position
	 * @param name
	 * @return
	 */
	Object addDicInfo(String orgCode, Integer type, String name);
	/**
	 * 根据机构编码删除
	 * @param type 类型 0：岗位；1：团队
	 * @param orgCode
	 * @return
	 */
	boolean deleteDic(Integer type, String orgCode);
	/**
	 * 根据机构编码和类型获取字典
	 * @param orgCode
	 * @param type 0：岗位；1：团队
	 * @return
	 */
	List<DcmDicOrgExt> getDics(String orgCode, Integer type);
	/**
	 * 根据域删除机构信息，并级联删除机构和用户的关联信息
	 * @param realm
	 */
	void removeAll(String realm);

}
