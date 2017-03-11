package com.wm.lock.module.biz;

import android.content.Context;
import android.text.TextUtils;

import com.wm.lock.core.utils.CollectionUtils;
import com.wm.lock.core.utils.IoUtils;
import com.wm.lock.dao.DaoManager;
import com.wm.lock.entity.AttachmentSource;
import com.wm.lock.entity.AttachmentType;
import com.wm.lock.entity.AttachmentUpload;
import com.wm.lock.entity.AttachmentUploadSource;
import com.wm.lock.entity.Communication;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.InspectionItem;
import com.wm.lock.entity.InspectionState;
import com.wm.lock.entity.LockDevice;
import com.wm.lock.entity.TemperatureHumidity;
import com.wm.lock.entity.params.CommunicationDeleteParam;
import com.wm.lock.entity.params.InspectionQueryParam;
import com.wm.lock.entity.params.TemperatureHumidityQueryParam;
import com.wm.lock.module.BaseModule;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import static android.R.attr.path;
import static android.R.id.list;

@EBean
public abstract class BizServiceBase extends BaseModule implements IBizService {

    @RootContext
    Context mCtx;

    @Bean
    DaoManager mDaoManager;

    @Override
    public List<Inspection> listInspection(InspectionQueryParam param) {
        return mDaoManager.getInspectionDao().list(param);
    }

    @Override
    public void receiveInspection(Inspection inspection) {
        inspection.setState(InspectionState.IN_PROCESS);
        inspection.setLast_modify_date(new Date());
        mDaoManager.getInspectionDao().update(inspection);
    }

    @Override
    public void refuseInspection(Inspection inspection, String reason) {
        inspection.setState(InspectionState.REFUSE);
        inspection.setLast_modify_date(new Date());
        mDaoManager.getInspectionDao().update(inspection);
    }

    @Override
    public void submitInspection(final long inspectionId) {
        // 变更巡检计划的状态
        final Inspection inspection = mDaoManager.getInspectionDao().queryForId(inspectionId);
        inspection.setState(InspectionState.SUBMIT_FAIL);
        inspection.setLast_modify_date(new Date());
        mDaoManager.getInspectionDao().update(inspection);

        // 添加附件
        final List<InspectionItem> inspectionItemList = listInspectionItem(inspectionId);
        mDaoManager.getAttachmentUploadDao().doInTransaction(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                // 添加巡检任务的附件
                addAttachmentUploads(inspectionId, AttachmentSource.INSPECTION, inspection);

                // 添加巡检项任务附件
                for (InspectionItem item : inspectionItemList) {
                    addAttachmentUploads(item.getId_(), AttachmentSource.INSPECTION_ITEM, inspection);
                }
                return null;
            }
        });
    }

    @Override
    public void submitInspectionSuccess(String userJobNumber, String planId) {
        final Inspection inspection = mDaoManager.getInspectionDao().findByPlanId(userJobNumber, planId);
        // 删除附件上传记录
        mDaoManager.getAttachmentUploadDao().delete(userJobNumber, AttachmentUploadSource.INSPECTION, inspection.getId_());
        // 删除巡检计划
        deleteInspection(inspection.getId_());
    }

    @Override
    public long addInspection(final Inspection inspection) {
        inspection.setState(InspectionState.PENDING);
        if (inspection.getStart_date() == null) {
            inspection.setStart_date(new Date());
        }
        inspection.setLast_modify_date(new Date());
        final int count = mDaoManager.getInspectionDao().add(inspection);
        if (count <= 0) {
            return 0;
        }

        mDaoManager.getInspectionItemDao().doInTransaction(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                final List<InspectionItem> inspectionItemList = inspection.getItems();
                for (InspectionItem item : inspectionItemList) {
                    item.setStart_date(new Date());
                    item.setLast_modify_date(new Date());
                    item.setInspection(inspection);
                    mDaoManager.getInspectionItemDao().create(item);
                }
                return null;
            }
        });
        return 1;
    }

    @Override
    public void deleteInspection(final long inspectionId) {
        mDaoManager.getInspectionDao().doInTransaction(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                final List<InspectionItem> inspectionItems = mDaoManager.getInspectionItemDao().list(inspectionId);
                if (!CollectionUtils.isEmpty(inspectionItems)) {
                    for (InspectionItem inspectionItem : inspectionItems) {
                        final File file = getAttachmentFolder(AttachmentSource.INSPECTION_ITEM, inspectionItem.getId_());
                        IoUtils.deleteFiles(file.getAbsolutePath());
                        mDaoManager.getInspectionItemDao().deleteById(inspectionItem.getId_());
                    }
                }
                mDaoManager.getInspectionDao().deleteById(inspectionId);
                return null;
            }
        });
    }

//    @Override
//    public void deleteInspection(String userJobNumber, String planId) {
//        final Inspection inspection = mDaoManager.getInspectionDao().findByPlanId(userJobNumber, planId);
//        if (inspection != null) {
//            deleteInspection(inspection.getId_());
//        }
//    }

    @Override
    public long countInspection(InspectionQueryParam param) {
        return mDaoManager.getInspectionDao().count(param);
    }

    @Override
    public Inspection findInspection(long inspectionId) {
        return mDaoManager.getInspectionDao().queryForId(inspectionId);
    }

    @Override
    public Inspection findInspection(String userJobNumber, String planId) {
        return mDaoManager.getInspectionDao().findByPlanId(userJobNumber, planId);
    }

    @Override
    public List<String> listInspectionCategory(long inspectionId) {
        return mDaoManager.getInspectionItemDao().listCategory(inspectionId);
    }

    @Override
    public List<InspectionItem> listInspectionItemByCategory(long inspectionId, String categoryName) {
        return mDaoManager.getInspectionItemDao().listByCategory(inspectionId, categoryName);
    }

    @Override
    public List<InspectionItem> listInspectionItem(long inspectionId) {
        return mDaoManager.getInspectionItemDao().list(inspectionId);
    }

    @Override
    public void updateInspectionItem(InspectionItem item) {
        mDaoManager.getInspectionItemDao().update(item);
    }

    @Override
    public List<LockDevice> listInspectionBluetooth(InspectionQueryParam param) {
        return mDaoManager.getInspectionDao().listBluetooth(param);
    }

    @Override
    public List<LockDevice> listInspectionItemCategoryBluetooth(String userJobNumber, long inspectionId, String category) {
        List<LockDevice> result = null;
        if (TextUtils.isEmpty(category)) {
            result = mDaoManager.getInspectionItemDao().listBluetooth(inspectionId);
        }
        else {
            result = mDaoManager.getInspectionItemDao().listBluetoothByCategory(inspectionId, category);
        }
        result = fixLockDeviceList(userJobNumber, result);
        return result;
    }

    @Override
    public List<AttachmentUpload> findNextAttachmentUploadGroup(String userJobNumber) {
        return mDaoManager.getAttachmentUploadDao().findNextGroup(userJobNumber);
    }

    @Override
    public int countAttachments(long foreignId, AttachmentSource source, AttachmentType type) {
        final File folder = getAttachmentFolder(source, foreignId);
        final File[] files = folder.listFiles();
        return files == null ? 0 : files.length;
    }

    @Override
    public List<String> listAttachments(long foreignId, AttachmentSource source, AttachmentType type) {
        final File folder = getAttachmentFolder(source, foreignId);
        final File[] files = folder.listFiles();

        if (files == null && files.length == 0) {
            return null;
        }

        final List<String> result = new ArrayList<>();
        for (File item : files) {
            final String path = item.getAbsolutePath();
            if (path.endsWith(getAttachmentSuffix(type))) {
                result.add(item.getAbsolutePath());
            }
        }
        return result;
    }

    @Override
    public String getAttachmentSavePath(long foreignId, AttachmentSource source, AttachmentType type) {
        final File folder = getAttachmentFolder(source, foreignId);
        final String name = new Date().getTime() + getAttachmentSuffix(type);
        return new File(folder, name).getAbsolutePath();
    }

    @Override
    public void deleteAttachment(String path) {
        IoUtils.deleteFile(path);
    }

    @Override
    public void deleteAttachmentUpload(String userJobNumber, long foreignId, AttachmentUploadSource source) {
        mDaoManager.getAttachmentUploadDao().delete(userJobNumber, source, foreignId);
    }

    @Override
    public AttachmentUpload findAttachmentUploadByPath(String path) {
        return mDaoManager.getAttachmentUploadDao().findByPath(path);
    }

    @Override
    public Communication findNextWriteCommunication(String userJobNumber) {
        return mDaoManager.getCommunicationDao().findNextWrite(userJobNumber);
    }

    @Override
    public void addCommunication(Communication communication) {
        mDaoManager.getCommunicationDao().add(communication);
    }

    @Override
    public void deleteCommunication(long communicationId) {
        mDaoManager.getCommunicationDao().deleteById(communicationId);
    }

    @Override
    public void deleteCommunication(CommunicationDeleteParam param) {
        mDaoManager.getCommunicationDao().delete(param);
    }

    @Override
    public Communication findCommunication(String userJobNumber, String communicationBizId) {
        return mDaoManager.getCommunicationDao().find(userJobNumber, communicationBizId);
    }

    @Override
    public void syncLockDevice(String userJobNumber, List<LockDevice> list) {
        mDaoManager.getLockDeviceDao().sync(userJobNumber, list);
    }

    @Override
    public List<LockDevice> listLockDevice(String userJobNumber) {
        return mDaoManager.getLockDeviceDao().list(userJobNumber);
    }

    @Override
    public void syncTemperatureHumidity(TemperatureHumidity input) {
        mDaoManager.getTemperatureHumidityDao().sync(input);
    }

    @Override
    public TemperatureHumidity findTemperatureHumidityByRoomName(String roomName, String userJobNumber) {
        return mDaoManager.getTemperatureHumidityDao().findByRoomName(roomName, userJobNumber);
    }

    @Override
    public List<TemperatureHumidity> listTemperatureHumidity(TemperatureHumidityQueryParam param) {
        return mDaoManager.getTemperatureHumidityDao().list(param);
    }

    @Override
    public <B> B inTransaction(Callable<B> callable) {
        return mDaoManager.getInspectionDao().doInTransaction(callable);
    }

    protected File getAttachmentFolder(AttachmentSource source, long foreignId) {
        final StringBuilder builder = new StringBuilder();
        builder.append(mCtx.getExternalCacheDir().getAbsolutePath())
                .append(File.separator)
                .append("attachments")
                .append(File.separator)
                .append(source.name())
                .append(File.separator)
                .append(foreignId)
                .append(File.separator);
        final File folder = new File(builder.toString());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }

    protected String getAttachmentSuffix(AttachmentType type) {
        switch (type) {
            case PHOTO:
                return ".jpg";

            default:
                return ".unknown";
        }
    }

    private List<LockDevice> fixLockDeviceList(String userJobNumber, List<LockDevice> list) {
        if (!CollectionUtils.isEmpty(list)) {
            for (LockDevice item : list) {
                final LockDevice queryItem = mDaoManager.getLockDeviceDao().findByMac(userJobNumber, item.getLock_mac());
                if (queryItem != null) {
                    item.setLock_name(queryItem.getLock_name());
                }
            }
        }
        return list;
    }

    private void addAttachmentUploads(long foreignId, AttachmentSource source, Inspection inspection) {
        final List<String> picList = listAttachments(foreignId, source, AttachmentType.PHOTO);
        if (!CollectionUtils.isEmpty(picList)) {
            for (String path : picList) {
                addAttachmentUpload(path, AttachmentType.PHOTO, inspection);
            }
        }
    }

    private void addAttachmentUpload(String path, AttachmentType type, Inspection inspection) {
        final AttachmentUpload temp = new AttachmentUpload();
        temp.setCreate_date(new Date());
        temp.setForeignId(inspection.getId_());
        temp.setType(type);
        temp.setPath(path);
        temp.setUser_job_number(inspection.getUser_job_number());
        temp.setSource(AttachmentUploadSource.INSPECTION);
        temp.setUploadedId(null);
        mDaoManager.getAttachmentUploadDao().create(temp);
    }

}
