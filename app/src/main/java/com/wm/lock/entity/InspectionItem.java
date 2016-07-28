package com.wm.lock.entity;

import java.util.List;

/**
 * 巡检项
 */
public class InspectionItem {

    /** 唯一标识 */
    private String id;

    /** 名称 */
    private String name;

    /** 关键字 */
    private String key;

    /** 所属的层级 */
    private int level;

    /** 是否为子叶 */
    private boolean isLeaf;

    /** 创建时间 */
    private long createDateTime;

    /** 最后更新时间 */
    private long lastModifyDateTime;

    /** 所属的巡检 */
    private Inspection inspection;

    /** 关联的父巡检项 */
    private InspectionItem parent;

    /** 关联的子巡检项列表 */
    private List<InspectionItem> childList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
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

    public Inspection getInspection() {
        return inspection;
    }

    public void setInspection(Inspection inspection) {
        this.inspection = inspection;
    }

    public InspectionItem getParent() {
        return parent;
    }

    public void setParent(InspectionItem parent) {
        this.parent = parent;
    }

    public List<InspectionItem> getChildList() {
        return childList;
    }

    public void setChildList(List<InspectionItem> childList) {
        this.childList = childList;
    }

}
