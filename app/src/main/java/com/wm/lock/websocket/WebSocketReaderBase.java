package com.wm.lock.websocket;

import com.wm.lock.LockConstants;
import com.wm.lock.entity.Chat;
import com.wm.lock.entity.ChatDirective;
import com.wm.lock.entity.UserInfo;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.biz.IBizService;
import com.wm.lock.module.user.IUserService;

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

}
