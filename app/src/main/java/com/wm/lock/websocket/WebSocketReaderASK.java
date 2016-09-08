package com.wm.lock.websocket;

import com.wm.lock.entity.Chat;
import com.wm.lock.entity.Communication;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.biz.IBizService;

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
        final String content = communication.getContent();
        if (contains(content, "PLAN_RETURN")) {
            bizService().deleteCommunication(communication.getId_());
        }
    }

}
