package com.wm.lock.module.sys;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.wm.lock.LockConstants;
import com.wm.lock.core.cache.CacheManager;
import com.wm.lock.core.utils.HardwareUtils;
import com.wm.lock.entity.VersionInfo;
import com.wm.lock.module.BaseModule;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

/**
 * Created by WM on 2015/8/1.
 */
@EBean
public abstract class SysServiceBase extends BaseModule implements ISysService {

    private static final String KEY_VERSION = "key_version";

    @RootContext
    Context mCtx;

    @Override
    public int getCacheAppVersion() {
        return CacheManager.getInstance().getInt(KEY_VERSION, 0, CacheManager.CHANNEL_PREFERENCE);
    }

    @Override
    public void setCacheAppVersion() {
        int version = HardwareUtils.getVerCode(mCtx);
        CacheManager.getInstance().putInt(KEY_VERSION, version, CacheManager.CHANNEL_PREFERENCE);
    }

    @Override
    public boolean isNewInstall() {
        int currVersion = HardwareUtils.getVerCode(mCtx);
        int cacheVersion = getCacheAppVersion();
        return currVersion > cacheVersion;
    }

//    @Override
//    public void ignoreVersion(VersionInfo version) {
//        // TODO
//    }
//
//    @Override
//    public void downloadNewVersion(Context ctx, VersionInfo version) {
//        Intent intent = new Intent();
//        intent.setAction("android.intent.action.VIEW");
//        Uri url = Uri.parse(version.getAddress());
//        intent.setData(url);
//        ctx.startActivity(intent);
//    }
//
//    @Override
//    public VersionInfo getNewVersionInCache() {
//        Object obj = CacheManager.getInstance().getObject(LockConstants.VERSION);
//        if (obj == null) {
//            return null;
//        }
//        return (VersionInfo) obj;
//    }

}
