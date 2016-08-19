package tech.honc.android.apps.soldier.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import tech.honc.android.apps.soldier.ui.fragment.CityFragment;
import tech.honc.android.apps.soldier.ui.fragment.FriendsFragment;
import tech.honc.android.apps.soldier.ui.fragment.TaskFragment;

/**
 * Created by Administrator on 2016/4/14.
 */
public class DynamicFragmentViewPagerAdapter extends FragmentPagerAdapter {

  private final static String TAB_TITLE[] = { "任务", "同城", "好友" };
  private Fragment fragment1;
  private Fragment fragment2;
  private Fragment fragment3;

  public DynamicFragmentViewPagerAdapter(FragmentManager fm) {
    super(fm);
    fragment1 = TaskFragment.newInstance();
    fragment2 = new CityFragment();
    fragment3 = new FriendsFragment();
  }

  @Override public Fragment getItem(int position) {
    Fragment fragment = null;
    switch (position) {
      case 0:
        fragment = fragment1;
        break;
      case 1:
        fragment = fragment2;
        break;
      case 2:
        fragment = fragment3;
        break;
    }

    return fragment;
  }

  @Override public int getCount() {
    return TAB_TITLE.length;
  }

  @Override public CharSequence getPageTitle(int position) {
    return TAB_TITLE[position];
  }
}
