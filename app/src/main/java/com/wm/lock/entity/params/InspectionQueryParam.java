package com.wm.lock.entity.params;

public class InspectionQueryParam extends PageParam {

    /** 状态 */
    private int state = -1;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
