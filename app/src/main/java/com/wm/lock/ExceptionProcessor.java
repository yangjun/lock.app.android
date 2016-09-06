package com.wm.lock;

import android.app.Dialog;

import com.wm.lock.core.logger.Logger;
import com.wm.lock.exception.BizException;
import com.wm.lock.exception.DbException;
import com.wm.lock.exception.RemoteException;

/**
 * Created by wm on 15/9/11.
 */
public final class ExceptionProcessor {

    private static Dialog mDialog;
    private ExceptionProcessor() {
    }

    public static void show(String operation, Throwable e) {
        log(operation, e);
        show(e);
    }

    public static void log(String operation, Throwable e) {
        if (e == null) {
            return;
        }

//        if (e instanceof RemoteException) {
//            Object responseObj = ((RemoteException) e).getResponse();
//            if (responseObj == null) {
//                Logger.p(operation, e);
//                return;
//            }
//
//            try {
//                String responseStr = responseObj instanceof RestResult ? GsonUtils.toJson(responseObj) : responseObj.toString();
//                operation = TextUtils.isEmpty(operation) ? responseStr : (operation + ", " + responseStr);
//                Logger.p(operation, e);
//            } catch (Exception e1) {
//                Logger.d(e1);
//            }
//        }
//        else {
            Logger.p(operation, e);
//        }
    }

    private static void show(Throwable e) {
        if (e == null) {
            return;
        }

        if (!(e instanceof BizException)) {
            showTip(getString(R.string.message_connet_error));
        }
        else if (e instanceof RemoteException) {
            handleRemoteException((RemoteException) e);
        }
        else if (e instanceof DbException) {
            handleDbException((DbException) e);
        }
        else {
            showTip(R.string.message_connet_error);
        }
    }

    private static void handleRemoteException(RemoteException e) {
//        RestErrorData errorData = getRestError(e);
//        if (errorData != null && handleSpecialCode(errorData.getCode())) {
//            return;
//        }
//
//        if (!e.isNeedNotify()) {
//            return;
//        }
//
//        if (!HardwareUtils.isNetworkAvailable(TechApplication.getInstance())) {
//            showTip(R.string.msg_no_net);
//        }
//        else if (errorData != null && !TextUtils.isEmpty(errorData.getErrorMsg())) {
//            showTip(errorData.getErrorMsg());
//        }
//        else {
//            showTip(R.string.msg_http_fail);
//        }
    }

    private static void handleDbException(DbException e) {
        if (e.isNeedNotify()) {
            showTip(R.string.message_connet_error);
        }
    }

//    private static boolean handleSpecialCode(String code) {
//        Activity act = TechApplication.getInstance().getCurrActivity();
//        if (act == null) {
//            return false;
//        }
//
//        final boolean isSpecialPage = act instanceof LoginWithUserNameActivity;
//        if (isSpecialPage) {
//            return false;
//        }
//
//        if (TextUtils.equals(code, "IAM_MOBILE_001") || TextUtils.equals(code, "SYSTEM-0002")) {
//            showLogoffDialog(R.string.msg_logoff_by_exception);
//            return true;
//        }
//        if (TextUtils.equals(code, "IAM_MOBILE_002")) {
//            showLogoffDialog(R.string.msg_logoff_by_repeat);
//            return true;
//        }
//        return false;
//    }
//
//    private static RestErrorData getRestError(RemoteException e) {
//        Object response = e.getResponse();
//        if (response != null && response instanceof RestResult) {
//            Object data = ((RestResult) response).getData();
//            if (data != null && data instanceof RestErrorData) {
//                return  (RestErrorData) data;
//            }
//        }
//        return null;
//    }

    private static void showTip(int resId) {
        showTip(getString(resId));
    }

    private static void showTip(String msg) {
//        ThemedToast.show(TechApplication.getInstance(), msg);
        Helper.showTip(msg);
    }

    private static String getString(int resId) {
        return LockApplication.getInstance().getString(resId);
    }

}
