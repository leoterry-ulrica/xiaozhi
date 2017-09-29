package com.dist.bdf.facade.service.biz.domain.system.impl;


import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.domain.GenericDmnImpl;
import com.dist.bdf.facade.service.biz.dao.DcmRegionDAO;
import com.dist.bdf.facade.service.biz.domain.system.DcmRegionDmn;
import com.dist.bdf.model.entity.system.DcmRegion;


@Service
@Transactional(propagation = Propagation.REQUIRED) 
public class DcmRegionDmnImpl  extends GenericDmnImpl<DcmRegion, Long> implements DcmRegionDmn {

	@Autowired
	private DcmRegionDAO regionDao;
	//private GenericDAOImpl<DcmRegion, Long> regionDao;
	
	@Override
	public GenericDAO<DcmRegion, Long> getDao() {
		
		return regionDao;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getProvinces() {
		
		SQLQuery query = this.getDao().createSQLQuery("SELECT DISTINCT province From DCM_Region ORDER BY NLSSORT(province, 'NLS_SORT=SCHINESE_PINYIN_M') ", new Object[]{});
		List<String> list = query.list();
		
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getCities(String provinceName) {
		
		SQLQuery query = this.getDao().createSQLQuery("SELECT DISTINCT city From DCM_Region where province='"+provinceName+"' ORDER BY NLSSORT(city, 'NLS_SORT=SCHINESE_PINYIN_M')", new Object[]{});
		List<String> list = query.list();
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getCounties(String provinceName, String cityName) {
		
		SQLQuery query = this.getDao().createSQLQuery("SELECT DISTINCT county From DCM_Region where province='"+provinceName+"' and city='"+cityName+"' ORDER BY NLSSORT(county, 'NLS_SORT=SCHINESE_PINYIN_M')", new Object[]{});
		List<String> list = query.list();
		
		return list;
	}
	
}
