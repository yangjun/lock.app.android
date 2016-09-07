package com.wm.lock.websocket;

import android.content.Context;

import com.wm.lock.LockApplication;
import com.wm.lock.R;
import com.wm.lock.core.utils.GsonUtils;
import com.wm.lock.core.utils.NotificationUtils;
import com.wm.lock.dto.InspectionNewDto;
import com.wm.lock.dto.InspectionResultDto;
import com.wm.lock.entity.Chat;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.InspectionResult;
import com.wm.lock.entity.InspectionState;
import com.wm.lock.ui.activities.HomeActivity_;

import de.greenrobot.event.EventBus;

class WebSocketReaderDATA extends WebSocketReaderBase {

    private Chat mChat;
    private String payload;

    @Override
    void execute(Chat chat) {
        mChat = chat;
        payload = chat.getData().getPayload();
        if (contains(payload, "PLAN")) {
            getInspection();
        }
        else if (contains(payload, "RESULT_RETURN")) {
            getInspectionSubmitResult();
        }
    }

    /**
     * 获取到推送的巡检计划
     */
    private void getInspection() {
        WebSocketReaderProcessor.getInstance().execute(new WebSocketReaderProcessor.WebSocketReaderWork<Long>() {
            @Override
            public Long execute() throws Exception {
                final Inspection inspection = GsonUtils.fromJson(payload, Inspection.class);
                return bizService().addInspection(inspection);
            }

            @Override
            public void onSuccess(Long result) {
                if (result > 0) {
                    InspectionNewDto dto = new InspectionNewDto();
                    dto.setCount(result);
                    EventBus.getDefault().post(dto);

                    // 通知栏提醒
                    final Context ctx = LockApplication.getInstance();
                    String message = ctx.getResources().getString(R.string.message_new_inspection);
                    message = String.format(message, result);
                    NotificationUtils.showNotification(LockApplication.getInstance(), 0, message, HomeActivity_.class);
                }
                ask();
            }
        });
    }

    /**
     * 获取到巡检计划的提交结果
     */
    private void getInspectionSubmitResult() {
        WebSocketReaderProcessor.getInstance().execute(new WebSocketReaderProcessor.WebSocketReaderWork<InspectionResult>() {

            @Override
            public InspectionResult execute() throws Exception {
                final InspectionResult result = GsonUtils.fromJson(payload, InspectionResult.class);
                if (result.getState() == InspectionState.COMPLETE) {
                    bizService().deleteCommunication(getLoginUser().getJobNumber(), mChat.getData().getId());
                }
                return result;
            }

            @Override
            public void onSuccess(InspectionResult result) {
                final InspectionResultDto dto = new InspectionResultDto();
                dto.setSuccess(result.getState() == InspectionState.COMPLETE);
                EventBus.getDefault().post(dto);
                ask();
            }
        });
    }

    /**
     * 回复
     */
    private void ask() {
        WebSocketWriter.ask(mChat.getData().getId());
    }

}
