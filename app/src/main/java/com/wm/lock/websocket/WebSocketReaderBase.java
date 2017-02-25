package com.wm.lock.websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.wm.lock.LockConstants;
import com.wm.lock.entity.Chat;
import com.wm.lock.entity.UserInfo;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.biz.IBizService;
import com.wm.lock.module.user.IUserService;

import java.lang.reflect.Type;
import java.util.Date;

abstract class WebSocketReaderBase {

    abstract void execute(Chat chat);

    protected UserInfo loginUser() {
        final IUserService userService = ModuleFactory.getInstance().getModuleInstance(IUserService.class);
        return userService.getLoginedInfo();
    }

    protected boolean contains(String content, String flag) {
        flag = String.format("\"%s\":\"%s\"", LockConstants.BIZ_FLAG, flag);
        content = content.replace("\\", "");
        return content.contains(flag);
    }

    protected IBizService bizService() {
        return ModuleFactory.getInstance().getModuleInstance(IBizService.class);
    }

    protected <T> T convertFormJson(String json, Class<T> clazz) {
        final Gson gson = getGsonBuilder().create();
        return gson.fromJson(json, clazz);
    }

    protected <T> T convertFormJson(String json, TypeToken<T> typeToken) {
        final Gson gson = getGsonBuilder().create();
        return gson.fromJson(json, typeToken.getType());
    }

    private GsonBuilder getGsonBuilder() {
        return new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(Date.class, new DateDeserializer());
    }

    private static class DateDeserializer implements JsonDeserializer{
        @Override
        public Object deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return new Date(jsonElement.getAsJsonPrimitive().getAsLong());
        }
    }

}
