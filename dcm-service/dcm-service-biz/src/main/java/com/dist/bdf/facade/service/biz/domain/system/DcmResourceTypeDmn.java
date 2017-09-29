
package com.dist.bdf.facade.service.biz.domain.system;

import com.dist.bdf.base.domain.GenericDmn;
import com.dist.bdf.model.entity.system.DcmResourceType;

/**
 * 资源类型领域层
 * @author weifj
 * @version 1.0，2016/01/28，创建资源类型领域层
 */
public interface DcmResourceTypeDmn extends GenericDmn<DcmResourceType, Long> {

	/**
	 * 添加资源类型
	 * @param code
	 * @param name
	 * @return
	 */
	public Object addType(DcmResourceType resType);
	/**
	 * 更新资源类型
	 * @param resType
	 * @return
	 */
	public Object updateType(DcmResourceType resType);
	/**
	 * 根据id删除资源类型
	 * @param id
	 * @return
	 */
	public Object deleteType(String id);
	/**
	 * 根据编码删除资源类型
	 * @param code
	 * @return
	 */
	public Object deleteTypeByCode(String code);
	/**
	 * 根据名称删除资源类型
	 * @param name
	 * @return
	 */
	public Object deleteTypeByName(String name);
	/**
	 * 根据编码获取资源类型
	 * @param code
	 * @return
	 */
	public Object getResTypeByCode(String code);
	/**
	 * 根据名称获取资源类型
	 * @param name
	 * @return
	 */
	public Object getResTypeByName(String name);
}
