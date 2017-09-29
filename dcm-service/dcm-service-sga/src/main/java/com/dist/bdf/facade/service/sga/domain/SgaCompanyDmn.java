package com.dist.bdf.facade.service.sga.domain;

import java.util.List;

import com.dist.bdf.base.domain.GenericDmn;
import com.dist.bdf.model.entity.sga.SgaCompany;
import com.dist.bdf.model.entity.sga.SgaComAudit;
import com.dist.bdf.model.entity.sga.SgaComUser;


public interface SgaCompanyDmn  extends GenericDmn<SgaCompany, Long>{
	/**
	 * 添加或更新企业描述信息
	 * @param companyId
	 * @param desc
	 * @return
	 */
	Object addOrUpdateComDesc(Long companyId, String desc);
	/**
	 * 获取企业描述信息
	 * @param comId
	 * @return
	 */
	Object getComDesc(Long comId);
	/**
	 * 添加企业与用户的关联
	 * @param cu
	 * @return
	 */
	Object addComUser(SgaComUser cu);
	/**
	 * 获取关联对象
	 * @param companyId
	 * @param uid
	 */
	SgaComUser getComUser(Long companyId, Long uid);
	/**
	 * 更新企业和用户关联
	 * @param cu
	 */
	SgaComUser modify(SgaComUser cu);
	/**
	 * 根据企业缩写编号获取企业信息
	 * @param companySymbolicName
	 * @return
	 */
	SgaCompany getComByRealm(String realmName);
	/**
	 * 根据企业id获取统计信息
	 * @param companyId
	 * @return
	 */
	SgaComAudit getStatByComId(Long companyId);
	/**
	 * 企业的项目统计数
	 * @param isAdd
	 * @param comId
	 * @return
	 */
	//int operateComStatProjectCount(boolean isAdd, Long comId);
	/**
	 * 企业的报名人数
	 * @param isAdd
	 * @param comId
	 * @return
	 */
	//int operateComStatRegisterCount(boolean isAdd, Long comId);
	/**
	 * 企业的招募人数
	 * @param isAdd
	 * @param comId
	 * @return
	 */
	//int operateComStatJoininCount(boolean isAdd, Long comId);
	/**
	 * 判断企业下是否已存在用户
	 * @param companyId
	 * @param userId
	 */
	boolean existUser(Long companyId, Long userId);
	/**
	 * 保存或更新企业和用户的关联关系
	 * @param comUser
	 */
	void saveOrUpdate(SgaComUser comUser);
	/**
	 * 获取企业下的关联用户
	 * @param cid 企业id
	 * @return
	 */
	List<SgaComUser> getComUserRef(Long cid);
	/**
	 * 同步企业审计信息
	 * @param companyId
	 * @return
	 */
	SgaComAudit syncComAuditInfo(Long companyId);
	/**
	 * 根据用户序列id获取关联信息
	 * @param userId 用户id
	 */
	List<SgaComUser> getComRefByUserId(Long userId);

}
