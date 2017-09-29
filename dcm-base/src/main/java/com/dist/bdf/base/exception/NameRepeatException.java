package com.dist.bdf.base.exception;

/**
 * 名称重复异常
 * Created by heshunwq on 2014/12/31.
 */
public class NameRepeatException extends BusinessException {
    public NameRepeatException() {
        super("名称重复");
    }

    public NameRepeatException(String message) {
        super(message);
    }

    public NameRepeatException(Throwable e) {
        super(e);
    }

    public NameRepeatException(String pattern, Object... params) {
        super(pattern, params);
    }
}
