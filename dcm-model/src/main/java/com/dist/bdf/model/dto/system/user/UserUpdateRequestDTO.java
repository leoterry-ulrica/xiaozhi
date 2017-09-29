package com.dist.bdf.model.dto.system.user;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 
 * 用户信息更新的dto模型
 * @author weifj
 *
 */
public class UserUpdateRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户的序列id
	 */
	private Long id;
	 /**
     * 用户名，一般为中文名
     */
	@NotEmpty(message = "userName can not be empty")
	protected String userName;
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
     * 部门
     */
    // private String department;
    /**
     * 职位
     */
    private String position;
    /**
     * 团队名称
     */
    private String teamName;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 特长
     */
    private String speciality;
    /**
     * 性别
     */
    private String sex;
    /**
     * 密码密文
     */
    private String pwd;
    /**
     * 密码模式
     * true：修改密码
     * false：不修改密码
     */
    private boolean pwdMode;
    
    private UserOrgRolesUpdateDTO institute;
    
    private UserOrgRolesUpdateDTO preDepartment;
    private UserOrgRolesUpdateDTO newDepartment;
    /**
     * 原来的部门序列id
     */
    //@NotNull( message = "preOrgId can not be null")
    @Deprecated
    private Long preOrgId;
    /**
     * 新部门序列id
     */
    //@NotNull( message = "newOrgId can not be null")
    @Deprecated
    private Long newOrgId;
    /**
     * 原来的角色id
     */
    //@NotNull( message = "preRoleId can not be null")
    @Deprecated
    private Long preRoleId;
    /**
     * 新的角色id
     */
    //@NotNull( message = "newRoleId can not be null")
    @Deprecated
    private Long newRoleId;
    
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
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
/*	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}*/
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	 @Deprecated
	public Long getPreOrgId() {
		return preOrgId;
	}
	 @Deprecated
	public void setPreOrgId(Long preOrgId) {
		this.preOrgId = preOrgId;
	}
	 @Deprecated
	public Long getNewOrgId() {
		return newOrgId;
	}
	 @Deprecated
	public void setNewOrgId(Long newOrgId) {
		this.newOrgId = newOrgId;
	}
	 @Deprecated
	public Long getPreRoleId() {
		return preRoleId;
	}
	 @Deprecated
	public void setPreRoleId(Long preRoleId) {
		this.preRoleId = preRoleId;
	}
	 @Deprecated
	public Long getNewRoleId() {
		return newRoleId;
	}
	 @Deprecated
	public void setNewRoleId(Long newRoleId) {
		this.newRoleId = newRoleId;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public boolean getPwdMode() {
		return pwdMode;
	}
	public void setPwdMode(boolean pwdMode) {
		this.pwdMode = pwdMode;
	}
	/**
	 * 院
	 * @return
	 */
	public UserOrgRolesUpdateDTO getInstitute() {
		return institute;
	}
	public void setInstitute(UserOrgRolesUpdateDTO institute) {
		this.institute = institute;
	}
	/**
	 * 新部门
	 * @return
	 */
	public UserOrgRolesUpdateDTO getNewDepartment() {
		return newDepartment;
	}
	public void setNewDepartment(UserOrgRolesUpdateDTO newDepartment) {
		this.newDepartment = newDepartment;
	}
	/**
	 * 原来部门
	 * @return
	 */
	public UserOrgRolesUpdateDTO getPreDepartment() {
		return preDepartment;
	}
	public void setPreDepartment(UserOrgRolesUpdateDTO preDepartment) {
		this.preDepartment = preDepartment;
	}
}
