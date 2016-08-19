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
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import com.facebook.drawee.view.SimpleDraweeView;
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
import tech.honc.android.apps.soldier.model.Feed;
import tech.honc.android.apps.soldier.model.Status;
import tech.honc.android.apps.soldier.model.TaskComment;
import tech.honc.android.apps.soldier.model.enums.GenderType;
import tech.honc.android.apps.soldier.model.enums.ReportType;
import tech.honc.android.apps.soldier.model.enums.TaskType;
import tech.honc.android.apps.soldier.ui.appInterface.OnItemReturnNullListener;
import tech.honc.android.apps.soldier.ui.viewholder.TaskCommentViewHolder;
import tech.honc.android.apps.soldier.ui.widget.PhotoCollectionView;
import tech.honc.android.apps.soldier.utils.toolsutils.LoginDialog;
import tech.honc.android.apps.soldier.utils.toolsutils.LoginNavigationsUtil;
import tech.honc.android.apps.soldier.utils.toolsutils.SnackBarUtil;

/**
 * Created by Administrator on 2016/4/25.
 * 任务详情
 */
public class TaskDynamicDetailsActivity extends BaseActivity implements OnItemReturnNullListener {

  public int taskId;
  @Bind(R.id.task_details_img) public SimpleDraweeView avatar;
  @Bind(R.id.task_details_name) public TextView name;
  @Bind(R.id.task_details_sex) public ImageView sex;
  @Bind(R.id.task_details_content) public TextView contents;
  @Bind(R.id.task_details_gv) public PhotoCollectionView gv;
  @Bind(R.id.task_details_time) public TextView time;
  @Bind(R.id.task_line_head) public View mHeadLine;
  @Bind(R.id.task_details_commentsNum) public TextView commentsNum;
  @Bind(R.id.task_details_comments) public RecyclerView mCommentsRecyclerView;
  @Bind(R.id.task_input_editText_rl) public RelativeLayout inputEditText;
  @Bind(R.id.task_status) public ImageView status;
  @Bind(R.id.send_mge) public TextView mSendMessage;
  @Bind(R.id.dynamic_task_location) public TextView mLocations;
  @Bind(R.id.comments_content) public EditText mCommentContent;
  @Bind(R.id.task_help_image_view) public ImageView mHelpImageView;

  private FeedService mFeedService;
  private EasyRecyclerAdapter mCommentAdapter;
  private Feed mFeed;
  private String mReportInfo;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.list_item_taskfragment);
    init();
    getFeedDetails();
    getComments();
    setComments();
  }

  private void init() {
    Intent intent = getIntent();
    taskId = intent.getIntExtra("taskid", 0);
    mFeedService = ApiService.createFeedService();
    mHelpImageView.setVisibility(View.GONE);
  }

  /**
   * 点击事件
   */
  @OnClick({ R.id.send_mge, R.id.task_help_image_view }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.task_help_image_view:
        if (LoginNavigationsUtil.navigationActivity() == LoginNavigationsUtil.TAG_NO_REGISTER) {
          LoginDialog dialog = LoginDialog.getInstance();
          dialog.init(this);
          dialog.showDialog();
          return;
        }
        mHelpImageView.setVisibility(View.GONE);
        inputEditText.setVisibility(View.VISIBLE);
        mCommentContent.setHint("请输入帮助内容···");
        showSoftInputMethod();
        mCommentContent.setFocusable(true);
        mCommentContent.requestFocus();
        break;
      case R.id.send_mge:
        toComments();
        break;
    }
  }

  /**
   * 发布评论
   */
  private void toComments() {
    String comment = mCommentContent.getText().toString();
    if (comment.equals("")) {
      hideSoftInputMethod();
      mHelpImageView.setVisibility(View.VISIBLE);
      inputEditText.setVisibility(View.GONE);
      SnackBarUtil.showText(TaskDynamicDetailsActivity.this, "帮助内容不能为空");
      return;
    }
    mSendMessage.setClickable(false);
    Call<Status> toComments = mFeedService.TaskComment(taskId, comment, "help");
    toComments.enqueue(new Callback<Status>() {
      @Override public void onResponse(Call<Status> call, Response<Status> response) {
        if (response.isSuccessful()) {
          getComments();
          getFeedDetails();
          hideSoftInputMethod();
          mHelpImageView.setVisibility(View.VISIBLE);
          inputEditText.setVisibility(View.GONE);
        }
        mSendMessage.setClickable(true);
        mCommentContent.setText("");
      }

      @Override public void onFailure(Call<Status> call, Throwable t) {
        mSendMessage.setClickable(true);
        SnackBarUtil.showText(TaskDynamicDetailsActivity.this, "帮助失败");
      }
    });
  }

  /**
   * 获取动态详情
   */
  private void getFeedDetails() {
    Call<Feed> feedCall = mFeedService.getTaskDetails(taskId);
    feedCall.enqueue(new Callback<Feed>() {
      @Override public void onResponse(Call<Feed> call, Response<Feed> response) {
        if (response.isSuccessful()) {
          mFeed = response.body();
          setHead(mFeed);
        }
      }

      @Override public void onFailure(Call<Feed> call, Throwable t) {

      }
    });
  }

  /**
   * 设置头部基本数据
   */
  @SuppressLint("SetTextI18n") private void setHead(final Feed feed) {
    if (feed.user != null) {
      if (avatar != null) {
        avatar.setImageURI(feed.user.uri() != null ? feed.user.uri() : null);
      }
      if (name != null) {
        name.setText(feed.user.nickname != null ? feed.user.nickname : " ");
      }
      if (sex != null) {
        sex.setImageResource(GenderType.getIconResource(feed.user.gender));
      }
    }
    mHelpImageView.setVisibility(
        TaskType.getIconResource(feed.status) == R.mipmap.ic_ing ? View.VISIBLE : View.INVISIBLE);
    if (feed.address != null
        && feed.distance != null
        && !TextUtils.isEmpty(feed.address)
        && !TextUtils.isEmpty(feed.distance.toString())) {
      mLocations.setVisibility(View.VISIBLE);
      mLocations.setText(feed.address + " " + feed.distance);
    } else if (feed.address != null && !TextUtils.isEmpty(feed.address)) {
      mLocations.setVisibility(View.VISIBLE);
      mLocations.setText(feed.address);
    } else {
      mLocations.setVisibility(View.GONE);
    }
    if (TextUtils.isEmpty(feed.content)) {
      contents.setVisibility(View.GONE);
    } else {
      contents.setVisibility(View.VISIBLE);
      contents.setText(feed.content);
    }
    if (feed.images != null && feed.images.size() == 0) {
      gv.setVisibility(View.GONE);
    } else {
      gv.setVisibility(View.VISIBLE);
      gv.setData(feed.images);
    }
    time.setText(DateTimeUtils.getRelativeTime(feed.createdAt));

    avatar.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (LoginNavigationsUtil.navigationActivity() == LoginNavigationsUtil.TAG_NO_REGISTER) {
          LoginDialog dialog = LoginDialog.getInstance();
          dialog.init(TaskDynamicDetailsActivity.this);
          dialog.showDialog();
          return;
        }
        if (feed.user != null) {
          Navigator.startUserDetailActivty(TaskDynamicDetailsActivity.this, feed.user.id);
        }
      }
    });
    if (feed.commentTimes > 0) {
      commentsNum.setVisibility(View.VISIBLE);
      commentsNum.setText(feed.commentTimes + "人帮助");
    } else {
      commentsNum.setVisibility(View.GONE);
    }
    mHelpImageView.setClickable(true);
    status.setImageResource(TaskType.getIconResource(feed.status));
  }

  /**
   * 获取评论数据，并填充
   */
  private void getComments() {
    Call<ArrayList<TaskComment>> commentCall = mFeedService.getTaskComments(taskId);
    commentCall.enqueue(new Callback<ArrayList<TaskComment>>() {
      @Override public void onResponse(Call<ArrayList<TaskComment>> call,
          Response<ArrayList<TaskComment>> response) {
        if (response.isSuccessful()) {
          ArrayList<TaskComment> arrayList = response.body();
          mCommentAdapter.clear();
          mCommentAdapter.addAll(arrayList);
          mCommentAdapter.notifyDataSetChanged();
        }
      }

      @Override public void onFailure(Call<ArrayList<TaskComment>> call, Throwable t) {

      }
    });
  }

  /**
   * 设置Comment的adapter
   */
  private void setComments() {
    mCommentAdapter = new EasyRecyclerAdapter(TaskDynamicDetailsActivity.this, TaskComment.class,
        TaskCommentViewHolder.class);
    mCommentsRecyclerView.setLayoutManager(
        new LinearLayoutManager(TaskDynamicDetailsActivity.this, LinearLayoutManager.VERTICAL,
            false));
    mCommentsRecyclerView.setAdapter(mCommentAdapter);
  }

  /**
   * 重写返回事件
   */
  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
      if (inputEditText.isShown()) {
        mHelpImageView.setVisibility(View.VISIBLE);
        inputEditText.setVisibility(View.GONE);
        mCommentContent.setText("");
        hideSoftInputMethod();
      } else {
        finish();
      }
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

  @Override public void sendCallback(final Integer id) {
    //打开dialog
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
                mReportService.report(ReportType.TASK_HELP.toString(), id, mReportInfo);
            call.enqueue(new Callback<Status>() {
              @Override public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                  SnackBarUtil.showText(TaskDynamicDetailsActivity.this, response.body().message);
                }
              }

              @Override public void onFailure(Call<Status> call, Throwable t) {
                SnackBarUtil.showText(TaskDynamicDetailsActivity.this, "举报失败");
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
