package tech.honc.android.apps.soldier.ui.viewholder;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.smartydroid.android.starter.kit.account.AccountManager;
import com.smartydroid.android.starter.kit.utilities.DateTimeUtils;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.Feed;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.model.enums.GenderType;
import tech.honc.android.apps.soldier.model.enums.TaskType;
import tech.honc.android.apps.soldier.ui.widget.PhotoCollectionView;

/**
 * Created by MrJiang on 2016/4/25.
 * 我的 任务列表，与主页复用一个布局
 */
public class TaskPersonalViewHolder extends EasyViewHolder<Feed> {

  @Bind(R.id.dynamic_task_img) SimpleDraweeView avatar;
  @Bind(R.id.dynamic_task_name) TextView name;
  @Bind(R.id.dynamic_task_sex) ImageView sex;
  @Bind(R.id.dynamic_task_status) ImageView status;
  @Bind(R.id.dynamic_task_content) TextView contents;
  @Bind(R.id.dynamic_task_picker_gv) PhotoCollectionView gv;
  @Bind(R.id.dynamic_task_time) TextView time;
  @Bind(R.id.dynamic_task_location) TextView location;
  @Bind(R.id.dynamic_task_praise_num) TextView praise_num;
  @Bind(R.id.dynamic_task_msg_num) TextView msg_num;

  private Context mContext;

  public TaskPersonalViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_item_task);
    mContext = context;
    ButterKnife.bind(this, itemView);
  }

  @Override public void bindTo(int position, final Feed value) {
    User user = AccountManager.getCurrentAccount();
    if (value != null) {
      if (TextUtils.isEmpty(value.content)) {
        contents.setVisibility(View.GONE);
      } else {
        contents.setVisibility(View.VISIBLE);
        contents.setText(value.content);
      }
      if (value.images.size() == 0) {
        gv.setVisibility(View.GONE);
      } else {
        gv.setVisibility(View.VISIBLE);
        gv.setData(value.images);
      }

      time.setText(DateTimeUtils.getRelativeTime(value.createdAt));
      if (value.address != null && value.distance != null) {
        location.setText(value.address + "  " + value.distance + "米");
      } else if (value.distance == null) {
        location.setText(value.address);
      }
      praise_num.setText(value.interestTimes + "");
      msg_num.setText(value.commentTimes + "");

      avatar.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          int id = value.user.id;
          Navigator.startUserDetailActivty((Activity) mContext, id);
        }
      });
    }
    if (value != null && user != null) {
      avatar.setImageURI(user.uri() != null ? user.uri() : null);
      name.setText(user.nickname);
      sex.setImageResource(GenderType.getIconResource(user.gender));
      status.setImageResource(TaskType.getIconResource(value.status));
    }
  }
}
