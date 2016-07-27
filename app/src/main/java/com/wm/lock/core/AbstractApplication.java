package com.wm.lock.core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;

import com.wm.lock.core.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WM on 2015/7/22.
 */
public class AbstractApplication extends MultiDexApplication implements Application.ActivityLifecycleCallbacks {

    public boolean isInited = false;
    private static AbstractApplication mInstance;
    private List<Activity> mBackgroundActs = new ArrayList<>();
    private List<Activity> mRunningActs = new ArrayList<>();
    private boolean isBackgroundRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        registerActivityLifecycleCallbacks(this);
    }

    public static AbstractApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mRunningActs.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (mBackgroundActs.isEmpty()) {
            toForeground();
        }
        mBackgroundActs.add(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        // 更换位置，把最近打开的放在最后面
        if (mRunningActs.isEmpty()) {
            mRunningActs.add(activity);
        }
        else if (mRunningActs.indexOf(activity) != mRunningActs.size() - 1) {
            mRunningActs.remove(activity);
            mRunningActs.add(activity);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        mBackgroundActs.remove(activity);
        if (mBackgroundActs.isEmpty()) {
            toBackground();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mRunningActs.remove(activity);
    }

    public List<Activity> getRunningActs() {
        return mRunningActs;
    }

    public boolean isActivityRunning(Class<?> clazz) {
        final List<Activity> list = getRunningActs();
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }

        for (Activity item : list) {
            if (item.getClass() == clazz) {
                return true;
            }
        }

        return false;
    }

    protected void toForeground() {
        isBackgroundRunning = false;
    }

    protected void toBackground() {
        isBackgroundRunning = true;
    }

    public boolean isBackgroundRunning() {
        return isBackgroundRunning;
    }

    public Activity getCurrActivity() {
        if (mRunningActs == null || mRunningActs.isEmpty()) {
            return null;
        }
        return mRunningActs.get(mRunningActs.size() - 1);
    }

}
