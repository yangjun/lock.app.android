package com.wm.lock.module;

import com.wm.lock.exception.BizException;

/**
 * Created by WM on 2015/8/3.
 */
public class BaseModule {

    protected void waitting() throws BizException {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new BizException(e);
        }
    }

}
