package com.dist.bdf.base.dao;


/**
 * <p>
 * 排序封装类
 * </p>
 * 此类用于将所选择的列表中字段的排序方式（升序/降序）封装成特定的字符串格式，如“name desc,age asc”。
 * @author 李其云
 */
public class Sort {
    /**
     * 排序的字段
     */
    private String orderPropertyName;

    /**
     * 排序方式（DESC/ASC），可通过Sort.ASC或Sort.DESC获取
     */
    private String orderPropertyWay;
    /**
     * 排序中升序常量，从小到大
     */
    public static final String ASC = "asc";
    /**
     * 排序中降序常量，从大到小
     */
    public static final String DESC = "desc";

    public String getOrderPropertyWay() {
        return orderPropertyWay;
    }

    public String getOrderPropertyName() {
        return orderPropertyName;
    }

    /**
     * 构造函数
     * 
     * @param orderPropertyName
     *            排序字段
     * @param orderPropertyWay
     *            排序方式，可通过Sort.ASC或Sort.DESC获取
     */
    public Sort(String orderPropertyName, String orderPropertyWay) {
        super();
        if (orderPropertyName == null) {
            throw new RuntimeException("未指定所需排序的列");
        } else {
            this.orderPropertyName = orderPropertyName;
        }
        if (orderPropertyWay == null) {
            throw new RuntimeException("未指定排序方式（升序/降序）");
        } else if (!Sort.DESC.equals(orderPropertyWay) && !Sort.ASC.equals(orderPropertyWay)) {
            throw new RuntimeException("排序方式错误，应为asc/desc");
        } else {
            this.orderPropertyWay = orderPropertyWay;
        }
    }

    /**
     * <p>
     * 将所需排序的字段封装为字符串
     * </p>
     * 判断orderPropertyName和orderPropertyWay是否为空，如果两者均不为空，则拼接字符串； 否则返回业务信息处理类信息
     * 
     * @return 封装成sql中的order by 语句之后部分的字符串
     */
    public String toString() {
        StringBuffer order = new StringBuffer();
        System.out.println("ad");
        if (this.orderPropertyName == null || "".equals(this.orderPropertyName)) {
            throw new RuntimeException("未指定所需排序的列");
        } else if (this.orderPropertyWay == null || "".equals(this.orderPropertyWay)) {
            throw new RuntimeException("未指定排序方式（升序/降序）");
        } else if (!this.orderPropertyWay.equals(Sort.ASC) && !this.orderPropertyWay.equals(Sort.DESC)) {
            throw new RuntimeException("排序方式错误，应为asc/desc");
        }
        return order.append(this.orderPropertyName).append(" ").append(this.orderPropertyWay).toString();
    }

}
