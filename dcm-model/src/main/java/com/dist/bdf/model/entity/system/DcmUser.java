package com.dist.bdf.model.entity.system;

import java.lang.Long;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import com.dist.bdf.base.entity.BaseEntity;
import com.dist.bdf.common.constants.DomainType;

/**
 * 
 * @author weifj
 *
 */
@Entity
@Table(name = "DCM_USER")
@XmlRootElement
public class DcmUser extends BaseEntity implements Cloneable {

	// Fields
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
	private String loginName;
	private String userPwd;
	private String userCode;
	private String email;
	private Long currentStatus;
	private Date dateCreated;
	private Date dateLastActivity;
	private String telephone;
	private String domainType = DomainType.PERSON;
	private String sex;
	private Long isBuildin;
	private String avatar;
	private String phone;
	private String major;
	private String qq;
	private String nativePlace;
	private String department;
	private String position;
	private Date birthday;
	private String speciality;
	private String dn;
	private String realm;
	private String teamName;
	private Integer superAdmin;
	
	@Override
    public DcmUser clone() {
        try {
			return (DcmUser) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
    }
	
	/*@Override
	public boolean equals(Object user) {
		
		if(StringUtil.isNullOrEmpty(this.getDn())) return false;
		
		if(user instanceof DcmUser){
			
			return this.getDn().equalsIgnoreCase(((DcmUser) user).getDn());
			
		}else{
			return false;
		}
		
	};*/
	// Constructors
	// Property accessors
	
	@Column(name = "USERNAME", length = 20)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "LOGINNAME", length = 20)
	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Column(name = "USERPWD", length = 100)
	public String getUserPwd() {
		return this.userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	@Column(name = "USERCODE")
	public String getUserCode() {
		return this.userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	@Column(name = "EMAIL", length = 50)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "CURRENTSTATUS", precision = 22, scale = 0)
	public Long getCurrentStatus() {
		return this.currentStatus;
	}

	public void setCurrentStatus(Long currentStatus) {
		this.currentStatus = currentStatus;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATECREATED", length = 7)
	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATELASTACTIVITY", length = 7)
	public Date getDateLastActivity() {
		return this.dateLastActivity;
	}

	public void setDateLastActivity(Date dateLastActivity) {
		this.dateLastActivity = dateLastActivity;
	}

	@Column(name = "TELEPHONE", length = 50)
	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Column(name = "DOMAINTYPE", length = 50)
	public String getDomainType() {
		return this.domainType;
	}

	public void setDomainType(String domainType) {
		this.domainType = DomainType.PERSON;
	}

	@Column(name = "SEX", length = 5)
	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name = "ISBUILDIN", precision = 2, scale = 0)
	public Long getIsBuildin() {
		return this.isBuildin;
	}

	public void setIsBuildin(Long isBuildin) {
		this.isBuildin = isBuildin;
	}

	@Column(name = "AVATAR")
	public String getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar the avatar to set
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Column(name = "PHONE")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "MAJOR")
	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}
	@Column(name = "QQ")
	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Column(name = "NATIVEPLACE")
	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

	@Column(name = "DEPARTMENT")
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@Column(name = "POSITION")
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Column(name = "BIRTHDAY")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	@Column(name = "DN")
	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	@Column(name = "REALM")
	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public Integer getSuperAdmin() {
		return superAdmin;
	}

	public void setSuperAdmin(Integer superAdmin) {
		this.superAdmin = superAdmin;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DcmUser [userName=" + userName + ", loginName=" + loginName + ", userPwd=" + userPwd + ", userCode="
				+ userCode + ", email=" + email + ", currentStatus=" + currentStatus + ", dateCreated=" + dateCreated
				+ ", dateLastActivity=" + dateLastActivity + ", telephone=" + telephone + ", domainType=" + domainType
				+ ", sex=" + sex + ", isBuildin=" + isBuildin + ", avatar=" + avatar + ", phone=" + phone + ", major="
				+ major + ", qq=" + qq + ", nativePlace=" + nativePlace + ", department=" + department + ", position="
				+ position + ", birthday=" + birthday + ", speciality=" + speciality + ", dn=" + dn + ", realm=" + realm
				+ ", teamName=" + teamName + ", superAdmin=" + superAdmin + "]";
	}

}