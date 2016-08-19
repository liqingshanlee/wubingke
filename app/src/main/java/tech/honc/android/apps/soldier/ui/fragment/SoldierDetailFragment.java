package tech.honc.android.apps.soldier.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.facebook.drawee.view.SimpleDraweeView;
import com.smartydroid.android.starter.kit.account.AccountManager;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import support.ui.adapters.EasyViewHolder;
import support.ui.app.SupportApp;
import tech.honc.android.apps.soldier.BuildConfig;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.AccountService;
import tech.honc.android.apps.soldier.api.Service.AttentionService;
import tech.honc.android.apps.soldier.feature.im.helper.LoginHelper;
import tech.honc.android.apps.soldier.model.BeanCertification;
import tech.honc.android.apps.soldier.model.Image;
import tech.honc.android.apps.soldier.model.Status;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.model.enums.RoleType;
import tech.honc.android.apps.soldier.ui.adapter.DetailAdapter;
import tech.honc.android.apps.soldier.ui.widget.CertificationDialog;
import tech.honc.android.apps.soldier.utils.buildUtils.UserDetailBuildUtils;
import tech.honc.android.apps.soldier.utils.settings.SettingItems;
import tech.honc.android.apps.soldier.utils.toolsutils.LevelUtils;
import tech.honc.android.apps.soldier.utils.toolsutils.LoginDialog;

/**
 * Created by MrJiang on 4/17/2016.
 * soldier detail 军人详细资料页
 */
// TODO: 2016/6/20 认证功能待添加 
public class SoldierDetailFragment extends BaseFragment
    implements AppBarLayout.OnOffsetChangedListener, EasyViewHolder.OnItemClickListener {

  @Bind(R.id.toolbar_detail) Toolbar mToolbarDetail;
  @Bind(R.id.detail_simple) SimpleDraweeView mDetailSimple;
  @Bind(R.id.sex) ImageView mSex;
  @Bind(R.id.username) TextView mUsername;
  @Bind(R.id.soldier_area) TextView mSoldierArea;
  @Bind(R.id.area) TextView mArea;
  @Bind(R.id.image_degree) ImageView mImageDegree;
  @Bind(R.id.appbar_layout) AppBarLayout mAppbarLayout;
  @Bind(R.id.recycler_detail) RecyclerView mRecyclerDetail;
  @Bind(R.id.background) SimpleDraweeView mBackground;
  @Bind(R.id.collapsing_toolbar_layout) CollapsingToolbarLayout mCollapsingToolbarLayout;
  @Bind(R.id.dynamic_praise_num) TextView mAttentionsText;
  @Bind(R.id.dynamic_msg_num) TextView mChatTextView;
  @Bind(R.id.ui_view_image_level) ImageView mLevelImageView;
  private DetailAdapter mDetailAdapter;
  private AccountService mAccountService;
  private boolean isShow = false;
  private int scrollRange = -1;
  private User mUser;
  private AttentionService mAttentionService;
  private ArrayList<SettingItems> mItems;
  private Integer mUserId;
  private Bundle mBundle;

  public static BaseFragment create() {
    return new SoldierDetailFragment();
  }

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_detail_soldier;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mAccountService = ApiService.createAccountService();
    mAttentionService = ApiService.createAttentionService();
    mUser = new User();
    mItems = new ArrayList<>();
    initIntent();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = super.onCreateView(inflater, container, savedInstanceState);
    ButterKnife.bind(this, rootView);
    setUpView();
    mAppbarLayout.addOnOffsetChangedListener(this);
    setUpRecyclerView();
    return rootView;
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mBundle = saveState();
    outState.putInt("id", mUserId);
    mBundle = outState;
  }

  private void initIntent() {
    Intent intent = getActivity().getIntent();
    mUserId = intent.getIntExtra("userid", 0);
  }

  @Override public void onResume() {
    super.onResume();
    if (mUserId == null) {
      if (mBundle != null) {
        mUserId = mBundle.getInt("id");
        getUserDetail();
      }
    } else {
      getUserDetail();
    }
  }

  private void getUserDetail() {
    showHud("正在努力加载...");
    Call<User> call = mAccountService.getAccountDetail(mUserId);
    call.enqueue(new Callback<User>() {
      @Override public void onResponse(Call<User> call, Response<User> response) {
        if (response.isSuccessful()) {
          mUser = response.body();
          startUpdate();
        }
      }

      @Override public void onFailure(Call<User> call, Throwable t) {
      }
    });
  }

  public void startUpdate() {
    mItems.clear();
    setUserInformation(mUser);
    if (mUser.role.toString().equals(RoleType.JUNSAO.toString())) {
      mItems.addAll(UserDetailBuildUtils.BuildFemaleDatas(mUser));
    } else {
      mItems.addAll(UserDetailBuildUtils.BuildMaleDatas(mUser));
    }
    mDetailAdapter.notifyDataSetChanged();
  }

  public void setUserInformation(User user) {
    if (user != null && mChatTextView != null) {
      if (user.friend == 0) {
        mChatTextView.setText("加好友");
      } else {
        mChatTextView.setText("聊天");
      }
      if (user.role.toString().equals(RoleType.JUNSAO.toString())) {
        try {
          if (mSex != null) {
            mSex.setImageResource(R.mipmap.ic_green_woman);
          }
        } catch (NullPointerException e) {
          throw e;
        }
        if (user.blurryUri() != null) {
          mBackground.setImageURI(user.blurryUri());
        }
        mDetailSimple.setImageURI(user.uri());
        mSoldierArea.setText(
            (user.accountDetail != null && user.accountDetail.age != null && !TextUtils.isEmpty(
                user.accountDetail.age)) ? user.accountDetail.age + "岁" : "");
        mUsername.setText(user.nickname);
        mLevelImageView.setImageResource(LevelUtils.getWoManMipmapFromLevel(user.level));
        //设置地区
        mArea.setText(user.city);
        if (user.focus) {
          mAttentionsText.setText("已关注");
        } else {
          mAttentionsText.setText("关注");
        }
      } else {
        if (mSex != null) {
          mSex.setImageResource(R.mipmap.ic_sex_man);
        }
        if (user.blurryUri() != null) mBackground.setImageURI(user.blurryUri());
        mDetailSimple.setImageURI(user.uri() != null ? user.uri() : null);
        mUsername.setText(user.nickname);
        mSoldierArea.setText((user.accountDetail != null) ? user.accountDetail.age + "" : "");
        mLevelImageView.setImageResource(LevelUtils.getManMipmapFromLevel(user.level));
        //设置地区
        mArea.setText(user.city);
        if (user.focus) {
          mAttentionsText.setText("已关注");
        } else {
          mAttentionsText.setText("关注");
        }
      }
    }
    dismissHud();
  }

  private void setUpRecyclerView() {
    if (mUser.role == RoleType.JUNSAO) {
      mItems.addAll(UserDetailBuildUtils.BuildFemaleDatas(mUser));
      mDetailAdapter = new DetailAdapter(getContext(), mItems);
    } else {
      mItems.addAll(UserDetailBuildUtils.BuildMaleDatas(mUser));
      mDetailAdapter = new DetailAdapter(getContext(), mItems);
    }
    mDetailAdapter.setOnClickListener(this);
    mRecyclerDetail.setHasFixedSize(true);
    mRecyclerDetail.setLayoutManager(new LinearLayoutManager(getContext()));
    mRecyclerDetail.addItemDecoration(
        new HorizontalDividerItemDecoration.Builder(getContext()).size(1)
            .showLastDivider()
            .build());
    mRecyclerDetail.setAdapter(mDetailAdapter);
  }

  @SuppressLint("PrivateResource") private void setUpView() {
    setHasOptionsMenu(true);
    mCollapsingToolbarLayout.setTitle("");
    mToolbarDetail.setNavigationIcon(SupportApp.drawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
    mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
    mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
    mToolbarDetail.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getActivity().finish();
      }
    });
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
    if (scrollRange == -1) {
      scrollRange = appBarLayout.getTotalScrollRange();
    }
    if (scrollRange + verticalOffset == 0) {
      mCollapsingToolbarLayout.setTitle(mUser.nickname != null ? mUser.nickname : "详情");
      isShow = true;
    } else if (isShow) {
      mCollapsingToolbarLayout.setTitle("");
      isShow = false;
    }
  }

  @Override public void onItemClick(int position, View view) {
    switch (mDetailAdapter.getItemViewType(position)) {
      case SettingItems.VIEW_TYPE_DYNAMIC_CELL:
        if (mUser != null) {
          Navigator.startUserFeedActivity(getActivity(), mUser);
        }
        break;
      //case SettingItems.VIEW_TYPE_PHOTO_CELL:
      //  Navigator.startPersonalPhotoGalleryActivity(getActivity());
      //  break;
    }
  }

  @OnClick({ R.id.attentions, R.id.add_people, R.id.detail_simple, R.id.image_degree })
  public void onClick(View v) {
    User user = AccountManager.getCurrentAccount();
    switch (v.getId()) {
      case R.id.attentions:
        aboutAttention();
        break;
      case R.id.add_people:
        addFriend(user);
        break;
      case R.id.detail_simple:
        //查看大图
        ArrayList<Image> images = new ArrayList<>();
        Image image = new Image();
        image.url = mUser.avatar;
        images.add(image);
        Navigator.startPhotoBDActivity(getActivity(), images, 0);
        break;
      case R.id.image_degree:
        BeanCertification beanCertification = new BeanCertification();
        beanCertification.title = "服役地区";
        beanCertification.value = "北京军区";
        ArrayList<BeanCertification> list = new ArrayList<>();
        list.add(beanCertification);
        list.add(beanCertification);
        list.add(beanCertification);
        list.add(beanCertification);
        list.add(beanCertification);
        CertificationDialog dialog = CertificationDialog.newInstance(list);
        dialog.show(getChildFragmentManager(), "tag");
        break;
    }
  }

  public void aboutAttention() {
    if (!AccountManager.isLogin()) {
      LoginDialog dialog = LoginDialog.getInstance();
      dialog.init(getActivity());
      dialog.showDialog();
      return;
    }
    if (mUser.focus) {
      cancelAttentions();
    } else {
      addAttentions();
    }
  }

  public void addFriend(User user) {
    if (user == null) {
      LoginDialog dialog = LoginDialog.getInstance();
      dialog.init(getActivity());
      dialog.showDialog();
    } else {
      //跳转到加好友界面
      if (mUser != null && mUser.openImId != null) {
        if (mUser.friend == 0) {
          Navigator.startSendAddContactRequestActivity(getActivity(), mUser.openImId);
        } else {
          //跳转到聊天
          Intent intent = LoginHelper.getInstance()
              .getYWIMKit()
              .getChattingActivityIntent(mUser.openImId, BuildConfig.IM_APP_KEY);
          intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
          startActivity(intent);
        }
      }
    }
  }

  public void addAttentions() {
    Call<Status> call = mAttentionService.attentionUser(mUser.id);
    call.enqueue(new Callback<Status>() {
      @Override public void onResponse(Call<Status> call, Response<Status> response) {
        if (response.isSuccessful()) {
          getUserDetail();
        }
      }

      @Override public void onFailure(Call<Status> call, Throwable t) {
      }
    });
  }

  public void cancelAttentions() {
    Call<Status> call = mAttentionService.cancelAttention(mUser.id);
    call.enqueue(new Callback<Status>() {
      @Override public void onResponse(Call<Status> call, Response<Status> response) {
        if (response.isSuccessful()) {
          getUserDetail();
        }
      }

      @Override public void onFailure(Call<Status> call, Throwable t) {
      }
    });
  }

  public Bundle saveState() {
    Bundle bundle = new Bundle();
    bundle.putInt("id", mUserId);
    return bundle;
  }

  @Override public void onDestroy() {
    super.onDestroy();
  }
}
