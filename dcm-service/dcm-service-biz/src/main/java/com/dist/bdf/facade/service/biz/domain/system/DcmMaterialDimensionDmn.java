
package com.dist.bdf.facade.service.biz.domain.system;

import com.dist.bdf.base.domain.GenericDmn;
import com.dist.bdf.model.entity.system.DcmMaterialDimension;

/**
 * 资料维度模型领域层
 * @author weifj
 * @version 1.0，2016/04/19，创建领域层
 */
public interface DcmMaterialDimensionDmn extends GenericDmn<DcmMaterialDimension, Long> {

	/**
	 * 根据父id获取子级别
	 * @param parentId 父id
	 * @return
	 */
	public Object getSubLevelByParentId(Long id);
	/**
	 * 根据父名字获取子级别
	 * @param name 父名字
	 * @return
	 */
	public Object getSubLevelByParentName(String name);
}
