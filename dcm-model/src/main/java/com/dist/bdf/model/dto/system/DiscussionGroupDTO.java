
package com.dist.bdf.model.dto.system;

import com.dist.bdf.common.constants.DomainType;

/**
 * 讨论组DTO
 * @author weifj
 * @version 1.0，2016/01/27，创建讨论组DTO
 */
public class DiscussionGroupDTO extends GroupDTO {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public String getDomainType() {

		return DomainType.DISCUSSION;
	}


	@Override
	public String getDomainName() {

		return "讨论组";
	}

}
