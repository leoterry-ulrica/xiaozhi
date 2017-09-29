
package com.dist.bdf.model.dto.system;

import org.hibernate.validator.constraints.NotEmpty;

import com.dist.bdf.base.dto.BaseDTO;

/**
 * 组DTO，派生出项目组和讨论组
 * @author wefj
 * @version 1.0，2016/01/27，创建组DTO
 */
public abstract class GroupDTO extends BaseDTO {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 组名称
	 */
	protected String groupName;
	/**
	 * 创建者
	 */
	@NotEmpty( message = "creator can not be empty")
	protected String creator;
	/**
	 * 组编码
	 */
	@NotEmpty( message = "groupCode can not be empty")
	protected String groupCode;
	/**
	 * 创建时间
	 */
	protected String createTime;
	/**
	 * 修改时间
	 */
	protected String modifiedTime;
	/**
	 * 机构编码
	 */
	protected String orgCode;
	/**
	 * 机构类型
	 */
	protected String orgType;
	/**
	 * 域
	 */
	protected String realm;

	/**
	 * @return 组名称
	 */
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	/**
	 * 获取组编码
	 * @return
	 */
	public String getGroupCode(){
		
		return this.groupCode;
		//return CodeFactory.newCode(new GroupCodeGenerator());
	}

	/**
	 * @return 空间域类型
	 */
	public abstract String getDomainType();
	/**
	 * 获取空间域名称
	 * @return
	 */
	public abstract String getDomainName();

	/**
	 * @return the creatorId
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @param groupCode the groupCode to set
	 */
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the modifiedTime
	 */
	public String getModifiedTime() {
		return modifiedTime;
	}

	/**
	 * @param modifiedTime the modifiedTime to set
	 */
	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}
	
}
