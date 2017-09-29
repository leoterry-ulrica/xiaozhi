package com.dist.bdf.facade.service.uic.domain;


import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.dist.bdf.model.dto.ldap.LdapOrg;
import com.dist.bdf.model.dto.ldap.LdapPerson;
import com.dist.bdf.model.dto.ldap.LdapTree;
import com.dist.bdf.model.dto.ldap.LdapWorkgroup;
import com.unboundid.ldap.sdk.AddRequest;
import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldap.sdk.SearchResultEntry;

/**
 * Created by kivi on 14/12/9.
 * @version 1.0，2014/12/09，kivi，创建ldap领域
 * @version 1.1，2016/03/08，weifj
 *    1. 添加方法：getGroupMembers(LDAPConnection conn, String distinguishName)：获取组成员列表
 *    2. 添加方法：isExistMemberOfGroup(LDAPConnection conn, String groupDistinguishName, String cnValue)：判断组内部是否已经存在某个成员
 *    3. 修改方法参数类型：addUserToGroup的String... userDn修改为：List<String> userDns
 *  @version 1.2，2016/03/10，weifj
 *     1. 添加方法：alterUserName(LDAPConnection conn, String distinguishName, String newName)，修改用户名称
 */
public interface LdapDmn {

	/**
	 * 获取当前的ldap链接
	 * @return
	 */
	public LDAPConnection getCurrLdapConnection();
    /**
     * 获取ldap连接并认证
     *
     * @param ldapUrl
     * @param port
     * @param bindDn
     * @param pasword
     * @return
     */
    public LDAPConnection getLdapConnection(String ldapUrl, int port, String bindDn, String pasword);

    /**
     * 添加ldap用户
     *
     * @param conn
     * @param addRequest
     * @return
     */
    public boolean add(LDAPConnection conn, AddRequest addRequest);
    /**
     * 使用默认ldap链接添加用户
     * @param addRequest
     * @return
     */
    public boolean add(AddRequest addRequest);

    /**
     * 删除ldap中的用户
     *
     * @param distinguishName
     * @return
     */
    public boolean delete(LDAPConnection conn, String distinguishName);
    /**
     * 使用默认ldap链接删除用户
     * @param distinguishName
     * @return
     */
    public boolean delete(String distinguishName);

    /**
     * 修改ldap中的用户密码
     *
     * @param distinguishName
     * @param newPassword
     * @return
     */
    public boolean alterUserPassowrd(LDAPConnection conn, String distinguishName, String newPassword);
    /**
     * 修改用户的别名（并非登录名，aliasName的值）
     * @param conn
     * @param distinguishName
     * @param newName
     * @return
     */
    public boolean alterUserName(LDAPConnection conn, String distinguishName, String newName);

    /**
     * 关闭打开的ldap连接
     *
     * @param connection
     * @return
     */
    public void closeLdapConnection(LDAPConnection connection);

    /**
     * 生成ldap entry
     *
     * @param name
     * @param parentDN
     * @param firstName
     * @param password
     * @param additionalAttributes
     * @return
     */
    public Entry generateUserEntry(String name, String parentDN, String firstName, String password, Attribute... additionalAttributes);
    /**
     * 生成ldap entry
     *
     * @param name
     * @param firstName
     * @param password
     * @param additionalAttributes
     * @return
     */
    public Entry generateUserEntry(String name, String firstName, String password, Attribute... additionalAttributes);

    /**
     * 添加ldap group entry
     *
     * @param name
     * @param parentDN
     * @param memberDNs
     * @return
     */
    public Entry generateGroupOfNamesEntry(String name, String groupType, String parentDN, String... memberDNs);
    /**
     * 添加ldap group entry
     *
     * @param name
     * @param groupType 组类别，i：院；d：所；r：角色
     * @param memberDNs
     * @return
     */
    public Entry generateGroupOfNamesEntry(String name, String groupType, String... memberDNs);
    /**
     * 添加ldap group entry
     * @param name
     * @param groupType  组类别，i：院；d：所；r：角色
     * @param properties
     * @param memberDNs
     * @return
     */
    public Entry generateGroupOfNamesEntry(String name, Map<String, String> properties, String... memberDNs);

    /**
     * 添加用户到组
     * @param conn
     * @param groupDn
     * @param userDns
     * @return
     */
    public boolean addUserToGroup(LDAPConnection conn, String groupDn, List<String> userDns);

    /**
     * 从组移除用户
     * @param conn
     * @param groupDn
     * @param userDn
     * @return
     */
    public boolean removeUsersFromGroup(LDAPConnection conn, String groupDn, String... userDn);
    /**
     * 根据dn获取ldap条目
     * @param conn
     * @param distinguishName
     * @return
     */
    public Entry searchByDn(LDAPConnection conn, String distinguishName);
    /**
     * 根据过滤器，查询条目集合
     * @param conn
     * @param filter
     * @return
     */
    public List<SearchResultEntry> searchByFilter(LDAPConnection conn, Filter filter);
    /**
     * 根据过滤器和baseDN，查询条目集合
     * @param conn
     * @param filter
     * @param baseDN
     * @return
     */
    public List<SearchResultEntry> searchByFilter(LDAPConnection conn, Filter filter, String baseDN);
    /**
     * 根据过滤器，查询唯一一条条目
     * @param conn
     * @param filter
     * @return
     */
    public SearchResultEntry searchUniqueByFilter(LDAPConnection conn, Filter filter);
    /**
     * 根据过滤器和baseDN，查询唯一一条条目
     * @param conn
     * @param filter
     * @param baseDN
     * @return
     */
    public SearchResultEntry searchUniqueByFilter(LDAPConnection conn, Filter filter, String baseDN);
    /**
     * 获取组成员列表
     * @param conn
     * @param groupDistinguishName 组的dn名称
     * @return 返回所有成员的dn值
     */
    public String[] getGroupMembers(LDAPConnection conn, String groupDistinguishName);
    /**
     * 判断组内部是否已经存在某个成员
     * @param conn
     * @param groupDistinguishName
     * @param memberDn 成员的DN值
     * @return true/false
     * @throws LDAPException 
     */
    public boolean isExistMemberOfGroup(LDAPConnection conn, String groupDistinguishName, String memberDn) throws LDAPException;
    /**
     * 判断组内部是否已经存在某个成员，通过member属性
     * @param groupDistinguishName
     * @param memberDn 成员的DN值
     * @return true/false
     * @throws LDAPException 
     */
    public boolean isExistMemberOfGroup(String groupDistinguishName, String memberDn) throws LDAPException;
    /**
     * 查询指定base name下的所有用户信息
     * @param conn
     * @param searchBn 查询的父节点，如：dc=dist，或者：cn=xdata,DC=DIST
     * @return
     * @throws LDAPSearchException 
     */
    public List<SearchResultEntry> searchUsers(LDAPConnection conn, String searchBn) throws LDAPSearchException;
    /**
     * 查询当前bn下的所有用户信息
     * @return
     * @throws LDAPSearchException
     */
    public List<SearchResultEntry> searchCurrUsers() throws LDAPSearchException;
    /**
     * 根据common name获取dn
     * @param cnValue
     * @return
     */
    @Deprecated
    public String getDistinguishedName(String cnValue);
    /**
     * 添加用户
     * @param loginName
     * @param name
     * @param password
     * @return
     */
    public boolean addUser(String loginName,String name, String password);
    /**
     * 添加用户
     * @param loginName 登录名
     * @param lastName 姓氏
     * @param password 密码
     * @param properties 其它属性Map
     * @return
     */
    public boolean addUser(String loginName,String lastName, String password, Map<String, String> properties);
    
    /**
     * 添加用户
     * @param loginName 登录名
     * @param lastName 姓氏
     * @param password 密码
     * @param parentDN 父dn
     * @param properties 其它属性Map
     * @return
     */
    @Deprecated
    public boolean addInetOrgPerson(String loginName,String lastName, String password, String parentDN, Map<String, String> properties);

    /**
     * 添加ldap组
     *
     * @param groupName
     * @param groupType 组类别，i：院；d：所；r：角色
     * @return
     */
    @Deprecated
    public boolean addGroup(String groupName, String groupType, String... memberDNs);
    /**
     * 添加ldap组
     * @param groupName
     * @param groupType 组类别，i：院；d：所；r：角色
     * @param properties 扩展属性
     * @param memberDNs 组下面的人员
     * @return
     */
    @Deprecated
    public boolean addGroup(String groupName,Map<String, String> properties, String... memberDNs);
    /**
     * 添加ldap组，并把组加入到父类的member属性
     * @param parentDn 
     * @param groupName
     * @param uid 组编码
     * @param groupType 组类别，i：院；d：所；r：角色
     * @param properties
     * @param memberDNs
     * @return
     */
    public boolean addGroup(String parentDn, String groupName, String uid, String groupType, Map<String, String> properties, List<String> memberDNs);
    /**
     * 根据名称删除ldap实体
     * @param cnValue，例如：cn=dist，后面的值：dist
     * @return
     */
    public boolean deleteByCName(String cnValue);
    /**
     * 批量删除实体（用户或者组）
     * @param ids
     * @return
     */
    public boolean batchDeleteEntries(String[] ids);
    /**
     * 修改用户密码
     * @param loginName
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
     * 添加一组人到LDAP中已存在的组
     * @param groupName
     * @param userNames
     * @return
     */
    public boolean addUsersToLdapGroup(String groupName, String[] userNames);
    /**
     * 从ldap组移除一组用户
     *
     * @param groupDn
     * @param userDns
     * @return
     */
    public boolean removeUsersFromLdapGroup(String groupDn, String[] userDns);
    /**
     * 从ldap验证用户信息
     * @param loginName
     * @param password
     * @return
     */
    public boolean checkUser(String loginName, String password);
    /**
     * 查询所有组
     * @return
     */
    Object searchCurrGroups();
    Object searchCurrGroupMap();
    /**
     * 检索机构域下的单元，以dn为key
     * @param realm
     * @return
     */
    Map<String, LdapOrg> searchOrganizationUnitMap(String realm);
    /**
     * 检索机构域下的机构（院，很有可能是它本身）
     * @param realm
     * @return
     */
    Map<String, LdapOrg> searchOrganizationMap(String realm);
    /**
     * 检索根目录下的机构（院），以dn为key
     * @return
     */
    Map<String, LdapOrg> searchOrganizationMap();
    /**
     * 检索根下的一级目录下的机构（院），以ldap中o属性为key
     * @return
     */
    Map<String, LdapOrg> searchOrganizationOneLevelOMap();
    /**
     * 获取指定机构域下的单元和它本身，以dn为key
     * @param realm
     * @return
     */
    Map<String, LdapOrg> searchOrganizationAndUnitMap(String realm);
    /**
     * 获取工作组
     * @return
     */
    Object searchWorkGroups();
    /**
     * 获取指定工作组
     * @param name
     * @return
     */
    LdapWorkgroup searchWorkGroup(String realm, String name);
    /**
     * 获取院
     * @return
     */
    Map<String, LdapTree> searchInstituteGroupsMap();
    /**
     * 获取所
     * @return
     */
    Map<String, LdapTree> searchDepartmentGroupsMap();
    
    /**
     * 从所有组中过滤出工作组
     * @param allGroups
     * @return
     */
    List<LdapTree> filterWorkGroups(Collection<LdapTree> allGroups);
    /**
     * 根据工作组名称获取唯一的工作组实体
     * @param groupName
     * @return
     */
    LdapTree getUniqueWorkGroup(String groupName);
    
    SearchResultEntry getUniqueWorkGroupEntry(String groupName);
    String[] getWorkGroupMembers(SearchResultEntry entry);
    
    /**
     * 获取ldap用户map
     * @return
     */
    Map<String, LdapPerson> searchLdapPersonsMap();
    
    /***************************************************
     * 
     * 重新设计LDAP信息同步
     * 
     * 
     ***************************************************/
   /**
    * 检索院列表
    * @return
    */
    public List<LdapOrg> searchInstitutes();
    /**
     * 检索院下的机构（包括广规院的工作小组）,类：organizationalUnit
     * @param baseDN 院的dn
     * @return
     */
    public List<LdapOrg> searchDepartments(String baseDN);
    /**
     * 检索院下的一级机构，机构类：groupOfNames
     * @param baseDN
     * @return
     */
    public List<LdapOrg> searchDepartmentsDefinedByGroupofname(String baseDN);
    /**
     * 检索院下的一级机构，机构类：groupOfNames，以cn为key
     * @param baseDN
     * @return
     */
    public Map<String, LdapOrg> searchDepartmentsDefinedByGroupofnameCnMap(String baseDN);
    /**
     * 检索所有人员
     * @return
     */
    public List<LdapPerson> searchAllPersons();
    /**
     * 检索所机构下的人员
     * @param baseDN 所的dn或者工作小组的dn
     * @return
     */
    public List<LdapPerson> searchPersons(String baseDN);
    /**
     * 检索ldap用户信息
     * @param baseDN
     * @param cnValue cn的值
     * @return 唯一的用户信息
     */
    public LdapPerson searchUniquePerson(String baseDN, String cnValue);
  /*  *//**
     * 新建机构实体
     * @param parentDN
     * @param name
     * @param alias
     * @return
     *//*
    public Entry generateOrganizationEntry(String parentDN, String name, String alias, String uid);
    *//**
     * 新建机构单元实体
     * @param parentDN
     * @param name
     * @param alias
     * @return
     *//*
    public Entry generateOrganizationUnitEntry(String parentDN, String name, String alias, String uid);
    *//**
     * 新建机构成员
     * @param name
     * @param firstName
     * @param password
     * @param parentDN
     * @param additionalAttributes
     * @return
     *//*
    public Entry generateInetOrgPersonEntry(String name, String lastName, String password, String parentDN, Collection<Attribute> additionalAttributes);
    */
    /**
     * 获取指定base dn下的所有用户
     * @param bn
     * @return
     */
	List<LdapPerson> searchAllPersons(String bn);
	/**
	 * 获取指定base dn下的所有Map用户，以cn为key
	 * @param bn
	 * @return
	 */
	Map<String, LdapPerson> searchAllPersonsCnMap(String bn);
	Entry generateUserEntry(String bn, String name, String firstName, String password, String[] objectClasses,
			Attribute[] additionalAttributes);
	boolean addInetOrgPerson(String bn, String loginName, String lastName, String password, String uid,
			Map<String, String> properties);
	/**
	 * 创建机构organization
	 * @param parentDN
	 * @param name
	 * @param alias
	 * @param uid
	 * @param memberDNs
	 * @return
	 */
	boolean addOrganization(String parentDN, String name, String alias, String uid, String[] memberDNs);
	/**
	 * 检测o=organization下是否存在指定用户
	 * @param orgDn
	 * @param personCnValue
	 */
	boolean isExistInetOrgPerson(String orgDn, String personCnValue);
	/**
	 * 添加人员到组，添加人员dn到groupOfNames的member属性
	 * @param groupDn
	 * @param memberDn
	 */
	public boolean addInetOrgPersonToGroup(String groupDn, List<String> memberDns);
	/**
	 * 检测是否已存在组
	 * @param groupRootDn 如：cn=thupdi,o=thupdi,DC=XIAOZHI，thupdi这里特指院的标识符
	 * @param groupCnValue 组名字
	 */
	public boolean isExistGroupOfname(String groupRootDn, String groupCnValue);
	/**
	 * 删除用户
	 * @param parentDn
	 * @param cnValue
	 * @return
	 */
	boolean deleteInetOrgPerson(String parentDn, String cnValue);
	/**
	 * 根据dn修改用户显示名称
	 * @param dn
	 * @param newName
	 * @return
	 */
	boolean alterUserNameByDN(String dn, String newName);
	/**
	 * 添加人员到组，添加人员dn到groupOfNames的member属性
	 * @param groupDn
	 * @param memberDns
	 * @return
	 */
	boolean addInetOrgPersonToGroup(String groupDn, String[] memberDns);
	/**
	 * 通过dn，修改用户密码
	 * @param dn
	 * @param newPassword
	 * @return
	 */
	boolean alterUserPassowrdByDN(String dn, String newPassword);
	/**
	 * 修改组的显示名称
	 * @param dn
	 * @param newName
	 * @return
	 */
	boolean alterGroupDisplayNameByDN(String dn, String newName);

}
