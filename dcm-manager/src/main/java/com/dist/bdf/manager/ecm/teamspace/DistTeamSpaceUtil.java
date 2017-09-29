package com.dist.bdf.manager.ecm.teamspace;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.dist.bdf.manager.ecm.teamspace.p8.DistP8TeamspaceService;
import com.ibm.ecm.teamspace.Teamspace;
import com.ibm.ecm.teamspace.TeamspaceUtil;
import com.ibm.ecm.util.MessageUtil;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class DistTeamSpaceUtil extends TeamspaceUtil {

	public static final String localizeRoleNames(String teamspaceJson) {
		
	    String methodName = "localizeRoleName";
	    String json = teamspaceJson;
	    try
	    {
	      JSONObject jsonObj = JSONObject.parse(teamspaceJson);
	      if (jsonObj != null) {
	       // Logger.logDebug(TeamspaceUtil.class, methodName, request, "json: " + jsonObj.toString());
	        JSONArray jsonRoles = (JSONArray)jsonObj.get("roles");
	        if (jsonRoles != null) {
	         // Logger.logDebug(TeamspaceUtil.class, methodName, request, "roles: " + jsonRoles.toString());
	          Object[] rolesArray = jsonRoles.toArray();
	          //Logger.logDebug(TeamspaceUtil.class, methodName, request, "roles.length: " + rolesArray.length);
	          for (int i = 0; i < rolesArray.length; i++) {
	            JSONObject roleObj = (JSONObject)jsonRoles.get(i);
	            boolean debug = false;
	            if (debug)
	            {
	              Set keySet = roleObj.keySet();
	              Iterator iter = keySet.iterator();
	              while (iter.hasNext()) {
	                String key = (String)iter.next();
	                Object valueObj = roleObj.get(key);
	                if ((valueObj instanceof String)) {
	                  String value = (String)valueObj;
	                 // Logger.logDebug(TeamspaceUtil.class, methodName, request, "role - key: " + key + ", value: " + value);
	                }
	              }
	            }
	            String rid = String.valueOf(roleObj.get("id"));
	            String predefined = String.valueOf(roleObj.get("preDefined"));
	            //Logger.logDebug(TeamspaceUtil.class, methodName, request, "teamspace contains role: " + rid + ", preDefined: " + predefined);
	            if (Boolean.valueOf(predefined).booleanValue() == true) {
	              String messageId = String.valueOf(roleObj.get("messageId"));
	              //Logger.logDebug(TeamspaceUtil.class, methodName, request, "predefined role: " + messageId);
	              if ((messageId != null) && (messageId.length() > 0) && (!messageId.equals("null")))
	              {
	                String name = MessageUtil.getMessage(java.util.Locale.CHINA, messageId + ".name");
	                String desc = MessageUtil.getMessage(java.util.Locale.CHINA, messageId + ".description");
	                //Logger.logDebug(TeamspaceUtil.class, methodName, request, "localized predefined role: " + name + ", " + desc);
	                roleObj.remove("name");
	                roleObj.put("name", name);
	                roleObj.remove("description");
	                roleObj.put("description", desc);
	              }
	            }
	          }
	        }
	        json = jsonObj.toString();
	      }
	    } catch (Exception e) {
	    	e.printStackTrace();
	      //Logger.logError(TeamspaceUtil.class, methodName, request, e);
	    }
	    return json;
	  }
	/**
	 * 根据teamspace模板名称，检索模板对象
	 * @param workspaceService
	 * @param templateName
	 * @return
	 */
	public static Teamspace retreiveTeamspacesTemplate(DistP8TeamspaceService workspaceService, String templateName) {
		Teamspace workspaceObject = null;
		try {
			List workspaces = workspaceService.retrieveTeamspaces("template", "published", false);
			Iterator iterator = workspaces.iterator();
			System.out.println("-->查询teamspace模板：" + templateName );
			while (iterator.hasNext()) {
				workspaceObject = (Teamspace) iterator.next();
				if (workspaceObject.getName().equalsIgnoreCase(templateName)) {
					System.out.println("    找到teamspace name：" + workspaceObject.getName());
					return workspaceObject;
				}

			}
			System.out.println("没有找到名称为【" + templateName + "】的teamspace模板。");
			return null;
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return null;
	}
}
