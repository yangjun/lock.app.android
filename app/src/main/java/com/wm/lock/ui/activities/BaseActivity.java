package com.wm.lock.ui.activities;

import android.app.Dialog;

import com.umeng.analytics.MobclickAgent;
import com.wm.lock.R;
import com.wm.lock.core.AbstractActivity;
import com.wm.lock.helper.Helper;
import com.wm.lock.helper.dialog.DialogManager;

import org.androidannotations.annotations.EActivity;

/**
 * Created by WM on 2015/7/23.
 */
@EActivity
public abstract class BaseActivity extends AbstractActivity {

    protected Dialog mDialog;

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void showTip(CharSequence message) {
        Helper.showTip(message);
    }

    @Override
    public void showTip(int messageId) {
        Helper.showTip(messageId);
    }

    @Override
    protected void onDestroy() {
        dismissDialog();
        super.onDestroy();
    }

    public void showDialog(Dialog dialog) {
        if (dialog != null) {
            dismissDialog();
            mDialog = dialog;
            mDialog.show();
        }
    }

    public void dismissDialog() {
        DialogManager.dismissDialog(mDialog);
    }

    public void showWaittingDialog(int messageId) {
        showWaittingDialog(getString(messageId));
    }

    public void showWaittingDialog(String message) {
        showWaittingDialog(R.string.holdon, message);
    }

    public void showWaittingDialog(int titleId, String message) {
        DialogManager.showWaittingDialog(this, titleId, message, false);
    }

}
