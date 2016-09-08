package com.wm.lock.websocket;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.wm.lock.LockApplication;
import com.wm.lock.LockConfig;
import com.wm.lock.LockConstants;
import com.wm.lock.core.logger.Logger;
import com.wm.lock.core.utils.GsonUtils;
import com.wm.lock.core.utils.HardwareUtils;
import com.wm.lock.dto.DataWriteFailDto;
import com.wm.lock.entity.Chat;
import com.wm.lock.entity.ChatDirective;
import com.wm.lock.entity.Communication;
import com.wm.lock.entity.InspectionState;
import com.wm.lock.entity.UserInfo;
import com.wm.lock.exception.BizException;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.biz.IBizService;
import com.wm.lock.module.user.IUserService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.greenrobot.event.EventBus;

class WebSocketWriterProcessor {

    private ExecutorService mExecutorService;

    /**
     * 上次写入的通信记录id
     */
    private long mLastWriteId = 0L;

    /**
     * 是否正在写入
     */
    private volatile boolean isWriting = false;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private WebSocketWriterProcessor() {
        mExecutorService = Executors.newFixedThreadPool(10);
    }

    private static class InstanceHolder {
        static final WebSocketWriterProcessor instance = new WebSocketWriterProcessor();
    }

    public static WebSocketWriterProcessor getInstance() {
        return InstanceHolder.instance;
    }

    public void execute(final Chat chat) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                final Communication communication = toCommunication(chat);
                bizService().addCommunication(communication);
                startIfNot();

                // FIXME 测试用,正式发布时去掉该代码
                simulateAsk(chat);
            }
        });
    }

    public synchronized void startIfNot() {
        if (isWriting) {
            return;
        }

        isWriting = true;

        new Thread() {
            @Override
            public void run() {
                final Communication communication = bizService().findNextWriteCommunication(loginUser().getJobNumber(), mLastWriteId);
                if (communication == null) {
                    isWriting = false;
                    return;
                }
                try {
                    send(communication.getContent());
                    sendSuccess(communication);
                    mLastWriteId = communication.getId_();
                    isWriting = false;
                    startIfNot(); //继续
                } catch (Exception e) {
                    sendFail(e);
                    isWriting = false;
                }
            }
        }.start();
    }

    public void stop() {
        isWriting = false;
    }

    private Communication toCommunication(Chat chat) {
        final Communication result = new Communication();
        result.setId(chat.getData().getId());
        result.setDirective(chat.getDirective());
        result.setTarget("admin");
        result.setSource(loginUser().getJobNumber());

        try {
            result.setContent(GsonUtils.toJson(chat));
        } catch (Exception e) {
            throw new BizException(e);
        }

        return result;
    }

    private void send(String data) throws Exception {
        // TODO 发送数据
    }

    private void sendSuccess(Communication communication) {
        final String content = communication.getContent();
        Chat chat = null;
        try {
            chat = GsonUtils.fromJson(content, Chat.class);
        } catch (Exception e) {
            Logger.p("fail to parse string to Chat entity", e);
        }
        if (chat != null && TextUtils.equals(chat.getDirective(), ChatDirective.ASK)) {
            bizService().deleteCommunication(communication.getId_());
        }
    }

    private void sendFail(Exception e) {
        Logger.p("fail to send data to web socket server", e);

        int error = DataWriteFailDto.ERROR_OTHER;
        if (!HardwareUtils.isNetworkAvailable(LockApplication.getInstance())) {
            error = DataWriteFailDto.ERROR_NETWORK;
        }

        final DataWriteFailDto dto = new DataWriteFailDto();
        dto.setError(error);
        EventBus.getDefault().post(dto);
    }

    private UserInfo loginUser() {
        final IUserService userService = ModuleFactory.getInstance().getModuleInstance(IUserService.class);
        return userService.getLoginedInfo();
    }

    private IBizService bizService() {
        return ModuleFactory.getInstance().getModuleInstance(IBizService.class);
    }

    private void simulateAsk(final Chat chat) {
        if (LockConfig.MODE == LockConfig.MODE_JUNIT) {
            if (!chat.getDirective().equals(ChatDirective.DATA)) {
                return;
            }

            // 模拟回复
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        final Chat.ChatData chatData = new Chat.ChatData();
                        chatData.setId(chat.getData().getId());
                        final Chat askChat = new Chat();
                        askChat.setDirective(ChatDirective.ASK);
                        WebSocketReader.execute(GsonUtils.toJson(chat));
                    } catch (Exception e) {
                        Logger.d("fail to simulate ask data from web socket server", e);
                    }
                }
            }, 1000);

            // 模拟提交结果反馈
            if (chat.getData().getPayload().contains("\"" + LockConstants.BIZ_FLAG + "\": \"" + LockConstants.BIZ_RESULT + "\"")) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final Map<String, Object> map1 = GsonUtils.fromJson(chat.getData().getPayload(), new TypeToken<Map<String, Object>>() {
                            });

                            final Map<String, Object> map = new HashMap<>();
                            map.put(LockConstants.BIZ_FLAG, LockConstants.BIZ_RESULT_RETURN);
                            map.put("plan_id", map1.get("plan_id"));
                            map.put("state", InspectionState.COMPLETE);

                            final Chat.ChatData chatData = new Chat.ChatData();
                            chatData.setId(chat.getData().getId());
                            chatData.setSource("admin");
                            chatData.setPayload(GsonUtils.toJson(map));

                            final Chat dataChat = new Chat();
                            dataChat.setDirective(ChatDirective.DATA);
                            dataChat.setData(chatData);

                            WebSocketReader.execute(GsonUtils.toJson(dataChat));
                        } catch (Exception e) {
                            Logger.d("fail to simulate ask data from web socket server", e);
                        }
                    }
                }, 2000);
            }
        }
    }

}