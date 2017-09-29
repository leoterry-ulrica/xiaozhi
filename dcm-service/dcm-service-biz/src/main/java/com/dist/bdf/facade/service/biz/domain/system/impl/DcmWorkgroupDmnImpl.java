package com.dist.bdf.facade.service.biz.domain.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.facade.service.biz.dao.DcmWgOrgDAO;
import com.dist.bdf.facade.service.biz.dao.DcmWorkgroupDAO;
import com.dist.bdf.facade.service.biz.domain.system.DcmWorkgroupDmn;
import com.dist.bdf.model.entity.system.DcmWgOrg;
import com.dist.bdf.model.entity.system.DcmWorkGroup;


@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class DcmWorkgroupDmnImpl  extends GenericDmnImpl<DcmWorkGroup, Long> implements DcmWorkgroupDmn {

	@Autowired
	private DcmWorkgroupDAO workgroupDAO;
	@Autowired
	private DcmWgOrgDAO wgOrgDAO;

	@Override
	public GenericDAO<DcmWorkGroup, Long> getDao() {
		
		return workgroupDAO;
	}
	@Override
	public DcmWgOrg saveOrUpdateOrgToWorkgroup(DcmWgOrg wgOrg) {
		
		return this.wgOrgDAO.saveOrUpdate(wgOrg);
	}
	@Override
	public void deleteOrgFromWorkgroup(Long workgroupId, String orgGuid) {
		
		 this.wgOrgDAO.removeByProperties(new String[]{"wgId", "orgGuid"},  new Object[]{workgroupId, orgGuid}); 
	}
	@Override
	public void deleteWorkgroup(Long workgroupId) {
		this.workgroupDAO.removeById(workgroupId);
	}
	@Override
	public void deleteWorkgroupsByProjectGuid(String projectGuid) {
		List<DcmWorkGroup> workgroups = this.workgroupDAO.findByProperty("projectGuid", projectGuid);
		if(workgroups != null && !workgroups.isEmpty()) {
			for(DcmWorkGroup wg : workgroups) {
				this.wgOrgDAO.removeByProperty("wgId", wg.getId());
				this.workgroupDAO.remove(wg);
			}
		}
	}
	@Override
	public List<DcmWgOrg> getRefOrgs(Long wgId) {
		return this.wgOrgDAO.findByProperty("wgId", wgId);
	}
	@Override
	public void deleteOrgsFromWorkgroup(Long workgroupId) {
		this.wgOrgDAO.removeByProperty("wgId", workgroupId);
	}
}
