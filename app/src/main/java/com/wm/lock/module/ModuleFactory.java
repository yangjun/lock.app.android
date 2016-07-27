package com.wm.lock.module;

import android.content.Context;

import com.wm.lock.LockConfig;
import com.wm.lock.module.biz.BizServiceJunit_;
import com.wm.lock.module.biz.BizService_;
import com.wm.lock.module.biz.IBizService;
import com.wm.lock.module.sys.ISysService;
import com.wm.lock.module.sys.SysServiceJunit_;
import com.wm.lock.module.sys.SysService_;
import com.wm.lock.module.user.IUserService;
import com.wm.lock.module.user.UserServiceJunit_;
import com.wm.lock.module.user.UserService_;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by WM on 2015/7/29.
 */
public final class ModuleFactory {

    private Context mApplication;
    private Map<Class<?>, Object> moduleMap;

    private ModuleFactory() {

    }

    private static class SingletonInstanceHolder {
        static ModuleFactory instance = new ModuleFactory();
    }

    public static ModuleFactory getInstance() {
        return SingletonInstanceHolder.instance;
    }

    public void init(Context application) {
        this.mApplication = application;
    }

    public void destroy() {

    }

    public <T> T getModuleInstance(Class<T> clazz) {
        if (moduleMap == null) {
            setupModules();
        }

        Object result = moduleMap.get(clazz);
        return result == null ? null : (T) result;
    }

    private void setupModules() {
        moduleMap = new ConcurrentHashMap<>();
        if (LockConfig.MODE == LockConfig.MODE_JUNIT) {
            moduleMap.put(ISysService.class, SysServiceJunit_.getInstance_(mApplication));
            moduleMap.put(IUserService.class, UserServiceJunit_.getInstance_(mApplication));
            moduleMap.put(IBizService.class, BizServiceJunit_.getInstance_(mApplication));
        }
        else {
            moduleMap.put(ISysService.class, SysService_.getInstance_(mApplication));
            moduleMap.put(IUserService.class, UserService_.getInstance_(mApplication));
            moduleMap.put(IBizService.class, BizService_.getInstance_(mApplication));
        }
    }

}
