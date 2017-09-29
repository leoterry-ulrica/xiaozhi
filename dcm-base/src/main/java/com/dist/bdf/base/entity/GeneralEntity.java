package com.dist.bdf.base.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.sql.Date;

/**
 * 公用的实体，包含创建时间、修改时间、名称等常用字段。
 * @author 何顺
 * @create 2014年12月2日
 */
@SuppressWarnings("serial")
@MappedSuperclass
public class GeneralEntity extends BaseEntity {


    /**
     * 创建日期
     */
    @Column(name = "CREATE_DATE")
    private Date createDate;

    /**
     * 修改日期
     */
    @Column(name = "MODIFY_DATE")
    private Date modifyDate;

    /**
     * 创建者名字
     */
    private String creatorName;
    /**
     * 修改者名字
     */
    private String modifierName;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

}
