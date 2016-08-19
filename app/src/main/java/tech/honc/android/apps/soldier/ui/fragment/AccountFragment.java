package tech.honc.android.apps.soldier.ui.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.smartydroid.android.starter.kit.account.AccountManager;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import java.util.ArrayList;
import java.util.List;
import mediapicker.MediaItem;
import mediapicker.MediaOptions;
import mediapicker.activities.MediaPickerActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import support.ui.adapters.EasyRecyclerAdapter;
import support.ui.adapters.EasyViewHolder;
import support.ui.cells.CellModel;
import support.ui.cells.CellsViewHolderFactory;
import tech.honc.android.apps.soldier.BuildConfig;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.AccountService;
import tech.honc.android.apps.soldier.api.Service.InviteCodeService;
import tech.honc.android.apps.soldier.api.Service.PhotoService;
import tech.honc.android.apps.soldier.model.AccountCenter;
import tech.honc.android.apps.soldier.model.Avatar;
import tech.honc.android.apps.soldier.model.InviteCode;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.oss.OSSUtils;
import tech.honc.android.apps.soldier.ui.app.SoldierApp;
import tech.honc.android.apps.soldier.ui.appInterface.AccoutHeaderOnClickListener;
import tech.honc.android.apps.soldier.ui.viewholder.AccountHeaderViewHolder;
import tech.honc.android.apps.soldier.utils.toolsutils.Md5Util;
import tech.honc.android.apps.soldier.utils.toolsutils.SnackBarUtil;

/**
 * 我的 MrJiang
 */
public class AccountFragment extends BaseFragment implements EasyViewHolder.OnItemClickListener {

  public static AccoutHeaderOnClickListener mAccoutHeaderOnClickListener;
  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(android.R.id.list) RecyclerView mRecyclerView;
  private EasyRecyclerAdapter mAdapter;
  private AccountService mAccountService;
  private List<MediaItem> mMediaSelectedList;
  private String key;
  private OSSUtils mOSSUtils;
  private InviteCodeService mInviteCodeService;
  private InviteCode mInviteCode;
  private static final int REQUEST_MEDIA = 100;
  private static final int REQUEST_CODE = 1;
  public static final int SELF_SIGNATURE = 1;
  public static final int PHOTO_ALBUM = 2;
  public static final int MY_DYNAMIC = 3;
  public static final int MY_COLLECT = 4;
  public static final int MY_HLEP = 5;
  public static final int INVITE_SOLDIER = 6;
  public static final int ACCOUNT_SETT = 7;
  public static final int SOLDIER_CERTIFICATION = 8;

  @TargetApi(Build.VERSION_CODES.M) public void requestPermission() {
    //判断当前Activity是否已经获得了该权限
    String[] permissions =
        { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE };
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
          != PackageManager.PERMISSION_GRANTED) {

        //如果App的权限申请曾经被用户拒绝过，就需要在这里跟用户做出解释
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
            Manifest.permission.CAMERA)) {
          SnackBarUtil.showText(getActivity(), "选择照片需要权限哦，请同意");
        } else {
          //进行权限请求
          ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_CODE);
        }
      } else {
        takePicture();
      }
    } else {
      takePicture();
    }
  }

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_account;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupAdapter();
    updateData();
    mOSSUtils = new OSSUtils();
    mMediaSelectedList = new ArrayList<>();
    mAccountService = ApiService.createAccountService();
    mInviteCodeService = ApiService.createInviteCodeService();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setupToolbar();
    setupRecyclerView();
    getProfession();
    getInviteCode();
  }

  private void setUpHeaderOnClickListener() {
    mAccoutHeaderOnClickListener = new AccoutHeaderOnClickListener() {
      @Override public void onImageOnClickListener(User user) {
        requestPermission();
      }

      @Override public void onNicknameOnClickListener(User user) {
        Navigator.startPersonageDataActivity(getActivity());
      }

      @Override public void onFansOnClickListener(User user) {
        Navigator.startFansActivity(getActivity());
      }

      @Override public void onFocusOnClickListener(User user) {
        Navigator.startFocusActivity(getActivity());
      }

      @Override public void onHelpOnClickListener(User user) {
        Navigator.startHelpActivity(getActivity());
      }
    };
  }

  public void getInviteCode() {
    Call<InviteCode> call = mInviteCodeService.getInviteCode();
    call.enqueue(new Callback<InviteCode>() {
      @Override public void onResponse(Call<InviteCode> call, Response<InviteCode> response) {
        if (response.isSuccessful()) {
          mInviteCode = response.body();
        }
      }

      @Override public void onFailure(Call<InviteCode> call, Throwable t) {
        Toast.makeText(getContext(), "获取邀请码失败", Toast.LENGTH_SHORT).show();
      }
    });
  }

  //获取个人中心
  private void getProfession() {
    Call<AccountCenter> call = mAccountService.getAccountCenter();
    call.enqueue(new Callback<AccountCenter>() {
      @Override public void onResponse(Call<AccountCenter> call, Response<AccountCenter> response) {
        if (response.isSuccessful()) {
          User mUser = AccountManager.getCurrentAccount();
          mUser.followers = response.body().followers;
          mUser.followings = response.body().followings;
          mUser.helps = response.body().helps;
          mUser.nickname = response.body().nickname;
          mUser.city = response.body().city;
          mUser.avatar = response.body().avatar;
          AccountManager.setCurrentAccount(mUser);
          AccountManager.notifyDataChanged();
        }
        updateData();
      }

      @Override public void onFailure(Call<AccountCenter> call, Throwable t) {
        updateData();
      }
    });
  }

  private ArrayList<CellModel> buildAccountData() {
    ArrayList<CellModel> cellModels = new ArrayList<>();
    User mUser = AccountManager.getCurrentAccount();
    cellModels.add(CellModel.shadowCell().build());
    cellModels.add(CellModel.settingCell(getString(R.string.account_signature))
        .tag(SELF_SIGNATURE)
        .value(mUser.signature != null ? mUser.signature : "")
        .showArrowRight(true)
        .build());

    cellModels.add(CellModel.shadowCell().build());
    cellModels.add(CellModel.textCell(getString(R.string.account_albums))
        .tag(PHOTO_ALBUM)
        .drawable(SoldierApp.drawable(R.mipmap.ic_account_photo))
        .showArrowRight(true)
        .needDivider(true)
        .build());
    cellModels.add(CellModel.textCell(getString(R.string.account_feeds))
        .tag(MY_DYNAMIC)
        .drawable(SoldierApp.drawable(R.mipmap.ic_account_dynamic))
        .showArrowRight(true)
        .needDivider(true)
        .build());
    cellModels.add(CellModel.textCell(getString(R.string.account_collections))
        .tag(MY_COLLECT)
        .drawable(SoldierApp.drawable(R.mipmap.ic_account_collection))
        .showArrowRight(true)
        .build());

    cellModels.add(CellModel.shadowCell().build());
    cellModels.add(CellModel.textCell(getString(R.string.account_helps))
        .tag(MY_HLEP)
        .drawable(SoldierApp.drawable(R.mipmap.ic_account_help))
        .showArrowRight(true)
        .needDivider(true)
        .build());
    cellModels.add(CellModel.textCell(getString(R.string.account_invite))
        .drawable(SoldierApp.drawable(R.mipmap.ic_account_invite_soldier))
        .showArrowRight(true)
        .needDivider(true)
        .tag(INVITE_SOLDIER)
        .build());
    cellModels.add(CellModel.textCell(getString(R.string.account_certification))
        .drawable(SoldierApp.drawable(R.mipmap.ic_account_certification))
        .valueDrawable(SoldierApp.drawable(R.mipmap.ic_authenticate))
        .detail("hahah")
        .showArrowRight(true)
        .tag(SOLDIER_CERTIFICATION)
        .build());
    cellModels.add(CellModel.shadowCell().build());
    cellModels.add(CellModel.textCell(getString(R.string.account_settings))
        .drawable(SoldierApp.drawable(R.mipmap.ic_account_setting))
        .showArrowRight(true)
        .tag(ACCOUNT_SETT)
        .build());
    return cellModels;
  }

  @Override public void onResume() {
    super.onResume();
    getProfession();
    setUpHeaderOnClickListener();
  }

  public void updateData() {
    mAdapter.clear();
    User user = AccountManager.getCurrentAccount();
    mAdapter.add(user);
    mAdapter.appendAll(buildAccountData());
    mAdapter.notifyDataSetChanged();
  }

  private void setupToolbar() {
    TextView mTextView = new TextView(getContext());
    mTextView.setText("我的");
    mTextView.setTextColor(Color.WHITE);
    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
        getResources().getDimensionPixelSize(R.dimen.text_size_18));
    Toolbar.LayoutParams params = new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
    mToolbar.addView(mTextView, params);
  }

  private void setupAdapter() {
    mAdapter = new EasyRecyclerAdapter(getContext());
    mAdapter.viewHolderFactory(new CellsViewHolderFactory(getContext()));
    mAdapter.bind(User.class, AccountHeaderViewHolder.class);
    mAdapter.setOnClickListener(this);
  }

  private void setupRecyclerView() {
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mRecyclerView.setAdapter(mAdapter);
  }

  @Override public void onItemClick(int position, View view) {
    if (mAdapter != null && mAdapter.get(position) != null && mAdapter.get(position)
        .getClass()
        .getSimpleName()
        .equals("CellModel")) {
      CellModel model = (CellModel) mAdapter.get(position);
      switch (model.tag) {
        case SELF_SIGNATURE:
          Navigator.startSignatureActivity(getActivity());
          break;
        case PHOTO_ALBUM:
          Navigator.startPersonalPhotoGalleryActivity(getActivity());
          break;
        case MY_DYNAMIC:
          User user = AccountManager.getCurrentAccount();
          Navigator.startMyFeedActivity(getActivity(), user);
          break;
        case MY_COLLECT:
          Navigator.startPersonalCollectionActivity(getActivity());
          break;
        case MY_HLEP:
          Navigator.startPersonalTaskActivity(getActivity());
          break;
        case INVITE_SOLDIER:
          openShareBoard();
          break;
        case SOLDIER_CERTIFICATION:
          Navigator.startSoldierCertificationActivity(getActivity());
          break;
        case ACCOUNT_SETT:
          Navigator.startSettingActivity(getActivity());
          break;
      }
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    mAccoutHeaderOnClickListener = null;
  }

  private void openShareBoard() {
    //.withMedia()
    UMShareListener mUMShareListener = new UMShareListener() {
      @Override public void onResult(SHARE_MEDIA share_media) {

      }

      @Override public void onError(SHARE_MEDIA share_media, Throwable throwable) {

      }

      @Override public void onCancel(SHARE_MEDIA share_media) {

      }
    };
    //SHARE_MEDIA.SINA,关掉新浪
    if (mInviteCode != null) {

      UMImage image = new UMImage(getContext(),
          getContext().getResources().getResourceEntryName(R.mipmap.ic_small_luncher));
      new ShareAction(getActivity()).setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
          SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
          .withText(mInviteCode.title)
          .withTitle(mInviteCode.title)
          .withMedia(image)
          .withTargetUrl(BuildConfig.API_ENDPOINT + mInviteCode.url)
          .setCallback(mUMShareListener)
          .open();
    }
  }

  private void takePicture() {
    MediaOptions.Builder builder = new MediaOptions.Builder();
    MediaOptions options =
        builder.setIsCropped(true).setFixAspectRatio(true).setImageSize(1).build();
    MediaPickerActivity.open(AccountFragment.this, REQUEST_MEDIA, options);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      switch (requestCode) {
        case REQUEST_MEDIA:
          mMediaSelectedList.clear();
          mMediaSelectedList.addAll(MediaPickerActivity.getMediaItemSelected(data));
          if (mMediaSelectedList != null) {
            for (MediaItem mediaItem : mMediaSelectedList) {
              uploadPictureOss(mediaItem, mediaItem.getPathCropped(getActivity()));
            }
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
            Call<Avatar> call = mPhotoService.updateAvatar(key);
            call.enqueue(new Callback<Avatar>() {
              @Override public void onResponse(Call<Avatar> call, Response<Avatar> response) {
                if (response.isSuccessful()) {
                  User mUser = AccountManager.getCurrentAccount();
                  mUser.avatar = response.body().message;
                  Log.d("------->", response.body().message);
                  AccountManager.setCurrentAccount(mUser);
                  AccountManager.notifyDataChanged();
                  mAdapter.notifyDataSetChanged();
                }
              }

              @Override public void onFailure(Call<Avatar> call, Throwable t) {
                SnackBarUtil.showText(getActivity(), "网络错误");
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
    return Md5Util.encode(mediaItem.getPathOrigin(getActivity()));
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
          SnackBarUtil.showText(getActivity(), "你没有权限哦");
        }
      }
    }
  }
}