package com.dist.bdf.model.dto.system.user;

import java.io.Serializable;

/**
 * 
 * 个人信息更新的dto模型
 * @author weifj
 *
 */
public class UserPersonUpdateRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户的序列id
	 */
	private Long id;
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

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
