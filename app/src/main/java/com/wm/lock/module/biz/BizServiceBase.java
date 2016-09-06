package com.wm.lock.module.biz;

import android.content.Context;

import com.wm.lock.core.utils.CollectionUtils;
import com.wm.lock.core.utils.IoUtils;
import com.wm.lock.dao.DaoManager;
import com.wm.lock.entity.AttachmentType;
import com.wm.lock.entity.Communication;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.InspectionItem;
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

/**
 * Created by WM on 2015/8/6.
 */
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
        // TODO 接收任务，并添加消息到发送队列
    }

    @Override
    public void refuseInspection(Inspection inspection, String reason) {
        // TODO 拒绝任务，并添加消息到发送队列
    }

    @Override
    public void submitInspection(long inspectionId) {
        // TODO 提交任务，添加到发送队列，如果已经有任务已经在排队咋办（这个时候不能及时收到回复）
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
    public long countInspection(InspectionQueryParam param) {
        return mDaoManager.getInspectionDao().count(param);
    }

    @Override
    public List<String> listInspectionCategory(long inspectionId) {
        return mDaoManager.getInspectionItemDao().listCategory(inspectionId);
    }

    @Override
    public List<InspectionItem> listInspectionItemByCategory(long inspectionId, String categoryId) {
        return mDaoManager.getInspectionItemDao().listByCategory(inspectionId, categoryId);
    }

    @Override
    public void updateInspectionItem(InspectionItem item) {
        mDaoManager.getInspectionItemDao().update(item);
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
    public void onCommunicationReceived(Communication communication) {
        // TODO 先添加到数据库，然后添加到发送队列
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
