package com.wm.lock.entity;

/**
 * 任务模板-照片
 */
public class TaskTemplatePhoto extends TaskTemplate {

    /** 最少照片数量 */
    private int minCount;

    /** 最多照片数量 */
    private int maxCount;

    public int getMinCount() {
        return minCount;
    }

    public void setMinCount(int minCount) {
        this.minCount = minCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

}
