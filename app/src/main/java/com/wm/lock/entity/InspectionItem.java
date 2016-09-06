package com.wm.lock.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 巡检项
 */
@DatabaseTable(tableName = "tb_inspection_item")
public class InspectionItem {

    /** 唯一id */
    @DatabaseField(id = true)
    private long id_;

    /** id */
    @DatabaseField
    private long item_id;

    /** 巡检事项 */
    @DatabaseField
    private long item_name;

    /** 类别名称 */
    @DatabaseField
    private long item_cate_name;

    /** 设备名称 */
    @DatabaseField
    private long equipment_name;

    /** 机柜名称 */
    @DatabaseField
    private long cabinet_name;

    /** 机柜锁mac地址 */
    @DatabaseField
    private long cabinet_lock_mac;

    /** 创建时间 */
    @DatabaseField
    private String create_date;

    /** 最后修改时间 */
    @DatabaseField
    private String last_modify_date;

    /** 关联的巡检计划 */
    @DatabaseField(canBeNull = true, foreign = true, columnName = "inspection_id")
    private Inspection inspection;

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

    public long getItem_name() {
        return item_name;
    }

    public void setItem_name(long item_name) {
        this.item_name = item_name;
    }

    public long getItem_cate_name() {
        return item_cate_name;
    }

    public void setItem_cate_name(long item_cate_name) {
        this.item_cate_name = item_cate_name;
    }

    public long getEquipment_name() {
        return equipment_name;
    }

    public void setEquipment_name(long equipment_name) {
        this.equipment_name = equipment_name;
    }

    public long getCabinet_name() {
        return cabinet_name;
    }

    public void setCabinet_name(long cabinet_name) {
        this.cabinet_name = cabinet_name;
    }

    public long getCabinet_lock_mac() {
        return cabinet_lock_mac;
    }

    public void setCabinet_lock_mac(long cabinet_lock_mac) {
        this.cabinet_lock_mac = cabinet_lock_mac;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getLast_modify_date() {
        return last_modify_date;
    }

    public void setLast_modify_date(String last_modify_date) {
        this.last_modify_date = last_modify_date;
    }

    public Inspection getInspection() {
        return inspection;
    }

    public void setInspection(Inspection inspection) {
        this.inspection = inspection;
    }

}
