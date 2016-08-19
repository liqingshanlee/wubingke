package tech.honc.android.apps.soldier.feature.im.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import mediapicker.MediaItem;

/**
 * Created by kevin on 2016/5/31.
 */
public final class FileUtil {

  public static int getFileSize(MediaItem mediaItem, Context context) {
    File file = new File(mediaItem.getPathOrigin(context));
    return getFileSize(file);
  }

  private static int getFileSize(File file) {
    int size = 0;
    if (file.exists()) {
      FileInputStream fis = null;
      try {
        fis = new FileInputStream(file);
        size = fis.available();
      } catch (Exception e) {
        e.printStackTrace();
        Log.e("获取文件大小", "文件不存在!");
      }finally {
        try {
          if (fis != null) {
            fis.close();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return size;
  }

  public static String imageType(Context context, Uri uri) {
    ContentResolver cR = context.getContentResolver();
    MimeTypeMap mime = MimeTypeMap.getSingleton();
    String type = mime.getExtensionFromMimeType(cR.getType(uri));
    if (type != null) {
      return type;
    }
    return "png";
  }
}
