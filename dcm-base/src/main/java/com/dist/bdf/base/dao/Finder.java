package com.dist.bdf.base.dao;

/**
 *
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.Type;

/**
 * HQL语句分页查询
 * 
 * @author 李其云
 * 
 */
public class Finder {

    /**
     * 无参构造器
     */
    protected Finder() {
        hqlBuilder = new StringBuilder();
    }

    /**
     * 带参构造
     * 
     * @param hql
     *            一个hql语句
     */
    protected Finder(String hql) {
        hqlBuilder = new StringBuilder(hql);
    }

    /**
     * 创建一个分页查询对象
     * 
     * @return HQL分页查询对象
     */
    public static Finder create() {
        return new Finder();
    }

    /**
     * 根据参数创建一个分页查询对象
     * 
     * @param hql
     *            字符串类型的HQL语句
     * @return HQL分页查询对象
     */
    public static Finder create(String hql) {
        return new Finder(hql);
    }

    /**
     * 字符串拼接
     * 
     * @param hql
     *            字符串类型的HQL语句
     * @return HQL分页查询对象
     */
    public Finder append(String hql) {
        hqlBuilder.append(hql);
        return this;
    }

    /**
     * 获得原始hql语句
     * 
     * @return hql语句
     */
    public String getOrigHql() {
        return hqlBuilder.toString();
    }

    /**
     * 获得查询数据库记录数的hql语句。
     * 
     * @return hql语句
     */
    public String getRowCountHql() {
        String hql = hqlBuilder.toString();

        int fromIndex = hql.toLowerCase().indexOf(FROM);
        String projectionHql = hql.substring(0, fromIndex);

        hql = hql.substring(fromIndex);
        String rowCountHql = hql.replace(HQL_FETCH, "");

        int index = rowCountHql.indexOf(ORDER_BY);
        if (index > 0) {
            rowCountHql = rowCountHql.substring(0, index);
        }
        return wrapProjection(projectionHql) + rowCountHql;
    }

    /**
     * <p>
     * 获得查询后排序的hql语句
     * </p>
     * <p>
     * 判断排序集合是否为空，若不为空则在原始hql语句中拼接上order by。
     * 遍历List<Sort>集合，取出Sort对象，通过Sort对象去调用toString方法，再依次拼接至已拼接好ORDER_BY的字符串上面；
     * 若为空，直接返回原始hql语句
     * </p>
     * 
     * @param sortList
     *            一个或多个字段排序集合
     */
    public String getSortHql(List<Sort> sortList) {
        StringBuffer hqlBuffer = new StringBuffer(this.getOrigHql());
        if (sortList != null) {
            hqlBuffer.append(" order by ");
            int size = sortList.size();
            for (int i = 0; i < size; i++) {
                hqlBuffer.append(sortList.get(i).toString());
                if (i < size - 1) {
                    hqlBuffer.append(",");
                }
            }
        }
        return hqlBuffer.toString();
    }

    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    /**
     * 是否使用查询缓存
     * 
     * @return 是否使用查询缓存，true=使用，false=不使用
     */
    public boolean isCacheable() {
        return cacheable;
    }

    /**
     * 设置是否使用查询缓存
     * 
     * @param cacheable
     *            传入一个人布尔型为true/false的参数
     */
    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    /**
     * 设置参数
     * 
     * @param param
     *            传入一个人字符串类型的参数
     * @param value
     *            以一个实体对象作为方法的参数
     * @return 返回sql分页查询对象
     * 
     */
    public Finder setParam(String param, Object value) {
        return setParam(param, value, null);
    }

    /**
     * 设置参数与hibernate的Query接口一致。
     * 
     * @param param
     *            传入一个字符串类型的参数
     * @param value
     *            以实体对象作为方法的参数
     * @param type
     *            实体对象
     * @return 返回sql分页查询对象
     */
    public Finder setParam(String param, Object value, Type type) {
        getParams().add(param);
        getValues().add(value);
        getTypes().add(type);
        return this;
    }

    /**
     * 设置参数。与hibernate的Query接口一致。
     * 
     * @param paramMap
     *            以一个存放字符串类型的map集合作为方法参数
     * @return 返回sql分页查询对象
     */
    public Finder setParams(Map<String, Object> paramMap) {
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            setParam(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * 设置参数。与hibernate的Query接口一致。
     * 
     * @param name
     *            字符串类型的
     * @param vals
     * @param type
     *            实体对象
     * @return sql分页查询实体对象
     */
    public Finder setParamList(String name, Collection<Object> vals, Type type) {
        getParamsList().add(name);
        getValuesList().add(vals);
        getTypesList().add(type);
        return this;
    }

    /**
     * 设置参数。与hibernate的Query接口一致。
     * 
     * @param name
     *            参数名
     * @param vals
     *            需为该参数设置的值
     * @return sql分页查询实体对象
     */
    public Finder setParamList(String name, Collection<Object> vals) {
        return setParamList(name, vals, null);
    }

    /**
     * 设置参数。与hibernate的Query接口一致。
     * 
     * @param name
     *            参数名
     * @param vals
     *            以一个任意类型的数组作为方法的第二个参数，为此参数设置的值
     * @param type
     *            以一个Type实体对象作为方法的第三个参数
     * @return sql分页查询实体对象
     */
    public Finder setParamList(String name, Object[] vals, Type type) {
        getParamsArray().add(name);
        getValuesArray().add(vals);
        getTypesArray().add(type);
        return this;
    }

    /**
     * 设置参数，与hibernate的Query接口一致。
     * 
     * @param name
     *            参数名
     * @param vals
     *            以一个任意类型的数组作为方法的第二个参数，为此参数设置的值
     * @return 返回sql分页查询对象
     */
    public Finder setParamList(String name, Object[] vals) {
        return setParamList(name, vals, null);
    }

    /**
     * 将finder中的参数设置到query中。
     * 
     * @param query
     *            一个query实体对象
     * @return 返回一个query实体对象
     */
    public Query setParamsToQuery(Query query) {
        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                if (types.get(i) == null) {
                    query.setParameter(params.get(i), values.get(i));
                } else {
                    query.setParameter(params.get(i), values.get(i), types.get(i));
                }
            }
        }
        if (paramsList != null) {
            for (int i = 0; i < paramsList.size(); i++) {
                if (typesList.get(i) == null) {
                    query.setParameterList(paramsList.get(i), valuesList.get(i));
                } else {
                    query.setParameterList(paramsList.get(i), valuesList.get(i), typesList.get(i));
                }
            }
        }
        if (paramsArray != null) {
            for (int i = 0; i < paramsArray.size(); i++) {
                if (typesArray.get(i) == null) {
                    query.setParameterList(paramsArray.get(i), valuesArray.get(i));
                } else {
                    query.setParameterList(paramsArray.get(i), valuesArray.get(i), typesArray.get(i));
                }
            }
        }
        return query;
    }

    /**
     * 创建一个Query对象
     * 
     * @param s
     *            以一个session实体对象作为方法的参数
     * @return 返回一个Query类型的实体对象
     */
    public Query createQuery(Session s) {
        // 调用SharedSessionContrac接口的createQuery方法，实现传入一个字符串类型的hql语句后，并创建一个Query对象，调用finder类的将参数设置到对象中去的方法
        Query query = setParamsToQuery(s.createQuery(getOrigHql()));
        if (getFirstResult() > 0) {
            query.setFirstResult(getFirstResult());
        }
        if (getMaxResults() > 0) {
            query.setMaxResults(getMaxResults());
        }
        if (isCacheable()) {
            query.setCacheable(true);
        }
        return query;
    }

    /**
     * 查询行数的hql语句封装
     * 
     * @param projection
     *            以一个字符串类型的参数作为方法的参数
     * @return 返回一个封装好的查询行数的语句
     */
    private String wrapProjection(String projection) {
    	//当在select中使用了别名时（Oracle、MySql）会报错，因此换成以下直接使用count(*)的方式
//        if (projection.indexOf("select") == -1) {
            return ROW_COUNT;
//        } else {
//            return projection.replace("select", "select count(") + ") ";
//        }
    }

    /**
     * 获取参数集
     * 
     * @return 返回一个字符串类型的list集合
     */
    private List<String> getParams() {
        if (params == null) {
            params = new ArrayList<String>();
        }
        return params;
    }

    /**
     * 获取值的集合
     * 
     * @return 返回一个list集合
     */
    private List<Object> getValues() {
        if (values == null) {
            values = new ArrayList<Object>();
        }
        return values;
    }

    /**
     * 获取类型
     * 
     * @return 返回一个Type对象集合
     */
    private List<Type> getTypes() {
        if (types == null) {
            types = new ArrayList<Type>();
        }
        return types;
    }

    /**
     * 
     * @return
     */
    private List<String> getParamsList() {
        if (paramsList == null) {
            paramsList = new ArrayList<String>();
        }
        return paramsList;
    }

    private List<Collection<Object>> getValuesList() {
        if (valuesList == null) {
            valuesList = new ArrayList<Collection<Object>>();
        }
        return valuesList;
    }

    private List<Type> getTypesList() {
        if (typesList == null) {
            typesList = new ArrayList<Type>();
        }
        return typesList;
    }

    private List<String> getParamsArray() {
        if (paramsArray == null) {
            paramsArray = new ArrayList<String>();
        }
        return paramsArray;
    }

    private List<Object[]> getValuesArray() {
        if (valuesArray == null) {
            valuesArray = new ArrayList<Object[]>();
        }
        return valuesArray;
    }

    private List<Type> getTypesArray() {
        if (typesArray == null) {
            typesArray = new ArrayList<Type>();
        }
        return typesArray;
    }

    private StringBuilder hqlBuilder;

    private List<String> params;
    private List<Object> values;
    private List<Type> types;

    private List<String> paramsList;
    private List<Collection<Object>> valuesList;
    private List<Type> typesList;

    private List<String> paramsArray;
    private List<Object[]> valuesArray;
    private List<Type> typesArray;

    private int firstResult = 0;

    private int maxResults = 0;

    private boolean cacheable = false;

    public static final String ROW_COUNT = "select count(*) ";
    public static final String FROM = "from";
    public static final String DISTINCT = "distinct";
    public static final String HQL_FETCH = "fetch";
    public static final String ORDER_BY = "order";

    public static void main(String[] args) {
        Finder find = Finder.create("select distinct p FROM BookType join fetch p");
        System.out.println(find.getRowCountHql());
        System.out.println(find.getOrigHql());
    }

}