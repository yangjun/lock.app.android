package com.wm.lock.entity;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by WM on 2015/7/29.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo {

    /** 用户id */
    private String userApiKey;

    /** 账号 */
    private String account;

    /** 密码 */
    private String password;

    public String getUserId() {
        return userApiKey;
    }

    public void setUserId(String userId) {
        this.userApiKey = userId;
    }

    public String getUserApiKey() {
        return userApiKey;
    }

    public void setUserApiKey(String userApiKey) {
        this.userApiKey = userApiKey;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
