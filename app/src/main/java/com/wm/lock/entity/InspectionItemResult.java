package com.wm.lock.entity;

/**
 * 巡检计划项的提交结果
 */
public class InspectionItemResult {

    /** 巡检项id */
    private String item_id;

    /** 巡检状态 */
    private int state;

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
