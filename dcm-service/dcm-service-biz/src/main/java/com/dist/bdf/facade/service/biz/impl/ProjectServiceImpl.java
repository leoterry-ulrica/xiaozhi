package com.dist.bdf.facade.service.biz.impl;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.dist.bdf.base.dao.Sort;
import com.dist.bdf.base.dto.DateFilterRangeDTO;
import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.base.security.DesEncryptRSA;
import com.dist.bdf.base.utils.Base64Utils;
import com.dist.bdf.base.utils.DateUtil;
import com.dist.bdf.base.utils.DistThreadManager;
import com.dist.bdf.base.utils.FileUtil;
import com.dist.bdf.base.utils.StringUtil;
import com.dist.bdf.base.utils.mail.MailUtil;
import com.dist.bdf.facade.service.GroupService;
import com.dist.bdf.facade.service.ProjectService;
import com.dist.bdf.facade.service.biz.domain.dcm.CaseMgmtDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmDownloadDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmGroupDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmNaotuTeamDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmShareDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmSocialResourceDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmTaskDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmTaskMaterialDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmTeamDmn;
import com.dist.bdf.facade.service.biz.domain.system.DcmWorkgroupDmn;
import com.dist.bdf.facade.service.biz.impl.cache.ProjectStatCacheDTO;
import com.dist.bdf.facade.service.biz.impl.cache.WzNoThumbCacheDTO;
import com.dist.bdf.facade.service.biz.task.ComputeMyProjectTask;
import com.dist.bdf.facade.service.biz.task.ComputeProjectSummaryTask;
import com.dist.bdf.facade.service.biz.task.ComputeTaskSummaryTask;
import com.dist.bdf.facade.service.sga.SgaProjectService;
import com.dist.bdf.facade.service.sga.SgaUserService;
import com.dist.bdf.facade.service.uic.domain.DcmUserdomainroleDmn;
import com.dist.bdf.common.conf.common.MailConf;
import com.dist.bdf.common.conf.ecm.ECMConf;
import com.dist.bdf.common.conf.ecm.ExtPropertyCaseConf;
import com.dist.bdf.common.conf.ecm.ExtPropertyMaterialConf;
import com.dist.bdf.common.conf.ecm.ExtPropertyWZConf;
import com.dist.bdf.common.conf.imgserver.ImgServerConf;
import com.dist.bdf.common.conf.wechat.WechatConf;
import com.dist.bdf.common.constants.DomainType;
import com.dist.bdf.common.constants.ResourceConstants;
import com.dist.bdf.common.constants.RoleConstants;
import com.dist.bdf.common.constants.WZTypeConstants;
import com.dist.bdf.manager.cache.CacheStrategy;
import com.dist.bdf.manager.ecm.define.DataType;
import com.dist.bdf.manager.ecm.define.SimplePropertyFilter;
import com.dist.bdf.manager.ecm.security.ConnectionService;
import com.dist.bdf.manager.ecm.security.PrivilegeFactory;
import com.dist.bdf.manager.ecm.utils.CaseUtil;
import com.dist.bdf.manager.ecm.utils.FolderUtil;
import com.dist.bdf.manager.ecm.utils.SearchEngine;
import com.dist.bdf.manager.mongo.MongoFileStorageDmn;
import com.dist.bdf.manager.wechat.WechatManager;
import com.dist.bdf.model.dto.dcm.CaseDTO;
import com.dist.bdf.model.dto.dcm.CaseTypeAndDataDTO;
import com.dist.bdf.model.dto.dcm.CaseTypeDTO;
import com.dist.bdf.model.dto.dcm.DocumentDTO;
import com.dist.bdf.model.dto.dcm.PropertiesExDTO;
import com.dist.bdf.model.dto.sga.ImgInfo;
import com.dist.bdf.model.dto.sga.UserResponseDTO;
import com.dist.bdf.model.dto.system.ChannelDTO;
import com.dist.bdf.model.dto.system.GroupDTO;
import com.dist.bdf.model.dto.system.GroupSummaryDTO;
import com.dist.bdf.model.dto.system.HotProjectSummaryDTO;
import com.dist.bdf.model.dto.system.IndexInfo;
import com.dist.bdf.model.dto.system.M2ultipartFileDTO;
import com.dist.bdf.model.dto.system.NaotuTeamAddDTO;
import com.dist.bdf.model.dto.system.Org2UsersDTO;
import com.dist.bdf.model.dto.system.ProjectExcelDTO;
import com.dist.bdf.model.dto.system.ProjectStatDTO;
import com.dist.bdf.model.dto.system.ProjectSummaryDTO;
import com.dist.bdf.model.dto.system.ProjectTeamDTO;
import com.dist.bdf.model.dto.system.ProjectUserDTO;
import com.dist.bdf.model.dto.system.TaskAddDTO;
import com.dist.bdf.model.dto.system.TaskAndProjectRespDTO;
import com.dist.bdf.model.dto.system.TaskResponseDTO;
import com.dist.bdf.model.dto.system.TaskSummaryFilterDTO;
import com.dist.bdf.model.dto.system.ThumbnailDTO;
import com.dist.bdf.model.dto.system.WzEmailResponseDTO;
import com.dist.bdf.model.dto.system.WzInfoDTO;
import com.dist.bdf.model.dto.system.WzInfoPageDTO;
import com.dist.bdf.model.dto.system.WzInfoParaDTO;
import com.dist.bdf.model.dto.system.WzInfoParaWebDTO;
import com.dist.bdf.model.dto.system.WzInfoSendToUserDTO;
import com.dist.bdf.model.dto.system.WzNewResponseDTO;
import com.dist.bdf.model.dto.system.priv.PrivTemplateDTO;
import com.dist.bdf.model.dto.system.team.TeamLeaderRequestDTO;
import com.dist.bdf.model.dto.system.team.TeamUserDTO;
import com.dist.bdf.model.dto.system.user.BaseUserDTO;
import com.dist.bdf.model.dto.system.workgroup.WorkGroupAddDTO;
import com.dist.bdf.model.dto.system.workgroup.WorkGroupOrgAddDTO;
import com.dist.bdf.model.dto.system.workgroup.WorkGroupRespDTO;
import com.dist.bdf.model.dto.wechat.AccessToken;
import com.dist.bdf.model.dto.wechat.TemplateMsgData;
import com.dist.bdf.model.dto.wechat.WechatTemplate;
import com.dist.bdf.model.entity.sga.SgaPrjUser;
import com.dist.bdf.model.entity.sga.SgaPrjWz;
import com.dist.bdf.model.entity.system.DcmDicChannel;
import com.dist.bdf.model.entity.system.DcmGroup;
import com.dist.bdf.model.entity.system.DcmNaotuTeam;
import com.dist.bdf.model.entity.system.DcmOrganization;
import com.dist.bdf.model.entity.system.DcmSocialResource;
import com.dist.bdf.model.entity.system.DcmTask;
import com.dist.bdf.model.entity.system.DcmTeam;
import com.dist.bdf.model.entity.system.DcmTeamUser;
import com.dist.bdf.model.entity.system.DcmUser;
import com.dist.bdf.model.entity.system.DcmUserdomainrole;
import com.dist.bdf.model.entity.system.DcmWgOrg;
import com.dist.bdf.model.entity.system.DcmWorkGroup;
import com.filenet.api.collection.StringList;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.property.Properties;
import com.google.common.base.Objects;
import com.ibm.ecm.util.p8.P8Connection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
/**
 * 
 * @author weifj
 * @version 1.0，2016/05/11，weifj，创建项目服务实现
 *
 */
@Service("ProjectService")
@Transactional(propagation = Propagation.REQUIRED) 
public class ProjectServiceImpl extends CommonServiceImpl implements ProjectService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	
	@Autowired
	private CaseMgmtDmn caseMgmtDmn;
	@Autowired
	private ConnectionService connService;
	@Autowired
	private DcmUserdomainroleDmn udrDmn;
	@Autowired
	private SearchEngine searchEngine;
	@Autowired
	private ECMConf ecmConf;
	@Autowired
	private DcmSocialResourceDmn srDmn;
/*	@Autowired
	private DcmGroupDmn groupDmn;*/
	@Autowired
	private DcmShareDmn shareDmn;
	@Autowired
	private DcmDownloadDmn downloadDmn;
	@Autowired
	private DcmTaskDmn taskDmn;
	@Autowired
	private DcmTaskMaterialDmn taskMaterialDmn;
	@Autowired
	private GroupService groupService;
	@Autowired
	private ExtPropertyWZConf extPropWZConf;
	@Autowired
	private ExtPropertyCaseConf extPropCaseConf;
	@Autowired
	private ExtPropertyMaterialConf extPropMaterialConf;
	@Autowired
	private FolderUtil folderUtil;
	@Autowired
	private DcmTeamDmn dcmTeamDmn;
	@Autowired
	private CaseUtil caseUtil;
	@Autowired
	private MailConf mailConf;
	@Autowired
	private DcmGroupDmn groupDmn;
	@Autowired
	private SgaProjectService sgaProjectService;
	@Autowired
	private Mapper dozerMapper;
	@Autowired
	private SgaUserService sgaUserService;
	@Autowired
	private WechatConf wechatConf;
	@Autowired
	private ImgServerConf imgSerConf;
	@Autowired
	private CacheStrategy redisCache;
	@Autowired
	private MongoFileStorageDmn mongoFileStorageDao;
	@Autowired
	private DcmWorkgroupDmn dcmWorkgroupDmn;
	@Autowired
	private DcmNaotuTeamDmn dcmNaotuTeamDmn;

	@Override
	public List<CaseTypeDTO> getCaseTypes() {
		this.connService.initialize();
		List<CaseTypeDTO> list = this.caseMgmtDmn.getCaseTypes(this.connService.getCurrSolution());
		this.connService.release();
		return list;
	}

	@Override
	public List<CaseDTO> getCasesByType(String typePrefix) {
		this.connService.initialize();
		List<CaseDTO> list = this.caseMgmtDmn.getCasesByType(this.connService.getDefaultOS(), typePrefix);
		this.connService.release();
		return list;
	}
	@Override
	public Object getAllCases() {
		
		List<CaseTypeDTO> caseTypes = this.getCaseTypes();
		if(null == caseTypes || 0 == caseTypes.size()){
			logger.info("没有获取到任何案例类型数据。");
			return null;
		}
		List<CaseTypeAndDataDTO> data = new ArrayList<CaseTypeAndDataDTO>();
		for(CaseTypeDTO type : caseTypes){
			CaseTypeAndDataDTO typeAndData = new CaseTypeAndDataDTO();
			typeAndData.setDisplayName(type.getDisplayName());
			typeAndData.setName(type.getName());
			typeAndData.setCases(this.getCasesByType(type.getName()));
			
			data.add(typeAndData);
		}
		return data;
	}
	@Deprecated
	@Override
	public CaseDTO getCaseByIdentifier(String caseIdentifier) {
		this.connService.initialize();
		CaseDTO dto = (CaseDTO) this.caseMgmtDmn.getCaseByIdentifier(this.connService.getDefaultOS(), caseIdentifier);
		this.connService.release();
		return dto;
	}
	@Override
	public CaseDTO getCaseByIdentifier(String realm, String userId, String pwd, String caseIdentifier) {
		
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm), userId, pwd);
		CaseDTO dto = (CaseDTO) this.caseMgmtDmn.getCaseByIdentifier(p8conn.getObjectStore(), caseIdentifier);
		
		return dto;
	}
	@Override
	public CaseDTO getCaseById(String realm, String userId, String pwd, String caseId) {
		
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm), userId, pwd);
		CaseDTO dto = (CaseDTO) this.caseMgmtDmn.getCaseById(p8conn.getObjectStore(), caseId);
		
		return dto;
	}
	@Override
	public CaseDTO getCaseById(String realm, String caseId) {
		
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		CaseDTO dto = (CaseDTO) this.caseMgmtDmn.getCaseById(p8conn.getObjectStore(), caseId);
		
		return dto;
	}
	@Deprecated
	@Override
	public Object getPackages(String caseIdentifier) {
		
	    this.connService.initialize();
		Object result = this.caseMgmtDmn.getCasePackages(this.connService.getDefaultOS(), caseIdentifier);
		this.connService.release();
		return result;
	}

	@Override
	public List<DocumentDTO> getPackages(String realm, String caseIdentifier) {
		
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		List<DocumentDTO> result = this.caseMgmtDmn.getCasePackages(p8conn.getObjectStore(), caseIdentifier);
		
		return result;
		
	}
	@Override
	public List<DocumentDTO> getPackages(String realm, String userId, String pwd, String caseIdentifier) {
		
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm), userId, pwd);
		List<DocumentDTO> result = this.caseMgmtDmn.getCasePackages(p8conn.getObjectStore(), caseIdentifier);
		
		return result;
	}
	@Override
	public Object getStatInfo(String caseIdentifier) {
		
		this.connService.initialize();

		Map<Long, ProjectStatDTO> userIdStatMap = new HashMap<Long, ProjectStatDTO>();
		// 获取项目的人
		List<DcmUserdomainrole> udrs = udrDmn.getByDomainCode(caseIdentifier);
		if(udrs != null && udrs.size()>0){
			ProjectStatDTO dto = null;
			
			BaseUserDTO userDTO = null;
			int countOfWz = 0;
			int countOfLike = 0;
			int countOfComment = 0;
			int countOfDoc = 0;
			for(DcmUserdomainrole udr : udrs) {
				
				if(!userIdStatMap.containsKey(udr.getUserId())) {
					dto = new ProjectStatDTO();
					DcmUser user = this.userOrgService.getUserEntityById(udr.getUserId());
					userDTO = new BaseUserDTO(user.getId(), user.getUserName(), user.getLoginName());
					dto.setUser(userDTO);
					userIdStatMap.put(udr.getUserId(), dto);
				}else{
					dto = userIdStatMap.get(udr.getUserId());
					userDTO = dto.getUser();
				}
				//user = this.userorgService.getUserById(udr.getUserId());
				// 微作数
				countOfWz = this.searchEngine.getTotalCount(this.connService.getDefaultOS(), this.ecmConf.getWzDefaultClassId(), new String[]{this.extPropWZConf.getAssociateProject(),"Creator"}, new String[]{caseIdentifier, userDTO.getLoginName()});
				// 微作点赞数
				List<DcmSocialResource> sresources = this.srDmn.getWzLikedByCaseIdentifierAndCreator(caseIdentifier, userDTO.getLoginName());
				countOfLike = sresources.size();
				// 微作评论数（统计很繁琐，暂缓）
				// 文档贡献数
				countOfDoc = this.searchEngine.getTotalCount(this.connService.getDefaultOS(), this.ecmConf.getDefaultDocumentClass(), new String[]{this.extPropWZConf.getAssociateProject(),"Creator"}, new String[]{caseIdentifier, userDTO.getLoginName()}, true);
				dto.setIndexInfo(countOfWz, countOfLike, countOfComment, countOfDoc);
				
				countOfWz = 0;
				countOfLike = 0;
				countOfComment = 0;
				countOfDoc = 0;
			}
		}
		
		return userIdStatMap.values();
	}
	
	@Override
	public Object getStatInfo(String realm, String caseId) {
		
		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		
		Map<Long, ProjectStatDTO> userIdStatMap = new HashMap<Long, ProjectStatDTO>();
		// 获取项目的人
		List<DcmUserdomainrole> udrs = udrDmn.getByDomainCode(caseId);
		if(udrs != null && udrs.size()>0){
			ProjectStatDTO dto = null;
			
			BaseUserDTO userDTO = null;
			int countOfWz = 0;
			int countOfLike = 0;
			int countOfComment = 0;
			int countOfDoc = 0;
			for(DcmUserdomainrole udr : udrs) {
				
				if(!userIdStatMap.containsKey(udr.getUserId())) {
					dto = new ProjectStatDTO();
					DcmUser user = null;
					 // 只统计内部人员信息
					if(1 == udr.getUserType()){
						continue;
					}
					user = this.userOrgService.getUserEntityByCode(udr.getUserCode());
					if(null == user) continue;
					
					userDTO = new BaseUserDTO(user.getId(), user.getUserName(), user.getLoginName());
					dto.setUser(userDTO);
					userIdStatMap.put(udr.getUserId(), dto);
				}else {
					dto = userIdStatMap.get(udr.getUserId());
					userDTO = dto.getUser();
				}
				//user = this.userorgService.getUserById(udr.getUserId());
				// 微作数
				countOfWz = this.searchEngine.getTotalCount(p8conn.getObjectStore(), this.ecmConf.getWzDefaultClassId(), new String[]{this.extPropWZConf.getAssociateProject(),"Creator"}, new String[]{caseId, userDTO.getLoginName()});
				// 微作点赞数
				List<DcmSocialResource> sresources = this.srDmn.getWzLikedByCaseIdentifierAndCreator(caseId, userDTO.getLoginName());
				countOfLike = sresources.size();
				// 微作评论数（统计很繁琐，暂缓）
				// 文档贡献数
				countOfDoc = this.searchEngine.getTotalCount(p8conn.getObjectStore(), this.ecmConf.getDefaultDocumentClass(), new String[]{this.extPropWZConf.getAssociateProject(),"Creator"}, new String[]{caseId, userDTO.getLoginName()}, true);
				dto.setIndexInfo(countOfWz, countOfLike, countOfComment, countOfDoc);
				
				countOfWz = 0;
				countOfLike = 0;
				countOfComment = 0;
				countOfDoc = 0;
			}
		}
		
		return userIdStatMap.values();
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProjectStatDTO> getStatInfoEx(String realm, String caseId, int size) {
		
		List<ProjectStatDTO> stats = null;
		// TODO 临时
		Date now = new Date();
		// 三分钟超时
		Long expireTime = 3 * 60 * 1000L;
		final String cacheKey = realm + "_"  + caseId + "_" + size;
	
		ProjectStatCacheDTO cacheDTO = (ProjectStatCacheDTO) this.redisCache.get(cacheKey);
		if(cacheDTO != null) {
			if((now.getTime() - cacheDTO.getStartTime()) < expireTime) {
				logger.info(">>>已获取到项目[{}]的统计缓存数据，并未过期，直接返回.......", caseId);
				stats = cacheDTO.getData();
				if(stats != null && !stats.isEmpty()) { 
					return stats;
				}
			} else {
				logger.info(">>>已获取到项目[{}]的统计缓存数据，但已过期，清理缓存.......", caseId);
				DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						redisCache.remove(cacheKey);
					}
				});
			}
		}
		
		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		// 获取项目下的微作信息
		Pagination allWzInfos = this.caseUtil.getWZsOfCase(p8conn.getObjectStore(), caseId, 1, this.ecmConf.getSearchMaxRecords());
		if(null == allWzInfos || null == allWzInfos.getData() || allWzInfos.getData().isEmpty()) {
			return null;
		}
		DcmUser user = null;
		Map<String, IndexInfo> publisherCode2indexInfo = new HashMap<String, IndexInfo>();
		List<WzInfoDTO> wzDTOs = (List<WzInfoDTO>) allWzInfos.getData();
		for(WzInfoDTO wz : wzDTOs) {

			String publisher = wz.getPublisher();
			
			if(StringUtils.isEmpty(publisher)) {
				user = this.userOrgService.getUserEntityByLoginName(wz.getCreator());
				if(null == user) continue;
				
				publisher = user.getUserCode();
			}
			IndexInfo index = publisherCode2indexInfo.get(publisher);
			if(null == index) {
				index = new IndexInfo(0,0,0,0);
				publisherCode2indexInfo.put(publisher, index);
			}
			index.setCountOfWz(index.getCountOfWz()+1);
			index.setCountOfComment(index.getCountOfComment() + wz.getCommentCount());
			index.setCountOfLike(index.getCountOfLike() + wz.getUpvoteCount());
		}

		// 获取项目的人
		List<DcmUserdomainrole> udrs = udrDmn.getByDomainCode(caseId);
	
		if(udrs != null && udrs.size()>0) {
			ProjectStatDTO statDTO = null;
			// 主要是为了去重
			List<String> duplicateUserCodesTemp = new ArrayList<String>();
			stats = new ArrayList<ProjectStatDTO>();
			for(DcmUserdomainrole udr : udrs) {
				if(!publisherCode2indexInfo.containsKey(udr.getUserCode())) {
					if(!duplicateUserCodesTemp.contains(udr.getUserCode())) {
						statDTO = new ProjectStatDTO();
						statDTO.setUserCode(udr.getUserCode());
						statDTO.setPoint(0.0);
						stats.add(statDTO);
						duplicateUserCodesTemp.add(udr.getUserCode());
					}
					continue;
				}
				if(	duplicateUserCodesTemp.contains(udr.getUserCode())) {
					continue;
				}
				statDTO = new ProjectStatDTO();
				statDTO.setUserCode(udr.getUserCode());
				IndexInfo tempIndex = publisherCode2indexInfo.get(udr.getUserCode());
				statDTO.setIndex(tempIndex);
				statDTO.setPoint(tempIndex.getCountOfWz() * 0.7 + tempIndex.getCountOfLike() * 0.2 + tempIndex.getCountOfComment() * 0.1);
				// 保留2位小数点
				DecimalFormat df = new DecimalFormat("######0.00");   
				statDTO.setPoint(Double.valueOf(df.format(statDTO.getPoint())));
				stats.add(statDTO);
				duplicateUserCodesTemp.add(udr.getUserCode());
			}

			Collections.sort(stats, new Comparator<ProjectStatDTO>() {
				@Override
				public int compare(ProjectStatDTO first, ProjectStatDTO second) {
					return second.getPoint().compareTo(first.getPoint());
				}
			});
			duplicateUserCodesTemp.clear();
			if(stats.size() > size) {
				List<ProjectStatDTO> tempStats = new ArrayList<>();
				for(int i = 0; i<size;i++) {
					tempStats.add(stats.get(i));
				}
				stats = tempStats;//stats.subList(0, size);
			}
			if(stats != null && !stats.isEmpty()) {
				logger.info(">>>缓存项目统计数据......");
				final List<ProjectStatDTO> cacheStats = stats;
				DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						/*String dataKey = UUID.randomUUID().toString();
						CacheKeyDTO cacheDTO = new CacheKeyDTO();
						cacheDTO.setStartTime(new Date().getTime());
						cacheDTO.setDataKey(dataKey);
						redisCache.set(cacheKey, cacheDTO);
						redisCache.setList(dataKey, cacheStats);*/
						ProjectStatCacheDTO newCacheDTO = new ProjectStatCacheDTO();
						newCacheDTO.setStartTime(new Date().getTime());
						newCacheDTO.setData(cacheStats);
						redisCache.set(cacheKey, newCacheDTO);
					}
				});
			}
			return stats;
		}
		return null;
	}

	@Override
	public Object deleteProject(String caseIdentifier) {
		
		logger.info(">>>正在删除项目[{}]......", caseIdentifier);
		logger.info(">>>正在删除项目组......");
		this.groupService.deleteGroupByCode(caseIdentifier);
		logger.info(">>>正在删除跟项目相关的社交化数据......");
		this.srDmn.deleteByParentResId(caseIdentifier);
		logger.info(">>>正在删除项目组成员......");
		this.udrDmn.deleteByDomainCode(caseIdentifier);
		logger.info(">>>正在删除与项目关联的分享信息......");
		this.shareDmn.delShareBySourceDomainCode(caseIdentifier);
		this.shareDmn.delShareByTargetDomainCode(caseIdentifier);
		logger.info(">>>正在删除与项目关联的下载信息......");
		this.downloadDmn.delDownloadLogByDomainCode(caseIdentifier);
		logger.info(">>>正在删除与项目关联的任务......");
		this.deleteTaskByCaseIdentifier(caseIdentifier);
		logger.info(">>>正在删除公开项目信息......");
		this.sgaProjectService.deleteProjectByCode(caseIdentifier);
		logger.info(">>>完成删除项目所有信息。");
		return true;
	}
	@Override
	public Object deleteProjectByGuid(String guid) {
		
		logger.info(">>>正在删除项目[{}]......", guid);
		logger.info(">>>正在删除项目组......");
		this.groupService.deleteGroupByGuid(guid);
		logger.info(">>>正在删除跟项目相关的社交化数据......");
		this.srDmn.deleteByParentResId(guid);
		logger.info(">>>正在删除项目组成员......");
		this.udrDmn.deleteByDomainCode(guid);
		logger.info(">>>正在删除与项目关联的分享信息......");
		this.shareDmn.delShareBySourceDomainCode(guid);
		this.shareDmn.delShareByTargetDomainCode(guid);
		logger.info(">>>正在删除与项目关联的下载信息......");
		this.downloadDmn.delDownloadLogByDomainCode(guid);
		logger.info(">>>正在删除与项目关联的任务......");
		this.deleteTaskByCaseIdentifier(guid);
		logger.info(">>>正在删除与项目关联的频道......");
		this.deleteProjectChannel(guid);
		logger.info(">>>正在删除与项目关联的团队信息......");
		this.deleteTeamsByCaseId(guid);
		logger.info(">>>正在删除与脑图关联的case记录......");
		this.dcmNaotuTeamDmn.removeByProperty("caseId", guid);
		logger.info(">>>正在删除与脑图关联的团队记录......");
		this.dcmNaotuTeamDmn.removeByProperty("teamId", guid);
		logger.info(">>>正在删除公开项目信息......");
		try{
		this.sgaProjectService.deleteProjectByCode(guid);
		}catch(Exception ex) {
			
		}
		logger.info(">>>完成删除项目所有信息。");
		return true;
	}
	

	@Override
	public List<CaseDTO> getMyProjects(String userId) {
		
		List<CaseDTO> list = new ArrayList<CaseDTO>();
		
		DcmUser findUser = this.userOrgService.getUserByLoginNameInCache(userId);//.findByLoginName(userId);
		List<DcmUserdomainrole> udrs = this.udrDmn.getProjectGroupsByUserId(findUser.getId());
		if(udrs != null && udrs.size()>0){
			for(DcmUserdomainrole udr : udrs){
				try{
				CaseDTO caseDTO = this.getCaseByIdentifier(udr.getDomainCode());
				
				list.add(caseDTO);
				}catch(Exception ex){	
					ex.printStackTrace();
					logger.error("没找到对应case，案例标识：[{}]，详情：[{}]", udr.getDomainCode(), ex.getMessage());
				}
				
			}
		}
		return list;
	}
	
	@Override
	public List<CaseDTO> getMyProjects(String realm, String userId, String pwd) {
		
		List<CaseDTO> list = new ArrayList<CaseDTO>();
		
		DcmUser findUser = this.userOrgService.getUserEntityByDN(userId);//.findByLoginName(userId);
		List<DcmUserdomainrole> udrs = this.udrDmn.getProjectGroupsByUserId(findUser.getId());
		if(udrs != null && udrs.size()>0){
			for(DcmUserdomainrole udr : udrs){
				try{
				CaseDTO caseDTO = this.getCaseById(realm, userId, pwd, udr.getDomainCode());
				
				list.add(caseDTO);
				}catch(Exception ex){	
					ex.printStackTrace();
					logger.error("没找到对应case，案例id：[{}]，详情：[{}]", udr.getDomainCode(), ex.getMessage());
				}
				
			}
		}
		return list;
	}
	@Override
	public Object getMyProjects(String realm, String userId) {

		List<CaseDTO> list = new ArrayList<CaseDTO>();
		List<String> domainCodes = new ArrayList<String>();
		DcmUser findUser = this.userOrgService.getUserEntityByDN(userId);// .findByLoginName(userId);
		List<DcmUserdomainrole> udrs = this.udrDmn.getProjectGroupsByUserId(findUser.getId());
		if (udrs != null && udrs.size() > 0) {
			for (DcmUserdomainrole udr : udrs) {
				if (domainCodes.contains(udr.getDomainCode())) {
					continue;
				}
				try {
					domainCodes.add(udr.getDomainCode());
					CaseDTO caseDTO = this.getCaseById(realm, udr.getDomainCode());
					list.add(caseDTO);
				} catch (Exception ex) {
					ex.printStackTrace();
					logger.error(">>>没找到对应case，案例id：[{}]，详情：[{}]", udr.getDomainCode(), ex.getMessage());
				}
			}
		}
		return list;
	}
	
	@Override
	public Object getMyProjectsGZ(String realm, String userId) {

		List<CaseDTO> list = new ArrayList<CaseDTO>();
		List<String> domainCodes = new ArrayList<String>();
		DcmUser findUser = this.userOrgService.getUserEntityByDN(userId);// .findByLoginName(userId);
		List<DcmUserdomainrole> udrs = this.udrDmn.getProjectGroupsByUserId(findUser.getId());
		if (udrs != null && udrs.size() > 0) {
			for (DcmUserdomainrole udr : udrs) {
				if (domainCodes.contains(udr.getDomainCode())) {
					continue;
				}
				try {
					domainCodes.add(udr.getDomainCode());
					CaseDTO caseDTO = this.getCaseByIdentifier(realm, udr.getDomainCode());

					list.add(caseDTO);
				} catch (Exception ex) {
					ex.printStackTrace();
					logger.error("没找到对应case，案例id：[{}]，详情：[{}]", udr.getDomainCode(), ex.getMessage());
				}
			}
		}
		return list;
	}
	
	@Override
	public CaseDTO getCaseByIdentifier(String realm, String caseIdentifier) {
		
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		CaseDTO dto = (CaseDTO) this.caseMgmtDmn.getCaseByIdentifier(p8conn.getObjectStore(), caseIdentifier);
		
		return dto;
	}

	/*@Override
	public synchronized int updateWzCommentsCount(String userId, String password, String wzId, int op) {
		int commentsCount = 0;
		try {
			P8Connection p8conn = this.connectionService.getP8Connection(userId, password);
			Document doc = (Document) this.caseMgmtDmn.getWz(p8conn.getObjectStore(), wzId);
			commentsCount = doc.getProperties().getInteger32Value(this.extPropWZConf.getCommentCount());
			if (1 == op) {
				// 添加评论
				commentsCount++;

			} else if (0 == op) {
				// 删除评论
				commentsCount--;
			}
			doc.getProperties().putObjectValue(this.extPropWZConf.getCommentCount(), commentsCount);
			doc.save(RefreshMode.REFRESH);
			return commentsCount;

		} catch (Exception ex) {
			logger.error("修改评论数失败，详情：[{}]", ex.getMessage());
			return commentsCount;
		}
	}*/
	@Override
	public int updateWzCommentsCount(String realm, String wzId, int op) {
		
		return updateWzSocialCounter(realm, wzId, op, this.extPropWZConf.getCommentCount());
	}
	@Override
	public Boolean updateHotProjectStatus(String realm, String caseId, int status) {
		
		Lock writeLock = lock.writeLock();
		writeLock.lock();
		try {
			P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
			Folder caseFolder = this.folderUtil.loadById(p8conn.getObjectStore(), caseId);
			Properties props = caseFolder.getProperties();
			props.putValue(this.extPropCaseConf.getSfrm(), status);
			caseFolder.save(RefreshMode.REFRESH);
			
			return true;

		} catch (Exception ex) {
			logger.error(">>>修改计数器失败，详情：[{}]", ex.getMessage());
			return false;
		} finally {
			writeLock.unlock();
		}
	}
	@Override
	public int updateWzUpvoteCount(String realm, String wzId, int op) {
		
		return updateWzSocialCounter(realm, wzId, op, this.extPropWZConf.getUpvoteCount());
	}
	@Override
	public int updateWzFavoriteCount(String realm, String wzId, int op) {
		
		return updateWzSocialCounter(realm, wzId, op, this.extPropWZConf.getFavorites());
	}
	/**
	 * 更新微作社会属性计数器，例如：点赞数、收藏数和评论数
	 * @param realm
	 * @param wzId
	 * @param op
	 * @return
	 */
	private int updateWzSocialCounter(String realm, String wzId, int op, String updateProperty) {
		Lock writeLock = lock.writeLock();
		writeLock.lock();
		int count = 0;
		
		try {
			P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
			Document doc = this.caseUtil.getWz(p8conn.getObjectStore(), wzId);
			count = doc.getProperties().getInteger32Value(updateProperty);
			if (1 == op) {
				// 添加
				count = count + 1;

			} else if (0 == op) {
				// 删除
				if(count > 0){
					count = count - 1;
				}
			}
			doc.getProperties().putObjectValue(updateProperty, count);
			doc.save(RefreshMode.REFRESH);
			return count;

		} catch (Exception ex) {
			logger.error(">>>修改计数器失败，详情：[{}]", ex.getMessage());
			return count;
		} finally {
			writeLock.unlock();
		}
	}
	@Override
	public TaskResponseDTO getTaskById(String taskId) {
		
		DcmTask task =  this.taskDmn.findUniqueByProperty("taskId", taskId);
		if(task != null) {
			return this.dozerMapper.map(task, TaskResponseDTO.class);
		}
		return null;
	}
	
	@Override
	public void addTask(String caseIdentifier, String taskId, String parentTaskId, String taskName) {

		DcmTask task = new DcmTask();
		task.setTaskId(taskId);
		task.setParentTaskId(parentTaskId);
		task.setDateCreated(new Date());
		task.setCaseIdentifier(caseIdentifier);
		task.setTaskName(taskName);
		
		this.taskDmn.add(task);
	}
	
	@Override
	public void deleteTaskById(String taskId) {
		
		// 删除关联的材料
		this.taskMaterialDmn.deleteMaterialByTaskId(taskId);
		// 删除任务本身
		this.taskDmn.deleteTaskById(taskId);
		
	}
	
	@Override
	public void deleteTaskByCaseIdentifier(String caseIdentifier) {
		
		this.taskDmn.deleteTaskByCaseIdentifier(caseIdentifier);
	}
	
	@Override
	public void deleteTasksByParentId(String parentTaskId) {
		
		// 删除关联的材料
		List<DcmTask> tasks = this.taskDmn.getSubTasks(parentTaskId);
		if(tasks != null && !tasks.isEmpty()){
			for(DcmTask task : tasks){
				this.taskMaterialDmn.deleteMaterialByTaskId(task.getTaskId());
				this.taskDmn.remove(task);
			}
		}
	}

	@Override
	public Object getSubTasks(String caseIdentifier) {
		
		return  this.taskDmn.getSubTasks(caseIdentifier);
	}
	
	@Override
	public Object listChannels() {
		
		return this.dcmDicChannelDmn.find();
	}

	@Override
	public Object listChannels(String caseId) {
		
		Sort isBuildInSort = new Sort("isBuildIn", Sort.DESC);
		Sort createTimeSort = new Sort("createTime", Sort.ASC);
		List<Sort> sorts = new ArrayList<Sort>();
		sorts.add(isBuildInSort);
		sorts.add(createTimeSort);
		
		Map<String, Object> equalProperties = new HashMap<String, Object>();
		equalProperties.put("caseId", caseId);
		
		Map<String, Object> orProperties = new HashMap<String, Object>(); 
		orProperties.put("isBuildIn", 1L);
		//TODO 优化查询
		return this.dcmDicChannelDmn.find("From " +DcmDicChannel.class.getSimpleName() +" WHERE caseId='"+caseId+"' or isBuildIn=1"+ " order by isBuildIn desc,createTime asc ", null);
		// 頻道限制为1000
		//return this.dcmDicChannelDmn.findByProperties(1, 1000, equalProperties, orProperties, sorts).getData();
	}
	@Override
	public Object addChannel(String code, String name, Long isBuildIn)  throws BusinessException{
		
		DcmDicChannel dc = this.dcmDicChannelDmn.findUniqueByProperty("name", name);
		if(dc != null) throw new BusinessException("频道名称 [{0}] 已存在", name);
		
		dc = this.dcmDicChannelDmn.findUniqueByProperty("code", code);
		if(dc != null) throw new BusinessException("频道编码 [{0}] 已存在", code);
		
		dc = new DcmDicChannel();
		dc.setCode(StringUtil.isNullOrEmpty(code)? UUID.randomUUID().toString().toUpperCase() : code);// 
		dc.setName(name);
		dc.setIsBuildIn(isBuildIn);
		
		return this.dcmDicChannelDmn.add(dc);
	}
	
	@Override
	public DcmDicChannel addChannel(ChannelDTO dto, Long isBuildIn) throws BusinessException {
		
		dto.setCode(dto.getCode().replace("-1", ""));
		
		DcmDicChannel dc = this.dcmDicChannelDmn.findUniqueByProperties(new String[]{"caseId", "name"},new Object[]{ dto.getCaseId(), dto.getName()});
		if(dc != null) throw new BusinessException("频道名称 [{0}] 已存在", dto.getName());
		
		if(!StringUtil.isNullOrEmpty(dto.getCode())){
			
			dc = this.dcmDicChannelDmn.findUniqueByProperties(new String[]{"caseId", "code"}, new Object[]{ dto.getCaseId(), dto.getCode()});
			if(dc != null) throw new BusinessException("频道编码 [{0}] 已存在", dto.getCode());
			
		}
		
		dc = new DcmDicChannel();
		dc.setCode(StringUtil.isNullOrEmpty(dto.getCode())? UUID.randomUUID().toString().toUpperCase() : dto.getCode());// 
		dc.setName(dto.getName());
		dc.setCaseId(dto.getCaseId());
		dc.setCreateTime(new Date());
		dc.setIsBuildIn(isBuildIn);
		
		return this.dcmDicChannelDmn.add(dc);
		
	}
	
	@Override
	public Object updateChannel(Long id, String newName) throws BusinessException {
		
		Assert.notNull(id);
		Assert.notNull(newName);
		
		DcmDicChannel dc = this.dcmDicChannelDmn.findById(id);
		
		if(null == dc) throw new BusinessException("频道id [{0}] 不存在", id);
		
		DcmDicChannel finddc = this.dcmDicChannelDmn.findUniqueByProperties(new String[]{"caseId", "name"},new Object[]{ dc.getCaseId(), newName});
		if(finddc != null) throw new BusinessException("频道名称 [{0}] 已存在", newName);
	
		dc.setName(newName);
		
		return this.dcmDicChannelDmn.saveOrUpdate(dc);
	}
	
	@Override
	public Object deleteChannel(Long id) {
		
		return this.dcmDicChannelDmn.removeById(id);
		
	}
	
	@Override
	public void deleteProjectChannel(String caseId) {
		
		this.dcmDicChannelDmn.removeByProperty("caseId", caseId);
		
	}

	@Override
	public Object getCooperationPackage(String realm, String caseId) {
	
		Folder folder = null;
		try {
			P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
			
			// 获取项目根文件夹
			Folder projectFolder = this.folderUtil.getById(p8conn.getObjectStore(), caseId);
		
			// 指定某个父文件夹
			folder = this.folderUtil.loadAndCreateCaseSubFolderByParentFolder(p8conn.getObjectStore(), projectFolder, "合作项目");
			
			return folderUtil.folder2dto(folder);

		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw new BusinessException("创建文档资料失败。详情：[{0}]", ex.getMessage());
		}
	}
	
	@Override
	public DcmTeam addTeam(String caseId, String name) throws BusinessException {
		
		DcmGroup group = this.groupService.getGroupByGuid(caseId);
		if(null == group)
			throw new BusinessException("项目信息不存在，case id [{0}]", caseId);
		
		DcmTeam team = new DcmTeam();
		team.setProjectId(group.getId());
		team.setName(name);
		team.setCreateTime(new Date());
		return this.dcmTeamDmn.add(team);
		
	}
	
	@Override
	public boolean modifyTeamName(Long teamId, String newName) {
		
		DcmTeam team = this.dcmTeamDmn.loadById(teamId);
		if(null == team)
			throw new BusinessException("团队信息不存在，team id [{0}]", teamId);
		
		team.setName(newName);
		team.setCreateTime(new Date());
		
		return true;
	}
	
	@Override
	public boolean addTeamUser(Long teamId, Long userId) {
		
		Lock locallock = lock.writeLock();
		try {
			locallock.lock();
			DcmTeamUser teamUser = this.dcmTeamDmn.getTeamUserRef(teamId, userId);
			if(teamUser != null)
				return true;
			
			teamUser = new DcmTeamUser();
			teamUser.setCreateTime(new Date());
			teamUser.setTeamId(teamId);
			teamUser.setUserId(userId);
			this.dcmTeamDmn.saveOrUpdateTeamUserRef(teamUser);
			
			return true;
			
		}catch(Exception ex){ 
			logger.error(ex.getMessage());
		}
		finally {
			locallock.unlock();
		}
		return false;
	}
	@Override
	public boolean addTeamUser(String caseId, Long teamId, Long userId) {
		
		DcmGroup group = this.groupService.getGroupByGuid(caseId);
		if(null == group)
			throw new BusinessException("项目信息不存在，case id [{0}]", caseId);
		
		Lock locallock = lock.writeLock();
		try {
			locallock.lock();
			DcmTeamUser teamUser = this.dcmTeamDmn.getTeamUserRef(teamId, userId);
			if(teamUser != null)
				return true;
			
			teamUser = new DcmTeamUser();
			teamUser.setCreateTime(new Date());
			teamUser.setTeamId(teamId);
			teamUser.setUserId(userId);
			teamUser.setProjectId(group.getId());
			this.dcmTeamDmn.saveOrUpdateTeamUserRef(teamUser);
			
			return true;
			
		}catch(Exception ex){ 
			logger.error(ex.getMessage());
		}
		finally {
			locallock.unlock();
		}
		return false;
	}
	
	@Override
	public List<ProjectTeamDTO> getTeams(String caseId) {

		DcmGroup group = this.groupService.getGroupByGuid(caseId);
		if (null == group)
			throw new BusinessException("项目信息不存在，case id [{0}]", caseId);

		List<DcmTeam> teams = this.dcmTeamDmn.findByProperty("projectId", group.getId());
		if (null == teams || 0 == teams.size())
			return null;

		List<ProjectTeamDTO> teamDTOs = new ArrayList<ProjectTeamDTO>(teams.size());
		for (DcmTeam t : teams) {
			ProjectTeamDTO dto = new ProjectTeamDTO();
			dto.setId(t.getId());
			dto.setTeamName(t.getName());
			List<DcmTeamUser> teamuserRef = this.dcmTeamDmn.getUserRef(t.getId());
			if (teamuserRef != null && teamuserRef.size() > 0) {
				for (DcmTeamUser tu : teamuserRef) {
					DcmUser findUser = this.userOrgService.getUserEntityById(tu.getUserId());
					if(null == findUser) continue;
					
					TeamUserDTO baseUser = new TeamUserDTO();
					baseUser.setId(findUser.getId());
					baseUser.setLoginName(findUser.getLoginName());
					baseUser.setUserName(findUser.getUserName());
					baseUser.setUserCode(findUser.getUserCode());
					baseUser.setIsLeader(tu.getIsLeader());
					dto.getUsers().add(baseUser);
				}
			}

			teamDTOs.add(dto);
		}

		return teamDTOs;
	}
	
	@Override
	public void deleteTeamUsers(Long teamId, Long[] userIds) {
		
		this.dcmTeamDmn.deleteUsers(teamId, userIds);
		
	}
	@Override
	public void deleteTeam(Long teamId) {
		
		this.dcmTeamDmn.removeById(teamId);
	}
	/**
	 * 根据项目id删除其下的所有团队信息
	 * @param guid
	 */
	private void deleteTeamsByCaseId(String guid) {
		
		DcmGroup group = this.groupService.getGroupByGuid(guid);
		if(null == group)
			return;
		
		List<DcmTeam> teams = this.dcmTeamDmn.findByProperty("projectId", group.getId());
		if(null == teams || teams.isEmpty())
			return;
		
		for(DcmTeam team : teams){
			this.deleteTeam(team.getId());
		}
	}
	@Deprecated
	@Override
	public void removeUsersFromTeam(Long[] userIds) {
		
		this.dcmTeamDmn.removeUsers(userIds);
	}
	@Override
	public void removeUsersFromTeam(Map<String, Integer> users) {
		
		for(Entry<String, Integer> entry : users.entrySet()){
			if(0 == entry.getValue()){
				logger.info(">>>内部用户[{}]", entry.getKey());
				DcmUser user = this.userOrgService.getUserEntityByCode(entry.getKey());
				if(null == user) continue;
				
				this.dcmTeamDmn.removeUsers(new Long[]{user.getId()});
			}
		}
	}
	@Override
	public Object setTeamLeader(TeamLeaderRequestDTO dto) {
		
		if(dto.getPreLeader().equals(dto.getNewLeader())){
			
			logger.debug(">>>设置队长，针对同一个人");
			DcmTeamUser tu =  this.dcmTeamDmn.getTeamUserRef(dto.getTeamId(), dto.getNewLeader());
			if(null == tu)
				throw new BusinessException("关联记录无法找到，teamId[{0}]，userId[{1}]", dto.getTeamId(), dto.getNewLeader());
			
			tu.setIsLeader(dto.getOp());
			this.dcmTeamDmn.saveOrUpdateTeamUserRef(tu);
			return true;
			
		}else{
			
			logger.debug(">>>设置队长，针对不同的人");
			DcmTeamUser tu =  this.dcmTeamDmn.getTeamUserRef(dto.getTeamId(), dto.getPreLeader());
			if(tu != null){
				tu.setIsLeader(0);
				this.dcmTeamDmn.saveOrUpdateTeamUserRef(tu);
			}
			
			tu =  this.dcmTeamDmn.getTeamUserRef(dto.getTeamId(), dto.getNewLeader());
			if(tu != null){
				tu.setIsLeader(dto.getOp());
				this.dcmTeamDmn.saveOrUpdateTeamUserRef(tu);
			}
			return true;
		}
	}
	@Override
	public void saveOrUpdateTask(TaskAddDTO dto) {
		
		DcmTask task = this.taskDmn.findUniqueByProperty("taskId", dto.getTaskId());
		if(null == task){
			task = new DcmTask();
			task.setDateCreated(new Date());
		}
		this.dozerMapper.map(dto, task);
		// task.setTaskId(dto.getTaskId());
		task.setCaseIdentifier(dto.getCaseId());
		//task.setBeginTime(null == dto.getBeginTime()? null:new Date(dto.getBeginTime()));
		//task.setEndTime(null == dto.getEndTime()? null:new Date(dto.getEndTime()));
		//task.setAudience(dto.getAudience());
		//task.setCustomerFeedback(dto.getCustomerFeedback());
		//task.setExpectPeople(dto.getExpectPeople());
		//task.setFocus(dto.getFocus());
		//task.setHardwareReq(dto.getHardwareReq());
		//task.setHeadFeedback(dto.getHeadFeedback());
		//task.setLocalSupport(dto.getLocalSupport());
		//task.setSolutionTemp(dto.getSolutionTemp());
		//task.setTarget(dto.getTarget());
		//task.setType(dto.getType());
		//task.setResponseTime(dto.getResponseTime());
		//task.setStatus(dto.getStatus());
		
		this.taskDmn.saveOrUpdate(task);
	}
	@Override
	public Object getTasksByCaseId(String caseId) {
		
		List<DcmTask> tasks = this.taskDmn.findByProperty("caseIdentifier", caseId);
		
		if(null == tasks || 0 == tasks.size())
			return null;
		
		Map<String, TaskResponseDTO> map = new HashMap<String,TaskResponseDTO>(tasks.size());
		for(DcmTask task : tasks){
			
/*			TaskResponseDTO dto = new TaskResponseDTO();
			dto.setTaskId(task.getTaskId());
			dto.setBeginTime(task.getBeginTime());
			dto.setEndTime(task.getEndTime());
			dto.setAudience(task.getAudience());
			dto.setCustomerFeedback(task.getCustomerFeedback());
			dto.setExpectPeople(task.getExpectPeople());
			dto.setFocus(task.getFocus());
			dto.setHardwareReq(task.getHardwareReq());
			dto.setHeadFeedback(task.getHeadFeedback());
			dto.setLocalSupport(task.getLocalSupport());
			dto.setSolutionTemp(task.getSolutionTemp());
			dto.setTarget(task.getTarget());
			dto.setType(task.getType());
			*/
			map.put( task.getTaskId(), this.dozerMapper.map(task, TaskResponseDTO.class));
		}
		return map;
	}
	@Override
	public Pagination getWZOfCase(String caseIdentifier, int pageNo, int pageSize) {
		this.connService.initialize();
		Pagination page = (Pagination) this.caseMgmtDmn.getWZOfCase(this.connService.getDefaultOS(), caseIdentifier, pageNo, pageSize);
		this.connService.release();
		return page;
	}
	@Override
	public Pagination getWZsOfCaseById(WzInfoPageDTO pageInfo) {
		
		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(pageInfo.getRealm()));
		Pagination page = this.caseUtil.getWZsOfCase(p8conn.getObjectStore(), pageInfo.getCaseId(), pageInfo.getPageNo(), pageInfo.getPageSize());	
		if(null == page) {
			return new Pagination(pageInfo.getPageNo(), pageInfo.getPageSize(), 0, null);
		}
		if(null == page.getData()) {
			return page;
		}
		@SuppressWarnings("unchecked")
		List<WzInfoDTO> wzInfoDTOs = (List<WzInfoDTO>) page.getData();
		setWzPublisherAndTaskPropEx(wzInfoDTOs);
		return page;
	}
	/**
	 * 设置微作的发布者和任务的扩展属性
	 * @param wzInfoDTOs
	 */
	private void setWzPublisherAndTaskPropEx(List<WzInfoDTO> wzInfoDTOs) {
		if(wzInfoDTOs !=null && !wzInfoDTOs.isEmpty()) {
			for(WzInfoDTO wz : wzInfoDTOs) {
				if(StringUtils.isEmpty(wz.getPublisher())) {
					SgaPrjWz prjRefWz = this.sgaProjectService.getRefWzById(wz.getGuid());
					if(prjRefWz != null) {
						wz.setPublisher(prjRefWz.getCreator());
					} else {
						DcmUser findUser = this.userOrgService.getUserEntityByLoginName(wz.getCreator());
						if(findUser != null) {
							wz.setPublisher(findUser.getUserCode());
						}
					}
				}
				if(WZTypeConstants.TASK == wz.getType()) {
					// 任务类型，则返回相关属性
					TaskResponseDTO dto = null;
					if((dto = this.getTaskById(wz.getGuid())) != null) {
						wz.setTaskPropEx(dto);
					}
				}
			}
		}
	}
	private void setWzPublisherAndTaskPropEx(List<WzInfoDTO> wzInfoDTOs, String baseURL, String contextPath) {
		if(wzInfoDTOs !=null && !wzInfoDTOs.isEmpty()) {
			for(WzInfoDTO wz : wzInfoDTOs) {
				if(StringUtils.isEmpty(wz.getPublisher())) {
					SgaPrjWz prjRefWz = this.sgaProjectService.getRefWzById(wz.getGuid());
					if(prjRefWz != null) {
						wz.setPublisher(prjRefWz.getCreator());
					} else {
						DcmUser findUser = this.userOrgService.getUserEntityByLoginName(wz.getCreator());
						if(findUser != null) {
							wz.setPublisher(findUser.getUserCode());
						}
					}
				}
				if(WZTypeConstants.TASK == wz.getType()) {
					// 任务类型，则返回相关属性
					TaskResponseDTO dto = null;
					if((dto = this.getTaskById(wz.getGuid())) != null) {
						wz.setTaskPropEx(dto);
					}
				}
				List<ThumbnailDTO> thumbnails = wz.getThumbnails();
				if(thumbnails != null && !thumbnails.isEmpty()) {
					for(ThumbnailDTO thumb : thumbnails) {
						String tempFile = contextPath + "/" +thumb.getId()+".png";
						try {
							if(!new File(tempFile).exists()) {
								Base64Utils.decodeToFile(tempFile, thumb.getImg());
							}
							thumb.setImg(baseURL +  "/" +thumb.getId()+".png");
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
						}
					}
				}
			}
		}
		
	}
	private void setWzThumb(List<WzInfoDTO> wzInfoDTOs, String baseURL, String contextPath) {
		if(wzInfoDTOs !=null && !wzInfoDTOs.isEmpty()) {
			for(WzInfoDTO wz : wzInfoDTOs) {
				List<ThumbnailDTO> thumbnails = wz.getThumbnails();
				if(thumbnails != null && !thumbnails.isEmpty()) {
					for(ThumbnailDTO thumb : thumbnails) {
						String tempFile = contextPath + "/" +thumb.getId()+".png";
						try {
							if(!new File(tempFile).exists() && !StringUtils.isEmpty(thumb.getImg())) {
								Base64Utils.decodeToFile(tempFile, thumb.getImg());
							}
							thumb.setImg(baseURL +  "/" +thumb.getId()+".png");
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
						}
					}
				}
			}
		}
	}
	
	@Override
	public Pagination getWZsOfCaseById(int wzType, WzInfoPageDTO pageInfo) {
		
		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(pageInfo.getRealm()));
		Pagination page = this.caseUtil.getWZsOfCase(p8conn.getObjectStore(), new String[]{this.extPropWZConf.getWZType(), this.extPropWZConf.getAssociateProject()}, new Object[]{wzType, pageInfo.getCaseId()}, pageInfo.getPageNo(), pageInfo.getPageSize());
		if(WZTypeConstants.TASK == wzType && page != null) {
			// 任务类型的微作，继续附上任务的扩展属性
			@SuppressWarnings("unchecked")
			List<WzInfoDTO> wzInfoDTOs = (List<WzInfoDTO>) page.getData();
			setWzPublisherAndTaskPropEx(wzInfoDTOs);
		}
		return page;
	}
	
	@Override
	public Pagination getWZsOfCaseByIdThumbByte(int wzType, WzInfoPageDTO pageInfo) {
		
		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(pageInfo.getRealm()));
		Pagination page = this.caseUtil.getWZsOfCaseThumbnailByte(p8conn.getObjectStore(),
				new String[] { this.extPropWZConf.getWZType(), this.extPropWZConf.getAssociateProject() },
				new Object[] { wzType, pageInfo.getCaseId() }, pageInfo.getPageNo(), pageInfo.getPageSize());
		if (WZTypeConstants.TASK == wzType && page != null) {
			// 任务类型的微作，继续附上任务的扩展属性
			@SuppressWarnings("unchecked")
			List<WzInfoDTO> wzInfoDTOs = (List<WzInfoDTO>) page.getData();
			setWzPublisherAndTaskPropEx(wzInfoDTOs);
		}
		return page;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Pagination getWZsOfCaseByIdForMobile(int wzType, WzInfoPageDTO pageInfo, String baseURL, String contextPath) {
		
		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(pageInfo.getRealm()));
		Pagination page = this.caseUtil.getWZsOfCase(p8conn.getObjectStore(), new String[]{this.extPropWZConf.getWZType(), this.extPropWZConf.getAssociateProject()}, 
				new Object[]{wzType, pageInfo.getCaseId()}, pageInfo.getPageNo(), pageInfo.getPageSize(), false, true, true, false);
		if(WZTypeConstants.TASK == wzType && page != null) {
			// 任务类型的微作，继续附上任务的扩展属性
			List<WzInfoDTO> wzInfoDTOs = (List<WzInfoDTO>) page.getData();
			setWzPublisherAndTaskPropEx(wzInfoDTOs, baseURL, contextPath);
		} else {
			setWzThumb( (List<WzInfoDTO>) page.getData(), baseURL, contextPath);
		}
		
		return page;
	}
	
	@Override
	public Pagination getWZsOfCaseNoThumbById(int wzType, WzInfoPageDTO pageInfo) {
		// TODO 临时
		Date now = new Date();
		// 三分钟超时
		Long expireTime = 3 * 60 * 1000L;
		final String cacheKey = wzType + "_"  + pageInfo.getRealm() + "_" +pageInfo.getCaseId();
		WzNoThumbCacheDTO cacheDTO = (WzNoThumbCacheDTO) this.redisCache.get(cacheKey);
		if(cacheDTO != null) {
			if((now.getTime() - cacheDTO.getStartTime()) < expireTime) {
				logger.info(">>>已获取到微作的缓存数据，并未过期，直接返回.......");
				return cacheDTO.getPage();
			} else {
				logger.info(">>>已获取到微作的缓存数据，但已过期，清理缓存.......");
				DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						redisCache.remove(cacheKey);
					}
				});
			}
		}
		P8Connection p8conn = this.connService.getP8Connection(this.ecmConf.getTargetObjectStore(pageInfo.getRealm()));
		final Pagination page = this.caseUtil.getWZsOfCase(p8conn.getObjectStore(), new String[]{this.extPropWZConf.getWZType(), this.extPropWZConf.getAssociateProject()}, new Object[]{wzType, pageInfo.getCaseId()}, pageInfo.getPageNo(), pageInfo.getPageSize(), false);
		if(WZTypeConstants.TASK == wzType && page != null) {
			// 任务类型的微作，继续附上任务的扩展属性
			@SuppressWarnings("unchecked")
			List<WzInfoDTO> wzInfoDTOs = (List<WzInfoDTO>) page.getData();
			setWzPublisherAndTaskPropEx(wzInfoDTOs);
		}
		if(page != null && page.getData() != null && !page.getData().isEmpty()) {
			logger.info(">>>缓存微作数据......");
			DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					WzNoThumbCacheDTO newCacheDTO = new WzNoThumbCacheDTO();
					newCacheDTO.setStartTime(new Date().getTime());
					newCacheDTO.setPage(page);
					redisCache.set(cacheKey, newCacheDTO);
				}
			});
		}
		
		return page;
	}
	@SuppressWarnings("unchecked")
	@Override
	public WzInfoDTO createWZForMobile(WzInfoParaDTO infoDTO, List<M2ultipartFileDTO> files) {

		WzNewResponseDTO newResponse = new WzNewResponseDTO();
		Document wzDocObj = null;

		P8Connection p8con = connService.getP8Connection(this.ecmConf.getTargetObjectStore(infoDTO.getRealm()));
		// > 查询到案例所在的文件夹
		// CaseDTO caseDTO = (CaseDTO)
		// this.caseMgmtDmn.getCaseByIdentifier(p8con.getObjectStore(),
		// infoDTO.getCaseIdentifier());
		Folder caseFolder = this.folderUtil.loadById(p8con.getObjectStore(), infoDTO.getCaseId());

		// > 在case根目录创建文件夹【mobile】，用于存储微作关联的文件
		// > 如果没有mobile文件夹，则创建，这个属于案例子文件夹
		Folder mobileFolder = (Folder) this.folderUtil.loadAndCreateCaseSubFolderByParentFolder(p8con.getObjectStore(),
				caseFolder, ecmConf.getMobileFileStoreFolderName());

		// > 在mobile文件夹下存放文档
		List<DocumentDTO> addDocs = null;
		if (files != null && files.size() > 0) {
			try {
				PropertiesExDTO propEx = new PropertiesExDTO();
				propEx.setFileType(infoDTO.getPropertiesEx().getFileType());
				propEx.setBusiness(infoDTO.getPropertiesEx().getBusiness());
				propEx.setDomain(infoDTO.getPropertiesEx().getDomain());
				propEx.setOrg(infoDTO.getPropertiesEx().getOrg());
				propEx.setRegion(infoDTO.getPropertiesEx().getRegion());
				propEx.setResourceType(ResourceConstants.ResourceType.RES_PCK_PROJECT);
				propEx.setSearchable(true); // 项目的资料都可以查询
				propEx.setTags(infoDTO.getPropertiesEx().getTags());
				// 文档真正的发布者编码
				propEx.setPublisher(infoDTO.getUserId());
				addDocs = docUtil.createDocumentsByFolder(p8con.getObjectStore(), mobileFolder, propEx, files);
			} catch (Exception ex) {
				logger.error(">>>在mobile文件夹下创建文件失败，详情：" + ex.getMessage(), ex);
				// ex.printStackTrace();
			}
		}

		// > 在【微作】根目录创建根案例标识相同的文件夹（如果没有），用于存储微作本身
		// 注意使用的case id
		Folder wzRootFolder = this.createWZFolderRoot(p8con.getObjectStore(), infoDTO.getCaseId());
		// > 创建微作实体
		StringList fileList = Factory.StringList.createList(); // 微作关联的文档id列表
		StringList atUserList = Factory.StringList.createList(); // 微作@人员
		String[] associatePersons = infoDTO.getAtPersons();
		if (associatePersons != null && associatePersons.length > 0) {
			for (String p : associatePersons) {
				atUserList.add(p);
			}
		}
		if (addDocs != null && addDocs.size() > 0) {
			for (DocumentDTO doc : addDocs) {
				fileList.add(doc.getVersionSeriesId()); // 修改存为版本系列id
				newResponse.addDocId(doc.getVersionSeriesId()); // 修改存为版本系列id
			}
		}

		try {
			wzDocObj = Factory.Document.createInstance(p8con.getObjectStore(), this.ecmConf.getWzDefaultClassId());
			Properties props = wzDocObj.getProperties();
			props.putValue("DocumentTitle", "微作" + System.currentTimeMillis());
			props.putValue(this.extPropWZConf.getAssociatePerson(), atUserList); // "XZ_AssociatePerson"
			props.putValue(this.extPropWZConf.getAssociateFileLink(), fileList); // "XZ_AssociateFileLink"
			props.putValue(this.extPropWZConf.getContent(), infoDTO.getContent()); // "XZ_Content"
			props.putValue(this.extPropWZConf.getUpvoteCount(), 0); // "XZ_UpvoteCount"
			props.putValue(this.extPropWZConf.getAssociateTache(), infoDTO.getTache()); // "XZ_AssociateTache"
			props.putValue(this.extPropWZConf.getAssociateProject(),
					infoDTO.getPropertiesEx().getAssociateProject()); // "XZ_AssociateProject"
			props.putValue(this.extPropWZConf.getAssociateTask(), infoDTO.getTask()); // "XZ_AssociateTask"
			// 微作真正的发布者编码
			props.putValue(this.extPropWZConf.getPublisher(), infoDTO.getUserId());
			logger.info(">>>微作类型：" + infoDTO.getType());
			props.putValue(this.extPropWZConf.getWZType(), infoDTO.getType());

			JSONObject locationJson = new JSONObject();
			locationJson.put("name", infoDTO.getLocationName());
			locationJson.put("address", infoDTO.getAddress());
			locationJson.put("latitude", infoDTO.getLatitude());
			locationJson.put("longitude", infoDTO.getLongitude());
			props.putValue(this.extPropWZConf.getLocation(), locationJson.toJSONString());
			
			wzDocObj.set_MimeType("text/plain");
			wzDocObj.checkin(AutoClassify.AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
			wzDocObj.save(RefreshMode.REFRESH);
			ReferentialContainmentRelationship rcr = wzRootFolder.file(wzDocObj, AutoUniqueName.AUTO_UNIQUE,
					wzDocObj.get_Name(), DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
			rcr.save(RefreshMode.REFRESH);

			newResponse.setWzId(wzDocObj.get_Id().toString());
		} catch (Exception ex) {
			logger.error(">>>创建微作实体失败，详情：" + ex.getMessage(), ex);
			logger.info(">>>正在进行回滚操作，删除已上传的文件......");
			if (addDocs != null && addDocs.size() > 0) {
				for (DocumentDTO doc : addDocs) {
					logger.info(">>>删除文件 [{}]......", doc.getId());
					this.docUtil.deleteDocument(p8con.getObjectStore(), doc.getId());
				}
			}
		}

		return this.caseUtil.document2WzDto(p8con.getObjectStore(),  wzDocObj, true, true);//document2WzDto(p8con.getObjectStore(), wzDocObj);
		// return newResponse;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Object createWZForWeb(WzInfoParaWebDTO infoDTO) {

		P8Connection p8con = connService.getP8Connection(this.ecmConf.getTargetObjectStore(infoDTO.getRealm()));
		
		// > 在【微作】根目录创建根案例标识相同的文件夹（如果没有），用于存储微作本身
		Folder wzRootFolder = this.createWZFolderRoot(p8con.getObjectStore(), infoDTO.getCaseId());
    		
		// > 创建微作实体
		StringList fileList = Factory.StringList.createList();       // 微作关联的文档id列表
		StringList atUserList = Factory.StringList.createList(); // 微作@人员
		String[] associatePersons = infoDTO.getAtPersons();
		if(associatePersons !=null && associatePersons.length>0){
			for(String p : associatePersons){
				atUserList.add(p);
			}
		}
		if(infoDTO.getFiles() != null && infoDTO.getFiles().length>0){
			for(String docId : infoDTO.getFiles()) {
				fileList.add(docId);
			}
		}
		try {
			Document wzDocObj = Factory.Document.createInstance(p8con.getObjectStore(),
					this.ecmConf.getWzDefaultClassId());
			Properties props = wzDocObj.getProperties();
			props.putValue("DocumentTitle", "微作" + System.currentTimeMillis());
			props.putValue(this.extPropWZConf.getAssociatePerson(), atUserList); // "XZ_AssociatePerson"
			props.putValue(this.extPropWZConf.getAssociateFileLink(), fileList); // "XZ_AssociateFileLink"
			props.putValue(this.extPropWZConf.getContent(), infoDTO.getContent()); // "XZ_Content"
			props.putValue(this.extPropWZConf.getUpvoteCount(), 0); // "XZ_UpvoteCount"
			props.putValue(this.extPropWZConf.getAssociateTache(), infoDTO.getChannelCode()); // "XZ_AssociateTache"
			props.putValue(this.extPropWZConf.getAssociateProject(), infoDTO.getCaseId()); // "XZ_AssociateProject"
			//wzDocObj.getProperties().putValue(this.extPropWZConf.getAssociateTask(), infoDTO.getTask()); // "XZ_AssociateTask"
			// 增加发布者编码
			props.putObjectValue(this.extPropWZConf.getPublisher(), infoDTO.getUserId());
			wzDocObj.set_MimeType("text/plain");
			// 新增微作类型属性，2017.02.20 by weifj
			props.putValue(this.extPropWZConf.getWZType(), infoDTO.getType());
			wzDocObj.checkin(AutoClassify.AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
			wzDocObj.save(RefreshMode.REFRESH);
			ReferentialContainmentRelationship rcr = wzRootFolder.file(wzDocObj, AutoUniqueName.AUTO_UNIQUE,
					wzDocObj.get_Name(), DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
			rcr.save(RefreshMode.REFRESH);

			return this.caseUtil.document2WzDto(p8con.getObjectStore(),  wzDocObj, true, true);//document2WzDto(p8con.getObjectStore(), wzDocObj);
			
		} catch (Exception ex) {
			logger.error(">>>创建微作实体失败，详情：" + ex.getMessage());
			ex.printStackTrace();
			logger.info(">>>正在进行回滚操作，删除已上传的文件......");
			if (infoDTO.getFiles() != null && infoDTO.getFiles().length > 0) {
				for (String docId : infoDTO.getFiles()) {
					logger.info(">>>删除文件 [{}]......", docId);
					this.docUtil.deleteDocument(p8con.getObjectStore(), docId);
				}
			}
		}
	    return null;
	}
	/**
	    * 创建微作根目录文件夹，并返回
	    * 以案例标识作为微作根文件夹名称
	    * @param caseIdentifier
	    * @return
	    */
	private Folder createWZFolderRoot(ObjectStore os, String caseIdentifier) throws BusinessException {
		
		try {
			
			Folder wzRootFolder = this.folderUtil.loadByPath(os, ecmConf.getWzRootPath());//.loadById(os, ecmConf.getWzRootFolderId());
			if(null == wzRootFolder) throw new BusinessException("微作根文件夹[{0}]不存在", ecmConf.getWzRootFolderId());
			
			Folder wzFolder = this.folderUtil.loadAndCreateByParentFolder(os, wzRootFolder, caseIdentifier);
			/*if(wzFolder != null){
				FolderDTO folder = new FolderDTO();
				folder.setGuid(wzFolder.get_Id().toString());
				folder.setDateLastModified(wzFolder.get_DateLastModified().getTime());
				folder.setName(wzFolder.get_Name());
				folder.setLastModifier(wzFolder.get_LastModifier());
				folder.setOwner(wzFolder.get_Owner());
				
				return folder;
			}*/
			return wzFolder;
			
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			ex.printStackTrace();
			throw new BusinessException("创建微作文件夹失败，详情："+ex.getMessage());
		}
	}
	
	@Override
	public WzInfoDTO getWZById(String realm, String id) {
		
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		return this.caseUtil.getWzInfoDTO(p8conn.getObjectStore(), id);
	}
	@Override
	public WzInfoDTO getWZByIdThumbnailByte(String realm, String id) {
		
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		return this.caseUtil.getWzInfoDTOThumbnailByte(p8conn.getObjectStore(), id);
	}
	
	@Override
	public WzInfoDTO getWZByIdForMobile(String realm, String id, String baseURL, String contextPath) {
		
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		WzInfoDTO dto = this.caseUtil.getWzInfoDTO(p8conn.getObjectStore(), id);
		List<WzInfoDTO> array = new ArrayList<WzInfoDTO>();
		array.add(dto);
		this.setWzThumb(array, baseURL, contextPath);
		return dto;
	}
	@Override
	public Object sendWzToUserByEmail(WzInfoSendToUserDTO dto) {
		
		if(StringUtils.isEmpty(dto.getCaseCode())){
			dto.setCaseCode("--");
		}
		if(StringUtils.isEmpty(dto.getCaseName())){
			dto.setCaseName("--");
		}
		WzEmailResponseDTO resp = new WzEmailResponseDTO();
		
		String[] userIds = dto.getUserIds();
		for(String userCode : userIds){
			DcmUser findUser = this.userOrgService.getUserEntityByCode(userCode);
			if(StringUtils.isEmpty(findUser.getEmail())){
				logger.warn(">>>用户[{}]没有登记邮箱地址，已忽略发送", findUser.getUserName());
				resp.getNoEUsers().add(findUser.getUserName());
				continue;
			}
			if(!MailUtil.checkEmail(findUser.getEmail())){
				logger.warn(">>>用户[{}]的邮箱地址[{}]无效，已忽略发送", findUser.getUserCode(), findUser.getEmail());
				resp.getIllegalEUsers().add(findUser.getUserName());
				continue;
			}
			try {
				logger.info(">>>已把微作发送给用户[{}]，邮箱[{}]", findUser.getUserName(), findUser.getEmail());
				if(!StringUtils.isEmpty(dto.getSubject())){
					MailUtil.sendTextMessage(this.mailConf.getUserName(), this.mailConf.getPassword(), dto.getSubject(), dto.getContent(), findUser.getEmail());
				}else {
					MailUtil.sendTextMessage(this.mailConf.getUserName(), this.mailConf.getPassword(), "任务微作：["+dto.getCaseName()+"]-["+dto.getCaseCode()+"]", dto.getContent(), findUser.getEmail());
				}
				
			} catch (Exception e) {
				logger.error(">>>发送给用户["+findUser.getUserName()+"]的邮件失败，详情："+e.getMessage(), e);
				resp.getFailEUsers().add(findUser.getUserName());
			}
		}
		return resp;
	}
	/*@Override
	public Pagination getTaskSummary(String userCode, int pageNo, int pageSize) {
		
		Date timeStart = new Date();

		List<DcmGroup> projects = new ArrayList<DcmGroup>();
		List<DcmGroup> tempProjects = null;
		
		StringBuilder buf = new StringBuilder();
		buf.append("select * From dcm_group g  where g.realm in");
		buf.append("(select orgName from Dcm_Organization org where orgCode in");
		buf.append(String.format("(select udr.domainCode from Dcm_Userdomainrole udr where udr.userCode = '%s'  and udr.roleCode = '%s')", userCode, RoleConstants.RoleCode.R_Ins_DecisionManager));                
		buf.append(")");
		buf.append("and (g.groupCode like 'XZ_CASETYPE_JYXM_%' or g.groupCode like 'XZ_CASETYPE_HZXM_%')");
		logger.debug(">>>院级项目SQL："+buf.toString());
		tempProjects = this.groupDmn.searchBySQL(buf.toString());
		if(tempProjects != null && !tempProjects.isEmpty()){
			projects.addAll(tempProjects);
		}
		buf.setLength(0);
		buf.append(" select *  from dcm_group g where g.orgcode in");
		buf.append(String.format("(select udr.domaincode from dcm_userdomainrole udr  where udr.usercode = '%s'  and udr.rolecode = '%s')",userCode, RoleConstants.RoleCode.R_Dep_DecisionManager));
        buf.append("and (g.groupcode like 'XZ_CASETYPE_JYXM_%' or g.groupcode like 'XZ_CASETYPE_HZXM_%')");
        logger.debug(">>>所级项目SQL："+buf.toString());
        tempProjects = this.groupDmn.searchBySQL(buf.toString());
        if(tempProjects != null && !tempProjects.isEmpty()){
			projects.addAll(tempProjects);
		}
        if(projects.isEmpty()) {
        	return null;
        }
        logger.info(">>>计算前结果集大小：[{}]，获取项目列表用时：[{}ms]", projects.size(), new Date().getTime()-timeStart.getTime());
        timeStart = new Date();
        DcmUser user = this.userOrgService.getUserEntityByCode(userCode);
        final ForkJoinPool pool = new ForkJoinPool(5);
        final ComputeMyProjectTask projTask = new ComputeMyProjectTask(user.getRealm(), projects, this.connectionService, this.caseUtil, this.ecmConf, 0, projects.size());
        Map<String, CaseDTO> projectDTOMap = pool.invoke(projTask);
        
        logger.info(">>>计算完成后结果集大小：[{}]，检索ECM项目用时：[{}ms]", projectDTOMap.size(),  new Date().getTime()-timeStart.getTime());
        timeStart = new Date();
        Map<String, Object[]> propertiesValuesMap = new HashMap<String, Object[]>(); 
        propertiesValuesMap.put("caseIdentifier", projectDTOMap.keySet().toArray());
        
        Pagination pageResult = this.taskDmn.findByPropertiesAndValues(pageNo, pageSize, propertiesValuesMap, "dateCreated", false);
        logger.info(">>>分页查询任务用时：[{}ms]", new Date().getTime()-timeStart.getTime());
        if(null == pageResult.getData() || 0 == pageResult.getData().size()) {
        	return null;
        }
        timeStart = new Date();
        @SuppressWarnings("unchecked")
		List<DcmTask> tasks = (List<DcmTask>) pageResult.getData();
        final ComputeTaskSummaryTask taskSummaryTask= new ComputeTaskSummaryTask(user.getRealm(), this.connectionService, this.caseUtil, this.ecmConf, this.socialResDmn, 0, tasks.size(), projectDTOMap, tasks);
        List<TaskAndProjectRespDTO> taskprjDTOs = pool.invoke(taskSummaryTask);
        pageResult.setData(taskprjDTOs);
        logger.info(">>>任务汇总用时：[{}ms]", new Date().getTime()-timeStart.getTime());
        return pageResult;
	}*/
	/**
	 * 实现逻辑比较复杂，函数体比较长，需要拆分优化
	 */
	@Override
	public Pagination getTaskSummary(TaskSummaryFilterDTO filterInfo) {
		
		Date timeStart = new Date();

		List<DcmGroup> allProjects = new ArrayList<DcmGroup>();
		List<DcmGroup> tempProjects = null;
		Pagination pageResult = null;
		// 筛选只有决策管理角色范围内的项目
		StringBuilder buf = new StringBuilder();
		buf.append("select * From dcm_group g  where g.realm in");
		buf.append("(select orgName from Dcm_Organization org where orgCode in");
		buf.append(String.format("(select udr.domainCode from Dcm_Userdomainrole udr where udr.userCode = '%s'  and udr.roleCode = '%s')", filterInfo.getUserCode(), RoleConstants.RoleCode.R_Ins_DecisionManager));  
		buf.append(")");
		buf.append("and (g.groupCode like 'XZ_CASETYPE_JYXM_%' or g.groupCode like 'XZ_CASETYPE_HZXM_%')");
		logger.info(">>>院级项目过滤");
		if(filterInfo.getProjectNo()!=null && filterInfo.getProjectNo().length>0) {
			buf.append(" and g.guid in ("+ StringUtils.join(filterInfo.getProjectNo(),",").replace("{", "'{").replace("}", "}'")+")");
		}
		logger.debug(">>>院级项目SQL："+buf.toString());
		tempProjects = this.groupDmn.searchBySQL(buf.toString());
		if(tempProjects != null && !tempProjects.isEmpty()) {
			allProjects.addAll(tempProjects);
		}
		buf.setLength(0);
		buf.append(" select *  from dcm_group g where g.orgcode in");
		buf.append(String.format("(select udr.domaincode from dcm_userdomainrole udr  where udr.usercode = '%s'  and udr.rolecode = '%s')",filterInfo.getUserCode(), RoleConstants.RoleCode.R_Dep_DecisionManager));
        buf.append("and (g.groupcode like 'XZ_CASETYPE_JYXM_%' or g.groupcode like 'XZ_CASETYPE_HZXM_%')");
    	logger.info(">>>所级项目过滤");
        if(filterInfo.getProjectNo()!=null && filterInfo.getProjectNo().length>0) {
			buf.append(" and g.guid in ("+org.apache.commons.lang3.StringUtils.join(filterInfo.getProjectNo(),",").replace("{", "'{").replace("}", "}'")+")");
		}
        logger.debug(">>>所级项目SQL："+buf.toString());
        tempProjects = this.groupDmn.searchBySQL(buf.toString());
        if(tempProjects != null && !tempProjects.isEmpty()){
			allProjects.addAll(tempProjects);
		}
        if(allProjects.isEmpty()) {
        	return new Pagination(filterInfo.getPageNo(), filterInfo.getPageSize(), 0);
        }
        logger.info(">>>计算前结果集大小：[{}]，获取项目列表用时：[{}ms]", allProjects.size(), new Date().getTime()-timeStart.getTime());
        timeStart = new Date();
        // DcmUser user = this.userOrgService.getUserEntityByCode(filterInfo.getUserCode());
        final ForkJoinPool pool = new ForkJoinPool(5);
		final ComputeMyProjectTask projTask = new ComputeMyProjectTask(filterInfo.getRealm(), allProjects,
				this.connectionService, this.caseUtil, this.ecmConf, this.extPropCaseConf, filterInfo.getBusinessType(),  0, allProjects.size());
		Map<String, CaseDTO> projectDTOMap = pool.invoke(projTask);

		if(null == projectDTOMap || projectDTOMap.isEmpty()){
			  logger.info(">>>没有找到相关项目数据，不继续执行任务查询。");
			return null;
		}
		
        logger.info(">>>计算完成后结果集大小：[{}]，检索ECM项目用时：[{}ms]", projectDTOMap.size(),  new Date().getTime()-timeStart.getTime());
        timeStart = new Date();
        Map<String, Object[]> propertiesValuesMap = new HashMap<String, Object[]>(); 
        propertiesValuesMap.put("caseIdentifier", projectDTOMap.keySet().toArray());
        logger.info(">>>任务类型过滤......");
        if(filterInfo.getTaskType()!= null && filterInfo.getTaskType().length>0){
        	propertiesValuesMap.put("type", filterInfo.getTaskType());
        }
        logger.debug(">>>是否本地支持过滤......");
        if(filterInfo.getLocalSupport()!= null && filterInfo.getLocalSupport().length>0){
        	propertiesValuesMap.put("localSupport", filterInfo.getLocalSupport());
        }

        List<SimplePropertyFilter> taskPropFilters = new ArrayList<SimplePropertyFilter>();
    	// 微作类型：任务，2
    	logger.debug(">>>类型：2，任务微作过滤......");
    	taskPropFilters.add(new SimplePropertyFilter(this.extPropWZConf.getWZType(), DataType.SingleOfInteger, new Integer[]{2}));
    	// 关联项目
    	logger.debug(">>>关联项目过滤......");
    	taskPropFilters.add(new SimplePropertyFilter(this.extPropWZConf.getAssociateProject(), DataType.SingleOfString, projectDTOMap.keySet().toArray()));
    	
        if(filterInfo.getAssociatePerson() != null && filterInfo.getAssociatePerson().length > 0) {
        	logger.debug(">>>@执行者过滤......");
        	// 关联人
        	if(Objects.equal("AssociatePersonEmpty", filterInfo.getAssociatePerson()[0])){
        		logger.debug(">>>过滤没指定执行者的任务");
        		taskPropFilters.add(new SimplePropertyFilter(this.extPropWZConf.getAssociatePerson(), DataType.ListOfString, null));
        	} else {
        		logger.debug(">>>过滤指定执行者的任务："+ JSONObject.toJSONString(filterInfo.getAssociatePerson()));
        		taskPropFilters.add(new SimplePropertyFilter(this.extPropWZConf.getAssociatePerson(), DataType.ListOfString, filterInfo.getAssociatePerson()));
        	}
        }
        if(filterInfo.getPublisher() != null && filterInfo.getPublisher().length > 0) {
        	logger.debug(">>>发布者过滤......");
        	// 关联人
        	logger.debug(">>>过滤发布者的任务："+ JSONObject.toJSONString(filterInfo.getPublisher()));
        	taskPropFilters.add(new SimplePropertyFilter("Creator", DataType.SingleOfString, filterInfo.getPublisher()));
        }
        logger.info(">>>CE中任务类型过滤计算");
        P8Connection p8con = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(filterInfo.getRealm()));
    	List<WzInfoDTO> wzTasks = this.caseUtil.getWzsByFilter(p8con.getObjectStore(), taskPropFilters);
    	if(null == wzTasks || wzTasks.isEmpty()) {
    		 logger.info(">>>没有找到相关的任务微作数据，不继续执行任务查询。");
    		return null;
    	}
    	// 任务微作id与微作发布者Map映射
    	Map<String, String> wzTaskId2CreatorMap = new HashMap<String, String>();
    	if(wzTasks != null && !wzTasks.isEmpty()){
    		String[] guids = new String[wzTasks.size()];
			for(int i =0; i<  wzTasks.size(); i++) {
				guids[i] = wzTasks.get(i).getGuid();
				if(!wzTaskId2CreatorMap.containsKey(wzTasks.get(i).getGuid())) {
					wzTaskId2CreatorMap.put(wzTasks.get(i).getGuid(), wzTasks.get(i).getCreator());
				}
			}
    		propertiesValuesMap.put("taskId", guids);
    		// wzTaskId2CreatorMap = wzTasks.stream().collect(DistCollectors.toMap(WzInfoDTO::getGuid, WzInfoDTO::getCreator));
    	}
        logger.info(">>>任务状态过滤......");
        if(filterInfo.getStatus() != null && filterInfo.getStatus().length >0) {
        	propertiesValuesMap.put("status", filterInfo.getStatus());
        }
        pageResult = this.taskDmn.findByPropertiesAndValues(filterInfo.getPageNo(), filterInfo.getPageSize(), propertiesValuesMap, "endTime", filterInfo.getBeginTime(), filterInfo.getEndTime(), "dateCreated", false);
        logger.info(">>>分页查询任务用时：[{}ms]", new Date().getTime()-timeStart.getTime());
        if(null == pageResult.getData() || 0 == pageResult.getData().size()) {
        	return pageResult;
        }
        timeStart = new Date();
        @SuppressWarnings("unchecked")
		List<DcmTask> tasks = (List<DcmTask>) pageResult.getData();
        logger.info(">>>获取机构映射信息");
        List<Org2UsersDTO> org2users = this.userOrgService.getOrgUserTree(false, filterInfo.getRealm());
        Map<String, String> orgCodeNameMap = new HashMap<String, String>(0);
        if(org2users != null && !org2users.isEmpty()){
        	for(Org2UsersDTO o2u : org2users) {
        		if(!orgCodeNameMap.containsKey(o2u.getOrgCode())) {
        			orgCodeNameMap.put(o2u.getOrgCode(), o2u.getAlias());
        		}
        	}
        	// orgCodeNameMap = org2users.stream().collect(DistCollectors.toMap(Org2UsersDTO::getOrgCode, Org2UsersDTO::getAlias));
        }
        final ComputeTaskSummaryTask taskSummaryTask= new ComputeTaskSummaryTask(filterInfo.getRealm(), this.connectionService, this.caseUtil, this.ecmConf, 0, tasks.size(), 
        		projectDTOMap, tasks, orgCodeNameMap, wzTaskId2CreatorMap, this.udrDmn);
        List<TaskAndProjectRespDTO> taskprjDTOs = pool.invoke(taskSummaryTask);
        // Pagination p = new Pagination(pageNo, pageSize, totalCount);
        pageResult.setData(taskprjDTOs);
        logger.info(">>>任务汇总用时：[{}ms]", new Date().getTime()-timeStart.getTime());
        return pageResult;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Pagination getTaskSummaryEx(TaskSummaryFilterDTO filterInfo) {

		Date timeStart = new Date();
		Pagination pageResult = null;

		List<DcmGroup> allProjects = getProjectsByFilter(filterInfo);

		if (null== allProjects || allProjects.isEmpty()) {
			return new Pagination(filterInfo.getPageNo(), filterInfo.getPageSize(), 0);
		}
		logger.info(">>>计算前结果集大小：[{}]，获取项目列表用时：[{}ms]", allProjects.size(), new Date().getTime() - timeStart.getTime());
		timeStart = new Date();
		final ForkJoinPool pool = new ForkJoinPool(5);
		final ComputeMyProjectTask projTask = new ComputeMyProjectTask(filterInfo.getRealm(), allProjects,
				this.connectionService, this.caseUtil, this.ecmConf, this.extPropCaseConf, filterInfo.getBusinessType(),
				0, allProjects.size());
		Map<String, CaseDTO> projectDTOMap = pool.invoke(projTask);

		if (null == projectDTOMap || projectDTOMap.isEmpty()) {
			logger.info(">>>没有找到相关项目数据，不继续执行任务查询。");
			return null;
		}

		logger.info(">>>计算完成后结果集大小：[{}]，检索ECM项目用时：[{}ms]", projectDTOMap.size(),
				new Date().getTime() - timeStart.getTime());
		timeStart = new Date();
		Map<String, Object[]> propertiesValuesMap = new HashMap<String, Object[]>();
		propertiesValuesMap.put("caseIdentifier", projectDTOMap.keySet().toArray());
		logger.info(">>>任务类型过滤......");
		if (filterInfo.getTaskType() != null && filterInfo.getTaskType().length > 0) {
			propertiesValuesMap.put("type", filterInfo.getTaskType());
		}
		logger.debug(">>>是否本地支持过滤......");
		if (filterInfo.getLocalSupport() != null && filterInfo.getLocalSupport().length > 0) {
			propertiesValuesMap.put("localSupport", filterInfo.getLocalSupport());
		}

		List<SimplePropertyFilter> taskPropFilters = new ArrayList<SimplePropertyFilter>();
		// 微作类型：任务，2
		logger.debug(">>>类型：2，任务微作过滤......");
		taskPropFilters.add( new SimplePropertyFilter(this.extPropWZConf.getWZType(), DataType.SingleOfInteger, new Integer[] { 2 }));
		// 关联项目
		logger.debug(">>>关联项目过滤......");
		taskPropFilters.add(new SimplePropertyFilter(this.extPropWZConf.getAssociateProject(), DataType.SingleOfString, projectDTOMap.keySet().toArray()));

		if (filterInfo.getAssociatePerson() != null && filterInfo.getAssociatePerson().length > 0) {
			logger.debug(">>>@执行者过滤......");
			// 关联人
			if (Objects.equal("AssociatePersonEmpty", filterInfo.getAssociatePerson()[0])) {
				logger.debug(">>>过滤没指定执行者的任务");
				taskPropFilters.add(new SimplePropertyFilter(this.extPropWZConf.getAssociatePerson(), DataType.ListOfString, null));
			} else {
				logger.debug(">>>过滤指定执行者的任务：" + JSONObject.toJSONString(filterInfo.getAssociatePerson()));
				taskPropFilters.add(new SimplePropertyFilter(this.extPropWZConf.getAssociatePerson(), DataType.ListOfString, filterInfo.getAssociatePerson()));
			}
		}
		if (filterInfo.getPublisher() != null && filterInfo.getPublisher().length > 0) {
			logger.debug(">>>发布者过滤......");
			// 关联人
			logger.debug(">>>过滤发布者的任务：" + JSONObject.toJSONString(filterInfo.getPublisher()));
			// this.extPropWZConf.getPublisher()
			taskPropFilters.add( new SimplePropertyFilter("Creator", DataType.SingleOfString, filterInfo.getPublisher()));
		}
		logger.info(">>>CE中任务类型过滤计算");
		P8Connection p8con = this.connectionService
				.getP8Connection(this.ecmConf.getTargetObjectStore(filterInfo.getRealm()));
		List<WzInfoDTO> wzTasks = this.caseUtil.getWzsByFilter(p8con.getObjectStore(), taskPropFilters);
		if (null == wzTasks || wzTasks.isEmpty()) {
			logger.info(">>>没有找到相关的任务微作数据，不继续执行任务查询。");
			return null;
		}
		// 任务微作id与微作发布者Map映射
		Map<String, String> wzTaskId2CreatorMap = new HashMap<String, String>();
		if (wzTasks != null && !wzTasks.isEmpty()) {
			String[] guids = new String[wzTasks.size()];
			for(int i =0; i<  wzTasks.size(); i++) {
				guids[i] = wzTasks.get(i).getGuid();
				if(!wzTaskId2CreatorMap.containsKey(wzTasks.get(i).getGuid())) {
					wzTaskId2CreatorMap.put(wzTasks.get(i).getGuid(), wzTasks.get(i).getCreator());
				}
			}
			propertiesValuesMap.put("taskId", guids);
		}
		logger.info(">>>任务状态过滤......");
		if (filterInfo.getStatus() != null && filterInfo.getStatus().length > 0) {
			propertiesValuesMap.put("status", filterInfo.getStatus());
		}
		List<DateFilterRangeDTO> datePropertyFilters = new ArrayList<DateFilterRangeDTO>();
		datePropertyFilters.add(new DateFilterRangeDTO("endTime", filterInfo.getBeginTime(), filterInfo.getEndTime()));
		datePropertyFilters.add(new DateFilterRangeDTO("dateCreated", filterInfo.getPublishBeginTime(), filterInfo.getPublishEndTime()));
		
		pageResult = this.taskDmn.findByPropertiesAndValues(filterInfo.getPageNo(), filterInfo.getPageSize(),
				propertiesValuesMap, datePropertyFilters, "dateCreated", false);
		logger.info(">>>分页查询任务用时：[{}ms]", new Date().getTime() - timeStart.getTime());
		if (null == pageResult.getData() || 0 == pageResult.getData().size()) {
			return pageResult;
		}
		timeStart = new Date();
		List<DcmTask> tasks = (List<DcmTask>) pageResult.getData();
		logger.info(">>>获取机构映射信息");
		List<Org2UsersDTO> org2users = this.userOrgService.getOrgUserTree(false, filterInfo.getRealm());
		Map<String, String> orgCodeNameMap = new HashMap<String, String>(0);
		if (org2users != null && !org2users.isEmpty()) {
			for(Org2UsersDTO o2u : org2users) {
				if(!orgCodeNameMap.containsKey(o2u.getOrgCode())) {
					orgCodeNameMap.put(o2u.getOrgCode(), o2u.getAlias());
				}
			}
		}
		final ComputeTaskSummaryTask taskSummaryTask = new ComputeTaskSummaryTask(filterInfo.getRealm(),
				this.connectionService, this.caseUtil, this.ecmConf, 0, tasks.size(), projectDTOMap, tasks,
				orgCodeNameMap, wzTaskId2CreatorMap, this.udrDmn);
		List<TaskAndProjectRespDTO> taskprjDTOs = pool.invoke(taskSummaryTask);
		taskSummaryTask.clear();
		// Pagination p = new Pagination(pageNo, pageSize, totalCount);
		pageResult.setData(taskprjDTOs);
		logger.info(">>>任务汇总用时：[{}ms]", new Date().getTime() - timeStart.getTime());
		return pageResult;
	}
	@Override
	@SuppressWarnings("unchecked")
	public Pagination getTaskSummaryNew(TaskSummaryFilterDTO filterInfo) {

		Date timeStart = new Date();
		Pagination pageResult = null;

		List<DcmGroup> allProjects = this.getProjectsByFilterAndPrivs(filterInfo);

		if (null== allProjects || allProjects.isEmpty()) {
			return new Pagination(filterInfo.getPageNo(), filterInfo.getPageSize(), 0);
		}
		logger.info(">>>计算前结果集大小：[{}]，获取项目列表用时：[{}ms]", allProjects.size(), new Date().getTime() - timeStart.getTime());
		timeStart = new Date();
		final ForkJoinPool pool = new ForkJoinPool(5);
		final ComputeMyProjectTask projTask = new ComputeMyProjectTask(filterInfo.getRealm(), allProjects,
				this.connectionService, this.caseUtil, this.ecmConf, this.extPropCaseConf, filterInfo.getBusinessType(),
				0, allProjects.size());
		Map<String, CaseDTO> projectDTOMap = pool.invoke(projTask);

		if (null == projectDTOMap || projectDTOMap.isEmpty()) {
			logger.info(">>>没有找到相关项目数据，不继续执行任务查询。");
			return null;
		}

		logger.info(">>>计算完成后结果集大小：[{}]，检索ECM项目用时：[{}ms]", projectDTOMap.size(),
				new Date().getTime() - timeStart.getTime());
		timeStart = new Date();
		Map<String, Object[]> propertiesValuesMap = new HashMap<String, Object[]>();
		propertiesValuesMap.put("caseIdentifier", projectDTOMap.keySet().toArray());
		logger.info(">>>任务类型过滤......");
		if (filterInfo.getTaskType() != null && filterInfo.getTaskType().length > 0) {
			propertiesValuesMap.put("type", filterInfo.getTaskType());
		}
		logger.debug(">>>是否本地支持过滤......");
		if (filterInfo.getLocalSupport() != null && filterInfo.getLocalSupport().length > 0) {
			propertiesValuesMap.put("localSupport", filterInfo.getLocalSupport());
		}

		List<SimplePropertyFilter> taskPropFilters = new ArrayList<SimplePropertyFilter>();
		// 微作类型：任务，2
		logger.debug(">>>类型：2，任务微作过滤......");
		taskPropFilters.add( new SimplePropertyFilter(this.extPropWZConf.getWZType(), DataType.SingleOfInteger, new Integer[] { 2 }));
		// 关联项目
		logger.debug(">>>关联项目过滤......");
		taskPropFilters.add(new SimplePropertyFilter(this.extPropWZConf.getAssociateProject(), DataType.SingleOfString, projectDTOMap.keySet().toArray()));

		if (filterInfo.getAssociatePerson() != null && filterInfo.getAssociatePerson().length > 0) {
			logger.debug(">>>@执行者过滤......");
			// 关联人
			if (Objects.equal("AssociatePersonEmpty", filterInfo.getAssociatePerson()[0])) {
				logger.debug(">>>过滤没指定执行者的任务");
				taskPropFilters.add(new SimplePropertyFilter(this.extPropWZConf.getAssociatePerson(), DataType.ListOfString, null));
			} else {
				logger.debug(">>>过滤指定执行者的任务：" + JSONObject.toJSONString(filterInfo.getAssociatePerson()));
				taskPropFilters.add(new SimplePropertyFilter(this.extPropWZConf.getAssociatePerson(), DataType.ListOfString, filterInfo.getAssociatePerson()));
			}
		}
		if (filterInfo.getPublisher() != null && filterInfo.getPublisher().length > 0) {
			logger.debug(">>>发布者过滤......");
			// 关联人
			logger.debug(">>>过滤发布者的任务：" + JSONObject.toJSONString(filterInfo.getPublisher()));
			// this.extPropWZConf.getPublisher()
			taskPropFilters.add( new SimplePropertyFilter("Creator", DataType.SingleOfString, filterInfo.getPublisher()));
		}
		logger.info(">>>CE中任务类型过滤计算");
		P8Connection p8con = this.connectionService
				.getP8Connection(this.ecmConf.getTargetObjectStore(filterInfo.getRealm()));
		List<WzInfoDTO> wzTasks = this.caseUtil.getWzsByFilter(p8con.getObjectStore(), taskPropFilters);
		if (null == wzTasks || wzTasks.isEmpty()) {
			logger.info(">>>没有找到相关的任务微作数据，不继续执行任务查询。");
			return null;
		}
		// 任务微作id与微作发布者Map映射
		Map<String, String> wzTaskId2CreatorMap = new HashMap<String, String>();
		if (wzTasks != null && !wzTasks.isEmpty()) {
			String[] guids = new String[wzTasks.size()];
			for(int i =0; i<  wzTasks.size(); i++) {
				guids[i] = wzTasks.get(i).getGuid();
				if(!wzTaskId2CreatorMap.containsKey(wzTasks.get(i).getGuid())) {
					wzTaskId2CreatorMap.put(wzTasks.get(i).getGuid(), wzTasks.get(i).getCreator());
				}
			}
			propertiesValuesMap.put("taskId", guids);
		}
		logger.info(">>>任务状态过滤......");
		if (filterInfo.getStatus() != null && filterInfo.getStatus().length > 0) {
			propertiesValuesMap.put("status", filterInfo.getStatus());
		}
		List<DateFilterRangeDTO> datePropertyFilters = new ArrayList<DateFilterRangeDTO>();
		datePropertyFilters.add(new DateFilterRangeDTO("endTime", filterInfo.getBeginTime(), filterInfo.getEndTime()));
		datePropertyFilters.add(new DateFilterRangeDTO("dateCreated", filterInfo.getPublishBeginTime(), filterInfo.getPublishEndTime()));
		
		pageResult = this.taskDmn.findByPropertiesAndValues(filterInfo.getPageNo(), filterInfo.getPageSize(),
				propertiesValuesMap, datePropertyFilters, "dateCreated", false);
		logger.info(">>>分页查询任务用时：[{}ms]", new Date().getTime() - timeStart.getTime());
		if (null == pageResult.getData() || 0 == pageResult.getData().size()) {
			return pageResult;
		}
		timeStart = new Date();
		List<DcmTask> tasks = (List<DcmTask>) pageResult.getData();
		logger.info(">>>获取机构映射信息");
		List<Org2UsersDTO> org2users = this.userOrgService.getOrgUserTree(false, filterInfo.getRealm());
		Map<String, String> orgCodeNameMap = new HashMap<String, String>(0);
		if (org2users != null && !org2users.isEmpty()) {
			for(Org2UsersDTO o2u : org2users) {
				if(!orgCodeNameMap.containsKey(o2u.getOrgCode())) {
					orgCodeNameMap.put(o2u.getOrgCode(), o2u.getAlias());
				}
			}
		}
		final ComputeTaskSummaryTask taskSummaryTask = new ComputeTaskSummaryTask(filterInfo.getRealm(),
				this.connectionService, this.caseUtil, this.ecmConf, 0, tasks.size(), projectDTOMap, tasks,
				orgCodeNameMap, wzTaskId2CreatorMap, this.udrDmn);
		List<TaskAndProjectRespDTO> taskprjDTOs = pool.invoke(taskSummaryTask);
		taskSummaryTask.clear();
		// Pagination p = new Pagination(pageNo, pageSize, totalCount);
		pageResult.setData(taskprjDTOs);
		logger.info(">>>任务汇总用时：[{}ms]", new Date().getTime() - timeStart.getTime());
		return pageResult;
	}
	/**
	 * 根据过滤器，获取项目基本信息
	 * @param filterInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<DcmGroup> getProjectsByFilter(TaskSummaryFilterDTO filterInfo) {
		
		List<DcmGroup> allProjects = new ArrayList<DcmGroup>();
		// 精确检索
		if(filterInfo.getProjectNo() != null && filterInfo.getProjectNo().length > 0) {
			allProjects = this.groupDmn.findByProperty("guid", filterInfo.getProjectNo());
			return allProjects;
		} 
		
		String[] tempDomainCodes = null;
		// 先判断项目负责人和项目助理的过滤
		Pagination pagePrjManager = null;
		List<DcmUserdomainrole> projectsFilter = null;
		// 判断当前人是否有院级决策管理角色。如果有，则项目范围为全院
		List<DcmUserdomainrole> insDecisionManagers = this.udrDmn.getByUsercodeRolecode(filterInfo.getUserCode(), RoleConstants.RoleCode.R_Ins_DecisionManager);
		if(insDecisionManagers != null && !insDecisionManagers.isEmpty()) {
			logger.info(">>>当前用户[{}]拥有院级决策管理角色", filterInfo.getUserCode());
			
			if(filterInfo.getPrjManager() != null && filterInfo.getPrjManager().length > 0) {
				Map<String, Object[]> equalPrjManagers = new HashMap<String, Object[]>();
				equalPrjManagers.put("domainType", new String[]{DomainType.PROJECT});
				equalPrjManagers.put("roleCode", new String[]{RoleConstants.RoleCode.R_Project_Manager});
				equalPrjManagers.put("userCode", filterInfo.getPrjManager());
				pagePrjManager = this.udrDmn.findByProperty(1, 1000, equalPrjManagers, "", false);
				if(null == pagePrjManager || null == pagePrjManager.getData() || pagePrjManager.getData().isEmpty()) {
					logger.info(">>>没有找到指定项目负责人的项目信息");
					return null;
				}
				projectsFilter = (List<DcmUserdomainrole>) pagePrjManager.getData();	
			}
			Pagination pagePrjAssistant = null;
			if(filterInfo.getPrjAssistant() != null && filterInfo.getPrjAssistant().length > 0) {
				// 并且过滤项目助理
				Map<String, Object[]> equalPrjAssistants = new HashMap<String, Object[]>();
				equalPrjAssistants.put("domainType", new String[]{DomainType.PROJECT});
				equalPrjAssistants.put("roleCode", new String[]{RoleConstants.RoleCode.R_Project_Assistant});
				equalPrjAssistants.put("userCode", filterInfo.getPrjAssistant());
				if(projectsFilter != null &&  !projectsFilter.isEmpty()) {
					tempDomainCodes = new String[projectsFilter.size()];
					for(int i=0; i< projectsFilter.size(); i++) {
						tempDomainCodes[i] = projectsFilter.get(i).getDomainCode();
					}
					// projectCode2UdrMap = projectsFilter.stream().collect(DistCollectors.toMap(DcmUserdomainrole::getDomainCode, p->(p)));
					equalPrjAssistants.put("domainCode", tempDomainCodes);
				}
				pagePrjAssistant = this.udrDmn.findByProperty(1, 1000, equalPrjAssistants, "", false);
				if(null == pagePrjAssistant || null == pagePrjAssistant.getData() || pagePrjAssistant.getData().isEmpty()) {
					logger.info(">>>没有找到指定项目助理的项目信息");
					return null;
				}
				// 注意此处不是addAll，而是取交集
				projectsFilter = (List<DcmUserdomainrole>) pagePrjAssistant.getData();
			}
			
			Map<String, Object[]> equalProperties = new HashMap<String, Object[]>();
			equalProperties.put("domainType", new String[]{DomainType.PROJECT});
			equalProperties.put("realm", new String[]{filterInfo.getRealm()});
			if(projectsFilter != null && !projectsFilter.isEmpty()) {
				tempDomainCodes = new String[projectsFilter.size()];
				for(int i=0; i< projectsFilter.size(); i++) {
					tempDomainCodes[i] = projectsFilter.get(i).getDomainCode();
				}
				// projectCode2UdrMap = projectsFilter.stream().collect(DistCollectors.toMap(DcmUserdomainrole::getDomainCode, p->(p)));
				equalProperties.put("guid", tempDomainCodes);
			}
			Map<String, Object[]> likeProperties = new HashMap<String, Object[]>();
			likeProperties.put("groupCode", new String[]{"XZ_CASETYPE_JYXM_","XZ_CASETYPE_HZXM_"});
			
			Pagination pageAllGroups = this.groupDmn.findByProperties(1, 1000, equalProperties, likeProperties, "", false);
			if(pageAllGroups != null && pageAllGroups.getData() != null && !pageAllGroups.getData().isEmpty()) {
				allProjects =  (List<DcmGroup>) pageAllGroups.getData();
			}
		} else {
			// 判断当前人是否有所级决策管理角色。如果有，则项目范围为用户所在部门的项目
			List<DcmUserdomainrole> departmentDecisionManagers = this.udrDmn.getByUsercodeRolecode(filterInfo.getUserCode(), RoleConstants.RoleCode.R_Dep_DecisionManager);
			if(departmentDecisionManagers != null && !departmentDecisionManagers.isEmpty()) {
				logger.info(">>>当前用户[{}]拥有所级决策管理角色", filterInfo.getUserCode());
				String[] departmentCodes = new String[departmentDecisionManagers.size()];
				for(int i=0; i< departmentDecisionManagers.size(); i++) {
					departmentCodes[i] = departmentDecisionManagers.get(i).getDomainCode();
				}
				//Map<String, DcmUserdomainrole> departmentCode2UdrMap = departmentDecisionManagers.stream().collect(DistCollectors.toMap(DcmUserdomainrole::getDomainCode, p->(p)));
				if(filterInfo.getPrjManager() != null && filterInfo.getPrjManager().length > 0) {
					Map<String, Object[]> equalPrjManagers = new HashMap<String, Object[]>();
					equalPrjManagers.put("domainType", new String[]{DomainType.PROJECT});
					equalPrjManagers.put("roleCode", new String[]{RoleConstants.RoleCode.R_Project_Manager});
					equalPrjManagers.put("userCode", filterInfo.getPrjManager());
					pagePrjManager = this.udrDmn.findByProperty(1, 1000, equalPrjManagers, "", false);
					if(null == pagePrjManager || null == pagePrjManager.getData() || pagePrjManager.getData().isEmpty()) {
						logger.info(">>>没有找到指定项目负责人的项目信息");
						return null;
					}
					projectsFilter = (List<DcmUserdomainrole>) pagePrjManager.getData();	
				}
				Pagination pagePrjAssistant = null;
				if(filterInfo.getPrjAssistant() != null && filterInfo.getPrjAssistant().length > 0) {
					// 并且过滤项目助理
					Map<String, Object[]> equalPrjAssistants = new HashMap<String, Object[]>();
					equalPrjAssistants.put("domainType", new String[]{DomainType.PROJECT});
					equalPrjAssistants.put("roleCode", new String[]{RoleConstants.RoleCode.R_Project_Assistant});
					equalPrjAssistants.put("userCode", filterInfo.getPrjAssistant());
					if(projectsFilter != null &&  !projectsFilter.isEmpty()) {
						tempDomainCodes = new String[projectsFilter.size()];
						for(int i=0; i< projectsFilter.size(); i++) {
							tempDomainCodes[i] = projectsFilter.get(i).getDomainCode();
						}
						// projectCode2UdrMap = projectsFilter.stream().collect(DistCollectors.toMap(DcmUserdomainrole::getDomainCode, p->(p)));
						equalPrjAssistants.put("domainCode", tempDomainCodes);
					}
					pagePrjAssistant = this.udrDmn.findByProperty(1, 1000, equalPrjAssistants, "", false);
					if(null == pagePrjAssistant || null == pagePrjAssistant.getData() || pagePrjAssistant.getData().isEmpty()) {
						logger.info(">>>没有找到指定项目助理的项目信息");
						return null;
					}
					// 注意此处不是addAll，而是取交集
					projectsFilter = (List<DcmUserdomainrole>) pagePrjAssistant.getData();
				}
				Map<String, Object[]> properties = new HashMap<String, Object[]>();
				if(projectsFilter != null && !projectsFilter.isEmpty()) {
					tempDomainCodes = new String[projectsFilter.size()];
					for(int i=0; i< projectsFilter.size(); i++) {
						tempDomainCodes[i] = projectsFilter.get(i).getDomainCode();
					}
					// projectCode2UdrMap = projectsFilter.stream().collect(DistCollectors.toMap(DcmUserdomainrole::getDomainCode, p->(p)));
					properties.put("guid", tempDomainCodes);
				}
				
				properties.put("realm", new String[]{filterInfo.getRealm()});
				properties.put("orgCode", departmentCodes);
				Pagination pageDepartmentGroups = this.groupDmn.findByPropertiesAndValues(1, 1000, properties, null, false);
				if(pageDepartmentGroups != null && pageDepartmentGroups.getData() != null && !pageDepartmentGroups.getData().isEmpty()) {
					allProjects = (List<DcmGroup>) pageDepartmentGroups.getData();
				}
			}
		}
		return allProjects;
	}
	/**
	 * 根据过滤器和权限，获取项目基本信息
	 * 拥有权限PRIV_TASK_SUMMARY，并且scope=1，则是全院下的项目列表；如果scope=0，则是用户所在部门的项目列表
	 * @param filterInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<DcmGroup> getProjectsByFilterAndPrivs(TaskSummaryFilterDTO filterInfo) {

		List<DcmGroup> allProjects = new ArrayList<DcmGroup>();
		// 精确检索
		if (filterInfo.getProjectNo() != null && filterInfo.getProjectNo().length > 0) {
			allProjects = this.groupDmn.findByProperty("guid", filterInfo.getProjectNo());
			return allProjects;
		}
		// 判断当前人是否有院级决策管理角色。如果有，则项目范围为全院
		String privCode = "";
		Integer scope = 0;

		List<PrivTemplateDTO> insPrivs = (List<PrivTemplateDTO>) this.privService.getPrivCodesOfRestypeDomain(
				filterInfo.getRealm(), new String[] { ResourceConstants.ResourceType.RES_SPACE_INSTITUTE },
				new String[] { DomainType.INSTITUTE }, filterInfo.getUserCode());
		if (insPrivs != null && !insPrivs.isEmpty()) {
			for (PrivTemplateDTO privTemp : insPrivs) {
				if (privTemp.getPrivCode().equals(PrivilegeFactory.PRIV_TASK_SUMMARY.getCode())) {
					privCode = PrivilegeFactory.PRIV_TASK_SUMMARY.getCode();
					scope = privTemp.getScope();
					break;
				}
			}
		}
		if (privCode.equals(PrivilegeFactory.PRIV_TASK_SUMMARY.getCode()) && 1 == scope) {
			logger.info(">>>当前用户[{}]拥有全局任务汇总权限", filterInfo.getUserCode());
			allProjects = this.getGlobalProject(filterInfo);
		} else {
			List<PrivTemplateDTO> depPrivs = (List<PrivTemplateDTO>) this.privService.getPrivCodesOfRestypeDomain(
					filterInfo.getRealm(), new String[] { ResourceConstants.ResourceType.RES_SPACE_DEPARTMENT },
					new String[] { DomainType.DEPARTMENT }, filterInfo.getUserCode());
			if (depPrivs != null && !depPrivs.isEmpty()) {
				for (PrivTemplateDTO privTemp : depPrivs) {
					if (privTemp.getPrivCode().equals(PrivilegeFactory.PRIV_TASK_SUMMARY.getCode())) {
						privCode = PrivilegeFactory.PRIV_TASK_SUMMARY.getCode();
						scope = privTemp.getScope();
						break;
					}
				}
			}
			if (privCode.equals(PrivilegeFactory.PRIV_TASK_SUMMARY.getCode()) && 0 == scope) {
				logger.info(">>>当前用户[{}]拥有所在部门任务汇总", filterInfo.getUserCode());
				allProjects = getCurrentOrgProject(filterInfo);
			}
		}
		return allProjects;
	}

	/**
	 * 在当前用户所在部门范围内过滤项目
	 * 
	 * @param filterInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<DcmGroup> getCurrentOrgProject(TaskSummaryFilterDTO filterInfo) {
		String[] tempDomainCodes;
		Pagination pagePrjManager;
		List<DcmUserdomainrole> projectsFilter = null;

		Map<String, Object[]> propertiesValuesMap = new HashMap<String, Object[]>();
		propertiesValuesMap.put("domainType", new String[] { DomainType.DEPARTMENT });
		propertiesValuesMap.put("userCode", new Object[] { filterInfo.getUserCode() });
		List<DcmUserdomainrole> departmentUdrs = this.udrDmn.findByProperties(propertiesValuesMap);

		if (departmentUdrs != null && !departmentUdrs.isEmpty()) {
			String[] departmentCodes = new String[departmentUdrs.size()];
			for (int i = 0; i < departmentUdrs.size(); i++) {
				departmentCodes[i] = departmentUdrs.get(i).getDomainCode();
			}

			if (filterInfo.getPrjManager() != null && filterInfo.getPrjManager().length > 0) {
				Map<String, Object[]> equalPrjManagers = new HashMap<String, Object[]>();
				equalPrjManagers.put("domainType", new String[] { DomainType.PROJECT });
				equalPrjManagers.put("roleCode", new String[] { RoleConstants.RoleCode.R_P_LEADER });
				equalPrjManagers.put("userCode", filterInfo.getPrjManager());
				pagePrjManager = this.udrDmn.findByProperty(1, 1000, equalPrjManagers, "", false);
				if (null == pagePrjManager || null == pagePrjManager.getData() || pagePrjManager.getData().isEmpty()) {
					logger.info(">>>没有找到指定项目负责人的项目信息");
					return null;
				}
				projectsFilter = (List<DcmUserdomainrole>) pagePrjManager.getData();
			}
			Pagination pagePrjAssistant = null;
			if (filterInfo.getPrjAssistant() != null && filterInfo.getPrjAssistant().length > 0) {
				// 并且过滤项目助理
				Map<String, Object[]> equalPrjAssistants = new HashMap<String, Object[]>();
				equalPrjAssistants.put("domainType", new String[] { DomainType.PROJECT });
				equalPrjAssistants.put("roleCode", new String[] { RoleConstants.RoleCode.R_P_ASSISTANT });
				equalPrjAssistants.put("userCode", filterInfo.getPrjAssistant());
				if (projectsFilter != null && !projectsFilter.isEmpty()) {
					tempDomainCodes = new String[projectsFilter.size()];
					for (int i = 0; i < projectsFilter.size(); i++) {
						tempDomainCodes[i] = projectsFilter.get(i).getDomainCode();
					}
					equalPrjAssistants.put("domainCode", tempDomainCodes);
				}
				pagePrjAssistant = this.udrDmn.findByProperty(1, 1000, equalPrjAssistants, "", false);
				if (null == pagePrjAssistant || null == pagePrjAssistant.getData()
						|| pagePrjAssistant.getData().isEmpty()) {
					logger.info(">>>没有找到指定项目助理的项目信息");
					return null;
				}
				// 注意此处不是addAll，而是取交集
				projectsFilter = (List<DcmUserdomainrole>) pagePrjAssistant.getData();
			}
			Map<String, Object[]> properties = new HashMap<String, Object[]>();
			if (projectsFilter != null && !projectsFilter.isEmpty()) {
				tempDomainCodes = new String[projectsFilter.size()];
				for (int i = 0; i < projectsFilter.size(); i++) {
					tempDomainCodes[i] = projectsFilter.get(i).getDomainCode();
				}
				properties.put("guid", tempDomainCodes);
			}

			properties.put("realm", new String[] { filterInfo.getRealm() });
			properties.put("orgCode", departmentCodes);
			Pagination pageDepartmentGroups = this.groupDmn.findByPropertiesAndValues(1, 1000, properties, null, false);
			if (pageDepartmentGroups != null && pageDepartmentGroups.getData() != null
					&& !pageDepartmentGroups.getData().isEmpty()) {
				return (List<DcmGroup>) pageDepartmentGroups.getData();
			}
		}
		return null;
	}

	/**
	 * 全院范围内过滤项目
	 * 
	 * @param filterInfo
	 * @param allProjects
	 * @param projectsFilter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<DcmGroup> getGlobalProject(TaskSummaryFilterDTO filterInfo) {
		String[] tempDomainCodes = null;
		Pagination pagePrjManager = null;
		List<DcmUserdomainrole> projectsFilter = null;
		if (filterInfo.getPrjManager() != null && filterInfo.getPrjManager().length > 0) {
			Map<String, Object[]> equalPrjManagers = new HashMap<String, Object[]>();
			equalPrjManagers.put("domainType", new String[] { DomainType.PROJECT });
			equalPrjManagers.put("roleCode", new String[] { RoleConstants.RoleCode.R_P_LEADER });
			equalPrjManagers.put("userCode", filterInfo.getPrjManager());
			pagePrjManager = this.udrDmn.findByProperty(1, 1000, equalPrjManagers, "", false);
			if (null == pagePrjManager || null == pagePrjManager.getData() || pagePrjManager.getData().isEmpty()) {
				logger.info(">>>没有找到指定项目负责人的项目信息");
				return null;
			}
			projectsFilter = (List<DcmUserdomainrole>) pagePrjManager.getData();
		}
		Pagination pagePrjAssistant = null;
		if (filterInfo.getPrjAssistant() != null && filterInfo.getPrjAssistant().length > 0) {
			// 并且过滤项目助理
			Map<String, Object[]> equalPrjAssistants = new HashMap<String, Object[]>();
			equalPrjAssistants.put("domainType", new String[] { DomainType.PROJECT });
			equalPrjAssistants.put("roleCode", new String[] { RoleConstants.RoleCode.R_P_ASSISTANT });
			equalPrjAssistants.put("userCode", filterInfo.getPrjAssistant());
			if (projectsFilter != null && !projectsFilter.isEmpty()) {
				tempDomainCodes = new String[projectsFilter.size()];
				for (int i = 0; i < projectsFilter.size(); i++) {
					tempDomainCodes[i] = projectsFilter.get(i).getDomainCode();
				}
				equalPrjAssistants.put("domainCode", tempDomainCodes);
			}
			pagePrjAssistant = this.udrDmn.findByProperty(1, 1000, equalPrjAssistants, "", false);
			if (null == pagePrjAssistant || null == pagePrjAssistant.getData()
					|| pagePrjAssistant.getData().isEmpty()) {
				logger.info(">>>没有找到指定项目助理的项目信息");
				return null;
			}
			// 注意此处不是addAll，而是取交集
			projectsFilter = (List<DcmUserdomainrole>) pagePrjAssistant.getData();
		}

		Map<String, Object[]> equalProperties = new HashMap<String, Object[]>();
		equalProperties.put("domainType", new String[] { DomainType.PROJECT });
		equalProperties.put("realm", new String[] { filterInfo.getRealm() });
		if (projectsFilter != null && !projectsFilter.isEmpty()) {
			tempDomainCodes = new String[projectsFilter.size()];
			for (int i = 0; i < projectsFilter.size(); i++) {
				tempDomainCodes[i] = projectsFilter.get(i).getDomainCode();
			}
			equalProperties.put("guid", tempDomainCodes);
		}
		Map<String, Object[]> likeProperties = new HashMap<String, Object[]>();
		likeProperties.put("groupCode", new String[] { "XZ_CASETYPE_JYXM_", "XZ_CASETYPE_HZXM_" });

		Pagination pageAllGroups = this.groupDmn.findByProperties(1, 1000, equalProperties, likeProperties, "", false);
		if (pageAllGroups != null && pageAllGroups.getData() != null && !pageAllGroups.getData().isEmpty()) {
			return (List<DcmGroup>) pageAllGroups.getData();
		}
		return null;
	}
	@Override
	public Pagination fuzzySearchProjectName(String realm, Integer pageNo, Integer pageSize, String keyword) {
		
		Map<String, Object[]> equalPropertyValueMap = new HashMap<String, Object[]>();
		equalPropertyValueMap.put("realm", new String[]{realm});
		Map<String, Object[]> likePropertyValueMap = new HashMap<String, Object[]>();
		likePropertyValueMap.put("groupName", new String[]{keyword});
		
		return this.groupDmn.findByProperties(pageNo, pageSize, equalPropertyValueMap, likePropertyValueMap, "groupName", true);
	}
	@Override
	public void deleteWzById(String realm, String resId) {
		
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		this.caseUtil.deleteWzById(p8conn.getObjectStore(), resId);
	}
	@Override
	public List<ProjectSummaryDTO> getProjectSummary(String realm, String userCode) {

		TaskSummaryFilterDTO filter = new TaskSummaryFilterDTO();
		filter.setUserCode(userCode);
		filter.setRealm(realm);
		List<DcmGroup> projects = this.getProjectsByFilter(filter);
		if (null == projects || projects.isEmpty()) {
			return null;
		}
		final ForkJoinPool pool = new ForkJoinPool(5);
		final ComputeProjectSummaryTask taskSummaryTask = new ComputeProjectSummaryTask(projects, 0, projects.size(),
				this.udrDmn);
		List<ProjectSummaryDTO> prjSummarys = pool.invoke(taskSummaryTask);
		Collections.sort(prjSummarys, new Comparator<ProjectSummaryDTO>() {

			@Override
			public int compare(ProjectSummaryDTO first, ProjectSummaryDTO second) {
				return second.getMemberCount().compareTo(first.getMemberCount());
			}
		});
		int toIndex = 20;
		if (prjSummarys.size() < 20) {
			toIndex = prjSummarys.size();
		}
		return prjSummarys.subList(0, toIndex);
	}
	@SuppressWarnings("unchecked")
	@Override
	public Pagination getProjectInfoSummary(String realm, String userCode, int pageNo, int pageSize, String[] projectTypes) {
		
		Map<String, Object[]> equalProperties = new HashMap<String, Object[]>();
		equalProperties.put("realm", new String[]{realm});
		Map<String, Object[]> likeProperties = new HashMap<String, Object[]>();
		likeProperties.put("groupCode", projectTypes);
		Pagination projectPage = this.groupDmn.findByProperties(pageNo, pageSize, equalProperties, likeProperties, "createTime", false);
		List<DcmGroup> groups = (List<DcmGroup>) projectPage.getData();
		List<ProjectExcelDTO> projectExcelDTOs = new ArrayList<ProjectExcelDTO>();
		ProjectExcelDTO projectExcelDTO = null;
		projectPage.setData(projectExcelDTOs);
		if(groups != null && !groups.isEmpty()) {
			P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
			CaseDTO caseDTO = null;
			List<String> managers = null;
			List<String> assistants = null;
			for(DcmGroup g : groups) {
				caseDTO = this.caseUtil.getCaseById(p8conn.getObjectStore(), g.getGuid());
				projectExcelDTO = new ProjectExcelDTO();
				projectExcelDTO.setProjectName(caseDTO.getProjectName());
				projectExcelDTO.setProjectCode(caseDTO.getProjectCode());
				projectExcelDTO.setBusinessType(caseDTO.getBusinessType());
				projectExcelDTO.setCity(caseDTO.getCity());
				projectExcelDTO.setCounty(caseDTO.getCounty());
				projectExcelDTO.setProvince(caseDTO.getProvince());
				projectExcelDTO.setZbje(caseDTO.getZbje());
				projectExcelDTO.setZbjg(caseDTO.getZbjg());
				projectExcelDTO.setZbdw(caseDTO.getZbdw());
				projectExcelDTO.setZbsj(caseDTO.getZbsj());
				managers = new ArrayList<String>();
				assistants = new ArrayList<String>();
				getProjectManagersAndAssistants(g.getGuid(), managers, assistants);
				projectExcelDTO.setProjectManager(managers.toArray(new String[managers.size()]));
				projectExcelDTO.setProjectAssistant(assistants.toArray(new String[assistants.size()]));
				
				projectExcelDTOs.add(projectExcelDTO);	
			}
		}
		return projectPage;
	}
	/**
	 * 获取项目的负责人和助理
	 * @param caseId
	 * @param managers
	 * @param assistants
	 */
	private void getProjectManagersAndAssistants(String caseId, List<String> managers, List<String> assistants) {
		
		Assert.notNull(managers);
		Assert.notNull(assistants);
		
		Map<String, Object[]> properties = new HashMap<String, Object[]>();
		properties.put("domainType", new String[]{DomainType.PROJECT});
		properties.put("domainCode", new String[]{caseId});
		properties.put("roleCode", new String[]{RoleConstants.RoleCode.R_Project_Manager, RoleConstants.RoleCode.R_Project_Assistant});
		List<DcmUserdomainrole> udrs = this.udrDmn.findByProperties(properties);
		if(udrs != null && !udrs.isEmpty()) {
			DcmUser user = null;
			for(DcmUserdomainrole udr : udrs) {
				user = this.userOrgService.getUserEntityByCode(udr.getUserCode());
				// 项目负责人
				if(Objects.equal(RoleConstants.RoleCode.R_Project_Manager, udr.getRoleCode())) {
					managers.add(null == user? "" : user.getUserName());
				} else if(Objects.equal(RoleConstants.RoleCode.R_Project_Assistant, udr.getRoleCode())) {
					// 项目助理
					assistants.add(null == user? "" : user.getUserName());
				}
			}
		}
	}
	@Override
	public void syncTaskPublishTime(String realm) {

		List<DcmTask> tasks = this.taskDmn.findByProperty("realm", realm);
		if (null == tasks || tasks.isEmpty()) {
			return;
		}
		final P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		for(DcmTask task :  tasks) {
			try {
				Document doc = this.caseUtil.getWz(p8conn.getObjectStore(), task.getTaskId());
				if(doc != null) {
					task.setDateCreated(doc.get_DateCreated());
					this.taskDmn.modify(task);
				}
			} catch (Exception ex) {
				logger.error("任务资源[{}]不存在，详情：{}，", task.getTaskId(), ex.getMessage());
			}	
		}
	}
	@Override
	public void sendWechatTemplateMsg(String projectCode, String userCode) throws BusinessException {
		try {
			UserResponseDTO outerUser = this.sgaUserService.getUserByCode(userCode);
			if (null == outerUser) {
				throw new BusinessException("外部用户：{0}不存在", userCode);
			}
			SgaPrjUser prjuser = this.sgaProjectService.getPrjRefUser(projectCode, outerUser.getId());
			if (null == prjuser) {
				throw new BusinessException("项目与用户未关联");
			}
			if (StringUtils.isEmpty(prjuser.getFormId())) {
				throw new BusinessException("表单id不存在，不再发送小程序模板消息", userCode);
			}
			AccessToken accessToken = WechatManager.getAccessTokenByClientCredential(
					this.wechatConf.getInvokeAccessTokenByClientCredentialURI(),
					DesEncryptRSA.getInstance().decrypt(this.wechatConf.getWxAppAppIdEncrypt()),
					DesEncryptRSA.getInstance().decrypt(this.wechatConf.getWxAppAppSecretEncrypt()));

			WechatTemplate templateData = new WechatTemplate();
			templateData.setTouser(outerUser.getOpenId());
			templateData.setTemplate_id(this.wechatConf.getWxappTemplateMessageId());
			templateData.setForm_id(prjuser.getFormId());
			templateData.setPage(this.wechatConf.getWxappTemplateMessagePage());
			Map<String, TemplateMsgData> dataMap = new HashMap<String, TemplateMsgData>();

			TemplateMsgData people = new TemplateMsgData();
			people.setColor("#000000");
			people.setValue(outerUser.getUserName());
			dataMap.put(this.wechatConf.getWxappTemplatemsgPropertyApplicant(), people);
			TemplateMsgData address = new TemplateMsgData();
			address.setColor("#000000");
			address.setValue(this.wechatConf.getWxappTemplatemsgPropertyAddressValue());
			dataMap.put(this.wechatConf.getWxappTemplatemsgPropertyAddress(), address);

			TemplateMsgData meetingTime = new TemplateMsgData();
			meetingTime.setColor("#000000");
			meetingTime.setValue(this.wechatConf.getWxappTemplatemsgPropertyMeetingNameValue());
			dataMap.put(this.wechatConf.getWxappTemplatemsgPropertyMeetingTime(), meetingTime);

			TemplateMsgData registerTime = new TemplateMsgData();
			registerTime.setColor("#000000");
			registerTime.setValue(DateUtil.toDateTimeStr(prjuser.getCreateTime()));
			dataMap.put(this.wechatConf.getWxappTemplatemsgPropertyRegistertimeTime(), registerTime);

			TemplateMsgData registerResult = new TemplateMsgData();
			registerResult.setColor("#000000");
			registerResult.setValue(this.wechatConf.getWxappTemplatemsgPropertyResultValue());
			dataMap.put(this.wechatConf.getWxappTemplatemsgPropertyResult(), registerResult);

			TemplateMsgData meetingName = new TemplateMsgData();
			meetingName.setColor("#000000");
			meetingName.setValue(this.wechatConf.getWxappTemplatemsgPropertyMeetingNameValue());
			dataMap.put(this.wechatConf.getWxappTemplatemsgPropertyMeetingName(), meetingName);

			TemplateMsgData remark = new TemplateMsgData();
			remark.setColor("#000000");
			remark.setValue(this.wechatConf.getWxappTemplatemsgPropertyRemarkValue());
			dataMap.put(this.wechatConf.getWxappTemplatemsgPropertyRemark(), remark);
			// 装载数据源
			templateData.setData(dataMap);

			WechatManager.sendTemplateMessage(this.wechatConf.getWxappSendTemplateMsgURI(),
					accessToken.getAccess_token(), templateData);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}
	@Override
	public Object getProjectUsers(String caseId, String contextPath, String baseURL) {
		
		List<ProjectUserDTO> userResponseDTOs = new ArrayList<ProjectUserDTO>();
		ProjectUserDTO userResponse = null;
		DcmUser innerUser = null;
		DcmOrganization org = null;
		
		List<DcmUserdomainrole> udrs = this.udrDmn.getByDomainCode(caseId);
		if(udrs != null && !udrs.isEmpty()) {
			for(DcmUserdomainrole udr : udrs) {
				if(0 == udr.getUserType()) {
					// 内部用户
					innerUser = this.userOrgService.getUserEntityByCode(udr.getUserCode());
					if(null == innerUser) continue;
					
					userResponse = this.dozerMapper.map(innerUser, ProjectUserDTO.class);
					userResponse.setAvatar(this.userOrgService.getUserAvatarLink(this.imgSerConf.getServerURI(), contextPath, baseURL, innerUser));
					org = this.userOrgService.getInstituteByUniqueName(innerUser.getRealm());
					if(org != null) {
						userResponse.setUnit(org.getAlias());
					}
				} else {
					UserResponseDTO dto = this.sgaUserService.getUserByCode(udr.getUserCode());
					if(null == dto) continue;
					
					userResponse =  this.dozerMapper.map(dto, ProjectUserDTO.class);
					userResponse.setUserCode(dto.getUserCode());
				}
				userResponse.setIsTop(udr.getIsTop());
				userResponse.setUserType(udr.getUserType());
				userResponseDTOs.add(userResponse);
			}
		}
		return userResponseDTOs;
	}
	@Override
	public Object getProjectSummaryById(String[] caseIds) {
		
		List<ProjectSummaryDTO> prjSummarys = new ArrayList<>(caseIds.length);
		List<DcmUserdomainrole> udrs = null;
		ProjectSummaryDTO tempPrjSummaryDTO = null;
		for(String caseId : caseIds) {
			udrs = this.udrDmn.getByDomainCode(caseId);
			tempPrjSummaryDTO = new ProjectSummaryDTO();
			tempPrjSummaryDTO.setCaseId(caseId);
			tempPrjSummaryDTO.setMemberCount((null == udrs | udrs.isEmpty()) ? 0 : udrs.size());
			prjSummarys.add(tempPrjSummaryDTO);
		}
		return prjSummarys;
	}
	@Override
	public boolean checkUserInProject(String caseId, String userCode) {
		
		List<DcmUserdomainrole> udrs = this.udrDmn.getByDomainCodeUserCode(caseId, userCode);
		return !(null == udrs || 0 == udrs.size());
	}
	@Override
	public List<String> getRefFileIdsOfWz(String realm, String wzId) {
		
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		StringList vids = this.caseUtil.getWzRefFiles(p8conn.getObjectStore(), wzId);
		if(null == vids || vids.isEmpty()) {
			return null;
		}
		List<String> list = new LinkedList<String>();
		for(int i=0; i< vids.size(); i++) {
			list.add(vids.get(i).toString());
		}
		return list;
	}
	@Override
	public List<HotProjectSummaryDTO> getHotProjectSummaryByRealm(String realm) {
		P8Connection p8conn = this.connectionService.getP8Connection(this.ecmConf.getTargetObjectStore(realm));
		List<SimplePropertyFilter> filters = new ArrayList<SimplePropertyFilter>();
		filters.add(
				new SimplePropertyFilter(this.extPropCaseConf.getSfrm(), DataType.SingleOfInteger, new Object[] { 1 }));

		List<CaseDTO> cases = this.caseUtil.getCasesByFilter(p8conn.getObjectStore(), filters, "DateLastModified",
				false);
		if (null == cases || cases.isEmpty())
			return null;

		List<HotProjectSummaryDTO> list = new ArrayList<HotProjectSummaryDTO>();
		List<DcmUserdomainrole> udrs = null;
		List<DcmTask> tasks = null;
		DcmGroup projectGroup = null;
		HotProjectSummaryDTO hotProject = null;
		for (CaseDTO caseDto : cases) {
			hotProject = new HotProjectSummaryDTO();
			hotProject.setStage(caseDto.getProjectType());
			udrs = this.udrDmn.getByDomainCode(caseDto.getGuid());
			hotProject.setMemberCount((null == udrs || udrs.isEmpty()) ? 0 : udrs.size());
			tasks = this.taskDmn.getSubTasks(caseDto.getGuid());
			hotProject.setTaskCount((null == tasks || tasks.isEmpty()) ? 0 : tasks.size());
			hotProject.setDocCount(
					this.searchEngine.getTotalCount(p8conn.getObjectStore(), this.ecmConf.getDefaultDocumentClass(),
							this.extPropMaterialConf.getSpatialDomain(), new String[] { caseDto.getGuid() }));
			hotProject.setCaseId(caseDto.getGuid());
			hotProject.setName(caseDto.getProjectName());
			projectGroup =this.groupDmn.getGroupByGuid(caseDto.getGuid());
			if(projectGroup != null && !StringUtils.isEmpty(projectGroup.getImg())) {
				hotProject.setImgURL(this.imgSerConf.getServerURI() + projectGroup.getImg());
			} else {
				logger.warn(">>>没有找到相关项目信息：" + caseDto.getGuid());
			}
			list.add(hotProject);
		}
		return list;
	}
	@Override
	public GroupSummaryDTO getGroupsSummaryByRealm(String realm) {
		
		GroupSummaryDTO dto = new GroupSummaryDTO();
		Pagination projectGroupPage = this.groupDmn.getGroups(1, 1, realm, this.ecmConf.getProjectTypeCommon().split(";"));
		if(projectGroupPage != null) {
			dto.setProjectCount(projectGroupPage.getTotalCount());
		}
		Pagination teamGroupPage = this.groupDmn.getGroups(1, 1, realm, this.ecmConf.getProjectTypeTeam().split(";"));
		if(teamGroupPage != null) {
			dto.setTeamCount(teamGroupPage.getTotalCount());
		}
		List<DcmOrganization> orgs = this.userOrgService.getOrgsByRealm(realm);
		if(orgs != null) {
			dto.setOrgCount(orgs.size());
		}
		Pagination page = null;
		page = this.userOrgService.getOrgsByRealm(realm, "中方");
		if(page != null) {
			dto.setDomesticOrgCount(page.getTotalCount());
		}
		page = this.userOrgService.getOrgsByRealm(realm, "外方");
		if(page != null) {
			dto.setAbroadOrgCount(page.getTotalCount());
		}
		List<DcmUser> users = this.userOrgService.listAllUsers(realm);
		if(users != null) {
			dto.setMemberCount(users.size());
		}
		// 计算参与人数
	/*	StringBuilder buf = new StringBuilder();
		buf.append(" select count(distinct udr.usercode) userCount");
		buf.append(" from (select g.guid from dcm_group g  where (instr(g.groupcode, 'XZ_CASETYPE_JYXM') > 0 or instr(g.groupcode, 'XZ_CASETYPE_HZXM') > 0) and g.realm = '"+realm+"' ) g1");
		buf.append(" inner join dcm_userdomainrole udr on g1.guid = udr.domaincode");
		List<?> queryResult = this.groupDmn.queryBySql(buf.toString());
		if(queryResult != null && !queryResult.isEmpty()) {
			dto.setMemberCount(Integer.valueOf(queryResult.get(0).toString()));
		}*/
		return dto;
	}
	@Override
	public void deleteImgInMongoByProjectCode(String code) {
		this.mongoFileStorageDao.deleteByCode(code);
	}
	@Override
	public String storeImgToMongo(ImgInfo imgInfo) {
	    String newFileName = System.currentTimeMillis() + (imgInfo.getSuffix().startsWith(".")? imgInfo.getSuffix() : "."+imgInfo.getSuffix());
		
		DBObject metaData = new BasicDBObject();
    	metaData.put("suffix", imgInfo.getSuffix());
    	metaData.put("code", imgInfo.getId().replace("{", "").replace("}", ""));
    	metaData.put("type", "projectimg");
    	
    	return mongoFileStorageDao.store(FileUtil.base64ToInputStream(imgInfo.getContent()) , newFileName, imgInfo.getType(), metaData);
	}
	@Override
	public String updateProjectImgURL(String projectGuid, String relativeUrl) {
	
		if(!relativeUrl.startsWith("/")) {
			relativeUrl = "/" + relativeUrl;
		}
		DcmGroup projectGroup = this.groupDmn.getGroupByGuid(projectGuid);
		if(projectGroup != null) {
			projectGroup.setImg(relativeUrl);
		}
		this.groupDmn.modify(projectGroup);
		return this.imgSerConf.getServerURI() + relativeUrl;
	}
	@Override
	public String getProjectImgURL(String projectGuid) {
		DcmGroup projectGroup = this.groupDmn.getGroupByGuid(projectGuid);
		if(projectGroup != null && !StringUtil.isEmpty(projectGroup.getImg())) {
			return  this.imgSerConf.getServerURI() + projectGroup.getImg();
		}
		return null;
	}
	@Override
	public DcmWorkGroup addWorkGroup(WorkGroupAddDTO dto) {
		DcmWorkGroup workgroupEntity = new DcmWorkGroup();
		workgroupEntity.setName(dto.getName());
		workgroupEntity.setProjectGuid(dto.getProjectGuid());
		workgroupEntity.setRealm(dto.getRealm());
		return this.dcmWorkgroupDmn.add(workgroupEntity);
	}
	@Override
	public DcmWgOrg addOrgToWorkgroup(WorkGroupOrgAddDTO dto) {
		
		DcmWgOrg wgOrg = new DcmWgOrg();
		wgOrg.setWgId(dto.getWgId());
		wgOrg.setOrgGuid(dto.getOrgGuid());
		return this.dcmWorkgroupDmn.saveOrUpdateOrgToWorkgroup(wgOrg);
	}
	@Override
	public void deleteOrgFromWorkgroup(Long workgroupId, String orgGuid) {
		
		 this.dcmWorkgroupDmn.deleteOrgFromWorkgroup(workgroupId, orgGuid);
	}
	@Override
	public void deleteWorkgroupsByProjectGuid(String projectGuid) {
		this.dcmWorkgroupDmn.deleteWorkgroupsByProjectGuid(projectGuid);
	}

	@Override
	public List<WorkGroupRespDTO> getWorkgroupsByProjectGuid(String projectGuid) {
		List<DcmWorkGroup> workgroups = this.dcmWorkgroupDmn.findByProperty("projectGuid", projectGuid);
		if(null == workgroups || workgroups.isEmpty()) {
			return null;
		}
		List<WorkGroupRespDTO> wgRespDTOs = new ArrayList<WorkGroupRespDTO>();
		WorkGroupRespDTO wgResp = null;
		List<DcmWgOrg> wgOrgs = null;
		DcmOrganization org = null;
		for(DcmWorkGroup wg : workgroups) {
			wgResp = new WorkGroupRespDTO();
			wgResp.setId(wg.getId());
			wgResp.setName(wg.getName());
			wgOrgs = this.dcmWorkgroupDmn.getRefOrgs(wg.getId());
			if(wgOrgs != null && !wgOrgs.isEmpty()) {
				for(DcmWgOrg wgOrg : wgOrgs) {
					org = this.userOrgService.getOrgByCode(wgOrg.getOrgGuid());
					if(null == org) continue;
					
					wgResp.addOrg(org.getOrgCode(), org.getAlias());
				}
			}
			wgRespDTOs.add(wgResp);
		}
		return wgRespDTOs;
	}
	@Override
	public void deleteWorkGroup(Long workgroupId) {
		DcmWorkGroup wg = this.dcmWorkgroupDmn.loadById(workgroupId);
		if(wg != null) {
			this.dcmWorkgroupDmn.deleteOrgsFromWorkgroup(workgroupId);
			this.dcmWorkgroupDmn.remove(wg);
		}
	}
	@Override
	public DcmNaotuTeam addNaotuTeamRef(NaotuTeamAddDTO dto) {
		DcmNaotuTeam naotuRefTeam = this.dcmNaotuTeamDmn.findUniqueByProperties(new String[]{"caseId","minderId","nodeId","teamId","realm"}, new Object[]{
				dto.getCaseId(), dto.getMinderId(), dto.getNodeId(), dto.getTeamId(), dto.getRealm()
		});
		if(null == naotuRefTeam) {
			naotuRefTeam = this.dozerMapper.map(dto, DcmNaotuTeam.class);
			return this.dcmNaotuTeamDmn.saveOrUpdate(naotuRefTeam);
		}
		return naotuRefTeam;
	}
	@Override
	public Pagination getTeamGroups(String realm, int pageNo, int pageSize) {
		
		Map<String, Object[]> equalProperties = new HashMap<String, Object[]>();
		equalProperties.put("realm", new String[]{realm});
		Map<String, Object[]> likeProperties = new HashMap<String, Object[]>();
		likeProperties.put("groupCode", new String[]{"XZ_CPTDGL_"});
		return this.groupDmn.findByProperties(pageNo, pageSize, equalProperties, likeProperties, "groupName", true);
	}
	@Override
	public Object getNaotuRefTeams(String realm, String caseId, Integer minderId) {
		List<DcmNaotuTeam> naotuTeams = this.dcmNaotuTeamDmn.findByProperties(new String[]{"realm","caseId","minderId"},  new Object[]{realm, caseId, minderId});
		if(null == naotuTeams || 0 == naotuTeams.size()) return null;
		
		Map<String, List<DcmGroup>> nodeTeamMap = new HashMap<String, List<DcmGroup>>();
		DcmGroup group = null;
		for(DcmNaotuTeam nt : naotuTeams) {
			if(!nodeTeamMap.containsKey(nt.getNodeId())) {
				nodeTeamMap.put(nt.getNodeId(), new ArrayList<DcmGroup>());
			}
			group = this.groupDmn.getGroupByGuid(nt.getTeamId());
			if(null == group) continue;
			
			nodeTeamMap.get(nt.getNodeId()).add(group);
		}
		return nodeTeamMap;
	}
	@Override
	public Object deleteNaotuTeamRef(String realm, String caseId, int minderId, String nodeId, String teamId) {
		this.dcmNaotuTeamDmn.removeByProperty(new String[]{"realm", "caseId", "minderId", "nodeId", "teamId"}, 
				new Object[]{realm, caseId, minderId, nodeId, teamId});
		return true;
	}
	@Override
	public Object deleteNaotuTeamRef(String realm, String caseId, int minderId, String nodeId) {
		this.dcmNaotuTeamDmn.removeByProperty(new String[]{"realm", "caseId", "minderId", "nodeId"}, 
				new Object[]{realm, caseId, minderId, nodeId});
		return true;
	}
	@Override
	public Object deleteNaotuTeamRef(String realm, String caseId, int minderId) {
		this.dcmNaotuTeamDmn.removeByProperty(new String[]{"realm", "caseId", "minderId"}, 
				new Object[]{realm, caseId, minderId});
		return true;
	}
	
}
