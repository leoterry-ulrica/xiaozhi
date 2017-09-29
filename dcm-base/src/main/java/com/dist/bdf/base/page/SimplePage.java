package com.dist.bdf.base.page;

/**
 * SimplePage是一个简单的分页类，实现了通用分页接口Paginable。SimplePage只提供对
 * 当前页码、记录总数、分页大小的处理，并不包括分页数据的处理。
 * 
 * @author 李其云
 * @version V1.0, 2013-7-23
 */
public class SimplePage implements Paginable {
    /**
     * 默认的分页大小
     */
    public static final int DEF_COUNT = 20;


    protected boolean lastPage = false;
    /**
     * 分页数据总记录数
     */
    protected int totalCount;
    /**
     * 分页大小
     */
    protected int pageSize = DEF_COUNT;
    /**
     * 当前分页号
     */
    protected int pageNo = 1;
    
    protected Integer start;

    /**
     * 
     */
    public SimplePage() {
        super();
    }

    /**
     * SimplePage的构造器，主要用于调整传入参数。
     * 
     * @param pageNo
     *            当前页码
     * @param pageSize
     *            分页大小
     * @param totalCount
     *            总记录数
     */
    public SimplePage(int pageNo, int pageSize, int totalCount) {
        super();
        setTotalCount(totalCount);
        setPageSize(pageSize);
        setPageNo(pageNo);
        adjustPageNo();
    }

    public SimplePage(int pageNo, int pageSize, int totalCount, boolean lastPage) {
        super();
        setTotalCount(totalCount);
        setPageSize(pageSize);
        setPageNo(pageNo);
        adjustPageNo();
        this.lastPage = lastPage;
    }
    /**
     * 用于处理页码。如果页码为空，或者小于1时，会造成分页程序处理 出现错误，当传入页码为空，或者小于1时，该方法返回固定值1，否则直接返回传入页码。
     * 
     * @param pageNo
     *            调整后的当前页码
     * @return
     */
    public static int cpn(Integer pageNo) {
        return (pageNo == null || pageNo < 1) ? 1 : pageNo;
    }

    /**
     * 用于调整当前页码，使当前页码不超过最大页数。
     * <p>
     * 该方法在构造函数SimplePage(int pageNo, int pageSize, int totalCount)中调用。 比如用
     * 使用SimplePage构造函数时传入的pageNo（当前页码）是10，但是最大页数是9，分页中不允许
     * 当前页码大于最大页数。所以需要使用该方法将10调整为9。
     * 
     */
    public final void adjustPageNo() {
        if (pageNo == 1) {
            return;
        }
        int tp = getTotalPage();
        if (pageNo > tp) {
            pageNo = tp;
        }
        
    }

    /**
     * 用于调整总记录数，使总记录数不能小于0。
     * <p>
     * 该方法在构造函数SimplePage(int pageNo, int pageSize, int totalCount)中调用。
     * 比如用使用SimplePage构造
     * 函数时传入的totalCount（总记录数）是-1，但是在分页中不允许总记录数小于0。所以需要使用该方法-1调整为0。
     * 
     * @param totalCount
     *            调整后的总记录数
     */
    public final void setTotalCount(int totalCount) {
        if (totalCount < 0) {
            this.totalCount = 0;
        } else {
            this.totalCount = totalCount;
        }
    }

    /**
     * 用于调整分页大小，使分页大小不能于0。 如果小于0，则调整为默认常量值：DEF_COUNT。
     * <p>
     * 该方法在构造函数SimplePage(int pageNo, int pageSize, int totalCount)中调用。
     * 比如用使用SimplePage构造
     * 函数时传入的pageSize（分页大小）是-1，但是在分页中不允分页大小小于1。所以需要使用该方法-1调整为0。
     * 
     * @param pageSize
     *            调整后的分页大小
     */
    public final void setPageSize(int pageSize) {
        if (pageSize < 1) {
            this.pageSize = DEF_COUNT;
        } else {
            this.pageSize = pageSize;
        }
    }

    /**
     * 用于调整当前页号，使当前页号不能小于1。如果小于1，则调整为1。
     * <p>
     * 该方法在构造函数SimplePage(int pageNo, int pageSize, int totalCount)中调用。
     * 比如用使用SimplePage构造 函数时传入的pageNo（当前页号）是0，但是在分页中不允许当前页号小于1。所以需要使用该方法-1调整为0。
     * 
     * @param pageNo
     */
    public final void setPageNo(int pageNo) {
        if (pageNo < 1) {
            this.pageNo = 1;
        } else {
            this.pageNo = pageNo;
        }
    }

    /**
     * 获得页码
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * 每页几条数据
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 总共几条数据
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * 总共几页
     */
    public final int getTotalPage() {
        int totalPage = totalCount / pageSize;
        if (totalPage == 0 || totalCount % pageSize != 0) {
            totalPage++;
        }
        return totalPage;
    }

    /**
     * 是否第一页
     */
    public boolean isFirstPage() {
        return pageNo <= 1;
    }

    /**
     * 是否最后一页
     */
    public boolean isLastPage() {
    	if(-1 == this.totalCount){
    		return lastPage;
    	}
        return pageNo >= getTotalPage();
    }

    /**
     * 下一页页码
     */
    public int getNextPage() {
        if (isLastPage()) {
            return pageNo;
        } else {
            return pageNo + 1;
        }
    }

    /**
     * 上一页页码
     */
    public int getPrePage() {
        if (isFirstPage()) {
            return pageNo;
        } else {
            return pageNo - 1;
        }
    }
}
