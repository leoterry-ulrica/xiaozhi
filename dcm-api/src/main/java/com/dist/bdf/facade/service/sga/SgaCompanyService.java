package com.dist.bdf.facade.service.sga;

import java.util.List;

import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.model.dto.sga.CompanyInfoAddDTO;
import com.dist.bdf.model.dto.sga.CompanyInfoResponseDTO;
import com.dist.bdf.model.dto.sga.CompanyInfoUpdateDTO;
import com.dist.bdf.model.dto.sga.ImgInfo;
import com.dist.bdf.model.entity.sga.SgaCompany;
import com.dist.bdf.model.entity.sga.SgaComAudit;

/**
 * 企业服务
 * @author weifj
 *
 */
public interface SgaCompanyService {

	/**
	 * 获取有效的企业清单，包括公开和不公开的
	 * @return
	 */
	List<CompanyInfoResponseDTO> listValidCompany();
	/**
	 * 获取所有的企业清单，包括已废弃
	 * @return
	 */
	Object listAllCompany();
	/**
	 * 添加或修改项目
	 * @param dto
	 * @return
	 */
	Object addOrUpdate(CompanyInfoAddDTO dto) throws BusinessException;
	/**
	 * 单独修改logo
	 * @param comId 企业id（序列号）
	 * @param logo logo路径
	 * @return
	 */
	String changeLogo(Long comId, String logo) throws BusinessException;
	/**
	 * 修改背景图
	 * @param comId
	 * @param img
	 * @return
	 */
	String changeImg(Long comId, String img) throws BusinessException;
	/**
	 * 更新企业的基本信息
	 * @param dto
	 * @return
	 */
	Object updateBasicInfo(CompanyInfoUpdateDTO dto) throws BusinessException, Exception;
	/**
	 * 根据id获取企业信息
	 * @param id
	 * @return
	 */
	CompanyInfoResponseDTO getComInfoById(Long id) throws BusinessException;
	/**
	 * 根据唯一编码获取企业信息
	 * @param sysCode
	 * @return
	 */
	SgaCompany getComInfoByCode(String sysCode);
	/**
	 * 根据序列id删除企业
	 * @param id
	 * @return
	 */
	boolean deleteCompanyById(Long id) throws BusinessException;
	/**
	 * 根据企业的域获取信息，包括描述信息
	 * @param realm
	 * @return
	 */
	CompanyInfoResponseDTO getComInfoByRealm(String realm) throws BusinessException;
	/**
	 * 根据企业域（realm）更改状态
	 * @param realm
	 * @param status 企业状态。-1：删除:0：关闭；1：正常；
	 * @return
	 */
	Object changeCompanyStatusByRealm(String realm, int status) throws BusinessException;
	/**
	 * 获取公开的企业信息
	 * @return
	 */
	List<CompanyInfoResponseDTO> listPublicCompany();
	/**
	 * 根据域，获取公开企业信息，包括详情
	 * @param realm
	 * @return
	 */
	CompanyInfoResponseDTO getPubComInfoByRealm(String realm) throws BusinessException;
	/**
	 * 同步企业审计信息
	 * @param realm 域
	 */
	SgaComAudit syncCompanyAudit(String realm);
	/**
	 * 根据用户编码，获取跟用户相关的企业信息
	 * @param userCode
	 * @return
	 */
	 List<SgaCompany> getComInfoByUserCode(String userCode);
	 /**
	  * 保存企业背景图到mongodb中
	  * @param imgInfo
	  * @return 返回文件相对路径
	  */
	String updateImg(ImgInfo imgInfo);
	 /**
	  * 保存企业logo到mongodb中
	  * @param imgInfo
	  * @return 返回文件相对路径
	  */
	String updateLogo(ImgInfo imgInfo);
	/**
	 * 根据企业编码，删除背景图
	 * @param code
	 */
	void deleteImgByCompanyCode(String code);
	/**
	 * 根据企业编码，删除logo
	 * @param code
	 */
	void deleteLogoByCompanyCode(String code);
}
