package com.wm.lock.entity;

/**
 * 任务模板-输入
 */
public class TaskTemplateInput extends TaskTemplate {

    /** 提示文字 */
    private String hint;

    /** 最小长度 */
    private int minLength;

    /** 最大长度 */
    private int maxLength;

    /** 是否为多行输入 */
    private boolean multiLine = false;

    /** 输入类型 */
    private TaskTemplateInputType inputType = TaskTemplateInputType.ALL;

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public boolean isMultiLine() {
        return multiLine;
    }

    public void setMultiLine(boolean multiLine) {
        this.multiLine = multiLine;
    }

    public TaskTemplateInputType getInputType() {
        return inputType;
    }

    public void setInputType(TaskTemplateInputType inputType) {
        this.inputType = inputType;
    }

    public static enum TaskTemplateInputType {
        ALL, NUMBER, NUMBER_DECIMAL
    }

}
