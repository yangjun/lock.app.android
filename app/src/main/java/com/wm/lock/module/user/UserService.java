package com.wm.lock.module.user;

import com.wm.lock.entity.UserInfo;
import com.wm.lock.entity.params.UserLoginParam;

import org.androidannotations.annotations.EBean;

/**
 * Created by WM on 2015/7/29.
 */
@EBean
public class UserService extends UserServiceBase {

    @Override
    public UserInfo login(UserLoginParam param) {
        // TODO
        return null;
//        mLoginUser = mRestClient.login(param);
//        super.login(param.getUserName(), param.getPassword());
//        return mLoginUser;
    }

    @Override
    public void logoff() {
        super.logoff();
        // TODO
//        mRestClient.logoff(notifyServer);
    }

}
