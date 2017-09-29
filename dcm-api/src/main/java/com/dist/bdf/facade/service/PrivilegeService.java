
package com.dist.bdf.facade.service;

import java.util.List;
import java.util.Map;

import com.dist.bdf.model.dto.system.UserDomainRoleDTO;
import com.dist.bdf.model.dto.system.UserResPrivRequestDTO;
import com.dist.bdf.model.entity.system.DcmUser;

/**
 * @author weifj
 * @version 1.0，2016/03/01，weifj，创建权限服务
 * @version 1.1，2016/03/05，weifj
 *    1. 添加方法：getPermissionMasksByRoleRes(Long roleId, String ceResType, String resId)；
 *    2. 添加授权方法：authorize(InheritableDepth inheritableDepth ,Long roleId, String resType, String resId, Long masks)
 *  @version 1.2，2016/03/07，weifj
 *     1. 修改方法authorize参数InheritableDepth inheritableDepth为 int inheritableDepth
 */
public interface PrivilegeService {

	/**
	 * 把人添加到项目组
	 * @param udrDto
	 * @return
	 */
	public Map<Long, Long> addUserToProjectGroup(UserDomainRoleDTO udrDto);
	/**
	 * 把人从项目组中去除
	 * @param udrDto
	 */
	public void removeUserFromProjectGroup(UserDomainRoleDTO udrDto);
	
	/**
	 * 获取用户对资源所具有权限的mask值，合并了CE和系统扩展的权限
	 * @param userId
	 * @param domainCode
	 * @param resType
	 * @param resId
	 * @return
	 */
	public Long getDocMasksOfUserRes(long userId, String domainCode, String resType, String resId);

	public Long getMasksOfUserRes(DcmUser user, String domainCode, String resType, String resId);
	/**
	 * 获取文档的权限值
	 * @param userId 一般是登录名
	 * @param resId
	 * @return
	 */
	//public Long getDocMasksOfUserRes(String userId, String resId);
	/**
	 * 获取文档的权限值
	 * @param userId 用户登录名
	 * @param resType 
	 *    Res_Pck_Institute：院包
	 *    Res_Pck_Department：所包
	 *    Res_Pck_Project：项目包
	 *    Res_Pck_Person：个人包
	 *    Res_Pck_Group：组包
	 *    
	 * @param resId 资源id
	 * @return
	 */
	@Deprecated
	public Long getDocMasksOfUserRes(String userId, String resType, String resId);
	/**
	 * 针对CE，获取角色对资源的直接权限mask值，不包括继承
	 * @param roleId 角色id
	 * @param resType CE的资源类型
	 * @param resId 资源id
	 * @return
	 */
	public Long getDirectPermissionMasksByRoleRes(Long roleId, String resType, String resId);
	/**
	 * 针对CE，获取角色对资源的权限mask值，过滤了操作类型为拒绝（AccessType.Deny）的权限
	 * @param roleId 角色id
	 * @param resType ce资源类型
	 * @param resId ce资源的标识符
	 * @return
	 */
	//public Long getPermissionMasksByRoleRes(Long roleId, String resType, String resId);
	//public Long getPermissionMasksByRoleRes(DcmRole role, String resType, String resId);
	//public Long getPermissionMasksByUserRes(String userId, String resType, String resId);
	/**
	 * 授权
	 * @param inheritableDepth 继承深度
	 * @param roleId 角色id
	 * @param resType 资源类型，包括了ce的文件夹或者文档类型
	 * @param resId 资源id
	 * @param masks 权限mask值
	 */
	public void authorize(int inheritableDepth ,Long roleId, String resType, String resId, Long masks);
	/**
	 * 根据权限模板给资源授权
	 * @param inheritableDepth 继承深度
	 * @param sysRoleCode 系统的角色代码
	 * @param ldapGroupName ldap中组的名称（cn值）
	 * @param resType 资源类型，包括了ce的文件夹或者文档类型
	 * @param resStatus 资源状态，“在办”或者“归档”
	 * @param resId 资源id
	 */
	public void authorizeFromTemplate(int inheritableDepth ,String sysRoleCode, String ldapGroupName, String resType,  long resStatus,  String resId);
	/**
	 * 获取用户对资源的权限编码
	 * @param user 用户id
	 * @param resId 资源id
	 * @return
	 */
	//Object getDocPrivCodesOfUserRes(String user, String resId);
	/**
	 * 获取文档权限编码
	 * @param userId 登录名
	 * @param versionSeriesId 版本系列id
	 * @return
	 */
	/*@Deprecated
	List<String> getDocPrivCodes(String userId, String versionSeriesId);*/
	/**
	 * 获取文档权限编码
	 * @param realm 域
	 * @param userId
	 * @param pwd
	 * @param versionSeriesId  版本系列id
	 * @return
	 */
	List<String> getDocPrivCodes(String realm, String userId, String pwd, String versionSeriesId);
	/**
	 * 获取文档权限编码
	 * @param realm
	 * @param userId
	 * @param versionSeriesId
	 * @return
	 */
	public Object getDocPrivCodes(String realm, String userId, String versionSeriesId);
	/**
	 * 获取文档权限编码
	 * @param realm
	 * @param userId
	 * @param pwd 以加密的方式传递
	 * @param versionSeriesId
	 * @return
	 */
	List<String> getDocPrivCodesSecurity(String realm, String userId, String pwd, String versionSeriesId);
	/**
	 * 获取文档权限mask值
	 * @param userId userId 登录名
	 * @param versionSeriesId 版本系列id
	 * @return
	 */
	//Long getDocPrivMasks(String userId, String versionSeriesId);
	/**
	 * 获取文档权限mask值
	 * @param realm 域
	 * @param userId userId 登录名
	 * @param versionSeriesId 版本系列id
	 * @return
	 */
	Long getDocPrivMasks(String realm, String userId, String pwd, String versionSeriesId);
	/**
	 * 根据文档系列id获取权限值
	 * @param realm
	 * @param userId
	 * @param versionSeriesId 文档的系列id
	 * @return
	 */
	@Deprecated
	Long getDocPrivMasksByVID(String realm, String userId, String versionSeriesId);
	@Deprecated
	Long getDocPrivMasksByGUID(String realm, String userId, String id);
	/**
	 * 
	 * @param userId
	 * @param versionSeriesId
	 * @param resType
	 * @param domainCode
	 * @return
	 */
	//Long getDocPrivMasks(String userId,  String versionSeriesId, String resType, String domainCode);
	/**
	 * 获取角色对某类资源的权限编码
	 * @param roleCode
	 * @param resourceTypeCode
	 * @return
	 */
	Object getRolePrivsCodes(String roleCode, String resourceTypeCode);
	/**
	 * 获取用户对文档的权限编码
	 * @param dto
	 * @return
	 */
	
	Object getDocPrivCodes(UserResPrivRequestDTO dto);
	Object getDocPrivCodesEx(UserResPrivRequestDTO dto);
	/**
	 * 新版本获取权限编码
	 * @param dto
	 * @return
	 */
	Object getDocPrivCodesV1(UserResPrivRequestDTO dto);

	/**
	 * 
	 * @param realm
	 * @param userId 用户dn
	 * @param versionSeriesId
	 * @param parentId
	 * @return
	 */
	@Deprecated
	Long getDocPrivMasksByVID(String realm, String userId, String versionSeriesId, String parentId);
	/**
	 * 修改用户角色
	 * @param userSeqId 用户序列id
	 * @param domainCode 空间域编码
	 * @param roleCode 角色编码
	 */
	void modifyUserRole(Long userSeqId, String domainCode, String roleCode);
	/**
	 * 修改用户在项目中是否大牛
	 * @param userSeqId
	 * @param domainCode
	 * @param isTop
	 */
	void modifyUserIstop(Long userSeqId, String domainCode, int isTop);
	/**
	 * 获取人员对脑图的角色编码
	 * @param caseId
	 * @param userId
	 * @return
	 */
	Object getPrivCodesOfBrainMap(String caseId, String userId);
	/**
	 * 获取用户指定空间域下，对指定资源类型的权限编码
	 * @param resTypeCode 资源类型编码
	 * @param domainTypeCodes 空间域类型编码
	 * @param userId
	 * @return
	 */
	public Object getPrivCodesOfRestypeDomain(String resTypeCode, String[] domainTypeCodes, String userId);
	/**
	 * 获取用户指定空间域下，对指定资源类型的权限编码
	 * @param realm
	 * @param resTypeCode
	 * @param domainTypeCodes
	 * @param userId
	 * @return
	 */
	public Object getPrivCodesOfRestypeDomain(String realm, String[] resTypeCodes, String[] domainTypeCodes, String userId);
	/**
	 * 获取用户指定空间域和域值下，对指定资源类型的权限编码
	 * @param realm
	 * @param resTypeCodes
	 * @param domainTypeCodes
	 * @param userId
	 * @param domainCode
	 * @return
	 */
	public Object getPrivCodesOfRestypeDomain(String realm, String[] resTypeCodes, String[] domainTypeCodes, String userId, String domainCode);
	/**
	 * 修改人在空间域中的角色
	 * @param userCode 用户编码
	 * @param domainCode
	 * @param roleCode
	 */
	public void modifyUserRoleOfDomain(String userCode, String domainCode, String roleCode);
	/**
	 * 添加人员到项目组
	 * @param dto
	 * @return
	 */
	public Object addUserToProjectGroupEx(UserDomainRoleDTO udrDto);
	/**
	 * 
	 * @param dto
	 */
	void removeUserFromProjectGroupEx(UserDomainRoleDTO dto);
	
	
}
