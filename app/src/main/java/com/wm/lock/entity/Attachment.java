package com.wm.lock.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "tb_attachment")
public class Attachment {

    /** 唯一id */
    @DatabaseField(generatedId = true)
    private long id_;

    /** 类型 */
    @DatabaseField
    private int type;

    /** 创建时间 */
    @DatabaseField
    private Date create_date;

    /** 本地存放路径 */
    @DatabaseField
    private String path;

    /** 关联的外键id */
    @DatabaseField
    private long foreignId;

    /** 来源 */
    @DatabaseField
    private int source;

}
