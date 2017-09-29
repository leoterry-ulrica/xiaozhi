package com.dist.bdf.model.dto.log;

/**
 * 类描述： 操作日志的统计结果javabean
 *
 * @创建人：沈宇汀
 * @创建时间：2015-1-4 上午11:49:20
 */
public class OperationLogStaticticsDTO {

    /**
     * 操作内容
     */
    private String point;

    /**
     * 操作次数
     */
    private Long count;

    /**
     * 平均耗时
     */
    private Long avgTime;

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getAvgTime() {
        return avgTime;
    }

    public void setAvgTime(Long avgTime) {
        this.avgTime = avgTime;
    }
}
