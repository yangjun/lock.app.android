package com.wm.lock.entity;

/**
 * 巡检信息
 */
public class Inspection {

    /** 唯一标识 */
    private String id;

    /** 创建时间 */
    private long createDateTime;

    /** 最后更新时间 */
    private long lastModifyDateTime;

    /** 关联的用户 */
    private UserInfo user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(long createDateTime) {
        this.createDateTime = createDateTime;
    }

    public long getLastModifyDateTime() {
        return lastModifyDateTime;
    }

    public void setLastModifyDateTime(long lastModifyDateTime) {
        this.lastModifyDateTime = lastModifyDateTime;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

}
