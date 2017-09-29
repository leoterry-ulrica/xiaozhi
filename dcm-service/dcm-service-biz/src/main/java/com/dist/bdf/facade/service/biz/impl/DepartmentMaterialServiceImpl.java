package com.dist.bdf.facade.service.biz.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.facade.service.DepartmentMaterialService;
import com.dist.bdf.facade.service.UserOrgService;
import com.dist.bdf.facade.service.uic.domain.DcmUserdomainroleDmn;
import com.dist.bdf.common.conf.ecm.ECMConf;
import com.dist.bdf.common.constants.DomainType;
import com.dist.bdf.common.constants.RoleConstants;
import com.dist.bdf.manager.ecm.security.ConnectionService;
import com.dist.bdf.manager.ecm.utils.FolderUtil;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
import com.dist.bdf.model.entity.system.DcmDicOrgExt;
import com.dist.bdf.model.entity.system.DcmOrganization;
import com.dist.bdf.model.entity.system.DcmUser;
import com.dist.bdf.model.entity.system.DcmUserdomainrole;
import com.filenet.api.core.Folder;
import com.ibm.ecm.util.p8.P8Connection;


/**
 * 
 * @author weifj
 *
 */
@Service("DepartmentMaterialService")
@Transactional(propagation = Propagation.REQUIRED)
public class DepartmentMaterialServiceImpl extends CommonMaterialServiceImpl  implements DepartmentMaterialService {

	private static Logger logger = LoggerFactory.getLogger(DepartmentMaterialServiceImpl.class);
	
	@Autowired
	private DcmUserdomainroleDmn udrDmn;
	/*@Autowired
	private DcmOrganizationDmn orgDmn;*/
	@Autowired
	private FolderUtil folderUtil;
	@Autowired
	private ECMConf ecmConf;
	@Autowired
	private UserOrgService userOrgService;
	@Autowired
	private ConnectionService connectionService;
	
	private static Map<String, DocumentDTO> folderPathMap = Collections.synchronizedMap(new HashMap<String, DocumentDTO>());

	@Override
	public Object getRootFolderAdmin(String realm, String userId) {

		List<DocumentDTO> folderDTOs = new ArrayList<DocumentDTO>();
		String folderPath = null;
		Folder folder = null;
		DocumentDTO folderDTO = null;
		DcmUser findUser = userOrgService.getUserEntityByDN(userId);
	
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));

		// 需要先根据当前用户的角色（所管），使用所在机构的编码作为根文件夹名称
		/*List<DcmUserdomainrole> udrs = udrDmn.getByUserIdDomainType(findUser.getId(), DomainType.Department,
				RoleConstants.RoleCode.R_Department_Manager);
		if (udrs != null && !udrs.isEmpty()) {
			logger.info(">>>说明当前人是所管......");
			for (DcmUserdomainrole udr : udrs) {

				DcmOrganization findOrg = this.userOrgService.getOrgByCode(udr.getDomainCode());
				folderPath = (ecmConf.getDepartmentDirRoot().endsWith("/"))
						? (ecmConf.getDepartmentDirRoot() + findOrg.getOrgCode())
						: (ecmConf.getDepartmentDirRoot() + "/" + findOrg.getOrgCode());
				if (!folderPathMap.containsKey(folderPath)) {
					folder = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), folderPath);
					folderDTO = this.folderUtil.folder2dto(folder);
					folderDTO.setName(findOrg.getAlias()); // 作为机构资料显示名字
					folderDTO.setText(findOrg.getAlias()); // 作为存储别名
					folderDTOs.add(folderDTO);
					folderPathMap.put(folderPath, folderDTO);
				}
			}
			return folderPathMap.values();
		}

		return null;*/
		// 只要是所成员都能看到自己所的资料
		List<DcmUserdomainrole> udrs = udrDmn.getByUserIdDomainType(findUser.getId(), DomainType.DEPARTMENT);
		if (udrs != null && !udrs.isEmpty()) {
			logger.info(">>>当前人属于某个所......");
			List<String> tempDomainCodes = new ArrayList<>();
			for (DcmUserdomainrole udr : udrs) {

				if(tempDomainCodes.contains(udr.getDomainCode())){
					continue;
				}
				DcmOrganization findOrg = this.userOrgService.getOrgByCode(udr.getDomainCode());
				folderPath = (ecmConf.getDepartmentDirRoot().endsWith("/"))
						? (ecmConf.getDepartmentDirRoot() + findOrg.getOrgCode())
						: (ecmConf.getDepartmentDirRoot() + "/" + findOrg.getOrgCode());
						
				if (!folderPathMap.containsKey(folderPath)) {
					folder = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), folderPath);
					folderDTO = this.folderUtil.folder2dto(folder);
					folderDTO.setName(findOrg.getOrgCode()); // 作为机构编码
					folderDTO.setText(findOrg.getAlias()); // 作为存储别名
					folderDTOs.add(folderDTO);
		
					folderPathMap.put(folderPath, folderDTO);
				}else {
					folderDTOs.add(folderPathMap.get(folderPath));
				}
				tempDomainCodes.add(udr.getDomainCode());
			}
			return folderDTOs;
		} else {
			logger.info(">>>当前人不属于任何所......");
		}

		return null;
	}
	
	@Override
	public Object getRootAndTeamFolderAdmin(String realm, String userId) {

		Map<String, DocumentDTO> rootFolderPathMap = Collections
				.synchronizedMap(new ConcurrentHashMap<String, DocumentDTO>());

		List<DocumentDTO> folderDTOs = new ArrayList<DocumentDTO>();
		String orgFolderPath = "";
		String teamFolderPath = "";
		Folder folder = null;
		DocumentDTO folderDTO = null;
		DcmUser findUser = userOrgService.getUserEntityByCode(userId);
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));

		// 需要先根据当前用户的角色（所管），使用所在机构的编码作为根文件夹名称
		// 只要是所成员都能看到自己所的资料
		List<DcmUserdomainrole> udrs = udrDmn.getByUserIdDomainType(findUser.getId(), DomainType.DEPARTMENT);
		if (udrs != null && !udrs.isEmpty()) {
			logger.info(">>>当前人属于某个所......");

			for (DcmUserdomainrole udr : udrs) {

				DcmOrganization findOrg = this.userOrgService.getOrgByCode(udr.getDomainCode());
				orgFolderPath = (ecmConf.getDepartmentDirRoot().endsWith("/"))
						? (ecmConf.getDepartmentDirRoot() + findOrg.getOrgCode())
						: (ecmConf.getDepartmentDirRoot() + "/" + findOrg.getOrgCode());

				if (rootFolderPathMap.containsKey(orgFolderPath)) {
					folderDTO = rootFolderPathMap.get(orgFolderPath);
				} else {
					folder = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), orgFolderPath);
					folderDTO = this.folderUtil.folder2dto(folder);
					folderDTO.setName(findOrg.getOrgCode()); // 作为机构编码
					folderDTO.setText(findOrg.getAlias()); // 作为存储别名
					folderDTOs.add(folderDTO);
				}
				// 自动创建“团队”文件夹
				List<DcmDicOrgExt> teams = this.userOrgService.getOrgTeams(findOrg.getId());
				if (teams != null && !teams.isEmpty()) {
					folderDTO.setChildren(new ArrayList<DocumentDTO>(teams.size()));
					for (DcmDicOrgExt team : teams) {
						teamFolderPath = (ecmConf.getDepartmentDirRoot().endsWith("/"))
								? (ecmConf.getDepartmentDirRoot() + findOrg.getOrgCode() + "/" + team.getName())
								: (ecmConf.getDepartmentDirRoot() + "/" + findOrg.getOrgCode() + "/" + team.getName());
						folder = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), teamFolderPath);
						folderDTO.getChildren().add(this.folderUtil.folder2dto(folder));
					}
				}
			}
			return folderDTOs;
		} else {
			logger.info(">>>当前人不属于任何所......");
		}

		return null;
	}

	@Override
	public Object getRootFolderAdmin(String realm, String userId, String pwd) {
		
		List<DocumentDTO> folderDTOs = new ArrayList<DocumentDTO>();
		String folderPath = null;
		Folder folder = null;
		DocumentDTO folderDTO = null;
		DcmUser findUser = userOrgService.getUserEntityByDN(userId);
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm), userId, pwd);

		// 需要先根据当前用户的角色（所管），使用所在机构的编码作为根文件夹名称
		List<DcmUserdomainrole> udrs = udrDmn.getByUserIdDomainType(findUser.getId(), DomainType.DEPARTMENT,
				RoleConstants.RoleCode.R_Department_Manager);
		if (udrs != null && !udrs.isEmpty()) {
			// 说明当前人是所管，有权限
			for (DcmUserdomainrole udr : udrs) {

				DcmOrganization findOrg = this.userOrgService.getOrgByCode(udr.getDomainCode());
				folderPath = (ecmConf.getDepartmentDirRoot().endsWith("/"))
						? (ecmConf.getDepartmentDirRoot() + findOrg.getOrgCode())
						: (ecmConf.getDepartmentDirRoot() + "/" + findOrg.getOrgCode());
				if (!folderPathMap.containsKey(folderPath)) {
					folder = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), folderPath);
					folderDTO = this.folderUtil.folder2dto(folder);
					folderDTO.setText(findOrg.getAlias()); // 作为存储别名
					folderDTOs.add(folderDTO);
					folderPathMap.put(folderPath, folderDTO);
				}
			}
			return folderPathMap.values();
		}

		return null;
		
	}

	
}
