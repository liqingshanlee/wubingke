package tech.honc.android.apps.soldier.feature.im.helper;

import android.app.Application;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.IYWP2PPushListener;
import com.alibaba.mobileim.IYWTribePushListener;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWConstants;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.LoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.IYWContactCacheUpdateListener;
import com.alibaba.mobileim.contact.IYWContactHeadClickListener;
import com.alibaba.mobileim.contact.IYWContactOperateNotifyListener;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWCustomMessageBody;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeMember;
import com.alibaba.mobileim.login.YWLoginState;
import com.alibaba.mobileim.login.YWPwdType;
import com.alibaba.mobileim.utility.IMAutoLoginInfoStoreUtil;
import com.alibaba.tcms.env.EnvManager;
import com.alibaba.tcms.env.TcmsEnvType;
import com.alibaba.tcms.env.YWEnvManager;
import com.alibaba.tcms.env.YWEnvType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.honc.android.apps.soldier.BuildConfig;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.AccountService;
import tech.honc.android.apps.soldier.feature.im.advice.operation.ChattingOperation;
import tech.honc.android.apps.soldier.feature.im.common.Notification;
import tech.honc.android.apps.soldier.feature.im.contact.ContactCacheUpdateListenerImpl;
import tech.honc.android.apps.soldier.feature.im.contact.ContactOperateNotifyListenerImpl;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.ui.app.SoldierApp;

/**
 * Created by wangh on 2016-5-20-0020.
 */
public class LoginHelper {

  private static LoginHelper sInstance = new LoginHelper();
  private AccountService mAccountService;

  public static LoginHelper getInstance() {
    return sInstance;
  }

  private YWIMKit mYWIMKit;

  private Application mApplication;
  public static YWEnvType sEnvType = YWEnvType.ONLINE;
  private YWLoginState mAutoLoginState = YWLoginState.idle;

  public YWIMKit getYWIMKit() {
    return mYWIMKit;
  }

  public void initSDK(Application application) {
    mApplication = application;
    sEnvType = YWEnvManager.getEnv(application);

    //初始化IMKit
    final String userId = IMAutoLoginInfoStoreUtil.getLoginUserId();
    final String appkey = IMAutoLoginInfoStoreUtil.getAppkey();
    if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(appkey)) {
      LoginHelper.getInstance().initIMKit(userId, appkey);
      InitNotificationHelper.init();//重复初始化了
    }

    TcmsEnvType type = EnvManager.getInstance().getCurrentEnvType(application);
    if (type == TcmsEnvType.ONLINE || type == TcmsEnvType.PRE) {
      YWAPI.init(mApplication, BuildConfig.IM_APP_KEY);
    } else if (type == TcmsEnvType.TEST) {
      YWAPI.init(mApplication, BuildConfig.IM_APP_KEY);
    }
    NotigicationHelper.init();
  }

  public void initIMKit(String userId, String appKey) {
    mYWIMKit = YWAPI.getIMKitInstance(userId, appKey);
    addPushMessageListener();
    //添加联系人通知和更新监听 todo 在初始化后、登录前添加监听，离线的联系人系统消息才能触发监听器
    addContactListeners();
    contactHeadClickCallback();
  }
//添加头像点击时间
  public void contactHeadClickCallback(){
    mYWIMKit.getContactService().setContactHeadClickListener(new IYWContactHeadClickListener() {
      @Override
      public void onUserHeadClick(final Fragment fragment, YWConversation ywConversation, String s,
          String s1, boolean b) {
        //根据useId获取用户
        mAccountService = ApiService.createAccountService();
        Call<User> call = mAccountService.getUserIm(s);
        call.enqueue(new Callback<User>() {
          @Override public void onResponse(Call<User> call, Response<User> response) {
            if (response.isSuccessful()){
              User user = response.body();
              Navigator.startUserDetailActivty(fragment.getActivity(),user.id);
            }
          }

          @Override public void onFailure(Call<User> call, Throwable t) {

          }
        });

      }

      @Override
      public void onTribeHeadClick(Fragment fragment, YWConversation ywConversation, long l) {

      }

      @Override public void onCustomHeadClick(Fragment fragment, YWConversation ywConversation) {

      }
    });
  }

  public YWLoginState getAutoLoginState() {
    return mAutoLoginState;
  }

  public void setAutoLoginState(YWLoginState state) {
    this.mAutoLoginState = state;
  }

  private List<Map<YWTribe, YWTribeMember>> mTribeInviteMessages =
      new ArrayList<Map<YWTribe, YWTribeMember>>();

  public List<Map<YWTribe, YWTribeMember>> getTribeInviteMessages() {
    return mTribeInviteMessages;
  }

  private IYWTribePushListener mTribeListener = new IYWTribePushListener() {
    @Override public void onPushMessage(YWTribe tribe, YWMessage message) {
      //TODO 收到群消息
      NotificationHelper.showNotificationBySys(tribe.getTribeName(),message.getContent(),true,tribe.getTribeId());
    }
  };

  private IYWP2PPushListener mP2PListener = new IYWP2PPushListener() {
    @Override public void onPushMessage(IYWContact contact, YWMessage message) {
      if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_P2P_CUS) {
        if (message.getMessageBody() instanceof YWCustomMessageBody) {
          YWCustomMessageBody messageBody = (YWCustomMessageBody) message.getMessageBody();
          if (messageBody.getTransparentFlag() == 1) {
            String content = messageBody.getContent();
            try {
              JSONObject object = new JSONObject(content);
              if (object.has("text")) {
                String text = object.getString("text");
                Notification.showToastMsgLong(SoldierApp.getContext(), "透传消息，content = " + text);
              } else if (object.has("customizeMessageType")) {
                String customType = object.getString("customizeMessageType");
                if (!TextUtils.isEmpty(customType) && customType.equals(
                    ChattingOperation.CustomMessageType.READ_STATUS)) {
                  YWConversation conversation = mYWIMKit.getConversationService()
                      .getConversationByConversationId(message.getConversationId());
                  long msgId = Long.parseLong(object.getString("PrivateImageRecvReadMessageId"));
                  conversation.updateMessageReadStatus(conversation, msgId);
                }
              }
            } catch (JSONException e) {

            }
          }
        }
      }
    }
  };

  private IYWContactOperateNotifyListener mContactOperateNotifyListener =
      new ContactOperateNotifyListenerImpl();

  private IYWContactCacheUpdateListener mContactCacheUpdateListener =
      new ContactCacheUpdateListenerImpl();

  private void removeContactListeners() {
    if (mYWIMKit != null) {
      if (mContactOperateNotifyListener != null) {
        mYWIMKit.getContactService()
            .removeContactOperateNotifyListener(mContactOperateNotifyListener);
      }
      if (mContactCacheUpdateListener != null) {
        mYWIMKit.getContactService().removeContactCacheUpdateListener(mContactCacheUpdateListener);
      }
    }
  }

  /**
   * 联系人相关操作通知回调，SDK使用方可以实现此接口来接收联系人操作通知的监听
   * 所有方法都在UI线程调用
   * SDK会自动处理这些事件，一般情况下，用户不需要监听这个事件
   *
   * @author shuheng
   */
  private void addContactListeners() {
    //添加联系人通知和更新监听，先删除再添加，以免多次添加该监听
    removeContactListeners();
    if (mYWIMKit != null) {
      if (mContactOperateNotifyListener != null) {
        mYWIMKit.getContactService().addContactOperateNotifyListener(mContactOperateNotifyListener);
      }
      if (mContactCacheUpdateListener != null) {
        mYWIMKit.getContactService().addContactCacheUpdateListener(mContactCacheUpdateListener);
      }
    }
  }

  /**
   * 添加新消息到达监听，该监听应该在登录之前调用以保证登录后可以及时收到消息
   */
  private void addPushMessageListener() {
    if (mYWIMKit == null) {
      return;
    }

    IYWConversationService conversationService = mYWIMKit.getConversationService();
    //添加单聊消息监听，先删除再添加，以免多次添加该监听
    conversationService.removeP2PPushListener(mP2PListener);
    conversationService.addP2PPushListener(mP2PListener);

    //添加群聊消息监听，先删除再添加，以免多次添加该监听
    conversationService.removeTribePushListener(mTribeListener);
    conversationService.addTribePushListener(mTribeListener);
  }

  public void logIn(String userId, String password, IWxCallback callback) {
    this.logIn(userId, password, BuildConfig.IM_APP_KEY, callback);
  }

  /**
   * 登录操作
   *
   * @param userId 用户id
   * @param password 密码
   * @param callback 登录操作结果的回调
   */
  //------------------请特别注意，OpenIMSDK会自动对所有输入的用户ID转成小写处理-------------------
  //所以开发者在注册用户信息时，尽量用小写
  public void logIn(String userId, String password, String appKey, IWxCallback callback) {

    if (mYWIMKit == null) {
      return;
    }

    YWLoginParam loginParam = YWLoginParam.createLoginParam(userId, password);
    //YWLoginParam loginParam = YWLoginParam.createLoginParam("testpro56", "taobao1234");
    if (TextUtils.isEmpty(appKey) || appKey.equals(YWConstants.YWSDKAppKey) || appKey.equals(
        YWConstants.YWSDKAppKeyCnHupan)) {
      loginParam.setServerType(LoginParam.ACCOUNTTYPE_WANGXIN);
      loginParam.setPwdType(YWPwdType.pwd);
    }
    // openIM SDK提供的登录服务
    IYWLoginService mLoginService = mYWIMKit.getLoginService();
    mLoginService.login(loginParam, callback);
  }

  /**
   * 登出
   */
  public void loginOut() {
    if (mYWIMKit == null) {
      return;
    }

    // openIM SDK提供的登录服务
    IYWLoginService mLoginService = mYWIMKit.getLoginService();
    mLoginService.logout(new IWxCallback() {

      @Override public void onSuccess(Object... arg0) {

      }

      @Override public void onProgress(int arg0) {

      }

      @Override public void onError(int arg0, String arg1) {

      }
    });
  }
}
