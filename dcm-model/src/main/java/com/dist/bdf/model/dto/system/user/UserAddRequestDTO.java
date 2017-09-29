package com.dist.bdf.model.dto.system.user;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 用户dto
 * @author weifj
 * @version 1.0，2016/01/12，创建机构dto
 * @version 1.1，2016/01/20，添加获取用户编码
 * @version 1.2，2016/01/27，删除属性state
 * @version 1.3，2016/04/16，去掉机构相关属性
 */
public class UserAddRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 登录名
	 */
	@NotEmpty(message = "loginName can not be empty")
	private String loginName;
    /**
     * 用户名，一般为中文名
     */
	@NotEmpty(message = "userName can not be empty")
	private String userName;
    /**
     * 用户密码，json属性是pwd，类属性是userPwd
     */
	@JsonProperty("pwd")
	@NotEmpty(message = "user pwd can not be empty")
    private String userPwd;
	private String sex;
    /**
     * 邮件
     */
    private String email;

    /**
     * 移动电话
     */
    private String telephone;

    /**
     * 座机电话
     */
    private String phone;
   
    /**
     * 域
     */
    @NotEmpty(message = "realm can not be empty")
    private String realm;
    
    /**
     * 用户所属机构序列id
     */
    // @NotNull(message = "orgId can not be Null")
    private Long orgId;
   
    private UserOrgRolesAddDTO institute;
    private UserOrgRolesAddDTO department;
    // private Map<Long, Long[]> orgRoles;
    /**
     * 用户在机构中的角色编码
     */
  /*  @NotEmpty(message = "roleCodes can not be empty")
    private String[] roleCodes;*/
    //@NotNull( message = "roleId can not be Null")
    private Long roleId;
    
    /**
     * qq联系方式
     */
    private String qq;
    /**
     * 专业
     */
    private String major;
    /**
     * 籍贯
     */
    private String nativePlace;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 特长
     */
    private String speciality;
    /**
     * 职位
     */
    private String position;
    /**
     * 团队
     */
    private String teamName;
	/**
	 * @return the userPwd
	 */
	public String getUserPwd() {
		return userPwd;
	}

	/**
	 * @param userPwd the userPwd to set
	 */
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephone the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}


	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	
	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

/*	public String[] getRoleCodes() {
		return roleCodes;
	}

	public void setRoleCodes(String[] roleCodes) {
		this.roleCodes = roleCodes;
	}*/

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

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

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	/**
	 * 院机构和角色
	 * @return
	 */
	public UserOrgRolesAddDTO getInstitute() {
		return institute;
	}

	public void setInstitute(UserOrgRolesAddDTO institute) {
		this.institute = institute;
	}
	/**
	 * 所机构和角色
	 * @return
	 */
	public UserOrgRolesAddDTO getDepartment() {
		return department;
	}

	public void setDepartment(UserOrgRolesAddDTO department) {
		this.department = department;
	}

}
