package tech.honc.android.apps.soldier.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.smartydroid.android.starter.kit.utilities.DateTimeUtils;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import support.ui.adapters.EasyRecyclerAdapter;
import tech.honc.android.apps.soldier.BuildConfig;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.FeedService;
import tech.honc.android.apps.soldier.api.Service.ReportService;
import tech.honc.android.apps.soldier.feature.im.helper.LoginHelper;
import tech.honc.android.apps.soldier.model.Feed;
import tech.honc.android.apps.soldier.model.Status;
import tech.honc.android.apps.soldier.model.TaskComment;
import tech.honc.android.apps.soldier.model.enums.GenderType;
import tech.honc.android.apps.soldier.model.enums.ReportType;
import tech.honc.android.apps.soldier.model.enums.TaskCommentType;
import tech.honc.android.apps.soldier.model.enums.TaskType;
import tech.honc.android.apps.soldier.ui.appInterface.OnItemReturnNullListener;
import tech.honc.android.apps.soldier.ui.viewholder.PersonalTaskViewHolder;
import tech.honc.android.apps.soldier.ui.widget.PhotoCollectionView;
import tech.honc.android.apps.soldier.utils.toolsutils.SnackBarUtil;

/**
 * Created by MrJiang on 2016/5/10.
 * 我的求助详情
 */
public class PersonalTaskDetailActivity extends BaseActivity
    implements PersonalTaskViewHolder.TaskCommentListener, OnItemReturnNullListener {

  @Bind(R.id.personal_task_details_img) SimpleDraweeView mPersonalTaskDetailsImg;
  @Bind(R.id.personal_task_details_name) TextView mPersonalTaskDetailsName;
  @Bind(R.id.personal_task_details_sex) ImageView mPersonalTaskDetailsSex;
  @Bind(R.id.personal_task_status) ImageView mPersonalTaskStatus;
  @Bind(R.id.personal_task_details_content) TextView mPersonalTaskDetailsContent;
  @Bind(R.id.personal_task_details_gv) PhotoCollectionView mPersonalTaskDetailsGv;
  @Bind(R.id.personal_task_details_time) TextView mPersonalTaskDetailsTime;
  @Bind(R.id.personal_task_line_likes) View mPersonalTaskLineLikes;
  @Bind(R.id.personal_task_details_commentsNum) TextView mPersonalTaskDetailsCommentsNum;
  @Bind(R.id.personal_task_details_comments) RecyclerView mPersonalTaskDetailsComments;
  @Bind(R.id.personal_task_address) TextView mPersonalTaskAddress;
  private FeedService mFeedService;
  private int taskId;
  private EasyRecyclerAdapter mCommentAdapter;
  private String mReportInfo;
  private Feed mFeed;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_personal_task_detail);
    ButterKnife.bind(this);
    init();
    getFeedDetail();
    setAdapter();
    getComments();
  }

  private void init() {
    mFeedService = ApiService.createFeedService();
    Intent intent = getIntent();
    taskId = intent.getIntExtra("taskid", 0);
  }

  private void setAdapter() {
    mCommentAdapter = new EasyRecyclerAdapter(PersonalTaskDetailActivity.this);
    mCommentAdapter.bind(TaskComment.class, PersonalTaskViewHolder.class);
    mPersonalTaskDetailsComments.setLayoutManager(
        new LinearLayoutManager(PersonalTaskDetailActivity.this, LinearLayoutManager.VERTICAL,
            false));
    mPersonalTaskDetailsComments.addItemDecoration(
        new HorizontalDividerItemDecoration.Builder(this).size(1).showLastDivider().build());
    mPersonalTaskDetailsComments.setAdapter(mCommentAdapter);
  }

  /**
   * 获取文章详情
   */
  private void getFeedDetail() {
    Call<Feed> feedCall = mFeedService.getTaskDetails(taskId);
    feedCall.enqueue(new Callback<Feed>() {
      @Override public void onResponse(Call<Feed> call, Response<Feed> response) {
        if (response.isSuccessful()) {
          mFeed = response.body();
          setFeedContent(mFeed);
        } else {
          SnackBarUtil.showText(PersonalTaskDetailActivity.this, "获取帮助失败");
        }
      }

      @Override public void onFailure(Call<Feed> call, Throwable t) {
        SnackBarUtil.showText(PersonalTaskDetailActivity.this, "获取帮助失败");
      }
    });
  }

  /**
   * 获取评论
   */
  private void getComments() {
    Call<ArrayList<TaskComment>> commentCall = mFeedService.getTaskComments(taskId);
    commentCall.enqueue(new Callback<ArrayList<TaskComment>>() {
      @Override public void onResponse(Call<ArrayList<TaskComment>> call,
          Response<ArrayList<TaskComment>> response) {
        if (response.isSuccessful()) {
          ArrayList<TaskComment> arrayList = response.body();
          if (arrayList != null) {
            mCommentAdapter.clear();
            mCommentAdapter.appendAll(arrayList);
            mCommentAdapter.notifyDataSetChanged();
          }
        }
      }

      @Override public void onFailure(Call<ArrayList<TaskComment>> call, Throwable t) {

      }
    });
  }

  private void setFeedContent(Feed feed) {
    if (feed != null) {
      mPersonalTaskDetailsImg.setImageURI(feed.user.uri());
      mPersonalTaskDetailsName.setText(feed.user.nickname);
      mPersonalTaskDetailsSex.setImageResource(GenderType.getIconResource(feed.user.gender));
      mPersonalTaskStatus.setImageResource(TaskType.getIconResource(feed.status));
      mPersonalTaskDetailsContent.setText(feed.content);
      mPersonalTaskDetailsGv.setData(feed.images);
      mPersonalTaskDetailsTime.setText(DateTimeUtils.getRelativeTime(feed.createdAt));
      mPersonalTaskAddress.setText(feed.address);
      if (feed.commentTimes != null && feed.commentTimes > 0) {
        mPersonalTaskLineLikes.setVisibility(View.VISIBLE);
        mPersonalTaskDetailsCommentsNum.setText(feed.commentTimes + "人帮助");
      } else {
        mPersonalTaskLineLikes.setVisibility(View.GONE);
        mPersonalTaskDetailsCommentsNum.setText("");
      }
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    ButterKnife.unbind(this);
  }

  @Override public void sendUserId(int id) {
    Navigator.startUserDetailActivty(this, id);
  }

  @Override public void sendHelpId(final TaskComment value) {
    if (TaskType.getIconResource(mFeed.status) == R.mipmap.ic_ing) {
      if (TaskCommentType.getIconResource(value.status).equals("被选中")) {
        //让他妈的去聊天，操
        if (mFeed.user.openImId != null) {
          Intent intent = LoginHelper.getInstance()
              .getYWIMKit()
              .getChattingActivityIntent(mFeed.user.openImId, BuildConfig.IM_APP_KEY);
          intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
          startActivity(intent);
        }
      } else {
        new AlertDialog.Builder(this).setTitle("温馨提示")
            .setMessage("确定设置此帮助为有用?")
            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
              @Override public void onClick(DialogInterface dialog, int which) {

              }
            })
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
              @Override public void onClick(DialogInterface dialog, int which) {
                //设置为有用
                setHelp(value.id);
              }
            })
            .show();
      }
    } else {
      SnackBarUtil.showText(PersonalTaskDetailActivity.this, "该求助已过期");
    }
  }

  public void setHelp(int id) {
    Call<Status> statusCall = mFeedService.setFeedUseful(id);
    statusCall.enqueue(new Callback<Status>() {
      @Override public void onResponse(Call<Status> call, Response<Status> response) {
        if (response.isSuccessful()) {
          getComments();
        } else {
          SnackBarUtil.showText(PersonalTaskDetailActivity.this, "发生错误");
        }
      }

      @Override public void onFailure(Call<Status> call, Throwable t) {
        SnackBarUtil.showText(PersonalTaskDetailActivity.this, "发生错误");
      }
    });
  }

  @Override public void sendCallback(final Integer taskId) {
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
                mReportService.report(ReportType.TASK_HELP.toString(), taskId, mReportInfo);
            call.enqueue(new Callback<Status>() {
              @Override public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                  SnackBarUtil.showText(PersonalTaskDetailActivity.this, response.body().message);
                }
              }

              @Override public void onFailure(Call<Status> call, Throwable t) {
                SnackBarUtil.showText(PersonalTaskDetailActivity.this, "举报失败");
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
