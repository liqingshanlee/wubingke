package tech.honc.android.apps.soldier.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.ArrayList;
import java.util.List;
import mediapicker.MediaItem;
import mediapicker.MediaOptions;
import mediapicker.activities.MediaPickerActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import support.ui.components.SupportButton;
import tech.honc.android.apps.soldier.BuildConfig;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.PhotoService;
import tech.honc.android.apps.soldier.model.Certification;
import tech.honc.android.apps.soldier.oss.OSSUtils;
import tech.honc.android.apps.soldier.utils.toolsutils.Md5Util;
import tech.honc.android.apps.soldier.utils.toolsutils.SnackBarUtil;

/**
 * Created by Kevin on 2016/6/14.
 * 军人认证
 */
public class SoldierCertificationActivity extends BaseActivity {

  private static final int REQUEST_MEDIA = 100;
  @Bind(R.id.ui_view_certification) SimpleDraweeView mUiViewCertification;
  @Bind(R.id.ui_view_certification_pro) TextView mUiViewCertificationPro;
  @Bind(R.id.ui_view_support_sure) SupportButton mUiViewSupportSure;
  private List<MediaItem> mMediaSelectedList;
  private String key;
  private OSSUtils mOSSUtils;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_soldier_certification);
    ButterKnife.bind(this);
    initialization();
  }

  private void initialization() {
    mOSSUtils = new OSSUtils();
    mMediaSelectedList = new ArrayList<>();
  }

  @OnClick({ R.id.ui_view_certification, R.id.ui_view_support_sure }) public void onClick(View v) {
    switch (v.getId()) {
      case R.id.ui_view_certification:
        pickerImage();
        break;
      case R.id.ui_view_support_sure:
        //提交信息
        submitCertification();
        break;
    }
  }

  public void submitCertification() {
    if (mMediaSelectedList != null && mMediaSelectedList.size() > 0) {
      for (MediaItem mediaItem : mMediaSelectedList) {
        uploadPictureOss(mediaItem, mediaItem.getPathCropped(this));
      }
    } else {
      SnackBarUtil.showText(this, "请选择你的证件照");
    }
  }

  public void pickerImage() {
    MediaOptions.Builder builder = new MediaOptions.Builder();
    MediaOptions options =
        builder.setIsCropped(false).setFixAspectRatio(true).setImageSize(1).build();
    MediaPickerActivity.open(this, REQUEST_MEDIA, options, false);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      switch (requestCode) {
        case REQUEST_MEDIA:
          mMediaSelectedList.clear();
          mMediaSelectedList.addAll(MediaPickerActivity.getMediaItemSelected(data));
          if (mMediaSelectedList.size() > 0) {
            mUiViewCertification.setImageURI(mMediaSelectedList.get(0).getUriOrigin());
          } else {
            SnackBarUtil.showText(this, "请选择你的证件照");
          }
          break;
      }
    }
  }

  private void uploadPictureOss(MediaItem mediaItem, String location) {
    final OSS oss = mOSSUtils.oss;
    final String bucketName = BuildConfig.OSS_BUCKET;
    PutObjectRequest putObjectRequest =
        new PutObjectRequest(bucketName, buildObjectKey(mediaItem), location);
    final OSSAsyncTask task = oss.asyncPutObject(putObjectRequest,
        new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
          @Override
          public void onSuccess(PutObjectRequest object, PutObjectResult putObjectResult) {
            key = object.getObjectKey();
            PhotoService mPhotoService = ApiService.createPhotoService();
            Call<Certification> call = mPhotoService.userCertification(key);
            call.enqueue(new Callback<Certification>() {
              @Override
              public void onResponse(Call<Certification> call, Response<Certification> response) {
                //将url显示到里面
                if (response.isSuccessful()) {
                  //mUiViewCertification.setImageURI(response.body().getUri());
                  SnackBarUtil.showText(SoldierCertificationActivity.this, "上传成功");
                  finish();
                } else {
                  SnackBarUtil.showText(SoldierCertificationActivity.this, "上传图片错误");
                  Log.d("------>", response.code() + response.message());
                }
              }

              @Override public void onFailure(Call<Certification> call, Throwable t) {
                SnackBarUtil.showText(SoldierCertificationActivity.this, "上传图片错误");
                Log.d("------>", t.toString());
              }
            });
          }

          @Override
          public void onFailure(PutObjectRequest putObjectRequest, ClientException clientExcepion,
              ServiceException serviceException) {
            // 请求异常
            if (clientExcepion != null) {
              // 本地异常如网络异常等
              clientExcepion.printStackTrace();
            }
            if (serviceException != null) {
              // 服务异常

            }
          }
        });
  }

  //生成MD5
  private String buildObjectKey(MediaItem mediaItem) {
    return Md5Util.encode(mediaItem.getPathOrigin(this));
  }
}
