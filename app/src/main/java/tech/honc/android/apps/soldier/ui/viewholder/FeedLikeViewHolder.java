package tech.honc.android.apps.soldier.ui.viewholder;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.utils.toolsutils.LoginDialog;
import tech.honc.android.apps.soldier.utils.toolsutils.LoginNavigationsUtil;

/**
 * Created by MrJiang on 4/26/2016.
 * feed like
 */
public class FeedLikeViewHolder extends EasyViewHolder<User>
{
  @Bind(R.id.simple_model) SimpleDraweeView mSimpleModel;
  private Context context;

  public FeedLikeViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_item_feed_like);
    ButterKnife.bind(this, itemView);
    this.context = context;
  }

  // TODO: 16-5-23 使用accountId任务中有效，使用id同城中有效 
  @Override public void bindTo(int position, final User value) {
    if (value.uri() != null) {
      mSimpleModel.setImageURI(value.uri());
    }
    mSimpleModel.setOnClickListener(new View.OnClickListener()
    {
      @Override public void onClick(View v) {
        if (value.accountId != null) {
          if (LoginNavigationsUtil.navigationActivity() == LoginNavigationsUtil.TAG_NO_REGISTER) {
            LoginDialog dialog = LoginDialog.getInstance();
            dialog.init((Activity) context);
            dialog.showDialog();
            return;
          }
          Navigator.startUserDetailActivty((Activity) context, value.accountId);
        } else {
          if (LoginNavigationsUtil.navigationActivity() == LoginNavigationsUtil.TAG_NO_REGISTER) {
            LoginDialog dialog = LoginDialog.getInstance();
            dialog.init((Activity) context);
            dialog.showDialog();
            return;
          }
          Navigator.startUserDetailActivty((Activity) context, value.id);
          //SnackBarUtil.showText((Activity) context, "数据获取失败");
        }
      }
    });
  }
}
