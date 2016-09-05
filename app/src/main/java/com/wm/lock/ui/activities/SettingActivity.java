package com.wm.lock.ui.activities;

import android.widget.TextView;

import com.wm.lock.R;
import com.wm.lock.core.utils.RedirectUtils;
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
public class SettingActivity extends BaseActivity {

    @ViewById(R.id.tv_job_number)
    TextView mTvJobNumber;

    @Override
    protected int getContentViewId() {
        return R.layout.act_setting;
    }

    @Override
    protected void init() {
        final IUserService userService = ModuleFactory.getInstance().getModuleInstance(IUserService.class);
        final UserInfo userInfo = userService.getLoginedInfo();
        mTvJobNumber.setText(userInfo.getJobNumber());
    }

    @Click(R.id.btn_setting_lock_pwd)
    void onLockPwdClick() {
        RedirectUtils.goActivity(this, SettingLockPwdActivity_.class);
    }

    @Click(R.id.btn_setting_upgrade)
    void onUpgradeClick() {
        // TODO
    }

}
