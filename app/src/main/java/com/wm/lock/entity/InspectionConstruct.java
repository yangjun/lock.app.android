package com.wm.lock.entity;

import java.util.List;

/**
 * 巡检项的施工单
 */
public class InspectionConstruct {

    /** 唯一标识 */
    private String id;

    /** 创建时间 */
    private long createDateTime;

    /** 最后更新时间 */
    private long lastModifyDateTime;

    /** 指南 */
    private String guide;

    /** 所属的巡检项 */
    private InspectionItem inspectionItem;

    /** 包含的具体施工项 */
    private List<InspectionConstructItem> items;

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

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    public InspectionItem getInspectionItem() {
        return inspectionItem;
    }

    public void setInspectionItem(InspectionItem inspectionItem) {
        this.inspectionItem = inspectionItem;
    }

    public List<InspectionConstructItem> getItems() {
        return items;
    }

    public void setItems(List<InspectionConstructItem> items) {
        this.items = items;
    }

}
