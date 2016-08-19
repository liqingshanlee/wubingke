package tech.honc.android.apps.soldier.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.ArrayList;
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
import tech.honc.android.apps.soldier.model.Profession;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.model.enums.RoleType;
import tech.honc.android.apps.soldier.utils.buildUtils.FilterBuildUtils;
import tech.honc.android.apps.soldier.utils.data.SearchData;
import tech.honc.android.apps.soldier.utils.toolsutils.DialogUtil;
import tech.honc.android.apps.soldier.utils.toolsutils.SnackBarUtil;

/**
 * Created by MrJiang on 4/15/2016.
 * 军嫂筛选
 */
public class FilterFemaleSoldierFragment extends BaseFragment
    implements SearchView.OnQueryTextListener, EasyViewHolder.OnItemClickListener,
    DialogUtil.ItemsTag, MaterialDialog.ListCallback {

  public static final int TAG_WEIGHT = 200;
  public static final int TAG_AGE = 300;
  public static final int TAG_HEIGHT = 400;
  public static final int TAG_WORK = 500;
  public static final int TAG_GRADE = 600;
  public static final int TAG_MARRY = 700;

  @Bind(R.id.search) SearchView mSearchView;
  @Bind(R.id.filter_recycler) RecyclerView mFilterRecycler;
  private EasyRecyclerAdapter mAdapter;
  private SearchService mSearchService;
  private int mTag;
  private SearchData mSearchData;
  private FilterService mFilterService;
  private ArrayList<Profession> mProfessions;
  private ArrayList<String> mProfessionsList;
  private String mUserName;

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_filter_soldier;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mFilterService = ApiService.createFilterService();
    mSearchService = ApiService.createSearchService();
    mProfessions = new ArrayList<>();
    mProfessionsList = new ArrayList<>();
    mSearchData = new SearchData();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = super.onCreateView(inflater, container, savedInstanceState);
    ButterKnife.bind(this, rootView);
    getProfession();
    setHasOptionsMenu(true);
    setUpSearchView();
    setupAdapter();
    return rootView;
  }

  public void startUpdate() {
    mAdapter.clear();
    mAdapter.appendAll(FilterBuildUtils.buildFemaleDatas(mSearchData));
    mAdapter.notifyDataSetChanged();
  }

  private void getProfession() {
    Call<ArrayList<Profession>> call = mFilterService.searchProfession();
    call.enqueue(new Callback<ArrayList<Profession>>() {
      @Override public void onResponse(Call<ArrayList<Profession>> call,
          Response<ArrayList<Profession>> response) {
        if (response.isSuccessful()) {
          mProfessions = response.body();
          for (Profession p : mProfessions) {
            mProfessionsList.add(p.name);
          }
        } else {
          SnackBarUtil.showText(getActivity(), "数据获取失败");
        }
      }

      @Override public void onFailure(Call<ArrayList<Profession>> call, Throwable t) {
        SnackBarUtil.showText(getActivity(), "服务器异常");
      }
    });
  }

  private void setUpSearchView() {
    mSearchView.setIconifiedByDefault(true);
    mSearchView.setSubmitButtonEnabled(false);
    mSearchView.setQueryHint(getText(R.string.input_username));
    mSearchView.onActionViewExpanded();
    mSearchView.setOnQueryTextListener(this);
    mSearchView.clearFocus();
  }

  private void setupAdapter() {
    mAdapter = new EasyRecyclerAdapter(getContext());
    mAdapter.viewHolderFactory(new CellsViewHolderFactory(getContext()));
    mAdapter.appendAll(FilterBuildUtils.buildFemaleDatas(mSearchData));
    mAdapter.setOnClickListener(this);
    mFilterRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    mFilterRecycler.setHasFixedSize(true);
    mFilterRecycler.setAdapter(mAdapter);
  }

  @Override public void onResume() {
    super.onResume();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override public void onItemClick(int position, View view) {
    CellModel cellModel = (CellModel) mAdapter.get(position);
    DialogUtil.setItemTagListener(this);
    switch (cellModel.tag) {
      case TAG_AGE:
        DialogUtil.showFilterDialog(getActivity(), this, R.array.age, TAG_AGE);
        break;
      case TAG_HEIGHT:
        DialogUtil.showFilterDialog(getActivity(), this, R.array.height, TAG_HEIGHT);
        break;
      case TAG_WEIGHT:
        DialogUtil.showFilterDialog(getActivity(), this, R.array.weight, TAG_WEIGHT);
        break;
      case TAG_WORK:
        DialogUtil.showFilterDialog(getActivity(), this, mProfessionsList, TAG_WORK);
        break;
      case TAG_GRADE:
        DialogUtil.showFilterDialog(getActivity(), this, R.array.grade, TAG_GRADE);
        break;
      case TAG_MARRY:
        DialogUtil.showFilterDialog(getActivity(), this, R.array.marry, TAG_MARRY);
        break;
    }
  }

  @Override public boolean onQueryTextSubmit(final String query) {
    submitSearchData(query);
    return false;
  }

  @Override public boolean onQueryTextChange(String newText) {
    mUserName = newText;
    return false;
  }

  public void submitSearchData(final String query) {
    showHud("正在努力查询...");
    Call<ArrayList<User>> call = mSearchService.filterByname(query, RoleType.JUNSAO.toString());
    call.enqueue(new Callback<ArrayList<User>>() {
      @Override
      public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
        if (response.isSuccessful()) {
          dismissHud();
          if (response.body().size() != 0) {
            Navigator.startFilterAgainActivity(getActivity(), RoleType.JUNSAO.toString(),
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

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.menu_ok) {
      if (mUserName != null && !TextUtils.isEmpty(mUserName)) {
        submitSearchData(mUserName);
      } else {
        sendSearch();
      }
    }
    return super.onOptionsItemSelected(item);
  }

  public void sendSearch() {
    Call<ArrayList<User>> call =
        mSearchService.filterJunSao(mSearchData.getAge(), mSearchData.getHeight(),
            mSearchData.getWeight(), FilterBuildUtils.getProfessions(mProfessions, mProfessionsList,
                mSearchData.getProfession() != null ? mSearchData.getProfession().name : null),
            mSearchData.getEducation(), FilterBuildUtils.getMarray(mSearchData.getMarry()), null);
    call.enqueue(new Callback<ArrayList<User>>() {
      @Override
      public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
        if (response.isSuccessful()) {
          if (response.body().size() != 0) {
            Navigator.startFilterAgainActivity(getActivity(), RoleType.JUNSAO.toString(),
                response.body(), mSearchData, null);
          } else {
            SnackBarUtil.showText(getActivity(), "没有数据哦");
          }
        } else {
          SnackBarUtil.showText(getActivity(), "没有数据哦");
        }
      }

      @Override public void onFailure(Call<ArrayList<User>> call, Throwable t) {
        SnackBarUtil.showText(getActivity(), "没有数据哦");
      }
    });
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_filter, menu);
  }

  @Override public void sendItemTag(int tag) {
    mTag = tag;
  }

  @Override
  public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
    switch (mTag) {
      case TAG_AGE:
        mSearchData.setAge(text.toString());
        break;
      case TAG_HEIGHT:
        mSearchData.setHeight(text.toString());
        break;
      case TAG_WEIGHT:
        mSearchData.setWeight(text.toString());
        break;
      case TAG_WORK:
        Profession profession = new Profession();
        profession.name = text.toString();
        profession.id =
            FilterBuildUtils.getProfessions(mProfessions, mProfessionsList, text.toString());
        mSearchData.setProfession(profession);
        break;
      case TAG_GRADE:
        mSearchData.setEducation(text.toString());
        break;
      case TAG_MARRY:
        mSearchData.setMarry(text.toString());
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
