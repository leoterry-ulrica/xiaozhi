package com.dist.bdf.facade.service;


import java.util.List;
import com.dist.bdf.model.dto.ldap.LdapPerson;
import com.dist.bdf.model.entity.system.DcmUser;


/**
 * Created by kivi on 14/12/9.
 * @version 1.0，2016/03/03，weifj
 *    1. 添加获取dn接口：public String getDistinguishedName(String cnValue)
 * @version 1.1，2016/03/10，weifj
 *    1. 添加方法：alterUserName(String loginName, String newName)，修改用户名称
 */
public interface LdapService {

	/**
	 * 针对组织机构下，根据cn的值获取dn值
	 * @param cnValue
	 * @return
	 */
	public String getDistinguishedNameOfOrg(String cnValue);
    /**
     * 批量创建ldap用户
     *
     * @param UserList
     * @return
     */
    public void batchAddUser(List<DcmUser> UserList);

    /**
     * 添加ldap用户
     * @param user
     * @param password
     * @return
     */
    public boolean addUser(String loginName,String name, String password);
    
    /**
     * 添加ldap组
     *
     * @param groupName
     * @return
     */
    public boolean addGroup(String groupName, String... memberDNs);

    /**
     * 删除ldap中的用户或组
     * @param userId
     * @return
     */
    public boolean delete(String userId);

    /**
     * 批量删除ldap中的用户或组
     * @param ids，唯一标识符的数组
     * @return
     */
    public boolean batchDelete(String[] ids);

    /**
     * 修改ldap中的用户密码
     * @param loginName 登录名
     * @param newPassword
     * @return
     */
    public boolean alterUserPassowrd(String loginName, String newPassword);
    /**
     * 修改ldap中的用户名称
     * @param loginName 登录名
     * @param newName 新的名称
     * @return
     */
    public boolean alterUserName(String loginName, String newName);

    /**
     * 添加ldap用户到组
     *
     * @param userNames
     * @param groupName
     * @return
     */
    public boolean addUserToGroup(String groupName, String[] userNames);

    /**
     * 从ldap组移除用户
     *
     * @param userNames
     * @param groupName
     * @return
     */
    public boolean removeUserFromGroup(String groupName, String[] userNames);
    /**
     * 获取ldap中所有人员信息
     * @return
     */
    public List<LdapPerson> getAllPersons();
    
	public String getDistinguishedName(String cnValue);
}
