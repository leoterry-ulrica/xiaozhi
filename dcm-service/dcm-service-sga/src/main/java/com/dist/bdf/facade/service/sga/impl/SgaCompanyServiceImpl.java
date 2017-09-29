package com.dist.bdf.facade.service.sga.impl;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.facade.service.sga.SgaCompanyService;
import com.dist.bdf.facade.service.sga.domain.SgaCompanyDmn;
import com.dist.bdf.facade.service.sga.domain.SgaProjectDmn;
import com.dist.bdf.facade.service.sga.domain.SgaUserDmn;
import com.dist.bdf.manager.mongo.MongoFileStorageDmn;
import com.dist.bdf.model.dto.sga.CompanyInfoAddDTO;
import com.dist.bdf.model.dto.sga.CompanyInfoResponseDTO;
import com.dist.bdf.model.dto.sga.CompanyInfoUpdateDTO;
import com.dist.bdf.model.dto.sga.ImgInfo;
import com.dist.bdf.model.entity.sga.SgaComDetail;
import com.dist.bdf.model.entity.sga.SgaComUser;
import com.dist.bdf.model.entity.sga.SgaCompany;
import com.dist.bdf.model.entity.sga.SgaComAudit;
import com.dist.bdf.model.entity.sga.SgaProject;
import com.dist.bdf.model.entity.sga.SgaUser;

@Service("SgaCompanyService")
@Transactional(propagation = Propagation.REQUIRED)
public class SgaCompanyServiceImpl implements SgaCompanyService {

	private static Logger logger = LoggerFactory.getLogger(SgaCompanyServiceImpl.class);
	
	@Autowired
	private SgaCompanyDmn sgaCompanyDmn;
	@Autowired
	private SgaProjectDmn sgaProjectDmn;
	@Autowired
	private SgaUserDmn sgaUserDmn;
	@Autowired
	private Mapper dozerMapper;
	@Autowired
	private MongoFileStorageDmn mongoFileStorageDmn;
	/**
	 * 背景图类型
	 */
	private static final String IMAGE_TYPE_BACKGROUND = "img";
	/**
	 * logo类型
	 */
	private static final String IMAGE_TYPE_LOGO = "logo";
	/**
	 * 企业背景图的相对路径前缀
	 */
	public static final String IMAGE_PATH_PREFIX_BACKGROUND = "/sga/company/fs/img/";
	/**
	 * 企业logo的相对路径前缀
	 */
	public static final String IMAGE_PATH_PREFIX_LOGO = "/sga/company/fs/logo/";
	
	
	@Override
	public List<CompanyInfoResponseDTO> listValidCompany() {
		
		Map<String, Object[]> properties = new HashMap<String, Object[]>();
		properties.put("status", new Object[]{1});
		
		List<SgaCompany> cominfos = sgaCompanyDmn.findByProperties(properties);
		if(null == cominfos || 0 == cominfos.size())
			return null;
	
		List<CompanyInfoResponseDTO> dtos = new ArrayList<CompanyInfoResponseDTO>();
		for(SgaCompany info : cominfos){
			CompanyInfoResponseDTO dto = setCompanyStat(info); 
			if(null == dto) continue;
			
			dtos.add(dto);
		}

		return dtos;
	}
	
	@Override
	public List<CompanyInfoResponseDTO> listPublicCompany() {
		
		List<SgaCompany> cominfos = sgaCompanyDmn.findByProperties(new String[]{"status"}, new Object[]{1}, "createTime", true);
		if(null == cominfos || 0 == cominfos.size())
			return null;
	
		List<CompanyInfoResponseDTO> dtos = new ArrayList<CompanyInfoResponseDTO>();
		for(SgaCompany info : cominfos){
			CompanyInfoResponseDTO dto = setCompanyStat(info); 
			if(null == dto) continue;
			
			dtos.add(dto);
		}

		return dtos;
	}

	/**
	 * 设置企业统计信息
	 * @param info
	 * @return
	 */
	private CompanyInfoResponseDTO setCompanyStat(SgaCompany info) {

		try {
			CompanyInfoResponseDTO dto = this.dozerMapper.map(info, CompanyInfoResponseDTO.class);
			SgaComAudit cs = this.sgaCompanyDmn.getStatByComId(info.getId());
			List<SgaProject> projects = this.sgaProjectDmn.findByProperties(new String[]{"cid","status"}, new Object[]{info.getId(), 1});
			dto.setProjectRecruitedCount(null == projects? 0 : projects.size());
			if(cs != null){
				dto.setProjectCount(cs.getProjectCount());
				dto.setRegisterCount(cs.getRegisterCount());
				dto.setJoininCount(cs.getJoininCount());
				//double rate = (cs.getJoininCount()/Double.parseDouble(String.valueOf(cs.getRegisterCount()))) * 100;
				//dto.setRate(Double.isInfinite(rate)? 0 : (Double.isNaN(rate))? 0:rate);
		        try {
		        	 // 创建一个数值格式化对象  
			        NumberFormat numberFormat = NumberFormat.getInstance();  
			        // 设置精确到小数点后2位  
			        numberFormat.setMaximumFractionDigits(2);  
			        String rate = numberFormat.format((float) cs.getJoininCount() / (float) cs.getRegisterCount() * 100);  
		        	 dto.setRate(Double.valueOf(rate));
		        } catch(Exception e) {
		        	logger.warn(">>>数字格式不对，忽略，详情：{}", e.getMessage());
		        }
			}
			return dto;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public Object listAllCompany() {
		
		List<SgaCompany> cominfos = sgaCompanyDmn.find("createTime", true);
		if(null == cominfos || 0 == cominfos.size())
			return null;
		
	/*	for(SgaComInfo com : cominfos) {	
			com.setLogo(this.sgaComInfoDmn.getComLogoURL(contextPath, baseURL, com));
			com.setImg(this.sgaComInfoDmn.getComImgURL(contextPath, baseURL, com));
		}*/
		return cominfos;
	}

	@Override
	public Object addOrUpdate(CompanyInfoAddDTO dto)  throws BusinessException {
		
		SgaCompany cominfo = null;
		try {
			Map<String, Object[]> properties = new HashMap<String, Object[]>();
			properties.put("realm", new Object[]{dto.getRealm()});
			properties.put("status", new Object[]{0,1});
			List<SgaCompany> list = this.sgaCompanyDmn.findByProperties(properties);
			if(list != null && !list.isEmpty()){
				cominfo = list.get(0);
			}else {
				cominfo = new SgaCompany();
				cominfo.setSysCode(UUID.randomUUID().toString().toUpperCase());
			}
			this.dozerMapper.map(dto, cominfo);
			// BeanUtils.copyProperties(cominfo, dto);
			
			//cominfo.setStatus(1);
			cominfo.setCreateTime(new Date());
			this.sgaCompanyDmn.saveOrUpdate(cominfo);
			this.sgaCompanyDmn.addOrUpdateComDesc(cominfo.getId(), dto.getDescription());
			
		} catch (Exception e) {
			
			logger.error(e.getMessage(), e);
			throw new BusinessException("添加企业信息失败，详情："+e.getMessage());
		}
		return cominfo;
	}

	@Override
	public String changeLogo(Long comId, String logo)  throws BusinessException {
		
		SgaCompany com = this.sgaCompanyDmn.findById(comId);
		if(null == com) throw new BusinessException("没有找到对应的企业实体，id["+comId+"]");
		
		com.setLogo(logo);
		this.sgaCompanyDmn.modify(com);
		
		return logo;
	}
	
	@Override
	public String changeImg(Long comId, String img) throws BusinessException {
		
		SgaCompany com = this.sgaCompanyDmn.findById(comId);
		if(null == com) throw new BusinessException("没有找到对应的企业实体，id["+comId+"]");
		
		com.setImg(img);
		this.sgaCompanyDmn.modify(com);
		
		return img;
	}

	@Override
	public Object updateBasicInfo(CompanyInfoUpdateDTO dto) throws BusinessException, Exception {
		
		SgaCompany com = this.sgaCompanyDmn.findById(dto.getId());
		if(null == com) {
			throw new BusinessException("没有找到对应的企业实体，id["+dto.getId()+"]");
		}
		try {
			BeanUtils.copyProperties(com, dto);
			this.sgaCompanyDmn.modify(com);
			// 修改企业的简介信息
			this.sgaCompanyDmn.addOrUpdateComDesc(com.getId(), dto.getDescription());
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return com;
	}

	@Override
	public CompanyInfoResponseDTO getComInfoById(Long id) throws BusinessException {

		SgaCompany cominfo = this.sgaCompanyDmn.findById(id);
		if (null == cominfo) {
			throw new BusinessException("没有找到对应的企业信息，id[{0}]", id);
		}
		try {
			CompanyInfoResponseDTO dto = new CompanyInfoResponseDTO();
			BeanUtils.copyProperties(dto, cominfo);
			SgaComDetail cd = (SgaComDetail) this.sgaCompanyDmn.getComDesc(cominfo.getId());
			dto.setDescription(null == cd ? "" : cd.getDescription());

			return dto;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public SgaCompany getComInfoByCode(String sysCode) {
	
		return this.sgaCompanyDmn.findUniqueByProperty("sysCode", sysCode);
	}
	
	public static void main(String[] args) throws Exception {
		
		/*SgaComInfo com = new SgaComInfo();
		com.setSysCode(UUID.randomUUID().toString());
		
		CompanyInfoUpdateDTO updateDTO = new CompanyInfoUpdateDTO();
		updateDTO.setId(12390L);
		
		BeanUtils.copyProperties(com, updateDTO);
		
		System.out.println("666666");*/
		
		int a = 20;
		// int b = 2000;
		Double c = (a/Double.parseDouble(String.valueOf(0))) * 100;
		System.out.println(Double.isInfinite(c));
		
		int num1 = 9;  
        int num2 = 0;  
        // 创建一个数值格式化对象  
        NumberFormat numberFormat = NumberFormat.getInstance();  
        // 设置精确到小数点后2位  
        numberFormat.setMaximumFractionDigits(2);  
        String result = numberFormat.format((float) num1 / (float) num2 * 100);  
        System.out.println("num1和num2的百分比为:" + result + "%");  
	}
	
	@Override
	public boolean deleteCompanyById(Long id) throws BusinessException {
		
		SgaCompany info = this.sgaCompanyDmn.findById(id);
		if(null == info)
			throw new BusinessException("没有找到对应的企业信息，id[{0}]", id);
		
		info.setStatus(-1);
		this.sgaCompanyDmn.modify(info);
		return true;
	}
	
	@Override
	public CompanyInfoResponseDTO getComInfoByRealm(String realm) throws BusinessException {
		
		SgaCompany cominfo = this.sgaCompanyDmn.findUniqueByProperty("realm", realm);
		if(null == cominfo)
			throw new BusinessException("没有找到对应的企业信息，realm[{0}]", realm);
		
		CompanyInfoResponseDTO dto = this.setCompanyStat(cominfo);
		
		SgaComDetail cd = (SgaComDetail) this.sgaCompanyDmn.getComDesc(cominfo.getId());
		dto.setDescription(null == cd ? "":cd.getDescription());
		
		return dto;
	}
	
	@Override
	public Object changeCompanyStatusByRealm(String realm, int status) throws BusinessException {
		
		SgaCompany cominfo = this.sgaCompanyDmn.findUniqueByProperty("realm", realm);
		if(null == cominfo)
			throw new BusinessException("没有找到对应的企业信息，realm[{0}]", realm);
		
		cominfo.setStatus(status);
		this.sgaCompanyDmn.modify(cominfo);
		
		return true;
	}
	@Override
	public CompanyInfoResponseDTO getPubComInfoByRealm(String realm) throws BusinessException{
		
		CompanyInfoResponseDTO dto = null;
		SgaCompany cominfo = this.sgaCompanyDmn.findUniqueByProperties(new String[]{"realm", "status"}, new Object[]{realm, 1} );
		if(null == cominfo) {
			throw new BusinessException("没有找到对应的企业信息，realm[{0}]", realm);
		}
		
		dto = this.setCompanyStat(cominfo);
		
		SgaComDetail cd = (SgaComDetail) this.sgaCompanyDmn.getComDesc(cominfo.getId());
		dto.setDescription(null == cd ? "":cd.getDescription());
		
		return dto;
	}
	@Override
	public SgaComAudit syncCompanyAudit(String realm) {
		SgaCompany cominfo = this.sgaCompanyDmn.findUniqueByProperties(new String[]{"realm", "status"}, new Object[]{realm, 1} );
		if(null == cominfo) {
			throw new BusinessException("没有找到对应的企业信息，realm[{0}]", realm);
		}

		return this.sgaCompanyDmn.syncComAuditInfo(cominfo.getId());
	}
	@Override
	public List<SgaCompany> getComInfoByUserCode(String userCode) {
		SgaUser sgaUser = this.sgaUserDmn.getByCode(userCode);
		List<SgaComUser> comRefs = this.sgaCompanyDmn.getComRefByUserId(sgaUser.getId());
		if(null == comRefs || comRefs.isEmpty()) {
			return null;
		}
		List<Long> companyIds = new ArrayList<Long>();
		for(SgaComUser comref : comRefs) {
			companyIds.add(comref.getCid());
		}
		return this.sgaCompanyDmn.findByIds(companyIds.toArray(new Long[companyIds.size()]));
	}

	@Override
	public String updateImg(ImgInfo imgInfo) {
		
		SgaCompany company = this.getComInfoByCode(imgInfo.getId());
		if(null == company) {
			throw new BusinessException("没有找到对应的企业信息，code[{0}]", imgInfo.getId());
		}
		String mongoFileId = this.mongoFileStorageDmn.storeToMongo(imgInfo, IMAGE_TYPE_BACKGROUND);
		String relativePath = IMAGE_PATH_PREFIX_BACKGROUND + mongoFileId;
		company.setImg(relativePath);
		company.setImgName(imgInfo.getName());
		this.sgaCompanyDmn.modify(company);
		return relativePath;
	}
	@Override
	public String updateLogo(ImgInfo imgInfo) {
		
		SgaCompany company = this.getComInfoByCode(imgInfo.getId());
		if(null == company) {
			throw new BusinessException("没有找到对应的企业信息，code[{0}]", imgInfo.getId());
		}
		String mongoFileId = this.mongoFileStorageDmn.storeToMongo(imgInfo, IMAGE_TYPE_LOGO);
		String relativePath = IMAGE_PATH_PREFIX_LOGO + mongoFileId;
		company.setLogo(relativePath);
		company.setLogoName(imgInfo.getName());
		this.sgaCompanyDmn.modify(company);
		return relativePath;
	}
	@Override
	public void deleteImgByCompanyCode(String code) {
		
		this.mongoFileStorageDmn.deleteByFields(new String[]{"metadata.code", "metadata.type"},  new String[]{code, IMAGE_TYPE_BACKGROUND});
	}
	@Override
	public void deleteLogoByCompanyCode(String code) {
		
		this.mongoFileStorageDmn.deleteByFields(new String[]{"metadata.code", "metadata.type"},  new String[]{code, IMAGE_TYPE_LOGO});
	}
}
