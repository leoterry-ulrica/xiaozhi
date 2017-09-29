
package com.dist.bdf.model.dto.system;

import com.dist.bdf.common.constants.DomainType;

/**
 * 组织机构-院dto层
 * @author weifj
 * @version 1.0，2016/01/15，创建
 * @version 1.1，2016/02/17，移除重复的方法：getOrgCode，超类已存在
 *
 */
public class InstituteDTO extends OrgDTO {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getDomaintype() {
		return DomainType.INSTITUTE;
	}
	
	@Override
	public String getDomainname() {
		return "院";
	}

	@Override
	public String getGroupType() {
		
		return "i";
	}

}
