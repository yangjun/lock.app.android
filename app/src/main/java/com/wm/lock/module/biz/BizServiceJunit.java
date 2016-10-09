package com.wm.lock.module.biz;

import com.wm.lock.entity.AttachmentUpload;
import com.wm.lock.entity.AttachmentUploadResult;
import com.wm.lock.entity.AttachmentUploadSource;
import com.wm.lock.entity.params.AttachmentUploadParam;

import org.androidannotations.annotations.EBean;

import java.util.List;
import java.util.UUID;

/**
 * Created by WM on 2015/8/6.
 */
@EBean
public class BizServiceJunit extends BizServiceBase {

    @Override
    public void uploadAttachments(List<AttachmentUpload> list) {
        waitting();
        for (AttachmentUpload item : list) {
            item.setUploadedId(UUID.randomUUID().toString());
            mDaoManager.getAttachmentUploadDao().update(item);
        }
    }

}
