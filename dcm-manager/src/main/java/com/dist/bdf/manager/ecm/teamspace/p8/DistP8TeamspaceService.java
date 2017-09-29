package com.dist.bdf.manager.ecm.teamspace.p8;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dist.bdf.manager.ecm.teamspace.DistBaseTeamspaceService;
import com.dist.bdf.manager.ecm.teamspace.DistTeamSpaceUtil;
import com.dist.bdf.manager.ecm.teamspace.DistTeamspace;
import com.dist.bdf.manager.ecm.teamspace.DistTeamspaceValidator;
import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.ClassDescriptionSet;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Connection;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.IndependentlyPersistableObject;
import com.filenet.api.core.ObjectReference;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.core.RetrievingBatch;
import com.filenet.api.core.Versionable;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.filenet.api.meta.ClassDescription;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.Properties;
import com.filenet.api.property.Property;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.security.Group;
import com.filenet.api.security.User;
import com.filenet.api.util.Id;
import com.ibm.ecm.configuration.RepositoryConfig;
import com.ibm.ecm.security.AccessControlList;
import com.ibm.ecm.security.Role;
import com.ibm.ecm.security.SecurityConstants;
import com.ibm.ecm.security.SecurityException;
import com.ibm.ecm.security.Subject;
import com.ibm.ecm.security.UserGroup;
import com.ibm.ecm.security.p8.P8AccessControlListData;
import com.ibm.ecm.security.p8.P8SecurityService;
import com.ibm.ecm.struts.actions.p8.P8FindClassesAction;
import com.ibm.ecm.teamspace.BaseTeamspaceService;
import com.ibm.ecm.teamspace.Teamspace;
import com.ibm.ecm.teamspace.TeamspaceDB;
import com.ibm.ecm.teamspace.TeamspaceException;
import com.ibm.ecm.teamspace.TeamspaceUtil;
import com.ibm.ecm.util.DateUtil;
import com.ibm.ecm.util.MessageUtil;
import com.ibm.ecm.util.SearchTemplateBase;
import com.ibm.ecm.util.p8.P8Connection;
import com.ibm.ecm.util.p8.P8DocID;
import com.ibm.ecm.util.p8.P8FolderTemplates;
import com.ibm.ecm.util.p8.P8Util;
import com.ibm.json.java.JSON;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

/**
 * 
 * @author weifj
 * @version 1.0，2016/04/01，weifj，创建
 */
public class DistP8TeamspaceService extends DistBaseTeamspaceService {

	protected Logger Logger = (Logger) LoggerFactory.getLogger(getClass());

	public static final String TEAMSPACE_PARENT_FOLDER = "/ClbTeamspaces";
	public static final String TEAMSPACE_TEMPLATE_PARENT_FOLDER = "/ClbTeamspace Templates";
	public static final String ROLE_PARENT_FOLDER = "/ClbRoles";
	public static final String TEAMSPACE_CLASS = "ClbTeamspace";
	public static final String TEAMSPACE_TEMPLATE_CLASS = "ClbTeamspaceTemplate";
	public static final String TEAMSPACE_PROP_DESCRIPTION = "Description";
	public static final String TEAMSPACE_PROP_NAME = "ClbTeamspaceName";
	public static final String TEAMSPACE_PROP_JSON = "ClbJSONData";
	public static final String TEAMSPACE_PROP_STATE = "ClbTeamspaceState";
	public static final String SECURITY_ADAPTER_OVP = "ClbSecurityAdapter";

	private TeamspaceDB teamspaceDB;
	private Connection connection;
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	private ObjectStore objectStore;
	private Domain domain;
	private P8Connection p8connection;
	private P8SecurityService securityService;
	private DistTeamspaceValidator teamspaceValidator = new DistTeamspaceValidator();
	private Map<String, User> userCache = new HashMap<String, User>();
	private Map<String, Group> groupCache = new HashMap<String, Group>();

	public DistP8TeamspaceService(Connection connection, Domain domain, ObjectStore objectStore,
			P8Connection p8connection) {

		super(null);
		this.connection = connection;
		this.objectStore = objectStore;
		this.domain = domain;
		this.p8connection = p8connection;
		this.securityService = new P8SecurityService(null, connection, objectStore);

	}

	public List<Teamspace> retrieveTeamspaces(String type, String retrievalOption, boolean validateTemplateData)
			throws Exception {
		return retrieveTeamspaces(type, "all", retrievalOption, validateTemplateData, true);
	}

	public List<Teamspace> retrieveTeamspaces(String type, String state, String retrievalOption,
			boolean validateTemplateData, boolean getOffline) throws Exception {
		String methodName = "retrieveTeamspaces";

		List<Teamspace> teamspaces = new ArrayList<Teamspace>();

		SearchSQL sqlObject = new SearchSQL();
		sqlObject.setSelectList("*");
		sqlObject.setMaxRecords(2000);

		String className = "ClbTeamspace";
		if (type.equals("template")) {
			className = "ClbTeamspaceTemplate";
		}

		sqlObject.setFromClauseInitialValue(className, null, false);

		String whereClause = "";
		if ((retrievalOption.equals("instance")) && (getOffline)) {
			String extraCondition = new StringBuilder().append("ClbTeamspaceState <> ")
					.append(BaseTeamspaceService.State.getEnum("deleted").getValue()).append(" AND ")
					.append("ClbTeamspaceState").append(" <> ").append(BaseTeamspaceService.State.PERSONAL.getValue())
					.toString();
			whereClause = new StringBuilder().append(whereClause).append(extraCondition).toString();
		} else if (retrievalOption.equals("instance")) {
			String extraCondition = new StringBuilder().append("ClbTeamspaceState = ")
					.append(BaseTeamspaceService.State.getEnum("published").getValue()).toString();
			whereClause = new StringBuilder().append(whereClause).append(extraCondition).toString();
		} else if (!state.equalsIgnoreCase("all")) {
			String extraCondition = new StringBuilder().append("ClbTeamspaceState = ")
					.append(BaseTeamspaceService.State.getEnum(state).getValue()).toString();
			whereClause = new StringBuilder().append(whereClause).append(extraCondition).toString();
		}

		if (whereClause.length() > 0) {
			sqlObject.setWhereClause(whereClause);
		}
		SearchScope search = new SearchScope(this.objectStore);

		PropertyFilter pf = new PropertyFilter();
		pf.addIncludeProperty(1, null, null, "ClbTeamspaceState", null);
		pf.addIncludeProperty(1, null, null, "Description", null);
		pf.addIncludeProperty(1, null, null, "ClbTeamspaceName", null);
		if (retrievalOption.equals("template"))
			pf.addIncludeProperty(1, null, null, "ClbJSONData", null);
		pf.addIncludeProperty(1, null, null, "Id", null);
		pf.addIncludeProperty(1, null, null, "Name", null);
		pf.addIncludeProperty(1, null, null, "Description", null);
		pf.addIncludeProperty(1, null, null, "DateLastModified", null);
		pf.addIncludeProperty(1, null, null, "LastModifier", null);

		IndependentObjectSet myObjects;

		if ((retrievalOption.equals("template")) || (retrievalOption.equals("instance")))
			myObjects = search.fetchObjects(sqlObject, null, pf, Boolean.valueOf(false));
		else {
			myObjects = search.fetchObjects(sqlObject, null, null, Boolean.valueOf(false));
		}

		Iterator iter = myObjects.iterator();
		while (iter.hasNext()) {
			IndependentlyPersistableObject ipo = (IndependentlyPersistableObject) iter.next();
			Teamspace teamspace = createTeamspaceObject(ipo, retrievalOption, type, validateTemplateData);

			 if ((type == null) || (type.length() == 0)) {
				teamspaces.add(teamspace);
			} else if ((teamspace != null) && (teamspace.getType().equalsIgnoreCase(type))) {
				teamspaces.add(teamspace);
			}
		}

		return teamspaces;
	}

	public void logTeamspaceObject(Teamspace teamspace, String methodName) {
		/*
		 * Logger.logDebug(this, methodName, this.request,
		 * "-----------------------------------------------------------------");
		 * Logger.logDebug(this, methodName, this.request, new
		 * StringBuilder().append("teamspace.getName(): "
		 * ).append(teamspace.getName()).toString()); Logger.logDebug(this,
		 * methodName, this.request, new StringBuilder().append(
		 * "teamspace.getDescription(): "
		 * ).append(teamspace.getDescription()).toString());
		 * Logger.logDebug(this, methodName, this.request, new
		 * StringBuilder().append("teamspace.getType(): "
		 * ).append(teamspace.getType()).toString()); Logger.logDebug(this,
		 * methodName, this.request, new StringBuilder().append(
		 * "teamspace.getId(): ").append(teamspace.getId()).toString());
		 * Logger.logDebug(this, methodName, this.request, new
		 * StringBuilder().append("teamspace.getState(): "
		 * ).append(teamspace.getState()).toString()); Logger.logDebug(this,
		 * methodName, this.request, new StringBuilder().append(
		 * "teamspace.getTemplateId(): "
		 * ).append(teamspace.getTemplateId()).toString()); if
		 * (teamspace.getTeam() != null) { Logger.logDebug(this, methodName,
		 * this.request, new StringBuilder().append("teamspace.getTeam(): "
		 * ).append(teamspace.getTeam().toArray().toString()).toString()); }
		 * Logger.logDebug(this, methodName, this.request,
		 * "-----------------------------------------------------------------");
		 */
	}

	public Teamspace createTeamspaceObject(IndependentlyPersistableObject ipo, String retrievalOptions, String type,
			boolean validateTemplateData) throws SecurityException, Exception {
		String methodName = "createTeamspaceObject";
		// Logger.logEntry(this, methodName, this.request);
		// Logger.logDebug(this, methodName, this.request, new
		// StringBuilder().append("teamspace retrievalOptions =
		// ").append(retrievalOptions).toString());
		Properties props = ipo.getProperties();

		String id = props.getIdValue("Id").toString();
		// Logger.logDebug(this, methodName, this.request, new
		// StringBuilder().append("teamspace id = ").append(id).toString());
		ObjectReference objRef = ipo.getObjectReference();
		id = P8Util.getDocId(objRef);
		// Logger.logDebug(this, methodName, this.request, new
		// StringBuilder().append("teamspace pid = ").append(id).toString());

		Date lastModifiedDate = props.getDateTimeValue("DateLastModified");
		String lastModified = DateUtil.getISODateString(lastModifiedDate, true);

		String lastModifiedUser = props.getStringValue("LastModifier");
		com.ibm.ecm.security.User userInfo = this.p8connection.retrieveUserInfo(props.getStringValue("LastModifier"));
		if (userInfo != null) {
			lastModifiedUser = userInfo.getDisplayName();
		}

		Integer state = props.getInteger32Value("ClbTeamspaceState");
		String usesClasses = "";
		String modelVersion = null;

		String teamspaceName = props.getStringValue("ClbTeamspaceName");
		// Logger.logDebug(this, methodName, this.request, new
		// StringBuilder().append("cto teamspaceName =
		// ").append(teamspaceName).toString());

		if (teamspaceName == null) {
			if (!props.isPropertyPresent("FolderName")) {
				ipo.fetchProperties(new String[] { "FolderName" });
			}
			teamspaceName = props.getStringValue("FolderName");
			// Logger.logDebug(this, methodName, this.request, new
			// StringBuilder().append("cto teamspaceName = null, using folder
			// name ").append(teamspaceName).toString());
		}

		String description = props.getStringValue("Description");
		// Logger.logDebug(this, methodName, this.request, new
		// StringBuilder().append("cto description =
		// ").append(description).toString());

		String json = null;
		byte[] jsonBytes = props.getBinaryValue("ClbJSONData");
		if (jsonBytes != null) {
			json = new String(jsonBytes);
			// Logger.logDebug(this, methodName, this.request, new
			// StringBuilder().append("cto json: ").append(json).toString());

			json = DistTeamSpaceUtil.localizeRoleNames(json);
			this.teamspaceDB = new TeamspaceDB(null,json);

			modelVersion = this.teamspaceDB.geTeamspaceModelVersion();
			usesClasses = this.teamspaceDB.getUsesClasses();
		}
		// Logger.logDebug(this, methodName, this.request, new
		// StringBuilder().append("modelVersion:
		// ").append(modelVersion).toString());
		Teamspace teamspace = null;
		if (teamspaceName != null) {
			List team = null;
			/*if ((retrievalOptions == null) || ((retrievalOptions != null) && (retrievalOptions.equals("allData")))) {
				// Logger.logDebug(this, methodName, this.request, "cto
				// retrieved ACL");
			}*/

			// Logger.logDebug(this, methodName, this.request, "cto new
			// Teamspace(" + teamspaceName + ", " + description + ", " +
			// ipo.getClassName() + ", " + json + ", " + team + ")");
			if (retrievalOptions != null &&((retrievalOptions.equals("template")) || (retrievalOptions.equals("allData")))) {
				// Logger.logDebug(this, methodName, this.request, "create
				// teamspace object and set json data");
				teamspace = new Teamspace(null, teamspaceName, description, type, ipo.getClassName(), json, null);
			} else {
				// Logger.logDebug(this, methodName, this.request, "create
				// teamspace object without setting json data");
				teamspace = new Teamspace(null, teamspaceName, description, type, ipo.getClassName(), null, null);
			}
			teamspace.setValidate(validateTemplateData);
			teamspace.setModelVersion(modelVersion);
			teamspace.setLastModifiedUserInfo(userInfo);
			try {
				teamspace.setState(BaseTeamspaceService.State.getEnum(state.intValue()).getStringValue());
			} catch (TeamspaceException e) {
				e.printStackTrace();
				// Logger.logError(this, methodName, this.request, e);
			}

			teamspace.setId(id);
			if (lastModified != null) {
				teamspace.setLastModified(lastModified.toString());
			}
			if (lastModifiedUser != null) {
				teamspace.setLastModifiedUser(lastModifiedUser);
			}
			if ((this.teamspaceDB != null) && (this.teamspaceDB.getTeamspaceTemplateId() != null)) {
				teamspace.setTemplateId(this.teamspaceDB.getTeamspaceTemplateId());
			}
			String defaultEntryTemplate = "";
			String defaultClass = "";

			/* 1757 */ if (this.teamspaceDB != null) {
				/* 1758 */ defaultClass = this.teamspaceDB.getDefaultClass();
				/* 1759 */ defaultEntryTemplate = this.teamspaceDB.getDefaultEntryTemplate();
				/*      */ }
			// Logger.logDebug(this, methodName, this.request, new
			// StringBuilder().append("cto defaultClass =
			// ").append(defaultClass).toString());
			// Logger.logDebug(this, methodName, this.request, new
			// StringBuilder().append("cto defaultEntryTemplate =
			// ").append(defaultEntryTemplate).toString());
			/*      */
			if (defaultClass != null) {
				teamspace.setDefaultClass(defaultClass);
			}
			if (defaultEntryTemplate != null) {
				teamspace.setDefaultEntryTemplate(defaultEntryTemplate);
			}
			if (usesClasses != null) {
				teamspace.setUsesClasses(usesClasses);
			}
			if ((props.isPropertyPresent("ClbSecurityAdapter")) && retrievalOptions != null && (retrievalOptions.equals("allData"))) {
				IndependentlyPersistableObject adapter = (IndependentlyPersistableObject) props
						.getObjectValue("ClbSecurityAdapter");
				if (adapter != null) {
					String securityAdapterId = adapter.getProperties().getIdValue("Id").toString();
					teamspace.setSecurityAdapterId(securityAdapterId);
				}
			}

			if ((this.teamspaceDB != null) && (retrievalOptions != null)) {
				if (retrievalOptions.equals("allData")) {
					retrieveTeamspaceSearches(teamspace, validateTemplateData);

					teamspace.setJson(this.teamspaceDB.toJson());
					retrieveTeamspaceClasses(teamspace, validateTemplateData);
					retrieveTeamspaceEntryTemplates(teamspace, validateTemplateData);

					if (validateTemplateData) {
						validateFolderData(teamspace);
						teamspace.setTeamspaceValidator(this.teamspaceValidator);
					}
					retrieveTeamspaceColumns(teamspace);
					List teamList = retrieveTeam(teamspace, modelVersion);
					teamspace.setTeam(teamList);
				} else if (retrievalOptions.equals("searches")) {
					retrieveTeamspaceSearches(teamspace, validateTemplateData);

					teamspace.setJson(this.teamspaceDB.toJson());
				} else if (retrievalOptions.equals("classes")) {
					retrieveTeamspaceClasses(teamspace, validateTemplateData);
				} else if (retrievalOptions.equals("entryTemplates")) {
					retrieveTeamspaceEntryTemplates(teamspace, validateTemplateData);
				} else if (retrievalOptions.equals("columns")) {
					retrieveTeamspaceColumns(teamspace);
				} else if (retrievalOptions.equals("team")) {
					List teamList = retrieveTeam(teamspace, modelVersion);
					teamspace.setTeam(teamList);
				}

				teamspace.setJson(this.teamspaceDB.toJson());
			}

			String currentUserName = this.p8connection.getUserName();

			if ((teamspace != null) && (retrievalOptions != null)
					&& ((retrievalOptions.equals("allData")) || (retrievalOptions.equals("instance")))) {
				// Logger.logDebug(this, methodName, this.request, "get user
				// data");
				List roles = this.securityService.retrieveRoles(ipo);

				updatePreR3Roles(roles, modelVersion);
				com.ibm.ecm.security.User user = new com.ibm.ecm.security.User(currentUserName.toUpperCase(),
						currentUserName, null, null, null, roles);
				user.setIsOwner(isCurrentUserOwner(roles, ipo));
				teamspace.setCurrentUser(user);
			}
		} else {
			teamspace = null;
		}
		// Logger.logExit(this, methodName, this.request);
		return teamspace;
	}

	public boolean isCurrentUserOwner(List<Role> roles, IndependentlyPersistableObject ipo)
	  {
	    boolean isOwner = false;
	    List privsForOwners = new ArrayList(SecurityConstants.ALL_PRIVILEGES_P8);
	    privsForOwners.remove("modifyRoles");
	    privsForOwners.remove("createNewSearches");

	    for (Role role : roles)
	    {
	      if (role.isOwner()) {
	        ArrayList privs = new ArrayList(role.getPrivileges());
	        if (privs.containsAll(privsForOwners)) {
	          isOwner = true;
	        }
	      }
	    }

	    if ((!isOwner) && (isOSAdmin())) {
	      isOwner = true;
	    }
	    return isOwner;
	  }

	private boolean isOSAdmin() {
	    int accessAllowed1 = this.objectStore.getAccessAllowed().intValue();
	    boolean canChangeOwner = (accessAllowed1 & 0x1000000) > 0;
	    boolean canChangePermissions = (accessAllowed1 & 0x40000) > 0;
	    if ((canChangePermissions) && (canChangeOwner)) {
	      return true;
	    }
	    return false;
	  }

	private List<Subject> retrieveTeam(Teamspace teamspace, String modelVersion) {
		
	    String methodName = "retrieveTeam";
	    //Logger.logEntry(this, methodName, this.request, new StringBuilder().append("teamspace: ").append(teamspace.getName()).toString());
	    Map map = new HashMap();

	    JSONArray membersJson = this.teamspaceDB.getMembers();

	    JSONArray rolesArray = this.teamspaceDB.getRoles();
	    HashMap rolesMap = new HashMap();
	    JSONArray updatedRolesArray = new JSONArray();

	    //Logger.logDebug(this, methodName, this.request, new StringBuilder().append("role count: ").append(rolesArray != null ? Integer.valueOf(rolesArray.size()) : "0").toString());
	    if (rolesArray != null)
	      _processTeamRoles(rolesArray, updatedRolesArray, rolesMap, modelVersion);
	    this.teamspaceDB.setRoles(updatedRolesArray);
	    if (membersJson != null)
	      System.out.println( new StringBuilder().append(methodName+" : ").append("membersJson count: ").append(membersJson.size()).toString());
	    batchRetrieveUsersAndGroups(membersJson);
	    for (int i = 0; (membersJson != null) && (i < membersJson.size()); i++) {
	      JSONObject member = (JSONObject)membersJson.get(i);
	      String principalId = (String)member.get("id");
	      String principalName = (String)member.get("name");

	      SecurityConstants.PrincipalType principalType = SecurityConstants.PrincipalType.valueOf(((String)member.get("type")).toUpperCase());

	      ArrayList usrRoles = new ArrayList();
	      try
	      {
	        if (principalType == SecurityConstants.PrincipalType.USER) {
	          String principalSrcId = principalId;
	          if (principalId.equals("CURRENT_USER")) {
	            principalSrcId = principalName;
	          }
	          com.filenet.api.security.User usr = (com.filenet.api.security.User)this.userCache.get(principalSrcId);
	          if (usr != null) {
	            com.ibm.ecm.security.User user = new com.ibm.ecm.security.User(usr.get_Id(), usr.get_Name(), usr.get_ShortName(), usr.get_DisplayName(), usr.get_Email(), usrRoles);
	            addMember(map, user);
	          }
	          else {
	            continue;
	          }
	        }
	        else if (principalType == SecurityConstants.PrincipalType.GROUP)
	        {
	          Group group = (Group)this.groupCache.get(principalName);
	          if (group != null)
	          {
	            String displayName = group.get_DisplayName();
	            String name = group.get_Name();

	            if (((name != null) && (name.contains("-$IC-C-"))) || ((displayName != null) && (displayName.contains("-$IC-C-")))) {
	              String s = name != null ? name : displayName;

	              if (s.contains("$IC-C-O"))
	                displayName = new StringBuilder().append(teamspace.getName()).append(" ").append(MessageUtil.getMessage(java.util.Locale.CHINA, "roles.owner.name")).toString();
	              else if (s.contains("$IC-C-M")) {
	                displayName = new StringBuilder().append(teamspace.getName()).append(" ").append(MessageUtil.getMessage(java.util.Locale.CHINA, "roles.member.name")).toString();
	              }
	              name = displayName;
	            }
	            UserGroup userGroup = new UserGroup(group.get_Id(), group.get_Name(), group.get_ShortName(), displayName, null, usrRoles);
	            addMember(map, userGroup);
	          }
	          else {
	            continue;
	          }
	        }
	      } catch (Exception e) {
	    	e.printStackTrace();
	    	System.err.println(methodName+" : The user or group has been removed from LDAP");
	        continue;
	      }

	      JSONArray memberRolesArray = (JSONArray)member.get("roles");
	      for (int j = 0; j < memberRolesArray.size(); j++) {
	        String roleId = (String)memberRolesArray.get(j);

	        Role role = null;
	        try {
	          role = ((Role)rolesMap.get(roleId)).clone();
	        }
	        catch (CloneNotSupportedException e) {
	          e.printStackTrace();
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

	    }

	    List members = new ArrayList(map.values());
	    _processUpdatedMemberInfo(members);
	
	    return members;
	  }

	private void clearUserCache() {
	  this.userCache.clear();
	}

	 private void _processUpdatedMemberInfo(List<Subject> members)
	  {
	    JSONArray updatedMemberArray = new JSONArray();
	    for (Subject user : members) {
	      JSONObject jsonObj = new JSONObject();
	      jsonObj.put("id", user.getId());
	      jsonObj.put("name", user.getName());
	      jsonObj.put("displayName", user.getDisplayName());
	      jsonObj.put("type", user.getType().toString());
	      jsonObj.put("shortName", user.getShortName());

	      List<Role> roles = user.getRoles();
	      JSONArray jsonArray;
	      if ((roles != null) && (roles.size() > 0)) {
	        jsonArray = new JSONArray();
	        jsonObj.put("roles", jsonArray);

	        for (Role role : roles) {
	          jsonArray.add(role.getId());
	        }
	      }
	      updatedMemberArray.add(jsonObj);
	    }
	    if (updatedMemberArray.size() > 0)
	      this.teamspaceDB.setMembers(updatedMemberArray);
	  }

	  private void batchRetrieveUsersAndGroups(JSONArray membersJson)
	  {
	    String methodName = "batchRetrieveUsersAndGroups";
	    RetrievingBatch rb = RetrievingBatch.createRetrievingBatchInstance(this.domain);
	    clearUserCache();
	    clearUserGroupCache();
	    boolean retrieveBatch = false;
	    for (int i = 0; (membersJson != null) && (i < membersJson.size()); i++) {
	      JSONObject member = (JSONObject)membersJson.get(i);
	      String principalId = (String)member.get("id");
	      String principalName = (String)member.get("name");
	      SecurityConstants.PrincipalType principalType = SecurityConstants.PrincipalType.valueOf(((String)member.get("type")).toUpperCase());
	      try {
	        if (principalType == SecurityConstants.PrincipalType.USER) {
	          String principalSrcId = principalId;
	          if (principalId.equals("CURRENT_USER"))
	            principalSrcId = principalName;
	          com.filenet.api.security.User usr = Factory.User.getInstance(this.connection, principalSrcId);
	          rb.add(usr, null);
	          retrieveBatch = true;
	          addUserToCache(principalSrcId, usr);
	        } else if (principalType == SecurityConstants.PrincipalType.GROUP) {
	          Group group = Factory.Group.getInstance(this.connection, principalName);
	          rb.add(group, null);
	          retrieveBatch = true;
	          addGroupCache(principalName, group);
	        }
	      } catch (Exception e) {
	    	  System.err.println("The user or group has been removed from LDAP");
	    
	      }

	    }

	    if (retrieveBatch)
	      rb.retrieveBatch();
	  }


	private void addGroupCache(String key, Group value) {
	   this.groupCache.put(key, value);
    }

	private void addUserToCache(String key, com.filenet.api.security.User value) {
		this.userCache.put(key, value);
	 }

	private void clearUserGroupCache() {
		/* 165 */ this.groupCache.clear();
		/*      */ }

	private void _processTeamRoles(JSONArray rolesArray, JSONArray updatedRolesArray, HashMap<String, Role> rolesMap,
			String modelVersion) {
		/* 2100 */ String methodName = "_processTeamRoles";
		/* 2101 */ for (int i = 0; i < rolesArray.size(); i++) {
			/* 2102 */ JSONObject roleObj = (JSONObject) rolesArray.get(i);
			/*      */
			/* 2104 */ String roleId = (String) roleObj.get("id");
			/* 2105 */ String messageId = (String) roleObj.get("messageId");
			/* 2106 */ String roleName = (String) roleObj.get("name");
			/* 2107 */ String roleDesc = (String) roleObj.get("description");
			/* 2108 */ boolean isOwner = ((Boolean) roleObj.get("owner")).booleanValue();
			/* 2109 */ // Logger.logDebug(this, methodName, this.request, new
						// StringBuilder().append("from json data isOwnerRole :
						// ").append(isOwner).toString());
			/*      */
			/* 2111 */ JSONArray privsArray = (JSONArray) roleObj.get("privileges");
			/* 2112 */ List privileges = new ArrayList();
			/* 2113 */ for (int j = 0; (privsArray != null) && (j < privsArray.size()); j++) {
				/* 2114 */ String priv = (String) privsArray.get(j);
				/* 2115 */ privileges.add(priv);
				/*      */ }
			/*      */
			/* 2118 */ Role role = new Role(roleId, roleName, roleDesc, privileges);
			/* 2119 */ updatePreV3Privs(privileges, modelVersion, isPreR3Mode(), role, isOwner);
			/* 2120 */ role.setOwner(isOwner);
			/* 2121 */ role.setRoleType(SecurityConstants.RoleType.TEAMSPACE);
			/* 2122 */ role.setMessageId(messageId);
			/* 2123 */ // Logger.logDebug(this, methodName, this.request, new
						// StringBuilder().append("add role to roleMap:
						// ").append(role.toJSON()).toString());
			/* 2124 */ rolesMap.put(roleId, role);
			/* 2125 */ updatedRolesArray.add(role.toJSON());
			/*      */ }
		/*      */ }

	private void updatePreR3Roles(List<Role> roles, String modelVersion)
	/*      */ {
		/* 1875 */ String methodName = "updatePreR3Roles";
		/* 1876 */ List privsForOwners = new ArrayList(SecurityConstants.ALL_PRIVILEGES_P8);
		/* 1877 */ privsForOwners.remove("modifyRoles");
		/* 1878 */ privsForOwners.remove("createNewSearches");
		/*      */
		/* 1880 */ if (modelVersion == null) {
			/* 1881 */ for (Role role : roles) {
				/* 1882 */ boolean isOwner = false;
				/* 1883 */ ArrayList privs = new ArrayList(role.getPrivileges());
				/*      */
				/* 1885 */ if (privs.containsAll(privsForOwners)) {
					/* 1886 */ // Logger.logDebug(this, methodName,
								// this.request, "is owner");
					/* 1887 */ isOwner = true;
					/* 1888 */ } else if (/* (Logger.getLogLevel() > 2) && */
				/* 1889 */ (privsForOwners != null) && (privs != null)) {
					/* 1890 */ Iterator iterator = privsForOwners.iterator();
					/* 1891 */ while (iterator.hasNext()) {
						/* 1892 */ String priv = (String) iterator.next();
						/* 1893 */ if (!privs.contains(priv)) {
							/* 1894 */ // Logger.logDebug(this, methodName,
										// this.request, new
										// StringBuilder().append("missing priv:
										// ").append(priv).append(" in order to
										// be owner").toString());
							/*      */ }
						/*      */ }
					/*      */ }
				/*      */
				/* 1899 */ updatePreV3Privs(privs, modelVersion, isPreR3Mode(), role, isOwner);
				/*      */
				/* 1901 */ role.setPrivileges(privs);
				/* 1902 */ // dumpList(this.request, privs, "model update
							// privs:");
				/*      */ }
			/*      */ }
		/*      */ else
		/*      */ {
			/* 1907 */ // Logger.logDebug(this, methodName, this.request, "is R3
						// model");
			/*      */ }
		/*      */ }

	public static boolean isPreR3Mode() {
		/* 263 */ String methodName = "isPreR3Mode";
		/* 264 */ RepositoryConfig repositoryConfig = null;
		// Config.getRepositoryConfigUsingIdOrServerName(request, null);
		/* 265 */ boolean value = false;
		/* 266 */ if (repositoryConfig != null)/* 267 */ value = repositoryConfig.isTeamspaceOwnerModifyRole();
		/*     */ else {
			/* 269 */ // Logger.logDebug(BaseTeamspaceService.class, methodName,
						// request, "no repository config");
			/*     */ }
		/* 271 */ // Logger.logDebug(BaseTeamspaceService.class, methodName,
					// request, "isTeamspaceOwnerModifyRole: " + value);
		/* 272 */ return value;
		/*     */ }

	public static boolean updatePreV3Privs(List<String> privileges, String modelVersion, boolean preR3Mode, Role role,
			boolean isOwner) {
		/* 331 */ String methodName = "updatePreV3Privs";
		/* 332 */ boolean isOwnerName = false;
		/* 333 */ if (modelVersion == null) {
			/* 334 */ if (!privileges.contains("createNewSearches")) {
				/* 335 */ privileges.add("createNewSearches");
				/* 336 */ // Logger.logDebug(BaseTeamspaceService.class,
							// methodName, request, "added priv
							// createNewSearches for older data model");
				/*     */ }
			/* 338 */ String ownerName = "Owner";
			// resources.getMessage(request.getLocale(), "roles.owner.name");
			/* 339 */ // Logger.logDebug(BaseTeamspaceService.class, methodName,
						// request, "ownerName: " + ownerName);
			/* 340 */ // Logger.logDebug(BaseTeamspaceService.class, methodName,
						// request, "isOwner: " + isOwner);
			/* 341 */ // Logger.logDebug(BaseTeamspaceService.class, methodName,
						// request, "preR3Mode: " + preR3Mode);
			/*     */
			/* 343 */ String roleName = ownerName;
			/* 344 */ String roleDescription = ownerName;
			/* 345 */ if (role != null) {
				/* 346 */ roleName = role.getName();
				/* 347 */ roleDescription = role.getDescription();
				/*     */ }
			/* 349 */ if (roleDescription == null)/* 350 */ roleDescription = "";
			/* 351 */ if ((roleName.equalsIgnoreCase(ownerName)) || (roleDescription.equalsIgnoreCase(ownerName))
					|| (roleName.equalsIgnoreCase(SecurityConstants.AppPrivilegeSets.TEAMSPACE.getValue())))
				/* 352 */ isOwnerName = true;
			/* 353 */ // Logger.logDebug(BaseTeamspaceService.class, methodName,
						// request, "isOwnerName: " + isOwnerName);
			/* 354 */ // Logger.logDebug(BaseTeamspaceService.class, methodName,
						// request, "roleName: " + roleName);
			/*     */
			/* 356 */ if ((preR3Mode) && (!privileges.contains("modifyRoles")) && (isOwner) && (isOwnerName)) {
				/* 357 */ privileges.add("modifyRoles");
				/* 358 */ // Logger.logDebug(BaseTeamspaceService.class,
							// methodName, request, "added priv modifyRoles for
							// older data model");
				/*     */ } else {
				/* 360 */ // Logger.logDebug(BaseTeamspaceService.class,
							// methodName, request, "not adding modifyRoles");
				/*     */ }
			/*     */ } else {
			/* 363 */ // Logger.logDebug(BaseTeamspaceService.class, methodName,
						// request, "is R3 role");
			/*     */ }
		/* 365 */ return isOwnerName;
		/*     */ }

	private void retrieveTeamspaceColumns(Teamspace teamspace) {
		String methodName = "retrieveTeamspaceColumns";
		// Logger.logEntry(this, methodName, this.request);

		SearchTemplateBase.ResultsDisplay colProps = this.teamspaceDB.getColumnProperties();
		teamspace.setColumnProperties(colProps);
		// Logger.logExit(this, methodName, this.request);
	}

	private void validateFolderData(Teamspace teamspace)
	/*      */ {
		/* 1913 */ String methodName = "validateFolderData";
		// Logger.logEntry(this, methodName, this.request);
		/*      */
		/* 1916 */ JSONArray foldersArray = this.teamspaceDB.getFolders();
		// Logger.logDebug(this, methodName, this.request, new
		// StringBuilder().append("foldersArray =
		// ").append(foldersArray).toString());
		/* 1918 */ JSONArray jsonArray = new JSONArray();
		/* 1919 */ JSONArray childItemsArray = new JSONArray();
		/* 1920 */ if (foldersArray != null) {
			// Logger.logDebug(this, methodName, this.request, new
			// StringBuilder().append("number of folders =
			// ").append(foldersArray.size()).toString());
			/* 1922 */ ListIterator iterator = foldersArray.listIterator();
			/* 1923 */ while (iterator.hasNext()) {
				/* 1924 */ JSONObject object = (JSONObject) iterator.next();
				/* 1925 */ removeJsonObject(object, jsonArray, iterator, childItemsArray, true);
				/*      */ }
			/*      */
			/*      */ }
		/*      */
		/* 1930 */ getTeamspaceValidator().removeInvalidJsonFolderChildItems(foldersArray, childItemsArray, jsonArray);
		/* 1931 */ this.teamspaceDB.setFolders(foldersArray);
		/* 1932 */ getTeamspaceValidator().setInvalidFoldersandDocs(jsonArray);
		/*      */
		/* 1934 */ getTeamspaceValidator().cleanUpFolderDataRefs(this.teamspaceDB);
		/*      */ }

	public DistTeamspaceValidator getTeamspaceValidator()
	/*      */ {
		/* 146 */ return this.teamspaceValidator;
		/*      */ }

	private void removeJsonObject(JSONObject object, JSONArray jsonArray, ListIterator iterator,
			JSONArray childItemsArray, boolean removeItems) {
		String methodName = "removeJsonObject";
		String type = (String) object.get("type");
		String name = (String) object.get("name");
		String path = (String) object.get("path");
		String id = (String) object.get("id");

		JSONObject invalidObject = new JSONObject();
		invalidObject.put("type", type);
		invalidObject.put("name", name);
		invalidObject.put("path", path);
		invalidObject.put("id", id);
	
		/* 1954 */ if ((type != null) && (type.equals("folder"))) {
			/* 1955 */ JSONObject rowProperties = (JSONObject) object.get("rowProperties");
			/* 1956 */ JSONObject _value = (JSONObject) rowProperties.get("_value");
			/* 1957 */ JSONObject properties = (JSONObject) _value.get("properties");
			/* 1958 */ String contentClass = null;
			/* 1959 */ if (properties != null)/* 1960 */ contentClass = (String) properties.get("documentType");
			/*      */ else/* 1962 */ contentClass = (String) _value.get("template");
			/* 1963 */// Logger.logDebug(this, methodName, this.request,new
						// StringBuilder().append("contentClass =
						// ").append(contentClass).toString());
			/* 1964 */ ArrayList arrayList = new ArrayList();
			/* 1965 */ arrayList.add(contentClass);
			/* 1966 */ ArrayList invalidClass = getInvalidClasses(arrayList);
			/*      */
			/* 1968 */ if ((invalidClass != null) && (invalidClass.size() > 0)) {
				/* 1969 */ invalidObject.put("contentClass", contentClass);
				/* 1970 */ if (!jsonArray.contains(invalidObject))/* 1971 */ jsonArray.add(invalidObject);
				/* 1972 */ if ((object.get("children") instanceof JSONArray)) {
					/* 1973 */ JSONArray children = null;
					/* 1974 */ children = (JSONArray) object.get("children");
					/* 1975 */ ListIterator referencesIterator = children.listIterator();
					/*      */
					/* 1977 */ while (referencesIterator.hasNext()) {
						/* 1978 */ JSONObject reference = (JSONObject) referencesIterator.next();
						/* 1979 */ String _RefId = (String) reference.get("_reference");
						/* 1980 */ JSONObject foundObject = getTeamspaceValidator().findJsonObjectById(this.teamspaceDB,
								_RefId);
						/* 1981 */ if (foundObject != null) {
							/* 1982 */ childItemsArray.add(foundObject);
							// Logger.logDebug(this, methodName,
							// this.request,"found a child object by
							// reference");
							/* 1984 */ removeJsonObject(foundObject, jsonArray, iterator, childItemsArray, false);
							/*      */ }
						/*      */ }
					/*      */ }
				/*      */ else
				/*      */ {
					/* 1990 */ JSONObject reference = (JSONObject) object.get("children");
					/* 1991 */ if (reference != null) {
						/* 1992 */ String _RefId = (String) reference.get("_reference");
						/* 1993 */ JSONObject foundObject = getTeamspaceValidator().findJsonObjectById(this.teamspaceDB,
								_RefId);
						/* 1994 */ if (foundObject != null) {
							/* 1995 */ childItemsArray.add(foundObject);
							// Logger.logDebug(this, methodName, this.request,
							// "found a child object by reference");
							/* 1997 */ removeJsonObject(foundObject, jsonArray, iterator, childItemsArray, false);
							/*      */ }
						/*      */ }
					/*      */ }
				/* 2001 */ if (removeItems)/* 2002 */ iterator.remove();
				/*      */ }
			/*      */ }
		/* 2005 */ else if ((type != null) && (!type.equals("folder"))) {
			// Logger.logDebug(this, methodName, this.request, "type is
			// document");
			/* 2007 */ String docId = P8Util.getObjectIdentity(id);
			/* 2008 */ boolean isValid = isItemIdValid(docId);
			/* 2009 */ if (!isValid) {
				/* 2010 */ jsonArray.add(invalidObject);
				/* 2011 */ if (removeItems)/* 2012 */ iterator.remove();
				/*      */ }
			/*      */ }
		/*      */ }

	private boolean isItemIdValid(String itemId)
	  {
	    boolean isValid = false;
	    try {
	      Id id = new Id(itemId);
	      Document document = Factory.Document.fetchInstance(this.objectStore, id, null);
	      isValid = true;
	    } catch (Exception e) {
	      isValid = false;
	    }
	    return isValid;
	  }

	private void retrieveTeamspaceEntryTemplates(Teamspace teamspace, boolean validate)
	  {
	    String methodName = "retrieveTeamspaceEntryTemplates";
	   
	    JSONArray jsonArray = new JSONArray();
	    JSONArray entryTemplatesJsonArray = this.teamspaceDB.getEntryTemplates();
	    List entryTemplates = new ArrayList();
	    ArrayList arrayListofAvailableETs = new ArrayList();
	    ArrayList arrayListofInvalidETs = new ArrayList();

	    if (entryTemplatesJsonArray != null) {
	      if (validate) {
	        arrayListofAvailableETs = createArrayListFromJsonArray(entryTemplatesJsonArray);
	        arrayListofInvalidETs = getInvalidEntryTemplates(arrayListofAvailableETs);
	      }
	      ListIterator iterator = entryTemplatesJsonArray.listIterator();
	      while (iterator.hasNext()) {
	        JSONObject object = (JSONObject)iterator.next();
	        String entryTemplateId = (String)object.get("id");
	        entryTemplateId = P8Util.getObjectIdentity(entryTemplateId);
	        
	        if ((validate) && (arrayListofInvalidETs.contains(entryTemplateId))) {
	          jsonArray.add(object.clone());
	          iterator.remove();
	        } else {
	          entryTemplates.add(entryTemplateId);
	        }
	      }
	    }
	    getTeamspaceValidator().setInvalidEntryTemplates(jsonArray);
	    teamspace.setEntryTemplates(entryTemplates);

	  }

	private ArrayList<String> getInvalidEntryTemplates(List<String> templateIds)
	  {
	    String methodName = "validateItemId";
	    DocumentSet templates = null;
	    ArrayList invalidItems = new ArrayList();
	    ArrayList validItems = new ArrayList();
	    if ((templateIds != null) && (!templateIds.isEmpty())) {
	      SearchSQL search = new SearchSQL();
	      search.setSelectList("VersionSeries");
	      search.setFromClauseInitialValue("EntryTemplate", null, true);
	      StringBuilder where = new StringBuilder();
	      for (String templateId : templateIds) {
	        if (where.length() > 0)
	          where.append(" OR ");
	        where.append("Id").append(" = '").append(P8Util.getObjectIdentity(templateId)).append("'");
	      }
	      where.insert(0, "(").append(")");
	      where.append(" AND ").append("MimeType").append(" = '").append("application/x-filenet-documententrytemplate").append("'");
	      search.setWhereClause(where.toString());

	      SearchScope scope = new SearchScope(this.objectStore);

	      templates = (DocumentSet)scope.fetchObjects(search, null, null, Boolean.valueOf(false));
	    }
	    String id = null; String versionSeriesId = null;

	    if (templates != null) {
	      for (Iterator i = templates.iterator(); i.hasNext(); ) {
	        Document template = (Document)i.next();
	        versionSeriesId = template.get_VersionSeries().get_Id().toString();
	        id = P8Util.getDocId(template.getObjectReference());
	        validItems.add(id);
	      }
	      for (int i = 0; i < templateIds.size(); i++) {
	        if (!validItems.contains(templateIds.get(i))) {
	            invalidItems.add(P8Util.getObjectIdentity((String)templateIds.get(i)));
	        } else {
	          //Logger.logDebug(this, methodName, this.request, new StringBuilder().append((String)templateIds.get(i)).append(" is valid").toString());
	        }
	      }
	    }
	    return invalidItems;
	  }

	private void retrieveTeamspaceSearches(Teamspace teamspace, boolean validate) {
		
		 String methodName = "retrieveTeamspaceSearches";
	
		 JSONArray jsonArray = new JSONArray();
		JSONArray searchJsonArray = this.teamspaceDB.getSearches();
		
		this.teamspaceDB.clearSearches();
		List searches = new ArrayList();
		if (searchJsonArray != null) {
			ListIterator iterator = searchJsonArray.listIterator();
			while (iterator.hasNext()) {
				JSONObject object = (JSONObject) iterator.next();
				String searchId = (String) object.get("id");
				String templateName = (String) object.get("name");
				String templateDesc = (String) object.get("description");
				String templateMimeType = (String) object.get("mimeType");
				String templateVsId = (String) object.get("vsId");
				P8DocID p8Id = new P8DocID(searchId);
				searchId = p8Id.getObjectID();
				
				 String currentVersionId = getCurrentSearchVersionId(searchId);
				if (currentVersionId == null) {
					 jsonArray.add(object.clone());
					}
				else {
					searches.add(currentVersionId);
					JSONObject search = new JSONObject();
					 search.put("id",P8Util.getDocId(p8Id.getClassID(), p8Id.getObjectStoreID(), currentVersionId));
				    search.put("name", templateName);
					search.put("description", templateDesc);
					search.put("mimeType", templateMimeType);
					search.put("vsId", templateVsId);
					
					this.teamspaceDB.addSearch(search);
					 }
				 }
			 }
		 getTeamspaceValidator().setInvalidSearches(jsonArray);
		 teamspace.setSearches(searches);
	 }

	private String getCurrentSearchVersionId(String templateId) {
		
		PropertyFilter pf = new PropertyFilter();
		
		pf.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "Name", null));
		 pf.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "Id", null));
		 pf.addIncludeProperty(new FilterElement(Integer.valueOf(1), null, null, "CurrentVersion", null));
		
		String latestId = null;
		 try {
			 Id id = new Id(templateId);
			 Document document = Factory.Document.fetchInstance(this.p8connection.getObjectStore(), id, pf);
			 Versionable versionable = document.get_CurrentVersion();
			 Properties props = ((Document) versionable).getProperties();
			 latestId = props.getIdValue("Id").toString();
			}
		catch (Exception e) {
			 }
		return latestId;
	}

	private void retrieveTeamspaceClasses(Teamspace teamspace, boolean validate)
	/*      */ {
		/* 2321 */ String methodName = "retrieveTeamspaceClasses";
		// Logger.logEntry(this, methodName, this.request);
		/*      */
		/* 2324 */ JSONArray classesJsonArray = this.teamspaceDB.getClasses();
		/* 2325 */ JSONArray jsonArray = new JSONArray();
		/* 2326 */ List classes = new ArrayList();
		// Logger.logDebug(this, methodName, this.request, new
		// StringBuilder().append("cto _classes = ").append(classesJsonArray !=
		// null ? classesJsonArray : "null").toString());
		// Logger.logDebug(this, methodName, this.request, new
		// StringBuilder().append("cto number of classes =
		// ").append(classesJsonArray != null ?
		// Integer.valueOf(classesJsonArray.size()) : "0").toString());
		/* 2329 */ ArrayList arrayListofAvailableClasses = new ArrayList();
		/* 2330 */ ArrayList arrayListofInvalidClasses = new ArrayList();
		/* 2331 */ if (classesJsonArray != null) {
			/* 2332 */ if (validate) {
				/* 2333 */ arrayListofAvailableClasses = createArrayListFromJsonArray(classesJsonArray);
				/* 2334 */ arrayListofInvalidClasses = getInvalidClasses(arrayListofAvailableClasses);
				/*      */ }
			/* 2336 */ ListIterator iterator = classesJsonArray.listIterator();
			/* 2337 */ while (iterator.hasNext()) {
				/* 2338 */ JSONObject object = (JSONObject) iterator.next();
				/* 2339 */ String classesId = (String) object.get("id");
				// Logger.logDebug(this, methodName, this.request, new
				// StringBuilder().append("cto classes Id =
				// ").append(classesId).toString());
				/*      */
				/* 2342 */ if ((validate) && (arrayListofInvalidClasses.contains(classesId))) {
					/* 2343 */ jsonArray.add(object.clone());
					/* 2344 */ iterator.remove();
					/*      */ } else {
					/* 2346 */ classes.add(classesId);
					/*      */ }
				/*      */ }
			/*      */ }
		/*      */
		/* 2351 */ getTeamspaceValidator().setInvalidClasses(jsonArray);
		/* 2352 */ teamspace.setClasses(classes);
		// Logger.logExit(this, methodName, this.request);
		/*      */ }

	private ArrayList<String> createArrayListFromJsonArray(JSONArray jsonArray)
	/*      */ {
		/* 2358 */ ArrayList arrayList = new ArrayList();
		/* 2359 */ if (jsonArray != null) {
			/* 2360 */ ListIterator iterator = jsonArray.listIterator();
			/* 2361 */ while (iterator.hasNext()) {
				/* 2362 */ JSONObject object = (JSONObject) iterator.next();
				/* 2363 */ String id = (String) object.get("id");
				/* 2364 */ arrayList.add(id);
				/*      */ }
			/*      */ }
		/* 2367 */ return arrayList;
		/*      */ }

	private ArrayList<String> getInvalidClasses(ArrayList<String> classes)
	/*      */ {
		/* 365 */ String methodName = "validateItemId";
		// Logger.logEntry(this, methodName, this.request);
		/* 367 */ String[] sl = (String[]) classes.toArray(new String[0]);
		/* 368 */ ClassDescriptionSet classSet = P8FindClassesAction.getClassDefinitions(null, this.objectStore, sl);
		/*      */
		/* 370 */ Iterator classDescriptionIterator = classSet.iterator();
		/* 371 */ ArrayList invalidClasses = new ArrayList();
		/* 372 */ ArrayList validClasses = new ArrayList();
		/* 373 */ String id = null;
		/* 374 */ String name = null;
		/* 375 */ while ((classDescriptionIterator != null) && (classDescriptionIterator.hasNext())) {
			// Logger.logDebug(this, methodName, this.request,new
			// StringBuilder().append("classDescriptionIterator.hasNext() =
			// ").append(classDescriptionIterator.hasNext()).toString());
			/* 377 */ Object nextClass = classDescriptionIterator.next();
			/* 378 */ if ((nextClass instanceof ClassDescription)) {
				/* 379 */ ClassDescription classDescription = (ClassDescription) nextClass;
				/* 380 */ id = classDescription.get_Id().toString();
				/* 381 */ name = classDescription.get_SymbolicName();
				// Logger.logDebug(this, methodName, this.request,new
				// StringBuilder().append("ClassDescription id =
				// ").append(id).toString());
				// Logger.logDebug(this, methodName, this.request,new
				// StringBuilder().append("ClassDescription name =
				// ").append(name).toString());
				/* 384 */ validClasses.add(name);
				/* 385 */ } else if ((nextClass instanceof ClassDefinition)) {
				/* 386 */ ClassDefinition classDef = (ClassDefinition) nextClass;
				/* 387 */ id = classDef.get_Id().toString();
				/* 388 */ name = classDef.get_SymbolicName();
				/* 389 */ // Logger.logDebug(this, methodName, this.request, new
							// StringBuilder().append("ClassDefinition id =
							// ").append(id).toString());
				/* 390 */ // Logger.logDebug(this, methodName, this.request, new
							// StringBuilder().append("ClassDefinition name =
							// ").append(name).toString());
				/* 391 */ validClasses.add(name);
				/*      */ }
			/*      */ }
		/*      */
		/* 395 */ for (int i = 0; i < classes.size(); i++) {
			/* 396 */ if (!validClasses.contains(classes.get(i))) {
				/* 397 */ // Logger.logDebug(this, methodName, this.request, new
							// StringBuilder().append((String)classes.get(i)).append("
							// is invalid").toString());
				/* 398 */ invalidClasses.add(classes.get(i));
				/*      */ }
			/*      */ }
		// Logger.logExit(this, methodName, this.request);
		/* 402 */ return invalidClasses;
	}

	protected void applyRoles(Folder folder, Teamspace teamspace, List<Role> roles, AccessControlList secObjACL)
			throws SecurityException {
		String methodName = "applyRoles";
		// Logger.logEntry(this, methodName, this.request);

		// Logger.logDebug(this, methodName, this.request, "Applying role-based
		// security to teamspace...");
		if ((roles != null) && (roles.size() > 0)) {
			List roleIds = new ArrayList();

			for (Role role : roles) {
				this.securityService.createRole(role, null, secObjACL);
				roleIds.add(role.getId());
				// Logger.logDebug(this, methodName, this.request, new
				// StringBuilder().append("adding role:
				// ").append(role.getId()).toString());
			}
			String teamspaceId = P8Util.getObjectIdentity(teamspace.getId());
			this.securityService.applyRolesToObject(
					new StringBuilder().append(teamspaceId).append("_adapter").toString(), folder, roleIds, secObjACL);
		}
		// Logger.logExit(this, methodName, this.request);
	}

	public String createTeamspace(Teamspace teamspace, List<Role> roles, AccessControlList secObjACL, AccessControlList teamspaceObjACL) throws TeamspaceException {
		String methodName = "createTeamspace";

		// Logger.logEntry(this, methodName, this.request);
		// Logger.logDebug(this, methodName, this.request, new
		// StringBuilder().append("teamspaceType :
		// ").append(teamspace.getType()).toString());

		String teamspaceJson = teamspace.toJSON().toString();
		// Logger.logDebug(this, methodName, this.request, new
		// StringBuilder().append("teamspaceJson :
		// ").append(teamspaceJson).toString());
		Folder folder = null;
		String id = null;
		String type = teamspace.getType();

		this.teamspaceDB = new TeamspaceDB(null,teamspaceJson);
		addModelVersion(teamspace);

		// Logger.logDebug(this, methodName, this.request, new
		// StringBuilder().append("teamspaceType :
		// ").append(teamspace.getType()).toString());
		try {
			String className = "ClbTeamspace";
			Folder parentFolder = null;
			if (teamspace.getType().equals("template")) {
				className = "ClbTeamspaceTemplate";
				parentFolder = Factory.Folder.fetchInstance(this.objectStore, "/ClbTeamspace Templates", null);
			} else {
				parentFolder = getTeamspaceParentFolder(this.objectStore);
			}

			folder = Factory.Folder.createInstance(this.objectStore, className);
			Properties props = folder.getProperties();

			boolean CE5_2 = P8Util.ifCE52OrAbove(this.objectStore);
			if (CE5_2) {
				props.putValue("InheritParentPermissions", false);
			}

			// Logger.logDebug(this, methodName, this.request, new
			// StringBuilder().append("uniqueId for :
			// ").append(teamspace.getName()).toString());
			String folderName = TeamspaceUtil.getUniqueId(teamspace.getName());

			// Logger.logDebug(this, methodName, this.request, new
			// StringBuilder().append("folderName :
			// ").append(folderName).toString());
			folder.set_FolderName(folderName);

			folder.set_Parent(parentFolder);

			// Logger.logDebug(this, methodName, this.request, new
			// StringBuilder().append("parentFolderPath :
			// ").append(parentFolder.get_PathName()).toString());

			String description = teamspace.getDescription();
			// Logger.logDebug(this, methodName, this.request, new
			// StringBuilder().append("teamspace.getDescription() :
			// ").append(description).toString());
			if (teamspace.getDescription() != null) {
				props.putValue("Description", description);
			}

			String name = teamspace.getName();
			// Logger.logDebug(this, methodName, this.request, new
			// StringBuilder().append("teamspace.getName() :
			// ").append(name).toString());
			props.putValue("ClbTeamspaceName", name);

			byte[] jsonBytes = teamspace.getJson().getBytes();
		/*	if (type.equalsIgnoreCase("instance")) {
				jsonBytes = DistTeamspace.convert(teamspace).toInstanceJSONFromTemplate().toString().getBytes();// 
			} else{
				jsonBytes = teamspace.getJson().getBytes();
			}*/
			// Logger.logDebug(this, methodName, this.request, new
			// StringBuilder().append("teamspace.getJson() :
			// ").append(teamspace.getJson()).toString());
			props.putObjectValue("ClbJSONData", jsonBytes);

			// Logger.logDebug(this, methodName, this.request, new
			// StringBuilder().append("teamspace.getState() :
			// ").append(teamspace.getState()).toString());
			if (type.equalsIgnoreCase("instance")) {
				// Logger.logDebug(this, methodName, this.request, new
				// StringBuilder().append("teamspace.getTemplateId() :
				// ").append(teamspace.getTemplateId()).toString());
				if ((teamspace.getTemplateId() != null) && (teamspace.getTemplateId().length() > 0))
					this.teamspaceDB.setTeamspaceTemplateId(teamspace.getTemplateId());
				props.putValue("ClbTeamspaceState", BaseTeamspaceService.State.PUBLISHED.getValue());
			} else if ((teamspace.getState() != null) && (teamspace.getState().length() > 0)) {
				props.putValue("ClbTeamspaceState",
						BaseTeamspaceService.State.getEnum(teamspace.getState()).getValue());
			}

			PropertyFilter pfId = new PropertyFilter();
			pfId.addIncludeProperty(new FilterElement(null, null, null, "Id", null));
			pfId.addIncludeProperty(new FilterElement(null, null, null, "DateLastModified", null));
			pfId.addIncludeProperty(new FilterElement(null, null, null, "LastModifier", null));
			pfId.addIncludeProperty(new FilterElement(null, null, null, "PathName", null));
			pfId.addIncludeProperty(new FilterElement(null, null, null, "ClbSecurityAdapter", null));
			pfId.addIncludeProperty(new FilterElement(null, null, null, "Permissions", null));
			pfId.setMaxRecursion(1);

			// Logger.logDebug(this, methodName, this.request, "Invoking save");

			folder.save(RefreshMode.REFRESH, pfId);
			id = folder.getProperties().getIdValue("Id").toString();
			// Logger.logDebug(this, methodName, this.request, new
			// StringBuilder().append("id: ").append(id).toString());

			/*if ((type.equals("template")) && (permissions != null)) {
				AccessPermissionList apl = folder.get_Permissions();
				Iterator permissionsIter = permissions.iterator();
				while (permissionsIter.hasNext()) {
					AccessPermission ap = (AccessPermission) permissionsIter.next();
					apl.add(ap);
				}

				folder.set_Permissions(apl);
				folder.save(RefreshMode.REFRESH);
			}
*/
			teamspace.setId(id);

			Date lastModifiedDate = folder.getProperties().getDateTimeValue("DateLastModified");
			String lastModified = DateUtil.getISODateString(lastModifiedDate, true);
			String lastModifiedUser = folder.getProperties().getStringValue("LastModifier");
			teamspace.setLastModified(lastModified);
			teamspace.setLastModifiedUser(lastModifiedUser);

			if (type.equals("instance")) {
				applyRoles(folder, teamspace, roles, secObjACL);
				applyACL(teamspace, teamspaceObjACL);
			}

			if (type.equalsIgnoreCase("instance")) {
				JSONObject templateJsonObject = createJsonObjectFromJsonString(teamspace.getJson());

				createTeamspaceItemsFromTemplateJson(folder, templateJsonObject);
				associateEntryTemplatesToTeampace(templateJsonObject, id);
			}

			// Logger.logExit(this, methodName, this.request);
			return id;
		} catch (EngineRuntimeException e) {
			_rollBackItem(folder, type, id);
			// Logger.logError(this, methodName, this.request, e);
			if ((e.getExceptionCode().equals(ExceptionCode.E_NOT_UNIQUE))
					|| (e.getExceptionCode().equals(ExceptionCode.DB_NOT_UNIQUE))) {
				throw new TeamspaceException(new StringBuilder().append("teamspace \"").append(teamspace.getName())
						.append("\" already exists!").toString(), e, 1091);
			}
			throw new TeamspaceException("Failed to create teamspace!", e, 1088);
		} catch (Exception ex) {
			_rollBackItem(folder, type, id);
			// Logger.logError(this, methodName, this.request, ex);
			throw new TeamspaceException("Failed to create teamspace!", ex, 1088);
		}
	}

	private void _rollBackItem(Folder folder, String type, String id) {
		try {
			if ((folder != null) && (id != null))
				deleteTeamspace(id, type);
		} catch (Exception e) {
		}
	}

	public void deleteTeamspace(Teamspace teamspace) throws TeamspaceException {
		deleteTeamspace(teamspace.getId(), teamspace.getType());
	}

	public void deleteTeamspace(String teamspaceId, String type) throws TeamspaceException {
		String methodName = "markTeamspaceInstanceDeleted";
		// Logger.logEntry(this, methodName, this.request);
		try {
			teamspaceId = P8Util.getObjectIdentity(teamspaceId);
			Folder folder = Factory.Folder.fetchInstance(this.objectStore, teamspaceId, null);

			// Logger.logInfo(this, methodName, this.request, new
			// StringBuilder().append("Delete teamspace called
			// ").append(folder.get_FolderName()).toString());

			Properties folderProps = folder.getProperties();

			if (StringUtils.equals(type, "template")) {
				folder.delete();
				folder.save(RefreshMode.REFRESH);
				// Logger.logInfo(this, methodName, this.request, new
				// StringBuilder().append("Teamspace template
				// ").append(teamspaceId).append(" was deleted").toString());
			} else {
				Property stateProp = folderProps.get("ClbTeamspaceState");
				stateProp.setObjectValue(Integer.valueOf(BaseTeamspaceService.State.getEnum("deleted").getValue()));
				folder.save(RefreshMode.NO_REFRESH);
				// Logger.logInfo(this, methodName, this.request, new
				// StringBuilder().append("Teamspace instance
				// ").append(teamspaceId).append(" was marked as
				// deleted").toString());
			}

			// Logger.logExit(this, methodName, this.request);
		} catch (EngineRuntimeException e) {
			if (e.getExceptionCode().equals(ExceptionCode.E_OBJECT_NOT_FOUND)) {
				throw new TeamspaceException(new StringBuilder().append("Teamspace with id \"").append(teamspaceId)
						.append("\" does not exist!").toString(), e, 1093);
			}
			throw new TeamspaceException("Failed to delete the teamspace!", e, 1090);
		} catch (Exception e) {
			throw new TeamspaceException("Failed to delete the teamspace !", e, 1090);
		}
	}

	private void associateEntryTemplatesToTeampace(JSONObject templateJsonObject, String folderId)
			throws TeamspaceException {
		String methodName = "associateEntryTemplatesToTeampace";
		// Logger.logEntry(this, methodName, this.request);

		JSONArray entryTemplatesJsonArray = (JSONArray) templateJsonObject.get("entryTemplates");
		String usesClasses = (String) templateJsonObject.get("usesClasses");

		if ((usesClasses != null) && (usesClasses.equals("false")) && (entryTemplatesJsonArray != null)) {
			// Logger.logDebug(this, methodName, this.request, new
			// StringBuilder().append("templateJsonObject size:
			// ").append(templateJsonObject.size()).toString());
			// Logger.logDebug(this, methodName, this.request, new
			// StringBuilder().append("templateJsonObject:
			// ").append(templateJsonObject.toString()).toString());
			Iterator iterator = entryTemplatesJsonArray.listIterator();
			JSONArray entryTemplatesAssociationJsonArray = new JSONArray();
			while (iterator.hasNext()) {
				JSONObject jsonClass = (JSONObject) iterator.next();
				JSONObject associationObject = new JSONObject();
				String id = (String) jsonClass.get("id");
				boolean currentFolderAsParent = ((Boolean) jsonClass.get("currentFolderAsParent")).booleanValue();
				JSONArray fileTypesJSONArray = (JSONArray) jsonClass.get("fileTypes");
				if (fileTypesJSONArray == null) {
					fileTypesJSONArray = new JSONArray();
				}

				String defaultFlag = (String) jsonClass.get("default");
				String type = (String) jsonClass.get("type");

				// Logger.logDebug(this, methodName, this.request, new
				// StringBuilder().append("docId: ").append(id).toString());
				// Logger.logDebug(this, methodName, this.request, new
				// StringBuilder().append("currentFolderAsParent:
				// ").append(currentFolderAsParent).toString());
				// Logger.logDebug(this, methodName, this.request, new
				// StringBuilder().append("fileTypes:
				// ").append(fileTypesJSONArray.toString()).toString());
				// Logger.logDebug(this, methodName, this.request, new
				// StringBuilder().append("default:
				// ").append(defaultFlag).toString());

				if ((id != null) && (!id.isEmpty())) {
					SearchSQL search = new SearchSQL();
					search.setSelectList("VersionSeries");
					search.setFromClauseInitialValue("EntryTemplate", null, true);
					StringBuilder where = new StringBuilder();
					where.append("Id").append(" = '").append(P8Util.getObjectIdentity(id)).append("'");

					search.setWhereClause(where.toString());

					SearchScope scope = new SearchScope(this.objectStore);
					DocumentSet templates = null;
					templates = (DocumentSet) scope.fetchObjects(search, null, null, Boolean.valueOf(false));
					Iterator i = templates.iterator();
					if (i.hasNext()) {
						Document template = (Document) i.next();
						associationObject.put("entryTemplateVsId", template.get_VersionSeries().get_Id().toString());

						associationObject.put("currentFolderAsParent", Boolean.valueOf(currentFolderAsParent));
						associationObject.put("fileTypes", fileTypesJSONArray);
						associationObject.put("type", type);

						if ((defaultFlag != null) && (!defaultFlag.isEmpty())) {
							associationObject.put("default", defaultFlag);
						}

						entryTemplatesAssociationJsonArray.add(associationObject);
					}
				}
			}

			try {
				P8FolderTemplates.associateTemplatesWithFolder(this.p8connection, this.objectStore,
						entryTemplatesAssociationJsonArray.toString(), folderId);
			} catch (Exception e) {
				// Logger.logError(this, methodName, this.request, e);
			}
		} else {
			// Logger.logDebug(this, methodName, this.request,
			// "entryTemplateJsonArray is null or empty");
		}

		// Logger.logExit(this, methodName, this.request);
	}

	private void createTeamspaceItemsFromTemplateJson(Folder rootFolder, JSONObject templateJsonObject)
			throws TeamspaceException {
		String methodName = "createTeamspaceItemsFromTemplateJson";
		// Logger.logEntry(this, methodName, this.request);

		Folder parentFolder = null;
		boolean isRoot = true;
		JSONArray itemsJsonArray = (JSONArray) templateJsonObject.get("folders");
		if ((itemsJsonArray != null) && (!itemsJsonArray.isEmpty())) {
			Iterator iterator = itemsJsonArray.listIterator();
			while (iterator.hasNext()) {
				JSONObject jsonItem = (JSONObject) iterator.next();
				String path = (String) jsonItem.get("path");
				String type = (String) jsonItem.get("type");
				String name = (String) jsonItem.get("name");
				//JSONObject rowProperties = (JSONObject) jsonItem.get("rowProperties");// weifj
				//if(null == rowProperties) continue;
				
				JSONObject properties = (JSONObject) jsonItem.get("properties");

				// Logger.logDebug(this, methodName, this.request, new
				// StringBuilder().append("path: ").append(path).toString());
				// Logger.logDebug(this, methodName, this.request, new
				// StringBuilder().append("type: ").append(type).toString());
				// Logger.logDebug(this, methodName, this.request, new
				// StringBuilder().append("name: ").append(name).toString());
				// Logger.logDebug(this, methodName, this.request, new
				// StringBuilder().append("properties:
				// ").append(properties).toString());

				if ((!path.equalsIgnoreCase("/")) && (properties != null)) {
					if (parentFolder == null) {
						// Logger.logDebug(this, methodName, this.request,
						// "parentFolder was null, setting it to the root
						// folder");
						parentFolder = rootFolder;

						// Logger.logDebug(this, methodName, this.request, new
						// StringBuilder().append("parentFolder path:
						// ").append(parentFolder.get_PathName()).toString());
					} else {
						// Logger.logDebug(this, methodName, this.request, "is
						// not root folder");
						isRoot = false;
					}

					path = new StringBuilder().append(rootFolder.get_PathName()).append(path).toString();
					// Logger.logDebug(this, methodName, this.request, new
					// StringBuilder().append("updated path with teamspace root
					// path to form full path: ").append(path).toString());

					String parentPath = getParentFldPath(path, name);
					// Logger.logDebug(this, methodName, this.request, new
					// StringBuilder().append("parentPath:
					// ").append(parentPath).toString());

					// Logger.logDebug(this, methodName, this.request, new
					// StringBuilder().append("Fetch parent folder using path:
					// ").append(parentPath).toString());
					PropertyFilter pf = new PropertyFilter();
					pf.addIncludeProperty(new FilterElement(null, null, null, "Id", null));
					pf.addIncludeProperty(new FilterElement(null, null, null, "PathName", null));
					pf.setMaxRecursion(1);

					if ((!isRoot) && (parentFolder != null)) {
						parentFolder = Factory.Folder.fetchInstance(this.objectStore, parentPath, pf);
					}
					// Logger.logDebug(this, methodName, this.request, new
					// StringBuilder().append("found folder:
					// ").append(parentFolder.get_PathName()).toString());

					if ((type != null) && (type.equals("folder"))) {
						// Logger.logDebug(this, methodName, this.request,
						// "create folder");

						String className = (String) properties.get("documentType");
						// Logger.logDebug(this, methodName, this.request, new
						// StringBuilder().append("className =
						// ").append(className).toString());

						String criterias = properties.get("criterias").toString();
						// Logger.logDebug(this, methodName, this.request, new
						// StringBuilder().append("criterias:
						// ").append(criterias).toString());
						ClassDescription classDescription = Factory.ClassDescription.fetchInstance(this.objectStore,
								className, null);

						Id classDescId = classDescription.get_Id();
						// Logger.logDebug(this, methodName, this.request, new
						// StringBuilder().append(" classDescription.get_Id(); =
						// ").append(classDescId).toString());

						Folder folder = Factory.Folder.createInstance(this.objectStore, classDescId.toString());

						folder.set_Parent(parentFolder);
						try {
							JSONArray criteriasJsonArray = JSONArray.parse(criterias);
							DistP8Util.setProperties(this.objectStore, this.p8connection, folder, criteriasJsonArray,
									classDescription, P8Util.ModifyType.ADD);
						} catch (Exception e) {
							// Logger.logError(this, methodName, this.request,
							// "Error setting the citeria data on the folder
							// object");
							// Logger.logError(this, methodName, this.request,
							// e);
							throw new TeamspaceException(e, 1088);
						}
						folder.save(RefreshMode.NO_REFRESH);
					} else {
						// Logger.logDebug(this, methodName, this.request, new
						// StringBuilder().append("about to add a document:
						// ").append(name).toString());
						String docId = (String) properties.get("docid");
						// Logger.logDebug(this, methodName, this.request, new
						// StringBuilder().append("securityInheritance :
						// ").append(properties.get("securityInheritance")).toString());
						String securityInheritanceString = (String) properties.get("securityInheritance") != null
								? (String) properties.get("securityInheritance") : "false";
						boolean securityInheritance = new Boolean(securityInheritanceString).booleanValue();

						Id id = new Id(P8Util.getObjectIdentity(docId));
						// Logger.logDebug(this, methodName, this.request,
						// "fetching the item");
						try {
							Document originalDocument = Factory.Document.fetchInstance(this.objectStore, id, null);

							Boolean copy = (Boolean) jsonItem.get("IsCopy");
							boolean isCopy = (copy != null) && (copy.booleanValue());

							if (isCopy) {
								createDocumentCopy(parentFolder, originalDocument);
							} else {
								DefineSecurityParentage securityParentage = DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE;
								if (securityInheritance) {
									securityParentage = DefineSecurityParentage.DEFINE_SECURITY_PARENTAGE;
								}
								// Logger.logDebug(this, methodName,
								// this.request, new
								// StringBuilder().append("about to file
								// document id: ").append(id).toString());
								ReferentialContainmentRelationship rcr = parentFolder.file(originalDocument,
										AutoUniqueName.AUTO_UNIQUE, null, securityParentage);
								rcr.save(RefreshMode.NO_REFRESH);
							}
						} catch (Exception e) {
							e.printStackTrace();
							// Logger.logError(this, methodName, this.request,
							// e);
						}
					}
				}
			}
		} else {
			// Logger.logDebug(this, methodName, this.request, "foldersJsonArray
			// is null or empty");
		}

		// Logger.logExit(this, methodName, this.request);
	}

	private String getParentFldPath(String path, String objName) {
		String methodName = "getParentFldPath";

		int indxName = path.lastIndexOf(objName);
		String parentPath;
		if (indxName >= 0) {
			// Logger.logDebug(this, methodName, this.request, new
			// StringBuilder().append("indxName:
			// ").append(indxName).toString());
			if (path.charAt(indxName - 1) == '/')
				indxName--;
			parentPath = path.substring(0, indxName);
		} else {
			int lastIndexofPathSymbol = path.lastIndexOf("/");
			// Logger.logDebug(this, methodName, this.request, new
			// StringBuilder().append("lastIndexofPathSymbol:
			// ").append(lastIndexofPathSymbol).toString());
			parentPath = path.substring(0, lastIndexofPathSymbol);
		}
		return parentPath;
	}

	private void createDocumentCopy(Folder parentFolder, Document originalDocument) {
		String classId = originalDocument.get_ClassDescription().getObjectReference().getObjectIdentity();
		Document newCopy = Factory.Document.createInstance(this.objectStore, classId);

		Properties originalProps = originalDocument.getProperties();
		Properties copyProps = newCopy.getProperties();
		Property[] originalPropsArray = originalProps.toArray();

		for (Property p : originalPropsArray) {
			String propertyName = p.getPropertyName();
			boolean settable = p.isSettable();
			if (settable) {
				Object value = p.getObjectValue();
				copyProps.putObjectValue(propertyName, value);
			}
		}

		ContentElementList originalContentElements = originalDocument.get_ContentElements();
		int numberOfContentElements = originalContentElements.size();
		ContentElementList newCopyContentElements = Factory.ContentElement.createList();
		for (int i = 0; i < numberOfContentElements; i++) {
			ContentTransfer element = (ContentTransfer) originalContentElements.get(i);
			String contentType = element.get_ContentType();
			InputStream originalContentInputStream = element.accessContentStream();

			ContentTransfer ctObject = Factory.ContentTransfer.createInstance();
			ctObject.setCaptureSource(originalContentInputStream);
			ctObject.set_ContentType(contentType);

			newCopyContentElements.add(ctObject);
		}

		newCopy.set_ContentElements(newCopyContentElements);
		newCopy.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
		newCopy.set_SecurityFolder(parentFolder);
		newCopy.save(RefreshMode.NO_REFRESH);

		ReferentialContainmentRelationship rcr = parentFolder.file(newCopy, AutoUniqueName.AUTO_UNIQUE, null,
				DefineSecurityParentage.DEFINE_SECURITY_PARENTAGE);
		rcr.save(RefreshMode.NO_REFRESH);
	}

	private JSONObject createJsonObjectFromJsonString(String jsonString) throws TeamspaceException {
		String methodName = "createJsonObjectFromJsonString";
		// Logger.logEntry(this, methodName, this.request);
		JSONObject jsonObject = null;
		// Logger.logDebug(this, methodName, this.request, new
		// StringBuilder().append("jsonString:
		// ").append(jsonString).toString());
		try {
			jsonObject = (JSONObject) JSON.parse(jsonString);
		} catch (Exception e) {
			// Logger.logError(this, methodName, this.request, e);
			throw new TeamspaceException(e, 1087);
		}

		// Logger.logExit(this, methodName, this.request);
		return jsonObject;
	}

	private void applyACL(Teamspace teamspace, AccessControlList teamspaceObjACL) throws SecurityException {
		if (teamspace.getType().equals("instance")) {
			String teamspaceId = P8Util.getObjectIdentity(teamspace.getId());
			IndependentlyPersistableObject ipo = Factory.Folder.fetchInstance(this.objectStore, new Id(teamspaceId),
					null);
			this.securityService.applyACL(teamspaceObjACL,
					(AccessPermissionList) ipo.getProperties().getObjectValue("Permissions"));
			ipo.save(RefreshMode.REFRESH);
		}
	}

	private Folder getTeamspaceParentFolder(ObjectStore objectStore) {
		String methodName = "getTeamspaceParentFolder";
		// Logger.logEntry(this, methodName, this.request);

		Folder parentFolder = null;

		GregorianCalendar currentDate = new GregorianCalendar();
		String year = String.valueOf(currentDate.get(1));
		// Logger.logDebug(this, methodName, this.request, new
		// StringBuilder().append("Year is : ").append(year).toString());

		String month = String.format("%02d", new Object[] { Integer.valueOf(currentDate.get(2) + 1) });
		// Logger.logDebug(this, methodName, this.request, new
		// StringBuilder().append("Month is : ").append(month).toString());

		StringBuilder monthFolderPath = new StringBuilder("/ClbTeamspaces");

		monthFolderPath.append("/");
		monthFolderPath.append(year);
		monthFolderPath.append("/");
		monthFolderPath.append(month);
		try {
			// Logger.logDebug(this, methodName, this.request, new
			// StringBuilder().append("Attempting to retrieve parent folder:
			// ").append(monthFolderPath.toString()).toString());
			parentFolder = Factory.Folder.fetchInstance(objectStore, monthFolderPath.toString(), null);
		} catch (EngineRuntimeException e) {
			// Logger.logDebug(this, methodName, this.request, new
			// StringBuilder().append("Parent folder not found:
			// ").append(monthFolderPath.toString()).toString());
			if (e.getExceptionCode() != ExceptionCode.E_OBJECT_NOT_FOUND) {
				throw e;
			}

		}

		StringBuilder yearFolderPath = new StringBuilder("/ClbTeamspaces");
		yearFolderPath.append("/");
		yearFolderPath.append(year);
		Folder yearFolder = null;
		Boolean checkForYearFolder = Boolean.valueOf(false);

		if (parentFolder != null) {
			Properties parentFolderProperties = parentFolder.getProperties();
			Boolean parentFolderInherits = parentFolderProperties.getBooleanValue("InheritParentPermissions");
			if (!parentFolderInherits.booleanValue()) {
				Folder oldYearFolder = null;
				try {
					// Logger.logDebug(this, methodName, this.request, new
					// StringBuilder().append("Attempting to retrieve old year
					// folder: ").append(yearFolderPath.toString()).toString());
					oldYearFolder = Factory.Folder.fetchInstance(objectStore, yearFolderPath.toString(), null);
				} catch (EngineRuntimeException e) {
					// Logger.logDebug(this, methodName, this.request, new
					// StringBuilder().append("Old year folder not found:
					// ").append(yearFolderPath.toString()).toString());
					if (e.getExceptionCode() != ExceptionCode.E_OBJECT_NOT_FOUND) {
						throw e;
					}
				}
				if (oldYearFolder != null) {
					StringBuilder newFolderName = new StringBuilder(year);
					newFolderName.append("-old");
					Properties yearFolderProperties = oldYearFolder.getProperties();
					yearFolderProperties.putValue("FolderName", newFolderName.toString());
					oldYearFolder.save(RefreshMode.REFRESH, null);
					parentFolder = null;
				}
			} else {
				checkForYearFolder = Boolean.valueOf(true);
			}
		} else {
			checkForYearFolder = Boolean.valueOf(true);
		}

		if (checkForYearFolder.booleanValue() == true) {
			try {
				// Logger.logDebug(this, methodName, this.request, new
				// StringBuilder().append("Attempting to retrieve year folder:
				// ").append(yearFolderPath.toString()).toString());
				yearFolder = Factory.Folder.fetchInstance(objectStore, yearFolderPath.toString(), null);
			} catch (EngineRuntimeException e) {
				// Logger.logDebug(this, methodName, this.request, new
				// StringBuilder().append("Year folder not found:
				// ").append(monthFolderPath.toString()).toString());
				if (e.getExceptionCode() != ExceptionCode.E_OBJECT_NOT_FOUND) {
					throw e;
				}
			}
		}

		if (yearFolder == null) {
			// Logger.logDebug(this, methodName, this.request, new
			// StringBuilder().append("Creating year folder:
			// ").append(year).toString());
			yearFolder = Factory.Folder.createInstance(objectStore, "Folder");
			Properties yearFolderProperties = yearFolder.getProperties();
			yearFolderProperties.putValue("FolderName", year);
			yearFolderProperties.putValue("IsHiddenContainer", true);
			yearFolderProperties.putValue("InheritParentPermissions", true);
			Folder rootFolder = Factory.Folder.fetchInstance(objectStore, "/ClbTeamspaces", null);
			yearFolder.set_Parent(rootFolder);

			// Logger.logDebug(this, methodName, this.request, new
			// StringBuilder().append("Saving year folder:
			// ").append(year).toString());
			yearFolder.save(RefreshMode.REFRESH, null);
		}

		if (parentFolder == null) {
			// Logger.logDebug(this, methodName, this.request, new
			// StringBuilder().append("Creating month folder:
			// ").append(month).toString());
			parentFolder = Factory.Folder.createInstance(objectStore, "Folder");
			Properties parentFolderProperties = parentFolder.getProperties();
			parentFolderProperties.putValue("FolderName", month);
			parentFolderProperties.putValue("IsHiddenContainer", true);
			parentFolderProperties.putValue("InheritParentPermissions", true);
			parentFolder.set_Parent(yearFolder);

			// Logger.logDebug(this, methodName, this.request, new
			// StringBuilder().append("Saving month folder:
			// ").append(month).toString());
			parentFolder.save(RefreshMode.REFRESH, null);
		}

		// Logger.logExit(this, methodName, this.request);
		return parentFolder;
	}
	public Teamspace retrieveTeamspaceByName(String teamspaceName, String type, boolean validateTemplateData)
		    throws Exception
		  {
		    Teamspace teamSpace = null;

		    String className = "ClbTeamspace";

		    if (type.equals("template")) {
		      className = "ClbTeamspaceTemplate";
		    }

		    SearchSQL sqlObject = new SearchSQL();
		    sqlObject.setSelectList("*");
		    sqlObject.setMaxRecords(100);
		    sqlObject.setFromClauseInitialValue(className, "e", false);
		    sqlObject.setWhereClause(new StringBuilder().append("ClbTeamspaceName='").append(teamspaceName).append("'").toString());

		    SearchScope search = new SearchScope(this.objectStore);

		    IndependentObjectSet myObjects = search.fetchObjects(sqlObject, Integer.valueOf(100), null, null);
		    Iterator iter = myObjects.iterator();

		    while (iter.hasNext()) {
		      IndependentlyPersistableObject ipo = (IndependentlyPersistableObject)iter.next();
		      teamSpace = createTeamspaceObject(ipo, null, type, validateTemplateData);
		    }

		    return teamSpace;
		  }
	
	public Teamspace retrieveTeamspace(String teamspaceId, String retrievalOptions, String type, boolean validateTemplateData)
		    throws TeamspaceException
		  {
		    String methodName = "retrieveTeamspace";
		    //Logger.logEntry(this, methodName, this.request);
		    Teamspace teamspace = null;
		    try {
		      //Logger.logDebug(this, methodName, this.request, new StringBuilder().append("teamspaceId: ").append(teamspaceId).toString());
		      //Logger.logDebug(this, methodName, this.request, new StringBuilder().append("retrievalOptions: ").append(retrievalOptions).toString());
		      //Logger.logDebug(this, methodName, this.request, new StringBuilder().append("type: ").append(type).toString());

		      PropertyFilter pf = new PropertyFilter();
		      pf.addIncludeProperty(1, null, null, "ClbTeamspaceState", null);
		      pf.addIncludeProperty(1, null, null, "Description", null);
		      pf.addIncludeProperty(1, null, null, "ClbTeamspaceName", null);
		      pf.addIncludeProperty(1, null, null, "Id", null);
		      pf.addIncludeProperty(1, null, null, "Name", null);
		      pf.addIncludeProperty(1, null, null, "Description", null);

		      teamspaceId = P8Util.getObjectIdentity(teamspaceId);

		      Folder teamspaceFolderObject = Factory.Folder.fetchInstance(this.objectStore, teamspaceId, null);

		      //Logger.logDebug(this, methodName, this.request, new StringBuilder().append("teamspace name: ").append(teamspaceFolderObject.get_Name()).toString());

		      teamspace = createTeamspaceObject(teamspaceFolderObject, retrievalOptions, type, validateTemplateData);
		      logTeamspaceObject(teamspace, methodName);
		    }
		    catch (EngineRuntimeException e) {
		      //Logger.logError(this, methodName, this.request, e);
		      if ((e.getExceptionCode().equals(ExceptionCode.E_OBJECT_NOT_FOUND)) && (type.equals("instance")))
		        throw new TeamspaceException(new StringBuilder().append("teamspace with id \"").append(teamspaceId).append("\" was not found!").toString(), e, 1003);
		      if (e.getExceptionCode().equals(ExceptionCode.E_OBJECT_NOT_FOUND))
		        throw new TeamspaceException(new StringBuilder().append("teamspace with id \"").append(teamspaceId).append("\" does not exist!").toString(), e, 1093);
		    }
		    catch (Throwable e) {
		      //Logger.logError(this, methodName, this.request, e);
		      throw new TeamspaceException("Failed to retrieve the teamspace !", e, 1092);
		    }

		   // Logger.logExit(this, methodName, this.request);
		    return teamspace;
		  }
	
	/**
	 * 添加团队空间
	 * @param teamspaceTemplateSymbolicName 团队空间模板标识名称
	 */
	public String createTeamspace(String teamspaceTemplateSymbolicName, String name, String description, List<Subject> team){
		
		 Teamspace workspaceObject = null;
		 Teamspace temp = null;
			try {
				List workspaces = retrieveTeamspaces("template", "published", false);
				Iterator iterator = workspaces.iterator();
				
				while (iterator.hasNext()) {
					temp = (Teamspace) iterator.next();
					if (workspaceObject.getName().equalsIgnoreCase(teamspaceTemplateSymbolicName)) {
						System.out.println("    找到目标，teamspace name：" + workspaceObject.getName());
						workspaceObject =temp;
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(null == workspaceObject) { 
				System.out.println("没有找到对应的teamspace模板："+teamspaceTemplateSymbolicName);
				return "";
			
			}
			return createTeamspace(workspaceObject, name, description, team);
		
	}
	/**
	 * 添加团队空间
	 * @param teamspaceTemplate 团队空间模板
	 */
	public String createTeamspace(Teamspace teamspaceTemplate, String name, String description, List<Subject> team){

		    String teamspaceType = "instance";
		    String className = "ClbTeamspace";

		    DistTeamspace teamspace = new DistTeamspace(name,description, teamspaceType, className, DistTeamspace.convert(teamspaceTemplate).exportTemplateToJSON().toString(),
					team);

		    String teamspaceJson =  teamspace.toInstanceJSON().toString();
		    teamspace.setJson(teamspaceJson);
		    try
		    {
		      JSONObject jsonObject = JSONObject.parse(teamspaceJson);
		
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
		        secObjAclData.add(new P8AccessControlListData(null, this.p8connection.getUserId(), null, SecurityConstants.PrincipalType.USER, Integer.valueOf(Integer.parseInt("998903")), P8AccessControlListData.InheritableDepth.NONE, null, AccessType.ALLOW));
		        AccessControlList secObjACL = new AccessControlList("", secObjAclData);

		        List teamspaceAclData = new ArrayList();
		        teamspaceAclData.add(new P8AccessControlListData(null,  this.p8connection.getUserId(), null, SecurityConstants.PrincipalType.USER, Integer.valueOf(Integer.parseInt("998903")), P8AccessControlListData.InheritableDepth.NONE, null, AccessType.ALLOW));
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

		      String teamspaceId = createTeamspace(teamspace, roles, secObjACL, teamspaceObjACL);

		      return teamspaceId;
		    }
		    catch (Exception e) {
		    System.out.println("创建teamspace失败。");
		     e.printStackTrace();
		    }
		 
		    return "";
	}
}
