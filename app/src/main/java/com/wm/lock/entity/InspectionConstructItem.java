package com.wm.lock.entity;

/**
 * 巡检项的施工单项
 */
public class InspectionConstructItem {

    /** 唯一标识 */
    private String id;

    /** 创建时间 */
    private long createDateTime;

    /** 最后更新时间 */
    private long lastModifyDateTime;

    /** 所属模板信息，json字符串 */
    private String template;

    /** 模板类型 */
    private TaskTemplateType templateType;

    /** 答案 */
    private String answer;

    /** 所属的施工单 */
    private InspectionConstruct construct;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(long createDateTime) {
        this.createDateTime = createDateTime;
    }

    public long getLastModifyDateTime() {
        return lastModifyDateTime;
    }

    public void setLastModifyDateTime(long lastModifyDateTime) {
        this.lastModifyDateTime = lastModifyDateTime;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public TaskTemplateType getTemplateType() {
        return templateType;
    }

    public void setTemplateType(TaskTemplateType templateType) {
        this.templateType = templateType;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public InspectionConstruct getConstruct() {
        return construct;
    }

    public void setConstruct(InspectionConstruct construct) {
        this.construct = construct;
    }

}
