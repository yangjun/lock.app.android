package com.wm.lock.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.wm.lock.LockApplication;
import com.wm.lock.LockConfig;
import com.wm.lock.attachment.AttachmentProcessor;
import com.wm.lock.bluetooth.BluetoothManager;
import com.wm.lock.bugly.BuglyManager;
import com.wm.lock.core.CrashHandler;
import com.wm.lock.core.cache.CacheManager;
import com.wm.lock.core.logger.Logger;
import com.wm.lock.core.logger.LoggerOption;
import com.wm.lock.core.utils.RedirectUtils;
import com.wm.lock.entity.InspectionType;
import com.wm.lock.exception.BizException;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.user.IUserService;
import com.wm.lock.ui.activities.HomeActivity_;
import com.wm.lock.ui.activities.InspectionConstructActivity_;
import com.wm.lock.ui.activities.InspectionConstructBaseActivity;
import com.wm.lock.ui.activities.InspectionConstructMakeActivity_;
import com.wm.lock.ui.activities.LoginActivity_;
import com.wm.lock.websocket.WebSocketService;

import java.util.List;

/**
 * Created by wangmin on 16/7/27.
 */
public final class Helper {

    public static void init(Context ctx) throws BizException {
        final boolean isDebugMode = !LockConfig.MODE.equals(LockConfig.MODE_RELEASE);

        BuglyManager.init(ctx);

        Logger.init(ctx, new LoggerOption.Builder()
                        .setDebug(isDebugMode)
                        .setUserDataProvider(new LoggerOption.DataProvider<String>() {
                            @Override
                            public String getData() {
                                return ModuleFactory.getInstance().getModuleInstance(IUserService.class).getLoginedInfo().getJobNumber();
                            }
                        })
                        .setReportProvider(new LoggerOption.ReportProvider() {
                            @Override
                            public void report(String description) {
                                BuglyManager.reportThrowable(description);
                            }

                            @Override
                            public void report(String description, Throwable t) {
                                BuglyManager.reportThrowable(description, t);
                            }
                        })
                        .build()
        );

        //crash handler
        CrashHandler.getInstance().init(ctx, new CrashHandler.CrashCallBack() {
            @Override
            public void onExist() {
                Activity currAct = LockApplication.getInstance().getCurrActivity();
                exit(currAct);
            }

            @Override
            public void showException(String msg) {
                showTip(msg);
            }
        });

        CacheManager.getInstance().init(ctx);

        //module
        ModuleFactory.getInstance().init(ctx);

//        //update
//        UpdateApi.getInstance().init(ctx);

        // bluetooth
        final String error = BluetoothManager.getInstance().checkHardware(ctx);
        if (!TextUtils.isEmpty(error)) {
            throw new BizException(error);
        }
    }

    public static void initByLogin() {
//        final IBizService bizService = ModuleFactory.getInstance().getModuleInstance(IBizService.class);
//        final IUserService userService = ModuleFactory.getInstance().getModuleInstance(IUserService.class);
//
//        // 删除提交的通信记录，由用户手动点击提交
//        final CommunicationDeleteParam deleteParam = new CommunicationDeleteParam();
//        deleteParam.setSource(userService.getLoginedInfo().getJobNumber());
//        deleteParam.setContents(new String[]{
//                getDbJson(LockConstants.BIZ_FLAG, LockConstants.BIZ_RESULT)
//        });
//        bizService.deleteCommunication(deleteParam);
    }

    public static void destroyByExit() {
        ModuleFactory.getInstance().destroy();
        CacheManager.getInstance().deleteAll(CacheManager.CHANNEL_RUNTIME);
        CacheManager.getInstance().destroy();
    }

    public static void destroyByLogoff() {
        final Context ctx = LockApplication.getInstance();

        // 停止web socket服务
        final Intent webSocketIntent = new Intent(ctx, WebSocketService.class);
        ctx.stopService(webSocketIntent);

        // 停止上传附件
        AttachmentProcessor.getInstance().stop();

        // 执行注销
        ModuleFactory.getInstance().getModuleInstance(IUserService.class).logoff();
    }

    public static void logoff(final Activity act) {
        if (act == null) {
            return;
        }

        destroyByLogoff();

        act.startActivity(new Intent(act, LoginActivity_.class));
        final List<Activity> list = LockApplication.getInstance().getRunningActs();
        for (Activity actItem : list) {
            actItem.finish();
        }
    }

    public static void exit(Activity act) {
        // TODO
//        if (act == null) {
//            return;
//        }
//        destroyByExit();
//        if (act instanceof HomeActivity) {
//            ((HomeActivity) act).exit();
//        } else {
//            Intent it = new Intent();
//            it.putExtra(Constants.DATA, Constants.FLAG_EXIT);
//            RedirectUtils.goActivityByClearTopForUnReCrate(act, HomeActivity_.class, it);
//        }
    }

    public static void showTip(int resId) {
        Toast.makeText(LockApplication.getInstance(), resId, Toast.LENGTH_SHORT).show();
    }

    public static void showTip(CharSequence cs) {
        Toast.makeText(LockApplication.getInstance(), cs, Toast.LENGTH_SHORT).show();
    }

    public static void goHomePage(Context ctx) {
        // 登录初始化
        initByLogin();

        // 启动web socket服务
        final Intent webSocketIntent = new Intent(ctx, WebSocketService.class);
        ctx.startService(webSocketIntent);

        // 启动附件上传
        AttachmentProcessor.getInstance().startIfNot();

        // 跳转到主页
        RedirectUtils.goActivity(ctx, HomeActivity_.class);
    }

    public static String getDbJson(String key, String value) {
       return String.format("\\\"%s\\\":\\\"%s\\\"", key, value);
    }

    public static Class<? extends InspectionConstructBaseActivity> getRedirectInspectionActivity(final int type) {
        switch (type) {
            case InspectionType.MAKE:
                return InspectionConstructMakeActivity_.class;
            default:
                return InspectionConstructActivity_.class;
        }
    }

}
