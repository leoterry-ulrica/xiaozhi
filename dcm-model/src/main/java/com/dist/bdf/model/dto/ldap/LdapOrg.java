package com.dist.bdf.model.dto.ldap;

import java.io.Serializable;

public class LdapOrg  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 名字，对应机构的o或者机构单元的ou
	 */
	private String name;
	/**
	 * 别名，对应机构或者机构单元的displayName
	 */
	private String alias;
	/**
	 * guid
	 */
	private String uid;
	/**
	 * DN
	 */
	private String dn;
	/**
	 * 人员
	 */
	private String[] members;
	
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
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getDn() {
		return dn;
	}
	public void setDn(String dn) {
		this.dn = dn;
	}
	public String[] getMembers() {
		return members;
	}
	public void setMembers(String[] members) {
		this.members = members;
	}
}
