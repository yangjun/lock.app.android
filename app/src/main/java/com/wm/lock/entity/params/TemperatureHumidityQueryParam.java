package com.wm.lock.entity.params;

public class TemperatureHumidityQueryParam extends PageParam {

    /** 用户工号 */
    private String user_job_number;

    public String getUser_job_number() {
        return user_job_number;
    }

    public void setUser_job_number(String user_job_number) {
        this.user_job_number = user_job_number;
    }

}
