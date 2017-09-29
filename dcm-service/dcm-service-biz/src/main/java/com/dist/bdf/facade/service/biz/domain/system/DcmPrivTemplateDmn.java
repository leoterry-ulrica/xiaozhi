
package com.dist.bdf.facade.service.biz.domain.system;

import java.util.List;

import com.dist.bdf.base.domain.GenericDmn;
import com.dist.bdf.model.entity.system.DcmPrivilege;
import com.dist.bdf.model.entity.system.DcmPrivtemplate;
import com.dist.bdf.model.entity.system.DcmRole;

/**
 * @author weifj
 * @version 1.0，2016/01/27，创建权限模板的领域层
 */
public interface DcmPrivTemplateDmn extends GenericDmn<DcmPrivtemplate, Long> {

	/**
	 * 给归档的资源添加权限模板
	 * @param privTemp
	 * @return
	 */
	public Object addPrivTempArchive(DcmPrivtemplate privTemp);
	/**
	 * 给未归档的资源添加权限模板
	 * @param privTemp
	 * @return
	 */
	public Object addPrivTempActive(DcmPrivtemplate privTemp);
	/**
	 * 更新权限模板
	 * @param privTemp
	 * @return
	 */
	public Object updatePrivTemp(DcmPrivtemplate privTemp);
	/**
	 * 删除权限模板
	 * @param id 权限id
	 * @return
	 */
	public Object deletePrivTemp(String id);
	/**
	 * 从权限模板中获取权限列表
	 * @param resTypeCode 资源类型编码
	 * @param resTypeStatus 资源类型状态
	 * @param roleCode 角色编码
	 * @return
	 */
	public List<DcmPrivilege> getPrivilegeList(String resTypeCode, long resTypeStatus, String roleCode);
	/**
	 * 从权限模板中获取权限的mask值
	 * @param resTypeCode 资源类型编码
	 * @param resTypeStatus 资源类型状态
	 * @param roleCode 角色编码
	 * @return
	 */
	public Long getPrivilegeMasks(String resTypeCode, long resTypeStatus, String roleCode);
	
	public Long getPrivilegeMasks(String resTypeCode, long resTypeStatus, List<DcmRole> roles);
	/**
	 * 从权限模板中获取权限的编码
	 * @param resTypeCode
	 * @param resTypeStatus
	 * @param roleCode
	 * @return
	 */
	public List<String> getPrivilegeCodes(String resTypeCode, long resTypeStatus, String roleCode);
	public List<DcmPrivtemplate> getPrivilegeCodes(String realm, String[] resTypeCodes, long resTypeStatus, String roleCode);
	List<DcmPrivtemplate> getPrivileges(String realm, String resTypeCode, long resTypeStatus, String roleCode);
}
