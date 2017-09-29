
package com.dist.bdf.model.dto.ldap;

import java.io.Serializable;

/**
 * @author weifj
 * @version 1.0，2016/03/15，weifj，创建ldap实体
 *
 */
public class LdapPerson implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cname;
	private String dn;
	private String rdn;
	private String pdn;
	private String userPassword;
	private String sex;
	private String aliasName;
	private String uid;

	/**
	 * distinguished name
	 */
	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}
	/**
	 * relative distinguished name
	 */
	public String getRdn() {
		return rdn;
	}

	public void setRdn(String rdn) {
		this.rdn = rdn;
	}
	/**
	 * parent distinguished name
	 */
	public String getPdn() {
		return pdn;
	}

	public void setPdn(String pdn) {
		this.pdn = pdn;
	}

	/**
	 * 名字，cn=后面的值
	 */
	public String getCname() {
		return cname;
	}

	/**
	 * @param cname the name to set
	 */
	public void setCname(String cname) {
		this.cname = cname;
	}

	/**
	 * @return 用户密码
	 */
	public String getUserPassword() {
		return userPassword;
	}

	/**
	 * @param userPassword the userPassword to set
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	/**
	 * 中文名
	 * @return
	 */
	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
}
