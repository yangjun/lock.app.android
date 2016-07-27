package com.wm.lock.exception;

/**
 * Created by WM on 2015/8/6.
 */
public class BizException extends RuntimeException {

    private boolean needNotify = true;

    public BizException() {
        super();
    }

    public BizException(Throwable throwable) {
        super(throwable);
    }

    public BizException(String message) {
        super(message);
    }

    public boolean isNeedNotify() {
        return needNotify;
    }

    public void setNeedNotify(boolean needNotify) {
        this.needNotify = needNotify;
    }
}
