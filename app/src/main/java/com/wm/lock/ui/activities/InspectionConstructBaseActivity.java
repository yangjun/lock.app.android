package com.wm.lock.ui.activities;

import android.os.Bundle;

import com.wm.lock.LockConstants;
import com.wm.lock.R;
import com.wm.lock.attachment.AttachmentProcessor;
import com.wm.lock.core.callback.Injector;
import com.wm.lock.core.utils.CollectionUtils;
import com.wm.lock.core.utils.RedirectUtils;
import com.wm.lock.dialog.DialogManager;
import com.wm.lock.dto.InspectionResultDto;
import com.wm.lock.entity.AttachmentSource;
import com.wm.lock.entity.AttachmentType;
import com.wm.lock.entity.InspectionItem;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.biz.IBizService;
import com.wm.lock.websocket.WebSocketWriter;

import org.androidannotations.annotations.EActivity;

import java.util.List;

import de.greenrobot.event.EventBus;

@EActivity
public abstract class InspectionConstructBaseActivity extends BaseActivity {

    protected boolean mEnable;
    protected long mInspectionId;
    protected String mPlanId;
    protected String mInspectionName;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEventMainThread(InspectionResultDto dto) {
        if (mEnable) {
            return;
        }

        // 如果该任务提交成功,则回到主页
        if (dto.isSuccess() && dto.getPlan_id().equals(mPlanId)) {
            showTip(R.string.message_submit_success_in_fail_list);
            finish();
        }
    }

    @Override
    protected void init() {
        mEnable = mSaveBundle.getBoolean(LockConstants.BOOLEAN, true);
        mInspectionId = mSaveBundle.getLong(LockConstants.ID);
        mPlanId = mSaveBundle.getString(LockConstants.FLAG);
        mInspectionName = mSaveBundle.getString(LockConstants.NAME);
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        setTitle(mSaveBundle.getString(LockConstants.TITLE));
    }

    protected IBizService bizService() {
        return ModuleFactory.getInstance().getModuleInstance(IBizService.class);
    }

    protected boolean hasAttachment() {
        final List<InspectionItem> inspectionItemList = bizService().listInspectionItem(mInspectionId);
        for (InspectionItem item : inspectionItemList) {
            final List<String> attachmentList = bizService().listAttachments(item.getId_(), AttachmentSource.INSPECTION_ITEM, AttachmentType.PHOTO);
            if (!CollectionUtils.isEmpty(attachmentList)) {
                return true;
            }
        }
        return false;
    }

    protected void doSubmit() {
        DialogManager.showConfirmDialog(this, R.string.label_notify, getString(R.string.message_submit_confirm), false, new Injector() {
            @Override
            public void execute() {
                bizService().submitInspection(mInspectionId);
                // 有附件, 走附件上传处理器
                if (hasAttachment()) {
                    AttachmentProcessor.getInstance().startIfNot();
                }
                // 没有附件, 走web socket
                else {
                    WebSocketWriter.submitInspection(mInspectionId, true);
                }
                showTip(R.string.message_submit_to_background);
                setResult(RESULT_FIRST_USER);
                finish();
            }
        });
    }

    protected void unLock(String category) {
        final Bundle bundle = new Bundle();
        bundle.putLong(LockConstants.ID, mInspectionId);
        bundle.putString(LockConstants.DATA, category);
        RedirectUtils.goActivity(this, OpenCabinetActivity_.class, bundle);
    }

}
