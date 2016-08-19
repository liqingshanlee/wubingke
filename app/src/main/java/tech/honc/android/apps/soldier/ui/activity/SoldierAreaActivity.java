package tech.honc.android.apps.soldier.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import butterknife.Bind;
import java.util.ArrayList;
import java.util.Collections;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.CityService;
import tech.honc.android.apps.soldier.feature.im.widget.SideBar;
import tech.honc.android.apps.soldier.model.City;
import tech.honc.android.apps.soldier.ui.adapter.AreaPickerAdapter;
import tech.honc.android.apps.soldier.ui.appInterface.OnItemClickListener;
import tech.honc.android.apps.soldier.utils.toolsutils.CityComparator;
import tech.honc.android.apps.soldier.utils.toolsutils.CityCompartor;

/**
 * Created by Administrator on 2016/6/20.
 */
// TODO: 2016/6/24 服役地区
public class SoldierAreaActivity extends BaseActivity implements OnItemClickListener
{
  public static final String AREA_VALUE = "areaValue";
  @Bind(R.id.ui_view_city) RecyclerView mRecyclerView;
  @Bind(R.id.ui_view_sidebar) SideBar mSideBar;
  @Bind(R.id.ui_view_bubble) TextView mTextView;
  public AreaPickerAdapter mAreaPickerAdapter;
  private CityService mCityService;
  public ArrayList<City> mAllCities;
  private ArrayList<String> mCityArrayList;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_soldier_area);
    init();
    getCites();
    showAllArea();
  }

  private void init() {
    mAllCities = new ArrayList<>();
    mCityArrayList = new ArrayList<>();
    mCityService = ApiService.createCityService();
  }

  //获取城市列表
  private void getCites() {
    showHud("数据加载中...");
    Call<ArrayList<City>> call = mCityService.getCitys();
    call.enqueue(new Callback<ArrayList<City>>()
    {
      @Override
      public void onResponse(Call<ArrayList<City>> call, Response<ArrayList<City>> response) {
        if (response.isSuccessful()) {
          mAllCities = response.body();
          for (City r : mAllCities) {
            mCityArrayList.add(r.name);
          }
          Collections.sort(mCityArrayList, new CityCompartor());
          mAreaPickerAdapter.notifyDataSetChanged();
          mSideBar.setUpCharList(mAreaPickerAdapter.getLetter());
          Collections.sort(mAllCities, new CityComparator());
          dismissHud();
        } else {
          dismissHud();
        }
      }

      @Override public void onFailure(Call<ArrayList<City>> call, Throwable t) {
        dismissHud();
      }
    });
  }

  private void showAllArea() {
    mAreaPickerAdapter = new AreaPickerAdapter(this, mCityArrayList);
    LinearLayoutManager manager = new LinearLayoutManager(this);
    manager.setOrientation(LinearLayoutManager.VERTICAL);
    mRecyclerView.setLayoutManager(manager);
    mRecyclerView.setAdapter(mAreaPickerAdapter);
    mSideBar.setUpCharList(mAreaPickerAdapter.getLetter());
    mSideBar.setBubble(mTextView);
    mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener()
    {
      @Override public void onTouchingLetterChanged(String s) {
        int position = mAreaPickerAdapter.getPositionForSection(s.charAt(0));
        if (position != -1) {
          mRecyclerView.scrollToPosition(position);
        }
      }
    });
  }

  @Override public void onItemClick(String result) {
    Intent intent = getIntent();
    intent.putExtra(AREA_VALUE, result);
    setResult(RESULT_OK, intent);
    finish();
  }
}
