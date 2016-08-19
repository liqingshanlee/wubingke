package tech.honc.android.apps.soldier.utils.settings;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2016/6/7.
 */
public class VersionCode
{
  //版本名
  public static String getVersionName(Context context) {
    return getPackageInfo(context).versionName;
  }

  //版本号
  public static int getVersionCode(Context context) {
    return getPackageInfo(context).versionCode;
  }

  private static PackageInfo getPackageInfo(Context context) {
    PackageInfo pi = null;

    try {
      PackageManager pm = context.getPackageManager();
      pi = pm.getPackageInfo(context.getPackageName(),
          PackageManager.GET_CONFIGURATIONS);

      return pi;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return pi;
  }
}
