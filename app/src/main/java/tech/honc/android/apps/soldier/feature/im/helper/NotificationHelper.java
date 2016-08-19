package tech.honc.android.apps.soldier.feature.im.helper;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.feature.im.ui.activity.SystemAddContactActivity;
import tech.honc.android.apps.soldier.feature.im.ui.activity.SystemAddTribeActivity;
import tech.honc.android.apps.soldier.feature.im.utils.NotifiyUtil;
import tech.honc.android.apps.soldier.ui.app.SoldierApp;

/**
 * Created by Administrator on 2016/6/13.
 */
public final class NotificationHelper {

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  public static void showNotificationBySys(String notificationTitle, String Content,
      Intent intent) {
    Intent openContactIntent = new Intent(SoldierApp.getContext(), SystemAddContactActivity.class);
    Intent openTribeIntent = new Intent(SoldierApp.getContext(), SystemAddTribeActivity.class);
    PendingIntent Pintent = PendingIntent.getActivity(SoldierApp.getContext(), 222, intent, 0);
    NotificationManager mNotificationManager = (NotificationManager) SoldierApp.getContext()
        .getSystemService(SoldierApp.getContext().NOTIFICATION_SERVICE);
    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(SoldierApp.getContext());
    mBuilder.setContentTitle(notificationTitle)
        .setContentText(Content)
        .setWhen(System.currentTimeMillis())
        .setPriority(android.app.Notification.PRIORITY_HIGH)
        .setDefaults(android.app.Notification.DEFAULT_ALL)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentIntent(Pintent)
        .setOngoing(false);
    android.app.Notification notification = mBuilder.build();
    notification.flags =
        android.app.Notification.FLAG_AUTO_CANCEL | android.app.Notification.FLAG_ONLY_ALERT_ONCE;
    mNotificationManager.notify(NotifiyUtil.FLAG_ID_ADD_CONTACT_REQ, notification);
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  public static void showNotificationBySys(String notificationTitle, String Content,
      boolean isTribeNotification,long tribeId) {
    Intent openContactIntent = new Intent(SoldierApp.getContext(), SystemAddContactActivity.class);
    Intent openTribeIntent = new Intent(SoldierApp.getContext(), SystemAddTribeActivity.class);
    //Intent intent = imKit.getTribeChattingActivityIntent(tribe.getTribeId());
    PendingIntent Pintent = PendingIntent.getActivity(SoldierApp.getContext(), 222,
        isTribeNotification ? openTribeIntent : openContactIntent, 0);
    NotificationManager mNotificationManager = (NotificationManager) SoldierApp.getContext()
        .getSystemService(SoldierApp.getContext().NOTIFICATION_SERVICE);
    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(SoldierApp.getContext());
    mBuilder.setContentTitle(notificationTitle)
        .setContentText(Content)
        .setWhen(System.currentTimeMillis())
        .setPriority(Notification.PRIORITY_HIGH)
        .setDefaults(Notification.DEFAULT_ALL)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentIntent(Pintent)
        .setOngoing(false);
    Notification notification = mBuilder.build();
    notification.flags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE;
    mNotificationManager.notify(NotifiyUtil.FLAG_ID_ADD_CONTACT_REQ, notification);
  }
}
