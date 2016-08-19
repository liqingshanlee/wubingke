package tech.honc.android.apps.soldier.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import support.ui.adapters.EasyViewHolder;
import support.ui.app.SupportApp;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.AccountService;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.model.enums.LevelType;
import tech.honc.android.apps.soldier.model.enums.RoleType;
import tech.honc.android.apps.soldier.ui.adapter.DetailAdapter;
import tech.honc.android.apps.soldier.ui.app.SoldierApp;
import tech.honc.android.apps.soldier.utils.buildUtils.UserDetailBuildUtils;
import tech.honc.android.apps.soldier.utils.settings.SettingItems;
import tech.honc.android.apps.soldier.utils.toolsutils.LevelUtils;
import tech.honc.android.apps.soldier.utils.toolsutils.SnackBarUtil;

/**
 * Created by MrJiang on 2016/4/26.
 */
// TODO: 2016/4/29 界面 deprecated
public class PersonalDetailFragment extends BaseFragment
    implements AppBarLayout.OnOffsetChangedListener, EasyViewHolder.OnItemClickListener {

  @Bind(R.id.background) SimpleDraweeView mBackground;
  @Bind(R.id.toolbar_detail) Toolbar mToolbarDetail;
  @Bind(R.id.detail_simple) SimpleDraweeView mDetailSimple;
  @Bind(R.id.sex) ImageView mSex;
  @Bind(R.id.username) TextView mUsername;
  @Bind(R.id.container) LinearLayout container;
  @Bind(R.id.soldier_area) TextView mSoldierArea;
  @Bind(R.id.area) TextView mArea;
  @Bind(R.id.extra) LinearLayout extra;
  @Bind(R.id.image_degree) ImageView mImageDegree;
  @Bind(R.id.collapsing_toolbar_layout) CollapsingToolbarLayout mCollapsingToolbarLayout;
  @Bind(R.id.appbar_layout) AppBarLayout mAppbarLayout;
  @Bind(R.id.recycler_detail) RecyclerView mRecyclerDetail;
  private AccountService mAccountService;
  private DetailAdapter mDetailAdapter;
  private User mUser;
  private boolean isShow = false;
  private int scrollRange = -1;

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_detail_personal;
  }

  public static PersonalDetailFragment create() {
    return new PersonalDetailFragment();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mAccountService = ApiService.createAccountService();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = super.onCreateView(inflater, container, savedInstanceState);
    ButterKnife.bind(this, rootView);
    setUpView();
    mAppbarLayout.addOnOffsetChangedListener(this);
    getUserDetail();
    return rootView;
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

  private void getUserDetail() {
    Intent intent = getActivity().getIntent();
    int id = intent.getIntExtra("userid", 0);
    Call<User> call = mAccountService.getAccountDetail(id);
    call.enqueue(new Callback<User>() {
      @Override public void onResponse(Call<User> call, Response<User> response) {
        if (response.isSuccessful()) {
          mUser = response.body();
          setUserInformations(mUser);
          setUpRecyclerView(mUser);
        } else {
          SnackBarUtil.showText(getActivity(),
              SoldierApp.appResources().getString(R.string.prompt_fail));
        }
      }

      @Override public void onFailure(Call<User> call, Throwable t) {
        SnackBarUtil.showText(getActivity(), "查询信息错误");
      }
    });
  }

  public void setUserInformations(User user) {
    if (user != null) {
      if (user.role == RoleType.JUNSAO) {
        mSex.setImageResource(R.mipmap.ic_green_woman);
        mBackground.setImageURI(user.uri());
        mDetailSimple.setImageURI(user.uri());
        mSoldierArea.setText((user.accountDetail != null ? user.accountDetail.age + "岁" : ""));
        mUsername.setText(user.nickname);
        mImageDegree.setImageResource(R.mipmap.ic_authenticate);
        //设置地区
        mArea.setText(user.city);
      } else {
        mSex.setImageResource(R.mipmap.ic_sex_man);
        mBackground.setImageURI(user.uri());
        mDetailSimple.setImageURI(user.uri() != null ? user.uri() : null);
        mUsername.setText(user.nickname);
        mSoldierArea.setText((user.accountDetail != null) ? user.accountDetail.age + "" : "");
        mImageDegree.setImageResource(
            LevelUtils.getManMipmapFromLevel(user.level != null ? user.level : LevelType.ONE));
        //设置地区
        mArea.setText(user.city);
      }
    }
  }

  private void setUpView() {
    setHasOptionsMenu(true);
    mCollapsingToolbarLayout.setTitle("");
    mToolbarDetail.setNavigationIcon(SupportApp.drawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
    mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
    mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
    mToolbarDetail.inflateMenu(R.menu.menu_write);
    mToolbarDetail.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        Navigator.startPersonageDataActivity(getActivity());
        return true;
      }
    });
    mToolbarDetail.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getActivity().finish();
      }
    });
  }

  private void setUpRecyclerView(User user) {
    if (user.role == RoleType.JUNSAO) {
      mDetailAdapter = new DetailAdapter(getContext(), UserDetailBuildUtils.BuildFemaleDatas(user));
    } else {
      mDetailAdapter = new DetailAdapter(getContext(), UserDetailBuildUtils.BuildMaleDatas(user));
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

  @Override public void onItemClick(int position, View view) {
    switch (mDetailAdapter.getItemViewType(position)) {
      case SettingItems.VIEW_TYPE_DYNAMIC_CELL:
        if (mUser != null) {
          Navigator.startMyFeedActivity(getActivity(), mUser);
        }
        break;
    }
  }
}
