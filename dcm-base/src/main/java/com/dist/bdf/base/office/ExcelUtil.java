package com.dist.bdf.base.office;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dist.bdf.base.office.meta.BeanMeta;
import com.dist.bdf.base.office.meta.ExcelMeta;
import com.dist.bdf.base.office.meta.FieldMeta;
import com.dist.bdf.base.office.meta.SheetMeta;
import com.dist.bdf.base.utils.DateUtil;
import com.dist.bdf.base.utils.FileUtil;
import com.dist.bdf.base.utils.StringUtil;

/**
 * excel导入导出工具类。<p>
 * 在使用导入和导出之前，必须需要获得类与excel映射关系。可以通过getExcelMapping()获取，得到一个ExcelMeta的实例。使用方法如下：
 * <pre>
 * ExcelMeta excel = ExcelUtil.getExcelMapping("mappingFile"); 
 * 导出时：
 * ExcelUtil.export("模板文件路径","导出文件路径", excel);
 * ExcelUtil.export("导出文件路径", excel);
 * 导入时：
 * ExcelUtil.importExcel("需要导入的Excel文件路径",excel,"是否记录日志");
 * 获取导入获取数据：
 * excel.getData("sheet名字","类路径");
 * 获取导入错误日志时：
 * excel.getErrorLogs("sheet名字");
 * </pre>
 * @author heshun
 * @version V1.0, 2013-9-12
 */
public class ExcelUtil {
    /**
     * excel映射配置所在路径。
     */
    public static final String XML_MAPPING_PATH = "src/main/resources/excel/mapping/";
    /**
     * 映射文件中的属性名name
     */
    public static final String NAME_ATRRIBUTE = "name";

    /**
     * 映射文件中的属性名isExportData
     */
    public static final String IS_EXPORT_DATA_ATRRIBUTE = "isExportData";

    /**
     * 字符串形式的true
     */
    public static final String DEFAULT_VALUE_TRUE_STR = "true";

    /**
     * 字符串形式的设置开始行数和结束行数的默认值
     */
    public static final String DEFAULT_VALUE_ONE_STR = "1";
    /**
     * 大小写字母之和
     */
    public static final int NUM_OF_CHARS = 65;

    /**
     * 大或小写字母之和
     */
    public static final int NUM_OF_CHAR = 26;

    /**
     * excel中行的最大数
     */
    public static final int EXCEL_MAX_ROW_NUM = 65535;

    /**
     * 使用自定义路径的配置文件。
     * @param fileName 文件名称
     * @param isCustomerFile true表示是用户自定义映射文件路径
     * @return 映射文件信息
     */
    @SuppressWarnings("unchecked")
    public static ExcelMeta getExcelMapping(String fileName, boolean isCustomerFile) {
        SAXReader reader = new SAXReader();
        Document document;
        try {
            String filePath = fileName;
            if (!isCustomerFile) {
                InputStream in = ExcelUtil.class.getClassLoader().getResourceAsStream(filePath);
                document = reader.read(in);
                filePath = XML_MAPPING_PATH + fileName;
            } else {
                document = reader.read(new File(filePath));
            }
        } catch (DocumentException e) {
            throw new ExcelException("解析dom时出错：" + e.getMessage());
        }
        ExcelMeta excelMeta = new ExcelMeta(); //用于存放解析结果
        excelMeta.setName(fileName);

        //获取根节点
        Element root = document.getRootElement();
        List<Element> sheets = root.elements("sheet");
        for (int i = 0; i < sheets.size(); i++) {
            Element sheet = sheets.get(i);
            SheetMeta sheetMeta = new SheetMeta(); //保存sheet对应的配置
            sheetMeta.setName(sheet.attribute(NAME_ATRRIBUTE).getValue());
            sheetMeta.setImportBegin(Integer.parseInt(sheet.attributeValue("importBegin", DEFAULT_VALUE_ONE_STR)));
            sheetMeta.setImportEnd(Integer.parseInt(sheet.attributeValue(SheetMeta.IMPORT_END, DEFAULT_VALUE_ONE_STR)));
            sheetMeta.setExportBegin(Integer.parseInt(sheet.attributeValue(SheetMeta.EXPORT_BEGIN,
                    DEFAULT_VALUE_ONE_STR)));
            sheetMeta.setExportEnd(Integer.parseInt(sheet.attributeValue(SheetMeta.EXPORT_END, DEFAULT_VALUE_ONE_STR)));
            sheetMeta.setExportData(Boolean.parseBoolean(sheet.attributeValue(IS_EXPORT_DATA_ATRRIBUTE,
                    DEFAULT_VALUE_TRUE_STR)));
            List<Element> beans = sheet.elements("bean");
            for (Element bean : beans) {
                BeanMeta beanMeta = new BeanMeta();
                beanMeta.setClassPath(bean.attributeValue("class"));
                beanMeta.setImportBegin(Integer.parseInt(bean.attributeValue(SheetMeta.IMPROT_BEGIN,
                        sheetMeta.getImportBegin() + "")));
                beanMeta.setImportEnd(Integer.parseInt(bean.attributeValue(SheetMeta.IMPORT_END,
                        sheetMeta.getImportEnd() + "")));
                beanMeta.setExportBegin(Integer.parseInt(bean.attributeValue(SheetMeta.EXPORT_BEGIN,
                        sheetMeta.getExportBegin() + "")));
                beanMeta.setExportEnd(Integer.parseInt(bean.attributeValue(SheetMeta.EXPORT_END,
                        sheetMeta.getExportEnd() + "")));
                beanMeta.setExportData(Boolean.parseBoolean(bean.attributeValue(IS_EXPORT_DATA_ATRRIBUTE,
                        DEFAULT_VALUE_TRUE_STR)));
                List<Element> fields = bean.elements("field");
                for (Element field : fields) {
                    FieldMeta fieldMeta = new FieldMeta();
                    fieldMeta.setCellIndex(field.attributeValue("index"));
                    fieldMeta.setCellName(field.attributeValue("cellName"));
                    fieldMeta.setName(field.attributeValue(NAME_ATRRIBUTE));
                    fieldMeta.setPrompt(field.attributeValue("prompt", ""));
                    fieldMeta.setType(FieldType.valueOf(field.attributeValue("type").toUpperCase()));

                    //取得field标签的code标签。
                    List<Element> codes = field.elements("code");
                    if (codes != null && codes.size() != 0) {
                        for (Element code : codes) {
                            fieldMeta.getCodes().put(code.getTextTrim(), code.attributeValue("value"));
                        }
                    }
                    beanMeta.getFieldMetas().add(fieldMeta);
                }
                sheetMeta.getBeans().add(beanMeta);
            }
            excelMeta.getSheets().add(sheetMeta);
        }
        return excelMeta;
    }

    /**
     * 获取Excel配置。根据传入的配置文件名字进行解析。 将解析结果保存至ExcelMeta中。
     * 
     * @param fileName 配置文件名称（不加路径）
     * @return 配置文件信息Map，key:sheet名字，value:映射信息Map（key:列号的整型值，value:列对应的属性值）。
     * @throws DocumentException
     */
    public static ExcelMeta getExcelMapping(String fileName) {
        return getExcelMapping(fileName, false);
    }

    /**
     * excel列名转列号。如何列号不是由A-Z字母组成，则会抛出ExcelException
     * <p>
     * 
     * <pre>
     * 1. 把列号转为大写。
     * 2. 如果列号不符合excel规则，不是由A-Z组成的，则抛出ExcelException，
     * 3. 把列号转为数字并返回，比如A，就是0，B就是1。
     * </pre>
     * 
     * @param columnIndex
     *            列号
     * @return 列号的整型值
     */
    public static int colIndex2colNo(String columnIndex) {

        // 转成大写
        String str = columnIndex.toUpperCase();

        // 判断是否都是字母
        boolean flag = str.matches("[A-Z]+");
        if (flag) {// 如果都是字母则转换成数字
            int colNo = 0;
            for (int i = 0; i < str.length(); i++) {
                int index = str.length() - i - 1;
                String ch = str.substring(index, index + 1);
                colNo += (ch.getBytes()[0] - NUM_OF_CHARS) + Math.pow(NUM_OF_CHAR, i) - 1;
            }
            return colNo;
        } else {// 如果不全是字母则不处理
            throw new ExcelException("column's index[" + columnIndex + "] is wrong.");
        }
    }

    /**
     * 设置单元格上提示
     * @param sheet  要设置的sheet.
     * @param promptTitle 标题
     * @param promptContent 内容
     * @param firstRow 开始行
     * @param endRow 结束行
     * @param firstCol 开始列
     * @param endCol 结束列
     * @return 设置好的sheet.
     */
    private static Sheet setHSSFPrompt(Sheet sheet, String promptTitle, String promptContent, int firstRow, int endRow,
            int firstCol, int endCol) {

        // 构造constraint对象
        DVConstraint constraint = DVConstraint.createCustomFormulaConstraint("DD1");

        // 四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);

        // 数据有效性对象
        HSSFDataValidation dataValidationView = new HSSFDataValidation(regions, constraint);
        dataValidationView.createPromptBox(promptTitle, promptContent);
        sheet.addValidationData(dataValidationView);
        return sheet;
    }

    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框.
     * @param sheet要设置的sheet.
     * @param textlist 下拉框显示的内容
     * @param firstRow 开始行
     * @param endRow 结束行
     * @param firstCol 开始列
     * @param endCol 结束列
     * @return 设置好的sheet.
     */
    private static Sheet setHSSFValidation(Sheet sheet, String[] textlist, int firstRow, int endRow, int firstCol,
            int endCol) {

        // 加载下拉列表内容
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(textlist);

        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);

        // 数据有效性对象
        HSSFDataValidation dataValidationList = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(dataValidationList);
        return sheet;
    }

    /**
     * 导入指定Excel的数据
     * @param importFilePath 指定的导入文件
     * @param excel 封装的导入信息并存放导入的数据
     * @param logError 是否记录日志
     */
    public static void importExcel(String importFilePath, ExcelMeta excel, boolean logError) {

        //是否获取到映射信息。
        isGotMapping(excel);

        //生成Workbook
        Workbook workbook = createWorkBook(importFilePath, "");

        //根据映射信息在workbook中解析Excel的单元格信息，并导出Excel中的数据到ExcelMeta。
        resolveCellInWorkbookByMappingAndImprotData2ExcelMeta(workbook, excel, logError);
    }

    /**
     * 根据映射信息在workbook中解析单元格信息，并导出Excel中的数据到ExcelMeta。
     * @param workbook Workbook 
     * @param excel 封装的导入信息并存放导入的数据
     * @param logError 是否记录日志
     */
    private static void resolveCellInWorkbookByMappingAndImprotData2ExcelMeta(Workbook workbook, ExcelMeta excel,
            boolean logError) {

        //从sheet中去解析单元格。
        resolveSheetInWorkbookBySheetMapping(workbook, excel, logError);
    }

    /**
     * 根据sheet映射信息在Workbook中解析Sheet。
     * @param workbook Workbook 
     * @param excel 封装的导入信息并存放导入的数据
     * @param logError 是否记录日志
     */
    private static void resolveSheetInWorkbookBySheetMapping(Workbook workbook, ExcelMeta excel, boolean logError) {

        //遍历映射文件中组成excel的sheet。
        for (SheetMeta sheetMeta : excel.getSheets()) {
            String sheetName = sheetMeta.getName();
            Sheet sheet = workbook.getSheet(sheetName);
            if (null == sheet) {
                continue;
            }
            resolveRowInSheetByBeanMapping(sheet, sheetMeta, logError);
        }
    }

    /**
     * 根据bean标签映射解析在sheet中的row
     * @param sheet Excel中的sheet
     * @param sheetMeta Excel中的sheet在映射文件中的映射信息 
     * @param logError 是否记录错误日志
     */
    private static void resolveRowInSheetByBeanMapping(Sheet sheet, SheetMeta sheetMeta, boolean logError) {

        //遍历映射中配置的类
        for (BeanMeta beanMeta : sheetMeta.getBeans()) {
            int lastRow = sheet.getLastRowNum() + 1;
            int begin = beanMeta.getImportBegin() - 1; //Workbook中的行数时从0开始。end配置也一样。
            int end = beanMeta.getImportEnd() - 1;
            end = end == 0 ? lastRow : end;
            List<Object> excelDataList = new ArrayList<Object>(); //用于存放Row中的数据
            for (int i = begin; i <= end; i++) { //注意是小于等于
                Row row = sheet.getRow(i);

                //如果有空的sheet，那么就不用导入。
                if (null == row) {
                    continue;
                }
                Object object = resolveCellInRowByFeildMappingAndImportData2BeanMeta(row, sheetMeta, beanMeta, logError);
                excelDataList.add(object);
            }
            beanMeta.setData(excelDataList);
        }
    }

    /**
     * 根据field映射信息在Row中解析单元格，并将单元格数据导入BeanMeta
     * @param row Excel的行
     * @param sheetMeta sheet对应的映射信息
     * @param beanMeta row对应的映射信息
     * @param logError 是否记录日志
     * @return 导入了一行数据的对象
     */
    private static Object resolveCellInRowByFeildMappingAndImportData2BeanMeta(Row row, SheetMeta sheetMeta,
            BeanMeta beanMeta, boolean logError) {
        try {
            Class<?> classModel = Class.forName(beanMeta.getClassPath());
            Object object = classModel.newInstance(); //用于存放一行的数据
            for (FieldMeta fieldMeta : beanMeta.getFieldMetas()) {
                int column = colIndex2colNo(fieldMeta.getCellIndex());
                Cell cell = row.getCell(column);
                if (null != cell) {
                    importCelldata2Object(cell, object, classModel, sheetMeta, fieldMeta, logError);
                }
            }
            return object;
        } catch (ClassNotFoundException e) {
            throw new ExcelException("从单元格导入数据时发生错误：找不到类" + beanMeta.getClassPath());
        } catch (InstantiationException e) {
            ExcelException.throwExcelExceptionWithKnowCause(e.getMessage());
        } catch (IllegalAccessException e) {
            ExcelException.throwExcelExceptionWithKnowCause(e.getMessage());
        }
        return null;
    }

    /**
     * 将单元格数据导入对象的某个属性中
     * @param cell 单元格
     * @param object 对象
     * @param classModel 类模板
     * @param sheetMeta sheet对应的映射信息
     * @param fieldMeta 类的映射信息
     * @param logError 是否记录日志
     */
    private static void importCelldata2Object(Cell cell, Object object, Class<?> classModel, SheetMeta sheetMeta,
            FieldMeta fieldMeta, boolean logError) {

        // 取值
        Object value = getCellValueByType(cell, sheetMeta, logError);

        //根据字段类型，将值设置到类模板
        shetValue2ClassModelByFieldType(value, object, classModel, fieldMeta, cell, sheetMeta, logError);

    }

    /**
     * 根据字段类型，将值设置到类模板
     * @param value 单元格的值
     * @param object 需要设值的对象
     * @param classModel 类模板
     * @param fieldMeta 字段映射信息
     * @param cell 单元格
     * @param sheetMeta sheet映射信息
     * @param logError 是否记录日志
     */
    private static void shetValue2ClassModelByFieldType(Object value, Object object, Class<?> classModel,
            FieldMeta fieldMeta, Cell cell, SheetMeta sheetMeta, boolean logError) {
        String name = fieldMeta.getName();
        String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);

        // 对应的类型
        FieldType type = fieldMeta.getType();
        String errorReason = "将单元格数据导入对象时发生异常：类型转换失败 ";
        try {
            if (fieldMeta.isCode()) {

                //如果有码表值，直接从映射信息里取值就可以了
                int codeValue = Integer.parseInt(fieldMeta.getCodes().get(cell.getStringCellValue()));
                classModel.getMethod(methodName, int.class).invoke(object, codeValue);
                return;
            }
            switch (type) {
            case STRING:
                String str = value.toString().trim();
                classModel.getMethod(methodName, String.class).invoke(object, str);
                break;
            case INT:
                double doubleValue = Double.parseDouble(value.toString().trim());
                if (doubleValue % 1 == 0) {// 如果是整数
                    int intValue = (int) doubleValue;
                    if (intValue != doubleValue) {// 如果转换后不等于原值，比如原值超出int范围。
                        throw new NumberFormatException();
                    }
                    classModel.getMethod(methodName, int.class).invoke(object, intValue);
                }
                break;
            case LONG:
                double valueDouble = Double.parseDouble(value.toString().trim());
                if (valueDouble % 1 == 0) {// 如果是整数
                    long longValue = (long) valueDouble;
                    if (longValue != valueDouble) {// 如果转换后不等于原值，比如原值超出long范围。
                        throw new NumberFormatException();
                    }
                    classModel.getMethod(methodName, long.class).invoke(object, longValue);
                }
                break;
            case DOUBLE:
                double doubleV = Double.parseDouble(value.toString().trim());
                classModel.getMethod(methodName, double.class).invoke(object, doubleV);
                break;
            case TIME:
            case DATETIME:
            case DATE:
                String format;
                if (type == FieldType.TIME) {
                    format = FieldMeta.TIME_FORMAT;
                } else if (type == FieldType.DATETIME) {
                    format = FieldMeta.DATE_TIME_FORMAT;
                } else {
                    format = FieldMeta.DATE_FORMAT;
                }
                Date datetimeValue = null;
                if (value instanceof String) {
                    SimpleDateFormat sdf = new SimpleDateFormat(format);
                    datetimeValue = sdf.parse(value.toString().trim());
                } else {
                    datetimeValue = cell.getDateCellValue();
                }
                classModel.getMethod(methodName, Date.class).invoke(object, datetimeValue);
                break;
            }
        } catch (IllegalArgumentException e) {
            setError(cell, errorReason + e.getMessage(), sheetMeta);
        } catch (SecurityException e) {
            setError(cell, errorReason + e.getMessage(), sheetMeta);
        } catch (IllegalAccessException e) {
            setError(cell, errorReason + e.getMessage(), sheetMeta);
        } catch (InvocationTargetException e) {
            setError(cell, errorReason + e.getMessage(), sheetMeta);
        } catch (NoSuchMethodException e) {
            setError(cell, errorReason + e.getMessage(), sheetMeta);
        } catch (ParseException e) {
            setError(cell, errorReason + e.getMessage(), sheetMeta);
        }
    }

    /**
     * 根据单元格类型取得相应类型的值。
     * @param cell 单元格
     * @param sheetMeta sheet映射信息
     * @param logError 是否记录日志
     * @return 装有单元格值的对象
     */
    private static Object getCellValueByType(Cell cell, SheetMeta sheetMeta, boolean logError) {
        Object value = null;

        // 取得单元格的类型
        int cellType = cell.getCellType();
        switch (cellType) {
        case HSSFCell.CELL_TYPE_BLANK:// 空
            value = "";
            break;
        case HSSFCell.CELL_TYPE_BOOLEAN:// 布尔值
            value = cell.getBooleanCellValue();
            break;
        case HSSFCell.CELL_TYPE_ERROR:// 错误
            value = "";
            break;
        case HSSFCell.CELL_TYPE_FORMULA:// 公式
            value = "";
            break;
        case HSSFCell.CELL_TYPE_NUMERIC:// 数值
            value = cell.getNumericCellValue();
            break;
        case HSSFCell.CELL_TYPE_STRING:// 文本
            value = cell.getStringCellValue();
            break;
        default:// 其它

            // 错误记录：不支持的类型
            if (logError) {
                setError(cell, "The type[" + cellType + "] is unsupport.", sheetMeta);
            }
            break;
        }
        return value;
    }

    /**
     * 记录单元格错误日志
     * @param cell 单元格
     * @param reason 原因
     * @param sheetMeta sheet映射信息
     */
    private static void setError(Cell cell, String reason, SheetMeta sheetMeta) {
        int rownum = cell.getRowIndex();
        int columnIndex = cell.getColumnIndex();

        // 创建错误记录
        ErrorLog errorLog = new ErrorLog();
        errorLog.setRowNumber(rownum);
        errorLog.setColumnNumber(columnIndex);
        errorLog.setReason(reason);
        sheetMeta.getErrorLogs().add(errorLog);

    }

    /**
     * 往单元格写入数据。
     * @param classModel 类模板
     * @param cell 列
     * @param targetObj 目标对象
     * @param fieldMeta 类对应的映射
     */
    private static void exportData2Cell(Class<?> classModel, Cell cell, Object targetObj, FieldMeta fieldMeta) {

        Object value = getValueFromClassModel(classModel, targetObj, fieldMeta);
        exportData2CellByFileType(cell, value, fieldMeta);
    }

    /**
     * 根据类模板，从目标对象中获取字段属性
     * @param classModel 类模板
     * @param targetObj 目标对象
     * @param fieldMeta 字段映射
     */
    private static Object getValueFromClassModel(Class<?> classModel, Object targetObj, FieldMeta fieldMeta) {
        String methodName = "get" + fieldMeta.getName().substring(0, 1).toUpperCase()
                + fieldMeta.getName().substring(1);
        Object value = null;
        try {
            value = classModel.getMethod(methodName).invoke(targetObj);
        } catch (IllegalArgumentException e) {
            ExcelException.throwExcelExceptionWithKnowCause(e.getMessage());
        } catch (SecurityException e) {
            ExcelException.throwExcelExceptionWithKnowCause(e.getMessage());
        } catch (IllegalAccessException e) {
            ExcelException.throwExcelExceptionWithKnowCause(e.getMessage());
        } catch (InvocationTargetException e) {
            ExcelException.throwExcelExceptionWithKnowCause(e.getMessage());
        } catch (NoSuchMethodException e) {
            throw new ExcelException(classModel + "找不到该方法：" + methodName);
        }
        return value;
    }

    /**
     * 根据Field类型导出数据至单元格。
     * @param cell 需要导入数据的单元格 
     * @param dataObj 数据值对象
     * @param fieldMeta filed映射信息
     */
    private static void exportData2CellByFileType(Cell cell, Object dataObj, FieldMeta fieldMeta) {

        //如果是下拉型，则需要直接从码表中取得对应显示的值
        if (fieldMeta.isCode()) {
            String value = fieldMeta.getShowValueByCode(dataObj.toString().trim());
            cell.setCellValue(value);
            return;
        }

        //根据类型设置
        if (dataObj instanceof Double) {
            double doubleValue = Double.parseDouble(dataObj.toString().trim());
            cell.setCellValue(doubleValue);
        } else if (dataObj instanceof Date) {
            Date dateValue = (Date) dataObj;
            switch (fieldMeta.getType()) {
            case DATE:
                cell.setCellValue(DateUtil.toStrWithFormat(dateValue, FieldMeta.DATE_FORMAT));
                break;
            case DATETIME:
                cell.setCellValue(DateUtil.toStrWithFormat(dateValue, FieldMeta.DATE_TIME_FORMAT));
                break;
            case TIME:
                cell.setCellValue(DateUtil.toStrWithFormat(dateValue, FieldMeta.TIME_FORMAT));
                break;
            default:
                break;
            }

        } else if (dataObj instanceof Boolean) {
            boolean boolValue = Boolean.parseBoolean(dataObj.toString().trim());
            cell.setCellValue(boolValue);
        } else {
            if (null != dataObj) {
                String strValue = dataObj.toString().trim();
                cell.setCellValue(strValue);
            }
        }

    }

    /**
     * 设置单元格的鼠标提示信息。
     * @param sheet 
     * @param cell 单元格
     * @param fieldMeta field映射信息
     */
    private static void setPromptStyle(Sheet sheet, Cell cell, FieldMeta fieldMeta) {
        String prompt = fieldMeta.getPrompt();
        if (!StringUtil.isEmpty(prompt)) {
            setHSSFPrompt(sheet, "", prompt, 1, EXCEL_MAX_ROW_NUM, cell.getColumnIndex(), cell.getColumnIndex());
        }
    }

    /**
     * 导出数据到制定文件。<p>
     * 注意：使用该方法之前，请确保excel中已经有了类与Excel的映射关系以及需要导出的数据。
     * @param exportFile 即将被导入数据的文件。
     * @param excel  装有映射关系与导出数据的ExcelMeta实例
     */
    public static void export(String exportFile, ExcelMeta excel) {
        export("", exportFile, excel);
    }

    /**
     *  使用模板进行导出，自行配置Excel与类的映射关系的场景下适用。
     * <p>
     * 总体思路：使用poi将模板读入内存，读取模板的行和列的信息，如果能读取到，并且行和列本身没有数据，则填入数据。如果有，则
     * 生成新的行和列。这样可以不破坏模板本身的样式。数据填入完成之后，则写入目标文件。
     */
    public static void export(String templateFile, String exportFile, ExcelMeta excel) {

        //是否获取到映射信息。
        isGotMapping(excel);

        //如果模板文件为空，则表示不适用模板导出
        boolean isUseTemplate = isExportByTemplate(templateFile);

        //生成Workbook，如果使用模板，则直接将模板文件装入流即可生成，否则要根据导出文件的后缀来生成。
        Workbook workbook = createWorkBook(templateFile, exportFile);

        //在workbook中生成Excel映射信息中所有的的Sheet、Row、Cell，并装配数据。
        createExcelAndExportDataInWorkbook(isUseTemplate, excel, workbook);

        wirteWorkbook2ExportFile(workbook, exportFile);

    }

    /**
     * 将workbook写入指定文件
     * @param workbook Workbook
     * @param filePath 指定的文件路径
     */
    private static void wirteWorkbook2ExportFile(Workbook workbook, String filePath) {
        File file = new File(filePath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            workbook.write(fos);
        } catch (FileNotFoundException e) {
            throw new ExcelException("导出Excel时找不到指定文件路径：" + filePath);
        } catch (IOException e) {
            throw new ExcelException("导出Excel时发生异常：" + e.getMessage());
        } finally {
            FileUtil.close(fos);
        }
    }

    /**
     * 在Workbook中生成Excel并写入数据。
     * @param isUseTemplate 是否用模板
     * @param excel 映射信息与需要写入的数据
     * @param workbook Wrokbook
     */
    private static void createExcelAndExportDataInWorkbook(boolean isUseTemplate, ExcelMeta excel, Workbook workbook) {
        createSheetInWorkbookBySheetMapping(isUseTemplate, excel, workbook);
    }

    /**
     * 根据映射信息在Workbook中创建Sheet
     * @param isUseTemplate 是否用模板
     * @param excel 映射信息与需要写入的数据
     * @param workbook Workbook
     */
    private static void createSheetInWorkbookBySheetMapping(boolean isUseTemplate, ExcelMeta excel, Workbook workbook) {

        //取得映射中的Sheet配置
        for (SheetMeta sheetMeta : excel.getSheets()) {
            String sheetName = sheetMeta.getName();

            //如果使用模板，从workbook中获取Sheet。不使用模板则直接在workbook中生成Sheet。
            Sheet sheet = getOrCreateSheetFromWorkbook(isUseTemplate, workbook, sheetName);

            //如果配置了sheet，但是模板中却没有，那么根据模板导出时，就不用生成这个sheet。
            if (null == sheet) {

                //生成下一个Sheet。
                continue;
            }
            //生成当前sheet中类对应的行
            createRowOfBeanMappedInSheet(isUseTemplate, sheet, sheetMeta);
        }
    }

    /**
     * 生成当前sheet中类对应的行
     * @param isUseTemplate 是否用模板
     * @param sheet Workbook中的页
     * @param sheetMeta sheet映射信息
     */
    private static void createRowOfBeanMappedInSheet(boolean isUseTemplate, Sheet sheet, SheetMeta sheetMeta) {

        //遍历映射文件中组成sheet的类。
        for (BeanMeta beanMeta : sheetMeta.getBeans()) {

            //如果没有使用模板，就要先生成表头。
            if (!isUseTemplate) {
                createColumnName(sheet, beanMeta);
                if (!sheetMeta.isExportData()) {
                    return;
                }
            }

            //根据数据判断决定生成行的数量。
            List<?> dataList = beanMeta.getData();
            if (null == dataList) {

                //如果这个类没有放置数据，就不用填入。
                continue;
            }

            //根据映射中sheet标签、bean标签的配置决定生成Row的起始行和结束行。
            int begin = beanMeta.getExportBegin() - 1;
            int end = beanMeta.getExportEnd() - 1;
            int numOfRow = calcNumOfRow(begin, end, dataList.size());
            int rowNum = 0;
            for (int i = 0; i < numOfRow; i++) {
                rowNum = begin + i;

                //如果已经导入表头，那么从第2行开始导入。
                rowNum = isUseTemplate ? rowNum : begin > 0 ? rowNum : rowNum + 1;

                //根据bean标签的配置，从sheet中获取或生成行对象。如果用的是模板，就直接从sheet获取就可以了。
                Row row = getOrCreateRowInSheet(true, sheet, rowNum);

                Object data = dataList.get(i);

                //根据field标签的配置，从row中获取或生成单元格对象，然后往单元格写入数据。
                getOrCreateCellInRowByFieldMappingAndExportData(isUseTemplate, sheet, row, beanMeta, data);
            }
        }
    }

    /**
     * 生成表头
     * @param sheet
     * @param beanMeta
     */
    private static void createColumnName(Sheet sheet, BeanMeta beanMeta) {

        //默认生成的表头在第一行
        Row row = getOrCreateRowInSheet(true, sheet, 0);
        for (FieldMeta fieldMeta : beanMeta.getFieldMetas()) {
            int column = colIndex2colNo(fieldMeta.getCellIndex());
            Cell cell = row.createCell(column);
            cell.setCellValue(fieldMeta.getCellName());
        }

    }

    /**
     * 根据field标签的配置，从row中获取或生成单元格对象，然后往单元格写入数据。
     * @param isUseTemplate 是否用模板
     * @param sheet 
     * @param row 行对象的实例
     * @param beanMeta bean标签映射信息
     * @param data 行对应的数据
     */
    private static void getOrCreateCellInRowByFieldMappingAndExportData(boolean isUseTemplate, Sheet sheet, Row row,
            BeanMeta beanMeta, Object data) {

        //遍历类中需要导出的列。
        for (FieldMeta fieldMeta : beanMeta.getFieldMetas()) {
            int column = colIndex2colNo(fieldMeta.getCellIndex());
            Cell cell = getOrCreateCellInRow(isUseTemplate, row, column, fieldMeta, sheet);
            if (beanMeta.isExportData()) {
                //如果没有配置需要导出数据，则不用导出。
                exportData2CellByFieldMapping(cell, fieldMeta, data, beanMeta.getClassPath());
            }
        }

    }

    /**
     * 根据field标签配置，往单元格写入数据。
     * @param cell 单元格
     * @param fieldMeta field标签映射信息
     * @param data 行对应的数据
     * @param classPath 类路径
     */
    private static void exportData2CellByFieldMapping(Cell cell, FieldMeta fieldMeta, Object data, String classPath) {
        //如果单元格有值，则不用再设值。
        if (StringUtil.isBlank(cell.getStringCellValue())) {
            Class<?> classModel;
            try {
                classModel = Class.forName(classPath);
            } catch (ClassNotFoundException e) {
                throw new ExcelException("从对象中导出数据时，没有找到该类：" + classPath);
            }
            exportData2Cell(classModel, cell, data, fieldMeta);
        }
    }

    /**
     * 从行中获取或创建单元格。<p>
     * 如果使用模板，则可以需要从行中获取。
     * @param isUseTemplate 是否用模板
     * @param row 行对象的实例
     * @param column 列号
     * @param sheet 
     * @param fieldMeta 
     * @return 单元格
     */
    private static Cell getOrCreateCellInRow(boolean isUseTemplate, Row row, int column, FieldMeta fieldMeta,
            Sheet sheet) {
        Cell cell = null;
        if (isUseTemplate) {
            cell = row.getCell(column);
            return null == cell ? createCellAndStyle(sheet, row, column, fieldMeta) : cell;
        }
        return createCellAndStyle(sheet, row, column, fieldMeta);
    }

    /**
     * 创建单元格并设置样式
     * @param sheet
     * @param row
     * @param column
     * @param fieldMeta
     * @return
     */
    private static Cell createCellAndStyle(Sheet sheet, Row row, int column, FieldMeta fieldMeta) {
        Cell cell = row.createCell(column);
        setPromptStyle(sheet, cell, fieldMeta);
        setSelectStyle(sheet, cell, fieldMeta);
        return cell;
    }

    /**
     * 设置下拉选择
     * @param sheet
     * @param cell
     * @param fieldMeta
     */
    private static void setSelectStyle(Sheet sheet, Cell cell, FieldMeta fieldMeta) {
        Map<String, String> codes = fieldMeta.getCodes();

        //没有配置则不用生成。
        if (codes.size() == 0) {
            return;
        }
        String[] showValue = new String[codes.size()];
        int i = 0;
        for (String vlaue : codes.keySet()) {
            showValue[i] = vlaue;
            i++;
        }
        setHSSFValidation(sheet, showValue, 1, EXCEL_MAX_ROW_NUM, cell.getColumnIndex(), cell.getColumnIndex());// 这里默认设了2-101列只能选择不能输入
    }

    /**
     * 根据bean标签的配置，从sheet中获取或生成行对象。<p>
     * 如果用的是模板，就直接从sheet获取就可以了。
     * @param isUseTemplate 是否用模板
     * @param sheet Workbook中的页
     * @param sheetMeta sheet映射信息
     * @param rownum 行号
     * @return 行号对象的Row的实例
     */
    private static Row getOrCreateRowInSheet(boolean isUseTemplate, Sheet sheet, int rownum) {
        Row row = null;
        if (isUseTemplate) {
            row = sheet.getRow(rownum);

            //如果取不到该行，则需要创建一行。
            return null == row ? sheet.createRow(rownum) : row;
        } else {
            return sheet.createRow(rownum);
        }
    }

    /**
     * 根据配置和数据计算在Sheet应该要生成多少行。
     * @param begin 开始行号
     * @param end 结束行号
     * @param size 数据大小
     * @return
     */
    private static int calcNumOfRow(int begin, int end, int size) {
        if (0 == end) {
            return size;
        }
        return Math.min(size, end - begin + 1);
    }

    /**
     * 在workbook中获取或创建指定名字的Sheet。<p>
     * 如果使用模板，从workbook中获取Sheet。不使用模板则直接在workbook中生成Sheet。
     * @param isUseTemplate 是否使用模板。
     * @param workbook Workbook
     * @param sheetName  sheet的名字
     * @return Sheet的实例
     */
    private static Sheet getOrCreateSheetFromWorkbook(boolean isUseTemplate, Workbook workbook, String sheetName) {
        Sheet sheet = null;
        if (isUseTemplate) {
            sheet = workbook.getSheet(sheetName);
        } else {
            sheet = workbook.createSheet(sheetName);
        }
        return sheet;
    }

    /**
     * 判断是否获得映射信息
     * @param excel 
     */
    private static void isGotMapping(ExcelMeta excel) {
        if (!excel.isGotMapping()) {
            throw new ExcelException("没有获取到" + excel.getName() + "对应的映射信息");
        }
    }

    /**
     * 生成Workbook的实例。<p>
     * 如果使用模板，则直接将模板文件装入流即可生成，否则要根据导出文件的后缀来生成。
     * @param templateFile 模板文件
     * @param filePath 指定的文件
     * @return Workbook实例
     */
    private static Workbook createWorkBook(String templateFilePath, String filePath) {
        if (StringUtil.isEmpty(templateFilePath)) {
            String suffix = FileUtil.getSuffix(filePath);
            if ("xls".equals(suffix)) {
                return new HSSFWorkbook();
            } else if ("xlsx".equals(suffix)) {
                return new XSSFWorkbook();
            } else {
                throw new ExcelException("不支持的文件类型：." + suffix);
            }
        } else {
            File templataFile = new File(templateFilePath);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(templataFile);
                return WorkbookFactory.create(fis);
            } catch (FileNotFoundException e) {
                throw new ExcelException("找不到模板文件路径：" + templataFile);
            } catch (InvalidFormatException e) {
                throw new ExcelException(e.getMessage());
            } catch (IOException e) {
                throw new ExcelException("创建Workbook时发生异常!");
            } finally {
                FileUtil.close(fis);
            }
        }
    }

    /**
     * 判断是否是通过模板导出。
     * @param templateFile 模板文件
     * @return 使用模板导出为false，不使用模板导入为true
     */
    private static boolean isExportByTemplate(String templateFile) {
        if (StringUtil.isEmpty(templateFile)) {
            return false;
        }
        return true;
    }

}
