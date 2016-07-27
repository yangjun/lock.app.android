package com.wm.lock.module.biz;

import android.content.Context;

import com.wm.lock.module.BaseModule;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

/**
 * Created by WM on 2015/8/6.
 */
@EBean
public abstract class BizServiceBase extends BaseModule implements IBizService {

    @RootContext
    Context mCtx;

}
