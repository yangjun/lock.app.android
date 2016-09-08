package com.wm.lock.websocket;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.wm.lock.R;
import com.wm.lock.core.logger.Logger;
import com.wm.lock.core.utils.GsonUtils;
import com.wm.lock.entity.Chat;
import com.wm.lock.entity.ChatDirective;
import com.wm.lock.entity.Inspection;
import com.wm.lock.entity.InspectionItem;
import com.wm.lock.entity.params.InspectionQueryParam;
import com.wm.lock.module.ModuleFactory;
import com.wm.lock.module.biz.IBizService;
import com.wm.lock.module.user.IUserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class WebSocketService extends Service {

    private static final int NOTIFICATION_ID = 1017;
    private Handler mHandler = new Handler(Looper.getMainLooper());

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
        return Service.START_STICKY;
    }


    private void start() {
        // FIXME 模拟在没有巡检项的时候有推送到达，替换为正常逻辑
        simulatePush();
    }

    private void startForegroundCompat() {
        if (Build.VERSION.SDK_INT < 18) {
            // api 18（4.3）以下，随便玩
            startForeground(NOTIFICATION_ID, new Notification());
        } else {
            // api 18的时候，google管严了，得绕着玩
            // 先把自己做成一个前台服务，提供合法的参数
            startForeground(NOTIFICATION_ID, fadeNotification(this));
            // 再起一个服务，也是前台的
            startService(new Intent(this, InnerService.class));
        }
    }

    private static Notification fadeNotification(Context context) {
        Notification notification = new Notification();
        // 随便给一个icon，反正不会显示，只是假装自己是合法的Notification而已
        notification.icon = R.mipmap.ic_launcher;
        notification.contentView = new RemoteViews(context.getPackageName(), R.layout.notification_transparent);
        return notification;
    }

    public static class InnerService extends Service {

        @Override
        public void onCreate() {
            super.onCreate();
            // 先把自己也搞成前台的，提供合法参数
            startForeground(NOTIFICATION_ID, fadeNotification(this));
            // 关键步骤来了：自行推掉。
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

    private void simulatePush() {
        new Thread() {
            @Override
            public void run() {
                final IBizService bizService = ModuleFactory.getInstance().getModuleInstance(IBizService.class);
                final IUserService userService = ModuleFactory.getInstance().getModuleInstance(IUserService.class);

                final InspectionQueryParam param = new InspectionQueryParam();
                param.setUser_job_number(userService.getLoginedInfo().getJobNumber());
                final long count = bizService.countInspection(param);
                if (count <= 0) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final InspectionItem item1 = new InspectionItem();
                                item1.setCabinet_lock_mac("21:32:4A:5C");
                                item1.setCabinet_name("机柜1");
                                item1.setEquipment_name("设备1");
                                item1.setItem_cate_name("分类1");
                                item1.setItem_id(UUID.randomUUID().toString());
                                item1.setItem_name("检查冷却液的运行情况");

                                final InspectionItem item2 = new InspectionItem();
                                item2.setCabinet_lock_mac("2Q:32:4A:5A");
                                item2.setCabinet_name("机柜2");
                                item2.setEquipment_name("设备2");
                                item2.setItem_cate_name("分类3");
                                item2.setItem_id(UUID.randomUUID().toString());
                                item2.setItem_name("检查机柜的温度，湿度，并把对应的结果友好圆满的展示出来");

                                final List<InspectionItem> inspectionItemList = new ArrayList<>();
                                inspectionItemList.add(item1);
                                inspectionItemList.add(item2);

                                final Inspection inspection = new Inspection();
                                inspection.setPlan_id(UUID.randomUUID().toString());
                                inspection.setPlan_name("巡检计划001");
                                inspection.setPlan_date(new Date());
                                inspection.setLock_mac("12:3S:6R:7B");
                                inspection.setRoom_name("机房1");
                                inspection.setDispatch_man("张经理");
                                inspection.setInspection_item_list(inspectionItemList);

                                final Chat.ChatData chatData = new Chat.ChatData();
                                chatData.setId(UUID.randomUUID().toString());
                                chatData.setSource("admin");
                                chatData.setPayload(GsonUtils.toJson(inspection));

                                final Chat chat = new Chat();
                                chat.setDirective(ChatDirective.DATA);
                                chat.setData(chatData);

                                WebSocketReader.execute(GsonUtils.toJson(chat));
                            } catch (Exception e) {
                                Logger.p("fail to simulate push inspection from web socket server", e);
                            }
                        }
                    }, 5000);
                }
            }
        }.start();
    }

}
