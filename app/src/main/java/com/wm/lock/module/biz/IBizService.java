package com.wm.lock.module.biz;

import com.wm.lock.entity.AttachmentSource;
import com.wm.lock.entity.AttachmentType;
import com.wm.lock.entity.Communication;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.InspectionItem;
import com.wm.lock.entity.LockDevice;
import com.wm.lock.entity.TemperatureHumidity;
import com.wm.lock.entity.params.CommunicationDeleteParam;
import com.wm.lock.entity.params.InspectionQueryParam;

import java.util.List;
import java.util.concurrent.Callable;

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
     * 查询巡检计划
     */
    public Inspection findInspection(String userJobNumber, String planId);

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
    public List<InspectionItem> listInspectionItemByCategory(long inspectionId, String categoryName);

    /**
     * 根据巡检计划id获取巡检项列表
     */
    public List<InspectionItem> listInspectionItem(long inspectionId);

    /**
     * 修改巡检计划内容
     */
    public void updateInspectionItem(InspectionItem item);

    /**
     * 获取巡检计划的蓝牙列表
     */
    public List<LockDevice> listInspectionBluetooth(InspectionQueryParam param);

    /**
     * 根据巡检项的分类，获取对应机柜的蓝牙列表
     */
    public List<LockDevice> listInspectionItemCategoryBluetooth(long inspectionId, String category);

    /**
     * 获取附件数量
     */
    public int countAttachments(long foreignId, AttachmentSource source, AttachmentType type);

    /**
     * 获取附件列表
     */
    public List<String> listAttachments(long foreignId, AttachmentSource source, AttachmentType type);

    /**
     * 获取要保存的附件的路径
     */
    public String getAttachmentSavePath(long foreignId, AttachmentSource source, AttachmentType type);

    /**
     * 删除某个路径的附件
     */
    public void deleteAttachment(String path);

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
    public void deleteCommunication(CommunicationDeleteParam param);

    /**
     * 获取通信记录
     */
    public Communication findCommunication(String userJobNumber, String communicationBizId);

    /**
     * 同步可开锁的蓝牙列表
     */
    public void syncLockDevice(String userJobNumber, List<LockDevice> list);

    /**
     * 获取某个用户可开锁的蓝牙列表
     */
    public List<LockDevice> listLockDevice(String userJobNumber);

    /**
     * 同步温湿度
     */
    public void syncTemperatureHumidity(TemperatureHumidity input);

    /**
     * 根据机房找到对应的温湿度
     */
    public TemperatureHumidity findTemperatureHumidityByRoomName(String roomName);

    /**
     * 事务处理
     */
    public <B> B inTransaction(Callable<B> callable);

}
