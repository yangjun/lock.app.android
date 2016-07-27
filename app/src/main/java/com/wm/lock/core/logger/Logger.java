package com.wm.lock.core.logger;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016/4/14.
 */
public final class Logger {

    private static final String TAG = Logger.class.getSimpleName();

    private static Context mCtx;
    private static LoggerOption mOption;

    private Logger() { }

    public static void init(Context ctx) {
        init(ctx, null);
    }

    public static void init(Context ctx, LoggerOption option) {
        mCtx = ctx;
        mOption = option;
    }

    public static void destory() {
        mOption = null;
        mCtx = null;
    }

    public static void d(String msg) {
        if (debug()) {
            msg = getMsg(msg, getFromStr());
            Log.e(TAG, msg);
        }
    }

    public static void d(Throwable throwable) {
        if (debug() && throwable != null) {
            throwable.printStackTrace();
        }
    }

    public static void d(String msg, Throwable throwable) {
        if (debug() && throwable != null) {
            Log.e(TAG, getUserMsg(msg));
            throwable.printStackTrace();
        }
    }

    public static void p(String msg) {
        d(msg);

        if (mOption == null || mOption.getReportProvider() == null) {
            return;
        }
        if (mCtx != null && !TextUtils.isEmpty(msg)) {
            msg = getMsg(getUserMsg(msg), getFromStr(), getDeviceStr());
            mOption.getReportProvider().report(mCtx, msg);
        }
    }

    public static void p(Throwable throwable) {
        p("", throwable);
    }

    public static void p(String msg, Throwable throwable) {
        d(msg, throwable);

        if (mOption == null || mOption.getReportProvider() == null) {
            return;
        }
        if (mCtx != null && throwable != null) {
            msg = getMsg(getUserMsg(msg), getDeviceStr());
            Throwable reportThrowable = new Throwable(msg, throwable);
            mOption.getReportProvider().report(mCtx, reportThrowable);
        }
    }

    private static String getMsg(String... args) {
        StringBuilder builder = new StringBuilder();
        for (String item : args) {
            if (TextUtils.isEmpty(item)) {
                continue;
            }
            builder.append(item);
            if (!TextUtils.equals(item, "\n")) {
                builder.append("\n");
            }
        }
        return getWrapStr(builder);
    }

    private static String getUserMsg(String msg) {
        StringBuilder builder = new StringBuilder();
        final String userStr = getUserStr();
        if (!TextUtils.isEmpty(userStr)) {
            builder.append(userStr).append("，");
        }
        String message = TextUtils.isEmpty(msg) ? "unknown message" : msg;
        builder.append(message);
        return builder.toString();
    }

    private static String getUserStr() {
        final String imei = getImei();

        if (mOption == null) {
            return imei;
        }

        final LoggerOption.DataProvider<String> userProvider = mOption.getUserDataProvider();
        if (userProvider == null) {
            return imei;
        }

        final String userFlag = userProvider.getData();
        return TextUtils.isEmpty(userFlag) ? imei : userFlag;
    }

//    private static String getOptionStr() {
//        if (mOption == null) {
//            return "";
//        }
//
//        StringBuilder builder = new StringBuilder();
//        final LoggerOption.DataProvider<String> userProvider = mOption.getUserDataProvider();
//        if (userProvider != null && !TextUtils.isEmpty(userProvider.getData())) {
//            builder.append("userData").append(": ").append(userProvider.getData());
//        }
//        return builder.toString();
//    }

    private static String getDeviceStr() {
        final StringBuilder builder = new StringBuilder();

        final String imei = getImei();
        if (!TextUtils.isEmpty(imei)) {
            builder.append("imei").append(": ").append(imei).append("\n");
        }

        Field[] fields = Build.class.getDeclaredFields();
        for ( Field field : fields) {
            boolean isAccessible = true;
            try {
                isAccessible = field.isAccessible();
                if (!isAccessible) {
                    field.setAccessible(true);
                }
                Object value = field.get(null);
                if (value != null) {
                    builder.append(field.getName()).append(": ").append(value.toString()).append("\n");
                }
            }
            catch (Exception e) {
                d("fail to collect device info!", e);
            }
            finally {
                if (!isAccessible) {
                    field.setAccessible(false);
                }
            }
        }
        return getWrapStr(builder);
    }

    private static String getFromStr() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return "";
        }

        final StringBuilder builder = new StringBuilder();
        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (st.getClassName().equals(Logger.class.getName())) {
                continue;
            }
            builder.append("at ")
                    .append(st.getClassName())
                    .append(".")
                    .append(st.getMethodName())
                    .append("(")
                    .append(st.getFileName())
                    .append(":")
                    .append(st.getLineNumber())
                    .append(")")
                    .append("\n");
        }
        return getWrapStr(builder);
    }

    private static String getImei() {
        if (mCtx == null) {
            return "";
        }
        TelephonyManager telephonyManager = (TelephonyManager) mCtx.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    private static String getWrapStr(StringBuilder builder) {
        String result = builder.toString();
        if (TextUtils.isEmpty(result)) {
            return "";
        }
        if (result.endsWith("\n")) {
            return result.substring(0, result.length() - 1);
        }
        return result;
    }

    private static boolean debug() {
        return mOption != null && mOption.isDebug();
    }

}
