package com.dist.bdf.model.entity.log;

import com.dist.bdf.base.entity.BaseEntity;

import org.hibernate.LobHelper;
import org.hibernate.annotations.Type;
import org.hibernate.jpa.internal.util.LogHelper;

import javax.persistence.*;
import javax.sql.rowset.serial.SerialClob;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Date;

/**
 * 类描述： 用于记录 异常日志的实体类。
 *
 * @创建人：沈宇汀
 * @创建时间：2015-1-4 下午1:38:49
 */
@Entity
@Table(name = "T_EXCEPTION_LOG")
public class ExceptionLog extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = -4240934255308514706L;

    /**
     * 异常发生时间
     */
    @Column(name = "DATE_DATEHAPPEN")
    private Date dateHappen;

    /**
     * 异常名称
     */
    @Column(name = "EXCEPTION_NAME")
    private String exceptionName;

    /**
     * 异常内容
     */
    @Column(name = "EXCEPTION_CONTENT", nullable = false)
    @Type(type = "text")
    private String exceptionContent;

    public Date getDateHappen() {
        return dateHappen;
    }

    public void setDateHappen(Date dateHappen) {
        this.dateHappen = dateHappen;
    }

    public String getExceptionName() {
        return exceptionName;
    }

    public void setExceptionName(String exceptionName) {
        this.exceptionName = exceptionName;
    }

    public String getExceptionContent() {
        return exceptionContent;
    }

    public void setExceptionContent(String exceptionContent) {
        this.exceptionContent = exceptionContent;
    }
}
