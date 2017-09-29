package com.dist.bdf.facade.service.biz.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.facade.service.InstituteMaterialService;
import com.dist.bdf.facade.service.UserOrgService;
import com.dist.bdf.facade.service.uic.domain.DcmUserdomainroleDmn;
import com.dist.bdf.common.conf.ecm.ECMConf;
import com.dist.bdf.common.constants.DomainType;
import com.dist.bdf.common.constants.RoleConstants;
import com.dist.bdf.manager.ecm.security.ConnectionService;
import com.dist.bdf.manager.ecm.utils.FolderUtil;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
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
@Service("InstituteMaterialService")
@Transactional(propagation = Propagation.REQUIRED)
public class InstituteMaterialServiceImpl extends CommonMaterialServiceImpl implements InstituteMaterialService {

	@Autowired
	private DcmUserdomainroleDmn udrDmn;
	@Autowired
	private FolderUtil folderUtil;
	@Autowired
	private ECMConf ecmConf;
	@Autowired
	private UserOrgService userOrgService;
	@Autowired
	private ConnectionService connectionService;

	private static Map<String, DocumentDTO> folderPathMap = Collections.synchronizedMap(new HashMap<String, DocumentDTO>());

	@Deprecated
	@Override
	public Object getRootFolderAdmin(String realm, String userId) {

		List<DocumentDTO> folderDTOs = new ArrayList<DocumentDTO>();
		String folderPath = null;
		Folder folder = null;
		DocumentDTO folderDTO = null;
		DcmUser findUser = userOrgService.getUserEntityByDN(userId);
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));

		// 需要先根据当前用户的角色（院管），使用所在机构的编码作为根文件夹名称
	/*	List<DcmUserdomainrole> udrs = udrDmn.getByUserIdDomainType(findUser.getId(), DomainType.Institute,
				RoleConstants.RoleCode.R_Institute_Manager);
		if (udrs != null && !udrs.isEmpty()) {
			// 说明当前人是院管，有权限
			for (DcmUserdomainrole udr : udrs) {

				DcmOrganization findOrg = this.userOrgService.getOrgByCode(udr.getDomainCode());
				folderPath = (ecmConf.getInstituteDirRoot().endsWith("/"))
						? (ecmConf.getInstituteDirRoot() + findOrg.getOrgCode())
						: (ecmConf.getInstituteDirRoot() + "/" + findOrg.getOrgCode());
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

		return null;*/
		// 返回相关院包
		DcmOrganization institute = userOrgService.getInstituteByUserSeqId(findUser.getId());
		if(null == institute ){ 
			return null;
		}
		
		folderPath = (ecmConf.getInstituteDirRoot().endsWith("/"))
				? (ecmConf.getInstituteDirRoot() + institute.getOrgCode())
				: (ecmConf.getInstituteDirRoot() + "/" + institute.getOrgCode());
				
		if (!folderPathMap.containsKey(folderPath)) {
			folder = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), folderPath);
			folderDTO = this.folderUtil.folder2dto(folder);
			folderDTO.setName(institute.getOrgCode()); // 机构编码
			folderDTO.setText(institute.getAlias()); // 作为存储别名
			folderDTOs.add(folderDTO);
			folderPathMap.put(folderPath, folderDTO);
		} else { 
			folderDTOs.add(folderPathMap.get(folderPath));
		}

		return folderDTOs;
	}
	
	@Override
	public Object getRootFolderAdminEx(String realm, String userId) {

		List<DocumentDTO> folderDTOs = new ArrayList<DocumentDTO>();
		String folderPath = null;
		Folder folder = null;
		DocumentDTO folderDTO = null;
		DcmUser findUser = userOrgService.getUserEntityByCode(userId);
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));

		// 需要先根据当前用户的角色（院管），使用所在机构的编码作为根文件夹名称
		// 返回相关院包
		DcmOrganization institute = userOrgService.getInstituteByUserSeqId(findUser.getId());
		if(null == institute ) return null;
		
		folderPath = (ecmConf.getInstituteDirRoot().endsWith("/"))
				? (ecmConf.getInstituteDirRoot() + institute.getOrgCode())
				: (ecmConf.getInstituteDirRoot() + "/" + institute.getOrgCode());
				
		if (!folderPathMap.containsKey(folderPath)) {
			folder = this.folderUtil.loadAndCreateByPath(p8conn.getObjectStore(), folderPath);
			folderDTO = this.folderUtil.folder2dto(folder);
			folderDTO.setName(institute.getOrgCode()); // 机构编码
			folderDTO.setText(institute.getAlias()); // 作为存储别名
			folderDTOs.add(folderDTO);
			folderPathMap.put(folderPath, folderDTO);
		}else {
			folderDTOs.add(folderPathMap.get(folderPath));
		}

		return folderDTOs;
	}
	
	@Override
	public Object getRootFolderAdmin(String realm, String userId, String pwd) {
		
		List<DocumentDTO> folderDTOs = new ArrayList<DocumentDTO>();
		String folderPath = null;
		Folder folder = null;
		DocumentDTO folderDTO = null;
		DcmUser findUser = userOrgService.getUserEntityByDN(userId);
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm),userId, pwd);

		// 需要先根据当前用户的角色（院管），使用所在机构的编码作为根文件夹名称
		List<DcmUserdomainrole> udrs = udrDmn.getByUserIdDomainType(findUser.getId(), DomainType.INSTITUTE,
				RoleConstants.RoleCode.R_Institute_Manager);
		if (udrs != null && !udrs.isEmpty()) {
			// 说明当前人是所管，有权限
			for (DcmUserdomainrole udr : udrs) {

				DcmOrganization findOrg = this.userOrgService.getOrgByCode(udr.getDomainCode());
				folderPath = (ecmConf.getInstituteDirRoot().endsWith("/"))
						? (ecmConf.getInstituteDirRoot() + findOrg.getOrgName())
						: (ecmConf.getInstituteDirRoot() + "/" + findOrg.getOrgName());
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
