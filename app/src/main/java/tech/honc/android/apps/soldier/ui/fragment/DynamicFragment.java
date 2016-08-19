package tech.honc.android.apps.soldier.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import support.ui.app.SupportApp;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.ui.adapter.DynamicFragmentViewPagerAdapter;
import tech.honc.android.apps.soldier.ui.widget.PublicDialog;
import tech.honc.android.apps.soldier.utils.toolsutils.LoginDialog;
import tech.honc.android.apps.soldier.utils.toolsutils.LoginNavigationsUtil;

/**
 * creat by lj at 2016/4/14
 * 发布帮助页面l
 */
public class DynamicFragment extends BaseFragment {
  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.frg_dynamic_tab) TabLayout mTabLayout;
  @Bind(R.id.frg_dynamic_viewpager) ViewPager mViewpager;

  private DynamicFragmentViewPagerAdapter mAdapter;

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_dynamic;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setUpToolbar();
    setUpTabLayout();
  }

  private void setUpToolbar() {
    TextView mTextView = new TextView(getContext());
    mTextView.setText(R.string.tab_dynamic);
    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
        getResources().getDimensionPixelSize(R.dimen.text_size_18));
    mTextView.setTextColor(SupportApp.color(R.color.white));
    Toolbar.LayoutParams paramsTextView =
        new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
    mToolbar.addView(mTextView, paramsTextView);
    mToolbar.inflateMenu(R.menu.menu_dynamic_release);
    mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
          case R.id.menu_dynamic:
            if (LoginNavigationsUtil.navigationActivity()==LoginNavigationsUtil.TAG_HAS_LOGIN) {
              showDialog();
            }else {
              LoginDialog dialog = LoginDialog.getInstance();
              dialog.init(getActivity());
              dialog.showDialog();
            }
            break;
        }
        return true;
      }
    });
  }

  private void setUpTabLayout() {
    mAdapter = new DynamicFragmentViewPagerAdapter(getChildFragmentManager());
    mViewpager.setAdapter(mAdapter);
    mTabLayout.setupWithViewPager(mViewpager);
    mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    mViewpager.setOffscreenPageLimit(3);
  }

  private void showDialog() {
    final PublicDialog dialog =
        new PublicDialog(getActivity(), R.style.AppTheme_PublicDialog, R.layout.fragment_dynamic_dialog);

    RelativeLayout rl = (RelativeLayout) dialog.findViewById(R.id.rl);
    Button btn1 = (Button) dialog.findViewById(R.id.btn1);
    Button btn2 = (Button) dialog.findViewById(R.id.btn2);
    dialog.show();

    rl.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dialog.dismiss();
      }
    });
    btn1.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dialog.dismiss();
        Navigator.startReleaseOrdinaryActivity(getActivity());
      }
    });
    btn2.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dialog.dismiss();
        Navigator.startReleaseHelpActivity(getActivity());
      }
    });
  }
}
