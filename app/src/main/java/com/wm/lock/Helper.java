package com.wm.lock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.wm.lock.LockApplication;
import com.wm.lock.LockConfig;
import com.wm.lock.core.CrashHandler;
import com.wm.lock.core.cache.CacheManager;
import com.wm.lock.core.logger.Logger;
import com.wm.lock.core.logger.LoggerOption;
import com.wm.lock.core.utils.RedirectUtils;
import com.wm.lock.core.utils.ToastUtils;
import com.wm.lock.entity.params.CommunicationDeleteParam;
import com.wm.lock.exception.BizException;
import com.wm.lock.bugly.BuglyManager;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.biz.IBizService;
import com.wm.lock.module.user.IUserService;
import com.wm.lock.ui.activities.HomeActivity_;
import com.wm.lock.websocket.WebSocketService;

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
    }

    public static void initByLogin() {
        final IBizService bizService = ModuleFactory.getInstance().getModuleInstance(IBizService.class);
        final IUserService userService = ModuleFactory.getInstance().getModuleInstance(IUserService.class);

        // 删除提交的通信记录，由用户手动点击提交
        final CommunicationDeleteParam deleteParam = new CommunicationDeleteParam();
        deleteParam.setSource(userService.getLoginedInfo().getJobNumber());
        deleteParam.setContents(new String[]{
                getDbJson(LockConstants.BIZ_FLAG, LockConstants.BIZ_RESULT)
        });
        bizService.deleteCommunication(deleteParam);
    }

    public static void destroyByExit() {
        ModuleFactory.getInstance().destroy();
        CacheManager.getInstance().deleteAll(CacheManager.CHANNEL_RUNTIME);
        CacheManager.getInstance().destroy();
    }

    public static void destroyByLogoff() {
        ModuleFactory.getInstance().getModuleInstance(IUserService.class).logoff();
    }

    public static void logoff(final Activity act) {
        if (act == null) {
            return;
        }

        destroyByLogoff();
        // TODO
//        if (act instanceof HomeActivity) {
//            ((HomeActivity) act).logoff();
//        } else if (TechApplication.getInstance().isActivityRunning(HomeActivity_.class)) {
//            Intent it = new Intent();
//            it.putExtra(Constants.DATA, Constants.FLAG_LOGOFF);
//            RedirectUtils.goActivityByClearTopForUnReCrate(act, HomeActivity_.class, it);
//        } else {
//            RedirectUtils.goActivityByClearTopForUnReCrate(act, LoginWithUserNameActivity_.class);
//        }
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

        // 跳转到主页
        RedirectUtils.goActivity(ctx, HomeActivity_.class);
    }

    public static String getDbJson(String key, String value) {
       return String.format("\\\"%s\\\":\\\"%s\\\"", key, value);
    }

}
