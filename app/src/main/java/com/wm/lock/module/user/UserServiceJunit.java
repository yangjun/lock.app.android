package com.wm.lock.module.user;

import com.wm.lock.entity.UserInfo;
import com.wm.lock.entity.params.UserLoginParam;

import org.androidannotations.annotations.EBean;

/**
 * Created by WM on 2015/7/29.
 */
@EBean
public class UserServiceJunit extends UserServiceBase {

    @Override
    public UserInfo login(UserLoginParam param) {
        waitting();
        mLoginUser = getTestUser();
        super.login(param.getUserName(), param.getPassword());
        return mLoginUser;
    }

    @Override
    public void logoff() {
        waitting();
        super.logoff();
    }

    private UserInfo getTestUser() {
        UserInfo result = new UserInfo();
        result.setUserApiKey("75YDMSWCGBHCNLEEOEDEDTLHU4");
        return result;
    }

}
