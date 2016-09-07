package com.wm.lock.entity;

/**
 * 会话内容
 */
public class Chat {

    /** 指令 */
    private String directive;

    /** 数据的接收端目标 */
    private String target;

    /** 发送给目标端的数据 */
    private ChatData data;

    public String getDirective() {
        return directive;
    }

    public void setDirective(String directive) {
        this.directive = directive;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public ChatData getData() {
        return data;
    }

    public void setData(ChatData data) {
        this.data = data;
    }

    public static class ChatData {

        /** 数据的标识，每条发送的数据必需唯一，同标识的数据多次发送，上次的数据被覆盖 */
        private String id;

        /** 数据来源，从哪里发来的数据 */
        private String source;

        /** 仅支持字符串类型，管理端和移动端协商的数据内容，建议采用json字符串传递 */
        private String payload;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getPayload() {
            return payload;
        }

        public void setPayload(String payload) {
            this.payload = payload;
        }
    }

}
