package tech.honc.android.apps.soldier.ui.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.ArrayList;
import java.util.Date;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import support.ui.adapters.EasyRecyclerAdapter;
import support.ui.adapters.EasyViewHolder;
import support.ui.cells.CellModel;
import support.ui.cells.CellsViewHolderFactory;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.FilterService;
import tech.honc.android.apps.soldier.api.Service.SearchService;
import tech.honc.android.apps.soldier.model.Region;
import tech.honc.android.apps.soldier.model.Soldiers;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.model.enums.RoleType;
import tech.honc.android.apps.soldier.utils.buildUtils.FilterBuildUtils;
import tech.honc.android.apps.soldier.utils.data.SearchData;
import tech.honc.android.apps.soldier.utils.toolsutils.DialogUtil;
import tech.honc.android.apps.soldier.utils.toolsutils.SnackBarUtil;

/**
 * Created by MrJiang on 4/15/2016.
 * 战友筛选
 */
public class FilterSoldierFragment extends BaseFragment
    implements SearchView.OnQueryTextListener, EasyViewHolder.OnItemClickListener,
    DialogUtil.ItemsTag, MaterialDialog.ListCallback, DatePickerDialog.OnDateSetListener {

  public static final String TAG = FilterSoldierFragment.class.getSimpleName();
  private String mUserName;

  public static final int TAG_SEX = 200;
  public static final int TAG_AGE = 300;
  public static final int TAG_HEIGHT = 400;
  public static final int TAG_WEIGHT = 500;
  public static final int TAG_SOLDIER_TYPE = 700;
  public static final int TAG_SOLDIER_AGE = 800;
  public static final int TAG_SOLDIER_AREA = 900;
  public static final int TAG_SOLDIER_COME = 1000;
  public static final int TAG_SOLDIER_GO = 1100;

  @Bind(R.id.search) SearchView mSearchView;
  @Bind(R.id.filter_recycler) RecyclerView mFilterRecycler;
  private EasyRecyclerAdapter mAdapter;
  private SearchService mSearchService;
  private FilterService mFilterService;
  private int mTag;
  private SearchData mSearchData;
  private ArrayList<Soldiers> mSoldierList;
  private ArrayList<Region> mRegionList;
  private ArrayList<String> mSoldierStrings;
  private ArrayList<String> mRegionStrings;
  private long mTimeCome;
  private long mTimeGo;

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_filter_soldier;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mSearchService = ApiService.createSearchService();
    mFilterService = ApiService.createFilterService();
    mSearchData = new SearchData();
    mSoldierList = new ArrayList<>();
    mRegionList = new ArrayList<>();
    mSoldierStrings = new ArrayList<>();
    mRegionStrings = new ArrayList<>();
    setHasOptionsMenu(true);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = super.onCreateView(inflater, container, savedInstanceState);
    ButterKnife.bind(this, rootView);
    getSearchData();
    setupAdapter();
    setUpSearchView();
    return rootView;
  }

  private void getSearchData() {
    Call<ArrayList<Soldiers>> callSoldier = mFilterService.searchSoldier();
    callSoldier.enqueue(new Callback<ArrayList<Soldiers>>() {
      @Override public void onResponse(Call<ArrayList<Soldiers>> call,
          Response<ArrayList<Soldiers>> response) {
        if (response.isSuccessful()) {
          mSoldierList = response.body();
          for (Soldiers s : mSoldierList) {
            mSoldierStrings.add(s.name);
          }
          startUpdate();
        }
      }

      @Override public void onFailure(Call<ArrayList<Soldiers>> call, Throwable t) {

      }
    });

    Call<ArrayList<Region>> callRegion = mFilterService.searchRegion();
    callRegion.enqueue(new Callback<ArrayList<Region>>() {
      @Override
      public void onResponse(Call<ArrayList<Region>> call, Response<ArrayList<Region>> response) {
        if (response.isSuccessful()) {
          if (response.isSuccessful()) {
            mRegionList = response.body();
            for (Region r : mRegionList) {
              mRegionStrings.add(r.name);
            }
          }
        }
      }

      @Override public void onFailure(Call<ArrayList<Region>> call, Throwable t) {

      }
    });
  }

  public void startUpdate() {
    mAdapter.clear();
    mAdapter.appendAll(FilterBuildUtils.buildMaleDatas(mSearchData));
    mAdapter.notifyDataSetChanged();
  }

  private void setUpSearchView() {
    mSearchView.setOnQueryTextListener(this);
    mSearchView.setIconifiedByDefault(true);
    mSearchView.setInputType(InputType.TYPE_CLASS_TEXT);
    mSearchView.setQueryHint("请输入姓名");
    mSearchView.onActionViewExpanded();
    mSearchView.clearFocus();
  }

  private void setupAdapter() {
    mAdapter = new EasyRecyclerAdapter(getContext());
    mAdapter.viewHolderFactory(new CellsViewHolderFactory(getContext()));
    mAdapter.appendAll(FilterBuildUtils.buildMaleDatas(mSearchData));
    mAdapter.setOnClickListener(this);
    mFilterRecycler.setHasFixedSize(true);
    mFilterRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    mFilterRecycler.setAdapter(mAdapter);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override public boolean onQueryTextSubmit(final String query) {
    submitSearchData(query);
    return false;
  }

  /**
   * 提交搜索信息
   */
  public void submitSearchData(final String query) {
    showHud("正在努力查询...");
    Call<ArrayList<User>> call = mSearchService.filterByname(query, RoleType.SOLDIER.toString());
    call.enqueue(new Callback<ArrayList<User>>() {
      @Override
      public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
        if (response.isSuccessful()) {
          dismissHud();
          if (response.body().size() != 0) {
            Navigator.startFilterAgainActivity(getActivity(), RoleType.SOLDIER.toString(),
                response.body(), mSearchData, query);
          } else {
            SnackBarUtil.showText(getActivity(), "没有数据哦");
          }
        } else {
          dismissHud();
          SnackBarUtil.showText(getActivity(), "没有数据哦");
        }
      }

      @Override public void onFailure(Call<ArrayList<User>> call, Throwable t) {
        dismissHud();
        SnackBarUtil.showText(getActivity(), "没有数据哦");
      }
    });
  }

  @Override public boolean onQueryTextChange(String newText) {
    mUserName = newText;
    return false;
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_filter, menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.menu_ok) {
      if (mUserName != null && !TextUtils.isEmpty(mUserName)) {
        submitSearchData(mUserName);
      } else {
        submitData();
      }
    }
    return super.onOptionsItemSelected(item);
  }

  public void submitData() {
    //判断两个值的大小
    if (mTimeGo>0&&mTimeCome>0&&mTimeGo > mTimeCome) {
      if (mSearchData != null) {
        showHud("正在努力查询...");
        Call<ArrayList<User>> call =
            mSearchService.filterSoldier(mSearchData.getAge() != null ? mSearchData.getAge() : "",
                mSearchData.getHeight(), mSearchData.getWeight(),
                FilterBuildUtils.getMarray(mSearchData.getMarry()), null,
                FilterBuildUtils.getSex(mSearchData.getSex()),
                FilterBuildUtils.getRegion(mRegionList, mRegionStrings,
                    mSearchData.getArmyArea() != null ? mSearchData.getArmyArea().name : null),
                null, FilterBuildUtils.getArmyType(mSoldierList, mSoldierStrings,
                    mSearchData.getArmyType() != null ? mSearchData.getArmyType().name : null),
                mSearchData.getStartTime(), mSearchData.getEndTime(),
                mSearchData.getArmyYear() != null ? mSearchData.getArmyYear()
                    .substring(0, mSearchData.getArmyYear().length() - 1) : "");
        call.enqueue(new Callback<ArrayList<User>>() {
          @Override
          public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
            if (response.isSuccessful()) {
              dismissHud();
              if (response.body().size() != 0) {
                Navigator.startFilterAgainActivity(getActivity(), RoleType.SOLDIER.toString(),
                    response.body(), mSearchData, null);
              } else {
                SnackBarUtil.showText(getActivity(), "没有数据哦");
              }
            } else {
              dismissHud();
              SnackBarUtil.showText(getActivity(), "没有数据哦");
            }
          }

          @Override public void onFailure(Call<ArrayList<User>> call, Throwable t) {
            dismissHud();
            SnackBarUtil.showText(getActivity(), "没有数据哦");
          }
        });
      } else {
        dismissHud();
        SnackBarUtil.showText(getActivity(), "你还没有任何选择");
      }
    } else {
      SnackBarUtil.showText(getActivity(), "入伍时间不能小于退伍时间");
    }
  }

  @Override public void onItemClick(int position, View view) {
    if (mAdapter != null && mAdapter.get(position) != null) {
      CellModel cellModel = (CellModel) mAdapter.get(position);
      DialogUtil.setItemTagListener(this);
      switch (cellModel.tag) {
        case TAG_SEX:
          DialogUtil.showFilterDialog(getActivity(), this, R.array.sex, TAG_SEX);
          break;
        case TAG_AGE:
          DialogUtil.showFilterDialog(getActivity(), this, R.array.age, TAG_AGE);
          break;
        case TAG_HEIGHT:
          DialogUtil.showFilterDialog(getActivity(), this, R.array.height, TAG_HEIGHT);
          break;
        case TAG_WEIGHT:
          DialogUtil.showFilterDialog(getActivity(), this, R.array.weight, TAG_WEIGHT);
          break;
        case TAG_SOLDIER_TYPE:
          DialogUtil.showFilterDialog(getActivity(), this, mSoldierStrings, TAG_SOLDIER_TYPE);
          break;
        case TAG_SOLDIER_AGE:
          DialogUtil.showFilterDialog(getActivity(), this, R.array.soldier_age, TAG_SOLDIER_AGE);
          break;
        case TAG_SOLDIER_AREA:
          DialogUtil.showFilterDialog(getActivity(), this, mRegionStrings, TAG_SOLDIER_AREA);
          break;
        case TAG_SOLDIER_COME:
          DialogUtil.showDatePicker(getActivity(), this, TAG_SOLDIER_COME);
          break;
        case TAG_SOLDIER_GO:
          DialogUtil.showDatePicker(getActivity(), this, TAG_SOLDIER_GO);
          break;
      }
    }
  }

  @Override
  public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
    if (mSearchData != null) {
      switch (mTag) {
        case TAG_SEX:
          mSearchData.setSex(text.toString());
          break;
        case TAG_AGE:
          mSearchData.setAge(text.toString());
          break;
        case TAG_HEIGHT:
          mSearchData.setHeight(text.toString());
          break;
        case TAG_WEIGHT:
          mSearchData.setWeight(text.toString());
          break;
        case TAG_SOLDIER_TYPE:
          Soldiers soldiers = new Soldiers();
          soldiers.name = text.toString();
          soldiers.id =
              FilterBuildUtils.getArmyType(mSoldierList, mSoldierStrings, text.toString());
          mSearchData.setArmyType(soldiers);
          break;
        case TAG_SOLDIER_AGE:
          mSearchData.setArmyYear(text.toString());
          break;
        case TAG_SOLDIER_AREA:
          Region region = new Region();
          region.name = text.toString();
          region.id = FilterBuildUtils.getRegion(mRegionList, mRegionStrings, text.toString());
          mSearchData.setArmyArea(region);
          break;
      }
      startUpdate();
    }
  }

  @Override public void sendItemTag(int tag) {
    mTag = tag;
  }

  @Override public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
    String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
    switch (mTag) {
      case TAG_SOLDIER_COME:
        Date dateCome = new Date(year, (monthOfYear + 1), dayOfMonth);
        mTimeCome = dateCome.getTime();
        mSearchData.setStartTime(date);
        break;
      case TAG_SOLDIER_GO:
        mSearchData.setEndTime(date);
        Date dateGo = new Date(year, (monthOfYear + 1), dayOfMonth);
        mTimeGo = dateGo.getTime();
        break;
    }
    startUpdate();
  }

  @Override public void onDestroy() {
    super.onDestroy();
    DialogUtil.mItemsView = null;
    DialogUtil.mItemsTag = null;
  }
}
