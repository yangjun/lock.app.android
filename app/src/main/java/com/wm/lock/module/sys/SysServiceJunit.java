package com.wm.lock.module.sys;

import android.text.TextUtils;

import com.wm.lock.LockConstants;
import com.wm.lock.core.cache.CacheManager;
import com.wm.lock.core.utils.HardwareUtils;
import com.wm.lock.entity.VersionInfo;

import org.androidannotations.annotations.EBean;

/**
 * Created by WM on 2015/8/1.
 */
@EBean
public class SysServiceJunit extends SysServiceBase {

    @Override
    public VersionInfo checkVersion() {
        waitting();
        VersionInfo android = new VersionInfo();
        android.setAddress("http://ddmyapp.kw.tc.qq.com/16891/A30A19FF89B7617CC783E41187863D54.apk?mkey=56fd294e7f38b243&f=d51b&fsname=com.tencent.mobileqq_6.2.3_336.apk&p=.apk");
        android.setFixlist(new String[]{"修复了一些BUG", "完善了部分界面，使得整体风格更大气上档次"});
        android.setInfo("发现新版本");
        android.setVersion("1.1.2");
        if (!TextUtils.equals(android.getVersion(), HardwareUtils.getVerName(mCtx))) {
            CacheManager.getInstance().putObject(LockConstants.VERSION, android);
            return android;
        }
        else {
            CacheManager.getInstance().deleteObject(LockConstants.VERSION);
            return null;
        }
    }

}
