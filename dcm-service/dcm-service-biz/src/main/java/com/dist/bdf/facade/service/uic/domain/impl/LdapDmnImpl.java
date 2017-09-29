package com.dist.bdf.facade.service.uic.domain.impl;

import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.utils.StringUtil;
import com.dist.bdf.common.conf.common.LdapConf;
import com.dist.bdf.facade.service.uic.domain.LdapDmn;
import com.dist.bdf.model.dto.ldap.LdapOrg;
import com.dist.bdf.model.dto.ldap.LdapPerson;
import com.dist.bdf.model.dto.ldap.LdapTree;
import com.dist.bdf.model.dto.ldap.LdapWorkgroup;
import com.unboundid.ldap.matchingrules.DistinguishedNameMatchingRule;
import com.unboundid.ldap.sdk.*;
import com.unboundid.ldif.LDIFException;
import com.unboundid.util.StaticUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Created by kivi on 14/12/9.
 * @version 1.0，2014/12/09，kivi，创建
 * @version 1.1，2016/03/08，weifj
 *    1. 添加方法：getGroupMembers具体实现
 *    2. 添加方法：isExistMemberOfGroup具体实现
 * @version 1.2，2016/03/10，weifj
 *    1.  添加方法：alterUserName(LDAPConnection conn, String distinguishName, String newName)具体实现
 *    
 *
 *LDAP命令如下：
 *
 * 添加member成员命令：
 *    dn: cn=developer,cn=thupdi,o=thupdi,DC=XIAOZHI
      changetype: modify
      add: member
      member: cn=thupdi_zhangchm,o=thupdi,DC=XIAOZHI
      
    删除member成员命令：
     dn: cn=developer,cn=thupdi,o=thupdi,DC=XIAOZHI
     changetype: modify
     delete: member
     member: cn=thupdi_zhangchm,o=thupdi,DC=XIAOZHI

 */
@Service
public class LdapDmnImpl implements LdapDmn {

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    private static ThreadLocal<LDAPConnection> ldapConnThreadLocal = new ThreadLocal<LDAPConnection>();

/*	 private String ldapUrl;//ldap服务器地址
	 private int ldapPort;//ldap服务器端口
	 private String bindDn;//连接ldap服务器的账号
	 private String password;//连接ldap服务器的密码
	 private String parentDn;//要添加entry的父dn
*/	 
	 @Autowired
	 private LdapConf ldapConf;
	 
	 public LdapDmnImpl(){
		 
	  /*      this.ldapUrl = ldapConf.getUrl();
	        this.ldapPort = ldapConf.getPort();
	        this.bindDn = ldapConf.getBindDn();
	        this.password = ldapConf.getPassword();
	        this.parentDn = ldapConf.getBn();*/
	 }
	 
	@Override
	public LDAPConnection getCurrLdapConnection() {

		LDAPConnection conn = ldapConnThreadLocal.get();
		if (null == conn) {

			conn = this.getLdapConnection(this.ldapConf.getUrl(), this.ldapConf.getPort(),
					this.ldapConf.getBindDn(), this.ldapConf.getPassword());
			ldapConnThreadLocal.set(conn);
			return conn;
		}
		if(conn.isConnected()){
			return conn;
		}
		conn = this.getLdapConnection(this.ldapConf.getUrl(), this.ldapConf.getPort(),
				this.ldapConf.getBindDn(), this.ldapConf.getPassword());
		ldapConnThreadLocal.set(conn);
		return conn;
	}

    /**
     * 获取ldap连接并认证
     *
     * @param ldapUrl
     * @param port
     * @param bindDn
     * @param password
     * @return
     */
    @Override
    public LDAPConnection getLdapConnection(String ldapUrl, int port, String bindDn, String password) {
        LDAPConnection connection = null;
        try {
        	logger.info("-->url : "+ldapUrl);
        	logger.info("-->port : "+port);
        	logger.info("-->bindDn : "+bindDn);
        	//logger.info("-->password : "+password);
            connection = new LDAPConnection(ldapUrl, port, bindDn, password);
        } catch (LDAPException e) {
        	e.printStackTrace();
            logger.debug(e.getMessage());
            throw new BusinessException("连接ldap失败");
        }
        return connection;
    }

    /**
     * 添加ldap用户
     * @param conn
     * @param addRequest
     * @return
     */
    @Override
    public boolean add(LDAPConnection conn, AddRequest addRequest) {
        LDAPResult result;
        boolean isAddSuccess = false;
        try {
            result = conn.add(addRequest);
            if (result.getResultCode() == ResultCode.SUCCESS) {
                isAddSuccess = true;
            }
        } catch (LDAPException e) {
            logger.debug(e.getMessage());
            throw new BusinessException("添加出错，详情：[{0}]",e.getMessage());
        }
        return isAddSuccess;
    }
    @Override
    public boolean add(AddRequest addRequest){
    	  LDAPResult result;
          boolean isAddSuccess = false;
          try {
              result = this.getCurrLdapConnection().add(addRequest);
              if (result.getResultCode() == ResultCode.SUCCESS) {
                  isAddSuccess = true;
              }
          } catch (LDAPException e) {
        	  e.printStackTrace();
              logger.error(e.getMessage());
              throw new BusinessException("ldap添加出错，详情：[{0}]",e.getMessage());
          }
          return isAddSuccess;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Entry generateUserEntry(String name, String parentDN, String firstName, String password, Attribute... additionalAttributes) {
        return generatePersonEntry(name, parentDN, firstName, password, (Collection) StaticUtils.toList(additionalAttributes));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Entry generateUserEntry(String name, String firstName, String password, Attribute... additionalAttributes){
    	return generatePersonEntry(name, this.ldapConf.getBn(), firstName, password, (Collection) StaticUtils.toList(additionalAttributes));
    }
    
    @Override
    public Entry generateUserEntry(String bn, String name, String firstName, String password, String[] objectClasses, Attribute... additionalAttributes){
    	return generatePersonEntry(name, bn, firstName, password, objectClasses, (Collection) StaticUtils.toList(additionalAttributes));
    }
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
    private Entry generatePersonEntry(String name, String parentDN, String firstName, String password, Collection<Attribute> additionalAttributes) {
        ArrayList<Attribute> attrList = new ArrayList<Attribute>();
        attrList.add(new Attribute("sn", firstName));
        if (password != null) {
            attrList.add(new Attribute("userPassword", password));
        }

        if (additionalAttributes != null) {
            attrList.addAll(additionalAttributes);
        }

        String[] objectClasses = new String[]{"top", "person"};
        return generateEntry("cn", name, parentDN, objectClasses, attrList);
    }
    
    private Entry generatePersonEntry(String name, String parentDN, String firstName, String password, String[] objectClasses, Collection<Attribute> additionalAttributes) {
        ArrayList<Attribute> attrList = new ArrayList<Attribute>();
        attrList.add(new Attribute("sn", firstName));
        if (password != null) {
            attrList.add(new Attribute("userPassword", password));
        }

        if (additionalAttributes != null) {
            attrList.addAll(additionalAttributes);
        }

        return generateEntry("cn", name, parentDN, objectClasses, attrList);
    }

    /**
     * 生成ldap entry
     *
     * @param rdnAttr
     * @param rdnValue
     * @param parentDN
     * @param objectClasses
     * @param additionalAttributes
     * @return
     */
    private Entry generateEntry(String rdnAttr, String rdnValue, String parentDN, String[] objectClasses, Collection<Attribute> additionalAttributes) {
        RDN rdn = new RDN(rdnAttr, rdnValue);
        String dn;
        if (parentDN != null && parentDN.trim().length() != 0) {
            dn = rdn.toString() + ',' + parentDN;
        } else {
            dn = rdn.toString();
        }

        Entry entry = new Entry(dn, new Attribute[]{new Attribute("objectClass", objectClasses), new Attribute(rdnAttr, rdnValue)});
        if (additionalAttributes != null) {
            Iterator i$ = additionalAttributes.iterator();

            while (i$.hasNext()) {
                Attribute a = (Attribute) i$.next();
                entry.addAttribute(a);
            }
        }

        return entry;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Entry generateGroupOfNamesEntry(String name, String groupType, String parentDN, String... memberDNs) {
        return generateGroupOfNamesEntry(name, parentDN, (Collection) StaticUtils.toList(memberDNs));
    }

	@Override
    public Entry generateGroupOfNamesEntry(String name, String groupType, String... memberDNs){
    	return generateGroupOfNamesEntry(name, this.ldapConf.getBn(), (Collection<String>) StaticUtils.toList(memberDNs));
    }

	@Override
    public Entry generateGroupOfNamesEntry(String name, Map<String, String> properties, String... memberDNs) {
    	return generateGroupOfNamesEntry(name, this.ldapConf.getBn(), (Collection<String>) StaticUtils.toList(memberDNs), properties);
    }

	private Entry generateGroupOfNamesEntry(String name, String parentDN, Collection<String> memberDNs) {
		
        List<Attribute> attrList = new LinkedList<Attribute>();
        attrList.add(new Attribute("member", DistinguishedNameMatchingRule.getInstance(), memberDNs));
        
        return generateEntry("cn", name, parentDN, new String[]{"top", "groupOfNames"}, attrList);
    }
    private Entry generateGroupOfNamesEntry(String name, String parentDN, Collection<String> memberDNs, Map<String, String> properties) {
		
        List<Attribute> attrList = new LinkedList<Attribute>();
        attrList.add(new Attribute("member", DistinguishedNameMatchingRule.getInstance(), memberDNs));

        if(properties != null && !properties.isEmpty()){
        	for(@SuppressWarnings("rawtypes") java.util.Map.Entry entry : properties.entrySet()) {
    			attrList.add(new Attribute(entry.getKey().toString(), (null == entry.getValue())? "": entry.getValue().toString()));
    		
    		}
        }
	
        return generateEntry("cn", name, parentDN, new String[]{"top", "groupOfNames"}, attrList);
    }
    /**
     * 
     * @param name
     * @param parentDN
     * @param uid
     * @param groupType
     * @param memberDNs PS：如果member成员为0个，则不添加此属性
     * @param properties
     * @return
     */
    private Entry generateGroupOfNamesEntry(String name, String parentDN, String uid, String groupType, Collection<String> memberDNs, Map<String, String> properties) {
		
        List<Attribute> attrList = new LinkedList<Attribute>();
        if(memberDNs.size() >0 ){
        	   attrList.add(new Attribute("member", DistinguishedNameMatchingRule.getInstance(), memberDNs));
        }
     
        attrList.add(new Attribute("uid", DistinguishedNameMatchingRule.getInstance(), uid));
        attrList.add(new Attribute("GroupType", DistinguishedNameMatchingRule.getInstance(), groupType));

        if(properties != null && !properties.isEmpty()){
        	for(@SuppressWarnings("rawtypes") java.util.Map.Entry entry : properties.entrySet()) {
    			attrList.add(new Attribute(entry.getKey().toString(), (null == entry.getValue())? "": entry.getValue().toString()));
    		
    		}
        }
	
        return generateEntry("cn", name, parentDN, new String[]{"top", "groupOfNames"}, attrList);
    }

    @Override
    public boolean delete(LDAPConnection conn, String distinguishName) {
        LDAPResult result;
        boolean isDeleteSuccess = false;

        try {
            if (conn.getEntry(distinguishName) == null) {
                return true;
            }
            DN toDeleteDn = new DN(distinguishName);
            DeleteRequest deleteRequest = new DeleteRequest(toDeleteDn);

            result = conn.delete(deleteRequest);
            if (result.getResultCode() == ResultCode.SUCCESS) {
                isDeleteSuccess = true;
            }
        } catch (LDAPException e) {
        	e.printStackTrace();
            logger.error("删除LDAP用户出错，详情："+e.getMessage());
            //throw new BusinessException("删除LDAP用户出错，详情："+e.getMessage());
        }
        return isDeleteSuccess;
    }
    @Override
    public boolean delete(String distinguishName) {
        LDAPResult result;
        boolean isDeleteSuccess = false;
        LDAPConnection conn = this.getCurrLdapConnection();
        try {
            if (conn.getEntry(distinguishName) == null) {
                return true;
            }
            DN toDeleteDn = new DN(distinguishName);
            DeleteRequest deleteRequest = new DeleteRequest(toDeleteDn);

            result = conn.delete(deleteRequest);
            if (result.getResultCode() == ResultCode.SUCCESS) {
                isDeleteSuccess = true;
            }
        } catch (LDAPException e) {
            logger.debug(e.getMessage());
            throw new BusinessException("删除用户出错");
        }
        return isDeleteSuccess;
    }

    
    @Override
    public boolean alterUserPassowrd(LDAPConnection conn, String distinguishName, String newPassword) {
        LDAPResult result;
        boolean isAlterSuccess = false;
        try {
            Entry entry = conn.getEntry(distinguishName);
            if (entry == null) {
                throw new BusinessException("用户不存在 ");
            }
            ModifyRequest modifyRequest = new ModifyRequest("dn:" + distinguishName, "changetype:modify",
                    "replace:userPassword", "userPassword:" + newPassword);
            if (conn != null) {
                result = conn.modify(modifyRequest);
                if (result.getResultCode() == ResultCode.SUCCESS) {
                    isAlterSuccess = true;
                }
            }
        } catch (LDIFException e) {
            logger.debug(e.getMessage());
            throw new BusinessException("修改用户密码出错");
        } catch (LDAPException e) {
            logger.debug(e.getMessage());
            throw new BusinessException("修改用户密码出错");
        }
        return isAlterSuccess;
    }
    
    @Override
    public boolean alterUserName(LDAPConnection conn, String distinguishName, String newName){
    	 LDAPResult result;
         boolean isAlterSuccess = false;
     
         try {
        	    Entry entry = conn.getEntry(distinguishName);
                if (entry == null) {
                    throw new BusinessException("用户不存在 ");
                }
             ModifyRequest modifyRequest = new ModifyRequest("dn:" + distinguishName, "changetype:modify",
                     "replace:displayName", "displayName:" + newName);
             if (conn != null) {
                 result = conn.modify(modifyRequest);
                 if (result.getResultCode() == ResultCode.SUCCESS) {
                     isAlterSuccess = true;
                 }
             }
         } catch (Exception e) {
             logger.debug(e.getMessage(), e);
             throw new BusinessException("修改用户名称出错，详情："+e.getMessage());
         }
         return isAlterSuccess;
    }

    private boolean alterDisplayName(LDAPConnection conn, String distinguishName, String newName){
   	 LDAPResult result;
        boolean isAlterSuccess = false;
        try {
            Entry entry = conn.getEntry(distinguishName);
            if (entry == null) {
                throw new BusinessException("实体不存在： "+distinguishName);
            }
            ModifyRequest modifyRequest = new ModifyRequest("dn:" + distinguishName, "changetype:modify",
                    "replace:displayName", "displayName:" + newName);
            if (conn != null) {
                result = conn.modify(modifyRequest);
                if (result.getResultCode() == ResultCode.SUCCESS) {
                    isAlterSuccess = true;
                }
            }
        } catch (LDIFException e) {
            logger.debug(e.getMessage());
            throw new BusinessException("修改实体显示名称出错");
        } catch (LDAPException e) {
            logger.debug(e.getMessage());
            throw new BusinessException("修改实体显示名称出错");
        }
        return isAlterSuccess;
   }

    /**
     * 添加用户到组
     *
     * @param groupDn
     * @param userDn
     * @return
     */
    @Override
    public boolean addUserToGroup(LDAPConnection conn, String groupDn, List<String> userDns) {
        if (userDns == null) {
            return false;
        } else {
            //List userDnList = new ArrayList(userDn.length);
            //userDnList.addAll(Arrays.asList(userDn));
            return operateGroup(conn, groupDn, userDns, ModificationType.ADD);
        }
    }


    /**
     * 从组移除用户
     *
     * @param conn
     * @param groupDn
     * @param userDn
     * @return
     */
    @Override
    public boolean removeUsersFromGroup(LDAPConnection conn, String groupDn, String... userDn) {
        if (userDn == null) {
            return false;
        } else {
            List<String> userDnList = new ArrayList<String>(userDn.length);
            userDnList.addAll(Arrays.asList(userDn));
            return operateGroup(conn, groupDn, userDnList, ModificationType.DELETE);
        }
    }

    private boolean operateGroup(LDAPConnection conn, String groupDn, List<String> userDnList, ModificationType type) {
        boolean isAddSuccess = false;
        try {
            Entry groupEntry = conn.getEntry(groupDn);
    
            if (groupEntry == null) {
                throw new BusinessException("用户组不存在");
            }

            List<Modification> modifications = new ArrayList<Modification>();
            for (String userDn : userDnList) {
                Modification modification = new Modification(type, "member", userDn);
                modifications.add(modification);
            }
            ModifyRequest request = new ModifyRequest(groupDn, modifications);
            LDAPResult result = conn.modify(request);
            if (result.getResultCode() == ResultCode.SUCCESS) {
                isAddSuccess = true;
            }
        } catch (LDAPException e) {
            e.printStackTrace();
        }
        return isAddSuccess;
    }

    @Override
    public void closeLdapConnection(LDAPConnection connection) {
        if (connection != null && connection.isConnected()) {
            connection.close();
        }
    }
    @Override
    public Entry searchByDn(LDAPConnection conn, String distinguishName) {
    	
    	  Entry entry = null;
		try {
			entry = conn.getEntry(distinguishName);
			
			  if (entry == null) {
	              throw new BusinessException(String.format("dn=[%s]不存在 ",distinguishName));
	          }
		} catch (LDAPException e) {
			
			e.printStackTrace();
		}
        
          return entry;
    }
    @Override
    public List<SearchResultEntry> searchByFilter(LDAPConnection conn, Filter filter) {
    	try{
		SearchRequest request = new SearchRequest(ldapConf.getBn(), SearchScope.SUB, filter);

		SearchResult result = conn.search(request);
		System.out.println(result);
		List<SearchResultEntry> list = result.getSearchEntries();
		return list;
    	}catch(LDAPException e){
    		e.printStackTrace();
    	}
    	return null;
    }
    @Override
	public List<SearchResultEntry> searchByFilter(LDAPConnection conn, Filter filter, String baseDN) {

		try {
			SearchRequest request = new SearchRequest(baseDN, SearchScope.SUB, filter);

			SearchResult result = conn.search(request);
			System.out.println(result);
			List<SearchResultEntry> list = result.getSearchEntries();
			return list;

		} catch (LDAPException e) {
			e.printStackTrace();
		}
		return null;
	}
    @Override
    public SearchResultEntry searchUniqueByFilter(LDAPConnection conn, Filter filter) {
    	
    	List<SearchResultEntry> list = this.searchByFilter(conn, filter);
    	if(list != null && list.size()>0) return list.get(0);
    	
    	return null;
    	//throw new BusinessException(String.format("没有找到过滤条件【%s】 的条目。",filter.toString()));

    }
    @Override
    public SearchResultEntry searchUniqueByFilter(LDAPConnection conn, Filter filter, String baseDN) {
    	
    	List<SearchResultEntry> list = this.searchByFilter(conn, filter, baseDN);
    	if(list != null && list.size()>0) return list.get(0);
    	
    	return null;
    	
    }
    @Override
    public String[] getGroupMembers(LDAPConnection conn, String groupDistinguishName) {
    	
    	Entry entry = this.searchByDn(conn, groupDistinguishName);
    	if(null == entry){
    		  throw new BusinessException(String.format("dn=[%s]不存在 ",groupDistinguishName));
    	}
    	return entry.getAttributeValues("member");
    	
    }
    
    @Override
    public boolean isExistMemberOfGroup(LDAPConnection conn, String groupDistinguishName, String memberDn) throws LDAPException {
    	
    	String[] members = this.getGroupMembers(conn, groupDistinguishName);
    	if(null == members) throw new BusinessException(String.format("dn=[%s]不是一个组，或者组成员为空 ",groupDistinguishName));
    	
    	for(String member : members){
    		if(member.equalsIgnoreCase(memberDn)) return true;
    	}
    	return false;
    }
    @Override
    public boolean isExistMemberOfGroup(String groupDistinguishName, String memberDn) throws LDAPException {
    	
    	String[] members = this.getGroupMembers(this.getCurrLdapConnection(), groupDistinguishName);
    	if(null == members) throw new BusinessException(String.format("dn=[%s]不是一个组，或者组成员为空 ",groupDistinguishName));
    	
    	for(String member : members){
    		if(member.equalsIgnoreCase(memberDn)) return true;
    	}
    	return false;
    }
    @Override
    public List<SearchResultEntry> searchUsers(LDAPConnection conn, String searchBn) throws LDAPSearchException{
    	
    	Filter filter = Filter.createEqualityFilter("objectClass","person");
		SearchRequest request = new SearchRequest(searchBn, SearchScope.SUB, filter);
		SearchResult result = conn.search(request);
		List<SearchResultEntry> list = result.getSearchEntries();
		return list;
		
    }
    @Override
    public List<SearchResultEntry> searchCurrUsers() throws LDAPSearchException{
    	
    	Filter filter = Filter.createEqualityFilter("objectClass","person");
		SearchRequest request = new SearchRequest(this.ldapConf.getBn(), SearchScope.SUB, filter);
		SearchResult result = this.getCurrLdapConnection().search(request);
		List<SearchResultEntry> list = result.getSearchEntries();
		return list;
    }
    @Deprecated
    @Override
    public String getDistinguishedName(String cnValue) {
    	
    	try {
			SearchResultEntry entry = this.searchUniqueByFilter(this.getCurrLdapConnection(), Filter.create(String.format("cn=%s",cnValue)));
			
			if(entry != null) return entry.getDN();
			
		} catch (LDAPException e) {
			
			e.printStackTrace();
		}
    	return "";
    	
    }
   
    @Override
    public boolean addUser(String loginName,String name, String password) {
 
        AddRequest addRequest = new AddRequest(this.generateUserEntry(loginName, name, password));
        return this.add(addRequest);
    }
    @Override
    public boolean addUser(String loginName,String lastName, String password, Map<String, String> properties) {
    	
    	 AddRequest addRequest = null;
    	if(null == properties || properties.isEmpty()){
    		addRequest = new AddRequest(this.generateUserEntry(loginName, lastName, password));
    		
    	}else{
    		Attribute[] attributes = new Attribute[properties.size()];
    		int i = 0;
    		for(@SuppressWarnings("rawtypes") java.util.Map.Entry entry : properties.entrySet()) {
    			attributes[i] = new Attribute(entry.getKey().toString(), (null == entry.getValue())? "": entry.getValue().toString());
    			i++;
    		}
    		addRequest = new AddRequest(this.generateUserEntry(loginName, lastName, password, attributes));
    		
    	}
    	 return this.add(addRequest);
    }
    
    @Override
	public boolean addInetOrgPerson(String bn, String loginName, String lastName, String password, String uid,
			Map<String, String> properties) {

		AddRequest addRequest = null;
		List<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(new Attribute("uid", uid));
		if (properties != null && !properties.isEmpty()) {

			for (@SuppressWarnings("rawtypes")
			java.util.Map.Entry entry : properties.entrySet()) {
				attributes.add(new Attribute(entry.getKey().toString(),
						(null == entry.getValue()) ? "" : entry.getValue().toString()));
			}

		}

		addRequest = new AddRequest(this.generateUserEntry(bn, loginName, lastName, password,
				new String[] { "top", "person", "organizationalPerson", "inetOrgPerson" },
				attributes.toArray(new Attribute[attributes.size()])));

		return this.add(addRequest);
	}
    
    @Deprecated
    @Override
	public boolean addInetOrgPerson(String loginName, String lastName, String password, String parentDN,
			Map<String, String> properties) {

		AddRequest addRequest = null;
		List<Attribute> attributes = new ArrayList<Attribute>(properties.size());

		for (@SuppressWarnings("rawtypes")
		java.util.Map.Entry entry : properties.entrySet()) {
			attributes.add(new Attribute(entry.getKey().toString(),
					(null == entry.getValue()) ? "" : entry.getValue().toString()));

		}
		addRequest = new AddRequest(
				this.generateInetOrgPersonEntry(loginName, lastName, password, parentDN, attributes));

		return this.add(addRequest);

	}
    @Deprecated
    @Override
    public boolean addGroup(String groupName, String groupType, String... memberDNs) {

        if (memberDNs.length == 0) {
            memberDNs = new String[]{""};
        }
        AddRequest addRequest = new AddRequest(this.generateGroupOfNamesEntry(groupName, groupType, memberDNs));
        return this.add(addRequest);
    }
    
    @Override
    public boolean addOrganization(String parentDN, String name, String alias, String uid, String[] memberDNs) {

        if (null == memberDNs || 0 == memberDNs.length) {
            memberDNs = new String[]{""};
        }
        AddRequest addRequest = new AddRequest(this.generateOrganizationEntry(parentDN, name, alias, uid, memberDNs));
        return this.add(addRequest);
    }
    @Deprecated
    @Override
    public boolean addGroup(String groupName, Map<String, String> properties, String... memberDNs) {
    	
    	   if (null == memberDNs || 0 == memberDNs.length) {
               memberDNs = new String[]{""};
           }
           AddRequest addRequest = new AddRequest(this.generateGroupOfNamesEntry(groupName, properties, memberDNs));
           return this.add(addRequest);
    }
    @Override
	public boolean addGroup(String parentDn, String groupName, String uid, String groupType,
			Map<String, String> properties, List<String> memberDNs) {

		if (null == memberDNs || 0 == memberDNs.size()) {
			memberDNs = new ArrayList<>(0);
		}
		AddRequest addRequest = new AddRequest(
				this.generateGroupOfNamesEntry(groupName, parentDn, uid, groupType, memberDNs, properties));
		boolean result = this.add(addRequest);
		if (result) {
			// 把组添加到父类组的member去
			// 给组的member添加组成员
			Modification modification = new Modification(ModificationType.ADD, "member", addRequest.getDN());
			List<Modification> modifications = new ArrayList<Modification>();
			modifications.add(modification);
			ModifyRequest request = new ModifyRequest(parentDn, modifications);

			try {
				LDAPResult ldapResult = this.getCurrLdapConnection().modify(request);
				return ldapResult.getResultCode() == ResultCode.SUCCESS;

			} catch (LDAPException e) {
				logger.error(e.getMessage());
			}
		}
		return false;
	}
    @Override
    public boolean deleteByCName(String cnValue) {
    	
        String distinguishName = this.getDistinguishedName(cnValue);//"cn=" + loginName + "," + parentDn;

        return this.delete( this.getCurrLdapConnection(), distinguishName);
    }
    
    @Override
    public boolean deleteInetOrgPerson(String parentDn, String cnValue) {
    	
        String distinguishName = "cn="+cnValue+","+parentDn;

        return this.delete( this.getCurrLdapConnection(), distinguishName);
    }
    @Override
    public boolean batchDeleteEntries(String[] ids) {
        for (String id : ids) {
            String distinguishName =this.getDistinguishedName(id);// "cn=" + userId + "," + parentDn;

            this.delete(distinguishName);
        }
        return true;
    }
    @Override
    public boolean alterUserPassowrd(String loginName, String newPassword) {
        String distinguishName = this.getDistinguishedName(loginName);//"cn=" + loginName + "," + parentDn;

        return this.alterUserPassowrd(this.getCurrLdapConnection(), distinguishName, newPassword);
    }
    @Override
    public boolean alterUserPassowrdByDN(String dn, String newPassword) {
        
        return this.alterUserPassowrd(this.getCurrLdapConnection(), dn, newPassword);
    }
    @Override
    public boolean alterUserName(String loginName, String newName){
    	   String distinguishName = this.getDistinguishedName(loginName);//"cn=" + loginName + "," + parentDn;

           return this.alterUserName( this.getCurrLdapConnection(), distinguishName, newName);
    }
    
    @Override
    public boolean alterUserNameByDN(String dn, String newName){
    	  
           return this.alterUserName( this.getCurrLdapConnection(), dn, newName);
    }
    
    @Override
    public boolean alterGroupDisplayNameByDN(String dn, String newName){
    	  
           return this.alterDisplayName(this.getCurrLdapConnection(), dn, newName);
    }
    @Override
    public boolean addUsersToLdapGroup(String groupName, String[] userNames) {
    	
        if (userNames == null || userNames.length == 0) {
            return false;
        } else {
        	 LDAPConnection conn = this.getCurrLdapConnection();
            //groupName = this.getDistinguishedName(groupName);//"cn=" + groupName + "," + parentDn;
        	 String groupDistinguishName = this.getDistinguishedName(groupName);
            List<String> userDns = new ArrayList<String>();
            for (int i = 0; i < userNames.length; i++) {
            	
            	String memberDistinguishName = this.getDistinguishedName(userNames[i]);
				try {
					boolean isExist = this.isExistMemberOfGroup(conn, groupDistinguishName, memberDistinguishName);
					if(!isExist) {
						userDns.add(memberDistinguishName);
	            		//userDns.add(String.format("cn=%s,%s", userNames[i], parentDn));
	            	}
				} catch (LDAPException e) {
					e.printStackTrace();
				}
            }
           
            return this.addUserToGroup(conn, groupDistinguishName, userDns);
        }
    }
    @Override
    public boolean removeUsersFromLdapGroup(String groupDn, String[] userDns) {
        if (userDns == null || userDns.length == 0) {
            return false;
        } else {
        /*	String groupDn =  this.getDistinguishedName(groupName);
            //groupName ="cn=" + groupName + "," + parentDn;
            String[] userDns = new String[userNames.length];
            for (int i = 0; i < userNames.length; i++) {
                userDns[i] = "cn=" + userNames[i] + "," + groupDn;
            }*/
            LDAPConnection conn = this.getCurrLdapConnection();
            return this.removeUsersFromGroup(conn, groupDn, userDns);
        }
    }
    @Override
    public boolean checkUser(String loginName, String password){
    	
    	SearchResultEntry findUser = this.searchUniqueByFilter(this.getCurrLdapConnection(), Filter.createEqualityFilter("cn", loginName));
    	
    	if(null == findUser) throw new BusinessException(String.format("在LDAP中没有找到用户【%s】的信息......",loginName));
    	
    	return findUser.getAttributeValue("userPassword").equals(password)? true:false;
    }

	@Override
	public Collection<LdapTree> searchCurrGroups() {
		
		Map<String, LdapTree> map = searchCurrGroupMap();
		if(null == map) return null;
		
		return map.values();
		
	}
	
	@Override
	public Map<String, LdapTree> searchCurrGroupMap() {
		
		try {
			// 查询所有组
			Filter filter = Filter.createEqualityFilter("objectClass", "groupOfNames");
			
			SearchRequest request = new SearchRequest(this.ldapConf.getBn(), SearchScope.SUB, filter);

			LDAPConnection conn = this.getCurrLdapConnection();
			SearchResult result = conn.search(request);
			System.out.println(result);
			List<SearchResultEntry> list = result.getSearchEntries();

			Map<String, LdapTree> map = new LinkedHashMap<String, LdapTree>();

			for (SearchResultEntry sre : list) {
				String dn = sre.getDN();
				String[] strs = dn.split(",");
				int max = strs.length - 1;

				for (int i = max; i > -1; i--) {
					String cn = strs[i];
					
					LdapTree tr = new LdapTree();
					tr.setCn(cn);
					tr.setGuid(UUID.randomUUID().toString());
					if (i != max) {
						tr.setParentTree(map.get(strs[i + 1].toLowerCase()));
					}
					if (!map.containsKey(tr.getCn().toLowerCase())) {
						map.put(tr.getCn().toLowerCase(), tr);
					}
					
					Filter filterObjectClass = (tr.getCn().toLowerCase().indexOf("dc") > -1)
							? Filter.createEqualityFilter("objectClass", "domain")
							: Filter.createEqualityFilter("objectClass", "groupOfNames");
					Filter filterCn = (tr.getCn().toLowerCase().indexOf("dc") > -1)
							? Filter.createEqualityFilter("dc", tr.getCn().split("=")[1])
							: Filter.createEqualityFilter("cn", tr.getCn().split("=")[1]);
							
					SearchRequest requestSub = new SearchRequest(this.ldapConf.getBn(), SearchScope.SUB,
							Filter.createANDFilter(new Filter[] { filterObjectClass, filterCn }));

					SearchResult resultSub = conn.search(requestSub);
					SearchResultEntry sreSub = (resultSub.getSearchEntries().size() > 0)
							? resultSub.getSearchEntries().get(0) : null;
					tr.setGroupType(sreSub.hasAttribute("GroupType")? sreSub.getAttributeValue("GroupType") : ""); // tds自带属性名称
					tr.setAliasName(StringUtil.isNullOrEmpty(sreSub.getAttributeValue("displayName"))? tr.getCn().split("=")[1] : sreSub.getAttributeValue("displayName"));
					tr.setGroupCode(sreSub.hasAttribute("groupCode")? sreSub.getAttributeValue("groupCode") : "");
					if (sreSub != null) {
						String[] members = sreSub.getAttributeValues("member");
						if (members != null) {
							tr.setMembers(members);
						}else {
							tr.setMembers(new String[0]);
						}
						
					}

				}

			}
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Map<String, LdapOrg> searchOrganizationUnitMap(String realm) {

		try {

			// 查询所有组
			Filter filter = Filter.createEqualityFilter("objectClass", "organizationalUnit");

			SearchRequest request = new SearchRequest("o=" + realm + "," + this.ldapConf.getBn(), SearchScope.SUB,
					filter);

			LDAPConnection conn = this.getCurrLdapConnection();
			SearchResult result = conn.search(request);
			System.out.println(result);
			List<SearchResultEntry> list = result.getSearchEntries();

			Map<String, LdapOrg> map = new LinkedHashMap<String, LdapOrg>();
			for (SearchResultEntry sre : list) {

				LdapOrg ldapOrg = new LdapOrg();
				ldapOrg.setAlias(sre.getAttributeValue("displayName"));
				ldapOrg.setDn(sre.getDN().toLowerCase());
				ldapOrg.setName(sre.getAttributeValue("ou"));
				ldapOrg.setUid(sre.getAttributeValue("uid"));

				map.put(ldapOrg.getDn(), ldapOrg);
			}

			return map;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Map<String, LdapOrg> searchOrganizationMap(String realm) {

		try {
			Filter filter = Filter.createEqualityFilter("objectClass", "organization");
			SearchRequest request = new SearchRequest("o=" + realm + "," + this.ldapConf.getBn(), SearchScope.SUB,
					filter);
			LDAPConnection conn = this.getCurrLdapConnection();
			SearchResult result = conn.search(request);
			List<SearchResultEntry> list = result.getSearchEntries();
			Map<String, LdapOrg> map = new LinkedHashMap<String, LdapOrg>();
			for (SearchResultEntry sre : list) {

				LdapOrg ldapOrg = new LdapOrg();
				ldapOrg.setAlias(sre.getAttributeValue("displayName"));
				ldapOrg.setDn(sre.getDN().toLowerCase());
				ldapOrg.setName(sre.getAttributeValue("o"));
				ldapOrg.setUid(sre.getAttributeValue("uid"));

				map.put(ldapOrg.getDn(), ldapOrg);
			}
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	@Override
	public Map<String, LdapOrg> searchOrganizationMap() {
		
		try {
			Filter filter = Filter.createEqualityFilter("objectClass", "organization");
			SearchRequest request = new SearchRequest(this.ldapConf.getBn(), SearchScope.SUB,
					filter);
			LDAPConnection conn = this.getCurrLdapConnection();
			SearchResult result = conn.search(request);
			List<SearchResultEntry> list = result.getSearchEntries();
			Map<String, LdapOrg> map = new LinkedHashMap<String, LdapOrg>();
			for (SearchResultEntry sre : list) {

				LdapOrg ldapOrg = new LdapOrg();
				ldapOrg.setAlias(sre.getAttributeValue("displayName"));
				ldapOrg.setDn(sre.getDN().toLowerCase());
				ldapOrg.setName(sre.getAttributeValue("o"));
				ldapOrg.setUid(sre.getAttributeValue("uid"));

				map.put(ldapOrg.getDn(), ldapOrg);
			}
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
		
	}
	@Override
	public Map<String, LdapOrg> searchOrganizationOneLevelOMap() {
		
		try {
			Filter filter = Filter.createEqualityFilter("objectClass", "organization");
			SearchRequest request = new SearchRequest(this.ldapConf.getBn(), SearchScope.ONE,
					filter);
			LDAPConnection conn = this.getCurrLdapConnection();
			SearchResult result = conn.search(request);
			List<SearchResultEntry> list = result.getSearchEntries();
			Map<String, LdapOrg> map = new LinkedHashMap<String, LdapOrg>();
			for (SearchResultEntry sre : list) {

				LdapOrg ldapOrg = new LdapOrg();
				ldapOrg.setAlias(sre.getAttributeValue("displayName"));
				ldapOrg.setDn(sre.getDN().toLowerCase());
				ldapOrg.setName(sre.getAttributeValue("o"));
				ldapOrg.setUid(sre.getAttributeValue("uid"));

				map.put(ldapOrg.getName(), ldapOrg);
			}
			return map;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	@Override
	public Map<String, LdapOrg> searchOrganizationAndUnitMap(String realm) {
		
		try {

			Map<String, LdapOrg> map = new LinkedHashMap<String, LdapOrg>();
			Map<String, LdapOrg> orgMap = this.searchOrganizationMap(realm); 
			if(orgMap != null && !orgMap.isEmpty()){
				map.putAll(orgMap);
			}
			Map<String, LdapOrg> orgunitMap = this.searchOrganizationUnitMap(realm);
			if(orgunitMap != null && !orgunitMap.isEmpty()){
				map.putAll(orgunitMap);
			}

			return map;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
		
	}
	@Override
	public Object searchWorkGroups() {
		
		try {
			// 查询所有工作组
			Filter objectClassFilter = Filter.createEqualityFilter("objectClass", "groupOfNames");
			Filter groupTypeFilter = Filter.createEqualityFilter("GroupType", "r"); // 根据类型过滤
			
			return searchGroupMap(new Filter[]{objectClassFilter, groupTypeFilter}).values();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}
	
	@Override
	public LdapWorkgroup searchWorkGroup(String realm, String name) {
		
		try {
			// 查询所有工作组
			Filter objectClassFilter = Filter.createEqualityFilter("objectClass", "groupOfNames");
			Filter groupTypeFilter = Filter.createEqualityFilter("GroupType", "r"); // 根据类型过滤
			Filter nameFilter = Filter.createEqualityFilter("displayName", name);
			
			List<SearchResultEntry> entries = this.searchByFilter(getCurrLdapConnection(), Filter.createANDFilter(new Filter[]{objectClassFilter, groupTypeFilter, nameFilter}), "o="+realm+","+this.ldapConf.getBn());
			if(null == entries || entries.isEmpty()) return null;
			
			LdapWorkgroup wg = new LdapWorkgroup();
			SearchResultEntry wgEntry = entries.get(0);
			wg.setName(wgEntry.getAttributeValue("cn"));
			wg.setAlias(wgEntry.getAttributeValue("displayName"));
			wg.setDn(wgEntry.getDN().toLowerCase());
			wg.setUid(wgEntry.getAttributeValue("uid"));
			wg.setGroupType(wgEntry.getAttributeValue("GroupType"));
			wg.setMembers(wgEntry.getAttributeValues("member"));
			
			return wg;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
		
	}

	/**
	 * 以dn为key，LdapTree为value
	 * @param filters
	 * @return
	 * @throws LDAPSearchException
	 * @throws LDAPException
	 */
	private Map<String, LdapTree> searchGroupMap(Filter... filters)
			throws LDAPSearchException, LDAPException {
		
		SearchRequest request = new SearchRequest(this.ldapConf.getBn(), SearchScope.SUB, Filter.createANDFilter(filters));

		LDAPConnection conn = this.getCurrLdapConnection();
		SearchResult result = conn.search(request);
		List<SearchResultEntry> list = result.getSearchEntries();
		Map<String, LdapTree> data = new HashMap<String, LdapTree>(list.size());

		for (SearchResultEntry sre : list) {
			
			String rdn = sre.getRDN().toString();
			if(StringUtil.isNullOrEmpty(rdn)){
				continue;
			}
			String cn = rdn.split("=")[1];
			LdapTree tr = new LdapTree();
			tr.setDn(sre.getDN().toLowerCase()); // 统一小写
			tr.setCn(cn);
			tr.setGroupType(sre.getAttributeValue("GroupType")); // tds自带属性名称
			tr.setAliasName(StringUtil.isNullOrEmpty(sre.getAttributeValue("aliasName"))? tr.getCn() : sre.getAttributeValue("aliasName"));
			tr.setGroupCode(sre.getAttributeValue("groupCode"));
			String[] members = sre.getAttributeValues("member");
			if (members != null) {
				tr.setMembers(members);
			}else {
				tr.setMembers(new String[0]);
			}

			data.put(tr.getDn(), tr);
		}
		return data;
	}
	
	@Override
	public Map<String, LdapTree> searchInstituteGroupsMap() {
		
		try{
			// 查询所有工作组
			Filter objectClassFilter = Filter.createEqualityFilter("objectClass", "groupOfNames");
		    Filter groupTypeFilter = Filter.createEqualityFilter("GroupType", "i"); // 根据类型过滤
			
		    Map<String, LdapTree> data = searchGroupMap(new Filter[]{objectClassFilter, groupTypeFilter});
		    
		    return data;
	
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public Map<String, LdapTree> searchDepartmentGroupsMap() {
		
		try{
			// 查询所有工作组
			Filter objectClassFilter = Filter.createEqualityFilter("objectClass", "groupOfNames");
		    Filter groupTypeFilter = Filter.createEqualityFilter("GroupType", "d"); // 根据类型过滤
			
		    Map<String, LdapTree> data = searchGroupMap(new Filter[]{objectClassFilter, groupTypeFilter});
		    
		    return data;
	
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return null;
	}
	@Override
	public  List<LdapTree> filterWorkGroups(Collection<LdapTree> allGroups) {
		
		List<LdapTree> workgroups = new ArrayList<LdapTree>();
		for(LdapTree g : allGroups){
			if(g.getGroupType() != null && g.getGroupType().equals("r")){
				workgroups.add(g);
			}
		}
		return workgroups;
	}
	@Override
	public LdapTree getUniqueWorkGroup(String groupName) {
		
		
		try {
			SearchResultEntry sre = getUniqueWorkGroupEntry(groupName);
		
			if(sre != null){
	
				String rdn = sre.getRDN().toString();
				if(StringUtil.isNullOrEmpty(rdn)){
					return null;
				}
				String cn = rdn.split("=")[1];
				LdapTree tr = new LdapTree();
				tr.setTag(sre);
				tr.setCn(cn);
				tr.setGroupType(sre.getAttributeValue("GroupType")); // tds自带属性名称
				tr.setAliasName(StringUtil.isNullOrEmpty(sre.getAttributeValue("aliasName"))? tr.getCn() : sre.getAttributeValue("aliasName"));
				tr.setGroupCode(sre.getAttributeValue("groupCode"));
				String[] members = sre.getAttributeValues("member");
				if (members != null) {
					tr.setMembers(members);
				}else {
					tr.setMembers(new String[0]);
				}
				return tr;
			}
			
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	@Override
	public SearchResultEntry getUniqueWorkGroupEntry(String groupName) {
		
		try {
			// 查询所有工作组
			Filter objectClassFilter = Filter.createEqualityFilter("objectClass", "groupOfNames");
			Filter groupTypeFilter = Filter.createEqualityFilter("GroupType", "r"); // 根据类型过滤
			Filter groupNameFilter = Filter.createEqualityFilter("cn", groupName);
			
			SearchRequest request = new SearchRequest(this.ldapConf.getBn(), SearchScope.SUB, Filter.createANDFilter(new Filter[] { objectClassFilter, groupTypeFilter, groupNameFilter }));

			LDAPConnection conn = this.getCurrLdapConnection();
			SearchResult result = conn.search(request);
			List<SearchResultEntry> list = result.getSearchEntries();

			if(list != null && !list.isEmpty()){
				SearchResultEntry sre = list.get(0);
				
				return sre;
				
			}
			
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	@Override
	public String[] getWorkGroupMembers(SearchResultEntry entry) {
		
		return entry.getAttributeValues("member");
	}
	
	@Override
	public Map<String, LdapPerson> searchLdapPersonsMap() {
		
		Map<String, LdapPerson> entries = new HashMap<String, LdapPerson>();
		try {
			List<SearchResultEntry> results = this.searchCurrUsers();
			if (null == results || 0 == results.size()) {
				return entries;
			}
			for (SearchResultEntry sre : results) {
				LdapPerson person = new LdapPerson();
				person.setDn(sre.getDN().toLowerCase()); // 统一小写
				person.setRdn(sre.getRDN().toNormalizedString());
				person.setPdn(sre.getParentDNString());
				person.setCname(sre.getDN().split(",")[0].split("=")[1]);//.getRDN().toNormalizedString().split("=")[1]);
				person.setUserPassword(sre.getAttributeValue("userPassword"));
				person.setSex(sre.getAttributeValue("sex"));
				person.setAliasName(sre.getAttributeValue("aliasName"));

				if(StringUtil.isNullOrEmpty(person.getAliasName())){
					logger.info("用户[{}] 没有设置别名，已经过滤，请检查LDAP完整性。", person.getCname());
					continue;
				}
				entries.put(person.getDn(), person);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entries;
		
	}
	/**
	 * 检索子级目录，包括本身
	 * @param baseDN
	 * @param objectClass
	 * @return
	 * @throws Exception
	 */
	private List<SearchResultEntry> searchEntry(String baseDN, String objectClass) throws Exception {
		
		Filter objectClassFilter = Filter.createEqualityFilter("objectClass", objectClass);

		SearchRequest request = new SearchRequest(baseDN, SearchScope.SUB, Filter.createANDFilter(new Filter[] { objectClassFilter}));

		LDAPConnection conn = this.getCurrLdapConnection();
		SearchResult result = conn.search(request);
		List<SearchResultEntry> list = result.getSearchEntries();
		
		return list;
	}
	
	/**
	 * 只检索一级目录
	 * @param baseDN
	 * @param objectClass
	 * @return
	 * @throws Exception
	 */
    private List<SearchResultEntry> searchEntryOneLevel(String baseDN, String objectClass) throws Exception {
		
		Filter objectClassFilter = Filter.createEqualityFilter("objectClass", objectClass);

		SearchRequest request = new SearchRequest(baseDN, SearchScope.ONE, Filter.createANDFilter(new Filter[] { objectClassFilter}));

		LDAPConnection conn = this.getCurrLdapConnection();
		SearchResult result = conn.search(request);
		List<SearchResultEntry> list = result.getSearchEntries();
		
		return list;
	}
    /**
	 * 只检索一级目录
	 * @param baseDN
	 * @param objectClass
	 * @param groupType 组类型，d：department（部门）；i：institute（院）
	 * @return
	 * @throws Exception
	 */
    private List<SearchResultEntry> searchEntryOneLevel(String baseDN, String objectClass, String groupType) throws Exception {
		
		Filter objectClassFilter = Filter.createEqualityFilter("objectClass", objectClass);
		Filter groupTypeFilter = Filter.createEqualityFilter("GroupType", groupType);

		SearchRequest request = new SearchRequest(baseDN, SearchScope.ONE, Filter.createANDFilter(new Filter[] { objectClassFilter, groupTypeFilter}));

		LDAPConnection conn = this.getCurrLdapConnection();
		SearchResult result = conn.search(request);
		List<SearchResultEntry> list = result.getSearchEntries();
		
		return list;
	}
	@Override
	public List<LdapOrg> searchInstitutes() {
		
		List<LdapOrg> data = new ArrayList<LdapOrg>();

		try {
			List<SearchResultEntry> list = searchEntry(this.ldapConf.getBn(), "organization");

			if(list != null && !list.isEmpty()){
				
				
				for(SearchResultEntry sre : list){
					// 设定ldap信息都是完整的
					LdapOrg org = new LdapOrg();
					org.setName(sre.getAttributeValue("o"));
					org.setAlias(sre.getAttributeValue("displayName"));
					org.setUid(sre.getAttributeValue("uid"));
					org.setDn(sre.getDN().toLowerCase());
					logger.info("ldap院机构属性，name：[{}], alias : [{}], uid : [{}]", org.getName(), org.getAlias(), org.getUid());
					
					data.add(org);
				}	
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return data;
	}
	
	@Override
	public List<LdapOrg> searchDepartments(String baseDN) {
		
		List<LdapOrg> data = new ArrayList<LdapOrg>();
		
		try {
			List<SearchResultEntry> list = searchEntryOneLevel(baseDN, "organizationalUnit");

			if(list != null && !list.isEmpty()){
				
				for(SearchResultEntry sre : list){
					
					// 忽视本身
					if(sre.getDN().toLowerCase().equals(baseDN)) continue;
					
					// 设定ldap信息都是完整的
					LdapOrg org = new LdapOrg();
					org.setName(sre.getAttributeValue("ou"));
					org.setAlias(sre.getAttributeValue("displayName"));
					org.setUid(sre.getAttributeValue("uid"));
					org.setDn(sre.getDN().toLowerCase());
					logger.info("ldap所机构属性，name：[{}], alias : [{}], uid : [{}]", org.getName(), org.getAlias(), org.getUid());
					
					data.add(org);
				}	
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return data;
		
	}
	@Override
	public List<LdapOrg> searchDepartmentsDefinedByGroupofname(String baseDN) {
		
        List<LdapOrg> data = new ArrayList<LdapOrg>();
		
		try {
			List<SearchResultEntry> list = searchEntryOneLevel(baseDN, "groupOfNames", "d");

			if(list != null && !list.isEmpty()){
				
				for(SearchResultEntry sre : list){
					
					// 忽视本身
					if(sre.getDN().toLowerCase().equals(baseDN)) continue;
					
					// 设定ldap信息都是完整的
					LdapOrg org = new LdapOrg();
					org.setName(sre.getAttributeValue("cn"));
					org.setAlias(sre.getAttributeValue("displayName"));
					org.setUid(sre.getAttributeValue("uid"));
					org.setDn(sre.getDN().toLowerCase());
					org.setMembers(sre.getAttributeValues("member"));
				
					logger.info("ldap所机构属性，name：[{}], alias : [{}], uid : [{}]", org.getName(), org.getAlias(), org.getUid());
					
					data.add(org);
				}	
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return data;
		
	}
	@Override
	public Map<String, LdapOrg> searchDepartmentsDefinedByGroupofnameCnMap(String baseDN) {
	
		 Map<String, LdapOrg> data = new HashMap<String, LdapOrg>();
			
			try {
				List<SearchResultEntry> list = searchEntryOneLevel(baseDN, "groupOfNames", "d");

				if(list != null && !list.isEmpty()){
					
					for(SearchResultEntry sre : list){
						
						// 忽视本身
						if(sre.getDN().toLowerCase().equals(baseDN)) continue;
						
						// 设定ldap信息都是完整的
						LdapOrg org = new LdapOrg();
						org.setName(sre.getAttributeValue("cn"));
						org.setAlias(sre.getAttributeValue("displayName"));
						org.setUid(sre.getAttributeValue("uid"));
						org.setDn(sre.getDN().toLowerCase());
						org.setMembers(sre.getAttributeValues("member"));
					
						logger.info("ldap所机构属性，name：[{}], alias : [{}], uid : [{}]", org.getName(), org.getAlias(), org.getUid());
						
						data.put(org.getName(), org);
					}	
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			return data;
	}
	@Override
	public List<LdapPerson> searchAllPersons() {
		
		List<LdapPerson> data = new ArrayList<LdapPerson>();

		try {
			List<SearchResultEntry> list = searchEntry(this.ldapConf.getBn(), "person");

			if (list != null && !list.isEmpty()) {

				for (SearchResultEntry sre : list) {
					// 设定ldap信息都是完整的
					LdapPerson per = searchEntryToLdapPerson(sre);

					data.add(per);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return data;
		
	}
	@Override
    public List<LdapPerson> searchAllPersons(String bn) {
		
		List<LdapPerson> data = new ArrayList<LdapPerson>();

		try {
			List<SearchResultEntry> list = searchEntry(bn, "person");

			if (list != null && !list.isEmpty()) {

				for (SearchResultEntry sre : list) {
					// 设定ldap信息都是完整的
					LdapPerson per = searchEntryToLdapPerson(sre);

					data.add(per);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return data;
		
	}
	@Override
    public Map<String, LdapPerson> searchAllPersonsCnMap(String bn) {
		
		Map<String, LdapPerson> data = new HashMap<String, LdapPerson>();

		try {
			List<SearchResultEntry> list = searchEntry(bn, "person");

			if (list != null && !list.isEmpty()) {

				for (SearchResultEntry sre : list) {
					
					String cn = sre.getAttributeValue("cn").toLowerCase();
					if(data.containsKey(cn)) continue;
					
					// 设定ldap信息都是完整的
					LdapPerson per = searchEntryToLdapPerson(sre);

					data.put(cn, per);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return data;
		
	}
	

	/**
	 * 检索条目转换成DTO-LdapPerson，关于ldap中各种路径统一使用小写格式，如：dn的值
	 * @param sre
	 * @return
	 * @throws LDAPException
	 */
	private LdapPerson searchEntryToLdapPerson(SearchResultEntry sre) throws LDAPException {
		
		LdapPerson per = new LdapPerson();
		per.setAliasName(sre.getAttributeValue("displayName"));
		per.setCname(sre.getAttributeValue("cn").toLowerCase());
		per.setDn(sre.getDN().toLowerCase());
		per.setPdn(sre.getParentDNString().toLowerCase());
		per.setRdn(sre.getRDN().toString().toLowerCase());
		per.setSex(sre.getAttributeValue("sex"));
		per.setUserPassword(sre.getAttributeValue("userPassword"));
		per.setUid(sre.getAttributeValue("uid"));

		logger.info("ldap人员属性，cname：[{}], alias : [{}], uid : [{}]", per.getCname(), per.getAliasName(),
				per.getUid());
		
		return per;
		
	}
	@Override
	public List<LdapPerson> searchPersons(String baseDN) {

		List<LdapPerson> data = new ArrayList<LdapPerson>();

		try {
			List<SearchResultEntry> list = searchEntry(baseDN, "inetOrgPerson");

			if (list != null && !list.isEmpty()) {

				for (SearchResultEntry sre : list) {
					LdapPerson per = searchEntryToLdapPerson(sre);

					data.add(per);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return data;
	}
	@Override
	public LdapPerson searchUniquePerson(String baseDN, String cnValue) {
		
		try {
			Filter objectClassFilter = Filter.createEqualityFilter("objectClass", "person");
			Filter cnFilter = Filter.createEqualityFilter("cn", cnValue);
			
			SearchResultEntry entry = this.searchUniqueByFilter(this.getCurrLdapConnection(), Filter.createANDFilter(new Filter[]{objectClassFilter, cnFilter}), baseDN);
			if(null == entry) return null;
			
			return searchEntryToLdapPerson(entry);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	//@Override
	private Entry generateOrganizationEntry(String parentDN, String name, String alias, String uid, String[] memberDNs) {
		
		   List<Attribute> attrList = new LinkedList<Attribute>();
	       attrList.add(new Attribute("o", DistinguishedNameMatchingRule.getInstance(), name));
	       attrList.add(new Attribute("displayName", DistinguishedNameMatchingRule.getInstance(), alias));
	       attrList.add(new Attribute("uid", DistinguishedNameMatchingRule.getInstance(), uid));
	       attrList.add(new Attribute("member", DistinguishedNameMatchingRule.getInstance(), memberDNs));
	       
	       return generateEntry("o", name, parentDN, new String[]{"top", "organization"}, attrList);
	}
	@Deprecated
	//@Override
	private Entry generateOrganizationUnitEntry(String parentDN, String name, String alias, String uid) {
		
		   List<Attribute> attrList = new LinkedList<Attribute>();
	       attrList.add(new Attribute("o", DistinguishedNameMatchingRule.getInstance(), name));
	       attrList.add(new Attribute("displayName", DistinguishedNameMatchingRule.getInstance(), alias));
	       attrList.add(new Attribute("uid", DistinguishedNameMatchingRule.getInstance(), uid));
		
	       return generateEntry("o", name, parentDN, new String[]{"top", "organizationalUnit"}, attrList);   
	}

	//@Override
	private Entry generateInetOrgPersonEntry(String name, String lastName, String password, String parentDN, Collection<Attribute> additionalAttributes) {
		
		   ArrayList<Attribute> attrList = new ArrayList<Attribute>();
	        attrList.add(new Attribute("sn", lastName));
	        attrList.add(new Attribute("userPassword", password));

	        if (additionalAttributes != null) {
	            attrList.addAll(additionalAttributes);
	        }

	        String[] objectClasses = new String[]{"top", "person", "organizationalPerson", "inetOrgPerson"};
	        return generateEntry("cn", name, parentDN, objectClasses, attrList);
	        
	}
	
	@Override
	public boolean isExistInetOrgPerson(String orgDn, String personCnValue) {
		
        try {
        	LDAPConnection conn = this.getCurrLdapConnection();
    		
			Entry entry = conn.getEntry("cn="+personCnValue+","+orgDn);
			
			return null != entry;
			
		} catch (LDAPException e) {
			logger.error(e.getMessage());
		}
        
        return false;
		// 方法需要优化
		// return null != this.searchUniquePerson(orgDn, personCnValue);
	}
	
	@Override
	public boolean addInetOrgPersonToGroup(String groupDn, List<String> memberDns) {

		Assert.notEmpty(memberDns);

		try {
			LDAPConnection conn = this.getCurrLdapConnection();
			Entry entry = conn.getEntry(groupDn);
			if (null == entry)
				return false;

			List<Modification> modifications = new ArrayList<Modification>();
			for (String mem : memberDns) {
				Modification modification = new Modification(ModificationType.ADD, "member", mem);
				modifications.add(modification);
			}

			ModifyRequest request = new ModifyRequest(entry.getDN(), modifications);
			LDAPResult result = conn.modify(request);
			return result.getResultCode() == ResultCode.SUCCESS;

		} catch (Exception ex) {
			logger.error(ex.getMessage());
			
		}
		return false;
		// return operateGroup(this.getCurrLdapConnection(), groupDn, memberDns,
		// ModificationType.ADD);

	}
	
	@Override
	public boolean addInetOrgPersonToGroup(String groupDn, String[] memberDns) {
		
		return this.addInetOrgPersonToGroup(groupDn, Arrays.asList(memberDns));
	}
	
	@Override
	public boolean isExistGroupOfname(String groupRootDn, String groupCnValue) {
		
		Entry entry = null;
		try {
			entry = this.getCurrLdapConnection().getEntry("cn="+groupCnValue+","+groupRootDn);

			
		} catch (LDAPException e) {
		
			logger.error(e.getMessage());
		}
		return null != entry;
	}
	
	public static void main(String[] args) throws Exception {
		
		// System.out.println(new String(new Base64().encode("数慧".getBytes("utf-8"))));
		// System.out.println(StringUtil.utf8Encode("数慧").replace("%", "\\"));
		
		
		 LDAPConnection conn = null;
	        try {
	      
	        	conn = new LDAPConnection("192.168.200.25", 389, "cn=root", "tdsadmin");
	            Entry entry = conn.getEntry("cn=gzpi,o=gzpi,DC=DIST");
	            // 给组的member添加组成员
	            Modification modification = new Modification(ModificationType.ADD, "member", "cn=机构2,cn=gzpi,o=gzpi,DC=DIST");
	            List<Modification> modifications = new ArrayList<Modification>();
	            modifications.add(modification);
	            ModifyRequest request = new ModifyRequest(entry.getDN(), modifications);
	            LDAPResult result = conn.modify(request);
	            if (result.getResultCode() == ResultCode.SUCCESS) {
	                System.out.println("修改成功");
	            }
	            
	            // 给组的member添加用户
	            entry = conn.getEntry("cn=机构2,cn=gzpi,o=gzpi,DC=DIST");
	            modification = new Modification(ModificationType.ADD, "member", "cn=gzpi_weifj,o=gzpi,DC=DIST");
	            modifications.clear();
	            
	            modifications.add(modification);
	            request = new ModifyRequest(entry.getDN(), modifications);
	            result = conn.modify(request);
	            if (result.getResultCode() == ResultCode.SUCCESS) {
	                System.out.println("修改成功");
	            }
	            
	        } catch (LDAPException e) {
	        	e.printStackTrace();
	        
	            throw new BusinessException("连接ldap失败");
	        }
	      
	}
}
