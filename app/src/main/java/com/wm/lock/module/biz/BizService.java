package com.wm.lock.module.biz;

import com.wm.lock.entity.AttachmentUpload;
import com.wm.lock.entity.AttachmentUploadResult;
import com.wm.lock.entity.params.AttachmentUploadParam;
import com.wm.lock.http.Rest;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;

import java.util.List;

@EBean
public class BizService extends BizServiceBase {

    @Bean
    Rest mRest;

    @Override
    public void uploadAttachments(List<AttachmentUpload> list) {
        for (AttachmentUpload item : list) {
            final AttachmentUploadParam param = new AttachmentUploadParam();
            param.setFile(item.getPath());
            final AttachmentUploadResult uploadResult = mRest.uploadAttachment(param);
            item.setUploadedId(uploadResult.getId());
            mDaoManager.getAttachmentUploadDao().update(item);
        }
    }

}
