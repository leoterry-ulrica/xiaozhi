package com.dist.bdf.model.dto.system;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.dist.bdf.model.dto.system.user.BaseUserDTO;

/**
 * 工作组DTO
 * @author weifj
 *
 */
public class WorkgroupDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cn;
	private String groupCode;
	private String aliasName;
	private Map<String, BaseUserDTO> usersMap = new HashMap<String, BaseUserDTO>();
	
	public void addUser(Long id, String userName, String loginName ){
		
		if(!usersMap.containsKey(loginName)){
			BaseUserDTO dto = new BaseUserDTO(id, userName, loginName);
			usersMap.put(loginName, dto);
		}
	
	}
    public void addUser(Long id, String userName, String loginName, String dn ){
		
    	if(!usersMap.containsKey(dn)){
		  BaseUserDTO dto = new BaseUserDTO(id, userName, loginName, dn);
		  usersMap.put(dn, dto);
    	}
	}
    
    public void addUserByKeyCode(Long id, String userName, String loginName, String userCode ){
		
    	if(!usersMap.containsKey(loginName)){
		  BaseUserDTO dto = new BaseUserDTO();
		  dto.setId(id);
		  dto.setLoginName(loginName);
		  dto.setUserName(userName);
		  dto.setUserCode(userCode);
		  usersMap.put(loginName, dto);
    	}
	}

	public String getCn() {
		return cn;
	}
	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getAliasName() {
		return aliasName;
	}
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	public Collection<BaseUserDTO> getUsers() {
		return usersMap.values();
	}

	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	

}
