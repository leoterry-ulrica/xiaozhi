package com.dist.bdf.base.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.hql.internal.ast.QuerySyntaxException;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.internal.CriteriaImpl.OrderEntry;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.dist.bdf.base.dao.Finder;
import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.dao.Sort;
import com.dist.bdf.base.dao.Updater;
import com.dist.bdf.base.dto.DateFilterRangeDTO;
import com.dist.bdf.base.page.Pagination;
import com.dist.bdf.base.utils.BeanUtils;


/**
 * @ClassName: GenericDAOImpl.java
 * @Description: TODO hibernate DAO基类 提供hql分页查询，拷贝更新等一些常用功能
 * 
 * @author 李其云
 * @param <T>
 *            表示对象的类型，实现该接口的时候，需要提供数据访问的对象
 * @param <ID>
 *            数据访问对象的id,这个实例使用。不同的域对象的id是用于此实例
 * @date: 2013-7-19 上午10:17:51
 * @version 1.0
 */
//@Repository
public class GenericDAOImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {

	/**
	 * HIBERNATE 的 order 属性
	 */
	public static final String ORDER_ENTRIES = "orderEntries";

	/**
	 * 日志，可用于子类
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 获得Dao对应的实体类
	 * 
	 * @return class
	 */
	// abstract protected Class<T> getEntityClass();
	// 使用反射获取实体类的class，可以避免继承本类时还需要实现本方法。
	@SuppressWarnings("unchecked")
	protected Class<T> getEntityClass() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * @param id
	 * @return 持久化对象
	 */
	public T get(ID id) {
		return get(id, false);
	}

	/**
	 * @param id
	 *            对象ID
	 * @param lock
	 *            是否锁定，使用LockMode.UPGRADE
	 * @return 持久化对象
	 */
	@SuppressWarnings("unchecked")
	protected T get(ID id, boolean lock) {
		T entity;
		if (lock) {
			entity = (T) getSession().get(getEntityClass(), id, LockOptions.UPGRADE); // LockMode.UPGRADE_NOWAIT);
		} else {
			entity = (T) getSession().get(getEntityClass(), id);
		}
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dgg.common.dao.GenericDAO#find()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> find() {
		logger.debug("find() start>>>>");

		List<T> list = createCriteria().list();

		// logger.debug("<<<<find() end.return=[{}]", list);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> notFindByProperty(String propertyName, Object propertyValue) {
		
		Criteria criteria = this.createCriteria();
		criteria.add(Restrictions.not(Restrictions.eqOrIsNull(propertyName, propertyValue)));

		return criteria.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dgg.common.dao.GenericDAO#find(int, int)
	 */
	@Override
	public Pagination find(int pageNo, int pageSize) {
		logger.debug("find(int pageNo=[{}], int pageSize=[{}]) start>>>>", pageNo, pageSize);

		Criteria criteria = createCriteria();
		Pagination pagination = findByCriteria(pageNo, pageSize, criteria);

		// logger.debug("<<<<find() end.return=[{}]", pagination);
		return pagination;
	}

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.dgg.common.dao.GenericDAO#find(java.lang.String, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> find(String sortPropertyName, boolean isAscending) {
		logger.debug("find(String sortPropertyName=[{}], boolean isAscending=[{}]) start>>>>", sortPropertyName,
				isAscending);

		Criteria criteria = createCriteria();
		if(!StringUtils.isEmpty(sortPropertyName)) {
			criteria.addOrder(isAscending ? Order.asc(sortPropertyName) : Order.desc(sortPropertyName));
		}
		List<T> entities = criteria.list();

		logger.debug("<<<<find() end.return=[{}]", entities);
		return entities;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dgg.common.dao.GenericDAO#find(int, int, java.lang.String,
	 * boolean)
	 */
	@Override
	public Pagination find(int pageNo, int pageSize, String sortPropertyName, boolean isAscending) {
		logger.debug(
				"find(int pageNo=[{}],"
						+ " int pageSize=[{}], String sortPropertyName=[{}], boolean isAscending=[{}]) start>>>>",
				pageNo, pageSize, sortPropertyName, isAscending);

		Criteria criteria = createCriteria();
		if(!StringUtils.isEmpty(sortPropertyName)) {
			criteria.addOrder(isAscending ? Order.asc(sortPropertyName) : Order.desc(sortPropertyName));
		}
		Pagination pagination = findByCriteria(pageNo, pageSize, criteria);

		logger.debug("<<<<find() end.return=[{}]", pagination);
		return pagination;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dgg.common.dao.GenericDAO#find(int, int, java.util.List)
	 */
	@Override
	public Pagination find(int pageNo, int pageSize, List<Sort> sorts) {
		logger.debug("find(int pageNo=[{}]," + " int pageSize=[{}], List<Sort> sorts=[{}]) start>>>>", pageNo, pageSize,
				sorts);

		Criteria criteria = createCriteria();
		if (sorts != null) {
			Order order = null;
			Sort sort = null;
			for (int i = 0; i < sorts.size(); i++) {
				sort = sorts.get(i);
				if (Sort.ASC.equals(sort.getOrderPropertyWay())) {
					order = Order.asc(sort.getOrderPropertyName());
				} else {
					order = Order.desc(sort.getOrderPropertyName());
				}
				criteria.addOrder(order);
			}
		}
		Pagination pagination = findByCriteria(pageNo, pageSize, criteria);
		logger.debug("<<<<find() end.return=[{}]", pagination);
		return pagination;
	}
	
	@Override
	public List<T> find(List<Sort> sorts) {
		
		logger.debug("find(List<Sort> sorts=[{}]) start>>>>",
				sorts);

		Criteria criteria = createCriteria();
		if (sorts != null) {
			Order order = null;
			Sort sort = null;
			for (int i = 0; i < sorts.size(); i++) {
				sort = sorts.get(i);
				if (Sort.ASC.equals(sort.getOrderPropertyWay())) {
					order = Order.asc(sort.getOrderPropertyName());
				} else {
					order = Order.desc(sort.getOrderPropertyName());
				}
				criteria.addOrder(order);
			}
		}
		return criteria.list();

	}
	@Override
	public Object findByProperties(String[] properties, Object[] values, List<Sort> sorts) {
		
		logger.debug("find(List<Sort> sorts=[{}]) start>>>>",
				sorts);

		Criteria criteria = createCriteria();
		if (sorts != null) {
			Order order = null;
			Sort sort = null;
			for (int i = 0; i < sorts.size(); i++) {
				sort = sorts.get(i);
				if (Sort.ASC.equals(sort.getOrderPropertyWay())) {
					order = Order.asc(sort.getOrderPropertyName());
				} else {
					order = Order.desc(sort.getOrderPropertyName());
				}
				criteria.addOrder(order);
			}
		}
		if(properties != null && properties.length > 0){
			for(int i=0; i< properties.length;i++){
				criteria.add(Restrictions.eq(properties[i], values[i]));
			}
		}
		return criteria.list();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dgg.common.dao.GenericDAO#find(java.io.Serializable)
	 */
	@Override
	public T find(ID id) {
		logger.debug("find(id=[{}]) start>>>>", id);

		T entity = get(id);

		logger.debug("<<<<find() end.return=[{}]", entity);
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dgg.common.dao.GenericDAO#find(ID[])
	 * 
	 * @param ids 多个id（可变长参数或数组）
	 * 
	 * @return 实体数组
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> find(ID... ids) {
		logger.debug("find({}ids=[{}]) start>>>>", "", ids);
		List<T> entities = new ArrayList<T>();

		if (ids.length > 0) {
			// 查询多个id所指定的实体。
			// TODO以下一句可能存在问题：如果实体的主键名称不是id或者是没有主键，Restrictions.in("id",
			// ids)可能会出错，需要验证一下
			Criteria c = createCriteria().add(Restrictions.in("id", ids));

			for (Object entity : c.list()) {
				Serializable id = getSession().getIdentifier(entity);
				for (int i = 0; i < ids.length; i++) {
					if (id.equals(ids[i])) {
						entities.add((T) entity);
						break;
					}
				}
			}
		}
		logger.debug("<<<<find() end.return=[{}]", entities);
		return entities;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByProperty(String propertyName, Object propertyValue) {
		logger.debug(">>>>start List<T> findByProperty(String propertyName=[{}], propertyValue value=[{}])",
				propertyName, propertyValue);

		Assert.hasText(propertyName);
		Assert.notNull(propertyValue);
		List<T> list = createCriteria(Restrictions.eq(propertyName, propertyValue)).list();

		logger.debug("<<<<end.List<T> findByProperty() return=[{}]", list);
		return list;
	}
	
	@Override
	public List<T> findByPropertyIgnoreCase(String propertyName, Object propertyValue) {
		logger.debug(">>>>start List<T> findByProperty(String propertyName=[{}], propertyValue value=[{}])",
				propertyName, propertyValue);

		Assert.hasText(propertyName);
		Assert.notNull(propertyValue);
		List<T> list = createCriteria(Restrictions.eq(propertyName, propertyValue).ignoreCase()).list();

		logger.debug("<<<<end.List<T> findByProperty() return=[{}]", list);
		return list;
	}

	@Override
	public List<T> findByProperties(String[] propertyNames, Object[] propertyValues) {
		logger.debug(">>>>start List<T> findByProperties(String propertyNames=[{}], propertyValues value=[{}])",
				propertyNames, propertyValues);

		Assert.notEmpty(propertyNames);
		Assert.notEmpty(propertyValues);
		Criterion[] criterions = new Criterion[propertyNames.length];
		for (int i = 0; i < propertyNames.length; i++) {
			criterions[i] = Restrictions.eq(propertyNames[i], propertyValues[i]);
		}
		@SuppressWarnings("unchecked")
		List<T> list = createCriteria(criterions).list();

		logger.debug("<<<<end.List<T> findByProperties() return=[{}]", list);
		return list;
	}
	
	@Override
	public List<T> findByProperties(String[] propertyNames, Object[] propertyValues, String sortPropertyName,
			boolean isAscending) {
		logger.debug(">>>>start List<T> findByProperties(String propertyNames=[{}], propertyValues value=[{}],sortPropertyName=[{}],isAscending=[{}])",
				propertyNames, propertyValues,sortPropertyName,isAscending);

		Assert.notEmpty(propertyNames);
		Assert.notEmpty(propertyValues);
		Criterion[] criterions = new Criterion[propertyNames.length];
		for (int i = 0; i < propertyNames.length; i++) {
			criterions[i] = Restrictions.eq(propertyNames[i], propertyValues[i]);
		}
		
		Criteria  criteria = createCriteria(criterions);
		if(!StringUtils.isEmpty(sortPropertyName)) {
			criteria.addOrder(isAscending ? Order.asc(sortPropertyName) : Order.desc(sortPropertyName));
		}
		@SuppressWarnings("unchecked")
		List<T> list = criteria.list();
		logger.debug("<<<<end.List<T> findByProperties() return=[{}]", list);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByProperty(String propertyName, Object propertyValue, String sortPropertyName,
			boolean isAscending) {
		logger.debug(
				">>>>List<T> findByProperty(String propertyName=[{}], propertyValue value=[{}], "
						+ "String sortPropertyName=[{}], boolean isAscending value=[{}]) start",
				propertyName, propertyValue, sortPropertyName, isAscending);

		Assert.hasText(propertyName);
		Assert.notNull(propertyValue);
		Criteria criteria = createCriteria(Restrictions.eq(propertyName, propertyValue));
		if(!StringUtils.isEmpty(sortPropertyName)) {
			criteria.addOrder(isAscending ? Order.asc(sortPropertyName) : Order.desc(sortPropertyName));
		}
		List<T> list = criteria.list();

		logger.debug("<<<<end.List<T> findByProperty() return=[{}]", list);
		return list;
	}

	@Override
	public Pagination findByProperty(int pageNo, int pageSize, String propertyName, Object propertyValue) {
		logger.debug(
				">>>>Pagination findByProperty(int pageNo=[{}],"
						+ " int pageSize=[{}], String property=[{}], Object value[{}]) start",
				pageNo, pageSize, propertyName, propertyValue);

		Assert.hasText(propertyName);
		Assert.notNull(propertyValue);
		Pagination pagination = findByCriteria(pageNo, pageSize,
				createCriteria(Restrictions.eq(propertyName, propertyValue)));

		logger.debug("<<<<end.Pagination findByProperty() return=[{}]", pagination);
		return pagination;
	}
	
	private Criteria createCriteria(Map<String, Object[]> equalProperties, Map<String, Object[]> likeProperties){
		
		Criteria criteria = this.createCriteria();

		Disjunction  dis = Restrictions.disjunction();
		for(Map.Entry<String, Object[]> entry : likeProperties.entrySet()){
			if(null == entry.getValue() || 0 == entry.getValue().length){
				continue;
			}
			for(Object val : entry.getValue()){	
				dis.add(Restrictions.like(entry.getKey(), val.toString(), MatchMode.ANYWHERE));
			}
		}
		criteria.add(dis);
		
		Set<String> keyset = equalProperties.keySet();
		for(String key : keyset) {
			
			if(null == equalProperties.get(key) || 0 == equalProperties.get(key).length){
				criteria.add(Restrictions.eq(key, "NULL_"+System.currentTimeMillis()));
			} else{
				criteria.add(Restrictions.in(key, equalProperties.get(key)));
			}

		}
		return criteria;
	}
	/**
	 * 
	 * @param equalProperties 精准匹配这些属性值
	 * @param likeProperties 模糊检索这些属性值
	 * @param notProperties 排除这些属性值
	 * @return
	 */
   private Criteria createCriteria(Map<String, Object[]> equalProperties, Map<String, Object[]> likeProperties, Map<String, Object[]> notProperties){
		
		Criteria criteria = this.createCriteria();

		Disjunction  dis = Restrictions.disjunction();
		if(likeProperties != null && !likeProperties.isEmpty()) {
			for(Entry<String, Object[]> entry : likeProperties.entrySet()){
				if(null == entry.getValue() || 0 == entry.getValue().length){
					continue;
				}
				for(Object val : entry.getValue()){	
					dis.add(Restrictions.like(entry.getKey(), val.toString(), MatchMode.ANYWHERE));
				}
			}
			criteria.add(dis);
		}
		if(equalProperties != null && !equalProperties.isEmpty()) {
			for(Entry<String, Object[]> entry : equalProperties.entrySet()) {		
				if(null == entry.getValue() || 0 == entry.getValue().length){
					continue;
				}
				criteria.add(Restrictions.in(entry.getKey(), equalProperties.get(entry.getKey())));
			}
		}
		if(notProperties != null && !notProperties.isEmpty()) {
			for(Entry<String, Object[]> entry : notProperties.entrySet())  {
				criteria.add(Restrictions.not(Restrictions.in(entry.getKey(), notProperties.get(entry.getKey()))));
			}
		}
		return criteria;
	}
	@Override
	public Pagination findByProperties(int pageNo, int pageSize, Map<String, Object[]> equalProperties, Map<String, Object[]> likeProperties, String orderPropertyName, Boolean isAscending) {

		Assert.notNull(equalProperties);
		Assert.notNull(likeProperties);
		
		Criteria criteria = this.createCriteria(equalProperties, likeProperties);
		if(!StringUtils.isEmpty(orderPropertyName)) {
			criteria.addOrder(isAscending ? Order.asc(orderPropertyName) : Order.desc(orderPropertyName));
		}
		Pagination pagination = findByCriteria(pageNo, pageSize, criteria);

		logger.debug("<<<<end.Pagination findByProperty() return=[{}]", pagination);
		return pagination;	
	}
	@Override
	public Pagination findByProperties(int pageNo, int pageSize, Map<String, Object[]> equalProperties, Map<String, Object[]> likeProperties, Map<String, Object[]> notProperties, String orderPropertyName, boolean isAscending) {
		
		Assert.notNull(equalProperties);
		Assert.notNull(likeProperties);
		Assert.notNull(notProperties);
		
		Criteria criteria = this.createCriteria(equalProperties, likeProperties, notProperties);

		Pagination pagination = findByCriteria(pageNo, pageSize, criteria.addOrder(isAscending ? Order.asc(orderPropertyName) : Order.desc(orderPropertyName)));

		logger.debug("<<<<end.Pagination findByProperty() return=[{}]", pagination);
		return pagination;
	}
	
	@Override
	public Pagination findByProperties(int pageNo, int pageSize, Map<String, Object> equalProperties, Map<String, Object> orProperties, List<Sort> sorts) {


		Assert.notNull(equalProperties);
		Assert.notNull(orProperties);
		
		Criteria criteria = this.createCriteria();
	
		Disjunction  orRestri = Restrictions.disjunction();
		Conjunction andRestri = Restrictions.conjunction();
		
		for(Map.Entry<String, Object> entry : orProperties.entrySet()){
				
			orRestri.add(Restrictions.eq(entry.getKey(), entry.getValue()));
	        //list.add(Restrictions.like(entry.getKey(), entry.getValue().toString(), MatchMode.ANYWHERE));
	        	//list.add(Restrictions.like(entry.getKey(), "%"+entry.getValue()+"%"));
	        	//orLike[i] = Restrictions.like(entry.getKey(), entry.getValue().toString()	, MatchMode.ANYWHERE);
	        	//i++;
	        
		}
		//Criterion[] orLike = new Criterion[likeProperties.size()];
		//list.toArray(orLike);
		//
		
		for(Map.Entry<String, Object> entry : equalProperties.entrySet()){
			criteria.add(Restrictions.eq(entry.getKey(), entry.getValue()));
		}
		orRestri.add(andRestri);
		criteria.add(orRestri);
		
		if(sorts != null && !sorts.isEmpty()){
			Order order = null;
			for(Sort s : sorts){
				
				if (Sort.ASC.equals(s.getOrderPropertyWay())) {
					order = Order.asc(s.getOrderPropertyName());
				} else {
					order = Order.desc(s.getOrderPropertyName());
				}
				criteria.addOrder(order);
			}
		}
		
		Pagination pagination = findByCriteria(pageNo, pageSize, criteria);

		logger.debug("<<<<end.Pagination findByProperty() return=[{}]", pagination);
		return pagination;
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByPropertiesStartMatch(String[] properties, Object[] values) {

		Assert.notNull(properties);
		Assert.notNull(values);

		Criteria criteria = this.createCriteria();

		Disjunction dis = Restrictions.disjunction();
		int i = 0;
		for (String p : properties) {
			dis.add(Restrictions.like(p, String.valueOf(values[i]), MatchMode.START));
			i++;
		}

		criteria.add(dis);

		return criteria.list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByPropertiesStartMatch(Map<String, Object[]> propertiesValuesMap) {

		Assert.notNull(propertiesValuesMap);
		
		Criteria criteria = this.createCriteria();

		Disjunction dis = Restrictions.disjunction();

		for (Entry<String, Object[]> p : propertiesValuesMap.entrySet()) {
			if(null == p.getValue() || 0 == p.getValue().length) {
				continue;
			}
			for(Object v : p.getValue()) {
				dis.add(Restrictions.like(p.getKey(), String.valueOf(v), MatchMode.START));
			}
		}

		criteria.add(dis);
		return criteria.list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByPropertiesMatch(String[] properties, Object[] values) {

		Assert.notNull(properties);
		Assert.notNull(values);

		Criteria criteria = this.createCriteria();

		Disjunction dis = Restrictions.disjunction();
		int i = 0;
		for (String p : properties) {

			dis.add(Restrictions.like(p, values[i].toString(), MatchMode.ANYWHERE));
			i++;
		}

		criteria.add(dis);

		return criteria.list();
	}
	@Override
	public Pagination findByPropertiesMatch(int pageNo, int pageSize, String[] properties, Object[] values) {

		Assert.notNull(properties);
		Assert.notNull(values);

		Criteria criteria = this.createCriteria();

		Disjunction dis = Restrictions.disjunction();
		int i = 0;
		for (String p : properties) {

			dis.add(Restrictions.like(p, values[i].toString(), MatchMode.ANYWHERE));
			i++;
		}
		criteria.add(dis);
		Pagination pagination = findByCriteria(pageNo, pageSize, criteria);
		return pagination;
	}
	@Override
	public Pagination findByProperty(int pageNo, int pageSize, Map<String, Object[]> propertiesValuesMap, String orderPropertyName, boolean isAscending ) {
		
		Criteria criteria = this.createCriteria(propertiesValuesMap);
		if(!StringUtils.isEmpty(orderPropertyName)) {
			criteria.addOrder(isAscending ? Order.asc(orderPropertyName) : Order.desc(orderPropertyName));
		}
		Pagination pagination = findByCriteria(pageNo, pageSize, criteria);

		logger.debug("<<<<end.Pagination findByProperty() return=[{}]", pagination);
		return pagination;
		
	}
	@Override
	public Pagination findByPropertiesAndValues(int pageNo, int pageSize, Map<String, Object[]> propertiesValues,
			String datePropertyName, Date beginTime, Date endTime, String sortPropertyName, boolean isAscending) {
		
	    Criteria criteria = this.createCriteria(propertiesValues);
	    if(beginTime != null) {
	    	criteria.add(Restrictions.ge(datePropertyName, beginTime));  
	    }
	    if(endTime != null){
	    	criteria.add(Restrictions.le(datePropertyName, endTime));  
	    }
		if(!StringUtils.isEmpty(sortPropertyName)) {
			criteria.addOrder(isAscending ? Order.asc(sortPropertyName) : Order.desc(sortPropertyName));
		}
	    //criteria.add(Restrictions.between(datePropertyName, beginTime,  endTime));
		Pagination pagination = findByCriteria(pageNo, pageSize, criteria);

		return pagination;
	}
	
	@Override
	public Pagination findByPropertiesAndValues(int pageNo, int pageSize, Map<String, Object[]> propertiesValues,
			List<DateFilterRangeDTO> datePropertyFilters, String sortPropertyName, boolean isAscending) {
		
	    Criteria criteria = this.createCriteria(propertiesValues);
	    if(datePropertyFilters != null && !datePropertyFilters.isEmpty()) {
	    	for(DateFilterRangeDTO dateFilter : datePropertyFilters){
	    		   if(dateFilter.getBeginTime() != null) {
	    		    	criteria.add(Restrictions.ge(dateFilter.getPropertyName(), dateFilter.getBeginTime()));  
	    		    }
	    		    if(dateFilter.getEndTime() != null){
	    		    	criteria.add(Restrictions.le(dateFilter.getPropertyName(), dateFilter.getEndTime()));  
	    		    }
	    	}
	    }
	 
		if(!StringUtils.isEmpty(sortPropertyName)) {
			criteria.addOrder(isAscending ? Order.asc(sortPropertyName) : Order.desc(sortPropertyName));
		}
	    //criteria.add(Restrictions.between(datePropertyName, beginTime,  endTime));
		Pagination pagination = findByCriteria(pageNo, pageSize, criteria);

		return pagination;
	}

	@Override
	public Pagination findByProperty(int pageNo, int pageSize, String propertyName, Object propertyValue,
			String sortPropertyName, boolean isAscending) {
		logger.debug(
				">>>>Pagination findByProperty(int pageNo=[{}], int pageSize=[{}], String property=[{}], "
						+ "Object value[{}, String sortPropertyName=[{}]], boolean isAscending=[{}]) start",
				pageNo, pageSize, propertyName, propertyValue, sortPropertyName, isAscending);

		Assert.hasText(propertyName);
		Assert.notNull(propertyValue);
		Criteria criteria = createCriteria(Restrictions.eq(propertyName, propertyValue));
		if(!StringUtils.isEmpty(sortPropertyName)) {
			criteria.addOrder(isAscending ? Order.asc(sortPropertyName) : Order.desc(sortPropertyName));
		}
		Pagination pagination = findByCriteria(pageNo, pageSize, criteria);

		logger.debug("<<<<end.Pagination findByProperty() return=[{}]", pagination);
		return pagination;
	}
	
	@Override
	public Pagination findByPropertiesAndValues(int pageNo, int pageSize, Map<String, Object[]> propertiesValuesMap,
			String sortPropertyName, boolean isAscending) {
		
		Assert.notEmpty(propertiesValuesMap);
		Criteria criteria = this.createCriteria(propertiesValuesMap);
		if(!StringUtils.isEmpty(sortPropertyName)) {
			criteria.addOrder(isAscending ? Order.asc(sortPropertyName) : Order.desc(sortPropertyName));
		}
		Pagination pagination = findByCriteria(pageNo, pageSize, criteria);

		return pagination;
	}

	/*
	 * 实现方法具有性能问题，实际上应该使用findByCriteria(int, int,
	 * Criteria)的方法，只不过把setFirstResult(0)、setMaxResults(1)即可;
	 */
	@Override
	public T findUniqueByProperty(String propertyName, Object propertyValue) {

		logger.debug(">>>>start: int findUniqueByProperty(String property=[{}], Object value[{}])", propertyName,
				propertyValue);

		T entity = null;

		List<T> list = findByProperty(propertyName, propertyValue);
		if (list.size() >= 1) {
			entity = list.get(0);
		}

		logger.debug("<<<<end.T findUniqueByProperty() return=[{}]", entity);
		return entity;
	}
	
	@Override
	public T findUniqueByPropertyIgnoreCase(String propertyName, Object propertyValue) {
		
		logger.debug(">>>>start: int findUniqueByProperty(String property=[{}], Object value[{}])", propertyName,
				propertyValue);

		T entity = null;

		List<T> list = findByPropertyIgnoreCase(propertyName, propertyValue);
		if (list.size() >= 1) {
			entity = list.get(0);
		}

		logger.debug("<<<<end.T findUniqueByProperty() return=[{}]", entity);
		return entity;
		
	}

	@Override
	public T findUniqueByProperties(String[] propertyNames, Object[] propertyValues) {
		logger.debug(">>>>start: int findUniqueByProperty(String property=[{}], Object value[{}])", propertyNames,
				propertyValues);

		T entity = null;

		List<T> list = findByProperties(propertyNames, propertyValues);
		if (list.size() >= 1) {
			entity = list.get(0);
		}

		logger.debug("<<<<T findUniqueByProperty() end.return=[{}]", entity);
		return entity;
	}

	/*
	 * 实现方法具有性能问题，实际上应该使用findByCriteria(int, int,
	 * Criteria)的方法，只不过把setFirstResult(0)、setMaxResults(1)即可;
	 */
	@Override
	public T findUniqueByProperty(String propertyName, Object propertyValue, String sortPropertyName,
			boolean isAscending) {
		logger.debug(
				"T findUniqueByProperty(String property=[{}], Object value[{}], String sortPropertyName=[{}], "
						+ "boolean isAscending=[{}]) start>>>>",
				propertyName, propertyValue, sortPropertyName, isAscending);

		// 使用uniqueResult()时，如果查询到的记录有多个，会抛出HibernateException，因此换为调用
		// findByProperty，然后取出第一条
		/*
		 * T entity = (T) createCriteria(Restrictions.eq(propertyName,
		 * propertyValue)).addOrder( isAscending ? Order.asc(sortPropertyName) :
		 * Order.desc(sortPropertyName)).uniqueResult();
		 */
		T entity = null;

		List<T> list = findByProperty(propertyName, propertyValue, sortPropertyName, isAscending);
		if (list.size() >= 1) {
			entity = list.get(0);
		}

		logger.debug("<<<<T findUniqueByProperty() end.return=[{}]", entity);
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByProperty(String propertyName, Object...propertyValues) {
		
		Criteria c = createCriteria().add(Restrictions.in(propertyName, propertyValues));

		return c.list();
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByProperties(Map<String, Object[]> propertiesValuesMap) {
		
		Assert.notEmpty(propertiesValuesMap);
		Criteria c = createCriteria(propertiesValuesMap);

		return c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T findUnique(String hql, Object... values) {
		T entity = (T) createQuery(hql, values).uniqueResult();
		return entity;
	}

	/**
	 * 按属性统计记录数
	 * 
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 */
	public int countByProperty(String propertyName, Object propertyValue) {
		logger.debug(" int countByProperty(String property=[{}], Object value[{}]) start>>>>", propertyName,
				propertyValue);

		Assert.hasText(propertyName);
		Assert.notNull(propertyValue);
		int count = ((Number) (createCriteria(Restrictions.eq(propertyName, propertyValue))
				.setProjection(Projections.rowCount()).uniqueResult())).intValue();

		logger.debug("<<<<int countByProperty() end.return=[{}]", count);
		return count;
	}
	
	
	/**
	 * 创建无条件Criteria（即查询全部）,后续可进行更多处理,辅助函数.
	 */
	@Override
	public Criteria createCriteria() {
		return getSession().createCriteria(getEntityClass());
	}

	/**
	 * 根据Criterion条件创建Criteria,后续可进行更多处理,辅助函数.
	 */
	// @Override
	protected Criteria createCriteria(Criterion... criterions) {
		logger.debug("Criteria createCriteria(Criterion{} criterions=[{}]) start>>>>", "", criterions);

		Criteria criteria = createCriteria();
		for (Criterion c : criterions) {
			criteria.add(c);
		}

		logger.debug("<<<<Criteria createCriteria() end.return=[{}]", criteria);
		return criteria;
	}

	/**
	 * 根据Criterion条件创建Criteria,后续可进行更多处理,辅助函数.
	 */
	// @Override
	protected Criteria createCriteria(Map<String, Object[]> propertiesValuesMap) {
		logger.debug("Criteria createCriteria(Map<String, Object[]> propertiesValuesMap) start>>>>", "");

		Criteria criteria = createCriteria();
		Set<String> keyset = propertiesValuesMap.keySet();
		for(String key : keyset) {
			
			if(null == propertiesValuesMap.get(key) || 0 == propertiesValuesMap.get(key).length){
				criteria.add(Restrictions.eq(key, "NULL_"+System.currentTimeMillis()));
			} else{
				criteria.add(Restrictions.in(key, propertiesValuesMap.get(key)));
			}

		}

		logger.debug("<<<<Criteria createCriteria(Map<String, Object[]> propertiesValuesMap) end.return");
		return criteria;
	}
	/**
	 * 按Criterion查询列表数据.
	 * 
	 * @param criterions
	 *            数量可变的Criterion.
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByCriterions(Criterion... criterions) {
		logger.debug(">>>>start:Pagination findByCriterions(Criterion{} criterions=[{}])", "", criterions);

		List<T> list = createCriteria(criterions).list();

		logger.debug("<<<<end.Pagination findByCriterions() return=[{}]", list);
		return list;
	}

	@Override
	public Pagination findByCriterions(int pageNo, int pageSize, Criterion... criterions) {
		logger.debug(">>>>start:Pagination findByCriterions(int pageNo=[{}], int pageSize=[{}], Criteria crit=[{}])",
				pageNo, pageSize, criterions);

		Pagination pagination = findByCriteria(pageNo, pageSize, createCriteria(criterions));

		logger.debug("<<<<end.Pagination findByCriterions() return=[{}]", pagination);
		return pagination;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Pagination findByCriteria(int pageNo, int pageSize, Criteria crit) {
		logger.debug("Pagination findByCriteria(int pageNo=[{}], int pageSize=[{}], Criteria crit=[{}]) start>>>>",
				pageNo, pageSize, crit.toString());

		CriteriaImpl impl = (CriteriaImpl) crit;
		// 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
		Projection projection = impl.getProjection();
		ResultTransformer transformer = impl.getResultTransformer();
		List<CriteriaImpl.OrderEntry> orderEntries;
		try {
			orderEntries = (List<OrderEntry>) BeanUtils.getFieldValue(impl, ORDER_ENTRIES);
			BeanUtils.setFieldValue(impl, ORDER_ENTRIES, new ArrayList());
		} catch (RuntimeException e) {
			throw new RuntimeException("cannot read/write 'orderEntries' from CriteriaImpl", e);
		}

		// Criteria criteria = crit.setProjection(Projections.rowCount());
		// Object object = criteria.uniqueResult();
		int totalCount = ((Number) crit.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		Pagination p = new Pagination(pageNo, pageSize, totalCount);
		if (totalCount < 1 || pageNo>p.getTotalPage()) {// 添加：pageNo>p.getTotalPage()，请求页码大于最大页码，则返回为0长度
			p.setData(new ArrayList());
			return p;
		}

		// 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
		crit.setProjection(projection);
		if (projection == null) {
			crit.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		if (transformer != null) {
			crit.setResultTransformer(transformer);
		}
		try {
			BeanUtils.setFieldValue(impl, ORDER_ENTRIES, orderEntries);
		} catch (RuntimeException e) {
			throw new RuntimeException("set 'orderEntries' to CriteriaImpl faild", e);
		}
		crit.setFirstResult(p.getFirstResult());
		crit.setMaxResults(p.getPageSize());
		p.setData(crit.list());

		logger.debug("<<<<Pagination findByCriteria() end.return=[{}]", p.toString());
		return p;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dgg.common.dao.GenericDAO#queryObject(java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List query(String hql) {
		logger.debug(">>>>start:List query(String hql=[{}])", hql);

		// TODO 此处还未执行到，需要测试
		List list = createQuery(hql).list();

		logger.debug("<<<<end.query() return=[{}]", list);
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dgg.common.dao.GenericDAO#queryObject(java.lang.String,
	 * java.lang.Object[])
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public List query(String hql, Object... values) {
		return createQuery(hql, values).list();
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List query(Class resultClass, String hql, Object... values) {
		return createQuery(resultClass, hql, values).list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dgg.common.dao.hibernate.GenericPageDAO#findUnique(java.lang
	 * .String, java.lang.Object)
	 */
	@Override
	public Object queryUnique(String hql, Object... values) {
		logger.debug("T queryUnique(String hql=[{}], Object... values=[{}]) start>>>>", hql, values);

		Object object = createQuery(hql, values).uniqueResult();

		logger.debug("<<<<queryUnique() end.return=[{}]", object);
		return object;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dgg.common.dao.hibernate.GenericPageDAO#find(com.dgg.
	 * common.dao.Finder)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List query(Finder finder) {
		logger.debug("query(Finder finder=[{}]) start>>>>", finder);

		List list = finder.createQuery(getSession()).list();

		logger.debug("<<<<query() end.return=[{}]", list);
		return list;
	}

	@Override
	public Pagination query(int pageNo, int pageSize, Finder finder) {
		return query(pageNo, pageSize, null, finder);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Pagination query(int pageNo, int pageSize, Class resultClass, Finder finder) {
		logger.debug(
				">>>>start:Pagination query(int pageNo=[{}], int pageSize=[{}], Class resultClass=[{}], Finder finder=[{}])",
				pageNo, pageSize, resultClass, finder);

		int totalCount = countQueryResult(finder);
		Pagination p = new Pagination(pageNo, pageSize, totalCount);
		if (totalCount < 1 || pageNo > p.getTotalPage()) {
			p.setData(new ArrayList());
		} else {
			Query query = getSession().createQuery(finder.getOrigHql());
			finder.setParamsToQuery(query);
			if (resultClass != null)
				query.setResultTransformer(Transformers.aliasToBean(resultClass));
			query.setFirstResult(p.getFirstResult());
			query.setMaxResults(p.getPageSize());
			if (finder.isCacheable()) {
				query.setCacheable(true);
			}
			List list = query.list();
			p.setData(list);
		}
		logger.debug("<<<<query() end.return=[{}]", p);
		return p;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pagination query(int pageNo, int pageSize, Class resultClass, String hql) {
		logger.debug(
				">>>>start:Pagination query(int pageNo=[{}], int pageSize=[{}], Class resultClass=[{}], String hql=[{}]",
				pageNo, pageSize, resultClass, hql);
		Pagination p = query(pageNo, pageSize, resultClass, Finder.create(hql));
		logger.debug("<<<<end.Pagination query()=[{}]", p);
		return p;
	}

	@Override
	public Pagination query(int pageNo, int pageSize, String hql) {
		logger.debug(">>>>start:Pagination query(int pageNo=[{}], int pageSize=[{}], String hql=[{}]", pageNo, pageSize,
				hql);
		Pagination p = query(pageNo, pageSize, Finder.create(hql));
		logger.debug("<<<<end.Pagination query()=[{}]", p);
		return p;
	}

	@Override
	public Pagination query(int pageNo, int pageSize, String hql, Object... values) {
		return query(pageNo, pageSize, null, hql, values);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pagination query(int pageNo, int pageSize, Class resultClass, String hql, Object... values) {
		logger.debug(
				">>>>start:Pagination query(int pageNo=[{}], int pageSize=[{}], Class resultClass=[{}], String hql=[{}], Object... values=[{}])",
				pageNo, pageSize, resultClass, hql, values);

		Finder finder = Finder.create(hql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				finder.setParam(String.valueOf(i), values[i]);
			}
		}
		Pagination p = query(pageNo, pageSize, resultClass, finder);
		logger.debug("<<<<end.Pagination query()=[{}]", p);
		return p;
	}

	@Override
	public Query createQuery(String hql) {
		return createQuery(null, hql);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Query createQuery(Class resultClass, String hql) {
		logger.debug(">>>>start:Query createQuery(Class resultClass=[{}], String hql=[{}])", resultClass, hql);
		Assert.hasText(hql);
		Query query = getSession().createQuery(hql);
		if (resultClass != null)
			query.setResultTransformer(Transformers.aliasToBean(resultClass));
		logger.debug("<<<<end.createQuery() return=[{}]", query);
		return query;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dgg.common.dao.hibernate.GenericPageDAO#createQuery(java.lang
	 * .String, java.lang.Object)
	 */
	@Override
	public Query createQuery(String hql, Object... values) {
		return createQuery(null, hql, values);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Query createQuery(Class resultClass, String hql, Object... values) {
		logger.debug(">>>>start:Query createQuery(Class resultClass=[{}], String hql=[{}], Object... values=[{}])",
				resultClass, hql, values);

		Assert.hasText(hql);
		Query query = getSession().createQuery(hql);
		if (resultClass != null)
			query.setResultTransformer(Transformers.aliasToBean(resultClass));
		String paraName;
	
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				paraName = String.valueOf(i);
				Object object = values[i];
				if (object instanceof Collection)
					query.setParameterList(paraName, (Collection) object);
				else if (object instanceof Object[])
					query.setParameterList(paraName, (Object[]) object);
				else
					query.setParameter(paraName, object);
			}
		}
		logger.debug("<<<<end.createQuery() return=[{}]", query);
		return query;
	}

	@Override
	public SQLQuery createSQLQuery(String sql, Object... values) {
		return createSQLQuery(null, sql, values);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public SQLQuery createSQLQuery(Class resultClass, String hql, Object... values) {
		logger.debug(">>>>start.SQLQuery createSQLQuery(Class resultClass, String hql=[{}], Object... values=[{}])",
				resultClass, hql, values);

		Assert.hasText(hql);
		SQLQuery query = getSession().createSQLQuery(hql);
		if (resultClass != null) {
			// query.setResultTransformer(Transformers.aliasToBean(resultClass)); // bug
			query.addEntity(resultClass);
		}
		String paraName;
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				paraName = String.valueOf(i);
				Object object = values[i];
				if (object instanceof Collection)
					query.setParameterList(paraName, (Collection) object);
				else if (object instanceof Object[])
					query.setParameterList(paraName, (Object[]) object);
				else
					query.setParameter(paraName, object);
			}
		}
		logger.debug("<<<<end.SQLQuery createSQLQuery() return=[{}]", query);
		return query;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dgg.common.dao.hibernate.GenericPageDAO#countQueryResult(com
	 * .dgg.common.dao.Finder)
	 */
	@Override
	public int countQueryResult(Finder finder) {
		logger.debug("int countQueryResult(Finder finder=[{}]) start>>>>", finder);

		try {
			Query query = getSession().createQuery(finder.getRowCountHql());
			finder.setParamsToQuery(query);
			if (finder.isCacheable()) {
				query.setCacheable(true);
			}
			int count = ((Number) query.iterate().next()).intValue();

			logger.debug("<<<<countQueryResult() end.return=[{}]", count);
			return count;
		} catch (QuerySyntaxException qse) {
			logger.error(qse.getMessage());
			throw new RuntimeException(qse.getMessage());
		}
	}

	@Override
	public void update(String hql) {
		Query query = createQuery(hql);
		query.executeUpdate();
	}

	@Override
	public void update(String hql, Object... values) {
		Query query = createQuery(hql, values);
		query.executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dgg.common.dao.GenericDAO#update(java.lang.Object)
	 */
	@Override
	public T update(T entity) {
		logger.debug("T update(T entity=[{}]) start>>>>", entity);

		if (entity == null) {
			throw new IllegalArgumentException("attempt to save with null entity");
		}

		if (!isAttached(entity)) {
			getSession().update(entity);
		}
		logger.debug("<<<<update() end.return=[{}]", entity);
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dgg.common.dao.GenericDAO#updateByUpdater(com.dgg.common.dao.Updater)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T updateByUpdater(Updater<T> updater) {
		ClassMetadata cm = sessionFactory.getClassMetadata(getEntityClass());
		T bean = updater.getBean();
		T po = (T) getSession().get(getEntityClass(), cm.getIdentifier(bean, (SessionImplementor) getSession()));
		// cm.getIdentifier(bean, POJO));
		updaterCopyToPersistentObject(updater, po, cm);
		return po;
	}

	/**
	 * 将更新对象拷贝至实体对象，并处理many-to-one的更新（！这个似乎还没实现！）。
	 * <p>
	 * 注意：根据实体对象除主键以外的属性进行复制，忽略updater中多余的属性
	 * </p>
	 * 
	 * @param updater
	 *            更新对象
	 * @param po
	 *            持久话的实体对象
	 * @param cm
	 *            实体对象的metadata
	 */
	private void updaterCopyToPersistentObject(Updater<T> updater, T po, ClassMetadata cm) {
		String[] propNames = cm.getPropertyNames();
		String identifierName = cm.getIdentifierPropertyName();
		T bean = updater.getBean();
		Object value;
		for (String propName : propNames) {
			// 主键不能复制
			if (propName.equals(identifierName)) {
				continue;
			}
			try {
				// 获取bean中与entity对应（名称相同）属性的值
				value = BeanUtils.getSimpleProperty(bean, propName);
				if (!updater.isUpdate(propName, value)) {
					continue;
				}
				cm.setPropertyValue(po, propName, value);
				// cm.setPropertyValue(po, propName, value, POJO);
			} catch (RuntimeException e) {
				throw new RuntimeException("copy property to persistent object failed: '" + propName + "'", e);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dgg.common.dao.GenericDAO#save(java.lang.Object)
	 */
	@Override
	public T save(T entity) {
		if (entity == null) {
			throw new IllegalArgumentException("attempt to save with null entity");
		}

		getSession().save(entity);

		return entity;
	}

	@Override
	public List<T> save(T... entities) {
		for (int i = 0; i < entities.length; i++) {
			save(entities[i]);
		}
		return Arrays.asList(entities);
	}

	@Override
	public List<T> save(Collection<T> entities) {
		List<T> newList = new ArrayList<T>();
		for (T entity : entities) {
			save(entity);
			newList.add(entity);
		}
		return newList;
	}

	@Override
	public boolean remove(T entity) {
		if (entity == null) {
			throw new IllegalArgumentException("attempt to remove with null entity");
		}

		if (getSession().getIdentifier(entity) != null) {
			getSession().delete(entity);
			return true;
		}
		return false;
	}

	@Override
	public void remove(T... entities) {
		for (T entity : entities) {
			remove(entity);
		}
	}

	@Override
	public void remove(Collection<T> entities) {
		for(T entity : entities){
			remove(entity);
		}
	}

	@Override
	public boolean removeById(ID id) {
		T entity = get(id, true);
		if (entity == null) {
			return false;
		}
		getSession().delete(entity);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dgg.common.dao.GenericDAO#removeByIds(ID[])
	 */
	@Override
	public void removeByIds(ID... ids) {
		for (ID id : ids) {
			removeById(id);
		}
	}

	@Override
	public void removeByProperty(String propertyName, Object propertyValue) {
		List<T> list = findByProperty(propertyName, propertyValue, propertyName, false);
		for (T entity : list) {
			getSession().delete(entity);
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void removeByProperty(String propertyName, Object[] propertyValue) {
		
		Criteria c = createCriteria().add(Restrictions.in(propertyName, propertyValue));
		this.remove(c.list());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void removeByProperties(Map<String, Object[]> propertiesValues) {
		
		Assert.notEmpty(propertiesValues);
		
		Criteria criteria = this.createCriteria(propertiesValues);
	
		this.remove(criteria.list());
	}

	@Override
	public void removeByProperties(String[] propertyNames, Object[] propertyValues) {
		List<T> list = findByProperties(propertyNames, propertyValues);
		for (T entity : list) {
			getSession().delete(entity);
		}
	}

	@Override
	public void removeBySql(String sql) {

		getSession().createSQLQuery(sql).executeUpdate();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dgg.common.dao.GenericDAO#isAttached(java.lang.Object)
	 */
	@Override
	public boolean isAttached(T entity) {
		return getSession().contains(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dgg.common.dao.GenericDAO#refresh(T[])
	 */
	@Override
	public void refresh(T... entities) {
		for (Object entity : entities) {
			getSession().refresh(entity);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dgg.common.dao.GenericDAO#flush()
	 */
	@Override
	public void flush() {
		getSession().flush();
	}

	/**
	 * 更新或保存单个实体
	 * 
	 * @param entity
	 *            实体对象
	 */
	@Override
	public T saveOrUpdate(T entity) {
		logger.debug("T saveOrUpdate(T entity=[{}]) start>>>>", entity);
		getSession().saveOrUpdate(entity);
		Assert.notNull(entity);
		logger.debug("<<<<saveOrUpdate() end.return=[{}]", entity);
		return entity;
	}

	@Override
	public boolean saveOrUpdate(Collection<T> entities) {
		logger.debug("T saveOrUpdate(T entities=[{}]) start>>>>", entities);
		// Transaction session = getSession().beginTransaction();
		for (T entity : entities) {
			getSession().saveOrUpdate(entity);
		}
		// session.commit();
		logger.debug("<<<<saveOrUpdate() end.return=[{}]", true);
		return true;
	}
	/**
	 * 保存实体
	 * 
	 * @param entity
	 *            实体对象
	 * @return 保存成功返回true，保存失败返回false
	 */
	@Override
	public boolean saveEntity(T entity) {
		logger.debug("T saveEntity(T entity=[{}]) start>>>>", entity);
		// Transaction transaction = getSession().beginTransaction();
		if (entity == null) {
			return false;
		} else {
			getSession().save(entity);
		}
		// transaction.commit();
		logger.debug("<<<<saveEntity() end.return=[{}]", true);
		return true;

	}

	/**
	 * 批量保存
	 * 
	 * @param
	 */
	@Override
	public boolean saveEntities(T... entities) {
		logger.debug("T saveEntities(T entities=[{}]) start>>>>", entities);
		// Transaction session = getSession().beginTransaction();
		for (T entity : entities) {
			boolean flag = saveEntity(entity);
			if (!flag) {
				return false;
			}
		}
		// session.commit();
		logger.debug("<<<<saveEntities() end.return=[{}]", true);
		return true;

	}

	/**
	 * 
	 * 更新成功
	 * 
	 * @param entity
	 *            实体对象
	 * @return true=更新成功，false=更新失败
	 */
	@Override
	public boolean updateEntity(T entity) {
		logger.debug("T updateEntity(T entity=[{}]) start>>>>", entity);
		if (entity == null) {
			return false;
		} else {
			// by weifj, 2015-06-09
			// 建议不使用Transaction transaction = getSession().beginTransaction()
			// transaction.commit()
			// 否则抛出nested transactions not supported（内嵌事务不支持）
			// Transaction transaction = getSession().beginTransaction();
			getSession().update(entity);
			// transaction.commit();
		}
		logger.debug("<<<<updateEntity() end.return=[{}]", true);
		return true;
	}

	/**
	 * 
	 * 批量更新
	 * 
	 * @param entities
	 *            多个实体对象
	 * @return true=更新成功，false=更新失败
	 */
	@Override
	public boolean updateEntities(T... entities) {
		logger.debug("T updateEntities(T entities=[{}]) start>>>>", entities);
		for (T entity : entities) {
			boolean flag = updateEntity(entity);
			if (!flag) {
				// logger.debug("<<<<updateEntities() end.return=[{}]", entity);
				return false;
			}
		}
		logger.debug("<<<<updateEntities() end.return=[{}]", entities);
		return true;

	}

	/**
	 * 
	 * 返回boolean值得批量删除
	 * 
	 * @param ids
	 *            多个id
	 * @return true=成功，false=失败
	 */
	@Override
	public boolean remove(ID... ids) {
		for (ID id : ids) {
			boolean flag = removeById(id);
			if (!flag) {

				return false;
			}
		}
		return true;

	}

	@Override
	public Object queryUniqueWithCache(String hql, Object... values) {
		logger.debug("T queryUnique(String hql=[{}], Object... values=[{}]) start>>>>", hql, values);

		Object object = createQuery(hql, values).setCacheable(true).uniqueResult();

		logger.debug("<<<<queryUnique() end.return=[{}]", object);
		return object;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List queryWithCache(String hql, Object... values) {
		logger.debug("List query(String hql=[{}], Object... values=[{}]) start>>>>", hql, values);

		List list = createQuery(hql, values).setCacheable(true).list();

		logger.debug("<<<<query() end.return=[{}]", list);
		return list;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List queryWithCache(String hql) {

		List list;
		try {
			list = getSession().createQuery(hql).setCacheable(true).list();
		} catch (QuerySyntaxException qse) {
			logger.error(qse.getMessage());
			throw new RuntimeException(qse.getMessage());
		}

		return list;
	}

	@Override
	public Pagination queryBySql(int pageNo, int pageSize, String sql) {

		// 首先获取总共有多少条
		Query query = getSession().createSQLQuery(sql);
		
		int totalCount = query.list().size();
		Pagination p = new Pagination(pageNo, pageSize, totalCount);
		query.setFirstResult(p.getFirstResult());
		query.setMaxResults(p.getPageSize());
		p.setData(query.list());
		return p;
	}
	
	@Override
	public Pagination queryBySql(int pageNo, int pageSize, String sql, Class clazz) {
		
		// 首先获取总共有多少条
		Query query = getSession().createSQLQuery(sql).addEntity(clazz);
		
		int totalCount = query.list().size();
		Pagination p = new Pagination(pageNo, pageSize, totalCount);
		query.setFirstResult(p.getFirstResult());
		query.setMaxResults(p.getPageSize());
		p.setData(query.list());
		return p;
		
	}

	@Override
	public Pagination queryBySql(int pageNo, int pageSize, String sql, Object... values) {

		// 首先获取总共有多少条
		SQLQuery query = createSQLQuery(sql, values);
		int totalCount = query.list().size();
		Pagination p = new Pagination(pageNo, pageSize, totalCount);
		query.setFirstResult(p.getFirstResult());
		query.setMaxResults(p.getPageSize());
		p.setData(query.list());
		return p;
	}

	@Override
	public List<T> queryByName(String hql, String[] names, Object[] values) {
		return queryByName(null, hql, names, values);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<T> queryByName(Class resultClass, String hql, String[] names, Object[] values) {
		Query query = getSession().createQuery(hql);
		if (resultClass != null)
			query.setResultTransformer(Transformers.aliasToBean(resultClass));
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			Object object = values[i];
			if (object instanceof Collection)
				query.setParameterList(name, (Collection) object);
			else if (object instanceof Object[])
				query.setParameterList(name, (Object[]) object);
			else
				query.setParameter(name, object);
		}
		return query.list();
	}

	@Override
	public List<T> queryByName(String hql, Map<String, Object> map) {
		return queryByName(null, hql, map);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<T> queryByName(Class resultClass, String hql, Map<String, Object> map) {
		Query query = getSession().createQuery(hql);
		if (resultClass != null)
			query.setResultTransformer(Transformers.aliasToBean(resultClass));
		Set<String> keys = map.keySet();
		for (String name : keys) {
			Object object = map.get(name);
			if (object instanceof Collection)
				query.setParameterList(name, (Collection) object);
			else if (object instanceof Object[])
				query.setParameterList(name, (Object[]) object);
			else
				query.setParameter(name, object);
		}
		return query.list();
	}

	@Override
	public void remove(String hql, Object[] params) {
		Query query = createQuery("delete  " + hql, params);
		query.executeUpdate();
	}

	@Override
	public void removeAll() {
		String hql = "delete  " + this.getEntityClass().getSimpleName();
		Query query = getSession().createQuery(hql);
		query.executeUpdate();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pagination find(int pageNo, int pageSize, String sortProperty, boolean isAsc, T valueObject,
			String[] properties) {

		logger.debug("Pagination query(int pageNo=[{}], int pageSize=[{}], T entity=[{}]) start>>>>", pageNo, pageSize,
				valueObject);

		Pagination p = null;
		Criteria criteria = this.getSession().createCriteria(valueObject.getClass());
		
		Conjunction conjunction = Restrictions.conjunction(); // And 操作
		for (String property : properties) {
			try {
				Object value = PropertyUtils.getProperty(valueObject, property);
				if (null == value || value.toString().isEmpty()) {
					continue;
				}
				if (value != null) {
					criteria.add(Restrictions.eq(property, value));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				continue;
			}
		}
		criteria.add(conjunction);
		/*
		 * if (entity != null) {
		 * criteria.add(Example.create(entity).enableLike()); }
		 */

		// 获取根据条件分页查询的总行数
		int rowCount = Integer.valueOf(criteria.setProjection(Projections.rowCount()).uniqueResult().toString());
		logger.debug("query count={}", rowCount);

		//将映射（即select）设置为空
		criteria.setProjection(null);
		criteria.setFirstResult((pageNo - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		criteria.addOrder((isAsc) ? Order.asc(sortProperty) : Order.desc(sortProperty));

		List list = criteria.list();
		p = new Pagination(pageNo, pageSize, rowCount);
		if (rowCount < 1) {
			p.setData(new ArrayList());
			return p;
		}
		p.setData(list);

		logger.debug("<<<<end.query() return=[{}]", list);
		return p;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByProperties(Map<String, Object[]> propertiesValuesMap, String orderPropertyName,
			boolean isAscending) {
		
        Criteria criteria = this.createCriteria(propertiesValuesMap);
        criteria.addOrder(isAscending ? Order.asc(orderPropertyName) : Order.desc(orderPropertyName));
        return criteria.list();
	}
	@Override
	public Pagination findByPropertiesMatch(int pageNo, int pageSize, String[] properties, String keyword, MatchMode matchMode) {
		
		Assert.notNull(properties);
		
		Criteria criteria = this.createCriteria();

		Disjunction dis = Restrictions.disjunction();
		for (String p : properties) {
			dis.add(Restrictions.like(p, keyword, matchMode));
		}
		criteria.add(dis);
		Pagination pagination = findByCriteria(pageNo, pageSize, criteria);
		return pagination;
	}
}
