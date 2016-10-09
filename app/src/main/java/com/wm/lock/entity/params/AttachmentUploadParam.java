package com.wm.lock.entity.params;

/**
 * 附件上传参数
 */
public class AttachmentUploadParam {

    /** 别名 */
    private String aliases;

    /** 附件路径 */
    private String file;

    public String getAliases() {
        return aliases;
    }

    public void setAliases(String aliases) {
        this.aliases = aliases;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
