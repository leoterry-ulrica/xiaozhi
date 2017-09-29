package com.dist.bdf.base.office;

/**
 *记录出错的位置和原因。
 * @author heshun
 * @version V1.0, 2013-9-12
 */
public class ErrorLog {

    /**
     * 行号
     */
    private int rowNumber;

    /**
     * 列号
     */
    private int columnNumber;

    /**
     * 错误原因
     */
    private String reason;


    /**
     *默认构造 
     */
    public ErrorLog() {

    }

    /**
     *方便使用时设置单元格信息和错误信息。 
     * @param rowNumber 行号
     * @param columnNumber 列号
     * @param reason 错误信息
     */
    public ErrorLog(int rowNumber, int columnNumber, String reason) {
        super();
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        this.reason = reason;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    
    /**
     * 只显示行号和列号，方便测试。
     * @return
     */
    public String toNumAndColString(){
        return "行号：" + rowNumber + "。列号：" +columnNumber;  
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ErrorLog [rowNumber=" + rowNumber + ", columnNumber=" + columnNumber + ", reason=" + reason + "]";
    }
    

}
