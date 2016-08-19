package tech.honc.android.apps.soldier.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.smartydroid.android.starter.kit.account.AccountManager;
import java.util.ArrayList;
import retrofit2.Call;
import support.ui.adapters.EasyRecyclerAdapter;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.AccountService;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.model.enums.RoleType;
import tech.honc.android.apps.soldier.ui.activity.CityPickerActivity;
import tech.honc.android.apps.soldier.ui.viewholder.HomeDetailViewHolder;

/**
 * Created by MrJiang on 4/14/2016.
 */
public class FemaleSoldierFragment extends BaseGridFragment<User> {

  private AccountService mAccountService;
  private String mCity;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mAccountService = ApiService.createAccountService();
  }

  @Override public Call<ArrayList<User>> paginate(User sinceItem, User maxItem, int perPage) {
    User user = AccountManager.getCurrentAccount();
    return mAccountService.recommend(RoleType.JUNSAO.toString(),
        maxItem != null && maxItem.id != 0 ? maxItem.id : 0,
        user != null && user.city != null ? user.city : "成都市");
  }

  @Override public Object getKeyForData(User item) {
    return item.id;
  }

  @Override public void bindViewHolders(EasyRecyclerAdapter adapter) {
    adapter.bind(User.class, HomeDetailViewHolder.class);
  }

  @Override public void onItemClick(int position, View view) {
    super.onItemClick(position, view);
      final User user = (User) getAdapter().get(position);
      if (user != null && user.id != null) {
        Navigator.startUserDetailActivty(getActivity(), user.id);
      }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    String mCity = data.getStringExtra(CityPickerActivity.CITY_VALUE);
    if (mCity != null) {
      User user = AccountManager.getCurrentAccount();
      if (user == null) {
        user = new User();
      }
      user.city = mCity;
      AccountManager.setCurrentAccount(user);
      AccountManager.notifyDataChanged();
      refresh();
    }
  }
}
