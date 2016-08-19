package tech.honc.android.apps.soldier.ui.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import com.facebook.drawee.view.SimpleDraweeView;
import com.smartydroid.android.starter.kit.account.AccountManager;
import com.smartydroid.android.starter.kit.utilities.DateTimeUtils;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import support.ui.adapters.EasyRecyclerAdapter;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.FeedService;
import tech.honc.android.apps.soldier.api.Service.ReportService;
import tech.honc.android.apps.soldier.model.Comments;
import tech.honc.android.apps.soldier.model.Feed;
import tech.honc.android.apps.soldier.model.Status;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.model.enums.GenderType;
import tech.honc.android.apps.soldier.model.enums.ReportType;
import tech.honc.android.apps.soldier.ui.appInterface.OnItemReturnNullListener;
import tech.honc.android.apps.soldier.ui.viewholder.FeedCommentViewHolder;
import tech.honc.android.apps.soldier.ui.viewholder.FeedLikeViewHolder;
import tech.honc.android.apps.soldier.ui.widget.PhotoCollectionView;
import tech.honc.android.apps.soldier.utils.toolsutils.LoginDialog;
import tech.honc.android.apps.soldier.utils.toolsutils.LoginNavigationsUtil;
import tech.honc.android.apps.soldier.utils.toolsutils.SnackBarUtil;

/**
 * creat by lijun 2016/4/21
 * 动态详情（同城/好友）
 */
public class DynamicDetailsActivity extends BaseActivity
    implements FeedCommentViewHolder.OnFeedCommentListener, OnItemReturnNullListener {

  public int feedId;
  @Bind(R.id.dynamic_details_img) public SimpleDraweeView avatar;
  @Bind(R.id.dynamic_details_name) public TextView name;
  @Bind(R.id.dynamic_details_sex) public ImageView sex;
  @Bind(R.id.dynamic_details_content) public TextView contents;
  @Bind(R.id.dynamic_details_gv) public PhotoCollectionView gv;
  @Bind(R.id.dynamic_details_time) public TextView time;
  @Bind(R.id.dynamic_details_praiseNum) public TextView praiseNum;
  @Bind(R.id.dynamic_details_likes) public RecyclerView mLikesRecyclerView;
  @Bind(R.id.line_likes) public View line;
  @Bind(R.id.line_head) public View mHeadLine;
  @Bind(R.id.dynamic_details_commentsNum) public TextView commentsNum;
  @Bind(R.id.dynamic_details_comments) public RecyclerView mCommentsRecyclerView;
  @Bind(R.id.dynamic_details_praise_tv) public TextView mPraiseStatus;
  @Bind(R.id.bottom_bar) public LinearLayout bottomBar;
  @Bind(R.id.input_editText_rl) public RelativeLayout inputEditText;
  @Bind(R.id.comments_content) public EditText comments_content;

  private FeedService mFeedService;
  private EasyRecyclerAdapter mLikesAdapter;
  private EasyRecyclerAdapter mCommentAdapter;
  private Boolean isLiked = false;
  private int num = 0;
  private int commentNmu = 0;
  private int userId;
  private String mReportInfo;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.list_item_feed_head);
    Intent intent = getIntent();
    feedId = intent.getIntExtra("feedid", 0);
    mFeedService = ApiService.createFeedService();
    getFeedDetails();
    getLikes();
    setLikes();
    getComments();
    setComments();
  }

  @OnClick({ R.id.dynamic_details_praise_bt, R.id.dynamic_details_comments_bt, R.id.send_mge })
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.dynamic_details_praise_bt:
        if (LoginNavigationsUtil.navigationActivity() == LoginNavigationsUtil.TAG_NO_REGISTER) {
          LoginDialog dialog = LoginDialog.getInstance();
          dialog.init(this);
          dialog.showDialog();
          return;
        }
        if (isLiked) {
          cancelPraise();
        } else {
          toPraise();
        }
        break;
      case R.id.dynamic_details_comments_bt:
        if (LoginNavigationsUtil.navigationActivity() == LoginNavigationsUtil.TAG_NO_REGISTER) {
          LoginDialog dialog = LoginDialog.getInstance();
          dialog.init(this);
          dialog.showDialog();
          return;
        }
        bottomBar.setVisibility(View.GONE);
        inputEditText.setVisibility(View.VISIBLE);
        comments_content.setHint("输入评论内容···");
        showSoftInputMethod();
        comments_content.requestFocus();
        this.userId = 0;
        break;
      case R.id.send_mge:
        if (!AccountManager.isLogin()) {
          LoginDialog dialog = LoginDialog.getInstance();
          dialog.init(this);
          dialog.showDialog();
          return;
        }
        toComments(userId);
        break;
    }
  }

  @Override public void onNameClick(User user) {
    bottomBar.setVisibility(View.GONE);
    inputEditText.setVisibility(View.VISIBLE);
    comments_content.setHint("回复:" + user.nickname);
    this.userId = user.id;
  }

  private void cancelPraise() {
    Call<Status> cancelPraise = mFeedService.cancelPraise(feedId);
    cancelPraise.enqueue(new Callback<Status>() {
      @Override public void onResponse(Call<Status> call, Response<Status> response) {
        if (response.isSuccessful()) {
          getLikes();
          isLiked = false;
          mPraiseStatus.setText("赞");
          num = num - 1;
          if (num > 0) {
            praiseNum.setText((num) + "人赞");
          } else {
            praiseNum.setVisibility(View.GONE);
            mHeadLine.setVisibility(View.GONE);
          }
        }
      }

      @Override public void onFailure(Call<Status> call, Throwable t) {

      }
    });
  }

  private void toPraise() {
    Call<Status> toPraise = mFeedService.toPraise(feedId);
    toPraise.enqueue(new Callback<Status>() {
      @Override public void onResponse(Call<Status> call, Response<Status> response) {
        if (response.isSuccessful()) {
          getLikes();
          isLiked = true;
          mPraiseStatus.setText("已赞");
          mHeadLine.setVisibility(View.VISIBLE);
          praiseNum.setVisibility(View.VISIBLE);
          praiseNum.setText((num + 1) + "人赞");
          num = num + 1;
        }
      }

      @Override public void onFailure(Call<Status> call, Throwable t) {

      }
    });
  }

  private void toComments(int userId) {
    User user = (User) (AccountManager.getCurrentAccount());
    String comment = comments_content.getText().toString();
    if (comment.equals("")) {
      hideSoftInputMethod();
      bottomBar.setVisibility(View.VISIBLE);
      inputEditText.setVisibility(View.GONE);
      SnackBarUtil.showText(DynamicDetailsActivity.this, "内容不能为空");
      return;
    }
    if (userId == user.id) {
      hideSoftInputMethod();
      bottomBar.setVisibility(View.VISIBLE);
      inputEditText.setVisibility(View.GONE);
      SnackBarUtil.showText(DynamicDetailsActivity.this, "自己不能回复自己");
      return;
    }
    Call<Status> toComments = mFeedService.comment(feedId, comment, userId);
    toComments.enqueue(new Callback<Status>() {
      @SuppressLint("SetTextI18n") @Override
      public void onResponse(Call<Status> call, Response<Status> response) {
        if (response.isSuccessful()) {
          getComments();
          hideSoftInputMethod();
          commentsNum.setText((commentNmu + 1) + "人评论");
          commentNmu = commentNmu + 1;
          comments_content.setText("");
          bottomBar.setVisibility(View.VISIBLE);
          line.setVisibility(View.VISIBLE);
          inputEditText.setVisibility(View.GONE);
        }
      }

      @Override public void onFailure(Call<Status> call, Throwable t) {

      }
    });
  }

  private void getFeedDetails() {
    Call<Feed> feedCall = mFeedService.getDynamicDetails(feedId);
    feedCall.enqueue(new Callback<Feed>() {
      @Override public void onResponse(Call<Feed> call, Response<Feed> response) {
        if (response.isSuccessful()) {
          Feed feed = response.body();
          setHead(feed);
        }
      }

      @Override public void onFailure(Call<Feed> call, Throwable t) {

      }
    });
  }

  /**
   * 设置除了两个RecyclerView的UI以及内容
   */
  @SuppressLint("SetTextI18n") private void setHead(final Feed feed) {
    if (feed != null) {
      if (feed.user != null) {
        if (feed.user.uri() != null && avatar != null) {
          avatar.setImageURI(feed.user.uri());
        }
        name.setText(feed.user.nickname);
        sex.setImageResource(GenderType.getIconResource(feed.user.gender));
      }
      if (TextUtils.isEmpty(feed.content)) {
        contents.setVisibility(View.GONE);
      } else {
        contents.setVisibility(View.VISIBLE);
        contents.setText(feed.content);
      }
      if (feed.images.size() == 0) {
        gv.setVisibility(View.GONE);
      } else {
        gv.setVisibility(View.VISIBLE);
        gv.setData(feed.images);
      }
      time.setText(DateTimeUtils.getRelativeTime(feed.createdAt));
      if (feed.likeTimes > 0) {
        praiseNum.setText(feed.likeTimes + "人赞");
        num = feed.likeTimes;
      } else {
        praiseNum.setVisibility(View.GONE);
        mHeadLine.setVisibility(View.GONE);
      }
      avatar.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          if (LoginNavigationsUtil.navigationActivity() == LoginNavigationsUtil.TAG_NO_REGISTER) {
            LoginDialog dialog = LoginDialog.getInstance();
            dialog.init(DynamicDetailsActivity.this);
            dialog.showDialog();
            return;
          }
          Navigator.startUserDetailActivty(DynamicDetailsActivity.this, feed.user.id);
        }
      });

      if (feed.commentTimes > 0) {
        commentsNum.setVisibility(View.VISIBLE);
        commentsNum.setText(feed.commentTimes + "人评论");
        commentNmu = feed.commentTimes;
        line.setVisibility(View.VISIBLE);
      } else {
        commentsNum.setVisibility(View.GONE);
        line.setVisibility(View.INVISIBLE);
      }
      if (feed.isLiked == 0) {
        mPraiseStatus.setText("赞");
        isLiked = false;
      } else {
        mPraiseStatus.setText("已赞");
        isLiked = true;
      }
    }
  }

  /**
   * 获取喜欢adapter
   */
  private void getLikes() {
    Call<ArrayList<User>> likesCall = mFeedService.getLikes(feedId);
    likesCall.enqueue(new Callback<ArrayList<User>>() {
      @Override
      public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
        if (response.isSuccessful()) {
          ArrayList<User> arrayList = response.body();
          mLikesAdapter.addAll(arrayList);
          mLikesAdapter.notifyDataSetChanged();
        }
      }

      @Override public void onFailure(Call<ArrayList<User>> call, Throwable t) {

      }
    });
  }

  /**
   * 初始化和设置like adapter
   */
  private void setLikes() {
    mLikesAdapter =
        new EasyRecyclerAdapter(DynamicDetailsActivity.this, User.class, FeedLikeViewHolder.class);
    mLikesRecyclerView.setLayoutManager(
        new LinearLayoutManager(DynamicDetailsActivity.this, LinearLayoutManager.HORIZONTAL,
            false));
    mLikesRecyclerView.setAdapter(mLikesAdapter);
  }

  /**
   * 获取评论
   */
  private void getComments() {
    Call<ArrayList<Comments>> commentCall = mFeedService.getComments(feedId);
    commentCall.enqueue(new Callback<ArrayList<Comments>>() {
      @SuppressLint("SetTextI18n") @Override public void onResponse(Call<ArrayList<Comments>> call,
          Response<ArrayList<Comments>> response) {
        if (response.isSuccessful()) {
          ArrayList<Comments> arrayList = response.body();
          if (arrayList.size() > 0) {
            commentsNum.setVisibility(View.VISIBLE);
            commentsNum.setText(arrayList.size() + "人评论");
            commentNmu = arrayList.size();
            line.setVisibility(View.VISIBLE);
          }
          mCommentAdapter.addAll(arrayList);
          mCommentAdapter.notifyDataSetChanged();
        }
      }

      @Override public void onFailure(Call<ArrayList<Comments>> call, Throwable t) {

      }
    });
  }

  /**
   * 初始化adapter和设定
   */
  private void setComments() {
    mCommentAdapter = new EasyRecyclerAdapter(DynamicDetailsActivity.this, Comments.class,
        FeedCommentViewHolder.class);
    mCommentsRecyclerView.setLayoutManager(
        new LinearLayoutManager(DynamicDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
    mCommentsRecyclerView.setAdapter(mCommentAdapter);
  }

  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
      if (inputEditText.isShown()) {
        bottomBar.setVisibility(View.VISIBLE);
        inputEditText.setVisibility(View.GONE);
        comments_content.setText("");
        hideSoftInputMethod();
      } else {
        finish();
      }
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

  @Override public void sendCallback(final Integer feedId) {
    final String[] list = getResources().getStringArray(R.array.report);
    mReportInfo = list[0];
    new AlertDialog.Builder(this).setTitle("举报信息")
        .setSingleChoiceItems(R.array.report, 0, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            mReportInfo = list[which];
          }
        })
        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

          @Override public void onClick(DialogInterface dialog, int which) {
            //举报该评论
            ReportService mReportService = ApiService.createReportService();
            Call<Status> call =
                mReportService.report(ReportType.TASK_HELP.toString(), feedId, mReportInfo);
            call.enqueue(new Callback<Status>() {
              @Override public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                  SnackBarUtil.showText(DynamicDetailsActivity.this, response.body().message);
                }
              }

              @Override public void onFailure(Call<Status> call, Throwable t) {
                SnackBarUtil.showText(DynamicDetailsActivity.this, "举报失败");
              }
            });
          }
        })
        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {

          }
        })
        .show();
  }
}
