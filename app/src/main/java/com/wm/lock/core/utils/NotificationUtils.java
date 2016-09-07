package com.wm.lock.core.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;

import com.wm.lock.R;

public class NotificationUtils {

    public static void showNotification(Context ctx, int id, String message, Class targetClazz) {
        final NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);
        builder.setContentTitle(ctx.getString(R.string.app_name))
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher);

        final Intent resultIntent = new Intent(ctx, targetClazz);
        int requestCode = (int) SystemClock.uptimeMillis();
        final PendingIntent pendingIntent = PendingIntent.getActivity(ctx, requestCode, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        final Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_ALL;
        notification.flags =Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(id, notification);
    }

    public static void cancelNotification(Context ctx, int id) {
        final NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }

}
