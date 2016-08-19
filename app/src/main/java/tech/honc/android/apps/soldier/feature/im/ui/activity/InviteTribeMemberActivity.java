package tech.honc.android.apps.soldier.feature.im.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeType;
import com.alibaba.mobileim.tribe.IYWTribeService;
import com.alibaba.mobileim.ui.contact.ContactsFragment;
import com.alibaba.mobileim.ui.contact.adapter.ContactsAdapter;
import java.util.List;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.feature.im.common.Notification;
import tech.honc.android.apps.soldier.feature.im.group.TribeConstants;
import tech.honc.android.apps.soldier.feature.im.helper.LoginHelper;
import tech.honc.android.apps.soldier.ui.activity.BaseActivity;

public class InviteTribeMemberActivity extends BaseActivity {

  private static final String TAG = "InviteTribeMemberActivity";

  private YWIMKit mIMKit;
  private IYWTribeService mTribeService;
  private long mTribeId;

  private ContactsFragment mFragment;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.im_activity_invite_tribe_member);

    mIMKit = LoginHelper.getInstance().getYWIMKit();
    mTribeService = mIMKit.getTribeService();
    mTribeId = getIntent().getLongExtra(TribeConstants.TRIBE_ID, 0);

    createFragment();
    YWLog.i(TAG, "onCreate");
  }

  public void clickFinish() {
    final YWTribeType tribeType = mTribeService.getTribe(mTribeId).getTribeType();
    ContactsAdapter adapter = mFragment.getContactsAdapter();
    List<IYWContact> list = adapter.getSelectedList();
    if (list != null && list.size() > 0) {
      mTribeService.inviteMembers(mTribeId, list, new IWxCallback() {
        @Override public void onSuccess(Object... result) {
          Integer retCode = (Integer) result[0];
          if (retCode == 0) {
            if (tribeType == YWTribeType.CHATTING_GROUP) {
              Notification.showToastMsg(InviteTribeMemberActivity.this, "添加群成员成功！");
            } else {
              Notification.showToastMsg(InviteTribeMemberActivity.this, "群邀请发送成功！");
            }
            finish();
          }
        }

        @Override public void onError(int code, String info) {
          Notification.showToastMsg(InviteTribeMemberActivity.this,
              "添加群成员失败，code = " + code + ", info = " + info);
        }

        @Override public void onProgress(int progress) {

        }
      });
    }
  }

  private void createFragment() {
    mFragment = mIMKit.getContactsFragment();
    Bundle bundle = new Bundle();
    bundle.putString(TribeConstants.TRIBE_OP, TribeConstants.TRIBE_INVITE);
    bundle.putLong(TribeConstants.TRIBE_ID, mTribeId);
    mFragment.setArguments(bundle);
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.contact_list_container, mFragment)
        .commit();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.im_menu_finish, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.menu_finish) {
      clickFinish();
    }
    return super.onOptionsItemSelected(item);
  }
}
