package tech.honc.android.apps.soldier.ui.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.facebook.drawee.view.SimpleDraweeView;
import com.smartydroid.android.starter.kit.utilities.DateTimeUtils;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.TaskComment;
import tech.honc.android.apps.soldier.model.enums.TaskCommentType;
import tech.honc.android.apps.soldier.ui.appInterface.OnItemReturnNullListener;

/**
 * Created by MrJiang on 2016/5/10.
 * 我的求助
 */
// TODO: 2016/6/21 在 TaskComment中需要返回当前任务的状态
public class PersonalTaskViewHolder extends EasyViewHolder<TaskComment> {
  @Bind(R.id.holder_personal_task_avatar) SimpleDraweeView mHolderPersonalTaskAvatar;
  @Bind(R.id.holder_personal_task_username) TextView mHolderPersonalTaskUsername;
  @Bind(R.id.holder_personal_task_comment) TextView mHolderPersonalTaskComment;
  @Bind(R.id.holder_personal_task_time) TextView mHolderPersonalTaskTime;
  @Bind(R.id.holder_personal_task_useful) TextView mHolderPersonalTaskUseful;
  private TaskCommentListener mTaskCommentListener;
  private TaskComment mTaskComment;
  private OnItemReturnNullListener mOnItemReturnNullListener;

  public PersonalTaskViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_item_personal_task);
    ButterKnife.bind(this, itemView);
    setTaskCommentListener((TaskCommentListener) context);
    mOnItemReturnNullListener = (OnItemReturnNullListener) context;
  }

  @Override public void bindTo(int position, TaskComment value) {
    if (value != null) {
      mTaskComment = value;
      mHolderPersonalTaskAvatar.setImageURI(value.uri());
      mHolderPersonalTaskUsername.setText(value.nickname);
      mHolderPersonalTaskComment.setText(value.content);
      mHolderPersonalTaskTime.setText(DateTimeUtils.getRelativeTime(value.createdAt));
      if (TaskCommentType.getIconResource(value.status) != null
          && TaskCommentType.getIconResource(value.status).length() != 0) {
        mHolderPersonalTaskUseful.setVisibility(View.VISIBLE);
        mHolderPersonalTaskUseful.setText(TaskCommentType.getIconResource(value.status));
      }else {
        mHolderPersonalTaskUseful.setVisibility(View.GONE);
      }
    }
  }

  @OnClick({
      R.id.holder_personal_task_avatar, R.id.dynamic_comment_report, R.id.personal_help_container
  }) public void onClick(View v) {
    switch (v.getId()) {
      case R.id.personal_help_container:
        mTaskCommentListener.sendHelpId(mTaskComment);
        break;
      case R.id.holder_personal_task_avatar:
        mTaskCommentListener.sendUserId(mTaskComment.accountId);
        break;
      case R.id.dynamic_comment_report:
        mOnItemReturnNullListener.sendCallback(mTaskComment.id);
        break;
    }
  }

  public interface TaskCommentListener {
    void sendUserId(int id);

    void sendHelpId(TaskComment value);
  }

  public void setTaskCommentListener(TaskCommentListener listener) {
    mTaskCommentListener = listener;
  }
}
