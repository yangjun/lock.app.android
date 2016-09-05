package com.wm.lock.helper;

import android.app.Activity;
import android.content.Context;

import com.umeng.analytics.MobclickAgent;
import com.wm.lock.LockApplication;
import com.wm.lock.LockConfig;
import com.wm.lock.core.CrashHandler;
import com.wm.lock.core.cache.CacheManager;
import com.wm.lock.core.logger.Logger;
import com.wm.lock.core.logger.LoggerOption;
import com.wm.lock.core.utils.ToastUtils;
import com.wm.lock.exception.BizException;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.user.IUserService;

/**
 * Created by wangmin on 16/7/27.
 */
public final class Helper {

    public static void init(Context ctx) throws BizException {
        final boolean isDebugMode = !LockConfig.MODE.equals(LockConfig.MODE_RELEASE);

        //Statistics
        MobclickAgent.setDebugMode(isDebugMode);
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
                            public void report(Context ctx, String message) {
                                MobclickAgent.reportError(ctx, message);
                            }

                            @Override
                            public void report(Context ctx, Throwable t) {
                                MobclickAgent.reportError(ctx, t);
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
        ToastUtils.showShortCenterToast(LockApplication.getInstance(), resId, 0, 0);
    }

    public static void showTip(CharSequence cs) {
        ToastUtils.showShortCenterToast(LockApplication.getInstance(), cs, 0, 0);
    }

}
