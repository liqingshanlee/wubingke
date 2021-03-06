package tech.honc.android.apps.soldier.feature.im.advice.operation;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.alibaba.mobileim.WXAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMChattingPageOperateion;
import com.alibaba.mobileim.aop.model.GoodsInfo;
import com.alibaba.mobileim.aop.model.ReplyBarItem;
import com.alibaba.mobileim.aop.model.YWChattingPlugin;
import com.alibaba.mobileim.channel.YWEnum;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.IYWContactProfileCallback;
import com.alibaba.mobileim.contact.IYWCrossContactProfileCallback;
import com.alibaba.mobileim.contact.YWContactFactory;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.conversation.YWCustomMessageBody;
import com.alibaba.mobileim.conversation.YWFileManager;
import com.alibaba.mobileim.conversation.YWGeoMessageBody;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.conversation.YWMessageChannel;
import com.alibaba.mobileim.conversation.YWP2PConversationBody;
import com.alibaba.mobileim.conversation.YWTribeConversationBody;
import com.alibaba.mobileim.kit.contact.YWContactHeadLoadHelper;
import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.media.MediaService;
import com.alibaba.sdk.android.media.upload.UploadListener;
import com.alibaba.sdk.android.media.upload.UploadTask;
import com.alibaba.sdk.android.media.utils.FailReason;
import com.alibaba.wxlib.thread.WXThreadPoolMgr;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import mediapicker.MediaItem;
import mediapicker.MediaOptions;
import mediapicker.activities.MediaPickerActivity;
import org.json.JSONException;
import org.json.JSONObject;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.feature.im.common.Constant;
import tech.honc.android.apps.soldier.feature.im.common.Notification;
import tech.honc.android.apps.soldier.feature.im.helper.LoginHelper;
import tech.honc.android.apps.soldier.feature.im.image.PictureUtils;
import tech.honc.android.apps.soldier.feature.im.image.PreviewImageActivity;
import tech.honc.android.apps.soldier.feature.im.interfaces.ISelectContactListener;
import tech.honc.android.apps.soldier.feature.im.utils.FileUtil;
import tech.honc.android.apps.soldier.ui.app.SoldierApp;

/**
 * 聊天界面(单聊和群聊界面)的定制点(根据需要实现相应的接口来达到自定义聊天界面)，不设置则使用openIM默认的实现
 * 1.CustomChattingTitleAdvice 自定义聊天窗口标题 2. OnUrlClickChattingAdvice 自定义聊天窗口中
 * 当消息是url是点击的回调。用于isv处理url的打开处理。不处理则用第三方浏览器打开 如果需要定制更多功能，需要实现更多开放的接口
 * 需要.继承BaseAdvice .实现相应的接口
 * <p/>
 * 另外需要在Application中绑定
 * AdviceBinder.bindAdvice(PointCutEnum.CHATTING_FRAGMENT_POINTCUT,
 * ChattingOperationCustomSample.class);
 */
public class ChattingOperation extends IMChattingPageOperateion {

  private static final String TAG = "ChattingOperation";
  private Context mContext;
  YWIMKit mIMKit = LoginHelper.getInstance().getYWIMKit();

  public class CustomMessageType {
    private static final String GREETING = "Greeting";
    private static final String CARD = "CallingCard";
    private static final String IMAGE = "PrivateImage";
    public static final String READ_STATUS = "PrivateImageRecvRead";
  }

  // 默认写法
  public ChattingOperation(Pointcut pointcut) {
    super(pointcut);
  }

  /**
   * 单聊ui界面，点击url的事件拦截 返回true;表示自定义处理，返回false，由默认处理
   *
   * @param fragment 可以通过 fragment.getActivity拿到Context
   * @param message 点击的url所属的message
   * @param url 点击的url
   */
  @Override public boolean onUrlClick(Fragment fragment, YWMessage message, String url,
      YWConversation conversation) {
    Notification.showToastMsgLong(fragment.getActivity(), "用户点击了url:" + url);
    if (!url.startsWith("http")) {
      url = "http://" + url;
    }
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(url));
    fragment.startActivity(intent);

    return true;
  }

  /**
   * 是否显示默认的Item，照片，相册
   */
  @Override public boolean showDefaultBarItems(YWConversation conversation) {
    return false;
  }

  private void showForwardMessageDialog(final Activity context, final YWMessage msg) {
    View view = View.inflate(context, R.layout.dialog_transparent_message, null);
    final EditText text = (EditText) view.findViewById(R.id.content);
    final TextView prefix = (TextView) view.findViewById(R.id.prefix);
    prefix.setText("用户id");
    text.setHint("用户id");
    final IWxCallback forwardCallBack = new IWxCallback() {

      @Override public void onSuccess(Object... result) {
        Notification.showToastMsg(context, "forward succeed!");
      }

      @Override public void onError(int code, String info) {
        Notification.showToastMsg(context, "forward fail!");
      }

      @Override public void onProgress(int progress) {

      }
    };
    MaterialDialog ad = new MaterialDialog.Builder(context).title("转发消息")
        .theme(Theme.LIGHT)
        .customView(view, true)
        .positiveText("确定")
        .onPositive(new MaterialDialog.SingleButtonCallback() {

          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            String content = text.getText().toString();
            if (TextUtils.isEmpty(content)) {
              Notification.showToastMsg(context, "forward userid can't be empty!");
            } else {
              YWIMKit imKit = LoginHelper.getInstance().getYWIMKit();
              if (imKit != null) {
                if (imKit.getIMCore() != null) {
                  String loginId = imKit.getIMCore().getLoginUserId();
                  if (content.equals(loginId)) {
                    Notification.showToastMsg(context, "forward userid can't be self!");
                  } else {
                    //转发给个人示例
                    IYWContact appContact =
                        YWContactFactory.createAPPContact(content, imKit.getIMCore().getAppKey());

                    imKit.getConversationService()
                        .forwardMsgToContact(appContact, msg, forwardCallBack);
                    context.startActivity(imKit.getChattingActivityIntent(content));
                    context.finish();
                  }
                }
              }
              dialog.dismiss();
            }
          }
        })
        .negativeText("取消")
        .onNegative(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            dialog.dismiss();
          }
        })
        .build();
    if (!ad.isShowing()) {
      ad.show();
    }
  }

  private UploadListener uploadListener = new UploadListener() {
    @Override public void onUploading(UploadTask uploadTask) {

    }

    @Override public void onUploadFailed(UploadTask uploadTask, FailReason failReason) {
      YWLog.d(TAG, "上传失败，desc = " + failReason.getMessage());
    }

    @Override public void onUploadComplete(UploadTask uploadTask) {
      String url = uploadTask.getResult().url;
      YWLog.d(TAG, "上传成功， url = " + url);
      sendTransparentMessage(url);
      //上传成功后将文件拷贝到缓存目录，以便查看
      YWFileManager manager = mIMKit.getIMCore().getFileManager();
      File toFile = new File(getImageFilePathFromUrl(url));
      manager.copyFile(uploadTask.getFile(), toFile);
    }

    @Override public void onUploadCancelled(UploadTask uploadTask) {

    }
  };

  /**
   * 上传图片
   */
  private void uploadImage(final Fragment fragment) {
    PictureUtils.getPictureFromAlbum(fragment.getActivity(), new IWxCallback() {
      @Override public void onSuccess(Object... result) {
        if (result != null && result.length > 0) {
          final String path = (String) result[0];
          if (!TextUtils.isEmpty(path)) {
            final MediaService mediaService = AlibabaSDK.getService(MediaService.class);
            WXThreadPoolMgr.getInstance().doAsyncRun(new Runnable() {
              @Override public void run() {
                File file = new File(path);
                if (file.exists()) {
                  mediaService.upload(file, SoldierApp.NAMESPACE, uploadListener);
                }
              }
            });
          }
        }
      }

      @Override public void onError(int code, String info) {

      }

      @Override public void onProgress(int progress) {

      }
    });
  }

  /**
   * 发送阅后即焚消息
   */
  private void sendTransparentMessage(String url) {
    JSONObject object = new JSONObject();
    try {
      object.put("customizeMessageType", CustomMessageType.IMAGE);
      object.put("url", url);
    } catch (JSONException e) {
    }
    YWCustomMessageBody body = new YWCustomMessageBody();
    body.setContent(object.toString());
    body.setSummary("阅后即焚");
    body.setTransparentFlag(1);
    YWMessage message = YWMessageChannel.createCustomMessage(body);
    message.setNeedSave(true);
    mConversation.getMessageSender().sendMessage(message, 120, null);
  }

  /**
   * 从阅后即焚消息的url中获取本地图片地址
   */
  private String getImageFilePathFromUrl(String url) {
    final String name = url.substring(url.lastIndexOf("/") + 1);
    final String filePath = Constant.STORE_PATH + name;
    return filePath;
  }

  public static int count = 1;

  /**
   * 发送群自定义消息
   */
  public static void sendTribeCustomMessage(YWConversation conversation) {
    // 创建自定义消息的messageBody对象
    YWCustomMessageBody messageBody = new YWCustomMessageBody();

    // 请注意这里不一定要是JSON格式，这里纯粹是为了演示的需要
    JSONObject object = new JSONObject();
    try {
      object.put("customizeMessageType", CustomMessageType.GREETING);
    } catch (JSONException e) {
    }

    messageBody.setContent(object.toString());// 用户要发送的自定义消息，SDK不关心具体的格式，比如用户可以发送JSON格式
    messageBody.setSummary("您收到一个招呼");// 可以理解为消息的标题，用于显示会话列表和消息通知栏
    //创建群聊自定义消息，创建单聊自定义消息和群聊自定义消息的接口不是同一个，切记不要用错！！
    YWMessage message = YWMessageChannel.createTribeCustomMessage(messageBody);
    //发送群聊自定义消息
    conversation.getMessageSender().sendMessage(message, 120, null);
  }

  /**
   * 发送单聊自定义消息
   */
  public static void sendP2PCustomMessage(String userId) {
    //创建自定义消息的messageBody对象
    YWCustomMessageBody messageBody = new YWCustomMessageBody();

    //定义自定义消息协议，用户可以根据自己的需求完整自定义消息协议，不一定要用JSON格式，这里纯粹是为了演示的需要
    JSONObject object = new JSONObject();
    try {
      object.put("customizeMessageType", CustomMessageType.CARD);
      object.put("personId", userId);
    } catch (JSONException e) {

    }

    messageBody.setContent(object.toString()); // 用户要发送的自定义消息，SDK不关心具体的格式，比如用户可以发送JSON格式
    messageBody.setSummary("[名片]"); // 可以理解为消息的标题，用于显示会话列表和消息通知栏
    //创建单聊自定义消息，创建单聊自定义消息和群聊自定义消息的接口不是同一个，切记不要用错！！
    YWMessage message = YWMessageChannel.createCustomMessage(messageBody);
    //发送单聊自定义消息
    mConversation.getMessageSender().sendMessage(message, 120, null);
  }

  /**
   * 发送单聊地理位置消息
   */
  public static void sendGeoMessage(YWConversation conversation, double latitude, double longtitude,
      String address) {
    if (conversation != null && latitude != 0 && longtitude != 0 && address != null) {
      conversation.getMessageSender()
          .sendMessage(YWMessageChannel.createGeoMessage(latitude, longtitude, address), 120, null);
    }
  }

  /**
   * 定制点击消息事件, 每一条消息的点击事件都会回调该方法，开发者根据消息类型，对不同类型的消息设置不同的点击事件
   *
   * @param fragment 聊天窗口fragment对象
   * @param message 被点击的消息
   * @return true:使用用户自定义的消息点击事件，false：使用默认的消息点击事件
   */
  @Override public boolean onMessageClick(final Fragment fragment, final YWMessage message) {
    if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_TEXT) {
      //点击文本消息
    } else if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_GEO) {
      //你点击了地理位置消息
      if (message.getContent() != null && !TextUtils.isEmpty(message.getContent())) {
        Navigator.startMapViewActivity(fragment.getActivity(), message.getContent());
      } else {
        Toast.makeText(fragment.getContext(), "地理位置消息不能为空", Toast.LENGTH_SHORT).show();
      }
      return true;
    } else if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_P2P_CUS
        || message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_TRIBE_CUS) {
      String msgType = null;
      try {
        String content = message.getMessageBody().getContent();
        JSONObject object = new JSONObject(content);
        msgType = object.getString("customizeMessageType");
      } catch (Exception e) {

      }
      if (!TextUtils.isEmpty(msgType) && msgType.equals(CustomMessageType.IMAGE)) {
        YWCustomMessageBody body = (YWCustomMessageBody) message.getMessageBody();
        //判断自己是否已经读过该消息，若已经读过则提示"你已查看过图片，图片已消失"
        if (body.getExtraData() != null
            && (Integer) body.getExtraData() == YWMessage.MSG_READED_STATUS) {
          Notification.showToastMsg(fragment.getActivity(), R.string.aliwx_message_already_destroy);
        } else {
          Intent intent = new Intent(fragment.getActivity(), PreviewImageActivity.class);
          intent.putExtra(PreviewImageActivity.MESSAGE, message);
          fragment.startActivity(intent);
        }
      } else {
        //你点击了自定义消息
      }
      return true;
    }
    return false;
  }

  /**
   * 定制长按消息事件，每一条消息的长按事件都会回调该方法，开发者根据消息类型，对不同类型的消息设置不同的长按事件
   *
   * @param fragment 聊天窗口fragment对象
   * @param message 被点击的消息
   * @return true:使用用户自定义的长按消息事件，false：使用默认的长按消息事件
   */
  @Override public boolean onMessageLongClick(final Fragment fragment, final YWMessage message) {
    final Activity context = fragment.getActivity();
    if (message != null) {
      final List<String> linkedList = new ArrayList<String>();
      //if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_TEXT
      //    || message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_GEO) {
      //  linkedList.add(context.getString(R.string.im_msg_forward));
      //}
      linkedList.add(context.getString(R.string.im_msg_delete));

      if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_TEXT) {
        linkedList.add(context.getString(R.string.im_msg_copy));
      }

      if ((message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_IMAGE
          || message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_GIF
          || message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_VIDEO)) {
        String content = message.getMessageBody().getContent();
        if (!TextUtils.isEmpty(content) && content.startsWith("http")) {
          if (!linkedList.contains(context.getString(R.string.im_msg_delete))) {
            linkedList.add(0, context.getString(R.string.im_msg_delete));
          }
        }
      }
      if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_AUDIO) {
        String text;
        if (mUserInCallMode) { //当前为听筒模式
          text = "使用扬声器模式";
        } else { //当前为扬声器模式
          text = "使用听筒模式";
        }
        linkedList.add(text);
      }

      final String[] strs = new String[linkedList.size()];
      linkedList.toArray(strs);

      final YWConversation conversation = LoginHelper.getInstance()
          .getYWIMKit()
          .getConversationService()
          .getConversationByConversationId(message.getConversationId());
      MaterialDialog materialDialog = new MaterialDialog.Builder(context).items(strs)
          .itemsCallback(new MaterialDialog.ListCallback() {
            @Override public void onSelection(MaterialDialog dialog, View itemView, int which,
                CharSequence text) {

              if (which < strs.length) {
                if (context.getResources().getString(R.string.im_msg_delete).equals(strs[which])) {
                  if (conversation != null) {
                    conversation.getMessageLoader().deleteMessage(message);
                  } else {
                    Notification.showToastMsg(context, "删除失败，请稍后重试");
                  }
                } else if (context.getResources()
                    .getString(R.string.im_msg_copy)
                    .equals(strs[which])) {
                  ClipboardManager clip =
                      (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                  String content = message.getMessageBody().getContent();
                  if (TextUtils.isEmpty(content)) {
                    return;
                  }

                  try {
                    clip.setText(content);
                  } catch (NullPointerException e) {
                    e.printStackTrace();
                  } catch (IllegalStateException e) {
                    e.printStackTrace();
                  }
                } else if ("使用扬声器模式".equals(strs[which]) || "使用听筒模式".equals(strs[which])) {

                  if (mUserInCallMode) {
                    mUserInCallMode = false;
                  } else {
                    mUserInCallMode = true;
                  }
                }
              }
            }
          })
          .build();

      materialDialog.show();
    }

    if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_GEO) {
      //长按地理位置消息,

      return true;
    } else if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_TEXT
        || message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_GIF
        || message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_IMAGE
        || message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_VIDEO
        || message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_AUDIO) {
      return true;
    } else if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_P2P_CUS
        || message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_TRIBE_CUS) {
      Notification.showToastMsg(context, "你长按了自定义消息");
      return true;
    }
    return false;
  }

  public String getShowName(YWConversation conversation) {
    String conversationName;
    if (conversation == null) {
      return "";
    }

    if (conversation.getConversationType() == YWConversationType.Tribe) {
      conversationName =
          ((YWTribeConversationBody) conversation.getConversationBody()).getTribe().getTribeName();
    } else {
      IYWContact contact =
          ((YWP2PConversationBody) conversation.getConversationBody()).getContact();
      String userId = contact.getUserId();
      String appkey = contact.getAppKey();
      conversationName = userId;
      IYWCrossContactProfileCallback callback =
          WXAPI.getInstance().getCrossContactProfileCallback();
      if (callback != null) {
        IYWContact iContact = callback.onFetchContactInfo(userId, appkey);
        if (iContact != null && !TextUtils.isEmpty(iContact.getShowName())) {
          conversationName = iContact.getShowName();
          return conversationName;
        }
      } else {
        IYWContactProfileCallback profileCallback = WXAPI.getInstance().getContactProfileCallback();
        if (profileCallback != null) {
          IYWContact iContact = profileCallback.onFetchContactInfo(userId);
          if (iContact != null && !TextUtils.isEmpty(iContact.getShowName())) {
            conversationName = iContact.getShowName();
            return conversationName;
          }
        }
      }
      IYWContact iContact = WXAPI.getInstance().getWXIMContact(userId);
      if (iContact != null && !TextUtils.isEmpty(iContact.getShowName())) {
        conversationName = iContact.getShowName();
      }
    }
    return conversationName;
  }

  public static ISelectContactListener selectContactListener = new ISelectContactListener() {
    @Override public void onSelectCompleted(List<IYWContact> contacts) {
      if (contacts != null && contacts.size() > 0) {
        for (IYWContact contact : contacts) {
          sendP2PCustomMessage(contact.getUserId());
        }
      }
    }
  };

  @Override public int getFastReplyResId(YWConversation conversation) {
    return R.drawable.aliwx_reply_bar_face_bg;
  }

  @Override public boolean onFastReplyClick(Fragment pointcut, YWConversation ywConversation) {
    return false;
  }

  @Override public int getRecordResId(YWConversation conversation) {
    return 0;
  }

  @Override public boolean onRecordItemClick(Fragment pointcut, YWConversation ywConversation) {
    return false;
  }

  /**
   * 获取url对应的商品详情信息，当openIM发送或者接收到url消息时会首先调用{@link ChattingOperation#getCustomUrlView(Fragment,
   * YWMessage, String, YWConversation)},
   * 若getCustomUrlView()返回null，才会回调调用该方法获取商品详情，若getCustomUrlView()返回非null的view对象，则直接用此view展示url消息，不再回调该方法。因此，如果希望该方法被调用,
   * 请确保{@link ChattingOperation#getCustomUrlView(Fragment, YWMessage, String,
   * YWConversation)}返回null。
   *
   * @param fragment 可以通过 fragment.getActivity拿到Context
   * @param message url所属的message
   * @param url url
   * @param ywConversation message所属的conversion
   * @return 商品信息
   */
  @Override public GoodsInfo getGoodsInfoFromUrl(Fragment fragment, YWMessage message, String url,
      YWConversation ywConversation) {
    return null;
  }

  /**
   * 获取url对应的自定义view,当openIM发送或者接收到url消息时会回调该方法获取该url的自定义view。若开发者实现了该方法并且返回了一个view对象，openIM将会使用该view展示对应的url消息。
   *
   * @param fragment 可以通过 fragment.getActivity拿到Context
   * @param message url所属的message
   * @param url url
   * @param ywConversation message所属的conversion
   * @return 自定义Url view
   */
  @Override public View getCustomUrlView(Fragment fragment, YWMessage message, String url,
      YWConversation ywConversation) {
    // TODO: 2016-5-20-0020 暂时不做处理 
    return null;
  }

  /**
   * 开发者可以根据用户操作设置该值
   */
  private static boolean mUserInCallMode = false;

  /**
   * 是否使用听筒模式播放语音消息
   *
   * @return true：使用听筒模式， false：使用扬声器模式
   */
  @Override public boolean useInCallMode(Fragment fragment, YWMessage message) {
    return mUserInCallMode;
  }

  /**
   * 当打开聊天窗口时，自动发送该字符串给对方
   *
   * @param fragment 聊天窗口fragment
   * @param conversation 当前会话
   * @return 自动发送的内容（注意，内容empty则不自动发送）
   */
  @Override public String messageToSendWhenOpenChatting(Fragment fragment,
      YWConversation conversation) {
    //p2p、客服和店铺会话处理，否则不处理，
    int mCvsType = conversation.getConversationType().getValue();
    if (mCvsType == YWConversationType.P2P.getValue()
        || mCvsType == YWConversationType.SHOP.getValue()) {
      //            return "你好";
      return null;
    } else {
      return null;
    }
  }

  /**
   * 当打开聊天窗口时，自动发送该消息给对方
   *
   * @param fragment 聊天窗口fragment
   * @param conversation 当前会话
   * @param isConversationFirstCreated 是否是首次创建会话
   * @return 自动发送的消息（注意，内容empty则不自动发送）
   */
  @Override public YWMessage ywMessageToSendWhenOpenChatting(Fragment fragment,
      YWConversation conversation, boolean isConversationFirstCreated) {
    //        YWMessageBody messageBody = new YWMessageBody();
    //        messageBody.setSummary("WithoutHead");
    //        messageBody.setContent("hi，我是单聊自定义消息之好友名片");
    //        YWMessage message = YWMessageChannel.createCustomMessage(messageBody);
    //        return message;

    //与客服的会话
    if (conversation.getConversationId().contains("openim官方客服")) {
      //首次进入会话
      //            if(isConversationFirstCreated){

      final SharedPreferences defalultSprefs =
          fragment.getActivity().getSharedPreferences("ywPrefsTools", Context.MODE_PRIVATE);

      long lastSendTime =
          defalultSprefs.getLong("lastSendTime_" + conversation.getConversationId(), -1);
      //24小时后再次发送本地隐藏消息
      if (System.currentTimeMillis() - lastSendTime > 24 * 60 * 60 * 1000) {

        YWMessage textMessage = YWMessageChannel.createTextMessage("你好");
        //添加发送的消息不显示在对方界面上的本地标记（todo 仅支持本地隐藏。当用户切换手机或清楚数据后，会漫游消息下这些消息并出现在用户的聊天界面上！！）
        textMessage.setLocallyHideMessage(true);

        //保存发送时间戳
        SharedPreferences.Editor edit = defalultSprefs.edit();
        edit.putLong("lastSendTime_" + conversation.getConversationId(),
            System.currentTimeMillis());
        edit.apply();

        return textMessage;
      }

      //            }

    }
    //返回null,则不发送
    return null;
  }

  /***************** 以下是定制自定义消息view的示例代码 ****************/

  //自定义消息view的种类数
  private final int typeCount = 4;

  /** 自定义viewType，viewType的值必须从0开始，然后依次+1递增，且viewType的个数必须等于typeCount，切记切记！！！ ***/
  //地理位置消息
  private final int type_0 = 0;

  //群自定义消息(Say-Hi消息)
  private final int type_1 = 1;

  //单聊自定义消息(名片消息)
  private final int type_2 = 2;

  //单聊阅后即焚消息
  private final int type_3 = 3;

  /**
   * 自定义消息view的种类数
   *
   * @return 自定义消息view的种类数
   */
  @Override public int getCustomViewTypeCount() {
    return typeCount;
  }

  /**
   * 自定义消息view的类型，开发者可以根据自己的需求定制多种自定义消息view，这里需要根据消息返回view的类型
   *
   * @param message 需要自定义显示的消息
   * @return 自定义消息view的类型
   */
  @Override public int getCustomViewType(YWMessage message) {
    if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_GEO) {
      return type_0;
    } else if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_P2P_CUS
        || message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_TRIBE_CUS) {
      String msgType = null;
      try {
        String content = message.getMessageBody().getContent();
        JSONObject object = new JSONObject(content);
        msgType = object.getString("customizeMessageType");
      } catch (Exception e) {

      }
      if (!TextUtils.isEmpty(msgType)) {
        if (msgType.equals(CustomMessageType.GREETING)) {
          return type_1;
        } else if (msgType.equals(CustomMessageType.CARD)) {
          return type_2;
        } else if (msgType.equals(CustomMessageType.IMAGE)) {
          return type_3;
        }
      }
    }
    return super.getCustomViewType(message);
  }

  /**
   * 是否需要隐藏头像
   *
   * @param viewType 自定义view类型
   * @return true: 隐藏头像  false：不隐藏头像
   */
  @Override public boolean needHideHead(int viewType) {
    if (viewType == type_2) {
      return true;
    }
    return super.needHideHead(viewType);
  }

  /**
   * 是否需要隐藏显示名
   *
   * @param viewType 自定义view类型
   * @return true: 隐藏显示名  false：不隐藏显示名
   */
  @Override public boolean needHideName(int viewType) {
    if (viewType == type_2) {
      return true;
    }
    return super.needHideName(viewType);
  }

  /**
   * 根据viewType获取自定义view
   *
   * @param fragment 聊天窗口fragment
   * @param message 当前需要自定义view的消息
   * @param convertView 自定义view
   * @param viewType 自定义view的类型
   * @param headLoadHelper 头像加载管理器，用户可以调用该对象的方法加载头像
   * @return 自定义view
   */
  @Override public View getCustomView(Fragment fragment, YWMessage message, View convertView,
      int viewType, YWContactHeadLoadHelper headLoadHelper) {
    YWLog.i(TAG, "getCustomView, type = " + viewType);
    if (viewType == type_0) { //地理位置消息
      ViewHolder0 holder = null;
      if (convertView == null) {
        holder = new ViewHolder0();
        convertView = View.inflate(fragment.getActivity(), R.layout.im_geo_message_layout, null);
        holder.address = (TextView) convertView.findViewById(R.id.content);
        convertView.setTag(holder);
        YWLog.i(TAG, "getCustomView, convertView == null");
      } else {
        holder = (ViewHolder0) convertView.getTag();
        YWLog.i(TAG, "getCustomView, convertView != null");
      }
      YWGeoMessageBody messageBody = (YWGeoMessageBody) message.getMessageBody();
      holder.address.setText(messageBody.getAddress());
      return convertView;
    } else if (viewType == type_1) {  //群聊自定义消息(Say-Hi消息)

    } else if (viewType == type_2) {  //单聊自定义消息(名片消息)
      String userId = null;
      try {
        String content = message.getMessageBody().getContent();
        JSONObject object = new JSONObject(content);
        userId = object.getString("personId");
      } catch (Exception e) {
      }

      ViewHolder2 holder = null;
      if (convertView == null) {
        holder = new ViewHolder2();
        convertView =
            View.inflate(fragment.getActivity(), R.layout.im_custom_msg_layout_without_head, null);
        holder.head = (ImageView) convertView.findViewById(R.id.head);
        holder.name = (TextView) convertView.findViewById(R.id.name);
        convertView.setTag(holder);
        YWLog.i(TAG, "getCustomView, convertView == null");
      } else {
        holder = (ViewHolder2) convertView.getTag();
        YWLog.i(TAG, "getCustomView, convertView != null");
      }
      holder.name.setText(userId);

      YWIMKit imKit = LoginHelper.getInstance().getYWIMKit();
      IYWContact contact =
          imKit.getContactService().getContactProfileInfo(userId, imKit.getIMCore().getAppKey());
      if (contact != null) {
        String nick = contact.getShowName();
        if (!TextUtils.isEmpty(nick)) {
          holder.name.setText(nick);
        }
        String avatarPath = contact.getAvatarPath();
        if (avatarPath != null) {
          headLoadHelper.setCustomHeadView(holder.head, R.mipmap.ic_information, avatarPath);
        }
      }
      return convertView;
    }
    return super.getCustomView(fragment, message, convertView, viewType, headLoadHelper);
  }

  public class ViewHolder0 {
    TextView address;
  }

  public class ViewHolder1 {
    TextView greeting;
  }

  public class ViewHolder2 {
    ImageView head;
    TextView name;
  }

  public class ViewHolder3 {
    TextView left;
    TextView right;
  }

  /**************** 以上是定制自定义消息view的示例代码 ****************/

  /**
   * 双击放大文字消息的开关
   *
   * @return true:开启双击放大文字 false: 关闭双击放大文字
   */
  @Override public boolean enableDoubleClickEnlargeMessageText(Fragment fragment) {
    return true;
  }

  /**
   * 数字字符串点击事件,开发者可以根据自己的需求定制
   *
   * @param clickString 被点击的数字string
   * @param widget 被点击的TextView
   * @return false:不处理
   * true:需要开发者在return前添加自己实现的响应逻辑代码
   */
  @Override public boolean onNumberClick(final Activity activity, final String clickString,
      final View widget) {
    ArrayList<String> menuList = new ArrayList<String>();
    menuList.add("呼叫");
    menuList.add("添加到手机通讯录");
    menuList.add("复制到剪贴板");
    final String[] items = new String[menuList.size()];
    menuList.toArray(items);
    MaterialDialog materialDialog = new MaterialDialog.Builder(activity).items(menuList)
        .itemsCallback(new MaterialDialog.ListCallback() {
          @Override public void onSelection(MaterialDialog dialog, View itemView, int which,
              CharSequence text) {
            if (TextUtils.equals(items[which], "呼叫")) {
              Intent intent = new Intent(Intent.ACTION_DIAL);
              intent.setData(Uri.parse("tel:" + clickString));
              activity.startActivity(intent);
            } else if (TextUtils.equals(items[which], "添加到手机通讯录")) {
              Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
              intent.setType("vnd.android.cursor.item/person");
              intent.setType("vnd.android.cursor.item/contact");
              intent.setType("vnd.android.cursor.item/raw_contact");
              intent.putExtra(android.provider.ContactsContract.Intents.Insert.PHONE, clickString);
              activity.startActivity(intent);
            } else if (TextUtils.equals(items[which], "复制到剪贴板")) {
              ClipboardManager clipboardManager =
                  (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
              clipboardManager.setText(clickString);
            }
          }
        })
        .build();

    materialDialog.setCancelable(true);
    materialDialog.setCanceledOnTouchOutside(true);
    materialDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
      @Override public void onDismiss(DialogInterface dialog) {
        widget.invalidate();
      }
    });
    if (!materialDialog.isShowing()) {
      materialDialog.show();
    }
    return true;
  }

  //TODO 不要使用60000之前的值，防止和SDK中使用的产生冲突
  private static final int CAMERA_WITH_DATA = 60001;
  private static final int PHOTO_PICKED_WITH_DATA = 60002;
  public static final int IMAGE_CAMERA_LOCATION_DATA = 60003;
  private static final int IMAGE_PHOTO_PICKED_WITH_DATA = 60004;

  /**
   * 请注意不要和内部的ID重合
   * {@link YWChattingPlugin.ReplyBarItem#ID_CAMERA}
   * {@link YWChattingPlugin.ReplyBarItem#ID_ALBUM}
   * {@link YWChattingPlugin.ReplyBarItem#ID_SHORT_VIDEO}
   */
  private static int ITEM_ID_1 = 0x1;
  private static int ITEM_ID_2 = 0x2;
  private static int ITEM_ID_3 = 0X3;
  private static int ITEM_ID_4 = 0x04;

  /**
   * 用于增加聊天窗口 下方回复栏的操作区的item
   *
   * ReplyBarItem
   * itemId:唯一标识 建议从1开始
   * ItemImageRes：显示的图片
   * ItemLabel：文字
   * needHide:是否隐藏 默认: false ,  显示：false ， 隐藏：true
   * OnClickListener: 自定义点击事件, null则使用默认的点击事件
   * 参照示例返回List<ReplyBarItem>用于操作区显示item列表，可以自定义顺序和添加item
   *
   * @param pointcut 聊天窗口fragment
   * @param conversation 当前会话，通过conversation.getConversationType() 区分个人单聊，与群聊天
   * @param replyBarItemList 默认的replyBarItemList，如拍照、选择照片、短视频等
   */
  @Override public List<ReplyBarItem> getCustomReplyBarItemList(final Fragment pointcut,
      final YWConversation conversation, List<ReplyBarItem> replyBarItemList) {
    List<ReplyBarItem> replyBarItems = new ArrayList<ReplyBarItem>();
    mConversation = conversation;
    for (ReplyBarItem replyBarItem : replyBarItemList) {
      if (replyBarItem.getItemId() == YWChattingPlugin.ReplyBarItem.ID_CAMERA) {
        //是否隐藏ReplyBarItem中的拍照选项
        replyBarItem.setNeedHide(true);
        //不自定义ReplyBarItem中的拍照的点击事件,设置OnClicklistener(null);
        replyBarItem.setOnClicklistener(null);
      } else if (replyBarItem.getItemId() == YWChattingPlugin.ReplyBarItem.ID_ALBUM) {
        //是否隐藏ReplyBarItem中的选择照片选项
        replyBarItem.setNeedHide(true);
        //不自定义ReplyBarItem中的相册的点击事件,设置OnClicklistener（null）
        replyBarItem.setOnClicklistener(null);
      } else if (replyBarItem.getItemId() == YWChattingPlugin.ReplyBarItem.ID_SHORT_VIDEO) {
        //检查是否集成了短视频SDK
        if (!haveShortVideoLibrary()) {
          //是否隐藏ReplyBarItem中的短视频选项
          replyBarItem.setNeedHide(true);
        }
      }
      replyBarItems.add(replyBarItem);
    }
    if (conversation.getConversationType() == YWConversationType.P2P
        || conversation.getConversationType() == YWConversationType.Tribe) {

      ReplyBarItem replyBarItem1 = new ReplyBarItem();
      replyBarItem1.setItemId(ITEM_ID_1);
      replyBarItem1.setItemImageRes(R.drawable.ic_im_image);
      replyBarItem1.setItemLabel("照片");
      replyBarItem1.setOnClicklistener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          // TODO: 2016-5-20-0020 添加照片相关操作
          Log.d(TAG, "onClick: " + "添加照片相关操作");
          mContext = v.getContext();
          startMediaPicker((Activity) v.getContext(), false);
        }
      });
      replyBarItems.add(0, replyBarItem1);
      ReplyBarItem replyBarItem2 = new ReplyBarItem();
      replyBarItem2.setItemId(ITEM_ID_2);
      replyBarItem2.setItemImageRes(R.drawable.ic_im_camera);
      replyBarItem2.setItemLabel("拍摄");
      replyBarItem2.setOnClicklistener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          // TODO: 2016-5-20-0020 添加拍摄相关操作
          mContext = v.getContext();
          startMediaPicker((Activity) v.getContext(), true);
        }
      });
      replyBarItems.add(1, replyBarItem2);

      ReplyBarItem replyBarItem3 = new ReplyBarItem();
      replyBarItem3.setItemId(ITEM_ID_3);
      replyBarItem3.setItemImageRes(R.drawable.ic_im_location);
      replyBarItem3.setItemLabel("位置");
      replyBarItem3.setOnClicklistener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          // TODO: 2016-5-20-0020  添加位置相关操作
          mContext = v.getContext();
          Navigator.startMapActivity((Activity) v.getContext(), IMAGE_CAMERA_LOCATION_DATA);
        }
      });
      replyBarItems.add(replyBarItem3);
    }

    return replyBarItems;
  }

  /**
   * 打开相册
   */
  public void startMediaPicker(Activity activity, boolean isTake) {
    MediaOptions.Builder builder = new MediaOptions.Builder();
    MediaOptions options = builder.setIsCropped(false)
        .setFixAspectRatio(true)
        .canSelectMultiPhoto(true)
        .setImageSize(9)
        .build();
    MediaPickerActivity.open(activity, PHOTO_PICKED_WITH_DATA, options, isTake);
  }

  private static YWConversation mConversation;

  /**
   * 如果开发者选择自己实现拍照或者选择照片的流程，则可以在该方法中实现照片(图片)的发送操作
   *
   * @param messageList 开发者构造图片消息并赋值给message参数，sdk会把该消息发送出去
   * @return 开发者在自己实现拍照处理或者选择照片时，一定要return true
   */
  public boolean onActivityResult(int requestCode, int resultCode, Intent data,
      List<YWMessage> messageList) {
    if (resultCode == Activity.RESULT_OK) {
      switch (requestCode) {
        case PHOTO_PICKED_WITH_DATA:
          ArrayList<MediaItem> mMediaSelectedList = new ArrayList<>();
          mMediaSelectedList.clear();
          mMediaSelectedList.addAll(MediaPickerActivity.getMediaItemSelected(data));
          for (MediaItem mediaItem : mMediaSelectedList) {
            //调用上传图片
            YWMessage message =
                YWMessageChannel.createImageMessage(mediaItem.getPathOrigin(mContext),
                    mediaItem.getPathOrigin(mContext), 200, 200,
                    FileUtil.getFileSize(mediaItem, mContext),
                    FileUtil.imageType(mContext, mediaItem.getUriOrigin()),
                    YWEnum.SendImageResolutionType.BIG_IMAGE);
            message.setNeedSave(true);
            mConversation.getMessageSender().sendMessage(message, 360, null);
          }
          break;
        case IMAGE_CAMERA_LOCATION_DATA:
          //定位
          double latitude = data.getDoubleExtra("latitude", 0);
          double longitude = data.getDoubleExtra("longitude", 0);
          String address = data.getStringExtra("address");
          sendGeoMessage(mConversation, latitude, longitude, address);
      }
    }
    return true;
  }

  private static boolean compiledShortVideoLibrary = false;
  private static boolean haveCheckedShortVideoLibrary = false;

  /**
   * 检查是否集成了集成了短视频的SDK
   */
  private boolean haveShortVideoLibrary() {
    if (!haveCheckedShortVideoLibrary) {
      try {
        Class.forName("com.im.IMRecordVideoActivity");
        compiledShortVideoLibrary = true;
        haveCheckedShortVideoLibrary = true;
      } catch (ClassNotFoundException e) {
        compiledShortVideoLibrary = false;
        haveCheckedShortVideoLibrary = true;
        e.printStackTrace();
      }
    }
    return compiledShortVideoLibrary;
  }

  /**
   * 自定义时间文案
   *
   * @param fragment 聊天窗口fragment
   * @param conversation 当前聊天窗口对应的会话
   * @param time 默认时间文案
   * @return 如果是NULL，则不显示，如果是空字符串，则使用SDK默认的文案，如果返回非空串，则使用用户自定义的
   */
  @Override public String getCustomTimeString(Fragment fragment, YWConversation conversation,
      String time) {
    return "";
  }

  /**
   * 自定义系统消息文案
   *
   * @param fragment 聊天窗口fragment
   * @param conversation 当前聊天窗口对应的会话
   * @param content 默认系统消息文案
   * @return 如果是NULL，则不显示，如果是空字符串，则使用SDK默认的文案，如果返回非空串，则使用用户自定义的
   */
  @Override public String getSystemMessageContent(Fragment fragment, YWConversation conversation,
      String content) {
    return "";
  }
}