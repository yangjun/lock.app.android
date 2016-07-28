package com.wm.lock.entity;

/**
 * 任务模板
 */
public class TaskTemplate {

    /** 名称 */
    private String name;

    /** 类型 */
    private TaskTemplateType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskTemplateType getType() {
        return type;
    }

    public void setType(TaskTemplateType type) {
        this.type = type;
    }

}
