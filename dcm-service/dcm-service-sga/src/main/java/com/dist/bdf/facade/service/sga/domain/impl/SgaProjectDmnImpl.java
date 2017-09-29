package com.dist.bdf.facade.service.sga.domain.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.facade.service.sga.dao.SgaPrjDetailDAO;
import com.dist.bdf.facade.service.sga.dao.SgaPrjUserDAO;
import com.dist.bdf.facade.service.sga.dao.SgaPrjWzDAO;
import com.dist.bdf.facade.service.sga.dao.SgaProjectDAO;
import com.dist.bdf.facade.service.sga.domain.SgaProjectDmn;
import com.dist.bdf.model.entity.sga.SgaPrjDetail;
import com.dist.bdf.model.entity.sga.SgaPrjUser;
import com.dist.bdf.model.entity.sga.SgaPrjWz;
import com.dist.bdf.model.entity.sga.SgaProject;


@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class SgaProjectDmnImpl extends GenericDmnImpl<SgaProject, Long> implements SgaProjectDmn {

	@Autowired
	private SgaProjectDAO prjDAO;
	@Autowired
	private SgaPrjDetailDAO prjDetailDAO;
	@Autowired
	private SgaPrjUserDAO prjUserDAO;
	@Autowired
	private SgaPrjWzDAO prjWzDAO;
	
	@Override
	public GenericDAO<SgaProject, Long> getDao() {
		
		return prjDAO;
	}
	@Override
	public SgaPrjDetail getDesc(Long pid) {
		
		return this.prjDetailDAO.findUniqueByProperty("pid", pid);
		
	}
	@Override
	public Object saveOrUpdatePrjDetail(SgaPrjDetail prjDetail) {
		
		return this.prjDetailDAO.saveOrUpdate(prjDetail);
	}
	@Override
	public List<SgaPrjUser> getPrjRefUsers(String caseId, Integer[]status) {
		
		SgaProject project = this.prjDAO.findUniqueByProperty("sysCode", caseId);
		if(null == project) return null;
		
		Map<String, Object[]> propMap = new HashMap<String, Object[]>();
		propMap.put("pid", new Object[]{project.getId()});
		propMap.put("status", status);
		
		return this.prjUserDAO.findByProperties(propMap);
		
	}
	@Override
	public SgaPrjUser getPrjRefUser(String caseId, Long userId) {
		
		SgaProject project = this.prjDAO.findUniqueByProperty("sysCode", caseId);
		if(null == project) return null;
		
		return this.prjUserDAO.findUniqueByProperties(new String[]{"pid","userId"}, new Object[]{project.getId(), userId});
	}
	@Override
	public void saveOrUpdate(SgaPrjUser pu) {
		
		this.prjUserDAO.saveOrUpdate(pu);
	}
	@Override
	public void addPrj2WZ(SgaPrjWz wz) {
	
		this.prjWzDAO.saveOrUpdate(wz);
	}
	@Override
	public void deleteRecordByWzId(String wzId) {
		
		this.prjWzDAO.removeByProperty("wzId", wzId);
	}
	@Override
	public List<SgaPrjUser> getProjectIdsByUserId(Long userId) {
		
		return this.prjUserDAO.findByProperty("userId", userId);
		
	}
	@Override
	public boolean existUser(Long pid, Long userId) {
		
		SgaPrjUser pu = this.prjUserDAO.findUniqueByProperties(new String[]{"pid","userId"}, new Object[]{pid, userId});
		return null != pu;
		
	}
	@Override
	public List<SgaPrjUser> getPrjRefUsers(Long projectId) {
		
		return this.prjUserDAO.findByProperty("pid", projectId);
	}
	@Override
	public SgaPrjWz getPrjRefWz(String wzId) {
		return this.prjWzDAO.findUniqueByProperty("wzId", wzId);
	}
	@Override
	public void deleteProjectByCode(String caseId) {
		
		SgaProject project = this.prjDAO.findUniqueByProperty("sysCode", caseId);
		if(null == project) {
			return;
		}
		this.prjUserDAO.removeByProperty("pid", project.getId());
		this.prjDetailDAO.removeByProperty("pid", project.getId());
		this.prjWzDAO.removeByProperty("pid", project.getId());
		this.prjDAO.removeByProperty("sysCode", caseId);
	}
}
