package com.dist.bdf.base.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;

import com.dist.bdf.base.dto.DateFilterRangeDTO;
import com.dist.bdf.base.page.Pagination;


/**
 * 该接口为数据访问对象，用于对一个特定类型的对象进行数据访问
 * 
 * @author 李其云
 * @param <T>
 *            表示对象的类型，实现该接口的时候，需要提供数据访问的对象
 * @param <ID>
 *            数据访问对象的id,这个实例使用。不同的域对象的id是用于此实例
 */
/**
 * @author Arthur
 *
 * @param <T>
 * @param <ID>
 */
public interface GenericDAO<T, ID extends Serializable> {

	/**
	 * 查询全部实体
	 * 
	 * @return 未排序的实体集合
	 */
	List<T> find();
	/**
	 * 不查找指定条件的实体
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 */
	List<T> notFindByProperty(String propertyName, Object propertyValue);

	/**
	 * 分页查询全部实体，返回其中的一页
	 * 
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页显示数量
	 * @return 分页对象（包含实际页码、一页数量、总数量、未进行排序的的实体列表，如果没有查到任何记录，列表的长度为0）
	 */
	Pagination find(int pageNo, int pageSize);

	/**
	 * 查询以属性名称排序的全部实体
	 * 
	 * @param sortPropertyName
	 *            排序属性名称
	 * @param isAscending
	 *            是否升序，true=升序，false=降序
	 * @return 按排序属性排序的实体集合
	 */
	List<T> find(String sortPropertyName, boolean isAscending);

	/**
	 * 分页查询以属性名称排序的全部实体
	 * 
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页显示数量
	 * @param sortPropertyName
	 *            排序属性名称
	 * @param isAscending
	 *            排序是否使用升序，true=升序，false=降序
	 * @return 分页对象（包含实际页码、一页数量、总数量、按照排序属性排序的实体列表，如果没有查到任何记录，列表的长度为0）
	 */
	Pagination find(int pageNo, int pageSize, String sortPropertyName, boolean isAscending);

	/**
	 * 根据单个或者多个字段进行排序，获取相应页码和数量的数据信息
	 * 
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            一页数量
	 * @param sorts
	 *            所需排序的字段及排序方式集合
	 * @return 分页对象（包含实际页码、一页数量、总数量、按照排序属性排序的实体对象列表）
	 */
	Pagination find(int pageNo, int pageSize, List<Sort> sorts);

	/**
	 * 根据id查询实体对象
	 * 
	 * @param id
	 *            实体对象主键
	 * @return 如果未找到，返回null，否则返回实体对象
	 */
	T find(ID id);

	/**
	 * 根据多个id查询实体
	 * 
	 * @param ids
	 *            变长数量的实体对象主键
	 * @return未 未排序的实体对象集合，如果没有找到任何实体对象，集合的长度为0
	 */
	List<T> find(ID... ids);

	/**
	 * <p>
	 * 参考实体，从数据存储与指定类型和id。得到一个参考实体与指定的类型和id从数据存储中。
	 * <p>
	 * This does not require a call to the datastore and does not populate any
	 * of the entity's values. Values may be fetched lazily at a later time.This
	 * increases performance if a another entity is being saved that should
	 * reference this entity but the values of this entity are not needed.
	 * 
	 * @throws a
	 *             HibernateException if no matching entity is found commented
	 *             by ShenYuTing, not used temporarily, wait
	 *             hibernate-generic-dao upgrade to 1.2.0
	 */
	// public T getReference(ID id);

	/**
	 * <p>
	 * Get a reference to the entities of the specified type with the given ids
	 * from the datastore.
	 * <p>
	 * This does not require a call to the datastore and does not populate any
	 * of the entities' values. Values may be fetched lazily at a later time.
	 * This increases performance if a another entity is being saved that should
	 * reference these entities but the values of these entities are not needed.
	 * 
	 * @throws a
	 *             HibernateException if any of the matching entities are not
	 *             found. commented by ShenYuTing, not used temporarily, wait
	 *             hibernate-generic-dao upgrade to 1.2.0
	 */
	// public T[] getReferences(ID... ids);

	/**
	 * 根据属性名称及其值进行分页查询
	 * 
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页显示数量
	 * @param propertyName
	 *            属性名称
	 * @param propertyValue
	 *            属性值
	 * @return 未排序的实体对象集合。如果未找到任何实体，集合长度为0
	 */
	Pagination findByProperty(int pageNo, int pageSize, String propertyName, Object propertyValue);
	/**
	 * 同时查询相等属性和模糊属性
	 * @param pageNo
	 * @param pageSize
	 * @param equalProperties
	 * @param likeProperties 默认情况下是任意：MatchMode.ANYWHERE
	 * @return
	 */
	Pagination findByProperties(int pageNo, int pageSize, Map<String, Object[]> equalProperties, Map<String, Object[]> likeProperties, String orderPropertyName, Boolean isAscending );

	/**
	 * 模糊查询，以值的前缀进行匹配
	 * @param pageNo
	 * @param pageSize
	 * @param properties
	 * @param startMatchValues
	 * @return
	 */
	List<T> findByPropertiesStartMatch(String[] properties, Object[] startMatchValues);

	/**
	 * 查询属性多个值
	 * @param pageNo
	 * @param pageSize
	 * @param propertiesValuesMap
	 * @param orderPropertyName
	 * @param isAscending
	 * @return
	 */
	Pagination findByProperty(int pageNo, int pageSize, Map<String, Object[]> propertiesValuesMap, String orderPropertyName, boolean isAscending );

	/**
	 * 根据属性名称及其值进行分页查询，并根据排序属性名称进行排序
	 * 
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页显示数量
	 * @param propertyName
	 *            属性名称
	 * @param propertyValue
	 *            属性值
	 * @param orderPropertyName
	 *            排序属性名称
	 * @param isAscending
	 *            排序是否使用升序，true=升序，false=降序
	 * @return 按排序属性排序的实体对象集合。如果未找到任何实体，则集合长度为0
	 */
	Pagination findByProperty(int pageNo, int pageSize, String propertyName, Object propertyValue,
			String orderPropertyName, boolean isAscending);

	
    Pagination findByPropertiesAndValues(int pageNo, int pageSize, Map<String, Object[]> propertiesValuesMap,
			String sortPropertyName, boolean isAscending);
	/**
	 * 根据属性名称及其值进行查询
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param propertyValue
	 *            属性值
	 * @return 未排序的实体对象集合。如果未找到任何实体，则集合长度为0
	 */
	List<T> findByProperty(String propertyName, Object propertyValue);
	/**
	 * 根据属性名称和值进行查询，忽略大小写
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 */
	List<T> findByPropertyIgnoreCase(String propertyName, Object propertyValue);
	/**
	 * 根据属性名称集合及其值集合进行查询
	 * @param propertyNames 属性名称集合
	 * @param propertyValues 属性值集合
	 * @return 未排序的实体对象集合。如果未找到任何实体，则集合长度为0
	 */
	List<T> findByProperties(String[] propertyNames, Object[] propertyValues);
	/**
	 * 多个属性，每个属性可能会多个值
	 * @param propertiesValuesMap
	 * @return
	 */
	List<T> findByProperties(Map<String, Object[]> propertiesValuesMap);
	/**
	 * 根据属性名称集合及其值集合进行查询，并指定属性排序
	 * @param propertyNames
	 * @param propertyValues
	 * @param sortPropertyName
	 * @param isAscending
	 * @return
	 */
    List<T> findByProperties(String[] propertyNames, Object[] propertyValues, String sortPropertyName,
			boolean isAscending);

	/**
	 * 根据属性名称及其值进行查询，并根据排序属性名称进行排序
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param propertyValue
	 *            属性值
	 * @param orderPropertyName
	 *            排序属性名称
	 * @param isAscending
	 *            排序是否使用升序，true=升序，false=降序
	 * @return 按排序属性名称排序的实体对象集合。如果未找到任何实体，则集合长度为0
	 */
	List<T> findByProperty(String propertyName, Object propertyValue, String orderPropertyName, boolean isAscending);
	/**
	 * 指定某个属性，获取同时满足多个有效值的结果
	 * @param propertyName 属性名称
	 * @param propertyValues 属性值集合
	 * @return
	 */
	List<T> findByProperty(String propertyName, Object...propertyValues);

	/**
	 * 按Criterion查询
	 * 
	 * @param criterions
	 *            数量可变的Criterion.
	 * @return 实体对象集合。如果未找到任何实体，则集合长度为0
	 */
	List<T> findByCriterions(Criterion... criterions);

	/**
	 * 按Criterion分页查询
	 * 
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页显示数量
	 * @param criterions
	 *            数量可变的Criterion.
	 * @return 分页对象（包含实际页码、一页数量、总数量、实体对象列表，如果没有查到任何记录，列表的长度为0）
	 */
	Pagination findByCriterions(int pageNo, int pageSize, Criterion... criterions);

	/**
	 * 根据属性名称及其值进行查询，返回其中的第一个对象
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param propertyValue
	 *            属性值
	 * @return 实体对象。如果未找到任何实体，则返回null
	 */
	T findUniqueByProperty(String propertyName, Object propertyValue);
	/**
	 * 根据属性名称及其值进行查询，忽略大小写，返回其中的第一个对象
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 */
	T findUniqueByPropertyIgnoreCase(String propertyName, Object propertyValue);

	/**
	 * 根据属性名称集合及其值集合进行查询，返回其中的第一个对象
	 * 
	 * @param propertyNames
	 *            属性名称集合
	 * @param propertyValues
	 *            属性值集合
	 * @return 实体对象。如果未找到任何实体，则返回null
	 */
	T findUniqueByProperties(String[] propertyNames, Object[] propertyValues);

	/**
	 * 根据属性名称及其值进行查找，返回按排序属性排序后的第一个对象
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param propertyValue
	 *            属性值
	 * @return 实体对象。如果未找到任何实体，则返回null
	 */
	T findUniqueByProperty(String propertyName, Object propertyValue, String sortPropertyName, boolean isAscending);

	/**
	 * 通过带参数的HQL查询实体对象集合
	 * @param hql 包含多个?number的参数型hql语句
	 * @param values 与hql中?number数量相等的并用于替换?number的数值对象
	 * @return List&lt;T&gt;，如果没有查到任何记录，集合的长度为0
	 */
	T findUnique(String hql, Object... values);

	/**
	 * 通过不带参数的HQL查询对象集合。所有的查询方法均使用session.createQuery()进行实际的查询
	 * 
	 * @param hql
	 *            不带参数的hql语句
	 * @return Object集合，如果没有查到任何记录，集合的长度为0
	 */
	@SuppressWarnings("rawtypes")
	public List query(String hql);

	/**
	 * 带缓存HQL查询
	 * <p>
	 * 通过设置<code>query.setCacheable(true);</code>启用缓存查询
	 * </p>
	 * 
	 * @param hql
	 *            要执行的HQL
	 * @return Object集合，如果没有查到任何记录，集合的长度为0
	 */
	@SuppressWarnings("rawtypes")
	public List queryWithCache(String hql);

	/**
	 * 通过带参数的HQL查询对象集合
	 * @param hql 包含多个{}的参数型hql语句
	 * @param values 与hql中{}数量相等的并用于替换{}的数值对象
	 * @return Object集合，如果没有查到任何记录，集合的长度为0
	 */
	@SuppressWarnings("rawtypes")
	public List query(String hql, Object... values);

	/**
	 * 根据参数型hql和参数值进行查询，并将数据封装为所需的类型
	 * @param resultClass 必须带有无参构造方法的查询结果类，当hql中select指定的返回字段的别名与resultClass中的属性名相同时，该属性被设置为select字段的值
	 * @param hql 参数型hql语句，注意必须使用select，且字段必须使用as设置别名，同时不能使用new进行返回值构造（返回值由resultClass进行构造）
	 * @param values 需要绑定到hql中参数的值（注意：必须使用JPA绑定方式，即使用?0、?1、?2的方式，不能使用:paraName的方式）
	 * @return returnClass列表，如果没有查到任何记录，列表的长度为0
	 */
	@SuppressWarnings("rawtypes")
	public List query(Class resultClass, String hql, Object... values);

	/**
	 * 带缓存的HQL多参数查询
	 * <p>
	 * 通过设置<code>query.setCacheable(true);</code>启用缓存查询
	 * </p>
	 * 
	 * @param hql
	 *            要执行的HQL
	 * @param values
	 *            与hql中{}数量相等的并用于替换{}的数值对象
	 * @return List，如果没有查到任何记录，List的长度为0
	 */
	@SuppressWarnings("rawtypes")
	public List queryWithCache(String hql, Object... values);

	/**
	 * 通过HQL查询唯一对象
	 * 
	 * @param hql
	 *            包含多个{}的参数型hql语句
	 * @param values
	 *            与hql中{}数量相等的并用于替换{}的数值对象
	 * @return 实体对象。如果没有找到任何记录，则返回null
	 */
	Object queryUnique(String hql, Object... values);

	/**
	 * 带缓存的HQL查询唯一对象
	 * <p>
	 * 通过设置<code>query.setCacheable(true);</code>启用缓存查询
	 * </p>
	 * 
	 * @param hql
	 *            包含多个{}的参数型hql语句
	 * @param values
	 *            与hql中{}数量相等的并用于替换{}的数值对象
	 * @return 实体对象。如果没有找到任何记录，则返回null
	 */
	Object queryUniqueWithCache(String hql, Object... values);

	/**
	 * 通过Finder查询对象集合
	 * 
	 * @param finder
	 *            Finder对象
	 * @return Object集合，如果没有查到任何记录，集合的长度为0
	 */
	List<Object> query(Finder finder);

	/**
	 * 通过Finder获得分页数据
	 * 
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页条数
	 * @param finder
	 *            Finder对象
	 * @return 分页对象（包含实际页码、一页数量、总数量、Object集合，如果没有查到任何记录，集合的长度为0）
	 */
	Pagination query(int pageNo, int pageSize, Finder finder);

	/**
	 * 根据查询函数与参数列表创建Query对象,后续可进行更多处理,辅助函数.
	 */
	public Query createQuery(String queryString);

	/**
	 * 根据hql语句创建Query对象,并设置返回类型为resultClass,后续可进行更多处理,辅助函数.
	 */
	@SuppressWarnings("rawtypes")
	public Query createQuery(Class resultClass, String hql);

	/**
	 * 根据查询函数与参数列表创建Query对象,后续可进行更多处理,辅助函数.
	 */
	public Query createQuery(String queryString, Object... values);

	/**
	 * 根据参数型hql语句与参数列表创建Query对象,并设置返回类型为resultClass,后续可进行更多处理,辅助函数.
	 */
	@SuppressWarnings("rawtypes")
	public Query createQuery(Class resultClass, String hql, Object... values);

	/**
	 * 根据查询函数与参数列表创建SQLQuery对象,后续可进行更多处理,辅助函数.
	 */
	SQLQuery createSQLQuery(String queryString, Object... values);

	/**
	 * 根据参数型sql语句与参数列表创建SQLQuery对象,并设置返回类型为resultClass,后续可进行更多处理,辅助函数.
	 */
	@SuppressWarnings("rawtypes")
	SQLQuery createSQLQuery(Class resultClass, String hql, Object... values);

	/**
	 * 获得Finder的记录总数
	 * 
	 * @param finder
	 *            Finder对象
	 * @return 满足条件的记录数
	 */
	int countQueryResult(Finder finder);

	/**
	 * 通过实体对象更新
	 * 
	 * @param entity
	 *            实体对象
	 * @return 实体对象
	 */
	T update(T entity);

	/**
	 * 通过Updater更新。注意：根据实体对象除主键以外的属性进行复制，忽略updater的bean中多余属性
	 * 
	 * @param updater
	 *            更新对象
	 * @return 持久化的实体对象
	 */
	T updateByUpdater(Updater<T> updater);

	/**
	 * 添加实体
	 * <p>
	 * 该实体的id为空，将该实体添加到数据库存储，并为其指定一个id
	 * <p>
	 * 
	 * @param entity
	 *            实体对象
	 * @return 实体对象
	 */
	T save(T entity);

	/**
	 * 循环添加实体对象 循环实体的id为空，将该实体添加到数据库存储，并为其指定一个id
	 * 
	 * @param entities
	 *            多个实体对象
	 * @return object集合
	 */
	List<T> save(T... entities);
	/**
	 * 循环添加实体对象 循环实体的id为空，将该实体添加到数据库存储，并为其指定一个id
	 * 
	 * @param entities
	 *            多个实体对象
	 * @return object集合
	 */
	List<T> save(Collection<T> entities);

	/**
	 * 从数据库存储中删除指定的实体对象
	 * 
	 * @return entity 实体对象
	 * @return 删除成功返回true,删除失败返回false
	 */
	boolean remove(T entity);

	/**
	 * 循环从数据库中删除指定的实体对象
	 * 
	 * @param entities多个实体对象
	 */
	void remove(T... entities);
	/**
	 * 批量删除实体
	 * @param entities
	 */
	void remove(Collection<T> entities);

	/**
	 * 根据id值删除指定对象的实体
	 * 
	 * @param id
	 *            实体对象的id值
	 * 
	 * @return 删除成功返回true,删除失败返回false
	 */
	boolean removeById(ID id);

	/**
	 * 循环根据id删除指定对象的实体
	 * 
	 * @param ids
	 *            多个实体对象的id值
	 */
	void removeByIds(ID... ids);

	/**
	 * 根据属性名称和属性值进行删除
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param propertyValue
	 *            属性值
	 */
	void removeByProperty(String propertyName, Object propertyValue);
	/**
	 * 根据属性的多个值删除
	 * @param propertyName
	 * @param propertyValue
	 */
	void removeByProperty(String propertyName, Object[] propertyValue);
	
	void removeByProperties(Map<String, Object[]> propertiesValues);

	void removeByProperties(String[] propertyNames, Object[] propertyValues);

	/**
	 * 通过原生态的SQL语句删除数据
	 * @param sql
	 */
	void removeBySql(String sql);

	/**
	 * 删除全部数据
	 */
	void removeAll();

	/**
	 * Search for entities given the search parameters in the specified
	 * <code>ISearch</code> object.
	 * 
	 * @param RT
	 *            The result type is automatically determined by the context in
	 *            which the method is called. commented by ShenYuTing, wait
	 *            hibernate-generic-dao upgrade to 1.2.0
	 */
	// public <RT> List<RT> search(ISearch search);

	/**
	 * Search for a single entity using the given parameters.
	 * 
	 * @param RT
	 *            The result type is automatically determined by the context in
	 *            which the method is called. commented by ShenYuTing, wait
	 *            hibernate-generic-dao upgrade to 1.2.0
	 */
	// public <RT> RT searchUnique(ISearch search);

	/**
	 * Returns the total number of results that would be returned using the
	 * given <code>ISearch</code> if there were no paging or maxResults limits.
	 * commented by ShenYuTing, wait hibernate-generic-dao upgrade to 1.2.0
	 */
	// public int count(ISearch search);

	/**
	 * Returns a <code>SearchResult</code> object that includes both the list of
	 * results like <code>search()</code> and the total length like
	 * <code>count()</code>.
	 * 
	 * @param RT
	 *            The result type is automatically determined by the context in
	 *            which the method is called. commented by ShenYuTing, wait
	 *            hibernate-generic-dao upgrade to 1.2.0
	 */
	// public <RT> SearchResult<RT> searchAndCount(ISearch search);

	/**
	 * 查询指定实体对象所对应的session是否存在
	 * 
	 * @return 存在返回true,不存在返回false
	 */
	boolean isAttached(T entity);

	/**
	 * 刷新获取到的实体对象在数据库的状态 Refresh the content of the given entity from the
	 * current datastore state. T
	 */
	void refresh(T... entities);

	/**
	 * 刷新在Hibernate会话的数据存储
	 */
	void flush();

	/**
	 * Generates a search filter from the given example using default options.
	 * commented by ShenYuTing, wait hibernate-generic-dao upgrade to 1.2.0
	 */
	// public Filter getFilterFromExample(T example);

	/**
	 * Generates a search filter from the given example using the specified
	 * options. commented by ShenYuTing, wait hibernate-generic-dao upgrade to
	 * 1.2.0
	 */
	// public Filter getFilterFromExample(T example, ExampleOptions options);

	/**
	 * 更新或保存单个实体
	 * 
	 * 利用框架中数据持久化层提供的方法执行保存或更新操作 该方法需要提供一个实体对象，持久化层会自动去判断对象中是否有主键
	 * 如果对象中主键有值则执行更新操作，主键没有值则执行保存操作
	 * 
	 * @param entity
	 *            实体对象
	 */
	T saveOrUpdate(T entity);
	/**
	 * 批量添加或更新
	 * @param collection
	 * @return
	 */
	boolean saveOrUpdate(Collection<T> entities);

	/**
	 * 保存实体
	 * 
	 * 利用框架中数据持久化层提供的方法执行保存，该方法的返回值为Serializable对象 若该对象不为空则保存成功；若该对象为空则保存失败
	 * 
	 * @param entity
	 *            实体对象
	 * @return 保存成功返回true，保存失败返回false
	 */
	boolean saveEntity(T entity);

	/**
	 * 批量保存
	 * 
	 * 循环调用返回值为boolean的单个实体对象保存方法
	 * 循环过程中若有一个单实体对象保存方法的返回值为false，则整个批量保存失败；反之则批量保存成功
	 * 
	 * @param entities
	 *            多个实体对象
	 * @return 批量保存成功返回true，批量保存失败返回false
	 */
	boolean saveEntities(T... entities);

	/**
	 * 在实现方法中使用try...catch，利用框架中数据持久化层提供更新方法执行更新操作 若捕捉到异常，说明更新失败，反之则更新成功
	 * 
	 * @param entity
	 *            实体对象
	 * @return 更新成功返回true，更新失败返回false
	 */
	boolean updateEntity(T entity);

	/**
	 * 循环调用返回值为boolean的单个实体对象更新方法
	 * 循环过程中若有一个单实体对象更新方法的返回值为false，则整个批量更新失败;反之则批量更新成功
	 * 
	 * @param entities
	 *            多个实体对象
	 * @return 批量更新成功返回true，批量更新失败返回false
	 */
	boolean updateEntities(T... entities);

	/**
	 * 循环调用根据id值删除指定对象实体且返回值为boolean的删除方法
	 * 循环过程中若有一个单实体对象删除方法的返回值为false，则整个批量删除失败，反之则批量删除成功
	 * 
	 * @param ids
	 *            多个实体对象的id值
	 * @return 删除成功返回true,删除失败返回false
	 */
	boolean remove(ID... ids);

	/**
	 * 获取session
	 * @author heshunwq
	 */
	Session getSession();

	/**
	 * 通过指定的SQL进行查询并分页，该SQL不能自带分页的代码
	 *
	 * @param pageNo 页码
	 * @param pageSize  每页条数
	 * @param sql SQL语句
	 * @return 分页对象（包含实际页码、一页数量、总数量、Object集合，如果没有查到任何记录，集合的长度为0）
	 */
	Pagination queryBySql(int pageNo, int pageSize, String sql);
	/**
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param sql
	 * @param clazz
	 * @return
	 */
	Pagination queryBySql(int pageNo, int pageSize, String sql, Class clazz);

	/**
	 * 通过指定的带参数SQL及参数值进行查询并分页，该SQL不能自带分页的代码
	 *
	 * @param pageNo 页码
	 * @param pageSize  每页条数
	 * @param sql 带有参数的SQL语句
	 * @param values 参数对应的值
	 * @return 分页对象（包含实际页码、一页数量、总数量、Object集合，如果没有查到任何记录，集合的长度为0）
	 */
	Pagination queryBySql(int pageNo, int pageSize, String sql, Object... values);

	/**
	 * 通过数组设置参数进行查询
	 * @param hql 
	 * @param names hql中参数的名称，与hql中带有":"的参数对应
	 * @param values 需要绑定到参数的值，该值可以是任何类型，包括Collection和数组[]
	 * @return
	 */
	List<T> queryByName(String hql, String[] names, Object[] values);

	/**
	 * 通过数组设置参数进行查询
	 * @param resultClass 必须带有无参构造方法的查询结果类，当hql中select指定的返回字段的别名与resultClass中的属性名相同时，该属性被设置为select字段的值
	 * @param hql 
	 * @param names hql中参数的名称，与hql中带有":"的参数对应
	 * @param values 需要绑定到参数的值，该值可以是任何类型，包括Collection和数组[]
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<T> queryByName(Class resultClass, String hql, String[] names, Object[] values);

	/**
	 * 通过键值对设置参数进行查询
	 * @param hql 
	 * @param map 参数名及值组成的Map&lt;String,Object&gt;，其中String为hql中的参数名称，与hql中带有":"的参数对应，Object为需要绑定到参数的值，该值可以是任何类型，包括Collection和数组[]
	 * @return List
	 */
	List<T> queryByName(String hql, Map<String, Object> map);

	/**
	 * 通过键值对设置参数进行查询
	 * @param hql 
	 * @param map 参数名及值组成的Map&lt;String,Object&gt;，其中String为hql中的参数名称，与hql中带有":"的参数对应，Object为需要绑定到参数的值，该值可以是任何类型，包括Collection和数组[]
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	List<T> queryByName(Class resultClass, String hql, Map<String, Object> map);

	/**
	 * 根据hql，进行删除
	 * @param hql
	 * @param params
	 */
	void remove(String hql, Object[] params);

	/**
	 * 分页查询
	 * @param pageNo 页码
	 * @param pageSize 每页大小
	 * @param sortProperty 排序属性
	 * @param isAsc 是否正序
	 * @param valueObject 取值对象，properties中的每一个property对应的值将从此对象中取出
	 * @param properties 需要用于where条件的property
	 * @return
	 * created by weifj, 2015-06-08
	 * modified by 李其云 2015-9-11
	 */
	Pagination find(int pageNo, int pageSize, String sortProperty, boolean isAsc, T valueObject, String[] properties);

	/**
	 * 根据参数型hql和参数值进行分页查询，并将数据封装为所需的类型
	 * @param pageNo 页码
	 * @param pageSize 每页大小
	 * @param resultClass 必须带有无参构造方法的查询结果类，当hql中select指定的返回字段的别名与resultClass中的属性名相同时，该属性被设置为select字段的值
	 * @param hql 参数型hql语句，注意必须使用select，且字段必须使用as设置别名，同时不能使用new进行返回值构造（返回值由resultClass进行构造）
	 * @param values 需要绑定到hql中参数的值（注意：必须使用JPA绑定方式，即使用?0、?1、?2的方式，不能使用:paraName的方式）
	 * @return 分页对象（包含实际页码、一页数量、总数量、returnClass列表，如果没有查到任何记录，列表的长度为0）
	 */
	@SuppressWarnings("rawtypes")
	Pagination query(int pageNo, int pageSize, Class resultClass, String hql, Object... values);

	/**
	 * 指定返回结果类型的分页查询
	 * @param pageNo 页码
	 * @param pageSize 每页大小
	 * @param resultClass 必须带有无参构造方法的查询结果类，当hql中select指定的返回字段的别名与resultClass中的属性名相同时，该属性被设置为select字段的值
	 * @param hql hql语句，注意：必须使用select，且字段必须使用as设置别名，同时不能使用new进行返回值构造（返回值由resultClass进行构造）
	 * @return 分页对象（包含实际页码、一页数量、总数量、returnClass列表，如果没有查到任何记录，列表的长度为0）
	 */
	@SuppressWarnings("rawtypes")
	Pagination query(int pageNo, int pageSize, Class resultClass, String hql);

	/**
	 * 分页查询
	 * @param pageNo 页码
	 * @param pageSize 每页大小
	 * @param hql 
	 * @return 分页对象
	 */
	Pagination query(int pageNo, int pageSize, String hql);

	/**
	 * 分页查询
	 * @param pageNo 页码
	 * @param pageSize 每页大小
	 * @param hql 
	 * @param values 需要绑定到hql中参数的值（注意：必须使用JPA绑定方式，即使用?0、?1、?2的方式，不能使用:paraName的方式）
	 * @return 分页对象
	 */
	/**
	 * 根据参数型hql和参数值进行分页查询
	 * @param pageNo 页码
	 * @param pageSize 每页大小
	 * @param hql 参数型hql语句，注意必须使用select，且字段必须使用as设置别名，同时不能使用new进行返回值构造（返回值由resultClass进行构造）
	 * @param values 需要绑定到hql中参数的值（注意：必须使用JPA绑定方式，即使用?0、?1、?2的方式，不能使用:paraName的方式）
	 * @return 分页对象（包含实际页码、一页数量、总数量、查询结果列表，如果没有查到任何记录，列表的长度为0）
	 */
	Pagination query(int pageNo, int pageSize, String hql, Object... values);

	/**
	 * 根据查找器进行分页查询，返回指定类型对象构成的分页器
	 * @param pageNo 页码
	 * @param pageSize 每页大小
	 * @param resultClass 必须带有无参构造方法的查询结果类
	 * @param finder 查找器
	 * @return 分页对象
	 */
	@SuppressWarnings("rawtypes")
	Pagination query(int pageNo, int pageSize, Class resultClass, Finder finder);

	/**
	 * 使用hql语句进行更新（修改或者删除）
	 * @param hql 执行更新的无参型hql语句，注意：是执行修改还是删除取决于hql中使用的是update还是delete
	 */
	public void update(String hql);

	/**
	 * 使用hql语句进行更新（修改或者删除）
	 * @param hql 执行更新的无参型hql语句，注意：是执行修改还是删除取决于hql中使用的是update还是delete
	 * @param values 需要绑定到hql中参数的值（注意：必须使用JPA绑定方式，即使用?0、?1、?2的方式，不能使用:paraName的方式）
	 */
	public void update(String hql, Object... values);
	/**
	 * 根据排序集合进行全局查询
	 * @param sorts
	 * @return
	 */
	List<T> find(List<Sort> sorts);
	
	Object findByProperties(String[] properties, Object[] values, List<Sort> sorts);
	/**
	 * 添加了or条件，格式示例：A or B or (C and D)，A和B來源于orProperties，C和D來源于equalProperties（有问题，需要继续调试）
	 * @param pageNo
	 * @param pageSize
	 * @param equalProperties
	 * @param orProperties
	 * @param orderPropertyName
	 * @param isAscending
	 * @return
	 */
	@Deprecated
	Pagination findByProperties(int pageNo, int pageSize, Map<String, Object> equalProperties,
			Map<String, Object> orProperties, List<Sort> sorts);
	/**
	 * 
	 * @param propertiesValuesMap
	 * @param orderPropertyName
	 * @param isAscending
	 * @return
	 */
	List<T> findByProperties(Map<String, Object[]> propertiesValuesMap, String orderPropertyName, boolean isAscending);
	/**
	 * 模糊检索
	 * @param properties
	 * @param values
	 * @return
	 */
	List<T> findByPropertiesMatch(String[] properties, Object[] values);
	/**
	 * 分页模糊检索，任意匹配属性
	 * @param pageNo
	 * @param pageSize
	 * @param properties
	 * @param values
	 * @return
	 */
	Pagination findByPropertiesMatch(int pageNo, int pageSize, String[] properties, Object[] values);
	/**
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param equalProperties
	 * @param likeProperties
	 * @param notProperties
	 * @return
	 */
	Pagination findByProperties(int pageNo, int pageSize, Map<String, Object[]> equalProperties,
			Map<String, Object[]> likeProperties, Map<String, Object[]> notProperties, String orderPropertyName, boolean isAscending);
	/**
	 * 以开头匹配
	 * @param propertiesValuesMap
	 * @return
	 */
	List<T> findByPropertiesStartMatch(Map<String, Object[]> propertiesValuesMap);
	Pagination findByPropertiesAndValues(int pageNo, int pageSize, Map<String, Object[]> propertiesValues,
			String datePropertyName, Date beginTime, Date endTime, String sortPropertyName, boolean isAscending);
	
	Pagination findByCriteria(int pageNo, int pageSize, Criteria crit);
	Criteria createCriteria();
	/**
	 * 任意匹配
	 * @param pageNo
	 * @param pageSize
	 * @param properties
	 * @param keyword
	 * @param matchMode 匹配模式，constant
	 * @return
	 */
	Pagination findByPropertiesMatch(int pageNo, int pageSize, String[] properties, String keyword, MatchMode matchMode);
	/**
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param propertiesValues
	 * @param datePropertyFilters
	 * @param sortPropertyName
	 * @param isAscending
	 * @return
	 */
	Pagination findByPropertiesAndValues(int pageNo, int pageSize, Map<String, Object[]> propertiesValues,
			List<DateFilterRangeDTO> datePropertyFilters, String sortPropertyName, boolean isAscending);

}
