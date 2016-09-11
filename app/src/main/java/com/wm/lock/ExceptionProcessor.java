package com.wm.lock;

import com.wm.lock.core.logger.Logger;
import com.wm.lock.core.utils.HardwareUtils;
import com.wm.lock.exception.BizException;
import com.wm.lock.exception.DbException;
import com.wm.lock.exception.RemoteException;

/**
 * Created by wm on 15/9/11.
 */
public final class ExceptionProcessor {

    private ExceptionProcessor() {
    }

    public static void show(String operation, Throwable e) {
        show(operation, e, getString(R.string.message_connet_error));
    }

    public static void show(String operation, Throwable e, String defaultMessage) {
        log(operation, e);
        show(e, defaultMessage);
    }

    public static void log(String operation, Throwable e) {
        if (e != null) {
            Logger.p(operation, e);
        }
    }

    private static void show(Throwable e, String defaultMessage) {
        if (e == null) {
            return;
        }

        if (!(e instanceof BizException)) {
            showTip(defaultMessage);
        }
        else if (e instanceof RemoteException) {
            handleRemoteException((RemoteException) e, defaultMessage);
        }
        else if (e instanceof DbException) {
            handleDbException((DbException) e, defaultMessage);
        }
        else {
            showTip(defaultMessage);
        }
    }

    private static void handleRemoteException(RemoteException e, String defaultMessage) {
        if (!HardwareUtils.isNetworkAvailable(LockApplication.getInstance())) {
            showTip(R.string.message_no_net);
        }
        else {
            showTip(defaultMessage);
        }
    }

    private static void handleDbException(DbException e, String defaultMessage) {
        if (e.isNeedNotify()) {
            showTip(defaultMessage);
        }
    }

    private static void showTip(int resId) {
        showTip(getString(resId));
    }

    private static void showTip(String msg) {
        Helper.showTip(msg);
    }

    private static String getString(int resId) {
        return LockApplication.getInstance().getString(resId);
    }

}
