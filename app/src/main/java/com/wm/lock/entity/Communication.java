package com.wm.lock.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * 通信数据
 */
@DatabaseTable(tableName = "tb_communication")
public class Communication {

    /** 唯一id */
    @DatabaseField(generatedId = true)
    private long id_;

    /** 会话id */
    @DatabaseField
    private String id;

    /** 来自哪端 */
    @DatabaseField
    private String source;

    /** 到哪端 */
    @DatabaseField
    private String target;

    /** 指令 */
    @DatabaseField
    private String directive;

    /** 具体内容 */
    @DatabaseField
    private String content;

    /** 创建时间 */
    @DatabaseField
    private Date create_date;

    public long getId_() {
        return id_;
    }

    public void setId_(long id_) {
        this.id_ = id_;
    }

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

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getDirective() {
        return directive;
    }

    public void setDirective(String directive) {
        this.directive = directive;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

}
