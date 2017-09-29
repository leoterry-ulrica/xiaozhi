
package com.dist.bdf.model.dto.system;

import com.dist.bdf.common.constants.DomainType;

/**
 * 项目组DTO
 * @author weifj
 * @version 1.0，2016/01/27，创建项目组DTO
 */
public class ProjectGroupDTO extends GroupDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String xmfzr;
	private String xmzl;
	
	@Override
	public String getDomainType() {
	
		return DomainType.PROJECT;
	}


	@Override
	public String getDomainName() {
		return "项目组";
	}

	/**
	 * 项目负责人
	 * @return
	 */
	public String getXmfzr() {
		return xmfzr;
	}


	public void setXmfzr(String xmfzr) {
		this.xmfzr = xmfzr;
	}

	public static void main(String[] args) {
		
		String s = "XZ_CASETYPE_JYXM_000000100002";
		
		System.out.println(s.substring(0, s.lastIndexOf("_")));
	}

	/**
	 * 项目助理
	 * @return
	 */
	public String getXmzl() {
		return xmzl;
	}

	public void setXmzl(String xmzl) {
		this.xmzl = xmzl;
	}
}
