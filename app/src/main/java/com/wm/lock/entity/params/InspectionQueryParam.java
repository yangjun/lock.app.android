package com.wm.lock.entity.params;

public class InspectionQueryParam extends PageParam {

    /** 状态 */
    private int state = -1;

    /** 用户工号 */
    private String user_job_number;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUser_job_number() {
        return user_job_number;
    }

    public void setUser_job_number(String user_job_number) {
        this.user_job_number = user_job_number;
    }

}
