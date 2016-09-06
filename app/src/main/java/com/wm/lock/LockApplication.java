package com.wm.lock;

import com.wm.lock.core.AbstractApplication;
import com.wm.lock.dto.AppDisplayBackgroundDto;
import com.wm.lock.dto.AppDisplayForegroundDto;

import de.greenrobot.event.EventBus;

/**
 * Created by wangmin on 16/7/27.
 */
public class LockApplication extends AbstractApplication {

    private boolean isFirst = true;

//    @Override
//    public void onCreate() {
//        super.onCreate();
//        LeakCanary.install(this); //OOM检测
//    }

    @Override
    protected void toForeground() {
        super.toForeground();

        if (!isFirst) {
            EventBus.getDefault().post(new AppDisplayForegroundDto());
        }
        isFirst = false;
    }

    @Override
    protected void toBackground() {
        super.toBackground();
        EventBus.getDefault().post(new AppDisplayBackgroundDto());
    }

}
