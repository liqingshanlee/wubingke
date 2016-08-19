package tech.honc.android.apps.soldier.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import java.util.ArrayList;
import mediapicker.MediaItem;
import mediapicker.MediaOptions;
import mediapicker.activities.MediaPickerActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.BuildConfig;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.PhotoService;
import tech.honc.android.apps.soldier.model.Image;
import tech.honc.android.apps.soldier.model.Status;
import tech.honc.android.apps.soldier.oss.OSSUtils;
import tech.honc.android.apps.soldier.ui.widget.PhotoGalleryCollectionView;
import tech.honc.android.apps.soldier.utils.toolsutils.DialogUtil;
import tech.honc.android.apps.soldier.utils.toolsutils.Md5Util;
import tech.honc.android.apps.soldier.utils.toolsutils.SnackBarUtil;

/**
 * Created by MrJiang
 * on 2016/4/29.
 * 我的相册
 */
public class PersonalPhotoGalleryActivity extends BaseNetworkActivity<ArrayList<Image>>
    implements EasyViewHolder.OnItemClickListener, EasyViewHolder.OnItemLongClickListener,
    MaterialDialog.ListCallback {

  public static final int REQUEST_MEDIA = 100;
  private static final int REQUEST_CODE = 1;
  String[] permissions =
      { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE };
  @Bind(R.id.photo_gallery) PhotoGalleryCollectionView mPhotoGallery;
  private ArrayList<Image> mImageLists;
  private PhotoService mPhotoService;
  private ArrayList<MediaItem> mMediaSelectedList = new ArrayList<>();
  private ArrayList<String> mObjectKeys = new ArrayList<>();
  private ArrayList<OSSAsyncTask> ossAsyncTasks = new ArrayList<>();
  private OSSUtils mOSSUtils;
  private Image mImage;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_photo_gallery);
    ButterKnife.bind(this);
    init();
  }

  private void init() {
    mOSSUtils = new OSSUtils();
    mImageLists = new ArrayList<>();
    mPhotoService = ApiService.createPhotoService();
    mPhotoGallery.setOnClickListener(this);
    mPhotoGallery.setLongOnClickListener(this);
  }

  private void startRequestImages() {
    Call<ArrayList<Image>> callPhotos = mPhotoService.getImages();
    networkQueue().enqueue(callPhotos);
  }

  @Override public void respondSuccess(ArrayList<Image> data) {
    super.respondSuccess(data);
    mImageLists.clear();
    mImageLists.addAll(data);
  }

  @Override public void endRequest() {
    super.endRequest();
    dismissHud();
    if (mImageLists != null && mImageLists.size() != 0) {
      mPhotoGallery.setData(mImageLists);
    }
    mMediaSelectedList.clear();
    mObjectKeys.clear();
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      if (requestCode == REQUEST_MEDIA) {
        mMediaSelectedList.clear();
        mMediaSelectedList.addAll(MediaPickerActivity.getMediaItemSelected(data));
        uploadImage();
      }
    }
  }

  public boolean isAddButton(int position) {
    return mImageLists.size() == position || mImageLists.size() <= 0 || mImageLists == null;
  }

  public void uploadImage() {
    showHud("正在努力上传...");
    if (mMediaSelectedList != null && mMediaSelectedList.size() > 0) {
      uploadToOss();
    } else {
      SnackBarUtil.showText(PersonalPhotoGalleryActivity.this, "你还没有选择照片..");
    }
  }

  public void doPublish() {
    Call<Status> callUpload = mPhotoService.uploadPhoto(mObjectKeys);
    callUpload.enqueue(new Callback<Status>() {
      @Override public void onResponse(Call<Status> call, Response<Status> response) {
        dismissHud();
        if (response.isSuccessful()) {
          startRequestImages();
          mObjectKeys.clear();
        } else {
          SnackBarUtil.showText(PersonalPhotoGalleryActivity.this, "上传出现问题,我们正在努力解决");
        }
      }

      @Override public void onFailure(Call<Status> call, Throwable t) {
        dismissHud();
        Log.e("---------->", "onFailure: " + t.toString());
        SnackBarUtil.showText(PersonalPhotoGalleryActivity.this, "上传出现问题,我们正在努力解决");
      }
    });
  }

  private void uploadToOss() {
    final OSS oss = mOSSUtils.oss;
    final String bucketName = BuildConfig.OSS_BUCKET;
    if (mMediaSelectedList.size() > 0) {
      for (MediaItem mediaItem : mMediaSelectedList) {
        uploadObject(oss, bucketName, buildObjectKey(mediaItem), mediaItem.getPathOrigin(this));
      }
    }
  }

  private String buildObjectKey(MediaItem mediaItem) {
    return Md5Util.encode(mediaItem.getPathOrigin(this));
  }

  private void uploadObject(OSS oss, String bucketName, String objectKey, String uploadFilePath) {
    PutObjectRequest put = new PutObjectRequest(bucketName, objectKey, uploadFilePath);
    // 异步上传时可以设置进度回调
    put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
      @Override public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
        Log.d("PutObject", "objectKey: "
            + request.getObjectKey()
            + " currentSize: "
            + currentSize
            + " totalSize: "
            + totalSize);
      }
    });
    final OSSAsyncTask task =
        oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
          @Override public void onSuccess(PutObjectRequest request, PutObjectResult result) {
            mObjectKeys.add(request.getObjectKey());
            if (shouldPublishToServer()) {
              doPublish();
            }
          }

          @Override public void onFailure(PutObjectRequest request, ClientException clientExcepion,
              ServiceException serviceException) {
            dismissHud();
            // 请求异常
            if (clientExcepion != null) {
              // 本地异常如网络异常等
              clientExcepion.printStackTrace();
              SnackBarUtil.showText(PersonalPhotoGalleryActivity.this,
                  clientExcepion.getLocalizedMessage());
            }
            if (serviceException != null) {
              // 服务异常
              Log.e("ErrorCode", serviceException.getErrorCode());
              Log.e("RequestId", serviceException.getRequestId());
              Log.e("HostId", serviceException.getHostId());
              Log.e("RawMessage", serviceException.getRawMessage());
              SnackBarUtil.showText(PersonalPhotoGalleryActivity.this,
                  serviceException.getRawMessage());
            }
          }
        });
    ossAsyncTasks.add(task);
  }

  private boolean shouldPublishToServer() {
    return mObjectKeys.size() == mMediaSelectedList.size();
  }

  @Override public void onItemClick(int position, View view) {
    if (isAddButton(position)) {
      requestPermission();
    } else {
      Navigator.startPhotoBDActivity(this, mImageLists, position);
    }
  }

  public void pickImage() {
    MediaOptions.Builder builder = new MediaOptions.Builder();
    MediaOptions options = builder.canSelectMultiPhoto(true)
        .setMediaListSelected(mMediaSelectedList)
        .setImageSize(9)
        .build();
    MediaPickerActivity.open(this, REQUEST_MEDIA, options, false);
  }

  @Override public boolean onLongItemClicked(int position, View view) {
    if (isAddButton(position)) {
      pickImage();
    } else {
      mImage = mImageLists.get(position);
      DialogUtil.relationDeleteDialogImage(this, this);
    }
    return true;
  }

  @Override
  public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
    switch (which) {
      case 0:
        deleteImage(mImage.id);
        break;
      case 1:
        break;
    }
  }

  public void deleteImage(Integer id) {
    Call<Status> call = mPhotoService.deletePhoto(id);
    call.enqueue(new Callback<Status>() {
      @Override public void onResponse(Call<Status> call, Response<Status> response) {
        if (response.isSuccessful()) {
          //更新图片
          startRequestImages();
          SnackBarUtil.showText(PersonalPhotoGalleryActivity.this, "删除成功");
        } else {
          SnackBarUtil.showText(PersonalPhotoGalleryActivity.this, "删除失败");
        }
      }

      @Override public void onFailure(Call<Status> call, Throwable t) {
        SnackBarUtil.showText(PersonalPhotoGalleryActivity.this, "删除失败");
      }
    });
  }

  @Override protected void onResume() {
    super.onResume();
    if (mMediaSelectedList.size() == 0) startRequestImages();
  }

  @TargetApi(Build.VERSION_CODES.M) public void requestPermission() {
    //判断当前Activity是否已经获得了该权限
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
          != PackageManager.PERMISSION_GRANTED) {

        //如果App的权限申请曾经被用户拒绝过，就需要在这里跟用户做出解释
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
          SnackBarUtil.showText(this, "选择照片需要权限哦，请同意");
        } else {
          //进行权限请求
          ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
        }
      } else {
        pickImage();
      }
    } else {
      pickImage();
    }
  }
}
