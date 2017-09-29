
package com.dist.bdf.facade.service.uic.domain;

import java.util.List;

import com.dist.bdf.base.domain.GenericDmn;
import com.dist.bdf.model.entity.system.DcmRole;
import com.dist.bdf.model.entity.system.DcmUser;
import com.dist.bdf.model.entity.system.DcmUserdomainrole;

/**
 * 用户-空间域-角色
 * @author weifj
 * @version 1.0，2016/01/25，weifj，创建
 * @version 1.1，2016/02/24，weifj
 *      修改接口getRoleCode，addUserToGroup和removeUserFromGroup；
 *      删除接口：addUserToDepertment、removeUserFromDepertment、addUserToInstitute和removeUserFromInstitute
 * @version 1.2,2016/02/25，weifj
 *     修改方法addUserToDomain的参数：String roleCode，改为：String[] roleCodes，并把返回类型改为void
 *
 */
public interface DcmUserdomainroleDmn  extends GenericDmn<DcmUserdomainrole, Long> {

	/**
	 * 根据用户id，域的编码，获取对应的角色
	 * @param userId，用户id
	 * @param domainCode，域值
	 * @return
	 */
	@Deprecated
	List<DcmUserdomainrole> getByUserIdDomainCode(Long userId, String domainCode);
	/**
	 * 根据用户id，域的类型，获取对应的关联关系
	 * @param userId 用户id
	 * @param domainType 域类型
	 * @param roleCode 角色编码
	 * @return
	 */
	@Deprecated
	List<DcmUserdomainrole> getByUserIdDomainType(Long userId, String domainType, String roleCode);
	@Deprecated
	List<DcmUserdomainrole> getByUserIdDomainCodeRoleCode(Long userId, String domainCode, String[] roleCode);
	/**
	 * 添加人域，并赋予角色
	 * @param userId
	 * @param domainType
	 * @param domainCode
	 * @param roleCod 角色代码
	 * @return
	 */
	@Deprecated
	public DcmUserdomainrole addUserToDomain(Long userId, String domainType, String domainCode, String roleCode);
	/**
	 * 从域中移除指定用户
	 * @param userId
	 * @param groupCode
	 */
	@Deprecated
	public void removeUserFromDomain(Long userId, String groupCode);
    /**
     * 根据空间域编码获取列表
     * @param domainCode
     * @return
     */
	public List<DcmUserdomainrole> getByDomainCode(String domainCode);
	/**
	 * 根据空间域和角色编码获取对象
	 * @param domainCode
	 * @param roleCode
	 * @return
	 */
	public List<DcmUserdomainrole> getByDomainCodeRoleCode(String domainCode, String roleCode);
	/**
	 * 根据空间域编码获取空间域类型
	 * @param code
	 * @return
	 */
	String getDomainTypeByDomainCode(String code);
	/**
	 * 获取用户在机构中的角色
	 * @param userId
	 * @param orgCode
	 * @return
	 */
	@Deprecated
	List<DcmRole> getRolesOfUser(Long userId, String orgCode);
	/**
	 * 根据空间域编码删除数据
	 * @param domainCode
	 */
    void deleteByDomainCode(String domainCode);
    /**
     * 根据用户的id号，获取所属的项目组编号（案例标识），并按照创建时间降序
     * @param id
     * @return
     */
    List<DcmUserdomainrole> getProjectGroupsByUserId(Long id);
    /**
     * 根据用户id和空间域类型，获取用户-空间域-角色关联信息
     * @param id
     * @param domainTypes
     * @return
     */
    @Deprecated
    List<DcmUserdomainrole> getByUserIdDomainType(Long userId, String[] domainTypes);
    /**
     * 
     * @param userCode
     * @param domainTypes
     * @return
     */
    List<DcmUserdomainrole> getByUserCodeDomainType(String userCode, String[] domainTypes);
    /**
     * 
     * @param id
     * @param userCode
     * @param userType
     * @param domainType
     * @param orgCode
     * @param roleCode
     */
	void addUserToDomain(Long userId, String userCode, int userType, String domainType, String orgCode, String roleCode);
	List<DcmUserdomainrole> getByUserIdDomainType(Long userId, String domainType);
	/**
	 * 添加人到空间域-角色表
	 * @param user
	 * @param domainType
	 * @param domainCode
	 * @param roleCode
	 * @return
	 */
	DcmUserdomainrole addUserToDomain(DcmUser user, String domainType, String domainCode, String roleCode);
	/**
	 * 把人从空间域中移除
	 * @param userCode
	 * @param domainCode
	 */
	void removeUserFromDomain(String userCode, String domainCode);
	/**
	 * 根据用户编码和角色编码获取关联
	 * @param userCode
	 * @param roleCode
	 * @return
	 */
	List<DcmUserdomainrole> getByUsercodeRolecode(String userCode, String roleCode);
	/**
	 * 
	 * @param userCode 用户编码
	 * @param domainCode 空间域编码
	 * @return
	 */
	List<DcmRole> getRolesOfUser(String userCode, String domainCode);
	@Deprecated
	List<DcmUserdomainrole> getByUserIdRolecode(Long userId, String roleCode);
	/**
	 * 根据项目id和用户编码获取关联信息
	 * @param caseId
	 * @param userCode
	 * @return
	 */
	List<DcmUserdomainrole> getByDomainCodeUserCode(String caseId, String userCode);
	/**
	 * 
	 * @param domainCode
	 * @param userCode
	 * @param roleCode
	 * @return
	 */
	DcmUserdomainrole getByDomainCodeUserCodeRoleCode(String domainCode, String userCode, String roleCode);
}
