package com.wm.lock.websocket;

import android.text.TextUtils;

import com.wm.lock.core.logger.Logger;
import com.wm.lock.core.utils.GsonUtils;
import com.wm.lock.entity.Chat;
import com.wm.lock.entity.ChatDirective;

class WebSocketReader {

    public void stop() {
        WebSocketReaderProcessor.getInstance().stop();
    }

    public static void execute(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }

        Chat chat = null;
        try {
            chat = GsonUtils.fromJson(message, Chat.class);
        } catch (Exception e) {
            Logger.p("fail to parse message to chat entity", e);
        }
        if (chat != null) {
            execute(chat);
        }
    }

    private static void execute(Chat chat) {
        final WebSocketReaderBase reader = getInstance(chat);
        if (reader != null) {
            reader.execute(chat);
        }
    }

    private static WebSocketReaderBase getInstance(Chat chat) {
        switch (chat.getDirective()) {
            case ChatDirective.ASK:
                return new WebSocketReaderASK();

            case ChatDirective.DATA:
                return new WebSocketReaderDATA();

            case ChatDirective.BYE:
                return new WebSocketReaderBye();
        }
        return null;
    }

}
