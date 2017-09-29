
package com.dist.bdf.model.dto.system;

import com.dist.bdf.common.constants.DomainType;

/**
 * 组织机构-所dto
 * @author weifj
 *
 */
public class DepartmentDTO extends OrgDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getDomaintype() {
		return DomainType.DEPARTMENT;
	}
	
	@Override
	public String getDomainname() {
		return "所";
	}

	@Override
	public String getGroupType() {

		return "d";
	}


}
