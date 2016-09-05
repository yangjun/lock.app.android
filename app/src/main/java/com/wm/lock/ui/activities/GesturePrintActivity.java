package com.wm.lock.ui.activities;

import android.content.Intent;
import android.widget.TextView;

import com.wm.lock.LockConfig;
import com.wm.lock.LockConstants;
import com.wm.lock.R;
import com.wm.lock.core.widget.GesturePrintView;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.user.IUserService;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity
public abstract class GesturePrintActivity extends BaseActivity implements GesturePrintView.CallBack {

    @ViewById(R.id.tv_indicator)
    TextView mTvIndicator;

    @ViewById(R.id.gpv)
    GesturePrintView mGpv;

    private boolean mEnableBack;
    private boolean mShowBackBtn;

    @Override
    public void onBackPressed() {
        if (mEnableBack) {
            super.onBackPressed();
        }
    }

    @Override
    protected void init() {
        if (mSaveBundle != null) {
            mEnableBack = mSaveBundle.getBoolean(LockConstants.ENABLE_BACK, true);
            mShowBackBtn = mSaveBundle.getBoolean(LockConstants.SHOW_BACK_BTN, true);
        }
        setBackBtnVisible(mShowBackBtn);

        mGpv.setMinConnectPoints(LockConfig.GESTURE_PRINT_MIN_CONNECT);
        mGpv.setCallBack(this);
    }

    @Override
    public void onSuccess(String password) {
        final Intent it = new Intent();
        it.putExtra(LockConstants.PWD_GESTURE, password);
        setResult(RESULT_OK, it);
        finish();
    }

    @Override
    public void onFail(GesturePrintView.Error error) {
        updateIndicator(getFailMsg(error), R.color.txt_accent);
    }

    protected void updateIndicator(int resId, int txtColor) {
        updateIndicator(getString(resId), txtColor);
    }

    protected void updateIndicator(String text, int txtColor) {
        mTvIndicator.setTextColor(getResources().getColor(txtColor));
        mTvIndicator.setText(text);
    }

    protected String getFailMsg(GesturePrintView.Error error) {
        switch (error) {
            case SHORT:
                return String.format(getString(R.string.gestureprint_short), LockConfig.GESTURE_PRINT_MIN_CONNECT) ;

            default:
                return null;
        }
    }

}
