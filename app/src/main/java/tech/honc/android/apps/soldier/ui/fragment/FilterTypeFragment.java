package tech.honc.android.apps.soldier.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.FilterService;
import tech.honc.android.apps.soldier.model.Region;
import tech.honc.android.apps.soldier.model.Soldiers;
import tech.honc.android.apps.soldier.model.enums.RoleType;
import tech.honc.android.apps.soldier.ui.app.SoldierApp;
import tech.honc.android.apps.soldier.utils.data.SearchData;
import tech.honc.android.apps.soldier.utils.toolsutils.DialogUtil;
import tech.honc.android.apps.soldier.utils.toolsutils.SnackBarUtil;

/**
 * Created by MrJiang
 * on 2016/4/19.
 */
public class FilterTypeFragment extends BaseFragment
    implements MaterialDialog.ListCallback, DialogUtil.ItemsView {
  @Bind(R.id.filter_soldier_area) TextView mFilterSoldierArea;
  @Bind(R.id.filter_soldier_type) TextView mFilterSoldierType;
  @Bind(R.id.filter_soldier_age) TextView mFilterSoldierAge;
  private View mCurrentView;
  private String mIdentity;
  private ArrayList<Region> mRegionLists;
  private ArrayList<Soldiers> mSoldiersLists;
  private ArrayList<String> mRegionStrings;
  private ArrayList<String> mSoldiersStrings;
  private FilterService mFilterService;
  private NoticeFragmentChange mNoticeFragmentChange;
  private SearchData mSearchData;

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_filter_type;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mIdentity = getArguments().getString("identity");
    mRegionLists = new ArrayList<>();
    mSoldiersLists = new ArrayList<>();
    mRegionStrings = new ArrayList<>();
    mSoldiersStrings = new ArrayList<>();
    mFilterService = ApiService.createFilterService();
    mSearchData = getArguments().getParcelable("data");
    if (mSearchData == null) {
      mSearchData = new SearchData();
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = super.onCreateView(inflater, container, savedInstanceState);
    ButterKnife.bind(this, rootView);
    initView();
    isStartNetWork();
    return rootView;
  }

  public void isStartNetWork() {
    if (mIdentity.equals(RoleType.SOLDIER.toString())) {
      startNetworkRequest();
    }
  }

  private void startNetworkRequest() {
    Call<ArrayList<Region>> callRegion = mFilterService.searchRegion();
    callRegion.enqueue(new Callback<ArrayList<Region>>() {
      @Override
      public void onResponse(Call<ArrayList<Region>> call, Response<ArrayList<Region>> response) {
        if (response.isSuccessful()) {
          mRegionLists = response.body();
          for (Region r : mRegionLists) {
            mRegionStrings.add(r.name);
          }
        } else {
          SnackBarUtil.showText(getActivity(), "暂时还没有数据..");
        }
      }

      @Override public void onFailure(Call<ArrayList<Region>> call, Throwable t) {
        SnackBarUtil.showText(getActivity(), "暂时还没有数据..");
      }
    });
    Call<ArrayList<Soldiers>> callSoldier = mFilterService.searchSoldier();
    callSoldier.enqueue(new Callback<ArrayList<Soldiers>>() {
      @Override public void onResponse(Call<ArrayList<Soldiers>> call,
          Response<ArrayList<Soldiers>> response) {
        if (response.isSuccessful()) {
          mSoldiersLists = response.body();
          for (Soldiers s : mSoldiersLists) {
            mSoldiersStrings.add(s.name);
          }
        } else {
          SnackBarUtil.showText(getActivity(), "暂时还没有数据..");
        }
      }

      @Override public void onFailure(Call<ArrayList<Soldiers>> call, Throwable t) {
        SnackBarUtil.showText(getActivity(), "暂时还没有数据..");
      }
    });
  }

  private void initView() {
    if (mSearchData != null) {
      if (mIdentity.equals(RoleType.JUNSAO.toString())) {
        mFilterSoldierArea.setText(
            mSearchData != null && mSearchData.getAge() != null ? mSearchData.getAge() : "年龄");
        mFilterSoldierType.setText(
            mSearchData != null && mSearchData.getEducation() != null && !mSearchData.getEducation()
                .equals("") ? mSearchData.getEducation() : "学历");
        mFilterSoldierAge.setText(
            mSearchData != null && (mSearchData.getMarry() != null && !TextUtils.isEmpty(
                mSearchData.getMarry())) ? mSearchData.getMarry() : "婚姻状况");
      } else {
        mFilterSoldierArea.setText(
            mSearchData.getArmyArea() != null ? mSearchData.getArmyArea().name : "军区");
        mFilterSoldierType.setText(
            mSearchData.getArmyType() != null ? mSearchData.getArmyType().name : "兵种");
        mFilterSoldierAge.setText(
            mSearchData.getArmyYear() != null ? mSearchData.getArmyYear() : "兵龄");
      }
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @OnClick({
      R.id.filter_soldier_area, R.id.filter_soldier_type, R.id.filter_soldier_age
  }) public void onClick(View v) {
    DialogUtil.setItemsViewListener(this);
    if (mIdentity.equals(RoleType.SOLDIER.toString())) {
      switch (v.getId()) {
        case R.id.filter_soldier_area:
          DialogUtil.showFilterDialog(getActivity(), this, mRegionStrings, mFilterSoldierArea);
          break;
        case R.id.filter_soldier_type:
          DialogUtil.showFilterDialog(getActivity(), this, mSoldiersStrings, mFilterSoldierType);
          break;
        case R.id.filter_soldier_age:
          DialogUtil.showFilterDialog(getActivity(), this, R.array.soldier_age, mFilterSoldierAge);
          break;
      }
    } else {
      switch (v.getId()) {
        case R.id.filter_soldier_area:
          DialogUtil.showFilterDialog(getActivity(), this, R.array.age, mFilterSoldierArea);
          break;
        case R.id.filter_soldier_type:
          DialogUtil.showFilterDialog(getActivity(), this, R.array.grade, mFilterSoldierType);
          break;
        case R.id.filter_soldier_age:
          DialogUtil.showFilterDialog(getActivity(), this, R.array.marry, mFilterSoldierAge);
          break;
      }
    }
  }

  @Override
  public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
    if (mIdentity.equals(RoleType.SOLDIER.toString())) {
      switch (mCurrentView.getId()) {
        case R.id.filter_soldier_area:
          mFilterSoldierArea.setText(mRegionStrings.get(which));
          Region region = new Region();
          region.name = mRegionStrings.get(which);
          region.id = mRegionLists.get(which).id;
          mSearchData.setArmyArea(region);
          break;
        case R.id.filter_soldier_type:
          mFilterSoldierType.setText(mSoldiersStrings.get(which));
          Soldiers soldiers = new Soldiers();
          soldiers.name = mSoldiersStrings.get(which);
          soldiers.id = mSoldiersLists.get(which).id;
          mSearchData.setArmyType(soldiers);
          break;
        case R.id.filter_soldier_age:
          String[] age = SoldierApp.appResources().getStringArray(R.array.soldier_age);
          mFilterSoldierAge.setText(age[which]);
          mSearchData.setArmyYear(text.toString());
          break;
        default:
          break;
      }
      mNoticeFragmentChange.noticeChange(mSearchData);
    } else {
      switch (mCurrentView.getId()) {
        case R.id.filter_soldier_area:
          String[] age = SoldierApp.appResources().getStringArray(R.array.age);
          mFilterSoldierArea.setText(age[which]);
          mSearchData.setAge(age[which]);
          break;
        case R.id.filter_soldier_type:
          String[] educations = SoldierApp.appResources().getStringArray(R.array.grade);
          mFilterSoldierType.setText(educations[which]);
          mSearchData.setEducation(educations[which]);
          break;
        case R.id.filter_soldier_age:
          String[] marry = SoldierApp.appResources().getStringArray(R.array.marry);
          mFilterSoldierAge.setText(marry[which]);
          mSearchData.setMarry(marry[which]);
          break;
        default:
          break;
      }
      mNoticeFragmentChange.noticeChange(mSearchData);
    }
  }

  @Override public void sendItemView(View v) {
    mCurrentView = v;
  }

  public interface NoticeFragmentChange {
    void noticeChange(SearchData searchData);
  }

  public void setNoticeFragmentChange(NoticeFragmentChange noticeFragmentChange) {
    mNoticeFragmentChange = noticeFragmentChange;
  }

  @SuppressWarnings("deprecation") @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mNoticeFragmentChange = (NoticeFragmentChange) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString() + " must implement NoticeFragmentChange");
    }
  }

  @Override public void onDetach() {
    super.onDetach();
    mNoticeFragmentChange = null;
  }
}
