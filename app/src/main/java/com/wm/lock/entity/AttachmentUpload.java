package com.wm.lock.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * 附件上传记录
 */
@DatabaseTable(tableName = "tb_attachment_upload")
public class AttachmentUpload {

    /** 唯一id */
    @DatabaseField(generatedId = true)
    private long id_;

    /** 创建时间 */
    @DatabaseField
    private Date create_date;

    /** 附件路径 */
    @DatabaseField
    private String path;

    /** 附件类型 */
    @DatabaseField
    private AttachmentType type;

    /** 上传完成返回的id */
    @DatabaseField
    private String uploadedId;

    /** 关联的外键id */
    @DatabaseField
    private long foreignId;

    /** 来源 */
    @DatabaseField
    private AttachmentUploadSource source;

    /** 用户工号 */
    @DatabaseField
    private String user_job_number;

    public long getId_() {
        return id_;
    }

    public void setId_(long id_) {
        this.id_ = id_;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public long getForeignId() {
        return foreignId;
    }

    public void setForeignId(long foreignId) {
        this.foreignId = foreignId;
    }

    public AttachmentUploadSource getSource() {
        return source;
    }

    public void setSource(AttachmentUploadSource source) {
        this.source = source;
    }

    public String getUser_job_number() {
        return user_job_number;
    }

    public void setUser_job_number(String user_job_number) {
        this.user_job_number = user_job_number;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public AttachmentType getType() {
        return type;
    }

    public void setType(AttachmentType type) {
        this.type = type;
    }

    public String getUploadedId() {
        return uploadedId;
    }

    public void setUploadedId(String uploadedId) {
        this.uploadedId = uploadedId;
    }

}
