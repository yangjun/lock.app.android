package com.wm.lock.attachment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.wm.lock.LockApplication;
import com.wm.lock.LockConfig;
import com.wm.lock.LockConstants;
import com.wm.lock.R;
import com.wm.lock.core.logger.Logger;
import com.wm.lock.core.utils.CollectionUtils;
import com.wm.lock.core.utils.NetWorkUtils;
import com.wm.lock.core.utils.NotificationUtils;
import com.wm.lock.entity.AttachmentUpload;
import com.wm.lock.entity.AttachmentUploadSource;
import com.wm.lock.entity.UserInfo;
import com.wm.lock.helper.NotificationHelper;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.biz.IBizService;
import com.wm.lock.module.user.IUserService;
import com.wm.lock.websocket.WebSocketWriter;

import java.util.List;

import static android.R.string.no;

public class AttachmentProcessor {

    /**
     * 是否正在写入
     */
    private volatile boolean isExecuting = false;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            switch (action) {
                case "android.net.conn.CONNECTIVITY_CHANGE":
                    onNetWorkChanged(context);
                    break;
            }
        }

        private void onNetWorkChanged(Context context) {
            final int type = NetWorkUtils.getNetworkState(context);
            if (type != NetWorkUtils.NETWORK_NONE) {
                startIfNot();
            }
        }
    };

    private AttachmentProcessor() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        LockApplication.getInstance().registerReceiver(mReceiver, intentFilter);
    }

    private static final class InstanceHolder {
        static final AttachmentProcessor instance = new AttachmentProcessor();
    }

    public static AttachmentProcessor getInstance() {
        return InstanceHolder.instance;
    }

    public synchronized void startIfNot() {
        if (!canConnect()) {
            return;
        }

        if (isExecuting) {
            return;
        }

        isExecuting = true;

        new Thread() {
            @Override
            public void run() {
                try {
                    final List<AttachmentUpload> list = bizService().findNextAttachmentUploadGroup(loginUser().getJobNumber());
                    if (CollectionUtils.isEmpty(list)) {
                        AttachmentProcessor.this.stop();
                    }
                    else {
                        pre();
                        bizService().uploadAttachments(list);
                        success(list);
                        startIfNot(); //继续
                    }
                } catch (Exception e) {
                    fail(e);
                }
            }
        }.start();
    }

    public synchronized void stop() {
        isExecuting = false;
        cancelNotification();
    }

    private void pre() {
        showNotification();
    }

    private void success(List<AttachmentUpload> list) {
        final AttachmentUpload attachmentUpload = list.get(0);
        if (attachmentUpload.getSource() == AttachmentUploadSource.INSPECTION) {
            WebSocketWriter.submitInspection(attachmentUpload.getForeignId(), false);
        }
        bizService().deleteAttachmentUpload(loginUser().getJobNumber(), attachmentUpload.getForeignId(), attachmentUpload.getSource());
        stop();
    }

    private void fail(Exception e) {
        Logger.p("fail to upload images", e);
        stop();
    }

    private boolean canConnect() {
        if (!userService().hasLogin()) {
            return false;
        }
        final int type = NetWorkUtils.getNetworkState(LockApplication.getInstance());
        return type >= LockConfig.NETWORK_MIN;
    }

    private UserInfo loginUser() {
        return userService().getLoginedInfo();
    }

    private IUserService userService() {
        return ModuleFactory.getInstance().getModuleInstance(IUserService.class);
    }

    private IBizService bizService() {
        return ModuleFactory.getInstance().getModuleInstance(IBizService.class);
    }

    private void showNotification() {
        NotificationHelper.showSync();
    }

    private void cancelNotification() {
        NotificationHelper.dismissSync();
    }

    private String getString(int resId) {
        return LockApplication.getInstance().getString(resId);
    }

}
