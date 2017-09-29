package com.dist.bdf.base.office.meta;

import java.util.HashMap;
import java.util.Map;

import com.dist.bdf.base.office.ExcelException;
import com.dist.bdf.base.office.FieldType;

/**
 * 与映射文件中的field标签对应。单元格导入错误时，也会使用该类进行记录。
 * @author heshun
 * @version V1.0, 2013-9-16
 */
public class FieldMeta {
    /**
     * Excel中默认的日期格式
     */
    public static final String DATE_FORMAT = "yyyy/MM/dd";
    /**
     * Excel中默认的日期时间格式
     */
    public static final String DATE_TIME_FORMAT = "yyyy/MM/dd hh:mm:ss";
    /**
     * Excel中默认的日期时间格式
     */
    public static final String TIME_FORMAT = "hh:mm:ss";
    /**
     * 列号 
     */
    private String cellIndex;

    /**
     * 列名
     */
    private String cellName;
    /**
     * 提示信息
     */
    private String prompt;
    /**
     * 字段名字
     */
    private String name;

    /**
     * 字段类型
     */
    private FieldType type;


    /**
     * 是否导出数据
     */
    private boolean isExportData;

    /**
     * 字段的码表值，键为显示的值，值为数据库的值。
     */
    private Map<String, String> codes = new HashMap<String, String>();

    /**
     * 该字段是否是码表类型。
     * @return true表示是码表
     */
    public boolean isCode() {
        if (codes.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据码表值获得显示值
     * @param code 码表值
     * @return 显示值
     */
    public String getShowValueByCode(String code) {
        for (String key : codes.keySet()) {
            if (codes.get(key).equals(code)) {
                return key;
            }
        }
        throw new ExcelException("没有找到与" + code + "匹配的显示值。");
    }

    public String getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(String cellIndex) {
        this.cellIndex = cellIndex;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public boolean isExportData() {
        return isExportData;
    }

    public void setExportData(boolean isExportData) {
        this.isExportData = isExportData;
    }

    public Map<String, String> getCodes() {
        return codes;
    }

    public void setCodes(Map<String, String> codes) {
        this.codes = codes;
    }

}
