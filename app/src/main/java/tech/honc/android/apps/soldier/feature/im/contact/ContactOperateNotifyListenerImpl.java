package tech.honc.android.apps.soldier.feature.im.contact;

import android.content.Intent;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.IYWContactOperateNotifyListener;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWCustomConversationBody;
import com.alibaba.mobileim.conversation.YWCustomConversationUpdateModel;
import java.util.Date;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.FriendService;
import tech.honc.android.apps.soldier.feature.im.common.Notification;
import tech.honc.android.apps.soldier.feature.im.helper.ContactAsyncTask;
import tech.honc.android.apps.soldier.feature.im.helper.LoginHelper;
import tech.honc.android.apps.soldier.feature.im.helper.NotificationHelper;
import tech.honc.android.apps.soldier.feature.im.interfaces.ContactTranform;
import tech.honc.android.apps.soldier.feature.im.ui.activity.SystemAddContactActivity;
import tech.honc.android.apps.soldier.model.Status;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.ui.app.SoldierApp;

/**
 * Created by kevin on 16/2/26.
 */
public class ContactOperateNotifyListenerImpl
    implements IYWContactOperateNotifyListener, ContactTranform {

  private FriendService mFriendService;
  private static final String SYSTEM_FRIEND_REQ_CONVERSATION = "request";
  private User mUser;

  /**
   * 用户请求加你为好友
   * todo 该回调在UI线程回调 ，请勿做太重的操作
   *
   * @param contact 用户的信息
   * @param message 附带的备注
   */
  @Override public void onVerifyAddRequest(IYWContact contact, String message) {
    ContactAsyncTask.getInstance().getIMProfile(contact.getUserId(), this);
    if (mUser != null) {
      Notification.showToastMsg(SoldierApp.getContext(), mUser.nickname + "请求加你为好友");
      showNotifyCation();
      Intent intent = new Intent(SoldierApp.getContext(), SystemAddContactActivity.class);
      NotificationHelper.showNotificationBySys("加好友", mUser.nickname + "请求加你为好友", intent);
    }
  }

  /**
   * 用户接受了你的好友请求
   * todo 该回调在UI线程回调 ，请勿做太重的操作
   *
   * @param contact 用户的信息
   */
  @Override public void onAcceptVerifyRequest(IYWContact contact) {
    ContactAsyncTask.getInstance().getIMProfile(contact.getUserId(), this);
    if (mUser != null) {
      Notification.showToastMsg(SoldierApp.getContext(), mUser.nickname + "接受了你的好友请求");
      showNotifyCation();
      //将数据传给服务器
      mFriendService = ApiService.createFriendService();
      Call<Status> call = mFriendService.addFriends(contact.getUserId());
      call.enqueue(new Callback<Status>() {
        @Override public void onResponse(Call<Status> call, Response<Status> response) {
        }

        @Override public void onFailure(Call<Status> call, Throwable t) {

        }
      });
    }
  }

  /**
   * 用户拒绝了你的好友请求
   * todo 该回调在UI线程回调 ，请勿做太重的操作
   *
   * @param contact 用户的信息
   */
  @Override public void onDenyVerifyRequest(IYWContact contact) {
    ContactAsyncTask.getInstance().getIMProfile(contact.getUserId(), this);
    if (mUser != null) {
      Notification.showToastMsg(SoldierApp.getContext(), mUser.nickname + "拒绝了你的好友请求");
      showNotifyCation();
    }
  }

  /**
   * 云旺服务端（或其它终端）进行了好友添加操作
   * todo 该回调在UI线程回调 ，请勿做太重的操作
   *
   * @param contact 用户的信息
   */
  @Override public void onSyncAddOKNotify(IYWContact contact) {
    ContactAsyncTask.getInstance().getIMProfile(contact.getUserId(), this);
    if (mUser != null) {
      Notification.showToastMsg(SoldierApp.getContext(), "添加好友" + mUser.nickname + "成功");
      //通知刷新
      showNotifyCation();
    }
  }

  /**
   * 用户从好友名单删除了您
   * todo 该回调在UI线程回调 ，请勿做太重的操作
   *
   * @param contact 用户的信息
   */
  @Override public void onDeleteOKNotify(IYWContact contact) {
    ContactAsyncTask.getInstance().getIMProfile(contact.getUserId(), this);
    if (mUser != null) {
      Notification.showToastMsg(SoldierApp.getContext(), mUser.nickname + "删除了您");
      showNotifyCation();
    }
  }

  @Override public void onNotifyAddOK(IYWContact contact) {
    ContactAsyncTask.getInstance().getIMProfile(contact.getUserId(), this);
    if (mUser != null) {
      Notification.showToastMsg(SoldierApp.getContext(), mUser.nickname + "添加您为好友了");
      showNotifyCation();
    }
  }

  @Override public void ContactTranformCallback(User user) {
    mUser = user;
  }

  public void showNotifyCation() {
    YWConversation conversation = LoginHelper.getInstance()
        .getYWIMKit()
        .getConversationService()
        .getCustomConversationByConversationId(SYSTEM_FRIEND_REQ_CONVERSATION);
    if (conversation != null) {
      YWCustomConversationUpdateModel model = new YWCustomConversationUpdateModel();
      model.setIdentity(SYSTEM_FRIEND_REQ_CONVERSATION);
      model.setLastestTime(new Date().getTime());
      model.setUnreadCount(conversation.getUnreadCount() + 1);
      if (conversation.getConversationBody() instanceof YWCustomConversationBody) {
        model.setExtraData(
            ((YWCustomConversationBody) conversation.getConversationBody()).getExtraData());
      }
      if (LoginHelper.getInstance().getYWIMKit().getConversationService() != null) {
        LoginHelper.getInstance()
            .getYWIMKit()
            .getConversationService()
            .updateOrCreateCustomConversation(model);
      }
    }
  }
}
