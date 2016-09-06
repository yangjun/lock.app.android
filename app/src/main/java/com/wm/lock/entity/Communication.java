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

    /** 具体内容 */
    @DatabaseField
    private String content;

    /** 通信类型 */
    @DatabaseField
    private CommunicationType type;

    /** 来自哪端 */
    @DatabaseField
    private CommunicationTerminal sender;

    /** 到哪端 */
    @DatabaseField
    private CommunicationTerminal receiver;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CommunicationType getType() {
        return type;
    }

    public void setType(CommunicationType type) {
        this.type = type;
    }

    public CommunicationTerminal getSender() {
        return sender;
    }

    public void setSender(CommunicationTerminal sender) {
        this.sender = sender;
    }

    public CommunicationTerminal getReceiver() {
        return receiver;
    }

    public void setReceiver(CommunicationTerminal receiver) {
        this.receiver = receiver;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

}
