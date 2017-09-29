package com.dist.bdf.base.exception;

/**
 * 编号重复异常
 * Created by heshunwq on 2014/12/31.
 */
public class SNRepeatException extends BusinessException {
    public SNRepeatException() {
        super("编号重复");
    }

    public SNRepeatException(String message) {
        super(message);
    }

    public SNRepeatException(Throwable e) {
        super(e);
    }

    public SNRepeatException(String pattern, Object... params) {
        super(pattern, params);
    }
}
