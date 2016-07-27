package com.wm.lock.entity.params;

/**
 * Created by WM on 2015/7/29.
 */
public class UserLoginParam extends BaseParam {

    /** 用户名 */
    private String userName;

    /** 密码 */
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
