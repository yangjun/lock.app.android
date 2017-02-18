package com.wm.lock.entity;

import java.util.Date;

/**
 * 开锁记录
 */
public class LockOpenRecord {

    /** 员工编号 */
    private String user_job_number;

    /** 锁mac地址 */
    private String lock_mac;

    /** 锁名称/柜锁名称 */
    private String lock_name;

    /** 开锁时间 */
    private Date lock_time;

    public String getUser_job_number() {
        return user_job_number;
    }

    public void setUser_job_number(String user_job_number) {
        this.user_job_number = user_job_number;
    }

    public String getLock_mac() {
        return lock_mac;
    }

    public void setLock_mac(String lock_mac) {
        this.lock_mac = lock_mac;
    }

    public String getLock_name() {
        return lock_name;
    }

    public void setLock_name(String lock_name) {
        this.lock_name = lock_name;
    }

    public Date getLock_time() {
        return lock_time;
    }

    public void setLock_time(Date lock_time) {
        this.lock_time = lock_time;
    }

}
