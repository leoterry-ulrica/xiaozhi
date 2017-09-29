
package com.dist.bdf.facade.service.biz.domain.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.facade.service.biz.dao.DcmPrivilegeDAO;
import com.dist.bdf.facade.service.biz.dao.DcmPrivtemplateDAO;
import com.dist.bdf.facade.service.biz.domain.system.DcmPrivTemplateDmn;
import com.dist.bdf.manager.ecm.security.PrivilegeFactory;
import com.dist.bdf.model.entity.system.DcmPrivilege;
import com.dist.bdf.model.entity.system.DcmPrivtemplate;
import com.dist.bdf.model.entity.system.DcmRole;


/**
 * @author weifj
 * @version 1.0，2016/03/14，weifj，创建权限模板领域
 *
 */
@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class DcmPrivTemplateDmnImpl extends GenericDmnImpl<DcmPrivtemplate, Long>implements DcmPrivTemplateDmn{

	@Autowired
	private DcmPrivtemplateDAO privTemplateDao;
	//private GenericDAOImpl<DcmPrivtemplate, Long> privTemplateDao;
	@Autowired
	private DcmPrivilegeDAO privDao;
	//private GenericDAOImpl<DcmPrivilege, Long> privDao;
	
	@Override
	public GenericDAO<DcmPrivtemplate, Long> getDao() {
	
		return this.privTemplateDao;
	}

	@Override
	public Object addPrivTempArchive(DcmPrivtemplate privTemp) {
	
		privTemp.setResTypeStatus(0L);
		return this.getDao().save(privTemp);
	}

	@Override
	public Object addPrivTempActive(DcmPrivtemplate privTemp) {
		
		privTemp.setResTypeStatus(1L);
		return this.getDao().save(privTemp);
	}

	@Override
	public Object updatePrivTemp(DcmPrivtemplate privTemp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object deletePrivTemp(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DcmPrivilege> getPrivilegeList(String resTypeCode, long resTypeStatus, String roleCode) {
		
		List<DcmPrivilege> list = new ArrayList<DcmPrivilege>();// 此处不适合使用LinkedList，尽管链表存的效率高，但读效率很低
		List<DcmPrivtemplate> templates = this.privTemplateDao.findByProperties(new String[]{"resTypeCode","resTypeStatus","roleCode"}, new Object[]{resTypeCode, resTypeStatus,roleCode});
		if(templates.isEmpty()) return list;
		
		for(DcmPrivtemplate pt : templates){
			list.add(privDao.findUniqueByProperty("privCode", pt.getPrivCode()));
		}
		return list;
	}
	@Override
	public Long getPrivilegeMasks(String resTypeCode, long resTypeStatus, String roleCode){
		
		long masks = PrivilegeFactory.Priv_None.getMask();
		
		List<DcmPrivtemplate> templates = this.privTemplateDao.findByProperties(new String[]{"resTypeCode","resTypeStatus","roleCode"}, new Object[]{resTypeCode, resTypeStatus,roleCode});
		if(templates.isEmpty()) return masks;
		
		for(DcmPrivtemplate pt : templates) {
			masks |= this.privDao.findUniqueByProperty("privCode", pt.getPrivCode()).getPrivValue();
		}
		return masks;
		
	}
	@Override
	public List<DcmPrivtemplate> getPrivileges(String realm, String resTypeCode, long resTypeStatus, String roleCode) {
		
		return this.privTemplateDao.findByProperties(new String[]{"resTypeCode","resTypeStatus","roleCode","realm"}, new Object[]{resTypeCode, resTypeStatus,roleCode,realm});
		
	}
	@Override
	public Long getPrivilegeMasks(String resTypeCode, long resTypeStatus, List<DcmRole> roles) {
		
		long masks = PrivilegeFactory.Priv_None.getMask();
		
		for(DcmRole role : roles){
			masks |= this.getPrivilegeMasks(resTypeCode, resTypeStatus, role.getRoleCode());
		}
		
		return masks;
	}
	
	@Override
	public List<String> getPrivilegeCodes(String resTypeCode, long resTypeStatus, String roleCode) {
		
		List<String> privCodes = new ArrayList<String>();
		List<DcmPrivtemplate> templates = this.privTemplateDao.findByProperties(new String[]{"resTypeCode","resTypeStatus","roleCode"}, new Object[]{resTypeCode, resTypeStatus,roleCode});
		if(templates.isEmpty()) return privCodes;
		
		for(DcmPrivtemplate pt : templates){
			
			if(privCodes.contains(pt.getPrivCode())) continue;
			
			privCodes.add(pt.getPrivCode());
		}
		
		return privCodes;
	}
	@Override
	public List<DcmPrivtemplate> getPrivilegeCodes(String realm, String[] resTypeCodes, long resTypeStatus, String roleCode) {

		Map<String, Object[]> data = new HashMap<String, Object[]>();
		data.put("realm", new String[]{realm});
		data.put("resTypeCode", resTypeCodes);
		data.put("resTypeStatus", new Object[]{resTypeStatus});
		data.put("roleCode", new String[]{roleCode});
		return this.privTemplateDao.findByProperties(data);
	}
}
