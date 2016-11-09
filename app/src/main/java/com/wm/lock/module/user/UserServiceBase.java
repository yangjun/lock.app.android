package com.wm.lock.module.user;

import android.content.Context;
import android.text.TextUtils;

import com.wm.lock.LockConstants;
import com.wm.lock.core.cache.CacheManager;
import com.wm.lock.core.security.SecurityManager;
import com.wm.lock.entity.UserInfo;
import com.wm.lock.module.BaseModule;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

/**
 * Created by WM on 2015/7/29.
 */
@EBean
public abstract class UserServiceBase extends BaseModule implements IUserService {

    @RootContext
    Context mCtx;

    protected UserInfo mLoginUser;

    @Override
    public UserInfo getLoginedInfo() {
        if (mLoginUser == null) {
            mLoginUser = getCache();
        }
        return mLoginUser;
    }

    @Override
    public void logoff() {
//        mLoginUser.setJobNumber(null);
        mLoginUser.setName(null);
        mLoginUser.setLockPwd(null);
        mLoginUser.setGesturePwd(null);
        setCache(mLoginUser);
    }

    @Override
    public boolean hasLogin() {
        final String lockPwd = getLoginedInfo().getLockPwd();
        return !TextUtils.isEmpty(lockPwd);
    }

    @Override
    public void login(UserInfo user) {
        user.setJobNumberCopy(user.getJobNumber());
        update(user);
    }

    @Override
    public void register(UserInfo user) {
        user.setJobNumberCopy(user.getJobNumber());
        update(user);
    }

    @Override
    public void update(UserInfo user) {
        mLoginUser = user;
        setCache(mLoginUser);
    }

    protected final void setCache(UserInfo user) {
        CacheManager cacheManager = CacheManager.getInstance();
        cacheManager.putString(LockConstants.JOB_NUMBER, SecurityManager.base64Encode(user.getJobNumber()), CacheManager.CHANNEL_PREFERENCE);
        cacheManager.putString(LockConstants.JOB_NUMBER_COPY, SecurityManager.base64Encode(user.getJobNumberCopy()), CacheManager.CHANNEL_PREFERENCE);
        cacheManager.putString(LockConstants.NAME, SecurityManager.base64Encode(user.getName()), CacheManager.CHANNEL_PREFERENCE);
        cacheManager.putString(LockConstants.PWD_LOCK, SecurityManager.base64Encode(user.getLockPwd()), CacheManager.CHANNEL_PREFERENCE);
        cacheManager.putString(LockConstants.PWD_GESTURE, user.getGesturePwd(), CacheManager.CHANNEL_PREFERENCE);
    }

    protected final UserInfo getCache() {
        UserInfo result = new UserInfo();
        CacheManager cacheManager = CacheManager.getInstance();
        result.setJobNumber(SecurityManager.base64Decode(cacheManager.getString(LockConstants.JOB_NUMBER, CacheManager.CHANNEL_PREFERENCE)));
        result.setJobNumberCopy(SecurityManager.base64Decode(cacheManager.getString(LockConstants.JOB_NUMBER_COPY, CacheManager.CHANNEL_PREFERENCE)));
        result.setName(SecurityManager.base64Decode(cacheManager.getString(LockConstants.NAME, CacheManager.CHANNEL_PREFERENCE)));
        result.setLockPwd(SecurityManager.base64Decode(cacheManager.getString(LockConstants.PWD_LOCK, CacheManager.CHANNEL_PREFERENCE)));
        result.setGesturePwd(cacheManager.getString(LockConstants.PWD_GESTURE, CacheManager.CHANNEL_PREFERENCE));
        return result;
    }

}
