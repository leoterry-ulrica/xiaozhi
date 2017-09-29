package com.dist.bdf.facade.service.biz.domain.dcm.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dist.bdf.base.utils.DateUtil;
import com.dist.bdf.facade.service.biz.domain.dcm.TeamSpaceDmn;
import com.dist.bdf.manager.ecm.security.ConnectionService;
import com.dist.bdf.manager.ecm.security.SecurityUtil;
import com.dist.bdf.manager.ecm.teamspace.DistTeamspace;
import com.dist.bdf.manager.ecm.teamspace.p8.DistP8TeamspaceService;
import com.dist.bdf.model.dto.dcm.TeamMemberDTO;
import com.dist.bdf.model.dto.dcm.TeamSpaceDTO;
import com.dist.bdf.model.dto.dcm.TeamSpaceDetailDTO;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.property.Properties;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.security.User;
import com.ibm.ecm.security.Role;
import com.ibm.ecm.security.Subject;
import com.ibm.ecm.teamspace.Teamspace;
import com.ibm.ecm.util.p8.P8Connection;

/**
 * 
 * @author weifj
 * @version 1.0，2016/03/29，weifj，创建实现
 */
@Service
public class TeamSpaceDmnImpl implements TeamSpaceDmn {

	@Autowired
	private static ConnectionService ConnectionService;

	private List<TeamSpaceDTO> searchAllSimple(String type) {
		List<TeamSpaceDTO> linklist = new LinkedList<TeamSpaceDTO>();

		try {
			ConnectionService.initialize();
			DistP8TeamspaceService workspaceService = new DistP8TeamspaceService(
					ConnectionService.getDefaultP8Connection().getCEConnection(),
					ConnectionService.getDefaultP8Connection().getDomain(),
					ConnectionService.getDefaultP8Connection().getObjectStore(),
					ConnectionService.getDefaultP8Connection());

			List<Teamspace> workspaces = workspaceService.retrieveTeamspaces(type, "published", false);
			if (workspaces != null && workspaces.size() > 0) {

				for (Teamspace ts : workspaces) {

					TeamSpaceDTO dto = new TeamSpaceDTO();
					dto.setName(ts.getName());
					dto.setGuid(ts.getId().split(",")[2]);
					dto.setType(ts.getType());
					dto.setLastModified(ts.getLastModified());
					dto.setLastModifiedUser(ts.getLastModifiedUser());

					linklist.add(dto);
				}
			}

			ConnectionService.release();

		} catch (Exception ex) {
			ex.printStackTrace();
			ConnectionService.release();
		}

		return linklist;
	}

	@Override
	public List<TeamSpaceDTO> searchAllTemplate() {

		return searchAllSimple("template");
	}

	@Override
	public List<TeamSpaceDTO> searchAllInstance() {

		return searchAllSimple("instance");
	}

	@Override
	public List<TeamSpaceDetailDTO> searchAllInstanceDetail() {

		List<TeamSpaceDetailDTO> linklist = new LinkedList<TeamSpaceDetailDTO>();

		ConnectionService.initialize();

		SearchSQL sqlObject = new SearchSQL();
		sqlObject.setSelectList(
				"ts.ClassDescription, ts.ClbTeamspaceName, ts.ClbTeamspaceState, ts.Creator, ts.DateCreated, ts.DateLastModified, ts.Description, ts.FolderName, ts.Id, ts.LastModifier, ts.Name, ts.Owner, ts.PathName");
		sqlObject.setFromClauseInitialValue("ClbTeamspace", "ts", false);

		System.out.println("Query to retrieve teamspaces=" + sqlObject.toString());
		SearchScope ss = new SearchScope(ConnectionService.getDefaultOS());
		IndependentObjectSet ios = ss.fetchObjects(sqlObject, (Integer) null, (PropertyFilter) null, Boolean.FALSE);
		Folder teamspaceFolder = null;
		Iterator it = ios.iterator();

		while (it.hasNext()) {
			teamspaceFolder = (Folder) it.next();
			Properties properties = teamspaceFolder.getProperties();
			TeamSpaceDetailDTO dto = new TeamSpaceDetailDTO();
			dto.setClassDescription(properties.getStringValue("ClassDescription"));
			dto.setTeamspaceName(properties.getStringValue("ClbTeamspaceName"));
			dto.setTeamspaceState(properties.getObjectValue("ClbTeamspaceState").toString());
			dto.setCreator(properties.getStringValue("Creator"));
			dto.setDateCreated(DateUtil.toDateTimeStr(properties.getDateTimeValue("DateCreated")));
			dto.setDateLastModified(DateUtil.toDateTimeStr(properties.getDateTimeValue("DateLastModified")));
			dto.setDescription(properties.getStringValue("Description"));
			dto.setFolderName(properties.getStringValue("FolderName"));
			dto.setGuid(properties.getStringValue("Id"));
			dto.setLastModifier(properties.getStringValue("LastModifier"));
			dto.setName(properties.getStringValue("Name"));
			dto.setOwner(properties.getStringValue("Owner"));
			dto.setPathName(properties.getStringValue("PathName"));
			linklist.add(dto);
		}
		ConnectionService.release();
		return linklist;
	}

	@Override
	public String createTeamspace(String userName, String userPwd, TeamSpaceDTO tsTemplateDto) {

		String teamspaceId = "";
		try {
			P8Connection p8conn = ConnectionService.getP8Connection(userName, userPwd);
			DistP8TeamspaceService p8TSService = new DistP8TeamspaceService(p8conn.getCEConnection(),
					p8conn.getDomain(), p8conn.getObjectStore(), p8conn);
			// 查询指定名称模板
			Teamspace workspaceTemplate = null;
			Teamspace temp = null;
			List<Teamspace> workspaces = p8TSService.retrieveTeamspaces("template", "published", false);
			Iterator<Teamspace> iterator = workspaces.iterator();
			while (iterator.hasNext()) {
				temp = (Teamspace) iterator.next();
				if (temp.getName().equalsIgnoreCase(tsTemplateDto.getTemplateName())) {
					workspaceTemplate = temp;
					break;
				}
			}
			List<Subject> team = new ArrayList<Subject>();
			Map<String, Role> map = (DistTeamspace.convert(workspaceTemplate)).getTemplateRolesMapRefName();
			System.out.println("团队成员：");
			User currUser = Factory.User.fetchCurrent(p8conn.getCEConnection(), null);
			TeamMemberDTO[] membsers = tsTemplateDto.getTeam();
			for (TeamMemberDTO mem : membsers) {

				Subject subject = SecurityUtil.getSecurityObjByShortName(p8conn.getCEConnection(), mem.getName(),
						mem.getType());

				if (currUser.get_Name().equals(subject.getName())) {// 特别重要，否则导致创建的人都没有权限删除teamspace
					subject.setId("CURRENT_USER");
				}
				String[] roleNames = mem.getRoleNames();
				List<Role> rolesOfUser = new ArrayList<Role>();
				for (String roleName : roleNames) {
					rolesOfUser.add(map.get(roleName));
				}

				subject.setRoles(rolesOfUser);
				team.add(subject);

			}
			teamspaceId = p8TSService.createTeamspace(workspaceTemplate, tsTemplateDto.getName(),
					tsTemplateDto.getDescription(), team);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return teamspaceId;
	}
}
