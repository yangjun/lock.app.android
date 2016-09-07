package com.wm.lock.module.biz;

import com.wm.lock.entity.AttachmentType;
import com.wm.lock.entity.Communication;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.InspectionItem;
import com.wm.lock.entity.params.InspectionQueryParam;

import java.util.List;

public interface IBizService {

    /**
     * 获取巡检计划列表
     */
    public List<Inspection> listInspection(InspectionQueryParam param);

    /**
     * 接收巡检计划
     */
    public void receiveInspection(Inspection inspection);

    /**
     * 拒绝巡检计划
     */
    public void refuseInspection(Inspection inspection, String reason);

    /**
     * 提交巡检计划
     */
    public void submitInspection(long inspectionId);

    /**
     * 新增巡检计划
     */
    public long addInspection(Inspection inspection);

    /**
     * 查询巡检计划
     */
    public Inspection findInspection(long inspectionId);

    /**
     * 删除巡检计划
     */
    public void deleteInspection(long inspectionId);

    /**
     * 删除巡检计划
     */
    public void deleteInspection(String userJobNumber, String planId);

    /**
     * 获取巡检计划数量
     */
    public long countInspection(InspectionQueryParam param);

    /**
     * 获取某个巡检计划的分类列表
     */
    public List<String> listInspectionCategory(long inspectionId);

    /**
     * 根据分类获取巡检项列表
     */
    public List<InspectionItem> listInspectionItemByCategory(long inspectionId, String categoryId);

    /**
     * 根据巡检计划id获取巡检项列表
     */
    public List<InspectionItem> listInspectionItem(long inspectionId);

    /**
     * 修改巡检计划内容
     */
    public void updateInspectionItem(InspectionItem item);

    /**
     * 获取附件列表
     */
    public List<String> listAttachments(long foreignId, AttachmentType type);

    /**
     * 删除某个路径的附件
     */
    public void deleteAttachment(String path);

    /**
     * 获取要保存的附件的路径
     */
    public String getAttachmentSavePath(long foreignId, AttachmentType type);

    /**
     * 查找下一个写入的通信记录
     */
    public Communication findNextWriteCommunication(String userJobNumber, long currCommunicationId);

    /**
     * 新增通信记录
     */
    public void addCommunication(Communication communication);

    /**
     * 删除通信记录
     */
    public void deleteCommunication(long communicationId);

    /**
     * 删除通信记录
     */
    public void deleteCommunication(String userJobNumber, String communicationBizId);

    /**
     * 获取通信记录
     */
    public Communication findCommunication(String userJobNumber, String communicationBizId);

}
