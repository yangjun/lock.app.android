package com.wm.lock.entity;

import java.util.List;

/**
 * 巡检计划的提交结果
 */
public class InspectionResult {

    /** 巡检计划id */
    private String plan_id;

    /** 巡检状态 */
    private int state;

    /** 巡检项的结果 */
    private List<InspectionItemResult> items;

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<InspectionItemResult> getItems() {
        return items;
    }

    public void setItems(List<InspectionItemResult> items) {
        this.items = items;
    }

}
