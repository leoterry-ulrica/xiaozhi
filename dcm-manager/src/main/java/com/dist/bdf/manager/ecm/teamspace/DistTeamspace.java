package com.dist.bdf.manager.ecm.teamspace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.ecm.security.Role;
import com.ibm.ecm.security.Subject;
import com.ibm.ecm.teamspace.Teamspace;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class DistTeamspace extends Teamspace {

	public DistTeamspace(String name, String description, String type, String className,
			String properties, List<Subject> team) {
		super(null, name, description, type, className, properties, team);

	}
	public static DistTeamspace convert(Teamspace ts) {
		
		return new DistTeamspace(ts.getName(), ts.getDescription(), ts.getType(), ts.getClassName(), ts.getJson(), ts.getTeam());
	}
	/**
	 * 导出teamspace模板成json对象
	 */
	public JSONObject exportTemplateToJSON() {

		if(!this.getType().equalsIgnoreCase("template")) return null;
		
	    String dataModelVersion = "";
	    JSONObject jsonObj = new JSONObject();
	    jsonObj.put("className", super.getClassName());
	    jsonObj.put("id", super.getId());
	    jsonObj.put("state", super.getState());
	    jsonObj.put("name", super.getName());
	    jsonObj.put("desc", super.getDescription());
	    jsonObj.put("lastModified", super.getLastModified());
	    jsonObj.put("lastModifiedUser", super.getLastModifiedUser());

	    if (super.getLastModifiedUserInfo() != null) {
	      jsonObj.put("lastModifiedUserInfo", super.getLastModifiedUserInfo().toJSON());
	    }
	    jsonObj.put("type", super.getType());
	    jsonObj.put("templateId", super.getTemplateId());
	    jsonObj.put("defaultClass", super.getDefaultClass());
	    jsonObj.put("defaultEntryTemplate", super.getDefaultEntryTemplate());
	    jsonObj.put("usesClasses", super.getUsesClasses());
	    try {
	      if (super.getJson() != null) {
	        JSONObject jobject = JSONObject.parse(super.getJson());
	        jsonObj.put("roles", jobject.get("roles"));
	        jsonObj.put("members", null);
	        jsonObj.put("searchTemplates", jobject.get("searchTemplates"));
	        jsonObj.put("contentClasses", jobject.get("contentClasses"));
	        jsonObj.put("entryTemplates", jobject.get("entryTemplates"));
	        JSONArray folders =  JSONArray.parse(jobject.get("folders").toString());
	        if(folders != null && folders.size()>0){
	        	for(int i=0;i<folders.size();i++) {
	        		JSONObject subFolder = (JSONObject) folders.get(i);
	        		if(subFolder.containsKey("rowProperties")){
	        			JSONObject rowProperties = (JSONObject)subFolder.get("rowProperties");
	        			if(rowProperties.containsKey("_value")){
	        				JSONObject value = (JSONObject) rowProperties.get("_value");
	        				if(value.containsKey("properties")){
		        				JSONObject folderProperties = (JSONObject) value.get("properties");
		        				if(!String.valueOf(folderProperties).equalsIgnoreCase("null")){
		        					String propertiesTxt = folderProperties.toString();
				        			subFolder.put("properties", JSONObject.parse(propertiesTxt));
		        				}	
		        			}	
	        			}
	        			
	        			subFolder.remove("rowProperties");
	        		}
	        	}
	        }
	        jsonObj.put("folders",folders);
	        jsonObj.put("columnProperties", jobject.get("columnProperties"));
	        jsonObj.put("templateName", jobject.get("name"));
	        dataModelVersion = (String)jobject.get("dataModelVersion");

	        JSONObject permissionObj = new JSONObject();
	        permissionObj.put("accessMask", Integer.valueOf(984531));
	        permissionObj.put("roleName", null);
	        permissionObj.put("granteeType", Integer.valueOf(2000));
	        permissionObj.put("granteeName", "#CREATOR-OWNER");
	        permissionObj.put("inheritableDepth", Integer.valueOf(0));
	        permissionObj.put("accessType", Integer.valueOf(1));

	        JSONArray permissionArray = new JSONArray();
	        permissionArray.add(permissionObj);
	        jsonObj.put("permissions", permissionArray);

	        //Logger.logDebug(this, methodName, this.request, "json: " + this.json);
	      }
	    } catch (IOException e) {
	    	e.printStackTrace();
	      //Logger.logError(this, methodName, this.request, e);
	    }
	    if ((dataModelVersion != null) && (dataModelVersion.length() > 0)) {
	      jsonObj.put("dataModelVersion", dataModelVersion);
	    }
	    //Logger.logDebug(this, methodName, this.request, "Returning JSON:" + jsonObj.toString());

	    return jsonObj;
	  }
	/**
	 * teamspace实例提供给其他类使用（因为带有team）？？
	 */
	/* public JSONObject toJSON() {
		    String methodName = "toJSON";

		    JSONObject jsonObj = new JSONObject();
		    jsonObj.put("className", super.getClassName());
		    jsonObj.put("id", super.getId());
		    jsonObj.put("state", super.getState());
		    jsonObj.put("states", "offline");
		    jsonObj.put("name", super.getName());
		    jsonObj.put("desc", super.getDescription());
		    //Logger.logDebug(this, methodName, this.request, "name: " + this.name);
		    //Logger.logDebug(this, methodName, this.request, "desc: " + this.description);
		    jsonObj.put("lastModified", super.getLastModified());
		    jsonObj.put("lastModifiedUser", super.getLastModifiedUser());

		    if (super.getLastModifiedUserInfo() != null) {
		      jsonObj.put("lastModifiedUserInfo", super.getLastModifiedUserInfo().toJSON());
		    }
		    jsonObj.put("type", super.getType());
		    jsonObj.put("templateId", super.getTemplateId());
		    jsonObj.put("defaultClass", super.getDefaultClass());
		    jsonObj.put("defaultEntryTemplate", super.getDefaultEntryTemplate());
		    jsonObj.put("usesClasses", super.getUsesClasses());
		    jsonObj.put("syncEnabled", super.isSyncEnabled());
		    jsonObj.put("isFavorite", super.isFavorite());
		    try {
		      if (super.getJson() != null) {
		        JSONObject jobject = JSONObject.parse(super.getJson());
		        jsonObj.put("props", jobject);
		       // Logger.logDebug(this, methodName, this.request, "json props: " + this.json);
		        jsonObj.put("templateName", jobject.get("name"));
		      }
		    } catch (IOException e) {
		    	e.printStackTrace();
		     // Logger.logError(this, methodName, this.request, e);
		    }

		    JSONArray jsonArray = new JSONArray();
		    jsonObj.put("team", jsonArray);
		    if (super.getTeam() != null) {
		      List<Subject> team =	super.getTeam();
		      for (Subject member : team) {
		        JSONObject memberJson = null;
		        if ((member instanceof UserGroup))
		          memberJson = ((UserGroup)member).toJSON();
		        else {
		          memberJson = ((User)member).toJSON();
		        }
		        jsonArray.add(memberJson);
		      }
		    }
		    
		    JSONArray membersJsonArray = new JSONArray();
		    jsonObj.put("members", membersJsonArray);
		    if (super.getTeam() != null) {
		      List<Subject> team =	super.getTeam();
		      for (Subject member : team) {
		        JSONObject memberJson = new JSONObject();
		        List<Role> roles = null;
		        if ((member instanceof UserGroup)){
		        	UserGroup ug = ((UserGroup)member);
		            memberJson.put("type", ug.getType().name());
		            memberJson.put("name", ug.getShortName());
		            memberJson.put("id", ug.getId());
		            memberJson.put("displayName", ug.getDisplayName());
		        }
		        else {
		        	User user = ((User)member);
		        }
		        memberJson.put("type", member.getType().name());
	            memberJson.put("name", member.getShortName());
	            memberJson.put("id", member.getId());
	            memberJson.put("displayName", member.getDisplayName());
		        JSONArray rolesJsonArray = new JSONArray();
	            roles = member.getRoles();
	            if(roles != null && roles.size()>0){
	            	for(Role r : roles){
	            		rolesJsonArray.add(r.getId());
	            	}
	            }
	            memberJson.put("roles", rolesJsonArray);
		        membersJsonArray.add(memberJson);
		      }
		    }
		    

		    if (super.getCurrentUser() != null) {
		      JSONObject currentUserJson = super.getCurrentUser().toJSON();
		      jsonObj.put("currentUser", currentUserJson);
		    }
		    if ((super.validate()) && (getTeamspaceValidator() != null)) {
		      jsonObj.put("validate", Boolean.valueOf(true));
		      jsonObj.put("invalidClasses", getTeamspaceValidator().getInvalidClasses());
		      jsonObj.put("invalidEntryTemplates", getTeamspaceValidator().getInvalidEntryTemplates());
		      jsonObj.put("invalidSearches", getTeamspaceValidator().getInvalidSearches());
		      jsonObj.put("invalidFoldersandDocs", getTeamspaceValidator().getInvalidFoldersandDocs());
		    } else {
		      //Logger.logDebug(this, methodName, this.request, "validate=" + this.validate + " : teamspaceValidator=" + getTeamspaceValidator());
		    }
		    //Logger.logDebug(this, methodName, this.request, "Returning JSON:" + jsonObj.toString());

		    return jsonObj;
		  }*/
	 /**
	  * 导出teamspace实例json
	  * @return
	  */
	 public JSONObject toInstanceJSON() {

		    JSONObject jsonObj = new JSONObject();
		    //jsonObj.put("className", super.getClassName());
		    
		    jsonObj.put("state", super.getState());
		    jsonObj.put("name", super.getName());
		    jsonObj.put("desc", super.getDescription());
		   // jsonObj.put("lastModified", super.getLastModified());
		    //jsonObj.put("lastModifiedUser", super.getLastModifiedUser());

		   /* if (super.getLastModifiedUserInfo() != null) {
		      jsonObj.put("lastModifiedUserInfo", super.getLastModifiedUserInfo().toJSON());
		    }*/
		    jsonObj.put("type", super.getType());
		
		   // jsonObj.put("defaultEntryTemplate", super.getDefaultEntryTemplate());

		    // 设置默认权限
		   /* JSONObject permissionObj = new JSONObject();
	        permissionObj.put("accessMask", Integer.valueOf(984531));
	        permissionObj.put("roleName", null);
	        permissionObj.put("granteeType", Integer.valueOf(SecurityPrincipalType.USER_AS_INT));//2000
	        permissionObj.put("granteeName", "#CREATOR-OWNER");
	        permissionObj.put("inheritableDepth", Integer.valueOf(0));
	        permissionObj.put("accessType", Integer.valueOf(AccessType.ALLOW_AS_INT));//1

	        JSONArray permissionArray = new JSONArray();
	        permissionArray.add(permissionObj);
	        jsonObj.put("permissions", permissionArray);*/
	        
		    try {
		      if (super.getJson() != null) {
		        JSONObject jobject = JSONObject.parse(super.getJson());
		        jsonObj.put("id", jobject.get("id"));
		        jsonObj.put("templateName", jobject.get("name"));
		        jsonObj.put("entryTemplates", jobject.get("entryTemplates"));
		        jsonObj.put("columnProperties", jobject.get("columnProperties"));
		        jsonObj.put("usesClasses",  jobject.get("usesClasses"));
		        jsonObj.put("contentClasses",  jobject.get("contentClasses"));
		        jsonObj.put("searchTemplates",  jobject.get("searchTemplates"));
		        jsonObj.put("defaultClass",  jobject.get("defaultClass"));
		        jsonObj.put("dataModelVersion",  jobject.get("dataModelVersion"));
		        jsonObj.put("folders",jobject.get("folders"));
		        // 成员
		        JSONArray membersJsonArray = new JSONArray();
			    jsonObj.put("members", membersJsonArray);
			    if (super.getTeam() != null) {// 添加团队成员或者组
			      List<Subject> team =	super.getTeam();
			      for (Subject member : team) {
			        JSONObject subMemberJson = new JSONObject();
			        List<Role> roles = null;
			        subMemberJson.put("type", member.getType().name());
			        subMemberJson.put("name", member.getShortName());
			        subMemberJson.put("id", member.getId());
			        subMemberJson.put("displayName", member.getDisplayName());
			        JSONArray rolesJsonArray = new JSONArray();
		            roles = member.getRoles();
		            if(roles != null && roles.size()>0){
		            	for(Role r : roles){
		            		rolesJsonArray.add(r.getId());
		            	}
		            }
		            subMemberJson.put("roles", rolesJsonArray);
			        membersJsonArray.add(subMemberJson);
			      }
			    }
			    // 模板角色
			    jsonObj.put("roles", jobject.get("roles"));
		      }
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }    

		    return jsonObj;
	 }
	/**
	 * 获取teamspace模板拥有的角色（所有者、成员和审阅者）
	 * @return
	 */
	public List<Role> getTemplateRoles(){
		
		List<Role>roles = new ArrayList<Role>();
		try{
		 JSONObject jobject = JSONObject.parse(super.getJson());
		 if(jobject.containsKey("roles")){
			 JSONArray array = (JSONArray) jobject.get("roles");
			 for(int i=0; i<array.size();i++){
				 List<String> privileges = new ArrayList<String>();
				 JSONObject roleProperties = (JSONObject) array.get(i);	
				 JSONArray privArray = (JSONArray) roleProperties.get("privileges");
				 if(privArray != null && privArray.size()>0){
					 for(int j=0;j<privArray.size();j++){
						 privileges.add(String.valueOf(privArray.get(j)));
					 }
				 }
				String id = String.valueOf(roleProperties.get("id"));
				String description = String.valueOf(roleProperties.get("description"));
				String name = String.valueOf(roleProperties.get("name"));
				String owner = String.valueOf(roleProperties.get("owner"));
				String preDefined = String.valueOf(roleProperties.get("preDefined"));
				String messageId = String.valueOf(roleProperties.get("messageId"));
				Role role = new Role(id,name,description,privileges);
				role.setMessageId(messageId);
				role.setOwner(Boolean.getBoolean(owner));
				role.setPreDefined(Boolean.getBoolean(preDefined));
				roles.add(role);
			 }
		 }
	
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return roles;
	}
	
	/**
	 * 获取teamspace模板拥有的角色（所有者、成员和审阅者）字典map，以id为key，Role对象为value
	 * @return
	 */
	public Map<String, Role> getTemplateRolesMapRefId(){
		
		Map<String, Role>roles = new HashMap<String,Role>();
		try{
		 JSONObject jobject = JSONObject.parse(super.getJson());
		 if(jobject.containsKey("roles")){
			 JSONArray array = (JSONArray) jobject.get("roles");
			 for(int i=0; i<array.size();i++){
				 List<String> privileges = new ArrayList<String>();
				 JSONObject roleProperties = (JSONObject) array.get(i);	
				 JSONArray privArray = (JSONArray) roleProperties.get("privileges");
				 if(privArray != null && privArray.size()>0){
					 for(int j=0;j<privArray.size();j++){
						 privileges.add(String.valueOf(privArray.get(j)));
					 }
				 }
				String id = String.valueOf(roleProperties.get("id"));
				String description = String.valueOf(roleProperties.get("description"));
				String name = String.valueOf(roleProperties.get("name"));
				String owner = String.valueOf(roleProperties.get("owner"));
				String preDefined = String.valueOf(roleProperties.get("preDefined"));
				String messageId = String.valueOf(roleProperties.get("messageId"));
				Role role = new Role(id,name,description,privileges);
				role.setMessageId(messageId);
				role.setOwner(Boolean.getBoolean(owner));
				role.setPreDefined(Boolean.getBoolean(preDefined));
				roles.put(id, role);
			 }
		 }
	
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return roles;
	}
	
	/**
	 * 获取teamspace模板拥有的角色（所有者、成员和审阅者）字典map，以name为key，Role对象为value
	 * @return
	 */
	public Map<String, Role> getTemplateRolesMapRefName(){
		
		Map<String, Role>roles = new HashMap<String,Role>();
		try{
		 JSONObject jobject = JSONObject.parse(super.getJson());
		 if(jobject.containsKey("roles")){
			 JSONArray array = (JSONArray) jobject.get("roles");
			 for(int i=0; i<array.size();i++){
				 List<String> privileges = new ArrayList<String>();
				 JSONObject roleProperties = (JSONObject) array.get(i);	
				 JSONArray privArray = (JSONArray) roleProperties.get("privileges");
				 if(privArray != null && privArray.size()>0){
					 for(int j=0;j<privArray.size();j++){
						 privileges.add(String.valueOf(privArray.get(j)));
					 }
				 }
				String id = String.valueOf(roleProperties.get("id"));
				String description = String.valueOf(roleProperties.get("description"));
				String name = String.valueOf(roleProperties.get("name"));
				String owner = String.valueOf(roleProperties.get("owner"));
				String preDefined = String.valueOf(roleProperties.get("preDefined"));
				String messageId = String.valueOf(roleProperties.get("messageId"));
				Role role = new Role(id,name,description,privileges);
				role.setMessageId(messageId);
				role.setOwner(Boolean.getBoolean(owner));
				role.setPreDefined(Boolean.getBoolean(preDefined));
				roles.put(name, role);
			 }
		 }
	
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return roles;
	}
}
