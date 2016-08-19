package tech.honc.android.apps.soldier.ui.viewholder;

import android.annotation.SuppressLint;
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
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.Helper;
import tech.honc.android.apps.soldier.model.enums.GenderType;
import tech.honc.android.apps.soldier.model.enums.TaskType;
import tech.honc.android.apps.soldier.ui.widget.PhotoCollectionView;

/**
 * Created by MrJiang on 2016/5/10.
 * 我的求助列表
 */
public class HelperViewHolder extends EasyViewHolder<Helper> {
  @Bind(R.id.dynamic_task_img) SimpleDraweeView mDynamicTaskImg;
  @Bind(R.id.dynamic_task_name) TextView mDynamicTaskName;
  @Bind(R.id.dynamic_task_sex) ImageView mDynamicTaskSex;
  @Bind(R.id.dynamic_task_status) ImageView mDynamicTaskStatus;
  @Bind(R.id.dynamic_task_content) TextView mDynamicTaskContent;
  @Bind(R.id.dynamic_task_picker_gv) PhotoCollectionView mDynamicTaskPickerGv;
  @Bind(R.id.dynamic_task_time) TextView mDynamicTaskTime;
  @Bind(R.id.dynamic_task_location) TextView mDynamicTaskLocation;
  @Bind(R.id.dynamic_task_praise_num) TextView mDynamicTaskPraiseNum;
  @Bind(R.id.dynamic_task_msg_num) TextView mDynamicTaskMsgNum;

  public HelperViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_item_task);
    ButterKnife.bind(this, itemView);
  }

  @SuppressLint("SetTextI18n") @Override public void bindTo(int position, Helper value) {
    if (value != null) {
      mDynamicTaskImg.setImageURI(value.uri());
      mDynamicTaskName.setText(value.nickname);
      mDynamicTaskSex.setImageResource(GenderType.getIconResource(value.gender));
      mDynamicTaskStatus.setImageResource(TaskType.getIconResource(value.status));
      if (TextUtils.isEmpty(value.content)) {
        mDynamicTaskContent.setVisibility(View.GONE);
      } else {
        mDynamicTaskContent.setVisibility(View.VISIBLE);
        mDynamicTaskContent.setText(value.content);
      }
      if (value.images.size() == 0) {
        mDynamicTaskPickerGv.setVisibility(View.GONE);
      } else {
        mDynamicTaskPickerGv.setVisibility(View.VISIBLE);
        mDynamicTaskPickerGv.setData(value.images);
      }
      mDynamicTaskTime.setText(DateTimeUtils.getRelativeTime(value.createAt));
      if (value.distance!=null) {
        mDynamicTaskLocation.setText(value.address + "  " + value.distance + "米");
      }else {
        mDynamicTaskLocation.setText(value.address);
      }
      mDynamicTaskPraiseNum.setText(value.commentTimes + "");
      mDynamicTaskMsgNum.setText(value.interestTimes + "");
    }
  }
}
