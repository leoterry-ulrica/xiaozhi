
package com.dist.bdf.model.dto.system.page;

/**
 * 
 * @author weifj
 * @version 1.0，2015/06/08，weifj，参数json的[用户组与机构]对象
 *  
 */
public class ParaJsonUsersOfOrg {

	/**
	 * 机构id
	 */
	private Long orgid;
	/**
	 * 用户id数组
	 */
	private Long[] userids;
	/**
	 * @return the orgid
	 */
	public Long getOrgid() {
		return orgid;
	}
	/**
	 * @param orgid the orgid to set
	 */
	public void setOrgid(Long orgid) {
		this.orgid = orgid;
	}
	/**
	 * @return the userids
	 */
	public Long[] getUserids() {
		return userids;
	}
	/**
	 * @param userids the userids to set
	 */
	public void setUserids(Long[] userids) {
		this.userids = userids;
	}
}
