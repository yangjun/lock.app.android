package com.wm.lock.module.user;

import com.wm.lock.entity.UserInfo;
import com.wm.lock.entity.params.UserLoginParam;

/**
 * Created by WM on 2015/7/29.
 */
public interface IUserService {

    public UserInfo login(UserLoginParam param);

    public UserInfo getLoginedInfo();

    public void logoff();

    public boolean canAutoLogin();

    public boolean hasLogin();

}
