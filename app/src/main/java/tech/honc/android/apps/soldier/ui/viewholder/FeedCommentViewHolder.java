package tech.honc.android.apps.soldier.ui.viewholder;

import android.app.Activity;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.facebook.drawee.view.SimpleDraweeView;
import com.smartydroid.android.starter.kit.utilities.DateTimeUtils;
import support.ui.adapters.EasyViewHolder;
import support.ui.app.SupportApp;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.Comments;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.ui.appInterface.OnItemReturnNullListener;
import tech.honc.android.apps.soldier.utils.toolsutils.LoginDialog;
import tech.honc.android.apps.soldier.utils.toolsutils.LoginNavigationsUtil;

/**
 * Created by MrJiang on 4/26/2016.
 * feed comment
 */
public class FeedCommentViewHolder extends EasyViewHolder<Comments>
{

  @Bind(R.id.dynamic_comment_img) public SimpleDraweeView avatar;
  @Bind(R.id.dynamic_comment_name) public TextView name;
  @Bind(R.id.dynamic_comment_content) public TextView content;
  @Bind(R.id.dynamic_comment_time) public TextView time;
  public SpannableString ss;
  public Context context;
  public OnFeedCommentListener onFeedCommentListener;
  private OnItemReturnNullListener mOnItemReturnNullListener;
  private Comments mComments;

  public FeedCommentViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_item_feed_comments);
    ButterKnife.bind(this, itemView);
    this.context = context;
    mOnItemReturnNullListener = (OnItemReturnNullListener) context;
    setOnFeedViewClickListener((OnFeedCommentListener) context);
  }

  public void setOnFeedViewClickListener(OnFeedCommentListener onFeedCommentListener) {
    this.onFeedCommentListener = onFeedCommentListener;
  }

  @Override public void bindTo(int position, final Comments value) {
    if (value != null) {
      mComments = value;
      if (value.parentId == 0) {
        if (value.Auser != null && value.Auser.thumbUri() != null) {
          avatar.setImageURI(value.Auser.thumbUri());
        }
        if (value.Auser != null) {
          ss = new SpannableString(value.Auser.nickname);
          ss.setSpan(new TextSpanClick(true, value.Auser), 0, value.Auser.nickname.length(),
              Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
          ss.setSpan(new ForegroundColorSpan(SupportApp.color(R.color.colorPrimary)), 0,
              value.Auser.nickname.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
          name.setText(ss);
        }
      } else {
        if (value.Puser != null && value.Puser.thumbUri() != null) {
          avatar.setImageURI(value.Puser.thumbUri());
        }
        if (value.Puser != null && value.Puser.nickname != null) {
          ss = new SpannableString(value.Auser.nickname + " 回复: " + value.Puser.nickname);
          ss.setSpan(new TextSpanClick(true, value.Auser), 0, value.Auser.nickname.length(),
              Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

          ss.setSpan(new TextSpanClick(false, value.Puser), value.Auser.nickname.length() + 5,
              value.Auser.nickname.length() + 5 + value.Puser.nickname.length(),
              Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

          ss.setSpan(new ForegroundColorSpan(SupportApp.color(R.color.colorPrimary)), 0,
              value.Auser.nickname.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

          ss.setSpan(new ForegroundColorSpan(SupportApp.color(R.color.colorPrimary)),
              value.Auser.nickname.length() + 5,
              value.Auser.nickname.length() + 5 + value.Puser.nickname.length(),
              Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

          name.setText(ss);
        }
      }
      name.setMovementMethod(LinkMovementMethod.getInstance());
      content.setText(value.content);
      time.setText(DateTimeUtils.getRelativeTime(value.createdAt));

      avatar.setOnClickListener(new View.OnClickListener()
      {
        @Override public void onClick(View v) {
          if (value.parentId == 0) {
            if (value.Auser.id != null) {
              if (LoginNavigationsUtil.navigationActivity()
                  == LoginNavigationsUtil.TAG_NO_REGISTER) {
                LoginDialog dialog = LoginDialog.getInstance();
                dialog.init((Activity) context);
                dialog.showDialog();
                return;
              }
              Navigator.startUserDetailActivty((Activity) context, value.Auser.id);
            }
          } else {
            if (value.Puser.id != null) {
              Navigator.startUserDetailActivty((Activity) context, value.Puser.id);
            }
          }
        }
      });
    }
  }

  public interface OnFeedCommentListener
  {
    void onNameClick(User user);
  }

  class TextSpanClick extends ClickableSpan
  {

    //private boolean status;
    private User user;

    public TextSpanClick(boolean status, User user) {
      //this.status = status;
      this.user = user;
    }

    @Override public void updateDrawState(TextPaint ds) {
      super.updateDrawState(ds);
      ds.setUnderlineText(false);// 取消下划线
    }

    @Override public void onClick(View widget) {
      //if (status) {
      onFeedCommentListener.onNameClick(user);
      //} else {
      //
      //}
    }
  }

  @OnClick({R.id.dynamic_comment_report})
  public void onClick(View v){
    switch (v.getId()){
      case R.id.dynamic_comment_report:
        mOnItemReturnNullListener.sendCallback(mComments.id);
        break;
    }
  }
}
