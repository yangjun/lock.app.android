package com.wm.lock.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.wm.lock.Helper;
import com.wm.lock.LockConstants;
import com.wm.lock.R;
import com.wm.lock.core.utils.RedirectUtils;
import com.wm.lock.core.utils.StringUtils;
import com.wm.lock.entity.UserInfo;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.user.IUserService;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

/**
 * Created by wangmin on 16/9/5.
 */
@EActivity
public class RegisterActivity extends BaseActivity {

    private static final int REQUEST_GESTURE_PRINT = 1;

    @ViewById(R.id.et_job_number)
    EditText mEtJobNumber;

    @ViewById(R.id.et_name)
    EditText mEtName;

    @ViewById(R.id.et_lock_pwd)
    EditText mEtLockPwd;

    @ViewById(R.id.et_lock_pwd_repeat)
    EditText mEtLockPwdRepeat;

    @Override
    protected int getContentViewId() {
        return R.layout.act_register;
    }

    @Override
    protected void init() {
        setBackBtnVisible(false);
    }

    @Click(R.id.btn_next)
    void onNextClick() {
        final int msgId = checkInput();
        if (msgId <= 0) {
            RedirectUtils.goActivityForResult(this, GesturePrintEnrollActivity_.class, REQUEST_GESTURE_PRINT);
        }
        else {
            showTip(msgId);
        }
    }

    @OnActivityResult(REQUEST_GESTURE_PRINT)
    void onEnrollGesturePrintResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            final UserInfo user = new UserInfo();
            user.setJobNumber(mEtJobNumber.getText().toString().trim());
            user.setName(mEtName.getText().toString().trim());
            user.setLockPwd(mEtLockPwd.getText().toString().trim());
            user.setGesturePwd(data.getStringExtra(LockConstants.PWD_GESTURE));

            final IUserService userService = ModuleFactory.getInstance().getModuleInstance(IUserService.class);
            userService.register(user);

            Helper.goHomePage(this);
            finish();
        }
    }

    private int checkInput() {
        if (StringUtils.isEmpty(mEtJobNumber)) {
            return R.string.empty_job_number;
        }
        if (StringUtils.isEmpty(mEtName)) {
            return R.string.empty_name;
        }
        if (StringUtils.isEmpty(mEtLockPwd)) {
            return R.string.empty_lock_pwd;
        }
        if (StringUtils.isEmpty(mEtLockPwdRepeat)) {
            return R.string.empty_lock_pwd_repeat;
        }
        if (!StringUtils.equals(mEtLockPwd, mEtLockPwdRepeat)) {
            return R.string.message_lock_pwd_not_equal;
        }
        return -1;
    }

}
