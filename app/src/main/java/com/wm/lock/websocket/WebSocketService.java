package com.wm.lock.websocket;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.google.gson.reflect.TypeToken;
import com.wm.lock.LockConfig;
import com.wm.lock.LockConstants;
import com.wm.lock.R;
import com.wm.lock.core.logger.Logger;
import com.wm.lock.core.utils.GsonUtils;
import com.wm.lock.core.utils.HardwareUtils;
import com.wm.lock.core.utils.NetWorkUtils;
import com.wm.lock.entity.Chat;
import com.wm.lock.entity.ChatDirective;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.InspectionItem;
import com.wm.lock.entity.UserInfo;
import com.wm.lock.entity.params.InspectionQueryParam;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.biz.IBizService;
import com.wm.lock.module.user.IUserService;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.R.attr.type;

public class WebSocketService extends Service {

    private static final long DELAY = 60000;

    private static final int NOTIFICATION_ID = 1017;
    private static WebSocketClient mClient;
    private BroadcastReceiver mNetChangeReceiver;

    private Handler mHandler = new Handler();
    private Runnable mReConnectRunnable;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForegroundCompat();
        start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        connect();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mNetChangeReceiver != null) {
            unregisterReceiver(mNetChangeReceiver);
        }
        WebSocketWriter.stop();
        disConnect();
        super.onDestroy();
    }

    private void start() {
        WebSocketImpl.DEBUG = true;
        System.setProperty("java.net.preferIPv6Addresses", "false");
        System.setProperty("java.net.preferIPv4Stack", "true");

        mNetChangeReceiver = new NetChangeReceiver();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetChangeReceiver, intentFilter);

//        simulatePush();
    }

    private void startForegroundCompat() {
        if (Build.VERSION.SDK_INT < 18) {
            startForeground(NOTIFICATION_ID, new Notification());
        } else {
            startForeground(NOTIFICATION_ID, fadeNotification(this));
            startService(new Intent(this, InnerService.class));
        }
    }

    private static Notification fadeNotification(Context context) {
        Notification notification = new Notification();
        notification.icon = R.mipmap.ic_launcher;
        notification.contentView = new RemoteViews(context.getPackageName(), R.layout.notification_transparent);
        return notification;
    }

    private boolean canConnect() {
        // 如果正在连接或已经连接,直接返回false
        if (mClient != null && (mClient.getReadyState() == WebSocket.READYSTATE.CONNECTING || mClient.getReadyState() == WebSocket.READYSTATE.OPEN)) {
            return false;
        }

        final int type = NetWorkUtils.getNetworkState(getApplicationContext());
        return type >= LockConfig.NETWORK_MIN;
    }

    private synchronized void connect() {
        // 判断是否能执行连接
        if (!canConnect()) {
            return;
        }

        try {
            final String address = LockConfig.WS_SERVER + userInfo().getJobNumber();
            final URI uri = new URI(address);
            mClient = new WebSocketClient(uri, new Draft_10()) {
                @Override
                public void onOpen(final ServerHandshake serverHandshakeData) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Logger.d("success to connect web socket server");
                            WebSocketWriter.start(); //每次重连成功后都去查看有没有需要写的数据
                        }
                    });
                }

                @Override
                public void onMessage(final String message) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Logger.d("success to get message form web socket server: " + message);
                            WebSocketReader.execute(message);
                        }
                    });
                }

                @Override
                public void onClose(final int code, final String reason, final boolean remote) {
                    if (code != 1000 || remote) { //如果不是手动关闭，记录日志并尝试重新连接
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Logger.d(String.format("close form web socket server, code: %d, reason: %s", code, reason));
                            }
                        });
                        connectDelay(DELAY);
                    }
                }

                @Override
                public void onError(final Exception e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Logger.d(String.format("error form web socket server"), e);
                        }
                    });
                    connectDelay(DELAY);
                }
            };
            mClient.connect();
        } catch (Exception e) {
            Logger.p("fail to connect web socket server", e);
        }
    }

    private void connectDelay(long timeMills) {
        if (mReConnectRunnable != null) {
            mHandler.removeCallbacks(mReConnectRunnable);
        }
        mReConnectRunnable = new Runnable() {
            @Override
            public void run() {
                connect();
            }
        };
        mHandler.postDelayed(mReConnectRunnable, timeMills);
    }

    private synchronized void disConnect() {
        if (mClient != null) {
            mClient.close();
            mClient = null;
        }
    }

    public static synchronized void send(String data) throws Exception {
//        try {
            if (mClient != null) {
                mClient.send(data);
            }
//        } catch (Exception e) {
//            Logger.p("fail to send data to web socket server", e);
//        }
    }

    private class NetChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            switch (action) {
                case "android.net.conn.CONNECTIVITY_CHANGE":
                    onNetWorkChanged(context);
                    break;
            }
        }

        private void onNetWorkChanged(Context context) {
            final int type = NetWorkUtils.getNetworkState(context);
            if (type != NetWorkUtils.NETWORK_NONE) {
                connectDelay(0);
            }
        }

    }

    public static class InnerService extends Service {

        @Override
        public void onCreate() {
            super.onCreate();
            startForeground(NOTIFICATION_ID, fadeNotification(this));
            stopSelf();
        }

        @Override
        public void onDestroy() {
            stopForeground(true);
            super.onDestroy();
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

    }

//    private void simulatePush() {
//        new Thread() {
//            @Override
//            public void run() {
//                final IBizService bizService = ModuleFactory.getInstance().getModuleInstance(IBizService.class);
//                final IUserService userService = ModuleFactory.getInstance().getModuleInstance(IUserService.class);
//
//                final InspectionQueryParam param = new InspectionQueryParam();
//                param.setUser_job_number(userService.getLoginedInfo().getJobNumber());
//                final long count = bizService.countInspection(param);
//                if (count <= 0) {
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            for (int i = 0; i < 1; i++) {
//                                try {
//                                    final InspectionItem item1 = new InspectionItem();
//                                    item1.setCabinet_lock_mac("21:32:4A:5C");
//                                    item1.setCabinet_name("机柜1");
//                                    item1.setEquipment_name("设备1");
//                                    item1.setItem_cate_name("分类1");
//                                    item1.setItem_id(UUID.randomUUID().toString());
//                                    item1.setItem_name("检查冷却液的运行情况");
//
//                                    final InspectionItem item2 = new InspectionItem();
//                                    item2.setCabinet_lock_mac("2Q:32:4A:5A");
//                                    item2.setCabinet_name("机柜2");
//                                    item2.setEquipment_name("设备2");
//                                    item2.setItem_cate_name("分类2分类2分类2分类2分类2分类2分类2分类2分类2分类2");
//                                    item2.setItem_id(UUID.randomUUID().toString());
//                                    item2.setItem_name("检查机柜的温度，湿度，并把对应的结果友好圆满的展示出来");
//
//                                    final InspectionItem item3 = new InspectionItem();
//                                    item3.setCabinet_lock_mac("2Q:32:4A:5A");
//                                    item3.setCabinet_name("机柜3");
//                                    item3.setEquipment_name("设备3");
//                                    item3.setItem_cate_name("分类2分类2分类2分类2分类2分类2分类2分类2分类2分类2");
//                                    item3.setItem_id(UUID.randomUUID().toString());
//                                    item3.setItem_name("检查冷却液的运行情况");
//
//                                    final InspectionItem item4 = new InspectionItem();
//                                    item4.setCabinet_lock_mac("2Q:32:4A:5A");
//                                    item4.setCabinet_name("机柜4");
//                                    item4.setEquipment_name("设备4");
//                                    item4.setItem_cate_name("分类2分类2分类2分类2分类2分类2分类2分类2分类2分类2");
//                                    item4.setItem_id(UUID.randomUUID().toString());
//                                    item4.setItem_name("颜色会显示在最上面，记录上的文字被遮住，所以点击文字不放，文字就看不到");
//
//                                    final List<InspectionItem> inspectionItemList = new ArrayList<>();
//                                    inspectionItemList.add(item1);
//                                    inspectionItemList.add(item2);
//                                    inspectionItemList.add(item3);
//                                    inspectionItemList.add(item4);
//
//                                    final Inspection inspection = new Inspection();
//                                    inspection.setPlan_id(UUID.randomUUID().toString());
//                                    inspection.setPlan_name("巡检计划001");
//                                    inspection.setPlan_date(new Date());
//                                    inspection.setLock_mac("12:3S:6R:7B");
//                                    inspection.setRoom_name("机房1");
//                                    inspection.setDispatch_man("张经理");
//                                    inspection.setItems(inspectionItemList);
//
//                                    final Map<String, Object> map = GsonUtils.fromJson(GsonUtils.toJson(inspection), new TypeToken<Map<String, Object>>() {});
//                                    map.put("plan_date", new Date().getTime());
//                                    map.put(LockConstants.BIZ_FLAG, LockConstants.BIZ_PLAN);
//
//                                    final Chat.ChatData chatData = new Chat.ChatData();
//                                    chatData.setId(UUID.randomUUID().toString());
//                                    chatData.setSource("admin");
//                                    chatData.setPayload(GsonUtils.toJson(map));
//
//                                    final Chat chat = new Chat();
//                                    chat.setDirective(ChatDirective.DATA);
//                                    chat.setTarget(userInfo().getJobNumber());
//                                    chat.setData(chatData);
//
//                                    WebSocketReader.execute(GsonUtils.toJson(chat));
//                                } catch (Exception e) {
//                                    Logger.p("fail to simulate push inspection from web socket server", e);
//                                }
//                            }
//                        }
//                    }, 5000);
//                }
//            }
//        }.start();
//    }

    private UserInfo userInfo() {
        final IUserService userService = ModuleFactory.getInstance().getModuleInstance(IUserService.class);
        return userService.getLoginedInfo();
    }

}
