
package com.dist.bdf.facade.service;

import java.util.List;

import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.model.dto.system.RoleDTO;
import com.dist.bdf.model.entity.system.DcmRole;

/**
 * @author weifj
 * @version 1.0，2016/03/01，weifj，创建角色服务
 */
public interface RoleService {

	/**
	 * 获取角色列表
	 * @return
	 */
	public List<DcmRole> listAllRoles();
	/**
	 * 获取关于项目的角色
	 * @return
	 */
	public List<DcmRole> getRolesOfProject();
	/**
	 * 获取机构的角色
	 * @return
	 */
	public List<DcmRole> getRolesOfOrg();
	/**
	 * 添加角色
	 * @param dto
	 * @return
	 */
	public DcmRole addRole(RoleDTO dto) throws BusinessException;
	/**
	 * 更新角色
	 * @param dto
	 * @return
	 */
	public DcmRole updateRole(RoleDTO dto);
	/**
	 * 根据标识符数组，批量删除角色
	 * @param idArray
	 * @return
	 */
	public boolean delRoles(Object... idArray);
	/**
	 * 根据标识符数组，批量删除角色
	 * @param idArray
	 * @return
	 */
	public boolean delRoles(List<Long> idArray);
	/**
	 * 根据角色编码获取实体
	 * @param code
	 * @return
	 */
	DcmRole getRoleByCode(String code);
	/**
	 * 根据类型和编码，获取角色实体
	 * @param type
	 * @param code
	 * @return
	 */
	DcmRole getRoleByCode(long type, String code);
	/**
	 * 根据角色id获取实体
	 * @param id
	 * @return
	 */
	DcmRole getRoleById(Long id);
	/**
	 * 根据机构类型，获取角色类别
	 * @param orgType 院（0）和所（1）
	 * @return
	 */
	List<DcmRole> getRolesOfOrg(long orgType);
	/**
	 * 获取域下跟机构相关的角色
	 * @param realm 域
	 * @param orgType 院（0）和所（1）
	 * @return
	 */
	Object getRolesOfOrg(String realm, long orgType);
	boolean addRole(String roleCode, String roleName, Long roleType);
	/**
	 * 获取域下跟项目相关的角色
	 * @param realm
	 * @return
	 */
	List<DcmRole> getRolesOfProject(String realm);
	/**
	 * 获取团队下的角色
	 * @param realm
	 * @return
	 */
	List<DcmRole> getRolesOfTeam(String realm);
	
	
}
