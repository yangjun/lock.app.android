package com.wm.lock.entity;

import java.util.List;

/**
 * 任务模板-单选
 */
public class TaskTemplateChoiceSingle extends TaskTemplate {

    /** 选项列表 */
    private List<TaskTemplateChoiceOption> optionList;

    public List<TaskTemplateChoiceOption> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<TaskTemplateChoiceOption> optionList) {
        this.optionList = optionList;
    }

}
