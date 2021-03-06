package tech.honc.android.apps.soldier.feature.im.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.WxThreadHandler;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.fundamental.widget.YWAlertDialog;
import com.alibaba.mobileim.fundamental.widget.refreshlist.PullToRefreshListView;
import com.alibaba.mobileim.fundamental.widget.refreshlist.YWPullToRefreshBase;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeMember;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeType;
import com.alibaba.mobileim.gingko.presenter.contact.IContactProfileUpdateListener;
import com.alibaba.mobileim.gingko.presenter.contact.YWContactManagerImpl;
import com.alibaba.mobileim.gingko.presenter.tribe.IYWTribeChangeListener;
import com.alibaba.mobileim.tribe.IYWTribeService;
import com.alibaba.mobileim.utility.IMConstants;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.feature.im.common.Notification;
import tech.honc.android.apps.soldier.feature.im.group.TribeConstants;
import tech.honc.android.apps.soldier.feature.im.helper.LoginHelper;
import tech.honc.android.apps.soldier.feature.im.ui.adapter.TribeMembersAdapter;
import tech.honc.android.apps.soldier.ui.activity.BaseActivity;

public class TribeMembersActivity extends BaseActivity
    implements AdapterView.OnItemLongClickListener {

  private static final int REQUEST_CODE = 1;
  private YWIMKit mIMKit;
  private IYWTribeService mTribeService;
  private IYWTribeChangeListener mTribeChangedListener;

  private long mTribeId;
  private PullToRefreshListView mPullToRefreshListView;
  private ListView mListView;

  private List<YWTribeMember> mList = new ArrayList<YWTribeMember>();
  private YWTribeMember myself;
  private TribeMembersAdapter mAdapter;
  private TextView mAddTribeMembers;
  private EditText mUserId;
  private Handler mHandler = new Handler(Looper.getMainLooper());
  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.submit) TextView mSubmit;
  /**
   * 用于筛选需要处理的ProfileUpdate通知
   */
  private Set<String> mContactUserIdSet = new HashSet<String>();
  private IContactProfileUpdateListener mContactProfileUpdateListener;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.im_activity_tribe_members);
    init();
  }

  private void initToolbar() {
    mToolbar.setTitle("群资料");
    mToolbar.setTitleTextColor(Color.WHITE);
    setSupportActionBar(mToolbar);
    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
    mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    mSubmit.setClickable(true);
    mSubmit.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

        YWTribe tribe = mTribeService.getTribe(mTribeId);
        //群的普通成员没有加入权限，所以因此加入view
        if (tribe != null
            && tribe.getTribeType() == YWTribeType.CHATTING_TRIBE
            && getLoginUserRole() == YWTribeMember.ROLE_NORMAL) {
          Notification.showToastMsg(TribeMembersActivity.this, "您不是群管理员，没有管理权限~");
        } else {
          mAddTribeMembers.setVisibility(View.VISIBLE);
        }
      }
    });
  }

  private void init() {
    initToolbar();
    mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.tribe_members_list);
    assert mPullToRefreshListView != null;
    mPullToRefreshListView.setMode(YWPullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
    mPullToRefreshListView.setShowIndicator(false);
    mPullToRefreshListView.setDisableScrollingWhileRefreshing(false);
    mPullToRefreshListView.setRefreshingLabel("同步群成员列表");
    mPullToRefreshListView.setReleaseLabel("松开同步群成员列表");
    mPullToRefreshListView.setDisableRefresh(false);
    mPullToRefreshListView.setOnRefreshListener(new YWPullToRefreshBase.OnRefreshListener() {
      @Override public void onRefresh() {
        getTribeMembers();
      }
    });
    mListView = mPullToRefreshListView.getRefreshableView();
    mListView.setOnItemLongClickListener(this);

    mList = new ArrayList<YWTribeMember>();
    mAdapter = new TribeMembersAdapter(this, mList);
    mListView.setAdapter(mAdapter);

    Intent intent = getIntent();
    mTribeId = intent.getLongExtra(TribeConstants.TRIBE_ID, 0);
    mIMKit = LoginHelper.getInstance().getYWIMKit();
    mTribeService = mIMKit.getTribeService();

    getTribeMembers();

    mAddTribeMembers = (TextView) findViewById(R.id.add_tribe_members);
    mAddTribeMembers.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = new Intent(TribeMembersActivity.this, InviteTribeMemberActivity.class);
        intent.putExtra(TribeConstants.TRIBE_ID, mTribeId);
        startActivity(intent);
      }
    });
    mAddTribeMembers.setVisibility(View.GONE);
    initTribeChangedListener();
    initContactProfileUpdateListener();
    addListeners();
  }

  private void initContactProfileUpdateListener() {
    mContactProfileUpdateListener = new IContactProfileUpdateListener() {
      @Override public void onProfileUpdate(final String userid, String appkey) {
        if (mContactUserIdSet.contains(userid) && mAdapter != null) {
          WxThreadHandler.getInstance().getHandler().removeCallbacks(reindexRunnable);
          WxThreadHandler.getInstance()
              .getHandler()
              .postDelayed(reindexRunnable, IMConstants.UPDATE_SPELL_AND_INDEX_DELAY_MILLIS);
        }
      }

      @Override public void onProfileUpdate() {
        //just empty
      }
    };
  }

  private void doPreloadContactProfiles(final List<YWTribeMember> members) {
    int length = Math.min(members.size(), IMConstants.PRELOAD_PROFILE_NUM);
    ArrayList<IYWContact> contacts = new ArrayList<IYWContact>();

    for (int i = 0; i < length; i++) {
      YWTribeMember ywTribeMember = members.get(i);
      contacts.add(ywTribeMember);
    }
    if (mIMKit != null && mIMKit.getContactService() != null) {
      mIMKit.getContactService().getCrossContactProfileInfos(contacts);
    }
  }

  private Runnable reindexRunnable = new Runnable() {
    @Override public void run() {
      refreshAdapter();
    }
  };

  public void addListeners() {
    if (mIMKit != null && mIMKit.getContactService() != null) {
      ((YWContactManagerImpl) mIMKit.getContactService()).addProfileUpdateListener(
          mContactProfileUpdateListener);
    }
    mTribeService.addTribeListener(mTribeChangedListener);
  }

  public void removeListeners() {
    if (mIMKit != null && mIMKit.getContactService() != null) {
      ((YWContactManagerImpl) mIMKit.getContactService()).removeProfileUpdateListener(
          mContactProfileUpdateListener);
    }
    mTribeService.removeTribeListener(mTribeChangedListener);
  }

  private void getTribeMembers() {
    mTribeService.getMembers(new IWxCallback() {
      @Override public void onSuccess(Object... result) {
        onSuccessGetMembers((List<YWTribeMember>) result[0]);
        doPreloadContactProfiles((List<YWTribeMember>) result[0]);
        //同时触发一下向服务器更新列表
        //TODO 其实每次深登录后做一次查询就可以了
        getMembersFromServer();
      }

      @Override public void onError(int code, String info) {
        if (isFinishing()) {
          return;
        }
        Notification.showToastMsg(TribeMembersActivity.this,
            "error, code = " + code + ", info = " + info);
        mHandler.post(new Runnable() {
          @Override public void run() {
            mPullToRefreshListView.onRefreshComplete(false, false);
          }
        });
      }

      @Override public void onProgress(int progress) {

      }
    }, mTribeId);
  }

  private void getMembersFromServer() {
    mTribeService.getMembersFromServer(new IWxCallback() {
      @Override public void onSuccess(Object... result) {
        onSuccessGetMembers((List<YWTribeMember>) result[0]);
      }

      @Override public void onError(int code, String info) {

      }

      @Override public void onProgress(int progress) {

      }
    }, mTribeId);
  }

  private void onSuccessGetMembers(final List<YWTribeMember> members) {
    if (members == null || isFinishing()) {
      return;
    }
    mHandler.post(new Runnable() {
      @Override public void run() {
        mList.clear();
        mList.addAll(members);
        mContactUserIdSet.clear();
        for (YWTribeMember member : members) {
          mContactUserIdSet.add(member.getUserId());
        }
        refreshAdapter();
        mPullToRefreshListView.onRefreshComplete(false, true);
      }
    });
  }

  /**
   * 刷新当前列表
   */
  private void refreshAdapter() {
    mHandler.post(new Runnable() {
      @Override public void run() {
        if (mAdapter != null) {
          mAdapter.refreshData(mList);
        }
      }
    });
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    removeListeners();
  }

  @Override
  public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
    final int pos = position - mListView.getHeaderViewsCount();
    final YWTribeMember member = mList.get(pos);
    YWTribe tribe = mTribeService.getTribe(mTribeId);
    final String[] items = getItems(tribe, member);
    if (items == null) {
      return true;
    }
    new YWAlertDialog.Builder(this).setTitle("群成员管理")
        .setItems(items, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            if (items[which].equals(TribeConstants.TRIBE_SET_MANAGER)) {
              mTribeService.setTribeManager(mTribeId, member, null);
            } else if (items[which].equals(TribeConstants.TRIBE_CANCEL_MANAGER)) {
              mTribeService.cancelTribeManager(mTribeId, member, null);
            } else if (items[which].equals(TribeConstants.TRIBE_EXPEL_MEMBER)) {
              mTribeService.expelMember(mTribeId, member, new IWxCallback() {
                @Override public void onSuccess(Object... result) {
                  Notification.showToastMsg(TribeMembersActivity.this, "踢人成功！");
                  mList.remove(member);
                  refreshAdapter();
                }

                @Override public void onError(int code, String info) {

                }

                @Override public void onProgress(int progress) {

                }
              });
            }
          }
        })
        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
          }
        })
        .create()
        .show();
    return true;
  }

  /**
   * 判断当前登录用户在群组中的身份
   */
  private int getLoginUserRole() {
    int role = YWTribeMember.ROLE_NORMAL;
    String loginUser = mIMKit.getIMCore().getLoginUserId();
    for (YWTribeMember member : mList) {
      if (member.getUserId().equals(loginUser)) {
        myself = member;
        role = member.getTribeRole();
      }
    }
    return role;
  }

  private String[] getItems(YWTribe tribe, YWTribeMember member) {
    String[] items = null;
    if (tribe.getTribeType() == YWTribeType.CHATTING_TRIBE
        && getLoginUserRole() == YWTribeMember.ROLE_HOST) {
      if (member.getTribeRole() == YWTribeMember.ROLE_NORMAL) {
        items =
            new String[] { TribeConstants.TRIBE_SET_MANAGER, TribeConstants.TRIBE_EXPEL_MEMBER };
      } else if (member.getTribeRole() == YWTribeMember.ROLE_MANAGER) {
        items =
            new String[] { TribeConstants.TRIBE_CANCEL_MANAGER, TribeConstants.TRIBE_EXPEL_MEMBER };
      }
    } else if (tribe.getTribeType() == YWTribeType.CHATTING_GROUP
        && getLoginUserRole() == YWTribeMember.ROLE_HOST
        && member.getTribeRole() != YWTribeMember.ROLE_HOST) {
      items = new String[] { TribeConstants.TRIBE_EXPEL_MEMBER };
    }
    return items;
  }

  private void initTribeChangedListener() {
    mTribeChangedListener = new IYWTribeChangeListener() {
      @Override public void onInvite(YWTribe tribe, YWTribeMember user) {

      }

      @Override public void onUserJoin(YWTribe tribe, YWTribeMember user) {
        mList.add(user);
        refreshAdapter();
      }

      @Override public void onUserQuit(YWTribe tribe, YWTribeMember user) {
        if (user.equals(myself)) {
          mTribeService.clearTribeSystemMessages(tribe.getTribeId());
        }
        mList.remove(user);
        refreshAdapter();
      }

      @Override public void onUserRemoved(YWTribe tribe, YWTribeMember user) {
        //只有被踢出群的用户会收到该回调，即如果收到该回调表示自己被踢出群了
        mTribeService.clearTribeSystemMessages(tribe.getTribeId());
        openTribeListFragment();
      }

      @Override public void onTribeDestroyed(YWTribe tribe) {
        mTribeService.clearTribeSystemMessages(tribe.getTribeId());
        openTribeListFragment();
      }

      @Override public void onTribeInfoUpdated(YWTribe tribe) {

      }

      @Override public void onTribeManagerChanged(YWTribe tribe, YWTribeMember user) {
        for (YWTribeMember member : mList) {
          if (member.getUserId().equals(user.getUserId()) && member.getAppKey()
              .equals(user.getAppKey())) {
            mList.remove(member);
            mList.add(user);
            refreshAdapter();
            break;
          }
        }
      }

      @Override public void onTribeRoleChanged(YWTribe tribe, YWTribeMember user) {
        for (YWTribeMember member : mList) {
          if (member.getUserId().equals(user.getUserId()) && member.getAppKey()
              .equals(user.getAppKey())) {
            mList.remove(member);
            mList.add(user);
            refreshAdapter();
            break;
          }
        }
      }
    };
  }

  private void openTribeListFragment() {
    //Intent intent = new Intent(this, FragmentTabs.class);
    //intent.putExtra(TribeConstants.TRIBE_OP, TribeConstants.TRIBE_OP);
    //startActivity(intent);
    //finish();
  }

  private void inviteTribeMembers(List<IYWContact> contacts) {
    mTribeService.inviteMembers(mTribeId, contacts, new IWxCallback() {
      @Override public void onSuccess(Object... result) {
        Integer retCode = (Integer) result[0];
        if (retCode == 0) {
          YWTribe tribe = mTribeService.getTribe(mTribeId);
          if (tribe.getTribeType() == YWTribeType.CHATTING_GROUP) {
            Notification.showToastMsg(TribeMembersActivity.this, "添加群成员成功！");
          } else {
            Notification.showToastMsg(TribeMembersActivity.this, "群邀请发送成功！");
          }
          finish();
        }
      }

      @Override public void onError(int code, String info) {
        Notification.showToastMsg(TribeMembersActivity.this,
            "添加群成员失败，code = " + code + ", info = " + info);
      }

      @Override public void onProgress(int progress) {

      }
    });
  }
}
