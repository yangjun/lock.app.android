package com.wm.lock.entity.params;

public class CommunicationDeleteParam {

    /** 发起方 */
    private String source;

    /** 类型 */
    private String directive;

    /** 业务id */
    private String chatId;

    /** 包含的内容 */
    private String[] contents;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getDirective() {
        return directive;
    }

    public void setDirective(String directive) {
        this.directive = directive;
    }

    public String[] getContents() {
        return contents;
    }

    public void setContents(String[] contents) {
        this.contents = contents;
    }
}
