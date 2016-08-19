package tech.honc.android.apps.soldier.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.smartydroid.android.starter.kit.account.AccountManager;
import java.util.ArrayList;
import java.util.List;
import mediapicker.MediaItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import support.ui.adapters.EasyRecyclerAdapter;
import support.ui.app.SupportApp;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.FeedService;
import tech.honc.android.apps.soldier.model.Feed;
import tech.honc.android.apps.soldier.model.Image;
import tech.honc.android.apps.soldier.model.Status;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.model.enums.GenderType;
import tech.honc.android.apps.soldier.oss.OSSUtils;
import tech.honc.android.apps.soldier.ui.viewholder.UserFeedViewHolder;
import tech.honc.android.apps.soldier.utils.toolsutils.DialogUtil;
import tech.honc.android.apps.soldier.utils.toolsutils.LevelUtils;
import tech.honc.android.apps.soldier.utils.toolsutils.SnackBarUtil;

/**
 * Created by MrJiang on 2016/4/28.
 */
public class PersonalFeedFragment extends BaseNotDividerKeysFragment<Feed>
    implements AppBarLayout.OnOffsetChangedListener, MaterialDialog.ListCallback {

  @Bind(R.id.toolbar_detail) Toolbar mToolbarDetail;
  @Bind(R.id.detail_simple) SimpleDraweeView mHeadImageDraw;
  @Bind(R.id.username) TextView mUsername;
  @Bind(R.id.age) TextView mAgeText;
  @Bind(R.id.area) TextView mAreaText;
  @Bind(R.id.level) ImageView mImageView;
  @Bind(R.id.signature) TextView mSignatureText;
  @Bind(R.id.collapsing_toolbar_layout) CollapsingToolbarLayout mCollapsingToolbarLayout;
  @Bind(R.id.appbar_layout) AppBarLayout mAppbarLayout;

  private static final int REQUEST_MEDIA = 100;
  private User mUser = AccountManager.getCurrentAccount();
  private FeedService mFeedService;
  private boolean isShow = false;
  private int scrollRange = -1;
  private List<MediaItem> mMediaSelectedList;
  private String key;
  private OSSUtils mOSSUtils;
  private int mPositions;

  public static PersonalFeedFragment create() {
    return new PersonalFeedFragment();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mOSSUtils = new OSSUtils();
    mFeedService = ApiService.createFeedService();
    mMediaSelectedList = new ArrayList<>();
    mUser = getArguments().getParcelable("user");
    if (mUser == null) {
      mUser = new User();
    }
  }

  @Override public Call<ArrayList<Feed>> paginate(Feed sinceItem, Feed maxItem, int perPage) {
    return mFeedService.getUserFeeds(mUser.id != null ? mUser.id : 0,
        maxItem != null && maxItem.id != null ? maxItem.id : 0);
  }

  @Override public Object getKeyForData(Feed item) {
    return item.id;
  }

  @Override public void bindViewHolders(EasyRecyclerAdapter adapter) {
    adapter.bind(Feed.class, UserFeedViewHolder.class);
    adapter.setOnClickListener(this);
  }

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_my_feed;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = super.onCreateView(inflater, container, savedInstanceState);
    ButterKnife.bind(this, rootView);
    mAppbarLayout.addOnOffsetChangedListener(this);
    setUpToolBar();
    initView();
    return rootView;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
    if (scrollRange == -1) {
      scrollRange = appBarLayout.getTotalScrollRange();
    }
    if (scrollRange + verticalOffset == 0) {
      mCollapsingToolbarLayout.setTitle(mUser.nickname != null ? mUser.nickname : "动态");
      isShow = true;
    } else if (isShow) {
      mCollapsingToolbarLayout.setTitle("");
      isShow = false;
    }
  }

  /**
   * 初始化头部信息
   */
  private void initView() {
    mHeadImageDraw.setImageURI(mUser.uri());
    mUsername.setText(mUser.nickname);
    mAgeText.setText(mUser.accountDetail != null ? mUser.accountDetail.age + "岁" : "");
    mAreaText.setText(mUser.city != null ? mUser.city : "");
    mSignatureText.setText(mUser.signature != null ? mUser.signature : "");
    if (GenderType.FEMALE.toString().equals(mUser.gender.toString())) {
      mImageView.setImageResource(LevelUtils.getWoManMipmapFromLevel(mUser.level));
    } else {
      mImageView.setImageResource(LevelUtils.getManMipmapFromLevel(mUser.level));
    }
  }

  private void setUpToolBar() {
    setHasOptionsMenu(true);
    mCollapsingToolbarLayout.setTitle("");
    mToolbarDetail.setNavigationIcon(SupportApp.drawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
    mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
    mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
    mToolbarDetail.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getActivity().finish();
      }
    });
  }

  @OnClick({ R.id.float_button, R.id.detail_simple }) public void onClick(View v) {
    switch (v.getId()) {
      case R.id.float_button:
        Navigator.startReleaseOrdinaryActivity(getActivity());
        break;
      case R.id.detail_simple:
        ArrayList<Image> images = new ArrayList<>();
        Image image = new Image();
        image.url = mUser.avatar;
        images.add(image);
        Navigator.startPhotoBDActivity(getActivity(),images,0);
        break;
    }
  }

  @Override public void onItemClick(int position, View view) {
    super.onItemClick(position, view);
    Feed feed = (Feed) getAdapter().get(position);
    Navigator.startDynamicDetailsActivity(getActivity(), feed.id);
  }

  @Override public boolean onLongItemClicked(int position, View view) {
    mPositions = position;
    DialogUtil.relationDeleteDialogImage(getActivity(), this);
    return true;
  }

  @Override
  public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
    switch (which) {
      case 0:
        deleteFeed();
        break;
      case 1:
        break;
    }
  }

  public void deleteFeed() {
    showHud("正在删除");
    Feed feed = (Feed) getAdapter().get(mPositions);
    Call<Status> feedStatusCall = mFeedService.deleteDynamic(feed.id);
    feedStatusCall.enqueue(new Callback<Status>() {
      @Override public void onResponse(Call<Status> call, Response<Status> response) {
        dismissHud();
        if (response.isSuccessful()) {
          refresh();
          SnackBarUtil.showText(getActivity(), "删除成功");
        } else {
          SnackBarUtil.showText(getActivity(), "删除失败" + response.body());
        }
      }

      @Override public void onFailure(Call<Status> call, Throwable t) {
        dismissHud();
        SnackBarUtil.showText(getActivity(), "删除失败" + t.toString());
      }
    });
  }

  @Override public void onResume() {
    super.onResume();
    refresh();
  }
}
