package com.wm.lock.helper;

import android.content.Context;
import android.os.Bundle;

import com.wm.lock.LockApplication;
import com.wm.lock.LockConstants;
import com.wm.lock.R;
import com.wm.lock.core.utils.NotificationUtils;
import com.wm.lock.dto.InspectionNewDto;
import com.wm.lock.ui.activities.HomeActivity_;

public class NotificationHelper {

    /**
     * 显示收到新巡检计划的通知
     */
    public static void showNewInspection(long count) {
        final Context ctx = LockApplication.getInstance();
        String message = ctx.getResources().getString(R.string.message_new_inspection);
        message = String.format(message, count);

        final InspectionNewDto dto = new InspectionNewDto();
        dto.setCount(count);

        final Bundle bundle = new Bundle();
        bundle.putSerializable(LockConstants.DATA, dto);
        NotificationUtils.showNotification(LockApplication.getInstance(), 0, message, HomeActivity_.class, bundle);
    }

    /**
     * 消失收到新巡检计划的通知
     */
    public static void dismissNewInspection() {
        NotificationUtils.cancelNotification(LockApplication.getInstance(), 0);
    }

    /**
     * 显示数据同步中的通知
     */
    public static void showSync() {
        final String message = LockApplication.getInstance().getString(R.string.message_sync_notification);
        NotificationUtils.showNotification(LockApplication.getInstance(), 110, message, null, null);
    }

    /**
     * 消失数据同步中的通知
     */
    public static void dismissSync() {
        NotificationUtils.cancelNotification(LockApplication.getInstance(), 110);
    }

}
