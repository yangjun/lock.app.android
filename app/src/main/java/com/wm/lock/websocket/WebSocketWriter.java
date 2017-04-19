package com.wm.lock.websocket;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.wm.lock.LockConstants;
import com.wm.lock.core.logger.Logger;
import com.wm.lock.core.security.SecurityManager;
import com.wm.lock.core.utils.CollectionUtils;
import com.wm.lock.entity.AttachmentSource;
import com.wm.lock.entity.AttachmentType;
import com.wm.lock.entity.AttachmentUpload;
import com.wm.lock.entity.Chat;
import com.wm.lock.entity.ChatDirective;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.InspectionItem;
import com.wm.lock.entity.InspectionState;
import com.wm.lock.entity.LockOpenRecord;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.biz.IBizService;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WebSocketWriter {

    public static void start() {
        WebSocketWriterProcessor.getInstance().startIfNot();
    }

    public static void stop() {
        WebSocketWriterProcessor.getInstance().stop();
    }

    public static void ask(String id) {
        final Chat chat = toAskChat(id);
        execute(chat);
    }

    public static void login(String userJobNumber, String lockPwd) throws Exception {
        final Map<String, Object> map = new HashMap<>();
        map.put(LockConstants.BIZ_FLAG, LockConstants.BIZ_LOGIN);
        map.put("user_job_number", userJobNumber);
        map.put("lock_password", SecurityManager.md5(lockPwd));

        final String payload = convertToString(map);
        final Chat chat = toDataChat(payload);

        final String data = convertToString(chat);
        WebSocketWriterProcessor.getInstance().doSend(data);
    }

    public static void uploadLockOpenRecord(LockOpenRecord record) {
        final Map<String, Object> map = new HashMap<>();
        map.put(LockConstants.BIZ_FLAG, LockConstants.BIZ_LOCK_OPEN_RECORD);

        final Map<String, Object> dtoMap = convertToMap(record);
        map.putAll(dtoMap);

        execute(map);
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

    public static void submitInspection(final long inspectionId, boolean async) {
        if (async) {
            new Thread() {
                @Override
                public void run() {
                    doSubmitInspection(inspectionId);
                }
            }.start();
        }
        else {
            doSubmitInspection(inspectionId);
        }
    }

    private static void doSubmitInspection(final long inspectionId) {
        final IBizService bizService = ModuleFactory.getInstance().getModuleInstance(IBizService.class);

        final Inspection inspection = bizService.findInspection(inspectionId);

        final Map<String, Object> map = new HashMap<>();
        map.put(LockConstants.BIZ_FLAG, LockConstants.BIZ_RESULT);
        map.put("plan_id", inspection.getPlan_id());
        buildAttachments(inspectionId, AttachmentSource.INSPECTION, map);

        final List<Map<String, Object>> itemList = new ArrayList<>();
        final List<InspectionItem> inspectionItemList = bizService.listInspectionItem(inspectionId);
        for (InspectionItem item : inspectionItemList) {
            final Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("item_id", item.getItem_id());
            itemMap.put("state",item.getState());
            itemMap.put("result", item.getResult());
            itemMap.put("note", item.getNote());
            buildAttachments(item.getId_(), AttachmentSource.INSPECTION_ITEM, itemMap);
            itemList.add(itemMap);
        }
        map.put("items", itemList);

        execute(map, true);
    }

    private static void buildAttachments(long foreignId, AttachmentSource source, Map<String, Object> map) {
        final IBizService bizService = ModuleFactory.getInstance().getModuleInstance(IBizService.class);

        // photo
        final List<String> attachmentList = bizService.listAttachments(foreignId, source, AttachmentType.PHOTO);
        if (!CollectionUtils.isEmpty(attachmentList)) {
            final List<Map<String, Object>> picList = new ArrayList<>();
            for (String path : attachmentList) {
                final AttachmentUpload attachmentUpload = bizService.findAttachmentUploadByPath(path);
                final Map<String, Object> photoMap = new HashMap<>();
                photoMap.put("pic", attachmentUpload.getUploadedId());
                photoMap.put("pic_type", 1);
                picList.add(photoMap);
            }
            map.put("pics", picList);
            map.put("pic_count", attachmentList.size());
        }
        else {
            map.put("pics", new ArrayList<>());
            map.put("pic_count", 0);
        }
    }

    private static void execute(Object obj) {
        execute(obj, false);
    }

    private static void execute(Object obj, boolean insertToHead) {
        if (obj instanceof Chat) {
            WebSocketWriterProcessor.getInstance().execute((Chat) obj, insertToHead);
            return;
        }

        String payload = null;
        try {
            payload = convertToString(obj);
        } catch (Exception e) {
            Logger.p("fail to parse object to json string", e);
        }
        if (!TextUtils.isEmpty(payload)) {
            final Chat chat = toDataChat(payload);
            WebSocketWriterProcessor.getInstance().execute(chat, insertToHead);
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

    private static String convertToString(Object obj) {
        final Gson gson = getGsonBuilder().create();
        return gson.toJson(obj);
    }

    private static Map<String, Object> convertToMap(Object obj) {
        final Gson gson = getGsonBuilder().create();
        final String json = gson.toJson(obj);
        final Map<String, Object> map = gson.fromJson(json, new TypeToken<Map<String, Object>>(){}.getType());
        return map;
    }

    private static GsonBuilder getGsonBuilder() {
        return new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .setDateFormat(DateFormat.LONG);
    }

    private static class DateSerializer implements JsonSerializer<Date> {
        @Override
        public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(date.getTime());
        }
    }

}
