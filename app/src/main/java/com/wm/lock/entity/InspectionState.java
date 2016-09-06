package com.wm.lock.entity;

/**
 * 巡检计划状态
 */
public class InspectionState {

    /** 待处理 */
    public static final int PENDING = 1;

    /** 处理中 */
    public static final int IN_PROCESS = 2;

    /** 已完成 */
    public static final int COMPLETE = 3;

    /** 拒绝 */
    public static final int REFUSE = 4;

}
