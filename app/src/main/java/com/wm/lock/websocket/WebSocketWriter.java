package com.wm.lock.websocket;

import android.text.TextUtils;

import com.wm.lock.LockConstants;
import com.wm.lock.core.logger.Logger;
import com.wm.lock.core.utils.CollectionUtils;
import com.wm.lock.core.utils.GsonUtils;
import com.wm.lock.core.utils.ImageUtils;
import com.wm.lock.entity.AttachmentType;
import com.wm.lock.entity.Chat;
import com.wm.lock.entity.ChatDirective;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.InspectionItem;
import com.wm.lock.entity.InspectionState;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.biz.IBizService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WebSocketWriter {

    public void start() {
        WebSocketWriterProcessor.getInstance().startIfNot();
    }

    public void stop() {
        WebSocketWriterProcessor.getInstance().stop();
    }

    public static void ask(String id) {
        final Chat chat = toAskChat(id);
        execute(chat);
    }

    public static void receiveInspection(Inspection inspection) {
        final Map<String, Object> map = new HashMap<>();
        map.put(LockConstants.BIZ_FLAG, LockConstants.BIZ_PLAN_RETURN);
        map.put("plan_id", inspection.getPlan_id());
        map.put("state", InspectionState.IN_PROCESS);
        execute(map);
    }

    public static void refuseInspection(Inspection inspection, String reason) {
        final Map<String, Object> map = new HashMap<>();
        map.put(LockConstants.BIZ_FLAG, LockConstants.BIZ_PLAN_RETURN);
        map.put("plan_id", inspection.getPlan_id());
        map.put("state", InspectionState.REFUSE);
        map.put("reason", reason);
        execute(map);
    }

    public static void submitInspection(long inspectionId) {
        final IBizService bizService = ModuleFactory.getInstance().getModuleInstance(IBizService.class);
        final Inspection inspection = bizService.findInspection(inspectionId);
        final List<InspectionItem> inspectionItemList = bizService.listInspectionItem(inspectionId);

        final Map<String, Object> map = new HashMap<>();
        map.put(LockConstants.BIZ_FLAG, LockConstants.BIZ_RESULT);
        map.put("plan_id", inspection.getPlan_id());

        final List<Map<String, Object>> itemList = new ArrayList<>();
        for (InspectionItem item : inspectionItemList) {
            final Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("item_id", item.getItem_id());
            itemMap.put("state",item.getState());
            itemMap.put("result_name", item.getResult_name());
            itemMap.put("note", item.getNote());

            final List<String> attachmentList = bizService.listAttachments(item.getId_(), AttachmentType.PHOTO);
            if (!CollectionUtils.isEmpty(attachmentList)) {
                itemMap.put("pic_count", attachmentList.size());

                final List<Map<String, Object>> photoList = new ArrayList<>();
                for (String path : attachmentList) {
                    try {
                        final String encodeString = encodePhoto(path);
                        final Map<String, Object> photoMap = new HashMap<>();
                        photoMap.put("pic", encodeString);
                        photoList.add(photoMap);
                    } catch (Exception e) {
                        Logger.p("fail to encode bitmap to string, the path is: " + path, e);
                    }
                }
                itemMap.put("pics", photoList);
            }
            itemList.add(itemMap);
        }
        map.put("items", itemList);

        execute(map);
    }

    private static void execute(Object obj) {
        if (obj instanceof Chat) {
            WebSocketWriterProcessor.getInstance().execute((Chat) obj);
            return;
        }

        String payload = null;
        try {
            payload = GsonUtils.toJson(obj);
        } catch (Exception e) {
            Logger.p("fail to parse object to json string", e);
        }
        if (!TextUtils.isEmpty(payload)) {
            final Chat chat = toDataChat(payload);
            WebSocketWriterProcessor.getInstance().execute(chat);
        }
    }

    private static Chat toDataChat(String payload) {
        final Chat.ChatData chatData = new Chat.ChatData();
        chatData.setId(UUID.randomUUID().toString());
        chatData.setPayload(payload);

        final Chat result = new Chat();
        result.setDirective(ChatDirective.DATA);
        result.setTarget("admin");
        result.setData(chatData);

        return result;
    }

    private static Chat toAskChat(String id) {
        final Chat.ChatData chatData = new Chat.ChatData();
        chatData.setId(id);

        final Chat result = new Chat();
        result.setDirective(ChatDirective.ASK);
        result.setData(chatData);

        return result;
    }

    private static String encodePhoto(String path) throws Exception {
        return ImageUtils.encode(path);
    }

}
