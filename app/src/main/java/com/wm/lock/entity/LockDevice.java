package com.wm.lock.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 开锁的设备信息
 */
@DatabaseTable(tableName = "tb_lock_device")
public class LockDevice {

    /** 唯一id */
    @DatabaseField(generatedId = true)
    private long id_;

    /** mac地址 */
    @DatabaseField
    private String lock_mac;

    /** 名称 */
    @DatabaseField
    private String lock_name;

    /** 执行人员的工号 */
    @DatabaseField
    private String user_job_number;

    /** 首字符 */
    @DatabaseField
    private String first_letter;

    public long getId_() {
        return id_;
    }

    public void setId_(long id_) {
        this.id_ = id_;
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

    public String getUser_job_number() {
        return user_job_number;
    }

    public void setUser_job_number(String user_job_number) {
        this.user_job_number = user_job_number;
    }

    public String getFirst_letter() {
        return first_letter;
    }

    public void setFirst_letter(String first_letter) {
        this.first_letter = first_letter;
    }
}
