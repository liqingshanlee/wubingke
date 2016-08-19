package tech.honc.android.apps.soldier.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartydroid.android.starter.kit.account.AccountManager;
import com.smartydroid.android.starter.kit.app.StarterNetworkActivity;
import com.smartydroid.android.starter.kit.utilities.Strings;
import java.util.ArrayList;
import mediapicker.MediaItem;
import mediapicker.MediaOptions;
import mediapicker.activities.MediaPickerActivity;
import retrofit2.Call;
import support.ui.app.SupportApp;
import tech.honc.android.apps.soldier.BuildConfig;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.AuthService;
import tech.honc.android.apps.soldier.feature.im.helper.LoginHelper;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.oss.OSSUtils;
import tech.honc.android.apps.soldier.utils.data.SettingsContainer;
import tech.honc.android.apps.soldier.utils.toolsutils.Md5Util;
import tech.honc.android.apps.soldier.utils.toolsutils.SnackBarUtil;

/**
 * Created by Administrator on 2016/4/19.
 */
public class ProfileRegisterActivity extends StarterNetworkActivity<User> {

  private static final int REQUEST_CODE = 1;
  private static final int REQUEST_MEDIA = 100;
  private static final int REQUEST_FILE = 200;
  String[] permissions =
      { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE };

  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.tv_data_soldier) TextView mSoldierTextView;
  @Bind(R.id.tv_data_man) TextView mManTextView;
  @Bind(R.id.tv_data_wumansoldier) TextView mwumanSoldierTextView;
  @Bind(R.id.tv_data_wumen) TextView mWumanTextView;
  @Bind(R.id.base_data_picture) SimpleDraweeView mImageView;
  @Bind(R.id.et_data_username) EditText mEditText;
  @Bind(R.id.tv_data_site) TextView mDataSiteTextView;
  @Bind(R.id.chk_register_agreement) AppCompatCheckBox mAppCompatCheckBox;

  private ArrayList<MediaItem> mMediaSelectedList = new ArrayList<>();
  private AuthService mAuthService;
  private SettingsContainer mSettingsContainer = new SettingsContainer();
  private OSSUtils mOSSUtils;
  private String item;
  private String key;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.base_data_info);
    ButterKnife.bind(this);
    initToolbar();
    mOSSUtils = new OSSUtils();
    mAuthService = ApiService.createAuthService();
  }

  private void initToolbar() {
    mToolbar.setNavigationIcon(R.mipmap.ic_back);
    mToolbar.setTitle("基本资料");
    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ProfileRegisterActivity.this.finish();
      }
    });
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
        takePicture();
      }
    } else {
      takePicture();
    }
  }

  @OnClick({
      R.id.tv_register_agreement, R.id.tv_data_soldier, R.id.tv_data_man, R.id.tv_data_wumansoldier,
      R.id.tv_data_wumen, R.id.base_data_picture, R.id.btn_data_ok, R.id.base_id
  }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.base_data_picture: {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          requestPermission();
        } else {
          takePicture();
        }
        break;
      }
      case R.id.tv_register_agreement: {
        Navigator.startagreementActivity(this);
        break;
      }

      case R.id.tv_data_soldier: {

        onclickSoldier();
        break;
      }
      case R.id.tv_data_man: {

        onclickMan();
        break;
      }

      case R.id.tv_data_wumansoldier: {

        onclickWumansoldier();
        break;
      }
      case R.id.tv_data_wumen: {
        onclickWumen();
        break;
      }
      case R.id.btn_data_ok: {
        doPostFile();
        break;
      }
      case R.id.base_id: {
        Intent intent = new Intent(this, CityPickerActivity.class);
        startActivityForResult(intent, CityPickerActivity.OPEN_CITY_PICKER);
        break;
      }
    }
  }

  private void onclickSoldier() {
    mSoldierTextView.setTextColor(SupportApp.color(R.color.colorPrimarygreen));
    mwumanSoldierTextView.setTextColor(SupportApp.color(R.color.colorTextview));
    mSettingsContainer.setRole("solider");
  }

  private void onclickMan() {
    TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mManTextView,
        R.mipmap.ic_green_man, 0, 0, 0);
    TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mWumanTextView,
        R.mipmap.ic_woman, 0, 0, 0);
    mwumanSoldierTextView.setTextColor(SupportApp.color(R.color.colorTextview));
    mSoldierTextView.setTextColor(SupportApp.color(R.color.colorPrimarygreen));
    mSettingsContainer.setGender("male");
    mSettingsContainer.setRole("solider");
  }

  private void onclickWumen() {
    TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mManTextView, R.mipmap.ic_man, 0,
        0, 0);
    TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mWumanTextView,
        R.mipmap.ic_green_woman, 0, 0, 0);
    mSettingsContainer.setGender("female");
  }

  private void onclickWumansoldier() {
    TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mManTextView, R.mipmap.ic_man, 0,
        0, 0);
    TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mWumanTextView,
        R.mipmap.ic_green_woman, 0, 0, 0);
    mwumanSoldierTextView.setTextColor(SupportApp.color(R.color.colorPrimarygreen));
    mSoldierTextView.setTextColor(SupportApp.color(R.color.colorTextview));
    mSettingsContainer.setGender("female");
    mSettingsContainer.setRole("junsao");
  }

  private void takePicture() {
    MediaOptions.Builder builder = new MediaOptions.Builder();
    MediaOptions options =
        builder.setIsCropped(true).setFixAspectRatio(true).setImageSize(1).build();
    MediaPickerActivity.open(this, REQUEST_MEDIA, options, false);
  }

  private void addImages(MediaItem mediaItem) {
    if (mediaItem.getUriCropped() == null) {
      ImageLoader.getInstance().displayImage(mediaItem.getUriOrigin().toString(), mImageView);
    } else {
      ImageLoader.getInstance().displayImage(mediaItem.getUriCropped().toString(), mImageView);
    }
  }

  //提交信息
  private void doPostFile() {
    final String userName = mEditText.getText().toString();
    final String role = mSettingsContainer.getRole();
    final String gender = mSettingsContainer.getGender();
    if (mMediaSelectedList != null) {
      if (mMediaSelectedList.size() != 1 ||key==null|| key.length() == 0 || TextUtils.isEmpty(key)) {
        Toast.makeText(this, "请选择你的头像", Toast.LENGTH_SHORT).show();
        return;
      }
      if (userName.length() > 6 || userName.length() == 0) {
        Toast.makeText(this, "昵称长度不能大于6", Toast.LENGTH_SHORT).show();
        return;
      }
      if (mMediaSelectedList.size() != 1 && userName.length() > 6 || userName.length() == 0) {
        Toast.makeText(this, "请选择你的头像", Toast.LENGTH_SHORT).show();
        return;
      }
      if (Strings.isBlank(role) || Strings.isBlank(gender)) {
        Toast.makeText(this, "请完善身份和性别", Toast.LENGTH_SHORT).show();
        return;
      }

      if (!mAppCompatCheckBox.isChecked()) {
        Toast.makeText(this, "请勾选用户协议", Toast.LENGTH_SHORT).show();
        return;
      }
      if (Strings.isBlank(item)) {
        Toast.makeText(this, "请选择城市", Toast.LENGTH_SHORT).show();
        return;
      }
      User mUser = AccountManager.getCurrentAccount();
      Call<User> userCall = mAuthService.postFile(key, userName, gender, role, item, mUser.id);
      networkQueue().enqueue(userCall);
    }
  }

  private void loginOpenIm(User user) {
    if (LoginHelper.getInstance().getYWIMKit() == null) {
      LoginHelper.getInstance().initIMKit(user.openIm.userId, BuildConfig.IM_APP_KEY);
    }
    LoginHelper.getInstance().logIn(user.openIm.userId, user.openIm.password, new IWxCallback() {
      @Override public void onSuccess(Object... objects) {
        dismissHud();
        Navigator.startMainActivity(ProfileRegisterActivity.this);
        finish();
      }

      @Override public void onError(int i, String s) {
        dismissHud();
        Toast.makeText(ProfileRegisterActivity.this, "登录错误,错误码:" + i, Toast.LENGTH_SHORT).show();
      }

      @Override public void onProgress(int i) {

      }
    });
  }

  @Override public void startRequest() {
    showHud("注册中");
  }

  @Override public void respondSuccess(User data) {
    super.respondSuccess(data);
    AccountManager.setCurrentAccount(data);
    AccountManager.notifyDataChanged();
    loginOpenIm(data);
  }

  @Override public void endRequest() {
  }
  //上传图片

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
            //mImageView.setImageURI(Uri.parse(BuildConfig.OSS_IMAGE_ENDPOINT + key));
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
    return Md5Util.encode(mediaItem.getPathCropped(this));
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode) {
      case REQUEST_CODE: {
        // 如果请求被拒绝，那么通常grantResults数组为空
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          takePicture();
        } else {
          SnackBarUtil.showText(ProfileRegisterActivity.this, "你没有权限哦");
        }
      }
    }
  }

  @Override public void respondWithError(Throwable t) {
    dismissHud();
    Toast.makeText(this, "注册失败,错误信息:" + t.getMessage(), Toast.LENGTH_SHORT).show();
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      switch (requestCode) {
        case REQUEST_MEDIA:
          mMediaSelectedList.clear();
          mMediaSelectedList.addAll(MediaPickerActivity.getMediaItemSelected(data));
          if (mMediaSelectedList != null) {
            for (MediaItem mediaItem : mMediaSelectedList) {
              uploadPictureOss(mediaItem, mediaItem.getPathOrigin(this));
              addImages(mediaItem);
            }
          }
          break;

        case REQUEST_FILE:
          item = data.getStringExtra("city");
          mDataSiteTextView.setText(item);
      }
    }
  }
}
