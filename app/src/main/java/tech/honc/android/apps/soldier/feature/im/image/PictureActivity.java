package tech.honc.android.apps.soldier.feature.im.image;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import com.alibaba.wxlib.util.IWxCallback;
import tech.honc.android.apps.soldier.ui.activity.BaseActivity;

/**
 * Created by wangh on 2016-5-20-0020.
 */
public class PictureActivity extends BaseActivity {
  private IWxCallback mCallback;
  private static final int PHOTO_REQUEST_ALBUM = 1;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    if (intent != null) {
      mCallback = PictureUtils.getCallback();
      int op = intent.getIntExtra(PictureUtils.OPERATION, -1);
      if (op == PictureUtils.ALBUM) {
        getPictureFromAlbum();
      }
    }
  }

  private void getPictureFromAlbum() {
    Intent intent = new Intent(Intent.ACTION_PICK);
    intent.setType("image/*");
    // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
    startActivityForResult(intent, PHOTO_REQUEST_ALBUM);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == PHOTO_REQUEST_ALBUM && data != null) {
      Uri uri = data.getData();
      String path = getFilePathFromUri(uri);
      if (mCallback != null) {
        mCallback.onSuccess(path);
      }
    }
    finish();
  }

  private String getFilePathFromUri(Uri uri) {
    String filePath;
    String[] filePathColumn = { MediaStore.MediaColumns.DATA };

    Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
    cursor.moveToFirst();

    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
    filePath = cursor.getString(columnIndex);
    cursor.close();
    return filePath;
  }
}
