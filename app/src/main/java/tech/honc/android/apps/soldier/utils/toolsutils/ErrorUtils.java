package tech.honc.android.apps.soldier.utils.toolsutils;

import com.smartydroid.android.starter.kit.BuildConfig;

/**
 * Created by YuGang Yang on 03 26, 2016.
 * Copyright 20015-2016 honc.tech. All rights reserved.
 */
public class ErrorUtils {

  public static void handleException(Exception e) {
    if (BuildConfig.DEBUG) {
      e.printStackTrace();
    } else {
    }
  }

  public static void handleMessage(String message) {
    if (BuildConfig.DEBUG) {
    } else {
    }
  }
}
