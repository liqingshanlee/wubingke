package tech.honc.android.apps.soldier.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.ArrayList;
import retrofit2.Call;
import support.ui.adapters.EasyRecyclerAdapter;
import support.ui.app.SupportApp;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.FeedService;
import tech.honc.android.apps.soldier.model.Feed;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.model.enums.GenderType;
import tech.honc.android.apps.soldier.ui.viewholder.UserFeedViewHolder;
import tech.honc.android.apps.soldier.utils.toolsutils.LevelUtils;

/**
 * Created by MrJiang on 2016/4/28.
 * 他的详情
 */
public class UserFeedFragment extends BaseKeysFragment<Feed>
    implements AppBarLayout.OnOffsetChangedListener {

  @Bind(R.id.toolbar_detail) Toolbar mToolbar;
  @Bind(R.id.detail_simple) SimpleDraweeView mHeadImageDraw;
  @Bind(R.id.username) TextView mUsername;
  @Bind(R.id.age) TextView mAgeText;
  @Bind(R.id.area) TextView mAreaText;
  @Bind(R.id.signature) TextView mSignatureText;
  @Bind(R.id.level) ImageView mImageView;
  @Bind(R.id.collapsing_toolbar_layout) CollapsingToolbarLayout mCollapsingToolbarLayout;
  @Bind(R.id.appbar_layout) AppBarLayout mAppbarLayout;
  private User mUser;
  private FeedService mFeedService;
  private boolean isShow = false;
  private int scrollRange = -1;

  public static UserFeedFragment create() {
    return new UserFeedFragment();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mFeedService = ApiService.createFeedService();
    mUser = getArguments().getParcelable("user");
    if (mUser == null) {
      mUser = new User();
    }
  }

  @Override public void bindViewHolders(EasyRecyclerAdapter adapter) {
    adapter.bind(Feed.class, UserFeedViewHolder.class);
  }

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_feed_user;
  }

  @Override public Call<ArrayList<Feed>> paginate(Feed sinceItem, Feed maxItem, int perPage) {
    return mFeedService.getUserFeeds(mUser.id != null ? mUser.id : 0,
        maxItem != null && maxItem.id != null ? maxItem.id : 0);
  }

  @Override public Object getKeyForData(Feed item) {
    return item.id;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = super.onCreateView(inflater, container, savedInstanceState);
    ButterKnife.bind(this, rootView);
    setUpToolBar();
    mAppbarLayout.addOnOffsetChangedListener(this);
    initView();
    return rootView;
  }

  /**
   * 初始化头部信息
   */
  @SuppressLint("SetTextI18n") private void initView() {
    mHeadImageDraw.setImageURI(mUser.uri());
    mUsername.setText(mUser.nickname);
    if (mUser.accountDetail != null&&mUser.accountDetail.age!=null&& !TextUtils.isEmpty(mUser.accountDetail.age))
    mAgeText.setText(mUser.accountDetail.age + "岁");
    mAreaText.setText(mUser.city != null ? mUser.city : "  ");
    mSignatureText.setText(mUser.signature != null ? mUser.signature : "  ");
    if (GenderType.fromValue(mUser.gender.name()) != null && GenderType.FEMALE.toString()
        .equals(mUser.gender.name())) {
      mImageView.setImageResource(LevelUtils.getWoManMipmapFromLevel(mUser.level));
    } else {
      mImageView.setImageResource(LevelUtils.getManMipmapFromLevel(mUser.level));
    }
  }

  @SuppressLint("PrivateResource") private void setUpToolBar() {
    setHasOptionsMenu(true);
    mCollapsingToolbarLayout.setTitle("");
    mToolbar.setNavigationIcon(SupportApp.drawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
    mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
    mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        getActivity().finish();
      }
    });
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

  @Override public void onItemClick(int position, View view) {
    super.onItemClick(position, view);
    Feed feed = (Feed) getAdapter().get(position);
    Navigator.startDynamicDetailsActivity(getActivity(), feed.id);
  }
}
