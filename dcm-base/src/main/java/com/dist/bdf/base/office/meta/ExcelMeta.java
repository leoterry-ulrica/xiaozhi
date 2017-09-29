package com.dist.bdf.base.office.meta;

import java.util.ArrayList;
import java.util.List;

import com.dist.bdf.base.office.ErrorLog;
import com.dist.bdf.base.utils.StringUtil;

/**
 * 与映射文件中的excel标签对应，该类包含了多个Sheet标签对应的对象。<p>
 * 该类同时还提供了获取数据和设置数据的方法。在导出Excel之前，要先使用该类封装映射文件信息，再使用该类设置要导出的数据。
 * 在导入Excel时，需要使用该类来存放导入的数据和导入错误日志。
 * @author heshun
 * @version V1.0, 2013-9-16
 */
public class ExcelMeta {

    /**
     * Excel文件名
     */
    private String name;

    /**
     *  与映射文件对应的sheet
     */
    private List<SheetMeta> sheets = new ArrayList<SheetMeta>();

    /**
     * 
     * 获得sheet页上一个类的数据集合
     * @param sheetName sheet名称
     * @param classPath 类路径
     * @return 一个sheet页中类数据的集合
     */
    public List<?> getData(String sheetName, Class<?> classPath) {

        //获取类路径
        String path = getClassPath(classPath);
        List<Object> data = new ArrayList<Object>();
        for (SheetMeta sheet : sheets) {

            //匹配传入的sheet名称
            if (sheetName.equals(sheet.getName())) {

                //获取sheet下的bean信息
                for (BeanMeta beanMeta : sheet.getBeans()) {

                    //匹配传入的类路径
                    if (beanMeta.getClassPath().equals(path)) {
                        data.addAll(beanMeta.getData());
                    }
                }
            }
        }
        return data;

    }

    /**
     * 根据类模板获取Excel中的该类所有的数据
     * @param classPath 类路径
     * @return 一个excel中类数据的集合
     */
    public List<?> getData(Class<?> classPath) {

        //获取类路径
        String path = getClassPath(classPath);
        List<Object> data = new ArrayList<Object>();
        for (SheetMeta sheet : sheets) {
            for (BeanMeta bean : sheet.getBeans()) {

                //匹配传入的类路径
                if (bean.getClassPath().equals(path)) {
                    data.addAll(bean.getData());
                }
            }
        }
        return data;
    }

    /**
     * 是否取得映射信息
     * @return 取得映射信息时为真
     */
    public boolean isGotMapping() {
        if (this.sheets.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 设置sheet中，某个类对应的数据
     * @param sheetName sheet名字
     * @param data 数据
     */
    public void setSheetData(String sheetName, List<?> data) {
        String classPath;
        for (SheetMeta sheet : sheets) {

            //遍历sheet。根据sheetName找到想要设值的sheet。
            if (sheetName.equals(sheet.getName())) {
                List<BeanMeta> beans = sheet.getBeans();

                //遍历sheet的bean
                for (BeanMeta beanMeta : beans) {

                    //取一行数据的类路径，如果类路径与配置的路径相同，则表示传入数据是这个类的数据。
                    classPath = getClassPath(data.get(0).getClass());
                    if (classPath.equals(beanMeta.getClassPath())) {
                        beanMeta.setData(data);
                    }
                }
            }
        }
    }

    /**
     * 去掉类路径中的前缀。
     * @author DuYueLi
     * @param classPath 类路径
     * @return 去掉前缀class 后的类路径 
     */
    private String getClassPath(Class<?> classPath) {
        if (null != classPath && !StringUtil.isEmpty(classPath.toString())) {

            //截取类路径
            return classPath.toString().split(" ")[1];
        } else {
            return null;
        }
    }

    /**
     * excel中是否有记录
     * @return true表示excel中有错误记录
     */
    public boolean hasError() {
        if (this.getErrorLogs().size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获得所有Excel中一个Sheet所有的错误记录。
     * @return Sheet中的所有记录
     */
    public List<ErrorLog> getErrorLogs(String sheetName) {
        for (SheetMeta sheet : sheets) {
            if (sheetName.equals(sheet.getName())) {
                return sheet.getErrorLogs();
            }
        }
        return new ArrayList<ErrorLog>();
    }

    public List<SheetMeta> getSheets() {
        return sheets;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获得所有的错误记录。
     * @return excel中的所有错误记录
     */
    public List<ErrorLog> getErrorLogs() {
        List<ErrorLog> logs = new ArrayList<ErrorLog>();
        for (SheetMeta sheet : sheets) {
            logs.addAll(sheet.getErrorLogs());
        }
        return logs;
    }

    public String getName() {
        return name;
    }

    public void setSheets(List<SheetMeta> sheets) {
        this.sheets = sheets;
    }

}
