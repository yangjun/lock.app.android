package com.wm.lock.entity;

import java.io.Serializable;

public class UserInfo implements Serializable {

    /** 工号 */
    private String jobNumber;

    /** 工号 */
    private String jobNumberCopy;

    /** 姓名 */
    private String name;

    /** 开锁密码 */
    private String lockPwd;

    /** 手势密码 */
    private String gesturePwd;

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getJobNumberCopy() {
        return jobNumberCopy;
    }

    public void setJobNumberCopy(String jobNumberCopy) {
        this.jobNumberCopy = jobNumberCopy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLockPwd() {
        return lockPwd;
    }

    public void setLockPwd(String lockPwd) {
        this.lockPwd = lockPwd;
    }

    public String getGesturePwd() {
        return gesturePwd;
    }

    public void setGesturePwd(String gesturePwd) {
        this.gesturePwd = gesturePwd;
    }
}
