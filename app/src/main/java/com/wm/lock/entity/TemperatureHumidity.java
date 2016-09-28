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

}
