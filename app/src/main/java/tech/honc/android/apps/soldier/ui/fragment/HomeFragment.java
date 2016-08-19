package tech.honc.android.apps.soldier.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.smartydroid.android.starter.kit.account.AccountManager;
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import support.ui.adapters.EasyRecyclerAdapter;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.amap.AmapLocation;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.AccountService;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.ui.activity.CityPickerActivity;
import tech.honc.android.apps.soldier.ui.adapter.HomeFragmentAdapter;
import tech.honc.android.apps.soldier.ui.viewholder.ModelViewHolder;

/**
 * HomeFragment
 * MrJiang
 */
public class HomeFragment extends BaseFragment
    implements EasyViewHolder.OnItemClickListener, AMapLocationListener {

  public static final int REQUEST_CODE = 100;
  private static final int INITIAL_REQUEST = 1337;
  private static final String[] INITIAL_PERMS = {
      Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
  };
  @Bind(R.id.tab_layout) TabLayout mFragmentTab;
  @Bind(R.id.view_pager) ViewPager mFragmentViewpager;
  @Bind(R.id.recycler_view) RecyclerView mHomeRecycler;
  @Bind(R.id.text_city) TextView mCityTextView;

  private EasyRecyclerAdapter mAdapter;
  private AmapLocation mAmapLocations;

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_main;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (!canAccessLocation()) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
      }
    }
    mAmapLocations = new AmapLocation(getContext());
    mAmapLocations.initLocations(getContext(), this);
    mAmapLocations.startLocations();
  }

  private boolean canAccessLocation() {
    return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
  }

  private boolean hasPermission(String perm) {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (PackageManager.PERMISSION_GRANTED
        == getActivity().checkSelfPermission(perm));
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setupCityValue();
    setUpRecyclerView();
    getHotLine();
    setUpTabLayout();
  }

  //设置位置信息
  public void setupCityValue() {
    if (AccountManager.isLogin()) {
      User user = AccountManager.getCurrentAccount();
      mCityTextView.setText(user != null && user.city != null && !TextUtils.isEmpty(user.city) ? user.city : "成都市");
    } else {
      mCityTextView.setText("成都市");
    }
  }

  @Override public void onResume() {
    super.onResume();
  }

  private void getHotLine() {
    AccountService accountService = ApiService.createAccountService();
    Call<ArrayList<User>> call = accountService.hotLine();
    call.enqueue(new Callback<ArrayList<User>>() {
      @Override
      public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
        if (response.isSuccessful()) {
          mAdapter.addAll(response.body());
          mAdapter.notifyDataSetChanged();
        }
      }
      @Override public void onFailure(Call<ArrayList<User>> call, Throwable t) {
      }
    });
  }

  /**
   * 设置头部标兵
   */
  private void setUpRecyclerView() {
    mAdapter = new EasyRecyclerAdapter(getContext(), User.class, ModelViewHolder.class);
    mAdapter.setOnClickListener(this);
    mHomeRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    mHomeRecycler.addItemDecoration(new VerticalDividerItemDecoration.Builder(getContext())
        .size(20)
        .color(Color.TRANSPARENT)
        .build());
    mHomeRecycler.setAdapter(mAdapter);
  }

  private void setUpTabLayout() {
    HomeFragmentAdapter homeAdapter = new HomeFragmentAdapter(getChildFragmentManager());
    mFragmentViewpager.setAdapter(homeAdapter);
    mFragmentTab.setupWithViewPager(mFragmentViewpager);
    mFragmentTab.setTabMode(TabLayout.MODE_FIXED);
    mFragmentViewpager.setOffscreenPageLimit(2);
  }

  @OnClick({
      R.id.container_city,
      R.id.container_filter,
  }) public void onClick(View view) {
    if (view.getId() == R.id.container_city) {
      Intent intent = new Intent(getContext(), CityPickerActivity.class);
      intent.putExtra("requestCode", CityPickerActivity.OPEN_CITY_PICKER);
      startActivityForResult(intent, CityPickerActivity.OPEN_CITY_PICKER);
      return;
    }

    if (view.getId() == R.id.container_filter) {
      Navigator.startFilterActivity(getActivity());
    }
  }


  @Override public void onItemClick(int position, View view) {
    final User mUser = (User) mAdapter.get(position);
    Navigator.startUserDetailActivty(getActivity(), mUser.accountId);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = super.onCreateView(inflater, container, savedInstanceState);
    ButterKnife.bind(this, rootView);
    return rootView;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
    mAmapLocations.startLocations();
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (data != null) {
      List<Fragment> fragments = getChildFragmentManager().getFragments();
      if (fragments != null) {
        for (Fragment fragment : fragments) {
          fragment.onActivityResult(requestCode, resultCode, data);
        }
      }
      if (resultCode == Activity.RESULT_OK) {
        String city = data.getStringExtra(CityPickerActivity.CITY_VALUE);
        if (city != null) {
          mCityTextView.setText(city);
        }
      }
    }
  }

  @Override public void onLocationChanged(AMapLocation aMapLocation) {
    if (aMapLocation != null) {
      if (aMapLocation.getErrorCode() == 0) {
        if (aMapLocation.getCity() != null) {
          String mCity = aMapLocation.getCity();
          if (mCity != null) {
            mCityTextView.setText(mCity);
            User user = AccountManager.getCurrentAccount();
            user.city = mCity;
            AccountManager.setCurrentAccount(user);
            AccountManager.notifyDataChanged();
          }
        }
        mAmapLocations.stopLocations();
        mAmapLocations.destroyLocations();
      }
    }
  }

  @Override public void onDestroy() {
    super.onDestroy();
    mAmapLocations=null;
  }
}
