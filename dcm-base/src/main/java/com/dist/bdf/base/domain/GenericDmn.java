/*
 * Copyright：上海数慧系统技术有限公司 2014
 */
package com.dist.bdf.base.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dist.bdf.base.dao.GenericDAO;
import com.dist.bdf.base.dao.Sort;
import com.dist.bdf.base.dao.Updater;
import com.dist.bdf.base.dto.DateFilterRangeDTO;
import com.dist.bdf.base.page.Pagination;

/**
 * 通用领域对象接口，支持泛型，所有领域对象均应继承本接口。
 * <p>
 * 注意：所有的findByProperty方法都是精确查询，即查询的是PropertyName属性的值完全与PropertyValue相等
 *
 * @author 李其云
 * @param <T>
 *            实体类
 * @param <ID>
 *            实体类的主键类
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public interface GenericDmn<T, ID extends Serializable> {

	// public void setDao(GenericDAO<T, ID> dao);

	GenericDAO<T, ID> getDao();

	/**
	 * 根据实体id载入实体。注意：此方法在GenericDmnImpl中的实现仅仅为了避免需要在所有已实现的Domain类中出现错误，
	 * 应尽量在具体的实现类中覆盖此方法，在覆盖方法中只需如下实现即可：
	 *   @Override
	 *   public Entity loadById(Long id) {
	 *     return loadById(id,"实体名称");
	 *   }
	 * @param id 实体id
	 * @return 如果存在，返回T；否则抛出异常BusinessException
	 */
	public T loadById(ID id);

	/**
	 * 根据实体id载入实体
	 * @param id 实体id
	 * @param entityName 实体名称，用于出现错误是抛出的异常信息中提示实体名称，而不是loadById(Long id)提示的"实体"
	 * @return 如果存在，返回T；否则抛出异常BusinessException
	 */
	public T loadById(ID id, String entityName);

	/**
	 * 根据id查找实体
	 *
	 * @param id
	 * @return 如果id存在，则返回实体，否则返回null
	 */
	public T findById(ID id);

	/**
	 * 根据多个id查找实体
	 *
	 * @param ids
	 *            可变数量的id
	 * @return 实体对象列表。如果查询到的结果为空，则列表的长度为0。
	 */
	public List<T> findByIds(ID... ids);

	/**
	 * 查询所有实体，返回未经排序的实体对象列表
	 *
	 * @return 实体对象列表。如果查询到的结果为空，则列表的长度为0。
	 */
	public List<T> find();

	/**
	 * 查询所有实体，返回未经排序的实体对象列表
	 *
	 * @return 实体对象列表。如果查询到的结果为空，则列表的长度为0。
	 */
	public List<T> find(String hql, Object... params);

	/**
	 * 查询所有实体，返回按照排序属性排序的实体对象列表
	 *
	 * @return 实体对象列表。如果查询到的结果为空，则列表的长度为0。
	 */
	public List<T> find(String sortPropertyName, boolean isAscending);

	/**
	 * 根据指定的页码和数量进行查询
	 *
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            一页数量
	 * @return 分页对象（包含实际页码、一页数量、总数量、未经排序的实体对象列表）
	 */
	public Pagination find(int pageNo, int pageSize);

	/**
	 * 根据指定的页码和数量进行查询
	 *
	 * @param sortPropertyName
	 *            排序属性名称
	 * @param isAscending
	 *            排序是否使用升序，true=升序，false=降序
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            一页数量
	 * @return 分页对象（包含实际页码、一页数量、总数量、按照排序属性排序的实体对象列表）
	 */
	public Pagination find(int pageNo, int pageSize, String sortPropertyName, boolean isAscending);

	/**
	 * 根据单个或者多个字段进行排序，获取相应页码和数量的数据信息
	 *
	 * @author ShenYuTing
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            一页数量
	 * @param sorts
	 *            所需排序的字段及排序方式集合
	 * @return 分页对象（包含实际页码、一页数量、总数量、按照排序属性排序的实体对象列表）
	 */
	public Pagination find(int pageNo, int pageSize, List<Sort> sorts);

	/**
	 * 根据实体的一个属性进行查询，返回按该属性升序排序的对象列表
	 * @param propertyName 属性名称
	 * @param propertyValue 属性值
	 * @return 实体对象列表。如果查询到的结果为空，则列表的长度为0。
	 */
	public List<T> findByProperty(String propertyName, Object propertyValue);

	/**
	 * 根据实体的多个属性进行查询
	 * @param propertyName[] 属性名称数组
	 * @param propertyValue[] 属性值数组
	 * @return 实体对象列表。如果查询到的结果为空，则列表的长度为0。
	 */
	public List<T> findByProperties(String[] propertyNames, Object[] propertyValues);
	
	public List<T> findByProperties(String[] propertyNames, Object[] propertyValues, List<Sort> sorts);
	/**
	 * 根据实体的多个属性进行查询，并可以指定属性排序
	 * @param propertyNames
	 * @param propertyValues
	 * @param sortPropertyName
	 * @param isAscending
	 * @return
	 */
	public List<T> findByProperties(String[] propertyNames, Object[] propertyValues, String sortPropertyName,
			boolean isAscending);

	/**
	 * 根据实体的一个属性进行查询，返回按排序属性排序的分页对象
	 *
	 * @param propertyName
	 *            属性名称
	 * @param propertyValue
	 *            属性值
	 * @param sortPropertyName
	 *            排序属性名称
	 * @param isAscending
	 *            排序是否使用升序，true=升序，false=降序
	 */
	public List<T> findByProperty(String propertyName, Object propertyValue, String sortPropertyName,
			boolean isAscending);
	/**
	 * 指定某个属性，获取同时满足多个有效值的结果
	 * @param propertyName 属性名称
	 * @param propertyValues 属性值集合
	 * @return
	 */
	public List<T> findByProperty(String propertyName, Object[]propertyValues);
	/**
	 * 多个属性，每个属性可能会多个值
	 * @param propertiesValuesMap
	 * @return
	 */
	public List<T> findByProperties(Map<String, Object[]> propertiesValuesMap);

	/**
	 * 根据实体的一个属性进行查询，返回其中的第一个对象
	 *
	 * @param propertyName
	 *            属性名称
	 * @param propertyValue
	 *            属性值
	 * @return 实体对象。如果查询到的结果为空，则返回null。
	 */
	public T findUniqueByProperty(String propertyName, Object propertyValue);

	/**
	 * 根据实体的一组属性进行查询，返回其中的第一个对象
	 *
	 * @param propertyNames
	 *            属性名称集合
	 * @param propertyValues
	 *            属性值集合
	 * @return 实体对象。如果查询到的结果为空，则返回null。
	 */
	public T findUniqueByProperties(String[] propertyNames, Object[] propertyValues);

	/**
	 * 根据实体的一个属性进行查询，返回按排序属性排序的第一个对象
	 *
	 * @param propertyName
	 *            属性名称
	 * @param propertyValue
	 *            属性值
	 * @param sortPropertyName
	 *            排序属性名称
	 * @param isAscending
	 *            排序是否使用升序，true=升序，false=降序
	 */
	public T findUniqueByProperty(String propertyName, Object propertyValue, String sortPropertyName,
			boolean isAscending);

	/**
	 * 是否存在指定属性 值的对象。
	 * 
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 */
	public boolean existsedByProperty(String propertyName, Object propertyValue, boolean isModify);
	/**
	 * 判断指定属性的值是否存在
	 * @param propertyNames
	 * @param propertyValues
	 * @return
	 */
	public boolean existByProperties(String[] propertyNames, Object[] propertyValues);

	/**
	 * 根据实体的一个属性和指定的页码及数量进行查询，返回按该属性升序排序的分页对象
	 *
	 * @param pageNo
	 *            查询页码
	 * @param pageSize
	 *            一页数量
	 * @param propertyName
	 *            属性名称
	 * @param propertyValue
	 *            属性值
	 * @return 分页对象（包含实际页码、一页数量、总数量、实体对象列表）
	 */
	public Pagination findByProperty(int pageNo, int pageSize, String propertyName, Object propertyValue);

	/**
	 * 根据实体的一个属性和指定的页码及数量进行查询，返回按排序属性排序的分页对象
	 *
	 * @param pageNo
	 *            查询页码
	 * @param pageSize
	 *            一页数量
	 * @param propertyName
	 *            属性名称
	 * @param propertyValue
	 *            属性值
	 * @param sortPropertyName
	 *            排序属性名称
	 * @param isAscending
	 *            排序是否使用升序，true=升序，false=降序
	 * @return 分页对象（包含实际页码、一页数量、总数量、实体对象列表）
	 */
	public Pagination findByProperty(int pageNo, int pageSize, String propertyName, Object propertyValue,
			String sortPropertyName, boolean isAscending);
	
    public Pagination findByProperties(int pageNo, int pageSize, Map<String, Object[]> equalProperties, Map<String, Object[]> likeProperties, String orderPropertyName, boolean isAscending);

    public Pagination findByProperty(int pageNo, int pageSize, Map<String, Object[]> propertiesValuesMap, String orderPropertyName, boolean isAscending );
		
    Pagination findByPropertiesAndValues(int pageNo, int pageSize, Map<String,Object[]> propertiesValues,
			String sortPropertyName, boolean isAscending);

	/**
	 * 根据无参型hql进行查询，返回Object列表
	 * @param hql 无参型hql语句，注意：hql可以提取任意属性（由这些属性组成一个Object，当hql中使用select
	 *            new()时，返回值可以是自定义的对象——不一定是entity）
	 * @return Object列表。如果查询到的结果为空，则列表的长度为0。
	 */
	@SuppressWarnings("rawtypes")
	public List query(String hql);

	/**
	 * 根据参数型hql和参数值查询列表
	 * @param hql 包含多个"?序号"的参数型hql语句
	 * @param values 与hql中"?序号"数量相等的并用于替换"?序号"的数值对象
	 * @return Object列表。如果查询到的结果为空，则列表的长度为0。
	 */
	@SuppressWarnings("rawtypes")
	public List query(String hql, Object... values);

	/**
	 * 根据参数型hql和参数值查询,返回指定类型的列表
	 * @param resultClass 必须带有无参构造方法的查询结果类，当hql中select指定的返回字段的别名与resultClass中的属性名相同时，该属性被设置为select字段的值
	 * @param hql 参数型hql语句，注意必须使用select，且字段必须使用as设置别名，同时不能使用new进行返回值构造（返回值由resultClass进行构造）
	 * @param values 与hql中"?序号"数量相等的并用于替换"?序号"的数值对象（注意：必须使用JPA绑定方式，即使用?0、?1、?2的方式，不能使用:paraName的方式）
	 * @return resultClass列表。如果查询到的结果为空，则列表的长度为0。
	 */
	@SuppressWarnings("rawtypes")
	//由于resultClass放在hql之后会导致调用时将resultClass被当成values，因此只能将resultClass放在hql之前
	public List query(Class resultClass, String hql, Object... values);

	/**
	 * 根据参数型hql和参数值查询对象
	 * @param hql 包含多个"?序号"的参数型hql语句
	 * @param values 与hql中"?序号"数量相等的并用于替换"?序号"的数值对象（注意：必须使用JPA绑定方式，即使用?0、?1、?2的方式，不能使用:paraName的方式）
	 * @return Object对象。如果查询到的结果为空，则列表的长度为0。
	 */
	//由于resultClass放在hql之后会导致调用时将resultClass被当成values，因此只能将resultClass放在hql之前
	public Object queryUnique(String hql, Object... values);

	/**
	 * 根据hql和指定的页码及数量进行查询，返回分页对象
	 * @param pageNo 查询页码
	 * @param pageSize  一页数量
	 * @param hql 无参型hql语句，注意：hql一定要返回实体对象
	 * @return 分页对象（包含实际页码、一页数量、总数量、实体对象列表，如果没有查到任何记录，列表的长度为0）
	 */
	public Pagination query(int pageNo, int pageSize, String hql);

	/**
	 * 根据hql和指定的页码及数量进行查询排序，返回分页对象
	 * @param pageNo 查询页码
	 * @param pageSize  一页数量
	 * @param hql 无参型hql语句，注意：hql一定要返回实体对象
	 * @return 分页对象（包含实际页码、一页数量、总数量、实体对象列表，如果没有查到任何记录，列表的长度为0）
	 */
	public Pagination query(int pageNo, int pageSize, String sortPropertyName, String ascending, String hql);

	/**
	 * 根据参数型hql和参数值进行分页查询
	 * @param pageNo 查询页码
	 * @param pageSize  一页数量
	 * @param hql 包含多个"?序号"的参数型hql语句
	 * @param values 与hql中"?序号"数量相等的并用于替换"?序号"的数值对象（注意：必须使用JPA绑定方式，即使用?0、?1、?2的方式，不能使用:paraName的方式）
	 * @return 分页对象（包含实际页码、一页数量、总数量、实体对象列表，如果没有查到任何记录，列表的长度为0）
	 */
	public Pagination query(int pageNo, int pageSize, String hql, Object... values);

	/**
	 * 根据参数型hql和参数值进行分页查询，并将数据封装为所需的类型
	 * @param pageNo 页码
	 * @param pageSize 每页大小
	 * @param resultClass 必须带有无参构造方法的查询结果类，当hql中select指定的返回字段的别名与resultClass中的属性名相同时，该属性被设置为select字段的值
	 * @param hql 包含多个"?序号"的参数型hql语句，注意必须使用select，且字段必须使用as设置别名，同时不能使用new进行返回值构造（返回值由resultClass进行构造）
	 * @param values 与hql中"?序号"数量相等的并用于替换"?序号"的数值对象（注意：必须使用JPA绑定方式，即使用?0、?1、?2的方式，不能使用:paraName的方式）
	 * @return 分页对象（包含实际页码、一页数量、总数量、returnClass列表，如果没有查到任何记录，列表的长度为0）
	 */
	@SuppressWarnings("rawtypes")
	//由于resultClass放在hql之后会导致调用时将resultClass被当成values，因此只能将resultClass放在hql之前
	public Pagination query(int pageNo, int pageSize, Class returnClass, String hql, Object... values);

	/**
	 * 通过指定的SQL进行查询
	 *
	 * @author DuYueLi
	 * @param sql
	 *            SQL语句
	 * @return 集合
	 */
	@SuppressWarnings("rawtypes")
	public List queryBySql(String sql);

	/**
	 * 通过指定的SQL进行查询并分页，该SQL不能自带分页的代码
	 *
	 * @author DuYueLi
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页条数
	 * @param sql
	 *            SQL语句
	 * @return 分页对象（包含实际页码、一页数量、总数量、Object集合，如果没有查到任何记录，集合的长度为0）
	 */
	public Pagination queryBySql(int pageNo, int pageSize, String sql);
	/**
	 * 
	 */
	public Pagination queryBySql(int pageNo, int pageSize, String sql, Class clazz);

	/**
	 * 根据实体对象添加一个持久化实体
	 * 
	 * @param entity
	 *            实体对象
	 * @return 保存后的实体对象
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public T add(T entity) throws RuntimeException;

	/**
	 * 根据实体对象更新一个持久化实体
	 * 
	 * @param entity
	 *            实体对象
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public T modify(T entity) throws RuntimeException;

	/**
	 * 根据实体对象更新一个持久化实体
	 *
	 * @param updater
	 *            实体对象
	 * @return 保存后的实体对象
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public T modify(Updater<T> updater);

	/**
	 * 根据id删除实体
	 *
	 * @param id
	 * @return
	 * @throws RuntimeException
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean removeById(ID id) throws RuntimeException;

	/**
	 * 删除实体
	 * 
	 * @param entity
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void remove(T entity);
	@Transactional(propagation = Propagation.REQUIRED)
	public void remove(Collection<T> entities);

	/**
	 * 根据对各id删除实体
	 *
	 * @param ids
	 *            可变数量的id
	 * @throws RuntimeException
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void removeByIds(ID... ids) throws RuntimeException;

	/**
	 * 根据对各id删除实体
	 *
	 * @param ids
	 *            id数组
	 * @throws RuntimeException
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void removeByIdsArray(ID[] ids) throws RuntimeException;

	/**
	 * 删除属性值为指定值的实体
	 *
	 * @param propertyName
	 *            属性名称
	 * @param propertyValue
	 *            属性值
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void removeByProperty(String propertyName, Object propertyValue);

	@Transactional(propagation = Propagation.REQUIRED)
	public void removeByProperty(String[] propertyNames, Object[] propertyValues);

	/**
	 * 删除全部数据
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void removeAll();

	/**
	 * 删除全部数据
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void remove(String hql, Object... params);

	/**
	 * 根据国际化的properties资源文件中的key获取对应的value信息。
	 * 
	 * @author heshun
	 * @param request
	 *            请求对象
	 * @param code
	 *            properties中的key
	 * @return properties中的value
	 */
	public String getMessage(HttpServletRequest request, String code);

	/**
	 * 根据国际化的properties资源文件中的key获取对应的value信息，并用传入参数替换value中的占位符。
	 * 
	 * @author heshun
	 * @param request
	 *            请求对象
	 * @param code
	 *            properties中的key
	 * @param args
	 *            需要替换的参数
	 * @return properties中的value
	 */
	public String getMessage(HttpServletRequest request, String code, Object[] args);

	/**
	 * 根据hql执行修改操作
	 * 
	 * @param hql
	 *            hql语句 例如“update YourEntityName set entityAttribute = XX where
	 *            condition = XX";
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void modify(String hql);

	/**
	 * 批量保存实体对象
	 * 
	 * @param entities
	 *            created by weifj, 2015-06-07
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void batchSave(T... entities);
	/**
	 * 批量保存或更新实体
	 * @param entities
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void batchSaveOrUpdate(Collection<T> entities);

	/**
	 * 保存或更新
	 * @param entity
	 * @author weifj，2016/03/30
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public T saveOrUpdate(T entity);
	/**
	 * 分页查询对象列表
	 * @param pageParam 分页参数
	 * @param sortProperty 排序属性
	 * @param isAsc 是否正序
	 * @param entity 实体
	 * @param properties 实体属性
	 * @return created by weifj, 2015-06-08
	 */
	public Pagination query(int pageNo, int pageSize, String sortProperty, boolean isAsc, T entity,
			String[] properties);

	/**
	 * 使用hql语句进行更新（修改或者删除）
	 * @param hql 执行更新的无参型hql语句，注意：是执行修改还是删除取决于hql中使用的是update还是delete
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void update(String hql);

	/**
	 * 使用hql语句进行更新（修改或者删除）
	 * @param hql 执行更新的无参型hql语句，注意：是执行修改还是删除取决于hql中使用的是update还是delete
	 * @param values 需要绑定到hql中参数的值（注意：必须使用JPA绑定方式，即使用?0、?1、?2的方式，不能使用:paraName的方式）
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void update(String hql, Object... values);

	Pagination findByProperties(int pageNo, int pageSize, Map<String, Object> equalProperties,
			Map<String, Object> orProperties, List<Sort> sorts);

	List<T> findByProperties(Map<String, Object[]> propertiesValuesMap, String sortPropertyName, boolean isAscending);
	
	public Pagination findByProperties(Integer pageNo, Integer pageSize, Map<String, Object[]> equalProperties,
			Map<String, Object[]> likeProperties, Map<String, Object[]> notProperties, String orderPropertyName, boolean isAscending);
	/**
	 * 带有时间区间过滤
	 * @param pageNo
	 * @param pageSize
	 * @param propertiesValues
	 * @param datePropertyName
	 * @param beginTime
	 * @param endTime
	 * @param sortPropertyName
	 * @param isAscending
	 * @return
	 */
	Pagination findByPropertiesAndValues(int pageNo, int pageSize, Map<String, Object[]> propertiesValues,
			String datePropertyName, Date beginTime, Date endTime, String sortPropertyName, boolean isAscending);

	Pagination findByCriteria(Integer pageNo, Integer pageSize, Criteria criteria);

	Criteria createCriteria();
	/**
	 * 任意匹配属性
	 * @param pageNo
	 * @param pageSize
	 * @param properties
	 * @param keyword
	 * @param matchMode 匹配模式，constant
	 * @return
	 */
	Pagination findByNameMatch(int pageNo, int pageSize, String[]properties, String keyword, MatchMode matchMode);
	/**
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param propertiesValues
	 * @param datePropertyFilters 时间范围过滤
	 * @param sortPropertyName
	 * @param isAscending
	 * @return
	 */
	Pagination findByPropertiesAndValues(int pageNo, int pageSize, Map<String, Object[]> propertiesValues,
			List<DateFilterRangeDTO> datePropertyFilters, String sortPropertyName, boolean isAscending);
}
