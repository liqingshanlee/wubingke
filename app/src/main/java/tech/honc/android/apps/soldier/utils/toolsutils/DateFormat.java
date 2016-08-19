package tech.honc.android.apps.soldier.utils.toolsutils;

import android.annotation.SuppressLint;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MrJiang on 4/5/2016.
 */
public class DateFormat {

  public static String mOldDate;
  public static final String TAG = DateFormat.class.getSimpleName();

  public static CharSequence getRelativeTime(final long time) {
    return getRelativeTime(new Date(time * 1000));
  }

  public static String getRelativeTime(final Date date) {
    @SuppressLint("SimpleDateFormat") String date1 =
        new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date(date.getTime()));
    return date1;
  }

  /**
   * 获取具体小时，分钟
   */
  public static String getCommonTime(final long time) {
    Date date = new Date(time * 1000);
    @SuppressLint("SimpleDateFormat") String date1 =
        new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(date.getTime()));
    return date1.substring(10, 16);
  }

  public static boolean isCommon(String date) {
    if (date.equals(mOldDate)) {
      Log.d(TAG, "isCommon: " + mOldDate);
      return true;
    } else {
      mOldDate = date;
    }
    Log.d(TAG, "isCommon: " + mOldDate);
    return false;
  }
}
