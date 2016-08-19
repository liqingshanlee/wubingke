package tech.honc.android.apps.soldier.ui.fragment;

import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;
import retrofit2.Call;
import support.ui.adapters.EasyRecyclerAdapter;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.HelpService;
import tech.honc.android.apps.soldier.model.Helper;
import tech.honc.android.apps.soldier.ui.viewholder.HelperViewHolder;

/**
 * Created by MrJiang on 4/28/2016.
 * 获取助人列表
 */
public class PersonalHelperFragment extends BaseKeysFragment<Helper> {

  private HelpService mHelpService;

  public static PersonalHelperFragment create() {
    return new PersonalHelperFragment();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mHelpService = ApiService.createHelperService();
  }

  @Override public Call<ArrayList<Helper>> paginate(Helper sinceItem, Helper maxItem, int perPage) {
    return mHelpService.getHelperList();
  }

  @Override public Object getKeyForData(Helper item) {
    return item.id;
  }

  @Override public void bindViewHolders(EasyRecyclerAdapter adapter) {
    adapter.bind(Helper.class, HelperViewHolder.class);
  }

  @Override public void onItemClick(int position, View view) {
    super.onItemClick(position, view);
    final Helper feed = (Helper) getAdapter().get(position);
    Navigator.startTaskDynamicDetailsActivity(getActivity(), feed.id);
  }

  @Override public void onResume() {
    super.onResume();
  }
}
