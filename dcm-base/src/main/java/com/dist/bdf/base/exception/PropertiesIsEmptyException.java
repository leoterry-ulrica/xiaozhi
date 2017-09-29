package com.dist.bdf.base.exception;

/**
 * 编号重复异常
 * Created by heshunwq on 2014/12/31.
 */
public class PropertiesIsEmptyException extends BusinessException {
    public PropertiesIsEmptyException() {
        super("属性不能为空");
    }

    public PropertiesIsEmptyException(String message) {
        super(message);
    }

    public PropertiesIsEmptyException(Throwable e) {
        super(e);
    }

    public PropertiesIsEmptyException(String pattern, Object... params) {
        super(pattern, params);
    }
}
