package tech.honc.android.apps.soldier.ui.fragment;

import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;
import retrofit2.Call;
import support.ui.adapters.EasyRecyclerAdapter;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.AttentionService;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.ui.viewholder.PersonalFocusViewHolder;

/**
 * Created by MrJiang on 2016/5/16.
 * 别人的粉丝
 */
public class OtherFansFragment extends BaseKeysFragment<User> {

  private AttentionService mAttentionService;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mAttentionService = ApiService.createAttentionService();
  }

  public static OtherFansFragment newInstance() {
    return new OtherFansFragment();
  }

  @Override public Call<ArrayList<User>> paginate(User sinceItem, User maxItem, int perPage) {
    return mAttentionService.otherFans(getArguments().getInt("id"));
  }

  @Override public Object getKeyForData(User item) {
    return item.id;
  }

  @Override public void bindViewHolders(EasyRecyclerAdapter adapter) {
    adapter.bind(User.class, PersonalFocusViewHolder.class);
  }

  @Override public void onItemClick(int position, View view) {
    super.onItemClick(position, view);
    final User user = (User) getAdapter().get(position);
    Navigator.startUserDetailActivty(getActivity(), user.accountId);
  }
}
