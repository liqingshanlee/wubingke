package tech.honc.android.apps.soldier.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;

/**
 * Created by MrJiang on 4/14/2016.
 * 聊天
 */
public class MessageAdapter extends FragmentPagerAdapter {

  private final static String TAB_TITLE[] = { "聊天", "联系人" };

  private List<Fragment> mFragments;

  public MessageAdapter(FragmentManager fm, List<Fragment> fragments) {
    super(fm);
    this.mFragments = fragments;
  }

  @Override public Fragment getItem(int position) {
    return mFragments.get(position);
  }

  @Override public int getCount() {
    return mFragments.size();
  }

  @Override public CharSequence getPageTitle(int position) {
    return TAB_TITLE[position];
  }
}
