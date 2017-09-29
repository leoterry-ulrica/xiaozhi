package com.dist.bdf.model.dto.ldap;

import java.io.Serializable;

public class LdapTree implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dn;
	private String cn;
	private String guid;
	private String groupType;
	private String groupCode;
	/**
	 * 附属数据
	 */
	private Object tag;
	
	private String aliasName;

	private String[] members;
	private LdapTree parentTree;
	
	public String getCn() {
		return cn;
	}
	public void setCn(String cn) {
		this.cn = cn;
	}
	
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	
	public String[] getMembers() {
		return members;
	}
	public void setMembers(String[] members) {
		this.members = members;
	}
	public LdapTree getParentTree() {
		return parentTree;
	}
	public void setParentTree(LdapTree parentTree) {
		this.parentTree = parentTree;
	}
	/**
	 * i：institute（院）；d：department（所）
	 * @return
	 */
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	/**
	 * 机构别名
	 * @return
	 */
	public String getAliasName() {
		return aliasName;
	}
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	public Object getTag() {
		return tag;
	}
	public void setTag(Object tag) {
		this.tag = tag;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getDn() {
		return dn;
	}
	public void setDn(String dn) {
		this.dn = dn;
	}
}
