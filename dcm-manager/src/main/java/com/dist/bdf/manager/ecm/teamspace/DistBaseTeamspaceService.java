package com.dist.bdf.manager.ecm.teamspace;

import com.ibm.ecm.configuration.Config;
import com.ibm.ecm.configuration.RepositoryConfig;
import com.ibm.ecm.security.Role;
import com.ibm.ecm.security.SecurityConstants;
import com.ibm.ecm.security.SecurityConstants.AppPrivilegeSets;
import com.ibm.ecm.security.Subject;
import com.ibm.ecm.serviceability.Logger;
import com.ibm.ecm.teamspace.Teamspace;
import com.ibm.json.java.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.util.MessageResources;

public class DistBaseTeamspaceService
{
  public static final String TEAMSPACE_MODEL_VERSION_3 = "2.02";
  public static final String TEAMSPACE_MODEL_VERSION = "2.0.3";
  public static final String TEAMSPACE_RETRIEVAL_TEMPLATE = "template";
  public static final String TEAMSPACE_RETRIEVAL_INSTANCE = "instance";
  public static final String TEAMSPACE_RETRIEVAL_ALL = "allData";
  public static final String TEAMSPACE_RETRIEVAL_JSON = "jsonData";
  public static final String TEAMSPACE_RETRIEVAL_SEARCHES = "searches";
  public static final String TEAMSPACE_RETRIEVAL_CLASSES = "classes";
  public static final String TEAMSPACE_RETRIEVAL_COLUMNS = "columns";
  public static final String TEAMSPACE_RETRIEVAL_TEAM = "team";
  public static final String TEAMSPACE_RETRIEVAL_ENTRYTEMPLATES = "entryTemplates";
  public static final String TEAMSPACE_RETRIEVAL_NONE = "none";
  public static final String TEAMSPACE_TYPE_TEMPLATE = "template";
  public static final String TEAMSPACE_TYPE_INSTANCE = "instance";
  public static final String TEAMSPACE_JSON_PROP_SEARCHES = "searchTemplates";
  public static final String TEAMSPACE_JSON_PROP_FOLDERS = "folders";
  public static final String TEAMSPACE_JSON_PROP_CLASSES = "contentClasses";
  public static final String TEAMSPACE_JSON_PROP_ROLES = "roles";
  public static final String TEAMSPACE_JSON_PROP_MEMBERS = "members";
  public static final String TEAMSPACE_JSON_PROP_ENTRYTEMPLATES = "entryTemplates";
  public static final String TEAMSPACE_JSON_PROP_TEMPLATE_ID = "templateId";
  public static final String TEAMSPACE_JSON_PROP_DEFAULT_CLASS = "defaultClass";
  public static final String TEAMSPACE_JSON_PROP_DEFAULT_ENTRY_TEMPLATE = "defaultEntryTemplate";
  public static final String TEAMSPACE_JSON_PROP_DATA_MODEL_VERSION = "dataModelVersion";
  public static final String TEAMSPACE_JSON_PROP_DEFAULT = "isDefault";
  public static final String TEAMSPACE_JSON_PROP_DEFAULT_VALUE = "Default";
  public static final String TEAMSPACE_JSON_PROP_NAME = "name";
  public static final String TEAMSPACE_JSON_PROP_DESCRIPTION = "description";
  public static final String TEAMSPACE_JSON_PROP_ID = "id";
  public static final String TEAMSPACE_JSON_PROP_ITEMS = "folders";
  public static final String TEAMSPACE_JSON_PROP_DOCID = "docid";
  public static final String TEAMSPACE_JSON_PROP_SECURITY_INHERITANCE = "securityInheritance";
  public static final String TEAMSPACE_JSON_PROP_USES_CLASSES = "usesClasses";
  public static final String TEAMSPACE_JSON_PROP_COLUMN_PROPS = "columnProperties";
  public static final String TEAMSPACE_JSON_PROP_ITEMS_PATH = "path";
  public static final String TEAMSPACE_JSON_PROP_ITEMS_NAME = "name";
  public static final String TEAMSPACE_JSON_PROP_ITEMS_TYPE = "type";
  public static final String TEAMSPACE_JSON_PROP_ITEMS_IS_COPY = "IsCopy";
  public static final String TEAMSPACE_JSON_PROP_ITEMS_PROPERTIES = "properties";
  public static final String TEAMSPACE_JSON_PROP_ITEMS_PROPERTIES_CRITERIA = "criterias";
  public static final String TEAMSPACE_JSON_PROP_ITEMS_CHILD_PROPERTIES_CRITERIA = "childComponentValues";
  public static final String TEAMSPACE_JSON_PROP_ITEMS_TYPE_FOLDER = "folder";
  public static final String TEAMSPACE_JSON_PROP_ITEMS_PROPERTIES_CLASS = "documentType";
  public static final String TEAMSPACE_PROP_STATE_TEMP = "draft";
  public static final String TEAMSPACE_PROP_STATE_DELETED = "deleted";
  public static final String TEAMSPACE_PROP_STATE_PUBLISHED = "published";
  public static final String APPLICATION_NAME = "Navigator";
  public MessageResources resources;

  protected DistBaseTeamspaceService(HttpServletRequest request)
  {
	  try{
    this.resources = MessageResources.getGlobalMessageResources(request);
	  }catch(Exception ex){
		  ex.printStackTrace();
	  }
  }

  protected static void addMember(Map<String, Subject> map, Subject newMember) {
    String name = newMember.getName();
    if (map.containsKey(name)) {
      Subject original = (Subject)map.get(name);
      List roles = mergeRoles(original.getRoles(), newMember.getRoles());
      original.setRoles(roles);
    } else {
      map.put(name, newMember);
    }
  }

  protected static List<Role> mergeRoles(List<Role> original, List<Role> newRoles) {
    Map map = new HashMap();
    if (original != null) {
      for (Role r : original) {
        map.put(r.getId(), r);
      }
    }
    if (newRoles != null) {
      for (Role r : newRoles) {
        map.put(r.getId(), r);
      }
    }

    List result = new ArrayList(map.values());
    return result;
  }

  public static boolean isPreR3Mode(HttpServletRequest request) {
    String methodName = "isPreR3Mode";
    RepositoryConfig repositoryConfig = Config.getRepositoryConfigUsingIdOrServerName(request, null);
    boolean value = false;
    if (repositoryConfig != null)
      value = repositoryConfig.isTeamspaceOwnerModifyRole();
    else {
      Logger.logDebug(DistBaseTeamspaceService.class, methodName, request, "no repository config");
    }
    Logger.logDebug(DistBaseTeamspaceService.class, methodName, request, "isTeamspaceOwnerModifyRole: " + value);
    return value;
  }

  public static boolean isInTeamspaceRecentMode(HttpServletRequest request)
  {
    String methodName = "isInTeamspaceRecentMode";
    RepositoryConfig repositoryConfig = Config.getRepositoryConfigUsingIdOrServerName(request, null);
    boolean value = false;
    if (repositoryConfig != null)
      value = repositoryConfig.isTeamspaceRecentEnabled();
    else {
      Logger.logDebug(DistBaseTeamspaceService.class, methodName, request, "no repository config");
    }
    Logger.logDebug(DistBaseTeamspaceService.class, methodName, request, "isTeamspacRecentMode: " + value);
    return value;
  }

  public static int getTeamspaceRecentValue(HttpServletRequest request)
  {
    String methodName = "getTeamspaceRecentValue";
    RepositoryConfig repositoryConfig = Config.getRepositoryConfigUsingIdOrServerName(request, null);
    int value = 30;
    if (repositoryConfig != null)
      value = repositoryConfig.getTeamspaceRecentValue();
    else {
      Logger.logDebug(DistBaseTeamspaceService.class, methodName, request, "no repository config");
    }
    Logger.logDebug(DistBaseTeamspaceService.class, methodName, request, "recent days: " + value);
    return value;
  }

  protected void addModelVersion(HttpServletRequest request, Teamspace teamspace)
  {
    String methodName = "addModelVersion";
    try {
      JSONObject jsonObject = JSONObject.parse(teamspace.getJson());
      jsonObject.put("dataModelVersion", "2.0.3");
      String tempTeamspaceJson = jsonObject.toString();
      teamspace.setJson(tempTeamspaceJson);
    } catch (IOException e1) {
      Logger.logError(this, methodName, request, e1);
    }
  }
  protected void addModelVersion(Teamspace teamspace)
  {
    String methodName = "addModelVersion";
    try {
      JSONObject jsonObject = JSONObject.parse(teamspace.getJson());
      jsonObject.put("dataModelVersion", "2.0.3");
      String tempTeamspaceJson = jsonObject.toString();
      teamspace.setJson(tempTeamspaceJson);
    } catch (IOException e1) {
    	e1.printStackTrace();
      //Logger.logError(this, methodName, request, e1);
    }
  }

  protected void dumpList(HttpServletRequest request, List<String> list, String listType)
  {
    String methodName = "dumpList";
    if (!Logger.isDebugLogged())
      return;
    Iterator iterator = list.iterator();
    while (iterator.hasNext()) {
      String item = (String)iterator.next();
      Logger.logDebug(this, methodName, request, listType + "  has item : " + item);
    }

    Logger.logDebug(this, methodName, request, listType + " ---------------------------------------- ");
  }

  public static boolean updatePreV3Privs(MessageResources resources, List<String> privileges, String modelVersion, boolean preR3Mode, Role role, boolean isOwner, HttpServletRequest request) {
    String methodName = "updatePreV3Privs";
    boolean isOwnerName = false;
    if (modelVersion == null) {
      if (!privileges.contains("createNewSearches")) {
        privileges.add("createNewSearches");
        Logger.logDebug(DistBaseTeamspaceService.class, methodName, request, "added priv createNewSearches for older data model");
      }
      String ownerName = resources.getMessage(request.getLocale(), "roles.owner.name");
      Logger.logDebug(DistBaseTeamspaceService.class, methodName, request, "ownerName: " + ownerName);
      Logger.logDebug(DistBaseTeamspaceService.class, methodName, request, "isOwner: " + isOwner);
      Logger.logDebug(DistBaseTeamspaceService.class, methodName, request, "preR3Mode: " + preR3Mode);

      String roleName = ownerName;
      String roleDescription = ownerName;
      if (role != null) {
        roleName = role.getName();
        roleDescription = role.getDescription();
      }
      if (roleDescription == null)
        roleDescription = "";
      if ((roleName.equalsIgnoreCase(ownerName)) || (roleDescription.equalsIgnoreCase(ownerName)) || (roleName.equalsIgnoreCase(SecurityConstants.AppPrivilegeSets.TEAMSPACE.getValue())))
        isOwnerName = true;
      Logger.logDebug(DistBaseTeamspaceService.class, methodName, request, "isOwnerName: " + isOwnerName);
      Logger.logDebug(DistBaseTeamspaceService.class, methodName, request, "roleName: " + roleName);

      if ((preR3Mode) && (!privileges.contains("modifyRoles")) && (isOwner) && (isOwnerName)) {
        privileges.add("modifyRoles");
        Logger.logDebug(DistBaseTeamspaceService.class, methodName, request, "added priv modifyRoles for older data model");
      } else {
        Logger.logDebug(DistBaseTeamspaceService.class, methodName, request, "not adding modifyRoles");
      }
    } else {
      Logger.logDebug(DistBaseTeamspaceService.class, methodName, request, "is R3 role");
    }
    return isOwnerName;
  }
}