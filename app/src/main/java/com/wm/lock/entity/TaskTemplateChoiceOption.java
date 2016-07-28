package com.wm.lock.entity;

/**
 * 任务模板-选项
 */
public class TaskTemplateChoiceOption {

    /** 名称 */
    private String name;

    /** 值 */
    private String value;

    /** 是否选中 */
    private boolean isSelect;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

}

