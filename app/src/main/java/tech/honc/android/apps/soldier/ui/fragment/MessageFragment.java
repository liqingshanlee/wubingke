package tech.honc.android.apps.soldier.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeType;
import java.util.ArrayList;
import java.util.List;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.feature.im.group.TribeConstants;
import tech.honc.android.apps.soldier.feature.im.helper.LoginHelper;
import tech.honc.android.apps.soldier.feature.im.ui.activity.EditTribeInfoActivity;
import tech.honc.android.apps.soldier.feature.im.ui.activity.SearchAccountActivity;
import tech.honc.android.apps.soldier.feature.im.ui.fragment.ContactsFragment;
import tech.honc.android.apps.soldier.ui.adapter.MessageAdapter;

/**
 * MrJiang 消息
 */
public class MessageFragment extends Fragment {

  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.frg_message_tab) TabLayout mTablayout;
  @Bind(R.id.frg_message_viewpager) ViewPager mViewpager;
  private MessageAdapter mMessageAdapter;
  private TextView mTextView;
  private YWIMKit mYWIMKit;
  private PopupMenu mPopupMenu;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  private void getIMKit() {
    mYWIMKit = LoginHelper.getInstance().getYWIMKit();
    if (mYWIMKit == null) {
      return;
    }
  }

  private List<Fragment> initMsgFragment() {
    getIMKit();
    List<Fragment> list = new ArrayList<>();
    list.add(mYWIMKit.getConversationFragment());
    list.add(new ContactsFragment());
    return list;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_message, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
    setupToolbar();
  }

  @Override public void onResume() {
    super.onResume();
    mMessageAdapter = new MessageAdapter(getChildFragmentManager(), initMsgFragment());
    setUpTabLayout();
  }

  private void setupToolbar() {
    initPopMenu();
    mTextView = new TextView(getContext());
    mTextView.setText("消息");
    mTextView.setTextColor(Color.WHITE);
    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
        getResources().getDimensionPixelSize(R.dimen.text_size_18));
    Toolbar.LayoutParams params = new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
    mToolbar.addView(mTextView, params);
    mToolbar.inflateMenu(R.menu.menu_add_friends);
    mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        mPopupMenu.show();
        return true;
      }
    });
  }

  private void initPopMenu() {
    mPopupMenu = new PopupMenu(getActivity(), mToolbar);
    mPopupMenu.setGravity(Gravity.RIGHT);
    mPopupMenu.getMenuInflater().inflate(R.menu.menu_im, mPopupMenu.getMenu());
    mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
          case R.id.menu_search:
            startActivity(new Intent(getContext(), SearchAccountActivity.class));
            break;
          case R.id.menu_create_group:
            Intent intent1 = new Intent(getActivity(), EditTribeInfoActivity.class);
            intent1.putExtra(TribeConstants.TRIBE_OP, TribeConstants.TRIBE_CREATE);
            intent1.putExtra(TribeConstants.TRIBE_TYPE, YWTribeType.CHATTING_TRIBE.toString());
            startActivityForResult(intent1, 0);
            break;
          case R.id.menu_invite:
            Navigator.startInviteContactMembersActivity(getActivity());
            break;
        }
        return true;
      }
    });
  }

  private void setUpTabLayout() {
    mViewpager.setAdapter(mMessageAdapter);
    mTablayout.setupWithViewPager(mViewpager);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }
}
