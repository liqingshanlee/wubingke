package tech.honc.android.apps.soldier.ui.viewholder;

import android.app.Activity;
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
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.TaskComment;
import tech.honc.android.apps.soldier.ui.appInterface.OnItemReturnNullListener;
import tech.honc.android.apps.soldier.utils.toolsutils.LoginDialog;
import tech.honc.android.apps.soldier.utils.toolsutils.LoginNavigationsUtil;

/**
 * Created by Administrator on 2016/5/3.
 */
public class TaskCommentViewHolder extends EasyViewHolder<TaskComment> {
  @Bind(R.id.dynamic_comment_img) public SimpleDraweeView avatar;
  @Bind(R.id.dynamic_comment_name) public TextView name;
  @Bind(R.id.dynamic_comment_content) public TextView content;
  @Bind(R.id.dynamic_comment_time) public TextView time;
  private TaskComment value;
  private OnItemReturnNullListener mOnItemReturnNullListener;
  public Context context;

  public TaskCommentViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_item_feed_comments);
    ButterKnife.bind(this, itemView);
    this.context = context;
    mOnItemReturnNullListener = (OnItemReturnNullListener) context;
  }

  @Override public void bindTo(int position, final TaskComment value) {
    if (value != null) {
      this.value = value;
      avatar.setImageURI(value.uri());
      name.setText(value.nickname);
      content.setText(value.content);
      time.setText(DateTimeUtils.getRelativeTime(value.createdAt));
    }
  }

  @OnClick({ R.id.dynamic_comment_img, R.id.dynamic_comment_report }) public void onClick(View v) {
    switch (v.getId()) {
      case R.id.dynamic_comment_img:
        if (value.accountId != null) {
          avatar.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
              if (LoginNavigationsUtil.navigationActivity()
                  == LoginNavigationsUtil.TAG_NO_REGISTER) {
                LoginDialog dialog = LoginDialog.getInstance();
                dialog.init((Activity) context);
                dialog.showDialog();
                return;
              }
              Navigator.startUserDetailActivty((Activity) context, value.accountId);
            }
          });
        }
        break;
      case R.id.dynamic_comment_report:
        mOnItemReturnNullListener.sendCallback(value.id);
        break;
    }
  }
}
