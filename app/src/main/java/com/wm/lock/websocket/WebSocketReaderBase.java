package com.wm.lock.websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
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

    protected <T> T converFormJson(String json, Class<T> clazz) {
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new JsonDeserializer() {

            @Override
            public Date deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2)
                    throws com.google.gson.JsonParseException {
                return new Date(arg0.getAsJsonPrimitive().getAsLong());
            }

        });
        final Gson gson = builder.create();
        return gson.fromJson(json, clazz);
    }

}
