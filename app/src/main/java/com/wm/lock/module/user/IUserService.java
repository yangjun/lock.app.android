package com.wm.lock.module.user;

import com.wm.lock.entity.UserInfo;

/**
 * Created by WM on 2015/7/29.
 */
public interface IUserService {

    public void register(UserInfo user);

    public UserInfo getLoginedInfo();

    public void logoff();

    public boolean hasLogin();

}
