package tech.honc.android.apps.soldier.feature.im.ui.activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.conversation.IYWMessageListener;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.lib.model.message.YWSystemMessage;
import com.alibaba.mobileim.tribe.IYWTribeService;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.ArrayList;
import java.util.List;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.feature.im.common.Constant;
import tech.honc.android.apps.soldier.feature.im.helper.LoginHelper;
import tech.honc.android.apps.soldier.feature.im.ui.adapter.TribeSystemMessageAdapter;
import tech.honc.android.apps.soldier.ui.activity.BaseActivity;

/**
 * Created by wangh on 2016-3-25-0025.
 */
public class SystemAddTribeActivity extends BaseActivity {

  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.message_list) ListView mListView;
  @Bind(R.id.empty_container) RelativeLayout mEmptyContainer;
  @Bind(R.id.sv_empty) SimpleDraweeView mSvEmptyImg;
  @Bind(R.id.title) TextView mEmptyTitle;
  @Bind(R.id.summary) TextView mEmptySummary;

  private YWIMKit mIMKit;
  private TribeSystemMessageAdapter mAdapter;
  private IYWTribeService mTribeService;
  private YWConversation mConversation;
  private List<YWMessage> mList = new ArrayList<YWMessage>();
  private Handler mUiHandler = new Handler(Looper.getMainLooper());

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.im_activity_sys_notifiy_msg);
    init();
  }

  private void initToolbar() {
    initEmpty();
    mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
    mToolbar.setTitleTextColor(Color.WHITE);
    mToolbar.setTitle("群组消息");
    setSupportActionBar(mToolbar);
  }

  private void initEmpty() {
    mEmptyContainer.setVisibility(View.GONE);
    mEmptyTitle.setText("什么都没有");
    mEmptySummary.setText("还没有收到群组消息噢");
    mSvEmptyImg.setImageURI(new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
        .path(String.valueOf(R.mipmap.ic_default_empty)).build());
  }

  private void init() {
    mIMKit = LoginHelper.getInstance().getYWIMKit();
    mTribeService = mIMKit.getTribeService();
    mConversation = mIMKit.getConversationService()
        .getConversationByConversationId(Constant.SYSTEM_TRIBE_CONVERSATION);
    mIMKit.getConversationService().markReaded(mConversation);
    initToolbar();
    mList = mConversation.getMessageLoader().loadMessage(20, null);
    mAdapter = new TribeSystemMessageAdapter(this, mList);
    mListView.setAdapter(mAdapter);
    //添加新消息到达监听,监听到有新消息到达的时候或者消息类别有变更的时候应该更新adapter
    mConversation.getMessageLoader().addMessageListener(mMessageListener);
  }

  private void refreshAdapter() {
    mUiHandler.post(new Runnable() {
      @Override public void run() {
        mAdapter.refreshData(mList);
      }
    });
  }

  IYWMessageListener mMessageListener = new IYWMessageListener() {
    @Override public void onItemUpdated() {  //消息列表变更，例如删除一条消息，修改消息状态，加载更多消息等等
      mUiHandler.post(new Runnable() {
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
      mUiHandler.post(new Runnable() {
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

    @Override public void onInputStatus(byte status) {

    }
  };

  public void acceptToJoinTribe(final YWMessage message) {
    final YWSystemMessage msg = (YWSystemMessage) message;
    YWIMKit imKit = LoginHelper.getInstance().getYWIMKit();
    if (imKit != null) {
      IYWTribeService tribeService = imKit.getTribeService();
      if (tribeService != null) {
        tribeService.accept(new IWxCallback() {
          @Override public void onSuccess(Object... result) {
            Boolean isSuccess = (Boolean) result[0];
            if (isSuccess) {
              msg.setSubType(YWSystemMessage.SYSMSG_TYPE_AGREE);
              refreshAdapter();
              mTribeService.updateTribeSystemMessage(msg);
            }
          }

          @Override public void onError(int code, String info) {

          }

          @Override public void onProgress(int progress) {

          }
        }, Long.valueOf(msg.getAuthorId()), msg.getRecommender());
      }
    }
  }

  public void refuseToJoinTribe(YWMessage message) {
    final YWSystemMessage msg = (YWSystemMessage) message;
    msg.setSubType(YWSystemMessage.SYSMSG_TYPE_IGNORE);
    refreshAdapter();
    mTribeService.updateTribeSystemMessage(msg);
  }

  @Override protected void onDestroy() {
    mConversation.getMessageLoader().removeMessageListener(mMessageListener);
    super.onDestroy();
  }
}
