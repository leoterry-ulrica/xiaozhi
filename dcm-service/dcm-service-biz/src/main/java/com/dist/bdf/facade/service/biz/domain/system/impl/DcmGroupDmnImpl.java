
package com.dist.bdf.facade.service.biz.domain.system.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.facade.service.biz.dao.DcmGroupDAO;
import com.dist.bdf.facade.service.biz.domain.system.DcmGroupDmn;
import com.dist.bdf.common.constants.DomainType;
import com.dist.bdf.model.entity.system.DcmGroup;


/**
 * @author weifj
 * @version 1.0，2016/02/23，weifj，创建组领域实现
 *
 */
@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class DcmGroupDmnImpl extends GenericDmnImpl<DcmGroup, Long> implements DcmGroupDmn {

	@Autowired
	private DcmGroupDAO groupDao;
	//private GenericDAOImpl<DcmGroup, Long> groupDao;

	@Override
	public GenericDAO<DcmGroup, Long> getDao() {
		
		return groupDao;
	}
	
	@Override
	public boolean deleteGroupByCode(String groupCode) {
		
		this.groupDao.removeByProperty("groupCode", groupCode);
		
		return true;
	}

	@Override
	public boolean deleteGroupByGuid(String groupGuid) {
		
        this.groupDao.removeByProperty("guid", groupGuid);
		
		return true;
	}
	@Override
	public DcmGroup getGroupById(Long id) {
		
		return super.findById(id);
	}

	@Override
	public DcmGroup getGroupByCode(String code) {
		
		return super.findUniqueByProperty("groupCode", code);
	}
	@Override
	public DcmGroup getGroupByGuid(String guid) {

		return super.findUniqueByProperty("guid", guid);
		
	}
	@Override
	public List<DcmGroup> listProjectGroups() {
		
		return this.getDao().findByProperty("domainType", DomainType.PROJECT);
	}

	@Override
	public List<DcmGroup> listDiscussionGroups() {
		
		return this.getDao().findByProperty("domainType", DomainType.DISCUSSION);
	}
	@Override
	public DcmGroup updateGroup(String code, String name){
		
		DcmGroup group = this.getDao().findUniqueByProperty("groupCode", code);
		if(null == group){
			throw new BusinessException(String.format("组编码为【%s】的对象不存在......", code));
		}
		group.setGroupName(name);
		group.setModifiedTime(new Date());
		
		return this.getDao().update(group);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DcmGroup> searchBySQL(String queryString) {
		
	    SQLQuery sqlquery = this.groupDao.createSQLQuery(queryString); // 
	    sqlquery.addEntity(DcmGroup.class);
		return sqlquery.list();
	}
	@Override
	public List<DcmGroup> getGroupByRealm(String realm) {
		return this.groupDao.findByProperty("realm", realm);
	}
	@Override
	public Pagination getGroups(int pageNo, int pageSize, String realm, String[] typePrefix) {
		Map<String, Object[]> equalProperties = new HashMap<String, Object[]>();
		equalProperties.put("realm", new String[]{realm});
		Map<String, Object[]> likeProperties = new HashMap<String, Object[]>();
		likeProperties.put("groupCode", typePrefix);
		
		return this.groupDao.findByProperties(pageNo, pageSize, equalProperties, likeProperties, "createTime", false);
	}
}
