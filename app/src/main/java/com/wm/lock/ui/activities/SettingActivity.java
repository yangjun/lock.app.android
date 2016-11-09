package com.wm.lock.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tencent.bugly.beta.UpgradeInfo;
import com.wm.lock.R;
import com.wm.lock.core.callback.Injector;
import com.wm.lock.core.utils.RedirectUtils;
import com.wm.lock.dialog.DialogManager;
import com.wm.lock.entity.UserInfo;
import com.wm.lock.bugly.BuglyManager;
import com.wm.lock.helper.Helper;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.user.IUserService;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

/**
 * Created by wangmin on 16/9/5.
 */
@EActivity
public class SettingActivity extends BaseActivity {

    @ViewById(R.id.tv_job_number)
    TextView mTvJobNumber;

    @ViewById(R.id.iv_upgrade)
    View mVIndicatorUpgrade;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateIndicator();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

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
        BuglyManager.checkUpgradeByBtn();
    }

    @Click(R.id.btn_logoff)
    void onLogoffClick() {
        DialogManager.showConfirmDialog(this, R.string.label_notify, getString(R.string.message_logoff_confirm), false, new Injector() {
            @Override
            public void execute() {
                Helper.logoff(SettingActivity.this);
            }
        });
    }

    private void updateIndicator() {
        final boolean hasNew = BuglyManager.hasUpgradeInfo(getApplicationContext());
        mVIndicatorUpgrade.setVisibility(hasNew ? View.VISIBLE : View.GONE);
    }

    public void onEventMainThread(UpgradeInfo upgradeInfo) {
        updateIndicator();
    }

}
