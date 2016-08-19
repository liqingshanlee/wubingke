package tech.honc.android.apps.soldier.feature.im.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.constant.YWProfileSettingsConstants;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.util.WxLog;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.IYWContactService;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.gingko.model.settings.YWTribeSettingsModel;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeMember;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeRole;
import com.alibaba.mobileim.gingko.presenter.tribe.IYWTribeChangeListener;
import com.alibaba.mobileim.tribe.IYWTribeService;
import com.alibaba.mobileim.utility.IMNotificationUtils;
import java.util.ArrayList;
import java.util.List;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.feature.im.common.Notification;
import tech.honc.android.apps.soldier.feature.im.group.TribeConstants;
import tech.honc.android.apps.soldier.feature.im.helper.LoginHelper;
import tech.honc.android.apps.soldier.ui.activity.BaseActivity;

/**
 *
 */
public class TribeInfoActivity extends BaseActivity {

  private static final String TAG = "TribeInfoActivity";
  private static final int SET_MSG_REC_TYPE_REQUEST_CODE = 10000;
  private static final int EDIT_MY_TRIBE_NICK_REQUEST_CODE = 10001;
  private YWIMKit mIMKit;
  private IYWTribeService mTribeService;
  private YWTribe mTribe;
  private long mTribeId;
  private String mTribeOp;
  private int mTribeMemberCount;
  List<YWTribeMember> mList = new ArrayList<YWTribeMember>();

  private IYWTribeChangeListener mTribeChangedListener;

  private Handler mHandler = new Handler(Looper.getMainLooper());

  private TextView mTribeName;
  private TextView mTribeDesc;
  private TextView mMemberCount;
  private TextView mQuiteTribe;
  private TextView mMangeTribeMembers;
  private TextView mMyTribeNick;
  private RelativeLayout mMangeTribeMembersLayout;
  private RelativeLayout mEditTribeInfoLayout;
  private RelativeLayout mEditMyTribeProfileLayout;
  private RelativeLayout mEditPersonalSettings;

  private RelativeLayout mTribeMsgRecTypeLayout;
  private TextView mTribeMsgRecType;
  private SwitchCompat mAtMsgRecSwitch;

  private TextView mCLearTribeMsgs;

  private int mMsgRecFlag = YWProfileSettingsConstants.TRIBE_MSG_REJ;
  private int mAtMsgRecFlag = YWProfileSettingsConstants.TRIBE_AT_MSG_REC;

  private Handler uiHandler = new Handler(Looper.getMainLooper());

  private IYWConversationService conversationService;
  private YWConversation conversation;

  private YWTribeMember mLoginUser;

  @Bind(R.id.toolbar) Toolbar mToolbar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.im_activity_tribe_info);

    Intent intent = getIntent();
    mTribeId = intent.getLongExtra(TribeConstants.TRIBE_ID, 0);
    mTribeOp = intent.getStringExtra(TribeConstants.TRIBE_OP);

    mIMKit = LoginHelper.getInstance().getYWIMKit();

    conversationService = mIMKit.getConversationService();
    conversation = conversationService.getTribeConversation(mTribeId);

    mTribeService = mIMKit.getTribeService();

    initTribeChangedListener();
    initTribeInfo();
    initView();
    getTribeMsgRecSettings();
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
  }

  @SuppressLint("SetTextI18n") private void initView() {
    initToolbar();
    mTribeName = (TextView) findViewById(R.id.tribe_name);
    TextView tribeId = (TextView) findViewById(R.id.tribe_id);
    if (tribeId != null) {
      tribeId.setText("群号 " + mTribeId);
    }


    mTribeDesc = (TextView) findViewById(R.id.tribe_description);
    mMemberCount = (TextView) findViewById(R.id.member_count);

    mMangeTribeMembers = (TextView) findViewById(R.id.manage_tribe_members);
    mMangeTribeMembersLayout = (RelativeLayout) findViewById(R.id.manage_tribe_members_layout);
    mMangeTribeMembersLayout.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = new Intent(TribeInfoActivity.this, TribeMembersActivity.class);
        intent.putExtra(TribeConstants.TRIBE_ID, mTribeId);
        startActivity(intent);
      }
    });

    mEditTribeInfoLayout = (RelativeLayout) findViewById(R.id.edit_tribe_info_layout);
    mEditTribeInfoLayout.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = new Intent(TribeInfoActivity.this, EditTribeInfoActivity.class);
        intent.putExtra(TribeConstants.TRIBE_ID, mTribeId);
        intent.putExtra(TribeConstants.TRIBE_OP, TribeConstants.TRIBE_EDIT);
        startActivity(intent);
      }
    });

    mMyTribeNick = (TextView) findViewById(R.id.my_tribe_nick);
    mMyTribeNick.setText(getLoginUserTribeNick());
    mEditMyTribeProfileLayout = (RelativeLayout) findViewById(R.id.my_tribe_profile_layout);
    mEditMyTribeProfileLayout.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = new Intent(TribeInfoActivity.this, EditMyTribeProfileActivity.class);
        intent.putExtra(TribeConstants.TRIBE_ID, mTribeId);
        intent.putExtra(TribeConstants.TRIBE_NICK, mMyTribeNick.getText());
        startActivityForResult(intent, EDIT_MY_TRIBE_NICK_REQUEST_CODE);
      }
    });

    mEditPersonalSettings = (RelativeLayout) findViewById(R.id.personal_tribe_setting_layout);
    mEditPersonalSettings.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

      }
    });

    mTribeMsgRecTypeLayout = (RelativeLayout) findViewById(R.id.tribe_msg_rec_type_layout);
    mTribeMsgRecType = (TextView) findViewById(R.id.tribe_msg_rec_type);
    mAtMsgRecSwitch = (SwitchCompat) findViewById(R.id.receive_tribe_at_msg);

    mTribeMsgRecTypeLayout.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent =
            TribeMsgRecTypeSetActivity.getTribeMsgRecTypeSetActivityIntent(TribeInfoActivity.this,
                mMsgRecFlag);
        startActivityForResult(intent, SET_MSG_REC_TYPE_REQUEST_CODE);
      }
    });

    mAtMsgRecSwitch.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (mMsgRecFlag != YWProfileSettingsConstants.TRIBE_MSG_REJ) {
          mAtMsgRecSwitch.setChecked(true);
          IMNotificationUtils.showToast("接收群消息时不能屏蔽群@消息哦!", TribeInfoActivity.this);
        } else {
          if (mAtMsgRecFlag == YWProfileSettingsConstants.TRIBE_AT_MSG_REC) {
            setMsgRecType(YWProfileSettingsConstants.TRIBE_MSG_REJ);
            setAtMsgRecType(YWProfileSettingsConstants.TRIBE_AT_MSG_REJ);
          } else {
            setAtMsgRecType(YWProfileSettingsConstants.TRIBE_AT_MSG_REC);
          }
        }
      }
    });

    mCLearTribeMsgs = (TextView) findViewById(R.id.clear_tribe_msg);
    mCLearTribeMsgs.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        clearMsgRecord();
      }
    });

    mQuiteTribe = (TextView) findViewById(R.id.quite_tribe);
  }

  /**
   * 群普通消息接收状态以及群@消息接收状态，SDK会在用户首次登录时或者清数据重登后自动获取并同步DB以及cache，
   * 并且会在一天后重新获取，如果开发者想实施获取可以调用{@link com.alibaba.mobileim.tribe.YWTribeManager#getTribesMsgRecSettingsFromServer(List,
   * int, IWxCallback)}
   * 获取最新配置（如果考虑到用户会经常跨终端登录，为了多端同步，开发者需要自行根据情况考虑缓存策略）
   */
  private void initMsgRecFlags() {
    if (mTribe == null) {
      mTribe = mTribeService.getTribe(mTribeId);
    }
    if (mTribe != null) {
      mMsgRecFlag = mTribe.getMsgRecType();
      mAtMsgRecFlag = mTribe.getAtFlag();
    }
    if (mAtMsgRecFlag == YWProfileSettingsConstants.TRIBE_AT_MSG_REJ) {
      mAtMsgRecSwitch.setChecked(false);
    } else {
      mAtMsgRecSwitch.setChecked(true);
    }
    setMsgRecTypeLabel(mMsgRecFlag);
  }

  private void setMsgRecTypeLabel(int flag) {
    switch (flag) {
      case YWProfileSettingsConstants.TRIBE_MSG_REC:
        mTribeMsgRecType.setText("接收并提醒");
        break;
      case YWProfileSettingsConstants.TRIBE_MSG_REC_NOT_REMIND:
        mTribeMsgRecType.setText("接收不提醒");
        break;
      case YWProfileSettingsConstants.TRIBE_MSG_REJ:
        mTribeMsgRecType.setText("不接收");
        break;
    }
  }

  private void setMsgRecType(int msgRecType) {
    switch (msgRecType) {
      case YWProfileSettingsConstants.TRIBE_MSG_REC:
        mTribeService.unblockTribe(mTribe, new TribeMsgRecSetCallback());
        break;
      case YWProfileSettingsConstants.TRIBE_MSG_REJ:
        mTribeService.blockTribe(mTribe, new TribeMsgRecSetCallback());
        break;
      case YWProfileSettingsConstants.TRIBE_MSG_REC_NOT_REMIND:
        mTribeService.receiveNotAlertTribeMsg(mTribe, new TribeMsgRecSetCallback());
        break;
    }
  }

  private void setAtMsgRecType(int atFlag) {
    switch (atFlag) {
      case YWProfileSettingsConstants.TRIBE_AT_MSG_REC:
        mTribeService.unblockAtMessage(mTribe, new TribeMsgRecSetCallback());
        break;
      case YWProfileSettingsConstants.TRIBE_AT_MSG_REJ:
        mTribeService.blockAtMessage(mTribe, new TribeMsgRecSetCallback());
        break;
    }
  }

  @SuppressLint("SetTextI18n") private void updateView() {
    mTribeName.setText(mTribe.getTribeName());
    IYWContact master = mTribe.getTribeMaster();
    mTribeDesc.setText(mTribe.getTribeNotice());
    mMyTribeNick.setText(getLoginUserTribeNick());
    if (mTribeMemberCount > 0) {
      mMemberCount.setText(mTribeMemberCount + "人");
    }
    initMsgRecFlags();
    if (getLoginUserRole() == YWTribeRole.TRIBE_HOST.type) {
      mMangeTribeMembers.setText("群成员管理");
      mEditTribeInfoLayout.setVisibility(View.VISIBLE);
      mQuiteTribe.setText("解散群");
      mQuiteTribe.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          mTribeService.disbandTribe(new IWxCallback() {
            @Override public void onSuccess(Object... result) {
              YWLog.i(TAG, "解散群成功！");
              Notification.showToastMsg(TribeInfoActivity.this, "解散群成功！");
              openTribeListFragment();
            }

            @Override public void onError(int code, String info) {
              Notification.showToastMsg(TribeInfoActivity.this,
                  "解散群失败");
            }

            @Override public void onProgress(int progress) {

            }
          }, mTribeId);
        }
      });
    } else {
      mMangeTribeMembers.setText("群成员列表");
      mEditTribeInfoLayout.setVisibility(View.GONE);
      mQuiteTribe.setText("退出群");
      mQuiteTribe.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          mTribeService.exitFromTribe(new IWxCallback() {
            @Override public void onSuccess(Object... result) {
              YWLog.i(TAG, "退出群成功！");
              Notification.showToastMsg(TribeInfoActivity.this, "退出群成功！");
              openTribeListFragment();
            }

            @Override public void onError(int code, String info) {
              Notification.showToastMsg(TribeInfoActivity.this,
                  "退出群失败");
            }

            @Override public void onProgress(int progress) {

            }
          }, mTribeId);
        }
      });
    }

    if (!TextUtils.isEmpty(mTribeOp)) {
      mMangeTribeMembersLayout.setVisibility(View.GONE);
      mEditTribeInfoLayout.setVisibility(View.GONE);
      mQuiteTribe.setText("加入群");
      mQuiteTribe.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          mTribeService.joinTribe(new IWxCallback() {
            @Override public void onSuccess(Object... result) {
              if (result != null && result.length > 0) {
                Integer retCode = (Integer) result[0];
                if (retCode == 0) {
                  YWLog.i(TAG, "加入群成功！");
                  Notification.showToastMsg(TribeInfoActivity.this, "加入群成功！");
                  mTribeOp = null;
                  mHandler.post(new Runnable() {
                    @Override public void run() {
                      updateView();
                    }
                  });
                }
              }
            }

            @Override public void onError(int code, String info) {
              Notification.showToastMsg(TribeInfoActivity.this,
                  "加入群失败");
            }

            @Override public void onProgress(int progress) {

            }
          }, mTribeId);
        }
      });
    } else {
      if (getLoginUserRole() == YWTribeRole.TRIBE_MEMBER.type) {
        mMangeTribeMembersLayout.setVisibility(View.VISIBLE);
        mEditTribeInfoLayout.setVisibility(View.GONE);
      } else {
        mMangeTribeMembersLayout.setVisibility(View.VISIBLE);
        mEditTribeInfoLayout.setVisibility(View.VISIBLE);
      }
    }
  }

  private void updateTribeMemberCount() {
    if (mTribeMemberCount > 0) {
      mMemberCount.setText(mTribeMemberCount + "人");
    }
  }

  private void openTribeListFragment() {
    Intent intent = new Intent(this, TribeActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    startActivity(intent);
    finish();
  }

  private void initTribeInfo() {
    mTribe = mTribeService.getTribe(mTribeId);
    mTribeService.addTribeListener(mTribeChangedListener);
    initTribeMemberList();
    getTribeInfoFromServer();
  }

  private void getTribeInfoFromServer() {
    mTribeService.getTribeFromServer(new IWxCallback() {
      @Override public void onSuccess(final Object... result) {
        mHandler.post(new Runnable() {
          @Override public void run() {
            mTribe = (YWTribe) result[0];
            mTribeMemberCount = mTribe.getMemberCount();
            updateView();
          }
        });
      }

      @Override public void onError(int code, String info) {

      }

      @Override public void onProgress(int progress) {

      }
    }, mTribeId);
  }

  private void initTribeMemberList() {
    getTribeMembersFromLocal();
    getTribeMembersFromServer();
  }

  private void getTribeMembersFromLocal() {
    mTribeService.getMembers(new IWxCallback() {
      @Override public void onSuccess(Object... result) {
        mList.clear();
        mList.addAll((List<YWTribeMember>) result[0]);
        if (mList != null || mList.size() > 0) {
          mTribeMemberCount = mList.size();
          mHandler.post(new Runnable() {
            @Override public void run() {
              initLoginUser();
              updateView();
            }
          });
        }
      }

      @Override public void onError(int code, String info) {

      }

      @Override public void onProgress(int progress) {

      }
    }, mTribeId);
  }

  private void getTribeMembersFromServer() {
    mTribeService.getMembersFromServer(new IWxCallback() {
      @Override public void onSuccess(Object... result) {
        mList.clear();
        mList.addAll((List<YWTribeMember>) result[0]);
        if (mList != null || mList.size() > 0) {
          mTribeMemberCount = mList.size();
          mHandler.post(new Runnable() {
            @Override public void run() {
              initLoginUser();
              updateView();
            }
          });
        }
      }

      @Override public void onError(int code, String info) {

      }

      @Override public void onProgress(int progress) {

      }
    }, mTribeId);
  }

  private void initLoginUser() {
    String loginUser = mIMKit.getIMCore().getLoginUserId();
    for (YWTribeMember member : mList) {
      if (member.getUserId().equals(loginUser)) {
        mLoginUser = member;
        break;
      }
    }
  }

  /**
   * 判断当前登录用户在群组中的身份
   */
  private int getLoginUserRole() {
    if (mTribe.getTribeRole() == null) return YWTribeRole.TRIBE_MEMBER.type;
    return mTribe.getTribeRole().type;
  }

  /**
   * 获取登录用户的群昵称
   */
  private String getLoginUserTribeNick() {
    if (mLoginUser != null && !TextUtils.isEmpty(mLoginUser.getTribeNick())) {
      return mLoginUser.getTribeNick();
    }
    String tribeNick = null;
    IYWContactService contactService = mIMKit.getContactService();
    IYWContact contact = contactService.getContactProfileInfo(mIMKit.getIMCore().getLoginUserId(),
        mIMKit.getIMCore().getAppKey());
    if (contact != null) {
      if (!TextUtils.isEmpty(contact.getShowName())) {
        tribeNick = contact.getShowName();
      } else {
        tribeNick = contact.getUserId();
      }
    }
    if (TextUtils.isEmpty(tribeNick)) {
      tribeNick = mIMKit.getIMCore().getLoginUserId();
    }
    return tribeNick;
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    mTribeService.removeTribeListener(mTribeChangedListener);
  }

  private void initTribeChangedListener() {
    mTribeChangedListener = new IYWTribeChangeListener() {
      @Override public void onInvite(YWTribe tribe, YWTribeMember user) {

      }

      @Override public void onUserJoin(YWTribe tribe, YWTribeMember user) {
        mTribeMemberCount = tribe.getMemberCount();
        if (mIMKit.getIMCore().getLoginUserId().equals(user.getUserId())) {
          getTribeInfoFromServer();
        } else {
          mHandler.post(new Runnable() {
            @Override public void run() {
              updateTribeMemberCount();
            }
          });
        }
      }

      @Override public void onUserQuit(YWTribe tribe, YWTribeMember user) {
        mTribeMemberCount = tribe.getMemberCount();
        mHandler.post(new Runnable() {
          @Override public void run() {
            updateTribeMemberCount();
          }
        });
      }

      @Override public void onUserRemoved(YWTribe tribe, YWTribeMember user) {
        openTribeListFragment();
      }

      @Override public void onTribeDestroyed(YWTribe tribe) {
        openTribeListFragment();
      }

      @Override public void onTribeInfoUpdated(YWTribe tribe) {
        mTribe = tribe;
        mHandler.post(new Runnable() {
          @Override public void run() {
            updateView();
          }
        });
      }

      @Override public void onTribeManagerChanged(YWTribe tribe, YWTribeMember user) {
        String loginUser = mIMKit.getIMCore().getLoginUserId();
        if (loginUser.equals(user.getUserId())) {
          for (YWTribeMember member : mList) {
            if (member.getUserId().equals(loginUser)) {
              mList.remove(member);
              mList.add(user);
              mHandler.post(new Runnable() {
                @Override public void run() {
                  updateView();
                }
              });
              break;
            }
          }
        }
      }

      @Override public void onTribeRoleChanged(YWTribe tribe, YWTribeMember user) {
        String loginUser = mIMKit.getIMCore().getLoginUserId();
        if (loginUser.equals(user.getUserId())) {
          for (YWTribeMember member : mList) {
            if (member.getUserId().equals(loginUser)) {
              mList.remove(member);
              mList.add(user);
              mHandler.post(new Runnable() {
                @Override public void run() {
                  updateView();
                }
              });
              break;
            }
          }
        }
      }
    };
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == EDIT_MY_TRIBE_NICK_REQUEST_CODE) {
        if (data != null) {
          String newUserNick = data.getStringExtra(TribeConstants.TRIBE_NICK);
          mMyTribeNick.setText(newUserNick);
        }
      } else if (requestCode == SET_MSG_REC_TYPE_REQUEST_CODE) {
        int flag = data.getIntExtra("Flag", mTribe.getMsgRecType());
        setMsgRecType(flag);
      }
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  /**
   * 获取群消息接收状态设置值
   */
  private void getTribeMsgRecSettings() {
    if (mTribe == null) {
      WxLog.w(TAG, "getTribeMsgRecSettings mTribe is null");
      return;
    }
    List<Long> list = new ArrayList<Long>();
    list.add(mTribe.getTribeId());
    mTribeService.getTribesMsgRecSettingsFromServer(list, 10, new IWxCallback() {
      @Override public void onSuccess(Object... result) {
        if (result != null && result.length > 0) {
          ArrayList<YWTribeSettingsModel> models = (ArrayList<YWTribeSettingsModel>) result[0];
          YWTribeSettingsModel model = models.get(0);
          if (model.getTribeId() == mTribe.getTribeId()) {
            uiHandler.post(new Runnable() {
              @Override public void run() {
                initMsgRecFlags();
              }
            });
          }
        }
      }

      @Override public void onError(int code, String info) {
        uiHandler.post(new Runnable() {
          @Override public void run() {
            initMsgRecFlags();
          }
        });
      }

      @Override public void onProgress(int progress) {

      }
    });
  }

  class TribeMsgRecSetCallback implements IWxCallback {

    private Handler uiHandler = new Handler(Looper.getMainLooper());

    public TribeMsgRecSetCallback() {

    }

    @Override public void onError(final int code, final String info) {
      uiHandler.post(new Runnable() {
        @Override public void run() {
          IMNotificationUtils.showToast("设置失败",
              TribeInfoActivity.this);
        }
      });
    }

    @Override public void onProgress(int progress) {

    }

    @Override public void onSuccess(Object... result) {
      mMsgRecFlag = mTribe.getMsgRecType();
      mAtMsgRecFlag = mTribe.getAtFlag();
      uiHandler.post(new Runnable() {
        @Override public void run() {
          initMsgRecFlags();
          IMNotificationUtils.showToast(
              "设置成功",
              TribeInfoActivity.this);
        }
      });
    }
  }

  protected void clearMsgRecord() {
    String message = "清空的消息再次漫游时不会出现。你确定要清空聊天消息吗？";
    MaterialDialog.Builder builder = new MaterialDialog.Builder(TribeInfoActivity.this);
    builder.content(message)
        .cancelable(false)
        .positiveText("确定")
        .onPositive(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            conversation.getMessageLoader().deleteAllMessage();
            IMNotificationUtils.showToast("记录已清空", TribeInfoActivity.this);
          }
        })
        .negativeText("取消")
        .onNegative(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            dialog.dismiss();
          }
        });
    builder.build().show();
  }
}
