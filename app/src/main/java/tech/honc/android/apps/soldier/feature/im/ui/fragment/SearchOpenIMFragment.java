package tech.honc.android.apps.soldier.feature.im.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.smartydroid.android.starter.kit.account.AccountManager;
import java.util.ArrayList;
import retrofit2.Call;
import support.ui.adapters.EasyRecyclerAdapter;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.SearchService;
import tech.honc.android.apps.soldier.feature.im.model.IMProfile;
import tech.honc.android.apps.soldier.feature.im.ui.viewholder.IMProfileViewHolder;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.ui.fragment.BaseKeysFragment;

/**
 * Created by MrJiang on 2016/5/31.
 */
public class SearchOpenIMFragment extends BaseKeysFragment<IMProfile>
    implements SearchView.OnQueryTextListener {

  @Bind(R.id.im_toolbar) Toolbar mToolbar;
  @Bind(R.id.im_search_view) SearchView mSearchView;
  private SearchService mSearchService;
  private String mName = null;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mSearchService = ApiService.createSearchService();
  }

  private void initToolbar() {
    mSearchView.setIconifiedByDefault(true);
    mSearchView.setInputType(InputType.TYPE_CLASS_TEXT);
    mSearchView.onActionViewExpanded();
    mSearchView.requestFocus();
    mSearchView.setOnQueryTextListener(this);
    mSearchView.setQueryHint("请输入手机号或者用户名...");
    mToolbar.setTitle("搜索");
    mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    mToolbar.setNavigationContentDescription("返回");
    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getActivity().finish();
      }
    });
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initToolbar();
  }

  public static SearchOpenIMFragment newInstance() {
    return new SearchOpenIMFragment();
  }

  @Override
  public Call<ArrayList<IMProfile>> paginate(IMProfile sinceItem, IMProfile maxItem, int perPage) {
    return mSearchService.queryOpenIm(mName != null && !TextUtils.isEmpty(mName) ? mName : "11");
  }

  @Override public Object getKeyForData(IMProfile item) {
    return item.id != null && item.id != 0 ? item.id : 0;
  }

  @Override public void bindViewHolders(EasyRecyclerAdapter adapter) {
    adapter.bind(IMProfile.class, IMProfileViewHolder.class);
  }

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_im_search;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override public boolean onQueryTextSubmit(String query) {
    mName = query;
    refresh();
    return true;
  }

  @Override public boolean onQueryTextChange(String newText) {
    mName = newText;
    return true;
  }

  @Override public void onItemClick(int position, View view) {
    super.onItemClick(position, view);
    User user = AccountManager.getCurrentAccount();
    IMProfile im = (IMProfile) getAdapter().get(position);
    if (user.openIm.userId != im.open_im_id) {
      if (!im.firend) {
        Navigator.startSendAddContactRequestActivity(getActivity(), im.open_im_id);
      }
    } else {
      Toast.makeText(getContext(), "这是你自己哦", Toast.LENGTH_SHORT).show();
    }
  }
}
