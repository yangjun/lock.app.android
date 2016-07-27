package com.wm.lock.entity;

/**
 * Created by wm on 16/2/14.
 */
public class RestResult<T> {

    private RestResultCode code;
    private T data;

    public RestResultCode getCode() {
        return code;
    }

    public void setCode(RestResultCode code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
