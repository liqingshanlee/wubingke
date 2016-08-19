package tech.honc.android.apps.soldier.feature.im.utils;

import android.app.NotificationManager;
import android.content.Context;

/**
 * Created by wangh on 2016-4-19-0019.
 */
public class NotifiyUtil {

  public static final int FLAG_ID_DISCONNECT = 2222;
  public static final int FLAG_ID_ADD_CONTACT_REQ = 3333;
  public static final int FLAG_ID_ADD_CONTACT_RESPON = 3334;

  public static void clearAllNotifiy(Context context) {
    NotificationManager notificationManager =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.cancelAll();
  }

  public static void clearNotifiyById(Context context, int Id) {
    NotificationManager notificationManager =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.cancel(Id);
  }
}
