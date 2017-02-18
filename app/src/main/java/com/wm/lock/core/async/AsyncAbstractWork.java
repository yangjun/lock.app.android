package com.wm.lock.core.async;

public abstract class AsyncAbstractWork<T> implements AsyncWork<T> {

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onFail(Exception e) {

    }

    @Override
    public void onSuccess(T result) {

    }

}
