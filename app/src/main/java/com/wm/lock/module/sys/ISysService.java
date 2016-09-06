package com.wm.lock.module.sys;

/**
 * Created by WM on 2015/8/1.
 */
public interface ISysService {

    /**
     * 获取缓存的app版本号
     */
    public int getCacheAppVersion();

    /**
     * 设置缓存的app 版本号
     */
    public void setCacheAppVersion();

    /**
     * 是否为新安装
     */
    public boolean isNewInstall();

//    /**
//     * 获取新版本
//     */
//    public VersionInfo checkVersion();
//
//    /**
//     * 缓存中是否有新版本
//     */
//    public VersionInfo getNewVersionInCache();
//
//    /**
//     * 忽略某个版本
//     */
//    public void ignoreVersion(VersionInfo version);
//
//    /**
//     * 下载新版本
//     */
//    public void downloadNewVersion(Context ctx, VersionInfo version);

}
