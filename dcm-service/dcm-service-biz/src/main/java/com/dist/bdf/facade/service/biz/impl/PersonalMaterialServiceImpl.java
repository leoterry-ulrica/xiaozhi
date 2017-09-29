package com.dist.bdf.facade.service.biz.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.base.utils.DistThreadManager;
import com.dist.bdf.base.utils.StringUtil;
import com.dist.bdf.facade.service.PersonalMaterialService;
import com.dist.bdf.facade.service.biz.domain.system.DcmDownloadDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmPersonalMaterialDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmShareDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmSocialResourceDmn;
import com.dist.bdf.common.conf.ecm.ECMConf;
import com.dist.bdf.common.constants.ResourceConstants;
import com.dist.bdf.manager.ecm.security.PrivilegeFactory;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
import com.dist.bdf.model.dto.system.DownloadParaDTO;
import com.dist.bdf.model.dto.system.DownloadResponseDTO;
import com.dist.bdf.model.dto.system.FavoriteParaDTO;
import com.dist.bdf.model.dto.system.PersonalPcksResponseDTO;
import com.dist.bdf.model.dto.system.SocialResourceDTO;
import com.dist.bdf.model.dto.system.page.PageSimple;
import com.dist.bdf.model.dto.system.pm.MaterialFavoriteDTO;
import com.dist.bdf.model.entity.system.DcmDownload;
import com.dist.bdf.model.entity.system.DcmPersonalmaterial;
import com.dist.bdf.model.entity.system.DcmSocialResource;
import com.dist.bdf.model.entity.system.DcmUser;
import com.filenet.api.core.Folder;
import com.ibm.ecm.util.p8.P8Connection;

import net.sf.json.JSONObject;

/**
 * 
 * @author weifj
 * @version 1.0，2016/04/27，weifj
 */
@Service("PersonalMaterialService")
@Transactional(propagation = Propagation.REQUIRED)
public class PersonalMaterialServiceImpl  extends CommonMaterialServiceImpl implements PersonalMaterialService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private DcmSocialResourceDmn socialResDmn;
	@Autowired
	private DcmPersonalMaterialDmn personalMaterialDmn;
	@Autowired
	private DcmDownloadDmn downloadDmn;
	@Autowired
	private DcmShareDmn shareDmn;
	@Autowired
	private ECMConf ecmConf;

	@Override
	public Boolean saveFavorite(final FavoriteParaDTO dto) {
		
		String resType = ResourceConstants.ResourceType.getResTypeCode(dto.getType());
		if (StringUtil.isNullOrEmpty(resType)) {
			return false;
		}
		DcmPersonalmaterial pm = this.personalMaterialDmn.getByResId(dto.getId());

		if (null == pm) {

			pm = new DcmPersonalmaterial();
			pm.setResId(dto.getId());
			pm.setParentResId("");
			pm.setDateCreated(new Date());
			pm.setResTypeCode(resType);
			pm.setIsFolder(0);
			pm.setCreator(""); // 当前用户是收藏的用户，并非资源创建者
			this.personalMaterialDmn.add(pm);
		}
		SocialResourceDTO srDTO = new SocialResourceDTO();
		srDTO.setGuid(dto.getId());
		srDTO.setIsFavorite(dto.getIsFavorite());
		srDTO.setIsTop(Boolean.FALSE);
		srDTO.setTag("");
		srDTO.setParentResId(pm.getParentResId());
		srDTO.setCreator(dto.getUser());
		this.socialResDmn.savaSocialData(srDTO, pm.getResTypeCode());

		// 添加或减少收藏数
		DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				PersonalMaterialServiceImpl.this.addFavoriteCountOfSummaryData(dto.getRealm(), dto.getId(), dto.getIsFavorite());
			}
		});
		
		return Boolean.TRUE;
	}
	@Override
	public Boolean saveFavoriteEx(final FavoriteParaDTO dto) {

		DcmPersonalmaterial pm = this.personalMaterialDmn.getByResId(dto.getId());

		if (null == pm) {

			pm = new DcmPersonalmaterial();
			pm.setResId(dto.getId());
			pm.setParentResId("");
			pm.setDateCreated(new Date());
			pm.setResTypeCode(dto.getResType());
			pm.setIsFolder(0);
			pm.setCreator(""); // 当前用户是收藏的用户，并非资源创建者
			this.personalMaterialDmn.add(pm);
		}
		SocialResourceDTO srDTO = new SocialResourceDTO();
		srDTO.setGuid(dto.getId());
		srDTO.setIsFavorite(dto.getIsFavorite());
		srDTO.setIsTop(Boolean.FALSE);
		srDTO.setTag("");
		srDTO.setParentResId(pm.getParentResId());
		srDTO.setCreator(dto.getUser());
		this.socialResDmn.savaSocialData(srDTO, pm.getResTypeCode());

		// 添加或减少收藏数
		DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				PersonalMaterialServiceImpl.this.addFavoriteCountOfSummaryData(dto.getRealm(), dto.getId(), dto.getIsFavorite());
			}
		});
		
		return Boolean.TRUE;
	}

	@Override
	public Boolean deleteMaterial(String jsonString) {

		JSONObject jsonObj = JSONObject.fromObject(jsonString);
		String id = jsonObj.getString("id");
		// String creator = jsonObj.getString("creator");
		// 递归删除资源
		this.deleteSubMaterialRecursion(id);
		//this.personalMaterialDmn.removeByProperty("parentResId", id);
		// 删除本资源
		//this.personalMaterialDmn.removeByProperty("resId", id);
		// 删除收藏记录
		this.socialResDmn.removeByProperty("resId", id);
		// 共享记录
		this.shareDmn.removeByProperty("resId", id);
		
		return Boolean.TRUE;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public Object getFavorite(String json) {

		JSONObject jsonObj = JSONObject.fromObject(json);
		String user = jsonObj.getString("user");
		int pageNo = jsonObj.getInt("pageNo");
		int pageSize = jsonObj.getInt("pageSize");

		// 获取当前用户的角色

		Map<String, Object[]> queryPara = new HashMap<String, Object[]>();
		queryPara.put("creator", new Object[] { user });
		queryPara.put("resTypeCode",
				new Object[] { "Res_Pck_Person", "Res_Pck_Project", "Res_Pck_Institute", "Res_Pck_Department" });
		queryPara.put("isFavorite", new Object[] { 1L });// 注意：需要传入long型

		Pagination page = this.socialResDmn.findByPropertiesAndValues(pageNo, pageSize, queryPara, "timeFavorite",
				Boolean.FALSE);
		List<MaterialFavoriteDTO> data = new ArrayList<MaterialFavoriteDTO>();
		List<DcmSocialResource> list = (List<DcmSocialResource>) page.getData();
		if (list != null && list.size() > 0) {

			for (DcmSocialResource sr : list) {
				MaterialFavoriteDTO mf = new MaterialFavoriteDTO();
				mf.setId(sr.getResId());
				mf.setType(ResourceConstants.ResourceType.getClientResType(sr.getResTypeCode()));

				data.add(mf);
			}

		}
		page.setData(data);
		return page;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getFavoriteEx(PageSimple pageInfo) {

		/*	JSONObject jsonObj = JSONObject.fromObject(json);*/
		String user = pageInfo.getUser();//jsonObj.getString("user");
		int pageNo = pageInfo.getPageNo();//jsonObj.getInt("pageNo");
		int pageSize = pageInfo.getPageSize();//jsonObj.getInt("pageSize");

		// 获取当前用户的角色

		Map<String, Object[]> queryPara = new HashMap<String, Object[]>();
		queryPara.put("creator", new Object[] { user });
		queryPara.put("resTypeCode",
				new Object[] { ResourceConstants.ResourceType.RES_PCK_PERSON, ResourceConstants.ResourceType.RES_PCK_PROJECT, ResourceConstants.ResourceType.RES_PCK_INSTITUTE, ResourceConstants.ResourceType.RES_PCK_DEPARTMENT });
		queryPara.put("isFavorite", new Object[] { 1L });// 注意：需要传入long型

		Pagination page = this.socialResDmn.findByPropertiesAndValues(pageNo, pageSize, queryPara, "timeFavorite",
				Boolean.FALSE);
		List<MaterialFavoriteDTO> data = new ArrayList<MaterialFavoriteDTO>();
		List<DcmSocialResource> list = (List<DcmSocialResource>) page.getData();
		if (list != null && list.size() > 0) {

			for (DcmSocialResource sr : list) {
				MaterialFavoriteDTO mf = new MaterialFavoriteDTO();
				mf.setId(sr.getResId());
				mf.setFavoriteTime(sr.getTimeFavorite().getTime());
				mf.setType(ResourceConstants.ResourceType.getClientResType(sr.getResTypeCode()));
				
				// 已去掉这个mask值判断
				// logger.info(">>>根据权限mask值判断是否有下载权限......");
				// Long privMasks = this.privService.getDocPrivMasksByVID(pageInfo.getRealm(), pageInfo.getUser(), sr.getResId());
				// mf.setHavePriv(PrivilegeFactory.haveDownload(privMasks));
				mf.setHavePriv(false);
				mf.setShare(super.isShare(sr.getResId()));
				data.add(mf);
			}

		}
		page.setData(data);
		return page;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getDownloadLogs(PageSimple pageInfo) {

		/*JSONObject jsonObj = JSONObject.fromObject(json);*/
		String user = pageInfo.getUser();//jsonObj.getString("user");
		int pageNo = pageInfo.getPageNo();//jsonObj.getInt("pageNo");
		int pageSize = pageInfo.getPageSize();// jsonObj.getInt("pageSize");

		StringBuilder sb = new StringBuilder();
		sb.append("select t.id, t.resid, t.downloader, t.createtime, t.restypecode, t.domaincode ");
		sb.append(" from (");
		sb.append(" select t.id, t.resid, t.downloader, t.createtime, t.restypecode, t.domaincode, "
				+ "row_number() OVER(PARTITION BY t.resid ORDER BY t.createtime desc) as row_flg from dcm_download t where t.downloader = '"+user+"'");
	    sb.append(") T");
	    sb.append(" where t.row_flg = '1'");
	    sb.append(" ORDER BY t.createtime DESC ");
	         
		Pagination page = downloadDmn.queryBySql(pageNo, pageSize, sb.toString(), DcmDownload.class);//.findByProperty(pageNo, pageSize, "downloader", user, "createTime", false);

		List<DcmDownload> data = (List<DcmDownload>) page.getData();
		List<DownloadResponseDTO> newData = new ArrayList<DownloadResponseDTO>();
		if (data != null && data.size() > 0) {
			for (DcmDownload dl : data) {
				DownloadResponseDTO dto = new DownloadResponseDTO();
				dto.setId(dl.getResId());
				dto.setType(ResourceConstants.ResourceType.getClientResType(dl.getResTypeCode()));
				dto.setDownloadTime(dl.getCreateTime().getTime());
				
				logger.info(">>>判断是否有再次下载的权限......");
				Long privMasks = this.privService.getDocPrivMasksByGUID(pageInfo.getRealm(), pageInfo.getUser(), dl.getResId());
				dto.setHavePriv(PrivilegeFactory.haveDownload(privMasks));
				
				newData.add(dto);
			}
		}
		page.setData(newData);
		return page;
	}
	
	@Override
	public Object delDownloadLogsByResIds(DownloadParaDTO dto) {
		
		this.downloadDmn.delDownloadLogByResIds(dto.getUser(), dto.getResIds());
		return true;
	}

	@Deprecated
	@Override
	public List<DocumentDTO> getPersonalPackages(String realm, String userId, String pwd) {
		
		DcmUser user = this.userOrgService.getUserEntityByDN(userId);
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm), userId, pwd);
		return this.personalMaterialDmn.getPersonalPackages(p8conn.getObjectStore(), user.getUserCode());
	}
	@Override
	public Object getPersonalPackages(String realm, String userId) {
		
		DcmUser user = this.userOrgService.getUserEntityByDN(userId.toLowerCase());
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		 
		Folder personRootFolder = this.personalMaterialDmn.getPersonalFolder(p8conn.getObjectStore(), user.getUserCode());
		PersonalPcksResponseDTO perResp = new PersonalPcksResponseDTO();
		perResp.setRootFolderId(personRootFolder.get_Id().toString());
		perResp.setDocs(this.personalMaterialDmn.getPersonalPackages(p8conn.getObjectStore(), user.getUserCode()));
		
		return perResp;
	}

}
