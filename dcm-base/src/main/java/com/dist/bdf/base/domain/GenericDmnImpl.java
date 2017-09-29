/*
 * @Copyright：上海数慧系统技术有限公司 2014
*/
package com.dist.bdf.base.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.dist.bdf.base.dao.Finder;
import com.dist.bdf.base.dao.Sort;
import com.dist.bdf.base.dao.Updater;
import com.dist.bdf.base.dto.DateFilterRangeDTO;
import com.dist.bdf.base.exception.BusinessException;
import com.dist.bdf.base.page.Pagination;

/**
 * 通用领域对象
 * <p>
 * findByProperty：根据属性进行精确查询并排序
 * </p>
 *
 * @author 李其云
 * @param <T>
 *            表示对象的类型，实现该接口的时候，需要提供数据访问的对象
 * @param <ID>
 *            数据访问对象的id,这个实例使用。不同的域对象的id是用于此实例
 *  @version 1.0
 *  @version 1.1，2016/04/14，weifj，修改分页排序bug
 *     public Pagination findByProperty(int pageNo, int pageSize, String propertyName, Object propertyValue,
			String sortPropertyName, boolean isAscending)，逻辑判断StringUtils.isBlank(sortPropertyName)写反导致排序失败
 */
public abstract class GenericDmnImpl<T, ID extends Serializable> implements GenericDmn<T, ID> {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * Spring提供的用于获取国际化properties文件信息的接口，实现类是spring配置文件中id为messageSource的bean
	 */
	@Autowired
	private MessageSource messageSource;

	@Override
	public T findById(ID id) {
		return getDao().find(id);
	}

	@Override
	public T loadById(ID id) {
		// TODO 是否应使用session的load方法，通过截获ObjectNotFoundException来实现？
		T entity = findById(id);
		if (entity == null)
			throw new BusinessException("找不到id为[{0}]的实体", id);

		return entity;
	}

	@Override
	public T loadById(ID id, String entityName) {
		// TODO 是否应使用session的load方法，通过截获ObjectNotFoundException来实现？
		T entity = findById(id);
		if (entity == null)
			throw new BusinessException("不存在id为[{0}]的{1}", id, entityName);

		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.easymeans.common.domain.GenericDmn#findByIds(ID[])
	 */
	@Override
	public List<T> findByIds(ID... ids) {
		return getDao().find(ids);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.easymeans.common.domain.GenericDmn#find()
	 */
	@Override
	public List<T> find() {
		return getDao().find();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> find(String hql, Object... params) {
		Query query = getDao().createQuery(hql, params);
		return query.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.easymeans.common.domain.GenericDmn#find(int, int)
	 */
	@Override
	public Pagination find(int pageNo, int pageSize) {
		logger.debug(">>>>find(int pageNo=[{}], int pageSize=[{}]) start", pageNo, pageSize);

		Pagination pagination = getDao().findByCriterions(pageNo, pageSize);

		// logger.debug("<<<<find() end.return=[{}]", pagination);
		return pagination;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.easymeans.common.domain.GenericDmn#find(java.lang.String,
	 * boolean)
	 */
	@Override
	public List<T> find(String sortPropertyName, boolean isAscending) {
		logger.debug(">>>>find(String sortPropertyName=[{}], boolean isAscending=[{}]) start", sortPropertyName,
				isAscending);

		List<T> entities = getDao().find(sortPropertyName, isAscending);

		logger.debug("<<<<find() end.return=[{}]", entities);
		return entities;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.easymeans.common.domain.GenericDmn#findPage(java.lang.String,
	 * boolean, int, int)
	 */
	@Override
	public Pagination find(int pageNo, int pageSize, String sortPropertyName, boolean isAscending) {
		logger.debug(">>>>findPage(int pageNo=[{}], int pageSize=[{}]) start", pageNo, pageSize);

		Pagination pagination = getDao().find(pageNo, pageSize, sortPropertyName, isAscending);

		logger.debug("<<<<findPage() end.return=[{}]", pagination);
		return pagination;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.easymeans.common.domain.GenericDmn#find(int, int,
	 * java.util.List)
	 */
	@Override
	public Pagination find(int pageNo, int pageSize, List<Sort> sorts) {
		logger.debug(">>>>findPage(int pageNo=[{}], int pageSize=[{}], List<Sort> sorts) start", pageNo, pageSize,
				sorts);

		Pagination pagination = getDao().find(pageNo, pageSize, sorts);

		logger.debug("<<<<findPage() end.return=[{}]", pagination);
		return pagination;
	}

	@Override
	public List<T> findByProperty(String propertyName, Object propertyValue) {
		return findByProperty(propertyName, propertyValue, propertyName, true);
	}

	@Override
	public List<T> findByProperty(String propertyName, Object propertyValue, String sortPropertyName,
			boolean isAscending) {
		// logger.debug(
		// ">>>>List<T> findByProperty(String propertyName=[{}], Object
		// propertyValue=[{}],
		// String sortPropertyName=[{}], boolean isAscending=[{}]) start",
		// propertyName, propertyValue, sortPropertyName, isAscending);

		List<T> list = getDao().findByProperty(propertyName, propertyValue, sortPropertyName, isAscending);

		logger.debug("<<<<List<T> findByProperty() end.return=[{}]", list);
		return list;
	}

	@Override
	public List<T> findByProperty(String propertyName, Object[]propertyValues) {
		
		return this.getDao().findByProperty(propertyName, propertyValues);
	}
	@Override
	public List<T> findByProperties(Map<String, Object[]> propertiesValuesMap){
		return this.getDao().findByProperties(propertiesValuesMap);
	}
	
	@Override
	public List<T> findByProperties(Map<String, Object[]> propertiesValuesMap, String sortPropertyName,
			boolean isAscending){
		
		return this.getDao().findByProperties(propertiesValuesMap, sortPropertyName, isAscending);
	}
	
	@Override
	public T findUniqueByProperty(String propertyName, Object propertyValue) {
		logger.debug(">>>>T findUniqueByProperty(String propertyName=[{}], Object propertyValue=[{}]) start",
				propertyName, propertyValue);

		T entity = getDao().findUniqueByProperty(propertyName, propertyValue);

		logger.debug("<<<<T findUniqueByProperty() end.return=[{}]", entity);
		return entity;
	}

	@Override
	public T findUniqueByProperties(String[] propertyNames, Object[] propertyValues) {

		logger.debug(">>>>T findUniqueByProperty(String propertyNames=[{}], Object propertyValues=[{}]) start",
				propertyNames, propertyValues);

		T entity = getDao().findUniqueByProperties(propertyNames, propertyValues);

		logger.debug("<<<<T findUniqueByProperty() end.return=[{}]", entity);
		return entity;

	}

	@Override
	public T findUniqueByProperty(String propertyName, Object propertyValue, String sortPropertyName,
			boolean isAscending) {
		// logger.debug(
		// ">>>>List<T> findUniqueByProperty(String propertyName=[{}], Object
		// propertyValue=[{}],
		// String sortPropertyName=[{}], boolean isAscending=[{}]) start",
		// propertyName, propertyValue, sortPropertyName, isAscending);

		T entity = getDao().findUniqueByProperty(propertyName, propertyValue, sortPropertyName, isAscending);

		logger.debug("<<<<List<T> findUniqueByProperty() end.return=[{}]", entity);
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.easymeans.common.domain.GenericDmn#findByProperty(int, int,
	 * java.lang.String, java.lang.Object)
	 */
	@Override
	public Pagination findByProperty(int pageNo, int pageSize, String propertyName, Object propertyValue) {
		return findByProperty(pageNo, pageSize, propertyName, propertyValue, propertyName, true);
	}

	@Override
	public List<T> findByProperties(String[] propertyNames, Object[] propertyValues) {
		List<T> list = getDao().findByProperties(propertyNames, propertyValues);
		return list;
	}
	@Override
	public List<T> findByProperties(String[] propertyNames, Object[] propertyValues, List<Sort> sorts) {
		
		return (List<T>) this.getDao().findByProperties(propertyNames, propertyValues, sorts);
	}
	@Override
	public List<T> findByProperties(String[] propertyNames, Object[] propertyValues,String sortPropertyName, boolean isAscending) {
		List<T> list = getDao().findByProperties(propertyNames, propertyValues,sortPropertyName,isAscending);
		return list;
	}

	@Override
	public Pagination findByProperty(int pageNo, int pageSize, String propertyName, Object propertyValue,
			String sortPropertyName, boolean isAscending) {
		
		Pagination pagination = getDao().findByProperty(pageNo, pageSize, propertyName, propertyValue, sortPropertyName,
				isAscending);	
		
		logger.debug("<<<<findByProperty() end.return=[{}]", pagination);
		return pagination;
	}

	@Override
	public Pagination findByProperties(int pageNo, int pageSize, Map<String, Object[]> equalProperties, Map<String, Object[]> likeProperties, String orderPropertyName, boolean isAscending) {

		Pagination pagination = getDao().findByProperties(pageNo, pageSize, equalProperties, likeProperties, orderPropertyName, isAscending);	
		
		logger.debug("<<<<findByProperty() end.return=[{}]", pagination);
		return pagination;
	}
	
	@Override
	public Pagination findByProperties(Integer pageNo, Integer pageSize, Map<String, Object[]> equalProperties,
			Map<String, Object[]> likeProperties, Map<String, Object[]> notProperties, String orderPropertyName, boolean isAscending) {
	
	   Pagination pagination = getDao().findByProperties(pageNo, pageSize, equalProperties, likeProperties, notProperties, orderPropertyName, isAscending);	
		
		return pagination;
	}
	@Override
	public Criteria createCriteria() {
		return this.getDao().createCriteria();
	}
	@Override
	public Pagination findByCriteria(Integer pageNo, Integer pageSize, Criteria criteria) {
		
		return this.getDao().findByCriteria(pageNo, pageSize, criteria);
	}
	@Override
	public Pagination findByProperties(int pageNo, int pageSize, Map<String, Object> equalProperties, Map<String, Object> orProperties, List<Sort> sorts) {

		Pagination pagination = getDao().findByProperties(pageNo, pageSize, equalProperties, orProperties, sorts);	
		
		logger.debug("<<<<findByProperty() end.return=[{}]", pagination);
		return pagination;
	}
	
	@Override
	public Pagination findByProperty(int pageNo, int pageSize, Map<String, Object[]> propertiesValuesMap, String orderPropertyName, boolean isAscending ) {
	
	     Pagination pagination = getDao().findByProperty(pageNo, pageSize, propertiesValuesMap, orderPropertyName, isAscending);	
		
		logger.debug("<<<<findByProperty() end.return=[{}]", pagination);
		return pagination;
	}
	@Override
	public Pagination findByPropertiesAndValues(int pageNo, int pageSize, Map<String,Object[]> propertiesValues,
				String sortPropertyName, boolean isAscending) {
		
		return this.getDao().findByPropertiesAndValues(pageNo, pageSize, propertiesValues, sortPropertyName, isAscending);
	}
	@Override
	public Pagination findByPropertiesAndValues(int pageNo, int pageSize, Map<String,Object[]> propertiesValues, 
			String datePropertyName,Date beginTime, Date endTime, String sortPropertyName, boolean isAscending) {
		
		return this.getDao().findByPropertiesAndValues(pageNo, pageSize, propertiesValues, datePropertyName, beginTime, endTime,  sortPropertyName, isAscending);
	}
	@Override
	public Pagination findByPropertiesAndValues(int pageNo, int pageSize, Map<String,Object[]> propertiesValues, 
			List<DateFilterRangeDTO> datePropertyFilters, String sortPropertyName, boolean isAscending) {
		
		return this.getDao().findByPropertiesAndValues(pageNo, pageSize, propertiesValues, datePropertyFilters,  sortPropertyName, isAscending);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.easymeans.common.domain.GenericDmn#queryObject(java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List query(String hql) {
		logger.debug(">>>>List query(String hql=[{}]) start", hql);

		List list = getDao().query(hql);

		logger.debug("<<<<query() end.return=[{}]", list);
		return list;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List query(String hql, Object... values) {
		logger.debug(">>>>List query(String hql=[{}], Object... Object=[{}]) start", hql, values);

		List list = getDao().query(hql, values);

		// logger.debug("<<<<query() end.return=[{}]", list);
		return list;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List query(Class resultClass, String hql, Object... values) {
		logger.debug(">>>>List query(Class resultClass=[{}], String hql=[{}], Object... values=[{}])", resultClass, hql,
				values);

		List list = getDao().query(resultClass, hql, values);

		logger.debug("<<<<end.query() return=[{}]", list);
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.easymeans.common.domain.GenericDmn#queryUnique(java.lang.String,
	 * java.lang.Object[])
	 */
	@Override
	public Object queryUnique(String hql, Object... values) {
		logger.debug(">>>>Object queryUnique(String hql=[{}], Object... Object=[{}]) start", hql, values);

		Object obj = getDao().queryUnique(hql, values);

		logger.debug("<<<<Object queryUnique() end.return=[{}]", obj);
		return obj;
	}

	@Override
	public boolean existsedByProperty(String propertyName, Object propertyValue, boolean isModify) {
		List<T> ts = this.findByProperty(propertyName, propertyValue);
		return ts != null && ts.size() > (isModify ? 1 : 0) ? true : false;
	}
	@Override
	public boolean existByProperties(String[] propertyNames, Object[] propertyValues) {
		
		List<T> ts = this.findByProperties(propertyNames, propertyValues);
		
		return ts != null && ts.size() > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.easymeans.common.domain.GenericDmn#query(int, int,
	 * java.lang.String)
	 */
	@Override
	public Pagination query(int pageNo, int pageSize, String hql) {
		logger.debug(">>>>Pagination query(int pageNo=[{}], int pageSize=[{}], String hql=[{}]) start", pageNo, pageSize,
				hql);

		Pagination pagination = getDao().query(pageNo, pageSize, hql);

		logger.debug("<<<<query() end.return=[{}]", pagination);
		return pagination;
	}

	@Override
	public Pagination query(int pageNo, int pageSize, String sortPropertyName, String ascending, String hql) {
		logger.debug(">>>>Pagination query(int pageNo=[{}], int pageSize=[{}], String hql=[{}]) start", pageNo, pageSize,
				hql);
		StringBuffer hqlBuffer = new StringBuffer(hql);
		if (!StringUtils.isBlank(sortPropertyName)) {
			hqlBuffer.append(" order by ").append(sortPropertyName).append(" ").append(ascending);
		}
		Pagination pagination = getDao().query(pageNo, pageSize, Finder.create(hqlBuffer.toString()));

		// logger.debug("<<<<query() end.return=[{}]", pagination);
		return pagination;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.easymeans.common.domain.GenericDmn#query(int, int,
	 * java.lang.String, java.lang.Object[])
	 */
	@Override
	public Pagination query(int pageNo, int pageSize, String hql, Object... values) {
		logger.debug(">>>>Pagination query(int pageNo, int pageSize, String hql=[{}], Object... values=[{}]) start",
				pageNo, pageSize, hql, values);

		Pagination pagination = getDao().query(pageNo, pageSize, hql, values);

		logger.debug("<<<<query() end.return=[{}]", pagination);
		return pagination;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pagination query(int pageNo, int pageSize, Class resultClass, String hql, Object... values) {
		logger.debug(
				">>>>start:Pagination query(int pageNo=[{}], int pageSize=[{}], Class resultClass=[{}], String hql=[{}], Object... values=[{}])",
				pageNo, pageSize, resultClass, hql, values);

		Pagination pagination = getDao().query(pageNo, pageSize, resultClass, hql, values);

		logger.debug("<<<<end.query() return=[{}]", pagination);
		return pagination;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List queryBySql(String sql) {
		return getDao().getSession().createSQLQuery(sql).list();
	}

	@Override
	public Pagination queryBySql(int pageNo, int pageSize, String sql) {
		return getDao().queryBySql(pageNo, pageSize, sql);
	}

	@Override
	public Pagination queryBySql(int pageNo, int pageSize, String sql, Class clazz) {
		return getDao().queryBySql(pageNo, pageSize, sql, clazz);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.easymeans.common.domain.GenericDmn#modify(com.easymeans.common.dao.
	 * Updater)
	 */
	@Override
	public T modify(Updater<T> updater) {
		logger.debug(">>>>T modify(Updater<T> updater=[{}]) start", updater);

		T entity = getDao().updateByUpdater(updater);

		// logger.debug("<<<<modify() end.return=[{}]", entity);
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.easymeans.common.domain.GenericDmn#add(java.lang.Object)
	 */
	@Override
	public T add(T entity) throws RuntimeException {
		logger.debug(">>>>T add(T entity=[{}]) start", entity);

		getDao().save(entity);

		logger.debug("<<<<add() end.return=[{}]", entity);
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.easymeans.common.domain.GenericDmn#modify(java.lang.Object)
	 */
	@Override
	public T modify(T entity) throws RuntimeException {
		
		logger.debug(">>>>T modify(T entity=[{}]) start", entity);
		getDao().update(entity);

		return entity;
	}

	@Override
	public void remove(T entity) {
		getDao().remove(entity);
	}

	@Override
	public void remove(Collection<T> entities) {
		getDao().remove(entities);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.easymeans.common.domain.GenericDmn#removeById(java.io.Serializable)
	 */
	@Override
	public boolean removeById(ID id) throws RuntimeException {
		logger.debug(">>>>boolean removeById(ID id=[{}]) start", id);

		boolean deleted = getDao().removeById(id);

		logger.debug("<<<<removeById() end.return=[{}]", id);
		return deleted;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.easymeans.common.domain.GenericDmn#removeByIds(ID[])
	 */
	@Override
	public void removeByIds(ID... ids) throws RuntimeException {
		logger.debug(">>>>boolean removeByIds{}(ID id=[{}]) start", "", ids);

		getDao().removeByIds(ids);

		logger.debug("<<<<removeById() end.return");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.easymeans.common.domain.GenericDmn#removeByIds(ID[])
	 */
	@Override
	public void removeByIdsArray(ID[] ids) throws RuntimeException {
		logger.debug(">>>>boolean removeByIdsArray{}(ID id=[{}]) start", "", ids);

		getDao().removeByIds(ids);

		logger.debug("<<<<removeByIdsArray() end.return");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.easymeans.common.domain.GenericDmn#removeByProperty(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public void removeByProperty(String propertyName, Object propertyValue) {
		logger.debug(">>>>void removeByProperty(String propertyName=[{}], Object propertyValue=[{}]') start",
				propertyName, propertyValue);

		getDao().removeByProperty(propertyName, propertyValue);

		logger.debug("<<<<removeByProperty() end.return");
	}

	@Override
	public void removeByProperty(String[] propertyNames, Object[] propertyValues) {
		logger.debug(">>>>void removeByProperty(String propertyName=[{}], Object propertyValue=[{}]') start",
				propertyNames, propertyValues);

		getDao().removeByProperties(propertyNames, propertyValues);

		logger.debug("<<<<removeByProperty() end.return");
	}

	@Override
	public void removeAll() {
		getDao().removeAll();
	}

	@Override
	public void remove(String hql, Object... params) {
		getDao().remove(hql, params);

	}

	@Override
	public String getMessage(HttpServletRequest request, String code) {
		Locale locale = RequestContextUtils.getLocale(request);
		return messageSource.getMessage(code, null, locale);
	}

	@Override
	public String getMessage(HttpServletRequest request, String code, Object[] args) {
		Locale locale = RequestContextUtils.getLocale(request);
		return messageSource.getMessage(code, args, locale);
	}

	@Override
	public void modify(String hql) {

		Session session = getDao().getSession();
		Query query = session.createQuery(hql);
		query.executeUpdate();

	}

	@Override
	public void update(String hql) {
		getDao().update(hql);
	}

	@Override
	public void update(String hql, Object... values) {
		getDao().update(hql, values);
	}

	@Override
	public void batchSave(T... entities) {

		getDao().saveEntities(entities);
	}
	@Override
	public void batchSaveOrUpdate(Collection<T> entities){
		getDao().saveOrUpdate(entities);
	}
	@Override
	public T saveOrUpdate(T entity){
		
		return getDao().saveOrUpdate(entity);
	}

	@Override
	public Pagination query(int pageNo, int pageSize, String sortProperty, boolean isAsc, T entity,
			String[] properties) {

		return this.getDao().find(pageNo, pageSize, sortProperty, isAsc, entity, properties);
	}
	@Override
	public Pagination findByNameMatch(int pageNo, int pageSize, String[]properties, String keyword, MatchMode matchMode) {
		
		return this.getDao().findByPropertiesMatch(pageNo, pageSize, properties, keyword, matchMode);
	}
}
