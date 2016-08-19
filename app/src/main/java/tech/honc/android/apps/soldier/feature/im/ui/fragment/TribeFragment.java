package tech.honc.android.apps.soldier.feature.im.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.fundamental.widget.refreshlist.YWPullToRefreshBase;
import com.alibaba.mobileim.fundamental.widget.refreshlist.YWPullToRefreshListView;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeType;
import com.alibaba.mobileim.tribe.IYWTribeService;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.ArrayList;
import java.util.List;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.feature.im.group.TribeConstants;
import tech.honc.android.apps.soldier.feature.im.helper.LoginHelper;
import tech.honc.android.apps.soldier.feature.im.helper.TribeHelper;
import tech.honc.android.apps.soldier.feature.im.ui.activity.EditTribeInfoActivity;
import tech.honc.android.apps.soldier.feature.im.ui.adapter.TribeAdapter;
import tech.honc.android.apps.soldier.feature.im.ui.adapter.TribeAndRoomList;
import tech.honc.android.apps.soldier.ui.fragment.BaseFragment;
import tech.honc.android.apps.soldier.utils.toolsutils.SnackBarUtil;

/**
 * Created by wangh on 2016-3-23-0023.
 * 群组
 */
public class TribeFragment extends BaseFragment
    implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.message_list) YWPullToRefreshListView mPullToRefreshListView;
  @Bind(R.id.empty_container) RelativeLayout mEmptyContainer;
  @Bind(R.id.sv_empty) SimpleDraweeView mEmptyImg;
  @Bind(R.id.title) TextView mEmptyTitle;
  @Bind(R.id.summary) TextView mEmptySummmary;

  private YWIMKit mIMKit;
  private String mUserId;
  protected static final long POST_DELAYED_TIME = 300;
  private static final String TAG = "TribeFragment";
  private final Handler handler = new Handler();
  private Activity mContext;
  private IYWTribeService mTribeService;
  private ListView mMessageListView; // 消息列表视图
  private TribeAdapter adapter;
  private int max_visible_item_count = 0; // 当前页面列表最多显示个数
  private TribeAndRoomList mTribeAndRoomList;
  private List<YWTribe> mList;
  private List<YWTribe> mTribeList;
  private List<YWTribe> mRoomsList;
  private Runnable cancelRefresh = new Runnable() {
    @Override public void run() {
      if (mPullToRefreshListView != null) {
        mPullToRefreshListView.onRefreshComplete(false, false, R.string.aliwx_sync_failed);
      }
    }
  };

  public IYWTribeService getTribeService() {
    if (mTribeService == null) {
      mIMKit = LoginHelper.getInstance().getYWIMKit();
      mTribeService = mIMKit.getTribeService();
    }
    return mTribeService;
  }

  private void initIM() {
    mIMKit = LoginHelper.getInstance().getYWIMKit();
    mUserId = mIMKit.getIMCore().getLoginUserId();
    if (TextUtils.isEmpty(mUserId)) {
      SnackBarUtil.showText(getActivity(), "当前用户还没有登录，载入群信息失败");
    }
    mTribeService = getTribeService();
  }

  private void initToolbar() {
    mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getActivity().finish();
      }
    });
    mToolbar.setTitleTextColor(Color.WHITE);
    mToolbar.setTitle("群组");
    ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
  }

  private void initEmptyView() {
    mEmptyImg.setImageURI(new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
        .path(String.valueOf(R.mipmap.ic_default_empty)).build());
    mEmptyTitle.setText("没有加入任何群组");
    mEmptySummmary.setText("快去创建或者加入群组和大家聊聊吧~");
    mEmptyContainer.setVisibility(View.GONE);
  }

  private void initView() {
    mContext.getWindow().setWindowAnimations(0);
    initToolbar();
    initEmptyView();
    mMessageListView = mPullToRefreshListView.getRefreshableView();
    mPullToRefreshListView.setMode(YWPullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
    mPullToRefreshListView.setShowIndicator(false);
    mPullToRefreshListView.setDisableScrollingWhileRefreshing(false);
    mPullToRefreshListView.setRefreshingLabel("同步群组");
    mPullToRefreshListView.setReleaseLabel("松开同步群组");
    mPullToRefreshListView.resetHeadLayout();
    mPullToRefreshListView.setDisableRefresh(false);
    mPullToRefreshListView.setOnRefreshListener(new YWPullToRefreshBase.OnRefreshListener() {
      @Override public void onRefresh() {
        handler.postDelayed(new Runnable() {
          @Override public void run() {
            handler.removeCallbacks(cancelRefresh);
            IYWTribeService tribeService = TribeHelper.getTribeService();
            if (tribeService != null) {
              tribeService.getAllTribesFromServer(new IWxCallback() {
                @Override public void onSuccess(Object... arg0) {
                  // 返回值为列表
                  mList.clear();
                  mList.addAll((ArrayList<YWTribe>) arg0[0]);
                  if (mList.size() > 0) {
                    mTribeList.clear();
                    mRoomsList.clear();
                    for (YWTribe tribe : mList) {
                      if (tribe.getTribeType() == YWTribeType.CHATTING_TRIBE) {
                        mTribeList.add(tribe);
                      } else {
                        mRoomsList.add(tribe);
                      }
                    }
                  }
                  mPullToRefreshListView.onRefreshComplete(false, true,
                      R.string.aliwx_sync_success);
                  refreshAdapter();
                }

                @Override public void onError(int code, String info) {
                  mPullToRefreshListView.onRefreshComplete(false, false,
                      R.string.aliwx_sync_failed);
                }

                @Override public void onProgress(int progress) {

                }
              });
            }
          }
        }, POST_DELAYED_TIME);
      }
    });
  }

  private void initData() {
    mList = new ArrayList<>();
    mTribeList = new ArrayList<>();
    mRoomsList = new ArrayList<>();
    mTribeAndRoomList = new TribeAndRoomList(mTribeList, mRoomsList);
    adapter = new TribeAdapter(mContext, mTribeAndRoomList);
  }

  private void initAll() {
    mContext = getActivity();
    initIM();
    initData();
    initView();
  }

  /**
   * 刷新当前列表
   */
  private void refreshAdapter() {
    if (adapter != null) {
      adapter.notifyDataSetChangedWithAsyncLoad();
    }
  }

  /**
   * 初始化群列表行为
   */
  private void initTribeListView() {
    if (mMessageListView != null) {
      mMessageListView.setAdapter(adapter);
      mMessageListView.setOnItemClickListener(this);
      mMessageListView.setOnScrollListener(this);
    }

    getAllTribesFromServer();
  }

  @Override public void onStart() {
    super.onStart();
    refreshAdapter();
  }

  private void getAllTribesFromServer() {
    getTribeService().getAllTribesFromServer(new IWxCallback() {
      @Override public void onSuccess(Object... arg0) {
        // 返回值为列表
        mList.clear();
        mList.addAll((ArrayList<YWTribe>) arg0[0]);
        updateTribeList();
        dismissHud();
      }

      @Override public void onError(int code, String info) {
        dismissHud();
      }

      @Override public void onProgress(int progress) {
        showHud("正在从服务器获取数据....");
      }
    });
  }

  private void updateTribeList() {
    mTribeList.clear();
    mRoomsList.clear();
    if (mList.size() > 0&&mEmptyContainer!=null)
    {
      mEmptyContainer.setVisibility(View.GONE);
      for (YWTribe tribe : mList) {
        if (tribe.getTribeType() == YWTribeType.CHATTING_TRIBE) {
          mTribeList.add(tribe);
        } else {
          mRoomsList.add(tribe);
        }
      }
    } else {
      if (mEmptyContainer!=null) {
        mEmptyContainer.setVisibility(View.VISIBLE);
      }
    }
    refreshAdapter();
  }

  @Override protected int getFragmentLayout() {
    return R.layout.im_fragment_tribe;
  }

  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    final int newPosition = position - mMessageListView.getHeaderViewsCount();
    if (newPosition >= 0 && newPosition < mTribeAndRoomList.size()) {

      YWTribe tribe = (YWTribe) mTribeAndRoomList.getItem(newPosition);
      YWIMKit imKit = LoginHelper.getInstance().getYWIMKit();
      //参数为群ID号
      Intent intent = imKit.getTribeChattingActivityIntent(tribe.getTribeId());
      startActivity(intent);
    }
  }

  @Override public void onScrollStateChanged(AbsListView view, int scrollState) {
    if (scrollState == SCROLL_STATE_IDLE && adapter != null) {
      adapter.loadAsyncTask();
    }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    mList = mTribeService.getAllTribes();
    updateTribeList();
  }

  @Override public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
      int totalItemCount) {

    max_visible_item_count =
        visibleItemCount > max_visible_item_count ? visibleItemCount : max_visible_item_count;
    if (adapter != null) {
      adapter.setMax_visible_item_count(max_visible_item_count);
    }
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.im_menu_fragment_tribe, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      //todo:移除搜索群
      //case R.id.action_search_tribe:
      //  Intent intent0 = new Intent(getActivity(), SearchUserActivity.class);
      //  intent0.putExtra(SearchUserActivity.QUERY_TYPE_FLAG, SearchUserActivity.SEARCH_TYPE_TRIBE);
      //  getActivity().startActivity(intent0);
      //  return true;
      case R.id.action_create_tribe:
        Intent intent1 = new Intent(getActivity(), EditTribeInfoActivity.class);
        intent1.putExtra(TribeConstants.TRIBE_OP, TribeConstants.TRIBE_CREATE);
        intent1.putExtra(TribeConstants.TRIBE_TYPE, YWTribeType.CHATTING_TRIBE.toString());
        startActivityForResult(intent1, 0);
        return true;
      /*todo:移除创建讨论组
      case R.id.action_create_discuss:
        Intent intent2 = new Intent(getActivity(), EditTribeInfoActivity.class);
        intent2.putExtra(TribeConstants.TRIBE_OP, TribeConstants.TRIBE_CREATE);
        intent2.putExtra(TribeConstants.TRIBE_TYPE, YWTribeType.CHATTING_GROUP.toString());
        startActivityForResult(intent2, 0);
        return true;
        */
    }
    return super.onOptionsItemSelected(item);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initAll();
    initTribeListView();
  }

  @Override public void onResume() {
    super.onResume();
    Intent intent = getActivity().getIntent();
    if (intent != null) {
      if (!TextUtils.isEmpty(intent.getStringExtra(TribeConstants.TRIBE_OP))) {
        mList = mTribeService.getAllTribes();
        updateTribeList();
      }
    }
  }
}
