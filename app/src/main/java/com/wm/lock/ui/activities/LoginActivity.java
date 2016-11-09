package com.wm.lock.ui.activities;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.EditText;

import com.wm.lock.R;
import com.wm.lock.core.utils.RedirectUtils;
import com.wm.lock.core.utils.StringUtils;
import com.wm.lock.dialog.DialogManager;
import com.wm.lock.entity.UserInfo;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

@EActivity
public class LoginActivity extends BaseActivity {

    @ViewById(R.id.et_job_number)
    EditText mEtJobNumber;

    @ViewById(R.id.et_lock_pwd)
    EditText mEtLockPwd;

    @Override
    protected int getContentViewId() {
        return R.layout.act_login;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Click(R.id.btn_next)
    void onLoginClick() {
        final int msgId = checkInput();
        if (msgId > 0) {
            showTip(msgId);
        }
        else {
            login();
        }
    }

    private int checkInput() {
        if (StringUtils.isEmpty(mEtJobNumber)) {
            return R.string.empty_job_number;
        }
        if (StringUtils.isEmpty(mEtLockPwd)) {
            return R.string.empty_lock_pwd;
        }
        return -1;
    }

    private void login() {
        mDialog = DialogManager.showWaittingDialog(this, R.string.holdon, getString(R.string.message_loging), false);
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                EventBus.getDefault().unregister(this);
            }
        });
        EventBus.getDefault().register(this);
        // TODO 执行websocket
    }

    public void onEventMainThread(UserInfo user) {
        if (TextUtils.isEmpty(user.getJobNumber())) {
            showTip(R.string.message_login_fail);
        }
        else if (user.getJobNumber().equals(mEtJobNumber.getText().toString().trim())) {
            // TODO
        }
    }

}
