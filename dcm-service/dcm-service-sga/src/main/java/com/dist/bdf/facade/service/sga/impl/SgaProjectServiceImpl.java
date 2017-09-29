package com.dist.bdf.facade.service.sga.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.utils.DistThreadManager;
import com.dist.bdf.facade.service.GroupService;
import com.dist.bdf.facade.service.sga.SgaProjectService;
import com.dist.bdf.facade.service.sga.SgaUserService;
import com.dist.bdf.facade.service.sga.domain.SgaCompanyDmn;
import com.dist.bdf.facade.service.sga.domain.SgaFileRecordDmn;
import com.dist.bdf.facade.service.sga.domain.SgaProjectDmn;
import com.dist.bdf.manager.mongo.MongoFileStorageDmn;
import com.dist.bdf.common.constants.UserStatus;
import com.dist.bdf.model.dto.sga.CompanySimpleInfoResponseDTO;
import com.dist.bdf.model.dto.sga.CooProjectAddDTO;
import com.dist.bdf.model.dto.sga.CooProjectResponseDTO;
import com.dist.bdf.model.dto.sga.FileRecordDTO;
import com.dist.bdf.model.dto.sga.ImgInfo;
import com.dist.bdf.model.dto.sga.InvitationInfoDTO;
import com.dist.bdf.model.dto.sga.PrjUserRequestDTO;
import com.dist.bdf.model.dto.sga.PrjUserStatusPutDTO;
import com.dist.bdf.model.dto.sga.ProjectUpdateDTO;
import com.dist.bdf.model.dto.sga.UserResponseDTO;
import com.dist.bdf.model.entity.sga.SgaCompany;
import com.dist.bdf.model.entity.sga.SgaComUser;
import com.dist.bdf.model.entity.sga.SgaFileRecord;
import com.dist.bdf.model.entity.sga.SgaPrjDetail;
import com.dist.bdf.model.entity.sga.SgaPrjUser;
import com.dist.bdf.model.entity.sga.SgaPrjWz;
import com.dist.bdf.model.entity.sga.SgaProject;
import com.dist.bdf.model.entity.system.DcmGroup;

@Service("SgaProjectService")
@Transactional(propagation = Propagation.REQUIRED)
public class SgaProjectServiceImpl implements SgaProjectService {

	private static Logger logger  = LoggerFactory.getLogger(SgaProjectServiceImpl.class);
	
	@Autowired
	private GroupService groupService;
	@Autowired
	private SgaCompanyDmn sgaComInfoDmn;
	@Autowired
	private SgaProjectDmn sgaProjectDmn;
	@Autowired
	private SgaUserService sgaUserService;
	@Autowired
	private SgaFileRecordDmn sgaFileRecordDmn;
	@Autowired
	private Mapper dozerMapper;
	@Autowired
	private MongoFileStorageDmn mongoFileStorageDmn;
	/**
	 * 背景图类型
	 */
	private static final String IMAGE_TYPE_BACKGROUND = "poster";
	/**
	 * 项目海报的相对路径前缀
	 */
	public static final String IMAGE_PATH_PREFIX_BACKGROUND = "/sga/project/fs/poster/";
	
	@Override
	public List<SgaProject> getAllProject() {
		
		return this.sgaProjectDmn.find();
	}
	@Override
	public Object saveOrUpdate(CooProjectAddDTO dto) throws BusinessException{
		
		// boolean isAdd = false;
		final SgaCompany comInfo = sgaComInfoDmn.getComByRealm(dto.getRealm());
		if(null == comInfo) 
			throw new BusinessException("企业信息不存在，编码[{0}]", dto.getRealm());
		
		DcmGroup projectGroup = this.groupService.getGroupByGuid(dto.getCaseId());
		if(null == projectGroup) 
			throw new BusinessException("项目组信息不存在，案例id[{0}]", dto.getCaseId());
		
		SgaProject sgaProject = this.sgaProjectDmn.findUniqueByProperty("sysCode", dto.getCaseId());
		if(null == sgaProject){
			sgaProject = new SgaProject();
			sgaProject.setCreateTime(new Date());
			sgaProject.setJoinInCount(0);
			sgaProject.setRegisterCount(0);
			// isAdd = true;
		}
		sgaProject.setCaseCode(projectGroup.getGroupCode());
		sgaProject.setName(dto.getName());
		sgaProject.setSysCode(dto.getCaseId());
		sgaProject.setStatus(dto.getStatus());
		// sgaProject.setPoster(dto.getPoster());
		sgaProject.setCid(comInfo.getId());
		sgaProject.setTag(dto.getTag());
		sgaProject.setDirection(dto.getDirection());
		
		this.sgaProjectDmn.saveOrUpdate(sgaProject);
		
		SgaPrjDetail prjDetail = this.sgaProjectDmn.getDesc(sgaProject.getId());
		if(null == prjDetail) {
			prjDetail = new SgaPrjDetail();
			prjDetail.setPid(sgaProject.getId());
		}
		prjDetail.setDescription(dto.getDescription());
		this.sgaProjectDmn.saveOrUpdatePrjDetail(prjDetail);

		// final boolean finalIsAdd = isAdd;
		logger.info(">>>企业审计，计算项目个数");
		DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				sgaComInfoDmn.syncComAuditInfo(comInfo.getId());
			}
		});

		return sgaProject;
	}

	@Deprecated
	@Override
	public Object updateBasicInfo(ProjectUpdateDTO dto) {
		
		return null;
	}

	@Override
	public Object changeStatus(String caseId, int mark)  throws BusinessException {
		
		SgaProject sgaProject = this.sgaProjectDmn.findUniqueByProperty("sysCode", caseId);
		if(null == sgaProject)
			throw new BusinessException("合作项目信息不存在，sysCode[{0}]", caseId);
		
		sgaProject.setStatus(mark);
		this.sgaProjectDmn.modify(sgaProject);
		
		return true;
	}

	@Override
	public String changePoster(String caseId, String poster) throws BusinessException {
		
		SgaProject sgaProject = this.sgaProjectDmn.findUniqueByProperty("sysCode", caseId);
		if(null == sgaProject)
			throw new BusinessException("合作项目信息不存在，sysCode[{0}]", caseId);
		
		sgaProject.setPoster(poster);
		this.sgaProjectDmn.modify(sgaProject);
		
		return poster;
	}
	@Override
	public Object updatePoster(String caseId, String img) throws BusinessException {
		
		SgaProject sgaProject = this.sgaProjectDmn.findUniqueByProperty("sysCode", caseId);
		if(null == sgaProject)
			throw new BusinessException("合作项目信息不存在，sysCode[{0}]", caseId);
		
		sgaProject.setPoster(img);
		this.sgaProjectDmn.modify(sgaProject);
		
		return true;
	}

	@Override
	public Object updateRegisterCount(String caseId) throws BusinessException {
		
		SgaProject sgaProject = this.sgaProjectDmn.findUniqueByProperty("sysCode", caseId);
		if(null == sgaProject)
			throw new BusinessException("合作项目信息不存在，sysCode[{0}]", caseId);
		
		sgaProject.setRegisterCount(sgaProject.getRegisterCount()+1);
		this.sgaProjectDmn.modify(sgaProject);
		
		return null;
	}

	@Override
	public Object updateJoinInCount(String caseId, int opType) throws BusinessException {
		
		SgaProject sgaProject = this.sgaProjectDmn.findUniqueByProperty("sysCode", caseId);
		if(null == sgaProject)
			throw new BusinessException("合作项目信息不存在，sysCode[{0}]", caseId);
		
		sgaProject.setJoinInCount(sgaProject.getJoinInCount()+1);
		this.sgaProjectDmn.modify(sgaProject);
		
		return true;
	}

	@Override
	public Object sendInvitation(InvitationInfoDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SgaProject> getProjectsByStatus(int status) {
		
		return this.sgaProjectDmn.findByProperty("status", status, "createTime", false);

	}

	@Override
	public List<SgaProject> getProjectsByCompanyId(Long companyId) {
		
		return this.sgaProjectDmn.findByProperty("cid", companyId, "createTime", false);
	
	}

	@Deprecated
	@Override
	public List<CooProjectResponseDTO> getProjectsByCompanyRealm(String realm) throws BusinessException {
		
		SgaCompany comInfo = this.sgaComInfoDmn.getComByRealm(realm);
		if(null == comInfo)
			throw new BusinessException("企业信息不存在，标识名称[{0}]", realm);
		
		List<SgaProject> projects = this.sgaProjectDmn.findByProperty("cid", comInfo.getId(), "createTime", false);
		if(null == projects || projects.isEmpty())
			return null;
		List<CooProjectResponseDTO> dtos = new ArrayList<CooProjectResponseDTO>(projects.size());
		for(SgaProject sp : projects){
		
			CooProjectResponseDTO dto = null;
			try {
				dto = this.projectModel2DTO(sp);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			if(null == dto) continue;
			
			dtos.add(dto);
		}
		return dtos;
	}
	@Override
	public List<CooProjectResponseDTO> getProjectsByCompanyRealm(String realm, Long userId) {
		
		SgaCompany comInfo = this.sgaComInfoDmn.getComByRealm(realm);
		if(null == comInfo)
			throw new BusinessException("企业信息不存在，标识名称[{0}]", realm);
		List<SgaProject> projects = this.sgaProjectDmn.findByProperty("cid", comInfo.getId(), "createTime", false);
		if(null == projects || projects.isEmpty())
			return null;
		
		List<SgaPrjUser> prjUserRefs = this.sgaProjectDmn.getProjectIdsByUserId(userId);
		// 转成map对象，key：项目id
		Map<Long, SgaPrjUser> prjUserRefsMap = new HashMap<Long, SgaPrjUser>(0); // 初始化长度为：0 
		if(prjUserRefs != null && !prjUserRefs.isEmpty()){
			for(SgaPrjUser sp : prjUserRefs) {
				if(!prjUserRefsMap.containsKey(sp.getPid())) {
					prjUserRefsMap.put(sp.getPid(), sp);
				}
			}
		}
			
		List<CooProjectResponseDTO> dtos = new ArrayList<CooProjectResponseDTO>(projects.size());
		for(SgaProject sp : projects){
			
			CooProjectResponseDTO dto = null;
			try {
				dto = this.projectModel2DTO(sp);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			if(null == dto) continue;
			
			SgaPrjUser temp = prjUserRefsMap.get(sp.getId());
			dto.setUserStatus(null == temp? UserStatus.PROJECT_NOT_REGISTER : temp.getStatus());
			
			dtos.add(dto);
		}
		return dtos;
	}
	
	@Override
	public List<CooProjectResponseDTO> getProjectsByUserId(Long userId) {
		
		List<SgaPrjUser> prjUserRef = (List<SgaPrjUser>) this.sgaProjectDmn.getProjectIdsByUserId(userId);
		
		if(null == prjUserRef || prjUserRef.isEmpty())
			return null;
		
		//List<SgaProject> projects = this.sgaProjectDmn.findByIds(projectIds.toArray(new Long[projectIds.size()]));
		List<CooProjectResponseDTO> dtos = new ArrayList<CooProjectResponseDTO>();
		for(SgaPrjUser pu : prjUserRef) {
			SgaProject sp = this.sgaProjectDmn.loadById(pu.getPid());
			// 过滤关闭的项目
			if(0 == sp.getStatus()) {
				continue;
			}
			CooProjectResponseDTO dto = null;
			try {
				dto = this.projectModel2DTO(sp);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			if(null == dto) continue;
			
			dto.setUserStatus(pu.getStatus());
			dto.setUserRegTime(pu.getCreateTime());
			dtos.add(dto);
		}
		
		return dtos;
	}
	
	/*@Override
	public Object addUserToProject(String caseId, Long userId, int status, String description, String direction)  throws BusinessException{
		
		SgaProject sgaProject = this.sgaProjectDmn.findUniqueByProperty("sysCode", caseId);
		if(null == sgaProject)
			throw new BusinessException("合作项目信息不存在，sysCode[{0}]", caseId);
		
		SgaPrjUser pu = this.sgaProjectDmn.getPrjRefUser(caseId, userId);
		if(null == pu) {
			pu = new SgaPrjUser();
			pu.setCreateTime(new Date());	
		}
		pu.setStatus(status);
		pu.setDescription(description);
		pu.setPid(sgaProject.getId());
		pu.setDirection(direction);
		this.sgaProjectDmn.saveOrUpdate(pu);
		
		return null;
	}*/

	@Override
	public Object updateUserStatusInProject(String caseId, Long userId, int status, String description) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserResponseDTO> getRegisterUsers(String caseId) throws BusinessException {

		SgaProject sgaProject = this.sgaProjectDmn.findUniqueByProperty("sysCode", caseId);
		if (null == sgaProject)
			throw new BusinessException("合作项目信息不存在，sysCode[{0}]", caseId);

		List<SgaPrjUser> prjusers = this.sgaProjectDmn.getPrjRefUsers(caseId, new Integer[]{-1, 0, 1, 2});
		if (null == prjusers)
			return null;

		List<UserResponseDTO> dtos = new ArrayList<UserResponseDTO>();
		for (SgaPrjUser pu : prjusers) {
			UserResponseDTO dto = this.sgaUserService.getUserById(pu.getUserId());
			if (null == dto)
				continue;

			dtos.add(dto);
		}

		return dtos;
	}

	@Override
	public List<UserResponseDTO> getJoinInUsers(String caseId) throws BusinessException {

		SgaProject sgaProject = this.sgaProjectDmn.findUniqueByProperty("sysCode", caseId);
		if (null == sgaProject)
			throw new BusinessException("合作项目信息不存在，sysCode[{0}]", caseId);

		List<SgaPrjUser> prjusers = this.sgaProjectDmn.getPrjRefUsers(caseId, new Integer[]{1});
		if (null == prjusers)
			return null;

		List<UserResponseDTO> dtos = new ArrayList<UserResponseDTO>();
		for (SgaPrjUser pu : prjusers) {
			UserResponseDTO dto = this.sgaUserService.getUserById(pu.getUserId());
			if (null == dto)
				continue;

			dtos.add(dto);
		}

		return dtos;
	}
	
	@Override
	public CooProjectResponseDTO getProjectById(Long id) throws BusinessException {

		SgaProject p = this.sgaProjectDmn.loadById(id);

		if (null == p)
			throw new BusinessException("合作项目信息不存在，id[{0}]", id);
		
		try {
			CooProjectResponseDTO dto = this.projectModel2DTO(p);
			return dto;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	@Override
	public CooProjectResponseDTO getProjectByCaseId(String caseId) throws BusinessException {
		
		SgaProject p = this.sgaProjectDmn.findUniqueByProperty("sysCode", caseId);

		if (null == p)
			throw new BusinessException("合作项目信息不存在，sysCode[{0}]", caseId);
		
		try {
			CooProjectResponseDTO dto = this.projectModel2DTO(p);
			return dto;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	/**
	 * 项目模型转响应DTO
	 * @param p
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private CooProjectResponseDTO projectModel2DTO(SgaProject p)
			throws IllegalAccessException, InvocationTargetException {
		
		CooProjectResponseDTO dto = this.dozerMapper.map(p, CooProjectResponseDTO.class);
		
		SgaPrjDetail pd = this.sgaProjectDmn.getDesc(p.getId());
		if (pd != null)
			dto.setDescription(pd.getDescription());
		
		SgaCompany com = this.sgaComInfoDmn.loadById(p.getCid());
		if(com != null){
			CompanySimpleInfoResponseDTO comDTO = this.dozerMapper.map(com, CompanySimpleInfoResponseDTO.class);
			dto.setCompany(comDTO);
		}
		// 项目审计信息（不使用模型SgaProject本身的统计数字，不准确）
		List<SgaPrjUser> prj2users = this.sgaProjectDmn.getPrjRefUsers(p.getId());
		if(prj2users != null && !prj2users.isEmpty()) {
			dto.setRegisterCount(prj2users.size());
			AtomicInteger joininCounter = new AtomicInteger(0);
			for(SgaPrjUser prj2user : prj2users) {
				if(UserStatus.PROJECT_JOININ == prj2user.getStatus()) {
					joininCounter.incrementAndGet();
				}
			}
			dto.setJoinInCount(joininCounter.get());
		} else {
			dto.setRegisterCount(0);
			dto.setJoinInCount(0);
		}
		return dto;
	}
	@Override
	public Object recordUploadFile(FileRecordDTO dto) {
	
		SgaFileRecord fr = this.sgaFileRecordDmn.findUniqueByProperty("resId", dto.getResId());
		if(fr != null) return true;
		
		fr = new SgaFileRecord();
		fr.setOpType("u");
		fr.setAgent(StringUtils.hasLength(dto.getAgent())? dto.getAgent() : "ceadmin");
		fr.setCreateTime(new Date());
		fr.setResId(dto.getResId());
		fr.setCreator(dto.getCreator());
		fr.setResTypeCode(dto.getResTypeCode());
		fr.setDomainCode(dto.getDomainCode());
		try {
			this.dozerMapper.map(fr, dto);
			this.sgaFileRecordDmn.add(fr);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return false;
	}
	@Override
    public boolean recordWz(String caseId, String wzId, String creator, Date createTime) throws BusinessException {
		
    	SgaProject p = this.sgaProjectDmn.findUniqueByProperty("sysCode", caseId);

		if (null == p)
			throw new BusinessException("合作项目信息不存在，sysCode[{0}]", caseId);
	
		SgaPrjWz wz = new SgaPrjWz();
		wz.setCreateTime(null == createTime? new Date() : createTime );
		wz.setCreator(creator);
		wz.setWzId(wzId);
		wz.setPid(p.getId());
		
		this.sgaProjectDmn.addPrj2WZ(wz);
		return true;
	}
	@Override
	public void deletePrjWzRecordByWzId(String wzId) {
		
		logger.debug(">>>删除项目与微作的关联记录");
		this.sgaProjectDmn.deleteRecordByWzId(wzId);
		
	}
	@Override
	public Object joinInProject(PrjUserRequestDTO dto) throws BusinessException{
		
		SgaProject p = this.sgaProjectDmn.findUniqueByProperty("sysCode", dto.getCaseId());

		if (null == p)
			throw new BusinessException("合作项目信息不存在，sysCode[{0}]", dto.getCaseId());
		
		// 项目与用户关联
		if(!this.sgaProjectDmn.existUser(p.getId(), dto.getUserId())){
			
			SgaPrjUser prj2user = new SgaPrjUser();
			prj2user.setPid(p.getId());
			prj2user.setUserId(dto.getUserId());
			prj2user.setStatus(0);
			prj2user.setCreateTime(new Date());
			prj2user.setDirection(dto.getDirection());
			prj2user.setFormId(dto.getFormId());
			this.sgaProjectDmn.saveOrUpdate(prj2user);
		}
		
		// 企业与用户关联
		if(!this.sgaComInfoDmn.existUser(dto.getCompanyId(), dto.getUserId())){
			
			// 项目的注册人数递增1
			p.setRegisterCount(p.getRegisterCount()+1);
			this.sgaProjectDmn.modify(p);
			
			SgaComUser com2user = new SgaComUser();
			com2user.setCid(dto.getCompanyId());
			com2user.setStatus(0);
			com2user.setUserId(dto.getUserId());
			this.sgaComInfoDmn.addComUser(com2user);
		}
		
		return true;
	}
	
	@Override
	public List<UserResponseDTO> listRegisterUsers(String caseId) {
		
		CooProjectResponseDTO projectDTO = this.getProjectByCaseId(caseId);
		List<SgaPrjUser> prjusers = this.sgaProjectDmn.getPrjRefUsers(projectDTO.getId());
		if(null == prjusers || 0 == prjusers.size()) return null;
		
		List<UserResponseDTO> respDTOs = new ArrayList<UserResponseDTO>(prjusers.size());
		for(SgaPrjUser pu : prjusers){
			UserResponseDTO dto = this.sgaUserService.getUserById(pu.getUserId());
			if(null == dto) continue;
			
			dto.setStatus(pu.getStatus());
			// 报名时间
			dto.setCreateTime(pu.getCreateTime());
			respDTOs.add(dto);
		}
		return respDTOs;
	}
	@Override
	public Object setUserStatusInProject(PrjUserStatusPutDTO dto) {
		
		// 修改模型SgaPrjUser中用户状态；
		SgaPrjUser pu = this.sgaProjectDmn.getPrjRefUser(dto.getCaseId(), dto.getUserId());
		if(null == pu){
			logger.info(">>>项目与用户未关联，case id[{}]，user id[{}]", dto.getCaseId(), dto.getUserId());
			return false;
		}
		pu.setStatus(dto.getStatus());
		this.sgaProjectDmn.saveOrUpdate(pu);
		
		final CooProjectResponseDTO prjResp = this.getProjectByCaseId(dto.getCaseId());
		// 同步模型SgaComUser中用户状态；
		if(dto.getStatus().equals(UserStatus.PROJECT_JOININ)) {
			SgaComUser comUser = this.sgaComInfoDmn.getComUser(prjResp.getCompany().getId(), dto.getUserId());
			comUser.setStatus(dto.getStatus());
			this.sgaComInfoDmn.saveOrUpdate(comUser);
		}

		// 统计个数到模型SgaComStatistics
		DistThreadManager.MyCacheThreadPool.execute(new Runnable() {	
			@Override
			public void run() {
				sgaComInfoDmn.syncComAuditInfo(prjResp.getCompany().getId());
			}
		});
		
/*		int joininCount = this.sgaComInfoDmn.operateComStatJoininCount(dto.getStatus().equals(UserStatus.Project_JoinIn), prjResp.getCompany().getId());
		logger.info(">>>目前企业已加入人数：[{}]", joininCount);
		*/
		return true;
	}
	@Override
	public SgaPrjWz getRefWzById(String wzId) {
		
		return this.sgaProjectDmn.getPrjRefWz(wzId);
	}
	@Override
	public void deleteProjectByCode(String caseId) {
		
		this.sgaProjectDmn.deleteProjectByCode(caseId);
	}
	@Override
	public SgaPrjUser getPrjRefUser(String projectCode, Long userId) {
		
		return this.sgaProjectDmn.getPrjRefUser(projectCode, userId);
	}
	@Override
	public void deletePosterByProjectCode(String code) {
		
		this.mongoFileStorageDmn.deleteByFields(new String[]{"metadata.code", "metadata.type"},  new String[]{code.replace("{", "").replace("}", ""), IMAGE_TYPE_BACKGROUND});
	}
	@Override
	public String updatePoster(ImgInfo imgInfo) {
		
		SgaProject p = this.sgaProjectDmn.findUniqueByProperty("sysCode", imgInfo.getId());
		if (null == p)
			throw new BusinessException("合作项目信息不存在，sysCode[{0}]", imgInfo.getId());
		
		String mongoFileId = this.mongoFileStorageDmn.storeToMongo(imgInfo, IMAGE_TYPE_BACKGROUND);
		String relativePath = IMAGE_PATH_PREFIX_BACKGROUND + mongoFileId;
		p.setPoster(relativePath);
		p.setPosterName(imgInfo.getName());
		this.sgaProjectDmn.modify(p);
		return relativePath;
	}
}
