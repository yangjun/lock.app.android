package com.wm.lock.ui.activities;

import android.view.WindowManager;
import android.widget.TextView;

import com.wm.lock.R;
import com.wm.lock.core.async.AsyncExecutor;
import com.wm.lock.core.async.AsyncWork;
import com.wm.lock.core.utils.RedirectUtils;
import com.wm.lock.helper.ExceptionHandler;
import com.wm.lock.http.Rest;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.finalteam.demo.ImageDemoActivity;

/**
 * Created by wangmin on 16/7/27.
 */

@EActivity
public class HomeActivity extends BaseActivity {

    @Bean
    Rest mRest;

    @ViewById(R.id.tv)
    TextView mTv;

    @Override
    protected int getStatusBarColor() {
        return android.R.color.transparent;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_home;
    }

    @Override
    protected void init() {

    }

    private boolean fullScreen;

    @Click(R.id.iv)
    void onFullScreenClick() {
        stateControl(!fullScreen);
        fullScreen = !fullScreen;
    }

    private void stateControl(boolean enable) {
        if (enable) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
        } else {
            WindowManager.LayoutParams attr = getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attr);
        }
    }

    @Click(R.id.btn)
    void onBtnClick() {
        RedirectUtils.goActivity(this, ImageDemoActivity.class);
    }

}
