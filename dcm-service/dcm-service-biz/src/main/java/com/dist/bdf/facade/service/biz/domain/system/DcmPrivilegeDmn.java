
package com.dist.bdf.facade.service.biz.domain.system;

import java.util.List;

import com.dist.bdf.base.domain.GenericDmn;
import com.dist.bdf.model.entity.system.DcmPrivilege;


/**
 * 
 * @author weifj
 * @version 1.0，2016/01/28，创建权限领域层
 * @version 1.1，2016/03/05，添加接口方法：
 *     getBasicPrivs：获取ce基础权限mask值
 *     getExtendedPrivs：获取系统扩展权限mask值
 *     getExtendedPrivCodes：获取扩展权限的编码集
 */
public interface DcmPrivilegeDmn extends GenericDmn<DcmPrivilege, Long> {

	/**
	 * 获取权限列表
	 * @return
	 */
	public List<DcmPrivilege> getAllPrivs();
	/**
	 * 根据编码获取权限
	 * @param code 权限编码
	 * @return
	 */
	public DcmPrivilege getPrivByCode(String code);
	/**
	 * 根据名字获取权限
	 * @param name 权限名称
	 * @return
	 */
	public DcmPrivilege getPrivByName(String name);
	/**
	 * 根据值获取权限
	 * @param value 权限值
	 * @return
	 */
	public DcmPrivilege getPrivByValue(String value);
	
	/**
	 * 抽取出ce本身权限
	 * @param masks 目标权限值
	 * @return
	 */
	public Long getBasicPrivs(Long masks);
	/**
	 * 抽取扩展权限
	 * @param masks
	 * @return
	 */
	public Long getExtendedPrivs(Long masks);
	
	/**
	 * 获取扩展权限编码
	 * @param validMasks 有效的权限mask值
	 * @return
	 */
	public List<String> getExtendedPrivCodes(Long validMasks);
}
