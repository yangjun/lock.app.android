package com.wm.lock.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.w3c.dom.Text;

import java.util.Date;

/**
 * 巡检项
 */
@DatabaseTable(tableName = "tb_inspection_item")
public class InspectionItem {

    /** 唯一id */
    @DatabaseField(generatedId = true)
    private long id_;

    /** id */
    @DatabaseField
    private long item_id;

    /** 巡检事项 */
    @DatabaseField
    private String item_name;

    /** 类别名称 */
    @DatabaseField
    private String item_cate_name;

    /** 设备名称 */
    @DatabaseField
    private String equipment_name;

    /** 机柜名称 */
    @DatabaseField
    private String cabinet_name;

    /** 机柜锁mac地址 */
    @DatabaseField
    private String cabinet_lock_mac;

    /** 创建时间 */
    @DatabaseField
    private Date create_date;

    /** 最后修改时间 */
    @DatabaseField
    private Date last_modify_date;

    /** 关联的巡检计划 */
    @DatabaseField(canBeNull = true, foreign = true, columnName = "inspection_id")
    private Inspection inspection;

    /** 是否正常 */
    @DatabaseField(canBeNull = false, defaultValue = "true")
    private Boolean state;

    /** 运行情况 */
    @DatabaseField
    private String result_name;

    /** 备注 */
    @DatabaseField
    private String note;

    public long getId_() {
        return id_;
    }

    public void setId_(long id_) {
        this.id_ = id_;
    }

    public long getItem_id() {
        return item_id;
    }

    public void setItem_id(long item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_cate_name() {
        return item_cate_name;
    }

    public void setItem_cate_name(String item_cate_name) {
        this.item_cate_name = item_cate_name;
    }

    public String getEquipment_name() {
        return equipment_name;
    }

    public void setEquipment_name(String equipment_name) {
        this.equipment_name = equipment_name;
    }

    public String getCabinet_name() {
        return cabinet_name;
    }

    public void setCabinet_name(String cabinet_name) {
        this.cabinet_name = cabinet_name;
    }

    public String getCabinet_lock_mac() {
        return cabinet_lock_mac;
    }

    public void setCabinet_lock_mac(String cabinet_lock_mac) {
        this.cabinet_lock_mac = cabinet_lock_mac;
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

    public Inspection getInspection() {
        return inspection;
    }

    public void setInspection(Inspection inspection) {
        this.inspection = inspection;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getResult_name() {
        return result_name;
    }

    public void setResult_name(String result_name) {
        this.result_name = result_name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
