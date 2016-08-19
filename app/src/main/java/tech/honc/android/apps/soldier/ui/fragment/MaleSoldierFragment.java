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
 * 首页-战友
 */
public class MaleSoldierFragment extends BaseGridFragment<User> {

  private AccountService mAccountService;
  private boolean mIsFirstIn;
  private String mCity;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mAccountService = ApiService.createAccountService();
    mIsFirstIn = true;
  }

  @Override public void bindViewHolders(EasyRecyclerAdapter adapter) {
    adapter.bind(User.class, HomeDetailViewHolder.class);
  }

  @Override public void onResume() {
    super.onResume();
    if (!mIsFirstIn) {
      onRefresh();
    }
    mIsFirstIn = false;
  }

  @Override public Object getKeyForData(User item) {
    return item.id != null ? item.id : 0;
  }

  @Override public void onItemClick(int position, View view) {
    super.onItemClick(position, view);
      final User mUser = (User) getAdapter().get(position);
      if (mUser != null && mUser.id != null) {
        Navigator.startUserDetailActivty(getActivity(), mUser.id);
      }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == CityPickerActivity.OPEN_CITY_PICKER) {
      User user = AccountManager.getCurrentAccount();
      mCity = data.getStringExtra(CityPickerActivity.CITY_VALUE);
      if (user != null) {
        user.city = mCity;
      } else {
        user = new User();
        user.city = mCity;
      }
      AccountManager.setCurrentAccount(user);
      AccountManager.notifyDataChanged();
      refresh();
    }
  }

  @Override public Call<ArrayList<User>> paginate(User sinceItem, User maxItem, int perPage) {
    User user = AccountManager.getCurrentAccount();
    return mAccountService.recommend(RoleType.SOLDIER.toString(),
        maxItem != null && maxItem.id != null ? maxItem.id : 0, mCity != null ? mCity : "成都市");
  }
}
