package com.dist.bdf.base.page;

import java.util.List;

import com.dist.bdf.base.utils.JSONUtil;

/**
 * Pagination是分页类。Pagination继承了SimplePage，故Pagination拥有了
 * 处理当前页码、记录总数、分页大小的能力，同时Pagination本身提供了对分页数据的处理，有了这
 * 能力，Pagination就可以完成一个常用的分页业务，在本系统中，对于分页的操作，通常是由Pagination 来完成的。
 * <p>
 * 比如GenericDmn提供了分页方法：public Pagination find(int pageNo, int pageSize)。这里就定义了
 * 该方法返回值是Pagination。
 * 
 * @author 李其云
 * @version V1.0, 2013-7-23
 */
public class Pagination extends SimplePage implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 当前页的数据
     */
    private List<?> data;

    protected Integer start;

    /**
     * 默认构造器
     */
    public Pagination() {
        super();
        this.start = this.getFirstResult();
    }

    /**
     * 构造器
     * 
     * @param pageNo
     *            页码
     * @param pageSize
     *            每页几条数据
     * @param totalCount
     *            总共几条数据
     */
    public Pagination(int pageNo, int pageSize, int totalCount) {
        super(pageNo, pageSize, totalCount);
        this.start = this.getFirstResult();
    }
    
    public Pagination(int pageNo, int pageSize, int totalCount, boolean lastPage,  List<?> list) {
        super(pageNo, pageSize, totalCount, lastPage);
        this.start = this.getFirstResult();
        this.data = list;
    }

    /**
     * 构造器
     * 
     * @param pageNo
     *            页码
     * @param pageSize
     *            每页几条数据
     * @param totalCount
     *            总共几条数据
     * @param list
     *            分页内容
     */
    public Pagination(int pageNo, int pageSize, int totalCount, List<?> list) {
        super(pageNo, pageSize, totalCount);
        this.data = list;
    }

    /**
     * 用于获得当前页数据中的开始数据所在总数据中的位置。
     * <p>
     * 例如分页数据50条，分页大小是5，第3页数据的开始数据所在的位置就是(3-1) * 5 = 10， 使用该方法获取的值就是10。
     * <p>
     * 该方法主要用于org.hibernate.Query接口的setFirstResult()。
     * <p>
     * 注意：数据位置是从0开始的！
     * 
     * @author heshun
     * @return 当前页数据中的开始数据所在总数据中的位置
     */
    public int getFirstResult() {
        return (pageNo - 1) * pageSize;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    /**
     * 转成JSON字符串
     * @return
     */
    public String toJsonString(){
    	return JSONUtil.toJSONString(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Pagination totalCount=");
        builder.append(totalCount);
        builder.append(", pageSize=");
        builder.append(pageSize);
        builder.append(", pageNo=");
        builder.append(pageNo);
        builder.append(", [data=");
        builder.append(data);
        builder.append("]");
        return builder.toString();
    }
}
