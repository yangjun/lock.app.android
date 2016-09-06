package com.wm.lock.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
import java.util.List;

/**
 * 巡检计划
 */
@DatabaseTable(tableName = "tb_inspection")
public class Inspection {

    /** 唯一id */
    @DatabaseField(generatedId = true)
    private long id_;

    /** id */
    @DatabaseField
    private String plan_id;

    /** 名称 */
    @DatabaseField
    private String plan_name;

    /** 派发人 */
    @DatabaseField
    private String send_man;

    /** 创建时间 */
    @DatabaseField
    private Date create_date;

    /** 最后修改时间 */
    @DatabaseField
    private Date last_modify_date;

    /** 要求完成时间 */
    @DatabaseField
    private Date plan_date;

    /** 实际完成时间 */
    @DatabaseField
    private Date finish_date;

    /** 所属机房 */
    @DatabaseField
    private String room_name;

    /** 机房门锁mac地址 */
    @DatabaseField
    private String lock_mac;

    /** 执行人员的工号 */
    @DatabaseField
    private String user_job_number;

    /** 状态 */
    @DatabaseField
    private int state;

    /** 巡检项列表 */
    private List<InspectionItem> inspection_item_list;

    public long getId_() {
        return id_;
    }

    public void setId_(long id_) {
        this.id_ = id_;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public String getSend_man() {
        return send_man;
    }

    public void setSend_man(String send_man) {
        this.send_man = send_man;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public Date getLast_modify_date() {
        return last_modify_date;
    }

    public void setLast_modify_date(Date last_modify_date) {
        this.last_modify_date = last_modify_date;
    }

    public Date getPlan_date() {
        return plan_date;
    }

    public void setPlan_date(Date plan_date) {
        this.plan_date = plan_date;
    }

    public Date getFinish_date() {
        return finish_date;
    }

    public void setFinish_date(Date finish_date) {
        this.finish_date = finish_date;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getLock_mac() {
        return lock_mac;
    }

    public void setLock_mac(String lock_mac) {
        this.lock_mac = lock_mac;
    }

    public String getUser_job_number() {
        return user_job_number;
    }

    public void setUser_job_number(String user_job_number) {
        this.user_job_number = user_job_number;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<InspectionItem> getInspection_item_list() {
        return inspection_item_list;
    }

    public void setInspection_item_list(List<InspectionItem> inspection_item_list) {
        this.inspection_item_list = inspection_item_list;
    }

}
