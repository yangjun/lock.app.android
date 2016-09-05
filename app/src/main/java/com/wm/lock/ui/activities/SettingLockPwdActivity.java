package com.wm.lock.ui.activities;

import android.text.TextUtils;
import android.widget.EditText;

import com.wm.lock.R;
import com.wm.lock.core.utils.StringUtils;
import com.wm.lock.entity.UserInfo;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.user.IUserService;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by wangmin on 16/9/5.
 */
@EActivity
public class SettingLockPwdActivity extends BaseActivity {

    @ViewById(R.id.et_lock_pwd_old)
    EditText mEtLockPwdOld;

    @ViewById(R.id.et_lock_pwd)
    EditText mEtLockPwd;

    @ViewById(R.id.et_lock_pwd_repeat)
    EditText mEtLockPwdRepeat;

    private IUserService mUserService;

    @Override
    protected int getContentViewId() {
        return R.layout.act_setting_lock_pwd;
    }

    @Override
    protected void init() {
        mUserService = ModuleFactory.getInstance().getModuleInstance(IUserService.class);
    }

    @Click(R.id.btn_concern)
    void onConcernClick() {
        final int msgId = checkInput();
        if (msgId <= 0) {
            update();
        }
        else {
            showTip(msgId);
        }
    }

    private void update() {
        final UserInfo user = mUserService.getLoginedInfo();
        user.setLockPwd(mEtLockPwd.getText().toString().trim());
        mUserService.update(user);

        showTip(R.string.message_setting_lock_pwd_success);
        finish();
    }

    private int checkInput() {
        if (StringUtils.isEmpty(mEtLockPwdOld)) {
            return R.string.empty_lock_pwd_old;
        }
        if (StringUtils.isEmpty(mEtLockPwd)) {
            return R.string.empty_lock_pwd_new;
        }
        if (StringUtils.isEmpty(mEtLockPwdRepeat)) {
            return R.string.empty_lock_pwd_new_repeat;
        }
        if (!StringUtils.equals(mEtLockPwd, mEtLockPwdRepeat)) {
            return R.string.message_lock_pwd_not_equal;
        }
        if (!TextUtils.equals(mEtLockPwdOld.getText().toString().trim(), mUserService.getLoginedInfo().getLockPwd())) {
            return R.string.message_setting_lock_pwd_not_same;
        }
        return -1;
    }

}
