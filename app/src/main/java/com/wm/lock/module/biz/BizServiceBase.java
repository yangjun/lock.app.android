package com.wm.lock.module.biz;

import android.content.Context;

import com.wm.lock.core.utils.CollectionUtils;
import com.wm.lock.core.utils.IoUtils;
import com.wm.lock.dao.DaoManager;
import com.wm.lock.entity.AttachmentType;
import com.wm.lock.entity.BluetoothDevice;
import com.wm.lock.entity.Communication;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.InspectionItem;
import com.wm.lock.entity.InspectionState;
import com.wm.lock.entity.params.CommunicationDeleteParam;
import com.wm.lock.entity.params.InspectionQueryParam;
import com.wm.lock.module.BaseModule;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

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
    public void submitInspection(long inspectionId) {
        final Inspection inspection = mDaoManager.getInspectionDao().queryForId(inspectionId);
        inspection.setState(InspectionState.SUBMIT_FAIL);
        inspection.setLast_modify_date(new Date());
        mDaoManager.getInspectionDao().update(inspection);
    }

    @Override
    public long addInspection(final Inspection inspection) {
        inspection.setState(InspectionState.PENDING);
        inspection.setCreate_date(new Date());
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
                    item.setCreate_date(new Date());
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
                        IoUtils.deleteFiles(getAttachmentFolder(inspectionItem.getId_()).getAbsolutePath());
                        mDaoManager.getInspectionItemDao().deleteById(inspectionItem.getId_());
                    }
                }
                mDaoManager.getInspectionDao().deleteById(inspectionId);
                return null;
            }
        });
    }

    @Override
    public void deleteInspection(String userJobNumber, String planId) {
        final Inspection inspection = mDaoManager.getInspectionDao().findByPlanId(userJobNumber, planId);
        if (inspection != null) {
            deleteInspection(inspection.getId_());
        }
    }

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
    public List<BluetoothDevice> listInspectionBluetooth(InspectionQueryParam param) {
        return mDaoManager.getInspectionDao().listBluetooth(param);
    }

    @Override
    public List<BluetoothDevice> listInspectionItemCategoryBluetooth(long inspectionId, String category) {
        return mDaoManager.getInspectionItemDao().listBluetoothByCategory(inspectionId, category);
    }

    @Override
    public int countAttachments(long foreignId, AttachmentType type) {
        final File folder = getAttachmentFolder(foreignId);
        final File[] files = folder.listFiles();
        return files == null ? 0 : files.length;
    }

    @Override
    public List<String> listAttachments(long foreignId, AttachmentType type) {
        final File folder = getAttachmentFolder(foreignId);
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
    public void deleteAttachment(String path) {
        IoUtils.deleteFile(path);
    }

    @Override
    public String getAttachmentSavePath(long foreignId, AttachmentType type) {
        final File folder = getAttachmentFolder(foreignId);
        final String name = new Date().getTime() + getAttachmentSuffix(type);
        return new File(folder, name).getAbsolutePath();
    }

    @Override
    public Communication findNextWriteCommunication(String userJobNumber, long currCommunicationId) {
        return mDaoManager.getCommunicationDao().findNextWrite(userJobNumber, currCommunicationId);
    }

    @Override
    public void addCommunication(Communication communication) {
        communication.setCreate_date(new Date());
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
    public <B> B inTransaction(Callable<B> callable) {
        return mDaoManager.getInspectionDao().doInTransaction(callable);
    }

    protected File getAttachmentFolder(long foreignId) {
        final File folder = new File(mCtx.getExternalCacheDir().getAbsolutePath() + File.separator + "attachments" + File.separator + foreignId + File.separator);
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

}
