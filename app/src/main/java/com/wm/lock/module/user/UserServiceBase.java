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
        mLoginUser.setUserApiKey("");
        mLoginUser.setPassword("");
        setCache(mLoginUser);
    }

    @Override
    public boolean canAutoLogin() {
        return !TextUtils.isEmpty(getLoginedInfo().getUserId());
    }

    @Override
    public boolean hasLogin() {
        final String uid = getLoginedInfo().getUserId();
        return !TextUtils.isEmpty(uid);
    }

    protected UserInfo login(String account, String pwd) {
        mLoginUser.setAccount(account);
        mLoginUser.setPassword(pwd);
        setCache(mLoginUser);
        return mLoginUser;
    }

    protected final void setCache(UserInfo user) {
        CacheManager cacheManager = CacheManager.getInstance();
        cacheManager.putString(LockConstants.ID, user.getUserApiKey(), CacheManager.CHANNEL_PREFERENCE);
        cacheManager.putString(LockConstants.ACCOUNT, SecurityManager.base64Encode(user.getAccount()), CacheManager.CHANNEL_PREFERENCE);
//        cacheManager.putString(Constants.PASSWORD, SecurityManager.base64Encode(user.getPassword()), CacheManager.CHANNEL_PREFERENCE);
    }

    protected final UserInfo getCache() {
        UserInfo result = new UserInfo();
        CacheManager cacheManager = CacheManager.getInstance();
        result.setUserApiKey(cacheManager.getString(LockConstants.ID, CacheManager.CHANNEL_PREFERENCE));
        result.setAccount(SecurityManager.base64Decode(cacheManager.getString(LockConstants.ACCOUNT, CacheManager.CHANNEL_PREFERENCE)));
//        result.setPassword(SecurityManager.base64Decode(cacheManager.getString(Constants.PASSWORD, CacheManager.CHANNEL_PREFERENCE)));
        return result;
    }

}
