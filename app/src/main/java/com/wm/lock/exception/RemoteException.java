package com.wm.lock.exception;

/**
 * Created by WM on 2015/8/3.
 */
public class RemoteException extends BizException {

    private Object response;

    public RemoteException() {
        super();
    }

    public RemoteException(Throwable throwable) {
        this(throwable, null);
    }

    public RemoteException(Object response) {
        super();
        this.response = response;
    }

    public RemoteException(Throwable throwable, Object response) {
        super(throwable);
        this.response = response;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

}
