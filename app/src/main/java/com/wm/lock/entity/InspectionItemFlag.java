package com.wm.lock.entity;

/**
 * 巡检项标记
 */
public class InspectionItemFlag {

    /** 通常值 */
    public static final int NORMAL = 0;

    /** 温度 */
    public static final int TEMPERATURE = 1;

    /** 湿度 */
    public static final int HUMIDITY = 2;

    /** 温/湿度 */
    public static final int TEMPERATURE_HUMIDITY = 3;

    /** 作业结论 */
    public static final int CONCLUSION = 4;

    /** 备注 */
    public static final int REMARK = 5;

}
