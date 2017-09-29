package com.dist.bdf.base.office.meta;

import java.util.ArrayList;
import java.util.List;

import com.dist.bdf.base.office.ErrorLog;

/**
 * 与映射文件中的sheet标签对应，根据映射文件规则，该类应该包含多个bean标签对应的对象。<p>
 * 该类还提供了记录异常信息和查询异常信息的方法。在导入Excel时，如果需要记录错误信息，就需要使用该类的记录异常信息方法记录异常
 * @author heshun
 * @version V1.0, 2013-9-16
 */
public class SheetMeta {

    /**
     * 映射文件中的导入开始属性名字
     */
    public static final String IMPROT_BEGIN = "importBegin";

    /**
     * 映射文件中的导入结束属性名字
     */
    public static final String IMPORT_END = "importEnd";

    /**
     * 映射文件中的导出开始属性名字
     */
    public static final String EXPORT_BEGIN = "exportBegin";

    /**
     * 映射文件中的导出结束行数名字
     */
    public static final String EXPORT_END = "exportEnd";

    /**
     *sheet名字 
     */
    private String name;

    /**
     * 组成sheet的类
     */
    private List<BeanMeta> beans = new ArrayList<BeanMeta>();

    /**
     * 导入结束行
     */
    private int importEnd;

    /**
     * 导入开始行
     */
    private int importBegin;

    /**
     * 导出开始行
     */
    private int exportBegin;

    /**
     * 导入结束行
     */
    private int exportEnd;

    /**
     * 是否导出数据
     */
    private boolean isExportData;

    /**
     * 导入错误的信息。
     */
    private List<ErrorLog> errorLogs = new ArrayList<ErrorLog>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImportEnd() {
        return importEnd;
    }

    public void setImportEnd(int importEnd) {
        this.importEnd = importEnd > 0 ? importEnd : 1;
    }

    public int getImportBegin() {
        return this.importBegin;
    }

    public void setImportBegin(int importBegin) {
        this.importBegin = importBegin > 0 ? importBegin : 1;
    }

    public int getExportBegin() {
        return this.exportBegin;
    }

    public void setExportBegin(int exportBegin) {
        this.exportBegin = exportBegin > 0 ? exportBegin : 1;
    }

    public int getExportEnd() {
        return this.exportEnd;
    }

    public void setExportEnd(int exportEnd) {
        this.exportEnd = exportEnd > 0 ? exportEnd : 1;
    }

    public List<BeanMeta> getBeans() {
        return beans;
    }

    public void setBeans(List<BeanMeta> beans) {
        this.beans = beans;
    }

    public boolean isExportData() {
        return isExportData;
    }

    public void setExportData(boolean isExportData) {
        this.isExportData = isExportData;
    }

    public List<ErrorLog> getErrorLogs() {
        return errorLogs;
    }

    public void setErrorLogs(List<ErrorLog> errorLogs) {
        this.errorLogs = errorLogs;
    }

}
