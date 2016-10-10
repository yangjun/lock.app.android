package com.wm.lock.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.wm.lock.helper.Helper;
import com.wm.lock.LockConstants;
import com.wm.lock.core.utils.RedirectUtils;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.user.IUserService;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;

/**
 * Created by wangmin on 16/9/5.
 */
@EActivity
public class LoginActivity extends BaseActivity {

    private static final int REQUEST_GESTURE_PRINT_VERIFY = 1;

    @Override
    protected int getContentViewId() {
        return -1;
    }

    @Override
    protected void init() {
        final IUserService userService = ModuleFactory.getInstance().getModuleInstance(IUserService.class);
        if (userService.hasLogin()) {
            login();
        }
        else {
            register();
        }
    }

    @OnActivityResult(REQUEST_GESTURE_PRINT_VERIFY)
    void onVerifyGesturePrintResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Helper.goHomePage(this);
        }
        finish();
    }

    private void login() {
        final Bundle bundle = new Bundle();
        bundle.putBoolean(LockConstants.SHOW_BACK_BTN, false);
        RedirectUtils.goActivityForResult(this, GesturePrintVerifyActivity_.class, bundle, REQUEST_GESTURE_PRINT_VERIFY);
    }

    private void register() {
        RedirectUtils.goActivity(this, RegisterActivity_.class);
        finish();
    }

}
