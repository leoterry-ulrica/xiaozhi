package com.dist.bdf.facade.service.biz.domain.system.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.facade.service.biz.dao.DcmTeamDAO;
import com.dist.bdf.facade.service.biz.dao.DcmTeamUserDAO;
import com.dist.bdf.facade.service.biz.domain.system.DcmTeamDmn;
import com.dist.bdf.model.entity.system.DcmTeam;
import com.dist.bdf.model.entity.system.DcmTeamUser;


@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class DcmTeamDmnImpl  extends GenericDmnImpl<DcmTeam, Long> implements DcmTeamDmn {

	@Autowired
	private DcmTeamDAO teamDao;
	@Autowired
	private DcmTeamUserDAO teamUserDao;

	@Override
	public GenericDAO<DcmTeam, Long> getDao() {
		
		return teamDao;
	}

	@Override
	public DcmTeamUser getTeamUserRef(Long teamId, Long userId){
	
		return this.teamUserDao.findUniqueByProperties(new String[]{"teamId","userId"}, new Object[]{teamId, userId});
	}
	@Override
	public void saveOrUpdateTeamUserRef(DcmTeamUser teamUser) {
		this.teamUserDao.saveOrUpdate(teamUser);
	}
	@Override
	public List<DcmTeamUser> getUserRef(Long teamId) {
		
		return this.teamUserDao.findByProperty("teamId", teamId);
	}
	@Override
	public void deleteUsers(Long teamId, Long[] userIds) {
		
		Map<String, Object[]> propertiesValuesMap = new HashMap<String, Object[]>();
		propertiesValuesMap.put("teamId", new Object[]{teamId});
		propertiesValuesMap.put("userId", userIds);
		
		this.teamUserDao.removeByProperties(propertiesValuesMap);
	}
	
	public boolean removeById(Long teamId) {
		// 删除团队与成员关系
		this.teamUserDao.removeByProperty("teamId", teamId);
		// 删除团队信息
		this.teamDao.removeById(teamId);
		return true;

	}
	
	@Override
	public void removeUsers(Long[] userIds) {
		
		this.teamUserDao.removeByProperty("userId", userIds);
	}
	@Override
	public DcmTeam getTeam(Long projectId, Long userId) {
		DcmTeamUser teamUser = this.teamUserDao.findUniqueByProperties(new String[]{"projectId", "userId"}, new Object[]{projectId, userId});
		if(null == teamUser) return null;
		
		return this.teamDao.find(teamUser.getTeamId());
	}
}
