package com.wm.lock.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 机房温湿度
 */
@DatabaseTable(tableName = "tb_temperature_humidity")
public class TemperatureHumidity {

    /** 唯一id */
    @DatabaseField(generatedId = true)
    private long id_;

    /** 温度 */
    @DatabaseField
    private String temperature;

    /** 湿度 */
    @DatabaseField
    private String humidity;

    /** 机房名称 */
    @DatabaseField
    private String room_name;

    /** 用户工号 */
    @DatabaseField
    private String user_job_number;

    public long getId_() {
        return id_;
    }

    public void setId_(long id_) {
        this.id_ = id_;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getUser_job_number() {
        return user_job_number;
    }

    public void setUser_job_number(String user_job_number) {
        this.user_job_number = user_job_number;
    }

}
