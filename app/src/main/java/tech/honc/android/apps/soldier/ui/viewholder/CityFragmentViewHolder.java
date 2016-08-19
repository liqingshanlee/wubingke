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
import tech.honc.android.apps.soldier.ui.widget.PhotoCollectionView;

/**
 * Created by Administrator
 * on 2016/4/19.
 */
public class CityFragmentViewHolder extends EasyViewHolder<Feed> {

  @Bind(R.id.dynamic_img) SimpleDraweeView mAvater;
  @Bind(R.id.dynamic_name) TextView name;
  @Bind(R.id.dynamic_sex) ImageView sex;
  @Bind(R.id.dynamic_content) TextView contents;
  @Bind(R.id.dynamic_picker_gv) PhotoCollectionView gv;
  @Bind(R.id.dynamic_time) TextView time;
  @Bind(R.id.dynamic_praise_num) TextView praise_num;
  @Bind(R.id.dynamic_msg_num) TextView msg_num;

  private Context mContext;

  public CityFragmentViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_item_dynamic);
    mContext = context;
    ButterKnife.bind(this, itemView);
  }

  @SuppressLint("SetTextI18n") @Override public void bindTo(int position, final Feed value) {
    if (value != null) {
      if (value.user != null) {
        mAvater.setImageURI(
            value.user.uri() != null ? value.user.uri(ScreenUtils.dp2px(60), ScreenUtils.dp2px(60))
                : null);
        name.setText(value.user.nickname);
        sex.setImageResource(GenderType.getIconResource(value.user.gender));
      }
      if (value.content != null && !TextUtils.isEmpty(value.content)) {
        contents.setVisibility(View.VISIBLE);
        contents.setText(value.content);
      } else {
        contents.setVisibility(View.GONE);
      }
      if (value.images.size() == 0) {
        gv.setVisibility(View.GONE);
      } else {
        gv.setVisibility(View.VISIBLE);
        gv.setData(value.images);
      }
      time.setText(DateTimeUtils.getRelativeTime(value.createdAt));
      praise_num.setText(value.likeTimes + " ");
      msg_num.setText(value.commentTimes + " ");

      mAvater.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          if (value.user != null && value.user.id != 0) {
            int id = value.user.id;
            Navigator.startUserDetailActivty((Activity) mContext, id);
          }
        }
      });
    }
  }
}
