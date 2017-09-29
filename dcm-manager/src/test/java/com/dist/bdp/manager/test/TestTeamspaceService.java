package com.dist.bdp.manager.test;

import com.dist.bdf.manager.ecm.security.SecurityUtil;
import com.dist.bdf.manager.ecm.teamspace.DistTeamSpaceUtil;
import com.dist.bdf.manager.ecm.teamspace.DistTeamspace;
import com.dist.bdf.manager.ecm.teamspace.p8.DistP8TeamspaceService;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.GroupSet;
import com.filenet.api.collection.UserSet;
import com.filenet.api.constants.AccessType;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectReference;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.meta.ClassDescription;
import com.filenet.api.property.Properties;
import com.filenet.api.property.Property;
//import com.filenet.api.core.Factory.Connection;
//import com.filenet.api.core.Factory.Domain;
//import com.filenet.api.core.Factory.ObjectStore;
// import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.security.AccessPermission;
import com.filenet.api.security.Group;
import com.filenet.api.security.User;
import com.filenet.api.util.UserContext;
import com.filenet.apiimpl.core.GlobalIdentity;
import com.filenet.apiimpl.core.Session;
import com.filenet.apiimpl.util.SessionLocator;
import com.ibm.ecm.configuration.ApplicationConfig;
import com.ibm.ecm.configuration.Config;
import com.ibm.ecm.configuration.RepositoryConfig;
import com.ibm.ecm.configuration.exception.MissingValueException;
import com.ibm.ecm.security.AccessControlList;
import com.ibm.ecm.security.Role;
import com.ibm.ecm.security.UserGroup;
import com.ibm.ecm.security.p8.P8AccessControlListData;
import com.ibm.ecm.serviceability.Logger;
import com.ibm.ecm.teamspace.Teamspace;
import com.ibm.ecm.teamspace.TeamspaceUtil;
import com.ibm.ecm.teamspace.p8.P8TeamspaceService;
import com.ibm.ecm.util.p8.P8Connection;
import com.ibm.ecm.util.p8.P8Permission;
import com.ibm.ecm.util.p8.P8Util;
import com.ibm.json.java.JSON;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

import com.ibm.ecm.security.SecurityConstants;
import com.ibm.ecm.security.SecurityConstants.PrincipalType;
import com.ibm.ecm.security.SecurityConstants.RoleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;
import javax.tools.DiagnosticCollector;

import org.apache.commons.configuration.ConfigurationException;

public class TestTeamspaceService {
	
	private static Connection ceConnection;
	private static Subject subject;
	private static Domain domain;
	private static ObjectStore objectStore;
	private static P8Connection p8conn;
	private static List<Role> roles;
	private static List<com.ibm.ecm.security.Subject> team;
	private static User currUser;
	
	 private static void init() throws Exception{
		 
		    String workspaceType = "instance";// template
		    String serverName = "http://192.168.1.237:9080/wsi/FNCEWS40MTOM/";//"http://58.246.138.178:9080/wsi/FNCEWS40MTOM/";
	    	// ecm接口中参数如果是serverName，则代表的就是：http://58.246.138.178:9080/wsi/FNCEWS40MTOM/
	    	ceConnection = Factory.Connection.getConnection(serverName);
	    	 UserContext uc = UserContext.get();
	    	// 协议（protocol）：FileNetP8WSI
	    	subject = UserContext.createSubject(ceConnection, "weifj", "pass", "FileNetP8WSI");
	    	uc.pushSubject(subject);// 这是上下文安全性设置，否则出现安全性异常
	    	domain = Factory.Domain.getInstance(ceConnection, "DCMTest");//当目前只有一个域的时候，可以不填name的值
	    	//Factory.Domain.fetchInstance(ceConn, null, P8Connection.domainOSIdAndNamesOnlyFilter);
			System.out.println("Obtained the DCMTest Domain.");
			
			objectStore = Factory.ObjectStore.fetchInstance(domain, "NewOS", null);
			System.out.println("Obtained realm："+"NewOS");
			
			Integer accessRights = objectStore.getAccessAllowed();
			currUser = Factory.User.fetchCurrent(ceConnection, null);
			String distinguishedName = currUser.get_DistinguishedName();
			String userid = currUser.get_ShortName();
			// 应用服务器类型
			 String appServerType = "websphere";
		/*	p8conn = new P8Connection("ceadmin", "cn=ceadmin,dc=dist", ceConnection, subject, domain, "NewOS", "ceadmin",
					"ceadmin", appServerType , false, null);*///repositoryId: icmnewos
           
			 //String applicationId = "navigator";
			 //String repositoryId = "icmnewos";
			 //ApplicationConfig appConfig = Config.getApplicationConfig("applicationId");
			 //RepositoryConfig repository = (RepositoryConfig)Config.getConfiguration(RepositoryConfig.class, appConfig.getObjectId(), repositoryId, new boolean[0]);
			 //Config.getRepositoryConfig(null);
           
			p8conn = new P8Connection(userid, distinguishedName, ceConnection, subject, domain, objectStore, currUser.get_Name(), currUser.get_DisplayName(), "" , serverName, false, null);
			p8conn.setPermissions(accessRights.intValue());

	    }
	    private static void end(){
	    	
			UserContext.get().popSubject();
			ceConnection = null;
			subject = null;
			domain = null;
			objectStore = null;
			p8conn = null;
	    }
	    
	public static void main(String[] args) {

		try {

			 System.out.println(PrincipalType.USER.name());//  value：0，name：USER
			 
			 System.out.println(PrincipalType.GROUP.name());// value：1，name：GROUP
			 
			init();
			DistP8TeamspaceService distTs = getDistP8TSService();
			
			User currUser = Factory.User.fetchCurrent(ceConnection, null);
			System.out.println("当前用户dn："+currUser.get_DistinguishedName());
			/*User findUser =  Factory.User.fetchInstance(conn, "weifj",null); 
			System.out.println("查询到用户："+findUser.get_DistinguishedName());
			Group ug = Factory.Group.fetchInstance(conn, "xdata", null);
			System.out.println("查询到用户组："+ug.get_DistinguishedName());
			UserSet us = ug.get_Users();
			System.out.print("用户组："+ug.get_DistinguishedName()+" 下的成员：");
			Iterator it = us.iterator();*/
			/*while(it.hasNext()){
				User subUser = (User) it.next();
				System.out.print(subUser.get_DisplayName()+"\t");
			}*/
			// 某个用户能看到的teamspace模板
			// retreiveDistTeamspaces(distTs,"template");
			
			   // 根据id查询teamspace
			//Teamspace findTS = retreiveTeamspaceById(distTs, "{301D41CB-C17C-4F70-8060-A955C3C7F5B4}");//{7003AA53-0000-C538-A275-AE71AA5FA315}{06E145B1-0FCE-4F2E-BB49-4130072864EC}
			//System.out.println(findTS.getJson());
			
			// 查询指定名称模板
			Teamspace tsTemplate = retreiveDistTeamspaces(distTs, "template", "team");//"规划论坛"
			/*JSONObject obj = tsTemplate.exportToJSON();
			Object folders = obj.get("folders");
			JSONArray newObj = JSONArray.parse(folders.toString());
			System.out.println(newObj);*/
			
			//System.out.println("ts模板json数据："+tsTemplate.exportToJSON().toString());
			
			team = new ArrayList<com.ibm.ecm.security.Subject>();
			Map<String, Role> map = (DistTeamspace.convert(tsTemplate)).getTemplateRolesMapRefName();
			Role roleOwner = map.get("所有者");//map.get("会议组织者");
		    Role roleMember = map.get("成员");//map.get("参会者");
		    
			System.out.println("模拟团队成员：weifj，ceadmin");
			com.ibm.ecm.security.User user1 = SecurityUtil.getUserByShortName(distTs.getConnection(), "weifj");
			 
			 if(currUser.get_Name().equals(user1.getName())){// 特别重要，否则导致创建的人都没有权限删除teamspace
				 user1.setId("CURRENT_USER");
			 }
		    List<Role> rolesOfUser1 = new ArrayList<Role>();
		    rolesOfUser1.add(roleMember);
		    user1.setRoles(rolesOfUser1);
		   
			com.ibm.ecm.security.User user2 = SecurityUtil.getUserByShortName(distTs.getConnection(), "ceadmin");
			 
			 if(currUser.get_Name().equals(user2.getName())){// 特别重要，否则导致创建的人都没有权限删除teamspace
				 user2.setId("CURRENT_USER");
			 }
			
			 List<Role> rolesOfUser2 = new ArrayList<Role>();
			 rolesOfUser2.add(roleOwner);
			 user2.setRoles(rolesOfUser2);
			
			    
			team.add(user1);
			team.add(user2);
			
			roles = new ArrayList<Role>();
			roles.add(roleOwner);
			roles.add(roleMember);

			// 根据某个模板，创建teamspace
			if (tsTemplate != null) {
		
				String tsId = distTs.createTeamspace(tsTemplate, "test-0406-04", "集成测试", team);
				System.out.println("新创建的teamspace id："+tsId);
				//DistP8TeamspaceService newDistTs = getDistP8TSService();
				//Teamspace newTs = createTeamspaceInstance(distTs, tsTemplate,null,team);
				//createTeamspace(tsTemplate);
	/*			com.ibm.ecm.security.User u = newTs.getCurrentUser();
				
				if(u != null){
					System.out.println("用户名称："+u.getName());
					List<Role> roles = u.getRoles();
					if(roles != null && roles.size()>0){
						System.out.print("用户拥有的角色：\t");
						for(Role r : roles){
							System.out.print(r.getName()+"，\t");
						}
					}
				}*/
				
			}
			end();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	private static DistP8TeamspaceService getDistP8TSService() {
		
		return new DistP8TeamspaceService(p8conn.getCEConnection(), p8conn.getDomain(), p8conn.getObjectStore(), p8conn);
		
	}

	public static Teamspace createTeamspaceTemplate(P8TeamspaceService workspaceService) {
		String id = null;
		Teamspace workspace = null;
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name", "Michael");
			workspace = new Teamspace(null, "Michael", "bad workspace", "template", "ClbTeamspaceTemplate",
					jsonObject.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workspace;
	}

	public static Teamspace createTeamspaceInstance(P8TeamspaceService workspaceService) {
		String id = null;
		Teamspace workspace = null;
		try {
			String jsonTemplate = "{\"id\":\"{F7D825C3-47BB-4564-B1A4-D5FA7BA0F034}\",\"name\":\"i9_folderTest1\",\"type\":\"instance\",\"state\":\"published\",\"description\":\"i9_folderTest1\",\"templateName\":\"\",\"folders\":[{\"name\":\"i9_folderTest1\",\"path\":\"/\",\"type\":\"folder\"},{\"name\":\"test\",\"path\":\"/test\",\"type\":\"folder\",\"properties\":{\"folderName\":\"test\",\"docid\":\"\",\"criterias\":[{\"name\":\"FolderName\",\"values\":[\"test\"],\"dataType\":\"xs:string\",\"label\":\"Folder Name\"},{\"name\":\"FolderName\",\"values\":[\"test\"],\"dataType\":\"xs:string\",\"label\":\"Folder Name\"}],\"documentType\":\"Folder\"}}],\"contentClasses\":[],\"searchTemplates\":[],\"roles\":[]}";
			workspace = new Teamspace(null, "Michael_2", "mike folder test 2", "instance", "ClbTeamspace", jsonTemplate,
					null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workspace;
	}

	public static Teamspace createTeamspaceInstance(DistP8TeamspaceService workspaceService, String jsonTemplate) {
		String id = null;
		Teamspace workspace = null;
		try {
			// String jsonTemplate =
			// "{\"id\":\"{F7D825C3-47BB-4564-B1A4-D5FA7BA0F034}\",\"name\":\"i9_folderTest1\",\"type\":\"instance\",\"state\":\"published\",\"description\":\"i9_folderTest1\",\"templateName\":\"\",\"folders\":[{\"name\":\"i9_folderTest1\",\"path\":\"/\",\"type\":\"folder\"},{\"name\":\"test\",\"path\":\"/test\",\"type\":\"folder\",\"properties\":{\"folderName\":\"test\",\"docid\":\"\",\"criterias\":[{\"name\":\"FolderName\",\"values\":[\"test\"],\"dataType\":\"xs:string\",\"label\":\"Folder
			// Name\"},{\"name\":\"FolderName\",\"values\":[\"test\"],\"dataType\":\"xs:string\",\"label\":\"Folder
			// Name\"}],\"documentType\":\"Folder\"}}],\"contentClasses\":[],\"searchTemplates\":[],\"roles\":[]}";	
			//User user1 = new com.ibm.ecm.security.User(id, name, shortName, displayName, emailAddress, roles);
			workspace = new Teamspace(null, "加班dog001", "第一次测试服务端创建", "instance", "ClbTeamspace", jsonTemplate,
					null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workspace;
	}
	
	public static Teamspace createTeamspaceInstance(DistP8TeamspaceService workspaceService, Teamspace teamspaceTemplate, List<Role> roles, List<com.ibm.ecm.security.Subject> team) {
		String id = null;
		Teamspace workspace = null;
		try {
			// String jsonTemplate =
			// "{\"id\":\"{F7D825C3-47BB-4564-B1A4-D5FA7BA0F034}\",\"name\":\"i9_folderTest1\",\"type\":\"instance\",\"state\":\"published\",\"description\":\"i9_folderTest1\",\"templateName\":\"\",\"folders\":[{\"name\":\"i9_folderTest1\",\"path\":\"/\",\"type\":\"folder\"},{\"name\":\"test\",\"path\":\"/test\",\"type\":\"folder\",\"properties\":{\"folderName\":\"test\",\"docid\":\"\",\"criterias\":[{\"name\":\"FolderName\",\"values\":[\"test\"],\"dataType\":\"xs:string\",\"label\":\"Folder
			// Name\"},{\"name\":\"FolderName\",\"values\":[\"test\"],\"dataType\":\"xs:string\",\"label\":\"Folder
			// Name\"}],\"documentType\":\"Folder\"}}],\"contentClasses\":[],\"searchTemplates\":[],\"roles\":[]}";
		
			//User user1 = new com.ibm.ecm.security.User(id, name, shortName, displayName, emailAddress, roles);
			workspace = new Teamspace(null, "加班dog", "持续测试服务端创建", "instance", "ClbTeamspace", DistTeamspace.convert(teamspaceTemplate).exportToJSON().toString(),
					team);
			
			String tsInstanceId = workspaceService.createTeamspace(workspace, roles, null, null);
			System.out.println(tsInstanceId);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workspace;
	}

	public static Teamspace retreiveTeamspaces(P8TeamspaceService workspaceService, String type, boolean delete) {
		Teamspace workspaceObject = null;
		try {
			int i = 0;
			List workspaces = workspaceService.retrieveTeamspaces(type, "published", false);
			Iterator iterator = workspaces.iterator();
			while (iterator.hasNext()) {
				if ((delete) && (i == 0)) {
					Teamspace tmpTeamspace = (Teamspace) iterator.next();

					printTeamspaceProps(tmpTeamspace);
					workspaceService.deleteTeamspace(tmpTeamspace);
					break;
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workspaceObject;
	}

	public static Teamspace retreiveTeamspaces(P8TeamspaceService workspaceService, String type) {
		/* 108 */ Teamspace workspaceObject = null;
		try {
			/* 110 */ int i = 0;
			/* 111 */ List workspaces = workspaceService.retrieveTeamspaces(type, "published", false);
			/* 112 */ Iterator iterator = workspaces.iterator();
			/* 113 */ while (iterator.hasNext()) {
				System.out.println("-->查询teamspace，类型【" + type + "】 的条目：");
				while (iterator.hasNext()) {
					Teamspace tmpTeamspace = (Teamspace) iterator.next();
					System.out.println(
							"    teamspace name：" + tmpTeamspace.getName() + "，模板json数据：" + tmpTeamspace.getJson());
					
				}
				/* 121 */ i++;
			}
		} catch (Exception e) {
			/* 126 */ e.printStackTrace();
		}
		/* 128 */ return workspaceObject;
	}

	public static List retreiveDistTeamspaces(DistP8TeamspaceService workspaceService, String type) {
		/* 108 */ Teamspace workspaceObject = null;
		try {
			/* 110 */ int i = 0;
			/* 111 */ List workspaces = workspaceService.retrieveTeamspaces(type, "published", false);
			/* 112 */ Iterator iterator = workspaces.iterator();
			System.out.println("-->查询teamspace，类型【" + type + "】 的条目：");
			while (iterator.hasNext()) {
				Teamspace tmpTeamspace = (Teamspace) iterator.next();
				System.out.println(
						"    teamspace name：" + tmpTeamspace.getName() + "，模板json数据：" + tmpTeamspace.getJson());
			}
			return workspaces;
		} catch (Exception e) {
			/* 126 */ e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查找指定名称的对象（teamspace or teamspace template）
	 * 
	 * @param workspaceService
	 * @param type
	 * @param findName
	 * @return
	 */
	public static Teamspace retreiveDistTeamspaces(DistP8TeamspaceService workspaceService, String type,
			String findName) {
		 Teamspace workspaceObject = null;
		try {
			List<Teamspace> workspaces = workspaceService.retrieveTeamspaces(type, "published", false);
			Iterator<Teamspace> iterator = workspaces.iterator();
			System.out.println("-->查询teamspace，类型【" + type + "】 的条目：");
			while (iterator.hasNext()) {
				workspaceObject = (Teamspace) iterator.next();
				if (workspaceObject.getName().equalsIgnoreCase(findName)) {
					System.out.println("    找到目标，teamspace name：" + workspaceObject.getName() + "，模板json数据："
							+ workspaceObject.exportToJSON());
					JSONArray roles = (JSONArray) workspaceObject.exportToJSON().get("roles");
					System.out.println("模板的角色列表："+roles);
					/*for(int i=0; i<roles.size();i++){
						 List<String> privileges = new ArrayList<String>();
						 JSONObject roleProperties = (JSONObject) roles.get(i);	
						 JSONArray privs = (JSONArray) roleProperties.get("privileges");
						 System.out.println(privs.get(0));
					 }*/
					return workspaceObject;
				}

			}
			System.out.println("没有找到名称为【" + findName + "】的目标。");
			return null;
		} catch (Exception e) {
			/* 126 */ e.printStackTrace();
		}
		return null;
	}

	public static Teamspace retreiveTeamspaceByName(P8TeamspaceService workspaceService, Teamspace workspace) {
		/* 132 */ Teamspace workspaceObject = null;
		try {
			/* 134 */ workspaceObject = workspaceService.retrieveTeamspaceByName(workspace.getName(),
					workspace.getType(), false);
			/* 135 */ printTeamspaceProps(workspaceObject);
		} catch (Exception e) {
			/* 138 */ e.printStackTrace();
		}
		/* 140 */ return workspaceObject;
	}

	public static Teamspace retreiveTeamspaceById(P8TeamspaceService workspaceService, String id) {
		/* 144 */ Teamspace workspaceObject = null;
		try {
			/* 146 */ workspaceObject = workspaceService.retrieveTeamspace(id, null, "instance", false);
			/* 147 */ printTeamspaceProps(workspaceObject);
		} catch (Exception e) {
			/* 150 */ e.printStackTrace();
		}
		/* 152 */ return workspaceObject;
	}
	public static Teamspace retreiveTeamspaceById(DistP8TeamspaceService workspaceService, String id) {
		/* 144 */ Teamspace workspaceObject = null;
		try {
			/* 146 */ workspaceObject = workspaceService.retrieveTeamspace(id, "allData", "instance", false);
			/* 147 */ printTeamspaceProps(workspaceObject);
		} catch (Exception e) {
			/* 150 */ e.printStackTrace();
		}
		/* 152 */ return workspaceObject;
	}

	public static void printTeamspaceProps(Teamspace workspace) {
		/* 156 */ System.out.println("------------------------------");
		/* 157 */ System.out.println("Name: \t\t" + workspace.getName());
		/* 158 */ System.out.println("Type: \t\t" + workspace.getType());
		/* 159 */ System.out.println("State: \t\t" + workspace.getState());
		/* 160 */ System.out.println("Description:" + workspace.getDescription());
		/* 161 */ System.out.println("Id: \t\t" + workspace.getId());
		/* 162 */ System.out.println("json: \t\t" + workspace.getJson());
	}
	
	/**
	 * 添加团队空间
	 * @param tsTemplate 团队空间模板
	 */
	public static void createTeamspace(Teamspace tsTemplate){

		    //String serverName = request.getParameter("repositoryId");

		    String teamspaceType = "instance";
		    String className = "ClbTeamspace";
		    P8Connection connection = p8conn;
		    //Connection ceConnection = connection.getCEConnection();
		   /* boolean importTeamspace = false;
		    boolean retrieveFullTeamspaceObject = false;
		    int counter = 0;
		    int templatesSize = 0;*/
		    DistTeamspace teamspace = new DistTeamspace("ts-server-040602", "持续测试服务端创建", teamspaceType, className, DistTeamspace.convert(tsTemplate).exportTemplateToJSON().toString(),
					team);

		    String teamspaceJson =  teamspace.toInstanceJSON().toString();
		    //P8TeamspaceService teamspaceService = null;
		    DistP8TeamspaceService teamspaceService = null;
		    try
		    {

		      //ObjectStore objectStore = connection.getObjectStore();

		      JSONObject jsonObject = JSONObject.parse(teamspaceJson);
		      //JSONArray templatesArray = TeamspaceUtil.getJsonArray(null, teamspaceJson, false);
		      //templatesSize = templatesArray.size();
		     // String teamspaceState = (String)jsonObject.get("state");// "published"
		  	
		        //String teamspaceId = "ClbTeamspace,{61BD00DE-81A1-4DCC-9C2C-5C2945023A49},{7003AA53-0000-C538-A275-AE71AA5FA315}";//request.getParameter("workspaceId");
		        
		      /*  String teamspaceTemplateId = (String)jsonObject.get("id");

		        String name = (String)jsonObject.get("name");

		        String description = (String)jsonObject.get("desc");

		        String templateName = (String)jsonObject.get("templateName");*/

		        JSONArray rolesArray = (JSONArray)jsonObject.get("roles");
		        HashMap<String, Role> rolesMap = new HashMap<String, Role>();
		        for (int i = 0; i < rolesArray.size(); i++) {
		          JSONObject roleObj = (JSONObject)rolesArray.get(i);

		          String roleId = (String)roleObj.get("id");
		          String messageId = (String)roleObj.get("messageId");
		          String roleName = (String)roleObj.get("name");
		          String roleDesc = (String)roleObj.get("description");
		          boolean isOwner = ((Boolean)roleObj.get("owner")).booleanValue();
		          JSONArray privsArray = (JSONArray)roleObj.get("privileges");
		          List<String> privileges = new ArrayList<String>();
		          for (int j = 0; (privsArray != null) && (j < privsArray.size()); j++) {
		            String priv = (String)privsArray.get(j);
		            privileges.add(priv);
		          }

		          Role role = new Role(roleId, roleName, roleDesc, privileges);
		          role.setOwner(isOwner);
		          role.setRoleType(SecurityConstants.RoleType.TEAMSPACE);
		          role.setMessageId(messageId);
		          rolesMap.put(roleId, role);
		        }

		        List secObjAclData = new ArrayList();
		        secObjAclData.add(new P8AccessControlListData(null, connection.getUserId(), null, SecurityConstants.PrincipalType.USER, Integer.valueOf(Integer.parseInt("998903")), P8AccessControlListData.InheritableDepth.NONE, null, AccessType.ALLOW));
		        AccessControlList secObjACL = new AccessControlList("", secObjAclData);

		        List teamspaceAclData = new ArrayList();
		        teamspaceAclData.add(new P8AccessControlListData(null, connection.getUserId(), null, SecurityConstants.PrincipalType.USER, Integer.valueOf(Integer.parseInt("998903")), P8AccessControlListData.InheritableDepth.NONE, null, AccessType.ALLOW));
		        AccessControlList teamspaceObjACL = new AccessControlList("", teamspaceAclData);

		        JSONArray members = (JSONArray)jsonObject.get("members");
		        for (int i = 0; (members != null) && (i < members.size()); i++) {
		          JSONObject member = (JSONObject)members.get(i);
		          String principalId = (String)member.get("id");
		          String principalName = (String)member.get("name");
		          SecurityConstants.PrincipalType principalType = SecurityConstants.PrincipalType.valueOf(((String)member.get("type")).toUpperCase());
		          boolean isOwner = false;

		          ArrayList usrRoles = new ArrayList();
		          com.ibm.ecm.security.User usrObj = new com.ibm.ecm.security.User(principalId, principalName, "", "", "", usrRoles);

		          JSONArray memberRolesArray = (JSONArray)member.get("roles");
		          for (int j = 0; j < memberRolesArray.size(); j++) {
		            String roleId = (String)memberRolesArray.get(j);

		            Role role = (Role)rolesMap.get(roleId);

		            if ((!isOwner) && (role.isOwner())) {
		              isOwner = role.isOwner();
		            }
		            usrRoles.add(role);
		            HashMap assignees = role.getAssignees();
		            if (assignees == null) {
		              assignees = new HashMap();
		              role.setAssigness(assignees);
		            }

		            if ((!assignees.containsKey(principalId)) && (!assignees.containsKey(principalName))) {
		              if ((principalId.equals("CURRENT_USER")) || (principalType == SecurityConstants.PrincipalType.GROUP))
		                assignees.put(principalName, principalType);
		              else {
		                assignees.put(principalId, principalType);
		              }
		            }
		          }
		          if (principalId.equals("CURRENT_USER")) {
		            secObjAclData.add(new P8AccessControlListData(null, principalName, null, principalType, Integer.valueOf(Integer.parseInt("998903")), P8AccessControlListData.InheritableDepth.NONE, null, AccessType.ALLOW));
		            teamspaceAclData.add(new P8AccessControlListData(null, principalName, null, principalType, Integer.valueOf(Integer.parseInt("998903")), P8AccessControlListData.InheritableDepth.NONE, null, AccessType.ALLOW));
		          } else {
		            String accessName = principalId;
		            if (principalType == SecurityConstants.PrincipalType.GROUP) {
		              accessName = principalName;
		            }
		            List<String> userPrivs = usrObj.getMaxPrivileges();

		            if ((userPrivs.contains("addRemoveRoleParticipants")) || (userPrivs.contains("modifyRoles"))) {
		              secObjAclData.add(new P8AccessControlListData(null, accessName, null, principalType, Integer.valueOf(Integer.parseInt("998903")), P8AccessControlListData.InheritableDepth.NONE, null, AccessType.ALLOW));
		            }

		            Integer denyDeleteMask = (Integer)SecurityConstants.p8PrivilegeMap.get("deleteFolders");
		            teamspaceAclData.add(new P8AccessControlListData(null, accessName, null, principalType, denyDeleteMask, P8AccessControlListData.InheritableDepth.NONE, null, AccessType.DENY));

		            boolean hasEdit = false;
		            boolean hasPermissionUpdate = false;

		            for (String priv : userPrivs) {
		              if (priv.equals("addRemoveClassesOrEntryTemplates")) {
		                hasEdit = true;
		              } else if (priv.equals("addNewSearches")) {
		                hasEdit = true;
		              } else if (priv.equals("modifyRoles")) {
		                hasEdit = true;
		                hasPermissionUpdate = true;
		              } else if (priv.equals("addRemoveRoleParticipants")) {
		                hasEdit = true;
		                hasPermissionUpdate = true;
		                break;
		              }
		            }

		            if (hasPermissionUpdate) {
		              Integer grantMask = Integer.valueOf(((Integer)SecurityConstants.p8PrivilegeMap.get("modifyFolderProperties")).intValue() | ((Integer)SecurityConstants.p8PrivilegeMap.get("modifyFolderPermissions")).intValue());
		              teamspaceAclData.add(new P8AccessControlListData(null, accessName, null, principalType, grantMask, P8AccessControlListData.InheritableDepth.NONE, null, AccessType.ALLOW));
		            } else if (hasEdit) {
		              Integer grantMask = (Integer)SecurityConstants.p8PrivilegeMap.get("modifyFolderProperties");
		              teamspaceAclData.add(new P8AccessControlListData(null, accessName, null, principalType, grantMask, P8AccessControlListData.InheritableDepth.NONE, null, AccessType.ALLOW));
		            }
		          }
		        }

		        List<Role> roles = new ArrayList<Role>();
		        roles.addAll(rolesMap.values());

		        teamspaceService = getDistP8TSService();//new P8TeamspaceService(ceConnection, connection.getDomain(), objectStore, connection, request);
		       /* String className = "ClbTeamspaceTemplate";
		        if (teamspaceType.equals("instance")) {
		          className = "ClbTeamspace";
		        }*/
		        //DistTeamspace teamspaceNew = new DistTeamspace(name, description, teamspaceType, className, teamspaceJson, null);
		        //teamspace.setId(teamspaceId);
		        //teamspaceNew.setState(teamspaceState);
		        //teamspaceNew.setTemplateId(teamspaceTemplateId);

		        String teamspaceId = teamspaceService.createTeamspace(teamspace, roles, secObjACL, teamspaceObjACL);
		        System.out.println("新创建的teamspace id ："+teamspaceId);
		      
		    }
		    catch (Exception e) {
		     e.printStackTrace();
		    }

	}
	
	/*public void deleteTeamspace()
		    throws Exception
		  {
		    String methodName = "deleteTeamspace";

		    P8Connection connection = p8conn;
		    Connection ceConnection = connection.getCEConnection();
	
		    try
		    {
		      String serverName = request.getParameter("repositoryId");
		      Logger.logDebug(this, methodName, request, "serverName = " + serverName);
		      String teamspaceId = request.getParameter("workspaceId");
		      Logger.logDebug(this, methodName, request, "workspaceId = " + teamspaceId);

		      String refileFolderId = request.getParameter("refileFolderId");
		      Logger.logDebug(this, methodName, request, "refileFolderId = " + refileFolderId);

		      String workspaceType = request.getParameter("workspaceType");
		      Logger.logDebug(this, methodName, request, "workspaceType = " + workspaceType);

		      String optionStringValue = request.getParameter("option");
		      Logger.logDebug(this, methodName, request, "option = " + optionStringValue);

		      if (optionStringValue == null)
		        option = Option.OFFLINE;
		      else {
		        option = Option.getEnum(optionStringValue);
		      }

		      if (workspaceType == null) {
		        workspaceType = "instance";
		      }
		      ObjectStore objectStore = connection.getObjectStore();
		      P8TeamspaceService teamspaceService = new P8TeamspaceService(ceConnection, connection.getDomain(), objectStore, connection, request);

		      if (option == Option.OFFLINE) {
		        Logger.logDebug(this, methodName, request, "take teamspace offline");
		        teamspaceService.deleteTeamspace(teamspaceId, workspaceType);
		        mediator.addMessage("teamspace.delete.successful", new Object[0]);
		      } else if (option == Option.REFILE_ALL) {
		        Logger.logDebug(this, methodName, request, "refile teamspace");
		        teamspaceService.reFile(teamspaceId, refileFolderId);
		      }
		      else {
		        Logger.logDebug(this, methodName, request, "unsupported option");
		      }
		    }
		    catch (TeamspaceException we) {
		      Logger.logError(this, methodName, request, we);
		      mediator.addError("delete.none");
		    } catch (Exception e) {
		      logFDC(request, response, null, methodName);
		      Logger.logError(this, methodName, request, e);
		      mediator.addError("error.exception.general", new Object[] { e.getMessage() });
		    }
		    writeJSONMediator(request, response);
		    Logger.logExit(this, methodName, request);
		  }*/
}