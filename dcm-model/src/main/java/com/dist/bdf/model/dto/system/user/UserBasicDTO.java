package com.dist.bdf.model.dto.system.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dist.bdf.common.constants.DomainType;
import com.dist.bdf.model.dto.system.OrgRolesDTO;
import com.dist.bdf.model.entity.system.DcmOrganization;
import com.dist.bdf.model.entity.system.DcmRole;

/**
 * 用户dto
 * @author weifj
 * @version 1.0，2016/01/12，创建机构dto
 * @version 1.1，2016/01/20，添加获取用户编码
 * @version 1.2，2016/01/27，删除属性state
 * @version 1.3，2016/04/16，去掉机构相关属性
 */
public class UserBasicDTO extends UserSimpleDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	 * 获取空间域类型编码
	 * @return
	 */
	public String getDomainType(){
		
		return DomainType.PERSON;
	}

	private List<OrgRolesDTO> orgRoles;
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

	public List<OrgRolesDTO> getOrgRoles() {
		return orgRoles;
	}

	public void setOrgRoles(List<OrgRolesDTO> orgRoles) {
		this.orgRoles = orgRoles;
	}

	public synchronized void addOrgAndRoles(DcmOrganization org, List<DcmRole> roles){
		
		if(null == orgRoles){
			orgRoles = new ArrayList<OrgRolesDTO>();
		}
		OrgRolesDTO dto = new OrgRolesDTO();
		dto.setOrg(org);
		dto.setRoles(roles);
		
		orgRoles.add(dto);
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

}
