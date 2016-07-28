package com.wm.lock.entity;

import java.util.List;

/**
 * 任务模板-多选
 */
public class TaskTemplateChoiceMulti extends TaskTemplate {

    private List<TaskTemplateChoiceOption> optionList;

    public List<TaskTemplateChoiceOption> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<TaskTemplateChoiceOption> optionList) {
        this.optionList = optionList;
    }

}
