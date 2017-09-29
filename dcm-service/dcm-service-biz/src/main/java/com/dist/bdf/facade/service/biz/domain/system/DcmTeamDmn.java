package com.dist.bdf.facade.service.biz.domain.system;

import java.util.List;

import com.dist.bdf.base.domain.GenericDmn;
import com.dist.bdf.model.entity.system.DcmTeam;
import com.dist.bdf.model.entity.system.DcmTeamUser;

public interface DcmTeamDmn  extends GenericDmn<DcmTeam, Long>{

	/**
	 * 获取团队与成员关联关系
	 * @param teamId
	 * @param userId
	 * @return
	 */
	DcmTeamUser getTeamUserRef(Long teamId, Long userId);

	/**
	 * 保存关联关系
	 * @param teamUser
	 */
	void saveOrUpdateTeamUserRef(DcmTeamUser teamUser);
	/**
	 * 根据团队id获取关联的用户
	 * @param teamId
	 */
	List<DcmTeamUser> getUserRef(Long teamId);
	/**
	 * 批量删除团队成员
	 * @param teamId
	 * @param userIds
	 */
	void deleteUsers(Long teamId, Long[] userIds);
	/**
	 * 删除与用户相关的团队记录
	 * @param userIds
	 */
	void removeUsers(Long[] userIds);
	/**
	 * 根据项目id和用户id，获取所在团队
	 * @param projectId
	 * @param userId
	 * @return
	 */
	DcmTeam getTeam(Long projectId, Long userId);

}
