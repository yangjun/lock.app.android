package com.wm.lock.websocket;

import com.google.gson.reflect.TypeToken;
import com.wm.lock.LockConstants;
import com.wm.lock.core.cache.CacheManager;
import com.wm.lock.dto.InspectionResultDto;
import com.wm.lock.entity.Chat;
import com.wm.lock.entity.Communication;
import com.wm.lock.entity.Inspection;

import java.util.Map;

import de.greenrobot.event.EventBus;

class WebSocketReaderASK extends WebSocketReaderBase {

    @Override
    void execute(Chat chat) {
        final String chatId = chat.getData().getId();
        final String userJobNumber = loginUser().getJobNumber();
        final Communication communication = bizService().findCommunication(userJobNumber, chatId);
        if (communication != null) {
            execute(communication);
        }
    }

    private void execute(Communication communication) {
        bizService().deleteCommunication(communication.getId_());
        final String content = communication.getContent();
        if (contains(content, LockConstants.BIZ_RESULT)) {
            submitInspectionResult(content);
        }
    }

    private void submitInspectionResult(String content) {
        final Map<String, Object> map = toBizMap(content);
        final String planId = map.get("plan_id").toString();
        WebSocketReaderProcessor.getInstance().execute(new WebSocketReaderProcessor.WebSocketReaderWork<Void>() {

            @Override
            public Void execute() throws Exception {
                // 清除缓存的滑动位置
                final Inspection inspection = bizService().findInspection(loginUser().getJobNumber(), planId);
                CacheManager.getInstance().delete(LockConstants.INDEX + inspection.getId_(), CacheManager.CHANNEL_PREFERENCE);
                CacheManager.getInstance().delete(LockConstants.POS + inspection.getId_(), CacheManager.CHANNEL_PREFERENCE);
                // 清除本地数据
                bizService().submitInspectionSuccess(loginUser().getJobNumber(), planId);
                return null;
            }

            @Override
            public void onSuccess(Void result) {
                final InspectionResultDto dto = new InspectionResultDto();
                dto.setPlan_id(planId);
                dto.setSuccess(true);
                EventBus.getDefault().post(dto);
            }
        });
    }

    private Map<String, Object> toBizMap(String content) {
        final Chat chat = convertFormJson(content, Chat.class);
        final String payload = chat.getData().getPayload();
        final Map<String, Object> map = convertFormJson(payload, new TypeToken<Map<String, Object>>() {});
        return map;
    }

}
