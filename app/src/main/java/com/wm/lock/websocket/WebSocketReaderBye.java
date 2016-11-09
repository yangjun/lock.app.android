package com.wm.lock.websocket;

import android.app.Activity;

import com.wm.lock.LockApplication;
import com.wm.lock.R;
import com.wm.lock.dialog.DialogManager;
import com.wm.lock.entity.Chat;
import com.wm.lock.helper.Helper;

public class WebSocketReaderBye extends WebSocketReaderBase {

    @Override
    void execute(Chat chat) {
        final Activity act = LockApplication.getInstance().getCurrActivity();
        Helper.logoff(act);
        showLogoffDialog();
    }

    private void showLogoffDialog() {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Activity act = LockApplication.getInstance().getCurrActivity();
                DialogManager.showNotifyDialog(act, R.string.label_notify, act.getString(R.string.message_pull_logoff), false);
            }
        }, 300);
    }

}
