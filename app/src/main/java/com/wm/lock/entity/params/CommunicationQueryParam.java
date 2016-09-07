package com.wm.lock.entity.params;

public class CommunicationQueryParam extends PageParam {

    /** 发起方 */
    private String source;

    /** 类型 */
    private String directive;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDirective() {
        return directive;
    }

    public void setDirective(String directive) {
        this.directive = directive;
    }

}
