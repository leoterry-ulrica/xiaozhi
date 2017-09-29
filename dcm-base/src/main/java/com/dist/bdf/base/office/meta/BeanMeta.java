package com.dist.bdf.base.office.meta;

import java.util.ArrayList;
import java.util.List;

/**
 *与配置文件中的bean标签对应。
 * @author heshun
 * @version V1.0, 2013-9-22
 */
public class BeanMeta {

    /**
     * 导入结束行
     */
    private int importEnd;

    /**
     * 类路径
     */
    private String classPath;

    /**
     * 导入开始行
     */
    private int importBegin;

    /**
     * 是否导出数据
     */
    private boolean isExportData;

    /**
     * 导出开始行
     */
    private int exportBegin;

    /**
     * 导入结束行
     */
    private int exportEnd;

    /**
     * 与field标签对应
     */
    private List<FieldMeta> fieldMetas = new ArrayList<FieldMeta>();

    /**
     * bean的数据。
     */
    private List<?> data;

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public List<FieldMeta> getFieldMetas() {
        return fieldMetas;
    }

    public void setFieldMetas(List<FieldMeta> fieldMetas) {
        this.fieldMetas = fieldMetas;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    public int getImportEnd() {
        return importEnd;
    }

    public void setImportEnd(int importEnd) {
        this.importEnd = importEnd > 0 ? importEnd : 1;
    }

    public void setExportEnd(int exportEnd) {
        this.exportEnd = exportEnd > 0 ? exportEnd : 1;
    }

    public void setExportBegin(int exportBegin) {
        this.exportBegin = exportBegin > 0 ? exportBegin : 1;
    }

    public int getExportEnd() {
        return exportEnd;
    }

    public int getImportBegin() {
        return importBegin;
    }

    public void setExportData(boolean isExportData) {
        this.isExportData = isExportData;
    }

    public void setImportBegin(int importBegin) {
        this.importBegin = importBegin > 0 ? importBegin : 1;
    }

    public int getExportBegin() {
        return exportBegin;
    }

    public boolean isExportData() {
        return isExportData;
    }

}
