package tech.honc.android.apps.soldier.feature.im.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.IYWContactService;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.kit.common.YWAsyncBaseAdapter;
import com.alibaba.mobileim.kit.contact.ContactHeadLoadHelper;
import com.alibaba.mobileim.lib.model.message.YWSystemMessage;
import java.util.List;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.feature.im.helper.LoginHelper;
import tech.honc.android.apps.soldier.feature.im.ui.activity.SystemAddContactActivity;

public class ContactSystemMessageAdapter extends YWAsyncBaseAdapter {

  private Context mContext;
  private LayoutInflater mInflater;
  private List<YWMessage> mMessageList;
  private ContactHeadLoadHelper mContactHeadLoadHelper;
  private String mAppKey;
  public final static String FROM_SYSTEM_AVATAR = "from_system_avatar";

  public ContactSystemMessageAdapter(Context context, List<YWMessage> messages) {
    mContext = context;
    mMessageList = messages;
    mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    mContactHeadLoadHelper = new ContactHeadLoadHelper((Activity) context, null);
    mAppKey = LoginHelper.getInstance().getYWIMKit().getIMCore().getAppKey();
  }

  private class ViewHolder {
    TextView showName;
    TextView message;
    TextView agreeButton;
    TextView result;
    ImageView head;
  }

  public void refreshData(List<YWMessage> list) {
    mMessageList = list;
    notifyDataSetChanged();
  }

  @Override public int getCount() {
    return mMessageList.size();
  }

  @Override public Object getItem(int position) {
    return mMessageList.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  private IYWContactService getContactService() {
    return LoginHelper.getInstance().getYWIMKit().getContactService();
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder = null;
    if (convertView == null) {
      convertView = mInflater.inflate(R.layout.im_item_contact_system_message, parent, false);
      holder = new ViewHolder();
      holder.showName = (TextView) convertView.findViewById(R.id.contact_title);
      holder.head = (ImageView) convertView.findViewById(R.id.head);
      holder.message = (TextView) convertView.findViewById(R.id.invite_message);
      holder.agreeButton = (TextView) convertView.findViewById(R.id.agree);
      holder.result = (TextView) convertView.findViewById(R.id.result);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    if (mMessageList != null) {
      final YWMessage msg = mMessageList.get(position);
      final YWSystemMessage message = (YWSystemMessage) msg;
      final String authorUserId = message.getAuthorUserId();
      IYWContact contactProfileInfo = LoginHelper.getInstance()
          .getYWIMKit()
          .getContactService()
          .getContactProfileInfo(authorUserId, message.getAuthorAppkey());
      String showName = contactProfileInfo.getShowName();
      Log.e("showName__", String.valueOf(TextUtils.isEmpty(showName)) + "showName" + showName);
      //todo:性能待优化--此处比较暴力
      while (showName==null||TextUtils.isEmpty(showName) || showName.equals(authorUserId)) {
        showName = LoginHelper.getInstance()
            .getYWIMKit()
            .getContactService()
            .getContactProfileInfo(authorUserId, message.getAuthorAppkey())
            .getShowName();
      }
      Log.e("__showName__", String.valueOf(TextUtils.isEmpty(showName)) + "showName" + showName);

      holder.showName.setText(showName);
      if (TextUtils.isEmpty(message.getMessageBody().getContent())) {
        holder.message.setText("对方请求添加您为朋友");
      } else {
        holder.message.setText(message.getMessageBody().getContent());
      }
      holder.agreeButton.setText("接受");

      holder.agreeButton.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          ((SystemAddContactActivity) mContext).acceptToBecomeFriend(msg);
        }
      });

      if (message.isAccepted()) {
        holder.agreeButton.setVisibility(View.GONE);
        holder.result.setVisibility(View.VISIBLE);
        holder.result.setText("已添加");
      } else if (message.isIgnored()) {
        holder.agreeButton.setVisibility(View.GONE);
        holder.result.setVisibility(View.VISIBLE);
        holder.result.setText("已忽略");
      } else {
        holder.agreeButton.setVisibility(View.VISIBLE);
        holder.result.setVisibility(View.GONE);
      }
      mContactHeadLoadHelper.setHeadView(holder.head, authorUserId, mAppKey, true);
      holder.head.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          // TODO: 2016-5-23-0023 点击头像的处理事件 
        }
      });
    }
    return convertView;
  }

  @Override public void loadAsyncTask() {

  }
}
