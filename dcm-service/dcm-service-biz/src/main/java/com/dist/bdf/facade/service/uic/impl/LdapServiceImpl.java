package com.dist.bdf.facade.service.uic.impl;

import com.dist.bdf.common.conf.common.LdapConf;
import com.dist.bdf.facade.service.LdapService;
import com.dist.bdf.facade.service.uic.domain.LdapDmn;
import com.dist.bdf.model.dto.ldap.LdapPerson;
import com.dist.bdf.model.dto.ldap.LdapTree;
import com.dist.bdf.model.entity.system.DcmUser;
import com.unboundid.ldap.sdk.AddRequest;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by kivi on 14/12/9.
 * @version 1.0，2016/03/10，weifj
 *    1. 添加方法alterUserName(String loginName, String newName)具体实现
 */
@Service("LdapService")
public class LdapServiceImpl implements LdapService {
	
    private static Logger logger = LoggerFactory.getLogger(LdapServiceImpl.class);

    @Autowired
    private LdapConf ldapConf;
    @Autowired
    private LdapDmn ldapDmn;

/*    private String ldapUrl;//ldap服务器地址
    private int ldapPort;//ldap服务器端口
    private String bindDn;//连接ldap服务器的账号
    private String password;//连接ldap服务器的密码
    private String parentDn;//要添加entry的父dn
*/
	
    public LdapServiceImpl() {
/*        this.ldapUrl = ldapConf.getUrl();
        this.ldapPort = ldapConf.getPort();
        this.bindDn = ldapConf.getBindDn();
        this.password = ldapConf.getPassword();
        this.parentDn = ldapConf.getBn();
      */
    }

    @Override
    public String getDistinguishedNameOfOrg(String cnValue) {
    	
    	try {
			SearchResultEntry entry = this.ldapDmn.searchUniqueByFilter(getLdapConnection(), Filter.create(String.format("cn=%s",cnValue)));
			
			if(entry != null) return entry.getDN();
			
		} catch (LDAPException e) {
			
			e.printStackTrace();
		}
    	return "";
    	
    }
    /**
     * 获取ldap连接
     *
     * @return
     */
    private LDAPConnection getLdapConnection() {
        return ldapDmn.getLdapConnection(this.ldapConf.getUrl(), this.ldapConf.getPort(), this.ldapConf.getBindDn(), this.ldapConf.getPassword());
    }

    /**
     * 添加ldap用户
     *
     * @param user
     * @param password
     * @return
     */
    @Override
    public boolean addUser(String loginName,String name, String password) {
        LDAPConnection conn = getLdapConnection();
        AddRequest addRequest = new AddRequest(ldapDmn.generateUserEntry(loginName, this.ldapConf.getBn(), name, password));
        return ldapDmn.add(conn, addRequest);
    }


    /**
     * 添加ldap组
     *
     * @param groupName
     * @return
     */
    @Override
    public boolean addGroup(String groupName, String... memberDNs) {
        LDAPConnection conn = getLdapConnection();
        if (memberDNs.length == 0) {
            memberDNs = new String[]{""};
        }
        AddRequest addRequest = new AddRequest(ldapDmn.generateGroupOfNamesEntry(groupName, this.ldapConf.getBn(), memberDNs));
        return ldapDmn.add(conn, addRequest);
    }


    /**
     * 删除ldap中的用户或组
     *
     * @param loginName
     * @return
     */
    @Override
    public boolean delete(String loginName) {
        String distinguishName = "cn=" + loginName + "," + this.ldapConf.getBn();
        LDAPConnection conn = getLdapConnection();
        return ldapDmn.delete(conn, distinguishName);
    }

    /**
     * 批量删除ldap中的用户或组
     *
     * @param ids
     * @return
     */
    @Override
    public boolean batchDelete(String[] ids) {
        for (String id : ids) {
            String distinguishName =this.getDistinguishedNameOfOrg(id);// "cn=" + userId + "," + parentDn;
            LDAPConnection conn = getLdapConnection();
            ldapDmn.delete(conn, distinguishName);
        }
        return true;
    }


    @Override
    public boolean alterUserPassowrd(String loginName, String newPassword) {
        String distinguishName = this.getDistinguishedNameOfOrg(loginName);//"cn=" + loginName + "," + parentDn;
        LDAPConnection conn = getLdapConnection();
        return ldapDmn.alterUserPassowrd(conn, distinguishName, newPassword);
    }
    
    @Override
    public boolean alterUserName(String loginName, String newName){
    	   String distinguishName = this.getDistinguishedNameOfOrg(loginName);//"cn=" + loginName + "," + parentDn;
           LDAPConnection conn = getLdapConnection();
           return ldapDmn.alterUserName(conn, distinguishName, newName);
    }

    @Override
    public void batchAddUser(List<DcmUser> userList) {
        LDAPConnection conn = getLdapConnection();
        if (userList != null && userList.size() > 0) {
            for (DcmUser user : userList) {
                String name = user.getLoginName();
                String firstName = user.getUserName();
                String password = "";
                AddRequest request = new AddRequest(ldapDmn.generateUserEntry(name, this.ldapConf.getBn(), firstName, password));
                try {
                    ldapDmn.add(conn, request);
                } catch (Exception e) {
                    String msg = "添加用户" + name + "失败";
                    logger.info(msg, e);
                }
            }
        }
    }

    @Override
    public boolean addUserToGroup(String groupName, String[] userNames) {
    	
        if (userNames == null || userNames.length == 0) {
            return false;
        } else {
        	 LDAPConnection conn = getLdapConnection();
            groupName = "cn=" + groupName + "," + this.ldapConf.getBn();
            List<String> userDns = new ArrayList<String>();
            for (int i = 0; i < userNames.length; i++) {
            	String groupDistinguishName = this.getDistinguishedNameOfOrg(groupName);
   
				try {
					boolean isExist = this.ldapDmn.isExistMemberOfGroup(conn, groupDistinguishName, userNames[i]);
					if(!isExist) {
	            		  userDns.add(String.format("cn=%s,%s", userNames[i], this.ldapConf.getBn()));
	            	}
				} catch (LDAPException e) {
					e.printStackTrace();
				}
            }
           
            return ldapDmn.addUserToGroup(conn, groupName, userDns);
        }
    }

    @Override
    public boolean removeUserFromGroup(String groupName, String[] userNames) {
        if (userNames == null || userNames.length == 0) {
            return false;
        } else {
            groupName = "cn=" + groupName + "," + this.ldapConf.getBn();
            String[] userDns = new String[userNames.length];
            for (int i = 0; i < userNames.length; i++) {
                userDns[i] = "cn=" + userNames[i] + "," + this.ldapConf.getBn();
            }
            LDAPConnection conn = getLdapConnection();
            return ldapDmn.removeUsersFromGroup(conn, groupName, userDns);
        }
    }
    
    @Override
    public List<LdapPerson> getAllPersons(){
    	
    	List<LdapPerson> entries = new ArrayList<LdapPerson>();
    	try {
			List<SearchResultEntry> results = this.ldapDmn.searchUsers(getLdapConnection(), this.ldapConf.getBn());
			if(null == results || 0 == results.size()){
				return entries;
			}
			for(SearchResultEntry sre : results){
				LdapPerson person = new LdapPerson();
				person.setDn(sre.getDN());
				person.setRdn(sre.getRDN().toNormalizedString());
				person.setPdn(sre.getParentDNString());
				person.setCname(sre.getRDN().toNormalizedString().split("=")[1]);
			
				entries.add(person);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return entries;
    }
    
    public static void main(String[] args) {

		try {
			// 测试ldap检索接口
			String url = "192.168.200.25";
			int port = 389;
			String userName = "cn=root";
			String pwd = "tdsadmin";
			String bn = "dc=dist";
			LDAPConnection conn = new LDAPConnection(url, port, userName, pwd);
			SearchResultEntry entry = conn.getEntry("dc=dist");
			
			/*   	 ;
			 	  String filter = "(&(cn={0}))";
			       String [] filterArgs = new String []
			       { "wangyl"};*/
			// 查询所有组
			Filter filter = Filter.createEqualityFilter("objectClass","groupOfNames");

			SearchRequest request = new SearchRequest(bn, SearchScope.SUB,
					filter);

			SearchResult result = conn.search(request);
			System.out.println(result);
			List<SearchResultEntry> list = result.getSearchEntries();

			Map<String, LdapTree> map = new LinkedHashMap<String, LdapTree>();

			for (SearchResultEntry sre : list) {
		        String dn = sre.getDN();
		        String[] strs = dn.split(",");
		        int max = strs.length-1;

		        for(int i=max;i>-1;i--){
		        	LdapTree tr = new LdapTree();
		        	tr.setCn(strs[i]);
		        	tr.setGuid(UUID.randomUUID().toString());
		        	if(i != max){

		        		tr.setParentTree(map.get(strs[i+1]));
		        	}
		        	if(!map.containsKey(tr.getCn())){
		        		map.put(tr.getCn(), tr);
		        	}
		        	Filter filter1 = (tr.getCn().toLowerCase().indexOf("dc")>-1)? Filter.createEqualityFilter("objectClass","domain") : Filter.createEqualityFilter("objectClass","groupOfNames");
				    Filter filter2 = (tr.getCn().toLowerCase().indexOf("dc")>-1)? Filter.createEqualityFilter("dc", tr.getCn().split("=")[1]):Filter.createEqualityFilter("cn", tr.getCn().split("=")[1]);
					SearchRequest request1 = new SearchRequest(bn, SearchScope.SUB,
							Filter.createANDFilter(new Filter[]{filter1, filter2}));

					SearchResult result1= conn.search(request1);
					SearchResultEntry sreSub = (result1.getSearchEntries().size()>0)? result1.getSearchEntries().get(0) : null;
					if(sreSub != null){
						String[] members = sreSub.getAttributeValues("member");
						if(members != null){
							System.out.println("组内成员dn分别是：");
							for(String mem : members){
								System.out.println(mem);
							}
						}
						tr.setMembers(members);
					}
					
		        	
		        }
		        
				System.out.println(dn);
	
			}
			System.out.println(map);
			for(String key : map.keySet()){
				System.out.println(key);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getDistinguishedName(String cnValue) {
		
		return this.ldapDmn.getDistinguishedName(cnValue);
	}
}

