package tech.honc.android.apps.soldier.feature.im.ui.activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContactService;
import com.alibaba.mobileim.conversation.IYWMessageListener;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.lib.model.message.YWSystemMessage;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.ArrayList;
import java.util.List;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.feature.im.common.Constant;
import tech.honc.android.apps.soldier.feature.im.helper.CustomConversationHelper;
import tech.honc.android.apps.soldier.feature.im.helper.LoginHelper;
import tech.honc.android.apps.soldier.feature.im.ui.adapter.ContactSystemMessageAdapter;
import tech.honc.android.apps.soldier.ui.activity.BaseActivity;

/**
 * Created by wangh on 2016-3-21-0021.
 */

public class SystemAddContactActivity extends BaseActivity {

  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.message_list) ListView mListView;
  @Bind(R.id.empty_container) RelativeLayout mEmptyContainer;
  @Bind(R.id.sv_empty) SimpleDraweeView mSvEmptyImg;
  @Bind(R.id.title) TextView mEmptyTitle;
  @Bind(R.id.summary) TextView mEmptySummary;

  private YWIMKit mIMKit;
  private YWConversation mConversation;
  private List<YWMessage> mList;
  private Handler mHandler = new Handler(Looper.getMainLooper());
  private ContactSystemMessageAdapter mAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.im_activity_sys_notifiy_msg);
    init();
    //NotifiyUtil.clearNotifiyById(this, NotifiyUtil.FLAG_ID_ADD_CONTACT_RESPON);
    //NotifiyUtil.clearNotifiyById(this, NotifiyUtil.FLAG_ID_ADD_CONTACT_REQ);
  }

  private void initTitle() {
    mToolbar.setTitle("新的朋友");
    mToolbar.setTitleTextColor(Color.WHITE);
    mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        SystemAddContactActivity.this.finish();
      }
    });
  }

  private void initIM() {
    mIMKit = LoginHelper.getInstance().getYWIMKit();
    mConversation = mIMKit.getConversationService()
        .getCustomConversationByConversationId(Constant.SYSTEM_FRIEND_REQ_CONVERSATION);
    if (mConversation == null) {
      CustomConversationHelper.addCustomConversation(Constant.SYSTEM_TRIBE_CONVERSATION, null);
      CustomConversationHelper.addCustomConversation(Constant.SYSTEM_FRIEND_REQ_CONVERSATION, null);
      mConversation = mIMKit.getConversationService()
          .getCustomConversationByConversationId(Constant.SYSTEM_FRIEND_REQ_CONVERSATION);
    }
  }

  private void init() {
    initTitle();
    initIM();
    initEmpty();
    mList = new ArrayList<>();
    mList = mConversation.getMessageLoader().loadMessage(20, null);
    mAdapter = new ContactSystemMessageAdapter(this, mList);
    mIMKit.getConversationService().markReaded(mConversation);
    mConversation.getMessageLoader().addMessageListener(mMessageListener);
    mListView.setAdapter(mAdapter);
    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO: 单个item点击事件  
      }
    });
  }

  private void initEmpty() {
    mEmptyContainer.setVisibility(View.GONE);
    mEmptyTitle.setText("什么都没有");
    mEmptySummary.setText("还没有收到谁的好友请求噢");
    mSvEmptyImg.setImageURI(new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
        .path(String.valueOf(R.mipmap.ic_default_empty)).build());
  }

  IYWMessageListener mMessageListener = new IYWMessageListener() {
    @Override public void onItemUpdated() {  //消息列表变更，例如删除一条消息，修改消息状态，加载更多消息等等
      mHandler.post(new Runnable() {
        @Override public void run() {
          mAdapter.notifyDataSetChangedWithAsyncLoad();
          if (mAdapter.getCount() > 0) {
            mEmptyContainer.setVisibility(View.GONE);
          } else {
            mEmptyContainer.setVisibility(View.VISIBLE);
          }
        }
      });
    }

    @Override public void onItemComing() { //收到新消息
      mHandler.post(new Runnable() {
        @Override public void run() {
          mAdapter.notifyDataSetChangedWithAsyncLoad();
          if (mIMKit != null && mIMKit.getConversationService() != null) {
            mIMKit.getConversationService().markReaded(mConversation);
          }
          if (mAdapter.getCount() > 0) {
            mEmptyContainer.setVisibility(View.GONE);
          } else {
            mEmptyContainer.setVisibility(View.VISIBLE);
          }
        }
      });
    }

    @Override public void onInputStatus(byte status) {

    }
  };

  @Override protected void onDestroy() {
    super.onDestroy();
    mConversation.getMessageLoader().removeMessageListener(mMessageListener);
  }

  private void refreshAdapter() {
    mHandler.post(new Runnable() {
      @Override public void run() {
        mAdapter.refreshData(mList);
      }
    });
  }

  private IYWContactService getContactService() {
    return LoginHelper.getInstance().getYWIMKit().getContactService();
  }

  public void acceptToBecomeFriend(final YWMessage message) {
    final YWSystemMessage msg = (YWSystemMessage) message;
    if (getContactService() != null) {
      getContactService().ackAddContact(message.getAuthorUserId(), message.getAuthorAppkey(), true,
          "", new IWxCallback() {
            @Override public void onSuccess(Object... result) {
              msg.setSubType(YWSystemMessage.SYSMSG_TYPE_AGREE);
              refreshAdapter();
              getContactService().updateContactSystemMessage(msg);
            }

            @Override public void onError(int code, String info) {

            }

            @Override public void onProgress(int progress) {

            }
          });
    }
  }

  public void refuseToBecomeFriend(YWMessage message) {
    final YWSystemMessage msg = (YWSystemMessage) message;

    if (getContactService() != null) {
      getContactService().ackAddContact(message.getAuthorUserId(), message.getAuthorAppkey(), false,
          "", new IWxCallback() {
            @Override public void onSuccess(Object... result) {
              msg.setSubType(YWSystemMessage.SYSMSG_TYPE_IGNORE);
              refreshAdapter();
              getContactService().updateContactSystemMessage(msg);
            }

            @Override public void onError(int code, String info) {

            }

            @Override public void onProgress(int progress) {

            }
          });
    }
  }
}
