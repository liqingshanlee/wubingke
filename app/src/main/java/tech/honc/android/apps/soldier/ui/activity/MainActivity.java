package tech.honc.android.apps.soldier.ui.activity;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.IYWConversationUnreadChangeListener;
import com.alibaba.mobileim.conversation.IYWMessageLifeCycleListener;
import com.alibaba.mobileim.conversation.IYWSendMessageToContactInBlackListListener;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.conversation.YWMessageChannel;
import com.alibaba.mobileim.conversation.YWMessageType;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeMember;
import com.alibaba.mobileim.gingko.presenter.tribe.IYWTribeChangeListener;
import com.alibaba.mobileim.login.IYWConnectionListener;
import com.alibaba.mobileim.login.YWLoginCode;
import com.alibaba.mobileim.login.YWLoginState;
import com.aspsine.fragmentnavigator.FragmentNavigator;
import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabClickListener;

import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import support.ui.app.SupportApp;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.feature.im.helper.CustomConversationHelper;
import tech.honc.android.apps.soldier.feature.im.helper.LoginHelper;
import tech.honc.android.apps.soldier.feature.im.helper.NotificationHelper;
import tech.honc.android.apps.soldier.ui.fragment.AccountFragment;
import tech.honc.android.apps.soldier.ui.fragment.DynamicFragment;
import tech.honc.android.apps.soldier.ui.fragment.HomeFragment;
import tech.honc.android.apps.soldier.ui.fragment.InformationFragment;
import tech.honc.android.apps.soldier.ui.fragment.MessageFragment;
import tech.honc.android.apps.soldier.utils.settings.VersionUtil;
import tech.honc.android.apps.soldier.utils.toolsutils.DateFormat;
import tech.honc.android.apps.soldier.utils.toolsutils.LoginNavigationsUtil;
import tech.honc.android.apps.soldier.utils.toolsutils.SnackBarUtil;

public class MainActivity extends BaseActivity {

    private static final int INDEX_MAIN = 0;
    private static final int INDEX_IM = 1;
    private static final int INDEX_DISCOVERY = 2;
    private static final int INDEX_INFO = 3;
    private static final int INDEX_ACCOUNT = 4;
    private static final int DEFAULT_POSITION = INDEX_MAIN;
    private static int OLD_POSITION = 0;
    public static final String SYSTEM_TRIBE_CONVERSATION = "sysTribe";
    public static final String SYSTEM_FRIEND_REQ_CONVERSATION = "sysfrdreq";

    private BottomBar mBottomBar;
    private FragmentNavigator mNavigator;
    private BottomBarBadge unreadMessages;
    private YWIMKit mYWIMKit;
    private IYWConversationService mConversationService;
    private IYWConversationUnreadChangeListener mConversationUnreadChangeListener;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private IYWTribeChangeListener mTribeChangedListener;
    private IYWMessageLifeCycleListener mMessageLifeCycleListener;
    private IYWSendMessageToContactInBlackListListener mSendMessageToContactInBlackListListener;
    private IYWConnectionListener mConnectionListener;
    private boolean isUpdating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupFragmentNavigator();
        setupBottomBar(savedInstanceState);
        initListeners();
        checkUpdate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (LoginNavigationsUtil.navigationActivity() == LoginNavigationsUtil.TAG_HAS_LOGIN) {
            mConversationService.removeTotalUnreadChangeListener(mConversationUnreadChangeListener);
        }
    }

    /**
     * 初始化相关监听
     */
    private void initListeners() {
        mYWIMKit = LoginHelper.getInstance().getYWIMKit();
        if (LoginNavigationsUtil.navigationActivity() == LoginNavigationsUtil.TAG_HAS_LOGIN) {
            mConversationService = mYWIMKit.getConversationService();
            //初始化并添加群变更监听
            addTribeChangeListener();
            //初始化自定义会话
            initCustomConversation();
            //设置发送消息生命周期监听
            setMessageLifeCycleListener();
            //设置发送消息给黑名单中的联系人监听
            setSendMessageToContactInBlackListListener();
            //添加IM连接状态监听
            addConnectionListener();
            //初始化并添加会话变更监听
            initConversationServiceAndListener();
        }
    }

    private void setSendMessageToContactInBlackListListener() {
        mSendMessageToContactInBlackListListener = new IYWSendMessageToContactInBlackListListener() {
            /**
             * 是否发送消息给黑名单中的联系人，当用户发送消息给黑名单中的联系人时我们会回调该接口
             * @param conversation 当前发送消息的会话
             * @param message      要发送的消息
             * @return true：发送  false：不发送
             */
            @Override
            public boolean sendMessageToContactInBlackList(YWConversation conversation,
                                                           YWMessage message) {
                //TODO 开发者可用根据自己的需求决定是否要发送该消息，SDK默认不发送
                return true;
            }
        };
        mConversationService.setSendMessageToContactInBlackListListener(
                mSendMessageToContactInBlackListListener);
    }

    private void setMessageLifeCycleListener() {
        mMessageLifeCycleListener = new IYWMessageLifeCycleListener() {
            /**
             * 发送消息前回调
             * @param conversation 当前消息所在会话
             * @param message      当前将要发送的消息
             * @return 需要发送的消息，若为null，则表示不发送消息
             */
            @Override
            public YWMessage onMessageLifeBeforeSend(YWConversation conversation,
                                                     YWMessage message) {
                //todo 以下代码仅仅是示例，开发者无需按照以下方式设置，应该根据自己的需求对消息进行修改
                String cvsType = "单聊";
                if (conversation.getConversationType() == YWConversationType.Tribe) {
                    cvsType = "群聊：";
                }
                String msgType = "文本消息";
                if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_IMAGE) {
                    msgType = "图片消息";
                } else if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_GEO) {
                    msgType = "地理位置消息";
                } else if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_AUDIO) {
                    msgType = "语音消息";
                } else if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_P2P_CUS
                        || message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_TRIBE_CUS) {
                    msgType = "自定义消息";
                }
                //根据消息类型对消息进行修改，切记这里只是示例，具体怎样对消息进行修改开发者可以根据自己的需求进行处理
                if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_TEXT) {
                    String content = message.getContent();
                    if (content.equals("55")) {
                        return message;
                    } else if (content.equals("66")) {
                        YWMessage newMsg = YWMessageChannel.createTextMessage("我创建了一条新消息, 原始消息内容：66");
                        return newMsg;
                    } else if (content.equals("77")) {
                        return null;
                    }
                }
                return message;
            }

            /**
             * 发送消息结束后回调
             * @param message   当前发送的消息
             * @param sendState 消息发送状态，具体状态请参考{@link com.alibaba.mobileim.conversation.YWMessageType.SendState}
             */
            @Override
            public void onMessageLifeFinishSend(YWMessage message,
                                                YWMessageType.SendState sendState) {

            }
        };
        mConversationService.setMessageLifeCycleListener(mMessageLifeCycleListener);
    }

    private void initCustomConversation() {
        CustomConversationHelper.addCustomConversation(SYSTEM_TRIBE_CONVERSATION, null);
        CustomConversationHelper.addCustomConversation(SYSTEM_FRIEND_REQ_CONVERSATION, null);
    }

    private void addTribeChangeListener() {
        mTribeChangedListener = new IYWTribeChangeListener() {
            @Override
            public void onInvite(YWTribe tribe, YWTribeMember user) {
                Map<YWTribe, YWTribeMember> map = new HashMap<YWTribe, YWTribeMember>();
                map.put(tribe, user);
                LoginHelper.getInstance().getTribeInviteMessages().add(map);
                String userName = user.getShowName();
                if (TextUtils.isEmpty(userName)) {
                    userName = user.getUserId();
                }
            }

            @Override
            public void onUserJoin(YWTribe tribe, YWTribeMember user) {
                //用户user加入群tribe
                if (user.getShowName() != null || !TextUtils.isEmpty(user.getShowName())) {
                    NotificationHelper.showNotificationBySys(tribe.getTribeName(),
                            user.getShowName() + "加入了" + tribe.getTribeName(), true, tribe.getTribeId());
                }
            }

            @Override
            public void onUserQuit(YWTribe tribe, YWTribeMember user) {
                //用户user退出群tribe
                if (user.getShowName() != null || !TextUtils.isEmpty(user.getShowName())) {
                    NotificationHelper.showNotificationBySys(tribe.getTribeName(),
                            user.getShowName() + "退出了" + tribe.getTribeName(), true, tribe.getTribeId());
                }
            }

            @Override
            public void onUserRemoved(YWTribe tribe, YWTribeMember user) {
                //用户user被提出群tribe
            }

            @Override
            public void onTribeDestroyed(YWTribe tribe) {
                //群组tribe被解散了
                if (tribe.getTribeName() != null && !TextUtils.isEmpty(tribe.getTribeName())) {
                    NotificationHelper.showNotificationBySys(tribe.getTribeName(),
                            tribe.getTribeName() + "群被解散了", true, tribe.getTribeId());
                }
            }

            @Override
            public void onTribeInfoUpdated(YWTribe tribe) {
                //群组tribe的信息更新了（群名称、群公告、群校验模式修改了）
            }

            @Override
            public void onTribeManagerChanged(YWTribe tribe, YWTribeMember user) {
                //用户user被设置为群管理员或者被取消管理员
            }

            @Override
            public void onTribeRoleChanged(YWTribe tribe, YWTribeMember user) {
                //用户user的群角色发生改变了
            }
        };
        mYWIMKit.getTribeService().addTribeListener(mTribeChangedListener);
    }

    private void initConversationServiceAndListener() {
        mConversationUnreadChangeListener = new IYWConversationUnreadChangeListener() {

            //当未读数发生变化时会回调该方法，开发者可以在该方法中更新未读数
            @Override
            public void onUnreadChange() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //获取当前登录用户的所有未读数
                        int unReadCount = mConversationService.getAllUnreadCount();
                        //设置桌面角标的未读数
                        if (unReadCount > 0) {
                            mYWIMKit.setShortcutBadger(unReadCount);
                            unreadMessages.setCount(unReadCount);
                            unreadMessages.show();
                        } else {
                            unreadMessages.hide();
                        }
                    }
                });
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LoginNavigationsUtil.navigationActivity() == LoginNavigationsUtil.TAG_HAS_LOGIN) {
            mConversationUnreadChangeListener.onUnreadChange();
            mConversationService.addTotalUnreadChangeListener(mConversationUnreadChangeListener);
        }
    }

    private void setupBottomBar(Bundle savedInstanceState) {
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setBackgroundColor(SupportApp.color(R.color.white));
        mBottomBar.useFixedMode();
        mBottomBar.setItemsFromMenu(R.menu.menu_bottombar, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                switch (menuItemId) {
                    case R.id.menu_home:
                        mNavigator.showFragment(INDEX_MAIN);
                        mBottomBar.selectTabAtPosition(INDEX_MAIN, true);
                        OLD_POSITION = INDEX_MAIN;
                        break;
                    case R.id.menu_message:
                        navigation(INDEX_IM);
                        break;
                    case R.id.menu_dynamic:
                        mNavigator.showFragment(INDEX_DISCOVERY);
                        mBottomBar.selectTabAtPosition(INDEX_DISCOVERY, true);
                        OLD_POSITION = INDEX_DISCOVERY;
                        break;
                    case R.id.menu_information:
                        mNavigator.showFragment(INDEX_INFO);
                        mBottomBar.selectTabAtPosition(INDEX_INFO, true);
                        OLD_POSITION = INDEX_INFO;
                        break;
                    case R.id.menu_accounts:
                        navigation(INDEX_ACCOUNT);
                        break;
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                //do nothing
            }
        });

        // Make a Badge for the first tab, with red background color and a value of "13".
        //BottomBarBadge unreadMessages = mBottomBar.makeBadgeForTabAt(INDEX_CONVERSIONS, "#FF0000", 13);
        // Control the badge's visibility
        //unreadMessages.show();

        //mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        unreadMessages = mBottomBar.makeBadgeForTabAt(1, "#FF0000", 0);
    }

    public void navigation(int location) {
        switch (LoginNavigationsUtil.navigationActivity()) {
            case LoginNavigationsUtil.TAG_HAS_LOGIN:
                mNavigator.showFragment(location);
                mBottomBar.selectTabAtPosition(location, true);
                break;
            case LoginNavigationsUtil.TAG_NO_REGISTER:
                checkLoginStatus();
                break;
        }
    }

    public void checkLoginStatus() {
        AlertDialog.Builder mBuilder;
        mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("温馨提示");
        mBuilder.setMessage("亲,你还没有登陆哦");
        mBuilder.setPositiveButton("去登陆", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Navigator.startFirstLoginActivity(MainActivity.this);
                finish();
            }
        });
        mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mNavigator.showFragment(OLD_POSITION);
                mBottomBar.selectTabAtPosition(OLD_POSITION, true);
            }
        }).show();
    }

    private void setupFragmentNavigator() {
        mNavigator =
                new FragmentNavigator(getSupportFragmentManager(), new FragmentAdapter(), R.id.container);
        // set default tab position
        mNavigator.setDefaultPosition(DEFAULT_POSITION);
    }

    private void addConnectionListener() {
        mConnectionListener = new IYWConnectionListener() {
            @Override
            public void onDisconnect(int i, String s) {
                if (i == YWLoginCode.LOGON_FAIL_KICKOFF) {
                    LoginHelper.getInstance().setAutoLoginState(YWLoginState.idle);
                    Intent intent = new Intent(MainActivity.this, FirstLoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction("LOGON_FAIL_KICKOFF");
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onReConnecting() {
                //
                Log.d("openIM", "正在重连...");
            }

            @Override
            public void onReConnected() {
                Log.d("openIM", "重连完成...");
            }
        };
        if (mYWIMKit != null) {
            mYWIMKit.getIMCore().addConnectionListener(mConnectionListener);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mBottomBar.onSaveInstanceState(outState);
    }

    //主要的fragment

    private static class FragmentAdapter implements FragmentNavigatorAdapter {
        private ArrayList<Fragment> mFragments = new ArrayList<>();

        FragmentAdapter() {
            mFragments.clear();
            mFragments.add(new HomeFragment());
            mFragments.add(new MessageFragment());
            mFragments.add(new DynamicFragment());
            mFragments.add(new InformationFragment());
            mFragments.add(new AccountFragment());
        }

        @Override
        public Fragment onCreateFragment(int position) {
            return mFragments.get(position);
        }

        @Override
        public String getTag(int position) {
            return mFragments.get(position).getClass().getSimpleName();
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    private void checkUpdate() {
        if (isUpdating) {
            return;
        }
        isUpdating = true;
        FIR.checkForUpdateInFIR("e381b756c07aad93cf00f982ca40fd00", new VersionCheckCallback() {
            public void onSuccess(String versionJson) {
                Log.i("fir", "check from fir.im success! " + "\n" + versionJson);
                delUpdateJson(versionJson);
            }

            @Override
            public void onFail(Exception exception) {
                Log.i("fir", "check fir.im fail! " + "\n" + exception.getMessage());
                isUpdating = false;
            }

            @Override
            public void onStart() {
                //Toast.makeText(getApplicationContext(), "正在获取", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                isUpdating = false;
                //Toast.makeText(getApplicationContext(), "获取完成", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void delUpdateJson(String versionJson) {
        try {
            JSONObject jsonObject = new JSONObject(versionJson);
            int remoteVersionCode = jsonObject.getInt("version");
            if (remoteVersionCode > VersionUtil.getVersionCode()) {
                showNewVersion(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showNewVersion(final JSONObject object) throws JSONException {
        new MaterialDialog.Builder(this).title("发现新版本")
                .
                        content("更新日期:"
                                + DateFormat.getRelativeTime(object.getLong("updated_at"))
                                + "\n"
                                +
                                "版本号:"
                                + object.getString("versionShort")
                                + "\n"
                                + "安装包大小:"
                                + VersionUtil.bytes2kb((object.getJSONObject("binary")).getLong("fsize"))
                                + "\n"
                                + "更新日志："
                                + object.getString("changelog"))
                .negativeText("暂不更新")
                .positiveText("现在更新")
                .autoDismiss(false)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL
                                | DownloadManager.STATUS_RUNNING
                                | DownloadManager.STATUS_PAUSED
                                | DownloadManager.STATUS_PENDING);
                        Cursor cursor = downloadManager.query(query);
                        ArrayList<Long> ids = new ArrayList<>();
                        while (cursor != null && cursor.moveToNext()) {
                            if (cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME))
                                    .endsWith(".apk")) {
                                ids.add(cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID)));
                            }
                        }
                        for (Long item : ids) {
                            downloadManager.remove(item);
                        }
                        File file = new File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                                "update.apk");
                        if (file.exists()) {
                            file.deleteOnExit();
                        }
                        DownloadManager.Request request = null;
                        try {
                            request = new DownloadManager.Request(Uri.parse(object.getString("installUrl")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }
                        request.setDestinationUri(Uri.fromFile(file));
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                        request.setTitle("下载更新...");
                        request.setNotificationVisibility(
                                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setVisibleInDownloadsUi(true);
                        request.setAllowedOverRoaming(true);
                        request.allowScanningByMediaScanner();
                        try {
                            long downloadId = downloadManager.enqueue(request);
                        } catch (IllegalArgumentException e) {
                            SnackBarUtil.showTextByToast(MainActivity.this, "检测到您的系统中下载组件被移除，请重置手机以恢复该组件。",
                                    Gravity.CENTER);
                        }
                        dialog.dismiss();
                    }
                })
                .build()
                .show();
    }
}
