package com.wm.lock.core;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by WM on 2015/7/22.
 */
public abstract class AbstractFragment extends Fragment {

    protected View mView;

    /** 传递的参数 */
    protected Bundle mArguments;
    /** 是否为新建 */
    protected boolean isNewCreate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isNewCreate = (mView == null);
        if (mView == null) {
            mView = onCreateView(inflater, savedInstanceState);
        }
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            onMemoryRecycled();
        }
        else if (isNewCreate) {
            init();
            isNewCreate = false;
        }
    }

    /**
     * 设置传递的参数
     */
    public void setMyArguments(Bundle bundle) {
        this.mArguments = bundle;
    }

    /**
     * 获取传递的参数
     */
    public Bundle getMyArguments() {
        return this.mArguments;
    }

    @Override
    @Deprecated
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    protected View findViewById(int id) {
        return mView.findViewById(id);
    }

    /**
     * 获取要展示的view，如果子类覆盖了本方法，那么getContentViewId()将自动失效。
     */
    protected View onCreateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(getContentViewId(), null);
    }

    /**
     * 要展示view的id
     */
    protected abstract int getContentViewId();

    /**
     * 初始化
     */
    protected abstract void init();

    /**
     * 内存被回收的回调
     */
    protected abstract void onMemoryRecycled();

    /**
     * 是否有菜单
     */
    public boolean hasMenu() {
        return false;
    }

}
