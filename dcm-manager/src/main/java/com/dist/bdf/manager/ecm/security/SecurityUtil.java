package com.dist.bdf.manager.ecm.security;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dist.bdf.common.conf.ecm.ECMConf;
import com.filenet.api.collection.GroupSet;
import com.filenet.api.collection.UserSet;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Factory;
import com.filenet.api.util.UserContext;
import com.filenet.apiimpl.core.GroupImpl;
import com.filenet.apiimpl.core.UserImpl;
import com.ibm.ecm.security.SecurityConstants.PrincipalType;
import com.ibm.ecm.security.SecurityException;
import com.ibm.ecm.security.Subject;
import com.ibm.ecm.security.User;
import com.ibm.ecm.security.UserGroup;
/**
 * 安全工具类，目前为teamspace提供工具服务
 * @author weifj
 * @version 1.0，2016/04/02，weifj，创建安全工具类
 */

@Service
public class SecurityUtil {

	private static Logger logger = LoggerFactory.getLogger(SecurityUtil.class);
	@Autowired
	private ECMConf ecmConf;
	/**
	 * 根据用户的简单名称获取 @com.ibm.ecm.security.User 信息
	 * @param conn
	 * @param shortName
	 * @return
	 */
	public static User getUserByShortName(Connection conn, String shortName) {
		
		com.filenet.api.security.User findUser = Factory.User.fetchInstance(conn, shortName , null); 
		if(findUser != null){
			User targetUser = new User(findUser.get_Id(), findUser.get_Name(), findUser.get_ShortName(), findUser.get_DisplayName(), findUser.get_Email(), null);
		    return targetUser;
		}
		return null;
	}
	
	/**
	 * 根据用户组的简单名称获取 @com.ibm.ecm.security.Group 信息
	 * @param conn
	 * @param shortName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static UserGroup getGroupByShortName(Connection conn, String shortName) {
		
		com.filenet.api.security.Group findGroup = Factory.Group.fetchInstance(conn, shortName , null); 
		if(findGroup != null){
			List<Subject> users = new ArrayList<Subject>();
			System.out.print("用户组："+findGroup.get_DisplayName()+" 下的成员：");
			UserSet us = findGroup.get_Users();
			Iterator it = us.iterator();
			while(it.hasNext()){
				com.filenet.api.security.User subUser = (com.filenet.api.security.User) it.next();
				User u = SecurityUtil.getUserByShortName(conn, subUser.get_ShortName());
				if(u != null) users.add(u);		 
			}
			UserGroup targetGroup = new UserGroup(findGroup.get_Id(), findGroup.get_Name(), findGroup.get_ShortName(), findGroup.get_DisplayName(), users , null);
		    return targetGroup;
		}
		return null;
	}
	/**
	 * 根据PrincipalType，动态获取用户或者用户组信息
	 * @param conn
	 * @param shortName
	 * @param ptype
	 * @return
	 */
	public static Subject getSecurityObjByShortName(Connection conn, String shortName, PrincipalType ptype) {
		 
		if(PrincipalType.USER == ptype){
			return getUserByShortName(conn, shortName);
		} else if(PrincipalType.GROUP == ptype){
			return getGroupByShortName(conn, shortName);
		} 
		System.out.println("不存在PrincipalType="+ptype+"的信息");
		return null;
	}
	
	/**
	 * 根据PrincipalType名称，动态获取用户或者用户组信息
	 * @param conn
	 * @param shortName
	 * @param principalTypeName，USER/GROUP
	 * @return
	 */
	public static Subject getSecurityObjByShortName(Connection conn, String shortName, String principalTypeName) {
		 
		if(PrincipalType.USER.name().equalsIgnoreCase(principalTypeName)){
			return getUserByShortName(conn, shortName);
		} else if(PrincipalType.GROUP.name().equalsIgnoreCase(principalTypeName)){
			return getGroupByShortName(conn, shortName);
		} 
		System.out.println("不存在PrincipalType名称为："+principalTypeName+"的信息");
		return null;
	}
	
	/**
	 * 根据PrincipalType值，动态获取用户或者用户组信息
	 * @param conn
	 * @param shortName
	 * @param principalTypeValue，0（USER）/1（GROUP）
	 * @return
	 */
	public static Object getSecurityObjByShortName(Connection conn, String shortName, int principalTypeValue) {

		try {
			PrincipalType ptype = PrincipalType.getEnum(principalTypeValue);
			return getSecurityObjByShortName(conn, shortName, ptype);
			
		} catch (SecurityException e) {
			e.printStackTrace();
		}

		return null;
	}
	/**
	 * 验证用户信息
	 * @param userId
	 * @param password
	 * @return
	 */
	public com.filenet.api.security.User authenticateUser(String userId, String password) {
		try{
		Connection ceCnn = Factory.Connection.getConnection(ecmConf.getEcmServerSite());
		UserContext uc = UserContext.get();
		// 协议（protocol）：FileNetP8WSI
		javax.security.auth.Subject subject = UserContext.createSubject(ceCnn, userId, password, "FileNetP8WSI");
		uc.pushSubject(subject);// 这是上下文安全性设置，否则出现安全性异常
		
		com.filenet.api.security.User user = Factory.User.fetchCurrent(ceCnn, null);
		if(null == user){
			logger.error("用户 [{}] 验证失败。", userId);
			return user;
		}
		logger.error("用户 [{}] 验证通过。", userId);
		return user;
		}catch(Exception ex){
			logger.error("用户 [{}] 验证失败，详情：[{}]", userId, ex.getMessage());
			return null;
		}
	}
	
	public static void main(String[]args) {
		
		Connection ceCnn = Factory.Connection.getConnection("http://54.223.106.164:9080/wsi/FNCEWS40MTOM/");
		UserContext uc = UserContext.get();
		// 协议（protocol）：FileNetP8WSI
		javax.security.auth.Subject subject = UserContext.createSubject(ceCnn, "ceadmin", "passw0rd", "FileNetP8WSI");
		uc.pushSubject(subject);// 这是上下文安全性设置，否则出现安全性异常
		
		GroupImpl group = (GroupImpl) Factory.Group.fetchInstance(ceCnn, "cn=gzpi,o=gzpi,DC=XIAOZHI", null);

		if(group != null){
			System.out.println(">>>group : "+group.get_DistinguishedName());
			group.get_Users();
			
			UserSet us = group.get_Users();
			if(!us.isEmpty()){
				Iterator itUser = us.iterator();
				
				while(itUser.hasNext()){
					UserImpl u = (UserImpl) itUser.next();
					System.out.println(">>>user : "+u.get_DistinguishedName());
				}
			} else {
				System.out.println(">>>当前组没有找到用户......: ");
			}
			GroupSet set = group.get_MemberOfGroups();
			printlGroup(set);
	
		}
			
	}

	private static void printlGroup(GroupSet set) {
		if(!set.isEmpty()){
			Iterator itGroup = set.iterator();
			while(itGroup.hasNext()){
				GroupImpl gi = (GroupImpl) itGroup.next();
				System.out.println(">>>group : "+gi.get_DistinguishedName());
				UserSet us = gi.get_Users();
				if(!us.isEmpty()){
					Iterator itUser = us.iterator();
					while(itUser.hasNext()){
						UserImpl u = (UserImpl) itUser.next();
						System.out.println(">>>user : "+u.get_DistinguishedName());
					}
				}
				printlGroup(gi.get_MemberOfGroups());
			
			}
		} else {
			System.out.println(">>>当前组没有找到成员组（get_MemberOfGroups）......");
		}
	}
}
