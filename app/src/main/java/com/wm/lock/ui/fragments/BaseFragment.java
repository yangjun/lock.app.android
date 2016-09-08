package com.wm.lock.ui.fragments;

import android.os.Bundle;

import com.wm.lock.core.AbstractFragment;
import com.wm.lock.entity.UserInfo;
import com.wm.lock.ui.activities.BaseActivity;

public abstract class BaseFragment extends AbstractFragment {

    protected BaseActivity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
    }

    @Override
    protected void onMemoryRecycled() {

    }

}
