package com.dist.bdf.facade.service.sga.domain;

import java.util.List;

import com.dist.bdf.base.domain.GenericDmn;
import com.dist.bdf.model.entity.sga.SgaPrjDetail;
import com.dist.bdf.model.entity.sga.SgaPrjUser;
import com.dist.bdf.model.entity.sga.SgaPrjWz;
import com.dist.bdf.model.entity.sga.SgaProject;


public interface SgaProjectDmn  extends GenericDmn<SgaProject, Long> {

	/**
	 * 获取项目的简介信息
	 * @param pid
	 * @return
	 */
	SgaPrjDetail getDesc(Long pid);
	/**
	 * 添加或更新项目简介信息
	 * @param prjDetail
	 */
	Object saveOrUpdatePrjDetail(SgaPrjDetail prjDetail);
	/**
	 * 根据项目的id，获取项目的人员关联信息
	 * @param caseId
	 * @param status 状态
	 * @return
	 */
	List<SgaPrjUser> getPrjRefUsers(String caseId, Integer[]status);
	/**
	 * 根据项目的id和用户id，获取项目的人员关联信息
	 * @param caseId
	 * @param userId
	 * @return
	 */
	SgaPrjUser getPrjRefUser(String caseId, Long userId);
	/**
	 * 
	 * @param pu
	 */
	void saveOrUpdate(SgaPrjUser pu);
	/**
	 * 保存项目微作
	 * @param wz
	 */
	void addPrj2WZ(SgaPrjWz wz);
	/**
	 * 根据微作id删除项目与微作的关联记录
	 * @param wzId
	 */
	void deleteRecordByWzId(String wzId);
	/**
	 * 根据用户id获取关联的项目id集合
	 * @param userId
	 */
	List<SgaPrjUser> getProjectIdsByUserId(Long userId);
	/**
	 * 判断用户是否与项目建立了关联
	 * @param pid
	 * @param userId
	 * @return
	 */
	boolean existUser(Long pid, Long userId);
	/**
	 * 获取项目下参与人员关联信息
	 * @param projectId
	 */
	List<SgaPrjUser> getPrjRefUsers(Long projectId);
	/**
	 * 根据微作id，获取关联信息
	 * @param wzId
	 * @return
	 */
	SgaPrjWz getPrjRefWz(String wzId);
	/**
	 * 删除项目，包括与项目相关的任何记录
	 * @param caseId 案例id
	 */
	void deleteProjectByCode(String caseId);	
}
