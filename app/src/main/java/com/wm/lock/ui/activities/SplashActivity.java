package com.wm.lock.ui.activities;

import android.os.Bundle;
import android.widget.ImageView;

import com.wm.lock.LockConstants;
import com.wm.lock.R;
import com.wm.lock.core.utils.RedirectUtils;
import com.wm.lock.ExceptionProcessor;
import com.wm.lock.Helper;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * Created by WM on 2015/7/23.
 */
@EActivity
public class SplashActivity extends BaseActivity {

    @ViewById(R.id.iv)
    ImageView mIv;

    @Override
    protected int getContentViewId() {
        return R.layout.act_splash;
    }

    @Override
    protected void init() {
        prepareApp();
    }

    @Background
    void prepareApp() {
        try {
            Helper.init(getApplicationContext());
            startApp();
        }
        catch (Throwable e) {
            onInitFailed(e);
        }
    }

    @UiThread(delay=2000)
    void startApp() {
        next();
    }

    @UiThread
    void onInitFailed(Throwable e) {
        ExceptionProcessor.log("fail to init app", e);
        showTip(R.string.message_inner_error);
        finish();
    }

    @Override
    public void onBackPressed() {

    }

    private void next() {
        if (!isDestroyed) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(LockConstants.FLAG, true);
            RedirectUtils.goActivity(this, GuideActivity_.class, bundle);
            finish();
        }
    }

}
