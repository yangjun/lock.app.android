package com.wm.lock.ui.activities;

import android.app.Dialog;
import android.os.Bundle;

import com.wm.lock.R;
import com.wm.lock.core.AbstractActivity;
import com.wm.lock.Helper;
import com.wm.lock.dialog.DialogManager;
import com.wm.lock.entity.UserInfo;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.user.IUserService;

import org.androidannotations.annotations.EActivity;

/**
 * Created by WM on 2015/7/23.
 */
@EActivity
public abstract class BaseActivity extends AbstractActivity {

    protected Dialog mDialog;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        super.setBackBtn(R.mipmap.ic_back);
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

    public Dialog showWaittingDialog(int messageId) {
        return showWaittingDialog(getString(messageId));
    }

    public Dialog showWaittingDialog(String message) {
        return showWaittingDialog(R.string.holdon, message);
    }

    public Dialog showWaittingDialog(int titleId, String message) {
        mDialog = DialogManager.showWaittingDialog(this, titleId, message, false);
        return mDialog;
    }

    public UserInfo loginUser() {
        final IUserService userService = ModuleFactory.getInstance().getModuleInstance(IUserService.class);
        return userService.getLoginedInfo();
    }

}
