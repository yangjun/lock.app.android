package com.wm.lock.websocket;

import com.wm.lock.LockConstants;
import com.wm.lock.helper.NotificationHelper;
import com.wm.lock.dto.InspectionNewDto;
import com.wm.lock.entity.Chat;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.LockDeviceGroup;
import com.wm.lock.entity.TemperatureHumidity;

import de.greenrobot.event.EventBus;

class WebSocketReaderDATA extends WebSocketReaderBase {

    private Chat mChat;
    private String payload;

    @Override
    void execute(Chat chat) {
        mChat = chat;
        payload = chat.getData().getPayload();
        if (contains(payload, LockConstants.BIZ_PLAN)) {
            getInspection();
        }
//        else if (contains(payload, LockConstants.BIZ_RESULT_RETURN)) {
//            getInspectionSubmitResult();
//        }
        else if (contains(payload, LockConstants.BIZ_LOCKS_AUTH)) {
            getDeviceLocks();
        }
        else if (contains(payload, LockConstants.BIZ_HUMITURE)) {
            getTemperatureHumidity();
        }
        else {
            ask();
        }
    }

    /**
     * 获取到推送的巡检计划
     */
    private void getInspection() {
        WebSocketReaderProcessor.getInstance().execute(new WebSocketReaderProcessor.WebSocketReaderWork<Long>() {
            @Override
            public Long execute() throws Exception {
                final Inspection inspection = convertFormJson(payload, Inspection.class);
                inspection.setUser_job_number(loginUser().getJobNumber());
                return bizService().addInspection(inspection);
            }

            @Override
            public void onSuccess(Long result) {
                if (result > 0) {
                    // 通知栏提醒
                    NotificationHelper.showNewInspection(result);

                    InspectionNewDto dto = new InspectionNewDto();
                    dto.setCount(result);
                    EventBus.getDefault().post(dto);
                }
                ask();
            }
        });
    }

//    /**
//     * 获取到巡检计划的提交结果
//     */
//    private void getInspectionSubmitResult() {
//        WebSocketReaderProcessor.getInstance().execute(new WebSocketReaderProcessor.WebSocketReaderWork<InspectionResult>() {
//
//            @Override
//            public InspectionResult execute() throws Exception {
//                final InspectionResult result = GsonUtils.fromJson(payload, InspectionResult.class);
//                if (result.getState() == InspectionState.COMPLETE) { //提交成功
////                    final CommunicationDeleteParam deleteParam = new CommunicationDeleteParam();
////                    deleteParam.setSource(loginUser().getJobNumber());
////                    deleteParam.setContents(new String[]{
////                            Helper.getDbJson(LockConstants.BIZ_FLAG, LockConstants.BIZ_RESULT),
////                            Helper.getDbJson("plan_id", result.getPlan_id())
////                    });
////                    bizService().deleteCommunication(deleteParam);
//                    bizService().submitInspectionSuccess(loginUser().getJobNumber(), result.getPlan_id());
//                }
//                else if (result.getState() == InspectionState.IN_PROCESS) { //提交失败
//                    final Inspection inspection = bizService().findInspection(loginUser().getJobNumber(), result.getPlan_id());
//                    if (inspection != null) {
//                        WebSocketWriter.submitInspection(inspection.getId_(), true);
//                    }
//                }
//                return result;
//            }
//
//            @Override
//            public void onSuccess(InspectionResult result) {
//                final InspectionResultDto dto = new InspectionResultDto();
//                dto.setPlan_id(result.getPlan_id());
//                dto.setSuccess(result.getState() == InspectionState.COMPLETE);
//                EventBus.getDefault().post(dto);
//
//                ask();
//            }
//        });
//    }

    /**
     * 获取到推送的开锁列表
     */
    private void getDeviceLocks() {
        WebSocketReaderProcessor.getInstance().execute(new WebSocketReaderProcessor.WebSocketReaderWork<Void>() {
            @Override
            public Void execute() throws Exception {
                final LockDeviceGroup lockDeviceGroup = convertFormJson(payload, LockDeviceGroup.class);
                bizService().syncLockDevice(loginUser().getJobNumber(), lockDeviceGroup.getLocks());
                return null;
            }

            @Override
            public void onSuccess(Void result) {
                ask();
            }
        });
    }

    /**
     * 获取到推送的温湿度变化
     */
    private void getTemperatureHumidity() {
        WebSocketReaderProcessor.getInstance().execute(new WebSocketReaderProcessor.WebSocketReaderWork<Void>() {
            @Override
            public Void execute() throws Exception {
                final TemperatureHumidity temperatureHumidity = convertFormJson(payload, TemperatureHumidity.class);
                bizService().syncTemperatureHumidity(temperatureHumidity);
                return null;
            }

            @Override
            public void onSuccess(Void result) {
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
