package com.wm.lock.ui.activities;

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
    protected int getContentViewId() {
        return R.layout.act_home;
    }

    @Override
    protected void init() {
        testHttp();
    }

    @Click(R.id.btn)
    void onBtnClick() {
        RedirectUtils.goActivity(this, ImageDemoActivity.class);
    }

    private void testHttp() {
        new AsyncExecutor().execute(new AsyncWork<String>() {
            @Override
            public void onPreExecute() {
                showWaittingDialog(R.string.holdon_);
            }

            @Override
            public void onSuccess(String result) {
                mTv.setText(result);
                dismissDialog();
            }

            @Override
            public void onFail(Exception e) {
                showTip("失败！");
                ExceptionHandler.log("fail to test http", e);
                dismissDialog();
            }

            @Override
            public String onExecute() throws Exception {
                return mRest.test();
            }
        });
    }

}
