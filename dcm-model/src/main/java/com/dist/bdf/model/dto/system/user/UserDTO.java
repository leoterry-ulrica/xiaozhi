package com.dist.bdf.model.dto.system.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.validator.constraints.NotEmpty;

import com.dist.bdf.common.constants.DomainType;
import com.dist.bdf.model.dto.system.OrgRolesDTO;
import com.dist.bdf.model.entity.system.DcmOrganization;
import com.dist.bdf.model.entity.system.DcmRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * 用户dto
 * @author weifj
 * @version 1.0，2016/01/12，创建机构dto
 * @version 1.1，2016/01/20，添加获取用户编码
 * @version 1.2，2016/01/27，删除属性state
 * @version 1.3，2016/04/16，去掉机构相关属性
 */
public class UserDTO extends UserSimpleDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long currentStatus;
    /**
     * 用户密码
     */
	@NotEmpty(message = "user pwd can not be empty")
    private String userPwd;

    /**
     * 邮件
     */
    private String email;

    /**
     * 移动电话
     */
    private String telephone;
   
    /**
     * 创建时间
     */
    private Date dateCreated;

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
    private String department;
    /**
     * 职位
     */
    private String position;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 特长
     */
    private String speciality;
    /**
     * 父dn
     */
    private String parentDn;
    /**
     * 域
     */
    @NotEmpty(message = "realm can not be empty")
    private String realm;
    /**
     * 团队名称
     */
    private String teamName;
    /**
     * 是否超管，1：是；0：否
     */
    private Integer superAdmin;
    
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
	 * 获取空间域类型编码
	 * @return
	 */
	public String getDomainType(){
		
		return DomainType.PERSON;
	}

	private List<OrgRolesDTO> orgRoles = new ArrayList<OrgRolesDTO>();
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

	/**
	 * @return the dateCreated
	 */
	public Date getDateCreated() {
		return dateCreated;
	}

	/**
	 * @param dateCreated the dateCreated to set
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Long getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(Long currentStatus) {
		this.currentStatus = currentStatus;
	}

	public List<OrgRolesDTO> getOrgRoles() {
		return orgRoles;
	}

	public void setOrgRoles(List<OrgRolesDTO> orgRoles) {
		this.orgRoles = orgRoles;
	}

	@JsonIgnore
	Map<String, OrgRolesDTO> orgRolesMap = new HashMap<String, OrgRolesDTO>();
	
	public synchronized void addOrgAndRoles(DcmOrganization org, List<DcmRole> roles){
		
		if(null == orgRoles){
			orgRoles = new ArrayList<OrgRolesDTO>();
		}
		OrgRolesDTO dto = new OrgRolesDTO();
		dto.setOrg(org);
		dto.setRoles(roles);
		
		orgRoles.add(dto);
	}
	
   public synchronized void addOrgAndRoles(DcmOrganization org, DcmRole role){
		
	   OrgRolesDTO dto = null;
	   if(!orgRolesMap.containsKey(org.getOrgCode())) {
			dto = new OrgRolesDTO();
			dto.setOrg(org);
			dto.setRoles(new ArrayList<DcmRole>());
			orgRoles.add(dto);
			orgRolesMap.put(org.getOrgCode(), dto);
	   } else {
		   dto =  orgRolesMap.get(org.getOrgCode());
	   }
	   dto.getRoles().add(role);
	
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

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

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

	public String getParentDn() {
		return parentDn;
	}

	public void setParentDn(String parentDn) {
		this.parentDn = parentDn;
	}

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

}
