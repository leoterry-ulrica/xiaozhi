package com.dist.bdf.model.dto.ldap;

import java.io.Serializable;

public class LdapWorkgroup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dn;
	private String name;
	private String alias;
	private String groupType;
	private String uid;
	private String[] members;
	
	public String getDn() {
		return dn;
	}
	public void setDn(String dn) {
		this.dn = dn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String[] getMembers() {
		return members;
	}
	public void setMembers(String[] members) {
		this.members = members;
	}
	
}
