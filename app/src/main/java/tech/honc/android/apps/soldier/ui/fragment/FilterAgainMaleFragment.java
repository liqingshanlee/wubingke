package tech.honc.android.apps.soldier.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemSpanLookup;
import com.smartydroid.android.starter.kit.StarterKit;
import java.util.ArrayList;
import retrofit2.Call;
import support.ui.adapters.EasyRecyclerAdapter;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.SearchService;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.model.enums.RoleType;
import tech.honc.android.apps.soldier.ui.activity.FilterAgainActivity;
import tech.honc.android.apps.soldier.ui.viewholder.HomeDetailViewHolder;
import tech.honc.android.apps.soldier.utils.buildUtils.FilterBuildUtils;
import tech.honc.android.apps.soldier.utils.data.SearchData;

/**
 * Created by MrJiang on 4/16/2016.
 * dynamic filter
 */
public class FilterAgainMaleFragment extends BaseGridFragment<User>
    implements EasyViewHolder.OnItemClickListener, FilterAgainActivity.NoticeContentChange {

  private SearchService mSearchService;
  private SearchData mSearchData;
  private boolean mIsFirstEnter;
  private String mUserName;

  @Override public Paginate buildPaginate() {
    return Paginate.with(getRecyclerView(), this)
        .setLoadingTriggerThreshold(StarterKit.getLoadingTriggerThreshold())
        .addLoadingListItem(false)
        .setLoadingListItemSpanSizeLookup(new LoadingListItemSpanLookup() {
          @Override public int getSpanSize() {
            return 3;
          }
        })
        .build();
  }

  @Override public void bindViewHolders(EasyRecyclerAdapter adapter) {
    adapter.bind(User.class, HomeDetailViewHolder.class);
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mIsFirstEnter = true;
    mSearchService = ApiService.createSearchService();
    mSearchData = getArguments().getParcelable("data");
    mUserName = getArguments().getString("name");
    if (mSearchData == null) {
      mSearchData = new SearchData();
    }
  }

  public static FilterAgainMaleFragment create() {
    return new FilterAgainMaleFragment();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = super.onCreateView(inflater, container, savedInstanceState);
    ButterKnife.bind(this, rootView);
    return rootView;
  }

  @Override public void onResume() {
    super.onResume();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override public void onItemClick(int position, View view) {
    super.onItemClick(position, view);
    final User user = (User) getAdapter().get(position);
    if (user != null && user.id != null) {
      Navigator.startUserDetailActivty(getActivity(), user.id);
    }
  }

  @Override public void sendContent(SearchData searchData) {
    if (searchData != null) {
      showHud("正在努力查找..");
      mSearchData = searchData;
      refresh();
    }
  }

  @Override public Call<ArrayList<User>> paginate(User sinceItem, User maxItem, int perPage) {
    if (mIsFirstEnter && mUserName != null) {
      mIsFirstEnter = false;
      return mSearchService.filterByname(mUserName, RoleType.SOLDIER.toString());
    }
    assert mSearchData != null;
    return mSearchService.filterSoldier(mSearchData.getAge() != null ? mSearchData.getAge() : null,
        mSearchData.getHeight(), mSearchData.getWeight(),
        FilterBuildUtils.getMarray(mSearchData.getMarry()), null,
        FilterBuildUtils.getSex(mSearchData.getSex() != null ? mSearchData.getSex() : null),
        mSearchData.getArmyArea() != null ? mSearchData.getArmyArea().id : null, null,
        mSearchData.getArmyType() != null ? mSearchData.getArmyType().id : null,
        mSearchData.getStartTime(), mSearchData.getEndTime(),
        mSearchData.getArmyYear() != null ? mSearchData.getArmyYear()
            .substring(0, mSearchData.getArmyYear().length() - 1) : "");
  }

  @Override public Object getKeyForData(User item) {
    return item.id;
  }

  @Override public void endRequest() {
    super.endRequest();
    dismissHud();
  }
}
