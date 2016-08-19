package tech.honc.android.apps.soldier.ui.viewholder;

import android.annotation.SuppressLint;
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
import com.smartydroid.android.starter.kit.utilities.DateTimeUtils;
import com.smartydroid.android.starter.kit.utilities.ScreenUtils;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.Feed;
import tech.honc.android.apps.soldier.model.enums.GenderType;
import tech.honc.android.apps.soldier.model.enums.TaskType;
import tech.honc.android.apps.soldier.ui.widget.PhotoCollectionView;

/**
 * Created by Administrator on 2016/4/25.
 */
public class TaskFragmentViewHolder extends EasyViewHolder<Feed> {

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

  public TaskFragmentViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_item_task);
    mContext = context;
    ButterKnife.bind(this, itemView);
  }

  @SuppressLint("SetTextI18n") @Override public void bindTo(int position, final Feed value) {

    if (value != null && value.user != null) {
      avatar.setImageURI(
          value.user.uri() != null ? value.user.uri(ScreenUtils.dp2px(60), ScreenUtils.dp2px(60))
              : null);
      name.setText(value.user.nickname);
      sex.setImageResource(GenderType.getIconResource(value.user.gender));
      status.setImageResource(TaskType.getIconResource(value.status != null ? value.status : null));
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
      location.setText(value.address + "  " + value.distance + "米");
      praise_num.setText(value.interestTimes + "");
      msg_num.setText(value.commentTimes + "");
      avatar.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          int id = value.user.id;
          Navigator.startUserDetailActivty((Activity) mContext, id);
        }
      });
    }
  }
}
