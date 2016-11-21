package com.wm.lock.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.widget.EditText;

import com.wm.lock.R;
import com.wm.lock.core.async.AsyncExecutor;
import com.wm.lock.core.async.AsyncWork;
import com.wm.lock.core.logger.Logger;
import com.wm.lock.core.utils.HardwareUtils;
import com.wm.lock.core.utils.StringUtils;
import com.wm.lock.dialog.DialogManager;
import com.wm.lock.dto.UserLoginDto;
import com.wm.lock.entity.UserInfo;
import com.wm.lock.helper.Helper;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.user.IUserService;
import com.wm.lock.websocket.WebSocketService;
import com.wm.lock.websocket.WebSocketWriter;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Date;

import de.greenrobot.event.EventBus;

@EActivity
public class LoginActivity extends BaseActivity {

    @ViewById(R.id.et_job_number)
    EditText mEtJobNumber;

    @ViewById(R.id.et_lock_pwd)
    EditText mEtLockPwd;

    private static final int INTERVAL_TIMEOUT = 30000;
    private Handler mHandler = new Handler();
    private Runnable mTimeoutRunnable;
    private AsyncExecutor mAsyncExecutor;

    @Override
    protected int getContentViewId() {
        return R.layout.act_login;
    }

    @Override
    protected void init() {
        setBackBtnVisible(false);

        if (hasLogin()) {
            loginSuccess();
        }
        else {
            final UserInfo user = loginUser();
            mEtJobNumber.setText(user.getJobNumberCopy());

            // 以临时账户登录
            final IUserService userService = ModuleFactory.getInstance().getModuleInstance(IUserService.class);
            final String tempJobNumber = "android_login_" + (new Date().getTime());
            user.setJobNumber(tempJobNumber);
            userService.update(user);

            // 启动临时账户的长连接
            final Intent webSocketIntent = new Intent(getApplicationContext(), WebSocketService.class);
            getApplicationContext().startService(webSocketIntent);
        }
    }

    @Override
    protected void onDestroy() {
        dismissLoginDialog();

        // 如果没有登录成功,注销临时账户的长连接
        if (!hasLogin()) {
            final Intent webSocketIntent = new Intent(getApplicationContext(), WebSocketService.class);
            getApplicationContext().stopService(webSocketIntent);
        }

        super.onDestroy();
    }

    @Click(R.id.btn_next)
    void onLoginClick() {
        final int msgId = check();
        if (msgId > 0) {
            showTip(msgId);
        }
        else {
            login();
        }
    }

    private int check() {
        if (StringUtils.isEmpty(mEtJobNumber)) {
            return R.string.empty_job_number;
        }
        if (StringUtils.isEmpty(mEtLockPwd)) {
            return R.string.empty_lock_pwd;
        }
        if (!HardwareUtils.isNetworkAvailable(getApplicationContext())) {
            return R.string.message_no_net;
        }
        return -1;
    }

    private void login() {
        showLoginDialog();
        final String userJobNumber = mEtJobNumber.getText().toString().trim();
        final String userLockPwd = mEtLockPwd.getText().toString().trim();

        mAsyncExecutor = new AsyncExecutor();
        mAsyncExecutor.execute(new AsyncWork<Void>() {

            @Override
            public void onPreExecute() {
            }

            @Override
            public void onSuccess(Void result) {
//                // FIXME test only, simulate login success
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        final UserLoginDto dto = new UserLoginDto();
//                        dto.setUser_job_number(mEtJobNumber.getText().toString().trim());
//                        dto.setState(1);
//                        EventBus.getDefault().post(dto);
//                    }
//                }, 2000);
            }

            @Override
            public void onFail(Exception e) {
                Logger.p("fail to login", e);
                dismissLoginDialog();
                showTip(R.string.message_login_fail);
            }

            @Override
            public Void onExecute() throws Exception {
                WebSocketWriter.login(userJobNumber, userLockPwd);
                return null;
            }
        });

    }

    public void onEventMainThread(UserLoginDto userLoginDto) {
        if (!userLoginDto.getUser_job_number().equals(mEtJobNumber.getText().toString().trim())) {
            return;
        }

        dismissLoginDialog();

        if (userLoginDto.getState() == 1) {
            final UserInfo user = new UserInfo();
            user.setJobNumber(mEtJobNumber.getText().toString().trim());
            user.setLockPwd(mEtLockPwd.getText().toString().trim());

            final IUserService userService = ModuleFactory.getInstance().getModuleInstance(IUserService.class);
            userService.login(user);

            loginSuccess();
        }
        else {
            showTip(R.string.message_login_illegal);
        }
    }

    private void showLoginDialog() {
        startTimeOut();
        registerLoginListener();

        mDialog = DialogManager.showWaittingDialog(this, R.string.holdon, getString(R.string.message_loging), true);
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                stopTimeOut();
                unRegisterLoginListener();
                if (mAsyncExecutor != null) {
                    mAsyncExecutor.cancel();
                }
            }
        });
    }

    private void dismissLoginDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    private void startTimeOut() {
        mTimeoutRunnable = new Runnable() {
            @Override
            public void run() {
                dismissLoginDialog();
                showTip(R.string.message_login_timeout);
            }
        };
        mHandler.postDelayed(mTimeoutRunnable, INTERVAL_TIMEOUT);
    }

    private void stopTimeOut() {
        if (mTimeoutRunnable != null) {
            mHandler.removeCallbacks(mTimeoutRunnable);
        }
    }

    private void registerLoginListener() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void unRegisterLoginListener() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private boolean hasLogin() {
        final IUserService userService = ModuleFactory.getInstance().getModuleInstance(IUserService.class);
        return userService.hasLogin();
    }

    private void loginSuccess() {
        Helper.goHomePage(this);
        finish();
    }

}
