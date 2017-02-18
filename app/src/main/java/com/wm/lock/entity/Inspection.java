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

    /** 任务类型 */
    @DatabaseField
    private int plan_type;

    /** 派发人 */
    @DatabaseField
    private String dispatch_man;

    /** 处理人 */
    @DatabaseField
    private String deal_man;

    /** 工作人员 */
    @DatabaseField
    private String job_man;

    /** 要求完成时间 */
    @DatabaseField
    private Date plan_date;

    /** 开始时间 */
    @DatabaseField
    private Date start_date;

    /** 完成时间 */
    @DatabaseField
    private Date end_date;

    /** 最后修改时间 */
    @DatabaseField
    private Date last_modify_date;

    /** 所属机房 */
    @DatabaseField
    private String room_name;

    /** 机房门锁mac地址 */
    @DatabaseField
    private String lock_mac;

    /** 状态 */
    @DatabaseField
    private int state;

    /** 工作内容 */
    @DatabaseField
    private String content;

    /** 备注 */
    @DatabaseField
    private String note;

    /** 执行人员的工号 */
    @DatabaseField
    private String user_job_number;

    /** 巡检项列表 */
    private List<InspectionItem> items;

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

    public int getPlan_type() {
        return plan_type;
    }

    public void setPlan_type(int plan_type) {
        this.plan_type = plan_type;
    }

    public String getDispatch_man() {
        return dispatch_man;
    }

    public void setDispatch_man(String dispatch_man) {
        this.dispatch_man = dispatch_man;
    }

    public String getDeal_man() {
        return deal_man;
    }

    public void setDeal_man(String deal_man) {
        this.deal_man = deal_man;
    }

    public String getJob_man() {
        return job_man;
    }

    public void setJob_man(String job_man) {
        this.job_man = job_man;
    }

    public Date getPlan_date() {
        return plan_date;
    }

    public void setPlan_date(Date plan_date) {
        this.plan_date = plan_date;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Date getLast_modify_date() {
        return last_modify_date;
    }

    public void setLast_modify_date(Date last_modify_date) {
        this.last_modify_date = last_modify_date;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUser_job_number() {
        return user_job_number;
    }

    public void setUser_job_number(String user_job_number) {
        this.user_job_number = user_job_number;
    }

    public List<InspectionItem> getItems() {
        return items;
    }

    public void setItems(List<InspectionItem> items) {
        this.items = items;
    }

}
