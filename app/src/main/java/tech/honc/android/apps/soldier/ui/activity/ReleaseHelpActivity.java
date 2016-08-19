package tech.honc.android.apps.soldier.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.smartydroid.android.starter.kit.utilities.Strings;
import java.util.ArrayList;
import mediapicker.MediaItem;
import mediapicker.MediaOptions;
import mediapicker.activities.MediaPickerActivity;
import retrofit2.Call;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.BuildConfig;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.FeedService;
import tech.honc.android.apps.soldier.model.Status;
import tech.honc.android.apps.soldier.oss.OSSUtils;
import tech.honc.android.apps.soldier.ui.widget.PublishFeedCollectionView;
import tech.honc.android.apps.soldier.utils.toolsutils.DialogUtil;
import tech.honc.android.apps.soldier.utils.toolsutils.Md5Util;
import tech.honc.android.apps.soldier.utils.toolsutils.SnackBarUtil;

/**
 * creat by lj on 2016/4/18
 * 发布帮助
 */
public class ReleaseHelpActivity extends BaseNetworkActivity<Status>
    implements EasyViewHolder.OnItemClickListener, DatePickerDialog.OnDateSetListener,
    DialogUtil.ItemsTag {
  public static final int REQUEST_CODE = 200;
  public static final int REQUEST_MEDIA = 100;
  public static final int TAG_STARTTIME = 1;
  public static final int TAG_ENDTIME = 2;
  private static final int REQUEST_CODE_PERMISSION = 1;

  String[] permissions =
      { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE };

  public String startTime = null;
  public String endTime = null;
  public String address = null;

  public double latitude;
  public double longitude;

  @Bind(R.id.edit_ReleaseHelper_content) EditText mContent;
  @Bind(R.id.publish_ReleaseHelper_img) PublishFeedCollectionView mRecyclerView;
  @Bind(R.id.release_help_address) TextView mReleaseHelpAddress;
  @Bind(R.id.release_help_start_time) TextView mReleaseHelpStartTime;
  @Bind(R.id.release_help_end_time) TextView mReleaseHelpEndTime;

  private int mTag;
  private ArrayList<MediaItem> mMediaSelectedList = new ArrayList<>();
  private ArrayList<String> mObjectKeys = new ArrayList<>();
  private ArrayList<OSSAsyncTask> ossAsyncTasks = new ArrayList<>();
  private OSSUtils mOSSUtils;
  private FeedService mFeedService;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mOSSUtils = new OSSUtils();
    setContentView(R.layout.activity_release_help);
    ButterKnife.bind(this);
    mRecyclerView.setOnClickListener(this);
    mFeedService = ApiService.createFeedService();
    notifyDataSetChanged();
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    notifyDataSetChanged();
  }

  private void notifyDataSetChanged() {
    mRecyclerView.addAll(mMediaSelectedList);
  }

  @OnClick({
      R.id.ReleaseHelper_SelectCity_bt, R.id.ReleaseHelper_startTime_bt,
      R.id.ReleaseHelper_endTime_bt
  }) public void onClick(View view) {
    DialogUtil.setItemTagListener(this);
    switch (view.getId()) {
      case R.id.ReleaseHelper_SelectCity_bt:
        Navigator.startMapActivity(ReleaseHelpActivity.this, REQUEST_CODE);
        break;
      case R.id.ReleaseHelper_startTime_bt:
        DialogUtil.showDatePicker(ReleaseHelpActivity.this, this, TAG_STARTTIME, false);
        break;
      case R.id.ReleaseHelper_endTime_bt:
        DialogUtil.showDatePicker(ReleaseHelpActivity.this, this, TAG_ENDTIME, false);
        break;
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      if (requestCode == REQUEST_MEDIA) {
        mMediaSelectedList.clear();
        mMediaSelectedList.addAll(MediaPickerActivity.getMediaItemSelected(data));
        notifyDataSetChanged();
      } else if (requestCode == REQUEST_CODE) {
        latitude = data.getDoubleExtra("latitude", 0);
        longitude = data.getDoubleExtra("longitude", 0);
        address = data.getStringExtra("address");
        mReleaseHelpAddress.setText(address != null && !TextUtils.isEmpty(address) ? address : "");
      }
    }
  }

  @Override public void onItemClick(int position, View view) {
    if (isAddButton(position)) {
      requestPermission();
    }
  }

  public boolean isAddButton(int position) {
    return mMediaSelectedList == null
        || mMediaSelectedList.size() <= 0
        || mMediaSelectedList.size() == position;
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    for (OSSAsyncTask ossAsyncTask : ossAsyncTasks) {
      if (!ossAsyncTask.isCompleted()) {
        ossAsyncTask.cancel();
      }
    }
  }

  @Override public void sendItemTag(int tag) {
    mTag = tag;
  }

  @Override public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
    int month = monthOfYear + 1;
    switch (mTag) {
      case TAG_STARTTIME:
        startTime = year + "-" + month + "-" + dayOfMonth + "";
        mReleaseHelpStartTime.setText(startTime);
        break;
      case TAG_ENDTIME:
        endTime = year + "-" + month + "-" + dayOfMonth + "";
        mReleaseHelpEndTime.setText(endTime);
        break;
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_dynamic_release_bt, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.menu_dynamic) {
      publish();
    } else if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return true;
  }

  private void publish() {
    hideSoftInputMethod();
    if (!validate()) {
      return;
    }
    showHud("正在努力上传中，请耐心等待..");
    if (mMediaSelectedList != null && mMediaSelectedList.size() > 0) {
      uploadToOss();
      return;
    }
    doPublish();
  }

  private boolean validate() {
    final String content = mContent.getText().toString();
    final int mMediaLen = mMediaSelectedList.size();

    if (Strings.isBlank(content) && mMediaLen <= 0) {
      SnackBarUtil.showText(this, "内容和图片不能同时为空");
      return false;
    }
    if (address == null) {
      SnackBarUtil.showText(this, "请选择预约地址");
      return false;
    }
    if (endTime == null) {
      SnackBarUtil.showText(this, "请选择预约时间");
      return false;
    }

    return true;
  }

  private void doPublish() {
    final String content = mContent.getText().toString();
    Call<Status> feedCall =
        mFeedService.releaseHelp(content, latitude, longitude, mObjectKeys, address, endTime);
    networkQueue().enqueue(feedCall);
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
              SnackBarUtil.showText(ReleaseHelpActivity.this, clientExcepion.getLocalizedMessage());
            }
            if (serviceException != null) {
              // 服务异常
              Log.e("ErrorCode", serviceException.getErrorCode());
              Log.e("RequestId", serviceException.getRequestId());
              Log.e("HostId", serviceException.getHostId());
              Log.e("RawMessage", serviceException.getRawMessage());
              SnackBarUtil.showText(ReleaseHelpActivity.this, serviceException.getRawMessage());
            }
          }
        });
    ossAsyncTasks.add(task);
  }

  private boolean shouldPublishToServer() {
    return mObjectKeys.size() == mMediaSelectedList.size();
  }

  @Override public void startRequest() {

  }

  @Override public void respondSuccess(Status data) {
    super.respondSuccess(data);
    dismissHud();
    finish();
  }

  @Override public void endRequest() {
    super.endRequest();
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode) {
      case REQUEST_CODE_PERMISSION: {
        // 如果请求被拒绝，那么通常grantResults数组为空
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          takePicture();
        } else {
          SnackBarUtil.showText(ReleaseHelpActivity.this, "你没有权限哦");
        }
      }
    }
  }

  private void takePicture() {
    MediaOptions.Builder builder = new MediaOptions.Builder();
    MediaOptions options = builder.canSelectMultiPhoto(true)
        .setMediaListSelected(mMediaSelectedList)
        .setImageSize(9)
        .build();
    MediaPickerActivity.open(this, REQUEST_MEDIA, options, false);
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
          ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION);
        }
      } else {
        takePicture();
      }
    } else {
      takePicture();
    }
  }
}
