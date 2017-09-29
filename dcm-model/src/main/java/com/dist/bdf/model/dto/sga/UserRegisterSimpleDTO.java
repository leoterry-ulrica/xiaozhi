package com.dist.bdf.model.dto.sga;

import java.io.Serializable;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.validator.constraints.NotEmpty;

import com.dist.bdf.model.entity.sga.SgaUser;


/**
 * 用户注册的简单DTO
 * @author weifj
 *
 */
public class UserRegisterSimpleDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long uid;
	@NotEmpty(message = "property email can not be empty")
	private String email;
	// private String weChat;
	/*@NotEmpty(message = "property userName can not be empty")
	private String userName;*/
	/*@NotEmpty(message = "property sex can not be empty")
	private String sex;
	private String unit;
	private String position;
	private String telephone;
	private int sources;*/
	private String userPwd;
	private Long comId;
	private Long queueId;
	
	public String getEmail() {

		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	/*public String getWeChat() {
		return weChat;
	}
	public void setWeChat(String weChat) {
		this.weChat = weChat;
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
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}*/
	
	public static void main(String[] args) throws Exception {
		
		SgaUser u = new SgaUser();
		u.setEmail("754236623");
		u.setUserName("weifj");
		u.setId(123L);
		
		UserRegisterSimpleDTO dto = new UserRegisterSimpleDTO();
		BeanUtils.copyProperties(dto, u);
		
		// System.out.println(dto.getEmail()+","+dto.getUserName());
	}
	/*public int getSources() {
		return sources;
	}
	public void setSources(int sources) {
		this.sources = sources;
	}*/
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	
	public Long getComId() {
		return comId;
	}
	public void setComId(Long comId) {
		this.comId = comId;
	}
	public Long getQueueId() {
		return queueId;
	}
	public void setQueueId(Long queueId) {
		this.queueId = queueId;
	}
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	@Override
	public String toString() {
		return "UserRegisterSimpleDTO [uid=" + uid + ", email=" + email + ", userPwd=" + userPwd + ", comId=" + comId
				+ ", queueId=" + queueId + "]";
	}
	
}
