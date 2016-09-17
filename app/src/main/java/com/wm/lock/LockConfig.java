package com.wm.lock;

/**
 * Created by wangmin on 16/7/27.
 */
public final class LockConfig {

    public static final int PHOTO_COUNT_MAX = 8;
    public static final int PHOTO_WIDTH_MAX = 1024;

    public static final int GESTURE_PRINT_MIN_CONNECT = 1;

    public static final String MODE_JUNIT = "mode_junit";
    public static final String MODE_DEV = "mode_dev";
    public static final String MODE_RELEASE = "mode_release";
    public static final String MODE = MODE_JUNIT;

    public static final String HTTP_SERVER = "";
    public static final int HTTP_CONN_TIMEOUT = 60000;
    public static final int HTTP_READ_TIMEOUT = 60000;

    public static final String WS_SERVER = "ws://112.74.27.140:9000/sync/mobile/tenant/";

}
