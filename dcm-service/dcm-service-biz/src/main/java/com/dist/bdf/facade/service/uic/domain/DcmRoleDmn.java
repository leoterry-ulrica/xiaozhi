
package com.dist.bdf.facade.service.uic.domain;

import java.util.List;

import com.dist.bdf.base.domain.GenericDmn;
import com.dist.bdf.model.entity.system.DcmRole;

/**
 * 角色领域
 * @author weifj
 * @version 1.0，2016/01/25，weifj，创建
 * @version 1.1，2016/02/24，weifj，修改方法getRole(String id)，为getRoleById(String id)；
 * @version 1.2，2016/02/24，weifj，修改方法updateRole为updateRoleById
 */
public interface DcmRoleDmn  extends GenericDmn<DcmRole, Long> {

	/**
	 * 获取所有角色
	 * @return
	 */
	public List<DcmRole> getAllRole();
	/**
	 * 获取跟项目相关的角色类型
	 * @return
	 */
	public List<DcmRole> getRolesOfProject();
	/**
	 * 根据id获取角色
	 * @param id
	 * @return
	 */
	public DcmRole getRoleById(Long id);
	/**
	 * 根据编码获取角色
	 * @param code
	 * @return
	 */
	public DcmRole getRoleByCode(String code);
	/**
	 * 根据名称获取角色
	 * @return
	 */
	public DcmRole getRoleByName(String name);

	/**
	 * 添加角色
	 * @param code
	 * @param name
	 * @return
	 */
	public DcmRole addRole(String code, String name);
	/**
	 * 根据id更新角色
	 * @param id
	 * @param code
	 * @param name
	 * @return
	 */
	public DcmRole updateRoleById(Long id, String code, String name);
	/**
	 * 根据id删除角色
	 * @param id
	 * @return
	 */
	public void deleteRoleById(Long id);
	/**
	 * 根据编码删除角色
	 * @param code
	 * @return
	 */
	public void deleteRoleByCode(String code);
	/**
	 * 根据名称删除角色
	 * @param name
	 * @return
	 */
	public void deleteRoleByName(String name);
}
