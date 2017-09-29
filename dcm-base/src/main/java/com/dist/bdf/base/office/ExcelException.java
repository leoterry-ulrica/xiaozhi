package com.dist.bdf.base.office;

/**
 *Excel异常。继承了RuntimeException。
 * @author heshun
 * @version V1.0, 2013-9-12
 */
public class ExcelException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ExcelException() {
        super();
    }

    /**
     * @param message
     */
    public ExcelException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ExcelException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ExcelException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 不知道发生的异常类型时，使用该方法将异常抛出
     * @param message 异常的信息
     */
    public static void throwExcelExceptionWithKnowCause(String message) {
        throw new ExcelException(message);
    }
}
